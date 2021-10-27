<template>
  <a-select :value="arrayValue" @change="onChange" mode="multiple" :placeholder="placeholder" allowClear>
    <a-select-option
      v-for="(item,index) in selectOptions"
      :key="index"
      :getPopupContainer="getParentContainer"
      :value="item.value">
      {{ item.text || item.label }}
    </a-select-option>
  </a-select>
</template>

<script>
  //option {label:,value:}
  import { getAction } from '@api/manage'

  export default {
    name: 'JSelectMultiple',
    props: {
      placeholder:{
        type: String,
        default:'',
        required: false
      },
      value:{
        type: String,
        required: false
      },
      readOnly:{
        type: Boolean,
        required: false,
        default: false
      },
      options:{
        type: Array,
        default:()=>[],
        required: false
      },
      triggerChange:{
        type: Boolean,
        required: false,
        default: false
      },
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
      dictCode:{
        type:String,
        required:false
      },
    },
    data(){
      return {
        arrayValue:!this.value?[]:this.value.split(this.spliter),
        dictOptions: [],
      }
    },
    computed:{
      selectOptions(){
        return this.dictOptions.length > 0 ? this.dictOptions : this.options
      },
    },
    watch:{
      value (val) {
        if(!val){
          this.arrayValue = []
        }else{
          this.arrayValue = this.value.split(this.spliter)
        }
      }
    },
    mounted(){
      if (this.dictCode) {
        this.loadDictOptions()
      }
    },
    methods:{
      onChange (selectedValue) {
        if(this.triggerChange){
          this.$emit('change', selectedValue.join(this.spliter));
        }else{
          this.$emit('input', selectedValue.join(this.spliter));
        }
      },
      getParentContainer(node){
        if(!this.popContainer){
          return node.parentNode
        }else{
          return document.querySelector(this.popContainer)
        }
      },
      // 根据字典code查询字典项
      loadDictOptions(){
        getAction(`/sys/dict/getDictItems/${this.dictCode}`,{}).then(res=>{
          if (res.success) {
            this.dictOptions = res.result.map(item => ({value: item.value, label: item.text}))
          } else {
            console.error('getDictItems error: : ', res)
            this.dictOptions = []
          }
        })
      },
    },

  }
</script>
