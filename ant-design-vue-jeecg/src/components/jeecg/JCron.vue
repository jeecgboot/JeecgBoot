<template>
  <div class="components-input-demo-presuffix">
    <a-input @click="openModal" placeholder="corn表达式" v-model="cron" @change="handleOK">
      <a-icon slot="prefix" type="schedule" title="corn控件"/>
      <a-icon v-if="cron" slot="suffix" type="close-circle" @click="handleEmpty" title="清空"/>
    </a-input>
    <JCronModal ref="innerVueCron" :data="cron" @ok="handleOK"></JCronModal>
  </div>
</template>
<script>
  import JCronModal from "./modal/JCronModal";
  export default {
    name: 'JCron',
    components: {
      JCronModal
    },
    props: {
      value: {
        required: false,
        type: String,
        default:()=>{
          return '* * * * * ? *'
        }
      }
    },
    data(){
      return {
        cron: this.value,
      }
    },
    watch:{
      value(val){
        this.cron = val
      }
    },
    methods:{
      openModal(){
        this.$refs.innerVueCron.show();
      },
      handleOK(val){
        this.cron = val;
        this.$emit("change", this.cron);
        //this.$emit("change", Object.assign({},  this.cron));
      },
      handleEmpty(){
        this.handleOK('')
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
<style scoped>
  .components-input-demo-presuffix .anticon-close-circle {
    cursor: pointer;
    color: #ccc;
    transition: color 0.3s;
    font-size: 12px;
  }
  .components-input-demo-presuffix .anticon-close-circle:hover {
    color: #f5222d;
  }
  .components-input-demo-presuffix .anticon-close-circle:active {
    color: #666;
  }
</style>
