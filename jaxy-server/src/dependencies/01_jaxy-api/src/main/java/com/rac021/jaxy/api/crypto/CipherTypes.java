
package com.rac021.jaxy.api.crypto ;

import java.util.List ;
import java.util.logging.Logger ;
import java.util.logging.Level ;
import java.util.stream.Collectors ;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import static com.rac021.jaxy.api.caller.UncheckCall.uncheckCall ;

/**
 *
 * @author ryahiaoui
 */

public enum CipherTypes {
    
    AES_128_CBC    ,
    AES_128_ECB    ,
    AES_192_CBC    ,
    AES_256_ECB    ,
    AES_256_CBC    ,
    AES_192_ECB    ,
    DESede_192_CBC ,
    DESede_192_ECB ,
    DES_64_CBC     ,
    DES_64_ECB     ;

    private static final Logger LOGGER = getLogger()            ;
    
    public static List<CipherTypes> toList( List<String> list ) {
      
      if( list == null ) return null ;
      
      try {
        return list.stream()
                   .map( content_t ->  uncheckCall ( () -> searchEnum( CipherTypes.class , 
                                                                       content_t.trim()  , 
                                                                       "CipherTypes" ) ) )
                   .collect(Collectors.toList()) ;
        } catch( Exception ex ) {
           LOGGER.log(Level.SEVERE, "                                         " ) ;
           LOGGER.log(Level.SEVERE, " *************************************** " ) ;
           LOGGER.log(Level.SEVERE, "                                         " ) ;
           LOGGER.log(Level.SEVERE, " Exception in CipherTypes List : {0}", list) ;
           LOGGER.log(Level.SEVERE, " {0}             ",           ex.getCause()) ;
           LOGGER.log(Level.SEVERE, "                                         " ) ;
           LOGGER.log(Level.SEVERE, " *************************************** " ) ;
           LOGGER.log(Level.SEVERE, "                                         " ) ;
           throw new RuntimeException(ex)                                         ;
        }
    }
    
    public static CipherTypes toCipherTypes( String cipherTypes ) throws BusinessException {
         return  searchEnum( CipherTypes.class, cipherTypes, "CipherTypes" ) ;
    }

    
    private static <T extends Enum<?>> T searchEnum( Class<T> enumeration ,
                                                     String search        ,
                                                     String enumName )    {
      if ( search == null) {
         throw new RuntimeException(  " CipherTypes  [  NULL ] "
                                      + " not supported !  " ) ;
      }
      if ( search.trim().isEmpty() ) {
          throw new RuntimeException(  " CipherTypes  [  EMPTY ] "
                                       + " not supported !  " )  ;
      }
      for (T each : enumeration.getEnumConstants())                 {
          if (each.name().compareToIgnoreCase(search.trim()) == 0 ) {
              return each                                           ;
          }
      }
       
      throw new RuntimeException( " " + enumName + "  [ " + search + " ] " +
                                  " doesn't exists ! ")                    ;
    }

}
