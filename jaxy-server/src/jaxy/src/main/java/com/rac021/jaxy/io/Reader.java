
package com.rac021.jaxy.io ;

import java.net.URL ;
import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;

/**
 *
 * @author ryahiaoui
 */
public class Reader {
    
    
    public static String readFile( URL resource ) throws IOException {

        StringBuilder content = new StringBuilder() ;
        
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader(resource.openStream())) ) {
            String line  = null                       ;
            while ((line = reader.readLine()) != null )
                content.append(line).append("\n")     ;
        } catch (IOException ex)                      {
            throw new RuntimeException( ex )          ;
        }
        return content.toString()                     ;
    }

}
