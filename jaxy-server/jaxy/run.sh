#!/bin/bash

 # Cmd Example : ./run.sh serviceConf=demo/Full_Conf/serviceConf.yaml       trustStore=keystoreKeyCloak.jks  debug
 # Cmd Example : ./run.sh serviceConf=jaxy/demo/Full_Conf/serviceConf.yaml  auto_extract_keycloak_certificate
  
 CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
   
 help() {
 
    echo
    echo " Total Arguments : Four                                                                                            "
    echo 
    echo "   serviceConf=                      :  Path of the serviceConf File ( REQUIRED )                                  "
    echo "   debug                             :  Start jaxy in debug mode                                                   "
    echo "   trustStore=                       :  Path of the certificate to Trust ( for trusting self-signed certiciates )  "
    echo "   auto_extract_keycloak_certificate :  Automatic download of keycloak SSL certificate FROM https://localhost:8543 "
    echo "                                        Then add this certificate in the JAXY TrustStore ( Only in HTTPS Mode )    "
    echo "   keycloakHost=                     :  Keycloak Host ( DEFAULT : localhost )                                      "
    echo "   keycloakPort=                     :  Keycloak Port ( DEFAULT : 8543      )                                      "
    echo 
    echo " Sample Cmd : ./run.sh  serviceConf=jaxy/demo/Full_Conf/serviceConf.yaml  trustStore=keystoreKeyCloak.jks  debug   "
    echo "              ./run.sh  serviceConf=jaxy/demo/Full_Conf/serviceConf.yaml  auto_extract_keycloak_certificate        "
    echo
    exit ;
 }

 while [[ "$#" > "0" ]] ; do
 
  case $1 in
  
      (*=*) KEY=${1%%=*}
      
            VALUE=${1#*=}
            
            case "$KEY" in
                               
                ("serviceConf")   CONFIGURATION_FILE=$VALUE
                ;;                    
                ("trustStore")    TRUST_STORE=$VALUE
                ;;                    
                ("keycloakHost")  KEYCLOAK_HOST=$VALUE
                ;;                    
                ("keycloakPort")  KEYCLOAK_PORT=$VALUE
                
            esac
      ;;
      
      debug)    DEBUG="true"
      
      ;; 
      
      auto_extract_keycloak_certificate) KEYCLOAK="AUTO_EXTRACT_CERTIFICATE_FROM_KEYCLOAK"
      
      ;; 

      help)  help
  esac
  
  shift
  
 done 
 
 if [ -f "$CONFIGURATION_FILE" ]; then 
     # Get Real Path of the File Configuration :
    CONFIGURATION_FILE="-DserviceConf=$(realpath $CONFIGURATION_FILE)"    
 elif [ -f "./serviceConf.yaml" ]; then 
    CONFIGURATION_FILE="-DserviceConf=$(realpath ./serviceConf.yaml)" 
 else
     cd $CURRENT_DIR ;
     if [ -f "./serviceConf.yaml" ]; then 
       CONFIGURATION_FILE="-DserviceConf=$(realpath ./serviceConf.yaml)" 
     else 
         echo ;
         if [ -z "$CONFIGURATION_FILE" ]; then
             echo " Missed : [ serviceConf ] Argument " ; echo 
             help ;
         else 
            echo " serviceConf : [ $CONFIGURATION_FILE ] Not Found " ; 
            echo ; exit ;
         fi
         echo ;
     fi
 fi
 
 
 cd $CURRENT_DIR
 
 SRC_JAXY_LOCATION="../src/jaxy/target/jaxy-thorntail.jar"
   
 if [ -f "$SRC_JAXY_LOCATION" ]; then
 
    echo 
    echo " Move [ $SRC_JAXY_LOCATION ] TO [ $CURRENT_DIR/ ] " 
    echo
    mv $SRC_JAXY_LOCATION $CURRENT_DIR/
 
 fi
 
 
 KEYCLOAK_CERTIFICATE_NAME="keycloak.cert"
 
 KEYCLOAK_CERTIFICATE_NAME_JKS="keycloak.jks"
 
 KEYCLOAK_HOST=${KEYCLOAK_HOST:-"localhost"}
 KEYCLOAK_PORT=${KEYCLOAK_PORT:-"8543"}
 
 if [ "$KEYCLOAK" == "AUTO_EXTRACT_CERTIFICATE_FROM_KEYCLOAK" ] ; then
 	
    if [ -f "$KEYCLOAK_CERTIFICATE_NAME" ]; then
        rm $KEYCLOAK_CERTIFICATE_NAME
    fi
    
    if [ -f "$KEYCLOAK_CERTIFICATE_NAME_JKS" ]; then
        rm $KEYCLOAK_CERTIFICATE_NAME_JKS
    fi
    
    # HTTPS MODE
    # Download the KEYCLOAK certificate that will be trusted by JAXY
    echo
    echo " Automatic download of keycloak SSL certificate from https://$KEYCLOAK_HOST:$KEYCLOAK_PORT" 
    echo 
    echo "Q" | openssl s_client -host $KEYCLOAK_HOST -port $KEYCLOAK_PORT -prexit -showcerts > $KEYCLOAK_CERTIFICATE_NAME
    
    keytool -importcert                              \
            -file $KEYCLOAK_CERTIFICATE_NAME         \
            -storetype JKS                           \
            -keystore $KEYCLOAK_CERTIFICATE_NAME_JKS \
            -storepass change-it                     \
            -alias $KEYCLOAK_CERTIFICATE_NAME_JKS    \
            -noprompt
    
    rm $KEYCLOAK_CERTIFICATE_NAME
   
 fi

  
 if [ "$DEBUG" == "true" ]; then
 
     DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,address=11555,server=y,suspend=y"
 else 
     
     DEBUG=""
     
 fi 
   
 if [ ! -z "$TRUST_STORE" ]; then
    
    TRUST_STORE="-Djavax.net.ssl.trustStore=$TRUST_STORE"
 fi
 
 if [ ! -z "$KEYCLOAK" ]; then
    
    TRUST_STORE_KEYCLOAK="-Djavax.net.ssl.trustStore=$KEYCLOAK_CERTIFICATE_NAME_JKS"
 fi
 
 
 ## Run Jaxy
  
 java  $DEBUG $TRUST_STORE $TRUST_STORE_KEYCLOAK $CONFIGURATION_FILE -jar $CURRENT_DIR/jaxy-thorntail.jar    


