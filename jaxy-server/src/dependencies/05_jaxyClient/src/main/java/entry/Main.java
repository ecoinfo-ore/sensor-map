
package entry ;

import java.io.File ;
import java.time.Instant ;
import java.nio.file.Files ;
import java.nio.file.Paths ;
import java.io.PrintWriter ;
import com.rac021.jaxy.client.mvc.Model ;
import com.rac021.jaxy.client.security.Digestor ;

/**
 *
 * @author ryahiaoui
 */

public class Main {

	
    public static void main(String[] args) throws Exception       {
        
       if( args.length == 0 ||  args[0].equalsIgnoreCase("help")) {
       
           System.out.println ("                                               "      ) ;
           System.out.println(" ============================================== "      ) ;
           System.out.println ("                                               "      ) ;
           System.out.println (" 1- In CustomSingOn Mode ( DB Authentication ),"
                               + " You have to Provide : " )                            ;
           System.out.println ("                                               "      ) ;
           System.out.println ("    [ confPath ] AND [ login ] AND [ password ] "     ) ;
           System.out.println ("                                               "      ) ;
           System.out.println ("  Ex Command :                                 "      ) ;
           System.out.println ("                                               "      ) ;
           System.out.println ("  java -jar jaxyClient.jar confPath jaxy_conf.txt "     +
                                  "login my_login password my_password "                +
                                  " out outputData.txt " )                              ;
           System.out.println ("                                               "      ) ;
           System.out.println(" ============================================== "      ) ;
           System.out.println ("                                               "      ) ;
           System.out.println (" 2- In SSO Mode ( Keycloak ), You have to Provide : " ) ;
           System.out.println ("                                               "      ) ;
           System.out.println ( "    [ confPath ] AND [ keycloak_client_id ] AND "      +
                                "[ keycloak_secret_id ] AND [ keycloak_login ] "        +
                                "AND [ keycloak_password ] " )                          ;
           System.out.println ("                                                "     ) ;
           System.out.println ("  Ex Command :                                  "     ) ;
           System.out.println ("                                                "     ) ;
           System.out.println ("  java -jar jaxyClient.jar confPath  jaxy_conf.txt"     +
                               " keycloak_client_id jaxyAppClient"                      +
                               " keycloak_secret_id jaxySecretApp"                      +
                               " keycloak_login my_login keycloak_password"             + 
                               " my_keycloack_password out outputData.txt  "          ) ;
           System.out.println ("                                               "      ) ;
           System.out.println(" ============================================== "      ) ;
           System.out.println ("                                               "      ) ;
            System.out.println(" 3- In Decryption mode, You have to provide one"        +
                              " of the two options : " )                                ;
           System.out.println ("                                               "      ) ;
           System.out.println("  * decrypt followed by [ pathFileToDecrypt ] AND"       +
                              " [ password ] AND  [ cipher ] AND  [ hash ] "          ) ;
           System.out.println("  * decrypt followed by [ confPath ] AND "               +
                              "[ pathFileToDecrypt ] AND [ password ] ")                ;
           System.out.println ("                                               "      ) ;
           System.out.println ("  Ex Command :                                 "      ) ;
           System.out.println ("                                               "      ) ;
           System.out.println (" java -jar jaxyClient.jar decrypt pathFileToDecrypt "   +
                               "./outputEncryptedData.txt password my_password "        +
                               " cipher AES_128_CBC hash MD5 out decryptedData.txt " )  ;
           System.out.println (" java -jar jaxyClient.jar decrypt pathFileToDecrypt "   +
                               "./outputEncryptedData.txt password my_password "        +
                               "confPath jaxy_conf.txt out decryptedData.txt " )        ;
           System.out.println ("                                               "      ) ;
           System.out.println(" ============================================== "      ) ;
           System.out.println ("                                               "      ) ;
           
           System.exit( 0 )  ;
       }
       
       String login               = null  ;
       String password            = null  ;
       String confPath            = null  ;
       
       String keycloak_secret_id  = null  ;
       String keycloak_client_id  = null  ;
       String keycloak_login      = null  ;
       String keycloak_password   = null  ;
       
       String pathFileToDecrypt   = null  ;
       
       String cipher              = null  ;
       String hashPasswordAlgo    = null  ;
       String hashLoginAlgo       = null  ;
       String hashTimeStampAlgo   = null  ;
       String out                 = null  ;
       boolean decryptMode        = false ;
       
       for ( int i = 0 ; i < args.length ; i++ )  {
            
         String token = args[i] ;
           
            switch(token)       {

              case "login"               : login              = args[i+1] ; i++ ;
                                           break ;
              case "password"            : password           = args[i+1] ; i++ ;
                                           break ;
              case "confPath"            : confPath           = args[i+1] ; i++ ;
                                           break ;    
              case "keycloak_client_id"  : keycloak_client_id = args[i+1] ; i++ ;
                                           break ;
              case "keycloak_secret_id"  : keycloak_secret_id = args[i+1] ; i++ ;
                                           break ;            
              case "keycloak_login"      : keycloak_login     = args[i+1] ; i++ ;
                                           break ;            
              case "keycloak_password"   : keycloak_password  = args[i+1] ; i++ ;
                                           break ;            
              case "decrypt"             : decryptMode        = true            ;
                                           break ;            
              case "pathFileToDecrypt"   : pathFileToDecrypt  = args[i+1] ; i++ ;
                                           break ;            
              case "cipher"              : cipher             = args[i+1] ; i++ ;
                                           break ;            
              case "hashPasswordAlgo"    : hashPasswordAlgo   = args[i+1] ; i++ ;
                                           break ;            
              case "hashLoginAlgo"       : hashLoginAlgo      = args[i+1] ; i++ ;
                                           break ;            
              case "hashTimeStampAlgo"   : hashTimeStampAlgo  = args[i+1] ; i++ ;
                                           break ;            
              case "out"                 : out                = args[i+1] ; i++ ;
                                           break ;            
           }
       }
       
       if ( confPath == null ) {
       
          confPath = new File ( System.getProperty("user.dir")                      + 
                                File.separator + "jaxy_conf.txt").getAbsolutePath() ;
       }
       
       if ( confPath != null                     && 
            ! Files.exists(Paths.get(confPath) ) && 
            ! decryptMode                      )  {
           
            System.out.println( "   " )                                   ;
            System.out.println( " File not found. Path : " + confPath )   ;
            System.out.println( "   " )                                   ;
            System.out.println(" For more details, run the command :  " ) ;
            System.out.println( "    java -jar jaxyClient.jar help    " ) ;
            System.out.println(" " )                                      ;
            System.exit( 0 )                                              ;
       }
      
       /** If decrypt Mode AND Cipher == null AND Hash == null 
           Then we need the Jaxy Conf File ( contains this informations ) . **/
       
       if ( ( confPath == null || ! Files.exists(Paths.get(confPath) ) )  && 
              cipher           == null             && 
              hashPasswordAlgo == null             && 
              decryptMode                          )  {
           
            System.out.println( "   " )                                   ;
            System.out.println( " File not found. Path : " + confPath )   ;
            System.out.println( "   " )                                   ;
            System.out.println(" For more details, run the command :  " ) ;
            System.out.println( "    java -jar jaxyClient.jar help    " ) ;
            System.out.println(" " )                                      ;
            System.exit( 0 )                                              ;
       }
      
       Configuration.initDisp ( confPath )                     ;
                
       if( hashLoginAlgo == null || 
           hashLoginAlgo.trim().isEmpty() )                    {
           hashLoginAlgo = Configuration.get("HashLoginAlgo")  ;
       }
       if( hashPasswordAlgo == null || 
           hashPasswordAlgo.trim().isEmpty() )                 {
           hashPasswordAlgo = 
                    Configuration.get("HashPasswordAlgo")      ;
       }
       if( hashTimeStampAlgo == null || 
           hashTimeStampAlgo.trim().isEmpty() )                {
           hashTimeStampAlgo = 
                    Configuration.get("HashTimeStampAlgo")     ;
       }
       if( cipher == null ||  cipher.trim().isEmpty() )        {
           cipher =  Configuration.get("Cipher")               ;
       }
        
       String params        = Configuration.get("Params")      ;
      
       String url           = Configuration.get("Url")         ;

       String urlKeyCloak   = Configuration.get("UrlKeycloak") ;

       String keep          = Configuration.get("Keep")        ;
               
       String accept        = Configuration.get("Accept")      ;
        
       String security      = Configuration.get("Security")    ;

       String algoSign      = Configuration.get("AlgoSign")    ;

       String _login        =  Model.hashMessage( login ,    hashLoginAlgo )     ;

       String _password     =  Model.hashMessage( password , hashPasswordAlgo )  ;

       long timeStampMillis = Instant.now().getEpochSecond()                     ;

       String timeStamp     = Model.hashMessage( String.valueOf(timeStampMillis) ,
                                                 hashTimeStampAlgo)              ;

       PrintWriter printWriter = out == null ? new PrintWriter(System.out)       : 
                                               new PrintWriter(new File(out))    ;

       if ( decryptMode )   {
           
           if ( pathFileToDecrypt == null || cipher == null  ||
                hashPasswordAlgo == null  || password == null )                              {
               
               System.out.println(" " )                                                      ;
                  
               System.out.println(" In Decryption mode, You have to provide : " )            ;
               System.out.println(" " )                                                      ;
               
               System.out.println("     decrypt AND [ pathFileToDecrypt ] AND"               +
                                  " [ password ] AND  [ cipher ] AND  [ hash ] ")            ;
               
               System.out.println(" OR ")                                                    ;
               
               System.out.println("     decrypt AND [ confPath ] AND [ pathFileToDecrypt ] " +
                                  " AND [ password ] ")                                      ;
                                  
               System.out.println(" " )                                                      ;
               System.out.println(" For more details, run the command :  " )                 ;
               System.out.println( "    java -jar jaxyClient.jar help    " )                 ;
               System.out.println(" " )                                                      ;
               System.exit( 0 )                                                              ;
              
           }
           
           String encryptedData = new String( Files.readAllBytes( 
                                              Paths.get(pathFileToDecrypt)))         ;
           
           String hashedPassword = Model.hashMessage( password ,  hashPasswordAlgo ) ;
            
           Model.decrypt( printWriter    ,
                          cipher         , 
                          hashedPassword ,
                          encryptedData  ) ;
           
           System.exit( 0 )                ;
       }
       
       if (  security != null  &&
           ( security.equalsIgnoreCase("Public")   )      )           {
         
           Model.invokeService_Using_Public( printWriter , 
                                             url         , 
                                             params      ,
                                             accept      ,
                                             keep        ) ;
       }
	    
       if (  security != null  &&
           ( security.equalsIgnoreCase("CustomSignOn")   )      )     {
           
           checkNotNull( "AlgeSign"          , algoSign          )    ;
           checkNotNull( "Password"          , password          )    ;
           checkNotNull( "Login"             , login             )    ;
           checkNotNull( "HashLoginAlgo"     , hashLoginAlgo     )    ;
           checkNotNull( "HashPasswordAlgo"  , hashPasswordAlgo  )    ;
           checkNotNull( "HashTimeStampAlgo" , hashTimeStampAlgo )    ;
           
           if( accept != null                                    &&
               accept.trim().toLowerCase().contains("encrypted") &&
               ( cipher == null || cipher.trim().isEmpty() ) )        {
               
               System.out.println(" " )                               ;
               System.out.println( " For Accept : [ " + accept + " ]" +
                                   " you Have to provide a Cipher " ) ;
               System.out.println( " Please, provide a Cipher   " )   ;
               System.out.println(" " )                               ;
               System.exit( 0 )                                       ;
           }
           
           String signe  = null                           ;
            
           if(algoSign.equalsIgnoreCase("SHA1"))          {
                signe = Digestor.toString(Digestor
                                .toSHA1 ( _login + _password + timeStamp )) ;
           }
           else if(algoSign.equalsIgnoreCase("MD5"))      {
                signe = Digestor.toString( Digestor
                                .toMD5 ( _login + _password + timeStamp )) ;
           }
           else if(algoSign.equalsIgnoreCase("SHA2"))     {
                signe = Digestor.toString( Digestor
                                .toSHA256(login + _password + timeStamp )) ;
           }
           
           Model.invokeService_Using_Custom( printWriter                            ,
                                             url                                    , 
                                             params                                 ,
                                             accept                                 , 
                                             _login + " " + timeStamp + " " + signe , 
                                             keep                                   , 
                                             cipher
           ) ;
           
       }
      
       else if ( security != null && security.equalsIgnoreCase("SSO"))      {
          
           if( keycloak_client_id == null || keycloak_secret_id == null ||
               keycloak_login     == null || keycloak_password  == null   ) {
           
               System.out.println ("                                  "   ) ;
               System.out.println (" In SSO Mode, You have to Provide [ confPath ] "        +
                                   "AND [ keycloak_client_id ] AND [ keycloak_secret_id ] " +
                                   " AND [ keycloak_login ] AND [ keycloak_password ] " )   ;
               System.out.println ("                                     " ) ;
               System.out.println(" For more details, run the command :  " ) ;
               System.out.println( "    java -jar jaxyClient.jar help    " ) ;
               System.out.println(" " )                                      ;
               System.exit( 0 )                                              ;
           }
            
           Model.invokeService_Using_SSO( printWriter        ,
                                          url                ,
                                          urlKeyCloak        ,
                                          params             ,
                                          accept             , 
                                          keep               ,
                                          keycloak_client_id ,
                                          keycloak_secret_id ,
                                          keycloak_login     ,
                                          keycloak_password           
           ) ;
       }
        
    }
    
    private static void checkNotNull ( String key , String toCheck )   {
          
           if( toCheck == null || toCheck.trim().isEmpty())            {
               System.out.println(" " )                                ;
               System.out.println( " "+ key + " not Provided ! " )     ;
               System.out.println( " Please, provide : "  +  key )     ;
               System.out.println(" " )                                ;
               System.exit( 0 )                                        ;
           }
    }
}

