
package com.rac021.sensormap.api.security ;

import com.rac021.sensormap.api.root.RuntimeServiceInfos ;
import com.rac021.sensormap.api.exceptions.BusinessException ;

/**
 *
 * @author ryahiaoui
 */

public interface ISignOn extends RuntimeServiceInfos {

   public boolean checkIntegrity ( String token    , 
                                   Long expiration ) throws BusinessException   ;

   public boolean checkIntegrity( String login     , 
                                  String timeStamp , 
                                  String signature ) throws BusinessException   ;
   
}
