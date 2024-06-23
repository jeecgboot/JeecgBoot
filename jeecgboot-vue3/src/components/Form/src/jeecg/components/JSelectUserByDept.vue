<!--用户选择组件-->
<template>
  <div>
    <JSelectBiz @change="handleChange" @handleOpen="handleOpen" :loading="loadingEcho" v-bind="attrs"></JSelectBiz>
    <UserSelectByDepModal :rowKey="rowKey" @register="regModal" @getSelectResult="setValue" v-bind="getBindValue"></UserSelectByDepModal>
  </div>
</template>
<script lang="ts">
  import { unref } from 'vue';
  import UserSelectByDepModal from './modal/UserSelectByDepModal.vue';
  import JSelectBiz from './base/JSelectBiz.vue';
  import { defineComponent, ref, reactive, watchEffect, watch, provide } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { SelectValue } from 'ant-design-vue/es/select';
  export default defineComponent({
    name: 'JSelectUserByDept',
    components: {
      UserSelectByDepModal,
      JSelectBiz,
    },
    inheritAttrs: false,
    props: {
      value: propTypes.oneOfType([propTypes.string, propTypes.array]),
      rowKey: {
        type: String,
        default: 'username',
      },
      labelKey: {
        type: String,
        default: 'realname',
      },
    },
    emits: ['options-change', 'change', 'update:value'],
    setup(props, { emit, refs }) {
      const emitData = ref<any[]>();
      //注册model
      const [regModal, { openModal }] = useModal();
      //表单值
      const [state] = useRuleFormItem(props, 'value', 'change', emitData);
      //下拉框选项值
      const selectOptions = ref<SelectValue>([]);
      //下拉框选中值
      let selectValues = reactive<object>({
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

      const tag = ref(false);
      const attrs = useAttrs();

      /**
       * 监听组件值
       */
      watchEffect(() => {
        initValue();
      });

      /**
       * 监听selectValues变化
       */
      watch(selectValues, () => {
        if (selectValues) {
          state.value = selectValues.value;
        }
      });

      /**
       * 打卡弹出框
       */
      function handleOpen() {
        tag.value = true;
        openModal(true, {
          isUpdate: false,
        });
      }

      /**
       * 将字符串值转化为数组
       */
      function initValue() {
        let value = props.value ? props.value : [];
        if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
          state.value = value.split(',');
          selectValues.value = value.split(',');
        } else {
          selectValues.value = value;
        }
      }

      /**
       * 设置下拉框的值
       */
      function setValue(options, values) {
        selectOptions.value = options;
        //emitData.value = values.join(",");
        state.value = values;
        selectValues.value = values;
        emit('update:value', values);
        emit('options-change', options);
      }

      function handleChange(values) {
        emit('update:value', values);
      }
      const getBindValue = Object.assign({}, unref(props), unref(attrs));
      return {
        state,
        attrs,
        selectOptions,
        getBindValue,
        selectValues,
        loadingEcho,
        tag,
        regModal,
        setValue,
        handleOpen,
        handleChange,
      };
    },
  });
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
