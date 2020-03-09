
package com.rac021.jaxy.util ;

import org.wildfly.swarm.Swarm ;
import org.wildfly.swarm.io.IOFraction ;
import org.wildfly.swarm.config.io.Worker ;
import org.wildfly.swarm.config.io.BufferPool ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */

public class WorkerFractionConfigurator {
    
    public static void configurate( Swarm swarm          ,
                                    YamlConfigurator cfg ) {
       
      Worker worker = new Worker("default") ;
      worker.ioThreads(cfg.getIoThreads()).taskMaxThreads(cfg.getTaskMaxThreads())          ;
      swarm.fraction(new IOFraction().worker(worker).bufferPool(new BufferPool("default"))) ;

    }
    
}
