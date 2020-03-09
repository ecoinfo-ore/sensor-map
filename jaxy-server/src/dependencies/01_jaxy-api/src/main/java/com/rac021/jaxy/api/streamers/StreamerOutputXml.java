
package com.rac021.jaxy.api.streamers ;

import java.io.Writer ;
import java.io.IOException ;
import javax.inject.Inject ;
import java.io.OutputStream ;
import java.io.BufferedWriter ;
import java.util.logging.Level ;
import javax.xml.bind.Marshaller ;
import javax.xml.namespace.QName ;
import java.io.OutputStreamWriter ;
import javax.xml.bind.JAXBElement ;
import java.util.concurrent.Future ;
import java.io.ByteArrayOutputStream ;
import java.util.concurrent.TimeUnit ;
import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.jaxy.api.manager.IDto ;
import javax.ws.rs.core.StreamingOutput ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.qualifiers.Format ;
import com.rac021.jaxy.api.manager.IResource ;
import static com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator.* ;

/**
 *
 * @author yahiaoui
 */

@Format(AcceptType.XML_PLAIN)
public class StreamerOutputXml extends Streamer implements StreamingOutput {

    @Inject
    @com.rac021.jaxy.api.qualifiers.MarshallerType("XML")
    Marshaller marshaller ;
   
    public StreamerOutputXml() {
    }

    @Override
    public void write(OutputStream output) throws IOException {

      LOGGER.log(Level.FINE ," Processing data in StreamerOutputXml ... " )         ;
      
      configureStreamer()                                                           ;

      /** Prepare Thread Producers . */
      Future<Long> producers = poolProducer.submit( () -> producerScheduler() )     ;
      
      Writer writer = new BufferedWriter( new OutputStreamWriter(output, "UTF8") )  ;
            
      try ( ByteArrayOutputStream baoStream = new ByteArrayOutputStream() )         {
               
          writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")  ;
          writer.write("\n<Root>") ;
               
          int iteration = 0 ;
           
          while (!isFinishedProcess || !dtos.isEmpty() )              {

            IDto poll = dtos.poll( 30 , TimeUnit.MILLISECONDS  )      ;
                   
            if(poll != null) {
                
              JAXBElement<IDto> je2 = new JAXBElement<>( new QName("Data") , 
                                                         resource.getDto() , 
                                                         poll )            ;
              marshaller.marshal(je2, baoStream)       ;
                       
              writer.write(baoStream.toString("UTF8")) ;
              baoStream.reset()                        ;
              iteration ++                             ;
              
              if (iteration % responseCacheSize == 0 ) {
                  writer.flush() ;
                  iteration = 0  ;
              }
            } 
          }
               
          writer.write("\n</Root>") ;
          writer.write("\n")        ;
          writer.flush()            ;
          
          /** Check Exceptions    */
          producers.get()           ;
           
          
        } catch ( Exception ex )   {
           
           throw new RuntimeException ( ex )                        ;
        }
        finally {
           LOGGER.log( Level.CONFIG, " StreamerOutputXml : CLOSE ") ;
        }
    }

    public ResourceWraper getResource() {
        return resource ;
    }
    
    @Override
    public StreamerOutputXml wrapResource( IResource resource     ,
                                           Class dto             ,
                                           String filteredIndexs ,
                                           MultivaluedMap<String , String> ... sqlParams ) {

      rootResourceWraper( resource, dto, filteredIndexs, sqlParams) ;
      return this                                                   ;
    }
    
    public StreamerOutputXml wrapResource( IResource resource , Class dto ) {

      rootResourceWraper( resource, dto, null ) ;        
      return this                               ;
    }
}

