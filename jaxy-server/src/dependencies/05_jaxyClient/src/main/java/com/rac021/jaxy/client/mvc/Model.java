
package com.rac021.jaxy.client.mvc ;

import java.io.File ;
import java.util.List ;
import java.io.Writer ;
import java.util.UUID ;
import javax.json.Json ;
import java.util.Objects ;
import java.util.ArrayList ;
import java.net.URLEncoder ;
import java.io.IOException ;
import java.nio.CharBuffer ;
import javax.json.JsonReader ;
import javax.json.JsonString ;
import java.io.BufferedReader;
import java.security.KeyStore ;
import java.util.regex.Pattern ;
import java.util.logging.Level ;
import javax.net.ssl.SSLContext ;
import java.io.FileOutputStream ;
import java.util.logging.Logger ;
import java.io.InputStreamReader ;
import java.security.SecureRandom ;
import org.apache.http.HttpResponse ;
import org.apache.http.nio.IOControl ;
import org.apache.http.NameValuePair ;
import java.security.KeyStoreException ;
import org.apache.http.ssl.SSLContexts ;
import org.apache.http.client.HttpClient ;
import java.nio.charset.StandardCharsets ;
import java.util.concurrent.CountDownLatch ;
import org.apache.http.protocol.HttpContext ;
import java.io.UnsupportedEncodingException ;
import java.security.NoSuchAlgorithmException ;
import org.apache.http.client.methods.HttpGet ;
import java.security.cert.CertificateException ;
import org.apache.http.client.methods.HttpPost ;
import com.rac021.jaxy.client.security.Digestor ;
import com.rac021.jaxy.client.security.ICryptor ;
import org.apache.http.conn.ssl.TrustAllStrategy ;
import org.apache.http.concurrent.FutureCallback ;
import org.apache.http.message.BasicNameValuePair ;
import com.rac021.jaxy.client.security.EncDecRyptor ;
import com.rac021.jaxy.client.security.FactoryCipher ;
import org.apache.http.impl.client.HttpClientBuilder ;
import org.apache.http.impl.nio.client.HttpAsyncClients ;
import org.apache.http.client.entity.UrlEncodedFormEntity ;
import org.apache.http.nio.client.methods.HttpAsyncMethods ;
import org.apache.http.nio.client.methods.AsyncCharConsumer ;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer ;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient ;

/**
 *
 * @author ryahiaoui
 */

public class Model {
    
    static         KeyStore                        keystore             ; 
    static         char[]                          password             ;
    
    private static CloseableHttpAsyncClient        httpclient           ;
    
    private static HttpAsyncRequestProducer        producer             ;
    
    private static AsyncCharConsumer<HttpResponse> consumer             ;
            
    private static final  int                      BUF_SIZE = 5000      ;
             
    private static final  String CERTIFICATE_FILE_NAME = "jaxy_cacerts" ;
    
    static {
        
           /** Initialise KeyStore for SSL Connections .        */
        
           try {
               
                String key      = UUID.randomUUID().toString()    ;
                
                File f = new File(CERTIFICATE_FILE_NAME)          ;
                if( ! f.exists() ) f.createNewFile()              ;
                
                keystore = KeyStore.getInstance(KeyStore.getDefaultType()) ;

                password = key.toCharArray()   ;
                keystore.load(null, password ) ;

                /** Store away the keystore. **/
               try ( FileOutputStream fos = 
                       new FileOutputStream( CERTIFICATE_FILE_NAME ) ) {
                     keystore.store( fos , password )       ;
               }

                keystore  = KeyStore.getInstance( KeyStore
                                    .getDefaultType() )     ;
                
           } catch( IOException | KeyStoreException |
                    NoSuchAlgorithmException        |
                    CertificateException ex  )      {
               System.out.println( ex )             ;
           }
    }

    private static SSLContext getSslContext()       {
        
        try {
             return  SSLContexts.custom()
                                .loadTrustMaterial( new File(CERTIFICATE_FILE_NAME) , 
                                                    password                        ,
                                                    new TrustAllStrategy()          ) 
                                .setSecureRandom(new SecureRandom() )
                                .build()   ; 
        } catch( Exception ex )            {
            throw new RuntimeException(ex) ;
        }
    }

    public Model() {
    }
    
    private static void initHttpClient() throws IOException  {
        
        if( httpclient != null &&  httpclient.isRunning() )  {
           closetHttpClient() ;
        }
        
        httpclient = HttpAsyncClients.createDefault() ;
        httpclient.start()                            ;
    }
    
    private static void initHttpsClient() throws Exception   {
        
        if( httpclient != null &&  httpclient.isRunning() )  {
           closetHttpClient() ;
        }

        httpclient = HttpAsyncClients.custom()
                                     .setSSLContext(getSslContext())
                                     .build()        ;
       httpclient.start()                            ;
    }
     
    private static void closetHttpClient()  {
    
      try {
            if( httpclient != null )    {
                httpclient.close()      ;
            }
            if( producer != null )      {
                producer.resetRequest() ;
                producer.close()        ;
            } 
            if( consumer != null )      {
               consumer.cancel()        ;
               consumer.close()         ;
            }
      }
      catch (IOException ex) {
          Logger.getLogger( Model.class.getName()).log(Level.SEVERE , 
                            null, ex                              ) ;
      }
    }
    
    private static String getToken( String url           , 
                                    String username      ,
                                    String password      , 
                                    String client_id     , 
                                    String client_secret 
                                    ) throws Exception   {        
     
        HttpClient client                          ;
       
        if( url.toLowerCase().startsWith("https")) {
            
              client = HttpClientBuilder.create().setSSLContext(getSslContext()).build() ; 
        } else {
             client = HttpClientBuilder.create().build() ; 
        }
       
        HttpPost post     = new HttpPost(url)            ;
  
	List<NameValuePair> params = new ArrayList<>()                     ;
	params.add(new BasicNameValuePair("grant_type"   , "password"))    ;
	params.add(new BasicNameValuePair("username"     , username))      ;
	params.add(new BasicNameValuePair("password"     , password))      ;
	params.add(new BasicNameValuePair("client_id"    , client_id))     ;
	params.add(new BasicNameValuePair("client_secret", client_secret)) ;

	post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"))          ;
        
	HttpResponse response = client.execute(post)                       ;
      
        int statusCode        = response.getStatusLine().getStatusCode()   ;
        
        if ( statusCode == 200 ) {
            
               InputStreamReader bodyResponse  = getKeyCloakResponse(response)  ;
               
               try ( JsonReader jsonReader = Json.createReader( bodyResponse) ) {
                   
                     JsonString jsonString = jsonReader.readObject()
                                                       .getJsonString("access_token") ;
                    
                    if( jsonString == null ) {
                        System.out.println(" BAD AUTHENTICATION ! ") ;
                        return null                                  ;
                    }
                    else {
                        return jsonString.getString()                ;
                    }
               }
        }
        else {
          return  "_ERROR_:" + getKeyCloakResponseAsString(response) ;
        }
    }    
    
    private static InputStreamReader getKeyCloakResponse( HttpResponse response ) throws IOException {
    
        return  new InputStreamReader(response.getEntity().getContent())  ;
    }
    
    private static String getKeyCloakResponseAsString( HttpResponse response ) throws IOException    {
    
        StringBuilder result = new StringBuilder()    ;
        
        try ( BufferedReader rd = new BufferedReader  (
	      new InputStreamReader(response.getEntity().getContent())) ) {
            
            String line = "";
            while (( line = rd.readLine() ) != null) {
                    result.append(line)              ;
            }
        }
        return result.toString() ;
        
    }
    
    private static String buildUrlWithParams( String url, String params )  {
        
        if( url == null || url.trim().isEmpty() ) return null  ;
        
        String _url = url.trim() ;

        if( params != null && ! params.trim().isEmpty())                   {
            _url = encodeUrlExceptEqualAndAmpersand ( url + "?" + params ) ;
        } else {
            _url = encodeUrlExceptEqualAndAmpersand ( url )                ;
        }

        return _url                                            ;
        
    }
    
    public static String invokeService_Using_SSO ( Writer out                ,
                                                   String url                ,
                                                   String urlKeycloak        ,
                                                   String params             ,
                                                   String accept             ,
                                                   String keep               ,
                                                   String keycloak_client_id ,
                                                   String keycloak_secret_id ,
                                                   String keycloak_login     ,
                                                   String keycloak_password  ) throws Exception {
       
       String token = getToken ( urlKeycloak        , 
                                 keycloak_login     ,
                                 keycloak_password  , 
                                 keycloak_client_id , 
                                 keycloak_secret_id ) ;

       if ( token == null || token.isEmpty() || token.startsWith("_ERROR_:")) {
           
           System.out.println("                               ")  ;
           System.out.println(" Keycloak Error Authentication ")  ;
           System.out.println(" Reason --> "   + token         )  ;
           System.out.println("                               ")  ;
           System.exit(0)                                         ;
       }
       
       String _url = buildUrlWithParams( url, params )            ;
        
       final HttpGet request = new HttpGet( _url )                ;

       request.addHeader( "Accept", accept )                      ;
       request.addHeader( "Keep", keep  )                         ;
       
       request.addHeader( "Authorization",  " Bearer " + token  ) ;
       
       invoker( _url.split("://")[0] , out , request )            ;
       return "Done ! "                                           ;  
    }
     
    public static void invokeService_Using_Public ( Writer out        ,
                                                    String serviceUrl ,
                                                    String params     ,
                                                    String accept     , 
                                                    String keep       ) throws Exception {
        
        Objects.requireNonNull(out)                            ;

        String _url = buildUrlWithParams( serviceUrl, params ) ;

        final HttpGet request = new HttpGet( _url )            ;

        request.addHeader( "Accept", accept )                  ;
        request.addHeader( "Keep", keep  )                     ;
        
        invoker( _url.split("://")[0] , out , request )        ;
    }
	
    public static void invokeService_Using_Custom ( Writer out        ,
                                                    String serviceUrl ,
                                                    String params     ,
                                                    String accept     , 
                                                    String token      ,
                                                    String keep       ,
                                                    String cipher     ) throws Exception {
        
        Objects.requireNonNull(out)                            ;

        String _url = buildUrlWithParams( serviceUrl, params ) ;

        final HttpGet request = new HttpGet( _url )            ;

        request.addHeader( "Accept", accept)                   ;
        request.addHeader( "API-key-Token", token.trim() )     ;
        request.addHeader( "Keep", keep  )                     ;
        
        if( cipher != null ) {
            request.addHeader( "Cipher", cipher )              ;
        }

        invoker( _url.split("://")[0] , out , request )        ;
    }
    
    
    private static void invoker(  String transport      ,
                                  final Writer  out     , 
                                  final HttpGet request ) throws Exception {
        
        final CountDownLatch latch = new CountDownLatch(1) ;

        if( transport.equalsIgnoreCase("https"))           {
            initHttpsClient()                              ;
        } else                                             {
            initHttpClient()                               ;
        }

        consumer = new AsyncCharConsumer<HttpResponse>(BUF_SIZE) {

            HttpResponse response;

            @Override
            protected void onResponseReceived(final HttpResponse response) {  
                this.response = response ;
            }

            @Override
            protected void onCharReceived( final CharBuffer buf, 
                                           final IOControl ioctrl ) throws IOException           {
                out.write( new String ( buf.toString().getBytes( StandardCharsets.ISO_8859_1 ) , 
                           StandardCharsets.UTF_8                                            ) ) ;
                out.flush() ;
                buf.clear() ;
            }

            @Override
            protected void releaseResources()            {
            }

            @Override
            protected HttpResponse buildResult(final HttpContext context) throws IOException  {
              if( this.response.getStatusLine().getStatusCode() == 404 ) {
                   out.write(this.response.toString())                   ;
                   out.flush()                                           ;
              }
              return this.response ;
            }

        } ;
        
        producer = HttpAsyncMethods.create(request) ;

        httpclient.execute ( producer, consumer, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(final HttpResponse response3) {
                latch.countDown();
            }

            @Override
            public void failed(final Exception ex)  {
                ex.printStackTrace()           ;
                latch.countDown()              ;
                throw new RuntimeException(ex) ;
            }

            @Override
            public void cancelled() {
                latch.countDown()   ;
            }
            
        } ) ;

        latch.await()      ;
        out.flush()        ;
        
        closetHttpClient() ;
    }

    public static void decrypt ( Writer out    ,
                                 String cipher , 
                                 String pass   ,
                                 String text   ) throws Exception {

        ICryptor crypt = FactoryCipher.getCipher( cipher , pass ) ;

        crypt.setOperationMode(EncDecRyptor._Operation.Decrypt )  ;

        out.write( new String ( crypt.process ( text , 
                                EncDecRyptor._CipherOperation.dofinal ) )) ;
        out.flush()                                                        ;
    }

    public static String hashMessage( String message, String hashAlog ) throws NoSuchAlgorithmException   {
        if( message == null || hashAlog == null ) return null ;
        if(hashAlog.equalsIgnoreCase("SHA1")) return Digestor.toString( Digestor.toSHA1(message))   ;
        if(hashAlog.equalsIgnoreCase("SHA2")) return Digestor.toString( Digestor.toSHA256(message)) ;
        if(hashAlog.equalsIgnoreCase("MD5"))  return Digestor.toString( Digestor.toMD5(message))    ;
        // Else PLAIN
        return message ;
    }
    
    
    private static String encodeUrlExceptEqualAndAmpersand(String fullUrl ) {
        
       if( ! fullUrl.contains("=")) return fullUrl                          ;
       
       try {
            /**  Before Encoding, Change =    to *..*  . */
            /**  Before Encoding, Change &    to ...   . */
            /**  After  Encoding, change *..* to =     . */
            /**  After  Encoding, change ...  to &     . */
             
            String url    = fullUrl.split(Pattern.quote("?"), 2) [0]        ;
            String params = fullUrl.split(Pattern.quote("?"), 2) [1]        ;
             
            return 
               url + "?" +
               URLEncoder.encode( params.trim() 
                                        .replaceAll(" +", " "  )
                                        .replaceAll("&", "..." )
                                        .replaceAll("=", "*..*") , "UTF-8"  )
                         .replaceAll(Pattern.quote("*..*"), "=") 
                         .replaceAll(Pattern.quote("...") , "&" ) ;
            
        } catch( UnsupportedEncodingException ex ) {
           Logger.getLogger( Model.class.getName())
                                        .log(Level.SEVERE, null, ex )       ;
        }
       
       return fullUrl ;
    }

}
