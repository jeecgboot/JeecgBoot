import type {BrowserWindowConstructorOptions} from 'electron';
import {BrowserWindow, dialog} from 'electron';
import path from 'path';
import {_PATHS} from '../paths';
import {$env, isDev} from '../env';
import {createTray} from './tray';

// 创建窗口
export function createBrowserWindow(options?: BrowserWindowConstructorOptions) {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      preload: path.join(_PATHS.preloadRoot, 'index.js'),
      nodeIntegration: false,
      contextIsolation: true,
    },
    // 应用图标
    icon: isDev ? _PATHS.appIcon : void 0,
    ...options,
  });

  // 设置窗口打开处理器
  win.webContents.setWindowOpenHandler(({url}) => {
    const win = createBrowserWindow();
    win.loadURL(url);
    // 阻止创建新窗口，因为已经被接管
    return {action: 'deny'};
  });

  // 当 beforeunload 阻止窗口关闭时触发
  win.webContents.on('will-prevent-unload', () => {
    const choice = dialog.showMessageBoxSync(win, {
      type: 'question',
      title: '确认关闭吗？',
      message: '系统可能不会保存您所做的更改。',
      buttons: ['关闭', '取消'],
      defaultId: 1,
      cancelId: 1,
      noLink: true,
    });
    // 用户选择了关闭，直接销毁窗口
    if (choice === 0) {
      win.destroy();
    }
  });

  return win;
}

// 创建主窗口、系统托盘
export function createMainWindow() {
  const win = createIndexWindow()

  // 设置系统托盘图标
  createTray(win);

  // 主窗口尝试关闭时，默认不直接退出应用，而是隐藏到托盘
  win.on('close', (event) => {
    event.preventDefault();
    win.hide();
  });

  return win;
}

// 创建索引窗口
export function createIndexWindow() {
  const win = createBrowserWindow({
    width: 1600,
    height: 1000,
    title: $env.VITE_GLOB_APP_TITLE!,
  });

  // 开发环境加载Vite服务，生产加载打包文件
  if (isDev) {
    win.loadURL($env.VITE_DEV_SERVER_URL!)
    // 开发环境下，自动打开调试工具
    // win.webContents.openDevTools()
  } else {
    win.loadFile(path.join(_PATHS.publicRoot, 'index.html'));
  }

  return win;
}
