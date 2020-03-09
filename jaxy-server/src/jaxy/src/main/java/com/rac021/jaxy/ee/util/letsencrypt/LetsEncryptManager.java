

package com.rac021.jaxy.ee.util.letsencrypt ;

import java.util.Arrays ;
import java.io.IOException ;
import java.io.BufferedReader ;
import java.util.logging.Logger ;
import java.io.InputStreamReader ;
import static com.rac021.jaxy.messages.Displayer.display ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */
public class LetsEncryptManager {
    
    private static Logger LOGGER  = getLogger() ;
     
    public static void installCertificate( YamlConfigurator cfg )        {
        
       if ( cfg.isHttpsTransport() && 
            cfg.getSslMode() == YamlConfigurator.SslMode.LETS_ENCRYPT )  {
         
            String letsEncryptGenerator = cfg.getLetsEncryptGeneratorLocation() ;

            try {
                   String alias       = cfg.getAlias()                          ;
                   String keyPassword = cfg.getKeyPassword()                    ;
                   String cerPath     = cfg.getCertificatePath()                ;
                   String staging     = cfg.getLetsEncryptCertificateStaging()  ;
                   String challengePa = cfg.getLetsEncryptChallengePath()       ;
                       
                   String[] cmd =  new String[]{ "java"               ,
                                                 "-jar"               , 
                                                 letsEncryptGenerator , 
                                                 "-challenge"         ,
                                                 challengePa          , 
                                                 "-outCertificate"    ,
                                                 cerPath              ,
                                                 "-password"          ,
                                                 keyPassword          ,
                                                 "-alias"             ,
                                                 alias                ,
                                                 "-staging"           ,
                                                 staging              ,
                   } ;

                   LOGGER.log( java.util.logging.Level.INFO, message("new_line")                               ) ;
                   LOGGER.log( java.util.logging.Level.INFO, message("lets_encrypt_generator_message")         ) ;
                   LOGGER.log( java.util.logging.Level.INFO, message("lets_encrypt_path"), letsEncryptGenerator) ;
                   LOGGER.log( java.util.logging.Level.INFO, message("lets_encrypt_cmd"), Arrays.asList(cmd)   ) ;
                   LOGGER.log( java.util.logging.Level.INFO, message("new_line")                               ) ;

                   Process process = Runtime.getRuntime().exec ( cmd ) ;

                   BufferedReader buf = new BufferedReader ( 
                                                   new InputStreamReader (
                                                             process.getInputStream()) ) ;
                      
                   String line = "" ;

                   while (( line = buf.readLine() ) !=null ) {                           
                       display( line ) ;
                   }

                   process.waitFor() ;
                   process.destroy() ;       

                } catch (IOException | InterruptedException ex) {
                   display("FAILED : " + ex.getMessage());
                }
       }
        
    }
}
