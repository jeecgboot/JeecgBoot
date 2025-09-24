<!--部门选择框-->
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="register" :title="modalTitle" width="600px" :minHeight="300" :maxHeight="maxHeight" @ok="handleOk" destroyOnClose @visible-change="visibleChange">
      <a-input-search v-if="izOnlySelectDepartPost" placeholder="按岗位名称搜索…" style="margin-bottom: 10px" @search="onSearch" @change="handelSearchChange"/>
      <BasicTree
        ref="treeRef"
        :treeData="treeData"
        :load-data="sync == false ? null : onLoadData"
        v-bind="getBindValue"
        @select="onSelect"
        @check="onCheck"
        :fieldNames="fieldNames"
        :checkedKeys="checkedKeys"
        :expandedKeys="expandedKeys"
        :multiple="multiple"
        :checkStrictly="getCheckStrictly"
        :key="reloadKey"
      >
        <template #title="{ orgCategory, title }">
          <TreeIcon :orgCategory="orgCategory" :title="title"></TreeIcon>
        </template>
      </BasicTree>
      <!--树操作部分-->
      <template #insertFooter>
        <a-dropdown placement="top">
          <template #overlay>
            <a-menu>
              <a-menu-item v-if="multiple" key="1" @click="checkALL(true)">全部勾选</a-menu-item>
              <a-menu-item v-if="multiple" key="2" @click="checkALL(false)">取消全选</a-menu-item>
              <a-menu-item key="3" @click="expandAll(true)">展开全部</a-menu-item>
              <a-menu-item key="4" @click="expandAll(false)">折叠全部</a-menu-item>
            </a-menu>
          </template>
          <a-button style="float: left"> 树操作 <Icon icon="ant-design:up-outlined" /> </a-button>
        </a-dropdown>
      </template>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { queryDepartTreeSync, queryTreeList, queryDepartAndPostTreeSync } from '/@/api/common/api';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { treeProps } from '/@/components/Form/src/jeecg/props/props';
  import { BasicTree, TreeActionType } from '/@/components/Tree';
  import { useTreeBiz } from '/@/components/Form/src/jeecg/hooks/useTreeBiz';
  import {propTypes} from "/@/utils/propTypes";
  import { omit } from 'lodash-es';
  import TreeIcon from '@/components/Form/src/jeecg/components/TreeIcon/TreeIcon.vue';

  export default defineComponent({
    name: 'DeptSelectModal',
    components: {
      TreeIcon,
      BasicModal,
      BasicTree,
    },
    props: {
      ...treeProps,
      //选择框标题
      modalTitle: {
        type: String,
        default: '部门选择',
      },
      // update-begin--author:liaozhiyang---date:20231220---for：【QQYUN-7678】部门组件内容过多没有滚动条（给一个默认最大高）
      maxHeight: {
        type: Number,
        default: 500,
      },
      // update-end--author:liaozhiyang---date:20231220---for：【QQYUN-7678】部门组件内容过多没有滚动条（给一个默认最大高）
      value: propTypes.oneOfType([propTypes.string, propTypes.array]),
      //查询参数
      params: {
        type: Object,
        default: () => ({}),
      },
    },
    emits: ['register', 'getSelectResult', 'close'],
    setup(props, { emit }) {
      //注册弹框
      const [register, { closeModal }] = useModalInner();
      const attrs = useAttrs();
      const treeRef = ref<Nullable<TreeActionType>>(null);
      //加载树key
      const reloadKey = ref<number>(Math.random());
      
      //update-begin-author:taoyan date:2022-10-28 for: 部门选择警告类型不匹配
      let propValue = props.value === ''?[]:props.value;
      // 确保传递给BasicTree的value是数组格式
      if (propValue && typeof propValue === 'string') {
        propValue = propValue.split(',');
      }
      //update-begin-author:liusq date:2023-05-26 for:  [issues/538]JSelectDept组件受 dynamicDisabled 影响
      let temp = Object.assign({}, unref(props), unref(attrs), {value: propValue},{disabled: false});
      const getBindValue = omit(temp, 'multiple');
      //update-end-author:liusq date:2023-05-26 for:  [issues/538]JSelectDept组件受 dynamicDisabled 影响
     //update-end-author:taoyan date:2022-10-28 for: 部门选择警告类型不匹配
      
      const queryUrl = getQueryUrl();
      const [{ visibleChange, checkedKeys, getCheckStrictly, getSelectTreeData, onCheck, onLoadData, treeData, checkALL, expandAll, onSelect, onSearch, expandedKeys }] =
        useTreeBiz(treeRef, queryUrl, getBindValue, props, emit);
      const searchInfo = ref(props.params || {});
      const tree = ref([]);
      //替换treeNode中key字段为treeData中对应的字段
      const fieldNames = {
        key: props.rowKey,
      };
      // {children:'children', title:'title', key:'key' }
      /**
       * 确定选择
       */
      function handleOk() {
        getSelectTreeData((options, values) => {
          //回传选项和已选择的值
          emit('getSelectResult', options, values);
          //关闭弹窗
          closeModal();
        });
      }

      /** 获取查询数据方法 */
      function getQueryUrl() {
        let queryFn = props.izOnlySelectDepartPost ? queryDepartAndPostTreeSync :props.sync ? queryDepartTreeSync : queryTreeList;
        //update-begin-author:taoyan date:2022-7-4 for: issues/I5F3P4 online配置部门选择后编辑，查看数据应该显示部门名称，不是部门代码
        return (params) => queryFn(Object.assign({}, params, { primaryKey: props.rowKey }));
        //update-end-author:taoyan date:2022-7-4 for: issues/I5F3P4 online配置部门选择后编辑，查看数据应该显示部门名称，不是部门代码
      }

      /**
       * 搜索值改变事件
       * @param value
       */
      function handelSearchChange(value) {
        if(!value.target.value){
          reloadKey.value = Math.random();
        }
      }
      return {
        tree,
        handleOk,
        searchInfo,
        treeRef,
        treeData,
        onCheck,
        onSelect,
        checkALL,
        expandAll,
        fieldNames,
        checkedKeys,
        expandedKeys,
        register,
        getBindValue,
        getCheckStrictly,
        visibleChange,
        onLoadData,
        onSearch,
        reloadKey,
        handelSearchChange,
      };
    },
  });
</script>
<style>
  .svg-company {
    width: 18px;
    height: 18px;
    position: relative;
    top: 1px;
    right: 2px;
  }
  .svg-depart,.svg-post {
    width: 14px;
    height: 16px;
    position: relative;
    right: 2px;
  }
</style>
