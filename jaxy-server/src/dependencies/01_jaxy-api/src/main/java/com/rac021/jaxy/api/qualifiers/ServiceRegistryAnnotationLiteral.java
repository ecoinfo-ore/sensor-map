
package com.rac021.jaxy.api.qualifiers ;

import javax.enterprise.util.AnnotationLiteral ;

/**
 *
 * @author ryahiaoui
 */

public class ServiceRegistryAnnotationLiteral extends    AnnotationLiteral<ServiceRegistry>  
                                              implements ServiceRegistry                  {  
    
    private final String value ;
    
    private ServiceRegistryAnnotationLiteral(String value) {
        this.value = value ;
    }
    @Override
    public String value() {
        return value ;
    }
    public static ServiceRegistryAnnotationLiteral serviceRegistry ( String value ) {
        return new ServiceRegistryAnnotationLiteral(value) ;
    }
}
