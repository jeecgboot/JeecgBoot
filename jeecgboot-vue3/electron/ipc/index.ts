import {ipcMain} from 'electron'
import {openInBrowser} from "../utils";

ipcMain.on('open-in-browser', (event, url) => openInBrowser(url));
