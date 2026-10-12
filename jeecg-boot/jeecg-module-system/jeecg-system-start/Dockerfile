FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:17-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 解决中文乱码：基础镜像自带 C.utf8 locale，直接使用，无需 yum 安装 glibc-langpack-en（避免构建时网络慢）
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

#RUN mkdir -p /jeecg-boot/config/jeecg/

WORKDIR /jeecg-boot

EXPOSE 8080

#ADD ./src/main/resources/jeecg ./config/jeecg
ADD ./target/jeecg-system-start-3.9.5.jar ./

RUN mkdir -p /jeecg-boot/config

# 【PR#9345】编码已通过JAVA_TOOL_OPTIONS设置；去掉sleep 60，容器启动后立即拉起应用（依赖库就绪由docker-compose的healthcheck+depends_on: service_healthy保证）
CMD exec java -Djava.security.egd=file:/dev/./urandom -jar jeecg-system-start-3.9.5.jar