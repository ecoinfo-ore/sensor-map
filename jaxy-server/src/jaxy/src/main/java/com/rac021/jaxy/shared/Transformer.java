
package com.rac021.jaxy.shared ;

import java.util.Map ;
import java.util.stream.Stream ;
import java.util.stream.Collectors ;

/**
 *
 * @author ryahiaoui
 */

public class Transformer {
  
    public static String getSnakeName( String serviceCode) {
        
       return Stream.of(serviceCode.split("(?<=\\p{Ll})(?=\\p{Lu})|(?<=\\p{L})(?=\\p{Lu}\\p{Ll})"))
                    .map(key -> key.toLowerCase()).collect(Collectors.joining("_")) ;
       
    }
    
    public static String  getServiceCode ( final Map  service )                             {
        
       return ((String ) service.keySet().stream().findFirst().orElse( null )).trim()       ;
    }
    
    public static String getSql(  final Map<String, Object> service , String serviceCode )  {
        
      return ((String) ((Map) service.get(serviceCode)).get("Query")).replaceAll(" +", " ") ;
    }
            
}

