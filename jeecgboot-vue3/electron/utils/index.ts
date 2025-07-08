import fs from 'fs';
import path from 'path'
import {shell, dialog} from 'electron'
import {_PATHS} from "../paths";
import {isDev} from "../env";

// 通过浏览器打开链接
export function openInBrowser(url: string) {
  return shell.openExternal(url);
}


export function getAppInfo(): any {
  try {
    const yamlPath = isDev ? path.join(_PATHS.publicRoot, '../electron-builder.yaml') : path.join(_PATHS.electronRoot, 'env.yaml');
    const yamlContent = fs.readFileSync(yamlPath, 'utf-8');
    // 通过正则表达式匹配 appId 和 productName
    const appIdMatch = yamlContent.match(/appId:\s*['"]([^'"]+)['"]/);
    const productNameMatch = yamlContent.match(/productName:\s*['"]([^'"]+)['"]/);
    const appId = appIdMatch ? appIdMatch[1] : '';
    const productName = productNameMatch ? productNameMatch[1] : '';
    return {appId, productName}
  } catch (e) {
    dialog.showMessageBoxSync(null, {
      type: 'error',
      title: '错误',
      message: '应用启动失败，请从官网下载最新版本安装包后重新安装！',
    });
    process.exit(-1);
  }
}
