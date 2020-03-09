
package com.rac021.jaxy.coby.service.configuration ;

import javax.ejb.Lock ;
import javax.ejb.Startup ;
import javax.ejb.LockType ;
import javax.ejb.Singleton ;
import java.util.Properties ;
import java.util.logging.Level ;
import java.io.FileInputStream ;
import java.util.logging.Logger ;
import javax.annotation.PostConstruct ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
@Startup
@Lock(LockType.READ)
public class CobyConfiguration {
 
    private final String  configFile = "sensor-map.properties" ;
    
    private       String  loggerFile                    = null ;

    private int           frequencyUpdateTimeMs         = 1    ;
    
    @PostConstruct
    public void init() {
        
        Properties prop = new Properties();
	
	try ( FileInputStream input = new FileInputStream(configFile) )  {
          
          prop.load(input) ;
          
          loggerFile               = prop.getProperty("logger_file")
                                         .replaceAll(" +", " ").trim() ;  
          
          frequencyUpdateTimeMs    = Integer.parseInt( prop.getProperty("frequency_logs_update_time_ms" )
                                            .replaceAll(" +", " ").trim()) ;
          
          System.out.println("                                                            "               ) ;
          System.out.println(" Sensor-Map Configuration  ******************************** "               ) ;
          System.out.println("                                                            "               ) ;
          System.out.println("     -->  loggerFile                = " + loggerFile                        ) ;
          System.out.println("     -->  frequencyUpdateTimeMs     = " + frequencyUpdateTimeMs + " ( ms )" ) ;
          System.out.println("                                                            "               ) ;
          System.out.println(" ********************************************************** "               ) ;
               
	} catch( Exception ex ) {
            Logger.getLogger(CobyConfiguration.class.getName()).log(Level.SEVERE, null, ex ) ;
            System.exit(2) ;
        }
    }

    public String getLoggerFile() {
        return loggerFile ;
    }

    public int getFrequencyUpdateTimeMs() {
        return frequencyUpdateTimeMs ;
    }

}
