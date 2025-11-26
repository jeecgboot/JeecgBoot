import { MenuTypeEnum } from '/@/enums/menuEnum';
import { APP_PRESET_COLOR_LIST } from '/@/settings/designSetting';

/**
 * 根据菜单类型和模式获取对应的主题色
 * @param menuType 菜单类型
 */
export function getThemeColorByMenuType(menuType: MenuTypeEnum): string {
  if (menuType === MenuTypeEnum.TOP_MENU) {
    // 顶部栏导航
    return APP_PRESET_COLOR_LIST[1];
  } else if (menuType === MenuTypeEnum.MIX) {
    // 顶部混合菜单使用青绿色主题
    return APP_PRESET_COLOR_LIST[2];
  } else if (menuType === MenuTypeEnum.MIX_SIDEBAR) {
    // 侧边折叠导航模式
    return APP_PRESET_COLOR_LIST[1];
  } else if (menuType === MenuTypeEnum.SIDEBAR) {
    // 侧边栏导航
    return APP_PRESET_COLOR_LIST[1];
  }
  return APP_PRESET_COLOR_LIST[1];
}
