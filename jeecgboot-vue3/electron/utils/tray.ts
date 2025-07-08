// tray = 系统托盘
import path from 'path';
import {Tray, Menu, app, dialog, nativeImage, BrowserWindow, Notification} from 'electron';
import {_PATHS} from '../paths';
import {$env, isDev} from '../env';

const TrayIcons = {
  normal: nativeImage.createFromPath(path.join(_PATHS.publicRoot, 'logo.png')),
  empty: nativeImage.createEmpty(),
};

// 创建托盘图标
export function createTray(win: BrowserWindow) {
  const tray = new Tray(TrayIcons.normal);

  const TrayUtils = useTray(tray, win);

  tray.setToolTip($env.VITE_GLOB_APP_TITLE! + (isDev ? ' (开发环境)' : ''));

  // 左键托盘图标显示主窗口
  tray.on('click', () => TrayUtils.showMainWindow());
  // 右键托盘图标显示托盘菜单
  tray.on('right-click', () => showTrayContextMenu());

  function showTrayContextMenu() {
    const trayContextMenu = getTrayMenus(win, TrayUtils);
    // 弹出托盘菜单，不使用 setContextMenu 方法是因为要实时更新菜单内容
    tray.popUpContextMenu(trayContextMenu);
  }
}

export function useTray(tray: Tray, win: BrowserWindow) {
  let isBlinking = false;
  let blinkTimer: NodeJS.Timeout | null = null;

  function showMainWindow() {
    win.show();
  }

  // 开始闪动
  function startBlink() {
    isBlinking = true;
    tray.setImage(TrayIcons.empty);
    blinkTimer = setTimeout(() => {
      tray.setImage(TrayIcons.normal);
      setTimeout(() => {
        if (isBlinking) {
          startBlink();
        }
      }, 500);
    }, 500);
  }

  // 结束闪动
  function stopBlink() {
    isBlinking = false;
    if (blinkTimer) {
      clearTimeout(blinkTimer);
      blinkTimer = null;
    }
    tray.setImage(TrayIcons.normal);
  }

  // 发送桌面通知
  function sendDesktopNotice() {
    // 判断是否支持桌面通知
    if (!Notification.isSupported()) {
      // todo 实际开发中不需要提示，直接返回或者换一种提示方式
      dialog.showMessageBoxSync(win, {
        type: 'error',
        title: '错误',
        message: '当前系统不支持桌面通知',
      });
      return;
    }
    const ins = new Notification({
      title: '通知标题',
      subtitle: '通知副标题',
      body: '通知内容第一行\n通知内容第二行',
      icon: TrayIcons.normal.resize({width: 32, height: 32}),
    });

    ins.on('click', () => {
      dialog.showMessageBoxSync(win, {
        type: 'info',
        title: '提示',
        message: '通知被点击',
      });
    });

    ins.show();
  }

  return {
    showMainWindow,

    startBlink,
    stopBlink,
    isBlinking: () => isBlinking,

    sendDesktopNotice,
  };
}

const MenuIcon = {
  exit: nativeImage
    .createFromDataURL(
      'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAACXBIWXMAAA7EAAAOxAGVKw4bAAACJ0lEQVR4nH1TzWvUQBRP7fpxsWqVXsSLiAevRWhhN28msRJo981kay4WRBCF/QdEFJpbaUHw4kFBQTwUKX4gKh48KPiBBcGLJ1F0uzPZ7ibWXf0DIjObielS+mDIm/fxm9/85sWyBixN06E0CIaV3wB2XhC8puOWNZSG4Y7B+k2mi7Kl9l2n9rHnzvbWJoLRYn7r5jTViQjwzM8ynlC+AFyVgN2NU8G+Rnn6QETx3FfP223A/jeHfWqCsAUJ7Hlryh9Te0nYqiDsz9rE6VHVIABvNwEf/ADYk4OsZPeVFbwiCHtcZBVR9k4CJhJmDuUxwEVJ8H4fINOkC9Vjbeq/UTR1IgPturX3f93Z35+B7ddxgJL6dih/skF9zE9KCJ//5bDLpii1+npIuzolKTubC5gBxzarJo6vWWjrUP+etFlF+ds9lRFOXalN+NPEmxvRDS3KH34v8+PFIgNmTh0EahH+InGCwzoQEbYcuTMnlR8aYbaxGHFvRNiznssP6sA65UsxrdU1+hYnFhlpAGAkdvzlPLFu88mY8pcrVjCsxcqGapC2eYW249/tUH4xS4QaVQLeigi/YWJqPl4DlNRSrAwzSaoXIspeWUYrI9qXINglgT1qAt5JPG+kkNN5BSAJuyoJfhAVdmST4PlPBFASNs6rIgnspqC8HlF+SQAuRQTfKpYiEy6fwuIdP42P71T+t0l/TBKcE8AXm4DXBfB6w50+apgUhf4HZ5j+Z5+zNTAAAAAASUVORK5CYII='
    )
    .resize({
      width: 16,
      height: 16,
    }),
};

// 设置托盘菜单
function getTrayMenus(win: BrowserWindow, TrayUtils: ReturnType<typeof useTray>) {
  const {startBlink, stopBlink, sendDesktopNotice} = TrayUtils;
  const isBlinking = TrayUtils.isBlinking();

  return Menu.buildFromTemplate([
    ...(isDev
      ? [
        {
          label: '开发工具',
          submenu: [
            {
              label: '以下菜单仅显示在开发环境',
              sublabel: '当前为开发环境',
              enabled: false,
            },
            {type: 'separator'},
            {
              label: '切换 DevTools',
              click: () => win.webContents.toggleDevTools(),
            },
            {
              label: `托盘图标${isBlinking ? '停止' : '开始'}闪烁`,
              sublabel: '模拟新消息提醒',
              click: () => (isBlinking ? stopBlink() : startBlink()),
            },
            {
              label: '发送桌面通知示例',
              click: () => sendDesktopNotice(),
            },
          ],
        },
        {type: 'separator'},
      ]
      : ([] as any)),
    {
      label: '显示主窗口',
      // 文件图标
      icon: TrayIcons.normal.resize({width: 16, height: 16}),
      click: () => win.show(),
    },
    {type: 'separator'},
    {
      label: '退出',
      // base64图标
      icon: MenuIcon.exit,
      click: () => {
        // 弹出是否确认退出提示框
        const choice = dialog.showMessageBoxSync(win, {
          type: 'question',
          title: '提示',
          message: '确定要退出应用吗？',
          buttons: ['退出', '取消'],
          defaultId: 1,
          cancelId: 1,
          noLink: true,
        });
        // 用户选择了退出，直接 exit
        if (choice === 0) {
          // global.isQuitting = true;
          app.exit(0);
        }
      },
    },
  ]);
}
