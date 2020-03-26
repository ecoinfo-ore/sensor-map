#!/usr/bin/env bash

 CURRENT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
 
 cd $CURRENT_PATH

 docker-compose down 

 docker-compose up -d 
