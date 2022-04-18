FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-system

WORKDIR /jeecg-cloud-system

EXPOSE 7001

ADD ./target/jeecg-cloud-system-start-3.2.0.jar ./

CMD sleep 10;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-system-start-3.2.0.jar