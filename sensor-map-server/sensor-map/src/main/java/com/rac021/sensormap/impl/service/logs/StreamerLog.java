
package com.rac021.sensormap.impl.service.logs ;

import java.util.Set ;
import java.io.Writer ;
import java.util.Timer ;
import java.util.HashSet ;
import java.nio.file.Path ;
import java.nio.file.Paths ;
import java.io.IOException ;
import java.util.TimerTask ;
import java.io.OutputStream ;
import java.io.BufferedWriter ;
import javax.inject.Singleton ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.io.OutputStreamWriter ;
import java.util.concurrent.Future ;
import java.util.concurrent.Executors ;
import javax.ws.rs.core.StreamingOutput ;
import org.apache.commons.io.input.Tailer ;
import java.util.concurrent.ExecutorService ;
import java.util.concurrent.ThreadPoolExecutor ;
import org.apache.commons.io.input.TailerListener ;
import org.apache.commons.io.input.TailerListenerAdapter ;

/**
 *
 * @author yahiaoui
 */
@Singleton
public class StreamerLog  implements StreamingOutput , LoggerTask {

    private String  loggerFile      = null            ;
    
    private int     tailDelayMillis = 300             ;
    
    static Set<Path> logFilesList   = new HashSet<>() ;
   
    private final static ExecutorService executorService = Executors.newFixedThreadPool(5) ;
    
    public StreamerLog( String logger_file      ,
                        int tailDelayMillis )   {
        this.loggerFile       = logger_file     ;
        this.tailDelayMillis  = tailDelayMillis ;
    }
    
    public StreamerLog( ) { }

    public String getLoggerFile() {
        return loggerFile         ;
    }

    @Override
    public void setLoggerFile(String loggerFile)        {
        this.loggerFile = loggerFile ;
    }
    @Override
    public void setTailDelayMillis(int tailDelayMillis) {
        this.tailDelayMillis = tailDelayMillis ;
    }
    
    public int getTailDelayMillis() {
        return tailDelayMillis      ;
    }
    
    @Override
    public void write(OutputStream output) throws IOException      {
       
       Thread currentThread = Thread.currentThread()               ; 

       System.out.println(" Processing data in Stream-Log ...  : " + currentThread ) ;
        
       Path logFile = Paths.get(loggerFile ) ;
       
       logFilesList.add(logFile)             ;

       Writer writer = new BufferedWriter ( new OutputStreamWriter(output, "UTF8") ) ;

       TimerTask myTask = new TimerTask() {
             
            @Override
            public void run() {

                try {
                    writer.write(" ") ;
                    writer.flush()    ;
                } catch ( IOException ex ) {
                    System.out.println( " Writer Exception : " +
                                        ex.getMessage() )      ;
                    currentThread.interrupt()                  ;
                }
            }
       } ;

       Timer timer = new Timer()              ;
        
       timer.schedule( myTask, 10000, 10000 ) ;
       
       TailerListener listener = new TailerListenerAdapter() {
       
          @Override
          public void handle(String line )    {

            try {
                  writer.write(line + "\n")   ;
                  writer.flush()              ;
                  
            } catch (IOException ex )         {
              currentThread.interrupt()       ;
            }
         }
       } ;
        
       Tailer tailer = null ;
       Future tail   = null ;
       
       try {
           
           tailer = new Tailer( logFile.toFile()     ,
                                listener             , 
                                this.tailDelayMillis ) ;
           // tailer.run()                       ;
           tail = executorService.submit(tailer) ;
           tail.get()                            ;
           
       } catch ( Exception ex )     {
           Logger.getLogger(StreamerLog.class.getName())
                 .log(Level.SEVERE, null, ex )         ;
         
       } finally {
           
           System.out.println(" Close Connection... ") ;
           timer.cancel()                              ;
           if( tailer != null ) tailer.stop()          ;
           if( tail   != null ) tail.cancel(true)      ;
            ((ThreadPoolExecutor) executorService )
                                 .remove( tailer  )    ;
       }
        
       System.out.println(" Service LOG Closed ")      ;
    }

    /*
    
    @Scheduled(every="30s")
    public void checkTaskLogger() throws Exception        {
        
        Iterator<Path> iterator = logFilesList.iterator() ;
        
        if( ((ThreadPoolExecutor) executorService).getActiveCount() == 0 ) {
            return ;
            
        } else {
               System.out.println( " ThreadPoolExecutor Size : " + 
               ( ( ThreadPoolExecutor) executorService).getActiveCount() ) ;
        }
        
        while (iterator.hasNext())         {
            
            Path logFile = iterator.next() ;
            if( Files.exists(logFile ))    {
              System.out.println(" Check Log File : " + logFile.toAbsolutePath()) ;
              FileUtils.writeStringToFile ( logFile.toFile() ,
                                            "\n"             ,
                                            StandardCharsets.UTF_8, true ) ;

            } else {
                
              iterator.remove() ;
            }
        }
    }
    
    */
}
