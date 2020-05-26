#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH
 
 cp -a sensor-map-ui/. \
       sensor-map-server/sensor-map/src/main/resources/META-INF/resources/
       
 ./sensor-map-server/compile.sh

