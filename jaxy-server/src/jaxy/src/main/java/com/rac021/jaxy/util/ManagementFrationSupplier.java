
package com.rac021.jaxy.util ;

import org.wildfly.swarm.management.ManagementFraction ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */

public class ManagementFrationSupplier {
    
    public static ManagementFraction get( YamlConfigurator cfg            ,
                                          String           admin_login    ,
                                          String           admin_password ,
                                          String           HOST           ) {
                
       ManagementFraction mf =
               
        ManagementFraction.createDefaultFraction() 
                          // .httpPort ( cfg.getManagrementPortHttp() )
                          // .httpsPort( cfg.getManagrementPortHttps())
                          .httpInterfaceManagementInterface ( 
                                         
                                    ( iface) -> {
                                                 iface.allowedOrigins (
                                                      cfg.getManagementAllowedOrigin()
                                                 ) ;
                                                 iface.securityRealm("ManagementRealm")      ;
                                                 if ( cfg.isHttpsTransport()) {
                                                     iface.socketBinding("management-https") ;
                                                 } else {
                                                     iface.socketBinding("management-http")  ;
                                                 }
                            })
                            .securityRealm ( "ManagementRealm", 
                                    
                                    (realm) -> {
                                                 realm.inMemoryAuthentication  (
                                                         (auth) -> {
                                                             auth.add(admin_login, admin_password, true ) ;
                                                         } ) ;
                                                 realm.inMemoryAuthorization (
                                                         (authz) -> {
                                                             authz.add( admin_login, admin_password );
                                                         } 
                                                 ) ;
                            })     ;
       
       
        if( ! cfg.deployManagementInterface() )                      {
            mf.httpInterfaceManagementInterface().httpDisable(true ) ;
        }
      
        return mf ;
        
    }
    
}
