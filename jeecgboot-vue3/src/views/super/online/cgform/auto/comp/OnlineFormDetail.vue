<template>
  <div :id="tableName + '_form'">

    <!-- 积木报表的打印按钮，只有配置了 reportUrl 才显示 -->
    <div v-if="!!formData.id && !!onlineExtConfigJson.reportPrintShow" style="text-align: right;position: absolute;top: 15px;right: 20px;z-index: 999">
      <PrinterOutlined title="打印" @click="onOpenReportPrint" style="font-size: 16px"/>
    </div>
    
    <detail-form :schemas="detailFormSchemas" :data="formData" :span="formSpan"></detail-form>

    <!-- 子表 -->
    <!-- update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表 -->
    <a-tabs v-if="hasSubTable && showSub && (isShare || themeTemplate !== 'erp')" @change="onTabChange">
    <!-- update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表 -->
      <a-tab-pane v-for="(sub, index) in subTabInfo" :tab="sub.describe" :key="index + ''" :forceRender="true">
        <div :style="{ 'overflow-y': 'auto', 'overflow-x': 'hidden', 'max-height': subFormHeight + 'px' }" v-if="sub.relationType == 1">
          <!-- 子表-一对一 -->
          <online-sub-form-detail :key="subReloadKey" :table="sub.key" :form-template="sub.formTemplate" :main-id="getSubTableForeignKeyValue(sub.foreignKey)" :properties="sub.properties"> </online-sub-form-detail>
        </div>
        <div v-else>
          <!-- 子表-一对多 -->
          <JVxeTable
            v-if="showStatus[sub.key]"
            :ref="refMap[sub.key]"
            keep-source
            :row-number="rowNumber"
            row-selection
            :height="subTableHeight"
            :disabled="true"
            :columns="sub.columns"
            :dataSource="subDataSource[sub.key]"
            :authPre="getSubTableAuthPre(sub.key)"
          />
          <a-spin v-else :spinning="true"/>
        </div>
      </a-tab-pane>
    </a-tabs>
    <Loading :loading="loading" :absolute="false" />
    <slot name="bottom"></slot>
  </div>
</template>

<script lang="ts">
  import { useMessage } from '/@/hooks/web/useMessage';
  import { ref, reactive, watch } from 'vue';
  import { Loading } from '/@/components/Loading';
  import { getToken } from '/@/utils/auth';
  import { goJmReportViewPage } from '/@/utils';
  import { PrinterOutlined } from '@ant-design/icons-vue';
  import DetailForm from '../../extend/form/DetailForm.vue';
  import OnlineSubFormDetail from './OnlineSubFormDetail.vue';
  import { getDetailFormSchemas } from '../../hooks/auto/useAutoForm';
  import { defHttp } from '/@/utils/http/axios';
  import { ERP } from "../../util/constant";
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { useOnlineFormDetailContext } from '../../hooks/auto/useAutoFormDetail';
  import { useEnhance } from '../../hooks/auto/useEnhance';
  export default {
    name: 'OnlineFormDetail',
    components: {
      DetailForm,
      Loading,
      PrinterOutlined,
      OnlineSubFormDetail,
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
      showSub:{
        type: Boolean,
        default: true,
      },
      themeTemplate: {
        type: String,
        default: '',
      },
      // update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
      isShare: {
        type: Boolean,
        default: false,
      },
      // update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
    },
    emits: ['success', 'rendered'],
    setup(props, { emit }) {
      console.log('onlineForm-setup》》');
      const { createMessage: $message } = useMessage();
      const { getIsMobile } = useAppInject();
      const tableName = ref('');
      const single = ref(true);
      // 加载状态
      const loading = ref(false);
      const tableType = ref(1);

      const formData = ref<any>({});
      // update-begin-author:liaozhiyang---date:20240313---for：【QQYUN-9034】online弹窗一对一子表移动端内容高度设置不合理
      const subFormHeight = ref(getIsMobile.value ? 'auto' : 300);
      // update-end-author:liaozhiyang---date:20240313---for：【QQYUN-9034】online弹窗一对一子表移动端内容高度设置不合理
      const subReloadKey = ref(0);
      // 子表表格高度
      // 【VUEN-803】一对多子表固定340高度，修复自定义列组件被遮挡的问题
      const subTableHeight = ref(340);
      
      const rowNumber = ref(getIsMobile.value ? false : true);

      let detailData = {};
      // 字段展示状态
      const fieldDisplayStatus = reactive<any>({});

      /**
       * online表单扩展配置
       */
      const onlineExtConfigJson = reactive({
        reportPrintShow: 0,
        reportPrintUrl: '',
        joinQuery: 0,
        modelFullscreen: 0,
        modalMinWidth: '',
      });

      const { detailFormSchemas, hasSubTable, subTabInfo, refMap, showStatus, subDataSource, createFormSchemas, formSpan } = getDetailFormSchemas(props);

      /**
       * 处理扩展配置
       */
      function handleExtConfigJson(jsonStr) {
        let extConfigJson = { reportPrintShow: 0, reportPrintUrl: '', joinQuery: 0, modelFullscreen: 1, modalMinWidth: '' };
        if (jsonStr) {
          extConfigJson = JSON.parse(jsonStr);
        }
        Object.keys(extConfigJson).map((k) => {
          onlineExtConfigJson[k] = extConfigJson[k];
        });
      }
      // update-begin--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
      const { onlineFormDetailContext, resetContext } = useOnlineFormDetailContext();
      let { EnhanceJS, initCgEnhanceJs } = useEnhance(onlineFormDetailContext, false);
      // update-end--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能

      // 渲染表单
      async function createRootProperties(data) {
        tableType.value = data.head.tableType;
        tableName.value = data.head.tableName;
        single.value = data.head.tableType == 1;
        handleExtConfigJson(data.head.extConfigJson);
        createFormSchemas(data.schema.properties);
        // update-begin--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
        EnhanceJS = initCgEnhanceJs(data.enhanceJs);
        // update-end--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
        emit('rendered', onlineExtConfigJson);
      }

      /**
       * status: 是否是修改页面
       * record: 列表页面的行数据
       * param： 树形列表添加子节点 传入的父级节点id
       * */
      async function show(_status, record) {
        console.log('进入表单详情》》form', record);
        // -update-begin--author:liaozhiyang---date:20251209---for：【QQYUN-13970】一对一子表编辑之后查看详情不会更新
        subReloadKey.value++;
        // -update-end--author:liaozhiyang---date:20251209---for：【QQYUN-13970】一对一子表编辑之后查看详情不会更新
        await edit(record);
        changeShowStatus(true);
      }

      function getFormData(dataId) {
        let url = `/online/cgform/api/detail/${props.id}/${dataId}`;
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
      
      //update-begin-author:taoyan date:2023-2-13 for: QQYUN-4226【vue3】online 一对多子表 详情界面，序号错位了 点一下子表表格就正常了
      function changeShowStatus(flag){
        Object.keys(showStatus).map(k=>{
          showStatus[k] = flag;
        })
      }
      
      function onTabChange(){
        changeShowStatus(false);
        setTimeout(()=>{
          changeShowStatus(true);
        }, 300);
      }
      //update-end-author:taoyan date:2023-2-13 for: QQYUN-4226【vue3】online 一对多子表 详情界面，序号错位了 点一下子表表格就正常了

      async function edit(record) {
        let temp: any = await getFormData(record.id);
        // update-begin--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
        detailData = temp;
        // 每次打开有js增强设置隐藏的，都要先置成初始化。
        detailFormSchemas.value.filter((item) => item.hidden).forEach((item) => (item.hidden = false));
        Object.keys(fieldDisplayStatus).forEach(function (key) {
          delete fieldDisplayStatus[key];
        });
        handleEnhanceJS({ buttonCode: 'loaded' });
        // 表单赋值
        formData.value = { ...detailData };
        editSubVxeTableData(detailData);
        // update-end--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
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

      function getSubTableAuthPre(table) {
        return 'online_' + table + ':';
      }

      //跳转至积木报表页面
      function onOpenReportPrint() {
        let url = onlineExtConfigJson.reportPrintUrl;
        let temp: any = formData.value;
        if (temp) {
          let id = temp.id;
          let token = getToken();
          goJmReportViewPage(url, id, token);
        }
      }

      function getSubTableForeignKeyValue(key) {
        let temp = formData.value;
        console.log('getValueIgnoreCase(temp, key)', temp, key, getValueIgnoreCase(temp, key));
        return getValueIgnoreCase(temp, key);
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
      // update-begin--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
      function handleEnhanceJS({ buttonCode }) {
        if (EnhanceJS && EnhanceJS[buttonCode]) {
          EnhanceJS[buttonCode].call(onlineFormDetailContext, onlineFormDetailContext);
        }
      }
      watch(fieldDisplayStatus, (newValue) => {
        Object.entries(newValue).forEach(([key, value]) => {
          if (value == false) {
            const findItem = detailFormSchemas.value.find((item) => item.field === key);
            if (findItem) {
              findItem.hidden = true;
            }
          }
        });
      });
      const context = {
        setFieldsValue: (values) => {
          Object.entries(values).forEach(([key, value]) => {
            detailData[key] = value;
          });
        },
        getFieldsValue: () => {
          return { ...detailData };
        },
        sh: fieldDisplayStatus,
        isUpdate: ref(false),
        isDetail: ref(true),
      };
      resetContext(context);
      // update-end--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能

      return {
        detailFormSchemas,
        formData,
        formSpan,

        //主表
        tableName,
        loading,

        //子表
        hasSubTable,
        subTabInfo,
        subFormHeight,
        subTableHeight,
        refMap,
        onTabChange,
        subReloadKey,

        //一对多子表
        subDataSource,
        getSubTableAuthPre,

        //父组件调用
        show,
        createRootProperties,

        // 扩展配置
        onOpenReportPrint,
        onlineExtConfigJson,
        getSubTableForeignKeyValue,
        showStatus,
        ERP,
        rowNumber,
      };
    },
  };
</script>

<style scoped></style>
