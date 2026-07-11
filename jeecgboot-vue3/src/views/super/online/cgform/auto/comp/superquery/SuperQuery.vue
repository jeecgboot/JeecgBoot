<template>
    <!-- 按钮区域 -->
    <div class="j-super-query-button">
        <a-tooltip v-if="superQueryFlag" :mouseLeaveDelay="0.2">
            <template #title>
                <span>执行查询中...</span>
                <divider type="vertical" style="background-color: #fff"/>
                <a @click="handleStop">取消查询</a>
            </template>
            <a-button-group>
                <a-button type="primary" @click="handleOpen">
                    <AppstoreTwoTone :spin="true"/>
                    <span>{{queryBtnCfg.buttonName}}</span>
                </a-button>
            </a-button-group>
        </a-tooltip>
        <a-button v-else type="primary" :preIcon="queryBtnCfg.buttonIcon" @click="handleOpen">
            <span>{{queryBtnCfg.buttonName}}</span>
        </a-button>
    </div>

    <!-- 高级查询弹框 -->
    <teleport to="body">
        <BasicModal :title="queryBtnCfg.buttonName + '构造器'" wrap-class-name="j-super-query-modal" :canFullscreen="true" :width="850" @register="registerFormModal" @ok="handleSubmit" @fullScreen="handleFullScreen">
            <template #footer>
                <div style="float: left">
                    <a-button :loading="loading" @click="handleReset">清空</a-button>
                    <a-button :loading="loading" @click="handleSave">保存查询</a-button>
                </div>

                <a-button key="submit" type="primary" @click="handleSubmit">执行查询</a-button>
                <a-button key="back" @click="handleCancel">关闭</a-button>
            </template>

            <a-empty v-if="dynamicRowValues.values.length == 0">
                <div slot="description">
                    <span>没有任何查询条件</span>
                    <a-divider type="vertical"/>
                    <a @click="addOne(-1)">点击新增</a>
                </div>
            </a-empty>

            <a-row :class="'j-super-query-modal-content'">
                <a-col :sm="24" :md="24">
                    <a-row v-show="dynamicRowValues.values.length > 0">
                        <a-col :md="12" :xs="24">
                            <a-form-item label="匹配模式" :labelCol="{md: 6,xs:24}" :wrapperCol="{md: 18,xs:24}" style="width: 100%;">
                                <a-select v-model:value="matchType" :getPopupContainer="node=>node?.parentNode" style="width: 100%;">
                                    <a-select-option value="and">AND（所有条件匹配）</a-select-option>
                                    <a-select-option value="or">OR（任意一个匹配）</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>

                    <a-form v-show="dynamicRowValues.values.length > 0" ref="formRef" :class="'jee-super-query-form'" :model="dynamicRowValues" @finish="onFinish" :style="queryFormStyle">
                        <a-space v-for="(item, index) in dynamicRowValues.values" :key="item.key" style="display: flex; margin-bottom: 8px" :align="item.curLineAlign ? item.align : 'baseline'">

                            <a-form-item class="field-clos" :name="['values', index, 'field']" >
                                <a-tree-select
                                        :popupClassName="getTreePopupClass"
                                        style="width:100%"
                                        placeholder="请选择字段"
                                        v-model:value="item.field"
                                        show-search
                                        tree-node-filter-prop="title"
                                        allow-clear
                                        tree-default-expand-all
                                        :dropdown-style="{ maxHeight: `${fieldTreeSelectHeight}px`, overflow: 'auto' }"
                                        :listHeight="fieldTreeSelectHeight - 20"
                                        @change="handleChangeField(item)"
                                        :tree-data="fieldTreeData">
                                </a-tree-select>
                            </a-form-item>
                            <a-form-item class="rule-clos" :name="['values', index, 'rule']">
                                <a-select style="width:100%" placeholder="请选择匹配规则" v-model:value="item.rule">
                                    <a-select-option :value="qItem.value" v-for="qItem in getQueryCondition(item)" :key="qItem.value">{{qItem.label}}</a-select-option>
                                    <!-- 当前表单设计器内专用 -->
                                   <!-- <a-select-option value="empty">为空</a-select-option>
                                    <a-select-option value="not_empty">不为空</a-select-option>-->
                                    <!-- 当前表单设计器内专用 -->
                                </a-select>
                            </a-form-item>
                            <a-form-item class="component-clos" :name="['values', index, 'val']">
                                <online-super-query-val-component style="width:100%" :schema="getSchema(item, index)" :formModel="item" :setFormModel="(key, value)=>{setFormModel(key, value, item)}" @submit="handleSubmit" />
                            </a-form-item>

                            <a-form-item>
                                <a-button @click="addOne(index)" style="margin-right: 6px">
                                    <PlusOutlined #icon/>
                                </a-button>
                                <a-button @click="removeOne(item)">
                                    <MinusCircleOutlined #icon/>
                                </a-button>
                            </a-form-item>
                        </a-space>
                    </a-form>
                </a-col>
            </a-row>
          <!-- 查询记录 -->
          <a-card :class="['j-super-query-history-card', {'collapsed': historyCollapsed}]" :bordered="false">
            <template #title><div>保存的查询</div></template>
            <a-empty
                v-if="saveTreeData.length === 0"
                class="j-super-query-history-empty"
                :image="simpleImage"
                description="没有保存的查询"
            />
            <a-tree
                v-else
                class="j-super-query-history-tree"
                :treeData="saveTreeData"
                :selectedKeys="[]"
                :show-icon="true"
                @select="handleTreeSelect">
              <template #title="{title}">
                <div>
                  <span :title="title">{{(title.length>10)?(title.substring(0, 10)+'...'):title }}</span>
                  <a-popconfirm title="确定删除吗？" @confirm="handleRemoveSaveInfo(title)">
                    <span class="icon-cancle" @click="(e)=>e.stopPropagation()"><close-circle-outlined /></span>
                  </a-popconfirm>
                </div>
              </template>
              <!-- antd-2是这么写的 升级到3会也许会改变写法 -->
              <template #custom>
                <file-text-outlined/>
              </template>
            </a-tree>
            <div class="collapse-box" @click="historyCollapsed=!historyCollapsed">
              <Icon v-if="historyCollapsed" icon="ant-design:caret-left"/>
              <Icon v-else icon="ant-design:caret-right"/>
            </div>
          </a-card>
        </BasicModal>
    </teleport>

    <!-- 保存信息弹框 -->
    <a-modal title="请输入保存的名称" :open="saveInfo.visible" @cancel="saveInfo.visible=false" @ok="doSaveQueryInfo" :confirmLoading="saveModalLoading">
        <div style="height:80px;line-height:75px;width:100%;text-align: center">
            <a-input v-model:value="saveInfo.title" style="width:90%" placeholder="请输入保存的名称"></a-input>
        </div>
    </a-modal>
</template>

<script lang="ts">

  import {ref, watch, computed} from 'vue'
  import { BasicModal, useModal } from '/@/components/Modal';
  import { useSuperQuery } from './useSuperQuery';
  import OnlineSuperQueryValComponent from './SuperQueryValComponent.vue';
  import { MinusCircleOutlined, PlusOutlined, FileTextOutlined, CloseCircleOutlined, AppstoreTwoTone } from '@ant-design/icons-vue';
  import { Divider, Empty, Popconfirm } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import dayjs from 'dayjs';
  import { cloneDeep } from 'lodash-es';
  import { useConditionFilter } from '/@/utils/index';
  import { useDesign } from '/@/hooks/web/useDesign';
//const { BasicModal, useModal } = defineAsyncComponent(()=>import('/@/components/Modal'));
  export default{
    name: "OnlineSuperQuery",
    props: {
      config:{
        type: Object,
        default: []
      },
      status:{
        type: Boolean,
        default: false
      },
      online: {
        type: Boolean,
        default: false
      },
      // update-begin--author:liaozhiyang---date:20240514---for：【issues/6205】高级查询组件增加保存条件自定义存储方式
      isCustomSave: {
        type: Boolean,
        default: false,
      },
      saveSearchData: {
        type: Array,
        default: () => [],
      },
      save: {
        type: Function,
      },
      // update-end--author:liaozhiyang---date:20240514---for：【issues/6205】高级查询组件增加保存条件自定义存储方式
      queryBtnCfg: {
        type: Object,
        default: () => {
          return {
            buttonName: '高级查询',
            buttonIcon: 'ant-design:filter-outlined',
          }
        }
      },
    },
    components: {
      BasicModal,
      MinusCircleOutlined,
      PlusOutlined,
      OnlineSuperQueryValComponent,
      FileTextOutlined,
      CloseCircleOutlined,
      AppstoreTwoTone,
      Divider,
      Popconfirm,
    },
    emits:['search'],
    setup(props, {emit}) {

      console.log("-------初始化  OnlineSuperQuery注册--------------")
      
      const [registerFormModal, formModal] = useModal()
      const { createMessage: $message } = useMessage();
      const queryFormStyle = ref({});
      const fieldTreeSelectHeight = ref(180);
      const { prefixCls } = useDesign('super-query');
      const treePopupClass = `${prefixCls}-tree-popup`;
      // 保存的查询是否折叠
      const historyCollapsed = ref(true)
      let currentQueryInfo = null;
      // -update-begin--author:liaozhiyang---date:20240614---for：【TV360X-76】高级查询条件根据控件类型区分
      const { filterCondition } = useConditionFilter();
      const getQueryCondition = (data) => {
        const auto = (arr, field) => {
          const findItem = arr.find((item) => item.value === field) ?? {};
          return filterCondition({ view: findItem.originView || findItem.view, fieldType: findItem.fieldType });
        };
        if (data.field?.indexOf('@') == -1 || !data.field) {
          return auto(fieldTreeData.value, data.field);
        } else {
          const tableName = data.field.split('@')[0];
          const findTableItem = fieldTreeData.value.find((item) => item.value === tableName);
          if (findTableItem?.children?.length) {
            return auto(findTableItem.children, data.field);
          }
        }
      };
      // -update-end--author:liaozhiyang---date:20240614---for：【TV360X-76】高级查询条件根据控件类型区分
      /**
       * 关闭按钮事件
       */
      function handleCancel() {
        formModal.closeModal();
      }

      /**
       * 确认按钮事件
       */
      function handleSubmit() {
        if(props.online === true){
          let dataArray = getQueryInfo(true)
          if(dataArray && dataArray.length>0){
            // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-86】年，年月，周查询出结果不准
            transformDateValus(dataArray);
            // update-end--author:liaozhiyang---date:20240517---for：【TV360X-86】年，年月，周查询出结果不准
            emit('search', dataArray, matchType.value)
            // update-begin--author:liaozhiyang---date:220230802---for：【QQYUN-5995】高级查询效果后关闭弹窗
            handleCancel()
            // update-end--author:liaozhiyang---date:220230802---for：【QQYUN-5995】高级查询效果后关闭弹窗
            currentQueryInfo = dataArray;
          }else{
            $message.warning('空条件无法查询！')
          }
        }else{
          //console.log('handleSubmit', dynamicRowValues.values)
          let dataArray = getQueryInfo(true)
          if(dataArray && dataArray.length>0){
            //update-begin---author:wangshuai---date:2025-07-18---for:【issues/8548】代码生成的高级查询里日期-月控件不能正常展示---
            transformDateValus(dataArray);
            //update-end---author:wangshuai---date:2025-07-18---for:【issues/8548】代码生成的高级查询里日期-月控件不能正常展示---
            let result = getSuperQueryParams(dataArray);
            //console.log('查询数据1', dataArray)
            //console.log('查询数据2', result)
            emit('search', result)
            // update-begin--author:wangshuai---date:20251112---for：【issues/9060】superQuery高级组件，点击"查询后"不能自动关闭弹窗
            handleCancel()
            // update-end--author:wangshuai---date:20251112---for：【issues/9060】superQuery高级组件，点击"查询后"不能自动关闭弹窗
          }else{
            $message.warning('空条件无法查询！')
          }
        }
        historyCollapsed.value = true;
      }

      /**
       * 2024-05-17
       * liaozhiyang
       * 把年，年月，周等时间重置到当前格式的第一天，因为存的时候也是第一天
       */
      const transformDateValus = (date) => {
        date.forEach((item) => {
          const value = item['val'];
          if (item.type === 'date' && typeof value === 'string' && value != '') {
            const obj = fieldProperties.value[item.field];
            if (obj) {
              let fieldExtendJson = obj.fieldExtendJson;
              if (fieldExtendJson) {
                fieldExtendJson = JSON.parse(fieldExtendJson);
                if (fieldExtendJson.picker && fieldExtendJson.picker !== 'default') {
                  const picker = fieldExtendJson.picker;
                  if (picker === 'year') {
                    item['val'] = dayjs(value).set('month', 0).set('date', 1).format('YYYY-MM-DD');
                  } else if (picker === 'month') {
                    item['val'] = dayjs(value).set('date', 1).format('YYYY-MM-DD');
                  } else if (picker === 'week') {
                    item['val'] = dayjs(value).startOf('week').format('YYYY-MM-DD');
                  }
                }
              }
            }
          }
        });
      };
      
      function getSuperQueryParams(dataArray){
        let arr:any = []
        for(let item of dataArray){
          let field = item.field;
          let val = item.val
          if(val instanceof Array){
            val = val.join(",")
          }
          arr.push({
            ...item,
            field,
            val
          })
        }
        if(arr.length>0){
          superQueryFlag.value = true;
        }else{
          superQueryFlag.value = false;
        }
        let result = {
          superQueryMatchType: matchType.value,
          superQueryParams: encodeURI(JSON.stringify(arr))
        }
        return result;
      }
      
      function handleStop(){
        let result = getSuperQueryParams([]);
        emit('search', result)
      } 
      /**
       * 重置按钮事件
       */
      function handleReset(){
        dynamicRowValues.values = []
        addOne(false);
        let result = getSuperQueryParams([])
        currentQueryInfo = null;
        historyCollapsed.value = true;
        emit('search', result)
      }
      // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-524】高级查询增加放大功能
      const handleFullScreen = (val) => {
        // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-810】高级查询放大之后树下拉框高一些多展示些字段
        if (val) {
          const contentHeight = document.documentElement.clientHeight - 165;
          queryFormStyle.value = { maxHeight: `${contentHeight}px` };
          fieldTreeSelectHeight.value = contentHeight * 0.62;
        } else {
          queryFormStyle.value = {};
          fieldTreeSelectHeight.value = 180;
        }
        // update-end--author:liaozhiyang---date:20240603---for：【TV360X-810】高级查询放大之后树下拉框高一些多展示些字段
      };
      // update-end--author:liaozhiyang---date:20240524---for：【TV360X-524】高级查询增加放大功能
      const {
        formRef,
        init,
        dynamicRowValues,
        matchType,
        registerModal,

        handleSave,
        doSaveQueryInfo,
        saveInfo,
        saveTreeData,
        handleTreeSelect,
        handleRemoveSaveInfo,
        fieldTreeData,
        addOne,
        removeOne,
        setFormModel,
        getSchema,
        loading,
        getQueryInfo,
        initDefaultValues,
        saveModalLoading,
        fieldProperties,
      } = useSuperQuery(props)

      /*--------------------按钮区域-beign------------------*/
      const superQueryFlag = ref(false)
      watch(()=>props.status, (val)=>{
        superQueryFlag.value = val;
      }, {immediate: true});
      
      function handleOpen(){
        // update-begin--author:liaozhiyang---date:20240604---for：【TV360X-204】修改内容没点确定，再次打开应该是恢复之前的内容
        if (superQueryFlag.value && currentQueryInfo) {
          dynamicRowValues.values = cloneDeep(currentQueryInfo);
        }
        // update-end--author:liaozhiyang---date:20240604---for：【TV360X-204】修改内容没点确定，再次打开应该是恢复之前的内容
        formModal.openModal();
        historyCollapsed.value = true;
        addOne(true)
      }
      /*--------------------按钮区域-end------------------*/


      function getPopupContainer(){
        return document.getElementsByClassName('jee-super-query-form')[0]
      }
      function onFinish(a){
        console.log('onfinish', a)
      }
      /**
       * 2024-06-04
       * liaozhiyang
       * 【TV360X-461】字段类型是string，则默认模糊查询
       * */
      function handleChangeField(data) {
        data['val'] = '';
        const auto = (arr, field) => {
          const findItem = arr.find((item) => item.value === field);
          if (findItem?.fieldType === 'string' && ['text'].includes(findItem?.originView || findItem?.view)) {
            data['rule'] = 'like';
          } else if (['file', 'image', 'password'].includes(findItem?.originView || findItem?.view)) {
            data['rule'] = 'empty';
          } else {
            data['rule'] = 'eq';
          }
        };
        if (data.field?.indexOf('@') == -1) {
          auto(fieldTreeData.value, data.field);
        } else {
          const tableName = data.field.split('@')[0];
          const findTableItem = fieldTreeData.value.find((item) => item.value === tableName);
          if (findTableItem?.children?.length) {
            auto(findTableItem.children, data.field);
          }
        }
      }

      
      watch(()=>props.config, (val)=>{
        if(val){
          // console.log('123', val)
          // console.log('123', val)
          // console.log('123', val)
          // Object.keys(val).map(k=>{
          //   console.log(k, val[k])
          // })
          // console.log('123', val)
          // console.log('123', val)
          // console.log('123', val)
          init(val);
        }
      }, {immediate: true});
      // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-342】字段下拉样式调整
      const getTreePopupClass = computed(() => {
        const findItem = fieldTreeData.value.find((item) => item.children);
        return findItem ? `${treePopupClass} containTable` : `${treePopupClass} noTable`;
      });
      // update-end--author:liaozhiyang---date:20240603---for：【TV360X-342】字段下拉样式调整

      return {
        formRef,
        registerFormModal,
        init,
        handleChangeField,
        dynamicRowValues,
        matchType,
        historyCollapsed,
        registerModal,
        handleSubmit,
        handleCancel,
        handleSave,
        handleReset,
        doSaveQueryInfo,
        saveInfo,
        saveTreeData,
        handleTreeSelect,
        handleRemoveSaveInfo,
        fieldTreeData,
        addOne,
        removeOne,
        setFormModel,
        getSchema,
        loading,
        onFinish,
        getPopupContainer,
        superQueryFlag,
        handleOpen,
        initDefaultValues,
        simpleImage: Empty.PRESENTED_IMAGE_SIMPLE,
        saveModalLoading,
        queryFormStyle,
        handleFullScreen,
        fieldTreeSelectHeight,
        getTreePopupClass,
        handleStop,
        getQueryCondition,
      }
    }

  }
</script>

<style lang="less">
  .j-super-query-modal {
    .scrollbar__wrap {
      margin-bottom: 0 !important;
    }
  }
</style>
<style scoped lang="less">
   
    :deep(.jee-super-query-form) {
        max-height: 400px;
        overflow-y: auto;
        min-height: 300px;
        .ant-form-item{
            margin-bottom: 9px;
        }
    }
    
    :deep(.j-super-query-history-tree) {
        .ant-tree-switcher{
            width: 0px;
        }
        .ant-tree-node-content-wrapper{
            width:100%;
            &:hover{
                background-color: #e6f7ff !important;
                border-radius: 0;
            }
        }
        .ant-tree-treenode-switcher-close{
            .ant-tree-title{
                display: inline-block;
                width: calc(100% - 30px);
                >div{
                    display: flex;
                    justify-content: space-between;
                    .icon-cancle{
                        display: none;
                        color: #666666;
                        &:hover{
                            color: black;
                        }
                    }
                }
            }
            &:hover {
                .icon-cancle{
                    display: inline-block !important;
                }
            }
        }
         .ant-card-body{
            padding: 0;
        }
        // 保存查询样式宽度调整
        .ant-tree-treenode { width: 100%;}
    }

    .j-super-query-history-card {
      position: absolute;
      top: 0;
      right: -0;
      bottom: 0;
      z-index: 2;
      //【QQYUN-7526】两行移入时会挤成三行
      width: 180px;
      min-height: 200px;
      box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.2);
      
      
      &.collapsed {
        //【QQYUN-7526】两行移入时会挤成三行
        right: -180px;
      }

      .collapse-box {
        position: absolute;
        top: calc(50% - 15px);
        left: -20px;
        width: 20px;
        height: 30px;
        background-color: #fff;
        border: 1px solid #e8e8e8;
        border-left: none;
        border-right-color: #ffffff;
        border-radius: 15px 0 0 15px;
        cursor: pointer;
        transition: all 0.3s;
        z-index: 1;
        display: flex;
        justify-content: center;
        align-items: center;
        box-shadow: -4px 0 6px rgba(0, 0, 0, 0.1);

        &:hover {
          background-color: #f5f5f5;
        }
        .app-iconify {
          font-size: 16px;
          position: relative;
          right: -3px;
          color: #666;
        }
      }

      &.collapsed .app-iconify{
        right: 0;
      }
        
      :deep(.ant-card-body),
      :deep(.ant-card-head){
        padding: 8px;
        min-height: 43px;
        .ant-card-head-title {
          padding: 0;
        }
      }
    }

    /*VUEN-1087 【移动端】高级查询显示不全 */
    @media only screen and (max-width: 1050px) {
        :deep(.jee-super-query-form){
            .ant-space{
                flex-direction:column;
                gap: 0 !important;
                margin-bottom: 16px !important;
            }
            .ant-space-item{
                width: 100%;
            }
            .ant-form-item{
                width: 100% !important;
            }
        }
    }
    // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-524】高级查询增加放大功能
    .ant-form {
      & > .ant-space {
        width: 100%;
        :deep(& > .ant-space-item) {
          &:nth-child(1) {
            width: 20%;
          }
          &:nth-child(2) {
            width: 15%;
          }
          &:nth-child(3) {
            width: 40%;
          }
        }
      }
    }
    // update-end--author:liaozhiyang---date:20240524---for：【TV360X-524】高级查询增加放大功能

    .containTable {
      :deep(.ant-select-tree-treenode) {
        > .ant-select-tree-switcher { display: none;}
      }
    }
</style>
<style lang="less">
  @tree-popup: ~'@{namespace}-super-query-tree-popup';
  .@{tree-popup} {
    // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-342】字段下拉样式调整
    &.noTable {
      .ant-select-tree-treenode {
        > .ant-select-tree-switcher {
          display: none;
        }
        .ant-select-tree-node-content-wrapper {
          display: block;
          max-width: 100%;
        }
        .ant-select-tree-title {
          width: 100%;
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    // update-end--author:liaozhiyang---date:20240603---for：【TV360X-342】字段下拉样式调整
    // update-end--author:liaozhiyang---date:20240612---for：【TV360X-1005】有子表时结构化主表且超长省略
    &.containTable {
      .ant-select-tree-indent-unit {
        display: none;
      }
      .ant-select-tree-node-content-wrapper {
        display: block;
        max-width: calc(100% - 24px);
      }
      .ant-select-tree-title {
        width: 100%;
        display: block;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    // update-end--author:liaozhiyang---date:20240612---for：【TV360X-1005】有子表时结构化主表且超长省略
  }
</style>
