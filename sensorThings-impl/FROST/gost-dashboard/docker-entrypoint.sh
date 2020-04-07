#!/bin/sh

sed '1d' /var/www/html/js/app.js > temp; mv temp /var/www/html/js/app.js

if [[ -z "${GOST_WEBSOCKET_URL}" ]]; then
	echo "var wsURL = \"\";"  | cat - /var/www/html/js/app.js > temp && mv temp /var/www/html/js/app.js
else
	urlParam="var wsURL = \"$GOST_WEBSOCKET_URL\";";	
	echo "$urlParam" | cat - /var/www/html/js/app.js > temp && mv temp /var/www/html/js/app.js
fi

if [[ -z "${NGINX_ENABLE_SSL}" ]]; then
  ssl="false"
else
  ssl="${NGINX_ENABLE_SSL}"
fi

echo "
client_body_buffer_size 		10K;
client_header_buffer_size 		1k;
client_max_body_size 			8m;
large_client_header_buffers 	2 1k;
client_body_timeout 			12;
client_header_timeout 			12;
send_timeout 	 			10;
gzip             			on;
gzip_comp_level  			2;
gzip_min_length  			1000;
gzip_proxied     			expired no-cache no-store private auth;
gzip_types       			text/plain application/x-javascript text/xml text/css application/xml application/json;
access_log 				off;
" > /etc/nginx/conf.d/default.conf

echo "server {" >> /etc/nginx/conf.d/default.conf

if [ "$ssl" == "true" ]; then

echo "Creating NGINX default.conf with ssl"

echo " 
	listen 443 ssl http2;
	listen [::]:443 ipv6only=on ssl;

	ssl_certificate /etc/certs/fullchain.pem;
	ssl_certificate_key /etc/certs/privkey.pem;	
	ssl_session_timeout 5m;
	ssl_protocols TLSv1 TLSv1.1 TLSv1w.2;
	ssl_ciphers ALL:!ADH:!EXPORT56:RC4+RSA:+HIGH:+MEDIUM:+LOW:+SSLv3:+EXP;
	ssl_prefer_server_ciphers on;"  >> /etc/nginx/conf.d/default.conf
	
else

echo "Creating NGINX default.conf without ssl"

echo "	
	listen 8282;
	listen [::]:8282 ipv6only=on;
    server_name localhost ;" >> /etc/nginx/conf.d/default.conf

fi

echo "

    root /var/www/html;
    index index.html;
    access_log /var/nginx-access.log ;
  
    location / {
 
        proxy_redirect              off;
        proxy_buffers               32 16k;
        proxy_busy_buffers_size     64k;
              
        if (\$request_method = 'OPTIONS') {
           add_header 'Access-Control-Allow-Origin' '*' ;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, PUT, PATCH, DELETE';
           add_header 'Access-Control-Allow-Headers' '*';
           add_header 'Access-Control-Max-Age' 1728000;
           add_header 'Content-Type' 'application/json, charset=UTF-8';
           add_header 'Content-Length' 0;
           
		  # add_header 'TEST_HEADER_RAC' 'INRAE';
           
           return 204;
		}
		if (\$request_method = 'GET') {
           add_header 'Access-Control-Allow-Origin' '*' always;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, PUT, PATCH, DELETE';
           add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Accept';
           add_header 'Access-Control-Expose-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
        }
        if (\$request_method = 'POST') {
           add_header 'Access-Control-Allow-Origin' '*' always;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, PUT, PATCH, DELETE';
           add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Accept';
           add_header 'Access-Control-Expose-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
		}
		if (\$request_method = 'DELETE') {
           add_header 'Access-Control-Allow-Origin' '*' always;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, PUT, PATCH, DELETE';
           add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Accept';
           add_header 'Access-Control-Expose-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
        }
        if (\$request_method = 'PATCH') {
           add_header 'Access-Control-Allow-Origin' '*' always;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, PUT, PATCH, DELETE';
           add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Accept';
           add_header 'Access-Control-Expose-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
        }
        if (\$request_method = 'PUT') {
           add_header 'Access-Control-Allow-Origin' '*' always;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS, PUT, PATCH, DELETE';
           add_header 'Access-Control-Allow-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Accept';
           add_header 'Access-Control-Expose-Headers' 'DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range';
        }
    }

    location = /favicon.ico { access_log off; log_not_found off; }
    
}" >> /etc/nginx/conf.d/default.conf


DEFAULT_SENSORTHINGS_URL="http://localhost:8181/"
SENSORTHINGS_URL="$SENSORTHINGS_URL"

if [ "$SENSORTHINGS_URL" != "" ]; then 
  echo " + Update SensorThings Server URL : $SENSORTHINGS_URL"
  sed -i "s|$DEFAULT_SENSORTHINGS_URL|$SENSORTHINGS_URL|g" /var/www/html/js/app.js
fi


if [ "$SENSORTHINGS_IDS" = "STRING" ]; then 
 
   ENTITY="entity\[\"@iot.id\"\]" ;
   WRAPPED_ENTITY=" \"'\" + $ENTITY + \"'\" " 

   echo " + Update SensorThings IDS to STRING "
   find /var/www/html/ -iname "*.js" -type f -exec \
   sed -i "s|$ENTITY|$WRAPPED_ENTITY|g" {} +

   SENSOR_ENTITY="sensor\[\"@iot.id\"\]" ;
   WRAPPED_SENSOR_ENTITY=" \"'\" + $SENSOR_ENTITY + \"'\" " 
   find /var/www/html/ -iname "*.js" -type f -exec \
   sed -i "s|$SENSOR_ENTITY|$WRAPPED_SENSOR_ENTITY|g" {} +   
   
   ENTITY="\$scope.Page.selectedThing\[\"@iot.id\"\]"
   WRAPPED_ENTITY=" \"'\" + $ENTITY + \"'\" " 
   find /var/www/html/ -iname "*.js" -type f -exec \
   sed -i "s|$ENTITY|$WRAPPED_ENTITY|g" {} +
   
   ENTITY="Things(\" + \$scope.id + \")"
   WRAPPED_ENTITY="Things(\" + \"'\" + \$scope.id + \"'\" + \")"
   find /var/www/html/ -iname "*.js" -type f -exec \
   sed -i "s|$ENTITY|$WRAPPED_ENTITY|g" {} +
  
   ENTITY_DATASTREAM="Datastreams(\" + \$scope.id + \""
   WRAPPED_ENTITY_DATASTREAM="Datastreams(\" + \"'\" + \$scope.id + \"'\" + \""
   find /var/www/html/ -iname "*.js" -type f -exec \
   sed -i "s|$ENTITY_DATASTREAM|$WRAPPED_ENTITY_DATASTREAM|g" {} +
   
fi

exec "$@"
