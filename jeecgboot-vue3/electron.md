# Electron桌面应用打包

- 1.安装依赖很慢，得10分钟左右
- 2.electron桌面应用打包文档
  https://help.jeecg.com/ui/setup/electron-build
- 3.临时注释掉electron功能
 注释代码：build/vite/plugin/electron.ts
 修改build/vite/plugin/index.ts，搜索`electron plugin`注释相关逻辑代码
 修改package.json删除相关依赖

```yaml
{
  "main": "dist/electron/main.js",
  "scripts": {
    "electron:dev": "cross-env VITE_GLOB_RUN_PLATFORM=electron npm run dev",
    "electron:build-all": "npm run electron:build-web && npm run electron:build-app",
    "electron:build-web": "cross-env VITE_GLOB_RUN_PLATFORM=electron NODE_ENV=production NODE_OPTIONS=--max-old-space-size=8192 vite build --mode prod_electron && cross-env VITE_GLOB_RUN_PLATFORM=electron esno ./build/script/postBuild.ts && esno ./build/script/copyChat.ts",
    "electron:build-app": "esno ./electron/script/buildBefore.ts && electron-builder && esno ./electron/script/buildAfter.ts",
  },
  "devDependencies": {
    "electron": "35.1.4",
    "electron-builder": "^26.0.12",
    "vite-plugin-electron": "^0.29.0",
  },
}

```


# Electron桌面通知示例和代码位置

1. 代码位置：electron/utils/tray.ts
2. 发送系统通知调用：sendDesktopNotice
3. 开始托盘图标闪动调用：startBlink
4. 停止托盘图标闪动调用：stopBlink
