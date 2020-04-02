
package com.rac021.sensormap.impl.service.logs ;

/**
 *
 * @author ryahiaoui
 */

import javax.ws.rs.GET ;
import javax.ws.rs.OPTIONS ;
import java.nio.file.Files ;
import java.nio.file.Paths ;
import javax.inject.Inject ;
import javax.ws.rs.Produces ;
import javax.inject.Singleton ;
import javax.ws.rs.HeaderParam ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import io.quarkus.arc.Unremovable ;
import com.rac021.sensormap.impl.io.Writer ;
import com.rac021.sensormap.api.security.Policy ;
import com.rac021.sensormap.api.security.Secured;
import com.rac021.sensormap.api.qualifiers.ServiceRegistry ;
import com.rac021.sensormap.impl.utils.checker.TokenManager ;
/**
 *
 * @author R.Yahiaoui
 */

@ServiceRegistry("logs")
@Secured(policy = Policy.CustomSignOn )
@Singleton
@Unremovable
public class SensorLogService  {
   
    @Inject 
    LoggerTask loggerTask   ;
    
    public SensorLogService() {
    }
   
    @GET
    @OPTIONS
    @Produces( { "xml/plain" , "json/plain" , "json/encrypted" , "xml/encrypted" } )
    public Response getResource ( @HeaderParam("API-key-Token") String  token        ,
                                  @HeaderParam("keep")          String  filterdIndex , 
                                  @Context                      UriInfo uriInfo      ) throws Exception {

        System.out.println(" Logger Service.." )        ;
        
        String login     = TokenManager.getLogin(token) ;
        
        String path_logs = TokenManager.builPathLog( "logger" /*configuration.getLoggerFile()*/, login ) ;
        
        if ( ! Writer.existFile( path_logs ) ||
               Files.lines( Paths.get(path_logs)).count() <=  0 ) {
            
            return Response.status(Response.Status.OK)
                           .entity( " \n Empty LOGS for the user : " + login + "\n" ) 
                           .build() ;        
        }
        
        /*
        return Response.status(Response.Status.OK)
                       .entity( new StreamerLog ( path_logs ,  
                                                  configuration.getFrequencyUpdateTimeMs() ) )
                       .build() ;   
        */
        
        /*
         return Response.status(Response.Status.OK)
                       .entity( new StreamerLogV2 ( path_logs ) )
                       .build() ;  
        */
       
        loggerTask.setLoggerFile( path_logs ) ;
            
        loggerTask.setTailDelayMillis( 5 )    ;
            
        return Response.status ( Response.Status.OK )
                       .entity ( loggerTask ) 
                       .build() ;
    }
}

