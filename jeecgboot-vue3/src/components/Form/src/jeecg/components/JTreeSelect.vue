<template>
  <a-tree-select
    v-if="show"
    allowClear
    labelInValue
    style="width: 100%"
    :getPopupContainer="(node) => node?.parentNode"
    :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
    :placeholder="placeholder"
    :loadData="asyncLoadTreeData"
    :value="treeValue"
    :treeData="treeData"
    :multiple="multiple"
    v-bind="attrs"
    @change="onChange"
    @search="onSearch"
    :tree-checkable="treeCheckAble"
  >
  </a-tree-select>
</template>
<script lang="ts" setup>
  /*
   * 异步树加载组件 通过传入表名 显示字段 存储字段 加载一个树控件
   * <j-tree-select dict="aa_tree_test,aad,id" pid-field="pid" ></j-tree-select>
   * */
  import { ref, watch, unref, nextTick } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { TreeSelect } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  enum Api {
    url = '/sys/dict/loadTreeData',
    view = '/sys/dict/loadDictItem/',
  }

  const props = defineProps({
    value: propTypes.string.def(''),
    placeholder: propTypes.string.def('请选择'),
    dict: propTypes.string.def('id'),
    parentCode: propTypes.string.def(''),
    pidField: propTypes.string.def('pid'),
    //update-begin---author:wangshuai ---date:20220620  for：JTreeSelect组件pidValue还原成空，否则会影响自定义组件树示例------------
    pidValue: propTypes.string.def(''),
    //update-end---author:wangshuai ---date:20220620  for：JTreeSelect组件pidValue还原成空，否则会影响自定义组件树示例--------------
    hasChildField: propTypes.string.def(''),
    converIsLeafVal: propTypes.integer.def(1),
    condition: propTypes.string.def(''),
    multiple: propTypes.bool.def(false),
    loadTriggleChange: propTypes.bool.def(false),
    reload: propTypes.number.def(1),
    //update-begin-author:taoyan date:2022-11-8 for: issues/4173 Online JTreeSelect控件changeOptions方法未生效
    url: propTypes.string.def(''),
    params: propTypes.object.def({}),
    //update-end-author:taoyan date:2022-11-8 for: issues/4173 Online JTreeSelect控件changeOptions方法未生效
    //update-begin---author:wangshuai date: 20230202 for: 新增是否有复选框
    //默认没有选择框
    treeCheckAble: propTypes.bool.def(false),
    //update-end---author:wangshuai date: 20230202 for: 新增是否有复选框
    hiddenNodeKey: propTypes.string.def(''),
  });
  const attrs = useAttrs();
  const emit = defineEmits(['change', 'update:value']);
  const { createMessage } = useMessage();
  //树形下拉数据
  const treeData = ref<any[]>([]);
  //选择数据
  const treeValue = ref<any>(null);
  const tableName = ref<any>('');
  const text = ref<any>('');
  const code = ref<any>('');
  const show = ref<boolean>(true);
  /**
   * 监听value数据并初始化
   */
  watch(
    () => props.value,
    () => loadItemByCode(),
    { deep: true, immediate: true }
  );
  /**
   * 监听dict变化
   */
  watch(
    () => props.dict,
    () => {
      initDictInfo();
      loadRoot();
    },
    { deep: true, immediate: true }
  );
  // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
  watch(
    () => props.hiddenNodeKey,
    () => {
      if (treeData.value?.length && props.hiddenNodeKey) {
        handleHiddenNode(treeData.value);
        treeData.value = [...treeData.value];
      }
    }
  );
  // update-end--author:liaozhiyang---date:20240529---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点

  //update-begin-author:taoyan date:2022-5-25 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的
  watch(
    () => props.reload,
    async () => {
      treeData.value = [];
      // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-88】online树表重复新增时父节点数据加载不全且已开的子节点不重新加载
      show.value = false;
      nextTick(() => {
        show.value = true;
      });
      // update-end--author:liaozhiyang---date:20240524---for：【TV360X-88】online树表重复新增时父节点数据加载不全且已开的子节点不重新加载
      await loadRoot();
    },
    {
      immediate: false,
    }
  );
  //update-end-author:taoyan date:2022-5-25 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的

  /**
   * 根据code获取下拉数据并回显
   */
  async function loadItemByCode() {
    if (!props.value || props.value == '0') {
      if(props.multiple){
        treeValue.value = [];
      }else{
        treeValue.value = { label: null, value: null };
      }
    } else {
      //update-begin-author:taoyan date:2022-11-8 for: issues/4173 Online JTreeSelect控件changeOptions方法未生效
      if(props.url){
        getItemFromTreeData();
      }else{
        let params = { key: props.value };
        let result = await defHttp.get({ url: `${Api.view}${props.dict}`, params }, { isTransformResponse: false });
        if (result.success) {
          //update-start-author:liaozhiyang date:2023-7-17 for:【issues/5141】使用JtreeSelect 组件 控制台报错
          if(props.multiple){
            let values = props.value.split(',');
            treeValue.value = result.result.map((item, index) => ({
              key: values[index],
              value: values[index],
              label: item,
            }));
          }else{
            treeValue.value = { key: props.value, value: props.value, label: result.result[0] };
          }
          //update-end-author:liaozhiyang date:2023-7-17 for:【issues/5141】使用JtreeSelect 组件 控制台报错
          onLoadTriggleChange(result.result[0]);
        }
      }
      //update-end-author:taoyan date:2022-11-8 for: issues/4173 Online JTreeSelect控件changeOptions方法未生效
    }
  }

  function onLoadTriggleChange(text) {
    //只有单选才会触发
    if (!props.multiple && props.loadTriggleChange) {
      emit('change', props.value, text);
    }
  }

  /**
   * 初始化数据
   */
  function initDictInfo() {
    let arr = props.dict?.split(',');
    tableName.value = arr[0];
    text.value = arr[1];
    code.value = arr[2];
  }

  /**
   * 加载下拉树形数据
   */
  async function loadRoot() {
    let params = {
      pid: props.pidValue,
      pidField: props.pidField,
      hasChildField: props.hasChildField,
      converIsLeafVal: props.converIsLeafVal,
      condition: props.condition,
      tableName: unref(tableName),
      text: unref(text),
      code: unref(code),
    };
    let res = await defHttp.get({ url: Api.url, params }, { isTransformResponse: false });
    if (res.success && res.result) {
      for (let i of res.result) {
        i.value = i.key;
        i.isLeaf = !!i.leaf;
      }
      // update-begin--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
      handleHiddenNode(res.result);
      // update-end--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
      treeData.value = [...res.result];
    } else {
      console.log('数根节点查询结果异常', res);
    }
  }

  /**
   * 异步加载数据
   */
  async function asyncLoadTreeData(treeNode) {
    if (treeNode.dataRef.children) {
      return Promise.resolve();
    }
    if(props.url){
      return Promise.resolve();
    }
    let pid = treeNode.dataRef.key;
    let params = {
      pid: pid,
      pidField: props.pidField,
      hasChildField: props.hasChildField,
      converIsLeafVal: props.converIsLeafVal,
      condition: props.condition,
      tableName: unref(tableName),
      text: unref(text),
      code: unref(code),
    };
    let res = await defHttp.get({ url: Api.url, params }, { isTransformResponse: false });
    if (res.success) {
      for (let i of res.result) {
        i.value = i.key;
        i.isLeaf = !!i.leaf;
      }
      // update-begin--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
      handleHiddenNode(res.result);
      // update-end--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
      //添加子节点
      addChildren(pid, res.result, treeData.value);
      treeData.value = [...treeData.value];
    }
    return Promise.resolve();
  }

  /**
   * 加载子节点
   */
  function addChildren(pid, children, treeArray) {
    if (treeArray && treeArray.length > 0) {
      for (let item of treeArray) {
        if (item.key == pid) {
          if (!children || children.length == 0) {
            item.isLeaf = true;
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

  /**
   * 选中树节点事件
   */
  function onChange(value) {
    if (!value) {
      emitValue('');
    } else if (value instanceof Array) {
      emitValue(value.map((item) => item.value).join(','));
    } else {
      emitValue(value.value);
    }
    treeValue.value = value;
  }

  function emitValue(value) {
    emit('change', value);
    emit('update:value', value);
  }

  /**
   * 文本框值变化
   */
  function onSearch(value) {
    console.log(value);
  }

  /**
   * 校验条件配置是否有误
   */
  function validateProp() {
    let mycondition = props.condition;
    return new Promise((resolve, reject) => {
      if (!mycondition) {
        resolve();
      } else {
        try {
          let test = JSON.parse(mycondition);
          if (typeof test == 'object' && test) {
            resolve();
          } else {
            createMessage.error('组件JTreeSelect-condition传值有误，需要一个json字符串!');
            reject();
          }
        } catch (e) {
          createMessage.error('组件JTreeSelect-condition传值有误，需要一个json字符串!');
          reject();
        }
      }
    });
  }

  //update-begin-author:taoyan date:2022-11-8 for: issues/4173 Online JTreeSelect控件changeOptions方法未生效
  watch(()=>props.url, async (val)=>{
    if(val){
      await loadRootByUrl();
    }
  });

  /**
   * 根据自定义的请求地址加载数据
   */
  async function loadRootByUrl(){
    let url = props.url;
    let params = props.params;
    let res = await defHttp.get({ url, params }, { isTransformResponse: false });
    if (res.success && res.result) {
      for (let i of res.result) {
        i.key = i.value;
        i.isLeaf = !!i.leaf;
      }
      // update-begin--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
      handleHiddenNode(res.result);
      // update-end--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
      treeData.value = [...res.result];
    } else {
      console.log('数根节点查询结果异常', res);
    }
  }

  /**
   * 根据已有的树数据 翻译选项
   */
  function getItemFromTreeData(){
    let data = treeData.value;
    let arr = []
    findChildrenNode(data, arr);
    if(arr.length>0){
      treeValue.value = arr
      onLoadTriggleChange(arr[0]);
    }
  }

  /**
   * 递归找子节点
   * @param data
   * @param arr
   */
  function findChildrenNode(data, arr){
    let val = props.value;
    if(data && data.length){
      for(let item of data){
        if(val===item.value){
          arr.push({
            key: item.key,
            value: item.value,
            label: item.label||item.title
          })
        }else{
          findChildrenNode(item.children, arr)
        }
      }
    }
  }
  //update-end-author:taoyan date:2022-11-8 for: issues/4173 Online JTreeSelect控件changeOptions方法未生效

  /**
   * 2024-05-23
   * liaozhiyang
   * 过滤掉指定节点(包含其子孙节点)
   */
  function handleHiddenNode(data) {
    if (props.hiddenNodeKey && data?.length) {
      for (let i = 0, len = data.length; i < len; i++) {
        const item = data[i];
        if (item.key == props.hiddenNodeKey) {
          data.splice(i, 1);
          i--;
          len--;
          return;
        }
      }
    }
  }
  // onCreated
  validateProp().then(() => {
    initDictInfo();
    loadRoot();
    loadItemByCode();
  });
</script>

<style lang="less"></style>
