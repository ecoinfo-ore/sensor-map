
package com.rac021.jaxy.ee.grafana ;

import java.io.File ;
import java.net.URL ;
import java.util.Set ;
import java.util.List ;
import java.util.UUID ;
import java.nio.file.Files ;
import java.io.IOException ;
import java.nio.file.Paths ;
import java.util.ArrayList ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.stream.Collectors ;
import com.rac021.jaxy.shared.Transformer ;
import com.rac021.jaxy.shared.JaxyLocator ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.root.ServicesManager ;
import static com.rac021.jaxy.io.Reader.readFile ;
import java.util.concurrent.atomic.AtomicInteger ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */
public class GrafanaManager {
    
    private static Logger LOGGER = getLogger() ;
    
    private static final AtomicInteger idDashBoardCounter  = new AtomicInteger(0) ;
   
    private static final AtomicInteger idDashBoardTimer    = new AtomicInteger(0) ;
    
    private static       int           counterColorCounter = -1                   ;
    
    private static       int           counterColorTimer   = -1                   ;
    
    
    private static void generateGrafanaFiles(  List<String> grafanaTemplateservicesCounter,
                                               List<String> grafanaTemplateservicesTimer  ) throws IOException {
        
        
        String stringTemplateserviceCounter = grafanaTemplateservicesCounter.stream().collect(Collectors.joining(",")) ;
        String stringTemplateserviceTimer   = grafanaTemplateservicesTimer.stream().collect(Collectors.joining(","))   ;

        String grafana_template_counter = Template.DashBoard
                                                  .replace( Template.NODE_TEMPLATE, stringTemplateserviceCounter )
                                                  .replace( Template.TITLE, Template.TITLE_COUNTERS )
                                                  .replace( Template.UUID, UUID.randomUUID().toString());
                
        String grafana_template_timer = Template.DashBoard
                                                .replace( Template.NODE_TEMPLATE, stringTemplateserviceTimer )
                                                .replace( Template.TITLE, Template.TITLE_TIMERS )
                                                .replace( Template.UUID, UUID.randomUUID().toString()) ;

        
        String folder = JaxyLocator.getJaxyJarLocation()                                    ;
       
        String grafanaOutputFolder = folder         +  File.separator  +  "monitoring_jaxy" +
                                     File.separator + "provisioning"   +  File.separator    ;
        
        Files.createDirectories(Paths.get(grafanaOutputFolder))                 ;
        Files.createDirectories(Paths.get(grafanaOutputFolder + "dashboards"))  ;
        Files.createDirectories(Paths.get(grafanaOutputFolder + "datasources")) ;
                
        String grafanaDashBoardServicesCounterOutFile =  grafanaOutputFolder +
                                                         "dashboards"        + 
                                                         File.separator      + 
                                                         "jaxy_grafana_dashboard_services_counter.json" ;
        
        Files.write(Paths.get(grafanaDashBoardServicesCounterOutFile), grafana_template_counter.getBytes()) ;
                
        String grafanaDashBoardServicesTimerOutFile =  grafanaOutputFolder + "dashboards" +  File.separator +
                                                       "jaxy_grafana_dashboard_services_timer.json"         ;
        
        Files.write(Paths.get(grafanaDashBoardServicesTimerOutFile), grafana_template_timer.getBytes())     ;
              
        String grafanaDataSourceOutFile =  grafanaOutputFolder  + "datasources" +  File.separator +
                                           "jaxy_grafana_datasource.yaml"                         ;
        
        Files.write(Paths.get(grafanaDataSourceOutFile), Template.DataSourceYml.getBytes()) ;

        String grafanaDashBoardMonitoringOutFile =  grafanaOutputFolder + "dashboards"       +
                                                    File.separator                           +
                                                    "jaxy_grafana_dashboard_monitoring.json" ;
        
        URL grafana_jaxxy_dashboard_monitoringUrl = new GrafanaManager().getClass()
                                                                        .getClassLoader()
                                                                        .getResource("monitoring/jaxy_grafana_dashboard_monitoring.json") ;
        
        String grafana_jaxxy_dashboard_monitoring_string = readFile(grafana_jaxxy_dashboard_monitoringUrl)              ;
              
        Files.write(Paths.get(grafanaDashBoardMonitoringOutFile), grafana_jaxxy_dashboard_monitoring_string.getBytes()) ;
             
        LOGGER.log(Level.INFO, message("new_line"))                                        ;
        LOGGER.log(Level.INFO, message("dash"))                                            ;
        LOGGER.log(Level.INFO, message("dash"))                                            ;
        LOGGER.log(Level.INFO, message("monitor_dashboard_generator"))                     ;
        LOGGER.log(Level.INFO, message("new_line"))                                        ;
        LOGGER.log(Level.INFO, message("grafana_jaxy_dashboard_monitoring"))               ;
        LOGGER.log(Level.INFO, message("flech") , grafanaDashBoardMonitoringOutFile)       ;
              
        LOGGER.log(Level.INFO, message("grafana_jaxy_dashboard_services")  )               ;
        LOGGER.log(Level.INFO, message("flech") , grafanaDashBoardServicesCounterOutFile ) ;
              
        LOGGER.log(Level.INFO, message("grafana_jaxy_datasource")         )                ;
        LOGGER.log(Level.INFO, message("flech") , grafanaDataSourceOutFile )               ;
                
        LOGGER.log(Level.INFO, message("dash"))                                            ;
        LOGGER.log(Level.INFO, message("dash"))                                            ;
        LOGGER.log(Level.INFO,message("dash") )                                            ;
        LOGGER.log(Level.INFO, message("new_line"))                                        ;
    }
    
    
    private static String buildGrafanaTemplateCounterForService( String serviceCode , 
                                                                 Set<AcceptType> acceptedTypeByTheService  ) {
       
        final String serviceCodeSnakeName = Transformer.getSnakeName(serviceCode) ;

        List<String> combinedNames = new ArrayList<>()   ;

        acceptedTypeByTheService.forEach( acceptType ->  {
            
            if( acceptType == AcceptType.XML_PLAIN )     {
                 combinedNames.add( serviceCodeSnakeName + "_xml_plain_counter_total"      )    ;
            }
            if( acceptType == AcceptType.JSON_PLAIN )    {
                 combinedNames.add(serviceCodeSnakeName  + "_json_plain_counter_total"     )     ;
            }
            if( acceptType == AcceptType.XML_ENCRYPTED ) {
                 combinedNames.add(serviceCodeSnakeName  + "_xml_encrypted_counter_total"  )     ;
            }
            if( acceptType == AcceptType.JSON_ENCRYPTED) {
                 combinedNames.add( serviceCodeSnakeName + "_json_encrypted_counter_total" )     ;
            }
            if( acceptType == AcceptType.TEMPLATE_PLAIN ) {
                 combinedNames.add(serviceCodeSnakeName  + "_template_plain_counter_total" )     ;
            }
            if( acceptType == AcceptType.TEMPLATE_ENCRYPTED ) {
                 combinedNames.add( serviceCodeSnakeName + "_template_encrypted_counter_total" ) ;
            }
            
        });
        
        counterColorCounter ++ ;
        
        return combinedNames.stream().map( serv -> buildeInstanceServiceGrafana( Template.ServiceCounterTemplate              , 
                                                                                 serv                                         , 
                                                                                 idDashBoardCounter.get()                     ,
                                                                                 6                                            ,
                                                                                 10                                           ,
                                                                                 (idDashBoardCounter.get() % 2 == 0) ? 1 : 11 , 
                                                                                 ( idDashBoardCounter.get() / 2 ) * 6         ,
                                                                                 counterColorCounter % Template.colors.size() ,
                                                                                 idDashBoardCounter )                         )
                                     .collect(Collectors.joining(","))                                                        ;
    }
    
    private static String buildGrafanaTemplateTimerForService( String serviceCode                       ,
                                                               Set<AcceptType> acceptedTypeByTheService ) {
       
        final String serviceCodeSnakeName = Transformer.getSnakeName(serviceCode) ;

        List<String> combinedNames = new ArrayList<>()   ;
                
        acceptedTypeByTheService.forEach( acceptType ->  {
            
            if( acceptType == AcceptType.XML_PLAIN ) {
                 combinedNames.add( serviceCodeSnakeName + "_xml_plain_timer_mean_seconds"          ) ;
            }
            if( acceptType == AcceptType.JSON_PLAIN )    {
                 combinedNames.add(serviceCodeSnakeName  + "_json_plain_timer_mean_seconds"         ) ;
            }
            if( acceptType == AcceptType.XML_ENCRYPTED ) {
                 combinedNames.add(serviceCodeSnakeName  + "_xml_encrypted_timer_mean_seconds"      ) ;
            }
            if( acceptType == AcceptType.JSON_ENCRYPTED) {
                 combinedNames.add( serviceCodeSnakeName + "_json_encrypted_timer_mean_seconds"     ) ;
            }
            if( acceptType == AcceptType.TEMPLATE_PLAIN ) {
                 combinedNames.add(serviceCodeSnakeName   + "_template_plain_timer_mean_seconds"    ) ;
            }
            if( acceptType == AcceptType.TEMPLATE_ENCRYPTED ) {
                 combinedNames.add( serviceCodeSnakeName + "_template_encrypted_timer_mean_seconds" ) ;
            }
            
        });
        
        counterColorTimer++ ;
        
        return combinedNames.stream().map( serv -> buildeInstanceServiceGrafana( Template.ServiceTimerTemplate              , 
                                                                                 serv                                       ,
                                                                                 idDashBoardTimer.get()                     ,
                                                                                 6                                          ,
                                                                                 10                                         ,
                                                                                 (idDashBoardTimer.get() % 2 == 0) ? 1 : 11 , 
                                                                                 ( idDashBoardTimer.get() / 2 ) * 6         ,
                                                                                 counterColorTimer % Template.colors.size() ,
                                                                                 idDashBoardTimer ) )
                                     .collect(Collectors.joining(",")) ;
    }

    private static String buildeInstanceServiceGrafana( String template        ,
                                                        String serviceName     , 
                                                        int idDashboard        , 
                                                        int gridPosH           , 
                                                        int gridPosW           ,
                                                        int gridPosX           , 
                                                        int gridPosY           ,
                                                        int colorIndex         ,
                                                        AtomicInteger whichCounter ) {
        
        String templ = template.replace(Template.GridPosH, String.valueOf(gridPosH) )
                               .replace(Template.GridPosW, String.valueOf(gridPosW) )
                               .replace(Template.GridPosX, String.valueOf(gridPosX) )
                               .replace(Template.GridPosY, String.valueOf(gridPosY) )
                               .replace(Template.ID, String.valueOf(idDashboard)    )
                               .replace(Template.SNAKE_SERVICE_NAME_FROM_PROMETHEUS, serviceName)
                               // .replace(Template.SNAKE_SERVICE_NAME, serviceName.replace("_counter", "") )
                               .replace(Template.SNAKE_SERVICE_NAME, serviceName.replace("_counter_total", "") )
                               .replace(Template.COLOR_TEMPLATE, Template.colors.get( colorIndex )     ) ; 
        
        whichCounter.getAndIncrement() ;
        return templ                   ;
    }

    public static void rootGenerator( List<String> servicesCode, ServicesManager servicesManager ) throws IOException {
    
        List<String> grafanaTemplateservicesCounter = new ArrayList<>() ;
          
        List<String> grafanaTemplateservicesTimer   = new ArrayList<>() ;
                
        servicesCode.forEach( serviceCode -> {
              
          grafanaTemplateservicesCounter.add ( 
                   GrafanaManager.buildGrafanaTemplateCounterForService( serviceCode ,
                                                                         servicesManager.getAcceptForServiceName(serviceCode) ) ) ;
             
          grafanaTemplateservicesTimer.add( 
                   GrafanaManager.buildGrafanaTemplateTimerForService( serviceCode , 
                                                                       servicesManager.getAcceptForServiceName(serviceCode) ))    ;
              
        } ) ;
                
        generateGrafanaFiles( grafanaTemplateservicesCounter, grafanaTemplateservicesTimer) ;
    }
    
}

