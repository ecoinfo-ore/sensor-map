
package com.rac021.sensormap.impl.utils.checker ;

import com.rac021.sensormap.api.exceptions.BusinessException;

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
  
    public static String builPathLog( String loggerFile, String login ) {
        return loggerFile + "_logs." + login  + ".log"  ;
    }
}
