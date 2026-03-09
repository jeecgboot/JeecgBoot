import { MenuTypeEnum, MenuModeEnum } from '/@/enums/menuEnum';
import { APP_PRESET_COLOR_LIST, HEADER_PRESET_BG_COLOR_LIST, SIDE_BAR_BG_COLOR_LIST } from '/@/settings/designSetting';

/**
 * 根据菜单类型和模式获取对应的主题色
 * @param menuType 菜单类型
 */
export function getConfigByMenuType(menuType: MenuTypeEnum): {
  themeColor: string;
  headerBgColor: string;
  sideBgColor: string;
  split: boolean;
  mode: MenuModeEnum;
} {
  let themeColor;
  let headerBgColor;
  let sideBgColor;
  let split = false;
  let mode: MenuModeEnum = MenuModeEnum.INLINE;
  if (menuType === MenuTypeEnum.TOP_MENU) {
    // 顶部栏导航
    themeColor = APP_PRESET_COLOR_LIST[1];
    headerBgColor = HEADER_PRESET_BG_COLOR_LIST[4];
    mode = MenuModeEnum.HORIZONTAL;
  } else if (menuType === MenuTypeEnum.MIX) {
    // 顶部混合菜单模式
    themeColor = APP_PRESET_COLOR_LIST[2];
    headerBgColor = HEADER_PRESET_BG_COLOR_LIST[2];
    sideBgColor = SIDE_BAR_BG_COLOR_LIST[3];
    split = true;
  } else if (menuType === MenuTypeEnum.MIX_SIDEBAR) {
    // 侧边折叠导航模式
    themeColor = APP_PRESET_COLOR_LIST[1];
    headerBgColor = HEADER_PRESET_BG_COLOR_LIST[0];
    sideBgColor = SIDE_BAR_BG_COLOR_LIST[0];
  } else if (menuType === MenuTypeEnum.SIDEBAR) {
    // 侧边栏导航
    themeColor = APP_PRESET_COLOR_LIST[1];
    headerBgColor = HEADER_PRESET_BG_COLOR_LIST[4];
    sideBgColor = SIDE_BAR_BG_COLOR_LIST[7];
  }
  return {
    themeColor,
    headerBgColor,
    sideBgColor,
    split,
    mode,
  };
}
