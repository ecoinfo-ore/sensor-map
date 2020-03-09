
package com.rac021.jaxy.api.security ;

import com.rac021.jaxy.api.root.RuntimeServiceInfos ;
import com.rac021.jaxy.api.configuration.IConfigurator ;
import com.rac021.jaxy.api.exceptions.BusinessException ;

/**
 *
 * @author ryahiaoui
 */

public interface ISignOn extends RuntimeServiceInfos {

   public static final ThreadLocal<String> ENCRYPTION_KEY = new ThreadLocal()   ;
   
   public static final ThreadLocal<String> CIPHER         = new ThreadLocal()   ;
   
   public IConfigurator getConfigurator() throws BusinessException              ;
   
   public boolean checkIntegrity ( String token    , 
                                   Long expiration ) throws BusinessException   ;

   public boolean checkIntegrity( String login     , 
                                  String timeStamp , 
                                  String signature ) throws BusinessException   ;
   
}
