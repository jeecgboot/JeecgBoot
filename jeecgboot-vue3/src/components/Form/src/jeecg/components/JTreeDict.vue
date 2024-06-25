<template>
  <TreeSelect
    :class="prefixCls"
    :value="treeValue"
    :treeData="treeData"
    :loadData="asyncLoadTreeData"
    allowClear
    labelInValue
    :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
    style="width: 100%"
    v-bind="attrs"
    @change="onChange"
  >
  </TreeSelect>
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { TreeSelect } from 'ant-design-vue';

  enum Api {
    view = '/sys/category/loadOne',
    root = '/sys/category/loadTreeRoot',
    children = '/sys/category/loadTreeChildren',
  }

  const { prefixCls } = useDesign('j-tree-dict');
  const props = defineProps({
    // v-model:value
    value: propTypes.string.def(''),
    field: propTypes.string.def('id'),
    parentCode: propTypes.string.def(''),
    async: propTypes.bool.def(false),
  });
  const attrs = useAttrs();
  const emit = defineEmits(['change', 'update:value']);

  const treeData = ref<any[]>([]);
  const treeValue = ref<any>(null);

  watch(
    () => props.value,
    () => loadViewInfo(),
    { deep: true, immediate: true }
  );
  watch(
    () => props.parentCode,
    () => loadRoot(),
    { deep: true, immediate: true }
  );

  async function loadViewInfo() {
    if (!props.value || props.value == '0') {
      treeValue.value = { value: null, label: null };
    } else {
      let params = { field: props.field, val: props.value };
      let result = await defHttp.get({ url: Api.view, params });
      treeValue.value = {
        value: props.value,
        label: result.name,
      };
    }
  }

  async function loadRoot() {
    let params = {
      async: props.async,
      pcode: props.parentCode,
    };
    let result = await defHttp.get({ url: Api.root, params });
    treeData.value = [...result];
    handleTreeNodeValue(result);
  }

  async function asyncLoadTreeData(treeNode) {
    if (!props.async || treeNode.dataRef.children) {
      return Promise.resolve();
    }
    let pid = treeNode.dataRef.key;
    let params = { pid: pid };
    let result = await defHttp.get({ url: Api.children, params });
    handleTreeNodeValue(result);
    addChildren(pid, result, treeData.value);
    treeData.value = [...treeData.value];
    return Promise.resolve();
  }

  function addChildren(pid, children, treeArray) {
    if (treeArray && treeArray.length > 0) {
      for (let item of treeArray) {
        if (item.key == pid) {
          if (!children || children.length == 0) {
            item.leaf = true;
          } else {
            item.children = children;
          }
          break;
        } else {
          addChildren(pid, children, item.children);
        }
      }
    }
  }

  function handleTreeNodeValue(result) {
    let storeField = props.field == 'code' ? 'code' : 'key';
    for (let i of result) {
      i.value = i[storeField];
      i.isLeaf = i.leaf;
      if (i.children && i.children.length > 0) {
        handleTreeNodeValue(i.children);
      }
    }
  }

  function onChange(value) {
    if (!value) {
      emitValue('');
    } else {
      emitValue(value.value);
    }
    treeValue.value = value;
  }

  function emitValue(value) {
    emit('change', value);
    emit('update:value', value);
  }
</script>

<style lang="less">
  //noinspection LessUnresolvedVariable
  @prefix-cls: ~'@{namespace}-j-tree-dict';

  .@{prefix-cls} {
  }
</style>
