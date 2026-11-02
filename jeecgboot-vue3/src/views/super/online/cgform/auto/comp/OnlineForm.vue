<template>
  <div ref="printRef" :id="tableName + '_form'" class="onlineFormWrap" :class="[`formTemplate_${formTemplate}`]">
    <!-- update-begin--author:liaozhiyang---date:20240401---for：【QQYUN-8721】编辑页面不显示打印（配置了积木报表） -->
    <!-- 积木报表的打印按钮，只有配置了 reportUrl 才显示 -->
    <!-- <div v-if="!!dbData.id && !!onlineExtConfigJson.reportPrintShow" style="text-align: right;position: absolute;top: 6px;right: 20px;z-index: 999">
      <PrinterOutlined title="打印" @click="onOpenReportPrint" style="font-size: 16px"/>
    </div> -->
     <!-- update-end--author:liaozhiyang---date:20240401---for：【QQYUN-8721】编辑页面不显示打印（配置了积木报表） -->

    <BasicForm ref="onlineFormRef" @register="registerForm" :name="'online-form_' + tableName"/>

    <!-- 子表 -->
    <!-- update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表 -->
    <a-tabs v-model:activeKey="subActiveKey" v-if="hasSubTable && (isShare || themeTemplate !== ERP)">
    <!-- update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表 -->
      <a-tab-pane v-for="(sub, index) in subTabInfo" :tab="sub.describe" :key="index + ''" :forceRender="true">
        <div ref="subFormWrapRef" :style="{ 'overflow-y': 'auto', 'overflow-x': 'hidden', 'max-height': subFormHeight + 'px' }" v-if="sub.relationType == 1">
          <!-- 子表-一对一 -->
          <online-sub-form
            :ref="refMap[sub.key]"
            :table="sub.key"
            :id="sub.id"
            :disabled="onlineFormDisabled"
            :form-template="sub.formTemplate"
            :main-id="getSubTableForeignKeyValue(sub.foreignKey)"
            :properties="sub.properties"
            :required-fields="sub.requiredFields"
            :is-update="isUpdate"
            :modal-class="modalClass"
            @formChange="(arg) => handleSubFormChange(arg, sub.key)"
          />
        </div>
        <div v-else>
          <!-- 子表-一对多 -->
          <JVxeTable
            :ref="refMap[sub.key]"
            toolbar
            keep-source
            :row-number="rowNumber"
            row-selection
            :height="
              // 【VUEN-803】一对多子表固定340高度，修复自定义列组件被遮挡的问题
              subTableHeight
            "
            :disabled="onlineFormDisabled"
            :columns="sub.columns"
            :dataSource="subDataSource[sub.key]"
            :addBtnCfg="getSubAddBtnCfg"
            :removeBtnCfg="getSubRemoveBtnCfg"
            @valueChange="(event) => handleValueChange(event, sub.key)"
            @removed="(event) => handleRemoved(event, sub.key)"
            :authPre="getSubTableAuthPre(sub.key)"
            @added="handleAdded(sub, $event)"
            @executeFillRule="handleSubTableDefaultValue(sub, $event)">

            <template #toolbarAfterAdd="{ selectedRowIds }">
              <a-button-group class="sub-popup-button-group">
                <!-- 子表内弹出新增按钮 -->
                <a-button
                    v-if="!onlineFormDisabled && getSubOpenAddBtnCfg.enabled && getBtnAuth('add', sub.key)"
                    :preIcon="getSubOpenAddBtnCfg.buttonIcon"
                    @click="openSubFormModalForAdd(sub)"
                >
                  <span>{{getSubOpenAddBtnCfg.buttonName}}</span>
                </a-button>
                <!-- 子表内弹出编辑按钮 -->
                <a-button
                    v-if="!onlineFormDisabled && getSubOpenEditBtnCfg.enabled && getBtnAuth('update', sub.key) && selectedRowIds.length === 1"
                    :preIcon="getSubOpenEditBtnCfg.buttonIcon"
                    @click="openSubFormModalForEdit(sub)"
                >
                  <span>{{getSubOpenEditBtnCfg.buttonName}}</span>
                </a-button>
              </a-button-group>
            </template>

          </JVxeTable>

        </div>
      </a-tab-pane>
    </a-tabs>
    <Loading :loading="loading" :absolute="false" />
    <!-- 表单中可以通过slot自定义提交按钮，流程表单中用到 -->
    <slot name="bottom"></slot>

    <!-- 弹窗到另外一张表单用-可编辑表单 -->
    <online-pop-modal formTableType="3" :request="popModalRequest" :id="popTableId" @register="registerPopModal" @success="getPopFormData" :taskId="taskId" :tableName="popTableName" topTip isVxeTableData></online-pop-modal>
  </div>
</template>

<script>
  import { useMessage } from '/@/hooks/web/useMessage';
  import { computed, defineComponent, ref, unref, watch, nextTick, toRaw, reactive, inject, provide, onMounted } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import {
    SUBMIT_FLOW_ID,
    SUBMIT_FLOW_KEY,
    VALIDATE_FAILED,
    ONL_FORM_TABLE_NAME,
    ENHANCEJS
  } from '../../types/onlineRender';
  import { defHttp } from '/@/utils/http/axios';
  import { pick, omit, cloneDeep, debounce } from 'lodash-es';
  import { sleep } from '/@/utils';
  import { useFormItems, getRefPromise, useOnlineFormContext } from '../../hooks/auto/useAutoForm';
  import { Loading } from '/@/components/Loading';
  import { useEnhance } from '../../hooks/auto/useEnhance';
  import OnlineSubForm from './OnlineSubForm.vue';
  import { JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import {handleFillRuleWatchKeysMap, loadFormFieldsDefVal} from '../../util/FieldDefVal';
  import { getToken } from '/@/utils/auth';
  import { goJmReportViewPage } from '/@/utils'
  import { PrinterOutlined } from '@ant-design/icons-vue';
  import {useOnlineFormEvent, useOnlineEventContext} from '../../hooks/auto/useOnlinePopEvent';
  import OnlinePopModal from './OnlinePopModal.vue';
  import { useModal } from '/@/components/Modal';
  import { useCustomHook, GET_FUN_BODY_REG } from "../../hooks/auto/useCustomHook";
  import { ERP } from "../../util/constant";
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { isArray } from '/@/utils/is';
  import dayjs from 'dayjs';
  import { usePermissionStore } from '/@/store/modules/permission';
  const urlObject = {
    optPre: '/online/cgform/api/form/',
    urlButtonAction: '/online/cgform/api/doButton',
  };
  export default {
    name: 'OnlineForm',
    components: {
      BasicForm,
      Loading,
      OnlineSubForm,
      PrinterOutlined,
      OnlinePopModal
    },
    props: {
      id: {
        type: String,
        default: '',
      },
      formTemplate: {
        type: Number,
        default: 1,
      },
      disabled: {
        type: Boolean,
        default: false,
      },
      isTree: {
        type: Boolean,
        default: false,
      },
      pidField: {
        type: String,
        default: '',
      },
      submitTip:{
        type: Boolean,
        default: true
      },
      izCopy:{
        type: Boolean,
        default: false
      },
      modalClass:{
        type: String,
        default: '',
      },
      themeTemplate: {
        type: String,
        default: '',
      },
      // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
      // erp风格会传来所有子表数据
      subTableSource: {
        default: () => ({}),
      },
      // update-end-author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
      // -update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
      // 流程会传taskId
      taskId: {
        type: String,
      },
      // -update-end--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
      cgBIBtnMap: Object,
      buttonSwitch: Object,
      // update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
      isShare: {
        type: Boolean,
        default: false,
      },
      // update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
    },
    emits: ['success', 'rendered', 'close','validate'],
    setup(props, { emit }) {
      console.log('onlineForm-setup》》');
      const { createMessage: $message } = useMessage();

      // 打印表单ref
      const printRef = ref(null);
      // 表单ref
      const onlineFormRef = ref(null);
      const single = ref(true);
      // 加载状态
      const loading = ref(false);
      // 显示提示
      const showTip = ref(true);
      const tableType = ref(1);
      // 自定义弹窗的编辑提交地址
      const customEditSubmitUrl = ref('');

      // 表单提交且提交流程
      const submitFlowFlag = ref(false);
      const isUpdate = ref(false);
      const { getIsMobile } = useAppInject();
      const isSetFormLabelLength = ref(false);
      // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      // erp子表获取外键
      const foreignkey = inject("foreignkey", {value:{}});
      // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      const rowNumber = ref(getIsMobile.value ? false : true);
      const subFormWrapRef = ref(null);
      let formItemData = null;
      /**
       * online表单扩展配置
       */
      const onlineExtConfigJson = reactive({
        reportPrintShow: 0,
        reportPrintUrl: '',
        joinQuery: 0,
        modelFullscreen: 0,
        modalMinWidth: '',
        commentStatus: 0
      });

      const {
        onlineFormContext,
        resetContext,

        getSubAddBtnCfg,
        getSubRemoveBtnCfg,
        getSubOpenAddBtnCfg,
        getSubOpenEditBtnCfg,
      } = useOnlineFormContext(props);

      const {
        formSchemas,
        defaultValueFields,
        changeDataIfArray2String,
        tableName,
        dbData,
        checkOnlyFieldValue,
        hasSubTable,
        subTabInfo,
        refMap,
        subDataSource,
        baseColProps,
        createFormSchemas,
        linkDownList,
        fieldDisplayStatus,
        labelCol,
        wrapperCol,
        labelWidth
      } = useFormItems(props, onlineFormRef);
      let { EnhanceJS, initCgEnhanceJs } = useEnhance(onlineFormContext, false);
      // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-8405】移动端可视区展示三个字段
      watch(
        [subTabInfo, getIsMobile],
        () => {
          if (getIsMobile.value && subTabInfo.value.length) {
            subTabInfo.value.forEach((item) => {
              if (item.relationType != 1) {
                item.columns.forEach((cItem) => {
                  cItem.width = 100;
                });
              }
            });
          }
        },
        { immediate: true }
      );
      // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-8405】移动端可视区展示三个字段
      // 新的js增强
      const { executeJsEnhanced } = useCustomHook({}, onlineFormContext);

      //表单配置
      const [registerForm, { setProps, validate, resetFields, clearValidate, setFieldsValue, updateSchema, getFieldsValue, scrollToField }] = useForm({
        schemas: formSchemas,
        showActionButtonGroup: false,
        baseColProps: baseColProps,
        // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
        labelWidth,
        // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
        // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
        labelCol,
        wrapperCol
        // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      });

      // 表单禁用
      const onlineFormDisabled = ref(false);
      function handleFormDisabled() {
        let flag = props.disabled;
        onlineFormDisabled.value = flag;
        setProps({ disabled: flag });
      }

      /**
       * status: 是否是修改页面
       * record: 列表页面的行数据
       * param： 树形列表添加子节点 传入的父级节点id
       * */
      async function show(status, record, param) {
        console.log('新增编辑进入表单》》form', record);
        //update-begin-author:taoyan date:2022-5-17 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的
        //如果是树 需要重新设置pid的formSchema 让他刷新树控件的数据
        await updatePidFieldDict();
        //update-end-author:taoyan date:2022-5-17 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的
        //重置自定义弹窗的表单提交地址
        customEditSubmitUrl.value = '';
        await resetFields();
        // update-begin--author:liaozhiyang---date:20240517---for：【QQYUN-9345】点击编辑再点击新增一下组件会直接触发验证
        setTimeout(() => {
          clearValidate();
        }, 0);
        // update-end--author:liaozhiyang---date:20240517---for：【QQYUN-9345】点击编辑再点击新增一下组件会直接触发验证
        dbData.value = '';
        let flag = unref(status);
        isUpdate.value = flag;
        // update-begin--author:liaozhiyang---date:20240301---for：【QQYUN-8401】新增弹窗一对一子表清空上次的数据
        restSubForm();
        // update-end--author:liaozhiyang---date:20240301---for：【QQYUN-8401】新增弹窗一对一子表清空上次的数据
        if (flag) {
          // 编辑页面
          await edit(record);
        } else {
          // 新增页面
          editSubVxeTableData();
        }
        nextTick(() => {
          if (!flag && param) {
            //如果是新增页面 且 param传入有值 需要设置表单
            setFieldsValue(param);
            props.izCopy && setSubTableFieldsValue(param);
          }
          handleDefaultValue();
          // 所有信息加载完毕 触发loaded事件
          handleCgButtonClick('js', 'loaded');
          //处理表单的禁用效果
          handleFormDisabled();
          // update-begin--author:liaozhiyang---date:20240527---for【TV360X-114】一对一子表编辑时滚动条回到顶部
          if (subFormWrapRef.value?.length) {
            subFormWrapRef.value[0].scrollTop = 0;
          }
          // update-end--author:liaozhiyang---date:20240527---for【TV360X-114】一对一子表编辑时滚动条回到顶部
        });
      }
      //update-begin-author:taoyan date:2022-5-17 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的
      /**
       * 刷新表单中的 JTreeSelect 树数据
       * - 树表：刷新父节点字段，并清空 hiddenNodeKey
       * - 普通表：每次进入表单（含第二次新增/编辑）重新加载自定义树控件
       */
      async function updatePidFieldDict() {
        let arr = formSchemas.value;
        if (!arr || arr.length === 0) {
          console.log('没有拿到表单配置信息，可能是第一次打开新增页面');
          return;
        }
        // update-begin--author:liaozhiyang---date:20260804---for：Online表单第二次进入时 JTreeSelect 重新加载树数据
        const treeSelectSchemas = arr.filter((item) => item.component === 'JTreeSelect');
        if (treeSelectSchemas.length > 0) {
          const reload = new Date().getTime();
          const pidFieldName = props.isTree === true ? props.pidField : '';
          await updateSchema(
            treeSelectSchemas.map((item) => {
              const componentProps = { reload };
              // 树表父节点字段：清空 hiddenNodeKey，编辑时再按当前节点过滤
              if (pidFieldName && item.field === pidFieldName) {
                componentProps.hiddenNodeKey = '';
              }
              return {
                field: item.field,
                componentProps,
              };
            })
          );
        }
        // update-end--author:liaozhiyang---date:20260804---for：Online表单第二次进入时 JTreeSelect 重新加载树数据
      }
      //update-end-author:taoyan date:2022-5-17 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的

      const watchDefValParamMap = {
        keys: [],
        map: new Map(),
        calcFn: new Map()
      };

      /**
       * 当前表单默认值逻辑-进入新增页面触发
       */
      function handleDefaultValue() {
        let fieldProperties = toRaw(defaultValueFields[tableName.value]);
        if (unref(isUpdate) === false) {
          loadFormFieldsDefVal(fieldProperties, (values) => {
            setFieldsValue(values);
          });
        }
        // 处理填值规则默认值中的动态参数
        const watchKeyMap = handleFillRuleWatchKeysMap(fieldProperties)
        watchDefValParamMap.keys = [...watchKeyMap.keys()]
        watchDefValParamMap.map = watchKeyMap
        watchDefValParamMap.calcFn.clear()
      }

      /**
       * 当前表单默认值逻辑-值变化时重新计算
       */
      function recalcDefaultValue(field, value) {
        // 判断当前更改的字段是否在监听keys中
        if (watchDefValParamMap.keys.includes(field)) {
          let fn = watchDefValParamMap.calcFn.get(field)
          if (typeof fn !== 'function') {
            // 防抖100ms
            fn = debounce(() => {
              let fieldProp = toRaw(defaultValueFields[tableName.value]);
              if (Array.isArray(fieldProp) && fieldProp.length > 0) {
                const fieldKeys = watchDefValParamMap.map.get(field)
                fieldProp = fieldProp.filter(item => {
                  return fieldKeys.includes(item.field)
                })
              } else {
                fieldProp = []
              }
              if (fieldProp.length > 0) {
                let formData = getFieldsValue();
                loadFormFieldsDefVal(fieldProp, (values) => setFieldsValue(values), formData);
              }
            }, 150)
            watchDefValParamMap.calcFn.set(field, fn)
          }
          fn(value)
        }
      }

      /**
       * 子表默认值-子表新增行的时候触发此逻辑
       */
      function handleSubTableDefaultValue(sub, $event) {
        //console.log('填值规则', sub, $event)
        let subFieldProperties = toRaw(defaultValueFields[sub.key]);
        loadFormFieldsDefVal(subFieldProperties, (values) => {
          const { row, target } = $event;
          let v = [{ rowKey: row.id, values: { ...values } }];
          target.setValues(v);
        });
      }

      async function edit(record) {
        let formData = await getFormData(record.id);
        //update-begin-author:taoyan date:2022-5-31 for: VUEN-1194 【online】字段填写完成后，设置权限不显示，再次编辑数据，则把原来的值替换成了空
        dbData.value = Object.assign({}, record, formData);
        //update-end-author:taoyan date:2022-5-31 for: VUEN-1194 【online】字段填写完成后，设置权限不显示，再次编辑数据，则把原来的值替换成了空
        //表单赋值
        let arr = realFormFieldNames.value;
        //update-begin-author:taoyan date:2022-7-11 for:  2、online 查看详情还提示必填
        let values = pick(formData, ...arr);
        if(props.disabled){
          //详情页面 将空的数据丢弃
          Object.keys(values).map(k=>{
            if(!values[k] && values[k]!==0 && values[k]!=='0'){
              delete values[k]
            }
          })
        }
        await setFieldsValue(values);
        // update-begin--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
        treeTablefilterNode(record.id);
        // update-end--author:liaozhiyang---date:20240523---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
        //update-end-author:taoyan date:2022-7-11 for:  2、online 查看详情还提示必填
        editSubVxeTableData(formData);
      }

      function editSubVxeTableData(record) {
        if (!record) {
          // 新增页面需要清空子表数据
          record = {};
        }
        let keys = Object.keys(subDataSource.value);
        if (keys && keys.length > 0) {
          let obj = {};
          for (let key of keys) {
            obj[key] = record[key] || [];
          }
          subDataSource.value = obj;
        }
      }
      /**
       * 2024-05-23
       * liaozhiyang
       * 【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
       * @param id (当前节点)
       */
      function treeTablefilterNode(id) {
        if (props.isTree === true) {
          const { schema } = formItemData;
          const properties = schema.properties ?? {};
          const arr = Object.entries(properties);
          if (arr.length) {
            const selectTreeData = arr.find(([key, value]) => value.view === 'sel_tree' && value.pidComponent != null);
            if (selectTreeData) {
              const field = selectTreeData[0];
              const compData = formSchemas.value.find((item) => item.field == field);
              if (compData) {
                updateSchema({
                  field,
                  componentProps: {
                    hiddenNodeKey: id,
                  },
                });
              }
            }
          }
        }
      }
      /**
       * 2024-03-01
       * liaozhiyang
       * 重置一对一子表form数据
       */
      function restSubForm(){
        subTabInfo.value?.forEach(item=>{
          if(item.relationType == 1){
            // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-8563】erp风格配置的默认值表达式 未显示
            if (refMap[item.key].value) {
              // update-begin--author:liaozhiyang---date:20260714---for：【LHZP-249】第二次打开新增弹窗，一对一子表的默认值没了
              if (unref(isUpdate) === false && typeof refMap[item.key].value[0]?.resetAndApplyDefaultValue === 'function') {
                refMap[item.key].value[0].resetAndApplyDefaultValue();
              } else {
                refMap[item.key].value[0].resetFields();
              }
              // update-end--author:liaozhiyang---date:20260714---for：【LHZP-249】第二次打开新增弹窗，一对一子表的默认值没了
            }
            // update-end--author:liaozhiyang---date:20240321---for：【QQYUN-5803】erp风格配置的默认值表达式 未显示
          }
        });
      }
      /**
       * 设置子表的数据
       */
      function setSubTableFieldsValue(param){
        //处理一对一子表数据
        subTabInfo.value?.forEach(item=>{
          if(item.relationType == 1){
            if (refMap[item.key].value && param[item.key]) {
              const oneDefaultValue = param[item.key] && param[item.key].length>0?param[item.key][0] : {};
              setTimeout(()=>{
                refMap[item.key].value[0].setValues(omit(oneDefaultValue, ['id']));
              },20)
            }
          }
        });
        //处理一对多子表数据
        let keys = Object.keys(subDataSource.value);
        if (keys && keys.length > 0) {
          let obj = {};
          for (let key of keys) {
            let arr = [];
            if(param[key]){
              param[key].forEach(item=>{
                arr.push(omit(item, ['id']));
              });
            }
            obj[key] = arr;
          }
          subDataSource.value = obj;
        }
      }

      let realFormFieldNames = computed(() => {
        let arr = formSchemas.value;
        let names = [];
        for (let a of arr) {
          names.push(a.field);
        }
        return names;
      });

      function getFormData(dataId) {
        let url = `${urlObject.optPre}${props.id}/${dataId}`;
        return new Promise((resolve, reject) => {
          defHttp
            .get({ url }, { isTransformResponse: false })
            .then((res) => {
              if (res.success) {
                resolve(res.result);
              } else {
                reject();
                $message.warning(res.message);
              }
            })
            .catch(() => {
              reject();
            });
        });
      }

      // 渲染表单
      async function createRootProperties(data) {
        tableType.value = data.head.tableType;
        tableName.value = data.head.tableName;
        single.value = data.head.tableType == 1;
        handleExtConfigJson(data.head.extConfigJson);

        createFormSchemas(data.schema.properties, data.schema.required, checkOnlyFieldValue, onlineExtConfigJson);
        EnhanceJS = initCgEnhanceJs(data.enhanceJs);
        emit('rendered', onlineExtConfigJson);

        //监听表单改变事件
        let formRefObject = await getRefPromise(onlineFormRef);
        formRefObject.$formValueChange = (field, value, changeFormData) => {
          onValuesChange(field, value);
          if(changeFormData){
            //如果存在其他表单控件的数据，直接设置该值
            setFieldsValue(changeFormData)
          }
          onChangeEvent(field, value, changeFormData)
          recalcDefaultValue(field, value)
        };
        // QQYUN-5315【online表单】online表单也需要支持hook增强
        if(EnhanceJS && EnhanceJS["setup"]){
          executeEnhanceFormJsHook(EnhanceJS["setup"])
        }

        formItemData = data;
      }

      //  触发表单change事件 区别于老的onlchange
      function onChangeEvent(field, value, changeFormData) {
        onlineFormContext.changEvent(field, value, changeFormData)
      }

      // 添加表单change事件
      function onlineFormValueChange(callback) {
        onlineFormContext.addObject2Context("changEvent", callback)
      }

      /**
       * 处理扩展配置
       */
      function handleExtConfigJson(jsonStr) {
        //update-begin-author:taoyan date:2023-2-6 for: QQYUN-4082 默认打开online表单，默认全屏
        let extConfigJson = { reportPrintShow: 0, reportPrintUrl: '', joinQuery: 0, modelFullscreen: 0, modalMinWidth: '', commentStatus: 0, formLabelLength: null };
        //update-end-author:taoyan date:2023-2-6 for: QQYUN-4082 默认打开online表单，默认全屏
        if (jsonStr) {
          extConfigJson = JSON.parse(jsonStr);
          // update-begin--author:liaozhiyang---date:20240326---for：【QQYUN-8644】移动端效果关闭聊天窗口
          if (getIsMobile.value) {
            extConfigJson.commentStatus = 0;
          }
          // update-begin--author:liaozhiyang---date:20240326---for：【QQYUN-8644】移动端效果关闭聊天窗口
        }
        isSetFormLabelLength.value = !!extConfigJson.formLabelLength;
        Object.keys(extConfigJson).map((k) => {
          onlineExtConfigJson[k] = extConfigJson[k];
        });
      }

      // 提交表单和流程
      function submitFormAndFlow() {
        submitFlowFlag.value = true;
        handleSubmit();
      }

      function handleSubmit(tip = true) {
        showTip.value = tip;
        if (single.value === true) {
          handleSingleSubmit();
        } else {
          handleOne2ManySubmit();
        }
      }
      //校验必填
      async function handleValidate() {
        if (single.value === true) {
          try {
             await validate();
          }catch (e) {
            if (Array.isArray(e?.errorFields) && e.errorFields[0]) {
              emit('validate',{passed:false})
            }
          }
        } else {
          await validateAll()
        }
      }

      function handleOne2ManySubmit() {
        validateAll().then((formData) => {
          handleApplyRequest(formData);
        });
      }

      // 触发所有表单验证
      function validateAll() {
        let temp = {};
        return new Promise((resolve, reject) => {
          // 验证主表表单
          validate().then(
            (values) => resolve(values),
            ({ errorFields, values }) => {
              reject({
                // update-begin--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
                errorFields,
                values,
                // update-end--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
                code: VALIDATE_FAILED,
                key: tableName.value,
                // 滚动到未通过校验的字段上
                scrollToField: () => errorFields[0] && scrollToField(errorFields[0].name, { behavior: 'smooth', block: 'center' }),
              });
            }
          );
        })
          .then((result) => {
            Object.assign(temp, changeDataIfArray2String(result));
            // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
            // erp 列表入口：弹窗内无子表，子表在列表页单独维护；分享入口 isShare 会展示子表 Tab，必须一并校验并提交，否则主子表关联不上
            // update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
            // 分享入口 ERP 新建主子表未关联
            if (props.themeTemplate === ERP && !props.isShare) {
              return Promise.resolve({});
            } else {
              return validateSubTableFields();
            }
            // update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
            // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
          })
          .then((allTableData) => {
            Object.assign(temp, allTableData);
            return Promise.resolve(temp);
          })
          .catch((e) => {
            if (e === VALIDATE_FAILED || e?.code === VALIDATE_FAILED) {
              // update-begin--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
              analysisErrorInfo(e.errorFields, e.values, single.value, e.key).then((val) => {
                if (!val) {
                  $message.warning('校验未通过');
                }
              });
              // update-end--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
              if (e.key) {
                changeTab(e.key);
                if (e.scrollToField) {
                  setTimeout(() => e.scrollToField(), 150)
                }
              }
              emit('validate',{passed:false})
            } else {
              console.error(e);
            }
            return Promise.reject(null);
          });
      }

      /**
       * 切换tab到出现校验错误的页面
       * */
      function changeTab(key) {
        let arr = subTabInfo.value;
        for (let i = 0; i < arr.length; i++) {
          if (key == arr[i].key) {
            // update-begin-author:sunjianlei date:2022-7-7 for:【VUEN-1425】【vue3】对多子表中校验不通过不提示
            let activeKey = i + '';
            if (subActiveKey.value === activeKey) {
              break;
            }
            subActiveKey.value = activeKey;
            // 如果切换的目标tab是一对多子表，则需要再次校验一下
            if (arr[i].relationType === 0) {
              let instance = getSubTableInstance(key);
              sleep(300, () => instance?.validateTable());
            }
            // update-end-author:sunjianlei date:2022-7-7 for:【VUEN-1425】【vue3】对多子表中校验不通过不提示
            break;
          }
        }
      }

      // 验证子表
      function validateSubTableFields() {
        return new Promise(async (resolve, reject) => {
          let subData = {};
          try {
            let arr = subTabInfo.value;
            for (let i = 0; i < arr.length; i++) {
              let key = arr[i].key;
              let instance = getSubTableInstance(key);
              if (arr[i].relationType == 1) {
                try {
                  let subFormData = await instance.getAll();
                  subData[key] = [];
                  subData[key].push(subFormData);
                } catch (e) {
                  return reject({code: VALIDATE_FAILED, key, ...e});
                }
              } else {
                let errMap = await instance.fullValidateTable();
                if (errMap) {
                  return reject({code: VALIDATE_FAILED, key});
                }
                subData[key] = instance.getTableData();
              }
            }
          } catch (e) {
            reject(e);
          }
          resolve(subData);
        });
      }

      /**
       * 表单提交-单表
       */
      async function handleSingleSubmit() {
        try {
          let values = await validate();
          values = Object.assign({}, dbData.value, values);
          values = changeDataIfArray2String(values);
          loading.value = true;
          handleApplyRequest(values);
        } catch (e) {
          if (Array.isArray(e?.errorFields) && e.errorFields[0]) {
            // 滚动到未通过校验的字段上
            scrollToField(e.errorFields[0].name, { behavior: 'smooth', block: 'center' });
            // update-begin--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
            analysisErrorInfo(e.errorFields, e.values, single.value);
            // update-end--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
            emit('validate',{passed:false})
          }
        } finally {
          loading.value = false;
          emit('close');
        }
      }
      /**
       * 2024-06-17
       * liaozhiyang
       * 解析错误信息看是否有组件是input-number，默认值有小数引起的校验错误
       */
      async function analysisErrorInfo(errorFields, values, single, curTableName = null) {
        let result = false;
        if (errorFields?.length) {
          const schema = formItemData.schema ?? {};
          const { properties = {} } = schema;
          const errorField = errorFields[0].name[0];
          let curField;
          if (single || curTableName === tableName.value) {
            // 单表或者主表
            curField = properties[errorField];
          } else {
            // 一对一or一对多(一对多可以排除，因为一对多就是显示的原始值)
            const subSchema = properties[curTableName];
            const { properties: subProperties = {} } = subSchema;
            curField = subProperties[errorField];
          }
          if (curField.type === 'number' && curField.view === 'number' && curField.defVal) {
            await loadFormFieldsDefVal(
              [
                {
                  field: errorField,
                  type: curField.type,
                  value: curField.defVal,
                  view: curField.view,
                },
              ],
              (res) => {
                if (res[errorField] === values[errorField]) {
                  $message.warning(`${curField.title}的默认值是：${values[errorField]}，导致校验通不过，需要正确配置默认值！`);
                  result = true;
                }
              }
            );
          }
        }
        return result;
      }

      //提交数据前 先走一下自定义的JS校验
      function handleApplyRequest(formData) {
        // 【issues/7190】【issues/9873】ERP列表子表是分页数据，由子表接口独立维护，禁止随主表全量提交
        customBeforeSubmit(context, formData)
          .then(() => {
            doApplyRequest(formData);
          })
          .catch((msg) => {
            $message.warning(msg);
          });
      }
      /**
       * 2024-04-30
       * liaozhiyang
       * 把日期(年月日)组件选择了年和年月的格式，数据提交前重置到该格式的第一天。
       */
      const changeDateValue = (formData) => {
        const { schema } = formItemData;
        const { properties } = schema;
        const recursion = (formData, properties) => {
          Object.entries(formData).forEach(([key, value]) => {
            const item = properties[key];
            if (item) {
              if (item.view === 'tab' && isArray(value)) {
                // 子表(一对一)
                if (item.properties) {
                  value.forEach((vItem) => {
                    recursion(vItem, item.properties);
                  });
                }
                // 子表（一对多）
                if (item.columns?.length) {
                  const findArr = cloneDeep(item.columns.filter((cItem) => cItem.type === 'date' && cItem.fieldExtendJson));
                  if (findArr.length) {
                    // 把结构处理成跟一对一一样；
                    const buildProperties = {};
                    findArr.forEach((fItem) => {
                      buildProperties[fItem.key] = {
                        view: 'date',
                        fieldExtendJson: fItem.fieldExtendJson,
                      };
                    });
                    value.forEach((vItem) => {
                      recursion(vItem, buildProperties);
                    });
                  }
                };
              } else if (item.view === 'date' && typeof value === 'string' && value !== '') {
                // 日期组件
                let fieldExtendJson = item.fieldExtendJson;
                if (fieldExtendJson) {
                  fieldExtendJson = JSON.parse(fieldExtendJson);
                  if (fieldExtendJson.picker && fieldExtendJson.picker !== 'default') {
                    /**
                     * 需要把年、年月、设置成这段时间内的第一天（[年季度]不需要处理antd回传的就是该季度的第一天，[年周]也不处理）
                     * 例如日期格式是年，传给数据库的时间必须是20240101
                     * 例如日期格式是年月（选择了202502），传给数据库的时间必须是20250201
                     */
                    if (fieldExtendJson.picker === 'year') {
                      formData[key] = dayjs(value).set('month', 0).set('date', 1).format('YYYY-MM-DD');
                    } else if (fieldExtendJson.picker === 'month') {
                      formData[key] = dayjs(value).set('date', 1).format('YYYY-MM-DD');
                    } else if (fieldExtendJson.picker === 'week') {
                      // 【TV360X-28】周也得重置到第一天，否则查询不出结果
                      formData[key] = dayjs(value).startOf('week').format('YYYY-MM-DD');
                    }
                  }
                }
              }
            }
          });
        };
        recursion(formData, properties);
      };
      /**
       * 发起请求
       * @param formData
       */
      function doApplyRequest(formData) {
        // 数组没有元素直接置空
        Object.keys(formData).map((key) => {
          if (Array.isArray(formData[key])) {
            if (formData[key].length == 0) {
              formData[key] = '';
            }
          }
        });
        // update-begin--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
        changeDateValue(formData);
        // update-end--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
        console.log('提交表单数据》》》form:', formData);
        let customUrl = customEditSubmitUrl.value;
        let url = `${urlObject.optPre}${props.id}?tabletype=${tableType.value}`;
        if (customUrl) {
          url = `${customUrl}?tabletype=${tableType.value}`;
        }
        // 如果需要提交流程 需要额外设置一个参数
        if (submitFlowFlag.value === true) {
          formData[SUBMIT_FLOW_KEY] = 1;
        }
        // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
        // 如果外键有则需要加上
        if (foreignkey.value.field && foreignkey.value.value) {
          formData[foreignkey.value.field] = foreignkey.value.value;
        }
        // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
        let method = isUpdate.value === true ? 'put' : 'post';
        defHttp
          .request({ url, method, params: formData }, { isTransformResponse: false })
          .then((res) => {
            //console.log('表单提交完成', res)
            if (res.success) {
              if (res.result) {
                formData[SUBMIT_FLOW_ID] = res.result;
              }
              //刷新列表
              emit('success', formData);
              // 工单申请提交的表单也会走这个逻辑，保存成功不需要提示信息
              if(props.submitTip === true && showTip.value === true){
                $message.success(res.message)
              }
            } else {
              $message.warning(res.message);
            }
          })
          .finally(() => {
            loading.value = false;
            emit('close');
          });
      }

      function triggleChangeValues(values, id, target) {
        if (id && target) {
          if (target.vxeProps) {
            //一对多子表
            target.setValues([
              {
                rowKey: id,
                values: values,
              },
            ]);
          } else {
            //一对一子表
            target.setValues(values);
          }
        } else {
          //主表
          setFieldsValue(values);
        }
      }
      function triggleChangeValue(field, value) {
        let obj = {};
        obj[field] = value;
        setFieldsValue(obj);
      }

      // 一对多子表Tab的Key，用于校验未通过时自动跳转
      const subActiveKey = ref('0');
      // update-begin-author:liaozhiyang---date:20240313---for：【QQYUN-9034】online弹窗一对一子表移动端内容高度设置不合理
      const subFormHeight = ref(getIsMobile.value ? 'auto' : 500);
      // update-end-author:liaozhiyang---date:20240313---for：【QQYUN-9034】online弹窗一对一子表移动端内容高度设置不合理
      // 子表表格高度
      // 【VUEN-803】一对多子表固定340高度，修复自定义列组件被遮挡的问题
      const subTableHeight = ref(340);

      function getSubTableForeignKeyValue(key) {
        if (isUpdate.value === true) {
          let formData = dbData.value;
          return getValueIgnoreCase(formData, key);
        }
        return '';
      }

      /**
       * VUEN-1056 30、生成的一对多，编辑的时候，子表数据挂不上
       */
      function getValueIgnoreCase(data, key) {
        if (data) {
          let temp = data[key];
          if (!temp && temp !== 0) {
            temp = data[key.toLowerCase()];
            if (!temp && temp !== 0) {
              temp = data[key.toUpperCase()];
            }
          }
          return temp;
        }
        return '';
      }

      //处理一对一子表的表单改变事件
      function handleSubFormChange(valueObj, tableKey) {
        if (EnhanceJS && EnhanceJS[tableKey + '_onlChange']) {
          let tableChangeObj = EnhanceJS[tableKey + '_onlChange']();
          let columnKey = Object.keys(valueObj)[0];
          if (tableChangeObj[columnKey]) {
            let subRef = getSubTableInstance(tableKey)
            let formEvent = subRef.getFormEvent();
            let event = {
              column: { key: columnKey },
              value: valueObj[columnKey],
              ...formEvent,
            };
            tableChangeObj[columnKey].call(onlineFormContext, onlineFormContext, event);
          }
        }
      }

      //处理一对多子表的改变事件
      function handleValueChange(event, tableKey) {
        if (EnhanceJS && EnhanceJS[tableKey + '_onlChange']) {
          let tableChangeObj = EnhanceJS[tableKey + '_onlChange'](onlineFormContext);
          //update-begin-author:taoyan date:2022-8-12 for: VUEN-1892【online子表弹框】有主从关联js时，子表弹框修改了数据，主表字段未修改
          if(event.column === 'all'){
            let keys = Object.keys(tableChangeObj);
            if(keys.length>0){
              for(let key of keys){
                tableChangeObj[key].call(onlineFormContext, onlineFormContext, event);
              }
            }
          }else{
            let columnKey = event.column.key || event.col.key
            if (tableChangeObj[columnKey]) {
              //event.row.id 加这个条件是： popform添加数据到vxetable的时候 不需要触发改变事件
              if(event.row && event.row.id){
                tableChangeObj[columnKey].call(onlineFormContext, onlineFormContext, event);
              }
            }
          }
          //update-end-author:taoyan date:2022-8-12 for: VUEN-1892【online子表弹框】有主从关联js时，子表弹框修改了数据，主表字段未修改
        }
      }

      /**
       * 2024-07-30
       * liaozhiyang
       * 【TV360X-1958】一对多子表删除时js增强 子表名_onlChange 事件没触发
       *  处理一对多子表的删除事件
       * */
      function handleRemoved(event, tableKey) {
        if (EnhanceJS && EnhanceJS[tableKey + '_onlChange']) {
          let tableChangeObj = EnhanceJS[tableKey + '_onlChange'](onlineFormContext);
          let keys = Object.keys(tableChangeObj);
          if (keys.length > 0) {
            for (let key of keys) {
              tableChangeObj[key]?.call(onlineFormContext, onlineFormContext, {...event, row: event.deleteRows});
            }
          }
        }
      }

      // 当行编辑新增完成后触发的事件
      function handleAdded(sub, event) {
        console.log('handleAdded', sub, event)
        //update-begin-author:taoyan date:2022-6-26 for: 控制台警告 这里直接调用函数 不触发事件了
       // event.target.emit('executeFillRule', event);
        if(!event.isModalData){
          handleSubTableDefaultValue(sub, event)
        }
        //update-end-author:taoyan date:2022-6-26 for: 控制台警告 这里直接调用函数 不触发事件了
      }

      function getSubTableAuthPre(table) {
        return 'online_' + table + ':';
      }

      //监听表单改变事件
      async function onValuesChange(columnKey, value) {
        //console.log('columnKey-value', `${columnKey}-${value}`)
        if (!EnhanceJS || !EnhanceJS['onlChange']) {
          return false;
        }
        if (!columnKey) {
          return false;
        }
        //let tableChangeObj = EnhanceJS["onlChange"].call(onlineFormContext);
        let tableChangeObj = EnhanceJS['onlChange']();
        if (tableChangeObj[columnKey]) {
          // update-begin--author:liaozhiyang---date:20250106---for：【issues/7642】js增强onlchange事件event.row.字段获取的是变化前的值
          setTimeout(async () => {
            let formData = await getFieldsValue();
            let event = {
              row: formData,
              column: { key: columnKey },
              value: value,
            };
            tableChangeObj[columnKey].call(onlineFormContext, onlineFormContext, event);
          }, 0);
          // update-end--author:liaozhiyang---date:20250106---for：【issues/7642】js增强onlchange事件event.row.字段获取的是变化前的值
        }
      }

      /**
       * 执行online表单的 hook
       * QQYUN-5315【online表单】online表单也需要支持hook增强
       */
      function executeEnhanceFormJsHook(funStr) {
        let str = funStr.toLocaleString();
        let arr = str.match(GET_FUN_BODY_REG);
        if (arr.length > 1) {
          let temp = arr[1];
          executeJsEnhanced(temp);
        }
      }

      // 自定义按钮 增强触发事件
      function handleCgButtonClick(optType, buttonCode) {
        if ('js' == optType) {
          let hookFunName = buttonCode+"_hook";
          if (EnhanceJS && EnhanceJS[buttonCode]) {
            EnhanceJS[buttonCode].call(onlineFormContext, onlineFormContext);
          }else if(EnhanceJS && EnhanceJS[hookFunName]){
            executeEnhanceFormJsHook(EnhanceJS[hookFunName])
          }
        } else if ('action' == optType) {
          let formData = dbData.value;
          let params = {
            formId: props.id,
            buttonCode: buttonCode,
            dataId: formData.id,
            uiFormData: Object.assign({}, formData),
          };
          //console.log("自定义按钮请求后台参数：",params)
          defHttp
            .post(
              {
                url: `${urlObject.urlButtonAction}`,
                params,
              },
              { isTransformResponse: false }
            )
            .then((res) => {
              if (res.success) {
                $message.success('处理完成!');
                emit('success', formData);
              } else {
                $message.warning('处理失败!');
              }
            });
        }
      }

      // --------------------增强-------------------------

      /**
       * 清除子表数据
       * @param tbname
       */
      function clearSubRows(tbname) {
        let instance = getSubTableInstance(tbname)
        let rows = [...instance.getNewDataWithId(), ...subDataSource.value[tbname]];
        if (!rows || rows.length == 0) {
          return false;
        }
        let ids = [];
        for (let i of rows) {
          ids.push(i.id);
        }
        instance.removeRowsById(ids);
      }

      /**
       * 添加子表数据
       * @param tbname
       * @param rows 可以是数组也可以是对象
       * @returns {boolean}
       */
      function addSubRows(tbname, rows) {
        if (!rows) {
          return false;
        }
        let instance = getSubTableInstance(tbname)
        if (typeof rows == 'object') {
          instance.addRows(rows, true);
        } else {
          this.$message.error('添加子表数据,参数不识别!');
        }
      }

      /**
       * 先删除后添加
       * @param tbname
       * @param rows
       */
      function clearThenAddRows(tbname, rows) {
        clearSubRows(tbname);
        addSubRows(tbname, rows);
      }

      /**
       * 修改下拉框的下拉选项
       * @param field
       * @param options
       */
      function changeOptions(field, options) {
        if (!options && options.length <= 0) {
          options = [];
        }
        options.map((item) => {
          if (!item.hasOwnProperty('label')) {
            item['label'] = item.text;
          }
        });
        updateSchema({
          field,
          componentProps: {
            options,
          },
        });
      }
      /**
       * 2024-03-21
       * liaozhiyang
       * js增强，改变下拉搜索options
       */
      function changeRemoteOptions({ field, dict, label, type, subTableName }) {
        const _dict = dict
          .split(',')
          .map((item) => encodeURIComponent(item))
          .join(',');
        if (type == 'subTable') {
          // 一对多
          const findSubTableInfo = subTabInfo.value.find((sub) => sub.key === subTableName);
          if (findSubTableInfo) {
            const findIndex = findSubTableInfo.columns.findIndex((item) => item.key === field);
            if (findIndex !== -1) {
              defHttp.get({
                url: `/sys/dict/loadDict/${_dict}`,
                params: { keyword: '',pageSize:1000 },
              }) .then((res) => {
                const data = dict.split(',');
                const result = { customOptions:true ,dictTable: data[0], dictCode: data[2], dictText: data[1],options:res };
                label && (result.title = label);
                findSubTableInfo.columns[findIndex] = { ...findSubTableInfo.columns[findIndex], ...result };
                window.findSubTableInfo=findSubTableInfo;
              });
            }
          }
        } else if (type == 'subForm') {
          // 一对一
          if (refMap[subTableName]?.value?.[0]) {
            const result = {
              field,
              componentProps: {
                dict: _dict,
              },
            };
            label && (result.label = label);
            refMap[subTableName].value[0].updateSchema(result);
          }
        } else {
          // 单表
          const result = {
            field,
            componentProps: {
              dict: _dict,
            },
          };
          label && (result.label = label);
          updateSchema(result);
        }
      }
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
      /**
       * 2024-03-13
       * liaozhiyang
       * 改变一对多子表某个字段的options选项（彼时是为了提供给js增强使用）
       */
      function changeSubTableOptions(subTableName, field, options) {
        const findSubTableInfo = subTabInfo.value.find((sub) => sub.key === subTableName);
        if (findSubTableInfo) {
          if (!options && options.length <= 0) {
            options = [];
          }
          options.map((item) => {
            if (!item.hasOwnProperty('label')) {
              item['label'] = item.text;
            }
          });
          const findIndex = findSubTableInfo.columns.findIndex((item) => item.key === field);
          if (findIndex !== -1) {
            findSubTableInfo.columns[findIndex] = { ...findSubTableInfo.columns[findIndex], ...{ options: options, dictCode: '' } };
          }
        }
      }

      /**
       * 2024-03-13
       * liaozhiyang
       * 改变一对一子表某个字段的options选项（彼时是为了提供给js增强使用）
       */
      function changeSubFormbleOptions(subTableName, field, options) {
        if (refMap[subTableName]?.value?.[0]) {
          if (!options && options.length <= 0) {
            options = [];
          }
          options.map((item) => {
            if (!item.hasOwnProperty('label')) {
              item['label'] = item.text;
            }
          });
          refMap[subTableName].value[0].updateSchema({
            field,
            componentProps: {
              dictCode: '',
              options,
            },
          });
        }
      }
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
      /**
       * 表单提交前事件
       * @param that
       * @param formData
       * @returns {Promise<void>|*}
       */
      function customBeforeSubmit(that, formData) {
        if (EnhanceJS && EnhanceJS['beforeSubmit']) {
          return EnhanceJS['beforeSubmit'](that, formData);
        } else {
          return Promise.resolve();
        }
      }

      /**
       * 处理自定义弹框 字段的显示隐藏
       * @param show
       * @param hide
       */
      function handleCustomFormSh(show, hide) {
        let plain = toRaw(fieldDisplayStatus);
        // update-begin--author:liaozhiyang---date:20241101---for：【issues/7387】js增强使用openCustomModal超过两个时show显示错误
        Object.keys(plain).map((k) => {
          if (k.endsWith('_load') || k.endsWith('_disabled')) {
            // 不处理（不是字段）；
          } else {
            fieldDisplayStatus[k] = true;
          }
        });
        // update-end--author:liaozhiyang---date:20241101---for：【issues/7387】js增强使用openCustomModal超过两个时show显示错误
        if (show && show.length > 0) {
          Object.keys(plain).map((k) => {
            if (!k.endsWith('_load') && show.indexOf(k) < 0) {
              fieldDisplayStatus[k] = false;
            }
          });
        } else if (hide && hide.length > 0) {
          Object.keys(plain).map((k) => {
            if (hide.indexOf(k) >= 0) {
              fieldDisplayStatus[k] = false;
            }
          });
        }
      }

      async function handleCustomFormEdit(record, editSubmitUrl) {
        console.log('自定义弹窗打开online表单2》》form', record);
        customEditSubmitUrl.value = editSubmitUrl;
        await resetFields();
        dbData.value = '';
        isUpdate.value = true;
        // 编辑数据
        await edit(record);
        await nextTick(() => {
          // 所有信息加载完毕 触发loaded事件
          handleCgButtonClick('js', 'loaded');
        });
      }

      /**
       * VUEN-1036
       * 获取子表的实例对象 可以直接调用子表的方法
       */
      function getSubTableInstance(tableName) {
        let instance = refMap[tableName].value;
        if (instance instanceof Array) {
          instance = instance[0];
        }
        if(!instance){
          $message.warning('子表ref找不到:'+tableName);
          return;
        }
        return instance;
      }

      //跳转至积木报表页面
      function onOpenReportPrint(){
        let url = onlineExtConfigJson.reportPrintUrl;
        let id = dbData.value.id;
        let token = getToken();
        goJmReportViewPage(url, id, token)
      }
      //打印表单组件
      function onCompPrint(){
        //组件打印
        setTimeout(() => {
          if (!printRef.value) return;

          const iframe = document.createElement('iframe');
          iframe.style.display = 'none';
          document.body.appendChild(iframe);

          const iframeDoc = iframe.contentDocument || iframe.contentWindow?.document;
          if (!iframeDoc) return;

          // 复制样式和内容
          iframeDoc.open();
          iframeDoc.write(`
          <!DOCTYPE html>
          <html>
            <head>
              <title>打印表单</title>
              ${Array.from(document.querySelectorAll('style, link[rel="stylesheet"]'))
              .map((tag) => tag.outerHTML)
              .join('')}
            </head>
            <body>
              ${printRef.value.outerHTML}
            </body>
          </html>
        `);
          // 添加打印样式
          const scrollHeight = printRef.value.scrollHeight;
          const style = iframeDoc.createElement('style');
          style.innerHTML = `
            body {
              margin: 0;
              padding: 15px;
              font-family: Arial, sans-serif;
            }
            img {
              max-width: 100%;
              height: auto;
            }
            @page {
              size: auto;
              margin: 15mm;
            }
            @media print {
              body {
                padding: 0;
                height: ${scrollHeight}px;
              }
            }
          `;
          iframeDoc.head.appendChild(style);
          iframeDoc.close();

          // 打印后移除 iframe
          iframe.contentWindow?.addEventListener('afterprint', () => {
            document.body.removeChild(iframe);
          });

          // 触发打印
          setTimeout(() => {
            iframe.contentWindow?.print();
          }, 200);
        }, 10);
      }

      //-----------------------------------一对多子表弹窗操作数据-begin-------------------------------------------------------------
      const [registerPopModal, { openModal: openPopModal }] = useModal();
      // -update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
      // 表ID
      const popTableId = ref('');
      // 表名
      const popTableName = ref('');
      // -update-end--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
      // 是否发起请求
      const popModalRequest = ref(true);

      // update-begin--author:liaozhiyang---date:20260519---for：【QQYUN-9773】online一对多内部弹窗新增js增强
      // 父→子注入：主表 context + enhanceJs（用 getter 取最新值，因为 EnhanceJS 在 createRootProperties 里后赋值）+ 当前打开的子表 key
      provide('parentOnlineForm', {
        context: onlineFormContext,
        getEnhanceJs: () => EnhanceJS,
        subTableKey: popTableName,
      });
      // update-end--author:liaozhiyang---date:20260519---for：【QQYUN-9773】online一对多内部弹窗新增js增强

      /**
       * 新增
       * @param sub
       */
      function openSubFormModalForAdd(sub){
        console.log('openSubFormModalForAdd', sub)
        // -update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
        popTableId.value = sub.id;
        popTableName.value = sub.key;
        // -update-end--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
        popModalRequest.value = false;
        openPopModal(true,{isUpdate: false, tableType: '3'})
      }

      /**
       * 编辑
       * @param sub
       */
      function openSubFormModalForEdit(sub){
        let ref = getSubTableInstance(sub.key)
        let arr = ref.getSelectedData();
        if(arr.length!=1){
          $message.warning('请选择一条数据');
          return;
        }
        // -update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
        popTableId.value = sub.id;
        //update-begin-author:liusq---date:2024-08-28--for: [TV360X-2262]online表单对接流程权限，设置字段不可编辑，但是这俩按钮可以修改
        popTableName.value = sub.key;
        //update-end-author:liusq---date:2024-08-28--for: [TV360X-2262]online表单对接流程权限，设置字段不可编辑，但是这俩按钮可以修改
        // -update-end--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
        popModalRequest.value = false;
        openPopModal(true,{isUpdate:true, record: arr[0]})
      }

      /**
       * 将数据回填到当前vxe table
       * @param data
       */
      function getPopFormData(data){
        const tableName = data[ONL_FORM_TABLE_NAME];
     //   console.log('getFormData', data)
        let record = omit(data, [ONL_FORM_TABLE_NAME]);
        if(record.id){
          let values = omit({...record}, 'id');
          let arr = [{ rowKey: record.id, values }];
          let instance = getSubTableInstance(tableName)
          instance.setValues(arr);
        }else{
          let instance = getSubTableInstance(tableName)
          instance.addRows(record, {isOnlineJS: false, setActive:false, emitChange: true, isModalData: true});
        }
      }
      //-----------------------------------一对多子表弹窗操作数据-end-------------------------------------------------------------

      //update-begin-author:taoyan date:2022-10-17 for: VUEN-2480【严重bug】online vue3测试的问题 6、勾中弹窗编辑后，再次打开，发现不能取消勾中
      function onCloseModal(){
        // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
        // update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
        // 分享入口 ERP 新建主子表未关联
        if (props.themeTemplate === ERP && !props.isShare) return;
        // update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
        // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
        let arr  = subTabInfo.value;
        if(arr && arr.length>0){
          for(let item of arr){
            if(item.relationType==1){
              //1对1忽略
            }else{
              let inst = getSubTableInstance(item.key);
              if(inst){
                inst.clearSelection();
              }
            }
          }
        }
      }
      //update-end-author:taoyan date:2022-10-17 for: VUEN-2480【严重bug】online vue3测试的问题 6、勾中弹窗编辑后，再次打开，发现不能取消勾中

      /** 供 js 增强调用：重新执行主表的填值规则  */
      function executeMainFillRule() {
        let formData = getFieldsValue();
        let fieldProperties = toRaw(defaultValueFields[tableName.value]);
        loadFormFieldsDefVal(fieldProperties, (values) => {
          setFieldsValue(values);
        }, formData);
      }

      /** 供 js 增强调用：重新执行子表的填值规则  */
      function executeSubFillRule(subKey, $event){
        let subList = subTabInfo.value;
        if(subList && subList.length>0){
          let arr = subList.filter(sub=>sub.key === subKey);
          if(arr.length==0){
            console.error('没找到与之匹配的子表', subKey)
            return;
          }
          if(arr[0].relationType == 1){
            //一对一
            let subInstance = getSubTableInstance(subKey);
            subInstance.executeFillRule();
          }else{
            // 一对多
            let subFieldProperties = toRaw(defaultValueFields[subKey]);
            let row = toRaw($event.row)
            loadFormFieldsDefVal(subFieldProperties,(values) => {
              const { row, target } = $event;
              let v = [{ rowKey: row.id, values: { ...values } }];
              target.setValues(v);
            }, row);
          }
        }


      }

      let context = {
        tableName,
        loading,
        subActiveKey,
        onlineFormRef,
        getFieldsValue,
        setFieldsValue,
        submitFlowFlag,
        subFormHeight,
        subTableHeight,
        refMap,
        triggleChangeValues,
        triggleChangeValue,
        sh: fieldDisplayStatus,
        clearSubRows,
        addSubRows,
        clearThenAddRows,
        changeOptions,
        isUpdate,
        getSubTableInstance,
        updateSchema,
        executeMainFillRule,
        executeSubFillRule,
        // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
        changeSubTableOptions,
        changeSubFormbleOptions,
        // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
        changeRemoteOptions,
        changEvent: ()=>{},
        onlineFormValueChange,
        // update-begin--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
        submitFormAndFlow,
        // update-end--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
      };
      resetContext(context);
      // -update-begin--author:liaozhiyang---date:20240614---for：【TV360X-1017】一对多新增、编辑钮根据权限控制
      const getBtnAuth = (code, tableName) => {
        const prefix = getSubTableAuthPre(tableName);
        const permissionStore = usePermissionStore();
        let onlineButtonAuths = permissionStore.getOnlineSubTableAuth(prefix);
        if (onlineButtonAuths?.length) {
          const result = onlineButtonAuths.find((item) => item === code);
          return result ? false : true;
        }
        return true;
      };
      // -update-end--author:liaozhiyang---date:20240614---for：【TV360X-1017】一对多新增、编辑钮根据权限控制

      return {
        //主表
        tableName,
        onlineFormRef,
        registerForm,
        loading,

        //子表
        subActiveKey,
        hasSubTable,
        subTabInfo,
        refMap,

        //一对一子表
        subFormHeight,
        getSubTableForeignKeyValue,
        isUpdate,
        handleSubFormChange,

        //一对多子表
        subTableHeight,
        onlineFormDisabled,
        subDataSource,
        getSubTableAuthPre,
        handleAdded,
        handleSubTableDefaultValue,
        handleValueChange,
        openSubFormModalForAdd,
        openSubFormModalForEdit,
        getBtnAuth,
        handleRemoved,
        //父组件调用
        show,
        createRootProperties,
        handleSubmit,
        handleValidate,
        sh: fieldDisplayStatus,
        handleCgButtonClick,
        handleCustomFormSh,
        handleCustomFormEdit,
        //跳转
        dbData,
        onOpenReportPrint,
        onlineExtConfigJson,
        onCompPrint,
        printRef,
        //一对多子表弹窗操作数据
        registerPopModal,
        popTableId,
        popTableName,
        getPopFormData,
        popModalRequest,
        onCloseModal,
        ERP,
        rowNumber,
        isSetFormLabelLength,
        subFormWrapRef,

        getSubAddBtnCfg,
        getSubRemoveBtnCfg,
        getSubOpenAddBtnCfg,
        getSubOpenEditBtnCfg,
      };
    },
  };
</script>

<style lang="less" scoped>
  .onlineFormWrap {
    // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    padding: 20px 1.5% 0;
    // update-begin--author:liaozhiyang---date:20240506---for：【QQYUN-9229】间隔调整
    &.formTemplate_1 {
      > form {
        padding-left: 5%;
        padding-right: 5%;
      }
    }
    &.formTemplate_2 {
      > form {
        padding-left: 1%;
        padding-right: 1%;
      }
    }
    // update-end--author:liaozhiyang---date:20240506---for：【QQYUN-9229】间隔调整
    :deep(.ant-form) {
      > .ant-row {
        > .ant-col {
          padding: 0 6px;
        }
      }
    }
    // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7632】 label栅格改成labelwidth固宽
    :deep(.vxe-buttons--wrapper) {
      > div {
        display: flex;
      }
    }
    .sub-popup-button-group {
      margin-right: 8px;

      :deep(.ant-btn) {
        margin-right: 0;
      }
    }
    // update-begin--author:liaozhiyang---date:20240517---for：【QQYUN-9353】markdown全屏之后，弹窗的关闭和全屏按钮在markdown上面
    :deep(.vditor--fullscreen) {
      z-index: 1011 !important;
    }
    // update-end--author:liaozhiyang---date:20240517---for：【QQYUN-9353】markdown全屏之后，弹窗的关闭和全屏按钮在markdown上面
  }
</style>
