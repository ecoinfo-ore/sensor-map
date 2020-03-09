
package entry;

import java.io.File ;
import java.util.Map ;
import java.util.HashMap ;
import java.util.Objects ;
import java.io.InputStream ;
import java.io.IOException ;
import java.io.BufferedReader ;
import java.io.FileInputStream ;
import java.io.InputStreamReader ;

/**
 *
 * @author ryahiaoui
 */

public class Configuration  {
    
    private static final String DELIMITTER = "="   ;
    
    static Map<String, String> map = new HashMap() ;
     
    
    public static void initDisp( String path ) throws IOException         {
       
       if( path == null ) return  ;
        
       InputStream resourceAsStream = new FileInputStream(new File(path)) ;
       
       Objects.requireNonNull( resourceAsStream )                         ;
       
       try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            
            String line ;
           
            while ((line = reader.readLine()) != null)     {
                
                if (line.trim().length()==0)     continue  ;
                if (line.trim().startsWith("#")) continue  ;
                if ( ! line.contains("="))       continue  ;
                
                int delimPosition = line.indexOf(DELIMITTER)  ;
                String key = line.substring(0, delimPosition-1).trim()              ;
                String value = line.substring(delimPosition+1).replace("\\n", "\n") ;
                map.put(key, value)                                                 ;
            }     
        }
    }

    public static String get( String key)                 {
       String message = map.getOrDefault(key, null)       ;
       return ( message != null ) ? message.trim() : null ;
    }

}
