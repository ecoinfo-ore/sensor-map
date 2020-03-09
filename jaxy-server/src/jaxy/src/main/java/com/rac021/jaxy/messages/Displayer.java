
package com.rac021.jaxy.messages ;

import java.util.Map ;
import java.util.HashMap ;
import javax.ejb.Startup ;
import java.io.IOException ;
import java.io.InputStream ;
import javax.ejb.Singleton ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import javax.annotation.PostConstruct ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
@Startup
public class Displayer {
    
    private String DELIMITTER       = "="           ;
    
    static  Map<String, String> map = new HashMap() ;
    
    private String PATH = "com/rac021/jaxy/messages/messages.properties" ;
     
    @PostConstruct
    public void init() {
    }
    
    public Displayer() {
        
        try {
            initDisp()          ;
        } catch( Exception ex ) {
            throw new RuntimeException(ex ) ;
        }
    }
    
    private void initDisp() throws IOException {
       
       InputStream resourceAsStream =  getClass().getClassLoader()
                                                 .getResource( PATH ).openStream() ;
       
       if( resourceAsStream == null ) return ;
       
       try (BufferedReader reader = new BufferedReader( new InputStreamReader(resourceAsStream))) {
            
            String line ;
           
            while ((line = reader.readLine()) != null) {
                
                if (line.trim().length()==0) continue        ;
                if (line.charAt(0)=='#')     continue        ;
                int delimPosition = line.indexOf(DELIMITTER) ;
                String key   = line.substring(0, delimPosition-1).trim()            ;
                String value = line.substring(delimPosition+1).replace("\\n", "\n") ;
                map.put(key, value)                                                 ;
            }     
        }
    }

    public static String message( String key)   {
      return map.getOrDefault(key, "null_mssg") ;
    }
    
    public static void display( String message) {
        System.out.println( message ) ;
    }
    
    public static void displayLine() {
        System.out.println( "\n" )   ;
    }
}

