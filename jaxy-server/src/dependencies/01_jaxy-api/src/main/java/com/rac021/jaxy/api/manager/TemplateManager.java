
package com.rac021.jaxy.api.manager ;

import java.util.Map ;
import java.util.List ;
import java.io.IOException ;
import java.nio.file.Files ;
import java.nio.file.Paths ;
import java.util.ArrayList ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;
import java.nio.charset.StandardCharsets ;

/**
 *
 * @author ryahiaoui
 */

public class TemplateManager {
    
    final static Pattern  PATTERN  =  Pattern.compile( "\\{\\{(.*?)\\}\\}" , 
                                                       Pattern.DOTALL )    ;
   
    static String MatcherPart = "(?i)PART(\t| |\n)*\\{\\{\\{(\t| |\n)*.*?(\\}\\}\\})" ;
    
    enum PART { HEADER, BODY, FOOTER }                                                ;
    
    public static List<String> extractArgs( String template ) {
        
       Matcher m = PATTERN.matcher( template )                ;
       
       List<String> vars = new ArrayList<>()                  ;
       
       while (m.find())      {
         vars.add(m.group().replace("{{", "") 
                           .replace("}}", "").trim() )        ;
       }
        
       return vars ;
    }

    public static String applyValue( final Map<String, String> map  , 
                                     final String template          ) {
        
        if( map == null || template == null ) return template ;
        
        StringBuilder templateInstance = new StringBuilder(template) ;
        
        map.forEach(( k, v) -> {
            replaceAllPattern ( templateInstance                     , 
                                Pattern.compile("\\{\\{"+k+"\\}\\}") ,
                                v                                    ) ;
        }) ;
        
        return templateInstance.toString() ;
    }
    
    private static void replaceAllPattern( StringBuilder sb          , 
                                           Pattern       pattern     , 
                                           String        replacement ) {
        
        Matcher m = pattern.matcher(sb) ;
        int start = 0                   ;
        while (m.find(start)) {
            sb.replace(m.start(), m.end(), replacement) ;
            start = m.start() + replacement.length()    ;
        }
    }
    
    public static String removeMultipleBlankSpaces ( String template ) {
       return template.replaceAll("(?s)\\{\\{(\t| |\n)*","{{")
                      .replaceAll("(?s)(\t| |\n)*\\}\\}","}}") ;
    }
    
    public static String extractHeader ( String template ) {
       return extractPart(template, PART.HEADER)           ;
    }
    
    public static String extractFooter ( String template ) {
       return extractPart(template, PART.FOOTER)           ;
    }
    
    public static String extractBody ( String template )   {
       return extractPart(template, PART.BODY)             ;
    }

    private static String extractPart ( String template , PART part ) {
        
        Pattern  pattern  = Pattern.compile( MatcherPart.replace("PART", part.name()) , 
                                              Pattern.DOTALL )                        ;
        
        Matcher match     = pattern.matcher(template)                                 ;
        
        if (match.find()) {
           return match.group().trim().replaceAll( "(?i)" + part.name()               +
                                                   "(\t| |\n)*.*\\{\\{\\{.*(\n)?", "" )
                                      .replaceAll("(\n)?(\t| )*.*\\}\\}\\}", "")      ;
        }
        return "" ;
    }
    
    public static String readFile ( String path ) {
        try {
              return new String( Files.readAllBytes(Paths.get(path)) ,
                                 StandardCharsets.UTF_8 ).trim()     ;
        } catch (IOException ex)           {
            throw new RuntimeException(ex) ;
        }
    }
   
}
