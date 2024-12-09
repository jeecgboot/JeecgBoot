/*
* 【JEECG作为乾坤子应用】
*/
import type {App} from 'vue';
import type {MainAppProps} from "#/main";

import {destroyStore} from "@/store";
import {destroyRouter} from "@/router";
import {clearComponent} from "@/components/jeecg/JVxeTable/src/componentMap";

import {renderWithQiankun} from 'vite-plugin-qiankun/dist/helper';

/**
 * 以乾坤子应用模式运行
 * @param render
 */
export async function useQiankunMicroApp(render: (props?: MainAppProps) => Promise<App>) {
  let instance: Nullable<App> = null;

  // 注册乾坤子应用生命周期函数
  renderWithQiankun({
    async mount(props) {
      console.debug('[qiankun-micro] mount - props :', props)
      instance = await render({
        container: props.container!,
        hideSider: props.hideSider,
        hideHeader: props.hideHeader,
        hideMultiTabs: props.hideMultiTabs,
      });
    },
    bootstrap() {
      console.debug('[qiankun-micro] bootstrap');
    },
    update(props) {
      console.debug('[qiankun-micro] update: ', props);
    },
    unmount(props) {
      console.debug('[qiankun-micro] unmount: ', props);

      destroyStore();
      destroyRouter();

      if (instance) {
        clearComponent();
        instance.unmount();
        instance._container.innerHTML = '';
        instance = null;
      }
    },
  });

  return instance!
}

export async function autoUseQiankunMicro(fn: Fn) {
  return useQiankunMicroApp(fn)
}
