
package com.rac021.sensormap.api.exceptions ;

/**
 *
 * @author ryahiaoui
 */

import java.util.logging.Level ;
import java.util.logging.Logger ;
import javax.ws.rs.ext.Provider ;
import javax.ws.rs.core.Response ;
import javax.ws.rs.core.MediaType ;
import javax.ws.rs.ext.ExceptionMapper ;
import static com.rac021.sensormap.api.logger.LoggerFactory.getLogger ;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    static final Logger LOGGER = getLogger()           ; 
   
    @Override
    public Response toResponse(Throwable ex)           {
         
        LOGGER.log(Level.SEVERE, ex.getMessage(), ex ) ;
         
        if( ex instanceof RuntimeException )           {
            
            // Metrics.incTotalExceptions()  ;
      
             return Response.status( Response.Status.INTERNAL_SERVER_ERROR    )
                            .header( "x-reason", ex.getMessage() )
                            .entity ( "<status> RuntimeException - "  +
                                      ex.getLocalizedMessage()        +
                                     " </status>" )
                            .type(MediaType.APPLICATION_XML)
                            .build() ;
        
        } 
        
        else if( ex instanceof UnAuthorizedResourceException )    {
      
            // Metrics.incTfailureAuthentication()                  ;
              
             return Response.status( Response.Status.UNAUTHORIZED )
                            .header( "x-reason",
                                     ex.getMessage())
                            .entity ( "<status> UnAuthorizedResourceException " +
                                      ex.getLocalizedMessage() + "</status>"    )
                            .type(MediaType.APPLICATION_XML)
                            .build() ;
        }
       
        return Response.status( Response.Status.INTERNAL_SERVER_ERROR )
                           .header( "x-reason",
                                    ex.getMessage())
                           .entity ( "<status> EXCEPTION : "   +
                                      ex.getLocalizedMessage() +
                                     "</status>" )
                           .type(MediaType.APPLICATION_XML ) 
                           .build() ;
    }
   
}

