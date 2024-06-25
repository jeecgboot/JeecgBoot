<template>
  <ConfigProvider :theme="appTheme" :locale="getAntdLocale">
    <AppProvider>
      <RouterView />
    </AppProvider>
  </ConfigProvider>
</template>

<script lang="ts" setup>
  import { watch, ref } from 'vue';
  import { theme } from 'ant-design-vue';
  import { ConfigProvider } from 'ant-design-vue';
  import { AppProvider } from '/@/components/Application';
  import { useTitle } from '/@/hooks/web/useTitle';
  import { useLocale } from '/@/locales/useLocale';
  import { useAppStore } from '/@/store/modules/app';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { ThemeEnum } from '/@/enums/appEnum';
  import { changeTheme } from '/@/logics/theme/index';

  const appStore = useAppStore();
  // 解决日期时间国际化问题
  import 'dayjs/locale/zh-cn';
  // support Multi-language
  const { getAntdLocale } = useLocale();

  useTitle();
  /**
   * 2024-04-07
   * liaozhiyang
   * 暗黑模式下默认文字白色，白天模式默认文字 #333
   * */
  const modeAction = (data) => {
    if (data.token) {
      if (getDarkMode.value === ThemeEnum.DARK) {
        Object.assign(data.token, { colorTextBase: 'fff' });
      } else {
        Object.assign(data.token, { colorTextBase: '#333' });
      }
    }
  };
  // update-begin--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
  const appTheme: any = ref({});
  const { getDarkMode } = useRootSetting();
  watch(
    () => getDarkMode.value,
    (newValue) => {
      delete appTheme.value.algorithm;
      if (newValue === ThemeEnum.DARK) {
        appTheme.value.algorithm = theme.darkAlgorithm;
      }
      // update-begin--author:liaozhiyang---date:20240322---for：【QQYUN-8570】生产环境暗黑模式下主题色不生效
      if (import.meta.env.PROD) {
        changeTheme(appStore.getProjectConfig.themeColor);
      }
      // update-end--author:liaozhiyang---date:20240322---for：【QQYUN-8570】生产环境暗黑模式下主题色不生效
      modeAction(appTheme.value);
      appTheme.value = {
        ...appTheme.value,
      };
    },
    { immediate: true }
  );
  watch(
    appStore.getProjectConfig,
    (newValue) => {
      const primary = newValue.themeColor;
      const result = {
        ...appTheme.value,
        ...{
          token: {
            colorPrimary: primary,
            wireframe: true,
            fontSize: 14,
            colorTextBase: '#333',
            colorSuccess: '#55D187',
            colorInfo: primary,
            borderRadius: 4,
            sizeStep: 4,
            sizeUnit: 4,
            colorWarning: '#EFBD47',
            colorError: '#ED6F6F',
            fontFamily:
              '-apple-system,BlinkMacSystemFont,Segoe UI,PingFang SC,Hiragino Sans GB,Microsoft YaHei,Helvetica Neue,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol',
          },
        },
      };
      appTheme.value = result;
      modeAction(result);
    },
    { immediate: true }
  );
  setTimeout(() => {
    appStore.getProjectConfig?.themeColor && changeTheme(appStore.getProjectConfig.themeColor);
  }, 300);
  // update-end--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x

</script>
<style lang="less">
  // update-begin--author:liaozhiyang---date:20230803---for：【QQYUN-5839】windi会影响到html2canvas绘制的图片样式
  img {
    display: inline-block;
  }
  // update-end--author:liaozhiyang---date:20230803---for：【QQYUN-5839】windi会影响到html2canvas绘制的图片样式
</style>
