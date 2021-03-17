CREATE database if NOT EXISTS `nacos` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `nacos`;

/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 127.0.0.1:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 03/03/2021 13:10:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'jeecg-dev.yaml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)\n        # 连接池的配置信息\n        # 初始化大小，最小，最大\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        # 配置获取连接等待超时的时间\n        maxWait: 60000\n        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\n        timeBetweenEvictionRunsMillis: 60000\n        # 配置一个连接在池中最小生存的时间，单位是毫秒\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        # 打开PSCache，并且指定每个连接上PSCache的大小\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，\'wall\'用于防火墙\n        filters: stat,wall,slf4j\n        # 通过connectProperties属性来打开mergeSql功能；慢SQL记录\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          # 多数据源配置\n          #multi-datasource1:\n          #url: jdbc:mysql://localhost:3306/jeecg-boot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          #username: root\n          #password: root\n          #driver-class-name: com.mysql.cj.jdbc.Driver\n  #redis 配置\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    lettuce:\n      pool:\n        max-active: 8   #最大连接数据库连接数,设 0 为没有限制\n        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制\n        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。\n        min-idle: 0     #最小等待连接中的数量,设 0 为没有限制\n      shutdown-timeout: 100ms\n    password:\n    port: 6379\n  #rabbitmq配置\n  rabbitmq:\n    host: 127.0.0.1\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        #消费者的最小数量\n        concurrency: 1\n        #消费者的最大数量\n        max-concurrency: 1\n        #是否支持重试\n        retry:\n          enabled: true\n#jeecg专用配置\njeecg :\n  # 本地：local\\Minio：minio\\阿里云：alioss\n  uploadType: local\n  path :\n    #文件上传根目录 设置\n    upload: D://opt//upFiles\n    #webapp文件路径\n    webapp: D://opt//webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  #阿里云oss存储配置\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgos\n    staticDomain: ??\n  # ElasticSearch 6设置\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: 127.0.0.1:9200\n    check-enabled: false\n  # 表单设计器配置\n  desform:\n    # 主题颜色（仅支持 16进制颜色代码）\n    theme-color: \"#1890ff\"\n    # 文件、图片上传方式，可选项：qiniu（七牛云）、system（跟随系统配置）\n    upload-type: system\n  # 在线预览文件服务器地址配置\n  file-view-domain: 127.0.0.1:8012\n  # minio文件上传\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  #大屏报表参数设置\n  jmreport:\n    mode: dev\n    #是否需要校验token\n    is_verify_token: false\n    #必须校验方法\n    verify_methods: remove,delete,save,add,update\n  #Wps在线文档\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  #xxl-job配置\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    address: jeecg-boot-system:30007\n    ip: jeecg-boot-system\n    port: 30007\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n   #自定义路由配置 yml nacos database\n  route:\n    config:\n      data-id: jeecg-gateway-router\n      group: DEFAULT_GROUP\n      data-type: yml\n  #分布式锁配置\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\n#Mybatis输出sql日志\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\n#cas单点登录\ncas:\n  prefixUrl: http://localhost:8888/cas\n#swagger\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\n\n#第三方登录\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: 1000002\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h', '87a50a11f0eb57d6ee4b927a63619173', '2021-03-03 13:01:11', '2021-03-03 13:07:28', NULL, '172.17.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (2, 'jeecg.yaml', 'DEFAULT_GROUP', 'server:\r\n  tomcat:\r\n    max-swallow-size: -1\r\n  error:\r\n    include-exception: true\r\n    include-stacktrace: ALWAYS\r\n    include-message: ALWAYS\r\n  compression:\r\n    enabled: true\r\n    min-response-size: 1024\r\n    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*\r\nmanagement:\r\n  health:\r\n    mail:\r\n      enabled: false\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \"*\" #暴露所有节点\r\n    health:\r\n      sensitive: true #关闭过滤敏感信息\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS  #显示详细信息\r\nspring:\r\n  servlet:\r\n    multipart:\r\n      max-file-size: 10MB\r\n      max-request-size: 10MB\r\n  mail:\r\n    host: smtp.163.com\r\n    username: jeecgos@163.com\r\n    password: ??\r\n    properties:\r\n      mail:\r\n        smtp:\r\n          auth: true\r\n          starttls:\r\n            enable: true\r\n            required: true\r\n  ## quartz定时任务,采用数据库方式\r\n  quartz:\r\n    job-store-type: jdbc\r\n    initialize-schema: embedded\r\n    #设置自动启动，默认为 true\r\n    auto-startup: true\r\n    #启动时更新己存在的Job\r\n    overwrite-existing-jobs: true\r\n    properties:\r\n      org:\r\n        quartz:\r\n          scheduler:\r\n            instanceName: MyScheduler\r\n            instanceId: AUTO\r\n          jobStore:\r\n            class: org.quartz.impl.jdbcjobstore.JobStoreTX\r\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\r\n            tablePrefix: QRTZ_\r\n            isClustered: true\r\n            misfireThreshold: 60000\r\n            clusterCheckinInterval: 10000\r\n          threadPool:\r\n            class: org.quartz.simpl.SimpleThreadPool\r\n            threadCount: 10\r\n            threadPriority: 5\r\n            threadsInheritContextClassLoaderOfInitializingThread: true\r\n  #json 时间戳统一转换\r\n  jackson:\r\n    date-format:   yyyy-MM-dd HH:mm:ss\r\n    time-zone:   GMT+8\r\n  aop:\r\n    proxy-target-class: true\r\n  activiti:\r\n    check-process-definitions: false\r\n    #启用作业执行器\r\n    async-executor-activate: false\r\n    #启用异步执行器\r\n    job-executor-activate: false\r\n  jpa:\r\n    open-in-view: false\r\n  #配置freemarker\r\n  freemarker:\r\n    # 设置模板后缀名\r\n    suffix: .ftl\r\n    # 设置文档类型\r\n    content-type: text/html\r\n    # 设置页面编码格式\r\n    charset: UTF-8\r\n    # 设置页面缓存\r\n    cache: false\r\n    prefer-file-system-access: false\r\n    # 设置ftl文件路径\r\n    template-loader-path:\r\n      - classpath:/templates\r\n  # 设置静态文件路径，js,css等\r\n  mvc:\r\n    static-path-pattern: /**\r\n  resource:\r\n    static-locations: classpath:/static/,classpath:/public/\r\n  autoconfigure:\r\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n#mybatis plus 设置\r\nmybatis-plus:\r\n  mapper-locations: classpath*:org/jeecg/modules/**/xml/*Mapper.xml\r\n  global-config:\r\n    # 关闭MP3.0自带的banner\r\n    banner: false\r\n    db-config:\r\n      #主键类型  0:\"数据库ID自增\",1:\"该类型为未设置主键类型\", 2:\"用户输入ID\",3:\"全局唯一ID (数字类型唯一ID)\", 4:\"全局唯一ID UUID\",5:\"字符串全局唯一ID (idWorker 的字符串表示)\";\r\n      id-type: ASSIGN_ID\r\n      # 默认数据库表下划线命名\r\n      table-underline: true\r\n  configuration:\r\n    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用\r\n    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n    # 返回类型为Map,显示null对应的字段\r\n    call-setters-on-nulls: true', 'd695ddf9b45ff9f8e009803c93650263', '2021-03-03 13:01:42', '2021-03-03 13:01:42', NULL, '172.17.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (3, 'jeecg-gateway-router.json', 'DEFAULT_GROUP', '[{\r\n  \"id\": \"jeecg-system\",\r\n  \"order\": 0,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/sys/**\",\r\n      \"_genkey_1\": \"/eoa/**\",\r\n      \"_genkey_2\": \"/joa/**\",\r\n      \"_genkey_3\": \"/jmreport/**\",\r\n      \"_genkey_4\": \"/bigscreen/**\",\r\n      \"_genkey_5\": \"/desform/**\",\r\n      \"_genkey_6\": \"/online/**\",\r\n      \"_genkey_8\": \"/act/**\",\r\n      \"_genkey_9\": \"/plug-in/**\",\r\n      \"_genkey_10\": \"/generic/**\",\r\n      \"_genkey_11\": \"/v1/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb://jeecg-system\"\r\n}, {\r\n  \"id\": \"jeecg-demo\",\r\n  \"order\": 1,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/mock/**\",\r\n      \"_genkey_1\": \"/test/**\",\r\n      \"_genkey_2\": \"/bigscreen/template1/**\",\r\n      \"_genkey_3\": \"/bigscreen/template2/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb://jeecg-demo\"\r\n}, {\r\n  \"id\": \"jeecg-system-websocket\",\r\n  \"order\": 2,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/websocket/**\",\r\n      \"_genkey_1\": \"/eoaSocket/**\",\r\n      \"_genkey_2\": \"/newsWebsocket/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb:ws://jeecg-system\"\r\n}, {\r\n  \"id\": \"jeecg-demo-websocket\",\r\n  \"order\": 3,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/vxeSocket/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb:ws://jeecg-demo\"\r\n}]', '82f4033ef6a51ce2ab6ce505be1b729a', '2021-03-03 13:02:14', '2021-03-03 13:02:14', NULL, '172.17.0.1', '', '', NULL, NULL, NULL, 'json', NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    druid:\r\n      stat-view-servlet:\r\n        enabled: true\r\n        loginUsername: admin\r\n        loginPassword: 123456\r\n        allow:\r\n      web-stat-filter:\r\n        enabled: true\r\n    dynamic:\r\n      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)\r\n        # 连接池的配置信息\r\n        # 初始化大小，最小，最大\r\n        initial-size: 5\r\n        min-idle: 5\r\n        maxActive: 20\r\n        # 配置获取连接等待超时的时间\r\n        maxWait: 60000\r\n        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\r\n        timeBetweenEvictionRunsMillis: 60000\r\n        # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n        minEvictableIdleTimeMillis: 300000\r\n        validationQuery: SELECT 1 FROM DUAL\r\n        testWhileIdle: true\r\n        testOnBorrow: false\r\n        testOnReturn: false\r\n        # 打开PSCache，并且指定每个连接上PSCache的大小\r\n        poolPreparedStatements: true\r\n        maxPoolPreparedStatementPerConnectionSize: 20\r\n        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，\'wall\'用于防火墙\r\n        filters: stat,wall,slf4j\r\n        # 通过connectProperties属性来打开mergeSql功能；慢SQL记录\r\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\r\n\r\n      datasource:\r\n        master:\r\n          url: jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          username: root\r\n          password: root\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          # 多数据源配置\r\n          #multi-datasource1:\r\n          #url: jdbc:mysql://localhost:3306/jeecg-boot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          #username: root\r\n          #password: root\r\n          #driver-class-name: com.mysql.cj.jdbc.Driver\r\n  #redis 配置\r\n  redis:\r\n    database: 0\r\n    host: 127.0.0.1\r\n    lettuce:\r\n      pool:\r\n        max-active: 8   #最大连接数据库连接数,设 0 为没有限制\r\n        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制\r\n        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。\r\n        min-idle: 0     #最小等待连接中的数量,设 0 为没有限制\r\n      shutdown-timeout: 100ms\r\n    password:\r\n    port: 6379\r\n  #rabbitmq配置\r\n  rabbitmq:\r\n    host: 127.0.0.1\r\n    username: guest\r\n    password: guest\r\n    port: 5672\r\n    publisher-confirms: true\r\n    publisher-returns: true\r\n    virtual-host: /\r\n    listener:\r\n      simple:\r\n        acknowledge-mode: manual\r\n        #消费者的最小数量\r\n        concurrency: 1\r\n        #消费者的最大数量\r\n        max-concurrency: 1\r\n        #是否支持重试\r\n        retry:\r\n          enabled: true\r\n#jeecg专用配置\r\njeecg :\r\n  # 本地：local\\Minio：minio\\阿里云：alioss\r\n  uploadType: local\r\n  path :\r\n    #文件上传根目录 设置\r\n    upload: D://opt//upFiles\r\n    #webapp文件路径\r\n    webapp: D://opt//webapp\r\n  shiro:\r\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\r\n  #阿里云oss存储配置\r\n  oss:\r\n    endpoint: oss-cn-beijing.aliyuncs.com\r\n    accessKey: ??\r\n    secretKey: ??\r\n    bucketName: jeecgos\r\n    staticDomain: ??\r\n  # ElasticSearch 6设置\r\n  elasticsearch:\r\n    cluster-name: jeecg-ES\r\n    cluster-nodes: 127.0.0.1:9200\r\n    check-enabled: false\r\n  # 表单设计器配置\r\n  desform:\r\n    # 主题颜色（仅支持 16进制颜色代码）\r\n    theme-color: \"#1890ff\"\r\n    # 文件、图片上传方式，可选项：qiniu（七牛云）、system（跟随系统配置）\r\n    upload-type: system\r\n  # 在线预览文件服务器地址配置\r\n  file-view-domain: 127.0.0.1:8012\r\n  # minio文件上传\r\n  minio:\r\n    minio_url: http://minio.jeecg.com\r\n    minio_name: ??\r\n    minio_pass: ??\r\n    bucketName: otatest\r\n  #大屏报表参数设置\r\n  jmreport:\r\n    mode: dev\r\n    #是否需要校验token\r\n    is_verify_token: false\r\n    #必须校验方法\r\n    verify_methods: remove,delete,save,add,update\r\n  #Wps在线文档\r\n  wps:\r\n    domain: https://wwo.wps.cn/office/\r\n    appid: ??\r\n    appsecret: ??\r\n  #xxl-job配置\r\n  xxljob:\r\n    enabled: false\r\n    adminAddresses: http://127.0.0.1:9080/xxl-job-admin\r\n    appname: ${spring.application.name}\r\n    accessToken: \'\'\r\n    address: 127.0.0.1:30007\r\n    ip: 127.0.0.1\r\n    port: 30007\r\n    logPath: logs/jeecg/job/jobhandler/\r\n    logRetentionDays: 30\r\n   #自定义路由配置 yml nacos database\r\n  route:\r\n    config:\r\n      data-id: jeecg-gateway-router\r\n      group: DEFAULT_GROUP\r\n      data-type: yml\r\n  #分布式锁配置\r\n  redisson:\r\n    address: 127.0.0.1:6379\r\n    password:\r\n    type: STANDALONE\r\n    enabled: true\r\n#Mybatis输出sql日志\r\nlogging:\r\n  level:\r\n    org.jeecg.modules.system.mapper : info\r\n#cas单点登录\r\ncas:\r\n  prefixUrl: http://localhost:8888/cas\r\n#swagger\r\nknife4j:\r\n  production: false\r\n  basic:\r\n    enable: false\r\n    username: jeecg\r\n    password: jeecg1314\r\n\r\n#第三方登录\r\njustauth:\r\n  enabled: true\r\n  type:\r\n    GITHUB:\r\n      client-id: ??\r\n      client-secret: ??\r\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\r\n    WECHAT_ENTERPRISE:\r\n      client-id: ??\r\n      client-secret: ??\r\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\r\n      agent-id: 1000002\r\n    DINGTALK:\r\n      client-id: ??\r\n      client-secret: ??\r\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\r\n  cache:\r\n    type: default\r\n    prefix: \'demo::\'\r\n    timeout: 1h', 'ee9e4d63cce2009104ccd100c8512c63', '2010-05-05 00:00:00', '2021-03-03 13:01:11', NULL, '172.17.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 2, 'jeecg.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  tomcat:\r\n    max-swallow-size: -1\r\n  error:\r\n    include-exception: true\r\n    include-stacktrace: ALWAYS\r\n    include-message: ALWAYS\r\n  compression:\r\n    enabled: true\r\n    min-response-size: 1024\r\n    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*\r\nmanagement:\r\n  health:\r\n    mail:\r\n      enabled: false\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \"*\" #暴露所有节点\r\n    health:\r\n      sensitive: true #关闭过滤敏感信息\r\n  endpoint:\r\n    health:\r\n      show-details: ALWAYS  #显示详细信息\r\nspring:\r\n  servlet:\r\n    multipart:\r\n      max-file-size: 10MB\r\n      max-request-size: 10MB\r\n  mail:\r\n    host: smtp.163.com\r\n    username: jeecgos@163.com\r\n    password: ??\r\n    properties:\r\n      mail:\r\n        smtp:\r\n          auth: true\r\n          starttls:\r\n            enable: true\r\n            required: true\r\n  ## quartz定时任务,采用数据库方式\r\n  quartz:\r\n    job-store-type: jdbc\r\n    initialize-schema: embedded\r\n    #设置自动启动，默认为 true\r\n    auto-startup: true\r\n    #启动时更新己存在的Job\r\n    overwrite-existing-jobs: true\r\n    properties:\r\n      org:\r\n        quartz:\r\n          scheduler:\r\n            instanceName: MyScheduler\r\n            instanceId: AUTO\r\n          jobStore:\r\n            class: org.quartz.impl.jdbcjobstore.JobStoreTX\r\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\r\n            tablePrefix: QRTZ_\r\n            isClustered: true\r\n            misfireThreshold: 60000\r\n            clusterCheckinInterval: 10000\r\n          threadPool:\r\n            class: org.quartz.simpl.SimpleThreadPool\r\n            threadCount: 10\r\n            threadPriority: 5\r\n            threadsInheritContextClassLoaderOfInitializingThread: true\r\n  #json 时间戳统一转换\r\n  jackson:\r\n    date-format:   yyyy-MM-dd HH:mm:ss\r\n    time-zone:   GMT+8\r\n  aop:\r\n    proxy-target-class: true\r\n  activiti:\r\n    check-process-definitions: false\r\n    #启用作业执行器\r\n    async-executor-activate: false\r\n    #启用异步执行器\r\n    job-executor-activate: false\r\n  jpa:\r\n    open-in-view: false\r\n  #配置freemarker\r\n  freemarker:\r\n    # 设置模板后缀名\r\n    suffix: .ftl\r\n    # 设置文档类型\r\n    content-type: text/html\r\n    # 设置页面编码格式\r\n    charset: UTF-8\r\n    # 设置页面缓存\r\n    cache: false\r\n    prefer-file-system-access: false\r\n    # 设置ftl文件路径\r\n    template-loader-path:\r\n      - classpath:/templates\r\n  # 设置静态文件路径，js,css等\r\n  mvc:\r\n    static-path-pattern: /**\r\n  resource:\r\n    static-locations: classpath:/static/,classpath:/public/\r\n  autoconfigure:\r\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n#mybatis plus 设置\r\nmybatis-plus:\r\n  mapper-locations: classpath*:org/jeecg/modules/**/xml/*Mapper.xml\r\n  global-config:\r\n    # 关闭MP3.0自带的banner\r\n    banner: false\r\n    db-config:\r\n      #主键类型  0:\"数据库ID自增\",1:\"该类型为未设置主键类型\", 2:\"用户输入ID\",3:\"全局唯一ID (数字类型唯一ID)\", 4:\"全局唯一ID UUID\",5:\"字符串全局唯一ID (idWorker 的字符串表示)\";\r\n      id-type: ASSIGN_ID\r\n      # 默认数据库表下划线命名\r\n      table-underline: true\r\n  configuration:\r\n    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用\r\n    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n    # 返回类型为Map,显示null对应的字段\r\n    call-setters-on-nulls: true', 'd695ddf9b45ff9f8e009803c93650263', '2010-05-05 00:00:00', '2021-03-03 13:01:42', NULL, '172.17.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'jeecg-gateway-router.json', 'DEFAULT_GROUP', '', '[{\r\n  \"id\": \"jeecg-system\",\r\n  \"order\": 0,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/sys/**\",\r\n      \"_genkey_1\": \"/eoa/**\",\r\n      \"_genkey_2\": \"/joa/**\",\r\n      \"_genkey_3\": \"/jmreport/**\",\r\n      \"_genkey_4\": \"/bigscreen/**\",\r\n      \"_genkey_5\": \"/desform/**\",\r\n      \"_genkey_6\": \"/online/**\",\r\n      \"_genkey_8\": \"/act/**\",\r\n      \"_genkey_9\": \"/plug-in/**\",\r\n      \"_genkey_10\": \"/generic/**\",\r\n      \"_genkey_11\": \"/v1/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb://jeecg-system\"\r\n}, {\r\n  \"id\": \"jeecg-demo\",\r\n  \"order\": 1,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/mock/**\",\r\n      \"_genkey_1\": \"/test/**\",\r\n      \"_genkey_2\": \"/bigscreen/template1/**\",\r\n      \"_genkey_3\": \"/bigscreen/template2/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb://jeecg-demo\"\r\n}, {\r\n  \"id\": \"jeecg-system-websocket\",\r\n  \"order\": 2,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/websocket/**\",\r\n      \"_genkey_1\": \"/eoaSocket/**\",\r\n      \"_genkey_2\": \"/newsWebsocket/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb:ws://jeecg-system\"\r\n}, {\r\n  \"id\": \"jeecg-demo-websocket\",\r\n  \"order\": 3,\r\n  \"predicates\": [{\r\n    \"name\": \"Path\",\r\n    \"args\": {\r\n      \"_genkey_0\": \"/vxeSocket/**\"\r\n    }\r\n  }],\r\n  \"filters\": [],\r\n  \"uri\": \"lb:ws://jeecg-demo\"\r\n}]', '82f4033ef6a51ce2ab6ce505be1b729a', '2010-05-05 00:00:00', '2021-03-03 13:02:14', NULL, '172.17.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (1, 4, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n    druid:\r\n      stat-view-servlet:\r\n        enabled: true\r\n        loginUsername: admin\r\n        loginPassword: 123456\r\n        allow:\r\n      web-stat-filter:\r\n        enabled: true\r\n    dynamic:\r\n      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)\r\n        # 连接池的配置信息\r\n        # 初始化大小，最小，最大\r\n        initial-size: 5\r\n        min-idle: 5\r\n        maxActive: 20\r\n        # 配置获取连接等待超时的时间\r\n        maxWait: 60000\r\n        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\r\n        timeBetweenEvictionRunsMillis: 60000\r\n        # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n        minEvictableIdleTimeMillis: 300000\r\n        validationQuery: SELECT 1 FROM DUAL\r\n        testWhileIdle: true\r\n        testOnBorrow: false\r\n        testOnReturn: false\r\n        # 打开PSCache，并且指定每个连接上PSCache的大小\r\n        poolPreparedStatements: true\r\n        maxPoolPreparedStatementPerConnectionSize: 20\r\n        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，\'wall\'用于防火墙\r\n        filters: stat,wall,slf4j\r\n        # 通过connectProperties属性来打开mergeSql功能；慢SQL记录\r\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\r\n\r\n      datasource:\r\n        master:\r\n          url: jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          username: root\r\n          password: root\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          # 多数据源配置\r\n          #multi-datasource1:\r\n          #url: jdbc:mysql://localhost:3306/jeecg-boot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          #username: root\r\n          #password: root\r\n          #driver-class-name: com.mysql.cj.jdbc.Driver\r\n  #redis 配置\r\n  redis:\r\n    database: 0\r\n    host: 127.0.0.1\r\n    lettuce:\r\n      pool:\r\n        max-active: 8   #最大连接数据库连接数,设 0 为没有限制\r\n        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制\r\n        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。\r\n        min-idle: 0     #最小等待连接中的数量,设 0 为没有限制\r\n      shutdown-timeout: 100ms\r\n    password:\r\n    port: 6379\r\n  #rabbitmq配置\r\n  rabbitmq:\r\n    host: 127.0.0.1\r\n    username: guest\r\n    password: guest\r\n    port: 5672\r\n    publisher-confirms: true\r\n    publisher-returns: true\r\n    virtual-host: /\r\n    listener:\r\n      simple:\r\n        acknowledge-mode: manual\r\n        #消费者的最小数量\r\n        concurrency: 1\r\n        #消费者的最大数量\r\n        max-concurrency: 1\r\n        #是否支持重试\r\n        retry:\r\n          enabled: true\r\n#jeecg专用配置\r\njeecg :\r\n  # 本地：local\\Minio：minio\\阿里云：alioss\r\n  uploadType: local\r\n  path :\r\n    #文件上传根目录 设置\r\n    upload: D://opt//upFiles\r\n    #webapp文件路径\r\n    webapp: D://opt//webapp\r\n  shiro:\r\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\r\n  #阿里云oss存储配置\r\n  oss:\r\n    endpoint: oss-cn-beijing.aliyuncs.com\r\n    accessKey: ??\r\n    secretKey: ??\r\n    bucketName: jeecgos\r\n    staticDomain: ??\r\n  # ElasticSearch 6设置\r\n  elasticsearch:\r\n    cluster-name: jeecg-ES\r\n    cluster-nodes: 127.0.0.1:9200\r\n    check-enabled: false\r\n  # 表单设计器配置\r\n  desform:\r\n    # 主题颜色（仅支持 16进制颜色代码）\r\n    theme-color: \"#1890ff\"\r\n    # 文件、图片上传方式，可选项：qiniu（七牛云）、system（跟随系统配置）\r\n    upload-type: system\r\n  # 在线预览文件服务器地址配置\r\n  file-view-domain: 127.0.0.1:8012\r\n  # minio文件上传\r\n  minio:\r\n    minio_url: http://minio.jeecg.com\r\n    minio_name: ??\r\n    minio_pass: ??\r\n    bucketName: otatest\r\n  #大屏报表参数设置\r\n  jmreport:\r\n    mode: dev\r\n    #是否需要校验token\r\n    is_verify_token: false\r\n    #必须校验方法\r\n    verify_methods: remove,delete,save,add,update\r\n  #Wps在线文档\r\n  wps:\r\n    domain: https://wwo.wps.cn/office/\r\n    appid: ??\r\n    appsecret: ??\r\n  #xxl-job配置\r\n  xxljob:\r\n    enabled: false\r\n    adminAddresses: http://127.0.0.1:9080/xxl-job-admin\r\n    appname: ${spring.application.name}\r\n    accessToken: \'\'\r\n    address: 127.0.0.1:30007\r\n    ip: 127.0.0.1\r\n    port: 30007\r\n    logPath: logs/jeecg/job/jobhandler/\r\n    logRetentionDays: 30\r\n   #自定义路由配置 yml nacos database\r\n  route:\r\n    config:\r\n      data-id: jeecg-gateway-router\r\n      group: DEFAULT_GROUP\r\n      data-type: yml\r\n  #分布式锁配置\r\n  redisson:\r\n    address: 127.0.0.1:6379\r\n    password:\r\n    type: STANDALONE\r\n    enabled: true\r\n#Mybatis输出sql日志\r\nlogging:\r\n  level:\r\n    org.jeecg.modules.system.mapper : info\r\n#cas单点登录\r\ncas:\r\n  prefixUrl: http://localhost:8888/cas\r\n#swagger\r\nknife4j:\r\n  production: false\r\n  basic:\r\n    enable: false\r\n    username: jeecg\r\n    password: jeecg1314\r\n\r\n#第三方登录\r\njustauth:\r\n  enabled: true\r\n  type:\r\n    GITHUB:\r\n      client-id: ??\r\n      client-secret: ??\r\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\r\n    WECHAT_ENTERPRISE:\r\n      client-id: ??\r\n      client-secret: ??\r\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\r\n      agent-id: 1000002\r\n    DINGTALK:\r\n      client-id: ??\r\n      client-secret: ??\r\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\r\n  cache:\r\n    type: default\r\n    prefix: \'demo::\'\r\n    timeout: 1h', 'ee9e4d63cce2009104ccd100c8512c63', '2010-05-05 00:00:00', '2021-03-03 13:03:41', NULL, '172.17.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (1, 5, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '', 'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid: # 全局druid参数，绝大部分值和默认保持一致。(现已支持的参数如下,不清楚含义不要乱设置)\n        # 连接池的配置信息\n        # 初始化大小，最小，最大\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        # 配置获取连接等待超时的时间\n        maxWait: 60000\n        # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒\n        timeBetweenEvictionRunsMillis: 60000\n        # 配置一个连接在池中最小生存的时间，单位是毫秒\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        # 打开PSCache，并且指定每个连接上PSCache的大小\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，\'wall\'用于防火墙\n        filters: stat,wall,slf4j\n        # 通过connectProperties属性来打开mergeSql功能；慢SQL记录\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n          # 多数据源配置\n          #multi-datasource1:\n          #url: jdbc:mysql://localhost:3306/jeecg-boot2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          #username: root\n          #password: root\n          #driver-class-name: com.mysql.cj.jdbc.Driver\n  #redis 配置\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    lettuce:\n      pool:\n        max-active: 8   #最大连接数据库连接数,设 0 为没有限制\n        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制\n        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。\n        min-idle: 0     #最小等待连接中的数量,设 0 为没有限制\n      shutdown-timeout: 100ms\n    password:\n    port: 6379\n  #rabbitmq配置\n  rabbitmq:\n    host: 127.0.0.1\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        #消费者的最小数量\n        concurrency: 1\n        #消费者的最大数量\n        max-concurrency: 1\n        #是否支持重试\n        retry:\n          enabled: true\n#jeecg专用配置\njeecg :\n  # 本地：local\\Minio：minio\\阿里云：alioss\n  uploadType: local\n  path :\n    #文件上传根目录 设置\n    upload: D://opt//upFiles\n    #webapp文件路径\n    webapp: D://opt//webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  #阿里云oss存储配置\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgos\n    staticDomain: ??\n  # ElasticSearch 6设置\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: 127.0.0.1:9200\n    check-enabled: false\n  # 表单设计器配置\n  desform:\n    # 主题颜色（仅支持 16进制颜色代码）\n    theme-color: \"#1890ff\"\n    # 文件、图片上传方式，可选项：qiniu（七牛云）、system（跟随系统配置）\n    upload-type: system\n  # 在线预览文件服务器地址配置\n  file-view-domain: 127.0.0.1:8012\n  # minio文件上传\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  #大屏报表参数设置\n  jmreport:\n    mode: dev\n    #是否需要校验token\n    is_verify_token: false\n    #必须校验方法\n    verify_methods: remove,delete,save,add,update\n  #Wps在线文档\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  #xxl-job配置\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    address: 127.0.0.1:30007\n    ip: 127.0.0.1\n    port: 30007\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n   #自定义路由配置 yml nacos database\n  route:\n    config:\n      data-id: jeecg-gateway-router\n      group: DEFAULT_GROUP\n      data-type: yml\n  #分布式锁配置\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\n#Mybatis输出sql日志\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\n#cas单点登录\ncas:\n  prefixUrl: http://localhost:8888/cas\n#swagger\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\n\n#第三方登录\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: 1000002\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h', '14deb24a5927bbf4b7cc010b55cab792', '2010-05-05 00:00:00', '2021-03-03 13:07:28', NULL, '172.17.0.1', 'U', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_username_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
