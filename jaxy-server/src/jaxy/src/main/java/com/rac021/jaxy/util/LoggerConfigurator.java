
package com.rac021.jaxy.util ;

import java.io.File ;
import java.util.Map ;
import java.util.HashMap ;
import org.jboss.modules.Module ;
import java.util.logging.LogManager ;
import com.rac021.jaxy.shared.JaxyLocator ;
import org.jboss.modules.ModuleLoadException ;
import org.wildfly.swarm.config.logging.Level ;
import org.wildfly.swarm.logging.LoggingFraction ;
import static com.rac021.jaxy.messages.Displayer.message ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */
public class LoggerConfigurator {
    
    private static final String LOG_FILE  =  JaxyLocator.getJaxyJarLocation() + 
                                             File.separator                   + 
                                             "logs"                           +
                                             File.separator                   + 
                                             "jaxy"                           ;
        
    public static LoggingFraction getLoggingFraction( String logLevel      , 
                                                      YamlConfigurator cfg ) {
            return 
                new LoggingFraction()
                        .applyDefaults( Level.valueOf(logLevel))
                        .formatter( message("jaxy_formatter"),
                                   "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%t] ( %C.%M %L ) %s%e%n")
                        .periodicSizeRotatingFileHandler( "FILE_ERROR"   ,
                                (h) -> {
                                       h.level(  Level.valueOf(logLevel))
                                                      .filterSpec("levelRange[WARNING,FATAL]")
                                                      .namedFormatter(message("jaxy_formatter"))
                                                      .append(true)
                                                      .suffix(".yyyy-MM-dd")
                                                      .rotateSize( cfg.getLogSize() + "m")
                                                      .enabled(true)
                                                      .encoding("UTF-8")
                                                     .maxBackupIndex( cfg.getMaxBackupLog() ) ;
                                    Map<String, String> fileSpec = new HashMap<>()            ;
                                    fileSpec.put("path", LOG_FILE + "_errors.log")            ;
                                    h.file(fileSpec)                                          ;
                                })
                        .periodicSizeRotatingFileHandler( "FILE_INFO",
                                (h) -> {
                                    h.level(  Level.valueOf(logLevel))
                                                   .filterSpec("levelRange[TRACE,INFO]")
                                                   .namedFormatter( message("jaxy_formatter"))
                                                   .append(true)
                                                   .suffix(".yyyy-MM-dd")
                                                   .rotateSize( cfg.getLogSize() + "m")
                                                   .enabled(true)
                                                   .encoding("UTF-8")
                                                   .maxBackupIndex( cfg.getMaxBackupLog() ) ;
                                    Map<String, String> fileSpec = new HashMap<>()          ;
                                    fileSpec.put("path", LOG_FILE + "_infos.log")           ;
                                    h.file(fileSpec)                                        ;
                                })
                        .logger( "jaxylogger",  (l) -> {
                                          /* Order level is important */
                                          l.level(Level.valueOf(logLevel)).handler("FILE_ERROR");
                                          l.level(Level.valueOf(logLevel)).handler("FILE_INFO") ;
                        }) ; 
    }
    
    public static void setupLogging() throws Exception {

      /** Need to setup Logging here so that Weld doesn't default to JUL. */
      
      /** https://github.com/thorntail/thorntail/blob/master/core/container/src/main/java/org/wildfly/swarm/Swarm.java . */

      try {
            Module loggingModule =  Module.getBootModuleLoader()
                                          .loadModule("org.wildfly.swarm.logging:runtime") ;

            ClassLoader originalCl = Thread.currentThread().getContextClassLoader()        ;
            
            try {
                  Thread.currentThread().setContextClassLoader ( loggingModule.getClassLoader())           ;
                  System.setProperty ( "java.util.logging.manager", "org.jboss.logmanager.LogManager" )    ;
                  System.setProperty ( "org.jboss.logmanager.configurator" ,
                                       "org.wildfly.swarm.container.runtime.wildfly.LoggingConfigurator" ) ;
                
                /** force logging init. */
                LogManager.getLogManager()  ;
                
            } finally {
                Thread.currentThread().setContextClassLoader(originalCl) ;
            }
        } catch (ModuleLoadException e ) {
            System.err.println("[WARN] logging not available, logging will not be configured") ;
        }
    }
}
