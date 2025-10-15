import path from 'path'
import {isDev} from "./env";

export const _PATHS = getPaths()

function getPaths() {
  const _root = __dirname;
  const publicRoot = path.join(_root, isDev ? '../../public' : '..');
  const preloadRoot = path.join(_root, 'preload')

  return {
    electronRoot: _root,
    publicRoot,
    preloadRoot,

    appIcon: path.join(_root, `icons/app.ico`).replace(/[\\/]dist[\\/]/, '/'),
  }
}