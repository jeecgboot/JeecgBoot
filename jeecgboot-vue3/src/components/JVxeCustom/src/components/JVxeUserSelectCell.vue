<template>
  <div :class="[prefixCls]">
    <JSelectUser v-bind="getProps" @change="handleChange" />
  </div>
</template>

<script lang="ts">
  import { computed, defineComponent } from 'vue';
  //import { JSelectUser } from '/@/components/Form';
  import JSelectUser from '/@/components/Form/src/jeecg/components/JSelectUser.vue';
  import { JVxeComponent } from '/@/components/jeecg/JVxeTable/types';
  import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';
  import { dispatchEvent } from '/@/components/jeecg/JVxeTable/utils';
  import { isArray, isEmpty, isString } from '/@/utils/is';

  export default defineComponent({
    name: 'JVxeUserSelectCell',
    components: { JSelectUser },
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const { innerValue, cellProps, handleChangeCommon, useCellDesign } = useJVxeComponent(props);
      const { prefixCls } = useCellDesign('user-select');

      const selectedValue = computed(() => {
        let val: any = innerValue.value;
        if (val == null) {
          return val;
        }
        if (isEmpty(val)) {
          return [];
        }
        if (isArray(val)) {
          return val;
        }
        if (isString(val)) {
          // @ts-ignore
          return val.split(',');
        }
        return [val];
      });
      // @ts-ignore
      const multiple = computed(() => cellProps.value.multi != false);

      const getProps = computed(() => {
        return {
          ...cellProps.value,
          value: selectedValue.value,
          showButton: false,
          // 不允许搜索
          showSearch: false,
          // 设置最大的显示个数
          maxTagCount: 1,
          // 显示提示重写，去掉省略号
          maxTagPlaceholder: ({ length }) => '+' + length,
        };
      });

      function handleChange(values) {
        handleChangeCommon(values.join(','));
      }

      return {
        prefixCls,
        selectedValue,
        multiple,
        cellProps,
        getProps,
        handleChange,
      };
    },
    enhanced: {
      switches: {
        visible: true,
      },
      translate: {
        enabled: false,
      },
      aopEvents: {
        editActived({ $event }) {
          dispatchEvent({
            $event,
            props: this.props,
            className: '.ant-select .ant-select-selection-search-input',
            isClick: true,
          });
        },
      },
    } as JVxeComponent.EnhancedPartial,
  });
</script>

<style lang="less">
  // noinspection LessUnresolvedVariable
  @prefix-cls: ~'@{namespace}-vxe-cell-user-select';

  .@{prefix-cls} {
    // 限制tag最大长度为100px，防止选中文字过多的选项时换行
    .ant-select .ant-select-selection-overflow-item {
      max-width: 80px;
    }
  }
</style>
