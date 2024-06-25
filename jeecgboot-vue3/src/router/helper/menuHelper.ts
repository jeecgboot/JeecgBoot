import { AppRouteModule } from '/@/router/types';
import type { MenuModule, Menu, AppRouteRecordRaw } from '/@/router/types';
import { findPath, treeMap } from '/@/utils/helper/treeHelper';
import { cloneDeep } from 'lodash-es';
import { isUrl } from '/@/utils/is';
import { RouteParams } from 'vue-router';
import { toRaw } from 'vue';

export function getAllParentPath<T = Recordable>(treeData: T[], path: string) {
  // update-begin--author:sunjianlei---date:220230426---for：【issues/478】修复菜单展开合并BUG
  // 原代码
  // const menuList = findPath(treeData, (n) => n.path === path) as Menu[];
  // 先匹配不包含隐藏菜单的路径
  let menuList = findMenuPath(treeData, path, false);
  // 如果没有匹配到，再匹配包含隐藏菜单的路径
  if(!(menuList?.length)) {
    menuList = findMenuPath(treeData, path, true)
  }
  // update-end--author:sunjianlei---date:220230426---for：【issues/478】修复菜单展开合并BUG
  return (menuList || []).map((item) => item.path);
}

/**
 * 查找菜单路径
 *
 * @param treeData
 * @param path
 * @param matchHide 是否匹配隐藏菜单
 */
function findMenuPath<T = Recordable>(treeData: T[], path: string, matchHide: boolean) {
  return findPath(treeData, (n) => {
    // 隐藏菜单不参与匹配
    if(!matchHide && n.hideMenu) {
      return false;
    }
    return n.path === path
  }) as Menu[];
}

// 路径处理
function joinParentPath(menus: Menu[], parentPath = '') {
  for (let index = 0; index < menus.length; index++) {
    const menu = menus[index];
    // https://next.router.vuejs.org/guide/essentials/nested-routes.html
    // Note that nested paths that start with / will be treated as a root path.
    // 请注意，以 / 开头的嵌套路径将被视为根路径。
    // This allows you to leverage the component nesting without having to use a nested URL.
    // 这允许你利用组件嵌套，而无需使用嵌套 URL。
    if (!(menu.path.startsWith('/') || isUrl(menu.path))) {
      // path doesn't start with /, nor is it a url, join parent path
      // 路径不以 / 开头，也不是 url，加入父路径
      menu.path = `${parentPath}/${menu.path}`;
    }
    if (menu?.children?.length) {
      joinParentPath(menu.children, menu.meta?.hidePathForChildren ? parentPath : menu.path);
    }
  }
}

// Parsing the menu module
export function transformMenuModule(menuModule: MenuModule): Menu {
  const { menu } = menuModule;

  const menuList = [menu];

  joinParentPath(menuList);
  return menuList[0];
}

// 将路由转换成菜单
export function transformRouteToMenu(routeModList: AppRouteModule[], routerMapping = false) {
  // 借助 lodash 深拷贝
  const cloneRouteModList = cloneDeep(routeModList);
  const routeList: AppRouteRecordRaw[] = [];

  // 对路由项进行修改
  cloneRouteModList.forEach((item) => {
    if (routerMapping && item.meta.hideChildrenInMenu && typeof item.redirect === 'string') {
      item.path = item.redirect;
    }

    if (item.meta?.single) {
      const realItem = item?.children?.[0];
      realItem && routeList.push(realItem);
    } else {
      routeList.push(item);
    }
  });
  // 提取树指定结构
  const list = treeMap(routeList, {
    conversion: (node: AppRouteRecordRaw) => {
      const { meta: { title, hideMenu = false } = {} } = node;

      return {
        ...(node.meta || {}),
        meta: node.meta,
        name: title,
        hideMenu,
        alwaysShow:node.alwaysShow||false,
        path: node.path,
        ...(node.redirect ? { redirect: node.redirect } : {}),
      };
    },
  });
  // 路径处理
  joinParentPath(list);
  return cloneDeep(list);
}

/**
 * config menu with given params
 */
const menuParamRegex = /(?::)([\s\S]+?)((?=\/)|$)/g;

export function configureDynamicParamsMenu(menu: Menu, params: RouteParams) {
  const { path, paramPath } = toRaw(menu);
  let realPath = paramPath ? paramPath : path;
  const matchArr = realPath.match(menuParamRegex);

  matchArr?.forEach((it) => {
    const realIt = it.substr(1);
    if (params[realIt]) {
      realPath = realPath.replace(`:${realIt}`, params[realIt] as string);
    }
  });
  // save original param path.
  if (!paramPath && matchArr && matchArr.length > 0) {
    menu.paramPath = path;
  }
  menu.path = realPath;
  // children
  menu.children?.forEach((item) => configureDynamicParamsMenu(item, params));
}
