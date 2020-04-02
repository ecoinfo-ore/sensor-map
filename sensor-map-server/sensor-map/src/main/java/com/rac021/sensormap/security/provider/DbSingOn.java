
package com.rac021.sensormap.security.provider ;

import javax.inject.Singleton ;
import com.rac021.sensormap.api.security.Db ;
import com.rac021.sensormap.api.security.ISignOn ;
import com.rac021.sensormap.api.exceptions.BusinessException ;

/**
 *
 * @author ryahiaoui
 */


@Db
@Singleton
// @RequestScoped
// @Transactional
// @Dependent

public class DbSingOn implements ISignOn {

    @Override
    public boolean checkIntegrity(String token, Long expiration ) throws BusinessException {
       throw new BusinessException("Not supported yet.") ;
    }

    @Override
    public boolean checkIntegrity(String login, String timeStamp, String signature) throws BusinessException {
       throw new BusinessException("Not supported yet.") ;
    }
}
