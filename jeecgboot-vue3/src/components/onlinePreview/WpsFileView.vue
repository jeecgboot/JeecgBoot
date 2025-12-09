<!--wps文件预览页面-->
<template></template>

<script lang="ts" setup name="online-preview">
  import { router } from '@/router';
  import { defHttp } from '/@/utils/http/axios';
  import OpenSDK from './open-jssdk.es';
  import { getToken } from '@/utils/auth';
  import {useMessage} from "@/hooks/web/useMessage";

  const {createWarningModal} = useMessage()

  initWpsPath();

  /**
   * 初始化wps路径
   */
  async function initWpsPath() {
    let query = router.currentRoute.value.query;
    if (query && query.url) {
      let result = await defHttp.get(
        { url: '/eoa/wps/oaWpsFile/getOfflineViewWpsUrl/fileurl', params: { fileUrl: query.url } },
        { isTransformResponse: false }
      );
      if (result && result.success) {
        const instance = OpenSDK.config({
          url: result.message,
          mode: 'normal',
          mount: document.querySelector('#app'),
        });
        // 设置 token
        instance.setToken({
          token: getToken(),
        });
      } else {
        const msg = '请求预览地址失败' + (result.message ? `：${result.message}` : '，请重试！');
        createWarningModal({
          title: '提示',
          content: msg,
          onOk: () => window.close(),
        });
      }
    } else {
      createWarningModal({
        title: '提示',
        content: '获取预览地址失败，请重试！',
        onOk: () => window.close(),
      });
    }
  }
</script>

<style lang="less">
  #office-iframe {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 9999;
    background: white;
  }
</style>
