
package com.rac021.jaxy.util ;

import java.io.File ;
import java.util.Map ;
import java.util.List ;
import java.util.Objects ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import org.wildfly.swarm.keycloak.Secured ;
import org.wildfly.swarm.jaxrs.JAXRSArchive ;
import static com.rac021.jaxy.messages.Displayer.display ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.messages.Displayer.displayLine ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */
public class KeycloakConfigurator {
    
    public static void configurate( YamlConfigurator cfg )         {
        
         Objects.requireNonNull( cfg )                             ;
        
        /** Set KeyCloak Properties if SSO is Enable . **/
          
        if ( cfg.getAuthenticationType().equalsIgnoreCase("SSO") && 
             cfg.getKeycloakFile() != null )                      {

            File kc = new File(cfg.getKeycloakFile()) ;
            
            if (! kc.exists() )                       {
                
                display (
                        " -> Error : File [ " + cfg.getKeycloakFile() + " ] Not found. ") ;
                display("                                                              ") ;
                System.exit(0)                                                            ;
            }

            System.setProperty("thorntail.keycloak.json.path", kc.getAbsolutePath()) ;
            display(message("used_keycloak_file") + kc.getAbsolutePath()      )      ;
            displayLine()                                                            ;
        }
    }
    
    
    public static void secureServices( JAXRSArchive war     , 
                                       YamlConfigurator cfg , 
                                       Logger LOGGER        )         {
        
         if ( cfg.getAuthenticationType().equalsIgnoreCase("SSO") )   {

            LOGGER.log( Level.INFO, message("new_line") )             ;
            LOGGER.log( Level.INFO, message("security_sso_enabled") ) ;
            LOGGER.log( Level.INFO, message("new_line") )             ;
            
            Secured keyCloakSecurity = war.as(Secured.class)          ;

            Map authentication = cfg.getAuthenticationInfos()         ;

            ((Map) authentication.get("secured"))
                    
                    .forEach (
                            
                            (_sName, _methods) -> {
                                
                                ((Map) _methods)
                                        
                                        .forEach(
                                                
                                                (_method, _roles) -> {
                                                    
                                                    String method      = (String) _method      ;
                                                    
                                                    List<String> roles = (List<String>) _roles ;

                                                    roles.forEach (
                                                            
                                                            role -> {
                                                                
                                                                keyCloakSecurity
                                                                        
                                                                        .protect (
                                                                                "/rest/resources/"
                                                                                        + _sName )
                                                                        .withMethod (
                                                                                method.toUpperCase()
                                                                                      .replaceAll (
                                                                                                " +",
                                                                                                " ")
                                                                                      .trim() )
                                                                        .withRole (
                                                                                    role.replaceAll (
                                                                                                " +",
                                                                                                " " )
                                                                                        .trim())    ;
                                                            }) ;
                                                }) ;
                            } ) ;
        }
        
    }
    
    
}
