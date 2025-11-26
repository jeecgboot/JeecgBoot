<!--部门选择组件-->
<template>
  <div class="JSelectDept">
    <JSelectBiz  @change="handleSelectChange" @handleOpen="handleOpen" :loading="loadingEcho" v-bind="attrs" :isCustomRenderTag="isCustomRenderTag" :rowKey="getBindValue?.rowKey"/>
    <a-form-item>
      <DeptSelectModal @register="regModal" @getSelectResult="setValue" v-bind="getBindValue" :multiple="multiple" @close="handleClose"/>
    </a-form-item>
  </div>
</template>
<script lang="ts">
  import DeptSelectModal from './modal/DeptSelectModal.vue';
  import JSelectBiz from './base/JSelectBiz.vue';
  import { defineComponent, ref, reactive, watchEffect, watch, provide, unref, toRaw } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { cloneDeep } from 'lodash-es';

  export default defineComponent({
    name: 'JSelectDept',
    components: {
      DeptSelectModal,
      JSelectBiz,
    },
    inheritAttrs: false,
    props: {
      value: propTypes.oneOfType([propTypes.string, propTypes.array]),
      // 是否允许多选，默认 true
      multiple: propTypes.bool.def(true),
      // 自定义渲染tag
      isCustomRenderTag: propTypes.bool.def(true),
    },
    emits: ['options-change', 'change', 'select', 'update:value'],
    setup(props, { emit, refs }) {
      const emitData = ref<any[]>();
      //注册model
      const [regModal, { openModal }] = useModal();
      //表单值
      // const [state] = useRuleFormItem(props, 'value', 'change', emitData);
      //下拉框选项值
      const selectOptions = ref<SelectValue>([]);
      //下拉框选中值
      let selectValues = reactive<Recordable>({
        value: [],
      });
      let tempSave: any = [];

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
        // 代码逻辑说明: [TV360X-840]用户授权，没有选择，点取消，也会回显一个选过的用户
        tempSave = [];
        props.value && initValue();
      });

      // 代码逻辑说明: 为了解决弹窗form初始化赋值问题 ---
      watch(
        () => props.value,
        () => {
          initValue();
        }
      );
      /**
       * 监听selectValues变化
       */
      // watch(selectValues, () => {
      //   if (selectValues) {
      //     state.value = selectValues.value;
      //   }
      // });
      /**
       * 监听selectOptions变化
       */
      watch(selectOptions, () => {
        if (selectOptions) {
          emit('select', toRaw(unref(selectOptions)), toRaw(unref(selectValues)));
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
        if (value && typeof value === 'string') {
          // state.value = value.split(',');
          selectValues.value = value.split(',');
          tempSave = value.split(',');
        } else {
          // 【VUEN-857】兼容数组（行编辑的用法问题）
          selectValues.value = value;
          tempSave = cloneDeep(value);
        }
      }

      /**
       * 设置下拉框的值
       */
      function setValue(options, values) {
        selectOptions.value = options;
        //emitData.value = values.join(",");
        // state.value = values;
        selectValues.value = values;
        send(values);
      }
      const getBindValue = Object.assign({}, unref(props), unref(attrs));

       // 代码逻辑说明: 【TV360X-414】部门设置了默认值，查询重置变成空了(同步JSelectUser组件改法)
      const handleClose = () => {
        if (tempSave.length) {
          selectValues.value = cloneDeep(tempSave);
        } else {
          send(tempSave);
        }
      };
      const handleSelectChange = (values) => {
        tempSave = cloneDeep(values);
        send(tempSave);
      };
      const send = (values) => {
        let result = typeof props.value == 'string' ? values.join(',') : values;
        emit('update:value', result);
        emit('change', result);
        // 代码逻辑说明: 【TV360X-1648】用户编辑界面“所属部门”与“负责部门”联动出错（同步之前丢的代码）
        if (!values || values.length == 0) {
          emit('select', null, null);
        }
      };
      
      return {
        // state,
        attrs,
        selectOptions,
        selectValues,
        loadingEcho,
        getBindValue,
        tag,
        regModal,
        setValue,
        handleOpen,
        handleClose,
        handleSelectChange,
      };
    },
  });
</script>
<style lang="less" scoped>
  // 代码逻辑说明: 【QQYUN-9260】必填模式下会影响到弹窗内antd组件的样式
  .JSelectDept {
    > .ant-form-item {
      display: none;
    }
  }
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
