
package com.rac021.jaxy.api.analyzer ;

import java.util.Map ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.regex.Pattern ;
import java.util.stream.Stream ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.util.stream.Collectors ;
import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.jaxy.api.pojos.Query ;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

public enum Lexer {
   
   G   ( "_>_"   ) ,
   L   ( "_<_"   ) ,
   or  ( "_or_"  ) ,
   OR  ( "_OR_"  ) ,
   not ( "_not_" ) ,
   NOT ( "_NOT_" ) ,
   and ( "_and_" ) ,
   AND ( "_AND_" ) ,
   GE  ( "_>=_"  ) ,
   LE  ( "_<=_"  ) ,
   EQ  ( "_=_"   ) ;
   
   private static final Logger LOGGER  = getLogger()        ;

   static final String  expectStrings  = "= > >= < <= != "  ;
   
   static final Pattern patternSyntax  = Pattern.compile("(.*?^(_or|_OR|_and|_AND|_NOT|_not))\"") ;
   
   static final String  splitOnSpaceIgnoreSingleQuote = "\'?( |$)(?=(([^\']*\'){2})*[^\']*$)\'?"  ;
           
   static final ThreadLocal<String> expected = new ThreadLocal<>()                                ;
            
   private final String value                                                                     ;

   
   private Lexer(String value) {
      this.value = value ;
   }

   @Override
   public String toString()
   {
      return this.value ; 
   }
   
   public static List<String> cleanFieldsFilters( Query query , 
                                                  MultivaluedMap<String, String> filedsFilters ) {
       
        List<String> fieldsFiltersList = new ArrayList<>() ;
         
        filedsFilters.forEach((key, list) -> list.forEach( v -> {
            
                                            if( query.getParameters().containsKey(key))                     {
                                                
                                                fieldsFiltersList.add( "( " + query.getParameters().get(key )
                                                                                   .get( Query.FULL_NAME )  +
                                                                       " " +
                                                                       _process( key                            , 
                                                                                 v.replaceAll(" +", " ").trim() , 
                                                                                 query.getParameters() 
                                                                               )
                                                ) ;
                                            }
                                           }
                          )) ;

         LOGGER.log( Level.CONFIG , " Fileds Filters  = {0}", fieldsFiltersList ) ;
       
         return fieldsFiltersList ;
   }

   private static String _process( String key   , 
                                   String value , 
                                   Map<String, Map<String, String>> columnNames ) {
    
       if( ! columnNames.containsKey(key) ) return "" ;
         
       expected.set(expectStrings) ;
         
       if( value.isEmpty()) return " " ;
         
       if( ! value.startsWith("=" )   && 
           ! value.startsWith("_>")   && 
           ! value.startsWith(">")    && 
           ! value.startsWith("<")    && 
           ! value.startsWith("_<")   && 
           ! value.startsWith("_NOT") && 
           ! value.startsWith("_not") )  {
           
           value = "_=_" + value ;
       }
         
       if( ! value.startsWith("_")) value = "_"+ value ;
 
       value = value.replace( "_or"  , " OR "  )
                    .replace( "_OR"  , " OR "  )
                    .replace( "_and" , " AND " )
                    .replace( "_AND" , " AND " )
                    .replace( "_NOT" , " != "  )
                    .replace( "_not" , " != "  )
                    .replace( "_>="  , " >= "  )
                    .replace( "_<="   , " <= " )
                    .replace( "_>"   , " > "   )
                    .replace( "_<"   , " < "   )
                    .replace( "_="   , " = "   ) ;

       return  Stream.of(value.split(splitOnSpaceIgnoreSingleQuote))
                     .map( tok -> {
                              try {
                               return  process( columnNames.get(key).get(Query.FULL_NAME), 
                                                 tok.replaceAll(" +", " ").trim()        ,
                                                   columnNames.get(key).get(Query.TYPE) 
                                               ) ;                  
                              } catch( BusinessException ex ) {
                                 throw new RuntimeException(ex) ;
                              }
                              
                           })
                     .collect(Collectors.joining("")) ;
       
   }
     
   private static String process(String key, String token , String type) throws BusinessException {
         
     if( token.isEmpty()) return " " ;
         
     else if( token.equalsIgnoreCase("=")   && expected.get().contains(token.toLowerCase()) )  { 
         expected.set("value") ; return "= " ;            
     } 
         
     else if( token.equalsIgnoreCase("or")  && expected.get().contains(token.toLowerCase()) )  {  
         expected.set(">= and > < <= != ") ; return " OR (" + key ;   
     }
         
     else if( token.equalsIgnoreCase(">=")  && expected.get().contains(token.toLowerCase()) )  { 
         expected.set("value")  ; return " >= " ;          
     }
         
     else if( token.equalsIgnoreCase("<=")  && expected.get().contains(token.toLowerCase()) )  { 
         expected.set("value")  ; return " <= " ;          
     }
         
     else if( token.equalsIgnoreCase("and") && expected.get().contains(token.toLowerCase()) )  { 
         expected.set(">= > = < <= or != ") ; return " AND (" + key  ; 
     }
         
     else if( token.equalsIgnoreCase(">")   && expected.get().contains(token.toLowerCase()) )  { 
         expected.set("value") ; return "> " ;            
     }
        
     else if( token.equalsIgnoreCase("<")   && expected.get().contains(token.toLowerCase()) )  { 
         expected.set("value") ; return "< " ;            
     }
        
     else if( token.startsWith( "!=" )      && expected.get().contains(token.toLowerCase()) )  { 
         expected.set("value")  ; return  "!= " ;          
     } 
         
     else if( token.startsWith("_")  &&  expected.get().contains("value") ) { 
         
                 expected.set("and or ")                 ; 
                
                 if( type != null && type.contains("Integer")) {
                    return  token.replaceAll("\"", ""  )
                                 .replaceAll("'", ""   )
                                 .replaceFirst("_", "" )
                                 + " ) "  ; 
                 }
                 
                 return "'" + token.replaceAll("\"", ""  )
                                   .replaceAll("'", ""   )
                                   .replaceFirst("_", "" )
                                   + "' ) "  ; 
     }
       
     else if( token.length() > 0 ) { 
         throw new BusinessException ( " token  [ " + token + 
                                       " ] not accepted // Key = " + key ) ;
     }
        
        else return "" ;
   }
  
}
