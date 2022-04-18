FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-nacos

WORKDIR /jeecg-cloud-nacos

EXPOSE 8848

ADD ./target/jeecg-cloud-nacos-3.2.0.jar ./

CMD sleep 5;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-nacos-3.2.0.jar