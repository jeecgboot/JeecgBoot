<template>
  <div v-show="download" class="upload-download-handler">
    <a class="download" title="下载" @click="onDownload">
      <Icon icon="ant-design:download" />
    </a>
  </div>
  <div v-show="mover && list.length > 1" class="upload-mover-handler">
    <a title="向前移动" @click="onMoveForward">
      <Icon icon="ant-design:arrow-left" />
    </a>
    <a title="向后移动" @click="onMoveBack">
      <Icon icon="ant-design:arrow-right" />
    </a>
  </div>
</template>

<script lang="ts" setup>
  import { unref, computed } from 'vue';
  import { Icon } from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();

  const props = defineProps({
    element: { type: HTMLElement, required: true },
    fileList: { type: Object, required: true },
    mover: { type: Boolean, required: true },
    download: { type: Boolean, required: true },
    emitValue: { type: Function, required: true },
  });
  const list = computed(() => unref(props.fileList));

  // 向前移动图片
  function onMoveForward() {
    let index = getIndexByUrl();
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
    let index = getIndexByUrl();
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
      props.emitValue(array.map((i) => i.url).join(','));
    }
  }

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
