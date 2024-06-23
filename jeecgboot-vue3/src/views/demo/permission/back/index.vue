<template>
  <PageWrapper
    title="后台权限示例"
    contentBackground
    contentClass="p-4"
    content="目前mock了两组数据， id为1 和 2 具体返回的菜单可以在mock/sys/menu.ts内查看"
  >
    <CurrentPermissionMode />

    <Alert class="mt-4" type="info" message="点击后请查看左侧菜单变化" show-icon />

    <div class="mt-4">
      权限切换(请先切换权限模式为后台权限模式):
      <a-button-group>
        <a-button @click="switchToken(1)" :disabled="!isBackPremissionMode"> 获取用户id为1的菜单 </a-button>
        <a-button @click="switchToken(2)" :disabled="!isBackPremissionMode"> 获取用户id为2的菜单 </a-button>
      </a-button-group>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, computed } from 'vue';
  import CurrentPermissionMode from '../CurrentPermissionMode.vue';
  import { RoleEnum } from '/@/enums/roleEnum';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useUserStore } from '/@/store/modules/user';
  import { PageWrapper } from '/@/components/Page';
  import { PermissionModeEnum } from '/@/enums/appEnum';
  import { useAppStore } from '/@/store/modules/app';
  import { Alert } from 'ant-design-vue';
  export default defineComponent({
    components: { Alert, CurrentPermissionMode, PageWrapper },
    setup() {
      const { refreshMenu } = usePermission();
      const userStore = useUserStore();
      const appStore = useAppStore();

      const isBackPremissionMode = computed(() => appStore.getProjectConfig.permissionMode === PermissionModeEnum.BACK);

      async function switchToken(userId: number) {
        // 本函数切换用户登录Token的部分仅用于演示，实际生产时切换身份应当重新登录
        const token = 'fakeToken' + userId;
        userStore.setToken(token);

        // 重新获取用户信息和菜单
        userStore.getUserInfoAction();
        refreshMenu();
      }

      return {
        RoleEnum,
        refreshMenu,
        switchToken,
        isBackPremissionMode,
      };
    },
  });
</script>
<style lang="less" scoped>
  .demo {
    background-color: @component-background;
  }
</style>
