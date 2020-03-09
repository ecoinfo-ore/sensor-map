# Jaxy  <img src="https://cloud.githubusercontent.com/assets/7684497/25315596/e191fb00-2857-11e7-99bf-8e233b4eb795.jpg" width="50"> [![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
<sup>Setting up web services has never been easier</sup>

| Branch  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  |  build status ( Travis ) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; |  [ Docker Pulls ](https://hub.docker.com/r/rac021/jaxy) | SourceForge |
|-----------|---------------|---------------------------|---------------------------|
| [master](https://github.com/rac021/Jaxy/tree/master)  | [![Build Status](https://api.travis-ci.org/rac021/Jaxy.svg?branch=master)](https://travis-ci.org/rac021/Jaxy) | ![Docker Pulls](https://img.shields.io/docker/pulls/rac021/jaxy.svg) | [ Jaxy Download Link ]( https://sourceforge.net/projects/jaxy/)

------------------------------------------------------

 
| **Description** | **Jaxy** is a Generic Jax-Rs web Service which generates robuste and secured **web services** from **SQL queries**, based over yaml configuration, it's implements the project **[Jaxy-Api]( https://github.com/rac021/Jaxy/tree/master/src/dependencies/01_jaxy-api)**  | 
|:-------------|:-------------|
| **Features** |  [ Jaxy Provided Features ](https://github.com/rac021/Jaxy/tree/master/docs)            |
| **Youtube**  |  [![Watch the video](https://user-images.githubusercontent.com/7684497/50719557-12ee6000-109e-11e9-8b9e-4c085b9b0760.png)](https://www.youtube.com/watch?v=6IqxzSankpw&list=PLgd4yhA9GWz3lc2XmuW1lwlH3sjT4gHwa)  |
| **Play With Docker** | [![Try in PWD](https://raw.githubusercontent.com/play-with-docker/stacks/master/assets/images/button.png)](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/18_Docker#4-try-play-with-docker-)


------------------------------------------------------

**Links and structuring of the project :** 

- **Jaxy ( Java Project ) ( Directory : [src/jaxy](https://github.com/rac021/Jaxy/tree/master/src/jaxy) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/jaxy]( https://github.com/rac021/Jaxy/tree/master/src/jaxy) 
      
- **Jaxy-Api : ( Directory : [src/dependencies/01_jaxy-api](https://github.com/rac021/Jaxy/tree/master/src/dependencies/01_jaxy-api) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/dependencies/jaxy-api]( https://github.com/rac021/Jaxy/tree/master/src/dependencies/01_jaxy-api)

- **Default Security Provider Implementation ( Directory : [src/dependencies/02_jaxy-security-provider](https://github.com/rac021/Jaxy/tree/master/src/dependencies/02_jaxy-security-provider) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/dependencies/jaxy-security-provider]( https://github.com/rac021/Jaxy/tree/master/src/dependencies/02_jaxy-security-provider) 
     
- **Default Service Discovery Implementation  ( Directory : [src/dependencies/03_jaxy-service-discovery](https://github.com/rac021/Jaxy/tree/master/src/dependencies/03_jaxy-service-discovery) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/dependencies/jaxy-service-discovery]( https://github.com/rac021/Jaxy/tree/master/src/dependencies/03_jaxy-service-discovery) 

- **CertMe ( Lets's Encrypt Certificate Generator ) ( Directory : [src/dependencies/04_certMe](https://github.com/rac021/Jaxy/tree/master/src/dependencies/04_certMe) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/dependencies/certMe]( https://github.com/rac021/Jaxy/tree/master/src/dependencies/04_certMe) 

- **Default Jaxy-Client-Api ( Downloadable from Jaxy Web-UI ) ( Directory : [src/dependencies/05_jaxyClient](https://github.com/rac021/Jaxy/tree/master/src/dependencies/05_jaxyClient) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/dependencies/jaxyClient-Api]( https://github.com/rac021/Jaxy/tree/master/src/dependencies/05_jaxyClient) 
   
- **GUI ( Directory : [src/add-on/JaxyClientUi](https://github.com/rac021/Jaxy/tree/master/src/add-on/JaxyClientUi) ) :**
   * [https://github.com/rac021/Jaxy/tree/master/src/add-on/JaxyClientUi]( https://github.com/rac021/Jaxy/tree/master/src/add-on/JaxyClientUi) 

-----------------------------------------------------

**Requirements :**

-    `JAVA 8`
    
-    `MAVEN 3.3.9 + `
   
-    `Postgres | mySql `

---

## Installation : 

 -  ### [ 00_0. Installing Jaxy ](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/00_0_installing_jaxy)

 -  ### [ 00_1. Installing the demo Database ](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/00_db-script)
 
 ---
 
## Demo 

 -  ### [ 01. Public services ( Minimalist configuration file ) ](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/01_public_services)
 
 -  ### [ 02. Secured services with Custom SignOn Auth ](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/02_secured_services_with_custom_signon_auth)
 
 -  ### [ 03. Web-UI](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/03_web_ui)
 
 -  ### [ 04. Test scripts for Public Services](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/04_test_scripts_for_public_services)
 
 -  ### [ 05. Test scripts for Secured Services](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/05_test_scripts_for_secured_services)
  
 -  ### [ 06. Test scripts for Decryption](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/06_test_scripts_for_decryption)
  
 -  ### [ 07. Test Templating](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/07_test_templating)
 
 -  ### [ 08. Test java Client for Public Services](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/08_test_java_client_for_public_services)
  
 -  ### [ 09. Test java Client for Secured Services](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/09_test_java_client_for_secured_services)
 
 -  ### [ 10. Test java Client for Decryption](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/10_test_java_client_for_decryption)
 
 -  ### [ 11. Secured services with keycloak Auth](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/11_secured_services_with_keycloak_auth)
 
 -  ### [ 12. Test scripts keycloak](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/12_test_scripts_keycloak)
 
 -  ### [ 13. Test java Client keycloak](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/13_test_java_client_keycloak)
 
 -  ### [ 14. Test CustomSignOn Auth from Ui](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/14_test_CustomSignOn_Auth_from_Ui)

 -  ### [ 15. Test Keycloak Auth from Ui](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/15_test_KeyCloak_Auth_from_Ui)

 -  ### [ 16. Test Monitoring](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/16_test_monitoring)

 -  ### [ 17. Test letsEncrypt](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/17_test_letsEncrypt)
 
 -  ### [ 18._Docker](https://github.com/rac021/Jaxy/tree/master/jaxy/demo/18_Docker)
 
------------------------------------------------------

  [Talk_2017_PasSageEnSeine]( https://github.com/rac021/Jax-Y/blob/master/demo_sourceForge/Talk_PasSageEnSeine/Jax-Y.pdf
) ( PDF ) 


 [Talk_2020_Infosol](https://github.com/rac021/Jaxy/tree/master/docs/talk) ( PDF + Demo )


