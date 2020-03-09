
package com.rac021.jaxy.client.security ;

import java.util.Objects ;
import com.rac021.jaxy.client.security.algos.Aes ;
import com.rac021.jaxy.client.security.algos.Des ;
import com.rac021.jaxy.client.security.algos.Desede ;

/**
 *
 * @author ryahiaoui
 */

public class FactoryCipher         {

    enum Ciphers {  AES_128_CBC    ,
                    AES_128_ECB    ,
                    AES_192_CBC    ,
                    AES_256_ECB    ,
                    AES_256_CBC    ,
                    AES_192_ECB    ,
                    DESede_192_CBC ,
                    DESede_192_ECB ,
                    DES_64_CBC     ,
                    DES_64_ECB 
    }
    
    public static ICryptor getCipher( String cipher, String password ) {
        
        Objects.requireNonNull( cipher, password ) ;
        
        if( cipher.equalsIgnoreCase(Ciphers.AES_128_CBC.name())) {
            return new Aes( EncDecRyptor._CIPHER_MODE.CBC  , 
                            EncDecRyptor._CIPHER_SIZE._128 , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.AES_128_ECB.name())) {
            return new Aes( EncDecRyptor._CIPHER_MODE.ECB  , 
                            EncDecRyptor._CIPHER_SIZE._128 , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.AES_192_CBC.name())) {
            return new Aes( EncDecRyptor._CIPHER_MODE.CBC  , 
                            EncDecRyptor._CIPHER_SIZE._192 , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.AES_192_ECB.name())) {
            return new Aes( EncDecRyptor._CIPHER_MODE.ECB  , 
                            EncDecRyptor._CIPHER_SIZE._192 , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.AES_256_CBC.name())) {
            return new Aes( EncDecRyptor._CIPHER_MODE.CBC  , 
                            EncDecRyptor._CIPHER_SIZE._256 , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.AES_256_ECB.name())) {
            return new Aes( EncDecRyptor._CIPHER_MODE.ECB  , 
                            EncDecRyptor._CIPHER_SIZE._256 , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.DES_64_CBC.name())) {
            return new Des( EncDecRyptor._CIPHER_MODE.CBC  , 
                            EncDecRyptor._CIPHER_SIZE._64  , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.DES_64_ECB.name())) {
            return new Des( EncDecRyptor._CIPHER_MODE.ECB  , 
                            EncDecRyptor._CIPHER_SIZE._64  , 
                            password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.DESede_192_CBC.name())) {
            return new Desede( EncDecRyptor._CIPHER_MODE.CBC  , 
                               EncDecRyptor._CIPHER_SIZE._192 , 
                               password )                     ;
        }
        
        if( cipher.equalsIgnoreCase(Ciphers.DESede_192_ECB.name())) {
             return new Desede( EncDecRyptor._CIPHER_MODE.ECB , 
                               EncDecRyptor._CIPHER_SIZE._192 , 
                               password )                     ;
        }
        
        throw  new IllegalArgumentException(" No Cipher found ! ") ;
    } 

}
