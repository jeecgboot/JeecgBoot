/**
 * @description: 默认四种菜单主题
 */
export enum MenuTypeEnum {
  // 左侧边菜单导航风格
  SIDEBAR = 'sidebar',
  // 顶部栏导航风格
  MIX = 'mix',
 // 侧边折叠导航风格
  MIX_SIDEBAR = 'mix-sidebar',
  //顶部混合导航风格
  TOP_MENU = 'top-menu',
}

// 折叠触发器位置
export enum TriggerEnum {
  // 不显示
  NONE = 'NONE',
  // 菜单底部
  FOOTER = 'FOOTER',
  // 头部
  HEADER = 'HEADER',
}

export type Mode = 'vertical' | 'vertical-right' | 'horizontal' | 'inline';

// menu mode
export enum MenuModeEnum {
  VERTICAL = 'vertical',
  HORIZONTAL = 'horizontal',
  VERTICAL_RIGHT = 'vertical-right',
  INLINE = 'inline',
}

export enum MenuSplitTyeEnum {
  NONE,
  TOP,
  LEFT,
}

export enum TopMenuAlignEnum {
  CENTER = 'center',
  START = 'start',
  END = 'end',
}

export enum MixSidebarTriggerEnum {
  HOVER = 'hover',
  CLICK = 'click',
}
