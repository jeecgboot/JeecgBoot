FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-system-cloud

WORKDIR /jeecg-system-cloud

EXPOSE 7001

ADD ./target/jeecg-system-cloud-start-3.8.0.jar ./

CMD sleep 60;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-system-cloud-start-3.8.0.jar