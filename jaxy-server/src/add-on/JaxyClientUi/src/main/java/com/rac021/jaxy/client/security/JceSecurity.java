
package com.rac021.jaxy.client.security ;

import java.lang.reflect.Field ;
import java.lang.reflect.Modifier ;

/**
 *
 * @author ryahiaoui
 */
public class JceSecurity {
    
    public static void unlimit() {
         
        try {
             /** hack for JCE Unlimited Strength . */
             Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted") ;
             field.setAccessible(true) ;

             Field modifiersField = Field.class.getDeclaredField("modifiers") ;
             modifiersField.setAccessible(true) ;
             modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL) ;

             field.set(null, false) ;
             
        } catch( ClassNotFoundException   | 
                 IllegalAccessException   |
                 IllegalArgumentException | 
                 NoSuchFieldException     |
                 SecurityException ex  )  {
        }
    }
}
