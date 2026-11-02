<template>
  <div ref="containerRef" :class="`${prefixCls}-container`">
    <a-upload
      :headers="headers"
      :multiple="multiple"
      :action="uploadUrl"
      :fileList="fileList"
      :disabled="disabled"
      v-bind="bindProps"
      @remove="onRemove"
      @change="onFileChange"
      @preview="onFilePreview"
    >
      <template #removeIcon="{ file }">
        <a-popconfirm
          v-if="removeConfirm"
          :title="`删除这${isImageMode ? '张图片' : '个文件'}？`"
          placement="topRight"
          overlayClassName="j-upload-remove-popconfirm"
          okText="删除"
          cancelText="取消"
          @confirm="confirmRemove(file)"
        >
          <span class="j-upload-remove-trigger" :data-file-uid="file.uid" @click.stop>
            <Icon icon="ant-design:delete-outlined" />
          </span>
        </a-popconfirm>
        <Icon v-else icon="ant-design:delete-outlined" />
      </template>
      <template v-if="isImageMode">
        <div v-if="!isMaxCount">
          <Icon icon="ant-design:plus-outlined" />
          <div class="ant-upload-text">{{ text }}</div>
        </div>
      </template>
      <a-button v-else-if="buttonVisible" :disabled="buttonDisabled">
        <Icon icon="ant-design:upload-outlined" />
        <span>{{ text }}</span>
      </a-button>
    </a-upload>
  </div>
</template>

<script lang="ts" setup>
  import { ref, computed, watch, nextTick, createApp,unref } from 'vue';
  import { Icon } from '/@/components/Icon';
  import { uploadUrl } from '/@/api/common/api';
  import { propTypes } from '/@/utils/propTypes';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { createImgPreview } from '/@/components/Preview/index';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { UploadTypeEnum } from './upload.data';
  import { getFileAccessHttpUrl, getHeaders } from '/@/utils/common/compUtils';
  import UploadItemActions from './components/UploadItemActions.vue';
  import { split } from '/@/utils/index';

  const { createMessage } = useMessage();
  const { prefixCls } = useDesign('j-upload');
  const attrs = useAttrs();
  const emit = defineEmits(['change', 'update:value']);
  const props = defineProps({
    value: propTypes.oneOfType([propTypes.string, propTypes.array]),
    text: propTypes.string.def('上传'),
    fileType: propTypes.string.def(UploadTypeEnum.all),
    /*这个属性用于控制文件上传的业务路径*/
    bizPath: propTypes.string.def('temp'),
    /**
     * 是否返回url，
     * true：仅返回url
     * false：返回fileName filePath fileSize
     */
    returnUrl: propTypes.bool.def(true),
    // 最大上传数量
    maxCount: propTypes.number.def(0),
    buttonVisible: propTypes.bool.def(true),
    multiple: propTypes.bool.def(true),
    // 是否显示左右移动按钮
    mover: propTypes.bool.def(true),
    // 是否显示下载按钮
    download: propTypes.bool.def(true),
    // 删除时是否显示确认框
    removeConfirm: propTypes.bool.def(false),
    beforeUpload: propTypes.func,
    disabled: propTypes.bool.def(false),
    // 替换前一个文件，用于超出最大数量依然允许上传
    replaceLastOne: propTypes.bool.def(false),
  });

  const headers = getHeaders();
  const fileList = ref<any[]>([]);
  const uploadGoOn = ref<boolean>(true);
  // refs
  const containerRef = ref<HTMLElement>();
  const confirmedRemoveUid = ref<string | number>();
  // 是否达到了最大上传数量
  const isMaxCount = computed(() => props.maxCount > 0 && fileList.value.length >= props.maxCount);
  // 当前是否是上传图片模式
  const isImageMode = computed(() => props.fileType === UploadTypeEnum.image);
  // 上传按钮是否禁用
  const buttonDisabled = computed(()=>{
    if(props.disabled === true){
      return true;
    }
    if(isMaxCount.value === true){
      if(props.replaceLastOne === true){
        return false
      }else{
        return true;
      }
    }
    return false
  });
  // 合并 props 和 attrs
  const bindProps = computed(() => {
    // 代码逻辑说明: [issue/455]上传组件传入accept限制上传文件类型无效
    const bind: any = Object.assign({}, props, unref(attrs));

    bind.name = 'file';
    bind.listType = isImageMode.value ? 'picture-card' : 'text';
    bind.class = [bind.class, { 'upload-disabled': props.disabled }];
    bind.data = { biz: props.bizPath, ...bind.data };
    // 代码逻辑说明: 自定义beforeUpload return false，并不能中断上传过程
    if (!bind.beforeUpload) {
      bind.beforeUpload = onBeforeUpload;
    }
    // 如果当前是图片上传模式，就只能上传图片
    if (isImageMode.value && !bind.accept) {
      bind.accept = 'image/*';
    }
    return bind;
  });

  watch(
    () => props.value,
    (val) => {
      const pathValue = Array.isArray(val) ? val.join(',') : val;
      if (props.returnUrl && pathValue === getFileListPathValue()) {
        return;
      }
      if (Array.isArray(val)) {
        if (props.returnUrl) {
          parsePathsValue(pathValue);
        } else {
          parseArrayValue(val);
        }
      } else {
        // 代码逻辑说明: [issues/5327]Upload组件returnUrl为false时上传的字段值返回了一个'[object Object]' ------------
        if (props.returnUrl) {
          parsePathsValue(val);
        } else {
          val && parseArrayValue(JSON.parse(val));
        }
      }
    },
    { immediate: true }
  );

  watch(fileList, () => nextTick(() => addActionsListener()), { immediate: true });

  const antUploadItemCls = 'ant-upload-list-item';

  // Listener
  function addActionsListener() {
    if (!isImageMode.value) {
      return;
    }
    const uploadItems = containerRef.value ? containerRef.value.getElementsByClassName(antUploadItemCls) : null;
    if (!uploadItems || uploadItems.length === 0) {
      return;
    }
    for (const uploadItem of uploadItems) {
      let hasActions = uploadItem.getAttribute('data-has-actions') === 'true';
      if (!hasActions) {
        uploadItem.addEventListener('mouseover', onAddActionsButton);
      }
    }
  }

  // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-73】上传图片操作按钮优化（解决报错导致箭头和下载icon不显示）
  // 添加可左右移动的按钮
  function onAddActionsButton(event: MouseEvent) {
    const uploadItem = event.currentTarget as HTMLElement | null;
    if (!uploadItem?.classList.contains(antUploadItemCls)) {
      return;
    }
    const actions = uploadItem.getElementsByClassName('ant-upload-list-item-actions');
    if (!actions || actions.length === 0) {
      return;
    }
    // 添加操作按钮
    const div = document.createElement('div');
    div.className = 'upload-actions-container';
    createApp(UploadItemActions, {
      element: uploadItem,
      // update-begin--author:liaozhiyang---date:20260831---for:【LHZP-73】批量上传图片后左右移动提示-1
      container: containerRef.value,
      // update-end--author:liaozhiyang---date:20260831---for:【LHZP-73】批量上传图片后左右移动提示-1
      fileList: fileList,
      mover: props.mover,
      download: props.download,
      emitValue: emitValue,
    }).mount(div);
    actions[0].appendChild(div);
    uploadItem.setAttribute('data-has-actions', 'true');
    uploadItem.removeEventListener('mouseover', onAddActionsButton);
  }
  // update-end--author:liaozhiyang---date:20260826---for:【LHZP-73】上传图片操作按钮优化（解决报错导致箭头和下载icon不显示）

  // 解析数据库存储的逗号分割
  function parsePathsValue(paths) {
    if (!paths || paths.length == 0) {
      fileList.value = [];
      return;
    }
    let list: any[] = [];
    // 代码逻辑说明: 【issues/7990】图片参数中包含逗号会错误的识别成多张图
    const result = split(paths);
    for (const item of result) {
      let url = getFileAccessHttpUrl(item);
      list.push({
        uid: uidGenerator(),
        name: getFileName(item),
        status: 'done',
        url: url,
        response: { status: 'history', message: item },
      });
    }
    fileList.value = list;
  }

  // 解析数组值
  function parseArrayValue(array) {
    if (!array || array.length == 0) {
      fileList.value = [];
      return;
    }
    let list: any[] = [];
    for (const item of array) {
      let url = getFileAccessHttpUrl(item.filePath);
      list.push({
        uid: uidGenerator(),
        name: item.fileName,
        url: url,
        status: 'done',
        response: { status: 'history', message: item.filePath },
      });
    }
    fileList.value = list;
  }

  // 文件上传之前的操作
  function onBeforeUpload(file) {
    uploadGoOn.value = true;
    if (isImageMode.value) {
      if (file.type.indexOf('image') < 0) {
        createMessage.warning('请上传图片');
        uploadGoOn.value = false;
        return false;
      }
    }
    // 扩展 beforeUpload 验证
    if (typeof props.beforeUpload === 'function') {
      return props.beforeUpload(file);
    }
    return true;
  }

  // 删除处理事件
  function onRemove(file) {
    if (!props.removeConfirm) {
      return true;
    }
    if (confirmedRemoveUid.value === file.uid) {
      confirmedRemoveUid.value = undefined;
      return true;
    }
    nextTick(() => getRemoveTrigger(file)?.click());
    return false;
  }

  function confirmRemove(file) {
    confirmedRemoveUid.value = file.uid;
    getRemoveTrigger(file)?.closest('button')?.click();
  }

  function getRemoveTrigger(file): HTMLElement | undefined {
    const triggers = containerRef.value?.querySelectorAll<HTMLElement>('.j-upload-remove-trigger') || [];
    return Array.from(triggers).find((trigger) => trigger.dataset.fileUid === String(file.uid));
  }

  // upload组件change事件
  function onFileChange(info) {
    if (!info.file.status && uploadGoOn.value === false) {
      info.fileList.pop();
    }
    let fileListTemp = info.fileList;
    // 限制最大上传数
    if (props.maxCount > 0) {
      let count = fileListTemp.length;
      if (count >= props.maxCount) {
        let diffNum = props.maxCount - fileListTemp.length;
        if (diffNum >= 0) {
          fileListTemp = fileListTemp.slice(-props.maxCount);
        } else {
          return;
        }
      }
    }
    if (info.file.status === 'done') {
      let successFileList = [];
      if (info.file.response.success) {
        successFileList = fileListTemp.map((file) => {
          if (file.response) {
            let reUrl = file.response.message;
            file.url = getFileAccessHttpUrl(reUrl);
          }
          return file;
        });
      }else{
        successFileList = fileListTemp.filter(item=>{
          return item.uid!=info.file.uid;
        });
        // update-begin--author:liaozhiyang---date:20260825---for：【LHZP-329】上传失败，显示接口的message
        createMessage.error(`${info.file.response.message}`);
        // update-end--author:liaozhiyang---date:20260825---for：【LHZP-329】上传失败，显示接口的message
      }
      fileListTemp = successFileList;
    } else if (info.file.status === 'error') {
      createMessage.error(`${info.file.name} 上传失败.`);
    }
    // beforeUpload 返回false，则没有status
    info.file.status && (fileList.value = fileListTemp);
    if (info.file.status === 'done' || info.file.status === 'removed') {
      //returnUrl为true时仅返回文件路径
      if (props.returnUrl) {
        handlePathChange();
      } else {
        //returnUrl为false时返回文件名称、文件路径及文件大小
        let newFileList: any[] = [];
        for (const item of fileListTemp) {
          if (item.status === 'done') {
            let fileJson = {
              fileName: item.name,
              filePath: item.response.message,
              fileSize: item.size,
            };
            newFileList.push(fileJson);
          }else{
            return;
          }
        }
        // 代码逻辑说明: [issues/5327]Upload组件returnUrl为false时上传的字段值返回了一个'[object Object]' ------------
        emitValue(JSON.stringify(newFileList));
      }
    }
  }

  function handlePathChange() {
    let uploadFiles = fileList.value;
    let path = '';
    if (!uploadFiles || uploadFiles.length == 0) {
      path = '';
    }
    let pathList: string[] = [];
    for (const item of uploadFiles) {
      if (item.status === 'done') {
        pathList.push(item.response.message);
      } else {
        return;
      }
    }
    if (pathList.length > 0) {
      path = pathList.join(',');
    }
    emitValue(path);
  }

  function getFileListPathValue() {
    return fileList.value.map((file) => file.response?.message).join(',');
  }

  // 预览文件、图片
  function onFilePreview(file) {
    if (isImageMode.value) {
      createImgPreview({ imageList: [file.url], maskClosable: true });
    } else {
      window.open(file.url);
    }
  }

  function emitValue(value) {
    emit('change', value);
    emit('update:value', value);
  }

  function uidGenerator() {
    return '-' + parseInt(Math.random() * 10000 + 1, 10);
  }

  function getFileName(path) {
    if (path.lastIndexOf('\\') >= 0) {
      let reg = new RegExp('\\\\', 'g');
      path = path.replace(reg, '/');
    }
    return path.substring(path.lastIndexOf('/') + 1);
  }

  defineExpose({
    addActionsListener,
  });
</script>

<style lang="less">
  //noinspection LessUnresolvedVariable
  @prefix-cls: ~'@{namespace}-j-upload';

  .@{prefix-cls} {
    &-container {
      position: relative;

      .upload-disabled {
        .ant-upload-list-item {
          .anticon-close {
            display: none;
          }

          .anticon-delete {
            display: none;
          }
        }

        .ant-upload-list-item-actions {
          display: block !important;

          a,
          a.anticon,
          .anticon,
          .anticon-eye,
          .anticon-download,
          .upload-action-icon,
          .app-iconify {
            pointer-events: auto !important;
            cursor: pointer !important;
            color: #fff !important;
            opacity: 1 !important;
          }

          svg,
          svg path {
            color: #fff !important;
            fill: #fff !important;
          }
        }

        /* update-begin-author:taoyan date:2022-5-24 for:VUEN-1093详情界面 图片下载按钮显示不全*/
        .upload-download-handler {
          right: 6px !important;
        }
        /* update-end-author:taoyan date:2022-5-24 for:VUEN-1093详情界面 图片下载按钮显示不全*/
      }
      .ant-upload-text-icon {
        color: @primary-color;
      }
      // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-73】上传图片操作按钮优化
      .ant-upload-list-item {
        &:not(.has-upload-mover-actions) {
          .ant-upload-list-item-actions {
            grid-template-rows: 20px;
            row-gap: 0;
          }
        }

        .ant-upload-list-item-actions {
          display: grid;
          grid-template-columns: repeat(6, 10px);
          grid-template-rows: repeat(2, 20px);
          align-items: center;
          justify-content: center;
          gap: 8px 2px;

          > a,
          > button,
          .j-upload-remove-trigger,
          .upload-actions-container a {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 20px;
            height: 20px;
            margin: 0;
            padding: 0;
            border: 0;
            border-radius: 4px;
            background: transparent;
            color: rgba(255, 255, 255, 0.65) !important;
            font-size: 16px;
            line-height: 1;
            opacity: 1;
            cursor: pointer;
            transition: color 0.2s, background-color 0.2s;
            grid-column: span 2;

            &:hover {
              background-color: rgba(255, 255, 255, 0.2);
              color: #fff !important;
            }
          }

          .anticon,
          .app-iconify,
          svg,
          svg path {
            color: inherit !important;
            fill: currentColor;
          }
        }

        .upload-actions-container {
          display: contents;

          .upload-mover-handler,
          .upload-download-handler {
            display: contents;
          }

          .upload-mover-handler {
            a:first-child {
              grid-row: 2;
              grid-column: 2 / span 2;
            }

            a:last-child {
              grid-row: 2;
              grid-column: 4 / span 2;
            }
          }
        }
      }
      // update-end--author:liaozhiyang---date:20260826---for:【LHZP-73】上传图片操作按钮优化
    }
  }

  .j-upload-remove-popconfirm {
    .ant-popover-inner {
      min-width: 148px;
      padding: 8px 10px;
    }

    .ant-popconfirm-message-icon {
      display: none;
    }

    .ant-popconfirm-message-title {
      margin-inline-start: 0;
    }

    .ant-popconfirm-buttons {
      margin-top: 6px;

      .ant-btn {
        padding-inline: 6px;
        box-shadow: none;
      }
    }
  }
</style>
