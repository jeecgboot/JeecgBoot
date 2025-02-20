import qiankun from 'vite-plugin-qiankun';

/**
 * 【JEECG作为乾坤子应用】Vite适配乾坤以子应用模式运行
 * @param env
 */
export function configQiankunMicroPlugin(env: ViteEnv) {
  const {VITE_GLOB_QIANKUN_MICRO_APP_NAME} = env

  return [
    qiankun(VITE_GLOB_QIANKUN_MICRO_APP_NAME!, {
      useDevMode: true,
    })
  ]

}
