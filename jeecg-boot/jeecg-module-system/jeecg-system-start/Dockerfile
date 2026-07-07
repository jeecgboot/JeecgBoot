FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:17-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 解决linuxkit 精简镜像对 locale 裁剪导致中文乱码问题 java:17-anolis基于anolis（CentOS/RHEL 系）应当使用yum
RUN yum install -y --setopt=tsflags=nodocs \
        glibc-langpack-en \
        glibc-common \
    && yum clean all

ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

#RUN mkdir -p /jeecg-boot/config/jeecg/

WORKDIR /jeecg-boot

EXPOSE 8080

#ADD ./src/main/resources/jeecg ./config/jeecg
ADD ./target/jeecg-system-start-3.9.3.jar ./

RUN mkdir -p /jeecg-boot/config

# 【PR#9345】编码已通过JAVA_TOOL_OPTIONS设置，CMD改用&&和exec
CMD sleep 60 && exec java -Djava.security.egd=file:/dev/./urandom -jar jeecg-system-start-3.9.3.jar