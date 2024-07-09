CREATE TABLE "NACOS"."CONFIG_INFO"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "DATA_ID" VARCHAR(255) NOT NULL,
 "GROUP_ID" VARCHAR(128) NULL,
 "CONTENT" CLOB NOT NULL,
 "MD5" VARCHAR(32) NULL,
 "GMT_CREATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "SRC_USER" TEXT NULL,
 "SRC_IP" VARCHAR(50) NULL,
 "APP_NAME" VARCHAR(128) NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL,
 "C_DESC" VARCHAR(256) NULL,
 "C_USE" VARCHAR(64) NULL,
 "EFFECT" VARCHAR(64) NULL,
 "TYPE" VARCHAR(64) NULL,
 "C_SCHEMA" TEXT NULL,
 "ENCRYPTED_DATA_KEY" TEXT NOT NULL
);
CREATE TABLE "NACOS"."CONFIG_INFO_AGGR"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "DATA_ID" VARCHAR(255) NOT NULL,
 "GROUP_ID" VARCHAR(128) NOT NULL,
 "DATUM_ID" VARCHAR(255) NOT NULL,
 "CONTENT" CLOB NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) NOT NULL,
 "APP_NAME" VARCHAR(128) NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL
);
CREATE TABLE "NACOS"."CONFIG_INFO_BETA"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "DATA_ID" VARCHAR(255) NOT NULL,
 "GROUP_ID" VARCHAR(128) NOT NULL,
 "APP_NAME" VARCHAR(128) NULL,
 "CONTENT" CLOB NOT NULL,
 "BETA_IPS" VARCHAR(1024) NULL,
 "MD5" VARCHAR(32) NULL,
 "GMT_CREATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "SRC_USER" TEXT NULL,
 "SRC_IP" VARCHAR(50) NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL,
 "ENCRYPTED_DATA_KEY" TEXT NOT NULL
);
CREATE TABLE "NACOS"."CONFIG_INFO_TAG"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "DATA_ID" VARCHAR(255) NOT NULL,
 "GROUP_ID" VARCHAR(128) NOT NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL,
 "TAG_ID" VARCHAR(128) NOT NULL,
 "APP_NAME" VARCHAR(128) NULL,
 "CONTENT" CLOB NOT NULL,
 "MD5" VARCHAR(32) NULL,
 "GMT_CREATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "SRC_USER" TEXT NULL,
 "SRC_IP" VARCHAR(50) NULL
);
CREATE TABLE "NACOS"."CONFIG_TAGS_RELATION"
(
 "ID" BIGINT NOT NULL,
 "TAG_NAME" VARCHAR(128) NOT NULL,
 "TAG_TYPE" VARCHAR(64) NULL,
 "DATA_ID" VARCHAR(255) NOT NULL,
 "GROUP_ID" VARCHAR(128) NOT NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL,
 "NID" BIGINT IDENTITY(1,1) NOT NULL
);
CREATE TABLE "NACOS"."GROUP_CAPACITY"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "GROUP_ID" VARCHAR(128) DEFAULT ''
 NOT NULL,
 "QUOTA" BIGINT DEFAULT 0
 NOT NULL,
 "USAGE" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_SIZE" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_AGGR_COUNT" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_AGGR_SIZE" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_HISTORY_COUNT" BIGINT DEFAULT 0
 NOT NULL,
 "GMT_CREATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL
);
CREATE TABLE "NACOS"."HIS_CONFIG_INFO"
(
 "ID" DECIMAL(20,0) NOT NULL,
 "NID" BIGINT IDENTITY(1,1) NOT NULL,
 "DATA_ID" VARCHAR(255) NOT NULL,
 "GROUP_ID" VARCHAR(128) NOT NULL,
 "APP_NAME" VARCHAR(128) NULL,
 "CONTENT" CLOB NOT NULL,
 "MD5" VARCHAR(32) NULL,
 "GMT_CREATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "SRC_USER" TEXT NULL,
 "SRC_IP" VARCHAR(50) NULL,
 "OP_TYPE" CHAR(10) NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL,
 "ENCRYPTED_DATA_KEY" TEXT NOT NULL
);
CREATE TABLE "NACOS"."PERMISSIONS"
(
 "ROLE" VARCHAR(50) NOT NULL,
 "RESOURCE" VARCHAR(255) NOT NULL,
 "ACTION" VARCHAR(8) NOT NULL
);
CREATE TABLE "NACOS"."ROLES"
(
 "USERNAME" VARCHAR(50) NOT NULL,
 "ROLE" VARCHAR(50) NOT NULL
);
CREATE TABLE "NACOS"."TENANT_CAPACITY"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NOT NULL,
 "QUOTA" BIGINT DEFAULT 0
 NOT NULL,
 "USAGE" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_SIZE" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_AGGR_COUNT" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_AGGR_SIZE" BIGINT DEFAULT 0
 NOT NULL,
 "MAX_HISTORY_COUNT" BIGINT DEFAULT 0
 NOT NULL,
 "GMT_CREATE" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL,
 "GMT_MODIFIED" TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP()
 NOT NULL
);
CREATE TABLE "NACOS"."TENANT_INFO"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "KP" VARCHAR(128) NOT NULL,
 "TENANT_ID" VARCHAR(128) DEFAULT ''
 NULL,
 "TENANT_NAME" VARCHAR(128) DEFAULT ''
 NULL,
 "TENANT_DESC" VARCHAR(256) NULL,
 "CREATE_SOURCE" VARCHAR(32) NULL,
 "GMT_CREATE" BIGINT NOT NULL,
 "GMT_MODIFIED" BIGINT NOT NULL
);
CREATE TABLE "NACOS"."USERS"
(
 "USERNAME" VARCHAR(50) NOT NULL,
 "PASSWORD" VARCHAR(500) NOT NULL,
 "ENABLED" TINYINT NOT NULL
);
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO" ON;
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(2,'jeecg-dev.yaml','DEFAULT_GROUP','spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)
        # 连接池的配置信息
        # 初始化大小，最小，最大
        initial-size: 5
        min-idle: 5
        maxActive: 20
        # 配置获取连接等待超时的时间
        maxWait: 60000
        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        # 配置一个连接在池中最小生存的时间，单位是毫秒
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        # 打开PSCache，并且指定每个连接上PSCache的大小
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，''wall''用于防火墙
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000

      datasource:
        master:
          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecgbootsy3_6?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
          # 多数据源配置
          #multi-datasource1:
          #url: jdbc:mysql://localhost:3306/jeecgboot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          #username: root
          #password: root
          #driver-class-name: com.mysql.cj.jdbc.Driver
  #redis 配置
  redis:
    database: 0
    host: jeecg-boot-redis
    lettuce:
      pool:
        max-active: 8   #最大连接数据库连接数,设 0 为没有限制
        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制
        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        min-idle: 0     #最小等待连接中的数量,设 0 为没有限制
      shutdown-timeout: 100ms
    password:
    port: 6379
    #mongodb
  data:
    mongodb:
      #有密码连接 账号密码包含特殊字符的需要用URLEncoder编码 库名必填
      #uri: mongodb://jeecgdev:jeecgd_89@111.225.222.176:27017/jeecgdev
      uri: mongodb://jeecg:123456@jeecg-boot-mongo:27017/jeecg?readPreference=secondaryPreferred&maxIdleTimeMS=60000&waitQueueTimeoutMS=2000&minPoolSize=5&maxPoolSize=100&maxLifeTimeMS=0&connectTimeoutMS=2000&socketTimeoutMS=2000
      #集群方式
      #uri: mongodb://192.168.0.221:27017,192.168.0.221:27018/imgdb
      print: true  #是否打印查询语句
      slowQuery: true  #是否记录慢查询到数据库中
      slowTime: 1000 #慢查询最短时间,默认为1000毫秒
  #rabbitmq配置
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        #消费者的最小数量
        concurrency: 1
        #消费者的最大数量
        max-concurrency: 1
        #是否支持重试
        retry:
          enabled: true
#jeecg专用配置
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  # 签名密钥串(前后端要一致，正式发布请自行修改)
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  # 本地：local\Minio：minio\阿里云：alioss
  uploadType: local
  # 前端访问地址
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path :
    #文件上传根目录 设置
    upload: /opt/upFiles
    #webapp文件路径
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/bigscreen/category/**,/bigscreen/visual/**,/bigscreen/map/**,/jmreport/bigscreen2/**
  #阿里云oss存储配置
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  # ElasticSearch 6设置
  elasticsearch:
    cluster-name: jeecg-ES
    cluster-nodes: 127.0.0.1:9200
    check-enabled: false
  # 表单设计器配置
  desform:
    # 主题颜色（仅支持 16进制颜色代码）
    theme-color: "#1890ff"
    # 文件、图片上传方式，可选项：qiniu（七牛云）、system（跟随系统配置）
    upload-type: system
    map:
      # 配置百度地图的AK，申请地址：https://lbs.baidu.com/apiconsole/key?application=key#/home
      baidu: ??
  # 在线预览文件服务器地址配置
  file-view-domain: 127.0.0.1:8012
  # minio文件上传
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  #大屏报表参数设置
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
  #Wps在线文档
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  #xxl-job配置
  xxljob:
    enabled: false
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  #分布式锁配置
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
#Mybatis输出sql日志
logging:
  level:
    org.jeecg.modules.system.mapper: info
#cas单点登录
cas:
  prefixUrl: http://localhost:8888/cas
#swagger
knife4j:
  enable: true
  #开启生产环境屏蔽
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314

#第三方登录
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: 1000002
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: ''demo::''
    timeout: 1h
#第三方APP对接
third-app:
  enabled: false
  type:
    #企业微信
    WECHAT_ENTERPRISE:
      enabled: false
      #CORP_ID
      client-id: ??
      #SECRET
      client-secret: ??
      agent-id: ??
      #自建应用秘钥（新版企微需要配置）
      # agent-app-secret: ??
    #钉钉
    DINGTALK:
      enabled: false
      # appKey
      client-id: ??
      # appSecret
      client-secret: ??
      agent-id: ??','350e31a280673586f2203956da576136',TO_DATE('2024-07-09 14:30:06','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:06','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(3,'jeecg.yaml','DEFAULT_GROUP','server:
  tomcat:
    max-swallow-size: -1
  error:
    include-exception: true
    include-stacktrace: ALWAYS
    include-message: ALWAYS
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*
management:
  health:
    mail:
      enabled: false
  endpoints:
    web:
      exposure:
        include: "*" #暴露所有节点
    health:
      sensitive: true #关闭过滤敏感信息
  endpoint:
    health:
      show-details: ALWAYS  #显示详细信息
flowable:
  # 自动部署验证设置:true-开启（默认）、false-关闭
  check-process-definitions: false
  #配置项可以设置流程引擎启动和关闭时数据库执行的策略
  database-schema-update: false
  #保存历史数据级别设置为full最高级别，便于历史数据的追溯
  history-level: full
  #开启定时任务
  async-executor-activate: true
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  mail:
    host: smtp.163.com
    username: jeecgos@163.com
    password: ??
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  ## quartz定时任务,采用数据库方式
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    #设置自动启动，默认为 true
    auto-startup: false
    #延迟1秒启动定时任务
    startup-delay: 1s
    #启动时更新己存在的Job
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            #class: org.quartz.impl.jdbcjobstore.JobStoreTX
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 12000
            clusterCheckinInterval: 15000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
  #json 时间戳统一转换
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  aop:
    proxy-target-class: true
  jpa:
    open-in-view: false
  #配置freemarker
  freemarker:
    # 设置模板后缀名
    suffix: .ftl
    # 设置文档类型
    content-type: text/html
    # 设置页面编码格式
    charset: UTF-8
    # 设置页面缓存
    cache: false
    prefer-file-system-access: false
    # 设置ftl文件路径
    template-loader-path:
      - classpath:/templates
    template_update_delay: 0
  # 设置静态文件路径，js,css等
  mvc:
    static-path-pattern: /**
    #Spring Boot 2.6+ 手动指定为ant-path-matcher
    pathmatch:
      matching-strategy: ant_path_matcher
  resource:
    static-locations: classpath:/static/,classpath:/public/
  autoconfigure:
    exclude:
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      - org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
#mybatis plus 设置
mybatis-plus:
  mapper-locations: classpath*:org/jeecg/**/xml/*Mapper.xml
  global-config:
    # 关闭MP3.0自带的banner
    banner: false
    db-config:
      #主键类型  0:"数据库ID自增",1:"该类型为未设置主键类型", 2:"用户输入ID",3:"全局唯一ID (数字类型唯一ID)", 4:"全局唯一ID UUID",5:"字符串全局唯一ID (idWorker 的字符串表示)";
      id-type: ASSIGN_ID
      # 默认数据库表下划线命名
      table-underline: true
  configuration:
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    # 返回类型为Map,显示null对应的字段
    call-setters-on-nulls: true','94755a848afefef22e34ff83668ec4f7',TO_DATE('2024-07-09 14:30:06','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:06','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(4,'jeecg-gateway-router.json','DEFAULT_GROUP','[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/eoa/**",
      "_genkey_2": "/joa/**",
      "_genkey_3": "/jmreport/**",
      "_genkey_4": "/bigscreen/**",
      "_genkey_5": "/desform/**",
      "_genkey_6": "/online/**",
      "_genkey_8": "/act/**",
      "_genkey_9": "/plug-in/**",
      "_genkey_10": "/generic/**",
      "_genkey_11": "/v1/**",
      "_genkey_12": "/desflow/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "jeecg-system-websocket",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/eoaSocket/**",
      "_genkey_2": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}]','c9eff51f264ebe266c07ad1c5b6778e2',TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','','',null,null,'json',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(5,'jeecg-gateway-dev.yaml','DEFAULT_GROUP','jeecg:
  route:
    config:
      #路由加载模式： database、nacos、yml
      data-type: database
      #Nacos模式，读取配置文件jeecg-gateway-router.json(固定)
      group: DEFAULT_GROUP
      data-id: jeecg-gateway-router
spring:
  #redis配置
  redis:
    database: 0
    host: jeecg-boot-redis
    port: 6379
    password:
#swagger
knife4j:
  #开启生产环境屏蔽
  production: false','8fea1277e460b477987521aecf432150',TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(6,'jeecg-sharding.yaml','DEFAULT_GROUP','spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        username: root
        password: root
        type: com.alibaba.druid.pool.DruidDataSource
    props:
      sql-show: true
    rules:
      sharding:
        binding-tables: sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
        tables:
          sys_log:
            actual-data-nodes: ds0.sys_log$->{0..1}
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','5d7aad99a23e68589e93facd1b221aea',TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','',null,null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(7,'jeecg-sharding-multi.yaml','DEFAULT_GROUP','spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
      ds1:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot2?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
    props:
      sql-show: true
    rules:
      replica-query:
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
            props:
              default: 0
        data-sources:
          prds:
            primary-data-source-name: ds0
            replica-data-source-names: ds1
            load-balancer-name: round_robin
      sharding:
        binding-tables:
          - sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{operate_type % 2}
        tables:
          sys_log:
            actual-data-nodes: ds$->{0..1}.sys_log$->{0..1}
            database-strategy:
              standard:
                sharding-column: operate_type
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','ef2f42fb2dda43cd0d4397a820f3144e',TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','',null,null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(14,'jeecg-dev.yaml','DEFAULT_GROUP','spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000
      datasource:
        master:
          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    redis:
      database: 0
      host: jeecg-boot-redis
      password:
      port: 6379
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 1
        max-concurrency: 1
        retry:
          enabled: true
  flyway:
    enabled: false
    encoding: UTF-8
    locations: classpath:flyway/sql/mysql
    sql-migration-prefix: V
    sql-migration-separator: __
    placeholder-prefix: ''#(''
    placeholder-suffix: )
    sql-migration-suffixes: .sql
    validate-on-migrate: true
    baseline-on-migrate: true
    clean-disabled: true
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys
  uploadType: local
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path:
    upload: /opt/upFiles
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  elasticsearch:
    cluster-name: jeecg-ES
    cluster-nodes: jeecg-boot-es:9200
    check-enabled: false
  file-view-domain: 127.0.0.1:8012
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  xxljob:
    enabled: false
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
logging:
  level:
    org.jeecg.modules.system.mapper : info
cas:
  prefixUrl: http://localhost:8888/cas
knife4j:
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: ??
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: ''demo::''
    timeout: 1h
third-app:
  enabled: false
  type:
    WECHAT_ENTERPRISE:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??
    DINGTALK:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??','91c29720dfb424916a769201a25200cf',TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','springboot3','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(15,'jeecg.yaml','DEFAULT_GROUP','server:
  undertow:
    # max-http-post-size: 10MB
    worker-threads: 16
    buffers:
      websocket: 8192
      io: 16384
  error:
    include-exception: true
    include-stacktrace: ALWAYS
    include-message: ALWAYS
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*
management:
  health:
    mail:
      enabled: false
  endpoints:
    web:
      exposure:
        include: "*"
    health:
      sensitive: true
  endpoint:
    health:
      show-details: ALWAYS
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  mail:
    host: smtp.163.com
    username: jeecgos@163.com
    password: ??
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    auto-startup: false
    startup-delay: 1s
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 12000
            clusterCheckinInterval: 15000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  aop:
    proxy-target-class: true
  activiti:
    check-process-definitions: false
    async-executor-activate: false
    job-executor-activate: false
  jpa:
    open-in-view: false
  freemarker:
    suffix: .ftl
    content-type: text/html
    charset: UTF-8
    cache: false
    prefer-file-system-access: false
    template-loader-path:
      - classpath:/templates
  mvc:
    static-path-pattern: /**
    pathmatch:
      matching-strategy: ant_path_matcher
  resource:
    static-locations: classpath:/static/,classpath:/public/
  autoconfigure:
    exclude:
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      - org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
mybatis-plus:
  mapper-locations: classpath*:org/jeecg/**/xml/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: ASSIGN_ID
      table-underline: true
  configuration:
    call-setters-on-nulls: true','ce1ca3b6f8431e884aed94ab29be43a9',TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','springboot3','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(16,'jeecg-gateway-router.json','DEFAULT_GROUP','[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/jmreport/**",
      "_genkey_3": "/online/**",
      "_genkey_4": "/generic/**",
      "_genkey_5": "/oauth2/**",
      "_genkey_6": "/drag/**",
      "_genkey_7": "/actuator/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "jeecg-system-websocket",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}]','9794beb09d30bc6b835f2ee870781587',TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','springboot3','',null,null,'json',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(17,'jeecg-sharding.yaml','DEFAULT_GROUP','spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        username: root
        password: root
        type: com.alibaba.druid.pool.DruidDataSource
    props:
      sql-show: true
    rules:
      sharding:
        binding-tables: sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
        tables:
          sys_log:
            actual-data-nodes: ds0.sys_log$->{0..1}
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','a93fa455c32cd37ca84631d2bbe13005',TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','springboot3','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(18,'jeecg-gateway-dev.yaml','DEFAULT_GROUP','jeecg:
  route:
    config:
      #type:database nacos yml
      data-type: database
      data-id: jeecg-gateway-router
spring:
  data:
    redis:
      database: 0
      host: jeecg-boot-redis
      port: 6379
      password:
knife4j:
  production: false','19d7cd93eeb85a582c8a6942d499c7f7',TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','springboot3','',null,null,'yaml',null,'');
INSERT INTO "NACOS"."CONFIG_INFO"("ID","DATA_ID","GROUP_ID","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","APP_NAME","TENANT_ID","C_DESC","C_USE","EFFECT","TYPE","C_SCHEMA","ENCRYPTED_DATA_KEY") VALUES(19,'jeecg-sharding-multi.yaml','DEFAULT_GROUP','spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
      ds1:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot2?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
    props:
      sql-show: true
    rules:
      replica-query:
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
            props:
              default: 0
        data-sources:
          prds:
            primary-data-source-name: ds0
            replica-data-source-names: ds1
            load-balancer-name: round_robin
      sharding:
        binding-tables:
          - sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{operate_type % 2}
        tables:
          sys_log:
            actual-data-nodes: ds$->{0..1}.sys_log$->{0..1}
            database-strategy:
              standard:
                sharding-column: operate_type
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','0fc2b030ca8c0008f148c84ecbd2a8c7',TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','','springboot3','',null,null,'yaml',null,'');

SET IDENTITY_INSERT "NACOS"."CONFIG_INFO" OFF;
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO_AGGR" ON;
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO_AGGR" OFF;
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO_BETA" ON;
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO_BETA" OFF;
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO_TAG" ON;
SET IDENTITY_INSERT "NACOS"."CONFIG_INFO_TAG" OFF;
SET IDENTITY_INSERT "NACOS"."CONFIG_TAGS_RELATION" ON;
SET IDENTITY_INSERT "NACOS"."CONFIG_TAGS_RELATION" OFF;
SET IDENTITY_INSERT "NACOS"."GROUP_CAPACITY" ON;
SET IDENTITY_INSERT "NACOS"."GROUP_CAPACITY" OFF;
SET IDENTITY_INSERT "NACOS"."HIS_CONFIG_INFO" ON;
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,1,'1','DEFAULT_GROUP','','1','c4ca4238a0b923820dcc509a6f75849b',TO_DATE('2024-07-09 14:24:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:24:06','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(1,2,'1','DEFAULT_GROUP','','1','c4ca4238a0b923820dcc509a6f75849b',TO_DATE('2024-07-09 14:24:07','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:24:08','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,3,'jeecg-dev.yaml','DEFAULT_GROUP','','spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)
        # 连接池的配置信息
        # 初始化大小，最小，最大
        initial-size: 5
        min-idle: 5
        maxActive: 20
        # 配置获取连接等待超时的时间
        maxWait: 60000
        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        timeBetweenEvictionRunsMillis: 60000
        # 配置一个连接在池中最小生存的时间，单位是毫秒
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        # 打开PSCache，并且指定每个连接上PSCache的大小
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，''wall''用于防火墙
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000

      datasource:
        master:
          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecgbootsy3_6?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
          # 多数据源配置
          #multi-datasource1:
          #url: jdbc:mysql://localhost:3306/jeecgboot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          #username: root
          #password: root
          #driver-class-name: com.mysql.cj.jdbc.Driver
  #redis 配置
  redis:
    database: 0
    host: jeecg-boot-redis
    lettuce:
      pool:
        max-active: 8   #最大连接数据库连接数,设 0 为没有限制
        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制
        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        min-idle: 0     #最小等待连接中的数量,设 0 为没有限制
      shutdown-timeout: 100ms
    password:
    port: 6379
    #mongodb
  data:
    mongodb:
      #有密码连接 账号密码包含特殊字符的需要用URLEncoder编码 库名必填
      #uri: mongodb://jeecgdev:jeecgd_89@111.225.222.176:27017/jeecgdev
      uri: mongodb://jeecg:123456@jeecg-boot-mongo:27017/jeecg?readPreference=secondaryPreferred&maxIdleTimeMS=60000&waitQueueTimeoutMS=2000&minPoolSize=5&maxPoolSize=100&maxLifeTimeMS=0&connectTimeoutMS=2000&socketTimeoutMS=2000
      #集群方式
      #uri: mongodb://192.168.0.221:27017,192.168.0.221:27018/imgdb
      print: true  #是否打印查询语句
      slowQuery: true  #是否记录慢查询到数据库中
      slowTime: 1000 #慢查询最短时间,默认为1000毫秒
  #rabbitmq配置
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        #消费者的最小数量
        concurrency: 1
        #消费者的最大数量
        max-concurrency: 1
        #是否支持重试
        retry:
          enabled: true
#jeecg专用配置
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  # 签名密钥串(前后端要一致，正式发布请自行修改)
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  # 本地：local\Minio：minio\阿里云：alioss
  uploadType: local
  # 前端访问地址
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path :
    #文件上传根目录 设置
    upload: /opt/upFiles
    #webapp文件路径
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/bigscreen/category/**,/bigscreen/visual/**,/bigscreen/map/**,/jmreport/bigscreen2/**
  #阿里云oss存储配置
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  # ElasticSearch 6设置
  elasticsearch:
    cluster-name: jeecg-ES
    cluster-nodes: 127.0.0.1:9200
    check-enabled: false
  # 表单设计器配置
  desform:
    # 主题颜色（仅支持 16进制颜色代码）
    theme-color: "#1890ff"
    # 文件、图片上传方式，可选项：qiniu（七牛云）、system（跟随系统配置）
    upload-type: system
    map:
      # 配置百度地图的AK，申请地址：https://lbs.baidu.com/apiconsole/key?application=key#/home
      baidu: ??
  # 在线预览文件服务器地址配置
  file-view-domain: 127.0.0.1:8012
  # minio文件上传
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  #大屏报表参数设置
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
  #Wps在线文档
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  #xxl-job配置
  xxljob:
    enabled: false
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  #分布式锁配置
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
#Mybatis输出sql日志
logging:
  level:
    org.jeecg.modules.system.mapper: info
#cas单点登录
cas:
  prefixUrl: http://localhost:8888/cas
#swagger
knife4j:
  enable: true
  #开启生产环境屏蔽
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314

#第三方登录
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: 1000002
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: ''demo::''
    timeout: 1h
#第三方APP对接
third-app:
  enabled: false
  type:
    #企业微信
    WECHAT_ENTERPRISE:
      enabled: false
      #CORP_ID
      client-id: ??
      #SECRET
      client-secret: ??
      agent-id: ??
      #自建应用秘钥（新版企微需要配置）
      # agent-app-secret: ??
    #钉钉
    DINGTALK:
      enabled: false
      # appKey
      client-id: ??
      # appSecret
      client-secret: ??
      agent-id: ??','350e31a280673586f2203956da576136',TO_DATE('2024-07-09 14:30:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:06','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,4,'jeecg.yaml','DEFAULT_GROUP','','server:
  tomcat:
    max-swallow-size: -1
  error:
    include-exception: true
    include-stacktrace: ALWAYS
    include-message: ALWAYS
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*
management:
  health:
    mail:
      enabled: false
  endpoints:
    web:
      exposure:
        include: "*" #暴露所有节点
    health:
      sensitive: true #关闭过滤敏感信息
  endpoint:
    health:
      show-details: ALWAYS  #显示详细信息
flowable:
  # 自动部署验证设置:true-开启（默认）、false-关闭
  check-process-definitions: false
  #配置项可以设置流程引擎启动和关闭时数据库执行的策略
  database-schema-update: false
  #保存历史数据级别设置为full最高级别，便于历史数据的追溯
  history-level: full
  #开启定时任务
  async-executor-activate: true
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  mail:
    host: smtp.163.com
    username: jeecgos@163.com
    password: ??
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  ## quartz定时任务,采用数据库方式
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    #设置自动启动，默认为 true
    auto-startup: false
    #延迟1秒启动定时任务
    startup-delay: 1s
    #启动时更新己存在的Job
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            #class: org.quartz.impl.jdbcjobstore.JobStoreTX
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 12000
            clusterCheckinInterval: 15000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
  #json 时间戳统一转换
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  aop:
    proxy-target-class: true
  jpa:
    open-in-view: false
  #配置freemarker
  freemarker:
    # 设置模板后缀名
    suffix: .ftl
    # 设置文档类型
    content-type: text/html
    # 设置页面编码格式
    charset: UTF-8
    # 设置页面缓存
    cache: false
    prefer-file-system-access: false
    # 设置ftl文件路径
    template-loader-path:
      - classpath:/templates
    template_update_delay: 0
  # 设置静态文件路径，js,css等
  mvc:
    static-path-pattern: /**
    #Spring Boot 2.6+ 手动指定为ant-path-matcher
    pathmatch:
      matching-strategy: ant_path_matcher
  resource:
    static-locations: classpath:/static/,classpath:/public/
  autoconfigure:
    exclude:
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      - org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
#mybatis plus 设置
mybatis-plus:
  mapper-locations: classpath*:org/jeecg/**/xml/*Mapper.xml
  global-config:
    # 关闭MP3.0自带的banner
    banner: false
    db-config:
      #主键类型  0:"数据库ID自增",1:"该类型为未设置主键类型", 2:"用户输入ID",3:"全局唯一ID (数字类型唯一ID)", 4:"全局唯一ID UUID",5:"字符串全局唯一ID (idWorker 的字符串表示)";
      id-type: ASSIGN_ID
      # 默认数据库表下划线命名
      table-underline: true
  configuration:
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    # 返回类型为Map,显示null对应的字段
    call-setters-on-nulls: true','94755a848afefef22e34ff83668ec4f7',TO_DATE('2024-07-09 14:30:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,5,'jeecg-gateway-router.json','DEFAULT_GROUP','','[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/eoa/**",
      "_genkey_2": "/joa/**",
      "_genkey_3": "/jmreport/**",
      "_genkey_4": "/bigscreen/**",
      "_genkey_5": "/desform/**",
      "_genkey_6": "/online/**",
      "_genkey_8": "/act/**",
      "_genkey_9": "/plug-in/**",
      "_genkey_10": "/generic/**",
      "_genkey_11": "/v1/**",
      "_genkey_12": "/desflow/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "jeecg-system-websocket",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/eoaSocket/**",
      "_genkey_2": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}]','c9eff51f264ebe266c07ad1c5b6778e2',TO_DATE('2024-07-09 14:30:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,6,'jeecg-gateway-dev.yaml','DEFAULT_GROUP','','jeecg:
  route:
    config:
      #路由加载模式： database、nacos、yml
      data-type: database
      #Nacos模式，读取配置文件jeecg-gateway-router.json(固定)
      group: DEFAULT_GROUP
      data-id: jeecg-gateway-router
spring:
  #redis配置
  redis:
    database: 0
    host: jeecg-boot-redis
    port: 6379
    password:
#swagger
knife4j:
  #开启生产环境屏蔽
  production: false','8fea1277e460b477987521aecf432150',TO_DATE('2024-07-09 14:30:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,7,'jeecg-sharding.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        username: root
        password: root
        type: com.alibaba.druid.pool.DruidDataSource
    props:
      sql-show: true
    rules:
      sharding:
        binding-tables: sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
        tables:
          sys_log:
            actual-data-nodes: ds0.sys_log$->{0..1}
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','5d7aad99a23e68589e93facd1b221aea',TO_DATE('2024-07-09 14:30:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,8,'jeecg-sharding-multi.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
      ds1:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot2?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
    props:
      sql-show: true
    rules:
      replica-query:
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
            props:
              default: 0
        data-sources:
          prds:
            primary-data-source-name: ds0
            replica-data-source-names: ds1
            load-balancer-name: round_robin
      sharding:
        binding-tables:
          - sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{operate_type % 2}
        tables:
          sys_log:
            actual-data-nodes: ds$->{0..1}.sys_log$->{0..1}
            database-strategy:
              standard:
                sharding-column: operate_type
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','ef2f42fb2dda43cd0d4397a820f3144e',TO_DATE('2024-07-09 14:30:05','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:07','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,9,'jeecg-dev.yaml','DEFAULT_GROUP','','spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000
      datasource:
        master:
          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    database: 0
    host: jeecg-boot-redis
    password:
    port: 6379
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 1
        max-concurrency: 1
        retry:
          enabled: true
  flyway:
    enabled: false
    encoding: UTF-8
    locations: classpath:flyway/sql/mysql
    sql-migration-prefix: V
    sql-migration-separator: __
    placeholder-prefix: ''#(''
    placeholder-suffix: )
    sql-migration-suffixes: .sql
    validate-on-migrate: true
    baseline-on-migrate: true
    clean-disabled: true
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys,/sys/sendChangePwdSms,/sys/user/sendChangePhoneSms,/sys/sms,/desform/api/sendVerifyCode
  uploadType: local
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path:
    upload: /opt/upFiles
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  elasticsearch:
    cluster-name: jeecg-ES
    cluster-nodes: jeecg-boot-es:9200
    check-enabled: false
  file-view-domain: 127.0.0.1:8012
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  xxljob:
    enabled: true
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
logging:
  level:
    org.jeecg.modules.system.mapper : info
cas:
  prefixUrl: http://localhost:8888/cas
knife4j:
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: ??
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: ''demo::''
    timeout: 1h
third-app:
  enabled: false
  type:
    WECHAT_ENTERPRISE:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??
    DINGTALK:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??','822f70f7a278a503a02568186582ceaa',TO_DATE('2024-07-09 14:30:19','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:20','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,10,'jeecg.yaml','DEFAULT_GROUP','','server:
  tomcat:
    max-swallow-size: -1
  error:
    include-exception: true
    include-stacktrace: ALWAYS
    include-message: ALWAYS
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*
management:
  health:
    mail:
      enabled: false
  endpoints:
    web:
      exposure:
        include: "*"
    health:
      sensitive: true
  endpoint:
    health:
      show-details: ALWAYS
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  mail:
    host: smtp.163.com
    username: jeecgos@163.com
    password: ??
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    auto-startup: false
    startup-delay: 1s
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 12000
            clusterCheckinInterval: 15000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  aop:
    proxy-target-class: true
  activiti:
    check-process-definitions: false
    async-executor-activate: false
    job-executor-activate: false
  jpa:
    open-in-view: false
  freemarker:
    suffix: .ftl
    content-type: text/html
    charset: UTF-8
    cache: false
    prefer-file-system-access: false
    template-loader-path:
      - classpath:/templates
  mvc:
    static-path-pattern: /**
    pathmatch:
      matching-strategy: ant_path_matcher
  resource:
    static-locations: classpath:/static/,classpath:/public/
  autoconfigure:
    exclude:
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      - org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
mybatis-plus:
  mapper-locations: classpath*:org/jeecg/**/xml/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: ASSIGN_ID
      table-underline: true
  configuration:
    call-setters-on-nulls: true','94dbdad61f7e2e3ace5a4fc07bb8c2a2',TO_DATE('2024-07-09 14:30:19','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:20','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,11,'jeecg-gateway-router.json','DEFAULT_GROUP','','[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/jmreport/**",
      "_genkey_3": "/online/**",
      "_genkey_4": "/generic/**",
      "_genkey_5": "/drag/**",
      "_genkey_6": "/actuator/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "jeecg-system-websocket",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}]','708c0948118bdb96bdfaa87200a14432',TO_DATE('2024-07-09 14:30:19','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:20','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,12,'jeecg-sharding.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        username: root
        password: root
        type: com.alibaba.druid.pool.DruidDataSource
    props:
      sql-show: true
    rules:
      sharding:
        binding-tables: sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
        tables:
          sys_log:
            actual-data-nodes: ds0.sys_log$->{0..1}
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','a93fa455c32cd37ca84631d2bbe13005',TO_DATE('2024-07-09 14:30:19','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:20','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,13,'jeecg-gateway-dev.yaml','DEFAULT_GROUP','','jeecg:
  route:
    config:
      #type:database nacos yml
      data-type: database
      data-id: jeecg-gateway-router
spring:
  redis:
    database: 0
    host: jeecg-boot-redis
    port: 6379
    password:
knife4j:
  production: false','98e211c54b43a73f7189d92f1c77f815',TO_DATE('2024-07-09 14:30:19','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:20','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,14,'jeecg-sharding-multi.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
      ds1:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot2?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
    props:
      sql-show: true
    rules:
      replica-query:
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
            props:
              default: 0
        data-sources:
          prds:
            primary-data-source-name: ds0
            replica-data-source-names: ds1
            load-balancer-name: round_robin
      sharding:
        binding-tables:
          - sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{operate_type % 2}
        tables:
          sys_log:
            actual-data-nodes: ds$->{0..1}.sys_log$->{0..1}
            database-strategy:
              standard:
                sharding-column: operate_type
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','0fc2b030ca8c0008f148c84ecbd2a8c7',TO_DATE('2024-07-09 14:30:19','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:30:20','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(8,15,'jeecg-dev.yaml','DEFAULT_GROUP','','spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000
      datasource:
        master:
          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    database: 0
    host: jeecg-boot-redis
    password:
    port: 6379
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 1
        max-concurrency: 1
        retry:
          enabled: true
  flyway:
    enabled: false
    encoding: UTF-8
    locations: classpath:flyway/sql/mysql
    sql-migration-prefix: V
    sql-migration-separator: __
    placeholder-prefix: ''#(''
    placeholder-suffix: )
    sql-migration-suffixes: .sql
    validate-on-migrate: true
    baseline-on-migrate: true
    clean-disabled: true
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys,/sys/sendChangePwdSms,/sys/user/sendChangePhoneSms,/sys/sms,/desform/api/sendVerifyCode
  uploadType: local
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path:
    upload: /opt/upFiles
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  elasticsearch:
    cluster-name: jeecg-ES
    cluster-nodes: jeecg-boot-es:9200
    check-enabled: false
  file-view-domain: 127.0.0.1:8012
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  xxljob:
    enabled: true
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
logging:
  level:
    org.jeecg.modules.system.mapper : info
cas:
  prefixUrl: http://localhost:8888/cas
knife4j:
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: ??
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: ''demo::''
    timeout: 1h
third-app:
  enabled: false
  type:
    WECHAT_ENTERPRISE:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??
    DINGTALK:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??','822f70f7a278a503a02568186582ceaa',TO_DATE('2024-07-09 14:34:25','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:27','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(9,16,'jeecg.yaml','DEFAULT_GROUP','','server:
  tomcat:
    max-swallow-size: -1
  error:
    include-exception: true
    include-stacktrace: ALWAYS
    include-message: ALWAYS
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*
management:
  health:
    mail:
      enabled: false
  endpoints:
    web:
      exposure:
        include: "*"
    health:
      sensitive: true
  endpoint:
    health:
      show-details: ALWAYS
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  mail:
    host: smtp.163.com
    username: jeecgos@163.com
    password: ??
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    auto-startup: false
    startup-delay: 1s
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 12000
            clusterCheckinInterval: 15000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  aop:
    proxy-target-class: true
  activiti:
    check-process-definitions: false
    async-executor-activate: false
    job-executor-activate: false
  jpa:
    open-in-view: false
  freemarker:
    suffix: .ftl
    content-type: text/html
    charset: UTF-8
    cache: false
    prefer-file-system-access: false
    template-loader-path:
      - classpath:/templates
  mvc:
    static-path-pattern: /**
    pathmatch:
      matching-strategy: ant_path_matcher
  resource:
    static-locations: classpath:/static/,classpath:/public/
  autoconfigure:
    exclude:
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      - org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
mybatis-plus:
  mapper-locations: classpath*:org/jeecg/**/xml/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: ASSIGN_ID
      table-underline: true
  configuration:
    call-setters-on-nulls: true','94dbdad61f7e2e3ace5a4fc07bb8c2a2',TO_DATE('2024-07-09 14:34:25','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:27','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(10,17,'jeecg-gateway-router.json','DEFAULT_GROUP','','[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/jmreport/**",
      "_genkey_3": "/online/**",
      "_genkey_4": "/generic/**",
      "_genkey_5": "/drag/**",
      "_genkey_6": "/actuator/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "jeecg-system-websocket",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}]','708c0948118bdb96bdfaa87200a14432',TO_DATE('2024-07-09 14:34:25','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:27','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(11,18,'jeecg-sharding.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        username: root
        password: root
        type: com.alibaba.druid.pool.DruidDataSource
    props:
      sql-show: true
    rules:
      sharding:
        binding-tables: sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
        tables:
          sys_log:
            actual-data-nodes: ds0.sys_log$->{0..1}
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','a93fa455c32cd37ca84631d2bbe13005',TO_DATE('2024-07-09 14:34:25','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:27','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(12,19,'jeecg-gateway-dev.yaml','DEFAULT_GROUP','','jeecg:
  route:
    config:
      #type:database nacos yml
      data-type: database
      data-id: jeecg-gateway-router
spring:
  redis:
    database: 0
    host: jeecg-boot-redis
    port: 6379
    password:
knife4j:
  production: false','98e211c54b43a73f7189d92f1c77f815',TO_DATE('2024-07-09 14:34:25','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:27','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(13,20,'jeecg-sharding-multi.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
      ds1:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot2?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
    props:
      sql-show: true
    rules:
      replica-query:
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
            props:
              default: 0
        data-sources:
          prds:
            primary-data-source-name: ds0
            replica-data-source-names: ds1
            load-balancer-name: round_robin
      sharding:
        binding-tables:
          - sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{operate_type % 2}
        tables:
          sys_log:
            actual-data-nodes: ds$->{0..1}.sys_log$->{0..1}
            database-strategy:
              standard:
                sharding-column: operate_type
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','0fc2b030ca8c0008f148c84ecbd2a8c7',TO_DATE('2024-07-09 14:34:25','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:27','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','D','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,21,'jeecg-dev.yaml','DEFAULT_GROUP','','spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: admin
        loginPassword: 123456
        allow:
      web-stat-filter:
        enabled: true
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall,slf4j
        wall:
          selectWhereAlwayTrueCheck: false
        stat:
          merge-sql: true
          slow-sql-millis: 5000
      datasource:
        master:
          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
          driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    redis:
      database: 0
      host: jeecg-boot-redis
      password:
      port: 6379
  rabbitmq:
    host: jeecg-boot-rabbitmq
    username: guest
    password: guest
    port: 5672
    publisher-confirms: true
    publisher-returns: true
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        concurrency: 1
        max-concurrency: 1
        retry:
          enabled: true
  flyway:
    enabled: false
    encoding: UTF-8
    locations: classpath:flyway/sql/mysql
    sql-migration-prefix: V
    sql-migration-separator: __
    placeholder-prefix: ''#(''
    placeholder-suffix: )
    sql-migration-suffixes: .sql
    validate-on-migrate: true
    baseline-on-migrate: true
    clean-disabled: true
minidao:
  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*
jeecg:
  firewall:
    dataSourceSafe: false
    lowCodeMode: dev
  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a
  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys
  uploadType: local
  domainUrl:
    pc: http://localhost:3100
    app: http://localhost:8051
  path:
    upload: /opt/upFiles
    webapp: /opt/webapp
  shiro:
    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    accessKey: ??
    secretKey: ??
    bucketName: jeecgdev
    staticDomain: ??
  elasticsearch:
    cluster-name: jeecg-ES
    cluster-nodes: jeecg-boot-es:9200
    check-enabled: false
  file-view-domain: 127.0.0.1:8012
  minio:
    minio_url: http://minio.jeecg.com
    minio_name: ??
    minio_pass: ??
    bucketName: otatest
  jmreport:
    saasMode:
    firewall:
      dataSourceSafe: false
      lowCodeMode: dev
  wps:
    domain: https://wwo.wps.cn/office/
    appid: ??
    appsecret: ??
  xxljob:
    enabled: false
    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin
    appname: ${spring.application.name}
    accessToken: ''''
    logPath: logs/jeecg/job/jobhandler/
    logRetentionDays: 30
  redisson:
    address: jeecg-boot-redis:6379
    password:
    type: STANDALONE
    enabled: true
  ai-chat:
    enabled: false
    apiKey: "？？？？"
    apiHost: "https://api.openai.com"
    timeout: 60
logging:
  level:
    org.jeecg.modules.system.mapper : info
cas:
  prefixUrl: http://localhost:8888/cas
knife4j:
  production: false
  basic:
    enable: false
    username: jeecg
    password: jeecg1314
justauth:
  enabled: true
  type:
    GITHUB:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback
    WECHAT_ENTERPRISE:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback
      agent-id: ??
    DINGTALK:
      client-id: ??
      client-secret: ??
      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback
  cache:
    type: default
    prefix: ''demo::''
    timeout: 1h
third-app:
  enabled: false
  type:
    WECHAT_ENTERPRISE:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??
    DINGTALK:
      enabled: false
      client-id: ??
      client-secret: ??
      agent-id: ??','91c29720dfb424916a769201a25200cf',TO_DATE('2024-07-09 14:34:32','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,22,'jeecg.yaml','DEFAULT_GROUP','','server:
  undertow:
    # max-http-post-size: 10MB
    worker-threads: 16
    buffers:
      websocket: 8192
      io: 16384
  error:
    include-exception: true
    include-stacktrace: ALWAYS
    include-message: ALWAYS
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*
management:
  health:
    mail:
      enabled: false
  endpoints:
    web:
      exposure:
        include: "*"
    health:
      sensitive: true
  endpoint:
    health:
      show-details: ALWAYS
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  mail:
    host: smtp.163.com
    username: jeecgos@163.com
    password: ??
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  quartz:
    job-store-type: jdbc
    initialize-schema: embedded
    auto-startup: false
    startup-delay: 1s
    overwrite-existing-jobs: true
    properties:
      org:
        quartz:
          scheduler:
            instanceName: MyScheduler
            instanceId: AUTO
          jobStore:
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            misfireThreshold: 12000
            clusterCheckinInterval: 15000
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  aop:
    proxy-target-class: true
  activiti:
    check-process-definitions: false
    async-executor-activate: false
    job-executor-activate: false
  jpa:
    open-in-view: false
  freemarker:
    suffix: .ftl
    content-type: text/html
    charset: UTF-8
    cache: false
    prefer-file-system-access: false
    template-loader-path:
      - classpath:/templates
  mvc:
    static-path-pattern: /**
    pathmatch:
      matching-strategy: ant_path_matcher
  resource:
    static-locations: classpath:/static/,classpath:/public/
  autoconfigure:
    exclude:
      - com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
      - org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
mybatis-plus:
  mapper-locations: classpath*:org/jeecg/**/xml/*Mapper.xml
  global-config:
    banner: false
    db-config:
      id-type: ASSIGN_ID
      table-underline: true
  configuration:
    call-setters-on-nulls: true','ce1ca3b6f8431e884aed94ab29be43a9',TO_DATE('2024-07-09 14:34:32','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,23,'jeecg-gateway-router.json','DEFAULT_GROUP','','[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/jmreport/**",
      "_genkey_3": "/online/**",
      "_genkey_4": "/generic/**",
      "_genkey_5": "/oauth2/**",
      "_genkey_6": "/drag/**",
      "_genkey_7": "/actuator/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "jeecg-system-websocket",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}]','9794beb09d30bc6b835f2ee870781587',TO_DATE('2024-07-09 14:34:32','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,24,'jeecg-sharding.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        username: root
        password: root
        type: com.alibaba.druid.pool.DruidDataSource
    props:
      sql-show: true
    rules:
      sharding:
        binding-tables: sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
        tables:
          sys_log:
            actual-data-nodes: ds0.sys_log$->{0..1}
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','a93fa455c32cd37ca84631d2bbe13005',TO_DATE('2024-07-09 14:34:32','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,25,'jeecg-gateway-dev.yaml','DEFAULT_GROUP','','jeecg:
  route:
    config:
      #type:database nacos yml
      data-type: database
      data-id: jeecg-gateway-router
spring:
  data:
    redis:
      database: 0
      host: jeecg-boot-redis
      port: 6379
      password:
knife4j:
  production: false','19d7cd93eeb85a582c8a6942d499c7f7',TO_DATE('2024-07-09 14:34:32','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');
INSERT INTO "NACOS"."HIS_CONFIG_INFO"("ID","NID","DATA_ID","GROUP_ID","APP_NAME","CONTENT","MD5","GMT_CREATE","GMT_MODIFIED","SRC_USER","SRC_IP","OP_TYPE","TENANT_ID","ENCRYPTED_DATA_KEY") VALUES(0,26,'jeecg-sharding-multi.yaml','DEFAULT_GROUP','','spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
      ds1:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot2?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
        type: com.alibaba.druid.pool.DruidDataSource
        username: root
        password: root
    props:
      sql-show: true
    rules:
      replica-query:
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
            props:
              default: 0
        data-sources:
          prds:
            primary-data-source-name: ds0
            replica-data-source-names: ds1
            load-balancer-name: round_robin
      sharding:
        binding-tables:
          - sys_log
        key-generators:
          snowflake:
            type: SNOWFLAKE
            props:
              worker-id: 123
        sharding-algorithms:
          table-classbased:
            props:
              strategy: standard
              algorithmClassName: org.jeecg.modules.test.sharding.algorithm.StandardModTableShardAlgorithm
            type: CLASS_BASED
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{operate_type % 2}
        tables:
          sys_log:
            actual-data-nodes: ds$->{0..1}.sys_log$->{0..1}
            database-strategy:
              standard:
                sharding-column: operate_type
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-algorithm-name: table-classbased
                sharding-column: log_type','0fc2b030ca8c0008f148c84ecbd2a8c7',TO_DATE('2024-07-09 14:34:32','YYYY-MM-DD HH24:MI:SS.FF'),TO_DATE('2024-07-09 14:34:33','YYYY-MM-DD HH24:MI:SS.FF'),null,'192.168.1.11','I','springboot3','');

SET IDENTITY_INSERT "NACOS"."HIS_CONFIG_INFO" OFF;
INSERT INTO "NACOS"."ROLES"("USERNAME","ROLE") VALUES('nacos','ROLE_ADMIN');

SET IDENTITY_INSERT "NACOS"."TENANT_CAPACITY" ON;
SET IDENTITY_INSERT "NACOS"."TENANT_CAPACITY" OFF;
SET IDENTITY_INSERT "NACOS"."TENANT_INFO" ON;
INSERT INTO "NACOS"."TENANT_INFO"("ID","KP","TENANT_ID","TENANT_NAME","TENANT_DESC","CREATE_SOURCE","GMT_CREATE","GMT_MODIFIED") VALUES(1,'1','springboot3','springboot3','springboot3版本配置文件，与springboot2有很大区别','nacos',1720506551826,1720506551826);

SET IDENTITY_INSERT "NACOS"."TENANT_INFO" OFF;
INSERT INTO "NACOS"."USERS"("USERNAME","PASSWORD","ENABLED") VALUES('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);

ALTER TABLE "NACOS"."CONFIG_INFO" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."CONFIG_INFO_AGGR" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."CONFIG_INFO_BETA" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."CONFIG_INFO_TAG" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."CONFIG_TAGS_RELATION" ADD CONSTRAINT  PRIMARY KEY("NID") ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."HIS_CONFIG_INFO" ADD CONSTRAINT  PRIMARY KEY("NID") ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."TENANT_INFO" ADD CONSTRAINT  PRIMARY KEY("ID") ;

ALTER TABLE "NACOS"."USERS" ADD CONSTRAINT  PRIMARY KEY("USERNAME") ;

ALTER TABLE "NACOS"."HIS_CONFIG_INFO" ADD CHECK("ID" >= 0) ENABLE ;

ALTER TABLE "NACOS"."CONFIG_INFO" ADD CONSTRAINT "UK_CONFIGINFO_DATAGROUPTENANT" UNIQUE("DATA_ID","GROUP_ID","TENANT_ID") ;

ALTER TABLE "NACOS"."CONFIG_INFO_AGGR" ADD CONSTRAINT "UK_CONFIGINFOAGGR_DATAGROUPTENANTDATUM" UNIQUE("DATA_ID","GROUP_ID","TENANT_ID","DATUM_ID") ;

ALTER TABLE "NACOS"."CONFIG_INFO_BETA" ADD CONSTRAINT "UK_CONFIGINFOBETA_DATAGROUPTENANT" UNIQUE("DATA_ID","GROUP_ID","TENANT_ID") ;

ALTER TABLE "NACOS"."CONFIG_INFO_TAG" ADD CONSTRAINT "UK_CONFIGINFOTAG_DATAGROUPTENANTTAG" UNIQUE("DATA_ID","GROUP_ID","TENANT_ID","TAG_ID") ;

ALTER TABLE "NACOS"."CONFIG_TAGS_RELATION" ADD CONSTRAINT "UK_CONFIGTAGRELATION_CONFIGIDTAG" UNIQUE("ID","TAG_NAME","TAG_TYPE") ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CONSTRAINT "UK_GROUP_ID" UNIQUE("GROUP_ID") ;

ALTER TABLE "NACOS"."PERMISSIONS" ADD CONSTRAINT "UK_ROLE_PERMISSION" UNIQUE("ROLE","RESOURCE","ACTION") ;

ALTER TABLE "NACOS"."ROLES" ADD CONSTRAINT "IDX_USER_ROLE" UNIQUE("USERNAME","ROLE") ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CONSTRAINT "UK_TENANT_ID" UNIQUE("TENANT_ID") ;

ALTER TABLE "NACOS"."TENANT_INFO" ADD CONSTRAINT "UK_TENANT_INFO_KPTENANTID" UNIQUE("KP","TENANT_ID") ;

COMMENT ON TABLE "NACOS"."CONFIG_INFO" IS 'config_info';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."ID" IS 'id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."DATA_ID" IS 'data_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."CONTENT" IS 'content';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."MD5" IS 'md5';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."GMT_CREATE" IS '创建时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."GMT_MODIFIED" IS '修改时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."SRC_USER" IS 'source user';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."SRC_IP" IS 'source ip';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."TENANT_ID" IS '租户字段';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO"."ENCRYPTED_DATA_KEY" IS '密钥';

COMMENT ON TABLE "NACOS"."CONFIG_INFO_AGGR" IS '增加租户字段';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."ID" IS 'id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."DATA_ID" IS 'data_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."GROUP_ID" IS 'group_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."DATUM_ID" IS 'datum_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."CONTENT" IS '内容';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."GMT_MODIFIED" IS '修改时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_AGGR"."TENANT_ID" IS '租户字段';

COMMENT ON TABLE "NACOS"."CONFIG_INFO_BETA" IS 'config_info_beta';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."ID" IS 'id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."DATA_ID" IS 'data_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."GROUP_ID" IS 'group_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."APP_NAME" IS 'app_name';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."CONTENT" IS 'content';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."BETA_IPS" IS 'betaIps';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."MD5" IS 'md5';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."GMT_CREATE" IS '创建时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."GMT_MODIFIED" IS '修改时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."SRC_USER" IS 'source user';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."SRC_IP" IS 'source ip';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."TENANT_ID" IS '租户字段';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_BETA"."ENCRYPTED_DATA_KEY" IS '密钥';

COMMENT ON TABLE "NACOS"."CONFIG_INFO_TAG" IS 'config_info_tag';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."ID" IS 'id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."DATA_ID" IS 'data_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."GROUP_ID" IS 'group_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."TENANT_ID" IS 'tenant_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."TAG_ID" IS 'tag_id';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."APP_NAME" IS 'app_name';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."CONTENT" IS 'content';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."MD5" IS 'md5';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."GMT_CREATE" IS '创建时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."GMT_MODIFIED" IS '修改时间';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."SRC_USER" IS 'source user';

COMMENT ON COLUMN "NACOS"."CONFIG_INFO_TAG"."SRC_IP" IS 'source ip';

COMMENT ON TABLE "NACOS"."CONFIG_TAGS_RELATION" IS 'config_tag_relation';

COMMENT ON COLUMN "NACOS"."CONFIG_TAGS_RELATION"."ID" IS 'id';

COMMENT ON COLUMN "NACOS"."CONFIG_TAGS_RELATION"."TAG_NAME" IS 'tag_name';

COMMENT ON COLUMN "NACOS"."CONFIG_TAGS_RELATION"."TAG_TYPE" IS 'tag_type';

COMMENT ON COLUMN "NACOS"."CONFIG_TAGS_RELATION"."DATA_ID" IS 'data_id';

COMMENT ON COLUMN "NACOS"."CONFIG_TAGS_RELATION"."GROUP_ID" IS 'group_id';

COMMENT ON COLUMN "NACOS"."CONFIG_TAGS_RELATION"."TENANT_ID" IS 'tenant_id';

CREATE INDEX "IDX_DID"
ON "NACOS"."HIS_CONFIG_INFO"("DATA_ID");

CREATE INDEX "IDX_GMT_CREATE"
ON "NACOS"."HIS_CONFIG_INFO"("GMT_CREATE");

CREATE INDEX "IDX_GMT_MODIFIED"
ON "NACOS"."HIS_CONFIG_INFO"("GMT_MODIFIED");

CREATE INDEX "IDX_TENANT_ID"
ON "NACOS"."TENANT_INFO"("TENANT_ID");

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CHECK("USAGE" >= 0) ENABLE ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CHECK("MAX_SIZE" >= 0) ENABLE ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CHECK("MAX_AGGR_COUNT" >= 0) ENABLE ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CHECK("MAX_AGGR_SIZE" >= 0) ENABLE ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CHECK("MAX_HISTORY_COUNT" >= 0) ENABLE ;

ALTER TABLE "NACOS"."GROUP_CAPACITY" ADD CHECK("QUOTA" >= 0) ENABLE ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CHECK("QUOTA" >= 0) ENABLE ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CHECK("USAGE" >= 0) ENABLE ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CHECK("MAX_SIZE" >= 0) ENABLE ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CHECK("MAX_AGGR_COUNT" >= 0) ENABLE ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CHECK("MAX_AGGR_SIZE" >= 0) ENABLE ;

ALTER TABLE "NACOS"."TENANT_CAPACITY" ADD CHECK("MAX_HISTORY_COUNT" >= 0) ENABLE ;

COMMENT ON TABLE "NACOS"."GROUP_CAPACITY" IS '集群、各Group容量信息表';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."ID" IS '主键ID';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."GROUP_ID" IS 'Group ID，空字符表示整个集群';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."QUOTA" IS '配额，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."USAGE" IS '使用量';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."MAX_SIZE" IS '单个配置大小上限，单位为字节，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."MAX_AGGR_COUNT" IS '聚合子配置最大个数，，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."MAX_AGGR_SIZE" IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."MAX_HISTORY_COUNT" IS '最大变更历史数量';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."GMT_CREATE" IS '创建时间';

COMMENT ON COLUMN "NACOS"."GROUP_CAPACITY"."GMT_MODIFIED" IS '修改时间';

COMMENT ON TABLE "NACOS"."HIS_CONFIG_INFO" IS '多租户改造';

COMMENT ON COLUMN "NACOS"."HIS_CONFIG_INFO"."APP_NAME" IS 'app_name';

COMMENT ON COLUMN "NACOS"."HIS_CONFIG_INFO"."TENANT_ID" IS '租户字段';

COMMENT ON COLUMN "NACOS"."HIS_CONFIG_INFO"."ENCRYPTED_DATA_KEY" IS '密钥';

COMMENT ON TABLE "NACOS"."TENANT_CAPACITY" IS '租户容量信息表';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."ID" IS '主键ID';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."TENANT_ID" IS 'Tenant ID';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."QUOTA" IS '配额，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."USAGE" IS '使用量';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."MAX_SIZE" IS '单个配置大小上限，单位为字节，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."MAX_AGGR_COUNT" IS '聚合子配置最大个数';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."MAX_AGGR_SIZE" IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."MAX_HISTORY_COUNT" IS '最大变更历史数量';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."GMT_CREATE" IS '创建时间';

COMMENT ON COLUMN "NACOS"."TENANT_CAPACITY"."GMT_MODIFIED" IS '修改时间';

COMMENT ON TABLE "NACOS"."TENANT_INFO" IS 'tenant_info';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."ID" IS 'id';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."KP" IS 'kp';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."TENANT_ID" IS 'tenant_id';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."TENANT_NAME" IS 'tenant_name';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."TENANT_DESC" IS 'tenant_desc';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."CREATE_SOURCE" IS 'create_source';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."GMT_CREATE" IS '创建时间';

COMMENT ON COLUMN "NACOS"."TENANT_INFO"."GMT_MODIFIED" IS '修改时间';

