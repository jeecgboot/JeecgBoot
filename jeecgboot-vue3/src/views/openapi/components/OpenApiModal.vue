<template>
  <BasicModal :bodyStyle="{ padding: '20px' }" v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" width="80%" @ok="handleSubmit">
    <a-row :gutter="24">
      <a-col :span="10">
        <BasicForm @register="registerForm" ref="formRef" name="OpenApiForm">
          <!-- 访问清单：输入 + 标签预览 + 整理按钮 -->
          <template #allowedListSlot="{ model, field }">
            <div style="width:100%">
              <a-textarea
                v-model:value="model[field]"
                :rows="6"
                placeholder="支持IP、CIDR、域名；支持10.2.3.*与10.2.3.[1-234]，每行一个或逗号分隔"
                allow-clear
              />
              <div class="allowed-tags">
                <span v-for="(item, idx) in splitAllowedList(model[field])" :key="idx">
                  <a-tag v-if="isValidAllowedItem(item)" class="allowed-tag">{{ item }}</a-tag>
                </span>
              </div>
              <div style="margin-top:8px; display:flex; justify-content:flex-end;">
                <a-button size="small" type="primary" @click="organizeAllowedList(model, field)">整理</a-button>
              </div>
            </div>
          </template>
        </BasicForm>
      </a-col>
      <a-col :span="14">
        <a-row :gutter="24">
          <a-col :span="24" style="margin-top: -0.6em">
            <JVxeTable
              keep-source
              ref="openApiHeader"
              :loading="openApiHeaderTable.loading"
              :columns="openApiHeaderTable.columns"
              :dataSource="openApiHeaderTable.dataSource"
              :height="240"
              :disabled="formDisabled"
              :rowNumber="true"
              :rowSelection="true"
              :toolbar="true"
              size="mini"
            />
          </a-col>
          <a-col :span="24">
            <JVxeTable
              keep-source
              ref="openApiParam"
              :loading="openApiParamTable.loading"
              :columns="openApiParamTable.columns"
              :dataSource="openApiParamTable.dataSource"
              :height="240"
              :disabled="formDisabled"
              :rowNumber="true"
              :rowSelection="true"
              :toolbar="true"
              size="mini"
            />
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { JVxeTable } from '/@/components/jeecg/JVxeTable';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods.ts';
  import { formSchema, openApiHeaderJVxeColumns, openApiParamJVxeColumns } from '../OpenApi.data';
  import { saveOrUpdate, queryOpenApiHeader, queryOpenApiParam, getGenPath } from '../OpenApi.api';
  import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils';
  import { useMessage } from "@/hooks/web/useMessage";

  // Emits声明
  const $message = useMessage();
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  const formDisabled = ref(false);
  const refKeys = ref(['openApiHeader', 'openApiParam']);
  const activeKey = ref('openApiHeader');
  const openApiHeader = ref();
  const openApiParam = ref();
  const tableRefs = { openApiHeader, openApiParam };
  const openApiHeaderTable = reactive({
    loading: false,
    dataSource: [],
    columns: openApiHeaderJVxeColumns,
  });
  const openApiParamTable = reactive({
    loading: false,
    dataSource: [],
    columns: openApiParamJVxeColumns,
  });
  //表单配置
  const [registerForm, { setProps, resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: formSchema,
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    wrapperCol: { span: 24 },
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await reset();
    setModalProps({ confirmLoading: false, showCancelBtn: data?.showFooter, showOkBtn: data?.showFooter });
    isUpdate.value = !!data?.isUpdate;
    formDisabled.value = !data?.showFooter;
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
      // 请求后端接口获取数据
      //  requestSubTableData(queryOpenApiHeader, {id:data?.record?.id}, openApiHeaderTable)
      //  requestSubTableData(queryOpenApiParam, {id:data?.record?.id}, openApiParamTable)
      openApiHeaderTable.dataSource = !!data.record.headersJson?JSON.parse(data.record.headersJson):[];
      openApiParamTable.dataSource = !!data.record.paramsJson?JSON.parse(data.record.paramsJson):[];
    } else {
      //  /openapi/genpath
      const requestUrlObj = await getGenPath({});
      await setFieldsValue({
        requestUrl: requestUrlObj.result

      });
    }
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !data?.showFooter });
  });
  //方法配置
  const [handleChangeTabs, handleSubmit, requestSubTableData, formRef] = useJvxeMethod(
    requestAddOrEdit,
    classifyIntoFormData,
    tableRefs,
    activeKey,
    refKeys
  );

  // 校验规则复用：IPv4段、IP、CIDR、域名、通配符、范围
  const ipv4Seg = '(?:25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)';
  const ipRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}${ipv4Seg}$`);
  const cidrRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}${ipv4Seg}\\/(?:[0-9]|[1-2][0-9]|3[0-2])$`);
  const domainRegex = /^([a-zA-Z0-9]+(-[a-zA-Z0-9]+)*\.)+[a-zA-Z]{2,}$/;
  const wildcardLastOctetRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}\\*$`);
  const rangeLastOctetRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}\\[(\\d{1,3})-(\\d{1,3})\\]$`);

  function isValidAllowedItem(raw: string): boolean {
    const item = (raw || '').trim();
    if (!item) return false;
    if (ipRegex.test(item) || cidrRegex.test(item) || domainRegex.test(item)) return true;
    if (wildcardLastOctetRegex.test(item)) return true;
    const m = item.match(rangeLastOctetRegex);
    if (m) {
      const start = Number(m[1]);
      const end = Number(m[2]);
      if (Number.isInteger(start) && Number.isInteger(end) && start >= 0 && end >= start && end <= 255) return true;
    }
    return false;
  }

  function splitAllowedList(val?: string): string[] {
    if (!val) return [];
    return val.split(/[,\n]/).map(s => s.trim()).filter(Boolean);
  }

  function organizeAllowedList(model: Record<string, any>, field: string) {
    const items = splitAllowedList(model[field]);

    // 去重（域名大小写不敏感；其他保持原样）
    const seen = new Set<string>();
    const normalizedPair: [string, string][] = items.map((x) => {
      const isDomain = domainRegex.test(x);
      return [isDomain ? x.toLowerCase() : x, x];
    });
    const deduped: string[] = [];
    for (const [key, original] of normalizedPair) {
      if (!seen.has(key)) {
        seen.add(key);
        deduped.push(original);
      }
    }

    // 类型排序：IP < CIDR < 通配符 < 范围 < 域名，然后字典序
    function typeRank(s: string): number {
      if (ipRegex.test(s)) return 1;
      if (cidrRegex.test(s)) return 2;
      if (wildcardLastOctetRegex.test(s)) return 3;
      if (rangeLastOctetRegex.test(s)) return 4;
      if (domainRegex.test(s)) return 5;
      return 9;
    }
    deduped.sort((a, b) => {
      const ra = typeRank(a), rb = typeRank(b);
      if (ra !== rb) return ra - rb;
      return a.localeCompare(b);
    });

    model[field] = deduped.join('\n');
  }

  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增' : !unref(formDisabled) ? '编辑' : '详情'));

  async function reset() {
    await resetFields();
    activeKey.value = 'openApiHeader';
    openApiHeaderTable.dataSource = [];
    openApiParamTable.dataSource = [];
  }
  function classifyIntoFormData(allValues) {
    let main = Object.assign({}, allValues.formValue);
    return {
      ...main, // 展开
      headersJson: allValues.tablesValue[0].tableData,
      paramsJson: allValues.tablesValue[1].tableData,
    };
  }
  //表单提交事件
  async function requestAddOrEdit(values) {
    let headersJson = !!values.headersJson?JSON.stringify(values.headersJson):null;
    let paramsJson = !!values.paramsJson?JSON.stringify(values.paramsJson):null;
    try {
      if (!!values.body){
        try {
          if (typeof JSON.parse(values.body)!='object'){
            $message.createMessage.error("JSON格式化错误,请检查输入数据");
            return;
          }
        } catch (e) {
          $message.createMessage.error("JSON格式化错误,请检查输入数据");
          return;
        }
      }
      // 处理访问清单，将逗号分隔转换为换行分隔
      if (values.allowedList) {
        values.allowedList = values.allowedList
          .split(/[,\s]+/)
          .filter(item => item.trim())
          .join('\n');
      }
      setModalProps({ confirmLoading: true });
      values.headersJson = headersJson
      values.paramsJson = paramsJson
      //提交表单
      await saveOrUpdate(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less" scoped>
  /** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-calendar-picker) {
    width: 100%;
  }

  .allowed-tags {
    margin-top: 8px;
    min-height: 28px;
  }
  .allowed-tag {
    background-color: #f6ffed; /* 绿色浅底 */
    color: #389e0d;
    border: 1px solid #b7eb8f;
    border-radius: 14px;
    padding: 2px 10px;
    margin: 2px 6px 6px 0;
    font-size: 12px;
    line-height: 20px;
  }
</style>
