#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH

######################################
##### SERVER CONFIG ##################
######################################

 HTTP_PORT="8080"
 HTTPS_PORT="8585"
 
 KEYSTORE_FILE="server.keystore"
 
 RAND_PASSWORD=`date +%s | sha256sum | base64 | head -c 32 ; echo`

if [ -f $KEYSTORE_FILE ]; then 
   rm -f $KEYSTORE_FILE
 fi
 
 echo ; echo
 
 keytool -genkeypair   -storepass $RAND_PASSWORD -keyalg RSA    \
         -keysize 2048 -dname "CN=server"        -alias server  \
         -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore $KEYSTORE_FILE

 echo

######################################

 # java -jar sensor-map/sensor-map-0.9.jar &

 java  -Dquarkus.http.port=$HTTP_PORT                                        \
       -Dquarkus.http.ssl-port=$HTTPS_PORT                                   \
       -Dquarkus.http.ssl.certificate.key-store-file=$KEYSTORE_FILE          \
       -Dquarkus.http.ssl.certificate.key-store-password=$RAND_PASSWORD      \
                                                                             \
       -Dquarkus.datasource.driver=org.postgresql.Driver                     \
       -Dquarkus.datasource.url=jdbc:postgresql://localhost:2346/sensorusers \
       -Dquarkus.hibernate-orm.database.generation=none                      \
       -Dquarkus.datasource.username=postgres                                \
       -Dquarkus.datasource.password=postgres                                \
       -Dquarkus.datasource.jdbc.min-size=4                                  \
       -Dquarkus.datasource.jdbc.max-size=16                                 \
       -jar sensor-map/sensor-map-0.9.jar  &

 # For Only HTTPS :
 # -Dquarkus.http.insecure-requests=redirect
 
# FOR DEBUG :
# -Xdebug -Xrunjdwp:transport=dt_socket,address=11555,server=y,suspend=y \
 
