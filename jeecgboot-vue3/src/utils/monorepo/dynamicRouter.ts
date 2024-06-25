export type DynamicViewsRecord = Record<string, () => Promise<Recordable>>;

/** 已注册模块的动态页面 */
export const packageViews: DynamicViewsRecord = {};

/**
 * 注册动态路由页面
 * @param getViews 获取该模块下所有页面的方法
 */
export function registerDynamicRouter(getViews: () => DynamicViewsRecord) {
  if (typeof getViews === 'function') {
    let dynamicViews = getViews();
    Object.keys(dynamicViews).forEach((key) => {
      // 处理动态页面的key，使其可以让路由识别
      let newKey = key.replace('./src/views', '../../views');
      packageViews[newKey] = dynamicViews[key];
    });
  }
}
