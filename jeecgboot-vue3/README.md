JeecgBoot 企业级低代码开发平台
===============
当前最新版本： 3.7.0（发布时间：2024-06-17）

[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zhangdaiscott/jeecg-boot/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-北京国炬软件-orange.svg)](http://jeecg.com/aboutusIndex)
[![](https://img.shields.io/badge/Blog-官方博客-blue.svg)](https://jeecg.blog.csdn.net)
[![](https://img.shields.io/badge/version-3.7.0-brightgreen.svg)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub stars](https://img.shields.io/github/stars/zhangdaiscott/jeecg-boot.svg?style=social&label=Stars)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub forks](https://img.shields.io/github/forks/zhangdaiscott/jeecg-boot.svg?style=social&label=Fork)](https://github.com/zhangdaiscott/jeecg-boot)



## 简介
JeecgBoot-Vue3采用 Vue3.0、Vite、 Ant-Design-Vue4、TypeScript 等新技术方案，包括二次封装组件、utils、hooks、动态菜单、权限校验、按钮级别权限控制等功能。
 
> 强大的代码生成器让前后端代码一键生成! JeecgBoot引领低代码开发模式(OnlineCoding-> 代码生成-> 手工MERGE)， 帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省成本，同时又不失灵活性

## 技术支持

使用中遇到问题或BUG可以在主项目的 [Github上提Issues](https://github.com/jeecgboot/JeecgBoot/issues/new)

##### 源码下载

- JAVA后台源码：https://github.com/jeecgboot/JeecgBoot


##### 项目说明

| 项目名                | 说明                                      | 
|--------------------|-----------------------------------------|
| `jeecgboot-vue3` | 前端源码Vue3                     | 
| `jeecg-boot`     | 后端源码JAVA（SpringBoot+SpringCloud） | 


## 开发环境搭建

- [前端开发环境准备](https://help.jeecg.com/setup/dev.html)
- [前端项目快速启动](https://help.jeecg.com/setup/startup.html)
- [通过IDEA启动项目](https://help.jeecg.com/java/setup/idea/startup.html)

## 技术文档

-   官方文档：[https://help.jeecg.com](https://help.jeecg.com)
-   快速入门：[快速入门](http://jeecg.com/doc/quickstart) | [常见问题](http://help.jeecg.com/qa.html) | [视频教程](https://www.bilibili.com/video/BV1V34y187Y9 "入门视频") |  [ 代码生成](http://help.jeecg.com/vue3/codegen/online.html)
-   QQ交流群：⑨808791225、⑧825232878(满)、⑦791696430(满)、683903138(满)、其他满
-   在线演示 ：  [Vue3演示](http://boot3.jeecg.com)   | [APP演示](http://jeecg.com/appIndex)
> 演示系统的登录账号密码，请点击 [获取账号密码](http://jeecg.com/doc/demo) 获取


## 安装与使用


 > 环境要求: 版本要求Node 14.18+ / 16+ 版本以上，不再支持 Node 12 / 13 / 15。
 > 建议使用pnpm，如果使用yarn,请用Yarn1.x版本，否则依赖可能安装不上。

  
- Get the project code

```bash
git clone https://github.com/jeecgboot/jeecgboot-vue3.git
```

- Installation dependencies

```bash
cd jeecgboot-vue3

pnpm install
```

- 配置接口地址 `.env.development`

```bash
VITE_PROXY = [["/jeecgboot","http://localhost:8080/jeecg-boot"],["/upload","http://localhost:3300/upload"]]
VITE_GLOB_DOMAIN_URL=http://localhost:8080/jeecg-boot
```

> 说明：把`http://localhost:8080/jeecg-boot` 换成自己地址，其他不用改。


- run

```bash
pnpm dev
```


- build

```bash
pnpm build
```


## Docker镜像启动前端(单体模式)

- host设置

>注意： 需要把`127.0.0.1`替换成真实IP 比如`192.`开头,不然后端不通。

```bash
127.0.0.1 jeecg-boot-system
127.0.0.1 jeecg-boot-gateway
```


- 下载项目

```bash
git clone https://github.com/jeecgboot/jeecgboot-vue3.git

cd jeecgboot-vue3
```

- 配置接口域名 `.env.production`

```bash
VITE_GLOB_API_URL=/jeecgboot
VITE_GLOB_DOMAIN_URL=http://jeecg-boot-system:8080/jeecg-boot
```
后台单体启动 [见此文档](https://help.jeecg.com/java/setup/docker/up.html)

- 编译项目

```bash
pnpm install

pnpm build
```

- 启动容器
```bash
docker build -t jeecgboot-vue3 .
docker run --name jeecgboot-vue3-nginx -p 80:80 -d jeecgboot-vue3
```

- 访问前台

http://localhost

## Docker镜像启动前端(微服务模式)
> 这里只写与单体的区别步骤

-  区别1. 修改后台域名
.env.production

```bash
VITE_GLOB_API_URL=/jeecgboot
VITE_GLOB_DOMAIN_URL=http://jeecg-boot-gateway:9999
```

后台微服务启动 [见此文档](https://help.jeecg.com/java/springcloud/docker.html)

- 区别2. 修改Dockerfile文件

```bash
- 把`http://jeecg-boot-system:8080/jeecg-boot`替换成 `http://jeecg-boot-gateway:9999`
- 把`jeecg-boot-system`替换成 `jeecg-boot-gateway`
```

-  其他与单体模式一样

```bash
镜像需要重现构建，最好把单体的镜像删掉，重新构建docker镜像。
```







## 入门必备

本项目需要一定前端基础知识，请确保掌握 Vue 的基础知识，以便能处理一些常见的问题。 建议在开发前先学一下以下内容，提前了解和学习这些知识，会对项目理解非常有帮助:

*   [JeecgBoot-Vue3文档](http://help.jeecg.com)
*   [Vue3 文档](https://cn.vuejs.org/)
*   [Vben文档](https://doc.vvbin.cn)
*   [Ant-Design-Vue](https://www.antdv.com/docs/vue/introduce-cn/)
*   [TypeScript](https://www.typescriptlang.org/)
*   [Vue-router](https://router.vuejs.org/zh)
*   [Es6](https://es6.ruanyifeng.com/)
*   [Vitejs](https://cn.vitejs.dev/guide/)
*   [Pinia(vuex替代方案)](https://pinia.esm.dev/introduction.html)
*   [Vue-RFCS](https://github.com/vuejs/rfcs)
*   [Vue2 迁移到 3](https://v3.vuejs.org/guide/migration/introduction.html)
*   [vxetable文档](https://vxetable.cn)
*   [~~WindiCss~~](https://windicss.netlify.app/)


##   浏览器支持

**本地开发**推荐使用`Chrome 最新版`浏览器，**不支持**`Chrome 90`以下版本。

**生产环境**支持现代浏览器，不支持 IE。

| [![IE](https://raw.githubusercontent.com/alrra/browser-logos/master/src/archive/internet-explorer_9-11/internet-explorer_9-11_48x48.png)](http://godban.github.io/browsers-support-badges/)IE | [![ Edge](https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png)](http://godban.github.io/browsers-support-badges/)Edge | [![Firefox](https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png)](http://godban.github.io/browsers-support-badges/)Firefox | [![Chrome](https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png)](http://godban.github.io/browsers-support-badges/)Chrome | [![Safari](https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png)](http://godban.github.io/browsers-support-badges/)Safari |
| --- | --- | --- | --- | --- |
| not support | last 2 versions | last 2 versions | last 2 versions | last 2 versions |



## 功能模块
> vue3版本已经实现了系统管理、系统监控、报表、各种组件、前端权限、GUI代码生成、Online表单、Online报表等平台功能，完全可以用于生产环境。

```
├─首页
│  ├─首页（四套首页满足不同场景需求）
│  ├─工作台
├─系统管理
│  ├─用户管理
│  ├─角色管理
│  ├─菜单管理
│  ├─权限设置（支持按钮权限、数据权限）
│  ├─表单权限（控制字段禁用、隐藏）
│  ├─部门管理
│  ├─我的部门（二级管理员）
│  └─字典管理
│  └─分类字典
│  └─系统公告
│  └─职务管理
│  └─通讯录
│  └─对象存储
│  └─多租户管理
├─系统监控
│  ├─网关路由配置（gateway）
│  ├─定时任务
│  ├─数据源管理
│  ├─系统日志
│  ├─消息中心（支持短信、邮件、微信推送等等）
│  ├─数据日志（记录数据快照，可对比快照，查看数据变更情况）
│  ├─系统通知
│  ├─SQL监控
│  ├─性能监控
│  │  ├─监控 Redis
│  │  ├─Tomcat
│  │  ├─jvm
│  │  ├─服务器信息
│  │  ├─请求追踪
│  │  ├─磁盘监控
├─消息中心
│  ├─我的消息
│  ├─消息管理
│  ├─模板管理
├─积木报表设计器
│─报表示例
│  ├─曲线图
│  └─饼状图
│  └─柱状图
│  └─折线图
│  └─面积图
│  └─雷达图
│  └─仪表图
│  └─进度条
│  └─排名列表
│  └─等等
│─大屏模板
│  ├─作战指挥中心大屏
│  └─物流服务中心大屏
├─代码生成器（GUI）
│  ├─代码生成器功能（一键生成前后端代码，生成后无需修改直接用，绝对是后端开发福音）
│  ├─代码生成器模板（提供4套模板，分别支持单表和一对多模型，不同风格选择）
│  ├─代码生成器模板（生成代码，自带excel导入导出）
│  ├─查询过滤器（查询逻辑无需编码，系统根据页面配置自动生成）
│  ├─高级查询器（弹窗自动组合查询条件）
│  ├─Excel导入导出工具集成（支持单表，一对多 导入导出）
│  ├─平台移动自适应支持
│─常用示例
│  ├─自定义组件示例
│  ├─JVxeTable示例(ERP行业复杂排版效果)
│  ├─单表模型例子
│  └─一对多模型例子
│  └─打印例子
│  └─一对多内嵌示例
│  └─异步树Table
│  └─图片拖拽排序
│  └─图片翻页
│  └─图片预览
│  └─PDF预览
│─封装通用组件 
│  ├─行编辑表格JVxeTable
│  └─省略显示组件
│  └─时间控件
│  └─高级查询 (未实现)
│  └─用户选择组件
│  └─报表组件封装
│  └─字典组件
│  └─下拉多选组件
│  └─选人组件
│  └─选部门组件
│  └─通过部门选人组件
│  └─封装曲线、柱状图、饼状图、折线图等等报表的组件（经过封装，使用简单）
│  └─在线code编辑器
│  └─上传文件组件
│  └─树列表组件
│  └─表单禁用组件
│  └─等等
│─更多页面模板
│  └─Mock示例（子菜单很多）
│  └─页面&导航（子菜单很多）
│  └─组件&功能（子菜单很多）
├─高级功能
│  ├─支持微前端
│  ├─提供CAS单点登录
│  ├─集成Websocket消息通知机制
│  ├─支持第三方登录（QQ、钉钉、微信等）
│  ├─系统编码规则
├─Online在线开发(低代码)
│  ├─Online在线表单 - 功能已开放
│  ├─Online代码生成器 - 功能已开放
│  ├─Online在线报表 - 功能已开放
│  ├─Online在线图表(暂未开源)
│  ├─多数据源管理
│─流程模块功能 (暂未开源)
│  ├─流程设计器
│  ├─表单设计器
│  ├─大屏设计器
│  ├─门户设计/仪表盘设计器
│  └─我的任务
│  └─历史流程
│  └─历史流程
│  └─流程实例管理
│  └─流程监听管理
│  └─流程表达式
│  └─我发起的流程
│  └─我的抄送
│  └─流程委派、抄送、跳转
│  └─OA办公组件
└─其他模块 
   └─更多功能开发中。。 
   
```


##   系统效果
系统后台

![](https://oscimg.oschina.net/oscnet/up-000530d95df337b43089ac77e562494f454.png)

![输入图片说明](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/site/vue3_20220310142354.png "在这里输入图片标题")


![输入图片说明](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/site/vue3_20220310142409.png "在这里输入图片标题")

![输入图片说明](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/site/vue3_20220310142401.png "在这里输入图片标题")

![输入图片说明](https://jeecgos.oss-cn-beijing.aliyuncs.com/files/site/vue3_11.png "在这里输入图片标题")

Online开发&代码生成
![](https://oscimg.oschina.net/oscnet/up-e8862f2c3c14eace9090c20a8fb928234a4.png)

![](https://oscimg.oschina.net/oscnet/up-e3b3a736236bc66f255a9a32ab3f9b7196b.png)

![](https://oscimg.oschina.net/oscnet/up-221b8cbdea3c17d31a1365023a73d3d439f.png)

![](https://oscimg.oschina.net/oscnet/up-14092f6f213b26ab145cf70b2dc6dec5635.png)




系统交互
![](https://oscimg.oschina.net/oscnet/up-78b151fc888d4319377bf1cc311fe826871.png)

![](https://oscimg.oschina.net/oscnet/up-16c07e000278329b69b228ae3189814b8e9.png)


流程设计
![](https://oscimg.oschina.net/oscnet/up-981ce174e4fbb48c8a2ce4ccfd7372e2994.png)

![输入图片说明](https://static.oschina.net/uploads/img/201907/05165142_yyQ7.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201904/14160917_9Ftz.png "在这里输入图片标题")

![输入图片说明](https://static.oschina.net/uploads/img/201904/14160633_u59G.png "在这里输入图片标题")

简版流程设计

![](https://oscimg.oschina.net/oscnet/up-1dc0d052149ec675f3e4fad632b82b48add.png)

![](https://oscimg.oschina.net/oscnet/up-de31bc2f9d9b8332c554b0954cc73d79593.png)

![](https://oscimg.oschina.net/oscnet/up-7f83b25159663686d67ed080eb16068c3b4.png)

仪表盘设计器
![](https://oscimg.oschina.net/oscnet/up-9c9d41288c31398d76b390bdd400f13a582.png)

![](https://oscimg.oschina.net/oscnet/up-fad98d42b2cf92f92a903c9cff7579f18ec.png)

报表设计器
![](https://oscimg.oschina.net/oscnet/up-64648de000851f15f6c7b9573d107ebb5f8.png)

![](https://oscimg.oschina.net/oscnet/up-fa52b44445db281c51d3f267dce7450d21b.gif)

![](https://oscimg.oschina.net/oscnet/up-68a19149d640f1646c8ed89ed4375e3326c.png)

![](https://oscimg.oschina.net/oscnet/up-f7e9cb2e3740f2d19ff63b40ec2dd554f96.png)

表单设计器
![](https://oscimg.oschina.net/oscnet/up-5f8cb657615714b02190b355e59f60c5937.png)

![](https://oscimg.oschina.net/oscnet/up-d9659b2f324e33218476ec98c9b400e6508.png)

![](https://oscimg.oschina.net/oscnet/up-4868615395272d3206dbb960ade02dbc291.png)

大屏设计器
![](https://oscimg.oschina.net/oscnet/up-402a6034124474bfef8dfc5b4b2bac1ce5c.png)

![](https://oscimg.oschina.net/oscnet/up-6f7ba2e2ebbeea0d203db8d69fd87644c9f.png)

![](https://oscimg.oschina.net/oscnet/up-ee8d34f318da466b8a6070a6e3111d12ce7.png)

![](https://oscimg.oschina.net/oscnet/up-6b81781b43086819049c4421206810667c5.png)


报表效果

![](https://static.oschina.net/uploads/img/201904/14160828_pkFr.png "")

![](https://static.oschina.net/uploads/img/201904/14160834_Lo23.png "")

![](https://static.oschina.net/uploads/img/201904/14160842_QK7B.png "")

![](https://static.oschina.net/uploads/img/201904/14160849_GBm5.png "")

![](https://static.oschina.net/uploads/img/201904/14160858_6RAM.png "")

接口文档

![](https://oscimg.oschina.net/oscnet/up-e6ea09dbaa01b8458c2e23614077ba9507f.png)


手机端
![](https://oscimg.oschina.net/oscnet/da543c5d0d57baab0cecaa4670c8b68c521.jpg)
![](https://oscimg.oschina.net/oscnet/fda4bd82cab9d682de1c1fbf2060bf14fa6.jpg)

PAD端
![](https://oscimg.oschina.net/oscnet/e90fef970a8c33790ab03ffd6c4c7cec225.jpg)
![](https://oscimg.oschina.net/oscnet/d78218803a9e856a0aa82b45efc49849a0c.jpg)
![](https://oscimg.oschina.net/oscnet/59c23b230f52384e588ee16309b44fa20de.jpg)



