<template>
  <div v-show="download" class="upload-download-handler">
    <a class="download" title="下载" @click="onDownload">
      <Icon icon="ant-design:download" />
    </a>
  </div>
  <div v-show="canMove" class="upload-mover-handler">
    <a title="向前移动" @click="onMoveForward">
      <Icon icon="ant-design:arrow-left" />
    </a>
    <a title="向后移动" @click="onMoveBack">
      <Icon icon="ant-design:arrow-right" />
    </a>
  </div>
</template>

<script lang="ts" setup>
  import { unref, computed, watchEffect } from 'vue';
  import { Icon } from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();

  const props = defineProps({
    element: { type: HTMLElement, required: true },
    // update-begin--author:liaozhiyang---date:20260831---for:【LHZP-73】批量上传图片后左右移动提示-1
    container: { type: HTMLElement, required: true },
    // update-end--author:liaozhiyang---date:20260831---for:【LHZP-73】批量上传图片后左右移动提示-1
    fileList: { type: Object, required: true },
    mover: { type: Boolean, required: true },
    download: { type: Boolean, required: true },
    emitValue: { type: Function, required: true },
  });
  const list = computed(() => unref(props.fileList));
  // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-73】上传图片操作按钮优化
  const canMove = computed(() => props.mover && list.value.length > 1);
  watchEffect(() => props.element.classList.toggle('has-upload-mover-actions', canMove.value));
  // update-end--author:liaozhiyang---date:20260826---for:【LHZP-73】上传图片操作按钮优化

  // 向前移动图片
  function onMoveForward() {
    let index = getIndex();
    if (index === -1) {
      createMessage.warn('移动失败：' + index);
      return;
    }
    if (index === 0) {
      doSwap(index, unref(list).length - 1);
      return;
    }
    doSwap(index, index - 1);
  }

  // 向后移动图片
  function onMoveBack() {
    let index = getIndex();
    if (index === -1) {
      createMessage.warn('移动失败：' + index);
      return;
    }
    if (index == unref(list).length - 1) {
      doSwap(index, 0);
      return;
    }
    doSwap(index, index + 1);
  }

  function doSwap(oldIndex, newIndex) {
    if (oldIndex !== newIndex) {
      let array: any[] = [...(unref(list) as Array<any>)];
      let temp = array[oldIndex];
      array[oldIndex] = array[newIndex];
      array[newIndex] = temp;
      props.fileList.value = array;
      props.emitValue(array.map((i) => i.response?.message).join(','));
    }
  }

  // update-begin--author:liaozhiyang---date:20260831---for:【LHZP-73】批量上传图片后左右移动提示-1
  function getIndex() {
    const uploadItems = Array.from(props.container.getElementsByClassName('ant-upload-list-item'));
    const index = uploadItems.indexOf(props.element);
    if (index >= 0 && index < unref(list).length) {
      return index;
    }
    return getIndexByUrl();
  }
  // update-end--author:liaozhiyang---date:20260831---for:【LHZP-73】批量上传图片后左右移动提示-1

  function getIndexByUrl() {
    const url = props.element?.getElementsByTagName('img')[0]?.src;
    if (url) {
      const fileList: any = unref(list);
      for (let i = 0; i < fileList.length; i++) {
        let current = fileList[i].url;
        const replace = url.replace(window.location.origin, '');
        if (current === replace || encodeURI(current) === replace) {
          return i;
        }
      }
    }
    return -1;
  }

  function onDownload() {
    const url = props.element?.getElementsByTagName('img')[0]?.src;
    window.open(url);
  }
</script>
