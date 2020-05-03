<template>
  <a-switch v-model="checkStatus" :disabled="disabled" @change="handleChange"/>
</template>
<script>

  export default {
    name: 'JSwitch',
    props: {
      value:{
        type: String,
        required: false
      },
      disabled:{
        type: Boolean,
        required: false,
        default: false
      },
      options:{
        type:Array,
        required:false,
        default:()=>['Y','N']
      }
    },
    data () {
      return {
        checkStatus: false
      }
    },
    watch: {
      value:{
        immediate: true,
        handler(val){
          if(!val){
            this.checkStatus = false
            this.$emit('change', this.options[1]);
          }else{
            if(this.options[0]==val){
              this.checkStatus = true
            }else{
              this.checkStatus = false
            }
          }
        }
      }
    },
    methods: {
      handleChange(checked){
        let flag = checked===false?this.options[1]:this.options[0];
        this.$emit('change', flag);
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
