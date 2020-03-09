
package com.rac021.jaxy.util ;

import entry.Main ;
import java.io.File;
import org.wildfly.swarm.jaxrs.JAXRSArchive ;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset ;

/**
 *
 * @author ryahiaoui
 */

public class ManagerPackager {
    
    public static void packageResources( JAXRSArchive war      , 
                                         boolean      deployUi , 
                                         boolean      secureUI ) {
        
        /** Persistence xml . **/
        
        addAsWebInfResource( war                               , 
                            "META-INF/persistence.xml"         ,  
                            "classes/META-INF/persistence.xml" ) ;
        
        /** beans xml . **/
        addAsWebInfResource( war , "WEB-INF/beans.xml", "beans.xml" ) ;

        /** moxy preperites . **/
        addAsWebInfResource( war , 
                            "com/rac021/jaxy/api/streamers/jaxb.properties",
                            "classes/com/rac021/jaxy/api/streamers/jaxb.properties" ) ;

        /** Add Templates resources Dto Resource Service Templates . **/
        addAsWebInfResource( war , "templates/Dto"     , "classes/templates/Dto"      ) ;
        addAsWebInfResource( war , "templates/Resource", "classes/templates/Resource" ) ;
        addAsWebInfResource( war , "templates/Service", "classes/templates/Service"   ) ;
        
        
        /** Add message properties, for internationalization . **/
        addAsWebInfResource( war ,
                             "com/rac021/jaxy/messages/messages.properties", 
                             "classes/com/rac021/jaxy/messages/messages.properties" ) ;
        
        /** Jaxy Client . **/
        addAsWebInfResource( war                                  ,
                             "jaxy-client/jaxyClient.jar"         , 
                             "classes/jaxy-client/jaxyClient.jar" ) ;
        
        if (secureUI) {

           addAsWebInfResource( war , 
                                "security/application-users.properties",  
                                "classes/application-users.properties" ) ;

           addAsWebInfResource( war ,  
                                "security/application-roles.properties",  
                                "classes/application-roles.properties" ) ;
        }

        if (deployUi) {

            war .addPackage("com.rac021.ui.beans")                       ;
            war .addPackage("com.rac021.jaxy.jsf.handler")               ;
             
            /** faces-config xml . **/
            addAsWebInfResource( war                                     ,
                                 "WEB-INF/faces-config.xml"              ,
                                 "faces-config.xml" )                    ;
        
            addAsWebResource( war , "index.html", "index.html"    )      ;
            
            addAsWebResource( war , "index.xhtml" , "index.xhtml" )      ;

            addAsWebResource( war ,"details.xhtml", "details.xhtml" )    ;
            
            
            addAsWebResource(war ,"WEB-INF/js/jaxy.js", "js/jaxy.js" )   ;
           
            
            addAsWebResource( war , 
                              "resources/ezcomp/authentication_checker.xhtml"   , 
                              "resources/ezcomp/authentication_checker.xhtml" ) ;
            
            addAsWebResource( war , 
                              "resources/ezcomp/authentication_sso_checker.xhtml" ,
                              "resources/ezcomp/authentication_sso_checker.xhtml" ) ;
            
            addAsWebResource( war , 
                              "resources/ezcomp/formula.xhtml"   ,
                              "resources/ezcomp/formula.xhtml" ) ;

            addAsWebResource( war , 
                              "resources/ezcomp/global_technical_configuration.xhtml"  , 
                              "resources/ezcomp/global_technical_configuration.xhtml") ;

            addAsWebResource( war , 
                              "resources/ezcomp/service_details.xhtml"  , 
                              "resources/ezcomp/service_details.xhtml") ;

            addAsWebResource( war , 
                              "resources/ezcomp/service_list.xhtml"  , 
                              "resources/ezcomp/service_list.xhtml") ;
            
            if (secureUI)   {
                
                addAsWebResource( war , "login.xhtml" , "login.xhtml"  ) ;
            }
            
          }

        /** Monitoring . **/
        
        addAsWebInfResource( war , 
                             "monitoring/jaxy_grafana_dashboard_monitoring.json"        , 
                             "classes/monitoring/jaxy_grafana_dashboard_monitoring.json") ;

        /** Packages . **/
        
        war .addPackage("com.rac021.jaxy.cors")                     ;
        war .addPackage("com.rac021.jaxy.health")                   ;
        war .addPackage("com.rac021.jaxy.messages")                 ;
        war .addPackage("com.rac021.jaxy.unzipper")                 ;
        war .addPackage("com.rac021.jaxy.service.time")             ;
        war .addPackage("com.rac021.jaxy.service.sensorthings")     ;
        war .addPackage("com.rac021.jaxy.override.configuration")   ;
        war .addPackage("com.rac021.jaxy.ghosts.services.manager")  ;
        war .addPackage("com.rac021.jaxy.service.script.generator") ;

        war .addPackage("com.rac021.jaxy.compilation")              ;
        
        war .addPackage("com.rac021.jaxy.ee.metrics")               ;
        war .addPackage("com.rac021.jaxy.ee.grafana")               ;
        
        war .addPackage("com.rac021.jaxy.io")                       ;
        war .addPackage("com.rac021.jaxy.shared")                   ;
        
        
        war .addPackage("com.rac021.jaxy.coby.io")                    ;
        war .addPackage("com.rac021.jaxy.coby.checker")               ;
        war .addPackage("com.rac021.jaxy.coby.service.logs")          ;
        war .addPackage("com.rac021.jaxy.coby.service.configuration") ;

        
        war .addPackage("entry")                                    ;
    
        if ( deployUi && secureUI )                 {
            war .addPackage("com.rac021.ui.filter") ;
        }
    }
    
    private static void addAsWebResource( JAXRSArchive war    ,
                                          String       source , 
                                          String       dest   ) {
        
           war.addAsWebResource ( new ClassLoaderAsset( source,
                                                        Main.class.getClassLoader() ) ,
                                   dest ) ;
    }
    
    private static void addAsWebInfResource( JAXRSArchive war    ,
                                             String       source , 
                                             String       dest   ) {
        
       war.addAsWebInfResource( new ClassLoaderAsset( source ,
                                                      Main.class.getClassLoader() ) ,
                                dest ) ;
    }
}

