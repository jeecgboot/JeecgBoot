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
