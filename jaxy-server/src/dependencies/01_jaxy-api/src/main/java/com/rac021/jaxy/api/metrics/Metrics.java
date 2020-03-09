
package com.rac021.jaxy.api.metrics ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.concurrent.TimeUnit ;
import javax.annotation.PostConstruct ;
import javax.enterprise.event.Observes ;
import javax.enterprise.context.Initialized ;
import java.util.concurrent.atomic.LongAdder ;
import org.eclipse.microprofile.metrics.Timer ;
import javax.enterprise.context.ApplicationScoped ;
import org.eclipse.microprofile.metrics.annotation.Gauge ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

@ApplicationScoped
public class Metrics {
   
    private static final Logger LOGGER = getLogger() ;
    
    private static final LongAdder totalExceptions       = new LongAdder() ;
    private static final LongAdder failureAuthentication = new LongAdder() ;            
    
    private static Map<String, Timer> timerServices = new HashMap<>()      ;
   
    @Gauge(unit = "count", absolute = true , name = "exceptions")
    public long getTotalExceptions()       {
        return totalExceptions.longValue() ;
    }
    
    @Gauge(unit = "count", absolute = true , name = "failureAuthentication")
    public long getFailureAuthentication()       {
        return failureAuthentication.longValue() ;
    }    
    
    @PostConstruct
    public void init( @Observes 
                      @Initialized(ApplicationScoped.class ) Object init ) {
        LOGGER.log(Level.INFO, " ++ Metrics Initialization.. " ) ;
    }
   
    public static void incTotalExceptions() {
       totalExceptions.increment()          ;
    }
    public static void incTfailureAuthentication() {
        failureAuthentication.increment()          ;
    }
       
    public static void addTimerService( String serviceName, Timer timer ) {
       timerServices.put( serviceName, timer ) ;
    }
    
    public static void updateTimerService( String serviceName, long val, TimeUnit unit ) {
       
       timerServices.computeIfPresent( serviceName , 
                                       ( sName, timer ) -> { timer.update( val, unit ) ;
                                                             return timer              ;
                                                           }
                                     ) ; 
    }
    
}

