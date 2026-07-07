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

RUN mkdir -p /jeecg-cloud-xxljob

WORKDIR /jeecg-cloud-xxljob

EXPOSE 9080

ADD ./target/jeecg-cloud-xxljob-3.9.3.jar ./

CMD exec java -Djava.security.egd=file:/dev/./urandom -jar jeecg-cloud-xxljob-3.9.3.jar

