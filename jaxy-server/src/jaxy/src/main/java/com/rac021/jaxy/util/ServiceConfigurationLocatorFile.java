
package com.rac021.jaxy.util ;

import java.io.File ;
import static com.rac021.jaxy.messages.Displayer.display ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.messages.Displayer.displayLine ;

/**
 *
 * @author ryahiaoui
 */
public class ServiceConfigurationLocatorFile {
    
    public static void locate()              {
        
        /** If no file congifuration where provided . */
        
        if ( System.getProperty("serviceConf") == null )            {

            /** search in the same forlder where jar is launched . */
             
            File conf = new File(System.getProperty("user.dir") +  File.separator + "serviceConf.yaml") ;

            if ( ! conf.exists()) {
            
                 /** else search in the forlder of the jar . */
                 
                File fi = new File(System.getProperty("java.class.path")) ;
                
                conf = new File(fi.getAbsoluteFile().getParentFile() + File.separator + "serviceConf.yaml") ;
                
                if( conf.exists()) {
                   
                    System.setProperty("serviceConf", conf.getAbsolutePath()) ;
                    
               } else {

                   display( message("error"))                            ;
                   display( message("no_conf_provided"))                 ;
                   display( message("restart_service_providing_conf"))   ;
                   display( message("restart_command"))                  ;
                   display( message("where_service_is"))                 ;
                   display( message("its_used_for_config_and_database")) ;
                   display( message("new_line"))                         ;
                   System.exit(0)                                        ;
               }
          } 
          else {
             System.setProperty("serviceConf", conf.getAbsolutePath() )  ;
          }
        }
        
        else {
            
            if( ! new File(System.getProperty("serviceConf")).exists() ) {
            
                File conf = new File( System.getProperty("user.dir")     +  
                                      File.separator                     +
                                      System.getProperty("serviceConf")) ;
                
                if( conf.exists()) {
                    System.setProperty( "serviceConf", 
                                        conf.getAbsolutePath() )         ;
                }
                else {
                    
                    display( message("config_file_not_found")            +
                             System.getProperty("serviceConf") )         ;
                    display( message("new_line")   )                     ;
                    System.exit(0)                                       ;
                }
            }
            
            File conf = new File(System.getProperty("serviceConf")     ) ;
           
            /** This will be used at the deployment time by
               YamlConfigurator . */
            System.setProperty( "serviceConf", conf.getAbsolutePath()  ) ;
 
        }
      
        display( message("provided_configuration") + System.getProperty("serviceConf")) ;
       
        displayLine()                                                                   ;
    }
}
