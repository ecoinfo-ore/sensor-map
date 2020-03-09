
package com.rac021.jaxy.api.configuration ;

/**
 *
 * @author ryahiaoui
 */

public interface IConfigurator {
    
    public default Long getValidRequestTimeout() {
        return 5l /** in seconds . */ ;
    }
}
