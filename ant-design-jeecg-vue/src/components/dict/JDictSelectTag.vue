<template>
  <a-select :placeholder="placeholder" :value="value" @change="handleInput">
    <a-select-option value="">请选择</a-select-option>
    <a-select-option v-for="(item, key) in dictOptions" :key="key" :value="item.value">{{ item.text }}</a-select-option>
  </a-select>
</template>

<script>
  import {ajaxGetDictItems} from '@/api/api'

  export default {
    name: "JDictSelectTag",
    props: {
      dictCode: String,
      placeholder: String,
      triggerChange: Boolean,
      value: String,// 1.接收一个 value prop
    },
    data() {
      return {
        dictOptions: [],
      }
    },
    created() {
      console.log(this.dictCode);
      //获取字典数据
      this.initDictData();
    },
    methods: {
      initDictData() {
        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(this.dictCode, null).then((res) => {
          if (res.success) {
//                console.log(res.result);
            this.dictOptions = res.result;
          }
        })
      },
      handleInput(val) {
        console.log(val);
        if(this.triggerChange){
          this.$emit('change', val);
        }else{
          this.$emit('input', val);
        }
      }
    }
  }
</script>

<style scoped>
</style>