<template>
  <div>
    <a-select v-if="query" style="width: 100%" @change="handleSelectChange">
      <a-select-option v-for="(item, index) in queryOption" :key="index" :value="item.value">
        {{ item.text }}
      </a-select-option>
    </a-select>
    <a-switch v-else v-model="checkStatus" :disabled="disabled" @change="handleChange"/>
  </div>
</template>
<script>

  export default {
    name: 'JSwitch',
    props: {
      value:{
        type: String | Number,
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
      },
      query:{
        type: Boolean,
        required: false,
        default: false
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
          if(!this.query){
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
      }
    },
    computed:{
      queryOption(){
        let arr = []
        arr.push({value:this.options[0],text:'是'})
        arr.push({value:this.options[1],text:'否'})
        return arr;
      }
    },
    methods: {
      handleChange(checked){
        let flag = checked===false?this.options[1]:this.options[0];
        this.$emit('change', flag);
      },
      handleSelectChange(value){
        this.$emit('change', value);
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
