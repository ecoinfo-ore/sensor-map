
package com.rac021.jaxy.ghosts.services.manager ;

import java.net.URL ;
import java.io.File ;
import java.util.Map ;
import java.util.List ;
import java.util.Arrays ;
import javax.ejb.Startup ;
import java.util.Optional ;
import java.nio.file.Path ;
import javax.ejb.DependsOn ;
import java.nio.file.Files ;
import javax.inject.Inject ;
import java.nio.file.Paths ; 
import javax.ejb.Singleton ; 
import java.io.IOException ;
import java.util.ArrayList ;
import java.sql.Connection ;
import java.util.Comparator ;
import java.sql.SQLException ;
import java.lang.reflect.Field ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.api.pojos.Query ;
import javax.persistence.EntityManager ;
import javax.enterprise.inject.spi.Bean ;
import com.rac021.jaxy.messages.Displayer ;
import com.rac021.jaxy.shared.Transformer ;
import javax.persistence.PersistenceContext ;
import javax.enterprise.inject.spi.Unmanaged ;
import com.rac021.jaxy.unzipper.UnzipUtility ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.streamers.Streamer ;
import com.rac021.jaxy.api.crypto.CipherTypes ;
import javax.enterprise.inject.spi.BeanManager ;
import com.rac021.jaxy.api.qualifiers.SqlQuery ;
import com.rac021.jaxy.api.root.ServicesManager ;
import com.rac021.jaxy.api.analyzer.SqlAnalyzer ;
import static java.util.stream.Collectors.toList ;
import com.rac021.jaxy.ee.grafana.GrafanaManager ;
import com.rac021.jaxy.ee.metrics.MetricsManager ;
import static com.rac021.jaxy.io.Reader.readFile ;
import javax.enterprise.context.ApplicationScoped ; 
import static java.util.stream.Collectors.joining ;
import com.rac021.jaxy.compilation.CompilerManager ;
import com.rac021.jaxy.api.manager.TemplateManager ;
import com.rac021.jaxy.api.qualifiers.security.Policy ;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry ;
import com.rac021.jaxy.api.qualifiers.ResourceRegistry ;
import com.rac021.jaxy.api.root.ConcurrentUsersManager ;
import com.rac021.jaxy.api.streamers.MarshallerXmlJson ;
import org.eclipse.microprofile.metrics.MetricRegistry ;
import static com.rac021.jaxy.messages.Displayer.message ;
import javax.enterprise.inject.spi.Unmanaged.UnmanagedInstance ;
import com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator ;
import static com.rac021.jaxy.api.caller.UncheckCall.uncheckCall ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
@Startup
@ApplicationScoped

@DependsOn("ServicesManager")

public class GhostServicesManager    {

    @Inject
    MetricRegistry   metrics         ;

    @Inject
    ServicesManager  servicesManager ;

    @Inject
    YamlConfigurator yamlConfigurator ;

    @Inject
    MarshallerXmlJson marshaller      ;
    
    @Inject
    private BeanManager bm            ;

    @Inject
    private Displayer   displayer     ;
    
    @PersistenceContext(unitName = Streamer.PU )
    private EntityManager        entityManager                          ;
    
    private static final String  VIRTUAL_FILE_SYSTEM = "java.io.tmpdir" ;

    private static final Logger  LOGGER              = getLogger()      ;

    
    private void processGhostService( String     serviceCode ,  
                                      Connection cnn         , 
                                      String     sql         ) throws Exception { 

        try {

            LOGGER.log(Level.INFO, message("precess_service"), serviceCode) ;
            LOGGER.log(Level.INFO, message("minus") )                       ;

            String security = yamlConfigurator.getAuthenticationType(serviceCode)    ;

            List<CipherTypes> ciphersType = yamlConfigurator.getCiphers(serviceCode) ;

            if (ciphersType.isEmpty() && security.equalsIgnoreCase(Policy.CustomSignOn.name())) {

                ciphersType = Arrays.asList(CipherTypes.AES_256_ECB, CipherTypes.AES_256_CBC)   ;
            }

            servicesManager.registerCiphers(serviceCode, ciphersType) ;

            String cipherString = ciphersType.stream().map(cipher -> "CipherTypes." + cipher )
                                             .collect(joining(", "))  ;

            String templateUri = yamlConfigurator.getTemplateUri(serviceCode)    ;

            if ( templateUri != null ) {
                String templateData = TemplateManager.readFile(templateUri)      ;
                LOGGER.log(Level.INFO, message("register_service"), templateUri) ;
                servicesManager.registerTemplate(serviceCode, templateData)      ;
            }

            List<AcceptType> acceptTypes = yamlConfigurator.getAcceptTypes(serviceCode) ;

            boolean emptyAcceptTypes = acceptTypes.isEmpty()   ;

            if (emptyAcceptTypes)                              {
                acceptTypes.addAll(AcceptType.getPlainTypes()) ;
            }

            else if (emptyAcceptTypes && security.equalsIgnoreCase(Policy.SSO.name()))  {
                acceptTypes.addAll(AcceptType.getEncryptedTypes()) ;
            }

            if (templateUri != null && security.equalsIgnoreCase(Policy.Public.name())) {
                acceptTypes.addAll(AcceptType.getPlainTemplate())  ;
            }

            if ( templateUri != null &&
                 emptyAcceptTypes    &&
                ( security.equalsIgnoreCase(Policy.Public.name()) ||
                  security.equalsIgnoreCase(Policy.SSO.name())) )  {

                acceptTypes.addAll(AcceptType.getPlainTemplate())  ;
            }

            if ( templateUri != null && 
                 emptyAcceptTypes    && 
                 ! security.equalsIgnoreCase(Policy.Public.name() )
                 && !security.equalsIgnoreCase(Policy.SSO.name()))        {
                 acceptTypes.addAll(AcceptType.getEncryptedTemplate())    ;
            }

            servicesManager.registerAcceptTypes(serviceCode, acceptTypes) ;

            LOGGER.log( Level.INFO, message("security_service") ,
                        new Object[] { serviceCode, security }) ;

            LOGGER.log(Level.INFO, message("ciphers"), cipherString.replace("CipherTypes.", "")) ;

            final String serviceCodeSnakeName = Transformer.getSnakeName(serviceCode)     ;

            if (yamlConfigurator.getMaxThreadsByServiceCode(serviceCode) != null )        {

                int maxThreads = yamlConfigurator.getMaxThreadsByServiceCode(serviceCode) ;

                servicesManager.apllyMaxThreads( serviceCode, maxThreads )                ;

                LOGGER.log(Level.INFO, message("extract_max_threads"), maxThreads)        ;

            } else {
                LOGGER.log( Level.INFO, message("apply_default_max_thread"),
                            DefaultStreamerConfigurator.defaultMaxThreadsPerService )     ;
            }

            ClassLoader classLoader = new GhostServicesManager().getClass()
                                                                .getClassLoader() ;
           
            marshaller.registerDto (
               CompilerManager.compileDto(serviceCode, classLoader, cnn, sql)        
            ) ;
            
            CompilerManager.compileDto(serviceCode, classLoader, cnn, sql)        ;

            URL resourceService   = classLoader.getResource("templates/Service")  ;
            
            String contentTeplateService = readFile(resourceService)              ;

            Query query = SqlAnalyzer.getSqlParamsWithTypes( cnn, sql )           ;

            Class<?> _resource = CompilerManager.compileResource ( serviceCode, classLoader ,sql ) ;

            LOGGER.log(Level.INFO, message("new_line"))                                            ;

            /** Create Metric before compiling the service .      */

            /** Rename ServiceCode from camelCase to Snake_case . */

            MetricsManager.registerMetric( metrics, serviceCodeSnakeName ) ;
           
            Class<?> serviceClazz = CompilerManager.compileService( serviceCode          , 
                                                                    serviceCodeSnakeName , 
                                                                    classLoader          ,
                                                                    query                , 
                                                                    contentTeplateService, 
                                                                    security             ,
                                                                    cipherString       ) ;

            ServiceRegistry serviceRegistry = serviceClazz.getAnnotation(ServiceRegistry.class) ;

            Bean<Object> bean = (Bean<Object>) bm.resolve(bm.getBeans(serviceClazz, serviceRegistry) ) ;

            if (bean != null) {
                Object cdiService = (Object) bm.getReference( bean                             ,
                                                              bean.getBeanClass()              ,
                                                              bm.createCreationalContext(bean) ) ;

                servicesManager.registerService(serviceCode, cdiService)                         ;
            }

            else {

                Object managedResource = getInstance(_resource) ;

                servicesManager.extractAndRegisterQueriesFromResource( _resource, SqlQuery.class , cnn ) ;

                try {

                    Object managedService = getInstance(serviceClazz)                           ;

                    Field resourceField = servicesManager.getFieldFor( serviceClazz , 
                                                                       ResourceRegistry.class ) ;
                    resourceField.setAccessible(true)                                           ;
                    resourceField.set(managedService, managedResource)                          ;

                    servicesManager.registerService(serviceCode, managedService)                ;

                } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex ) {
                    throw ex ;
                }
            }

        } catch ( Exception ex )  {
            throw ex ;
        }
    }

    private Object getInstance(Class<?> clazz) {

        Unmanaged unmanagedService = new Unmanaged<>(bm, clazz)                   ;
        UnmanagedInstance serviceInstanceService = unmanagedService.newInstance() ;
        serviceInstanceService.produce().inject().postConstruct()                 ;
        Object managedService = serviceInstanceService.get()                      ;
        return managedService                                                     ;
    }

    @PostConstruct
    public void init() {
        
        List<Map<String, Object>> ghostsServices = yamlConfigurator.getServices();

        if (ghostsServices != null && !ghostsServices.isEmpty()) {

            try {
                LOGGER.log(Level.INFO, message("new_line"))                   ;
                LOGGER.log(Level.INFO, message("stars_1"))                    ;
                LOGGER.log(Level.INFO, message("stars_1"))                    ;
                LOGGER.log(Level.INFO, message("compiling_ghosts_services"))  ;
                LOGGER.log(Level.INFO, message("stars_1"))                    ;
                LOGGER.log(Level.INFO, message("stars_1"))                    ;

                List<Optional<Path>> deps  = null                             ;
                
                String webInfLibLocation = findWebInfLibLocation( System.getProperty(VIRTUAL_FILE_SYSTEM)) ;
                 
                String tmpPathDependenciesForCompilation = webInfLibLocation + "/jaxy_deps" ;
                
                if ( ! ghostsServices.isEmpty() )  {
                    
                    try {
                        
                        deps = searchAndGetFullPath( webInfLibLocation                                 , 
                                                     Arrays.asList( "javaee-api"                       ,
                                                                    "jaxy-api"                         ,
                                                                    "microprofile-metrics-api"         ,
                                                                    "microprofile-fault-tolerance-api" ) ) ;
                        
                        UnzipUtility.unzipJavaDependencies( deps , tmpPathDependenciesForCompilation )     ;
                        CompilerManager.addClassPath( tmpPathDependenciesForCompilation )                  ;
                        
                    } catch ( Exception ex)                           {
                        LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
                        return                                        ;
                    }
                }
                
                if ( yamlConfigurator.getDefaultMaxThreadsPerService() != null ) {
                    
                    LOGGER.log(Level.INFO, message("new_line") )                            ;
                    LOGGER.log( Level.INFO, message("apply_default_max_thread_per_service") ,
                                yamlConfigurator.getDefaultMaxThreadsPerService()         ) ;
                    
                    DefaultStreamerConfigurator.defaultMaxThreadsPerService    =
                                          yamlConfigurator.getDefaultMaxThreadsPerService() ;
                }
                   
                final List<String> listServicesCodeForGrafana = new ArrayList<>()        ;
                
                try ( Connection cnn = entityManager.unwrap(java.sql.Connection.class )) {
                
                     ghostsServices.forEach (( final Map<String, Object> svice) ->       {
                        
                        LOGGER.log(Level.INFO, message("new_line") ) ;
                        
                        try  {
                                String serviceCode = Transformer.getServiceCode(svice) ;
                                String sql = Transformer.getSql( svice, serviceCode)   ;
                                processGhostService( serviceCode, cnn, sql )           ;
                                listServicesCodeForGrafana.add ( serviceCode )         ;
                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, ex.getMessage(), ex )  ;
                            throw new RuntimeException(ex)                  ; 
                        }
                     }) ;
                    
                    marshaller.instanciateJaxbContext()           ;
                    
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex) ;
                }
                
                GrafanaManager.rootGenerator( listServicesCodeForGrafana , servicesManager) ;
                listServicesCodeForGrafana.clear() ;
                 
                /** No need extracted Dependencies -> Remove them . */
                if( deps != null ) removeDependencies( tmpPathDependenciesForCompilation ) ;
                
            } catch (IOException ex)                         {
                LOGGER.log(Level.SEVERE, "Exception :", ex ) ;
                throw new RuntimeException(ex)               ;
            }

        } else {
            LOGGER.log(Level.INFO,message("new_line"))          ;
            LOGGER.log(Level.INFO, message("stars_2"))          ;
            LOGGER.log(Level.INFO, message("no_ghost_service")) ;
            LOGGER.log(Level.INFO, message("stars_2"))          ;
            LOGGER.log(Level.INFO,message("new_line"))          ;
        }

        LOGGER.log(Level.INFO, message("stars_3"))                       ;
        LOGGER.log(Level.INFO, message("applying_global_configuration")) ;
        LOGGER.log(Level.INFO, message("new_line"))                      ;

        if (ConcurrentUsersManager.maxConcurrentUsers != yamlConfigurator.getMaxConcurrentUsers()) {
            ConcurrentUsersManager.maxConcurrentUsers = yamlConfigurator.getMaxConcurrentUsers()   ;
        }

        if (DefaultStreamerConfigurator.responseCacheSize != yamlConfigurator.getResponseCacheSize()) {
            DefaultStreamerConfigurator.responseCacheSize = yamlConfigurator.getResponseCacheSize()   ;
        }

        if (DefaultStreamerConfigurator.selectSize != yamlConfigurator.getSelectSize()) {
            DefaultStreamerConfigurator.selectSize = yamlConfigurator.getSelectSize()   ;
        }

        if (DefaultStreamerConfigurator.ratio != yamlConfigurator.getRatio()) {
            DefaultStreamerConfigurator.ratio = yamlConfigurator.getRatio()   ;
        }

        if (DefaultStreamerConfigurator.threadPoolSizeApp != yamlConfigurator.getThreadPoolSizeApp()) {
            DefaultStreamerConfigurator.threadPoolSizeApp = yamlConfigurator.getThreadPoolSizeApp()   ;
        }

        
        LOGGER.log(Level.INFO , message("override_jaxy_thread_pool")     ) ; 
        LOGGER.log(Level.INFO , "                                      " ) ; 
        
        /** DefaultStreamerConfigurator is deployed at stratup, and    .  */
        /** order is not guarantee, so, we have to recall the initPool .  */
        
        DefaultStreamerConfigurator.initPoolProducer()                     ; 
        
        LOGGER.log(Level.INFO , message("dash_1") ) ; 
        LOGGER.log(Level.INFO, message("new_line"))  ;
        LOGGER.log(Level.INFO, message("max_concurent_users"), ConcurrentUsersManager.maxConcurrentUsers <= 0 ? "Unlimited" :
                                                               ConcurrentUsersManager.maxConcurrentUsers ) ;
        LOGGER.log(Level.INFO,message("default_max_thread_per_service_01"), 
                               DefaultStreamerConfigurator.defaultMaxThreadsPerService ) ;
        
        LOGGER.log(Level.INFO, message("max_pool_connection"), yamlConfigurator.getMaxPoolConnection())          ;
        LOGGER.log(Level.INFO, message("max_thread_size_app"), DefaultStreamerConfigurator.threadPoolSizeApp)    ;
        LOGGER.log(Level.INFO, message("mresponse_chache_size"), DefaultStreamerConfigurator.responseCacheSize)  ;
        LOGGER.log(Level.INFO, message("select_size"), DefaultStreamerConfigurator.selectSize)                   ;
        LOGGER.log(Level.INFO, message("ratio"), DefaultStreamerConfigurator.ratio)                              ;
        LOGGER.log(Level.INFO, "                                                                             ")  ;
        LOGGER.log(Level.INFO,  yamlConfigurator.getRejectConnectionsWhenLimitExceeded() > 0 ? 
                                message("reject_connec_when_limit_exeed_01") + 
                                yamlConfigurator.getRejectConnectionsWhenLimitExceeded() :
                                message("reject_connec_disabled")                               )                ;
        LOGGER.log(Level.INFO,message("new_line"))                                                               ;
        LOGGER.log(Level.INFO, message("stars_2"))                                                               ;
        LOGGER.log(Level.INFO, message("new_line"))                                                              ;
    }

    public GhostServicesManager() {
    }

    public YamlConfigurator getConfigurator() {
        return yamlConfigurator ;
    }
   
    private static List<Optional<Path>> searchAndGetFullPath ( String libLocation , 
                                                               List<String> jarNamePatterns ) throws IOException {
        
       return 
               
       jarNamePatterns.stream().map( path -> {
           
                                      return uncheckCall( () ->  Files.list(Paths.get(libLocation))
                                                                      .filter( file -> ( file.getFileName()
                                                                                             .toFile().getName()
                                                                                             .contains(path)) &&
                                                                                file.getFileName().toFile()
                                                                                    .getName().endsWith(".jar") )
                                                                      .findFirst()) ;  } )
                               .collect(toList() ) ;
    }
   
    private void removeDependencies( String directory) throws IOException {
        
       Files.walk(Paths.get(directory))
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete) ;
    }
    
    private static String findWebInfLibLocation( String path) throws IOException {
        
       return Files.walk(Paths.get(path ))
                   .filter(Files::isDirectory)
                   .filter( f -> f.toFile().getAbsoluteFile()
                                           .toString().endsWith(".war/WEB-INF/lib"))
                   .findFirst().get().toString() ;
    }

}

