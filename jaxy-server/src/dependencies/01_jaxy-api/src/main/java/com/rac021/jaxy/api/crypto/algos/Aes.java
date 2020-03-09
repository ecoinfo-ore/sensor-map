
package com.rac021.jaxy.api.crypto.algos ;

import java.util.Base64 ;
import java.util.Objects ;
import javax.crypto.Cipher ;
import javax.crypto.spec.SecretKeySpec ;
import javax.crypto.spec.IvParameterSpec ;
import java.nio.charset.StandardCharsets ;
import java.security.InvalidKeyException ;
import com.rac021.jaxy.api.crypto.EncDecRyptor ;
import java.security.InvalidAlgorithmParameterException ;
;

/**
 *
 * @author ryahiaoui
 */

public class Aes  extends EncDecRyptor    {
  
    private static final int IV_SIZE = 16 ; // Bytes
            
    public Aes( _CIPHER_MODE cipher_type , _CIPHER_SIZE cipher_size, String password ) {
        init(cipher_type, cipher_size, password) ;
     }
     
    private void init( _CIPHER_MODE cipher_type ,
                       _CIPHER_SIZE cipher_size, String password ) {
      
        try {
              Objects.requireNonNull( password ) ;

              CIPHER_TYPE  = cipher_type         ;
              CIPHER_SIZE  = cipher_size         ;

              SIZE_BYTE_KEY = Integer.parseInt( cipher_size.name().replace("_","") ) / 8   ;

              if( cipher_type == _CIPHER_MODE.CBC )            {
                   ivBytes     = randomInitIV( IV_SIZE )       ;
                   this.ivSpec =  new IvParameterSpec(ivBytes) ;
              }

              KEY           = getKeyFromPasswordUsingSha256( password )                    ;

              secretKeySpec = new SecretKeySpec( KEY , _CIPHER_NAME.AES.name() )           ;

              cipher        = Cipher.getInstance( "AES/" + CIPHER_TYPE + "/PKCS5Padding" ) ;
        
        } catch( Exception ex )            {
            throw new RuntimeException(ex) ;
        }
        
    }
    
    @Override
    public void setOperationMode( _Operation op ) {

       this.OPERATION = op ;
       
       try {
           
            if( CIPHER_TYPE == _CIPHER_MODE.CBC ) {
                 cipher.init( op ==  _Operation.Encrypt ? Cipher.ENCRYPT_MODE       : 
                                     Cipher.DECRYPT_MODE , secretKeySpec , ivSpec ) ;
            }
            else if(  CIPHER_TYPE == _CIPHER_MODE.ECB ) {
                 cipher.init( op ==  _Operation.Encrypt ? Cipher.ENCRYPT_MODE : 
                                     Cipher.DECRYPT_MODE , secretKeySpec )    ;
           }
            
       } catch( InvalidAlgorithmParameterException | InvalidKeyException ex )  {
           throw new RuntimeException(ex) ;
       }
       
   }
    
    @Override
    public byte[] process ( String message, _CipherOperation cipherOperation ) {
     
         try {  return OPERATION == _Operation.Decrypt ?
                      decrypt( message, cipherOperation ) :
                       cipherOperation == _CipherOperation.dofinal   ?
                                           cipher.doFinal( message.getBytes(StandardCharsets.UTF_8)) :
                                           cipher.update ( message.getBytes(StandardCharsets.UTF_8)) ;
            
         } catch( Exception ex )            {
             throw new RuntimeException(ex) ;
         }
    }
    
    @Override
    public byte[] decrypt( String encryptedMessage , 
                          _CipherOperation cipherOperation ) throws Exception {
        return super.decrypt(encryptedMessage, cipherOperation) ;
    }

    @Override
    public byte[] getIvBytesEncoded64() {
        return ivBytes != null ?
               Base64.getEncoder().encode(ivBytes)  : null ;
    }
 
}
