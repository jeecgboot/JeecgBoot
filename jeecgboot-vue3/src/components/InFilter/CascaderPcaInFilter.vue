<!-- 省市县选择组件，在筛选中的用法 -->
<template>
  <a-space :class="[prefixCls]" direction="vertical">
  </a-space>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: 'CascaderPcaInFilter',
  inheritAttrs: false,
})
</script>

<script lang="ts" setup>

</script>

<style lang="less">
//noinspection LessUnresolvedVariable
@prefix-cls: ~'@{namespace}-j-cascader-pca-in-filter';

.@{prefix-cls} {
  width: 100%;
}


.@{prefix-cls}-menu-item-icon {
  width: 16px;
  height: 16px;
  display: inline-block;
  margin-right: 6px;

  &, & + span {
    vertical-align: middle;
  }
}
</style>
