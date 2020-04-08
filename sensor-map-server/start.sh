#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH

 # java -jar sensor-map/sensor-map-0.9.jar &
 
 java  -Dquarkus.datasource.driver=org.postgresql.Driver                     \
       -Dquarkus.datasource.url=jdbc:postgresql://localhost:2346/sensorusers \
       -Dquarkus.hibernate-orm.database.generation=none                      \
       -Dquarkus.datasource.username=postgres                                \
       -Dquarkus.datasource.password=postgres                                \
       -Dquarkus.datasource.jdbc.min-size=4                                  \
       -Dquarkus.datasource.jdbc.max-size=16                                 \
       -jar sensor-map/sensor-map-0.9.jar  &

# FOR DEBUG :
# -Xdebug -Xrunjdwp:transport=dt_socket,address=11555,server=y,suspend=y \
 
