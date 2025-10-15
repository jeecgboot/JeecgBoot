import { contextBridge, ipcRenderer } from 'electron';
import { ElectronEnum } from '../../src/enums/jeecgEnum';

contextBridge.exposeInMainWorld(ElectronEnum.ELECTRON_API, {
  openInBrowser: (url: string) => ipcRenderer.send('open-in-browser', url),
  // 发送消息通知
  sendNotification: (title: string, body: string, path: string) => {
    ipcRenderer.send('notify-with-path', { title, body, path });
  },
  // 绑定路由跳转
  onNavigate: (cb: (path: string) => void) => {
    ipcRenderer.on('navigate-to', (_, path) => cb(path));
  },
  // 任务栏闪
  sendNotifyFlash: () => ipcRenderer.send('notify-flash'),
  // 托盘闪动
  trayFlash: () => ipcRenderer.send('tray-flash'),
  // 托盘停止闪动
  trayFlashStop: () => ipcRenderer.send('tray-flash-stop'),
});
