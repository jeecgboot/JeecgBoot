<template>
  <div :class="prefixCls">
    <div v-show="!isEdit" :class="{ [`${prefixCls}__normal`]: true, 'ellipsis-cell': column.ellipsis }" @click="handleEdit">
      <div class="cell-content" :title="column.ellipsis ? getValues ?? '' : ''">
        <!-- update-begin--author:liaozhiyang---date:20240731---for：【issues/6957】editableCell组件值长度为0，无法编辑 -->
        <!-- update-begin--author:liaozhiyang---date:20240709---for：【issues/6851】editableCell组件值为0时不展示 -->
        {{ typeof getValues === 'string' && getValues.length === 0 ? '&nbsp;' : getValues ?? '&nbsp;' }}
        <!-- update-end--author:liaozhiyang---date:20240709---for：【issues/6851】editableCell组件值为0时不展示 -->
        <!-- update-end--author:liaozhiyang---date:20240731---for：【issues/6957】editableCell组件值长度为0，无法编辑 -->
      </div>
      <FormOutlined :class="`${prefixCls}__normal-icon`" v-if="!column.editRow" />
    </div>

    <a-spin v-if="isEdit" :spinning="spinning">
      <div :class="`${prefixCls}__wrapper`" v-click-outside="onClickOutside">
        <CellComponent
          v-bind="getComponentProps"
          :component="getComponent"
          :style="getWrapperStyle"
          :popoverVisible="getRuleVisible"
          :rule="getRule"
          :ruleMessage="ruleMessage"
          :class="getWrapperClass"
          ref="elRef"
          @change="handleChange"
          @options-change="handleOptionsChange"
          @pressEnter="handleEnter"
        />
        <div :class="`${prefixCls}__action`" v-if="!getRowEditable">
          <CheckOutlined :class="[`${prefixCls}__icon`, 'mx-2']" @click="handleSubmitClick" />
          <CloseOutlined :class="`${prefixCls}__icon `" @click="handleCancel" />
        </div>
      </div>
    </a-spin>
  </div>
</template>
<script lang="ts">
  import type { CSSProperties, PropType } from 'vue';
  import { computed, defineComponent, nextTick, ref, toRaw, unref, watchEffect } from 'vue';
  import type { BasicColumn } from '../../types/table';
  import type { EditRecordRow } from './index';
  import { CheckOutlined, CloseOutlined, FormOutlined } from '@ant-design/icons-vue';
  import { CellComponent } from './CellComponent';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useTableContext } from '../../hooks/useTableContext';

  import clickOutside from '/@/directives/clickOutside';

  import { propTypes } from '/@/utils/propTypes';
  import { isArray, isBoolean, isFunction, isNumber, isString } from '/@/utils/is';
  import { createPlaceholderMessage } from './helper';
  import { omit, pick, set } from 'lodash-es';
  import { treeToList } from '/@/utils/helper/treeHelper';
  import { Spin } from 'ant-design-vue';

  export default defineComponent({
    name: 'EditableCell',
    components: { FormOutlined, CloseOutlined, CheckOutlined, CellComponent, ASpin: Spin },
    directives: {
      clickOutside,
    },
    props: {
      value: {
        type: [String, Number, Boolean, Object] as PropType<string | number | boolean | Recordable>,
        default: '',
      },
      record: {
        type: Object as PropType<EditRecordRow>,
      },
      column: {
        type: Object as PropType<BasicColumn>,
        default: () => ({}),
      },
      index: propTypes.number,
    },
    setup(props) {
      const table = useTableContext();
      const isEdit = ref(false);
      const elRef = ref();
      const ruleVisible = ref(false);
      const ruleMessage = ref('');
      const optionsRef = ref<LabelValueOptions>([]);
      const currentValueRef = ref<any>(props.value);
      const defaultValueRef = ref<any>(props.value);
      const spinning = ref<boolean>(false);

      const { prefixCls } = useDesign('editable-cell');

      const getComponent = computed(() => props.column?.editComponent || 'Input');
      const getRule = computed(() => props.column?.editRule);

      const getRuleVisible = computed(() => {
        return unref(ruleMessage) && unref(ruleVisible);
      });

      const getIsCheckComp = computed(() => {
        const component = unref(getComponent);
        return ['Checkbox', 'Switch'].includes(component);
      });

      const getComponentProps = computed(() => {
        const compProps = props.column?.editComponentProps ?? {};
        const component = unref(getComponent);
        const apiSelectProps: Recordable = {};
        if (component === 'ApiSelect') {
          apiSelectProps.cache = true;
        }

        const isCheckValue = unref(getIsCheckComp);

        const valueField = isCheckValue ? 'checked' : 'value';
        const val = unref(currentValueRef);

        const value = isCheckValue ? (isNumber(val) && isBoolean(val) ? val : !!val) : val;

        return {
          size: 'small',
          getPopupContainer: () => unref(table?.wrapRef.value) ?? document.body,
          getCalendarContainer: () => unref(table?.wrapRef.value) ?? document.body,
          placeholder: createPlaceholderMessage(unref(getComponent)),
          ...apiSelectProps,
          ...omit(compProps, 'onChange'),
          [valueField]: value,
        };
      });

      const getValues = computed(() => {
        const { editComponentProps, editValueMap } = props.column;

        const value = unref(currentValueRef);

        if (editValueMap && isFunction(editValueMap)) {
          return editValueMap(value);
        }

        const component = unref(getComponent);
        if (!component.includes('Select')) {
          return value;
        }

        const options: LabelValueOptions = editComponentProps?.options ?? (unref(optionsRef) || []);
        const option = options.find((item) => `${item.value}` === `${value}`);

        return option?.label ?? value;
      });

      const getWrapperStyle = computed((): CSSProperties => {
        if (unref(getIsCheckComp) || unref(getRowEditable)) {
          return {};
        }
        return {
          width: 'calc(100% - 48px)',
        };
      });

      const getWrapperClass = computed(() => {
        const { align = 'center' } = props.column;
        return `edit-cell-align-${align}`;
      });

      const getRowEditable = computed(() => {
        const { editable } = props.record || {};
        return !!editable;
      });

      watchEffect(() => {
        defaultValueRef.value = props.value;
        currentValueRef.value = props.value;
      });

      watchEffect(() => {
        const { editable } = props.column;
        if (isBoolean(editable) || isBoolean(unref(getRowEditable))) {
          isEdit.value = !!editable || unref(getRowEditable);
        }
      });

      function handleEdit() {
        if (unref(getRowEditable) || unref(props.column?.editRow)) return;
        ruleMessage.value = '';
        isEdit.value = true;
        nextTick(() => {
          const el = unref(elRef);
          el?.focus?.();
        });
      }

      async function handleChange(e: any) {
        const component = unref(getComponent);
        if (!e) {
          currentValueRef.value = e;
        } else if (e?.target && Reflect.has(e.target, 'value')) {
          currentValueRef.value = (e as ChangeEvent).target.value;
        } else if (component === 'Checkbox') {
          currentValueRef.value = (e as ChangeEvent).target.checked;
        } else if (isString(e) || isBoolean(e) || isNumber(e) || isArray(e)) {
          currentValueRef.value = e;
        }
        const onChange = props.column?.editComponentProps?.onChange;
        if (onChange && isFunction(onChange)) onChange(...arguments);

        table.emit?.('edit-change', {
          column: props.column,
          value: unref(currentValueRef),
          record: toRaw(props.record),
        });
        handleSubmiRule();
      }

      async function handleSubmiRule() {
        const { column, record } = props;
        const { editRule } = column;
        const currentValue = unref(currentValueRef);

        if (editRule) {
          if (isBoolean(editRule) && !currentValue && !isNumber(currentValue)) {
            ruleVisible.value = true;
            const component = unref(getComponent);
            ruleMessage.value = createPlaceholderMessage(component);
            return false;
          }
          if (isFunction(editRule)) {
            const res = await editRule(currentValue, record as Recordable);
            if (!!res) {
              ruleMessage.value = res;
              ruleVisible.value = true;
              return false;
            } else {
              ruleMessage.value = '';
              return true;
            }
          }
        }
        ruleMessage.value = '';
        return true;
      }

      async function handleSubmit(needEmit = true, valid = true) {
        if (valid) {
          const isPass = await handleSubmiRule();
          if (!isPass) return false;
        }

        const { column, index, record } = props;
        if (!record) return false;
        const { key, dataIndex } = column;
        const value = unref(currentValueRef);
        if (!key || !dataIndex) return;

        const dataKey = (dataIndex || key) as string;

        if (!record.editable) {
          const { getBindValues } = table;

          const { beforeEditSubmit, columns } = unref(getBindValues);

          if (beforeEditSubmit && isFunction(beforeEditSubmit)) {
            spinning.value = true;
            const keys: string[] = columns.map((_column) => _column.dataIndex).filter((field) => !!field) as string[];
            let result: any = true;
            try {
              result = await beforeEditSubmit({
                record: pick(record, keys),
                index,
                key,
                value,
              });
            } catch (e) {
              result = false;
            } finally {
              spinning.value = false;
            }
            if (result === false) {
              return;
            }
          }
        }

        set(record, dataKey, value);
        //const record = await table.updateTableData(index, dataKey, value);
        needEmit && table.emit?.('edit-end', { record, index, key, value });
        isEdit.value = false;
      }

      async function handleEnter() {
        if (props.column?.editRow) {
          return;
        }
        handleSubmit();
      }

      function handleSubmitClick() {
        handleSubmit();
      }

      function handleCancel() {
        isEdit.value = false;
        currentValueRef.value = defaultValueRef.value;
        const { column, index, record } = props;
        const { key, dataIndex } = column;
        table.emit?.('edit-cancel', {
          record,
          index,
          key: dataIndex || key,
          value: unref(currentValueRef),
        });
      }

      function onClickOutside() {
        if (props.column?.editable || unref(getRowEditable)) {
          return;
        }
        const component = unref(getComponent);

        if (component.includes('Input')) {
          handleCancel();
        }
      }

      // only ApiSelect or TreeSelect
      function handleOptionsChange(options: LabelValueOptions) {
        const { replaceFields } = props.column?.editComponentProps ?? {};
        const component = unref(getComponent);
        if (component === 'ApiTreeSelect') {
          const { title = 'title', value = 'value', children = 'children' } = replaceFields || {};
          let listOptions: Recordable[] = treeToList(options, { children });
          listOptions = listOptions.map((item) => {
            return {
              label: item[title],
              value: item[value],
            };
          });
          optionsRef.value = listOptions as LabelValueOptions;
        } else {
          optionsRef.value = options;
        }
      }

      function initCbs(cbs: 'submitCbs' | 'validCbs' | 'cancelCbs', handle: Fn) {
        if (props.record) {
          /* eslint-disable  */
          // update-begin--author:liaozhiyang---date:20240424---for：【issues/1165】解决canResize为true时第一行校验不过
          const { dataIndex, key } = props.column;
          const field: any = dataIndex || key;
          if (isArray(props.record[cbs])) {
            const findItem = props.record[cbs]?.find((item) => item[field]);
            if (findItem) {
              findItem[field] = handle;
            } else {
              props.record[cbs]?.push({ [field]: handle });
            }
          } else {
            props.record[cbs] = [{ [field]: handle }];
          }
          // update-end--author:liaozhiyang---date:20240424---for：【issues/1165】解决canResize为true时第一行校验不过
        }
      }

      if (props.record) {
        initCbs('submitCbs', handleSubmit);
        initCbs('validCbs', handleSubmiRule);
        initCbs('cancelCbs', handleCancel);

        if (props.column.dataIndex) {
          if (!props.record.editValueRefs) props.record.editValueRefs = {};
          props.record.editValueRefs[props.column.dataIndex] = currentValueRef;
        }
        /* eslint-disable  */
        props.record.onCancelEdit = () => {
          // update-begin--author:liaozhiyang---date:20240424---for：【issues/1165】解决canResize为true时第一行校验不过
          isArray(props.record?.cancelCbs) &&
            props.record?.cancelCbs.forEach((item) => {
              const [fn] = Object.values(item);
              fn();
            });
           // update-end--author:liaozhiyang---date:20240424---for：【issues/1165】解决canResize为true时第一行校验不过
        };
        /* eslint-disable */
        props.record.onSubmitEdit = async () => {
          if (isArray(props.record?.submitCbs)) {
            if (!props.record?.onValid?.()) return;
            const submitFns = props.record?.submitCbs || [];
            // update-begin--author:liaozhiyang---date:20240424---for：【issues/1165】解决canResize为true时第一行校验不过
            submitFns.forEach((item) => {
              const [fn] = Object.values(item);
              fn(false, false);
            });
            // update-end--author:liaozhiyang---date:20240424---for：【issues/1165】解决canResize为true时第一行校验不过
            table.emit?.('edit-row-end');
            return true;
          }
        };
      }

      return {
        isEdit,
        prefixCls,
        handleEdit,
        currentValueRef,
        handleSubmit,
        handleChange,
        handleCancel,
        elRef,
        getComponent,
        getRule,
        onClickOutside,
        ruleMessage,
        getRuleVisible,
        getComponentProps,
        handleOptionsChange,
        getWrapperStyle,
        getWrapperClass,
        getRowEditable,
        getValues,
        handleEnter,
        handleSubmitClick,
        spinning,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-editable-cell';

  .edit-cell-align-left {
    text-align: left;

    input:not(.ant-calendar-picker-input, .ant-time-picker-input) {
      text-align: left;
    }
  }

  .edit-cell-align-center {
    text-align: center;

    input:not(.ant-calendar-picker-input, .ant-time-picker-input) {
      text-align: center;
    }
  }

  .edit-cell-align-right {
    text-align: right;

    input:not(.ant-calendar-picker-input, .ant-time-picker-input) {
      text-align: right;
    }
  }

  .edit-cell-rule-popover {
    .ant-popover-inner-content {
      padding: 4px 8px;
      color: @error-color;
      // border: 1px solid @error-color;
      border-radius: 2px;
    }
  }
  .@{prefix-cls} {
    position: relative;

    &__wrapper {
      display: flex;
      align-items: center;
      justify-content: center;

      > .ant-select {
        min-width: calc(100% - 50px);
      }
    }

    &__icon {
      &:hover {
        transform: scale(1.2);

        svg {
          color: @primary-color;
        }
      }
    }

    .ellipsis-cell {
      .cell-content {
        overflow-wrap: break-word;
        word-break: break-word;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }

    &__normal {
      &-icon {
        position: absolute;
        top: 4px;
        right: 0;
        display: none;
        width: 20px;
        cursor: pointer;
      }
    }

    &:hover {
      .@{prefix-cls}__normal-icon {
        display: inline-block;
      }
    }
  }
</style>
