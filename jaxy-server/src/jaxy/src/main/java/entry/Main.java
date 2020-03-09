
package entry ;

import java.util.logging.Level ;
import org.wildfly.swarm.Swarm ;
import java.util.logging.Logger ;
import com.rac021.jaxy.messages.Displayer ;
import org.jboss.shrinkwrap.api.ShrinkWrap ;
import org.wildfly.swarm.jaxrs.JAXRSArchive ;
import com.rac021.jaxy.util.ManagerPackager ;
import com.rac021.jaxy.util.RejectConnection ;
import com.rac021.jaxy.util.WebXmlConfigurator ;
import com.rac021.jaxy.util.LoggerConfigurator ;
import org.wildfly.swarm.spi.api.SwarmProperties ;
import com.rac021.jaxy.util.KeycloakConfigurator ;
import com.rac021.jaxy.util.DataSourceConfigurator ;
import org.wildfly.swarm.logging.LoggingProperties ;
import com.rac021.jaxy.util.HttpTransportConfigurator ;
import com.rac021.jaxy.util.ManagementFrationSupplier ;
import com.rac021.jaxy.util.WorkerFractionConfigurator ;
import org.wildfly.swarm.management.ManagementFraction ;
import org.wildfly.swarm.management.ManagementProperties ;
import static com.rac021.jaxy.messages.Displayer.display ;
import static com.rac021.jaxy.messages.Displayer.message ;
import com.rac021.jaxy.util.ServiceConfigurationLocatorFile ;
import static com.rac021.jaxy.messages.Displayer.displayLine ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import com.rac021.jaxy.util.RealmWithUndertowFractionConfigurator ;
import org.wildfly.swarm.management.console.ManagementConsoleFraction ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/** @author ryahiaoui */


public class Main                {
    
    private static Logger LOGGER ;

    public static void main(String[] args) throws Exception     {
        
        /** Debugger Mode  . **/
        // System.setProperty("thorntail.debug.port" ,"11555")  ;
         
        /**  Setup The LOGGER.  **/
        LoggerConfigurator.setupLogging()       ;
        LOGGER = getLogger()                    ;

        /**  Initialize messages map.  **/
        Displayer displayer = new Displayer()   ;
        
        display( message("jaxy_version") )      ;
        
        /** Check if configuraition file was passed in args 
            or search for the default serviceConf.yaml    . */
        /** Must be called Before : new YamlConfigurator(). */
        ServiceConfigurationLocatorFile.locate()             ;

        /**  Load Configuration. **/
        YamlConfigurator cfg = new YamlConfigurator()        ;
 
        /** LOG LEVEL. **/ 
        String logLevel = cfg.getLogLevel()                  ;

        display( message("log_level") + logLevel )           ;
        
        display( message("vfs")                              +
                 System.getProperty("java.io.tmpdir") )      ;
        
        displayLine()                                        ;
        
        System.setProperty( LoggingProperties.LOGGING, logLevel ) ;
        
        System.setProperty( SwarmProperties.BIND_ADDRESS ,
                            cfg.getJaxyBindAdress()      )        ;
        
        String HOST = cfg.getHost()                               ;
        
        final String CONTEXT  = cfg.getRootApplicationContext()   ;
        
        System.setProperty("thorntail.context.path", CONTEXT)     ;
        
        LOGGER.log( Level.INFO, message("application_context")    , 
                    new Object[] { CONTEXT } )                    ;
        
        final String ADMIN_CONSLE = cfg.getAdminConsoleContext()  ;
        
        final String managementPortHttp  = String.valueOf(cfg.getManagrementPortHttp())  ;
        
        final String managementPortHttps = String.valueOf(cfg.getManagrementPortHttps()) ;

        System.setProperty( ManagementProperties.MANAGEMENT_BIND_ADDRESS , 
                            cfg.getManagementBindAdress() )              ;
        
        LOGGER.log(Level.INFO, message("new_line") )        ;
        LOGGER.log(Level.INFO, message("domain_ip" ), HOST) ;
        LOGGER.log(Level.INFO, message("new_line") )        ;
        
        LOGGER.log(Level.INFO, message("bind_adress"), cfg.getJaxyBindAdress()) ;
        LOGGER.log(Level.INFO, message("new_line") )                            ;
        
        System.setProperty("java.net.preferIPv4Stack", "true")                  ;

        /**  Default Session timeout in seconds.  **/
        System.setProperty( "thorntail.undertow.servlet-containers.default.default-session-timeout" , 
                            String.valueOf( YamlConfigurator.getSessionTimeOut() ) )                ;
        
        /**  Set KeyCloak Properties if SSO is Enable. **/
        KeycloakConfigurator.configurate(cfg)            ;

         
        String admin_login =
                ((String) cfg.getConfiguration().get("admin_login")) != null
                        ? ((String) cfg.getConfiguration().get("admin_login"))
                        : "admin" ;
        
        String admin_password =
                ((String) cfg.getConfiguration().get("admin_password")) != null
                        ? ((String) cfg.getConfiguration().get("admin_password"))
                        : "admin" ;

       
        /**  Configure Http / Https - SSL / LetsEncrypt       . **/
        HttpTransportConfigurator.configurate( cfg                 ,
                                               managementPortHttp  ,
                                               managementPortHttps ,
                                               HOST                ) ;
        HttpTransportConfigurator.configurateLetsEncrypt( cfg )      ;
        
        /**
         * Max Concurent Thread for the Application 
         * Micro Profile Config.                    
         */
        RejectConnection.configurate(cfg)         ;

        
        Swarm swarm = new Swarm()                                    ;
       
        swarm.fraction(DataSourceConfigurator.getDataSource( cfg ) ) ;

        /** Create A ManagementFraction. **/
        ManagementFraction managementFraction =  ManagementFrationSupplier.get( cfg            ,
                                                                                admin_login    ,
                                                                                admin_password ,
                                                                                HOST         ) ;
       
        /** Important : Because of a bug in HTTP/2 Push, HTTP/2 Push is set to Off. **/
        RealmWithUndertowFractionConfigurator.configurate( swarm, managementFraction, cfg )    ;
        
        swarm.fraction( managementFraction ) ;

        swarm.fraction( new ManagementConsoleFraction().contextRoot( CONTEXT + ADMIN_CONSLE))  ;

        WorkerFractionConfigurator.configurate(swarm, cfg ) ; 
        
        /** Configure and Install Logger. **/
        swarm.fraction( LoggerConfigurator.getLoggingFraction( logLevel, cfg ) ) ;

        display( message( "starting_server" ) )                                  ;
        display( message("new_line"         ) )                                  ;
        
        try {
               swarm.start()                                                     ;
        } catch( Exception ex )                           {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
            System.exit(2)                                ;
        }
        
        display( message( "create_jaxy_war" ) )                                  ;

        JAXRSArchive war = ShrinkWrap.create(JAXRSArchive.class, "jaxy.war")     ;

        /** Secure Services if Authentication Mode == Keycloak. **/
        KeycloakConfigurator.secureServices(war, cfg, LOGGER) ;

        war.addModule(DataSourceConfigurator.driverModule )   ;

        WebXmlConfigurator.configureWebXml(war)               ;

        ManagerPackager.packageResources( war, cfg.deployUI(), cfg.isSecuredUI() ) ;
        
        /* Faces Servlet */
        war.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet")
           .withDisplayName("FacesServlet")
           .withUrlPattern("*.xhtml") 
           .withUrlPattern("*.html") ;
       
        
        /** Debug Mode. **/
       if( LOGGER.getLevel() == Level.FINE )           {
            
           war.getContent().forEach( ( path, node ) -> { 
               LOGGER.log(Level.INFO, path.get() )     ;
            } ) ;
       }
       
        /** Deployment. **/

        war.addAllDependencies() ;
        
        display( message( "deploy_jaxy_war" ) )        ;

        swarm.deploy(war)                              ;

        /** Display Informations . **/
        
        LOGGER.log( Level.INFO , message("new_line") ) ;
        LOGGER.log( Level.INFO , message("stars"))     ;
        LOGGER.log( Level.INFO , message("new_line"))  ;
        
        LOGGER.log( Level.INFO                                      ,
                    message("server_started_at")                    ,
                    new Object[] { cfg.getTransport()               ,
                                   cfg.getHost()                    , 
                                   cfg.getSelectedPort(), CONTEXT } ) ;
        
        LOGGER.log( Level.INFO                             , 
                    message("server_listening_on")         ,
                    new Object[] { cfg.getJaxyBindAdress() ,
                                   cfg.getSelectedPort()}) ;
        
        LOGGER.log( Level.INFO, message("new_line") ) ;

        LOGGER.log( Level.INFO,  message("admin_console")    ,
                    new Object[] { cfg.getTransport()        ,
                                   cfg.getHost()             , 
                                   cfg.getSelectedPort()     ,  
                                   CONTEXT                   ,
                                   ADMIN_CONSLE }        )   ;

        LOGGER.log( Level.INFO, message("new_line")) ;
        
        if(  cfg.deployManagementInterface() )       {
            
           LOGGER.log( Level.INFO                               , 
                       message("management_interface")          ,
                       new Object[] { "http"                    ,
                                      cfg.getHost()             ,
                                      cfg.isHttpsTransport() ? managementPortHttps : 
                                                               managementPortHttp  } ) ;
              
           LOGGER.log( Level.INFO                                   ,
                       message("management_interface_listening")    ,
                       new Object[] { "http"                        , 
                                      cfg.getManagementBindAdress() , 
                                      cfg.isHttpsTransport() ? 
                                      managementPortHttps : managementPortHttp } ) ;
           
           LOGGER.log( Level.INFO, 
                       message("management_interface_allowed_origin") ,
                       new Object[] { cfg.getManagementAllowedOrigin() } ) ;
        }   
        
        LOGGER.log( Level.INFO, message("new_line")) ;

        LOGGER.log( Level.INFO, message("service_discovery") ,
                    new Object[] { cfg.getTransport()        ,
                                   cfg.getHost()             ,
                                   cfg.getSelectedPort()     ,
                                   CONTEXT}                ) ;

        LOGGER.log( Level.INFO, message("new_line")) ;
        LOGGER.log( Level.INFO, message("monitor"))  ;

        LOGGER.log( Level.INFO      , 
                    message("node") ,
                    new Object[] { cfg.getTransport()        , 
                                   cfg.getHost()             , 
                                   cfg.getSelectedPort()}    ) ;
        
        LOGGER.log( Level.INFO        , 
                    message("health") ,
                    new Object[] { cfg.getTransport()        ,
                                   cfg.getHost()             ,
                                   cfg.getSelectedPort()}    ) ;
        
        LOGGER.log( Level.INFO      ,
                    message("heap") ,
                    new Object[] { cfg.getTransport()       ,
                                   cfg.getHost()            ,
                                   cfg.getSelectedPort()}   ) ;
        
        LOGGER.log( Level.INFO         , 
                    message("threads") ,
                    new Object[] { cfg.getTransport()        , 
                                   cfg.getHost()             , 
                                   cfg.getSelectedPort()}    ) ;
        
        LOGGER.log( Level.INFO, message("new_line"));

        LOGGER.log( Level.INFO        , 
                    message("metrics"),
                    new Object[] { cfg.getTransport()        , 
                                   cfg.getHost()             , 
                                   cfg.getSelectedPort()}    ) ;
      
        LOGGER.log( Level.INFO, message("new_line")) ;
        LOGGER.log( Level.INFO, message("new_line")) ;

        if ( cfg.deployUI() )    {

          LOGGER.log( Level.INFO , 
                      message("web_ui")                        ,
                      new Object[] { cfg.getTransport()        , 
                                     cfg.getHost()             , 
                                     cfg.getSelectedPort()     ,
                                     CONTEXT                   ,
                                     cfg.isSecuredUI() })      ;

          LOGGER.log( Level.INFO, message("new_line"))         ;
          LOGGER.log( Level.INFO, message("stars"))            ;
          LOGGER.log( Level.INFO, message("new_line"))         ;
        }
    }
    
}
