
package com.rac021.jaxy.api.crypto ;

import java.util.Map ;
import javax.crypto.Cipher ;
import java.lang.reflect.Field ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.lang.reflect.Modifier ;
import java.lang.reflect.Constructor ;
import java.security.NoSuchAlgorithmException ;
import java.lang.reflect.InvocationTargetException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

public class JceSecurity {
 
    private static final Logger LOGGER  = getLogger() ;
    
    public static void unlimit() {
        
       try {
           
           int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES") ;
           
           LOGGER.log(Level.INFO,"                                                ") ;
           LOGGER.log(Level.INFO, " **********************************************") ;
           LOGGER.log(Level.INFO, " AES Crypto maxKeyLen --> {0}   ", maxKeyLen)     ;
           LOGGER.log(Level.INFO," **********************************************")  ;
           LOGGER.log(Level.INFO,"                                               ")  ;
           
           Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection")     ;

           Constructor con = c.getDeclaredConstructor()                    ;
           con.setAccessible(true)                                         ;
           Object allPermissionCollection = con.newInstance()              ;
           Field f = c.getDeclaredField("all_allowed")                     ;
           f.setAccessible(true)                                           ;
           f.setBoolean(allPermissionCollection, true)                     ;

           c = Class.forName("javax.crypto.CryptoPermissions")             ;
           con = c.getDeclaredConstructor()                                ;
           con.setAccessible(true)                                         ;
           Object allPermissions = con.newInstance()                       ;
           f = c.getDeclaredField("perms")                                 ;
           f.setAccessible(true)                                           ;
            
           ((Map) f.get(allPermissions)).put("*", allPermissionCollection) ;

           c = Class.forName("javax.crypto.JceSecurityManager")            ;
           f = c.getDeclaredField("defaultPolicy")                         ;
           f.setAccessible(true)                                           ;
           Field mf = Field.class.getDeclaredField("modifiers")            ;
           mf.setAccessible(true)                                          ;
           mf.setInt(f, f.getModifiers() & ~Modifier.FINAL)                ;
           f.set(null, allPermissions)                                     ;
      
       } catch( ClassNotFoundException | IllegalAccessException   | 
                IllegalArgumentException | InstantiationException | 
                NoSuchFieldException | NoSuchMethodException      | 
                SecurityException | InvocationTargetException     |
                NoSuchAlgorithmException ex ) {

           LOGGER.log(Level.SEVERE, ex.getMessage(), ex)          ;
       }
    }
    
    /*
    * Do the following, but with reflection to bypass access checks:
    *
    * JceSecurity.isRestricted = false;
    * JceSecurity.defaultPolicy.perms.clear();
    * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
         
    final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
    final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
    final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

    final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
    isRestrictedField.setAccessible(true);
    final Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(isRestrictedField, isRestrictedField.getModifiers() & ~Modifier.FINAL);
    isRestrictedField.set(null, false);

    final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
    defaultPolicyField.setAccessible(true);
    final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

    final Field perms = cryptoPermissions.getDeclaredField("perms");
    perms.setAccessible(true);
    ((Map<?, ?>) perms.get(defaultPolicy)).clear();

    final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
    instance.setAccessible(true);
    defaultPolicy.add((Permission) instance.get(null));
    
    */
   
}
