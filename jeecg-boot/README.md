
JeecgBoot 低代码开发平台
===============

当前最新版本： 3.7.0_all（发布日期：2024-06-23） 


[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zhangdaiscott/jeecg-boot/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-北京国炬软件-orange.svg)](http://jeecg.com/aboutusIndex)
[![](https://img.shields.io/badge/version-3.7.0_all-brightgreen.svg)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub stars](https://img.shields.io/github/stars/zhangdaiscott/jeecg-boot.svg?style=social&label=Stars)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub forks](https://img.shields.io/github/forks/zhangdaiscott/jeecg-boot.svg?style=social&label=Fork)](https://github.com/zhangdaiscott/jeecg-boot)



项目介绍
-----------------------------------

<h3 align="center">Java Low Code Platform for Enterprise web applications</h3>

JeecgBoot 是一款基于代码生成器的`低代码开发平台`！前后端分离架构 SpringBoot2.x和3.x，SpringCloud，Ant Design Vue3，Mybatis-plus，Shiro，JWT，支持微服务。强大的代码生成器让前后端代码一键生成，实现低代码开发!  JeecgBoot 引领新的低代码开发模式(OnlineCoding-> 代码生成器-> 手工MERGE)， 帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省研发成本，同时又不失灵活性！


#### 项目说明

| 项目名                | 说明                     | 
|--------------------|------------------------|
| `jeecg-boot`    | 后端源码JAVA（SpringBoot微服务架构）        |
| `jeecgboot-vue3` | 前端源码VUE3（vue3+vite5+ts最新技术栈）  |



技术文档
-----------------------------------

- 官方网站：  [http://www.jeecg.com](http://www.jeecg.com)
- 新手指南： [快速入门](http://www.jeecg.com/doc/quickstart)
- QQ交流群 ： ⑨808791225、其他(满)
- 在线演示 ：  [在线演示](http://boot3.jeecg.com)   | [APP演示](http://jeecg.com/appIndex)
> 演示系统的登录账号密码，请点击 [获取账号密码](http://jeecg.com/doc/demo) 获取 



启动项目
-----------------------------------

- [IDEA启动前后端项目](https://help.jeecg.com/java/setup/idea/startup.html)
- [Docker一键启动前后端](https://help.jeecg.com/java/docker/quick.html)


微服务启动
-----------------------------------
- [单体快速切换微服务](https://help.jeecg.com/java/springcloud/switchcloud/monomer.html)
- [Docker启动微服务后台](https://help.jeecg.com/java/docker/springcloud.html)



技术架构：
-----------------------------------

#### 后端

- IDE建议： IDEA (必须安装lombok插件 )
- 语言：Java 8+ (支持17)
- 依赖管理：Maven
- 基础框架：Spring Boot 2.7.18
- 微服务框架： Spring Cloud Alibaba 2021.0.1.0
- 持久层框架：MybatisPlus 3.5.3.2
- 报表工具： JimuReport 1.7.6
- 安全框架：Apache Shiro 1.12.0，Jwt 3.11.0
- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel、Skywalking
- 数据库连接池：阿里巴巴Druid 1.1.22
- 日志打印：logback
- 缓存：Redis
- 其他：autopoi, fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。
- 默认数据库脚本：MySQL5.7+
- [其他数据库，需要自己转](https://my.oschina.net/jeecg/blog/4905722)


#### 前端

- 前端IDE建议：WebStorm、Vscode
- 采用 Vue3.0+TypeScript+Vite+Ant-Design-Vue等新技术方案，包括二次封装组件、utils、hooks、动态菜单、权限校验、按钮级别权限控制等功能
- 最新技术栈：Vue3.0 + TypeScript + Vite5 + ant-design-vue4 + pinia + echarts + unocss + vxe-table + qiankun + es6
- 依赖管理：node、npm、pnpm



#### 支持库

|  数据库   |  支持   |
| --- | --- |
|   MySQL   |  √   |
|  Oracle11g   |  √   |
|  Sqlserver2017   |  √   |
|   PostgreSQL   |  √   |
|   MariaDB   |  √   |
|   达梦   |  √   |
|   人大金仓   |  √   |



 
## 微服务解决方案


- 1、服务注册和发现 Nacos √
- 2、统一配置中心 Nacos  √
- 3、路由网关 gateway(三种加载方式) √
- 4、分布式 http feign √
- 5、熔断降级限流 Sentinel √
- 6、分布式文件 Minio、阿里OSS √ 
- 7、统一权限控制 JWT + Shiro √
- 8、服务监控 SpringBootAdmin√
- 9、链路跟踪 Skywalking   [参考文档](https://help.jeecg.com/java/springcloud/super/skywarking.html)
- 10、消息中间件 RabbitMQ  √
- 11、分布式任务 xxl-job  √ 
- 12、分布式事务 Seata
- 13、轻量分布式日志 Loki+grafana套件
- 14、支持 docker-compose、k8s、jenkins
- 15、CAS 单点登录   √
- 16、路由限流   √



后台目录结构
-----------------------------------
```
项目结构
├─jeecg-boot-parent（父POM： 项目依赖、modules组织）
│  ├─jeecg-boot-base-core（共通模块： 工具类、config、权限、查询过滤器、注解等）
│  ├─jeecg-module-demo    示例代码
│  ├─jeecg-module-system  System系统管理目录
│  │  ├─jeecg-system-biz    System系统管理权限等功能
│  │  ├─jeecg-system-start  System单体启动项目(8080）
│  │  ├─jeecg-system-api    System系统管理模块对外api
│  │  │  ├─jeecg-system-cloud-api   System模块对外提供的微服务接口
│  │  │  ├─jeecg-system-local-api   System模块对外提供的单体接口
│  ├─jeecg-server-cloud           --微服务模块
     ├─jeecg-cloud-gateway       --微服务网关模块(9999)
     ├─jeecg-cloud-nacos       --Nacos服务模块(8848)
     ├─jeecg-system-cloud-start  --System微服务启动项目(7001)
     ├─jeecg-demo-cloud-start    --Demo微服务启动项目(7002)
     ├─jeecg-visual
        ├─jeecg-cloud-monitor       --微服务监控模块 (9111)
        ├─jeecg-cloud-xxljob        --微服务xxljob定时任务服务端 (9080)
        ├─jeecg-cloud-sentinel     --sentinel服务端 (9000)
        ├─jeecg-cloud-test           -- 微服务测试示例（各种例子）
           ├─jeecg-cloud-test-more         -- 微服务测试示例（feign、熔断降级、xxljob、分布式锁）
           ├─jeecg-cloud-test-rabbitmq     -- 微服务测试示例（rabbitmq）
           ├─jeecg-cloud-test-seata          -- 微服务测试示例（seata分布式事务）
           ├─jeecg-cloud-test-shardingsphere    -- 微服务测试示例（分库分表）
```




#### 微服务架构图
![微服务架构图](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/jeecgboot_springcloud2022.png "在这里输入图片标题")





