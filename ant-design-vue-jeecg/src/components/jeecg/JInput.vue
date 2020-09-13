<template>
  <a-input :placeholder="placeholder" :value="inputVal" @input="backValue"></a-input>
</template>

<script>
  const JINPUT_QUERY_LIKE = 'like';
  const JINPUT_QUERY_NE = 'ne';
  const JINPUT_QUERY_GE = 'ge'; //大于等于
  const JINPUT_QUERY_LE = 'le'; //小于等于
  
  export default {
    name: 'JInput',
    props:{
      value:{
        type:String,
        required:false
      },
      type:{
        type:String,
        required:false,
        default:JINPUT_QUERY_LIKE
      },
      placeholder:{
        type:String,
        required:false,
        default:''
      },
      trim:{
        type: Boolean,
        required: false,
        default:false
      }
    },
    watch:{
      value:{
        immediate:true,
        handler:function(){
          this.initVal();
        }
      },
      // update-begin author:sunjianlei date:20200225 for:当 type 变化的时候重新计算值 ------
      type() {
        this.backValue({ target: { value: this.inputVal } })
      },
      // update-end author:sunjianlei date:20200225 for:当 type 变化的时候重新计算值 ------
    },
    model: {
      prop: 'value',
      event: 'change'
    },
    data(){
      return {
        inputVal:''
      }
    },
    methods:{
      initVal(){
        if(!this.value){
          this.inputVal = ''
        }else{
          let text = this.value
          switch (this.type) {
            case JINPUT_QUERY_LIKE:
              //修复路由传参的值传送到jinput框被前后各截取了一位 #1336
              if(text.indexOf("*") != -1){
                text = text.substring(1,text.length-1);
              }
              break;
            case JINPUT_QUERY_NE:
              text = text.substring(1);
              break;
            case JINPUT_QUERY_GE:
              text = text.substring(2);
              break;
            case JINPUT_QUERY_LE:
              text = text.substring(2);
              break;
            default:
          }
          this.inputVal = text
        }
      },
      backValue(e){
        let text = e.target.value
        if(text && this.trim===true){
          text = text.trim()
        }
        switch (this.type) {
          case JINPUT_QUERY_LIKE:
            text = "*"+text+"*";
            break;
          case JINPUT_QUERY_NE:
            text = "!"+text;
            break;
          case JINPUT_QUERY_GE:
            text = ">="+text;
            break;
          case JINPUT_QUERY_LE:
            text = "<="+text;
            break;
          default:
        }
        this.$emit("change",text)
      }
    }
  }
</script>

<style scoped>

</style>