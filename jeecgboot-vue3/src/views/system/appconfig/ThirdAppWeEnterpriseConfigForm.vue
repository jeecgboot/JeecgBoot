<template>
  <div class="base-collapse">
    <div class="header"> 企业微信集成 </div>
    <a-collapse expand-icon-position="right" :bordered="false">
      <a-collapse-panel key="1">
        <template #header>
          <div style="font-size: 16px"> 1.获取对接信息</div>
        </template>
        <div class="base-desc">从企业微信平台获取对接信息，即可开始集成以及同步通讯录</div>
        <div style="margin-top: 5px">
          <a href="https://help.qiaoqiaoyun.com/expand/dingding.html" target="_blank">如何获取对接信息?</a>
        </div>
      </a-collapse-panel>
    </a-collapse>
    <div>
      <a-collapse expand-icon-position="right" :bordered="false">
        <a-collapse-panel key="2">
          <template #header>
            <div style="width: 100%; justify-content: space-between; display: flex">
              <div style="font-size: 16px"> 2.对接信息录入</div>
            </div>
          </template>
          <div class="flex-flow">
            <div class="base-title">Agentld</div>
            <div class="base-message">
              <a-input-password v-model:value="appConfigData.agentId" readonly />
            </div>
          </div>
          <div class="flex-flow">
            <div class="base-title">AppKey</div>
            <div class="base-message">
              <a-input-password v-model:value="appConfigData.clientId" readonly />
            </div>
          </div>
          <div class="flex-flow">
            <div class="base-title">AppSecret</div>
            <div class="base-message">
              <a-input-password v-model:value="appConfigData.clientSecret" readonly />
            </div>
          </div>
          <div style="margin-top: 20px; width: 100%; text-align: right">
            <a-button @click="weEnterpriseEditClick">编辑</a-button>
          </div>
        </a-collapse-panel>
      </a-collapse>
      <div class="sync-padding">
        <div style="font-size: 16px; width: 100%"> 3.数据同步</div>
        <div style="margin-top: 20px" class="base-desc">
          从企业微信同步到敲敲云
          <a style="margin-left: 10px" @click="seeBindWeChat">查看已绑定的企业微信用户</a>
          <div style="float: right">
            <a-button @loading="btnLoading" @click="thirdUserByWechat">同步</a-button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <ThirdAppConfigModal @register="registerAppConfigModal" @success="handleSuccess" />
  <ThirdAppBindWeEnterpriseModal @register="registerBindAppConfigModal" @success="handleBindSuccess" />
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { getThirdConfigByTenantId } from './ThirdApp.api';
  import ThirdAppConfigModal from './ThirdAppConfigModal.vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getTenantId } from '@/utils/auth';
  import ThirdAppBindWeEnterpriseModal from './ThirdAppBindWeEnterpriseModal.vue';
  import { Modal } from "ant-design-vue";

  export default defineComponent({
    name: 'ThirdAppWeEnterpriseConfigForm',
    components: {
      ThirdAppConfigModal,
      ThirdAppBindWeEnterpriseModal,
    },
    setup() {
      const btnLoading = ref<boolean>(false);
      //第三方配置数据
      const appConfigData = ref<any>({
        agentId: '',
        clientId: '',
        clientSecret: '',
        agentAppSecret: '',
      });
      //企业微信钉钉配置modal
      const [registerAppConfigModal, { openModal }] = useModal();
      const [registerBindAppConfigModal, { openModal: openBindModal }] = useModal();
      const { createMessage } = useMessage();

      /**
       * 初始化数据
       *
       * @param params
       */
      async function initThirdAppConfigData(params) {
        let values = await getThirdConfigByTenantId(params);
        if (values) {
          appConfigData.value = values;
        }
      }

      /**
       * 企业微信编辑
       */
      async function weEnterpriseEditClick() {
        let tenantId = getTenantId();
        openModal(true, {
          tenantId: tenantId,
          thirdType: 'wechat_enterprise',
        });
      }

      /**
       * 获取企业微信绑定的用户
       */
      async function thirdUserByWechat() {
        openBindModal(true, { izBind: false });
      }

      /**
       * 成功回调
       */
      function handleSuccess() {
        let tenantId = getTenantId();
        initThirdAppConfigData({ tenantId: tenantId, thirdType: 'wechat_enterprise' });
      }

      /**
       * 绑定成功返回值
       *
       * @param options
       * @param item
       */
      function handleBindSuccess(options, item) {
        console.log("options:::",options)
        console.log("item:::",item)
        if (item.success) {
          if (options != null) {
            Modal.success(options);
          } else {
            createMessage.warning(item.message);
          }
        } else {
          if (options && options.title) {
            Modal.warning(options);
          } else {
            createMessage.warning({
              content: '同步失败，请检查对接信息录入中是否填写正确，并确认是否已开启企业微信配置！',
              duration: 5,
            });
          }
        }
      }
      
      /**
       * 查看已绑定的企业微信
       */
      function seeBindWeChat() {
        openBindModal(true,{ izBind: true })
      }
      
      onMounted(() => {
        let tenantId = getTenantId();
        initThirdAppConfigData({ tenantId: tenantId, thirdType: 'wechat_enterprise' });
      });

      return {
        appConfigData,
        weEnterpriseEditClick,
        registerAppConfigModal,
        registerBindAppConfigModal,
        handleSuccess,
        btnLoading,
        thirdUserByWechat,
        handleBindSuccess,
        seeBindWeChat,
      };
    },
  });
</script>

<style lang="less" scoped>
  .header {
    align-items: center;
    box-sizing: border-box;
    display: flex;
    height: 50px;
    justify-content: space-between;
    font-weight: 700;
    font-size: 18px;
    color: @text-color;
  }

  .flex-flow {
    display: flex;
    min-height: 0;
  }

  .sync-padding {
    padding: 12px 0 16px;
    color: @text-color;
  }

  .base-collapse {
    margin-top: 20px;
    padding: 0 24px;
    font-size: 20px;

    .base-desc {
      font-size: 14px;
      color: @text-color;
    }

    .base-title {
      width: 100px;
      text-align: left;
      height: 50px;
      line-height: 50px;
    }

    .base-message {
      width: 100%;
      height: 50px;
      line-height: 50px;
    }

    :deep(.ant-collapse-header) {
      padding: 12px 0 16px;
    }

    :deep(.ant-collapse-content-box) {
      padding-left: 0;
    }
  }
  /*begin 兼容暗夜模式*/
  //暗黑模式下卡片的边框设置成none
  [data-theme='dark'] .base-collapse .ant-collapse {
    border: none !important;
  }
  /*end 兼容暗夜模式*/
  /*文档按钮问号样式*/
  .sync-text {
    margin-left: 2px;
    cursor: pointer;
    position: relative;
    top: 2px;
  }
</style>
