#https://blog.csdn.net/sinat_19528249/article/details/99994464
# Version 0.1
# 基础镜像
FROM centos:7
# 维护者信息
MAINTAINER kangxiaolin ksf@zgykkj.com
VOLUME /tmp
#自动安装依赖
RUN  cd /etc/yum.repos.d/ \
    && yum -y install wget \
    && wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo \
    && yum clean all \
    && yum makecache \
    && yum update -y \
    &&  yum -y  install nginx  \
    &&  yum -y  install  java-1.8.0-openjdk java-1.8.0-openjdk-devel  \
    &&  yum -y  install redis    \
#这里可以设置 域名
# 可以送本地加载 使用ADD 也可以直接写
   && echo "server {  \
                       listen       81; \
                       location ^~ /jeecg-boot { \
                      proxy_pass              http://127.0.0.1:8080/jeecg-boot/; \
                      proxy_set_header        Host 127.0.0.1; \
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
              } " > /etc/nginx/conf.d/default.conf  \

 # 设置启动脚本
     && touch /etc/init.d/start.sh \
     && touch jeecgboot.log \
     && chmod +x /etc/init.d/start.sh \
     && echo "#!/bin/bash  " >> /etc/init.d/start.sh \
     && echo "/usr/bin/redis-server  & " >> /etc/init.d/start.sh \
     && echo "/usr/sbin/nginx  -c /etc/nginx/nginx.conf" >> /etc/init.d/start.sh \
     && echo " java -jar /jeecgboot.jar   " >> /etc/init.d/start.sh \
     &&  mkdir  -p  /var/www \
     &&  mkdir -p /var/www/html
# 前端迁移到系统文件中 默认是80端口 同级目录下的html地址
ADD  ant-design-vue-jeecg/dist/ /var/www/html/
# 拷贝相关的jar包
ADD jeecg-boot/jeecg-boot-module-system/target/jeecg-boot-module-system-2.1.1.jar jeecgboot.jar
EXPOSE  80  8080 81
ENTRYPOINT /bin/sh -c   /etc/init.d/start.sh

#启动脚本
#docker rmi $(docker images | grep "^<none>" | awk "{print $3}") 删除<none>

#docker build -t jeecgboot:centos .
#docker run --privileged=true  -itd --name test -v /sys/fs/cgroup:/sys/fs/cgroup:ro  -p 81:81 -p 8080:8080 -p 82:80  jeecgboot:centos
