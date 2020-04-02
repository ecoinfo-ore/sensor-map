#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH

 java -jar sensor-map/sensor-map-0.9.jar &

