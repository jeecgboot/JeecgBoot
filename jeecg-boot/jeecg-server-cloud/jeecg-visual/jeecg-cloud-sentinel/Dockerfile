FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-sentinel

WORKDIR /jeecg-cloud-sentinel

EXPOSE 8848

ADD ./target/jeecg-cloud-sentinel-3.8.0.jar ./

CMD sleep 5;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-sentinel-3.8.0.jar
