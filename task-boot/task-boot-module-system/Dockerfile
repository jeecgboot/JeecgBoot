FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER taskos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /task-boot

WORKDIR /task-boot

EXPOSE 8080

ADD ./target/task-boot-module-system-2.3.0.jar ./

CMD sleep 60;java -Djava.security.egd=file:/dev/./urandom -jar task-boot-module-system-2.3.0.jar
