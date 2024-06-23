<template>
    <a-cascader v-bind="getProps" class="pca-select" @change="handleChange" />
</template>

<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { regionData, getRealCode } from '/@/components/Form/src/utils/areaDataUtil';
  import { JVxeComponent } from '/@/components/jeecg/JVxeTable/types';
  import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';
  import { dispatchEvent } from '/@/components/jeecg/JVxeTable/utils';

  export default defineComponent({
    name: 'JVxePcaCell',
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const { innerValue, cellProps, handleChangeCommon } = useJVxeComponent(props);

      const selectedValue = computed(() => {
        let val: any = innerValue.value;
        if (!val) {
          return []
        }
        let arr = getRealCode(val, 3);
        return arr;
      });

      const getProps = computed(() => {
        return {
          ...cellProps.value,
          options: regionData,
          showOverflow: false,
          value: selectedValue.value,
        };
      });
      
      function handleChange(arr) {
        let str = '';
        if(arr && arr.length==3){
          str = arr[2];
        }
        handleChangeCommon(str);
      }

      return {
        handleChange,
        selectedValue,
        getProps
      };
    },
    // 【组件增强】注释详见：JVxeComponent.Enhanced
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
    .pca-select{
        .ant-select-selection-placeholder{
            color: #bfbfbf !important;
        }
    }
</style>
