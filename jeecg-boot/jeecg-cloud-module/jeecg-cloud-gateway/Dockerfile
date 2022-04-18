FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-gateway

WORKDIR /jeecg-cloud-gateway

EXPOSE 9999

ADD ./target/jeecg-cloud-gateway-3.2.0.jar ./

CMD sleep 50;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-gateway-3.2.0.jar