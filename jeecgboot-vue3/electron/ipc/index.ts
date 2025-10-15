import { Tray, ipcMain, BrowserWindow, app, Notification } from 'electron';
import type { NotificationConstructorOptions, IpcMainInvokeEvent } from 'electron';
import { openInBrowser } from '../utils';
import { omit } from 'lodash-es';

ipcMain.on('open-in-browser', (event: IpcMainInvokeEvent, url: string) => openInBrowser(url));
// 处理任务栏闪烁
ipcMain.on('notify-flash', (event: IpcMainInvokeEvent, count: number = 0) => {
  const win = BrowserWindow.getAllWindows()[0];
  if (!win) return;
  if (win.isFocused()) return;
  if (process.platform === 'win32') {
    // windows
    win.flashFrame(true);
  } else if (process.platform === 'darwin') {
    // Mac
    if (app.dock) {
      app.dock.bounce('informational');
      // 设置角标(未读消息)
      if (count > 0) {
        app.dock.setBadge(count.toString());
      } else {
        app.dock.setBadge('');
      }
    }
  }
});
// 通知 (点击通知打开指定页面)
ipcMain.on('notify-with-path', (event: IpcMainInvokeEvent, options: NotificationConstructorOptions & { path: string }) => {
  const win = BrowserWindow.getAllWindows()[0];
  if (!win) return;
  if (win.isFocused()) return;
  const notification = new Notification({
    ...omit(options, 'path'),
  });
  notification.on('click', () => {
    if (win.isMinimized()) win.restore();
    win.show();
    win.focus();
    // win.webContents.send('navigate-to', options.path);
  });
  notification.show();
});
