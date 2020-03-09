
package com.rac021.jaxy.security_provider.configuration ;

import java.nio.file.Path ;
import java.nio.file.Paths ;

/**
 *
 * @author ryahiaoui
 */
public class PathCalculator {
    
    
    public static String getAbsolutPathFromRelatifPathFor ( String relativePath ,
                                                            String absolutePath ) {
     
        if( relativePath == null || relativePath.isEmpty() ) return null ;
        Path pathBase     = Paths.get(relativePath)                      ;
        Path pathAbsolute = Paths.get(absolutePath)                      ;
        
        return pathAbsolute.resolve(pathBase).normalize()
                           .toString()                  ;
    }
 
}
