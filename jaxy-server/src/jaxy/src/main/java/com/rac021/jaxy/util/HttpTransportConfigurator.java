
package com.rac021.jaxy.util ;

import static com.rac021.jaxy.messages.Displayer.display ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.messages.Displayer.displayLine ;
import com.rac021.jaxy.ee.util.letsencrypt.LetsEncryptManager ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 
   * Enable HTTPS /** ## Ex of Generating a Certificate using JDK 
      ##  keytool     -genkey -v 
      ##    -keystore  my-release-key.keystore 
      ##    -alias     alias_name 
      ##    -keyalg    RSA 
      ##    -keysize   2048 
      ##    -validity  10000 
      ##    -storepass jaxyjaxy 
      ##    -keypass   jaxyjaxy
      ##    -ext SAN=DNS:localhost,IP:127.0.0.1   
 */

public class HttpTransportConfigurator {
    
    public static void configurate( YamlConfigurator cfg                 ,
                                    String           managementPortHttp  ,
                                    String           managementPortHttps ,
                                    String           HOST              ) {
     
        if ( cfg.isHttpsTransport() )           {
            
            display(message("transport_https")) ;
            
            System.setProperty( "thorntail.https.port", cfg.getSelectedPort()) ;
            System.setProperty( "thorntail.https.only", "true")                ;
            System.setProperty( "thorntail.management.https.port" ,
                                managementPortHttps   )           ;
            
            /** Self-signed certificate Generation . **/

            if ( cfg.getSslMode() == YamlConfigurator.SslMode.SELF_SSL )       {

                display(message("self_signed_certificate")) ;
                
                /** Enable certificate generation . */
                System.setProperty("thorntail.https.certificate.generate", "true")    ;
                System.setProperty("thorntail.https.certificate.generate.host", HOST) ;
                displayLine()                                                         ;
            }
            
        } else {
            
            display( message  ( "transport_mode_http" ) )                 ;
            System.setProperty( "thorntail.http.port", cfg.getHttpPort()) ;
            System.setProperty( "thorntail.management.http.port"       , 
                                managementPortHttp  )                  ;
        }

        if ( cfg.isHttpsTransport() && 
             cfg.getSslMode() != YamlConfigurator.SslMode.SELF_SSL )   {
            
            display( message("use_custom_ssl_conf"))                   ;
        }
    }
    
    public static void configurateLetsEncrypt ( YamlConfigurator cfg ) {
        
        /** 
         Check if let's Encrypt Certificate Generation is enabled 
         Download a certificate if it's the case 
         NB : used before deployment .
        **/
        
        LetsEncryptManager.installCertificate( cfg ) ;
      
    }
    
    
}
