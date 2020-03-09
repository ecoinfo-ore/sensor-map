
package com.rac021.jaxy.api.executorservice ;


import java.util.concurrent.TimeUnit ;
import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.ThreadFactory ;
import java.util.concurrent.ThreadPoolExecutor ;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ryahiaoui
 */

public class Worker extends ThreadPoolExecutor {

    private static final String THREAD_NAME_PATTERN = "%s-%d";

    public Worker( int      corePoolSize    , 
                   int      maximumPoolSize , 
                   long     keepAliveTime   , 
                   TimeUnit unit            ,
                   BlockingQueue<Runnable> workQueue ) {
       
        super( corePoolSize    , 
               maximumPoolSize , 
               keepAliveTime   , 
               unit            , 
               workQueue       ,
               
                new ThreadFactory() {

                private final AtomicInteger counter = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    final String threadName = String.format( THREAD_NAME_PATTERN , 
                                                             "JaxyPool-Thread"   , 
                                                             counter.incrementAndGet()) ;
                    return new Thread(r, threadName ) ;
                }
             }) ; 
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r) ;
    }
    
    @Override
    protected void afterExecute(Runnable r, Throwable t ) {

       super.afterExecute(r, t ) ;
       /*
        if (t == null && r instanceof Future)    {
            try {
              Object result = ((Future) r).get() ;
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
               // Thread.currentThread().interrupt() ; // ignore/reset
            } finally {
               // Thread.currentThread().interrupt() ;
               // remove(r) ;
            }
        }
        if( t != null ) {
           // Do something with the Exception 
           // LOGGER.log( Level.SEVERE, t.getMessage(),t ) ;
        }
       */
    }

}
