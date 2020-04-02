#!/bin/bash

  CURRENT_SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
  cd $CURRENT_SCRIPT_PATH/sensor-map &&     \
     mvn clean install -Dmaven.test.skip=true 
 
  mv target/sensormap-quarkus-0.9-runner.jar ./sensor-map-0.9.jar
 
 mvn clean 
