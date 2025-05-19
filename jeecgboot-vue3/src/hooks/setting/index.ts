import type { GlobConfig } from '/#/config';

import { getAppEnvConfig } from '/@/utils/env';

export const useGlobSetting = (): Readonly<GlobConfig> => {
  const {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_APP_CAS_BASE_URL,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_APP_OPEN_QIANKUN,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_ONLINE_VIEW_URL,

    // 【JEECG作为乾坤子应用】
    VITE_GLOB_QIANKUN_MICRO_APP_NAME,
    VITE_GLOB_QIANKUN_MICRO_APP_ENTRY,
  } = getAppEnvConfig();

  // if (!/[a-zA-Z\_]*/.test(VITE_GLOB_APP_SHORT_NAME)) {
  //   warn(
  //     `VITE_GLOB_APP_SHORT_NAME Variables can only be characters/underscores, please modify in the environment variables and re-running.`
  //   );
  // }

  // 短标题：替换shortName的下划线为空格
  const shortTitle = VITE_GLOB_APP_SHORT_NAME.replace(/_/g, " ");
  // Take global configuration
  const glob: Readonly<GlobConfig> = {
    title: VITE_GLOB_APP_TITLE,
    domainUrl: VITE_GLOB_DOMAIN_URL,
    apiUrl: VITE_GLOB_API_URL,
    shortName: VITE_GLOB_APP_SHORT_NAME,
    shortTitle: shortTitle,
    openSso: VITE_GLOB_APP_OPEN_SSO,
    openQianKun: VITE_GLOB_APP_OPEN_QIANKUN,
    casBaseUrl: VITE_GLOB_APP_CAS_BASE_URL,
    urlPrefix: VITE_GLOB_API_URL_PREFIX,
    uploadUrl: VITE_GLOB_DOMAIN_URL,
    viewUrl: VITE_GLOB_ONLINE_VIEW_URL,

    // 【JEECG作为乾坤子应用】是否以乾坤子应用模式启动
    isQiankunMicro: VITE_GLOB_QIANKUN_MICRO_APP_NAME != null && VITE_GLOB_QIANKUN_MICRO_APP_NAME !== '',
    // 【JEECG作为乾坤子应用】乾坤子应用入口
    qiankunMicroAppEntry: VITE_GLOB_QIANKUN_MICRO_APP_ENTRY,
  };

  // 【JEECG作为乾坤子应用】乾坤子应用下，需要定义一下
  if (!window['_CONFIG']) {
    window['_CONFIG'] = {}
  }

  // update-begin--author:sunjianlei---date:220250115---for：【QQYUN-10956】配置了自定义前缀，外部连接打不开，需要兼容处理
  let domainURL = VITE_GLOB_DOMAIN_URL;

  // 如果不是以http(s)开头的，也不是以域名开头的，那么就是拼接当前域名
  if (!/^http(s)?/.test(domainURL) && !/^(\/\/)?(.*\.)?.+\..+/.test(domainURL)) {
    if (!domainURL.startsWith('/')) {
      domainURL = '/' + domainURL;
    }
    domainURL = window.location.origin + domainURL;
  }
  // update-end--author:sunjianlei---date:220250115---for：【QQYUN-10956】配置了自定义前缀，外部连接打不开，需要兼容处理

  // @ts-ignore
  window._CONFIG['domianURL'] = domainURL;

  return glob as Readonly<GlobConfig>;
};
