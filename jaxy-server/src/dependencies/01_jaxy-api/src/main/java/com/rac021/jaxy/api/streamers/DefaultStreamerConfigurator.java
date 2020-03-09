
package com.rac021.jaxy.api.streamers ;

import javax.ejb.Singleton ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.ThreadPoolExecutor ;
import java.util.concurrent.ArrayBlockingQueue ;
import com.rac021.jaxy.api.executorservice.Worker ; 
import javax.enterprise.context.ApplicationScoped ;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger;
;

/**
 *
 * @author yahiaoui
 */


@Singleton
@ApplicationScoped

public class DefaultStreamerConfigurator                        {
   
    /** Default Lenght of the request extraction . **/
    public static int selectSize                       = 5000   ;
    
    /** Ratio of the extraction . **/
    public static int ratio                            = 1      ;
    
    /*** Default Nbr Threads    . **/
    public static Integer defaultMaxThreadsPerService  = 2      ;

    /** Default cache size Response for flush data . **/
    public static int responseCacheSize                = 500    ;

    /** Default Block Size for Encryption . **/
    public static int blockSize                        = 16     ;

    /** Thread Pool Size - Shared by all services . **/
    public static int threadPoolSizeApp                = 8      ;
  
    /** Final . **/

    /** Size of the Queue which is used by Pool    . **/
    public static final int WORKER_QUEUE               = 5000   ;

    /** TimeOut Service ( in mn )  . **/
    public static final int THREAD_KEEPALIVE           = 1      ;

    /** ThreadPool Worker . **/
    public static Worker poolProducer                           ;
    
    private static final Logger LOGGER           = getLogger()  ;
    
    /** NB : Concurent Users is Configured in RootService .  **/
    
    static {
      initPoolProducer() ; 
    }

    public DefaultStreamerConfigurator() { }
    
    public static void initPoolProducer()  {
      
        if( (  defaultMaxThreadsPerService != null ) && 
               threadPoolSizeApp < defaultMaxThreadsPerService )  {
            try {
                throw new BusinessException(" maxPoolSize can't be lower than maxThreads" ) ;
            } catch (BusinessException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage() , ex ) ;
            }
        }
        
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>( WORKER_QUEUE )    ;
       
        LOGGER.log(Level.INFO , " --- Initialise JaxyPool-Thread ----------- "                     ) ;
        LOGGER.log(Level.INFO , "     -->  threadPoolSizeApp           : {0} " , threadPoolSizeApp ) ;
        LOGGER.log(Level.INFO , "     -->  Thread_KeepAliveTime ( mn ) : {0} " , THREAD_KEEPALIVE  ) ;
        LOGGER.log(Level.INFO , " ------------------------------------------ "                     ) ;
        LOGGER.log(Level.INFO , "                                            "                     ) ;
        
        poolProducer = new Worker( threadPoolSizeApp  ,
                                   threadPoolSizeApp  , 
                                   THREAD_KEEPALIVE   , 
                                   TimeUnit.MINUTES   , 
                                   blockingQueue   )  ;
     
        poolProducer.setRejectedExecutionHandler( (Runnable r, ThreadPoolExecutor executor) ->  {
        
        LOGGER.log( Level.FINE , " --> Thread ** {0} ** Rejected ", r.toString() ) ;
           
        try { 
              Thread.sleep( 10 ) ; 
        } 
        catch (InterruptedException ex ) { 
            LOGGER.log( Level.SEVERE, ex.getMessage() , ex ) ;
        }
          
        LOGGER.log( Level.FINE , " Retry Thread **  {0} ** ", r.toString()) ;
        executor.execute(r)                                                 ;
     
        });
    }
    
    public static int getSelectSize()           {
        return selectSize ;
    }
    public static int getRatio()                {
        return ratio ;
    }
    public static int getMaxThreadsPerService() {
        return defaultMaxThreadsPerService ;
    }
    public static int getResponseCacheSize()    {
        return responseCacheSize ;
    }
    public static int getBlockSize()            {
        return blockSize ;
    }
    public static int getThreadPoolSize()       {
        return threadPoolSizeApp ;
    }
    
}
