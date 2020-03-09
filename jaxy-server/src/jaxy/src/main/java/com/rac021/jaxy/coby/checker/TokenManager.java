
package com.rac021.jaxy.coby.checker ;

import com.rac021.jaxy.api.exceptions.BusinessException;
import java.util.Objects ;

/**
 *
 * @author yahiaoui
 */

public  class  TokenManager {

    public static String getLogin ( String token ) throws BusinessException {
        
        try {
          return token.replaceAll(" + ", " ").split(" ", 2)[0].trim() ;
       
        } catch( Exception x ) {
            throw new BusinessException( "\n Error : Login Extraction encountered"
                                         + " while Pasing Token \n  " ) ;
        }
    }
    
    public static String getJob ( String token ) throws BusinessException { //cpichot
  
        try {
          return token.replaceAll(" + ", " ").split(" ", 4)[3].trim() ;
       
        } catch( Exception x ) {
            throw new BusinessException( token.toString() + "\n Error : Job Extraction encountered"
                                         + " while Pasing Token \n  " ) ;
        }
    }
        
    public static String extractQuery(String login_query ) {
        Objects.requireNonNull( login_query) ;
//cpichot        return login_query.replaceAll(" + ", " ").split(" ", 2)[1].trim()  ;
        return login_query.replaceAll(" + ", " ").split(" ", 3)[2].trim()  ;
    }
    
    public static String extractJob(String login_query ) { //cpichot
        Objects.requireNonNull( login_query) ;
        return login_query.replaceAll(" + ", " ").split(" ", 3)[1].trim()  ;
    }
        
    public static String builPathLog( String loggerFile, String login ) {
        return loggerFile + "_logs." + login  + ".log"  ;
    }

    public static String buildOutputFolder( String outputDataFolder, String login ) {
        return outputDataFolder.trim().endsWith("/")     ?  
                  outputDataFolder.trim() +  login       :
                  outputDataFolder.trim() + "/" +login   ;
    }
 
}
