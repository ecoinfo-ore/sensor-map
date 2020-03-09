
package com.rac021.jaxy.api.root ;

/**
 *
 * @author ryahiaoui
 */
import java.time.Instant ;
import java.time.Duration ;
import java.io.IOException ;
import java.util.logging.Level ;
import javax.ws.rs.ext.Provider ;
import java.util.logging.Logger ;
import java.util.concurrent.TimeUnit ;
import javax.ws.rs.ext.WriterInterceptor ;
import com.rac021.jaxy.api.metrics.Metrics ;
import javax.ws.rs.ext.WriterInterceptorContext ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import static com.rac021.jaxy.api.root.ConcurrentUsersManager.tryingReleaseSemaphore ;

@Provider
public class ResponseWriterInterceptorContext implements WriterInterceptor {
    
    protected static final  Logger LOGGER   = getLogger()  ;
    
    @Override
    public void aroundWriteTo( WriterInterceptorContext context ) throws IOException {

        try {
            
            context.proceed()   ;

        } catch( Exception ex ) {
        
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex )     ;
            Metrics.incTotalExceptions()                       ;
            
            context.getOutputStream()
                   .write( ( "\n<status> RuntimeException - "  +
                              getCause( ex )                   +
                              " </status>\n\n" ) .getBytes() ) ;
        }
        
        finally {

            if ( RuntimeServiceInfos.STARTED_TIME.get() != null ) {

                Instant finish = Instant.now();
                long timeElapsed = Duration.between( RuntimeServiceInfos.STARTED_TIME.get(), finish ).toMillis() ;

                String timer = RuntimeServiceInfos.SERVICE_NAME.get() +  "_"           +
                               RuntimeServiceInfos.ACCEPT.get().replaceAll("/", "_")   +
                               "_timer" ;
                
                Metrics.updateTimerService( timer, timeElapsed, TimeUnit.MILLISECONDS) ;

                RuntimeServiceInfos.STARTED_TIME.remove() ;
                RuntimeServiceInfos.SERVICE_NAME.remove() ;
            }

            /**
             * RELEASE SEMAPHORE . *
             */
            if (RuntimeServiceInfos.SEMAPHORE_CURRENT_THREAD_NAME.get() != null) {
                tryingReleaseSemaphore()                                         ;
                RuntimeServiceInfos.SEMAPHORE_CURRENT_THREAD_NAME.remove()       ;

            }
        }
    }
    
    private Throwable getCause(Throwable e) {
        
        Throwable cause  = null ; 
        Throwable result = e    ;

        while( ( cause = result.getCause() ) != null  && ( result != cause ) )  {
            result = cause ;
        }
        
        return result ;
    }    
}

