<!--部门选择组件-->
<template>
  <div class="JSelectDepartPost">
    <JSelectBiz @change="handleSelectChange" @handleOpen="handleOpen" :loading="loadingEcho" v-bind="attrs" :isCustomRenderTag="isCustomRenderTag" :rowKey="getBindValue?.rowKey"/>
    <a-form-item>
      <DeptSelectModal @register="regModal" @getSelectResult="setValue" modalTitle="部门岗位选择" v-bind="getBindValue" :multiple="multiple" @close="handleClose" :izShowDepPath="izShowDepPath"/>
    </a-form-item>
  </div>
</template>
<script lang="ts">
  import DeptSelectModal from './modal/DeptSelectModal.vue';
  import JSelectBiz from './base/JSelectBiz.vue';
  import { defineComponent, ref, reactive, watchEffect, watch, provide, unref, toRaw } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { cloneDeep } from 'lodash-es';

  export default defineComponent({
    name: 'JSelectDepartPost',
    components: {
      DeptSelectModal,
      JSelectBiz,
    },
    inheritAttrs: false,
    props: {
      value: propTypes.oneOfType([propTypes.string, propTypes.array]),
      // 是否允许多选，默认 true
      multiple: propTypes.bool.def(true),
      //是否只选择岗位
      izOnlySelectDepartPost: propTypes.bool.def(true),
      // 自定义渲染tag
      isCustomRenderTag: propTypes.bool.def(true),
      //是否显示部门路径（用户部门选择主岗位和兼职岗位需要显示全路径）
      izShowDepPath: propTypes.bool.def(false),
    },
    emits: ['options-change', 'change', 'select', 'update:value'],
    setup(props, { emit, refs }) {
      const emitData = ref<any[]>();
      //注册model
      const [regModal, { openModal }] = useModal();
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
        tempSave = [];
        props.value && initValue();
      });

      watch(
        () => props.value,
        () => {
          initValue();
        }
      );

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
          selectValues.value = value.split(',');
          tempSave = value.split(',');
        } else {
          selectValues.value = value;
          tempSave = cloneDeep(value);
        }
      }

      /**
       * 设置下拉框的值
       */
      function setValue(options, values) {
        selectOptions.value = options;
        selectValues.value = values;
        send(values);
      }
      const getBindValue = Object.assign({}, unref(props), unref(attrs));

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
  .JSelectDepartPost {
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
