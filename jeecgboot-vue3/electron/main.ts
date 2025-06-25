import './ipc';

import { app, BrowserWindow, Menu } from 'electron';
import { isDev } from './env';
import { createMainWindow, createIndexWindow } from './utils/window';
import { getAppInfo} from "./utils";

// 隐藏所有菜单
Menu.setApplicationMenu(null);

let mainWindow: BrowserWindow | null = null;

function main() {
  mainWindow = createMainWindow();
  return mainWindow;
}

// 非开发环境，只允许一个实例运行
if (!isDev) {
  // 是否取得了单一实例锁
  const gotTheLock = app.requestSingleInstanceLock();

  if (gotTheLock) {
    app.on('second-instance', () => {
      // 开启一个新的窗口
      createIndexWindow();
    });
  } else {
    // 没有取得单一实例锁，则退出应用
    app.exit(0);
  }
}

// 生命周期管理
app.whenReady().then(() => {
  // 获取应用信息
  const $appInfo = getAppInfo();
  if ($appInfo?.productName && $appInfo?.appId) {
    app.setName($appInfo.productName);
    app.setAppUserModelId($appInfo.appId);
  }

  main();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      main();
    }
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});
