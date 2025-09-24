import { computed, unref, ref, watch } from 'vue';
import { getMenus } from '/@/router/menus';
import { useRoute } from 'vue-router';
import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
import { useRootSetting } from '/@/hooks/setting/useRootSetting';
import { lighten, darken } from '/@/utils/color';
export const useEmpty = () => {
  const { getThemeColor, getDarkMode } = useRootSetting();
  const route = useRoute();
  const { getHeaderBgColor } = useHeaderSetting();
  const { getMenuBgColor } = useMenuSetting();
  const pageTip = ref('');
  const effectVars = computed(() => {
    const primary = unref(getThemeColor) || '#1890ff';
    const menuBg = unref(getMenuBgColor) || '#ffffff';
    const headerBg = unref(getHeaderBgColor);
    const isDark = unref(getDarkMode) === 'dark';
    // 以主题色为基色，派生三组渐变色
    const a1 = lighten(primary, 25);
    const a2 = primary;
    const b1 = lighten(headerBg, 45);
    const b2 = lighten(headerBg, 10);
    const c1 = lighten(menuBg, 35);
    const c2 = darken(primary, 5);
    const bg1 = isDark ? '#0f172a' : '#f7f8fa';
    const bg2 = isDark ? '#111827' : '#f2f5f9';
    const grid = isDark ? 'rgba(255,255,255,0.04)' : 'rgba(60,70,90,0.06)';
    const tipColor = isDark ? '#626262' : '#b9b9b9';
    const tipBg = isDark ? 'rgba(17,24,39,0.6)' : 'rgba(255,255,255,0.6)';
    const tipBorder = isDark ? 'rgba(255,255,255,0.08)' : 'rgba(0,0,0,0.06)';
    return {
      '--blob-a-1': a1,
      '--blob-a-2': a2,
      '--blob-b-1': b1,
      '--blob-b-2': b2,
      '--blob-c-1': c1,
      '--blob-c-2': c2,
      '--bg-1': bg1,
      '--bg-2': bg2,
      '--grid-color': grid,
      '--tip-color': tipColor,
      '--tip-bg': tipBg,
      '--tip-border': tipBorder,
    } as Record<string, string>;
  });

  const getPageTip = async (route) => {
    const menus = await getMenus();
    const menu = getMatchingPath(menus, route.path);
    if (menu) {
      if (['/layouts/default/index'].includes(menu.originComponent)) {
        pageTip.value = '点击子菜单跳转到对应外部链接！';
      } else {
        pageTip.value = '查看组件引用是否正确';
      }
    }
  };
  watch(
    route,
    () => {
      getPageTip({ path: window.location.pathname });
    },
    { immediate: true }
  );

  function getMatchingPath(menus, path) {
    for (let i = 0, len = menus.length; i < len; i++) {
      const item = menus[i];
      if (item.path === path) {
        return item;
      } else if (item.children?.length) {
        const result = getMatchingPath(item.children, path);
        if (result) {
          return result;
        }
      }
    }
    return null;
  }

  return {
    pageTip,
    getPageTip,
    effectVars,
  };
};
