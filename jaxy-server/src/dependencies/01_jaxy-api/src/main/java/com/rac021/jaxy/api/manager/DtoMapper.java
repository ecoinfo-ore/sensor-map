
package com.rac021.jaxy.api.manager ;

import java.util.Map ;
import java.util.List ;
import java.util.Arrays ;
import java.lang.reflect.Field ;
import java.util.stream.Collectors ;
import com.rac021.jaxy.api.qualifiers.ResultColumn ;
import static com.rac021.jaxy.api.caller.UncheckCall.uncheckCall ;

/**
 *
 * @author ryahiaoui
 */

public class DtoMapper {

    public static <T> T map( Object[]      objectArr    , 
                             Class<T>      genericDto   , 
                             List<String>  keepFields   )             {
        
        List<Field> mappingFields = getAnnotatedFields( genericDto )  ;
        
        try {
                T t = genericDto.newInstance()             ;
                
                for (int i = 0; i < objectArr.length; i++) {
                    
                    if( i < mappingFields.size() )         {
                        
                        Field field = t.getClass()
                                       .getDeclaredField   (
                                        mappingFields.get(i)
                                       .getName()) ;
                        
                        if(  keepFields  != null   &&
                            !keepFields.isEmpty()  && 
                            !keepFields.contains(field.getName())
                            )  continue ;
                        
                           field.setAccessible(true)    ;
                           field.set( t , objectArr[i]) ; 
                    }
                }
                
                return t ;
            
        } catch ( IllegalAccessException | IllegalArgumentException | 
                  InstantiationException | NoSuchFieldException     |
                  SecurityException ex)                             {
            throw new RuntimeException(ex) ;
        }
    }
    
    private static <T> List<Field> getAnnotatedFields (Class<T> genericType )   {
        
        Field[] fields            = genericType.getDeclaredFields()             ;
        
        List<Field> orderedFields = Arrays.asList(new Field[fields.length])     ;
        
        for (int i = 0; i < fields.length; i++)                                 {
            
            if (fields[i].isAnnotationPresent( ResultColumn.class ))            {
                ResultColumn nqrc = fields[i].getAnnotation(ResultColumn.class) ;
                orderedFields.set(nqrc.index(), fields[i])                      ;
            }
        }
        return orderedFields ;
    }
    
    
    public static Map<String, String> extractValuesFromObject ( Object dto              ,
                                                                Class genericType       ,
                                                                List<String> attributes ) {
        Field[] fields = genericType.getDeclaredFields() ;
         
        return Arrays.asList(fields).stream()
                                    .filter( f -> attributes.contains( f.getName()))
                                    .filter( f -> uncheckCall( () -> getValueFromField(f, dto) != null ) )
                                    .collect( Collectors.toMap( f -> f.getName() ,
                                                                f -> uncheckCall( () -> getValueFromField(f, dto) )
                                                              )) ;
    }
    
    private static String getValueFromField( Field f , Object o ) {
      
        if( f == null || o == null ) return null ;
        try {
            f.setAccessible(true)                ;
            return ( f.get(o) == null ) ? null : f.get(o).toString()     ;
        } catch ( IllegalAccessException | IllegalArgumentException ex ) {
          throw new RuntimeException(ex) ; 
        }
    }    
    
    /*
    public static <T> List<T> map( List<Object[]> objectArrayList , 
                                   Class<T>       genericDto      , 
                                   List<String>   keepFields      )   {
        
        List<T> ret = new ArrayList<>()                               ;
        if(objectArrayList.isEmpty()) return ret                      ;
        List<Field> mappingFields = getAnnotatedFields( genericDto )  ;
        
        try {
            for (Object[] objectArr : objectArrayList)     {
                T t = genericDto.newInstance()             ;
                for (int i = 0; i < objectArr.length; i++) {
                    if( i < mappingFields.size() ) {
                        Field field = t.getClass()
                                       .getDeclaredField( mappingFields.get(i)
                                       .getName()) ;
                        if(  keepFields  != null   &&
                            !keepFields.isEmpty()  && 
                            !keepFields.contains(field.getName())
                            )  continue ;
                        
                           // if(field.getAnnotation(Public.class) != null ) {
                           field.setAccessible(true)    ;
                           field.set( t , objectArr[i]) ; 
                           //  }
                    }
                }
                ret.add(t) ;
            }
        } catch ( IllegalAccessException | IllegalArgumentException | 
                  InstantiationException | NoSuchFieldException     |
                  SecurityException ex)                             {
            throw new RuntimeException(ex) ;
        }
        
        return ret ;
    } */    
}

