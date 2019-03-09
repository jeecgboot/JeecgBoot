Jeecg-Boot 快速开发平台(前后端分离版本)
===============

当前最新版本： 1.0（发布日期：20190304）

项目介绍：
-----------------------------------
Jeecg-boot 是一款企业级快速开发平台!采用前后端分离技术:SpringBoot，Mybatis-plus，Shiro，JWT，Vue & Ant Design。提供强大的代码生成器，
前端页面代码和后端代码一键生成，不需要写任何代码，保持jeecg一贯的强大，绝对是全栈开发者福音！！
JeecgBoot的宗旨是降低前后端分离的开发成本，提高UI能力的同时，提高开发效率，追求更高的能力，No代码概念，一系列智能化在线开发。


技术架构：
-----------------------------------

#### 后端
- 基础框架：Spring Boot 2.0.3.RELEASE

- 持久层框架：Mybatis-plus_3.0.6

- 安全框架：Apache Shiro 1.4.0-RC2，Jwt_3.4.1

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


#### 开发环境

- 语言：Java 8

- IDE(JAVA)： Eclipse安装lombok插件 或者 IDEA

- IDE(前端)： WebStorm 或者 IDEA

- 依赖管理：Maven

- 数据库：MySQL5.0  &  Oracle 11g

- 缓存：Redis



#### 技术文档

- 在线演示 ：  [http://boot.jeecg.org](http://boot.jeecg.org)

- 官方文档 ：  [http://jeecg-boot.mydoc.io](http://jeecg-boot.mydoc.io)

- QQ交流群 ：  284271917

- 视频教程 ：  https://pan.baidu.com/s/1Il0TS50I70vH1AG1y40wtw 提取码：hok5
 
- 学习思路 ：  [跟着我们零基础学习前后端分离开发](http://jeecg-boot.mydoc.io/?t=340820)
 
- 常见问题 ：  [http://www.jeecg.org/forum.php?mod=viewthread&tid=7816&page=1&extra=#pid21237](http://www.jeecg.org/forum.php?mod=viewthread&tid=7816&page=1&extra=#pid21237)

### 功能模块
```
├─系统管理
│  ├─用户管理
│  ├─角色管理
│  ├─菜单管理（权限设置）
│  ├─部门管理
│  └─字典管理
├─智能化功能
│  ├─代码生成器功能（一键生成，包括前端页面也可以生成，绝对是后端开发福音）
├─系统监控
│  ├─定时任务
│  ├─系统日志
│  ├─系统通知
│  ├─SQL监控
│  ├─swagger-ui(在线接口文档)
│─常用示例
│  ├─单表模型例子
│  └─一对多模型例子
│  └─打印例子
│  └─一对多TAB例子
│─更多页面模板
│  ├─各种高级表单
│  ├─各种列表效果
│  └─结果页面
│  └─异常页面
│  └─个人页面
└─其他模块
   └─其他
   
```
   
   

系统效果
----
![输入图片说明](https://static.oschina.net/uploads/img/201902/25154007_icdX.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25153956_Q752.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201901/07154149_555Q.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25154209_qlCg.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25154251_XoW9.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25154331_0ndT.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25154414_ckFS.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25155155_Hm6H.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25155213_T04n.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25155224_MRLU.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25155234_7zCP.png "在这里输入图片标题")
![输入图片说明](https://static.oschina.net/uploads/img/201902/25155242_K7Sw.png "在这里输入图片标题")





后台开发环境和依赖
----
- java
- maven
- jdk8
- mysql
- redis
- 数据库脚步：jeecg-boot\docs\dbsys-init-20190225.sql
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
- vue-print-nb - 打印



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