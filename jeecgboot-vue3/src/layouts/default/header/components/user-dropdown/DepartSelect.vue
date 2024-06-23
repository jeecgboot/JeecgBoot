<template>
  <BasicModal v-bind="config" :maxHeight="500" :title="currTitle" v-model:visible="visible" wrapClassName="loginSelectModal">
    <a-form ref="formRef" v-bind="layout" :colon="false" class="loginSelectForm">
      <a-form-item v-if="isMultiTenant" :validate-status="validate_status">
        <!--label内容-->
        <template #label>
          <a-tooltip placement="topLeft">
            <template #title>
              <span>您隶属于多租户，请选择当前所属租户</span>
            </template>
            <a-avatar style="background-color: #87d068" :size="30"> 租户 </a-avatar>
          </a-tooltip>
        </template>
        <!--部门下拉内容-->
        <a-select v-model:value="tenantSelected" placeholder="请选择登录部门" :class="{ 'valid-error': validate_status == 'error' }">
          <template #suffixIcon>
            <Icon icon="ant-design:gold-outline" />
          </template>
          <template v-for="tenant in tenantList" :key="tenant.id">
            <a-select-option :value="tenant.id">{{ tenant.name }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item v-if="isMultiDepart" :validate-status="validate_status1">
        <!--label内容-->
        <template #label>
          <a-tooltip placement="topLeft">
            <template #title>
              <span>您隶属于多部门，请选择当前所在部门</span>
            </template>
            <a-avatar style="background-color: rgb(104, 208, 203)" :size="30"> 部门 </a-avatar>
          </a-tooltip>
        </template>
        <!--部门下拉内容-->
        <a-select v-model:value="departSelected" placeholder="请选择登录部门" :class="{ 'valid-error': validate_status1 == 'error' }">
          <template #suffixIcon>
            <Icon icon="ant-design:gold-outline" />
          </template>
          <template v-for="depart in departList" :key="depart.orgCode">
            <a-select-option :value="depart.orgCode">{{ depart.departName }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
    </a-form>

    <template #footer>
      <a-button @click="close">关闭</a-button>
      <a-button @click="handleSubmit" type="primary">确认</a-button>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, watch, unref } from 'vue';
  import { Avatar } from 'ant-design-vue';
  import { BasicModal } from '/@/components/Modal';
  import { getUserDeparts, selectDepart } from '/@/views/system/depart/depart.api';
  import { getUserTenants } from '/@/views/system/tenant/tenant.api';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';

  const userStore = useUserStore();
  const { createMessage, notification } = useMessage();
  const props = defineProps({
    title: { type: String, default: '部门选择' },
    closable: { type: Boolean, default: false },
    username: { type: String, default: '' },
  });

  const layout = {
    labelCol: { span: 4 },
    wrapperCol: { span: 18 },
  };

  const config = {
    maskClosable: false,
    closable: false,
    canFullscreen: false,
    width: '500px',
    minHeight: 20,
    maxHeight: 20,
  };
  const currTitle = ref('');

  const isMultiTenant = ref(false);
  const currentTenantName = ref('');
  const tenantSelected = ref();
  const tenantList = ref([]);
  const validate_status = ref('');

  const isMultiDepart = ref(false);
  const currentDepartName = ref('');
  const departSelected = ref('');
  const departList = ref([]);
  const validate_status1 = ref('');
  //弹窗显隐
  const visible = ref(false);
  /**
   * 弹窗打开前处理
   */
  async function show() {
    //加载部门
    await loadDepartList();
    //加载租户
    await loadTenantList();
    //标题配置
    if (unref(isMultiTenant) && unref(isMultiDepart)) {
      currTitle.value = '切换租户和部门';
    } else if (unref(isMultiTenant)) {
      currTitle.value =
        unref(currentTenantName) && unref(currentTenantName).length > 0 ? `租户切换（当前租户 :${unref(currentTenantName)}）` : props.title;
    } else if (unref(isMultiDepart)) {
      currTitle.value =
        unref(currentDepartName) && unref(currentDepartName).length > 0 ? `部门切换（当前部门 :${unref(currentDepartName)}）` : props.title;
    }
    //model显隐
    if (unref(isMultiTenant) || unref(isMultiDepart)) {
      visible.value = true;
    }
  }
  /**
   *加载部门信息
   */
  async function loadDepartList() {
    const result = await getUserDeparts();
    if (!result.list || result.list.length == 0) {
      return;
    }
    let currentDepart = result.list.filter((item) => item.orgCode == result.orgCode);
    departList.value = result.list;
    departSelected.value = currentDepart && currentDepart.length > 0 ? result.orgCode : '';
    currentDepartName.value = currentDepart && currentDepart.length > 0 ? currentDepart[0].departName : '';
    isMultiDepart.value = true;
  }
  /**
   *加载租户信息
   */
  async function loadTenantList() {
    const result = await getUserTenants();
    if (!result.list || result.list.length == 0) {
      return;
    }
    let tenantId = userStore.getTenant;
    let currentTenant = result.list.filter((item) => item.id == tenantId);
    currentTenantName.value = currentTenant && currentTenant.length > 0 ? currentTenant[0].name : '';
    tenantList.value = result.list;
    tenantSelected.value = tenantId;
    isMultiTenant.value = true;
  }

  /**
   * 提交数据
   */
  async function handleSubmit() {
    if (unref(isMultiTenant) && unref(tenantSelected)==null) {
      validate_status.value = 'error';
      return false;
    }
    if (unref(isMultiDepart) && !unref(departSelected)) {
      validate_status1.value = 'error';
      return false;
    }
    departResolve()
      .then(() => {
        if (unref(isMultiTenant)) {
          userStore.setTenant(unref(tenantSelected));
        }
        createMessage.success('切换成功');
        
        //切换租户后要刷新首页
        window.location.reload();
      })
      .catch((e) => {
        console.log('登录选择出现问题', e);
      })
      .finally(() => {
        if (unref(isMultiTenant)) {
          userStore.setTenant(unref(tenantSelected));
        }
        close();
      });
  }
  /**
   *切换选择部门
   */
  function departResolve() {
    return new Promise(async (resolve, reject) => {
      if (!unref(isMultiDepart)) {
        resolve();
      } else {
        const result = await selectDepart({
          username: userStore.getUserInfo.username,
          orgCode: unref(departSelected),
          loginTenantId: unref(tenantSelected),
        });
        if (result.userInfo) {
          const userInfo = result.userInfo;
          userStore.setUserInfo(userInfo);
          resolve();
        } else {
          requestFailed(result);
          userStore.logout();
          reject();
        }
      }
    });
  }
  /**
   * 请求失败处理
   */
  function requestFailed(err) {
    notification.error({
      message: '登录失败',
      description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
      duration: 4,
    });
  }
  /**
   * 关闭model
   */
  function close() {
    departClear();
  }

  /**
   *初始化数据
   */
  function departClear() {
    currTitle.value = '';

    isMultiTenant.value = false;
    currentTenantName.value = '';
    tenantSelected.value = '';
    tenantList.value = [];
    validate_status.value = '';

    isMultiDepart.value = false;
    currentDepartName.value = '';
    departSelected.value = '';
    departList.value = [];
    validate_status1.value = '';

    visible.value = false;
  }

  /**
   * 监听username
   */
  watch(
    () => props.username,
    (value) => {
      value && loadDepartList();
    }
  );

  defineExpose({
    show,
  });
</script>
<style lang="less" scoped>
  .loginSelectForm {
    margin-bottom: -20px;
  }

  .loginSelectModal {
    top: 20px;
  }

  .valid-error .ant-select-selection__placeholder {
    color: #f5222d;
  }
</style>
