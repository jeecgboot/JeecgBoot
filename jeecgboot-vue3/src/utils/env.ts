import type { GlobEnvConfig } from '/#/config';

import { warn } from '/@/utils/log';
import pkg from '../../package.json';
import { getConfigFileName } from '../../build/getConfigFileName';

export function getCommonStoragePrefix() {
  const { VITE_GLOB_APP_SHORT_NAME } = getAppEnvConfig();
  return `${VITE_GLOB_APP_SHORT_NAME}__${getEnv()}`.toUpperCase();
}

// Generate cache key according to version
export function getStorageShortName() {
  return `${getCommonStoragePrefix()}${`__${pkg.version}`}__`.toUpperCase();
}

export function getAppEnvConfig() {
  const ENV_NAME = getConfigFileName(import.meta.env);

  const ENV = (import.meta.env.DEV
    ? // Get the global configuration (the configuration will be extracted independently when packaging)
      (import.meta.env as unknown as GlobEnvConfig)
    : window[ENV_NAME as any]) as unknown as GlobEnvConfig;

  const {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_USE_MOCK,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_APP_OPEN_QIANKUN,
    VITE_GLOB_APP_CAS_BASE_URL,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_ONLINE_VIEW_URL,
  } = ENV;

  // if (!/^[a-zA-Z\_]*$/.test(VITE_GLOB_APP_SHORT_NAME)) {
  //   warn(
  //     `VITE_GLOB_APP_SHORT_NAME 变量只能是字符/下划线，请在环境变量中修改并重新运行.`
  //   );
  // }

  return {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_USE_MOCK,
    VITE_GLOB_APP_SHORT_NAME,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_APP_OPEN_SSO,
    VITE_GLOB_APP_OPEN_QIANKUN,
    VITE_GLOB_APP_CAS_BASE_URL,
    VITE_GLOB_DOMAIN_URL,
    VITE_GLOB_ONLINE_VIEW_URL,
  };
}

/**
 * @description: Development mode
 */
export const devMode = 'development';

/**
 * @description: Production mode
 */
export const prodMode = 'production';

/**
 * @description: Get environment variables
 * @returns:
 * @example:
 */
export function getEnv(): string {
  return import.meta.env.MODE;
}

/**
 * @description: Is it a development mode
 * @returns:
 * @example:
 */
export function isDevMode(): boolean {
  return import.meta.env.DEV;
}

/**
 * @description: Is it a production mode
 * @returns:
 * @example:
 */
export function isProdMode(): boolean {
  return import.meta.env.PROD;
}
