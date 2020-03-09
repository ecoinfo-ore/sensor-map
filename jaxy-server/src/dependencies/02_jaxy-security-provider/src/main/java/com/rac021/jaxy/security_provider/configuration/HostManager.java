
package com.rac021.jaxy.security_provider.configuration ;

import javax.ejb.Startup ;
import javax.ejb.Singleton ;
import java.net.InetAddress ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.net.UnknownHostException ;
import javax.enterprise.context.ApplicationScoped ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
@Startup
@ApplicationScoped

public class HostManager {

    private static       String HOST_NAME = null         ;
    
    private static       String IP        = null         ;

    private static final Logger LOGGER    = getLogger()  ;

    
    public static String getHostName()                  {

       if ( HOST_NAME != null ) return HOST_NAME        ;
        
       try {

           HOST_NAME  = InetAddress.getLocalHost()
                                   .getCanonicalHostName()   ;
           
       } catch( UnknownHostException ex )                    {
           
            LOGGER.log( Level.WARNING, ex.getMessage(), ex ) ;
            HOST_NAME = "localhost"                          ;
       }
       
       return HOST_NAME ;
       
    }
    
    public static String getIp()                     {

       if ( IP != null ) return IP                   ;
        
       try {

           if( HOST_NAME == null ) getHostName()     ;
           
           IP    = InetAddress.getByName( HOST_NAME )
                              .getHostAddress()             ;
           
       } catch( UnknownHostException ex )                   {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex ) ;
            IP = "127.0.0.1"                                ;
       }
       
       return IP ;
    }

}

