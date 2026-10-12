FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:17-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 解决中文乱码：基础镜像自带 C.utf8 locale，直接使用，无需 yum 安装 glibc-langpack-en（避免构建时网络慢）
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

RUN mkdir -p /jeecg-cloud-gateway

WORKDIR /jeecg-cloud-gateway

EXPOSE 9999

ADD ./target/jeecg-cloud-gateway-3.9.5.jar ./

# 去掉sleep 100，直接启动（docker-compose下由command覆盖为轮询nacos等system注册后再启动）
CMD exec java -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-gateway-3.9.5.jar