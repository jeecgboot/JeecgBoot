
JeecgBoot 低代码开发平台
===============

当前最新版本： 3.9.0（发布日期：2025-12-01） 


[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zhangdaiscott/jeecg-boot/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-北京国炬软件-orange.svg)](http://jeecg.com/aboutusIndex)
[![](https://img.shields.io/badge/version-3.9.0-brightgreen.svg)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub stars](https://img.shields.io/github/stars/zhangdaiscott/jeecg-boot.svg?style=social&label=Stars)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub forks](https://img.shields.io/github/forks/zhangdaiscott/jeecg-boot.svg?style=social&label=Fork)](https://github.com/zhangdaiscott/jeecg-boot)



项目介绍
-----------------------------------

<h3 align="center">企业级AI低代码平台</h3>

JeecgBoot 是一款基于BPM流程和代码生成的AI低代码平台，助力企业快速实现低代码开发和构建AI应用。
采用前后端分离架构（Ant Design&Vue3，SpringBoot3，SpringCloud Alibaba，Mybatis-plus），强大代码生成器实现前后端一键生成，无需手写代码。
平台引领AI低代码开发模式：AI生成→在线编码→代码生成→手工合并，解决Java项目80%重复工作，提升效率，节省成本，兼顾灵活性。
具备强大且颗粒化的权限控制，支持按钮权限和数据权限设置，满足大型业务系统需求。功能涵盖在线表单、表单设计、流程设计、门户设计、报表与大屏设计、OA办公、AI应用、AI知识库、大模型管理、AI流程编排、AI聊天，支持ChatGPT、DeepSeek、Ollama等多种AI大模型。

`AI赋能报表:` 积木报表是一款自主研发的强大开源企业级Web报表与大屏工具。它通过零编码的拖拽式操作，赋能用户如同搭积木般轻松构建各类复杂报表和数据大屏，全面满足企业数据可视化与分析需求，助力企业级数据产品的高效打造与应用。

`AI赋能低代码:` 提供完善成熟的AI应用平台，涵盖AI应用管理、AI模型管理、智能对话助手、知识库问答、流程编排与设计器、AI建表等多项功能。平台兼容多种主流大模型，包括ChatGPT、DeepSeek、Ollama、智普、千问等，助力企业高效构建智能化应用，推动低代码开发与AI深度融合。

`JEECG宗旨是:` JEECG旨在通过OnlineCoding平台实现简单功能的零代码快速搭建，同时针对复杂功能采用代码生成器生成代码并手工合并，打造智能且灵活的低代码开发模式，有效解决了当前低代码产品普遍缺乏灵活性的问题，提升开发效率的同时兼顾系统的扩展性和定制化能力。

`JEECG业务流程:` JEECG业务流程采用BPM工作流引擎实现业务审批，扩展任务接口供开发人员编写业务逻辑，表单提供表单设计器、在线配置表单和编码表单等多种解决方案。通过流程与表单的分离设计（松耦合）及任务节点的灵活配置，既保障了企业流程的安全性与保密性，又大幅降低了开发人员的工作量。


适用项目
-----------------------------------
JeecgBoot低代码平台兼容所有J2EE项目开发，支持信创国产化，特别适用于SAAS、企业信息管理系统（MIS）、内部办公系统（OA）、企业资源计划系统（ERP）、客户关系管理系统（CRM）及AI知识库等场景。其半智能手工Merge开发模式，可显著提升70%以上的开发效率，极大降低开发成本。同时，JeecgBoot还是一款全栈式AI开发平台，助力企业快速构建和部署个性化AI应用。。


**信创兼容说明**
- 操作系统：国产麒麟、银河麒麟等国产系统几乎都是基于 Linux 内核，因此它们具有良好的兼容性。
- 数据库：达梦、人大金仓、TiDB
- 中间件：东方通 TongWeb、TongRDS，宝兰德 AppServer、CacheDB, [信创配置文档](https://help.jeecg.com/java/tongweb-deploy/)




#### 项目说明

| 项目名                | 说明                                 | 
|--------------------|------------------------------------|
| `jeecg-boot`    | 后端源码JAVA（SpringBoot3微服务架构）         |
| `jeecgboot-vue3` | 前端源码VUE3（vue3+vite6+antd4+ts最新技术栈） |



启动项目
-----------------------------------

> 默认账号密码： admin/123456

- [开发环境搭建](https://help.jeecg.com/java/setup/tools)
- [IDEA启动前后端(单体模式)](https://help.jeecg.com/java/setup/idea/startup)
- [Docker一键启动(单体模式)](https://help.jeecg.com/java/docker/quick)
- [IDEA启动前后端(微服务方式)](https://help.jeecg.com/java/springcloud/switchcloud/monomer)
- [Docker一键启动(微服务方式)](https://help.jeecg.com/java/docker/quickcloud)


技术文档
-----------------------------------

- 官方网站：  [http://www.jeecg.com](http://www.jeecg.com)
- 在线演示：  [平台演示](https://boot3.jeecg.com) | [APP演示](https://jeecg.com/appIndex)
- 入门指南：  [快速入门](http://www.jeecg.com/doc/quickstart)  | [代码生成使用](https://help.jeecg.com/java/codegen/online) | [开发文档](https://help.jeecg.com)  | [AI应用手册](https://help.jeecg.com/aigc) | [视频教程](http://jeecg.com/doc/video)
- 技术支持：  [反馈问题](https://github.com/jeecgboot/JeecgBoot/issues/new?template=bug_report.md)    | [低代码体验一分钟](https://jeecg.blog.csdn.net/article/details/106079007)
- QQ交流群 ： 964611995、⑩716488839(满)、⑨808791225(满)、其他(满)


AI 应用平台介绍
-----------------------------------

一个全栈式 AI 开发平台，旨在帮助开发者快速构建和部署个性化的 AI 应用。

JeecgBoot平台提供了一套完善的AI应用管理系统模块，是一套类似`Dify`的`AIGC应用开发平台`+`知识库问答`，是一款基于LLM大语言模型AI应用平台和 RAG 的知识库问答系统。
其直观的界面结合了 AI 流程编排、RAG 管道、知识库管理、模型管理、对接向量库、实时运行可观察等，让您可以快速从原型到生产，拥有AI服务能力。

- [详细专题介绍，请点击查看](README-AI.md)

- AI视频介绍

[![](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/jeecg_aivideo.png)](https://www.bilibili.com/video/BV1zmd7YFE4w)


为什么选择JeecgBoot?
-----------------------------------
- 1.采用最新主流前后分离框架（Spring Boot3 + MyBatis + Shiro/SpringAuthorizationServer + Ant Design4 + Vue3），容易上手；代码生成器依赖性低，灵活的扩展能力，可快速实现二次开发。
- 2.前端大版本换代，最新版采用 Vue3.0 + TypeScript + Vite6 + Ant Design Vue4 等新技术方案。
- 3.支持微服务Spring Cloud Alibaba（Nacos、Gateway、Sentinel、Skywalking），提供简易机制，支持单体和微服务自由切换（这样可以满足各类项目需求）。
- 4.开发效率高，支持在线建表和AI建表，提供强大代码生成器，单表、树列表、一对多、一对一等数据模型，增删改查功能一键生成，菜单配置直接使用。
- 5.代码生成器提供强大模板机制，支持自定义模板，目前提供四套风格模板（单表两套、树模型一套、一对多三套）。
- 6.提供强大的报表和大屏可视化工具，支持丰富的数据源连接，能够通过拖拉拽方式快速制作报表、大屏和门户设计；支持多种图表类型：柱形图、折线图、散点图、饼图、环形图、面积图、漏斗图、进度图、仪表盘、雷达图、地图等。
- 7.低代码能力：在线表单（无需编码，通过在线配置表单，实现表单的增删改查，支持单表、树、一对多、一对一等模型，实现人人皆可编码），在线配置零代码开发、所见即所得支持23种类控件。
- 8.低代码能力：在线报表、在线图表（无需编码，通过在线配置方式，实现数据报表和图形报表，可以快速抽取数据，减轻开发压力，实现人人皆可编码）。
- 9.Online支持在线增强开发，提供在线代码编辑器，支持代码高亮、代码提示等功能，支持多种语言（Java、SQL、JavaScript等）。
- 10.封装完善的用户、角色、菜单、组织机构、数据字典、在线定时任务等基础功能，支持访问授权、按钮权限、数据权限等功能。
- 11.前端UI提供丰富的组件库，支持各种常用组件，如表格、树形控件、下拉框、日期选择器等，满足各种复杂的业务需求 [UI组件库文档](https://help.jeecg.com/category/ui%E7%BB%84%E4%BB%B6%E5%BA%93)。
- 12.提供APP配套框架，一份多代码多终端适配，一份代码多终端适配，小程序、H5、安卓、iOS、鸿蒙Next。
- 13.新版APP框架采用Uniapp、Vue3.0、Vite、Wot-design-uni、TypeScript等最新技术栈，包括二次封装组件、路由拦截、请求拦截等功能。实现了与JeecgBoot完美对接：目前已经实现登录、用户信息、通讯录、公告、移动首页、九宫格、聊天、Online表单、仪表盘等功能，提供了丰富的组件。
- 14.提供了一套成熟的AI应用平台功能，从AI模型、知识库到AI应用搭建，助力企业快速落地AI服务，加速智能化升级。
- 15.AI能力：目前JeecgBoot支持AI大模型chatgpt和deepseek，现在最新版默认使用deepseek，速度更快质量更高。目前提供了AI对话助手、AI知识库、AI应用、AI建表、AI报表等功能。
- 16.提供新行编辑表格JVXETable，轻松满足各种复杂ERP布局，拥有更高的性能、更灵活的扩展、更强大的功能。
- 17.平台首页风格，提供多种组合模式，支持自定义风格；支持门户设计，支持自定义首页。
- 18.常用共通封装，各种工具类（定时任务、短信接口、邮件发送、Excel导入导出等），基本满足80%项目需求。
- 19.简易Excel导入导出，支持单表导出和一对多表模式导出，生成的代码自带导入导出功能。
- 20.集成智能报表工具，报表打印、图像报表和数据导出非常方便，可极其方便地生成PDF、Excel、Word等报表。
- 21.采用前后分离技术，页面UI风格精美，针对常用组件做了封装：时间、行表格控件、截取显示控件、报表组件、编辑器等。
- 22.查询过滤器：查询功能自动生成，后台动态拼SQL追加查询条件；支持多种匹配方式（全匹配/模糊查询/包含查询/不匹配查询）。
- 23.数据权限（精细化数据权限控制，控制到行级、列表级、表单字段级，实现不同人看不同数据，不同人对同一个页面操作不同字段）。
- 24.接口安全机制，可细化控制接口授权，非常简便实现不同客户端只看自己数据等控制；也提供了基于AK和SK认证鉴权的OpenAPI功能。
- 25.活跃的社区支持；近年来，随着网络威胁的日益增加，团队在安全和漏洞管理方面积累了丰富的经验，能够为企业提供全面的安全解决方案。
- 26.权限控制采用RBAC（Role-Based Access Control，基于角色的访问控制）。
- 27.页面校验自动生成（必须输入、数字校验、金额校验、时间空间等）。
- 28.支持SaaS服务模式，提供SaaS多租户架构方案。
- 29.分布式文件服务，集成MinIO、阿里OSS等优秀的第三方，提供便捷的文件上传与管理，同时也支持本地存储。
- 30.主流数据库兼容，一套代码完全兼容MySQL、PostgreSQL、Oracle、SQL Server、MariaDB、达梦、人大金仓等主流数据库。
- 31.集成工作流Flowable，并实现了只需在页面配置流程转向，可极大简化BPM工作流的开发；用BPM的流程设计器画出了流程走向，一个工作流基本就完成了，只需写很少量的Java代码。
- 32.低代码能力：在线流程设计，采用开源Flowable流程引擎，实现在线画流程、自定义表单、表单挂靠、业务流转。
- 33.多数据源：极其简易的使用方式，在线配置数据源配置，便捷地从其他数据抓取数据。
- 34.提供单点登录CAS集成方案，项目中已经提供完善的对接代码。
- 35.低代码能力：表单设计器，支持用户自定义表单布局，支持单表、一对多表单，支持select、radio、checkbox、textarea、date、popup、列表、宏等控件。
- 36.专业接口对接机制，统一采用RESTful接口方式，集成Swagger-UI在线接口文档，JWT token安全验证，方便客户端对接。
- 37.高级组合查询功能，在线配置支持主子表关联查询，可保存查询历史。
- 38.提供各种系统监控，实时跟踪系统运行情况（监控Redis、Tomcat、JVM、服务器信息、请求追踪、SQL监控）。
- 39.消息中心（支持短信、邮件、微信推送等）；集成WebSocket消息通知机制。
- 40.支持多语言，提供国际化方案。
- 41.数据变更记录日志，可记录数据每次变更内容，通过版本对比功能查看历史变化。
- 42.提供简单易用的打印插件，支持谷歌、火狐、IE11+等各种浏览器。
- 43.后端采用Maven分模块开发方式；前端支持菜单动态路由。
- 44.提供丰富的示例代码，涵盖了常用的业务场景，便于学习和参考。


技术架构：
-----------------------------------

#### 后端

- IDE建议： IDEA (必须安装lombok插件 )
- 语言：Java 默认jdk17(jdk21、jdk24)
- 依赖管理：Maven
- 基础框架：Spring Boot 3.5.5
- 微服务框架： Spring Cloud Alibaba 2023.0.3.3
- 持久层框架：MybatisPlus 3.5.12
- 报表工具： JimuReport 2.1.3
- 安全框架：Apache Shiro 2.0.4，Jwt 4.5.0
- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel、Skywalking
- 数据库连接池：阿里巴巴Druid 1.2.24
- AI大模型：支持 `ChatGPT` `DeepSeek` `千问`等各种常规模式
- 日志打印：logback
- 缓存：Redis
- 其他：autopoi, fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。
- 默认提供MySQL5.7+数据库脚本
- [其他数据库，需要自己转](https://my.oschina.net/jeecg/blog/4905722)


#### 前端

- 前端环境要求：Node.js要求`Node 20+` 版本以上、pnpm 要求`9+` 版本以上
  ` ( Vite 不再支持已结束生命周期（EOL）的 Node.js 18。现在需要使用 Node.js 20.19+ 或 22.12+)`

- 依赖管理：node、npm、pnpm
- 前端IDE建议：IDEA、WebStorm、Vscode
- 采用 Vue3.0+TypeScript+Vite6+Ant-Design-Vue4等新技术方案，包括二次封装组件、utils、hooks、动态菜单、权限校验、按钮级别权限控制等功能
- 最新技术栈：Vue3.0 + TypeScript + Vite6 + ant-design-vue4 + pinia + echarts + unocss + vxe-table + qiankun + es6




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
|   TiDB   |  √   |



 
## 微服务解决方案


- 1、服务注册和发现 Nacos √
- 2、统一配置中心 Nacos  √
- 3、路由网关 gateway(三种加载方式) √
- 4、分布式 http feign √
- 5、熔断降级限流 Sentinel √
- 6、分布式文件 Minio、阿里OSS √ 
- 7、统一权限控制 JWT + Shiro √
- 8、服务监控 SpringBootAdmin√
- 9、链路跟踪 Skywalking   [参考文档](https://help.jeecg.com/java/springcloud/super/skywarking)
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





