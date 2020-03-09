
package com.rac021.jaxy.api.exceptions ;

import javax.enterprise.context.Dependent ;
import org.eclipse.microprofile.faulttolerance.FallbackHandler ;
import org.eclipse.microprofile.faulttolerance.ExecutionContext ;

/**
 *
 * @author ryahiaoui
 */

@Dependent
public class FallbackHandlerException implements FallbackHandler<Object> {

  @Override
  public Object handle(ExecutionContext context) {
     
     System.out.println(" ***** FallbackHandlerException ***** " )    ;
     throw new RuntimeException(" FallbackHandler Exception Raised ") ;
  }
}