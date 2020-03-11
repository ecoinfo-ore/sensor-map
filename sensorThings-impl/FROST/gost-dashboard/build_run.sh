#!/bin/sh

 docker build --no-cache -t gost-ui .

 # docker run --net host --rm --name gost-ui -p 8888:8080 gost-ui
 
 # docker run -p 8080:8080 -e GOST_WEBSOCKET_URL='wss://gosthost:443/ws'
