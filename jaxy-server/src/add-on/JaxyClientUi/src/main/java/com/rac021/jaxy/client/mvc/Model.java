
package com.rac021.jaxy.client.mvc ;

import java.io.File ;
import java.util.List ;
import java.util.UUID ;
import javax.json.Json ;
import java.awt.Toolkit ;
import java.util.Objects ;
import java.util.ArrayList ;
import java.net.URLEncoder ;
import java.io.IOException ;
import java.nio.CharBuffer ;
import javax.json.JsonReader ;
import javax.json.JsonString ;
import java.security.KeyStore ;
import java.io.BufferedReader ;
import java.util.regex.Pattern ;
import java.util.logging.Level ;
import java.io.FileOutputStream ;
import java.util.logging.Logger ;
import java.io.InputStreamReader ;
import org.apache.http.HttpResponse ;
import org.apache.http.NameValuePair ;
import org.apache.http.nio.IOControl ;
import java.security.KeyStoreException ;
import java.awt.datatransfer.Clipboard ;
import org.apache.http.client.HttpClient ;
import java.nio.charset.StandardCharsets ;
import java.util.concurrent.CountDownLatch ;
import java.io.UnsupportedEncodingException ;
import org.apache.http.protocol.HttpContext ;
import java.awt.datatransfer.StringSelection ;
import org.apache.http.client.methods.HttpGet ;
import java.security.NoSuchAlgorithmException ;
import java.security.cert.CertificateException ;
import org.apache.http.client.methods.HttpPost ;
import com.rac021.jaxy.client.security.ICryptor ;
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
    
    static KeyStore keystore                                 ;
    
    private static CloseableHttpAsyncClient httpclient       ;
    
    private static HttpAsyncRequestProducer producer         ;
    
    private static AsyncCharConsumer<HttpResponse> consumer  ;
            
    private static int BUF_SIZE = 6000                       ;
             
    static {
        
           /** Initialise KeyStore for SSL Connections . */
        
           try {
               
                String key      = UUID.randomUUID().toString()             ;
                String filename = "jaxy_client_cacerts.jks"                ;
                
                File f = new File(filename)                                ;
                if( ! f.exists() ) f.createNewFile()                       ;
                
                keystore = KeyStore.getInstance(KeyStore.getDefaultType()) ;

                char[] password = key.toCharArray() ;
                keystore.load(null, password )      ;

                /** Store away the keystore. **/
               try ( FileOutputStream fos = new FileOutputStream(filename) ) {
                   keystore.store(fos, password )        ;
               }

                keystore  = KeyStore.getInstance( KeyStore
                                    .getDefaultType() )  ;
                
           } catch( IOException              | KeyStoreException       | 
                    NoSuchAlgorithmException | CertificateException ex )     {
               System.out.println( ex ) ;
           }
    }
    
    public Model() {
    }
        
    
    private static void initHttpClient()   {
        
        if( httpclient != null      && 
            httpclient.isRunning() ) {
           closetHttpClient()        ;
        }
        
        httpclient = HttpAsyncClients.createDefault() ;
        httpclient.start()                            ;
    }
     
    private static void closetHttpClient()  {
    
      if( httpclient != null )   {
          try {
              httpclient.close()      ;
              producer.resetRequest() ;
              producer.close()        ;
              consumer.cancel()       ;
              consumer.close()        ;
          } catch (IOException ex)    {
              Logger.getLogger( Model.class.getName()) 
                                     .log(Level.SEVERE, null, ex) ;
          }
      }
    }
     
    public static String getToken(  String url           , 
                                    String username      ,
                                    String password      , 
                                    String client_id     , 
                                    String client_secret 
                                    ) throws Exception   {        
     
            
      HttpClient client = HttpClientBuilder.create().build() ; 
 
      HttpPost post     = new HttpPost(url)                  ;
  
	List<NameValuePair> params = new ArrayList<>();
	params.add(new BasicNameValuePair("grant_type", "password"))       ;
	params.add(new BasicNameValuePair("username", username))           ;
	params.add(new BasicNameValuePair("password", password))           ;
	params.add(new BasicNameValuePair("client_id", client_id))         ;
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
     
    private static String getKeyCloakResponseAsString( HttpResponse response ) throws IOException {
    
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
     
    public static String invokeService_Using_SSO ( IOutputWraper out         ,
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
       
       invocker(out, request )                                    ;
       
       return "Done ! "                                           ;  
    }
         
    public static void invokeService_Using_Custom ( IOutputWraper out ,
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
        request.addHeader( "Cipher", cipher )                  ;

        invocker(out, request )                                ;
    }
    
    private static void invocker(  final IOutputWraper  out    , 
                                   final HttpGet request ) throws Exception {
        
        final CountDownLatch latch = new CountDownLatch(1) ;

        initHttpClient()                                   ;

        consumer = new AsyncCharConsumer<HttpResponse>(BUF_SIZE) {

            HttpResponse response ;

            @Override
            protected void onResponseReceived(final HttpResponse response) {  
                this.response = response ;
            }

            @Override
            protected void onCharReceived( final CharBuffer buf, final IOControl ioctrl) throws IOException {
                out.write( new String ( buf.toString().getBytes( StandardCharsets.ISO_8859_1 ) , 
                           StandardCharsets.UTF_8                                            ) ) ;
                buf.clear() ;
            }

            @Override
            protected void releaseResources() {
            }

            @Override
            protected HttpResponse buildResult(final HttpContext context) throws IOException  {
              if( this.response.getStatusLine().getStatusCode() == 404 ) {
                   out.write(this.response.toString())                   ;
              }
              return this.response ;
            }

        } ;
        
        producer = HttpAsyncMethods.create(request) ;

        httpclient.execute ( producer, consumer, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(final HttpResponse response3) {
                latch.countDown() ;
            }

            @Override
            public void failed(final Exception ex)  {  
                latch.countDown()              ;
                throw new RuntimeException(ex) ;
            }

            @Override
            public void cancelled() {
                latch.countDown()   ;
            }
            
        } ) ;

        latch.await()      ;
        
        closetHttpClient() ;
    }
    
    private static String buildUrlWithParams( String url, String params )  {
        
        if( url == null || url.trim().isEmpty() ) return null  ;
        
        String _url = url.trim() ;

        if( params != null && ! params.trim().isEmpty())                   {
            _url = encodeUrlExceptEqualAndAmpersand ( url + "?" + params ) ;
        } else {
            _url = encodeUrlExceptEqualAndAmpersand ( url )                ;
        }

        return _url                                                        ;
        
    }
    
    private static String getBlanc( int nbr ) {
        String blanc = " " ;
        for(int i = 0 ; i < nbr ; i++ ) {
            blanc += " "   ;
        }
        return blanc       ;
    }
    
    public static String generateScriptSSO( String keyCloakUrl , 
                                            String userName    , 
                                            String password    , 
                                            String client_id   , 
                                            String secret_id   , 
                                            String keep        , 
                                            String url         , 
                                            String params      , 
                                            String accept ) throws UnsupportedEncodingException    {
        
        String _url = url.trim() ;        
        
        params      = URLEncoder.encode( params.trim(), "UTF-8") ;

        String trustCertkeyCloakUrl = keyCloakUrl.trim().startsWith("https") ? " -k " : " " ;
        String trustCertUrl         = _url.startsWith("https") ? " -k " : " " ;
         
        String KEYCLOAK_RESPONSE = " KEYCLOAK_RESPONSE=`curl "
                                   + trustCertkeyCloakUrl
                                   + "-s -X POST " + keyCloakUrl  + " \\\n " 
                                   + getBlanc(50) + " -H \"Content-Type: application/x-www-form-urlencoded\" \\\n " 
                                   + getBlanc(50) + " -d 'username=" + userName + "' \\\n "
                                   + getBlanc(50) + " -d 'password=" + password + "' \\\n "
                                   + getBlanc(50) + " -d 'grant_type=password' \\\n "
                                   + getBlanc(50) + " -d 'client_id=" + client_id + "' \\\n "
                                   + getBlanc(50) + " -d 'client_secret=" + secret_id + "' ` \n " ;
                 
        String _token = " ACCESS_TOKEN=`echo $KEYCLOAK_RESPONSE | "          + 
                        "sed 's/.*access_token\":\"//g' | sed 's/\".*//g'` " ;
               
        String invokeService =   " curl "        +
                                 trustCertUrl    +
                                 "-H \"Accept: " + 
                                 accept + "\"  " + 
                                 " -H \"Authorization: Bearer $ACCESS_TOKEN\" " ;
               
        if( keep != null && ! keep.isEmpty() ) {
             invokeService += " -H \"Keep: " + keep + " \" " ;
        }
       
        if( params != null && ! params.isEmpty() )
           _url += "?" + params ;
        
        invokeService += "\"" + _url + "\" " ;
               
        return  "# !/bin/bash"  + "\n\n "               + 
                "# Script generated by G-JAX-CLIENT \n" +
                "# Author : ---    \n\n\n "             +  
                " # INVOKE KEYCLOAD ENDPOINT \n "       + 
                KEYCLOAK_RESPONSE + "\n\n "             + 
                " # PARSE TOKEN FROM RESPONSE \n "      + 
                _token + " \n\n "                       + 
                "# INVOKE THE WEB SERVICE \n "          + 
                invokeService                           ;
    }
    
    public static String decrypt( String cipher , String pass ,String text ) throws Exception {

        ICryptor crypt = FactoryCipher.getCipher( cipher , pass ) ;

        crypt.setOperationMode(EncDecRyptor._Operation.Decrypt )  ;

        return new String ( crypt.process( text, EncDecRyptor._CipherOperation.dofinal ) ) ;
    }
    
    public static String generateScriptCUSTOM ( String url           , 
                                                String login         , 
                                                String password      , 
                                                String params        , 
                                                String keep          ,
                                                String accept        ,
                                                String hashLogin     , 
                                                String hashPassword  , 
                                                String hashTimeStamp ,
                                                String algoSign      ,
                                                String cipher      ) throws UnsupportedEncodingException {
            
        String _url =  url.trim() ;
        
        params  = URLEncoder.encode( params.trim(), "UTF-8") ;
  
        if( params != null && ! params.isEmpty() )
           _url += "?" + params ;
        
        String trustCert = _url.trim().startsWith("https") ? " -k " : " " ;
        
        String invokeService =  " curl "        +
                                trustCert       +
                                "-H \"Accept: " +
                                accept + "\"  " ;
               
        if( keep != null && ! keep.isEmpty() ) {
          invokeService += " -H \"keep: " + keep + " \" " ;
        }
        
        if( cipher != null && ! cipher.isEmpty() ) {
          invokeService += " -H \"Cipher: " + cipher + " \" " ;
        }
        
        return    " # !/bin/bash \n\n" 
                + " # Script generated by G-JAX-CLIENT \n" 
                + " # Author : ---                \n\n\n " 
                + " Login=\""     + login    + "\"  \n\n " 
                + " Password=\""  + password + "\"  \n\n "
                + " TimeStamp=$(date +%s)           \n\n " 
                + getHashedScript( "Login"     , hashLogin     ) + "\n\n " 
                + getHashedScript( "Password"  , hashPassword  ) + "\n\n " 
                + getHashedScript( "TimeStamp" , hashTimeStamp ) + "\n\n "  
                + getSigneScript( algoSign )                     +  "\n\n " 
                + invokeService
                + "-H \"API-key-Token: " + "$Login $TimeStamp $SIGNE\" "  
                + "\"" +_url.replaceAll(" ", "%20") + "\"" ;
    }

    private static String getHashedScript( String variable, String algo ) {
      
        if(algo.equalsIgnoreCase("SHA1")) {
          return " Hashed_"                            + 
                 variable.trim()                       + 
                 "=` echo -n $"                        + 
                 variable.trim()                       + 
                 " | sha1sum  | cut -d ' ' -f 1 ` \n"  + 
                 "  Hashed_"                           + 
                 variable.trim()                       + 
                 "=` echo $Hashed_"                    + 
                 variable.trim()                       + 
                 " | sed 's/^0*//'`"                   ;
        }
        if(algo.equalsIgnoreCase("SHA2")) {
          return " Hashed_"                             + 
                 variable.trim()                        + 
                 "=` echo -n $"                         + 
                 variable.trim()                        + 
                 " | sha256sum  | cut -d ' ' -f 1 ` \n" + 
                 "  Hashed_"                            + 
                 variable.trim()                        + 
                 "=` echo $Hashed_"                     + 
                 variable.trim()                        + 
                 " | sed 's/^0*//'`"                    ;
        }
        else if(algo.equalsIgnoreCase("MD5")) {
           return " Hashed_"                           + 
                  variable.trim()                      + 
                  "=` echo -n $"                       + 
                  variable.trim()                      + 
                  " | md5sum  | cut -d ' ' -f 1` \n"   + 
                  "  Hashed_" + variable.trim()        +
                  "=` echo $Hashed_" + variable.trim() +
                  " | sed 's/^0*//'`" ;
        }
        
        return " Hashed_" + variable.trim() + "=\"$" + variable.trim() + "\""  ;
    }
    
    private static String getSigneScript( String algo ) {
      
        if(algo.equalsIgnoreCase("SHA1"))  {
          return " SIGNE=`echo -n $Hashed_Login$Hashed_Password$Hashed_TimeStamp" + 
                  " | sha1sum  | cut -d ' ' -f 1 ` \n "                           + 
                  " SIGNE=` echo $SIGNE | sed 's/^0*//' ` "                       ; 
        }
        if(algo.equalsIgnoreCase("SHA2"))  {
          return " SIGNE=`echo -n $Hashed_Login$Hashed_Password$Hashed_TimeStamp" + 
                  " | sha256sum  | cut -d ' ' -f 1 ` \n "                         + 
                  " SIGNE=` echo $SIGNE | sed 's/^0*//' ` "                       ; 
        }
        else if(algo.equalsIgnoreCase("MD5")) {
          return " SIGNE=`echo -n $Hashed_Login$Hashed_Password$Hashed_TimeStamp " + 
                 "| md5sum  | cut -d ' ' -f 1 ` \n "                               +
                 " SIGNE=` echo $SIGNE | sed 's/^0*//' ` "                         ;
        }
        return " SIGNE=`echo -n $Hashed_Login$Hashed_Password$Hashed_TimeStamp ` " ;
    }
    
    public static void copyToClipBoard( String text )                         {
        StringSelection stringSelection = new StringSelection (text)          ;
        Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard () ;
        clpbrd.setContents (stringSelection, null)                            ;
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
