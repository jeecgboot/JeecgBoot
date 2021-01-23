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
    :placeholder="placeholder"
    :getPopupContainer="getParentContainer"
    optionFilterProp="children"
    :filterOption="filterOption"
    allowClear>
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
  import {ajaxGetDictItems,getDictItemsFromCache} from '@/api/api'
  export default {
    name: 'JMultiSelectTag',
    props: {
      dictCode: String,
      placeholder: String,
      disabled: Boolean,
      value: String,
      type: String,
      options:Array,
      spliter:{
        type: String,
        required: false,
        default: ','
      },
      popContainer:{
        type:String,
        default:'',
        required:false
      },
    },
    data() {
      return {
        dictOptions: [],
        tagType:"",
        arrayValue:!this.value?[]:this.value.split(this.spliter)
      }
    },
    created() {
      if(!this.type || this.type==="list_multi"){
        this.tagType = "select"
      }else{
        this.tagType = this.type
      }
      //获取字典数据
      //this.initDictData();
    },
    watch:{
      options: function(val){
        this.setCurrentDictOptions(val);
      },
      dictCode:{
        immediate:true,
        handler() {
          this.initDictData()
        },
      },
      value (val) {
        if(!val){
          this.arrayValue = []
        }else{
          this.arrayValue = this.value.split(this.spliter)
        }
      }
    },
    methods: {
      initDictData() {
        if(this.options && this.options.length>0){
          this.dictOptions = [...this.options]
        }else{
          //优先从缓存中读取字典配置
          let cacheOption = getDictItemsFromCache(this.dictCode)
          if(cacheOption && cacheOption.length>0){
            this.dictOptions = cacheOption
            return
          }
          //根据字典Code, 初始化字典数组
          ajaxGetDictItems(this.dictCode, null).then((res) => {
            if (res.success) {
              this.dictOptions = res.result;
            }
          })
        }

      },
      onChange (selectedValue) {
        this.$emit('change', selectedValue.join(this.spliter));
      },
      setCurrentDictOptions(dictOptions){
        this.dictOptions = dictOptions
      },
      getCurrentDictOptions(){
        return this.dictOptions
      },
      getParentContainer(node){
        if(!this.popContainer){
          return node.parentNode
        }else{
          return document.querySelector(this.popContainer)
        }
      },
      // update--begin--autor:lvdandan-----date:20201120------for：LOWCOD-1086 下拉多选框,搜索时只字典code进行搜索不能通过字典text搜索
      filterOption(input, option) {
        return option.componentOptions.children[0].children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      }
      // update--end--autor:lvdandan-----date:20201120------for：LOWCOD-1086 下拉多选框,搜索时只字典code进行搜索不能通过字典text搜索
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
