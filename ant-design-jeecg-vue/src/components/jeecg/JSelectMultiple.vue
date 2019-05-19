<template>
  <a-select :value="arrayValue" @change="onChange" mode="multiple" :placeholder="placeholder">
    <a-select-option
      v-for="(item,index) in options"
      :key="index"
      :value="item.value">
      {{ item.text || item.label }}
    </a-select-option>
  </a-select>
</template>

<script>
  //option {label:,value:}
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
        required: true
      },
      triggerChange:{
        type: Boolean,
        required: false,
        default: false
      }
    },
    data(){
      return {
        arrayValue:!this.value?[]:this.value.split(",")
      }
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
    methods:{
      onChange (selectedValue) {
        if(this.triggerChange){
          this.$emit('change', selectedValue.join(","));
        }else{
          this.$emit('input', selectedValue.join(","));
        }
      },
    }
  }
</script>
