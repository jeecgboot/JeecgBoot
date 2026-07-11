-- author:zhangdaiscott ---date:20260711--for: 前端构建工具升级到 Vite 8 ---

## 修改内容

- vite: ^7.3.3 → ^8.1.4
- vite-plugin-pwa: ^1.1.0 → ^1.3.0

## 解决的问题

1. 升级 Vite8 后 `vite.config.ts` 配置文件加载报错 —— 解决了 Rolldown 配置加载器无法解析 `/@/` 路径别名导致构建启动失败的问题。
2. 解决了 `manualChunks` 对象写法在 Rolldown 下不再支持、构建报错的问题。
3. 解决了 `SuperQuery.vue` 中 `@media` 语法缺失空格（`and(max-width...)`）被新默认 CSS 压缩器 Lightning CSS 判定为非法语法、导致构建失败的问题。

## 验证结果

- `pnpm build` / `pnpm build:docker` / `pnpm build:dockercloud` 均构建成功
- `pnpm dev` 正常启动，页面、mock 接口、模块热更新均正常
- 主题切换插件（浅色/暗色）、运行时动态换肤功能均验证正常，无写死配置

## 涉及文件

```
build/config/themeConfig.ts (调整为动态读取主题配置，避免写死)
src/settings/projectSetting.ts (别名导入改为相对路径)
src/utils/getConfigByMenuType.ts (别名导入改为相对路径)
vite.config.ts (manualChunks 改为函数写法)
src/views/super/online/cgform/auto/comp/superquery/SuperQuery.vue (修复 CSS 语法)
package.json (vite / vite-plugin-pwa 版本升级)
```

## 参考资料

- Vite 官方迁移指南（v7→v8）：https://cn.vite.dev/guide/migration
- Vite CHANGELOG：https://github.com/vitejs/vite/blob/main/packages/vite/CHANGELOG.md

## 依赖版本更新（稳妥模式，pnpm update）

在保持所有主版本号不变的前提下，用 `pnpm update` 把依赖升级到各自 semver 范围内的最新小版本/补丁版本（例如 vue 3.5.27→3.5.39、vue-router 4.5.1→4.6.4、axios 1.13.2→1.18.1、dayjs/qs/sortablejs 等补丁更新），未改动 pinia/vue-router/vue 的主版本。升级后 `pnpm build`、`pnpm dev` 均验证通过。

## 迁移说明要点（v7→v8）

Vite8 底层由 Rollup+esbuild 切换为 Rolldown+Oxc，核心变化：

- 默认浏览器 target 提升（Chrome 111 / Edge 111 / Firefox 114 / Safari 16.4）
- 依赖预构建（optimizeDeps）改用 Rolldown 实现
- JS 转译/压缩改用 Oxc（`esbuild` 配置项废弃但会自动转换生效）
- CSS 默认压缩器改为 Lightning CSS（比 esbuild 更严格，不容忍不规范写法）
- CommonJS 互操作规则调整（"Consistent CommonJS Interop"）—— 老 CJS 包的 default 导出解析方式变化，存在静默运行时风险
- 移除了基于 package.json `browser`/`module` 字段的"格式嗅探"，改为严格按 `resolve.mainFields` 顺序解析
- `build.rollupOptions.output.manualChunks` 对象写法移除，函数写法废弃（推荐迁移到 Rolldown 的 `codeSplitting`）
- 移除 `build.rollupOptions.watch.chokidar`
- `format: 'system'/'amd'`、部分 Rollup 钩子、legacy 插件（ES5 及以下）在 Rolldown 下不受支持

v6→v7 迁移内容项目此前已完成升级（升级 Vite8 前项目已在 v7.3.3），本次未发现遗留问题。

## 逐项核对本项目影响

| 迁移项 | 是否影响本项目 | 结论 |
|---|---|---|
| manualChunks 对象写法移除 | 是 | 已修复为函数写法 |
| Lightning CSS 更严格 | 是 | 修复 SuperQuery.vue 的 `and(max-width` 语法错误 |
| 配置文件别名解析（Rolldown 不认 `/@/`） | 是 | 别名改相对路径，恢复动态主题配置 |
| esbuild/oxc 选项冲突警告 | 否（仅警告） | 已核实 console/debugger 剥离功能正常，非功能性问题 |
| chokidar / customResolver / format amd,system | 否 | 项目未使用 |
| 自定义主题插件（theme-plugin）钩子兼容性 | 否 | 仅用标准钩子（transform/writeBundle/closeBundle等），已验证构建通过 |
| CJS 互操作变化 | 待观察 | crypto-js/md5/qs/mockjs/sortablejs 等已通过构建+dev联调验证可用，暂未发现异常 |
| 格式嗅探移除 | 待观察 | 静态分析+构建通过未发现问题，需长期使用中留意 |

-- author:zhangdaiscott ---date:20260711 ---

-- author:zhangdaiscott ---date:20260711--for: pinia 升级到 3.x、vue-router 升级到 5.x ---

## 修改内容

- pinia: 2.1.7 → ^3.0.4
- vue-router: ^4.6.4 → ^5.1.0
- vue / @vue/compiler-sfc 已在此前的补丁更新中满足 vue-router5 所需的 `^3.5.34` peer 依赖，无需再单独升级

## 解决的问题

1. 解决了 pinia 3 移除 `defineStore({ id: 'xxx', ... })` 单对象写法后，项目中 11 个 store 文件在运行时抛出 `Cannot destructure property 'state' of 'options' as it is undefined` 报错、导致应用启动白屏的问题。已全部改为 `defineStore('xxx', { ... })` 新写法。

## 验证结果

- `pnpm build` 构建成功
- `pnpm dev` + 无头浏览器（Playwright）实测：页面正常渲染登录页，无 pageerror/console.error
- 涉及的 11 个 store 文件：user.ts、permission.ts、multipleTab.ts、lock.ts、locale.ts、errorLog.ts、defIndex.ts、app.ts、cgform/enhance.ts、cgform/cgformState.ts、cgform/share/shareStore.ts

-- author:zhangdaiscott ---date:20260711 ---

-- author:zhangdaiscott ---date:20260711--for: 修复 vue-router 导航守卫 next() 回调废弃警告 ---

## 修改内容

将以下文件的导航守卫从 `next()` 回调风格改为返回值风格（vue-router 5 推荐写法，`next()` 回调已废弃）：

- src/router/index.ts
- src/router/guard/permissionGuard.ts
- src/router/guard/paramMenuGuard.ts
- src/views/super/online/cgform/share/route/index.ts（routerBeforeEach）

## 解决的问题

解决了浏览器控制台反复出现的 `[Vue Router warn]: The next() callback in navigation guards is deprecated. Return the value instead of calling next(value)` 警告。逐条改写时保持了原有分支逻辑不变（next(x) → return x；next() → return true；next(redirectData) → return redirectData）。

## 验证结果

- `pnpm build` 构建成功
- `pnpm dev` + 无头浏览器（Playwright）实测：登录页正常渲染，控制台无 next() 警告、无 pageerror/console.error

-- author:zhangdaiscott ---date:20260711 ---

-- author:zhangdaiscott ---date:20260711--for: 依赖版本变更总表（本次 Vite8 + pinia3/vue-router5 升级全过程汇总）---

## 主版本升级（breaking change）

| 依赖 | 升级前 | 升级后 |
|---|---|---|
| vite | ^7.3.3 | ^8.1.4 |
| vite-plugin-pwa | ^1.1.0 | ^1.3.0 |
| pinia | 2.1.7 | ^3.0.4 |
| vue-router | ^4.5.1 | ^5.1.0 |

## 补丁/小版本升级（pnpm update 稳妥模式，未破坏依赖顺序）

| 依赖 | 升级前 | 升级后 |
|---|---|---|
| vue | ^3.5.22 | ^3.5.39 |
| @vue/compiler-sfc | ^3.5.22 | ^3.5.39 |
| @vue/shared | ^3.5.22 | ^3.5.39 |
| @logicflow/core | ^2.1.2 | ^2.2.4 |
| @logicflow/extension | ^2.1.4 | ^2.3.0 |
| @logicflow/vue-node-registry | ^1.1.3 | ^1.2.4 |
| axios | ^1.12.2 | ^1.18.1 |
| codemirror | ^5.65.20 | ^5.65.21 |
| dayjs | ^1.11.18 | ^1.11.21 |
| lodash-es | ^4.17.21 | ^4.18.1 |
| markdown-it | ^14.1.0 | ^14.3.0 |
| pinyin-pro | ^3.27.0 | ^3.28.1 |
| qs | ^6.14.0 | ^6.15.3 |
| sortablejs | ^1.15.6 | ^1.15.7 |
| swagger-ui-dist | ^5.29.3 | ^5.32.8 |
| lunar-javascript | ^1.7.5 | ^1.7.7 |
| @iconify/json | ^2.2.394 | ^2.2.498 |
| @types/codemirror | ^5.60.16 | ^5.60.17 |
| @types/inquirer | ^9.0.9 | ^9.0.10 |
| @types/node | ^20.19.20 | ^20.19.43 |
| @types/qrcode | ^1.5.5 | ^1.5.6 |
| @types/qs | ^6.14.0 | ^6.15.1 |
| @types/sortablejs | ^1.15.8 | ^1.15.9 |
| @vitejs/plugin-vue | ^6.0.6 | ^6.0.7 |
| @vitejs/plugin-vue-jsx | ^5.1.5 | ^5.1.6 |
| @vue/test-utils | ^2.4.6 | ^2.4.11 |
| autoprefixer | ^10.4.21 | ^10.5.2 |
| commitizen | ^4.3.1 | ^4.3.2 |
| cz-git | ^1.12.0 | ^1.13.1 |
| czg | ^1.12.0 | ^1.13.1 |
| eslint-plugin-prettier | ^5.5.4 | ^5.5.6 |
| fs-extra | ^11.3.2 | ^11.3.6 |
| less | ^4.4.2 | ^4.6.7 |
| postcss | ^8.5.6 | ^8.5.16 |
| postcss-html | ^1.8.0 | ^1.8.1 |
| prettier | ^3.6.2 | ^3.9.5 |
| stylelint | ^16.25.0 | ^16.26.1 |
| ts-jest | ^29.4.4 | ^29.4.11 |
| vite-plugin-mkcert | ^1.17.9 | ^1.17.12 |
| workbox-window | ^7.3.0 | ^7.4.1 |
| unocss | ^66.6.8 | ^66.7.5 |
| dingtalk-jsapi | ^3.2.0 | ^3.2.9 |

未变化：ant-design-vue、@vueuse/core、echarts、xss、vxe-table、xe-utils、tinymce 等其余依赖版本保持不变，依赖顺序也未做调整。

-- author:zhangdaiscott ---date:20260711 ---
