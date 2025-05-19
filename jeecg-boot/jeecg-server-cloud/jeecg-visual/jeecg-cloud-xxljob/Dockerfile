FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-cloud-xxljob

WORKDIR /jeecg-cloud-xxljob

EXPOSE 9080

ADD ./target/jeecg-cloud-xxljob-3.8.0.jar ./

CMD java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-xxljob-3.8.0.jar

