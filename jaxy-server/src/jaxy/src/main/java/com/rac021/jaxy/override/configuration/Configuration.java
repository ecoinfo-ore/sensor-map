
package com.rac021.jaxy.override.configuration ;

import javax.ejb.Startup ;
import javax.ejb.DependsOn ;
import javax.ejb.Singleton ;
import javax.inject.Inject ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */

@Startup
@Singleton
@DependsOn("DefaultStreamerConfigurator")

public class Configuration {

    @Inject
    YamlConfigurator configurator;

    @PostConstruct
    public void initAndOverrideConfiguration()    {

        /** Result Select = Ratio * selectSize . */
        // ratio = configurator.getRatio() ;

        /**
         * MaxThreads per Request, Changing this value will Override MaxThreads for all Serivces .
         */
         //maxThreads = 4 ;

        /**
         * ResponseCacheSize : Total Data in Memory before flush - Response Size .
         */
        // responseCacheSize = configurator.getResponseCacheSize() ;

        /** Size of the Queue                          . */
        // selectSize = configurator.getSelectSize() ;

        /** Thread Pool Size -> Application Scope      . */
        // threadPoolSize = 17 ;
        
        /** Applying Modifications ... ( Mandatory )   . */
        // initPoolProducer() ;

        /*/ Total Concurrent Users Treated in parallel . */
        // maxConcurrentUsers = 1 ;
        
        /** Applying Modifications ... ( Mandatory )   . */
        // initSemaphoreConcurrentUsers() ;

    }
}
