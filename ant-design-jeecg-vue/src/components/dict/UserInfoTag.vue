<template>
  <span>
    <!--// 1、当有数据输入时触发了该组件的input事件-->
    <!--<input type="text" :value="value" @input="updateVal($event.target.value)">-->
    <a-input :placeholder="placeholder" :value="value" @input="updateVal($event.target.value)"></a-input>
  </span>
</template>

<script>
  export default {
    props: {
      value: String,
      placeholder: String
    },
    methods: {
      updateVal: function(val) {
        // 2、手动触发父组件的input事件并将值传给父组件
        this.$emit('input', val);
      }
    }
  }
</script>