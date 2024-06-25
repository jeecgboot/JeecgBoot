<template>
  <div class="base-collapse">
    <div class="header"> 钉钉集成 </div>
    <a-collapse expand-icon-position="right" :bordered="false">
      <a-collapse-panel key="1">
        <template #header>
          <div style="font-size: 16px"> 1.获取对接信息</div>
        </template>
        <div class="base-desc">从钉钉开放平台获取对接信息，即可开始集成以及同步通讯录</div>
        <div style="margin-top: 5px">
          <a href='https://help.qiaoqiaoyun.com/expand/dingding.html' target='_blank'>如何获取对接信息?</a>
        </div>
      </a-collapse-panel>
    </a-collapse>
    <div class="sync-padding">
      <a-collapse expand-icon-position="right" :bordered="false">
        <a-collapse-panel key="2">
          <template #header>
            <div style="width: 100%; justify-content: space-between; display: flex">
              <div style="font-size: 16px"> 2.对接信息录入</div>
            </div>
          </template>
          <div class="base-desc">完成步骤1后，填入Agentld、 AppKey、AppSecret后 可对接应用与同步通讯录</div>
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
            <a-button @click="dingEditClick">编辑</a-button>
          </div>
        </a-collapse-panel>
      </a-collapse>
      <div class="sync-padding">
        <div style="font-size: 16px; width: 100%"> 3.数据同步</div>
        <div style="margin-top: 20px" class="base-desc">
          从钉钉同步到本地
          <ul style='list-style-type: disc;margin-left: 20px;'>
            <li>同步部门到本地</li>
            <li>
              同步部门下的用户到本地
              <a-tooltip title='同步用户与部门文档'>
                <a-icon @click='handleIconClick' type="question-circle" class="sync-text"/>
              </a-tooltip>
            </li>
          </ul>
          <div style="float: right">
            <a-button :loading="btnLoading" @click="syncDingTalk">{{ !btnLoading ? '同步' : '同步中' }}</a-button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <ThirdAppConfigModal @register="registerAppConfigModal" @success="handleSuccess" />
</template>

<script lang="ts">
  import { defineComponent, h, inject, onMounted, reactive, ref, watch } from 'vue';
  import { getThirdConfigByTenantId, syncDingTalkDepartUserToLocal } from './ThirdApp.api';
  import { useModal } from '/@/components/Modal';
  import ThirdAppConfigModal from './ThirdAppConfigModal.vue';
  import { Modal } from 'ant-design-vue';
  import { getTenantId } from '/@/utils/auth';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'OrganDingConfigForm',
    components: {
      ThirdAppConfigModal,
    },
    setup() {
      const { createMessage } = useMessage();
      //折叠面板选中key
      const collapseActiveKey = ref<string>('');
      //按钮加载事件
      const btnLoading = ref<boolean>(false);
      //第三方配置数据
      const appConfigData = ref<any>({
        agentId: undefined,
        clientId: '',
        clientSecret: '',
      });

      //企业微信钉钉配置modal
      const [registerAppConfigModal, { openModal }] = useModal();

      /**
       * 钉钉编辑
       */
      async function dingEditClick() {
        let tenantId = getTenantId();
        openModal(true, {
          tenantId: tenantId,
          thirdType: 'dingtalk',
        });
      }

      /**
       * 初始化第三方数据
       */
      async function initThirdAppConfigData(params) {
        let values = await getThirdConfigByTenantId(params);
        if (values) {
          appConfigData.value = values;
        }
      }

      /**
       * 成功回调
       */
      function handleSuccess() {
        let tenantId = getTenantId();
        initThirdAppConfigData({ tenantId: tenantId, thirdType: 'dingtalk' });
      }

      /**
       * 同步钉钉
       */
      async function syncDingTalk() {
        btnLoading.value = true;
        await syncDingTalkDepartUserToLocal()
          .then((res) => {
            let options = {};
            if (res.result) {
              options = {
                width: 600,
                title: res.message,
                content: () => {
                  let nodes;
                  let successInfo = [`成功信息如下：`, renderTextarea(h, res.result.successInfo.map((v, i) => `${i + 1}. ${v}`).join('\n'))];
                  if (res.success) {
                    nodes = [...successInfo, h('br'), `无失败信息！`];
                  } else {
                    nodes = [
                      `失败信息如下：`,
                      renderTextarea(h, res.result.failInfo.map((v, i) => `${i + 1}. ${v}`).join('\n')),
                      h('br'),
                      ...successInfo,
                    ];
                  }
                  return nodes;
                },
              };
            }
            if (res.success) {
              if (options != null) {
                Modal.success(options);
              } else {
                createMessage.warning(res.message);
              }
            } else {
              if (options && options.title) {
                Modal.warning(options)
              } else {
                createMessage.warning({
                  content: "同步失败，请检查对接信息录入中是否填写正确，并确认是否已开启钉钉配置！",
                  duration: 5
                });
              }
            }
          })
          .finally(() => {
            btnLoading.value = false;
          });
      }

      /**
       * 渲染文本
       * @param h
       * @param value
       */
      function renderTextarea(h, value) {
        return h(
          'div',
          {
            id: 'box',
            style: {
              minHeight: '100px',
              border: '1px solid #d9d9d9',
              fontSize: '14px',
              maxHeight: '250px',
              whiteSpace: 'pre',
              overflow: 'auto',
              padding: '10px',
            },
          },
          value
        );
      }

      /**
       * 钉钉同步文档
       */
      function handleIconClick(){
        window.open("https://help.qiaoqiaoyun.com/expand/dingdingsyn.html","_target")
      }
      
      onMounted(() => {
        let tenantId = getTenantId();
        initThirdAppConfigData({ tenantId: tenantId, thirdType: 'dingtalk' });
      });

      return {
        appConfigData,
        collapseActiveKey,
        registerAppConfigModal,
        dingEditClick,
        handleSuccess,
        syncDingTalk,
        btnLoading,
        handleIconClick,
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
  [data-theme='dark'] .base-collapse .ant-collapse{
    border: none !important;
  }
  /*end 兼容暗夜模式*/
  /*文档按钮问号样式*/
  .sync-text{
    margin-left: 2px;
    cursor: pointer;
    position: relative;
    top: 2px
  }
 :deep(.ant-collapse-borderless >.ant-collapse-item:last-child) {border-bottom-width:1px;}
</style>
