FROM mysql:8.0.19

MAINTAINER taskos@163.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./taskboot-mysql-5.7.sql /docker-entrypoint-initdb.d
