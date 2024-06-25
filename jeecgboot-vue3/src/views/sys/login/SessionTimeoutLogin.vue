<template>
  <transition>
    <div :class="prefixCls">
      <Login sessionTimeout />
    </div>
  </transition>
</template>
<script lang="ts" setup>
  import { onBeforeUnmount, onMounted, ref } from 'vue';
  import Login from './Login.vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useUserStore } from '/@/store/modules/user';
  import { usePermissionStore } from '/@/store/modules/permission';
  import { useAppStore } from '/@/store/modules/app';
  import { PermissionModeEnum } from '/@/enums/appEnum';

  const { prefixCls } = useDesign('st-login');
  const userStore = useUserStore();
  const permissionStore = usePermissionStore();
  const appStore = useAppStore();
  const userId = ref<Nullable<number | string>>(0);

  const isBackMode = () => {
    return appStore.getProjectConfig.permissionMode === PermissionModeEnum.BACK;
  };

  onMounted(() => {
    // 记录当前的UserId
    userId.value = userStore.getUserInfo?.userId;
    console.log('Mounted', userStore.getUserInfo);
  });

  onBeforeUnmount(() => {
    if (userId.value && userId.value !== userStore.getUserInfo.userId) {
      // 登录的不是同一个用户，刷新整个页面以便丢弃之前用户的页面状态
      document.location.reload();
    } else if (isBackMode() && permissionStore.getLastBuildMenuTime === 0) {
      // 后台权限模式下，没有成功加载过菜单，就重新加载整个页面。这通常发生在会话过期后按F5刷新整个页面后载入了本模块这种场景
      document.location.reload();
    }
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-st-login';

  .@{prefix-cls} {
    position: fixed;
    z-index: 9999999;
    width: 100%;
    height: 100%;
    background: @component-background;
  }
</style>
