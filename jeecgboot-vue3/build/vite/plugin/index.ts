import { PluginOption } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import purgeIcons from 'vite-plugin-purge-icons';
import UnoCSS from 'unocss/vite';
import { presetTypography, presetUno } from 'unocss';

// 本地调试https配置方法
import VitePluginCertificate from 'vite-plugin-mkcert';
//[issues/555]开发环境，vscode断点调试，文件或行数对不上
import vueSetupExtend from 'vite-plugin-vue-setup-extend-plus';
import { configHtmlPlugin } from './html';
import { configMockPlugin } from './mock';
import { configCompressPlugin } from './compress';
import { configVisualizerConfig } from './visualizer';
import { configThemePlugin } from './theme';
import { configSvgIconsPlugin } from './svgSprite';
import { configQiankunMicroPlugin } from './qiankunMicro';
// //预编译加载插件(不支持vite3作废)
// import OptimizationPersist from 'vite-plugin-optimize-persist';
// import PkgConfig from 'vite-plugin-package-config';

/**
 *
 * @param viteEnv
 * @param isBuild
 * @param isQiankunMicro 是否【JEECG作为乾坤子应用】
 */
export function createVitePlugins(viteEnv: ViteEnv, isBuild: boolean, isQiankunMicro: boolean) {
  const { VITE_USE_MOCK, VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE } = viteEnv;

  const vitePlugins: (PluginOption | PluginOption[])[] = [
    // have to
    vue(),
    // have to
    vueJsx(),
    // support name
    vueSetupExtend(),
    // @ts-ignore
    VitePluginCertificate({
      source: 'coding',
    }),
  ];

  vitePlugins.push(UnoCSS({ presets: [presetUno(), presetTypography()] }));

  // vite-plugin-html
  vitePlugins.push(configHtmlPlugin(viteEnv, isBuild, isQiankunMicro));

  // vite-plugin-svg-icons
  vitePlugins.push(configSvgIconsPlugin(isBuild));

  // vite-plugin-mock
  VITE_USE_MOCK && vitePlugins.push(configMockPlugin(isBuild));

  // vite-plugin-purge-icons
  vitePlugins.push(purgeIcons());

  // rollup-plugin-visualizer
  vitePlugins.push(configVisualizerConfig());

  // vite-plugin-theme
  vitePlugins.push(configThemePlugin(isBuild));

  // 【JEECG作为乾坤子应用】注册乾坤子应用模式插件
  if (isQiankunMicro) {
    // vite-plugin-qiankun
    vitePlugins.push(...configQiankunMicroPlugin(viteEnv))
  }

  // The following plugins only work in the production environment
  if (isBuild) {
    
    // rollup-plugin-gzip
    vitePlugins.push(configCompressPlugin(VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE));

  }

  // //vite-plugin-theme【预编译加载插件，解决vite首次打开界面加载慢问题】
  // vitePlugins.push(PkgConfig());
  // vitePlugins.push(OptimizationPersist());
  return vitePlugins;
}
