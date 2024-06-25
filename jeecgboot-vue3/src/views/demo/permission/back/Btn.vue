<template>
  <PageWrapper contentBackground title="按钮权限控制" contentClass="p-4">
    <CurrentPermissionMode />
    <p>
      当前拥有的code列表: <a> {{ permissionStore.getPermCodeList }} </a>
    </p>
    <Divider />
    <Alert class="mt-4" type="info" message="点击后请查看按钮变化(必须处于后台权限模式才可测试此页面所展示的功能)" show-icon />
    <Divider />
    <a-button type="primary" class="mr-2" @click="switchToken(2)" :disabled="!isBackPremissionMode"> 点击切换按钮权限(用户id为2) </a-button>
    <a-button type="primary" @click="switchToken(1)" :disabled="!isBackPremissionMode"> 点击切换按钮权限(用户id为1,默认) </a-button>

    <template v-if="isBackPremissionMode">
      <Divider>组件方式判断权限</Divider>
      <Authority :value="'1000'">
        <a-button type="primary" class="mx-4"> 拥有code ['1000']权限可见 </a-button>
      </Authority>

      <Authority :value="'2000'">
        <a-button color="success" class="mx-4"> 拥有code ['2000']权限可见 </a-button>
      </Authority>

      <Authority :value="['1000', '2000']">
        <a-button color="error" class="mx-4"> 拥有code ['1000','2000']角色权限可见 </a-button>
      </Authority>

      <Divider>函数方式方式判断权限</Divider>
      <a-button v-if="hasPermission('1000')" type="primary" class="mx-4"> 拥有code ['1000']权限可见 </a-button>

      <a-button v-if="hasPermission('2000')" color="success" class="mx-4"> 拥有code ['2000']权限可见 </a-button>

      <a-button v-if="hasPermission(['1000', '2000'])" color="error" class="mx-4"> 拥有code ['1000','2000']角色权限可见 </a-button>

      <Divider>指令方式方式判断权限(该方式不能动态修改权限.)</Divider>
      <a-button v-auth="'1000'" type="primary" class="mx-4"> 拥有code ['1000']权限可见 </a-button>

      <a-button v-auth="'2000'" color="success" class="mx-4"> 拥有code ['2000']权限可见 </a-button>

      <a-button v-auth="['1000', '2000']" color="error" class="mx-4"> 拥有code ['1000','2000']角色权限可见 </a-button>
    </template>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, computed } from 'vue';
  import { Alert, Divider } from 'ant-design-vue';
  import CurrentPermissionMode from '../CurrentPermissionMode.vue';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { Authority } from '/@/components/Authority';
  import { usePermissionStore } from '/@/store/modules/permission';
  import { PermissionModeEnum } from '/@/enums/appEnum';
  import { PageWrapper } from '/@/components/Page';
  import { useAppStore } from '/@/store/modules/app';
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    components: { Alert, PageWrapper, CurrentPermissionMode, Divider, Authority },
    setup() {
      const { hasPermission } = usePermission();
      const permissionStore = usePermissionStore();
      const appStore = useAppStore();
      const userStore = useUserStore();

      const isBackPremissionMode = computed(() => appStore.getProjectConfig.permissionMode === PermissionModeEnum.BACK);

      async function switchToken(userId: number) {
        // 本函数切换用户登录Token的部分仅用于演示，实际生产时切换身份应当重新登录
        const token = 'fakeToken' + userId;
        userStore.setToken(token);

        // 重新获取用户信息和菜单
        userStore.getUserInfoAction();
        permissionStore.changePermissionCode();
      }

      return {
        hasPermission,
        permissionStore,
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
