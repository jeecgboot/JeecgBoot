import type { Menu } from '/@/router/types';
import type { Ref } from 'vue';
import { watch, unref, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { MenuSplitTyeEnum, MenuTypeEnum } from '/@/enums/menuEnum';
import { useThrottleFn } from '@vueuse/core';
import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
import { getChildrenMenus, getCurrentParentPath, getMenus, getShallowMenus } from '/@/router/menus';
import { usePermissionStore } from '/@/store/modules/permission';
import { useAppInject } from '/@/hooks/web/useAppInject';
import { PAGE_NOT_FOUND_NAME_404 } from '/@/router/constant';

export function useSplitMenu(splitType: Ref<MenuSplitTyeEnum>) {
  // Menu array
  const menusRef = ref<Menu[]>([]);
  const { currentRoute } = useRouter();
  const { getIsMobile } = useAppInject();
  const permissionStore = usePermissionStore();
  const { setMenuSetting, getIsHorizontal, getSplit, getMenuType } = useMenuSetting();

  const throttleHandleSplitLeftMenu = useThrottleFn(handleSplitLeftMenu, 50);

  const splitNotLeft = computed(() => unref(splitType) !== MenuSplitTyeEnum.LEFT && !unref(getIsHorizontal));

  const getSplitLeft = computed(() => !unref(getSplit) || unref(splitType) !== MenuSplitTyeEnum.LEFT);

  const getSpiltTop = computed(() => unref(splitType) === MenuSplitTyeEnum.TOP);

  const normalType = computed(() => {
    return unref(splitType) === MenuSplitTyeEnum.NONE || !unref(getSplit);
  });

  watch(
    [() => unref(currentRoute).path, () => unref(splitType)],
    async ([path]: [string, MenuSplitTyeEnum]) => {
      if (unref(splitNotLeft) || unref(getIsMobile)) return;
      const { meta } = unref(currentRoute);
      const currentActiveMenu = meta.currentActiveMenu as string;
      // update-begin--author:liaozhiyang---date:20250908---for：【QQYUN-13718】一级菜单默认重定向到子菜单，但子菜单未授权，导致点击一级菜单加载不出子菜单
      // 顶部混合模式且顶部左侧组合菜单开始时
      if (unref(getMenuType) === MenuTypeEnum.MIX && unref(getSplit)) { 
        // 404页面时，跳转到重定向的路径
        if (unref(currentRoute).name === PAGE_NOT_FOUND_NAME_404 && unref(currentRoute)?.redirectedFrom?.path) {
          const menus = await getMenus();
          const findItem = menus.find((item:any) => item.redirect === unref(currentRoute).path);
          if (findItem) {
            // 说明是从一级菜单重定向过来的
            path = findItem.path;
          }
        }
      }
      // update-end--author:liaozhiyang---date:20250908---for：【QQYUN-13718】一级菜单默认重定向到子菜单，但子菜单未授权，导致点击一级菜单加载不出子菜单
      let parentPath = await getCurrentParentPath(path);
      if (!parentPath) {
        parentPath = await getCurrentParentPath(currentActiveMenu);
      }
      parentPath && throttleHandleSplitLeftMenu(parentPath);
    },
    {
      immediate: true,
    }
  );

  // Menu changes
  watch(
    [() => permissionStore.getLastBuildMenuTime, () => permissionStore.getBackMenuList],
    () => {
      genMenus();
    },
    {
      immediate: true,
    }
  );

  // split Menu changes
  watch(
    () => getSplit.value,
    () => {
      // update-begin--author:liaozhiyang---date:20240919---for：【issues/7209】顶部左侧组合菜单关闭之后左侧导航没还原
      // if (unref(splitNotLeft)) return;
      // update-end--author:liaozhiyang---date:20240919---for：【issues/7209】顶部左侧组合菜单关闭之后左侧导航没还原
      genMenus();
    }
  );

  // Handle left menu split
  async function handleSplitLeftMenu(parentPath: string) {
    if (unref(getSplitLeft) || unref(getIsMobile)) return;

    // spilt mode left
    const children = await getChildrenMenus(parentPath);

    if (!children || !children.length) {
      setMenuSetting({ hidden: true });
      menusRef.value = [];
      return;
    }

    setMenuSetting({ hidden: false });
    menusRef.value = children;
  }

  // get menus
  async function genMenus() {
    // normal mode
    if (unref(normalType) || unref(getIsMobile)) {
      menusRef.value = await getMenus();
      return;
    }

    // split-top
    if (unref(getSpiltTop)) {
      const shallowMenus = await getShallowMenus();

      menusRef.value = shallowMenus;
      return;
    }
  }

  return { menusRef };
}
