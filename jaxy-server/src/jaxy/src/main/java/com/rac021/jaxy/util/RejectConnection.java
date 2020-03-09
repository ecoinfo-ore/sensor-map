
package com.rac021.jaxy.util ;

import com.rac021.jaxy.messages.Displayer ;
import static com.rac021.jaxy.messages.Displayer.message ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */
public class RejectConnection {
    
    public static void configurate( YamlConfigurator cfg )                  {
        
        /** Max Concurent Thread for the Application 
            Micro Profile Config . */
       
        if( cfg.getRejectConnectionsWhenLimitExceeded() > 0 )               {
            
            Displayer.display( message( "reject_connec_when_limit_exeed")   + 
                              cfg.getRejectConnectionsWhenLimitExceeded() ) ;
          
            Displayer.displayLine()                                         ;
          
            System.setProperty (
                    "com.rac021.jax.api.root.RootService/subResourceLocators/Bulkhead/value" ,
                    String.valueOf( cfg.getRejectConnectionsWhenLimitExceeded()) )           ;
        }
    }
    
}
