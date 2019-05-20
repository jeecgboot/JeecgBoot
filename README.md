Jeecg-Boot 快速开发平台(前后端分离版本)
===============

当前最新版本： 2.0.0（发布日期：20190520）

项目介绍：
-----------------------------------
Jeecg-boot 是一款基于代码生成器的智能开发平台! 采用前后端分离技术:SpringBoot，Mybatis-plus，Shiro，JWT，Vue & Ant Design。提供强大的代码生成器，
前端页面代码和后端代码一键生成，不需要写任何代码，保持jeecg一贯的强大，绝对是全栈开发者福音！！
JeecgBoot的宗旨是降低前后端分离的开发成本，提高UI能力的同时，提高开发效率，追求更高的能力，No代码概念，一系列智能化在线开发。

JEECG宗旨是: 简单功能由Online Coding配置实现，NO代码；复杂功能由代码生成器生成进行手工Merge; 复杂业务流程采用表单自定义，业务流程使用工作流来实现、扩展出任务接口，供开发编写业务逻辑。实现了流程任务节点和任务接口的灵活配置，既保证了公司流程的保密性，又减少了开发人员的工作量。


适用项目
-----------------------------------
Jeecg-Boot快速开发平台，可以应用在任何J2EE项目的开发中，尤其适合企业信息管理系统（MIS）、内部办公系统（OA）、企业资源计划系统（ERP）、客户关系管理系统（CRM）等，其半智能手工Merge的开发方式，可以显著提高开发效率70%以上，极大降低开发成本。


为什么选择JEECG-BOOT?
-----------------------------------
* 1.采用最新主流前后分离框架（Springboot+Mybatis+antd），容易上手; 代码生成器依赖性低,灵活的扩展能力，可灵活实现二次开发;
* 2.开发效率很高,采用代码生成器，单表数据模型和一对多(父子表)数据模型，增删改查功能自动生成，菜单配置直接使用；
* 3.代码生成器提供强大模板机制，支持自定义模板风格。目前提供四套风格模板（单表两套、一对多两套）
* 4.封装完善的用户、角色、菜单、组织机构、数据字典、在线定时任务等基础功能，支持访问授权、按钮权限、数据权限等功能
* 5.常用共通封装，各种工具类(定时任务,短信接口,邮件发送,Excel导入导出等),基本满足80%项目需求
* 6.简易Excel导入导出，支持单表导出和一对多表模式导出，生成的代码自带导入导出功能
* 7.集成简易报表工具，图像报表和数据导出非常方便，可极其方便的生成图形报表、pdf、excel、word等报表；
* 8.采用前后分离技术，页面UI风格精美，针对常用组件做了封装：时间、行表格控件、截取显示控件、报表组件，编辑器等等
* 9.查询过滤器：查询功能自动生成，后台动态拼SQL追加查询条件；支持多种匹配方式（全匹配/模糊查询/包含查询/不匹配查询）；
* 10.数据权限（精细化数据权限控制，控制到行级，列表级，表单字段级，实现不同人看不同数据，不同人对同一个页面操作不同字段
* 11.在线配置报表（无需编码，通过在线配置方式，实现曲线图，柱状图，数据等报表）
* 12.页面校验自动生成(必须输入、数字校验、金额校验、时间空间等);
* 13.集成工作流activiti，并实现了只需在页面配置流程转向，可极大的简化bpm工作流的开发；用bpm的流程设计器画出了流程走向，一个工作流基本就完成了，只需写很少量的java代码；
* 14.在线流程设计，采用开源Activiti流程引擎，实现在线画流程,自定义表单,表单挂靠,业务流转
* 15.多数据源：及其简易的使用方式，在线配置数据源配置，便捷的从其他数据抓取数据；
* 16.国际化：支持多语言，开发国际化项目非常方便；
* 17.自定义表单，支持用户自定义表单布局，支持单表，一对多表单、支持select、radio、checkbox、textarea、date、popup、列表、宏等控件
* 18.专业接口对接机制，统一采用restful接口方式，集成swagger-ui在线接口文档，Jwt token安全验证，方便客户端对接
* 19.接口安全机制，可细化控制接口授权，非常简便实现不同客户端只看自己数据等控制
* 20.高级组合查询功能，在线配置支持主子表关联查询，可保存查询历史
* 21.提供各种系统监控，实时跟踪系统运行情况（监控 Redis、Tomcat、jvm、服务器信息、请求追踪、SQL监控）
* 22.消息中心（支持短信、邮件、微信推送等等）
* 22.数据变更记录日志，可记录数据每次变更内容，通过版本对比功能查看历史变化
* 23.平台UI强大，实现了移动自适应
* 24.平台首页风格，提供多种组合模式，支持自定义风格
* 25.提供简单易用的打印插件，支持谷歌、IE浏览器等各种浏览器
* 26.示例代码丰富，提供很多学习案例参考


技术架构：
-----------------------------------

#### 后端
- 基础框架：Spring Boot 2.1.3.RELEASE

- 持久层框架：Mybatis-plus_3.0.6

- 安全框架：Apache Shiro 1.4.0，Jwt_3.7.0

- 数据库连接池：阿里巴巴Druid 1.1.10

- 缓存框架：redis

- 日志打印：logback

- 其他：fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。


#### 前端
 
- [Vue 2.5.22](https://cn.vuejs.org/),[Vuex](https://vuex.vuejs.org/zh/),[Vue Router](https://router.vuejs.org/zh/)
- [Axios](https://github.com/axios/axios)
- [ant-design-vue](https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce-cn/)
- [webpack](https://www.webpackjs.com/),[yarn](https://yarnpkg.com/zh-Hans/)
- [vue-cropper](https://github.com/xyxiao001/vue-cropper) - 头像裁剪组件
- [@antv/g2](https://antv.alipay.com/zh-cn/index.html) - Alipay AntV 数据可视化图表
- [Viser-vue](https://viserjs.github.io/docs.html#/viser/guide/installation)  - antv/g2 封装实现
- eslint，[@vue/cli 3.2.1](https://cli.vuejs.org/zh/guide)
- vue-print-nb - 打印

#### 开发环境

- 语言：Java 8

- IDE(JAVA)： Eclipse安装lombok插件 或者 IDEA

- IDE(前端)： WebStorm 或者 IDEA

- 依赖管理：Maven

- 数据库：MySQL5.0  &  Oracle 11g & Sqlserver2005

- 缓存：Redis



#### 技术文档


- 在线演示 ：  [http://boot.jeecg.org](http://boot.jeecg.org)

- 官方文档 ：  [http://jeecg-boot.mydoc.io](http://jeecg-boot.mydoc.io)

- 入门必看：  [http://jeecg-boot.mydoc.io/?t=344845](http://jeecg-boot.mydoc.io/?t=344845)

- QQ交流群 ：  284271917

- 视频教程（视频有点老，以文档为准） ：  https://pan.baidu.com/s/1Il0TS50I70vH1AG1y40wtw 提取码：hok5

- Angular版本 ：[如果你更熟悉Angular，请点击这里](https://gitee.com/dangzhenghui/jeecg-boot)
 

### 功能模块
```
├─系统管理
│  ├─用户管理
│  ├─角色管理
│  ├─菜单管理
│  ├─权限设置（支持按钮权限、数据权限）
│  ├─表单权限（控制字段禁用、隐藏）
│  ├─部门管理
│  └─字典管理
├─智能化功能
│  ├─代码生成器功能（一键生成前后端代码，生成后无需修改直接用，绝对是后端开发福音）
│  ├─代码生成器模板（提供4套模板，分别支持单表和一对多模型，不同风格选择）
│  ├─代码生成器模板（生成代码，自带excel导入导出）
│  ├─查询过滤器（查询逻辑无需编码，系统根据页面配置自动生成）
│  ├─高级查询器（弹窗自动组合查询条件）
│  ├─Excel导入导出工具集成（支持单表，一对多 导入导出）
│  ├─平台移动自适应支持
├─Online在线开发(暂未开源)
│  ├─Online在线表单
│  ├─Online在线图表
│  ├─Online图表模板配置
│  ├─Online在线报表
├─系统监控
│  ├─性能扫描监控
│  │  ├─监控 Redis
│  │  ├─Tomcat
│  │  ├─jvm
│  │  ├─服务器信息
│  │  ├─请求追踪
│  │  ├─磁盘监控
│  ├─定时任务
│  ├─系统日志
│  ├─消息中心（支持短信、邮件、微信推送等等）
│  ├─数据日志（记录数据快照，可对比快照，查看数据变更情况）
│  ├─系统通知
│  ├─SQL监控
│  ├─swagger-ui(在线接口文档)
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
│─常用示例
│  ├─单表模型例子
│  └─一对多模型例子
│  └─打印例子
│  └─一对多TAB例子
│  └─内嵌table例子
│  └─常用选择组件
│  └─异步树table
│  └─接口模拟测试
│  └─一对多JEditable
│  └─图片拖拽排序
│  └─图片翻页
│  └─图片预览
│  └─PDF预览
│  └─分屏功能
│─封装通用组件	
│  ├─行编辑表格JEditableTable
│  └─省略显示组件
│  └─时间控件
│  └─高级查询
│  └─通用选择用户组件
│  └─通过组织机构选择用户组件
│  └─报表组件封装
│  └─字典组件
│  └─下拉多选组件
│  └─选人组件
│  └─选部门组件
│  └─通过部门选人组件
│  └─封装曲线、柱状图、饼状图、折线图等等报表的组件（经过封装，使用简单）
│  └─在线code编辑器
│  └─上传文件组件
│  └─等等
│─更多页面模板
│  ├─各种高级表单
│  ├─各种列表效果
│  └─结果页面
│  └─异常页面
│  └─个人页面
│─流程模块功能 (暂未开源)
│  ├─在线流程设计
│  ├─在线表单设计
│  └─我的任务
│  └─历史流程
│  └─历史流程
│  └─流程实例管理
│  └─流程监听管理
│  └─流程表达式
│  └─我发起的流程
│  └─我的抄送
│  └─流程委派、抄送、跳转
│  └─。。。
└─其他模块
   └─更多功能开发中。。
   
```
   
   

系统效果
----

![输入图片说明](https://static.oschina.net/uploads/img/201902/25154007_icdX.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14155402_AmlV.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160623_8fwk.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160633_u59G.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160643_kCJ7.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160650_fcgw.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160657_cHwb.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160705_NAJn.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160751_bsO9.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160801_2AhS.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160813_KmXS.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160828_pkFr.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160834_Lo23.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160842_QK7B.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160849_GBm5.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160858_6RAM.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160905_RGJ5.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160917_9Ftz.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160926_PUDV.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160935_Nibs.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160947_gfoN.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14160957_hN3X.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14161004_bxQ4.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201904/14161013_zW5n.png "在这里输入图片标题")




后台开发环境和依赖
----
- java
- maven
- jdk8
- mysql
- redis
- 数据库脚步：jeecg-boot\docs\jeecg-boot-mysql.sql
- 默认登录账号： admin/123456


前端开发环境和依赖
----
- node
- yarn
- webpack
- eslint
- @vue/cli 3.2.1
- [ant-design-vue](https://github.com/vueComponent/ant-design-vue) - Ant Design Of Vue 实现
- [vue-cropper](https://github.com/xyxiao001/vue-cropper) - 头像裁剪组件
- [@antv/g2](https://antv.alipay.com/zh-cn/index.html) - Alipay AntV 数据可视化图表
- [Viser-vue](https://viserjs.github.io/docs.html#/viser/guide/installation)  - antv/g2 封装实现



项目下载和运行
----

- 拉取项目代码
```bash
git clone https://github.com/zhangdaiscott/jeecg-boot.git
cd  jeecg-boot/ant-design-jeecg-vue
```

1. 安装node.js
2. 切换到ant-design-jeecg-vue文件夹下
```
# 安装yarn
npm install -g yarn

# 下载依赖
yarn install

# 启动
yarn run serve

# 编译项目
yarn run build

# Lints and fixes files
yarn run lint
```



其他说明
----

- 项目使用的 [vue-cli3](https://cli.vuejs.org/guide/), 请更新您的 cli

- 关闭 Eslint (不推荐) 移除 `package.json` 中 `eslintConfig` 整个节点代码

- 修改 Ant Design 配色，在文件 `vue.config.js` 中，其他 less 变量覆盖参考 [ant design](https://ant.design/docs/react/customize-theme-cn) 官方说明
```ecmascript 6
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */

          'primary-color': '#F5222D',
          'link-color': '#F5222D',
          'border-radius-base': '4px',
        },
        javascriptEnabled: true,
      }
    }
  }
```



附属文档
----
- [Ant Design Vue](https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce-cn)

- [报表 viser-vue](https://viserjs.github.io/demo.html#/viser/bar/basic-bar)

- [Vue](https://cn.vuejs.org/v2/guide)

- [路由/菜单说明](https://github.com/zhangdaiscott/jeecg-boot/tree/master/ant-design-jeecg-vue/src/router/README.md)

- [ANTD 默认配置项](https://github.com/zhangdaiscott/jeecg-boot/tree/master/ant-design-jeecg-vue/src/defaultSettings.js)

- 其他待补充...


备注
----

> @vue/cli 升级后，eslint 规则更新了。由于影响到全部 .vue 文件，需要逐个验证。既暂时关闭部分原本不验证的规则，后期维护时，在逐步修正这些 rules


## 捐赠 

如果觉得还不错，请作者喝杯咖啡吧 ☺

![](https://static.oschina.net/uploads/img/201903/08155608_0EFX.png)