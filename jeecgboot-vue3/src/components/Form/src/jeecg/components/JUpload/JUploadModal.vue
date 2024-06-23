<template>
  <BasicModal @register="registerModal" :title="modalTitle" :width="width" @ok="handleOk" v-bind="$attrs">
    <JUpload ref="uploadRef" :value="value" v-bind="uploadBinds.props" @change="emitValue" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, unref, reactive, computed, nextTick } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import JUpload from './JUpload.vue';
  import { UploadTypeEnum } from './upload.data';
  import { propTypes } from '/@/utils/propTypes';

  const { createMessage } = useMessage();
  const emit = defineEmits(['change', 'update:value', 'register']);
  const props = defineProps({
    value: propTypes.oneOfType([propTypes.string, propTypes.array]),
    width: propTypes.number.def(520),
  });

  const uploadRef = ref();
  const uploadBinds = reactive({ props: {} as any });
  const modalTitle = computed(() => (uploadBinds.props?.fileType === UploadTypeEnum.image ? '图片上传' : '文件上传'));

  // 注册弹窗
  const [registerModal, { closeModal }] = useModalInner(async (data) => {
    uploadBinds.props = unref(data) || {};
    if ([UploadTypeEnum.image, 'img', 'picture'].includes(uploadBinds.props?.fileType)) {
      uploadBinds.props.fileType = UploadTypeEnum.image;
    } else {
      uploadBinds.props.fileType = UploadTypeEnum.file;
    }
    nextTick(() => uploadRef.value.addActionsListener());
  });

  function handleOk() {
    closeModal();
  }

  function emitValue(value) {
    emit('change', value);
    emit('update:value', value);
  }
</script>
