import { registerComponent, registerAsyncComponent, registerASyncComponentReal } from '/@/components/jeecg/JVxeTable';
import { JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
import { DictSearchSpanCell, DictSearchInputCell } from './src/components/JVxeSelectDictSearchCell';
import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
export async function registerJVxeCustom() {
  // ----------------- ⚠ 注意事项 ⚠ -----------------
  //  当组件内包含 BasicModal 时，必须使用异步引入！
  //  否则将会导致 i18n 失效！
  // ----------------- ⚠ 注意事项 ⚠ -----------------

  // 注册【Popup】（普通封装方式）
  await registerAsyncComponent(JVxeTypes.popup, import('./src/components/JVxePopupCell.vue'));

  // 注册【字典搜索下拉】组件（高级封装方式）
  registerComponent(JVxeTypes.selectDictSearch, DictSearchInputCell, DictSearchSpanCell);

  // 注册【文件上传】组件
  await registerAsyncComponent(JVxeTypes.file, import('./src/components/JVxeFileCell.vue'));
  // 注册【图片上传】组件
  await registerAsyncComponent(JVxeTypes.image, import('./src/components/JVxeImageCell.vue'));
  // 注册【用户选择】组件
  await registerAsyncComponent(JVxeTypes.userSelect, import('./src/components/JVxeUserSelectCell.vue'));
  // 注册【部门选择】组件
  await registerAsyncComponent(JVxeTypes.departSelect, import('./src/components/JVxeDepartSelectCell.vue'));
  // 注册【省市区选择】组件
  // await registerAsyncComponent(JVxeTypes.pca, import('./src/components/JVxePcaCell.vue'));
  // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-8241】为避免首次加载china-area-data，JVxePcaCell组件需异步加载
  registerASyncComponentReal(
    JVxeTypes.pca,
    createAsyncComponent(() => import('./src/components/JVxePcaCell.vue'))
  );
  // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-8241】为避免首次加载china-area-data，JVxePcaCell组件需异步加载
}
