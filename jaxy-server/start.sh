#!/bin/bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH
 
./jaxy/run.sh serviceConf=jaxy/service-conf/serviceConf.yaml &

