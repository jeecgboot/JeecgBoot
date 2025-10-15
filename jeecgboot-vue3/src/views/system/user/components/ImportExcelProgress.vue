<template>
  <div>
    <a-upload
      :headers="headers"
      :action="uploadAction"
      :showUploadList="false"
      :multiple="false"
      :maxCount="1"
      :data="uploadData"
      accept=".xls,.xlsx"
      @change="onFileChange"
      :beforeUpload="beforeUpload"
      ref="uploadRef"
      style="width: 100%"
    >
      <a-button type="primary" preIcon="ant-design:import-outlined" v-if="percent === 0 || percent === 100">{{ btnText }}</a-button>
      <a-button @click.prevent.stop="progressOpen = true" type="primary" v-else>
        <LoadingOutlined />
        导入中
      </a-button>
    </a-upload>
    <a-modal v-model:open="progressOpen" title="文件上传" :body-style="{ padding: '20px' }" :destroyOnClose="true" :footer="null">
      <div>
        <div style="display: flex">
          <div class="label">当前上传文件：</div>
          <div class="file-name">{{ fileName }}</div>
        </div>
        <div style="margin-top: 10px; display: flex">
          <div class="label">当前文件上传进度:</div>
          <a-progress :percent="percent" style="width: 300px; margin-left: 10px" :format="(percent) => `${percent}%`"></a-progress>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts">
  import { defineComponent, onUnmounted, ref, computed, watch } from 'vue';
  import { getTenantId, getToken } from '@/utils/auth';
  import { randomString } from '@/utils/common/compUtils';
  import { useMessage } from '@/hooks/web/useMessage';
  import { useGlobSetting } from '@/hooks/setting';
  import { defHttp } from '@/utils/http/axios';
  import { LoadingOutlined } from '@ant-design/icons-vue';

  export default defineComponent({
    name: 'ImportExcelProgress',
    components: { LoadingOutlined },
    props: {
      //上传路径
      uploadUrl: {
        type: String,
        required: true,
      },
      //按钮文字
      btnText: {
        type: String,
        default: '导入',
      },
      //上传类型
      type: {
        type: String,
        default: 'user',
      },
    },
    emit: ['success', 'register'],
    setup(props, { emit }) {
      const fileName = ref<string>('');
      //上传数据
      const uploadData = ref({});
      //上传请求头
      const headers = ref({});
      const { createMessage, createWarningModal } = useMessage();
      //文件上传定时任务
      const fileUploadInterval = ref<any>();
      const glob = useGlobSetting();
      const progressOpen = ref(false);
      //上传进度
      const percent = ref<number>(0);
      const uploadRef = ref();
      //随机数
      const uuid = ref();
      const baseApiUrl = glob.domainUrl;
      const uploadAction = computed(() => {
        return baseApiUrl + props.uploadUrl;
      });
      //文件发生改变回调
      function onFileChange(info) {
        if (info.file.name) {
          fileName.value = info.file.name;
        }
        if (info.file.status === 'done') {
          if (info.file.response.success) {
            if (info.file.response && info.file.response.code === 201) {
              let {
                message,
                result: { msg, fileUrl, fileName },
              } = info.file.response;
              let href = glob.uploadUrl + fileUrl;
              createWarningModal({
                title: message,
                centered: false,
                content: `<div>
                        <span>${msg}</span><br/> 
                        <span>具体详情请<a href = ${href} download = ${fileName}> 点击下载 </a> </span> 
                      </div>`,
              });
              percent.value = 100;
              emit('success');
            } else {
              createMessage.success('文件导入成功!');
              percent.value = 100;
              emit('success');
            }
          } else {
            createMessage.error(info.file.response.message || `${info.file.name} 导入失败.`);
          }
          clear();
        } else if (info.file.status === 'error' || (info.file.response && info.file.response.code === 500)) {
          clear();
          createMessage.error(info.file.response?.message || `导入失败，可能原因文件超出上传大小，请查看控制台或服务端日志`);
        }
      }

      /**
       * 清除定时器
       */
      function clear() {
        //查看状态 讲以导入的改成导入失败
        clearInterval(fileUploadInterval.value);
      }

      /**
       * 上传之前的回调
       */
      function beforeUpload() {
        percent.value = 1;
        headers.value = {
          'X-Access-Token': getToken(),
          'X-Tenant-Id': getTenantId() ? getTenantId() : '0',
        };
        uuid.value = randomString(16);
        uploadData.value['fileKey'] = uuid.value;
        progressOpen.value = true;
        fileUploadInterval.value = setInterval(() => {
          defHttp
            .get({ url: '/sys/user/getUploadFileProgress', params: { fileKey: uuid.value, type: props.type } }, { isTransformResponse: false })
            .then((res) => {
              if (res.success) {
                if (res.result) {
                  percent.value = Math.round(res.result);
                  if (res.result == 100) {
                    clear();
                  }
                }
              }
            })
            .catch(() => {
              clear();
            });
        }, 2000);
      }

      onUnmounted(() => {
        clear();
      });

      return {
        fileName,
        uploadData,
        headers,
        onFileChange,
        beforeUpload,
        progressOpen,
        percent,
        uploadAction,
        uploadRef,
      };
    },
  });
</script>

<style scoped lang="less">
  .label {
    font-size: 14px;
    color: #666;
  }
  .file-name {
    color: #151515;
    font-size: 12px;
    margin-top: 2px;
  }
</style>
