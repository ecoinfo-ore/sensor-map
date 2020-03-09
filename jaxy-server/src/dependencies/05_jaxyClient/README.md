# JaxyClient [![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT) 


**jaxyClient** is an add-on for the projects **[Jaxy]( https://github.com/rac021/Jaxy)** 


## installation

```xml
  mvn clean compile assembly:single
```  
```xml
  java -jar target/jaxyClient.jar
```  

-------------------------------------

#### Sample customSingOn Client

```xml

java -jar jaxyClient.jar confPath jaxy_conf.txt \
                         password admin         \
                         login    admin        

```
-------------------------------------

#### Sample KeyCloak Client

```xml
java -jar jaxyClient.jar confPath            jaxy_conf.txt \
                         keycloak_client_id  my_app        \
                         keycloak_secret_id  my_secret     \
                         keycloak_login      admin         \
                         keycloak_password   password      \
                         out outputData.txt
```             

