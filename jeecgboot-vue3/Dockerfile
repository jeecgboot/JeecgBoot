FROM nginx
MAINTAINER jeecgos@163.com
VOLUME /tmp
ENV LANG en_US.UTF-8
RUN echo "server {  \
                      listen       80; \
                      location   /jeecgboot/ { \
                      proxy_pass              http://jeecg-boot-system:8080/jeecg-boot/; \
                      proxy_redirect          off; \
                      proxy_set_header        Host jeecg-boot-system; \
                      proxy_set_header        X-Real-IP \$remote_addr; \
                      proxy_set_header        X-Forwarded-For \$proxy_add_x_forwarded_for; \
                  } \
                  #解决Router(mode: 'history')模式下，刷新路由地址不能找到页面的问题 \
                  location / { \
                     root   /var/www/html/; \
                      index  index.html index.htm; \
                      if (!-e \$request_filename) { \
                          rewrite ^(.*)\$ /index.html?s=\$1 last; \
                          break; \
                      } \
                  } \
                  access_log  /var/log/nginx/access.log ; \
              } " > /etc/nginx/conf.d/default.conf \
    &&  mkdir  -p  /var/www \
    &&  mkdir -p /var/www/html

ADD dist/ /var/www/html/
EXPOSE 80
EXPOSE 443
