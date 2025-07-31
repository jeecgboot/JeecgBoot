<template>
  <PageWrapper contentFullHeight>
    <a-card :bordered="false" title="版本管理">
      <!--编辑模式-->
      <a-spin v-if="active" :spinning="confirmLoading">
        <a-form ref="formRef" :model="model" :labelCol="labelCol" :wrapperCol="wrapperCol" :rules="validatorRules">
          <a-row>
            <a-col :span="24">
              <a-form-item label="版本" name="appVersion">
                <a-input v-model:value="model.appVersion" placeholder="请输入版本" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="APP安装apk" name="downloadUrl">
                <a-input placeholder="设置APP安装apk" v-model:value="model.downloadUrl">
                  <template #addonAfter>
                    <Icon icon="ant-design:upload-outlined" style="cursor: pointer" @click="showUploadModal('apk')" />
                  </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="APP热更新文件" name="wgtUrl">
                <a-input placeholder="设置APP热更新文件" v-model:value="model.wgtUrl">
                  <template #addonAfter>
                    <Icon icon="ant-design:upload-outlined" style="cursor: pointer" @click="showUploadModal('wgt')" />
                  </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="更新内容">
                <a-textarea :rows="4" v-model:value="model.updateNote" placeholder="请输入更新内容" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <JUploadModal :value="modalValue" :bizPath="filePath" :maxCount="1" @register="registerModel" @change="uploadBack" />
      </a-spin>
      <!--详情模式-->
      <Description v-else class="desc" :column="1" :data="model" :schema="schema" />
      <!--底部按钮-->
      <div class="anty-form-btn" v-if="hasPermission('app:edit:version')">
        <a-button v-if="active" @click="handleSubmit" type="primary" preIcon="ant-design:save-outlined">保存</a-button>
        <a-button v-else @click="active = true" type="primary" preIcon="ant-design:edit-outlined">开启编辑模式</a-button>
      </div>
    </a-card>
  </PageWrapper>
</template>

<script lang="ts" setup name="portalapp-sysAppVersion">
  import { useMessage } from '/@/hooks/web/useMessage';
  import { usePermission } from '@/hooks/web/usePermission';
  import { JUploadModal } from '@/components/Form/src/jeecg/components/JUpload';
  import { useModal } from '@/components/Modal';
  import { reactive, ref, toRaw, unref, onMounted } from 'vue';
  import { PageWrapper } from '@/components/Page';
  import { queryAppVersion, saveAppVersion } from './appVersion.api';
  import { Description, DescItem } from '/@/components/Description/index';

  const { hasPermission } = usePermission();
  const { createMessage } = useMessage();

  const [registerModel, { openModal }] = useModal();
  const confirmLoading = ref(false);
  const active = ref(false);
  const formRef = ref<any>(null);
  const appKey = 'E0CC280';
  const filePath = 'appVersion';
  const uploadType = ref('');
  const modalValue = ref('');
  const labelCol = {
    xs: { span: 24 },
    sm: { span: 5 },
  };
  const wrapperCol = {
    xs: { span: 24 },
    sm: { span: 16 },
  };
  const model = reactive({
    id: 'E0CC280',
    appVersion: '',
    versionNum: 0,
    updateNote: '',
    downloadUrl: '',
    wgtUrl: '',
  });

  /**
   * 初始化表单数据
   * @param record
   */
  async function initFormData() {
    const appVersion = await queryAppVersion({ key: appKey });
    if (appVersion) {
      Object.assign(model, appVersion);
    }
  }

  /**
   * 提交保存版本信息
   */
  function handleSubmit() {
    const form = unref(formRef);
    form.validate().then(async () => {
      let obj = toRaw(model);
      if (obj.appVersion.indexOf('.') != -1) {
        obj.versionNum = Number(obj.appVersion.replace(/\./g, ''));
      }
      obj.id = appKey;
      confirmLoading.value = true;
      await saveAppVersion(obj);
      createMessage.success('保存成功');
      confirmLoading.value = false;
      active.value = false;
    });
  }

  /**
   * 显示设置弹窗
   * @param type
   */
  function showUploadModal(type) {
    uploadType.value = type;
    modalValue.value = type == 'apk' ? model.downloadUrl : model.wgtUrl;
    openModal(true, {
      maxCount: 1,
      bizPath: filePath,
    });
  }

  /**
   *上传返回
   */
  function uploadBack(value) {
    if (unref(uploadType) == 'apk') {
      model.downloadUrl = value;
    } else {
      model.wgtUrl = value;
    }
  }
  //表单校验规则
  const validatorRules = {
    appVersion: [{ required: true, message: '版本不能为空', trigger: 'blur' }],
    downloadUrl: [{ required: true, message: 'APP安装apk不能为空', trigger: 'change' }],
    wgtUrl: [{ required: true, message: 'APP热更新文件不能为空', trigger: 'change' }],
  };
  // 显示字段
  const schema: DescItem[] = [
    {
      field: 'appVersion',
      label: '版本',
    },
    {
      field: 'downloadUrl',
      label: 'APP安装apk',
    },
    {
      field: 'wgtUrl',
      label: 'APP热更新文件',
    },
    {
      field: 'updateNote',
      label: '更新内容',
    },
  ];

  onMounted(() => {
    initFormData();
  });
</script>

<style scoped>
  .anty-form-btn {
    width: 100%;
    text-align: center;
  }
  .anty-form-btn button {
    margin: 20px;
  }
  .approveDiv span {
    margin: 0 20px;
  }
  .desc {
    width: 80%;
    margin: 0 auto;
  }

  :deep(.ant-descriptions-item-label) {
    width: 30% !important;
    min-width: 150px !important;
  }
  :deep(.ant-descriptions-item-content) {
    padding: 16px !important;
    width: 60% !important;
  }
</style>
