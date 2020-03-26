#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH
 
 ./sensorThings-impl/FROST/run.sh
 
 cd $CURRENT_PATH
 
 ./jaxy-server/start.sh
