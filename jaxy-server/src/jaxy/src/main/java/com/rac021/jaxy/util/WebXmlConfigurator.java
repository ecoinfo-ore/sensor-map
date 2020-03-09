
package com.rac021.jaxy.util ;

import org.wildfly.swarm.jaxrs.JAXRSArchive ;
import org.wildfly.swarm.undertow.descriptors.WebXmlAsset ;

/**
 *
 * @author ryahiaoui
 */

public class WebXmlConfigurator {
    
    public static void configureWebXml ( JAXRSArchive deployment ) {
        
         WebXmlAsset webXml = deployment.findWebXmlAsset() ;

        /*
         if( secureUI ) {
            webXml.setLoginConfig("BASIC", "other") ;
            webXml.protect("/index.xhtml")
            .withRole("UI-ROLE")
            .withMethod("GET")
            .withMethod("PUT")
            .withMethod("DELETE")
            .withMethod("POST")
            .withMethod("TRACE")
            .withMethod("HEAD")
            .withMethod("CONNECT")
            .withMethod("OPTIONS") ;
         }
        */
        
        /** Bootsfaces Config . **/
        webXml.setContextParam("net.bootsfaces.legacy_error_classes", "true") ;

    }
    
}
