import type { App } from 'vue';

/**
 * 动态引入 super 下的组件
 */
export async function registerSuper(app: App) {
  const modules = import.meta.glob('./**/register.ts');
  for (let [url, module] of Object.entries(modules)) {
    let { register } = await module();
    if (typeof register === 'function') {
      await register(app);
    } else {
      console.error(`${url} 没有导出 register 函数，无法完成注册！`);
    }
  }
}