import { MenuTypeEnum, MenuModeEnum, TriggerEnum, MixSidebarTriggerEnum } from '/@/enums/menuEnum';
import {
  ContentEnum,
  PermissionModeEnum,
  ThemeEnum,
  RouterTransitionEnum,
  SettingButtonPositionEnum,
  SessionTimeoutProcessingEnum,
} from '/@/enums/appEnum';

import { CacheTypeEnum } from '/@/enums/cacheEnum';

export type LocaleType = 'zh_CN' | 'en' | 'ru' | 'ja' | 'ko';

export interface MenuSetting {
  bgColor: string;
  fixed: boolean;
  collapsed: boolean;
  canDrag: boolean;
  show: boolean;
  hidden: boolean;
  split: boolean;
  menuWidth: number;
  mode: MenuModeEnum;
  type: MenuTypeEnum;
  theme: ThemeEnum;
  // update-begin--author:liaozhiyang---date:20240408---for：【QQYUN-8922】左侧导航栏文字颜色调整区分彩色和暗黑
  isThemeBright: boolean;
  // update-end--author:liaozhiyang---date:20240408---for：【QQYUN-8922】左侧导航栏文字颜色调整区分彩色和暗黑
  topMenuAlign: 'start' | 'center' | 'end';
  trigger: TriggerEnum;
  accordion: boolean;
  closeMixSidebarOnChange: boolean;
  collapsedShowTitle: boolean;
  mixSideTrigger: MixSidebarTriggerEnum;
  mixSideFixed: boolean;
}

export interface MultiTabsSetting {
  cache: boolean;
  show: boolean;
  showQuick: boolean;
  canDrag: boolean;
  showRedo: boolean;
  showFold: boolean;
  theme: string;
}

export interface HeaderSetting {
  bgColor: string;
  fixed: boolean;
  show: boolean;
  theme: ThemeEnum;
  // 是否显示全屏按钮
  showFullScreen: boolean;
  // 是否显示锁屏按钮
  useLockPage: boolean;
  // 是否显示文档连接
  showDoc: boolean;
  // 是否显示消息图标
  showNotice: boolean;
  // 是否显示搜索按钮
  showSearch: boolean;
}

export interface LocaleSetting {
  // 是否显示国际化切换按钮
  showPicker: boolean;
  // Current language
  locale: LocaleType;
  // default language
  fallback: LocaleType;
  // available Locales
  availableLocales: LocaleType[];
}

export interface TransitionSetting {
  //  Whether to open the page switching animation
  enable: boolean;
  // Route basic switching animation
  basicTransition: RouterTransitionEnum;
  // Whether to open page switching loading
  openPageLoading: boolean;
  // Whether to open the top progress bar
  openNProgress: boolean;
}

export interface ProjectConfig {
  // Storage location of permission related information
  permissionCacheType: CacheTypeEnum;
  // Whether to show the configuration button
  showSettingButton: boolean;
  // Whether to show the theme switch button
  showDarkModeToggle: boolean;
  // Configure where the button is displayed
  settingButtonPosition: SettingButtonPositionEnum;
  // Permission mode
  permissionMode: PermissionModeEnum;
  // Session timeout processing
  sessionTimeoutProcessing: SessionTimeoutProcessingEnum;
  // Website gray mode, open for possible mourning dates
  grayMode: boolean;
  // Whether to turn on the color weak mode
  colorWeak: boolean;
  // Theme color
  themeColor: string;

  // The main interface is displayed in full screen, the menu is not displayed, and the top
  fullContent: boolean;
  // content width
  contentMode: ContentEnum;
  // Whether to display the logo
  showLogo: boolean;
  // Whether to show the global footer
  showFooter: boolean;
  // menuType: MenuTypeEnum;
  headerSetting: HeaderSetting;
  // menuSetting
  menuSetting: MenuSetting;
  // Multi-tab settings
  multiTabsSetting: MultiTabsSetting;
  // Animation configuration
  transitionSetting: TransitionSetting;
  // pageLayout whether to enable keep-alive
  openKeepAlive: boolean;
  // Lock screen time
  lockTime: number;
  // Show breadcrumbs
  showBreadCrumb: boolean;
  // Show breadcrumb icon
  showBreadCrumbIcon: boolean;
  // Use error-handler-plugin
  useErrorHandle: boolean;
  // Whether to open back to top
  useOpenBackTop: boolean;
  // Is it possible to embed iframe pages
  canEmbedIFramePage: boolean;
  // Whether to delete unclosed messages and notify when switching the interface
  closeMessageOnSwitch: boolean;
  // Whether to cancel the http request that has been sent but not responded when switching the interface.
  removeAllHttpPending: boolean;
}

export interface GlobConfig {
  // Site title
  title: string;
  // Service interface url
  apiUrl: string;
  domainUrl: string;
  // Upload url (作废)
  uploadUrl?: string;
  openSso?: string;
  openQianKun?: string;
  casBaseUrl?: string;
  // onlineview url
  viewUrl?: string;
  //  Service interface url prefix
  urlPrefix?: string;
  // Project abbreviation
  shortName: string;
  // 短标题
  shortTitle: string;
}
export interface GlobEnvConfig {
  // Site title
  VITE_GLOB_APP_TITLE: string;
  // Service interface url
  VITE_GLOB_API_URL: string;
  VITE_USE_MOCK: string;
  // Service interface url prefix
  VITE_GLOB_API_URL_PREFIX?: string;
  // Project abbreviation
  VITE_GLOB_APP_SHORT_NAME: string;
  //是否开启单点登录
  VITE_GLOB_APP_OPEN_SSO: string;
  //是否开启微应用模式
  VITE_GLOB_APP_OPEN_QIANKUN: string;
  //单点服务端地址
  VITE_GLOB_APP_CAS_BASE_URL: string;
  VITE_GLOB_DOMAIN_URL: string;
  // Upload url
  VITE_GLOB_UPLOAD_URL?: string;
  // view url
  VITE_GLOB_ONLINE_VIEW_URL?: string;
}
