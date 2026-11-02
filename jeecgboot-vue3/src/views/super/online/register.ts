import type {App} from 'vue';
import {createAsyncComponent} from "@/utils/factory/createAsyncComponent";
import {registerCgformRouter} from "./cgform/router/cgformRouter";
import {registerCgreportRouter} from "./cgreport/router/cgreportRouter";
import {registerGraphreportRouter} from "./graphreport/router/graphreportRouter";

/** 注册 online */
export async function register(app: App) {

  // 注册Online弹窗
  const OnlineAutoModalAsync = createAsyncComponent(() => import('./cgform/auto/default/OnlineAutoModal.vue'), {loading: true});
  app.component('OnlineAutoModal', OnlineAutoModalAsync);
  // update-begin--author:liusq---date:20261102---for：【online表单评论】系统消息查看详情改用新版详情弹窗
  // 注册Online详情弹窗
  const OnlineDetailModalAsync = createAsyncComponent(() => import('./cgform/auto/default/OnlineDetailModal.vue'), {loading: true});
  app.component('OnlineDetailModal', OnlineDetailModalAsync);
  // update-end--author:liusq---date:20261102---for：【online表单评论】系统消息查看详情改用新版详情弹窗

  // 注册Online表单路由
  registerCgformRouter();
  // 注册Online报表路由
  registerCgreportRouter();
  // 注册Online图表路由
  registerGraphreportRouter();

  // 注册外部链接页面（如果有）
  await registerCgformShareView();

  console.log('[online] 注册完成！');
}

// 注册外部链接页面，自动判断是否存在
async function registerCgformShareView() {
  try {
    const globModels = import.meta.glob('./cgform/share/index.ts');
    if (!globModels) {
      return
    }
    const models = Object.values(globModels);
    if (models.length == 0) {
      return
    }
    const shareModel = await models[0]() as Recordable;
    if (typeof shareModel?.register !== 'function') {
      return
    }
    await shareModel.register();

    console.debug('[online] Online表单外部链接路由注册完成！');
  } catch (e) {
  }
}
