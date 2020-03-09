
package com.rac021.jaxy.api.caller ;

import java.util.concurrent.Callable ;

/**
 *
 * @author ryahiaoui
 */

public class UncheckCall {
    
    public static <T> T uncheckCall(Callable<T> callable) {
        try {
            return callable.call()         ;
        } catch (Exception ex)             {
            throw new RuntimeException(ex) ;
        }
    }
}
