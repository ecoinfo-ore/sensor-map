
package com.rac021.sensormap.impl.service.logs ;

import java.util.Set ;
import java.io.Writer ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.nio.file.Path ;
import java.nio.file.Files ;
import java.nio.file.Paths ;
import java.io.IOException ;
import java.io.OutputStream ;
import javax.inject.Singleton ;
import java.io.BufferedWriter ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.io.OutputStreamWriter ;
import java.util.concurrent.Future ;
import java.util.concurrent.TimeUnit;
import io.quarkus.scheduler.Scheduled ;
import java.util.concurrent.Executors ;
import org.apache.commons.io.FileUtils ;
import javax.ws.rs.core.StreamingOutput ;
import java.nio.charset.StandardCharsets ;
import org.apache.commons.io.input.Tailer ;
import java.util.concurrent.ExecutorService ;
import java.util.concurrent.TimeoutException ;
import java.util.concurrent.ExecutionException ;
import org.apache.commons.io.input.TailerListener ;
import org.apache.commons.io.input.TailerListenerAdapter ;


/**
 *
 * @author yahiaoui
 */

@Singleton
public class StreamerLog  implements StreamingOutput , LoggerTask {

    private String  loggerFile      = null          ;
    
    private int     tailDelayMillis = 300           ;
    
    static Set<Path> logFilesList = new HashSet<>() ;
   
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool( 10 ) ;
    
    public StreamerLog( ) { }
    
    @Override
    public void write(OutputStream output) throws IOException     {
       
       System.out.println(" Processing data in StreamerLog ... ") ;
       
       Path logFile = Paths.get(loggerFile ) ;
       
       logFilesList.add(logFile)             ;
       
       Writer writer = new BufferedWriter ( new OutputStreamWriter(output, "UTF8")) ;
       
       TailerListener listener = new TailerListenerAdapter() {
       
          Tailer tailer ;
          
          @Override
          public void handle(String line)     {

            try {
                  writer.write(line + "\n")   ;
                  writer.flush()              ;
            } catch (IOException ex)          {
               throw new RuntimeException(ex) ;
            }
         }
       } ;
        
       Tailer tailer = null ;
       Future tail   = null ;
       
       try {
           
           tailer = new Tailer( logFile.toFile() , listener, tailDelayMillis )  ;
           tail = EXECUTOR_SERVICE.submit(tailer) ;
           tail.get( 1 , TimeUnit.DAYS )          ;
           
       } catch ( InterruptedException | ExecutionException | TimeoutException ex )   {

           Logger.getLogger(StreamerLog.class.getName()).log(Level.SEVERE, null, ex) ;
         
       } finally {
           
           System.out.println(" Close Connection... ")    ;
           if( tailer != null ) tailer.stop()             ;
           if( tail   != null ) tail.cancel(true)         ;
       }
        
       System.out.println(" Service LOG Closed ")         ;
    }

    @Scheduled(every="15s")
    public void checkTaskLogger() throws Exception        {
        
        System.out.println("Check task logger..." )       ;
        
        Iterator<Path> iterator = logFilesList.iterator() ;
        
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

    public String getLoggerFile() {
        return loggerFile         ;
    }

    @Override
    public void setLoggerFile(String loggerFile) {
        this.loggerFile = loggerFile             ;
    }
    
    @Override
    public void setTailDelayMillis(int tailDelayMillis) {
        this.tailDelayMillis = tailDelayMillis          ;
    }
}
