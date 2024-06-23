import { colorIsDark, lighten, darken } from '/@/utils/color';
import { useAppStore } from '/@/store/modules/app';
import { ThemeEnum } from '/@/enums/appEnum';
import { setCssVar } from './util';
import { SIDE_BAR_BG_COLOR_LIST, SIDER_LOGO_BG_COLOR_LIST } from '/@/settings/designSetting';

const HEADER_BG_COLOR_VAR = '--header-bg-color';
const HEADER_BG_HOVER_COLOR_VAR = '--header-bg-hover-color';
const HEADER_MENU_ACTIVE_BG_COLOR_VAR = '--header-active-menu-bg-color';

const SIDER_LOGO_BG_COLOR = '--sider-logo-bg-color';
const SIDER_DARK_BG_COLOR = '--sider-dark-bg-color';
const SIDER_DARK_DARKEN_BG_COLOR = '--sider-dark-darken-bg-color';
const SIDER_LIGHTEN_BG_COLOR = '--sider-dark-lighten-bg-color';

/**
 * Change the background color of the top header
 * @param color
 */
export function updateHeaderBgColor(color?: string) {
  const appStore = useAppStore();
  const darkMode = appStore.getDarkMode === ThemeEnum.DARK;
  if (!color) {
    if (darkMode) {
      color = '#151515';
    } else {
      color = appStore.getHeaderSetting.bgColor;
    }
  }
  // bg color
  setCssVar(HEADER_BG_COLOR_VAR, color);

  // hover color
  const hoverColor = lighten(color!, 6);
  setCssVar(HEADER_BG_HOVER_COLOR_VAR, hoverColor);
  setCssVar(HEADER_MENU_ACTIVE_BG_COLOR_VAR, hoverColor);

  // Determine the depth of the color value and automatically switch the theme
  const isDark = colorIsDark(color!);

  appStore.setProjectConfig({
    headerSetting: {
      theme: isDark || darkMode ? ThemeEnum.DARK : ThemeEnum.LIGHT,
    },
  });
}

/**
 * Change the background color of the left menu
 * @param color  bg color
 */
export function updateSidebarBgColor(color?: string) {
  const appStore = useAppStore();

  // if (!isHexColor(color)) return;
  const darkMode = appStore.getDarkMode === ThemeEnum.DARK;
  if (!color) {
    if (darkMode) {
      color = '#212121';
    } else {
      color = appStore.getMenuSetting.bgColor;
    }
  }
  // update-begin--author:liaozhiyang---date:20230811---for：【QQYUN-5922】logo背景色渐变
  let findIndex = SIDE_BAR_BG_COLOR_LIST.findIndex((item) => item === color);
  setCssVar(SIDER_LOGO_BG_COLOR, findIndex == -1 ? 'linear-gradient(180deg, #000000, #282828)' : SIDER_LOGO_BG_COLOR_LIST[findIndex]);
  // update-end--author:liaozhiyang---date:20230811---for：【QQYUN-5922】llogo背景色渐变
  setCssVar(SIDER_DARK_BG_COLOR, color);
  setCssVar(SIDER_DARK_DARKEN_BG_COLOR, darken(color!, 6));
  setCssVar(SIDER_LIGHTEN_BG_COLOR, lighten(color!, 5));

  // only #ffffff is light
  // Only when the background color is #fff, the theme of the menu will be changed to light
  // update-begin--author:liaozhiyang---date:20240408---for：【QQYUN-8922】左侧导航栏文字颜色调整区分彩色和暗黑
  let theme;
  let isThemeBright = false;
  if (['#fff', '#ffffff'].includes(color!.toLowerCase()) && !darkMode) {
    theme = ThemeEnum.LIGHT;
  } else if (['#009688', '#e74c3c','#037bd5'].includes(color!.toLowerCase()) && !darkMode) {
    theme = ThemeEnum.DARK;
    isThemeBright = true;
  } else {
    theme = ThemeEnum.DARK;
  }
  appStore.setProjectConfig({
    menuSetting: {
      theme,
      isThemeBright,
    },
  });
  // update-end--author:liaozhiyang---date:20240408---for：【QQYUN-8922】左侧导航栏文字颜色调整区分彩色和暗黑
}
