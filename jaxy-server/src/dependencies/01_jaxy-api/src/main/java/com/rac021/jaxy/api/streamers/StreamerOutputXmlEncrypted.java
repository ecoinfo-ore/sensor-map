
package com.rac021.jaxy.api.streamers ;

import java.io.Writer ;
import java.util.Queue ;
import java.util.Arrays ;
import java.util.Base64 ;
import javax.inject.Inject ;
import java.io.IOException ;
import java.io.OutputStream ;
import java.util.LinkedList ;
import java.io.BufferedWriter ;
import java.util.logging.Level ;
import javax.xml.namespace.QName ;
import javax.xml.bind.Marshaller ;
import java.io.OutputStreamWriter ;
import javax.xml.bind.JAXBElement ;
import java.util.concurrent.Future ;
import java.util.concurrent.TimeUnit ;
import java.io.ByteArrayOutputStream ;
import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.jaxy.api.manager.IDto ;
import javax.ws.rs.core.StreamingOutput ; 
import java.nio.charset.StandardCharsets ;
import com.rac021.jaxy.api.crypto.ICryptor ;
import javax.ws.rs.WebApplicationException ;
import org.apache.commons.lang3.ArrayUtils ;
import com.rac021.jaxy.api.security.ISignOn ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.manager.IResource ;
import com.rac021.jaxy.api.qualifiers.Format ;
import com.rac021.jaxy.api.crypto.EncDecRyptor ;
import com.rac021.jaxy.api.crypto.FactoryCipher;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import static com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator.* ;

/**
 *
 * @author yahiaoui
 */

@Format(AcceptType.XML_ENCRYPTED)
public class StreamerOutputXmlEncrypted extends Streamer implements StreamingOutput {

    @Inject
    @com.rac021.jaxy.api.qualifiers.MarshallerType("XML")
    Marshaller marshaller ;
    
    public StreamerOutputXmlEncrypted() { }

    @Override
    
    public void write( OutputStream output ) throws IOException {

      LOGGER.log(Level.FINE ," Processing data in StreamerOutputXmlEncrypted ... ") ;
     
      if( ISignOn.ENCRYPTION_KEY.get() == null ) {
         LOGGER.log(Level.SEVERE, " Error : Key can't be NULL " )                   ;
         throw new WebApplicationException(" Error Key can't be NULL " )            ;
      }
      
      configureStreamer()                              ;

      /** Submit Producers . */
      Future<Long> producers =  poolProducer.submit( () -> producerScheduler() )   ; 
      
      Writer writer  = new BufferedWriter( new OutputStreamWriter(output, "UTF8")) ;
      
      ICryptor crypt = null ;
      
      try {
          
        crypt = FactoryCipher.getCipher ( ISignOn.CIPHER.get()           , 
                                          ISignOn.ENCRYPTION_KEY.get() ) ;
      } catch( BusinessException ex ) {
          
          LOGGER.log(Level.SEVERE, ex.getMessage(), ex ) ; 
          
          writer.write(" Exception  : Something went wrong  // " + ex.getMessage()       ) ;
          writer.write(" \n" ) ;  
          writer.write(" MediaType  : XML/ENCRYPTED "                                    ) ;
          writer.write(" \n" ) ;
          writer.write(" Specify accept header in the Request if it's not already done " ) ;
          writer.write(" \n" ) ;
          writer.flush()       ;
          writer.close()       ;
      }
      
      if( crypt == null ) throw new RuntimeException(" CRYPT CAN'T BE NULL ") ;
      
      crypt.setOperationMode(EncDecRyptor._Operation.Encrypt ) ;
        
      /** Send in the reponse Vector Inititialization . */
      if( crypt.getIvBytesEncoded64() != null ) {
          writer.write(new String( crypt.getIvBytesEncoded64(), StandardCharsets.UTF_8 ) + "." ) ;
          writer.flush() ;
      }
      
      Queue<Byte>         qeueBytes        = new LinkedList<>()          ;
      StringBuilder       plainTextBuilder = new StringBuilder()         ;
      
      int nbrBlocks = 0   ;

      try ( ByteArrayOutputStream baoStream  = new ByteArrayOutputStream() ;
            ByteArrayOutputStream outString  = new ByteArrayOutputStream() ; ) {
          
            baoStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                     .getBytes(StandardCharsets.UTF_8))                   ;
            baoStream.write("\n<Root>".getBytes(StandardCharsets.UTF_8))  ;

            int iteration = 0 ;
            
            while ( ! isFinishedProcess || ! dtos.isEmpty() )     {

               IDto poll = dtos.poll( 30, TimeUnit.MILLISECONDS ) ;
                    
               if( poll != null ) {
                       
                  JAXBElement<IDto> je2 = new JAXBElement<>( new QName("Data") , 
                                                             resource.getDto() , 
                                                             poll)             ;
                  marshaller.marshal(je2 , baoStream)           ;

                  plainTextBuilder.append(baoStream.toString()) ;

                  baoStream.reset()                             ;
                        
                  iteration ++                                  ;
                        
                  if (iteration % responseCacheSize == 0)       {
                      
                      nbrBlocks = (plainTextBuilder.length() / blockSize) ;

                      if ((plainTextBuilder.length() % blockSize > 0) && (nbrBlocks >= 1)) {

                           qeueBytes.addAll ( Arrays.asList ( ArrayUtils.toObject ( 
                                                              crypt.process ( plainTextBuilder
                                                                   .substring( 0 , 
                                                                               nbrBlocks * blockSize) ,
                                                              EncDecRyptor._CipherOperation.update ) )
                           )) ;
                           
                           plainTextBuilder.delete(0, nbrBlocks * blockSize) ;
                                
                      } else if (nbrBlocks > 1) {
                          
                           qeueBytes.addAll(Arrays.asList ( ArrayUtils.toObject ( 
                                                            crypt.process ( plainTextBuilder
                                                                 .substring( 0 , 
                                                                            (nbrBlocks - 1 ) * blockSize ) , 
                                   EncDecRyptor._CipherOperation.update ) )
                           )) ;

                           plainTextBuilder.delete(0, ( nbrBlocks - 1 ) * blockSize ) ;
                      }

                      while ((qeueBytes.size() / 3) >= 1 )   {
                          
                          outString.write(qeueBytes.poll())  ;
                          outString.write(qeueBytes.poll())  ;
                          outString.write(qeueBytes.poll())  ;
                      }
                         
                      writer.write( new String(Base64.getEncoder()
                                                     .encode(outString
                                                     .toByteArray())) ) ;
                      writer.flush()    ;
                      baoStream.reset() ;
                      outString.reset() ;
                      iteration = 0     ;
                  }
               }
            }

            plainTextBuilder.append(baoStream.toString()) ;
            
            qeueBytes.addAll(Arrays.asList(ArrayUtils.toObject ( crypt
                                                     .process( ( plainTextBuilder
                                                     .append("\n</Root>").append("\n"))
                                                     .toString() , 
                                                      EncDecRyptor. _CipherOperation.dofinal) )
            )) ;
            
            while ( ! qeueBytes.isEmpty() )      {
               outString.write(qeueBytes.poll()) ;
            }

            writer.write ( new String(Base64.getEncoder().encode(outString.toByteArray())))   ;

            writer.flush()         ;
            
            /** Check Exception **/
            producers.get()        ;
            
        } catch ( Exception ex )  {
            
          throw new RuntimeException( ex )                                     ;
        }
        
        finally {
           LOGGER.log( Level.CONFIG, " StreamerOutputXmlEncrypted : CLOSE " )  ;
           ISignOn.ENCRYPTION_KEY.remove() ;
           ISignOn.CIPHER.remove()         ;
           plainTextBuilder.setLength(0)   ;
        }       
    }

    public ResourceWraper getResource() {
        return resource ;
    }

    @Override
    public StreamerOutputXmlEncrypted wrapResource( IResource resource    ,
                                                    Class dto             ,
                                                    String filteredIndexs ,
                                                    MultivaluedMap <String, String> ... sqlParams ) {

      rootResourceWraper( resource, dto, filteredIndexs, sqlParams) ;
      return this                                                   ;
    }

    public StreamerOutputXmlEncrypted wrapResource( IResource resource , Class dto ) {

      rootResourceWraper( resource, dto, null ) ;        
      return this                               ;
    }

}

