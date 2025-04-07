FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-demo-cloud

WORKDIR /jeecg-demo-cloud

EXPOSE 7002

ADD ./target/jeecg-demo-cloud-start-3.8.0.jar ./

CMD sleep 60;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-demo-cloud-start-3.8.0.jar
