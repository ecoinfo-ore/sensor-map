
package com.rac021.ui.beans ;

import javax.inject.Named ; 
import javax.inject.Inject ;
import java.io.Serializable ;
import javax.enterprise.context.ApplicationScoped ;
import com.rac021.jaxy.api.root.ConcurrentUsersManager ;
import com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */
@ApplicationScoped
@Named("globalConf")
public class GlobalConf implements Serializable         {

    @Inject
    transient private YamlConfigurator yamlConfigurator ;
    
    public Integer getMaxConcurrentUsers() {
        return ConcurrentUsersManager.maxConcurrentUsers ;
    }
    
    public String getRejectConnectionsWhenLimitExceeded()                   {
    
        return
          ( yamlConfigurator.getRejectConnectionsWhenLimitExceeded() <= 0 ) ?
            "Disabled" : 
            String.valueOf ( 
                  yamlConfigurator.getRejectConnectionsWhenLimitExceeded()) ;
    }

    public Integer getThreadPoolSize()    {
        return DefaultStreamerConfigurator.threadPoolSizeApp ;
    }

    public Integer getDefaultMaxThread()  {
        return DefaultStreamerConfigurator.defaultMaxThreadsPerService ;
    }

    public Integer getSelectSize()        {
        return DefaultStreamerConfigurator.selectSize ;
    }

    public Integer getResponseCacheSize() {
        return DefaultStreamerConfigurator.responseCacheSize ;
    }

    public Integer getRatio()             {
        return DefaultStreamerConfigurator.ratio ;
    }

    public Integer getWorkerQueue()       {
        return DefaultStreamerConfigurator.WORKER_QUEUE ;
    }

    public Integer getSessionTimeOut()    {
        return YamlConfigurator.getSessionTimeOut() ;
    }

    public String getKeyCloakUrl()        {
       return yamlConfigurator.getKeycloakUrl()     ;
    }

    public Integer getWorkerThreadKeepAlive()       {
        return DefaultStreamerConfigurator.THREAD_KEEPALIVE ;
    }
    
    public String getHost() {

        return yamlConfigurator.getTransport()  + "://" + 
               yamlConfigurator.getHost()       + ":"   +
               yamlConfigurator.getSelectedPort()       ;
    }

    public boolean deployUI() {
        return yamlConfigurator.deployUI() ;
    }
     
    public String getAlgoSigneture()          {
        return yamlConfigurator.getAlgoSign() ;
    }

    public String getLoginSignature()      {
        return yamlConfigurator.getLoginSignature() ;
    }

    public String getPasswordSignature()   {
        return yamlConfigurator.getPasswordSignature() ;
    }

    public String getTimeStampSignature()  {
        return yamlConfigurator.getTimeStampSignature() ;
    }

    public String getValidRequestTimeout() {
        return yamlConfigurator.getValidRequestTimeout().toString() ;
    }
   
}

