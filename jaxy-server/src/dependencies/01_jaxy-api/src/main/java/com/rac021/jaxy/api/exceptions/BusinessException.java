
package com.rac021.jaxy.api.exceptions ;

/**
 *
 * @author yahiaoui
 */

public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message) ;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause) ;
    }
    
}

