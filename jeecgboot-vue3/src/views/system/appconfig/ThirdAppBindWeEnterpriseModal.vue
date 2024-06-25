<!--弹窗绑定企业微信页面-->
<template>
  <BasicModal @register="registerModal" :width="800" :title="title" destroyOnClose>
    <a-spin :spinning="loading">
      <div class="we-bind">
        <a-row :span="24" class="we-title-background">
          <a-col :span="12" class="border-right">
            <span>组织用户</span>
          </a-col>
          <a-col :span="12" class="padding-left">
            <span>企业微信用户</span>
          </a-col>
        </a-row>
        <a-row :span="24">
          <template v-for="(item, index) in bindData.jwUserDepartVos">
            <a-col :span="12" class="border-right padding-left border-bottom">
              <div class="we-account">
                <a-avatar v-if="item.avatar" :src="getFileAccessHttpUrl(item.avatar)" :size="28"></a-avatar>
                <a-avatar v-else :size="28">
                  {{ item.realName.length > 2 ? item.realName.substr(0, 2) : item.realName }}
                </a-avatar>
                <a-input style="margin-left: 20px" :value="item.realName" readonly />
              </div>
            </a-col>
            <a-col :span="12" class="padding-left border-bottom">
              <div class="we-account">
                <span v-if="item.wechatUserId || izBind" class="we-remove"
                  >{{ item.wechatRealName }} <span style="margin-right: 20px" @click="handleRemoveClick(index, item)">移出</span></span
                >
                <a-select
                  v-else
                  v-model:value="item.wechatUserId"
                  :options="userList"
                  :fieldNames="{ label: 'wechatRealName', value: 'wechatUserId' }"
                  style="width: 200px"
                  showSearch
                  @select="(val, option) => handleSelect(val, option, index)"
                />
              </div>
            </a-col>
          </template>
        </a-row>
      </div>
    </a-spin>
    <template #footer>
      <a-button v-if="!izBind" type="primary" @click="handleSubmit">同步</a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, h, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getThirdUserByWechat, wechatEnterpriseToLocal, getThirdUserBindByWechat, deleteThirdAccount } from './ThirdApp.api';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { useMessage } from '@/hooks/web/useMessage';
  import { Modal } from 'ant-design-vue';
  import { useUserStore } from '@/store/modules/user';

  export default defineComponent({
    name: 'ThirdAppBindWeEnterpriseModal',
    components: { BasicModal },
    setup(props, { emit }) {
      const title = ref<string>('企业微信绑定');
      //企业微信的绑定数据
      const bindData = ref<any>({});
      const loading = ref<boolean>(false);
      const btnLoading = ref<boolean>(false);
      const { createMessage } = useMessage();
      const userList = ref<any>([]);
      //同步文本信息展示
      const syncText = ref<string>('');
      //是否已绑定数据，展示不同的列表
      const izBind = ref<boolean>(false);
      const userStore = useUserStore();
      //表单赋值
      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        loading.value = true;
        console.log('izBind：：', izBind);
        if (!data.izBind) {
          await getUnboundData();
        } else {
          await getBoundData();
        }
        izBind.value = data.izBind;
      });

      /**
       * 未绑定的数据
       */
      async function getUnboundData() {
        await getThirdUserByWechat().then((res) => {
          if (res.success) {
            let userLists = res.result.userList;
            bindData.value = res.result;
            userList.value = res.result.userList;
            /*   if (userLists && userLists.length > 0) {
            syncText.value = "";
          } else {
            syncText.value = "企业微信用户均已同步";
          }*/
            loading.value = false;
          } else {
            createMessage.warning(res.message);
            loading.value = false;
          }
        });
      }

      /**
       * 已绑定的数据
       */
      async function getBoundData() {
        await getThirdUserBindByWechat().then((res) => {
          if (res.success) {
            bindData.value.jwUserDepartVos = res.result;
            loading.value = false;
          } else {
            createMessage.warn(res.message);
            loading.value = false;
          }
        });
      }

      /**
       * 第三方配置点击事件
       */
      async function handleSubmit() {
        btnLoading.value = true;
        let userList = bindData.value.userList;
        //重新封装数据，只留用户id和企业微信id即可,还需要把没绑定的用户传给后台
        let params: any = [];
        //查询用户绑定的企业微信用户
        for (const item of bindData.value.jwUserDepartVos) {
          if (item.wechatUserId) {
            userList = userList.filter((a) => a.wechatUserId != item.wechatUserId);
            params.push({
              wechatUserId: item.wechatUserId,
              wechatDepartId: item.wechatDepartId,
              wechatRealName: item.wechatRealName,
              userId: item.userId,
            });
          }
        }
        let text: string = '';
        //查询未被绑定的租户
        if (userList && userList.length > 0) {
          for (const item of userList) {
            params.push({ wechatUserId: item.wechatUserId, wechatDepartId: item.wechatDepartId, wechatRealName: item.wechatRealName });
          }
          text = '检测到未绑定的企业微信用户 ' + userList.length + ' 位，平台将会为这 ' + userList.length + ' 位用户创建新的账号';
        }

        Modal.confirm({
          title: '确认同步',
          content: text,
          okText: '确认',
          onOk: () => {
            let json = JSON.stringify(params);
            console.log('json：：', json);
            wechatEnterpriseToLocal({ jwUserDepartJson: json })
              .then((res) => {
                let options = {};
                if (res.success) {
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
                  closeModal();
                  emit('success', options, res);
                }
              })
              .finally(() => {
                btnLoading.value = false;
              });
          },
        });
      }

      /**
       * 下拉框选择事件
       */
      function handleSelect(val, option, index) {
        bindData.value.jwUserDepartVos[index].wechatUserId = option.wechatUserId;
        bindData.value.jwUserDepartVos[index].wechatRealName = option.wechatRealName;
        bindData.value.jwUserDepartVos[index].wechatDepartId = option.wechatDepartId;
        userList.value = userList.value.filter((item) => item.wechatUserId != option.wechatUserId);
      }

      /**
       * 移出事件
       * @param index
       * @param item
       */
      function handleRemoveClick(index, item) {
        if (!izBind.value) {
          userList.value.push({
            wechatUserId: item.wechatUserId,
            wechatRealName: item.wechatRealName,
            wechatDepartId: item.wechatDepartId,
          });
          bindData.value.jwUserDepartVos[index].wechatUserId = '';
          bindData.value.jwUserDepartVos[index].wechatRealName = '';
          bindData.value.jwUserDepartVos[index].wechatDepartId = '';
        } else {
          Modal.confirm({
            title: '确认取消绑定吗',
            okText: '确认',
            onOk: async () => {
              await deleteThirdAccount({ id: item.thirdId, sysUserId: userStore.getUserInfo.id }).then((res) => {
                if (res.success) {
                  createMessage.success('取消绑定成功！');
                  getBoundData();
                } else {
                  createMessage.warning(res.message);
                }
              });
            },
          });
        }
      }

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

      return {
        title,
        registerModal,
        handleSubmit,
        bindData,
        getFileAccessHttpUrl,
        loading,
        userList,
        handleSelect,
        handleRemoveClick,
        btnLoading,
        izBind,
      };
    },
  });
</script>

<style lang="less" scoped>
  .we-bind {
    overflow-y: auto;
    border: 1px @border-color-base solid;
    border-bottom: none;
    .we-title-background {
      background: @component-background;
      height: 40px;
      line-height: 40px;
      padding: 0 10px;
    }
    .we-account {
      display: flex;
      height: 40px;
      line-height: 40px;
      align-items: center;
    }

    :deep(.ant-input) {
      border: none;
      padding: 0;
      box-shadow: none;
    }

    .we-remove {
      display: flex;
      justify-content: space-between;
      width: 100%;
      cursor: pointer;
    }
    .border-right {
      border-right: 1px @border-color-base solid;
    }
    .border-bottom {
      border-bottom: 1px @border-color-base solid;
    }
    .padding-left {
      padding-left: 10px;
    }
  }
</style>
