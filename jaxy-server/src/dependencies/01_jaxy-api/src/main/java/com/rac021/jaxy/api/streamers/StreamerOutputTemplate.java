
package com.rac021.jaxy.api.streamers ;

import java.util.Map ;
import java.util.List ;
import java.io.Writer ;
import java.util.Objects ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.io.BufferedWriter ;
import java.util.logging.Level ;
import java.io.OutputStreamWriter ;
import java.util.concurrent.Future ;
import java.util.concurrent.TimeUnit ;
import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.jaxy.api.manager.IDto ;
import javax.ws.rs.core.StreamingOutput ;
import com.rac021.jaxy.api.security.ISignOn ;
import com.rac021.jaxy.api.qualifiers.Format ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.manager.IResource ;
import static com.rac021.jaxy.api.manager.TemplateManager.* ;
import static com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator.* ;
import static com.rac021.jaxy.api.manager.DtoMapper.extractValuesFromObject ;

/**
 *
 * @author yahiaoui
 */

@Format(AcceptType.TEMPLATE_PLAIN)
public class StreamerOutputTemplate extends Streamer implements StreamingOutput     {

    public StreamerOutputTemplate() {
    }

    @Override
    public void write(OutputStream output) throws IOException {
        
       LOGGER.log(Level.FINE ," Processing data in StreamerOutputTemplate ... ")    ;
       
       configureStreamer()     ;

       /** Submit Producers . */
        Future<Long> producers =  poolProducer.submit( () -> producerScheduler() )  ;      
      
       Writer writer = new BufferedWriter ( new OutputStreamWriter(output, "UTF8")) ;

       String template = 
       servicesManager.getTemplate(ISignOn.SERVICE_NAME.get())               ;
       
       Objects.requireNonNull(template)                                      ;
       
       String templateBody        = removeMultipleBlankSpaces(
                                                 extractBody  ( template ))  ;
      
       String templateHeader      = extractHeader( template )                ;
       
       String templateFooter      = extractFooter( template )                ;
       
       List<String> extractedArgs = extractArgs  ( templateBody )            ;
 
       if ( templateHeader != null && ! templateHeader.trim().isEmpty() )    {
           writer.write( templateHeader + "\n") ;
       }
       
       if( templateBody == null || templateBody.isEmpty() )      {

          writer.write( " \n Empty Template Generated ! \n\n" )  ;
          writer.flush()                                         ;
          writer.close()                                         ;
       } 

       else try  {

            int iteration  =  0                                     ;
            
            while ( !isFinishedProcess || !dtos.isEmpty() )         {

                IDto poll = dtos.poll( 30 , TimeUnit.MILLISECONDS)  ;
                   
                if( poll != null ) {
                    
                    Map<String, String> ext = extractValuesFromObject( poll            , 
                                                                       poll.getClass() , 
                                                                       extractedArgs ) ;
                    
                    writer.write( applyValue( ext, templateBody + "\n" ))    ;
                    
                    iteration ++                                             ;
                      
                    if (iteration % responseCacheSize == 0 )  writer.flush() ;
                }
             }

             if( templateFooter != null && ! templateFooter.isEmpty() ) {
                writer.write( templateFooter + "\n" )                   ;
             }
             
             writer.flush()                                             ;
           
             /** Check Exceptions */
             producers.get()                                            ;           
           
       } catch ( Exception ex )  {
           
           throw new RuntimeException( ex ) ;
        
       } finally {
           LOGGER.log( Level.CONFIG, " StreamerOutputTemplate : CLOSE ")  ;
       }
    }

    public ResourceWraper getResource() {
        return resource ;
    }
 
    @Override
    public StreamerOutputTemplate wrapResource(  IResource resource       , 
                                                 Class     dto            ,
                                                 String    filteredNmames ,
                                                 MultivaluedMap<String, String> ... sqlParams ) {

        rootResourceWraper( resource, dto, filteredNmames, sqlParams ) ;
        return this                                                    ;
    }
    
    public StreamerOutputTemplate wrapResource( IResource resource , Class dto ) {

        rootResourceWraper( resource, dto, null ) ;        
        return this                               ;
    }
}
