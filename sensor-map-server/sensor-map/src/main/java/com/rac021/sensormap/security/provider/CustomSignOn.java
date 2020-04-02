
package com.rac021.sensormap.security.provider ;

import java.util.List ;
import java.time.Instant ;
import javax.inject.Singleton ;
import java.util.logging.Level ;
import javax.persistence.Query ;
import java.util.logging.Logger ;
import javax.persistence.EntityManager ;
import javax.transaction.Transactional ;
import javax.persistence.PersistenceContext ;
import java.security.NoSuchAlgorithmException ;
import com.rac021.sensormap.api.security.Custom ;
import com.rac021.sensormap.api.crypto.Digestor ;
import com.rac021.sensormap.api.security.ISignOn ;
import com.rac021.sensormap.api.exceptions.BusinessException ;
import static com.rac021.sensormap.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author yahiaoui
 */

@Singleton
@Custom
// @Dependent
public class CustomSignOn implements ISignOn         {

    @PersistenceContext 
    EntityManager    entityManager ;
            
    private static final Logger LOGGER = getLogger() ;
    
    public CustomSignOn() {
    }
    
    private final String SHA1   = "SHA1"  ;
    private final String SHA2   = "SHA2"  ;
    private final String MD5    = "MD5"   ;
    private final String PLAIN  = "PLAIN" ;
    
    @Override
    public boolean checkIntegrity( String token, Long expiration ) throws BusinessException {
        
        String _token[] = token.replaceAll(" + ", " ").split(" ") ;
        if(_token.length < 3 ) return false                       ;
        
        if( expiration != null && expiration > 0  )               {
            
            long clientTime   = Long.parseLong( _token[1])        ;
            long now          = Instant.now().getEpochSecond()    ;
            
            if( clientTime > now ) {
                throw  new BusinessException(" Error : Not Expected TimeStamp >>> " ) ;
            }
            
            long expiredTime  = now - expiration ;
            
            if( clientTime < expiredTime )  {
                throw  new BusinessException( " Error : TimeStamp expired // -->  "
                                             + "Expiration delay = " + expiration 
                                             + " second(s) " ) ;
            }
        }
        
        return checkIntegrity(_token[0].trim(), _token[1].trim(), _token[2].trim()) ;
    }
    
    @Override
    @Transactional
    public boolean checkIntegrity( String _login       ,  
                                   String _timeStamp   , 
                                   String _clientSign  ) throws BusinessException {
        
       String tableName          = "users"    ; // configurator.getTableName()          ;
       String loginColumnName    = "login"    ; // configurator.getLoginColumnName()    ;
       String passwordColumnName = "password" ; // configurator.getPasswordColumnName() ;
       
       Query query = entityManager.createNativeQuery( " SELECT " + passwordColumnName + 
                                                      " FROM   " + tableName          + 
                                                      " WHERE  " + loginColumnName    +
                                                      " = ?    " )                    ;
       query.setParameter(1 , _login )                                                ;
        
       List<String> passwordList = query.getResultList() ;
       
       if ( passwordList.isEmpty()) return false         ;
       
       String loginSignature      = "PLAIN" ; // configurator.getLoginSignature()    ;
       String passwordSignature   = "MD5"   ; //configurator.getPasswordSignature()  ;
       String storedPassword      = "MD5"   ; // configurator.getPasswordStorage()   ;
       String timeStampSiignature = "PLAIN" ; //configurator.getTimeStampSignature() ;
       String algo                = "SHA2"  ; //configurator.getAlgoSign()           ;
       String dbPassword          = passwordList.get(0)                              ;
       
       String login               = _login     ;
       String password            = dbPassword ;
       String timeStamp           = _timeStamp ;
        
        /** Treat Strategy . */
        
        if( loginSignature.equalsIgnoreCase(SHA1))                  {
           try {
               login = Digestor.toString(Digestor.toSHA1(_login ))  ;
           } catch (NoSuchAlgorithmException ex)                    {
              LOGGER.log(Level.SEVERE, ex.getMessage(), ex )        ;
           }
        }       
        
        else if ( loginSignature.equalsIgnoreCase(MD5))             {
           try {
               login =  Digestor.toString( Digestor.toMD5(_login )) ;
           } catch (NoSuchAlgorithmException ex)                    {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex)        ;
           }
        }   
        
        else if ( loginSignature.equalsIgnoreCase(SHA2))               {
           try {
               login =  Digestor.toString( Digestor.toSHA256(_login )) ;
           } catch (NoSuchAlgorithmException ex)                       {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex)           ;
           }
        }       
        
        if ( passwordSignature.equalsIgnoreCase(PLAIN) && 
             storedPassword.equalsIgnoreCase(SHA1)     ||
             passwordSignature.equalsIgnoreCase(SHA1)  && 
             storedPassword.equalsIgnoreCase(PLAIN)  )  {
           try {
               password =  Digestor.toString( Digestor.toSHA1( password )) ;
           } catch (NoSuchAlgorithmException ex) {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
           }
        }
        
        else if ( passwordSignature.equalsIgnoreCase(PLAIN) && 
                  storedPassword.equalsIgnoreCase(MD5)      ||
                  passwordSignature.equalsIgnoreCase(MD5)   && 
                  storedPassword.equalsIgnoreCase(PLAIN)  )  {
           try {
               password =  Digestor.toString ( Digestor.toMD5(password ) ) ;
           } catch (NoSuchAlgorithmException ex) {
              LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
           }
        }
        else if ( passwordSignature.equalsIgnoreCase(PLAIN) && 
                  storedPassword.equalsIgnoreCase(SHA2)     ||
                  passwordSignature.equalsIgnoreCase(SHA2)  && 
                  storedPassword.equalsIgnoreCase(PLAIN)  )  {
           try {
               password =  Digestor.toString ( Digestor.toSHA256(password ) ) ;
           } catch (NoSuchAlgorithmException ex) {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
           }
        }
        
        if( timeStampSiignature.equalsIgnoreCase(SHA1)) {
           try {
               timeStamp =  Digestor.toString ( Digestor.toSHA1(_timeStamp )) ;
           } catch (NoSuchAlgorithmException ex)             {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
           }
        }
        else if( timeStampSiignature.equalsIgnoreCase(MD5))  {
           try {
               timeStamp =  Digestor.toString ( Digestor.toMD5(_timeStamp ) ) ;
           } catch (NoSuchAlgorithmException ex)             {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
           }
        }
        else if( timeStampSiignature.equalsIgnoreCase(SHA2)) {
           try {
               timeStamp =  Digestor.toString ( Digestor.toSHA256(_timeStamp ) ) ;
           } catch (NoSuchAlgorithmException ex)             {
               LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
           }
        }
              
        try {
            
           if( algo.equalsIgnoreCase(SHA1) ) {
            String calculatedSign =  Digestor.toString( Digestor.toSHA1( login        + 
                                                                         password     + 
                                                                         timeStamp )) ;
                if( calculatedSign.equals(_clientSign) )    {
                    // ISignOn.ENCRYPTION_KEY.set(password) ;
                    return true ; 
                }
           }
           
           else if( algo.equalsIgnoreCase(MD5) )     {
            String calculatedSign =  Digestor.toString ( Digestor.toMD5 ( login       + 
                                                                          password    + 
                                                                          timeStamp ) ) ;
                if( calculatedSign.equals(_clientSign) )    {
                    // ISignOn.ENCRYPTION_KEY.set(password) ;
                    return true ; 
                }
           }
           
           else if(algo.equalsIgnoreCase(SHA2)) {
            String calculatedSign =  Digestor.toString ( Digestor.toSHA256( login       + 
                                                                            password    + 
                                                                            timeStamp ) ) ;
                if(calculatedSign.equals(_clientSign))     {
                   // ISignOn.ENCRYPTION_KEY.set(password) ;
                    return true ; 
                }
           }
           
        } catch (NoSuchAlgorithmException ex)             {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
        }

        return false ;
    }
}
