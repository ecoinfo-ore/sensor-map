
package com.rac021.jaxy.service_discovery ;

import java.util.Set ;
import java.util.Map ;
import java.util.List ;
import javax.ws.rs.GET ;
import java.util.HashMap ;
import java.util.HashSet ;
import javax.inject.Inject ;
import java.util.ArrayList ;
import javax.ws.rs.Produces ;
import javax.inject.Singleton ;
import java.util.logging.Level ;
import java.lang.reflect.Field ;
import java.util.logging.Logger ;
import javax.xml.bind.Marshaller ;
import javax.ws.rs.core.Response ;
import javax.xml.bind.JAXBContext ;
import java.util.stream.Collectors ;
import javax.xml.bind.JAXBException ;
import java.io.ByteArrayOutputStream ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.api.pojos.Query ;
import com.rac021.jaxy.api.streamers.EmptyPojo ;
import com.rac021.jaxy.api.root.ServicesManager ;
import com.rac021.jaxy.api.qualifiers.security.Policy ;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry ;
import com.rac021.jaxy.api.qualifiers.ResourceRegistry ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author R.Yahioaui
 */

@Singleton
@ServiceRegistry("infoServices")

public class InfoServices      {

    private static final Logger LOGGER = getLogger() ;
    
    @Inject 
    ServicesManager servicesManager ;

    private List<ServiceDescription> services  = null ;
    private ByteArrayOutputStream    baoStream = null ;
    
    @PostConstruct
    public void init() {
    }

    public InfoServices() {
    }
   
    @GET
    /** Return Always JSON for all Produces . **/
    @Produces( { "json/plain" } )
    public Response getResourceJson() throws Exception      {    
        return produceInfoServices() ;
    }

    private Response produceInfoServices() throws Exception {

       return  Response.ok( scannServices()).build()     ;        
    }
    
    public String scannServices()  throws  Exception     {
    
        if( services == null ) {
       
           services = extractServices()                  ;
 
           baoStream = new ByteArrayOutputStream()       ;

           Marshaller marshaller = getMashellerWithJsonProperties() ;
                      marshaller.marshal(services, baoStream )      ;
            
            return baoStream.toString("UTF8")             ;
        }
        
        return baoStream != null ? baoStream.toString("UTF8") : "" ;
    }
    
    
    public List<ServiceDescription> extractServices() {
    
        if( services != null ) return services        ;
        
        List<ServiceDescription> _services = new ArrayList<>() ;
        
        servicesManager.getMapOfAllSubServices().forEach( ( String k, Object v) -> {

            String serviceName = k ;
            Object service     = v ;
                
            String security    = servicesManager.containService(k).name() ;
                
            Field resourceField = servicesManager.getFieldFor( service.getClass().getSuperclass(),
                                                               ResourceRegistry.class ) ;

            Map<String, String > params = new HashMap<>()                 ;
            
            if( resourceField != null ) {
                    
                Query queriesByResourceName = servicesManager.getQueriesByResourceName (
                                                    resourceField.getGenericType().getTypeName()) ;
                
                if( queriesByResourceName != null ) {
                    queriesByResourceName.getParameters().forEach( (s, t ) ->  {
                        params.put(s, t.get("TYPE").replace("java.lang.", "")) ;
                    }) ;
                }
            }
            
            Set<String> ciphers  = new HashSet() ;
            ciphers.add(" - ")                   ;
                    
            if( ! security.equalsIgnoreCase(Policy.Public.name())) {
                 ciphers = servicesManager.getCiphersForServiceName(serviceName)
                                          .stream()
                                          .map( cipher -> cipher.name())
                                          .collect(Collectors.toSet()) ;
            }
                    
            Set<String>  accepts = servicesManager.getAcceptForServiceName(serviceName)
                                                  .stream()
                                                  .map( accept -> accept.name())
                                                  .collect(Collectors.toSet()) ;
                    
            accepts =  accepts.stream()
                              .map( accept -> accept.toLowerCase()
                                                    .replace("_", "/")
                                                    .replace("-", "__"))
                              .collect(Collectors.toSet()) ;
                            
            _services.add( new ServiceDescription( serviceName , 
                                                   security    , 
                                                   params      , 
                                                   ciphers     ,
                                                   accepts     ,
                                                   servicesManager.
                                                    getOrDefaultMaxThreadsFor (
                                                              serviceName) ,
                                                   servicesManager.
                                                    getTemplate(serviceName))) ;
        }) ;
        
        return _services ;
    }
    
    private javax.xml.bind.Marshaller getMashellerWithJsonProperties()  {
        
        Marshaller localMarshaller = null ;
        JAXBContext jc                    ;
        try {
            jc = JAXBContext.newInstance( ServiceDescription.class, EmptyPojo.class)                   ;
            localMarshaller = jc.createMarshaller()                                                    ;
            localMarshaller.setProperty("eclipselink.media-type", "application/json")                  ;
            localMarshaller.setProperty("eclipselink.json.include-root", Boolean.FALSE)                ;
            localMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE) ;
        } catch (JAXBException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex ) ;
        }
        return localMarshaller ;
    }
     
}
