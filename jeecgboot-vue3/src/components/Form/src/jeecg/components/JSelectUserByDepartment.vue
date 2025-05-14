<!--用户选择组件-->
<template>
  <div>
    <JSelectBiz @handleOpen="handleOpen" :loading="loadingEcho" v-bind="attrs" @change="handleSelectChange"></JSelectBiz>
    <JSelectUserByDepartmentModal
      v-if="modalShow"
      :selectedUser="selectOptions"
      :modalTitle="modalTitle"
      :rowKey="rowKey"
      :labelKey="labelKey"
      :isRadioSelection="isRadioSelection"
      :params="params"
      @register="regModal"
      @change="setValue"
      @close="() => (modalShow = false)"
      v-bind="attrs"
    ></JSelectUserByDepartmentModal>
  </div>
</template>
<script lang="ts" setup>
  import { ref, reactive, watch, provide } from 'vue';
  import JSelectUserByDepartmentModal from './modal/JSelectUserByDepartmentModal.vue';
  import JSelectBiz from './base/JSelectBiz.vue';
  import { useModal } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { isArray, isString, isObject } from '/@/utils/is';
  import { getTableList as getTableListOrigin } from '/@/api/common/api';
  import { useMessage } from '/@/hooks/web/useMessage';

  defineOptions({ name: 'JSelectUserByDepartment' });
  const props = defineProps({
    value: propTypes.oneOfType([propTypes.string, propTypes.array]),
    modalTitle: {
      type: String,
      default: '部门用户选择',
    },
    rowKey: {
      type: String,
      default: 'username',
    },
    labelKey: {
      type: String,
      default: 'realname',
    },
    //查询参数
    params: {
      type: Object,
      default: () => {},
    },
    isRadioSelection: {
      type: Boolean,
      default: false,
    },
  });
  const emit = defineEmits(['options-change', 'change', 'update:value']);
  const { createMessage } = useMessage();
  //注册model
  const [regModal, { openModal }] = useModal();
  // 是否显示弹窗
  const modalShow = ref(false);
  //下拉框选项值
  const selectOptions: any = ref<SelectValue>([]);
  //下拉框选中值
  let selectValues: any = reactive<object>({
    value: [],
    change: false,
  });
  // 是否正在加载回显数据
  const loadingEcho = ref<boolean>(false);
  //下发 selectOptions,xxxBiz组件接收
  provide('selectOptions', selectOptions);
  //下发 selectValues,xxxBiz组件接收
  provide('selectValues', selectValues);
  //下发 loadingEcho,xxxBiz组件接收
  provide('loadingEcho', loadingEcho);

  const attrs: any = useAttrs();

  // 打开弹窗
  function handleOpen() {
    modalShow.value = true;
    setTimeout(() => {
      openModal(true, {
        isUpdate: false,
      });
    }, 0);
  }
  const handleSelectChange = (data) => {
    selectOptions.value = selectOptions.value.filter((item) => data.includes(item[props.rowKey]));
    setValue(selectOptions.value);
  };
  // 设置下拉框的值
  const setValue = (data) => {
    selectOptions.value = data.map((item) => {
      return {
        ...item,
        label: item[props.labelKey],
        value: item[props.rowKey],
      };
    });
    selectValues.value = data.map((item) => item[props.rowKey]);
    // 更新value
    emit('update:value', selectValues.value);
    // 触发change事件（不转是因为basicForm提交时会自动将字符串转化为数组）
    emit('change', selectValues.value);
    // 触发options-change事件
    emit('options-change', selectOptions.value);
  };
  // 翻译
  const transform = () => {
    let value = props.value;
    let len;
    if (isArray(value) || isString(value)) {
      if (isArray(value)) {
        len = value.length;
        value = value.join(',');
      } else {
        len = value.split(',').length;
      }
      value = value.trim();
      if (value) {
        // 如果value的值在selectedUser中存在，则不请求翻译
        let isNotRequestTransform = false;
        isNotRequestTransform = value.split(',').every((value) => !!selectOptions.value.find((item) => item[props.rowKey] === value));
        if (isNotRequestTransform) {
          selectValues.value = value.split(',')
          return;
        }
        const params = { isMultiTranslate: true, pageSize: len, [props.rowKey]: value };
        if (isObject(attrs.params)) {
          Object.assign(params, attrs.params);
        }
        getTableListOrigin(params).then((result: any) => {
          const records = result.records ?? [];
          selectValues.value = records.map((item) => item[props.rowKey]);
          selectOptions.value = records.map((item) => {
            return {
              ...item,
              label: item[props.labelKey],
              value: item[props.rowKey],
            };
          });
        });
      }
    } else {
      selectValues.value = [];
    }
  };
  // 监听value变化
  watch(
    () => props.value,
    () => {
      transform();
    },
    { deep: true, immediate: true }
  );
</script>
<style lang="less" scoped>
  .j-select-row {
    @width: 82px;
    .left {
      width: calc(100% - @width - 8px);
    }
    .right {
      width: @width;
    }
    .full {
      width: 100%;
    }
    :deep(.ant-select-search__field) {
      display: none !important;
    }
  }
</style>
