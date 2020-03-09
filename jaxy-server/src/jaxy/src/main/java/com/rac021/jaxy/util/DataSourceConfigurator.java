
package com.rac021.jaxy.util ;

import java.util.Objects ;
import java.util.logging.Logger ;
import com.rac021.jaxy.api.streamers.Streamer ;
import static com.rac021.jaxy.messages.Displayer.message ;
import org.wildfly.swarm.datasources.DatasourcesFraction ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */

public class DataSourceConfigurator {
    
    private static Logger LOGGER       =  getLogger() ;
    
    public static String  driverModule =  null        ;
    
    
    public static DatasourcesFraction getDataSource( YamlConfigurator cfg ) {
        
        String driverClassName = ((String) cfg.getConfiguration().get("driverClassName"))
                                              .replaceAll(" +", " ")
                                              .trim() ;
        String userName        = ((String) cfg.getConfiguration().get("userName"))
                                              .replaceAll(" +", " ").trim() ;
        
        String password        = ((String) cfg.getConfiguration().get("password"))
                                               .replaceAll(" +", " ")
                                               .trim() ;
        
        String connectionUrl   = ((String) cfg.getConfiguration().get("connectionUrl"))
                                              .replaceAll(" +", " ")
                                              .trim() ;

        int maxPoolConnection  = cfg.getMaxPoolConnection()  ;
       
        LOGGER.log(java.util.logging.Level.INFO , message("new_line"))                                 ;
        LOGGER.log(java.util.logging.Level.INFO , message("data_source_config"))                       ;
        LOGGER.log(java.util.logging.Level.INFO , message("driver_class_name"), driverClassName  )     ;
        LOGGER.log(java.util.logging.Level.INFO , message("connection_url")   , connectionUrl     )    ;
        LOGGER.log(java.util.logging.Level.INFO , message("user_name")   , userName       )            ;
        LOGGER.log(java.util.logging.Level.INFO , message("password")     )                            ;
        LOGGER.log(java.util.logging.Level.INFO , message("max_pool_connection") , maxPoolConnection ) ;
        LOGGER.log(java.util.logging.Level.INFO , message("new_line"))                                 ;
        
        Objects.requireNonNull( driverClassName )                                                      ;

        if (driverClassName.toLowerCase().contains("mysql")) {

            driverModule = "com.mysql"                    ;

            return datasourceWithMysql( driverClassName   , 
                                        connectionUrl     , 
                                        userName          , 
                                        password          , 
                                        maxPoolConnection ) ;
        }

        driverModule = "org.postgresql";

        return datasourceWithPostgresql( driverClassName   , 
                                         connectionUrl     , 
                                         userName          ,
                                         password          ,
                                         maxPoolConnection ) ;
    }

    private static DatasourcesFraction datasourceWithMysql( String driverClassName , 
                                                            String connectionUrl   , 
                                                            String userName        ,
                                                            String password        , 
                                                            int maxPoolConnection  ) {

        return new DatasourcesFraction().jdbcDriver(
                                                "com.mysql" ,
                                                (d) -> {
                                                   d.driverClassName(driverClassName)                                     ;
                                                   d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource") ;
                                                   d.driverModuleName("com.mysql")                                        ;
                                                })
                                        .dataSource (
                                                Streamer.PU ,
                                                (ds) -> {
                                                    ds.driverName("com.mysql")                      ;
                                                    ds.connectionUrl(connectionUrl)                 ;
                                                    ds.userName(userName)                           ;
                                                    ds.password(password)                           ;
                                                    ds.jndiName("java:jboss/datasources/Scheduler") ;
                                                    ds.maxPoolSize( maxPoolConnection)              ;
                                                }
                                        ) ;
    }

    private static DatasourcesFraction datasourceWithPostgresql ( String driverClassName , 
                                                                  String connectionUrl   ,
                                                                  String userName        ,
                                                                  String password        ,
                                                                  int maxPoolConnection  ) {

        return new DatasourcesFraction().jdbcDriver(
                                                "org.postgresql",
                                                (d) -> {
                                                    d.driverClassName(driverClassName)                      ;
                                                    d.xaDatasourceClass("org.postgresql.xa.PGXADataSource") ;
                                                    d.driverModuleName("org.postgresql")                    ;
                                                })
                                        .dataSource (
                                                Streamer.PU ,
                                                (ds) -> {
                                                    ds.driverName(driverClassName.replace(".Driver", "")) ;
                                                    ds.connectionUrl(connectionUrl)                       ;
                                                    ds.userName(userName)                                 ; 
                                                    ds.password(password)                                 ;
                                                    ds.jndiName("java:jboss/datasources/Scheduler")       ;
                                                    ds.maxPoolSize( maxPoolConnection )                   ;
                                                }
                                        ) ;
    }

}

