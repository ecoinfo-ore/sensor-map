
package com.rac021.jaxy.api.qualifiers ;

/**
 *
 * @author ryahiaoui
 */

import javax.inject.Qualifier ;
import java.lang.annotation.Target ;
import java.lang.annotation.Retention ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import static java.lang.annotation.ElementType.TYPE ;
import static java.lang.annotation.ElementType.FIELD ;
import static java.lang.annotation.ElementType.METHOD ;
import static java.lang.annotation.ElementType.PARAMETER ;
import static java.lang.annotation.RetentionPolicy.RUNTIME ;

@Qualifier
@Retention(RUNTIME)
@Target ( { FIELD, TYPE, METHOD, PARAMETER } )

public @interface Format {

  AcceptType value() ;

}
