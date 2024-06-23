<template>
  <template v-if="syncToApp || syncToLocal">
    <JThirdAppDropdown v-if="enabledTypes.wechatEnterprise" type="wechatEnterprise" name="企微" v-bind="bindAttrs" v-on="bindEvents" />
    <JThirdAppDropdown v-if="enabledTypes.dingtalk" type="dingtalk" name="钉钉" v-bind="bindAttrs" v-on="bindEvents" />
  </template>
  <template v-else>未设置任何同步方向</template>
</template>

<script lang="ts" setup>
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { ref, computed, createVNode, h, resolveComponent } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { backEndUrl, getEnabledTypes, doSyncThirdApp } from './jThirdApp.api';
  import { Modal, Input } from 'ant-design-vue';
  import JThirdAppDropdown from './JThirdAppDropdown.vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage, createWarningModal } = useMessage();
  const props = defineProps({
    // 同步类型，可以是 user、depart
    bizType: {
      type: String,
      required: true,
    },
    // 是否允许同步到第三方APP
    syncToApp: Boolean,
    // 是否允许第三方APP同步到本地
    syncToLocal: Boolean,
    // 选择的行
    selectedRowKeys: Array,
  });
  // 声明Emits
  const emit = defineEmits(['sync-ok', 'sync-error', 'sync-finally']);

  const enabledTypes = ref({});
  // 绑定属性
  const bindAttrs = computed(() => {
    return {
      syncToApp: props.syncToApp,
      syncToLocal: props.syncToLocal,
    };
  });
  // 绑定方法
  const bindEvents = computed(() => {
    return {
      'to-app': onToApp,
      'to-local': onToLocal,
    };
  });

  // 同步到第三方App
  function onToApp(e) {
    doSync(e.type, '/toApp');
  }

  // 同步到本地
  function onToLocal(e) {
    doSync(e.type, '/toLocal');
  }

  // 获取启用的第三方App
  async function loadEnabledTypes() {
    enabledTypes.value = await getEnabledTypes();
  }

  // 开始同步第三方App
  function doSync(type, direction) {
    let urls = backEndUrl[type];
    if (!(urls && urls[props.bizType])) {
      console.warn('配置出错');
      return;
    }
    let url = urls[props.bizType] + direction;
    let selectedRowKeys = props.selectedRowKeys;
    let content = '确定要开始同步全部数据吗？可能花费较长时间！';

    if (Array.isArray(selectedRowKeys) && selectedRowKeys.length > 0) {
      content = `确定要开始同步这 ${selectedRowKeys.length} 项吗？`;
    } else {
      selectedRowKeys = [];
    }
    return new Promise((resolve, reject) => {
      const model = Modal.confirm({
        icon: createVNode(ExclamationCircleOutlined),
        title: '同步',
        content,
        onOk: () => {
          model.update({
            keyboard: false,
            okText: '同步中…',
            cancelButtonProps: { disabled: true },
          });
          let params = { ids: selectedRowKeys.join(',') };
          return defHttp
            .get({ url, params }, { isTransformResponse: false })
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
                emit('sync-ok');
              } else {
                if (options != null) {
                  Modal.warning(options);
                } else {
                  createMessage.warning(res.message);
                }
                emit('sync-error');
              }
            })
            .catch(() => model.destroy())
            .finally(() => {
              resolve();
              emit('sync-finally', {
                type,
                direction,
                isToApp: direction === '/toApp',
                isToLocal: direction === '/toLocal',
              });
            });
        },
        onCancel() {
          resolve();
        },
      });
    });
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

  // 获取启用的第三方App
  loadEnabledTypes();
</script>

<style scoped>
  #box:hover {
    border-color: #40a9ff;
  }
</style>
