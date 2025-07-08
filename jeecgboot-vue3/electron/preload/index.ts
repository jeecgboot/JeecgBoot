import {contextBridge, ipcRenderer} from 'electron'

contextBridge.exposeInMainWorld('_ELECTRON_PRELOAD_UTILS_', {
  openInBrowser: (url: string) => ipcRenderer.send('open-in-browser', url),
});
