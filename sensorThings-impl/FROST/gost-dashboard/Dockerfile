FROM nginx:alpine

COPY content /var/www/html
COPY docker-entrypoint.sh /etc/nginx/docker-entrypoint.sh

COPY default.conf /etc/nginx/conf.d/default.conf

RUN chmod +x /etc/nginx/docker-entrypoint.sh

ENTRYPOINT ["/etc/nginx/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
