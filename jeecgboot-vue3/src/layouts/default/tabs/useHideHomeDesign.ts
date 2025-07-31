import { ref } from 'vue';
import { getMenus } from '/@/router/menus';

export const useHideHomeDesign = (currentRoute) => {
  let menus: any = [];
  // 是否隐藏门户设计
  const isHideHomeDesign = ref(true);
  const getHideHomeDesign = (isCurItem, path) => {
    if (/^\/portal-view\/[^/]+$/.test(path) && isCurItem) {
      if (['/portal-view/system', '/portal-view/template'].includes(path)) {
        // 主门户、模板门户 (需要检查是否存在设计列表,存在则显示门户设计,不存在则隐藏门户设计)
        getIsHasPortalDesignList();
      } else if (['/portal-view/default'].includes(path)) {
        // 设计器打开的预览需隐藏设计模式
        isHideHomeDesign.value = true;
      } else {
        // 个人工作台或者普通门户都可显示门户设计
        isHideHomeDesign.value = false;
      }
    } else {
      // 非门户页面隐藏门户设计
      isHideHomeDesign.value = true;
    }
  };
  const getMenusContainPath = async (ptah) => {
    if (!menus.length) {
      menus = await getMenus();
    }
    const result = getMatchingRouterName(menus, ptah);
    return !!result;
  };
  const getIsHasPortalDesignList = async () => {
    if (['/portal-view/system', '/portal-view/template'].includes(currentRoute.value.path)) {
      // 主门户、模板门户时才需要查询菜单中是否有portalDesignList
      getMenusContainPath('/super/eoa/portalapp/portalDesignList').then((result) => {
        isHideHomeDesign.value = !result;
      });
    }
  };
  getIsHasPortalDesignList();
  return {
    getHideHomeDesign,
    isHideHomeDesign,
  };
};

/*
 * 20250701
 * liaozhiyang
 * 通过path匹配菜单中的项
 * */
function getMatchingRouterName(menus, path) {
  for (let i = 0, len = menus.length; i < len; i++) {
    const item = menus[i];
    if (item.path === path && !item.redirect && !item.paramPath) {
      return item;
    } else if (item.children?.length) {
      const result = getMatchingRouterName(item.children, path);
      if (result) {
        return result;
      }
    }
  }
  return null;
}
