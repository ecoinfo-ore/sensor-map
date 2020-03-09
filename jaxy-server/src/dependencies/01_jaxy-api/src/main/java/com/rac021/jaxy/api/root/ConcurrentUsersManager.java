
package com.rac021.jaxy.api.root ;

import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.concurrent.Semaphore ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */
public class ConcurrentUsersManager {
   
    private static final Logger LOGGER = getLogger()         ;
    
     /** Manage Concurent Users . */
    static Semaphore semaphoreMaxConcurrentUsers             ;
     
    /** Max Concurrent Users . */
    public static int maxConcurrentUsers = Integer.MAX_VALUE ;
    
    
    static void initSemaphoreConcurrentUsers() {
       if( maxConcurrentUsers > 0 ) {
           LOGGER.log(Level.INFO , " --> Initialize the Semaphore with value : {0} ",
                                   maxConcurrentUsers ) ;
           semaphoreMaxConcurrentUsers = new Semaphore( maxConcurrentUsers ) ;
       } else {
           LOGGER.log(Level.INFO , " --> Semaphore : Unlimited Concurency Users <-- " )  ;
           LOGGER.log(Level.INFO , "                                                " )  ;
       }
    }
    
    static void tryingAcquireSemaphore() {
        
      if( maxConcurrentUsers > 0 )       {
          
        try {
              LOGGER.log(Level.FINE, " Trying Aquire Semaphore")  ;
              semaphoreMaxConcurrentUsers.acquire()               ;
              RuntimeServiceInfos.SEMAPHORE_CURRENT_THREAD_NAME.set (
                                   Thread.currentThread().getName() ) ;
              LOGGER.log(Level.FINE, " Semaphore Aquired")            ;
              
          } catch( InterruptedException x )              {
              LOGGER.log(Level.SEVERE , x.getMessage())  ;
          } 
      }
    }
   
    static void tryingReleaseSemaphore() {
       
      if( maxConcurrentUsers > 0 )       {
           LOGGER.log(Level.FINE, " Release Semaphore" )  ;
           semaphoreMaxConcurrentUsers.release()          ;
           LOGGER.log(Level.FINE, " Semaphore Released ") ;
      }
    }
}
