
package com.rac021.jaxy.util ;

import org.wildfly.swarm.Swarm ;
import org.wildfly.swarm.config.undertow.Server ;
import org.wildfly.swarm.undertow.UndertowFraction ;
import org.wildfly.swarm.config.undertow.BufferCache ;
import org.wildfly.swarm.config.undertow.server.Host ;
import org.wildfly.swarm.management.ManagementFraction ;
import org.wildfly.swarm.config.management.SecurityRealm ;
import org.wildfly.swarm.config.undertow.ServletContainer ;
import org.wildfly.swarm.config.undertow.HandlerConfiguration ;
import org.wildfly.swarm.config.undertow.server.HttpsListener ;
import org.wildfly.swarm.config.undertow.servlet_container.JSPSetting ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;
import org.wildfly.swarm.config.management.security_realm.SslServerIdentity ;
import org.wildfly.swarm.config.undertow.servlet_container.WebsocketsSetting ;

/**
 *
 * @author ryahiaoui
 */
public class RealmWithUndertowFractionConfigurator {
    
    public static void configurate( Swarm              swarm              ,
                                    ManagementFraction managementFraction ,
                                    YamlConfigurator   cfg              ) {
        /**
         * Enable HTTPS : Ex of Generating a Certificat using JDK : 
         * keytool -genkey -v -keystore my-release-key.keystore -alias
         * alias_name -keyalg RSA -keysize 2048 -validity 10000   .
         */ 

        /** Important : Because of a bug in HTTP/2 Push,
           HTTP/2 Push is set to Off. */
        
        if (cfg.isHttpsTransport()) {

            /**
             * Specific SSL Certificate .
             */

            if ( cfg.getSslMode() == YamlConfigurator.SslMode.PROVIDED_SSL ||
                 cfg.getSslMode() == YamlConfigurator.SslMode.LETS_ENCRYPT  ) {

                String alias             = cfg.getAlias()           ;
                String keyPassword       = cfg.getKeyPassword()     ;
                String certPath          = cfg.getCertificatePath() ;
                String keyStoredPassword = cfg.getKeytorePassword() ;

                managementFraction.securityRealm (
                        new SecurityRealm("SSLRealm")
                                .sslServerIdentity(
                                        new SslServerIdentity<>()
                                                .keystorePath(certPath)
                                                .keystorePassword(keyStoredPassword)
                                                .alias(alias)
                                                .keyPassword(keyPassword)) ) ;

                swarm.fraction (
                        new UndertowFraction()
                               .server(
                                       new Server("default-server")
                                               .httpsListener (
                                                       new HttpsListener("default")
                                                               .securityRealm("SSLRealm")
                                                               .socketBinding("https")
                                                               .http2EnablePush(Boolean.FALSE ) )
                                               .host(new Host("default-host")))
                               .bufferCache(new BufferCache("default"))
                               .servletContainer(
                                       new ServletContainer("default")
                                               .websocketsSetting(new WebsocketsSetting())
                                               .jspSetting(new JSPSetting()))
                               .handlerConfiguration(new HandlerConfiguration())) ;
           
            } 
            
            else if ( cfg.getSslMode() == YamlConfigurator.SslMode.SELF_SSL ) {
               
                swarm.fraction (
                        new UndertowFraction()
                                .server(
                                        new Server("default-server")
                                                .httpsListener(
                                                        new HttpsListener("default")
                                                                .securityRealm("SSLRealm")
                                                                .socketBinding("https")
                                                                .http2EnablePush(Boolean.FALSE) )
                                                .host(new Host("default-host")))
                                .bufferCache(new BufferCache("default"))
                                .servletContainer(
                                        new ServletContainer("default")
                                                .websocketsSetting(new WebsocketsSetting())
                                                .jspSetting(new JSPSetting()))
                                .handlerConfiguration(new HandlerConfiguration())) ;
            }
        
        } 
         else { 
                /** HTTP MODE . **/
                /** Default Undetrow server used in this case. */ 
                
                /*
                swarm.fraction (
                        new UndertowFraction()
                                .server ( new Server("default").httpListener (
                                                                      new HTTPListener("default")
                                                                               .socketBinding("http")
                                                                               .http2EnablePush(Boolean.FALSE) )
                                                               .host(new Host("default-host")))
                                .bufferCache(new BufferCache("default"))
                                .servletContainer( new ServletContainer("default")
                                                                    .websocketsSetting( new WebsocketsSetting())
                                                                    .jspSetting(new JSPSetting()))
                                .handlerConfiguration(new HandlerConfiguration())) ;
                */ 
                
        }
        
    }
    
}
