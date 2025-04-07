JeecgBoot 企业级低代码开发平台
===============
当前最新版本： 3.8.0（预计发布时间：2025-04-21）

[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/zhangdaiscott/jeecg-boot/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-北京国炬软件-orange.svg)](http://jeecg.com/aboutusIndex)
[![](https://img.shields.io/badge/version-3.8.0-brightgreen.svg)](https://github.com
/zhangdaiscott/jeecg-boot)
[![GitHub stars](https://img.shields.io/github/stars/zhangdaiscott/jeecg-boot.svg?style=social&label=Stars)](https://github.com/zhangdaiscott/jeecg-boot)
[![GitHub forks](https://img.shields.io/github/forks/zhangdaiscott/jeecg-boot.svg?style=social&label=Fork)](https://github.com/zhangdaiscott/jeecg-boot)



## 简介
JeecgBoot-Vue3采用 Vue3.0、Vite、 Ant-Design-Vue4、TypeScript 等新技术方案，包括二次封装组件、utils、hooks、动态菜单、权限校验、按钮级别权限控制等功能。
 
> 强大的代码生成器让前后端代码一键生成! JeecgBoot引领低代码开发模式(OnlineCoding-> 代码生成-> 手工MERGE)， 帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省成本，同时又不失灵活性


## 开发环境搭建

- [前端开发环境准备](https://help.jeecg.com/setup/dev)
- [前端项目快速启动](https://help.jeecg.com/setup/startup)
- [通过IDEA启动项目](https://help.jeecg.com/java/setup/idea/startup)

## 技术文档

-   官方文档：[https://help.jeecg.com](https://help.jeecg.com)
-   快速入门：[快速入门](http://jeecg.com/doc/quickstart) | [常见问题](http://help.jeecg.com/qa) 
-   QQ交流群：⑩716488839、⑨808791225、其他满
-   在线演示 ：  [系统演示](http://boot3.jeecg.com)   | [APP演示](http://jeecg.com/appIndex)
> 演示系统的登录账号密码，请点击 [获取账号密码](http://jeecg.com/doc/demo) 获取


## 安装与使用

*   本地环境安装 `Node.js 、npm 、pnpm`
*   Node.js 版本建议`v20.15.0`，要求`Node 20+` 版本以上

 ` ( 因为Vite5 不再支持已 EOL 的 Node.js 14 / 16 / 17 / 19，现在需要 Node.js 18 / 20+ )`


  
- Get the project code

```bash
git clone https://github.com/jeecgboot/JeecgBoot.git
```

- Installation dependencies

```bash
cd JeecgBoot/jeecgboot-vue3

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

## 入门必备

本项目需要一定前端基础知识，请确保掌握 Vue 的基础知识，以便能处理一些常见的问题。 建议在开发前先学一下以下内容，提前了解和学习这些知识，会对项目理解非常有帮助:

*   [JeecgBoot文档](http://help.jeecg.com)
*   [Vue3 文档](https://cn.vuejs.org/)
*   [Vben文档](https://doc.vvbin.cn)
*   [Ant-Design-Vue](https://www.antdv.com/docs/vue/introduce-cn/)
*   [TypeScript](https://www.typescriptlang.org/)
*   [Vue-router](https://router.vuejs.org/zh)
*   [Es6](https://es6.ruanyifeng.com/)
*   [Vitejs](https://cn.vitejs.dev/guide/)
*   [Pinia(vuex替代方案)](https://pinia.esm.dev/introduction.html)
*   [Vue-RFCS](https://github.com/vuejs/rfcs)
*   [vxetable文档](https://vxetable.cn)


##   浏览器支持

**本地开发**推荐使用`Chrome 最新版`浏览器，**不支持**`Chrome 90`以下版本。

**生产环境**支持现代浏览器，不支持 IE。

| [![IE](https://raw.githubusercontent.com/alrra/browser-logos/master/src/archive/internet-explorer_9-11/internet-explorer_9-11_48x48.png)](http://godban.github.io/browsers-support-badges/)IE | [![ Edge](https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png)](http://godban.github.io/browsers-support-badges/)Edge | [![Firefox](https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png)](http://godban.github.io/browsers-support-badges/)Firefox | [![Chrome](https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png)](http://godban.github.io/browsers-support-badges/)Chrome | [![Safari](https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png)](http://godban.github.io/browsers-support-badges/)Safari |
| --- | --- | --- | --- | --- |
| not support | last 2 versions | last 2 versions | last 2 versions | last 2 versions |
