FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-xxljob

WORKDIR /jeecg-cloud-xxljob

EXPOSE 9080

ADD ./target/jeecg-cloud-xxljob-3.2.0.jar ./

CMD java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-xxljob-3.2.0.jar

