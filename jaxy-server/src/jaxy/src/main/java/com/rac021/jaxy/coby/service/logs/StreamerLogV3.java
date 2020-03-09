
package com.rac021.jaxy.coby.service.logs ;

import java.util.Set ;
import java.io.Writer ;
import java.util.HashSet;
import javax.ejb.Schedule;
import java.util.Iterator;
import java.nio.file.Path;
import java.nio.file.Files;
import javax.ejb.Stateless;
import java.nio.file.Paths;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.BufferedWriter ;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Future;
import java.io.OutputStreamWriter ;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;
import javax.ws.rs.core.StreamingOutput ;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.input.Tailer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 *
 * @author yahiaoui
 */

@Stateless
public class StreamerLogV3  implements StreamingOutput , LoggerTask {

    private String  loggerFile      = null          ;
    
    private int     tailDelayMillis = 300           ;
    
    static Set<Path> logFilesList = new HashSet<>() ;
   
    private static ExecutorService executorService = Executors.newFixedThreadPool(5) ;
    
    public StreamerLogV3( String logger_file    ,
                          int tailDelayMillis ) {
        this.loggerFile       = logger_file     ;
        this.tailDelayMillis  = tailDelayMillis ;
    }
    
    public StreamerLogV3( ) { }

    public String getLoggerFile() {
        return loggerFile ;
    }

    @Override
    public void setLoggerFile(String loggerFile) {
        this.loggerFile = loggerFile ;
    }
    @Override
    public void setTailDelayMillis(int tailDelayMillis) {
        this.tailDelayMillis = tailDelayMillis ;
    }
    
    public int getTailDelayMillis() {
        return tailDelayMillis ;
    }
    
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
           tail = executorService.submit(tailer) ;
           tail.get( 7 , TimeUnit.DAYS )         ;
           
       } catch ( InterruptedException | ExecutionException | TimeoutException ex )     {

           Logger.getLogger(StreamerLogV3.class.getName()).log(Level.SEVERE, null, ex) ;
         
       } finally {
           
           System.out.println(" Close Connection... ") ;
           if( tailer != null ) tailer.stop()          ;
           if( tail   != null ) tail.cancel(true)      ;
       }
        
       System.out.println(" Service LOG Closed ")      ;
    }

    @Schedule(persistent = false, second = "*/30", minute = "*", hour = "*", info = " Coby Logs Connections Checker " )  
    @TransactionTimeout( value = 1, unit = TimeUnit.DAYS )
    public void checkTaskLogger() throws Exception        {
        
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
}
