import { computed } from 'vue';
import { fileGetValue, fileSetValue, useJVxeUploadCell } from '/@/components/jeecg/JVxeTable/src/hooks/cells/useJVxeUploadCell';
import { uploadUrl } from '/@/api/common/api';
import { JUploadModal, UploadTypeEnum } from '/@/components/Form/src/jeecg/components/JUpload';
import { useModal } from '/@/components/Modal';
import { JVxeComponent } from '/@/components/jeecg/JVxeTable/src/types/JVxeComponent';
import { Icon } from '/@/components/Icon';
import { Dropdown } from 'ant-design-vue';
import { LoadingOutlined } from '@ant-design/icons-vue';

export function useFileCell(props, fileType: UploadTypeEnum, options?) {
  const setup = useJVxeUploadCell(props, { token: true, action: uploadUrl, ...options });

  const { innerFile, handleChangeCommon, originColumn } = setup;
  const [registerModel, { openModal }] = useModal();

  // 截取文件名
  const ellipsisFileName = computed(() => {
    let length = 5;
    let file = innerFile.value;
    if (!file || !file.name) {
      return '';
    }
    if (file.name.length > length) {
      return file.name.substr(0, length) + '…';
    }
    return file.name;
  });

  const modalValue = computed(() => {
    if (innerFile.value) {
      if (innerFile.value['url']) {
        return innerFile.value['url'];
      } else if (innerFile.value['path']) {
        return innerFile.value['path'];
      }
    }
    return '';
  });

  const maxCount = computed(() => {
    let maxCount = originColumn.value.maxCount;
    // online 扩展JSON
    if (originColumn.value && originColumn.value.fieldExtendJson) {
      let json = JSON.parse(originColumn.value.fieldExtendJson);
      maxCount = json.uploadnum ? json.uploadnum : 0;
    }
    return maxCount ?? 0;
  });

  // 点击更多按钮
  function handleMoreOperation() {
    openModal(true, {
      removeConfirm: true,
      mover: true,
      download: true,
      ...originColumn.value.props,
      maxCount: maxCount.value,
      fileType: fileType,
    });
  }

  // 更多上传回调
  function onModalChange(path) {
    if (path) {
      // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-235】富文本禁用状态下图片上传按钮文字看不清
      if (innerFile.value === null) {
        innerFile.value = {};
      }
      // update-end-author:liaozhiyang---date:20240524---for：【TV360X-235】富文本禁用状态下图片上传按钮文字看不清
      innerFile.value.path = path;
      handleChangeCommon(innerFile.value);
    } else {
      //update-begin-author:liusq date:2023-06-05 for: [issues/530]JVxeTable 的JVxeTypes.image类型，无法全部删除上传图片
      handleChangeCommon(null);
      //update-end-author:liusq date:2023-06-05 for:  [issues/530]JVxeTable 的JVxeTypes.image类型，无法全部删除上传图片
    }
  }

  return {
    ...setup,
    modalValue,
    maxCount,
    ellipsisFileName,
    registerModel,
    onModalChange,
    handleMoreOperation,
  };
}

export const components = {
  Icon,
  Dropdown,
  LoadingOutlined,
  JUploadModal,
};

export const enhanced = {
  switches: { visible: true },
  getValue: (value) => fileGetValue(value),
  setValue: (value) => fileSetValue(value),
} as JVxeComponent.EnhancedPartial;
