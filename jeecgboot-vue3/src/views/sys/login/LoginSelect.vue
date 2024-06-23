<template>
  <BasicModal v-bind="config" @register="registerModal" :title="currTitle" wrapClassName="loginSelectModal" v-model:visible="visible">
    <a-form ref="formRef" :model="formState" :rules="rules" v-bind="layout" :colon="false" class="loginSelectForm">
      <!--多租户选择-->
      <a-form-item v-if="isMultiTenant" name="tenantId" :validate-status="validate_status">
        <!--label内容-->
        <template #label>
          <a-tooltip placement="topLeft">
            <template #title>
              <span>您隶属于多租户，请选择登录租户</span>
            </template>
            <a-avatar style="background-color: #87d068" :size="30"> 租户 </a-avatar>
          </a-tooltip>
        </template>
        <template #extra v-if="validate_status == 'error'">
          <span style="color: #ed6f6f">请选择登录租户</span>
        </template>
        <!--租户下拉内容-->
        <a-select
          v-model:value="formState.tenantId"
          @change="handleTenantChange"
          placeholder="请选择登录租户"
          :class="{ 'valid-error': validate_status == 'error' }"
        >
          <template v-for="tenant in tenantList" :key="tenant.id">
            <a-select-option :value="tenant.id">{{ tenant.name }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
      <!--多部门选择-->
      <a-form-item v-if="isMultiDepart" :validate-status="validate_status1" :colon="false">
        <!--label内容-->
        <template #label>
          <a-tooltip placement="topLeft">
            <template #title>
              <span>您隶属于多部门，请选择登录部门</span>
            </template>
            <a-avatar style="background-color: rgb(104, 208, 203)" :size="30"> 部门 </a-avatar>
          </a-tooltip>
        </template>
        <template #extra v-if="validate_status1 == 'error'">
          <span style="color: #ed6f6f">请选择登录部门</span>
        </template>
        <!--部门下拉内容-->
        <a-select
          v-model:value="formState.orgCode"
          @change="handleDepartChange"
          placeholder="请选择登录部门"
          :class="{ 'valid-error': validate_status1 == 'error' }"
        >
          <template v-for="depart in departList" :key="depart.orgCode">
            <a-select-option :value="depart.orgCode">{{ depart.departName }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
    </a-form>

    <template #footer>
      <a-button @click="handleSubmit" type="primary">确认</a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, ref, computed, watch, unref, reactive, UnwrapRef } from 'vue';
  import { Avatar } from 'ant-design-vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import { defHttp } from '/@/utils/http/axios';
  interface FormState {
    orgCode: string | undefined;
    tenantId: number;
  }
  export default defineComponent({
    name: 'loginSelect',
    components: {
      Avatar,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const userStore = useUserStore();
      const { notification } = useMessage();
      //租户配置
      const isMultiTenant = ref(false);
      const tenantList = ref([]);
      const validate_status = ref('');
      //部门配置
      const isMultiDepart = ref(false);
      const departList = ref([]);
      const validate_status1 = ref('');
      //弹窗显隐
      const visible = ref(false);
      //登录用户
      const username = ref('');
      //表单
      const formRef = ref();
      //选择的租户部门信息
      const formState: UnwrapRef<FormState> = reactive({
        orgCode: undefined,
        tenantId: null,
      });

      const config = {
        maskClosable: false,
        closable: false,
        canFullscreen: false,
        width: '500px',
        minHeight: 20,
        maxHeight: 20,
      };
      //弹窗操作
      const [registerModal, { closeModal }] = useModalInner();

      //当前标题
      const currTitle = computed(() => {
        if (unref(isMultiDepart) && unref(isMultiTenant)) {
          return '请选择租户和部门';
        } else if (unref(isMultiDepart) && !unref(isMultiTenant)) {
          return '请选择部门';
        } else if (!unref(isMultiDepart) && unref(isMultiTenant)) {
          return '请选择租户';
        }
      });

      const rules = ref({
        tenantId: [{ required: unref(isMultiTenant), type: 'number', message: '请选择租户', trigger: 'change' }],
        orgCode: [{ required: unref(isMultiDepart), message: '请选择部门', trigger: 'change' }],
      });

      const layout = {
        labelCol: { span: 4 },
        wrapperCol: { span: 18 },
      };
      /**
       * 处理部门情况
       */
      function bizDepart(loginResult) {
        //如果登录接口返回了用户上次登录租户ID，则不需要重新选择
        if(loginResult.userInfo?.orgCode && loginResult.userInfo?.orgCode!==''){
          isMultiDepart.value = false;
          return;
        }
        
        let multi_depart = loginResult.multi_depart;
        //0:无部门 1:一个部门 2:多个部门
        if (multi_depart == 0) {
          notification.warn({
            message: '提示',
            description: `您尚未归属部门,请确认账号信息`,
            duration: 3,
          });
          isMultiDepart.value = false;
        } else if (multi_depart == 2) {
          isMultiDepart.value = true;
          departList.value = loginResult.departs;
        } else {
          isMultiDepart.value = false;
        }
      }

      /**
       * 处理租户情况
       */
      function bizTenantList(loginResult) {
        //如果登录接口返回了用户上次登录租户ID，则不需要重新选择
        if(loginResult.userInfo?.loginTenantId && loginResult.userInfo?.loginTenantId!==0){
          isMultiTenant.value = false;
          return;
        }
        
        let tenantArr = loginResult.tenantList;
        if (Array.isArray(tenantArr)) {
          if (tenantArr.length === 0) {
            isMultiTenant.value = false;
            userStore.setTenant(formState.tenantId);
          } else if (tenantArr.length === 1) {
            formState.tenantId = tenantArr[0].id;
            isMultiTenant.value = false;
            userStore.setTenant(formState.tenantId);
          } else {
            isMultiTenant.value = true;
            tenantList.value = tenantArr;
          }
        }
      }

      /**
       * 确认选中的租户和部门信息
       */
      function handleSubmit() {
        if (unref(isMultiTenant) && !formState.tenantId) {
          validate_status.value = 'error';
          return false;
        }
        if (unref(isMultiDepart) && !formState.orgCode) {
          validate_status1.value = 'error';
          return false;
        }
        formRef.value
          .validate()
          .then(() => {
            departResolve()
              .then(() => {
                userStore.setTenant(formState.tenantId);
                emit('success');
              })
              .catch((e) => {
                console.log('登录选择出现问题', e);
              })
              .finally(() => {
                close();
              });
          })
          .catch((err) => {
            console.log('表单校验未通过error', err);
          });
      }
      /**
       *切换选择部门
       */
      function departResolve() {
        return new Promise((resolve, reject) => {
          if (!unref(isMultiDepart) && !unref(isMultiTenant)) {
            resolve();
          } else {
            let params = { orgCode: formState.orgCode,loginTenantId: formState.tenantId, username: unref(username) };
            defHttp.put({ url: '/sys/selectDepart', params }).then((res) => {
              if (res.userInfo) {
                userStore.setUserInfo(res.userInfo);
                resolve();
              } else {
                requestFailed(res);
                userStore.logout();
                reject();
              }
            });
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
        closeModal();
        reset();
      }
      /**
       * 弹窗打开前处理
       */
      async function show(loginResult) {
        if (loginResult) {
          username.value = userStore.username;
          await reset();
          await bizDepart(loginResult);
          await bizTenantList(loginResult);
          if (!unref(isMultiDepart) && !unref(isMultiTenant)) {
            emit('success', userStore.getUserInfo);
          } else {
            visible.value = true;
          }
        }
        //登录弹窗完成后，将登录的标识设置成false
        loginResult.isLogin = false;
        userStore.setLoginInfo(loginResult);
      }

      /**
       *重置数据
       */
      function reset() {
        tenantList.value = [];
        validate_status.value = '';

        departList.value = [];
        validate_status1.value = '';
      }

      function handleTenantChange(e) {
        validate_status.value = '';
      }

      function handleDepartChange(e) {
        validate_status1.value = '';
      }

      return {
        registerModal,
        visible,
        tenantList,
        isMultiTenant,
        validate_status,
        isMultiDepart,
        departList,
        validate_status1,
        formState,
        rules,
        layout,
        formRef,
        currTitle,
        config,
        handleTenantChange,
        handleDepartChange,
        show,
        handleSubmit,
      };
    },
  });
</script>

<style lang="less" scoped>
  .loginSelectForm {
    margin-bottom: -20px;
  }

  .loginSelectModal {
    top: 10px;
  }
</style>
