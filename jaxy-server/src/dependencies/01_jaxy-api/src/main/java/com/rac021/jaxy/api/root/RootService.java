
package com.rac021.jaxy.api.root ;

import javax.ws.rs.GET ;
import javax.ws.rs.Path ;
import java.time.Instant ;
import javax.inject.Inject ;
import javax.ws.rs.PathParam ;
import java.util.logging.Level ;
import javax.ws.rs.HeaderParam ;
import java.time.LocalDateTime ;
import java.util.logging.Logger ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import javax.ws.rs.core.MediaType ;
import javax.enterprise.inject.Any ;
import java.time.temporal.ChronoUnit ;
import javax.annotation.PostConstruct ;
import javax.enterprise.inject.Instance ;
import java.time.format.DateTimeFormatter ;
import com.rac021.jaxy.api.security.ISignOn ;
import javax.servlet.http.HttpServletRequest ;
import com.rac021.jaxy.api.crypto.CipherTypes ;
import com.rac021.jaxy.api.crypto.JceSecurity ;
import javax.enterprise.util.AnnotationLiteral ;
import javax.enterprise.context.ApplicationScoped ;
import com.rac021.jaxy.api.qualifiers.security.Custom ;
import com.rac021.jaxy.api.qualifiers.security.Policy ;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import org.eclipse.microprofile.faulttolerance.Bulkhead ;
import org.eclipse.microprofile.faulttolerance.Fallback ;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker ;
import com.rac021.jaxy.api.exceptions.FallbackHandlerException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import com.rac021.jaxy.api.exceptions.UnAuthorizedResourceException ;
import org.eclipse.microprofile.faulttolerance.exceptions.FaultToleranceException ;
import static com.rac021.jaxy.api.root.ConcurrentUsersManager.tryingAcquireSemaphore ;
import static com.rac021.jaxy.api.root.ConcurrentUsersManager.initSemaphoreConcurrentUsers ;

/**
 * REST Web Service
 *
 * @author yahiaoui
 */

@Path(RootService.PATH_RESOURCE)
@ApplicationScoped

public class RootService implements IRootService     {

    private static final Logger LOGGER = getLogger() ;
   
    public static final String LOGIN         = "{login}"          ;

    public static final String SIGNATURE     = "{signature}"      ;

    public static final String TIMESTAMP     = "{timeStamp}"      ;

    public static final String SERVICENAME   = "{_service_Name_}" ;

    public static final String SERVICENAME_P = "_service_Name_"   ;

    public static final String PATH_RESOURCE = "/resources"       ;

    public static final String SEPARATOR     = "/"                ;
    
    @Inject 
    ServicesManager servicesManager ;

    @Inject @Any
    private Instance<ISignOn> signOn ;

    public RootService() {
    }

    @PostConstruct
    public void init()       {
       JceSecurity.unlimit() ;
       initSemaphoreConcurrentUsers() ;
    }

    @Override
    @Path( SERVICENAME   )
    @Bulkhead(value = Integer.MAX_VALUE )
    /** NB : Bulkhead can be configured in the implementations using properties 
    System.setProperty (
    "com.rac021.jax.api.root.RootService/subResourceLocators/Bulkhead/value" ,
    String.valueOf(MaxConcurrentUsers )) ;   **/
    @CircuitBreaker( delay = 30                             , 
                     delayUnit = ChronoUnit.SECONDS         ,
                     failOn = FaultToleranceException.class ,
                     successThreshold       = 10            , 
                     requestVolumeThreshold = 4 )
    @Fallback( FallbackHandlerException.class )
    public Object subResourceLocators( @HeaderParam("API-key-Token") String             token     ,
                                       @HeaderParam("Accept")        String             accept    ,
                                       @HeaderParam("Cipher")        String             cipher    ,
                                       @HeaderParam("Keep")          String             keep      ,
                                       @Context                      HttpServletRequest request   ,
                                       @PathParam(SERVICENAME_P) final String  codeService ) throws BusinessException {

        RuntimeServiceInfos.STARTED_TIME.set(Instant.now()) ;
       
        tryingAcquireSemaphore()                            ;
          
        RuntimeServiceInfos.SERVICE_NAME.set( codeService ) ;
        RuntimeServiceInfos.ACCEPT.set( accept )            ;
          
        LOGGER.log( Level.INFO   , 
                    " +++ Invoke resource : ( code_service : {0} ) "     +
                    "( accept : {1} ) ( cipher : {2} ) ( keep : {3} ) "  +
                    "( token : {4} ) ( Date : {5} ) ( RemoteAddr : {6} ) " ,
                    new Object[] { codeService , 
                                   accept      ,
                                   cipher      , 
                                   keep        ,
                                   token       , 
                                   LocalDateTime.now()
                                                .format( DateTimeFormatter
                                                .ofPattern("dd/MM/yyyy HH:mm:ss")) ,
                                   getRemoteAddr(request) } ) ;
                
        return checkAuthAndProcessService ( codeService, accept, token, cipher) ;
    }
   
  
    private Object checkAuthAndProcessService ( final String codeService , 
                                                final String accept      ,
                                                final String token       , 
                                                final String cipher       ) throws BusinessException {
      
        Policy policy = servicesManager.containService(codeService) ;
        
        if( policy == null ) throw new BusinessException("Unavailable Service")   ;
        
        if(   accept != null              && 
              accept.contains("template") &&
             ! servicesManager.serviceContainsTemplate(codeService) )             {
               throw new BusinessException( " No Template assigned to the service "
                                           + "[ " +codeService + " ] ")           ;
        }
            
        if( policy == Policy.Public )                             {
            
            if( accept != null && accept.contains("encrypted") )  {
                throw new BusinessException(" Public Services can't be Encrypted ") ;
            }
           
            return servicesManager.get(codeService )  ;
        }
        
        if( policy == Policy.SSO ) {
            return servicesManager.get(codeService)   ;
        }

        /** The following need Authentication . */
          
        if( accept != null && accept.contains("encrypted") && token == null )
            throw new BusinessException(" Header [ API-key-Token ] can't be NULL for secured services ") ;
      
        if( token == null )  throw new BusinessException( " Authentication Required. "
                                                          + "Missing Header [ API-key-Token ] ") ;
        
        if( ! servicesManager.containAcceptForService( codeService, accept ))   {
            throw new BusinessException ( " The service [ " + codeService       +
                                          " ] Doesn't authorize Accept Header " +
                                          "[ " + accept + " ] " )               ;
        }
        if( ! servicesManager.containCiphersForService( codeService, cipher) 
              && accept != null  && accept.contains("crypt")  )            {
            throw new BusinessException ( " The service [ " + codeService  + 
                                          " ] Doesn't authorize "          +
                                          "[ " + cipher + " ] Cipher ")    ;
        }
        
        if( policy == Policy.CustomSignOn ) {

            ISignOn implSignOn = signOn.select( new AnnotationLiteral<Custom>() {}) 
                                       .get() ;
            
            if( implSignOn == null ) {
                throw new BusinessException(" No Provider found for Custom Authentication ") ;
            }
            
            if ( implSignOn.checkIntegrity ( token ,
                                             implSignOn.getConfigurator()
                                                       .getValidRequestTimeout() ) ) {
                
                if( cipher == null ) {
                    LOGGER.log(Level.INFO , " -- Default cipher activated : {0}", CipherTypes.AES_128_ECB.name()) ;
                    ISignOn.CIPHER.set( CipherTypes.AES_128_ECB.name() ) ;
                }
                else {
                    ISignOn.CIPHER.set(cipher.trim())   ; 
                }
                
                return servicesManager.get(codeService) ;
            }
        }
        
        LOGGER.log( Level.SEVERE, " --- Unauthorized Resource :" +
                                  " ( code_service : {0} ) ( accept : {1} ) ( cipher : {2} ) ( token : {3} ) " ,
                                 new Object[] { codeService, accept, cipher, token } )       ;
        
        throw new UnAuthorizedResourceException ("Unauthorized Resource - KO_Authentication") ;
    }

    @GET
    @Override
    @Path(LOGIN + SEPARATOR + SIGNATURE + SEPARATOR + TIMESTAMP )
    public Response authenticationCheck( @PathParam("login")     final String login     ,
                                         @PathParam("signature") final String signature ,
                                         @PathParam("timeStamp") final String timeStamp) throws BusinessException {

        if ( signOn.select(new AnnotationLiteral<Custom>() {}).get().checkIntegrity(login, timeStamp, signature)) {
            
             return Response.status ( Response.Status.OK )
                            .entity ( "<status> OK_Authentication </status>" )
                            .type(MediaType.APPLICATION_XML)
                            .build() ;
        }
        throw new UnAuthorizedResourceException ("KO_Authentication") ;
    }
    
    private String getRemoteAddr( HttpServletRequest request ) {
        
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null) {  
         return request.getRemoteAddr();  
        }
        return ipAddress ;
    }
    
    /** Force Init ApplicationScoped at deployement time . */
    /*
      public void init( @Observes 
                        @Initialized(ApplicationScoped.class ) Object init ) {
      }
    */
}

