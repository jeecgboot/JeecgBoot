FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-example

WORKDIR /jeecg-cloud-example

EXPOSE 7001

ADD ./target/jeecg-cloud-example-2.4.2.jar ./

CMD java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-example-2.4.2.jar

