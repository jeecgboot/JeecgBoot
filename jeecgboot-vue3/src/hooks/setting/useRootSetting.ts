import type { ProjectConfig } from '/#/config';

import { computed } from 'vue';

import { useAppStore } from '/@/store/modules/app';
import { ContentEnum, ThemeEnum } from '/@/enums/appEnum';

type RootSetting = Omit<ProjectConfig, 'locale' | 'headerSetting' | 'menuSetting' | 'multiTabsSetting'>;

export function useRootSetting() {
  const appStore = useAppStore();

  const getPageLoading = computed(() => appStore.getPageLoading);

  const getOpenKeepAlive = computed(() => appStore.getProjectConfig.openKeepAlive);

  const getSettingButtonPosition = computed(() => appStore.getProjectConfig.settingButtonPosition);

  const getCanEmbedIFramePage = computed(() => appStore.getProjectConfig.canEmbedIFramePage);

  const getPermissionMode = computed(() => appStore.getProjectConfig.permissionMode);

  const getShowLogo = computed(() => appStore.getProjectConfig.showLogo);

  const getContentMode = computed(() => appStore.getProjectConfig.contentMode);

  const getUseOpenBackTop = computed(() => appStore.getProjectConfig.useOpenBackTop);

  const getShowSettingButton = computed(() => appStore.getProjectConfig.showSettingButton);

  const getUseErrorHandle = computed(() => appStore.getProjectConfig.useErrorHandle);

  const getShowFooter = computed(() => appStore.getProjectConfig.showFooter);

  const getShowBreadCrumb = computed(() => appStore.getProjectConfig.showBreadCrumb);

  const getThemeColor = computed(() => appStore.getProjectConfig.themeColor);

  const getShowBreadCrumbIcon = computed(() => appStore.getProjectConfig.showBreadCrumbIcon);

  const getFullContent = computed(() => appStore.getProjectConfig.fullContent);

  const getColorWeak = computed(() => appStore.getProjectConfig.colorWeak);

  const getGrayMode = computed(() => appStore.getProjectConfig.grayMode);

  const getLockTime = computed(() => appStore.getProjectConfig.lockTime);

  const getShowDarkModeToggle = computed(() => appStore.getProjectConfig.showDarkModeToggle);

  const getDarkMode = computed(() => appStore.getDarkMode);

  const getLayoutContentMode = computed(() => (appStore.getProjectConfig.contentMode === ContentEnum.FULL ? ContentEnum.FULL : ContentEnum.FIXED));

  function setRootSetting(setting: Partial<RootSetting>) {
    appStore.setProjectConfig(setting);
  }

  function setDarkMode(mode: ThemeEnum) {
    appStore.setDarkMode(mode);
  }
  return {
    setRootSetting,

    getSettingButtonPosition,
    getFullContent,
    getColorWeak,
    getGrayMode,
    getLayoutContentMode,
    getPageLoading,
    getOpenKeepAlive,
    getCanEmbedIFramePage,
    getPermissionMode,
    getShowLogo,
    getUseErrorHandle,
    getShowBreadCrumb,
    getShowBreadCrumbIcon,
    getUseOpenBackTop,
    getShowSettingButton,
    getShowFooter,
    getContentMode,
    getLockTime,
    getThemeColor,
    getDarkMode,
    setDarkMode,
    getShowDarkModeToggle,
  };
}
