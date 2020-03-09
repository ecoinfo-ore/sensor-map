
package com.rac021.jaxy.api.crypto ;

import java.util.Arrays ;
import java.util.Base64 ;
import java.util.Objects ;
import javax.crypto.Cipher ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.security.SecureRandom ;
import javax.crypto.spec.SecretKeySpec ;
import javax.crypto.BadPaddingException ;
import javax.crypto.spec.IvParameterSpec ;
import java.security.NoSuchAlgorithmException ;
import javax.crypto.IllegalBlockSizeException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

public abstract class EncDecRyptor  implements ICryptor {
    
    protected  Cipher            cipher         ;
    protected  IvParameterSpec   ivSpec         ;
    protected  byte[]            ivBytes        ;
    protected  SecretKeySpec     secretKeySpec  ;

    protected  _Operation        OPERATION      ;
    
    protected  byte[]            KEY            ;
    
    protected int                SIZE_BYTE_KEY  ;
    
    protected _CIPHER_MODE       CIPHER_TYPE    ;
    protected _CIPHER_SIZE       CIPHER_SIZE    ;
    
    public enum _Operation       { Encrypt, Decrypt  }
    
    public enum _CIPHER_MODE     { CBC, ECB }
    public enum _CIPHER_NAME     { AES, DES }
    public enum _CipherOperation {  dofinal , update }
    public enum _CIPHER_SIZE     { _64, _96 , _128 , _192 , _256 }

    private static final Logger LOGGER = getLogger() ;
    
    
    protected static byte[] randomInitIV( int size ) {
        
       byte[] ivBytes      = new byte[size]     ;
       SecureRandom random = new SecureRandom() ;
       random.nextBytes(ivBytes)                ;
       return ivBytes                           ;
    }
      
    public byte[] getKeyFromPasswordUsingSha256( String password ) throws Exception {
       Objects.requireNonNull(password)             ;
       return toSHA256 ( password , SIZE_BYTE_KEY ) ;
    }
    
    protected byte[] decrypt( String encryptedMessage , 
                              _CipherOperation cipherOperation ) throws Exception {
        
        try {
              int indexOfPoint = encryptedMessage.indexOf(".") ;
                
              byte[] l_ivBytes  = null ;
              byte[] encry      = null ;
                
              if( indexOfPoint != -1 ) {
                 l_ivBytes = Base64.getDecoder().decode( encryptedMessage.substring(0, indexOfPoint) )   ;
                 encry     = Base64.getDecoder().decode( encryptedMessage.substring(indexOfPoint + 1 ) ) ;
                 this.ivSpec = new IvParameterSpec(l_ivBytes) ;
              }
              else {
                 encry = Base64.getDecoder().decode( encryptedMessage ) ;
              }
                
              setOperationMode( OPERATION )                      ;
                
              return cipherOperation == _CipherOperation.dofinal ? 
                                         cipher.doFinal( encry ) : 
                                         cipher.update ( encry ) ;

        } catch ( BadPaddingException | IllegalBlockSizeException ex)  {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
            return null                                   ;
        }
    }
      
    public static byte[]toSHA256( String password, int size ) throws NoSuchAlgorithmException {
        return Arrays.copyOfRange( Digestor.toSHA256(password), 0 , size ) ;
    }
    
    public static byte[]toSHA1( String password, int size ) throws NoSuchAlgorithmException   {
        return Arrays.copyOfRange( Digestor.toSHA1(password), 0 , size )   ;
    }
    
    public static byte[]toMD5( String password, int size ) throws NoSuchAlgorithmException   {
        return Arrays.copyOfRange( Digestor.toMD5(password), 0 , size )   ;
    }

    @Override
    public abstract void setOperationMode( _Operation op )                ;    
    
}
