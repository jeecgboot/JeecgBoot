/**
 * 生成外部配置文件，用于生产发布后配置，无需重新打包
 */
import { GLOB_CONFIG_FILE_NAME, OUTPUT_DIR } from '../constant';
import fs, { writeFileSync } from 'fs-extra';
import colors from 'picocolors';

import { getEnvConfig, getRootPath } from '../utils';
import { getConfigFileName } from '../getConfigFileName';

import pkg from '../../package.json';

interface CreateConfigParams {
  configName: string;
  config: any;
  configFileName?: string;
}

function createConfig(params: CreateConfigParams) {
  const { configName, config, configFileName } = params;
  try {
    const windowConf = `window.${configName}`;
    // Ensure that the variable will not be modified
    let configStr = `${windowConf}=${JSON.stringify(config)};`;
    configStr += `
      Object.freeze(${windowConf});
      Object.defineProperty(window, "${configName}", {
        configurable: false,
        writable: false,
      });
    `.replace(/\s/g, '');

    fs.mkdirp(getRootPath(OUTPUT_DIR));
    writeFileSync(getRootPath(`${OUTPUT_DIR}/${configFileName}`), configStr);

    console.log(colors.cyan(`✨ [${pkg.name}]`) + ` - configuration file is build successfully:`);
    console.log(colors.gray(OUTPUT_DIR + '/' + colors.green(configFileName)) + '\n');

    // update-begin--author:sunjianlei---date:20250423---for：【QQYUN-9685】构建 electron 桌面应用
    // 如果是 Electron 环境，还需要将配置文件写入到 JSON 文件中
    if (config.VITE_GLOB_RUN_PLATFORM === 'electron') {
      writeFileSync(getRootPath(`${OUTPUT_DIR}/electron/env.json`), JSON.stringify(config));
      console.log(colors.cyan(`✨ [${pkg.name}]`) + ` - electron env file is build successfully:`);
      console.log(colors.gray(OUTPUT_DIR + '/' + colors.green('electron/env.json')) + '\n');
    }
    // update-end----author:sunjianlei---date:20250423---for：【QQYUN-9685】构建 electron 桌面应用

  } catch (error) {
    console.log(colors.red('configuration file configuration file failed to package:\n' + error));
  }
}

export function runBuildConfig() {
  const config = getEnvConfig();
  const configFileName = getConfigFileName(config);
  createConfig({ config, configName: configFileName, configFileName: GLOB_CONFIG_FILE_NAME });
}
