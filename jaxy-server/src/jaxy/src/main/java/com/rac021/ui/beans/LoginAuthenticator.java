
package com.rac021.ui.beans ;

import java.io.File ;
import java.util.Map ;
import java.util.UUID ;
import javax.json.Json ;
import javax.inject.Named ;
import java.io.IOException ;
import javax.inject.Inject ;
import java.io.Serializable ;
import java.io.StringReader ;
import java.math.BigInteger ;
import javax.json.JsonReader ;
import javax.ws.rs.core.Form ;
import javax.json.JsonString ;
import java.security.KeyStore ;
import java.util.logging.Level ;
import java.io.FileOutputStream ;
import java.util.logging.Logger ;
import javax.ws.rs.client.Client ;
import javax.ws.rs.core.Response ;
import javax.ws.rs.client.Entity ;
import javax.ws.rs.core.MediaType ;
import java.security.MessageDigest ;
import javax.enterprise.inject.Any ;
import javax.ws.rs.client.Invocation ;
import javax.annotation.PostConstruct ;
import java.security.KeyStoreException ;
import javax.enterprise.inject.Instance ;
import javax.faces.context.FacesContext ;
import javax.ws.rs.client.ClientBuilder ;
import java.nio.charset.StandardCharsets ;
import com.rac021.jaxy.api.security.ISignOn ;
import javax.faces.application.FacesMessage ;
import javax.enterprise.context.SessionScoped ;
import java.security.NoSuchAlgorithmException ;
import javax.servlet.http.HttpServletResponse ;
import javax.enterprise.util.AnnotationLiteral ;
import java.security.cert.CertificateException ;
import com.rac021.jaxy.api.root.ServicesManager ;
import com.rac021.jaxy.api.qualifiers.security.Custom ;
import com.rac021.jaxy.api.qualifiers.security.Policy ;
import com.rac021.jaxy.api.exceptions.BusinessException ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;


/**
 *
 * @author ryahiaoui
 */

@SessionScoped
@Named(value = "loginAuthenticator")
public class LoginAuthenticator  implements Serializable {
    
    private boolean logedIn = false                  ;
   
    @Inject
    transient YamlConfigurator yamlConfigurator      ;
     
    @Inject
    transient ServicesManager  servicesManager       ;
    
    @Inject
    @Any
    private Instance<ISignOn>   signOn               ;
   
    private static final Logger LOGGER = getLogger() ;
    
    static KeyStore             keystore             ;

    public LoginAuthenticator() { }
    
    @PostConstruct
    public void init() {
       System.out.println(" - Init LoginAuthenticator " + this ) ;
       if( keystore == null ) initKeyStore()  ;
    }
    
    private void initKeyStore() {
        
       try {

            String key = UUID.randomUUID().toString()   ;
            String filename = "jaxyCaCertLoginAuth.jks" ;

            File f = new File(filename)                 ;
            
            if (!f.exists())  f.createNewFile()         ;

            keystore = KeyStore.getInstance(KeyStore.getDefaultType())  ;

            char[] password = key.toCharArray()                         ;
            keystore.load( null, password )                             ;

            /** Store away the keystore. */
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                keystore.store(fos, password) ;
            }

        } catch ( IOException | KeyStoreException | 
                  NoSuchAlgorithmException        |
                  CertificateException ex)        {
            throw new RuntimeException( ex )      ;
        }
    }
    
    public void checkAuth() throws BusinessException, Exception {

        checkAuth(servicesManager, signOn, yamlConfigurator)    ;
    }
    
    private static void checkAuth( ServicesManager   servicesManager  , 
                                   Instance<ISignOn> signOn           ,
                                   YamlConfigurator  yamlConfigurator ) throws Exception {

        FacesContext context = FacesContext.getCurrentInstance()      ;

        /** to retrieve additional parameter . **/
        Map<String, String> params = context.getExternalContext().getRequestParameterMap() ;

        Policy securityLevel = servicesManager.getSecurityLevel()                          ;

        if (securityLevel == Policy.CustomSignOn)      {

            String login     = params.get("login")     ;
            String timestamp = params.get("timestamp") ;
            String token     = params.get("token")     ;

            if (signOn.select(new AnnotationLiteral<Custom>() { }).get() == null )           {
                
                throw new BusinessException(" No Provider found for Custom Authentication ") ;
            }

            if ( signOn.select( new AnnotationLiteral<Custom>() {}).get()
                                                                   .checkIntegrity(login, timestamp, token) )   {
                
                context.addMessage( null                                        ,
                                    new FacesMessage(FacesMessage.SEVERITY_INFO ,
                                    ""                                          , 
                                    "   ** OK **  " ) )                         ;
            } else {
                
                context.addMessage( null                                         , 
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR ,
                                    ""                                           , 
                                    "   ** ERROR **  ") ) ;
            }
        }

        else if ( securityLevel == Policy.SSO )       {

            String login     = params.get("login")    ;
            String password  = params.get("password") ;
            String clientId  = params.get("clientId") ;
            String secrettId = params.get("secretId") ;

            String tok = getToken( yamlConfigurator.getKeycloakUrl() , 
                                   login                             , 
                                   password                          , 
                                   clientId                          , 
                                   secrettId                       ) ;

            if (!tok.contains("error")) {

                context.addMessage( null                                        , 
                                    new FacesMessage(FacesMessage.SEVERITY_INFO , 
                                    ""                                          ,
                                    "   ** OK SSO **  "   ) ) ;
                
            } else {

                System.out.println("token Error = " + tok )   ;
                context.addMessage( null                                         , 
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR , 
                                    ""                                           , 
                                    "   ** ERROR SSO **  ") ) ;
            }
        }

    }
    
    public static String getToken( String url          ,
                                   String username     ,
                                   String password     , 
                                   String client_id    ,
                                   String client_secret) {

        if ( url       == null    || 
             username  == null    || 
             password  == null    || 
             client_id == null    ||
             client_secret == null ) {  
            
            return " Error" ;
        } 
        
        Client clientB      ;

        try {

            if ( url.startsWith("https" ) )  {

                clientB = ClientBuilder.newBuilder().trustStore(keystore).build() ;
                
            } else {

                clientB = ClientBuilder.newClient()                               ;
            }

            Invocation.Builder client = clientB.target(url).request(MediaType.APPLICATION_JSON) ;

            Form form = new Form()                     ;
            form.param("username", username)           ;
            form.param("password", password)           ;
            form.param("client_id", client_id)         ;
            form.param("grant_type", "password")       ;
            form.param("client_secret", client_secret) ;

            Response response = client.post(Entity.form(form), Response.class) ;

            if (response.getStatus() == 200) {

                StringReader stringReader = new StringReader(response.readEntity(String.class))   ;

                try (JsonReader jsonReader = Json.createReader(stringReader))                     {

                    JsonString jsonString = jsonReader.readObject().getJsonString("access_token") ;

                    if (jsonString == null)                           {
                        
                        System.out.println(" BAD AUTHENTICATION ! ")  ;
                        return null                                   ;
                        
                    } else {
                        
                        return jsonString.getString()                 ;
                    }
                }
            } else {
                return "_error_:" + response.readEntity(String.class) ;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex ) ;
            return "_error_"                               ;
        }
    }
    
    public boolean isLoged() {
       return logedIn        ;
    }
    
    
    public void login() throws IOException, NoSuchAlgorithmException {
      
      if ( yamlConfigurator.getLogindUI() == null     ||
           yamlConfigurator.getLogindUI().isEmpty()   ||
           yamlConfigurator.getPasswordUI() == null   || 
           yamlConfigurator.getPasswordUI().isEmpty()) {

          return  ;
      }

      FacesContext context = FacesContext.getCurrentInstance()                               ;

      /** to retrieve additional parameter . */
      Map<String, String> params = context.getExternalContext().getRequestParameterMap()     ;

      String checkLoginPasswordSHA2 = params.get("hashedLoginPassword")                      ;
      String timestamp              = params.get("timestamp")                                ;

      String hashedLoginPassword = toString ( toSHA256(yamlConfigurator.getLogindUI()        + 
                                              yamlConfigurator.getPasswordUI() + timestamp)) ;

      if (hashedLoginPassword.equals(checkLoginPasswordSHA2))    {

          if ( ! logedIn )    {
              logedIn = true  ;
              System.out.println(" Redirect to index.xhtml - From LoginAuthenticator ");
              context.getExternalContext().redirect("index.xhtml") ;
          }
          
      } else {

        context.addMessage( null                                         ,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR , 
                            ""                                           ,
                            "   ** Error Auth **  "))                    ;
      }
    }
        
    private static byte[] toSHA256(String text) throws NoSuchAlgorithmException {
      MessageDigest sha256    = MessageDigest.getInstance("SHA-256")            ;
      byte[]        passBytes = text.getBytes(StandardCharsets.UTF_8)           ;
      return sha256.digest(passBytes)                                           ;
    }

    public static String toString(byte[] array) {
      BigInteger bI = new BigInteger(1, array)  ;
      return bI.toString(16)                    ;
    }
      
    public boolean isSecuredUI()                {
      return yamlConfigurator.isSecuredUI()     ;
    }
 
      
    public void logout() throws IOException {
        
       FacesContext context = FacesContext.getCurrentInstance()                                      ;
       context.getExternalContext().invalidateSession()                                              ;
       HttpServletResponse response =(HttpServletResponse)context.getExternalContext().getResponse() ;
       response.sendRedirect( yamlConfigurator.getRootApplicationContext() + "/index.xhtml")         ;
       context.responseComplete()                                                                    ;
       System.out.println("Log Out.... ")                                                            ;
    }

    public YamlConfigurator getYamlConfigurator() {
        return yamlConfigurator                   ;
    }
    
}

