
package com.rac021.jaxy.security_provider ;

import javax.ejb.Stateless ;
import com.rac021.jaxy.api.security.ISignOn ;
import com.rac021.jaxy.api.qualifiers.security.Db ;
import com.rac021.jaxy.api.configuration.IConfigurator ;
import com.rac021.jaxy.api.exceptions.BusinessException ;

/**
 *
 * @author ryahiaoui
 */

@Stateless
@Db
public class DbSingOn implements ISignOn {

    @Override
    public boolean checkIntegrity(String token, Long expiration ) throws BusinessException {
       throw new BusinessException("Not supported yet.") ;
    }

    @Override
    public boolean checkIntegrity(String login, String timeStamp, String signature) throws BusinessException {
       throw new BusinessException("Not supported yet.") ;
    }

    @Override
    public IConfigurator getConfigurator()  throws BusinessException {
       throw new BusinessException("Not supported yet.") ;
    }
    
}
