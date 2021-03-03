FROM mysql:8.0.19

MAINTAINER jeecgos@163.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./tables_nacos.sql /docker-entrypoint-initdb.d

COPY ./jeecgboot-mysql-5.7.sql /docker-entrypoint-initdb.d

COPY ./tables_xxl_job.sql /docker-entrypoint-initdb.d