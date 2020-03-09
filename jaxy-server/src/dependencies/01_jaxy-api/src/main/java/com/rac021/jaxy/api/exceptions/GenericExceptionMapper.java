
package com.rac021.jaxy.api.exceptions ;

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
import com.rac021.jaxy.api.metrics.Metrics ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException ;
import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException ;
import org.eclipse.microprofile.faulttolerance.exceptions.FaultToleranceException ;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException ;
import org.eclipse.microprofile.faulttolerance.exceptions.FaultToleranceDefinitionException ;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    static final Logger LOGGER = getLogger()           ; 
   
    @Override
    public Response toResponse(Throwable ex)           {
         
        LOGGER.log(Level.SEVERE, ex.getMessage(), ex ) ;
         
        if( ex instanceof RuntimeException )           {
            
             Metrics.incTotalExceptions()  ;
      
             return Response.status( Response.Status.INTERNAL_SERVER_ERROR    )
                            .header( "x-reason", ex.getMessage() )
                            .entity ( "<status> RuntimeException - "  +
                                      ex.getLocalizedMessage()        +
                                     " </status>" )
                            .type(MediaType.APPLICATION_XML)
                            .build() ;
        
        } 
        
        else if( ex instanceof UnAuthorizedResourceException )    {
      
             Metrics.incTfailureAuthentication()                  ;
              
             return Response.status( Response.Status.UNAUTHORIZED )
                            .header( "x-reason",
                                     ex.getMessage())
                            .entity ( "<status> UnAuthorizedResourceException " +
                                      ex.getLocalizedMessage() + "</status>"    )
                            .type(MediaType.APPLICATION_XML)
                            .build() ;
        }
        
        else if (ex instanceof FaultToleranceException )  {
            
            if( ex instanceof BulkheadException ) {
           
                return Response.status(Response.Status.SERVICE_UNAVAILABLE )
                               .header("x-reason" , ex.getMessage())
                               .entity ( "<status> BulkheadException : "          +
                                         ex.getClass()                            + 
                                         " ( Maybe MaxConcurrentUsers reached ) " +
                                         "</status>" )
                              .type(MediaType.APPLICATION_XML).build() ;
            }

            if( ex instanceof CircuitBreakerOpenException )                    {

                return Response.status(Response.Status.SERVICE_UNAVAILABLE )
                           .header("x-reason" , ex.getMessage() )
                           .entity ( "<status> CircuitBreakerOpenException : " +
                                     ex.getClass()                             +
                                     " </status> ")
                           .type(MediaType.APPLICATION_XML).build() ;
            }

            if( ex instanceof  FaultToleranceDefinitionException )             {
                   
                return Response.status(Response.Status.SERVICE_UNAVAILABLE )
                               .header("x-reason" , ex.getMessage() )
                               .entity ( "<status> FaultToleranceDefinitionException : " +
                                         ex.getClass()                                   +
                                         " </status> ")
                               .type(MediaType.APPLICATION_XML).build() ;
            }

            if( ex instanceof   TimeoutException ) {
                  
                return Response.status(Response.Status.SERVICE_UNAVAILABLE )
                               .header("x-reason" , ex.getMessage() )
                               .entity ( "<status> TimeoutException : " +
                                         ex.getClass()                  +
                                         " </status> ")
                               .type(MediaType.APPLICATION_XML).build() ;
            }

            return Response.status(Response.Status.SERVICE_UNAVAILABLE )
                           .header("x-reason" , ex.getMessage() )
                           .entity ( "<status> FaultToleranceException : " +
                                     ex.getClass()                         +
                                     " </status> ")
                           .type(MediaType.APPLICATION_XML).build()        ;
        }
        
        Metrics.incTotalExceptions()  ;
         
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

