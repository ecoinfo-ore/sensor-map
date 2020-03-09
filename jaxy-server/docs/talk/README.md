### SI_ORL_Présentation

--------------------------------------

### **Agenda :**

| <ins>Session</ins>  | <ins>Reference</ins>  |  <ins>Time</ins> |
|:---------------------------|:----------------------------|:----------------------------|
| **Talk** | [ Short_Talk_2020](https://github.com/rac021/Jaxy/blob/master/docs/talk/Jaxy.pdf) | ~ 15 mn |
|  **Demo** | [Short_Demo](https://github.com/rac021/Jaxy/tree/master/docs/talk#requirements-) | ~ 15 mn |
|  Resources | [Resources](https://github.com/rac021/Jaxy/blob/master/docs/talk/README.md#resources-) | ---- |
-----------------------------------------------------

###### **Requirements :**

| Tool               | Requirement                              | 
|:-------------------|:--------------------------------         |
| `JAVA 8`           | **Required**                             |
| `MAVEN 3.3.9 +`    | **Required for Compilation**             |
| `Postgres - mySql` | **Required**                             |
| `git`              | **Required for Cloning the project**     |
| `openssl`          | **Required for LetsEncrypt + Decryption**|
| `curl`             | **Required for Script client**           |

---

 I )   [Download](https://sourceforge.net/projects/jaxy/files/jaxy_2.2_bin.zip/download) / or [Compile](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/00_0_installing_jaxy) ( For the most courageour ) the project :
 
         1 ) Download : https://sourceforge.net/projects/jaxy/files/jaxy_2.2_bin.zip/download
         
         2 ) Compilation Steps : https://github.com/rac021/Jaxy/tree/master/jaxy/demo/00_0_installing_jaxy
 
         3 ) cd jaxy # ( From the root of the project )
         
 II )   [Install demo Database](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/00_db-script) :
 
         1 )  https://github.com/rac021/Jaxy/tree/master/jaxy/demo/00_db-script    
         
       
 III )  Public_services : [Minimalist configuration file](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/01_public_services) :
  
         1 )  https://github.com/rac021/Jaxy/tree/master/jaxy/demo/01_public_services
       
 
 IV )  Secured Services using CSO ( [With Full_Conf](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/Full_Conf) )
 
         1 )  ./run.sh serviceConf=demo/Full_Conf/serviceConf.yaml
 
         2 )  Go to UI : https://localhost:8443/jaxy
         
         3 )  Test UI Auth : ( Login / password : jaxy / jaxy )
         
         4 )  Test secured Services ( vip_plane ) ( without Authentication )
              
         5 )  Test Generated scripts client ( service vip_plane : XML / JSON )
            
              - Copy/download the Generated jaxy_script : jaxy_client.sh
              
              - ./jaxy_client.sh login=admin password=admin
              
              -  Filter on : total_pssengers > 300 
         
              - Keep Only : model 
         
              - Keep Only model + distance_km 
              
              - Change Accepted_Format to : XML/ENCRYPTED ( With Cipher : AES_256_CBC ) 
              
              - Update jaxy_client.sh
              
              - ./jaxy_client.sh login=admin password=admin 
              
              - ./jaxy_client.sh login=admin password=admin > encrypted_data.txt
        
         6 ) Test Generated java client 
         
              - Copy/download the generated jaxy_conf.txt
              
              - Download the  jaxyClient.jar
              
              - java -jar jaxyClient.jar confPath jaxy_conf.txt login admin password admin
              
              - Change Accepted_Format to : XML/ENCRYPTED ( With Cipher : AES_256_CBC ) 
              
              - Update  jaxy_conf.txt
              
              - java -jar jaxyClient.jar confPath  jaxy_conf.txt    \
                                         login     admin            \
                                         password  admin            \
                                         out       encrypted_data.txt
         
 
 V  ) Test Decryption Features : 
 
         1 ) Using Script_Decryptor : 
            
             1.1 ) Copy / download The generated jaxy_decryptor.sh 
         
             1.2 ) ./jaxy_decryptor.sh file=encrypted_data.txt  password=admin
             
             1.3 ) https://github.com/rac021/Jaxy/tree/master/jaxy/demo/06_test_scripts_for_decryption 
                  
         2 ) Using Java Client :
         
             2.1 java -jar jaxyClient.jar decrypt                              \
                                          pathFileToDecrypt encrypted_data.txt \
                                          confPath          jaxy_conf.txt      \
                                          password          admin
         
         
 VI ) Secured Services using SSO ( [Keycloak  With HTTPS + Full_Conf](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/Full_Conf) )
 
         1 )  Start keycloak Server :
         
               docker run -d                     \
                          --net host             \
                          -e "TRANSPORT=https"   \
                          -e "MODE=DEMO"         \
                          --name keycloakme      \
                          rac021/jaxy-keycloakme                         
         
               => Creates :          
                   - REALM            : my_realm
                   - CLIENT_ID        : my_app
                   - CLIENT_SECRET_ID : my_secret
                   - USER_1           : with login admin  / password admin
                   - USER_2           : with login public / password public
                   - ROLE             : manager
                   - Affect the Role "Manager" to the Client "my_app" 
                   - Affect the Role "Manager" to the user Admin 
             
         2 )  Take a look at the Keycloak configuration : https://localhost:8543
         
         3 )  ./run.sh serviceConf=demo/Full_Conf/serviceConf_keycloak.yaml \
                       auto_extract_keycloak_certificate
 
         4 )  Test Generated scripts client
         
         5 )  Test Generated java client 
         
              =>     java -jar jaxyClient.jar  confPath            jaxy_conf.txt \
                                               keycloak_client_id  my_app        \
                                               keycloak_secret_id  my_secret     \
                                               keycloak_login      admin         \
                                               keycloak_password   admin
         
         6 ) Remove Manager Role for Admin User & Test vip_planes service
         
         
 VII )  [Jaxy Monitoring ( Prometheus / Grafana )](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/18_Docker#3-docker-compose-) :
         
         1 ) cd demo/18_Docker/
         
         2 ) docker-compose -f docker-compose-sso.yml up
         
         3 ) Go to : 
          
             - http://jaxy:8181 # ( Login / password : jaxy / jaxy )
             
             - http://jaxy-prometheus:9090 
             
             - http://jaxy-grafana:3000 
             
         4 ) Test real time Total_Called_Services Dashboard 
         
         5 ) Test real time Response_Services Dashboard
         
         6 ) Test real time Monitoring Dashboard
         
         7 ) Test real time Faillure_Authentication Dashboard
         
         8 ) Test real time Exceptions Dashboard
         
         
 VIII )  [Cloud Deployment](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/18_Docker#4-try-play-with-docker-) :
         
         1 ) Because it works on the local machine, it should also works in the cloud :-) : 
             
             https://github.com/rac021/Jaxy/tree/master/jaxy/demo/18_Docker#4-try-play-with-docker-
         
  
 IX  )  [LetsEncryt Feature : X509 certificates Generator](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/17_test_letsEncrypt) :
 
         1 )  cd jaxy
      
         2 )  docker run -dit --name apache-web -p 80:80 -v /var/www/html/ httpd:2.4     

         3 ) ./run.sh serviceConf=demo/17_test_letsEncrypt/serviceConf.yaml 
       
       
 X  ) Bonus : GUI Client :
        
         1 )  cd jaxy
         
         2 )  java -jar lib/client/jaxy-client.jar



--------------------------------------

 #### Resources :

| Resource  | Link |  Link |
|:---------------------------|:----------------------------|:---------------------|
| Talks|  [Full_Talk_2017](https://github.com/rac021/Jax-Y/blob/master/demo_sourceForge/Talk_PasSageEnSeine/Jax-Y.pdf) | [ Short_Talk_2020](https://github.com/rac021/Jaxy/blob/master/docs/talk/Jaxy.pdf)  |
| Jaxy_src |    [sourceforge_project](https://sourceforge.net/projects/jaxy/files/Jaxy_2.2_src.zip/download)    |    [github_project](https://github.com/rac021/Jaxy) |
| Jaxy_bin | [bin_project](https://sourceforge.net/projects/jaxy/files/jaxy_2.2_bin.zip/download)  |    [CSO Auth Mechanism](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/02_secured_services_with_custom_signon_auth) |

