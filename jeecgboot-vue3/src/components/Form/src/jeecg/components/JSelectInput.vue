<template>
  <a-select v-bind="bindProps" @change="onChange" @search="onSearch" />
</template>

<script lang="ts">
  import { propTypes } from '/@/utils/propTypes';
  import { defineComponent, ref, watch, computed } from 'vue';

  // 可以输入的下拉框（此组件暂时没有人用）
  export default defineComponent({
    name: 'JSelectInput',
    props: {
      options: propTypes.array.def(() => []),
    },
    emits: ['change', 'update:value'],
    setup(props, { emit, attrs }) {
      // 内部 options 选项
      const options = ref<any[]>([]);
      // 监听外部 options 变化，并覆盖内部 options
      watch(
        () => props.options,
        () => {
          options.value = [...props.options];
        },
        { deep: true, immediate: true }
      );
      // 合并 props 和 attrs
      const bindProps: any = computed(() =>
        Object.assign(
          {
            showSearch: true,
          },
          props,
          attrs,
          {
            options: options.value,
          }
        )
      );

      function onChange(...args: any[]) {
        deleteSearchAdd(args[0]);
        emit('change', ...args);
        emit('update:value', args[0]);
      }

      function onSearch(value) {
        // 是否找到了对应的项，找不到则添加这一项
        let foundIt =
          options.value.findIndex((option) => {
            return option.value.toString() === value.toString();
          }) !== -1;
        // !!value ：不添加空值
        if (!foundIt && !!value) {
          deleteSearchAdd(value);
          // searchAdd 是否是通过搜索添加的
          options.value.push({ value: value, searchAdd: true });
          //onChange(value,{ value })
        } else if (foundIt) {
          onChange(value);
        }
      }

      // 删除无用的因搜索（用户输入）而创建的项
      function deleteSearchAdd(value = '') {
        let indexes: any[] = [];
        options.value.forEach((option, index) => {
          if (option.searchAdd) {
            if ((option.value ?? '').toString() !== value.toString()) {
              indexes.push(index);
            }
          }
        });
        // 翻转删除数组中的项
        for (let index of indexes.reverse()) {
          options.value.splice(index, 1);
        }
      }

      return {
        bindProps,
        onChange,
        onSearch,
      };
    },
  });
</script>

<style scoped></style>
