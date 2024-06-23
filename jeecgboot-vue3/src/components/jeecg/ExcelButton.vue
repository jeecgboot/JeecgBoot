<template>
  <a-button type="primary" v-if="hasExportAuth() && config.export" preIcon="ant-design:export-outlined" @click="onExportXls()"> 导出</a-button>
  <a-upload name="file" :showUploadList="false" v-if="hasImportAuth() && config.import" :customRequest="(file) => onImportXls(file)">
    <a-button type="primary" preIcon="ant-design:import-outlined">导入</a-button>
  </a-upload>
</template>

<script lang="ts" setup name="ExcelButton">
  import { PropType } from 'vue';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMethods } from '/@/hooks/system/useMethods';
  import { useMessage } from '/@/hooks/web/useMessage';

  // 定义 excel 方法所需参数
  interface ExcelConfig {
    // 导出配置
    exportConfig: {
      url: string;
      // 导出文件名
      name?: string | (() => string);
      //按钮权限
      auth?: string | string[];
    };
    // 导入配置
    importConfig: {
      url: string;
      // 导出成功后的回调
      success?: (fileInfo?: any) => void;
      //按钮权限
      auth?: string | string[];
    };
  }
  /**
   * 定义组件参数
   */
  const props = defineProps({
    config: {
      type: Object as PropType<ExcelConfig>,
      default: null,
    },
  });
  //按钮权限问题
  const { hasPermission } = usePermission();
  //导入导出方法
  const { handleExportXls, handleImportXls } = useMethods();

  const $message = useMessage();
  // 导出 excel
  function onExportXls() {
    let { url, name } = props.config?.export ?? {};
    if (url) {
      let title = typeof name === 'function' ? name() : name;
      return handleExportXls(title as string, url);
    } else {
      $message.createMessage.warn('没有传递 export.url 参数');
      return Promise.reject();
    }
  }

  // 导入 excel
  function onImportXls(file) {
    let { url, success } = props.config?.import ?? {};
    if (url) {
      return handleImportXls(file, url, success);
    } else {
      $message.createMessage.warn('没有传递 import.url 参数');
      return Promise.reject();
    }
  }

  // 导入按钮权限
  function hasImportAuth() {
    let auth = props.config?.import?.auth;
    return auth && auth.length > 0 ? hasPermission(auth) : true;
  }

  // 导出按钮权限
  function hasExportAuth() {
    let auth = props.config?.export?.auth;
    return auth && auth.length > 0 ? hasPermission(auth) : true;
  }
</script>

<style scoped lang="less"></style>
