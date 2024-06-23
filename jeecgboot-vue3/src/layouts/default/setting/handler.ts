import { HandlerEnum, tabsThemeOptions} from './enum';
import { updateHeaderBgColor, updateSidebarBgColor } from '/@/logics/theme/updateBackground';
import { updateColorWeak } from '/@/logics/theme/updateColorWeak';
import { updateGrayMode } from '/@/logics/theme/updateGrayMode';

import { useAppStore } from '/@/store/modules/app';
import { ProjectConfig } from '/#/config';
import { changeTheme } from '/@/logics/theme';
import { updateDarkTheme } from '/@/logics/theme/dark';
import { useRootSetting } from '/@/hooks/setting/useRootSetting';
import { MenuModeEnum, MenuTypeEnum } from '/@/enums/menuEnum';
import { HEADER_PRESET_BG_COLOR_LIST, APP_PRESET_COLOR_LIST, SIDE_BAR_BG_COLOR_LIST } from '/@/settings/designSetting';
import { isObject } from '/@/utils/is';
import { ThemeEnum } from '/@/enums/appEnum';
import { APP__THEME__COLOR } from '/@/enums/cacheEnum';

/**
 *  2024-04-07
 *  liaozhiyang
 *  切换导航栏模式都走这个方法，每个模式都会有固定的顶部和菜单颜色搭配。暗黑模式则不走固定搭配
 * */
export function layoutHandler(event: HandlerEnum, value: any) {
  const isHTopMenu = isObject(value) && value.type == MenuTypeEnum.TOP_MENU && value.mode == MenuModeEnum.HORIZONTAL;
  const isMixMenu = isObject(value) && value.type == MenuTypeEnum.MIX && value.mode == MenuModeEnum.INLINE;
  const isMixSidebarMenu = isObject(value) && value.type == MenuTypeEnum.MIX_SIDEBAR && value.mode == MenuModeEnum.INLINE;
  const appStore = useAppStore();
  const darkMode = appStore.getDarkMode === ThemeEnum.DARK;
  if (isHTopMenu) {
    baseHandler(event, value);
    baseHandler(HandlerEnum.HEADER_THEME, HEADER_PRESET_BG_COLOR_LIST[2]);
    baseHandler(HandlerEnum.CHANGE_THEME_COLOR, APP_PRESET_COLOR_LIST[2]);
    if (darkMode) {
      updateHeaderBgColor();
      updateSidebarBgColor();
    }
    baseHandler(HandlerEnum.TABS_THEME, tabsThemeOptions[1].value);
  } else if (isMixMenu) {
    baseHandler(event, value);
    baseHandler(HandlerEnum.HEADER_THEME, HEADER_PRESET_BG_COLOR_LIST[4]);
    baseHandler(HandlerEnum.MENU_THEME, SIDE_BAR_BG_COLOR_LIST[3]);
    if (darkMode) {
      updateHeaderBgColor();
      updateSidebarBgColor();
    }
    baseHandler(HandlerEnum.CHANGE_THEME_COLOR, APP_PRESET_COLOR_LIST[1]);
    baseHandler(HandlerEnum.TABS_THEME, tabsThemeOptions[1].value);
  } else if (isMixSidebarMenu) {
    baseHandler(event, value);
    baseHandler(HandlerEnum.CHANGE_THEME_COLOR, APP_PRESET_COLOR_LIST[1]);
    baseHandler(HandlerEnum.HEADER_THEME, HEADER_PRESET_BG_COLOR_LIST[0]);
    baseHandler(HandlerEnum.MENU_THEME, SIDE_BAR_BG_COLOR_LIST[0]);
    if (darkMode) {
      updateHeaderBgColor();
      updateSidebarBgColor();
    }
    baseHandler(HandlerEnum.TABS_THEME, tabsThemeOptions[1].value);
  } else {
    baseHandler(event, value);
    baseHandler(HandlerEnum.HEADER_THEME, HEADER_PRESET_BG_COLOR_LIST[4]);
    baseHandler(HandlerEnum.MENU_THEME, SIDE_BAR_BG_COLOR_LIST[7]);
    if (darkMode) {
      updateHeaderBgColor();
      updateSidebarBgColor();
    }
    baseHandler(HandlerEnum.CHANGE_THEME_COLOR, APP_PRESET_COLOR_LIST[1]);
    baseHandler(HandlerEnum.TABS_THEME, tabsThemeOptions[1].value);
  }
}

export function baseHandler(event: HandlerEnum, value: any) {
  const appStore = useAppStore();
  const config = handler(event, value);
  appStore.setProjectConfig(config);
  if (event === HandlerEnum.CHANGE_THEME) {
    updateHeaderBgColor();
    updateSidebarBgColor();
  }
}

export function handler(event: HandlerEnum, value: any): DeepPartial<ProjectConfig> {
  const appStore = useAppStore();

  const { getThemeColor, getDarkMode } = useRootSetting();
  switch (event) {
    case HandlerEnum.CHANGE_LAYOUT:
      const { mode, type, split } = value;
      const splitOpt = split === undefined ? { split } : {};

      return {
        menuSetting: {
          mode,
          type,
          collapsed: false,
          show: true,
          hidden: false,
          ...splitOpt,
        },
      };

    case HandlerEnum.CHANGE_THEME_COLOR:
      if (getThemeColor.value === value) {
        return {};
      }
      // update-begin--author:liaozhiyang---date:20240417---for:【QQYUN-8925】系统主题颜色（供页面加载使用）
      localStorage.setItem(APP__THEME__COLOR, value);
      // update-end--author:liaozhiyang---date:20240417---for:【QQYUN-8925】系统主题颜色（供页面加载使用）
      changeTheme(value);

      return { themeColor: value };

    case HandlerEnum.CHANGE_THEME:
      if (getDarkMode.value === value) {
        return {};
      }
      updateDarkTheme(value);

      return {};

    case HandlerEnum.MENU_HAS_DRAG:
      return { menuSetting: { canDrag: value } };

    case HandlerEnum.MENU_ACCORDION:
      return { menuSetting: { accordion: value } };

    case HandlerEnum.MENU_TRIGGER:
      return { menuSetting: { trigger: value } };

    case HandlerEnum.MENU_TOP_ALIGN:
      return { menuSetting: { topMenuAlign: value } };

    case HandlerEnum.MENU_COLLAPSED:
      return { menuSetting: { collapsed: value } };

    case HandlerEnum.MENU_WIDTH:
      return { menuSetting: { menuWidth: value } };

    case HandlerEnum.MENU_SHOW_SIDEBAR:
      return { menuSetting: { show: value } };

    case HandlerEnum.MENU_COLLAPSED_SHOW_TITLE:
      return { menuSetting: { collapsedShowTitle: value } };

    case HandlerEnum.MENU_THEME:
      updateSidebarBgColor(value);
      return { menuSetting: { bgColor: value } };

    case HandlerEnum.MENU_SPLIT:
      return { menuSetting: { split: value } };

    case HandlerEnum.MENU_CLOSE_MIX_SIDEBAR_ON_CHANGE:
      return { menuSetting: { closeMixSidebarOnChange: value } };

    case HandlerEnum.MENU_FIXED:
      return { menuSetting: { fixed: value } };

    case HandlerEnum.MENU_TRIGGER_MIX_SIDEBAR:
      return { menuSetting: { mixSideTrigger: value } };

    case HandlerEnum.MENU_FIXED_MIX_SIDEBAR:
      return { menuSetting: { mixSideFixed: value } };

    // ============transition==================
    case HandlerEnum.OPEN_PAGE_LOADING:
      appStore.setPageLoading(false);
      return { transitionSetting: { openPageLoading: value } };

    case HandlerEnum.ROUTER_TRANSITION:
      return { transitionSetting: { basicTransition: value } };

    case HandlerEnum.OPEN_ROUTE_TRANSITION:
      return { transitionSetting: { enable: value } };

    case HandlerEnum.OPEN_PROGRESS:
      return { transitionSetting: { openNProgress: value } };
    // ============root==================

    case HandlerEnum.LOCK_TIME:
      return { lockTime: value };

    case HandlerEnum.FULL_CONTENT:
      return { fullContent: value };

    case HandlerEnum.CONTENT_MODE:
      return { contentMode: value };

    case HandlerEnum.SHOW_BREADCRUMB:
      return { showBreadCrumb: value };

    case HandlerEnum.SHOW_BREADCRUMB_ICON:
      return { showBreadCrumbIcon: value };

    case HandlerEnum.GRAY_MODE:
      updateGrayMode(value);
      return { grayMode: value };

    case HandlerEnum.SHOW_FOOTER:
      return { showFooter: value };

    case HandlerEnum.COLOR_WEAK:
      updateColorWeak(value);
      return { colorWeak: value };

    case HandlerEnum.SHOW_LOGO:
      return { showLogo: value };

    // ============tabs==================
    case HandlerEnum.TABS_SHOW_QUICK:
      return { multiTabsSetting: { showQuick: value } };

    case HandlerEnum.TABS_SHOW:
      return { multiTabsSetting: { show: value } };

    case HandlerEnum.TABS_SHOW_REDO:
      return { multiTabsSetting: { showRedo: value } };

    case HandlerEnum.TABS_SHOW_FOLD:
      return { multiTabsSetting: { showFold: value } };

    case HandlerEnum.TABS_THEME:
      return { multiTabsSetting: { theme: value } };

    // ============header==================
    case HandlerEnum.HEADER_THEME:
      updateHeaderBgColor(value);
      return { headerSetting: { bgColor: value } };

    case HandlerEnum.HEADER_SEARCH:
      return { headerSetting: { showSearch: value } };

    case HandlerEnum.HEADER_FIXED:
      return { headerSetting: { fixed: value } };

    case HandlerEnum.HEADER_SHOW:
      return { headerSetting: { show: value } };
    default:
      return {};
  }
}
