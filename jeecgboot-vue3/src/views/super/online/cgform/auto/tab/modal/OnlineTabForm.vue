<template>
  <div :id="tableName + '_form'" class="onlineFormWrap" :class="[`formTemplate_${formTemplate}`]">
    <!-- update-begin--author:liaozhiyang---date:20240401---for：【QQYUN-8721】编辑页面不显示打印（配置了积木报表） -->
    <!-- 积木报表的打印按钮，只有配置了 reportUrl 才显示 -->
    <!-- <div
      v-if="!!dbData.id && !!onlineExtConfigJson.reportPrintShow"
      style="text-align: right; position: absolute; top: 6px; right: 20px; z-index: 999"
    >
      <PrinterOutlined title="打印" @click="onOpenReportPrint" style="font-size: 16px" />
    </div> -->
    <!-- update-end--author:liaozhiyang---date:20240401---for：【QQYUN-8721】编辑页面不显示打印（配置了积木报表） -->

    <a-tabs class="tabTheme" v-model:activeKey="subActiveKey">
      <a-tab-pane tab="主表" :key="'-1'">
        <BasicForm ref="onlineFormRef" @register="registerForm" />
      </a-tab-pane>
      <template v-if="hasSubTable">
        <a-tab-pane v-for="(sub, index) in subTabInfo" :tab="sub.describe" :key="index + ''" :forceRender="true">
          <div :style="{ 'overflow-y': 'auto', 'overflow-x': 'hidden', 'max-height': subFormHeight + 'px' }" v-if="sub.relationType == 1">
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
              @executeFillRule="handleSubTableDefaultValue(sub, $event)"
            >
              <template #toolbarAfterAdd="{ selectedRowIds }">
                <a-button-group class="sub-popup-button-group">
                  <!-- 子表内弹出新增按钮 -->
                  <a-button
                      v-if="!onlineFormDisabled && getSubOpenAddBtnCfg.enabled && getBtnAuth('popup_add', sub.key)"
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
      </template>
    </a-tabs>
    <Loading :loading="loading" :absolute="false" />
    <!-- 表单中可以通过slot自定义提交按钮，流程表单中用到 -->
    <slot name="bottom"></slot>

    <!-- 弹窗到另外一张表单用-可编辑表单 -->
    <online-pop-modal
      formTableType="3"
      :request="popModalRequest"
      :id="popTableName"
      @register="registerPopModal"
      @success="getPopFormData"
      topTip
      isVxeTableData
    />
  </div>
</template>

<script>
  import { useMessage } from '/@/hooks/web/useMessage';
  import { computed, ref, unref, watch, nextTick, toRaw, reactive, inject } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { SUBMIT_FLOW_ID, SUBMIT_FLOW_KEY, VALIDATE_FAILED, ONL_FORM_TABLE_NAME } from '../../../types/onlineRender';
  import { defHttp } from '/@/utils/http/axios';
  import { pick, omit } from 'lodash-es';
  import { sleep } from '/@/utils';
  import { useFormItems, getRefPromise, useOnlineFormContext } from '../../../hooks/auto/useAutoForm';
  import { Loading } from '/@/components/Loading';
  import { useEnhance } from '../../../hooks/auto/useEnhance';
  import OnlineSubForm from '../../comp/OnlineSubForm.vue';
  import { loadFormFieldsDefVal } from '../../../util/FieldDefVal';
  import { getToken } from '/@/utils/auth';
  import { goJmReportViewPage } from '/@/utils';
  import { PrinterOutlined } from '@ant-design/icons-vue';
  import OnlinePopModal from '../../comp/OnlinePopModal.vue';
  import { useModal } from '/@/components/Modal';
  import { useCustomHook, GET_FUN_BODY_REG } from '../../../hooks/auto/useCustomHook';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { usePermissionStore } from '/@/store/modules/permission';

  const urlObject = {
    optPre: '/online/cgform/api/form/',
    urlButtonAction: '/online/cgform/api/doButton',
  };
  export default {
    name: 'OnlineTabForm',
    components: {
      BasicForm,
      Loading,
      OnlineSubForm,
      PrinterOutlined,
      OnlinePopModal,
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
      submitTip: {
        type: Boolean,
        default: true,
      },
      modalClass: {
        type: String,
        default: '',
      },
      themeTemplate: {
        type: String,
        default: '',
      },
      tabIndex: {
        type: String,
        default: '',
      },
      cgBIBtnMap: Object,
      buttonSwitch: Object,
    },
    emits: ['success', 'rendered', 'toggleTab'],
    setup(props, { emit }) {
      console.log('onlineForm-setup》》');
      const { createMessage: $message } = useMessage();

      // 表单ref
      const onlineFormRef = ref(null);
      const single = ref(true);
      // 加载状态
      const loading = ref(false);
      const tableType = ref(1);
      // 自定义弹窗的编辑提交地址
      const customEditSubmitUrl = ref('');

      // 表单提交且提交流程
      const submitFlowFlag = ref(false);
      const isUpdate = ref(false);
      const { getIsMobile } = useAppInject();
      const rowNumber = ref(getIsMobile.value ? false : true);

      /**
       * online表单扩展配置
       */
      const onlineExtConfigJson = reactive({
        reportPrintShow: 0,
        reportPrintUrl: '',
        joinQuery: 0,
        modelFullscreen: 0,
        modalMinWidth: '',
        commentStatus: 0,
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
        fieldDisplayStatus,
        labelCol,
        wrapperCol,
        labelWidth
      } = useFormItems(props, onlineFormRef);
      let { EnhanceJS, initCgEnhanceJs } = useEnhance(onlineFormContext, false);

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
          }
          handleDefaultValue();
          // 所有信息加载完毕 触发loaded事件
          handleCgButtonClick('js', 'loaded');
          //处理表单的禁用效果
          handleFormDisabled();
        });
      }
      //update-begin-author:taoyan date:2022-5-17 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的
      /**
       * 如果是树 需要重新设置pid的formSchema--dict信息 让他刷新树控件的数据
       */
      async function updatePidFieldDict() {
        if (props.isTree === true) {
          let pidFieldName = props.pidField;
          let arr = formSchemas.value;
          if (arr && arr.length > 0) {
            //先拿到pid的 componentProps配置
            let temp = arr.filter((item) => item.field === pidFieldName);
            if (temp.length > 0) {
              await updateSchema({
                field: pidFieldName,
                componentProps: {
                  reload: new Date().getTime(),
                  // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
                  hiddenNodeKey: '',
                  // update-end--author:liaozhiyang---date:20240529---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
                },
              });
            }
          } else {
            console.log('没有拿到表单配置信息，可能是第一次打开新增页面');
          }
        }
      }
      //update-end-author:taoyan date:2022-5-17 for: VUEN-1056 15、严重——online树表单，添加的时候，父亲节点是空的

      /**
       * 当前表单默认值逻辑-进入新增页面触发
       */
      function handleDefaultValue() {
        if (unref(isUpdate) === false) {
          let fieldProperties = toRaw(defaultValueFields[tableName.value]);
          loadFormFieldsDefVal(fieldProperties, (values) => {
            setFieldsValue(values);
          });
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
        if (props.disabled) {
          //详情页面 将空的数据丢弃
          Object.keys(values).map((k) => {
            if (!values[k] && values[k] !== 0 && values[k] !== '0') {
              delete values[k];
            }
          });
        }
        await setFieldsValue(values);
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
          if (changeFormData) {
            //如果存在其他表单控件的数据，直接设置该值
            setFieldsValue(changeFormData);
          }
          onChangeEvent(field, value, changeFormData);
        };
        // QQYUN-5315【online表单】online表单也需要支持hook增强
        if (EnhanceJS && EnhanceJS['setup']) {
          executeEnhanceFormJsHook(EnhanceJS['setup']);
        }
      }

      //  触发表单change事件 区别于老的onlchange
      function onChangeEvent(field, value, changeFormData) {
        onlineFormContext.changEvent(field, value, changeFormData);
      }

      // 添加表单change事件
      function onlineFormValueChange(callback) {
        onlineFormContext.addObject2Context('changEvent', callback);
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
        }
        Object.keys(extConfigJson).map((k) => {
          onlineExtConfigJson[k] = extConfigJson[k];
        });
      }

      // 提交表单和流程
      function submitFormAndFlow() {
        submitFlowFlag.value = true;
        handleSubmit();
      }

      function handleSubmit() {
        if (single.value === true) {
          handleSingleSubmit();
        } else {
          handleOne2ManySubmit();
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
            ({ errorFields }) => {
              reject({
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
            return validateSubTableFields();
          })
          .then((allTableData) => {
            Object.assign(temp, allTableData);
            return Promise.resolve(temp);
          })
          .catch((e) => {
            if (e === VALIDATE_FAILED || e?.code === VALIDATE_FAILED) {
              $message.warning('校验未通过');
              if (e.key) {
                changeTab(e.key);
                if (e.scrollToField) {
                  setTimeout(() => e.scrollToField(), 150)
                }
              }
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
        // update-begin-author:liaozhiyang---date:20240523---for：【TV360X-126】主子表-tab风格，新建（编辑）时子表校验未通过，内容切换到子表了但是tab没切换
        if (key === tableName.value) {
          emit('toggleTab', '-1');
          return;
        }
        // update-end-author:liaozhiyang---date:20240523---for：【TV360X-126】主子表-tab风格，新建（编辑）时子表校验未通过，内容切换到子表了但是tab没切换
        let arr = subTabInfo.value;
        for (let i = 0; i < arr.length; i++) {
          if (key == arr[i].key) {
            // update-begin-author:sunjianlei date:2022-7-7 for:【VUEN-1425】【vue3】对多子表中校验不通过不提示
            let activeKey = i + '';
            if (subActiveKey.value === activeKey) {
              break;
            }
            // update-begin-author:liaozhiyang---date:20240523---for：【TV360X-126】主子表-tab风格，新建（编辑）时子表校验未通过，内容切换到子表了但是tab没切换
            // subActiveKey.value = activeKey;
            emit('toggleTab', activeKey);
            // update-end-author:liaozhiyang---date:20240523---for：【TV360X-126】主子表-tab风格，新建（编辑）时子表校验未通过，内容切换到子表了但是tab没切换
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
          }
        } finally {
          loading.value = false;
        }
      }

      //提交数据前 先走一下自定义的JS校验
      function handleApplyRequest(formData) {
        customBeforeSubmit(context, formData)
          .then(() => {
            doApplyRequest(formData);
          })
          .catch((msg) => {
            $message.warning(msg);
          });
      }

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
              if (props.submitTip === true) {
                $message.success(res.message);
              }
            } else {
              $message.warning(res.message);
            }
          })
          .finally(() => {
            loading.value = false;
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
      const subFormHeight = ref('auto');
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
      watch(
        () => props.tabIndex,
        (value) => {
          subActiveKey.value = value;
        },
        {
          immediate: true,
        }
      );
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
            let subRef = getSubTableInstance(tableKey);
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
          if (event.column === 'all') {
            let keys = Object.keys(tableChangeObj);
            if (keys.length > 0) {
              for (let key of keys) {
                tableChangeObj[key].call(onlineFormContext, onlineFormContext, event);
              }
            }
          } else {
            let columnKey = event.column.key || event.col.key;
            if (tableChangeObj[columnKey]) {
              //event.row.id 加这个条件是： popform添加数据到vxetable的时候 不需要触发改变事件
              if (event.row && event.row.id) {
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
        console.log('handleAdded', sub, event);
        //update-begin-author:taoyan date:2022-6-26 for: 控制台警告 这里直接调用函数 不触发事件了
        // event.target.emit('executeFillRule', event);
        if (!event.isModalData) {
          handleSubTableDefaultValue(sub, event);
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
          let hookFunName = buttonCode + '_hook';
          if (EnhanceJS && EnhanceJS[buttonCode]) {
            EnhanceJS[buttonCode].call(onlineFormContext, onlineFormContext);
          } else if (EnhanceJS && EnhanceJS[hookFunName]) {
            executeEnhanceFormJsHook(EnhanceJS[hookFunName]);
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
        let instance = getSubTableInstance(tbname);
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
        let instance = getSubTableInstance(tbname);
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
        if (!instance) {
          $message.warning('子表ref找不到:' + tableName);
          return;
        }
        return instance;
      }

      //跳转至积木报表页面
      function onOpenReportPrint() {
        let url = onlineExtConfigJson.reportPrintUrl;
        let id = dbData.value.id;
        let token = getToken();
        goJmReportViewPage(url, id, token);
      }

      //-----------------------------------一对多子表弹窗操作数据-begin-------------------------------------------------------------
      const [registerPopModal, { openModal: openPopModal }] = useModal();
      //表名/表ID都可以
      const popTableName = ref('');
      // 是否发起请求
      const popModalRequest = ref(true);

      /**
       * 新增
       * @param sub
       */
      function openSubFormModalForAdd(sub) {
        console.log('openSubFormModalForAdd', sub);
        popTableName.value = sub.id;
        popModalRequest.value = false;
        openPopModal(true, { isUpdate: false, tableType: '3' });
      }

      /**
       * 编辑
       * @param sub
       */
      function openSubFormModalForEdit(sub) {
        let ref = getSubTableInstance(sub.key);
        let arr = ref.getSelectedData();
        if (arr.length != 1) {
          $message.warning('请选择一条数据');
          return;
        }
        popTableName.value = sub.id;
        popModalRequest.value = false;
        openPopModal(true, { isUpdate: true, record: arr[0] });
      }

      /**
       * 将数据回填到当前vxe table
       * @param data
       */
      function getPopFormData(data) {
        const tableName = data[ONL_FORM_TABLE_NAME];
        //   console.log('getFormData', data)
        let record = omit(data, [ONL_FORM_TABLE_NAME]);
        if (record.id) {
          let values = omit({ ...record }, 'id');
          let arr = [{ rowKey: record.id, values }];
          let instance = getSubTableInstance(tableName);
          instance.setValues(arr);
        } else {
          let instance = getSubTableInstance(tableName);
          instance.addRows(record, { isOnlineJS: false, setActive: false, emitChange: true, isModalData: true });
        }
      }
      //-----------------------------------一对多子表弹窗操作数据-end-------------------------------------------------------------

      //update-begin-author:taoyan date:2022-10-17 for: VUEN-2480【严重bug】online vue3测试的问题 6、勾中弹窗编辑后，再次打开，发现不能取消勾中
      function onCloseModal() {
        let arr = subTabInfo.value;
        if (arr && arr.length > 0) {
          for (let item of arr) {
            if (item.relationType == 1) {
              //1对1忽略
            } else {
              let inst = getSubTableInstance(item.key);
              if (inst) {
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
        loadFormFieldsDefVal(
          fieldProperties,
          (values) => {
            setFieldsValue(values);
          },
          formData
        );
      }

      /** 供 js 增强调用：重新执行子表的填值规则  */
      function executeSubFillRule(subKey, $event) {
        let subList = subTabInfo.value;
        if (subList && subList.length > 0) {
          let arr = subList.filter((sub) => sub.key === subKey);
          if (arr.length == 0) {
            console.error('没找到与之匹配的子表', subKey);
            return;
          }
          if (arr[0].relationType == 1) {
            //一对一
            let subInstance = getSubTableInstance(subKey);
            subInstance.executeFillRule();
          } else {
            // 一对多
            let subFieldProperties = toRaw(defaultValueFields[subKey]);
            let row = toRaw($event.row);
            loadFormFieldsDefVal(
              subFieldProperties,
              (values) => {
                const { row, target } = $event;
                let v = [{ rowKey: row.id, values: { ...values } }];
                target.setValues(v);
              },
              row
            );
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
        changEvent: () => {},
        onlineFormValueChange,
        // update-begin--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
        submitFormAndFlow,
        // update-end--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
      };
      resetContext(context);

      // update-begin--author:liusq---date:20260831---for：【权限控制】Tab风格一对多子表弹窗按钮按授权显示
      const getBtnAuth = (code, tableName) => {
        const prefix = getSubTableAuthPre(tableName);
        const permissionStore = usePermissionStore();
        const onlineButtonAuths = permissionStore.getOnlineSubTableAuth(prefix);
        return !onlineButtonAuths?.includes(code);
      };
      // update-end--author:liusq---date:20260831---for：【权限控制】Tab风格一对多子表弹窗按钮按授权显示

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
        handleRemoved,
        //父组件调用
        show,
        createRootProperties,
        handleSubmit,
        sh: fieldDisplayStatus,
        handleCgButtonClick,
        handleCustomFormSh,
        handleCustomFormEdit,
        //跳转
        dbData,
        onOpenReportPrint,
        onlineExtConfigJson,
        //一对多子表弹窗操作数据
        registerPopModal,
        popTableName,
        getPopFormData,
        popModalRequest,
        onCloseModal,
        rowNumber,

        getSubAddBtnCfg,
        getSubRemoveBtnCfg,
        getSubOpenAddBtnCfg,
        getSubOpenEditBtnCfg,
        getBtnAuth,
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
  .tabTheme {
    :deep(.ant-tabs-nav) {
      display: none;
    }
  }
</style>
