#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH
 
 ./sensorThings-impl/FROST/run.sh
 
 cd $CURRENT_PATH
 
 ./sensor-map-server/start.sh

 echo  
 echo " ===============================================================================================  " 
 echo
 
 echo " Servers :                                                                                        "
 echo "  -  FROST          Deployed at :  http://localhost:8181/FROST-Server                             "
 echo "  -  Sensor-Map     Deployed at :  http://localhost:8080/rest/resources ( used by ui-sensor-map ) "
 echo
 echo " UI    :                                                                                          "
 echo "  -  GOST-UI        Deployed at :  http://localhost:8282                                          "
 echo "  -  Sensor-Map-Ui              :  sensor-map-ui/index.html (  Launch on browser )                "
 echo 
 echo " ===============================================================================================  " 
 echo
 
