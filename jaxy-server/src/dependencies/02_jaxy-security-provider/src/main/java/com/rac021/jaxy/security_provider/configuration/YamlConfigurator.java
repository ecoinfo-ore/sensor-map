
package com.rac021.jaxy.security_provider.configuration ;

import java.io.File ;
import java.util.Map ;
import java.util.List ;
import java.util.Arrays ;
import javax.ejb.Startup ;
import java.util.HashMap ;
import java.util.Objects ;
import java.io.FileReader ;
import java.util.ArrayList ;
import java.nio.file.Paths ;
import javax.inject.Singleton ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.io.FileNotFoundException ;
import com.rac021.jaxy.api.crypto.AcceptType ;
import com.rac021.jaxy.api.crypto.CipherTypes ;
import com.esotericsoftware.yamlbeans.YamlReader ;
import com.esotericsoftware.yamlbeans.YamlException ;
import com.rac021.jaxy.api.qualifiers.security.Policy ;
import com.rac021.jaxy.api.configuration.IConfigurator ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
@Startup
public class YamlConfigurator implements IConfigurator      {

    private static final Logger LOGGER  = getLogger()       ;
   
    private static String SERVICE_CONF_PATH  = null         ;
    
    private String pathConfig          = "serviceConf.yaml" ;
    private Map    configuration       = new HashMap()      ;
    private String authenticationType  = null               ;
    
    private String tableName           = "users"            ;
    private String loginColumnName     = "login"            ;
    private String passwordColumnName  = "password"         ;
    private String passwordStorage     = "MD5"              ;
    private String algoSign            = "SHA1"             ;
    
    private String loginSignature      = "PLAIN"            ;
    private String passwordSignature   = "MD5"              ;
    private String timeStampSignature  = "PLAIN"            ;
    private Long   validRequestTimeout = 30l                ;
    
    private String keycloakFile        = null               ;
    
    private String keycloakUrl         = null               ;
    
    private int  RATIO                 =  1                 ;
    private int  ThreadPoolSizeApp     =  8                 ;
    private int  responseCacheSize     =  500               ;
    private int  selectSize            =  5000              ;
    private int  maxConcurrentUsers    =  Integer.MAX_VALUE ;
    private String log_level           =  "INFO"            ;
    
    private final String LOG           =  "LOG_LEVEL"       ;
    
    Map<String ,String >             security               ;
    
    Map<String ,List<CipherTypes> >  ciphers                ;
    Map<String ,List<AcceptType> >   acceptTypes            ;
    
    private Integer defaultMaxThreadsPerService    = null   ;
    
    private int rejectConnectionsWhenLimitExceeded = Integer.MAX_VALUE ; 
    
    private String letsEncryptCertGenPath = "lib/letsEncryptCertGenerator.jar" ; 
   
    /* Server Configuration */
    
    private String  HOST              = "localhost" ; 
    private String  httpPort          = "8080"      ;
    private String  httpsPort         = "8443"      ;
    private String  transport         = "http"      ; // HTTPS 
    
    public static SslMode sslMode ;

   
    public static enum SslMode { SELF_SSL, PROVIDED_SSL, LETS_ENCRYPT } ;
    
    /** None SelfSSL Configuration . */
    
    private String keyStorePassword   =  "admin"                        ;
    private String keyPassword        =  "admin"                        ;
    private String alias              =  "alias_name_admin"             ;
    private String certificatePath    =  "/opt/my-release-key.keystore" ;

    private String letsEncryptCertificateStaging = "DEV"                ;
    private String letsEncryptChallengePath      = "/var/www/html/"     ;
    
    private int     maxPoolConnection            = 20                   ;
    private int     ioThreads                    = 8                    ;
    
    private int     taskMaxThreads               = 64                   ;
    
    private int     managementPortHttp           = 9990                 ;
  
    private int     managementPortHttps          = 9993                 ;
    
    private String  bindAdress                   = "0.0.0.0"            ;

    private String  adminConsoleContext          = "/console"           ;
    
    private String  managementBindAdress         = "127.0.0.1"          ;    
    
    private String  managementAllowedOrigin      = "*"                  ;
 
    private String  rootApplicationContext       = "/"                  ;
    
    private int     logSize                      = 30                   ;
    
    private int     maxBackupLog                 = 2                    ;
    
    private boolean deployManagementInterface    = true                 ;
    
    /** File Config Properties  . */
    
    private final String TYPE                = "type"                ;
    private final String ALIAS               = "ALIAS"               ;
    private final String LOGIN               = "login"               ;
    private final String PLAIN               = "plain"               ;
    private final String _RATIO              = "Ratio"               ;
    private final String ACCEPT              = "Accept"              ;
    private final String CIPHERS             = "Ciphers"             ;
    private final String SECURED             = "secured"             ;
    private final String SSL_MODE            = "SSL_MODE"            ;
    private final String ALGOSIGN            = "algoSign"            ;
    private final String SERVICES            = "Services"            ;
    private final String PASSWORD            = "password"            ;
    private final String TIMESTAMP           = "timeStamp"           ;
    private final String TABLENAME           = "tableName"           ;
    private final String TRANSPORT           = "TRANSPORT"           ;
    private final String HTTP_PORT           = "HTTP_PORT"           ;
    private final String HTTPS_PORT          = "HTTPS_PORT"          ;
    private final String MAX_THREADS         = "MaxThreads"          ;
    private final String CREDENTIALS         = "credentials"         ;
    private final String PARAMTOSIGN         = "paramToSign"         ;
    private final String SERVICECONF         = "serviceConf"         ;
    private final String TEMPLATE_URI        = "TemplateUri"         ;
    private final String AUTHENTICATION      = "authentication"      ;
    private final String CERT_PATH           = "CERTIFICATE_PATH"    ;
    private final String LOGINCOLUMNNAME     = "loginColumnName"     ;
    private final String THREADPOOLSIZEAPP   = "ThreadPoolSize"      ;
    private final String RESPONSECACHESIZE   = "ResponseCacheSize"   ;
    private final String PASSWORDCOLUMNNAME  = "passwordColumnName"  ;
    private final String KEY_STORE_PASSWORD  = "KEY_STORE_PASSWORD"  ;
    private final String MAXCONCURRENTUSERS  = "MaxConcurrentUsers"  ;
    private final String VALIDREQUESTTIMEOUT = "validRequestTimeout" ;
    
    private final String KEYCLOAKURL         = "url"                 ;
    private final String KEYCLOAKFILE        = "keycloakFile"        ;
    private final String KEY_PASSWORD        = "KEY_PASSWORD"        ;
   
    private final String HOST_TYPE           = "HOST_TYPE"           ;
    private final String IP                  = "IP"                  ;
    private final String OVERRIDE_HOST       = "OVERRIDE_HOST"       ;
        
    private final String REJECT_CONNECTION_WHEN_LIMIT_EXEEDED  = "RejectConnectionsWhenLimitExceeded"  ;
    private final String DEFAULT_MAX_THREADS_PER_SERVICE       = "DefaultMaxThreadsPerService"         ;
    
    private final String LETS_ENCRYPT_CERTIFICATE_STAGING      = "letsEncryptCertificateStaging"       ;
    private final String MAX_POOL_CONNECTION                   = "maxPoolConnection"                   ;
    
    private final String IO_THREADS                            = "IO_THREADS"                          ;
    private final String TASK_MAX_THREADS                      = "TASK_MAX_THREADS"                    ;
    private final String JAXY_BIND_ADRESS                      = "JAXY_BIND_ADRESS"                    ;
    private final String MANAGEMENT_PORT_HTTP                  = "MANAGEMENT_PORT_HTTP"                ;
    private final String MANAGEMENT_PORT_HTTPS                 = "MANAGEMENT_PORT_HTTPS"               ;
    
    private final String ADMIN_CONSOLE_CONTEXT                 = "ADMIN_CONSOLE_CONTEXT"               ;
    
    private final String MANAGEMENT_BIND_ADRESS                = "MANAGEMENT_BIND_ADRESS"              ;
    
    private final String MANAGEMENT_ALLOWED_ORIGIN             = "MANAGEMENT_ALLOWED_ORIGIN"           ;
    
    private final String LOG_SIZE                              = "LOG_SIZE"                            ;
    
    private final String MAX_BACKUP_LOG                        = "MAX_BACKUP_LOG"                      ;
    private final String DEPLOY_MANAGEMENT_INTERF              = "DEPLOY_MANAGEMENT_INTERFACE"         ;
    
    private final String ROOT_APPLICATION_CONTEXT              = "ROOT_APPLICATION_CONTEXT"            ;
    
     private final String LETS_ENCRYPT_CERT_GENERATOR_Path     = "letsEncryptCertificateGeneratorPath" ;
    private final String LETS_ENCRYPT_CHALLENGE_Path           = "letsEncryptChallengePath"            ;
   
    private static final String UI_SESSION_TIME_OUT            = "ui_session_timeout"                  ;
    private static       int       SESSION_TIME_OUT            = 15 /* mn */                           ;
    
    
    public YamlConfigurator()                                                    {

       SERVICE_CONF_PATH =  Paths.get (  System.getProperty("serviceConf"))
                                               .getParent().toAbsolutePath()
                                               .toString()                       ;
       security          = new HashMap<>()                                       ;
       ciphers           = new HashMap<>()                                       ;
       acceptTypes       = new HashMap<>()                                       ;
        
       try {
            
             if( System.getProperty(SERVICECONF) != null)      {
                 pathConfig = System.getProperty(SERVICECONF)  ;
             }
             
             if( pathConfig != null && new File(pathConfig).exists() )           {
                 YamlReader reader = new YamlReader( new FileReader(pathConfig)) ;
                 Object     object = reader.read() ;
                 Map        map    = (Map)object   ;            
                 configuration     = map           ;

                 setAuthenticationType()   ;
                 setCredentials()          ;
                 setAlgoSign()             ;
                 setParams()               ;
                 setSecurity()             ;
                 setRatio()                ;
                 setThreadPoolSizeApp()    ;
                 setResponseCacheSize()    ;
                 setselectSize()           ;
                 setMaxConcurrentUsers()   ;
                 setServerConfiguration()  ;
                 setSelfConfiguration()    ;
                 setLogLevel()             ;
                 setMaxPoolConnection()    ;
                 setIoThreads()            ;
                 settaskMaxThreads()       ;
                 setJaxyBindAdress()       ;
                 setManagementPortHttp()   ;
                 setManagementPortHttps()  ;
                 setAdminConsoleContext()  ;
                 setManagementBindAdress() ;
                 setMaxBackupLog()         ;
                 setLogSize()              ;
                 setSessionTimeOut()       ;
                 setRootApplicationContext()              ;
                 setletsEncryptChallengePath()            ;
                 setDeployManagementInterface()           ;
                 setDefaultMaxThreadsPerService()         ;
                 setletsEncryptCertGeneratorPath()        ;
                 setLetsEncryptCertGeneratorStaging()     ;
                 setRejectWhenConnectionLimitExceeded()   ;
                 setManagementAllowedOrigin()             ;
 
             }
            
          } catch( YamlException | FileNotFoundException ex ) {
              LOGGER.log(Level.SEVERE, ex.getMessage(), ex)   ;
          }
    }

    private void setAuthenticationType()                           {
       
      if( ((Map)this.configuration.get(AUTHENTICATION)) == null )  {
          authenticationType = Policy.Public.name().toLowerCase()  ;
      }
      
      else {
          
         authenticationType = (String) ((Map)this.configuration.get(AUTHENTICATION)).get( TYPE )
                                                 .toString().replaceAll(" +", " ").trim()      ; 
         
         if( authenticationType.equalsIgnoreCase(Policy.SSO.name()))  {
             
            String  keycloakPath  =  (String) ((Map)this.configuration
                                                        .get(AUTHENTICATION) ).get( KEYCLOAKFILE )
                                                        .toString().replaceAll(" +", " ").trim() ; 
             
            keycloakFile = getAbsolutePath( keycloakPath ) ;
              
            keycloakUrl   =  (String) ((Map)this.configuration
                                                .get(AUTHENTICATION) ).get( KEYCLOAKURL  )
                                                .toString().replaceAll(" +", " ").trim() ; 
         }
      }
   }
        
   private void setAlgoSign() {
        
        if( this.configuration.get(AUTHENTICATION) != null ) {
             
           if( ( ( (String) ((Map)this.configuration.get(AUTHENTICATION)).get(TYPE))
                                                    .equalsIgnoreCase( 
                                                            Policy.CustomSignOn.name()))) {
               
              this.algoSign = (String) ((Map)this.configuration.get(AUTHENTICATION))
                                                 .get(ALGOSIGN)
                                                 .toString().replaceAll(" +", " ").trim() ;         
           }
        }
    }

    public String getAlgoSign() {
        return algoSign ;
    }
    
    private void setParams() {
        
        if( this.configuration.get(AUTHENTICATION) != null ) {
            
            if( ( ( (String) ((Map)this.configuration.get(AUTHENTICATION)).get(TYPE))
                                                     .equalsIgnoreCase(Policy.CustomSignOn.name())) ) {
                
               this.loginSignature      = (String) ( (Map) ((Map)this.configuration
                                                                     .get(AUTHENTICATION))
                                                                     .get(PARAMTOSIGN))
                                                                     .get(LOGIN).toString()
                                                                     .replaceAll(" +", " ").trim() ;
               
               this.passwordSignature   = (String) ( (Map) ((Map)this.configuration
                                                                     .get(AUTHENTICATION))
                                                                     .get(PARAMTOSIGN))
                                                                     .get(PASSWORD).toString()
                                                                     .replaceAll(" +", " ").trim() ;
               
               this.timeStampSignature  = (String) ( (Map) ((Map)this.configuration
                                                                     .get(AUTHENTICATION))
                                                                     .get(PARAMTOSIGN))
                                                                     .get(TIMESTAMP).toString()
                                                                     .replaceAll(" +", " ").trim() ;
               
               this.validRequestTimeout = Long.parseLong( ( (String) ((Map)this.configuration
                                                                               .get(AUTHENTICATION))
                                                                               .get(VALIDREQUESTTIMEOUT)) ) ;
            }
        }
    }

    private void setSecurity() {
        
        if( getAuthenticationType() == null ) {
            authenticationType = Policy.Public.name().toLowerCase() ;
            return ;
        }
        
        if( getAuthenticationType().equalsIgnoreCase(Policy.SSO.name())) {
           if( getAuthenticationInfos() != null ) {
              ((Map)getAuthenticationInfos().get(SECURED)).keySet().forEach( _sName -> {
                   security.put((String) _sName, Policy.SSO.name() ) ;
              }) ;
           }
        }
        
        else if( getAuthenticationType().equalsIgnoreCase(Policy.CustomSignOn.name())) {
            
           if( getAuthenticationInfos() != null ) {
               
                ( (Map<String, Map>) ((Map) this.configuration.get(AUTHENTICATION)).get(SECURED))
                                           
                        .forEach((k, v) -> {
                           
                                    String _sName = k                                 ;
                                    security.put(_sName, Policy.CustomSignOn.name() ) ;

                                    Map<String, Object > serviceSescription = v       ;

                                    serviceSescription.forEach((n, m ) -> { 
                                        
                                         if( n.equals(ACCEPT)) {
                                             List<String> mm = ((List<String>) m)   ;
                                            ( mm ).replaceAll(String::toUpperCase ) ;
                                             this.acceptTypes.put(_sName, AcceptType.toList( mm) ) ;
                                         }
                                         
                                         if( n.equals(CIPHERS)) {
                                             List<String> mm = ((List<String>) m) ;
                                             mm.replaceAll ( String::toUpperCase ) ;
                                             List<CipherTypes> typed = CipherTypes.toList( mm )    ;
                                             this.ciphers.put(_sName, typed ) ;
                                         }
                                        
                                    }) ;
                        }) ; 
           }
        }
    }
    
    private void setCredentials() {
        
        if( this.configuration.get(AUTHENTICATION) != null ) {
            
            if( ( ( (String) ((Map) this.configuration.get(AUTHENTICATION)).get(TYPE))
                                                      .equalsIgnoreCase(Policy.CustomSignOn.name())) ) {
                
                this.tableName          = ( String) ( (Map) ((Map)this.configuration
                                                                      .get(AUTHENTICATION))
                                                                      .get(CREDENTIALS))
                                                                      .get(TABLENAME).toString()
                                                                      .replaceAll(" +", " ").trim() ;
                
                this.loginColumnName    = ( String) ( (Map) ((Map)this.configuration
                                                                      .get(AUTHENTICATION))
                                                                      .get(CREDENTIALS))
                                                                      .get(LOGINCOLUMNNAME).toString()
                                                                      .replaceAll(" +", " ").trim() ;
                
                this.passwordColumnName = ((String) ( (Map) ((Map)this.configuration
                                                                      .get(AUTHENTICATION))
                                                                      .get(CREDENTIALS))
                                                                      .get(PASSWORDCOLUMNNAME))
                                                                      .replaceAll(" +", " ")
                                                                      .split(" -> ")[0].trim() ; 

                this.passwordStorage    = ((String) ( (Map) ((Map)this.configuration
                                                                      .get(AUTHENTICATION))
                                                                      .get(CREDENTIALS))
                                                                      .get(PASSWORDCOLUMNNAME))
                                                                      .contains(" -> ") ?
                                          ((String) ( (Map) ((Map)this.configuration
                                                                      .get(AUTHENTICATION))
                                                                      .get(CREDENTIALS))
                                                                      .get(PASSWORDCOLUMNNAME))
                                                                      .replaceAll(" +", " ").trim()
                                                                      .split(" -> ")[1]    :
                                          PLAIN ;
            }
        }
    }
    
    public  Map getConfiguration() {
        return configuration ;
    }
    
    private void setRatio()                             {
       if(( getConfiguration().get(_RATIO)) != null )   {
           this.RATIO = Integer.parseInt((String) getConfiguration().get(_RATIO)) ;
       }
    }
    
    private void setletsEncryptCertGeneratorPath() {
       if(( getConfiguration().get(LETS_ENCRYPT_CERT_GENERATOR_Path)) != null )   {
           this.letsEncryptCertGenPath = ((String) getConfiguration()
               .get(LETS_ENCRYPT_CERT_GENERATOR_Path))                            ;
       }
    }
    
    private void setletsEncryptChallengePath()                               {
       if(( getConfiguration().get(LETS_ENCRYPT_CHALLENGE_Path)) != null )   {
           this.letsEncryptChallengePath = ((String) getConfiguration()
               .get(LETS_ENCRYPT_CHALLENGE_Path))                            ;
       }
    }
    
    public String getLetsEncryptChallengePath() {
      return this.letsEncryptChallengePath      ;
    }

    private void setThreadPoolSizeApp()                           {
       if(( getConfiguration().get(THREADPOOLSIZEAPP)) != null )  {
         this.ThreadPoolSizeApp = Integer.parseInt((String) getConfiguration()
                                         .get(THREADPOOLSIZEAPP)) ;
       }
    }

    private void setResponseCacheSize()                           {
       if((getConfiguration().get(RESPONSECACHESIZE)) != null )   {
         this.responseCacheSize = Integer.parseInt((String) getConfiguration()
                                         .get(RESPONSECACHESIZE)) ;
       }
    }
    private void setMaxConcurrentUsers()                          {
       if((getConfiguration().get(MAXCONCURRENTUSERS)) != null )  {
         this.maxConcurrentUsers= Integer.parseInt((String) getConfiguration()
                                         .get(MAXCONCURRENTUSERS)) ;
       }
    }
    
    private void setRejectWhenConnectionLimitExceeded() {
       if((getConfiguration().get(REJECT_CONNECTION_WHEN_LIMIT_EXEEDED)) != null ) {
         this.rejectConnectionsWhenLimitExceeded =
                  Integer.parseInt((String) getConfiguration()
                         .get(REJECT_CONNECTION_WHEN_LIMIT_EXEEDED)) ;
       }
    }

    private void setselectSize()                           {
      if((getConfiguration().get("SelectSize")) != null )  {
        this.selectSize     = Integer.parseInt((String) getConfiguration()
                                     .get("SelectSize")) ;
      }
    }
    
    private void setServerConfiguration()        {
 
       
        if( System.getenv("JAXY_URL") != null )  {
           
           this.HOST = System.getenv("JAXY_URL") ;
            
           System.out.println( " JAXY HOST "     +
                               "Retrieved from " +
                               "ENV variable : " +
                               "JAXY_URL  " )    ;
            
           System.out.println(" JAXY_HOST : "    +
                       System.getenv("JAXY_URL"));
            
           System.out.println(" ")               ;
            
        }
        
        else if ( ( getConfiguration().get(OVERRIDE_HOST)) != null &&
                ! ((String) getConfiguration().get(OVERRIDE_HOST)).isEmpty() ) {
       
          String ovr = (String) getConfiguration().get(OVERRIDE_HOST) ;
          
          this.HOST  = (String) getConfiguration().get(OVERRIDE_HOST) ;
       }
       
       else if ((getConfiguration().get(HOST_TYPE)) != null )         {
           
         String hostType = (String) getConfiguration().get(HOST_TYPE) ;
         
         if( hostType.equalsIgnoreCase( IP ))      {
             this.HOST = HostManager.getIp()       ;
         }
         else {
             this.HOST = HostManager.getHostName() ;
         }
         
       } else {
           
             this.HOST = HostManager.getHostName() ;
       }
       
       if ((getConfiguration().get(HTTP_PORT)) != null )   {
         this.httpPort = (String) getConfiguration()
                             .get(HTTP_PORT) ;
       }
       
       if ((getConfiguration().get(HTTPS_PORT)) != null )  {
         this.httpsPort = (String) getConfiguration()
                             .get(HTTPS_PORT) ;
       }
       
       if ((getConfiguration().get(TRANSPORT)) != null )   {
         this.transport = (String) getConfiguration()
                                  .get(TRANSPORT).toString()
                                  .toLowerCase()           ;
       }
       
       if ((getConfiguration().get(SSL_MODE)) != null )           {
        
        String sslMod = (String )getConfiguration().get(SSL_MODE) ;
           
        if( sslMod.equalsIgnoreCase(SslMode.SELF_SSL.name()    ) ||
            sslMod.equalsIgnoreCase(SslMode.LETS_ENCRYPT.name()) ||
            sslMod.equalsIgnoreCase(SslMode.PROVIDED_SSL.name())  ) 
        {
              YamlConfigurator.sslMode = SslMode.valueOf(sslMod)  ;
        } 
        else {
            
              YamlConfigurator.sslMode =  SslMode.SELF_SSL        ;
        }
        
       }
    }

    private void setSelfConfiguration()                          { 
   
      if((getConfiguration().get(CERT_PATH)) != null )           {
          
         this.certificatePath = getAbsolutePath ( 
                                  (String) getConfiguration()
                                  .get(CERT_PATH)  ) ;
      }
      
      if((getConfiguration().get(KEY_STORE_PASSWORD)) != null )  {
         this.keyStorePassword = (String) getConfiguration()
                                 .get(KEY_STORE_PASSWORD) ;
      }
      
      if((getConfiguration().get(KEY_PASSWORD)) != null )        {
         this.keyPassword = (String) getConfiguration()
                            .get(KEY_PASSWORD) ;
      }
      
      if((getConfiguration().get(ALIAS)) != null )               {
        this.alias = (String) getConfiguration().get(ALIAS)      ;
      }
    }
  
    @Override
    public Long getValidRequestTimeout() {
        return validRequestTimeout       ;
    }
    
    public Integer getMaxThreadsByServiceCode( String serviceCode )   { 
     
       String  maxTh = ( (String) ( (Map) getService( serviceCode )   )
                         .get(MAX_THREADS))     ;
        
       return ( (maxTh != null ) && ( ! maxTh.trim().isEmpty() ) )    ?
              Integer.parseInt ( maxTh.replaceAll(" +", " ") )  : 
              null                                              ;
    }
    
    public List<Map<String, Object>> getServices()  { 

       return ( List<Map<String, Object>> ) 
                getConfiguration().get(SERVICES)    ; 
    }
    
    public Object getService( String serviceCode )  {
       
       return  getServices().stream()
                            .filter( s -> s.containsKey(serviceCode))
                            .map( s -> s.get(serviceCode))
                            .findFirst().orElse( null )  ;
    }
    
    public List<CipherTypes> getCiphers(String serviceCode)             { 
      return ciphers.getOrDefault( serviceCode, new ArrayList<>() )     ;
    }
    
    public List<AcceptType> getAcceptTypes(String serviceCode)          { 
      return acceptTypes.getOrDefault( serviceCode, new ArrayList<>() ) ;
    }
    
    public String getAuthenticationType() {      
       return authenticationType          ;
    }
   
    public Map getAuthenticationInfos()   {
       return (Map)((Map) this.configuration
                              .get(AUTHENTICATION)) ;
    }

    public String getAuthenticationType(String serviceCode)    {
      return security.getOrDefault( serviceCode, 
                                    Policy.Public.name().toLowerCase() ) ;
    }

    public String getTemplateUri(String serviceCode )          {
        
      return ( serviceCode == null || serviceCode.isEmpty() )  ?
              null : 
              getAbsolutePath ( (String) 
                             ( (Map) getService( serviceCode ) )
                               .get(TEMPLATE_URI))             ; 
    }
       
    public String getPathConfig() {
        return pathConfig         ;
    }

    public String getTableName()  {
        return tableName          ;
    }

    public String getLoginColumnName()    {
        return loginColumnName    ;
    }

    public String getPasswordColumnName() {
        return passwordColumnName ;
    }

    public String getLoginSignature()     {
        return loginSignature     ;
    }

    public String getPasswordSignature()  {
        return passwordSignature  ;
    }

    public String getTimeStampSignature() {
        return timeStampSignature ;
    }

    public Map<String, String> getSecurity() {
        return security           ;
    }

    public void setKeycloakFile(String keycloakFile ) {
        this.keycloakFile = keycloakFile ;
    }

    public String getKeycloakFile()    {
        return keycloakFile       ;
    }
    
    public String  getKeycloakUrl()    {
        return keycloakUrl        ;
    }
          
    public String getPasswordStorage() {
        return passwordStorage  ;
    }
   
    
    public int getRatio()              {
       return RATIO ;
    }

    public int getThreadPoolSizeApp()  {
       return ThreadPoolSizeApp ;
    }

    public int getResponseCacheSize()  {
       return responseCacheSize ;
    }

    public int getSelectSize()         {
       return selectSize ;
    }

    public int getMaxConcurrentUsers() {
        return maxConcurrentUsers      ;
    }

    public String getHost()        {
        return HOST ;
    }

    public void setIp(String HOST) {
        this.HOST = HOST;
    }

    public String getHttpPort()    {
        return httpPort ;
    }

    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort ;
    }

    public String getHttpsPort() {
        return httpsPort ;
    }
    
    public String getSelectedPort() {
       return getTransport().equalsIgnoreCase("https") ? httpsPort : httpPort ;
    }

    public void setHttpsPort(String httpsPort) {
        this.httpsPort = httpsPort  ;
    }

    public String getTransport()    {
        return transport ;
    }

    public void setTransport(String transport) {
        this.transport = transport  ;
    }
    
    public boolean isHttpTransport()   {
        return getTransport().equalsIgnoreCase("http") ;
    }
    
    public boolean isHttpsTransport()  {
        return getTransport().equalsIgnoreCase("https") ;
    }

    public SslMode getSslMode()        {
        return sslMode ;
    }

    public String getKeytorePassword() {
        return keyStorePassword ;
    }

    public void setKeytorePassword(String keytorePassword) {
        this.keyStorePassword = keytorePassword ;
    }

    public String getKeyPassword()     {
        return keyPassword ;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword ;
    }

    public String getAlias()           {
        return alias ;
    }

    public void setAlias(String alias) {
        this.alias = alias ;
    }

    public String getCertificatePath() {
        return certificatePath ;
    }

    public void setsertificatePath(String certPath) {
        this.certificatePath = certPath ;
    }

    public String getLogLevel()  {
       return log_level ;
    }
    
    private void setLogLevel()   {
    
       String log = (String) getConfiguration() .get(LOG) ;
       
       if( log != null && valid(log) )        {
           this.log_level = log.toUpperCase() ;
       }
    }

    public int getRejectConnectionsWhenLimitExceeded() {
        return rejectConnectionsWhenLimitExceeded      ;
    }
    
    public String getLogindUI()  {
        
        return getConfiguration().get("ui_login") != null ?
                                 ((String) getConfiguration().get("ui_login"))
                                                             .replaceAll(" +", " ")
                                                             .trim() :
                                  null ;
    }
    
    public String getPasswordUI() {
        
         return getConfiguration().get("ui_password") != null ?
                                 ((String) getConfiguration().get("ui_password"))
                                                             .replaceAll(" +", " ")
                                                             .trim() :
                                  null ;
    }
    
    public boolean isSecuredUI() {
      
       return ((String) getConfiguration().get("SecureUI")) != null ?
                                            ((String) getConfiguration().get("SecureUI"))
                                                                        .replaceAll(" +", " ").trim()
                                                                        .equalsIgnoreCase("true") : false ;
    }
    
    public boolean deployUI() {
        
       return ((String) getConfiguration().get("DeployUI")) != null ?
                                     ((String) getConfiguration().get("DeployUI"))
                                                                 .replaceAll(" +", " ").trim()
                                                                 .equalsIgnoreCase("true") : false ;
    }
    
    private boolean valid(String log)       {
       
        Objects.requireNonNull(log )        ;

        String _log = log                   ;

        if( _log.equalsIgnoreCase("ALL")    ||
            _log.equalsIgnoreCase("CONFIG") ||
            _log.equalsIgnoreCase("DEBUG")  ||
            _log.equalsIgnoreCase("ERROR")  ||
            _log.equalsIgnoreCase("FATAL")  ||
            _log.equalsIgnoreCase("FINE")   ||
            _log.equalsIgnoreCase("FINEST") ||
            _log.equalsIgnoreCase("INFO")   ||
            _log.equalsIgnoreCase("OFF")    ||
            _log.equalsIgnoreCase("SEVERE") ||
            _log.equalsIgnoreCase("TRACE")  ||
            _log.equalsIgnoreCase("WARN")   ||
            _log.equalsIgnoreCase("WARNING")
          ) {
            return true ;
        }
        LOGGER.log(Level.WARNING, " ++ Invalid LOG LEVEL ++ ") ;
        return false ;
      
    }

    private void setDefaultMaxThreadsPerService()              {
        
      if( ( getConfiguration()
            .get( DEFAULT_MAX_THREADS_PER_SERVICE)) != null )  {
      
          this.defaultMaxThreadsPerService = 
                 Integer.parseInt((String) getConfiguration()
                        .get(DEFAULT_MAX_THREADS_PER_SERVICE)) ;
       }
    }
 
    public Integer getDefaultMaxThreadsPerService()   {
        return this.defaultMaxThreadsPerService ;
    }

    public String getLetsEncryptGeneratorLocation()   {
       return this.letsEncryptCertGenPath ;
    }

    public String getLetsEncryptCertificateStaging()  {
        return letsEncryptCertificateStaging;
    }

    private void setLetsEncryptCertGeneratorStaging() {
       
       Object stagingOb  = getConfiguration().get(LETS_ENCRYPT_CERTIFICATE_STAGING) ;
       if( stagingOb != null )    {
           if ( ((String) stagingOb ).trim().equalsIgnoreCase("PROD" ) ) {
             this.letsEncryptCertificateStaging = "PROD" ;
           } else {
             this.letsEncryptCertificateStaging = "DEV"  ;
           }
       }
    }
   
    private void setMaxPoolConnection() {
       
       if((getConfiguration().get( MAX_POOL_CONNECTION)) != null )  {
         this.maxPoolConnection = 
                 Integer.parseInt((String) getConfiguration()
                       .get(MAX_POOL_CONNECTION))   ;
       }
    }
    
    public int getMaxPoolConnection() {
        return maxPoolConnection      ;
    }
    
    private void setIoThreads() {
       
       if((getConfiguration().get( IO_THREADS)) != null )  {
         this.ioThreads = 
                 Integer.parseInt((String) getConfiguration()
                       .get(IO_THREADS))   ;
       }
    }
    
    public int getIoThreads() {
        return ioThreads ;
    }
    
    private void settaskMaxThreads() {
       
       if((getConfiguration().get( TASK_MAX_THREADS)) != null )  {
         this.taskMaxThreads = 
                 Integer.parseInt((String) getConfiguration()
                       .get(TASK_MAX_THREADS))   ;
       }
    }
    
    public int getTaskMaxThreads()   {
        return taskMaxThreads        ;
    }
    
    private void setJaxyBindAdress() {
       /** Accept Connection . */
       if((getConfiguration().get( JAXY_BIND_ADRESS)) != null )  {
         this.bindAdress =  (String) getConfiguration()
                                         .get(JAXY_BIND_ADRESS)  ;
       }
    }
    
    public String getJaxyBindAdress() {
        return bindAdress             ;
    }
    
    private void setManagementPortHttp() {
       
       if((getConfiguration().get( MANAGEMENT_PORT_HTTP )) != null )  {
         this.managementPortHttp = 
                 Integer.parseInt((String) getConfiguration()
                       .get(MANAGEMENT_PORT_HTTP))   ;
       }
    }
    
    public int getManagrementPortHttp()   {
        return managementPortHttp         ;
    }
     
    private void setManagementPortHttps() {
       
       if((getConfiguration().get( MANAGEMENT_PORT_HTTPS )) != null )  {
         this.managementPortHttps = 
                 Integer.parseInt((String) getConfiguration()
                       .get(MANAGEMENT_PORT_HTTPS))   ;
       }
    }
    
    public int getManagrementPortHttps()  {
        return managementPortHttps        ;
    }
     
    private void setAdminConsoleContext() {
       
       if((getConfiguration().get( ADMIN_CONSOLE_CONTEXT )) != null )  {
         this.adminConsoleContext = (String) getConfiguration()
                                         .get(ADMIN_CONSOLE_CONTEXT)   ;
         if( ! adminConsoleContext.startsWith("/")) {
               adminConsoleContext = "/" + adminConsoleContext ;
         }
       }
    }
    
    public String getAdminConsoleContext() {
        return adminConsoleContext ;
    }
     
    private void setManagementBindAdress() {
       
       if((getConfiguration().get( MANAGEMENT_BIND_ADRESS )) != null ) {
         this.managementBindAdress = ((String) getConfiguration()
                                     .get(MANAGEMENT_BIND_ADRESS ))    ;
       }
    }
    
    public String getManagementBindAdress() {
        return managementBindAdress         ;
    }
    
    private void setManagementAllowedOrigin() {
       
       Object mao = getConfiguration()
                              .get( MANAGEMENT_ALLOWED_ORIGIN )    ;
       
       if ( mao == null || ((String) mao ).isEmpty() )  {
           
          this.managementAllowedOrigin = 
          getTransport() + "://" + HOST   + ":" + getSelectedPort() ;
       }
        
       else  {
           
         this.managementAllowedOrigin =  ((String) mao )  ;
       }       
    }
    
    public List<String> getManagementAllowedOrigin()             {
       return Arrays.asList(managementAllowedOrigin.split(","))  ;
    }
    
    private void setLogSize() {
       
       if((getConfiguration().get( LOG_SIZE )) != null ) {
         this.logSize = Integer.parseInt((String) getConfiguration()
                                     .get(LOG_SIZE ))    ;
       }
    }
    
    public int getLogSize() {
        return logSize      ;
    }
    
    private void setMaxBackupLog() {
       
       if((getConfiguration().get( MAX_BACKUP_LOG )) != null ) {
         this.maxBackupLog =  Integer.parseInt((String) getConfiguration()
                                     .get(MAX_BACKUP_LOG ))    ;
       }
    }
    
    public int getMaxBackupLog() {
        return maxBackupLog      ;
    }
    
    private void setDeployManagementInterface()   {
       
       if((getConfiguration().get( DEPLOY_MANAGEMENT_INTERF )) != null ) 
           deployManagementInterface = 
                   Boolean.parseBoolean((String) getConfiguration()
                  .get(DEPLOY_MANAGEMENT_INTERF ) )                    ;
    }
    
    public boolean deployManagementInterface() {
        return this.deployManagementInterface  ;
    }
    
    private void setRootApplicationContext()   {
       
       if((getConfiguration().get( ROOT_APPLICATION_CONTEXT )) != null ) 
           this.rootApplicationContext = 
                   ((String) getConfiguration()
                  .get(ROOT_APPLICATION_CONTEXT ) )                    ;
       
       this.rootApplicationContext  = 
       this.rootApplicationContext .trim().equals("/") ?
                "" : "/" + this.rootApplicationContext
                               .replaceAll("/", "")
                               .trim()  ; 
    }
    
    public String getRootApplicationContext() {
        return this.rootApplicationContext    ;
    }

    private static String getAbsolutePath( String path ) {
      
      return ( path == null || path.isEmpty() )  ?
               null : 
               PathCalculator.
                    getAbsolutPathFromRelatifPathFor( path                , 
                                                      SERVICE_CONF_PATH ) ;
    }
      
    private void setSessionTimeOut()                               {
        
       if ( getConfiguration().get(UI_SESSION_TIME_OUT) != null )  {
               
          String sessionTimeOute = ((String) getConfiguration()
                                          .get(UI_SESSION_TIME_OUT))  
                                          .replaceAll(" +", " "    ) 
                                          .trim()                  ;
          
          SESSION_TIME_OUT =  Integer.parseInt ( sessionTimeOute ) ;
       }
    }
    
    public static int  getSessionTimeOut() {
       return SESSION_TIME_OUT ;
    }

}

