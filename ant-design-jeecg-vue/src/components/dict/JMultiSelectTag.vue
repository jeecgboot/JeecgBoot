<template>
  <a-checkbox-group v-if="tagType=='checkbox'" @change="onChange" :value="arrayValue" :disabled="disabled">
    <a-checkbox v-for="(item, key) in dictOptions" :key="key" :value="item.value">{{ item.text || item.label }}</a-checkbox>
  </a-checkbox-group>

  <a-select
    v-else-if="tagType=='select'"
    :value="arrayValue"
    @change="onChange"
    :disabled="disabled"
    mode="multiple"
    :placeholder="placeholder">
    <a-select-option
      v-for="(item,index) in dictOptions"
      :key="index"
      :value="item.value">
      <span style="display: inline-block;width: 100%" :title=" item.text || item.label ">
        {{ item.text || item.label }}
      </span>
    </a-select-option>
  </a-select>

</template>

<script>
  import {ajaxGetDictItems} from '@/api/api'
  export default {
    name: 'JMultiSelectTag',
    props: {
      dictCode: String,
      placeholder: String,
      triggerChange: Boolean,
      disabled: Boolean,
      value: String,
      type: String,
      options:Array
    },
    data() {
      return {
        dictOptions: [],
        tagType:"",
        arrayValue:!this.value?[]:this.value.split(",")
      }
    },
    created() {
      if(!this.type || this.type==="list_multi"){
        this.tagType = "select"
      }else{
        this.tagType = this.type
      }
      //获取字典数据
      this.initDictData();
    },
    watch:{
      value (val) {
        if(!val){
          this.arrayValue = []
        }else{
          this.arrayValue = this.value.split(",")
        }
      }
    },
    methods: {
      initDictData() {
        if(this.options && this.options.length>0){
          this.dictOptions = [...this.options]
        }else{
          //根据字典Code, 初始化字典数组
          ajaxGetDictItems(this.dictCode, null).then((res) => {
            if (res.success) {
              this.dictOptions = res.result;
            }
          })
        }

      },
      onChange (selectedValue) {
        if(this.triggerChange){
          this.$emit('change', selectedValue.join(","));
        }else{
          this.$emit('input', selectedValue.join(","));
        }
      },
      setCurrentDictOptions(dictOptions){
        this.dictOptions = dictOptions
      },
      getCurrentDictOptions(){
        return this.dictOptions
      }
    }
  }
</script>
