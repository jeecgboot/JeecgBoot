<template>
  <a-checkbox-group :options="options" :value="checkboxArray" v-bind="$attrs" @change="onChange" />
</template>

<script>
  export default {
    name: 'JCheckbox',
    props: {
      value:{
        type: String,
        required: false
      },
      /*label value*/
      options:{
        type: Array,
        required: true
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
        this.$emit('change', checkedValues.join(","));
      },
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
