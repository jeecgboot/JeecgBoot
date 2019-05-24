<template>
  <a-checkbox-group :options="options" :value="checkboxArray" @change="onChange" />
</template>

<script>
  export default {
    name: 'JCheckbox',
    props: {
      value:{
        type: String,
        required: false
      },
      readOnly:{
        type: Boolean,
        required: false,
        default: false
      },
      /*label value*/
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
        checkboxArray:!this.value?[]:this.value.split(",")
      }
    },
    watch:{
      value (val) {
        if(!val){
          this.checkboxArray = []
        }else{
          this.checkboxArray = this.value.split(",")
        }
      }
    },
    methods:{
      onChange (checkedValues) {
        if(this.triggerChange){
          this.$emit('change', checkedValues.join(","));
        }else{
          this.$emit('input', checkedValues.join(","));
        }
      },
    }
  }
</script>
