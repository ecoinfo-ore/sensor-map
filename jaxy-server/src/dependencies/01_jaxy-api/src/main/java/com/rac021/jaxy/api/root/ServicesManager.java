
package com.rac021.jaxy.api.root ;

import java.util.Set ;
import java.util.Map ;
import java.util.List ;
import java.util.Arrays ;
import java.util.HashSet ;
import javax.ejb.Startup ;
import java.util.HashMap ;
import javax.inject.Inject ;
import javax.ejb.Singleton ;
import java.util.ArrayList ;
import java.sql.Connection ;
import javax.ws.rs.Produces ;
import java.util.stream.Stream ;
import java.util.logging.Level ;
import java.lang.reflect.Field ;
import java.lang.reflect.Method ;
import java.util.logging.Logger ; 
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.api.pojos.Query ;
import javax.persistence.EntityManager ;
import java.lang.annotation.Annotation ;
import javax.enterprise.inject.spi.Bean ;
import javax.persistence.PersistenceContext ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.crypto.CipherTypes ;
import com.rac021.jaxy.api.streamers.Streamer ;
import com.rac021.jaxy.api.qualifiers.SqlQuery ;
import com.rac021.jaxy.api.analyzer.SqlAnalyzer ;
import javax.enterprise.inject.spi.BeanManager ;
import static java.util.stream.Collectors.toList ;
import javax.enterprise.context.ApplicationScoped ;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry ;
import com.rac021.jaxy.api.qualifiers.security.Cipher ;
import com.rac021.jaxy.api.qualifiers.security.Policy ;
import com.rac021.jaxy.api.qualifiers.ResourceRegistry ;
import com.rac021.jaxy.api.qualifiers.security.Secured ;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
@Startup
@ApplicationScoped

public class ServicesManager {
    
    @PersistenceContext  (unitName = Streamer.PU )
    private EntityManager entityManager    ;

    private static final String PROXY_CLASS =   "org.jboss.weld.bean.proxy.ProxyObject"   ;
    
    private final Map<String, Object>             publicServices        = new HashMap<>() ;
    private final Map<String, Object>             customSignOnServices  = new HashMap<>() ;
    private final Map<String, Object>             ssoServices           = new HashMap<>() ;
    private final Map<String, Query >             resources             = new HashMap<>() ;
    
    private final Map<String, Set<CipherTypes> >  ciphers               = new HashMap<>() ;
    private final Map<String, Set<AcceptType> >   accept                = new HashMap<>() ;
    private final Map<String, String >            template              = new HashMap<>() ;
    
    private final Map<String, Integer >           maxThreadsPerService = new HashMap<>()  ;
   
    
    private static final Logger LOGGER = getLogger()                                      ;
    
    @Inject
    private BeanManager bm ;
    
    @PostConstruct
    public void init() {

        scanAndRegisterRealServices() ; 
    }
    
    public ServicesManager() {
    }

   
    public void registerService( String id, Object service  )      {
        
        if( id == null || service == null ) return ;
        
        LOGGER.log(Level.INFO , "                               "  )      ;
        LOGGER.log(Level.INFO , " ****************************** " )      ;
        LOGGER.log(Level.INFO , " ===>>> Register Service   : {0} // {1}" , 
                                new Object[]{id, service})                ;
        LOGGER.log(Level.INFO , " ****************************** " )      ;
        
        /** Annotations are not existing in the Proxy
           So we use service class to check security annotations . */
        
        if(  ( service.getClass().getAnnotation(Secured.class) != null  )  ||
             ( isProxifiedByWeld( service ) ) &&
               service.getClass().getSuperclass().getAnnotation(Secured.class) != null ) 
           {
           
            Secured security =  isProxifiedByWeld( service ) ?
                    service.getClass().getSuperclass().getAnnotation(Secured.class) : 
                    service.getClass().getAnnotation(Secured.class)  ;
            
            String  policy     = security.policy().name()            ;
            
            if( policy.equalsIgnoreCase(Policy.CustomSignOn.name())) {
                this.customSignOnServices.put( id, service )         ;
            }
            else if( policy.equalsIgnoreCase(Policy.SSO.name()))     {
                this.ssoServices.put( id, service )                  ;
            }
            else {
                this.publicServices.put( id, service )               ;
            }
        }
        else {
            this.publicServices.put(id, service )                    ;
        }
    }
    
    public void registerCiphers ( String id, List<CipherTypes> ciphers ) {
        this.ciphers.computeIfAbsent( id, k -> new HashSet(ciphers) )    ;
    }
    
    public void registerAcceptTypes ( String id, List<AcceptType> accepts ) {
        this.accept.computeIfAbsent( id, k -> new HashSet(accepts) )        ;
    }
            
    public  Object get( String id )     {
      return publicServices.getOrDefault(        id , 
              customSignOnServices.getOrDefault( id , 
                      ssoServices.getOrDefault(  id , null )))  ;
    }
    
    public Policy containService( String idService )                             {
      if(publicServices.containsKey(idService)) return Policy.Public             ;
      if(customSignOnServices.containsKey(idService)) return Policy.CustomSignOn ;
      if(ssoServices.containsKey(idService)) return Policy.SSO                   ;
      return null                                                                ;
    }
    
    public Policy getSecurityLevel() {
      if( ! customSignOnServices.isEmpty() ) return Policy.CustomSignOn ;
      if( ! ssoServices.isEmpty())           return Policy.SSO          ;
      return  Policy.Public                                             ;
    }

    public Map<String, Object> getMapOfAllSubServices() {
   
       Map<String, Object>    res = new HashMap() ;
       res.putAll( publicServices )               ;
       res.putAll( customSignOnServices )         ;
       res.putAll( ssoServices )                  ;
       return res ;
    }
    
    public Map<String, Query> getMapResources() {
       return resources ;
    }
    
    private void scanAndRegisterRealServices()                        {
    
       LOGGER.log(Level.INFO ,"                                   " ) ;
       LOGGER.log(Level.INFO ," ********************************* " ) ;
       LOGGER.log(Level.INFO ," ********************************* " ) ;
       LOGGER.log(Level.INFO ," --> Scanning Real Services        " ) ;
       LOGGER.log(Level.INFO ," ********************************* " ) ;
       LOGGER.log(Level.INFO ," ********************************* " ) ;
       
       List<String> classes  = scanRealServices( ServiceRegistry.class    , 
                                                 ResourceRegistry.class   , 
                                                 SqlQuery.class         ) ;
       
       if( classes.isEmpty() ) {
           
           LOGGER.log(Level.INFO ,"                                   " ) ;
           LOGGER.log(Level.INFO ," ********************************* " ) ;
           LOGGER.log(Level.INFO ," --> Zero Real Service Found       " ) ;
           LOGGER.log(Level.INFO ," ********************************* " ) ;
       }
       
       for( String clazzName : classes ) {
        
          try {
              
               Class<?>        serviceClazz    = Class.forName(clazzName) ;
               
               Secured securityAnnotation = serviceClazz.getAnnotation(Secured.class)  != null ?
                                              ( Secured ) serviceClazz.getAnnotation(Secured.class)  : null  ;
               
               ServiceRegistry serviceRegistry = serviceClazz.getAnnotation(ServiceRegistry.class)           ;
               String          serviceName     = serviceClazz.getAnnotation( ServiceRegistry.class ).value() ;
             
               Bean<Object> bean = (Bean<Object>) bm.resolve(bm.getBeans(serviceClazz, serviceRegistry ) )   ;
              
               if(bean != null ) {
                   
                   Object cdiService = (Object) bm.getReference( bean, bean.getBeanClass(), 
                                                                 bm.createCreationalContext(bean) )  ;
                   registerService( serviceName , cdiService ) ;
                   
                   Cipher cipherAnnotation   = getInstanceAnnotationFromClass( cdiService.getClass() ,
                                                                               Cipher.class )        ;
                   
                   if( cipherAnnotation != null ) {
                       registerCiphers( serviceName , 
                                        Arrays.asList(cipherAnnotation.cipherType()) )               ;
                   }
                   
                   Method[] methods     = cdiService.getClass().getMethods()                         ;

                   List<String> typeList = new ArrayList()                                           ;
                   boolean existProduces = false                                                     ;
                   
                   for (final Method method : methods) {
                       
                     Annotation[] annotations = method.getAnnotations() ;
                       
                     if (method.isAnnotationPresent(Produces.class))    {
                       
                         List<String> types = Stream.of ( method.getAnnotation(Produces.class).value())
                                                    .map ( type -> type.toUpperCase())
                                                    .collect(toList()) ; 
                         typeList.addAll(types) ;
                           
                        } else {
                           existProduces = true ;                                
                        }
                     }
                   
                     if ( ! typeList.isEmpty() ) {
                         registerAcceptTypes(serviceName, AcceptType.toList(typeList)) ;
                     }
                     
                     else if( existProduces && typeList.isEmpty() ) {
                        registerAcceptTypes(serviceName, Arrays.asList( AcceptType.XML_PLAIN        ,
                                                                        AcceptType.XML_ENCRYPTED    , 
                                                                        AcceptType.JSON_PLAIN       ,
                                                                        AcceptType.JSON_ENCRYPTED)) ;
                     }                                      
               }
          
          } catch(ClassNotFoundException ex) {
             throw new RuntimeException(ex ) ;
           }
       }
    }
    
    private List<String > scanRealServices ( Class serviceAnnotation, Class registryAnnotation , Class queryAnnotation ) {
        
        
        List<String> namesOfAnnotationsWithMetaAnnotation = new FastClasspathScanner().scan()
                                                                .getNamesOfClassesWithAnnotation( registryAnnotation) ;
        
                                                            // new ClassGraph().scan()
                                                            //                 .getClassesWithAnnotation(registryAnnotation.getName())
                                                            //                 .getNames() ;
        
        try ( Connection cnn  = entityManager.unwrap(java.sql.Connection.class )) {
            
            namesOfAnnotationsWithMetaAnnotation.forEach ( resource -> {  try {
                                                                                extractAndRegisterQueriesFromResource (
                                                                                        Class.forName(resource), queryAnnotation , cnn ) ;
                                                                       } catch (ClassNotFoundException ex) {
                                                                          throw new RuntimeException(ex)   ;
                                                                       }
            } ) ;
        
        } catch( Exception ex )            {
            throw new RuntimeException(ex) ;
        }
        
        return new FastClasspathScanner().scan()
                                         .getNamesOfClassesWithAnnotation( serviceAnnotation ) ;
    }
    

    private <T> T getInstanceAnnotationFromClass( Class clazz, Class<T> annotToFind ) {
        
        Annotation[] annotations = clazz.getAnnotations() ;

        for(Annotation annotation : annotations )         {
            
          if ( annotToFind.isAssignableFrom(annotation.getClass())) {
              return (T) annotation ;             
          }
        }
    
       return null ;
    }
    
    public void extractAndRegisterQueriesFromResource ( Class resource        ,
                                                        Class queryAnnotation , 
                                                        Connection cnn        ) {
      
     Class<?>     c  = resource                  ;
          
     try {
            Object instance  = c.newInstance()   ;
            
            while (c != null) {
                
               for (Field field : c.getDeclaredFields())           {
                   
                   if (field.isAnnotationPresent(queryAnnotation)) {
                       
                       field.setAccessible(true) ;
                       
                       if(field.getType().toString().equals("class java.lang.String")) {
                           
                           String sqlQuery = (String) field.get( instance ) ;
                           
                           if( sqlQuery != null )        {
                               
                             LOGGER.log(Level.INFO , " " )                                              ;
                             LOGGER.log(Level.INFO , "  - Register Resource : {0}", resource.getName()) ;
                             LOGGER.log(Level.INFO , " " )                                              ;
                             registerResource( resource.getName(), 
                                               SqlAnalyzer.buildQueryObject( cnn, sqlQuery) )           ;
                             
                           }
                       }
                   }
               }
               c = c.getSuperclass() ;
           }

     } catch ( IllegalAccessException | IllegalArgumentException | 
               InstantiationException | SecurityException ex )   {
         throw new RuntimeException(ex)                          ;
     } 
     
    }

    public Field getFieldFor( Class clazz , Class annotation ) {
 
        for( Field field  : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                      return field                     ;
           }
        }
        
        return null ;
    }
    
    public Set<CipherTypes> getCiphersForServiceName( String serviceName ) {
        return ciphers.getOrDefault(serviceName, new HashSet<>() ) ;
    }
    
    public Set<AcceptType> getAcceptForServiceName( String serviceName )   {
        return accept.getOrDefault(serviceName, new HashSet<>() )  ;
    }
    
    public boolean containCiphersForService( String serviceName , 
                                                String cipher      )
                                                throws BusinessException {
        if( cipher == null ) return false       ;
        return ciphers.containsKey(serviceName) ?
               ciphers.get(serviceName)
                      .contains( CipherTypes.toCipherTypes(cipher)) :
               false ;
    }
    
    public boolean containAcceptForService( String serviceName , 
                                               String accept      )
                                               throws BusinessException {
        if ( accept == null ) return false ;
        return this.accept.containsKey(serviceName) ? 
               this.accept.get(serviceName)
                          .contains( AcceptType.toAcceptTypes ( 
                                        accept.toUpperCase().replace("/", "_"))) :
               false ;
    }
   
    public void registerResource( String resourceName, Query query)      {
        this.resources.put(resourceName , query ) ;
    }
    public void registerTemplate( String serviceName, String template ) {
       this.template.put(serviceName, template ) ;
    }
    
    public String getTemplate(String template )                  {
       return this.template.getOrDefault(template, null )        ;
    }
    
    public boolean serviceContainsTemplate(String serviceName )  {
       return this.template.containsKey(serviceName )            ;
    }
    
    public Query getQueriesByResourceName( String resourceName ) {
        return resources.getOrDefault(resourceName, null ) ;
    }

    public void apllyMaxThreads(String serviceCode , int maxThreads )  {
       this.maxThreadsPerService.put(serviceCode, maxThreads ) ;
    }
    public Integer getOrDefaultMaxThreadsFor(String serviceCode )      {
       return this.maxThreadsPerService.getOrDefault(serviceCode       , 
                     DefaultStreamerConfigurator.defaultMaxThreadsPerService )  ;
    }
    
    private boolean isProxifiedByWeld( Object object )           {
        
        try {
            return Class.forName(PROXY_CLASS).isInstance(object) ;
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex)        ;
            return false ;
        }
    }

}
