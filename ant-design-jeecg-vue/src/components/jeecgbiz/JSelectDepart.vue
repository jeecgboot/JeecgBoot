<template>
  <div class="components-input-demo-presuffix">
    <!---->
    <a-input @click="openModal" placeholder="请点击选择部门" v-model="departNames" readOnly :disabled="disabled">
      <a-icon slot="prefix" type="cluster" title="部门选择控件"/>
      <a-icon v-if="departIds" slot="suffix" type="close-circle" @click="handleEmpty" title="清空"/>
    </a-input>

    <j-select-depart-modal
      ref="innerDepartSelectModal"
      :modal-width="modalWidth"
      :multi="multi"
      :rootOpened="rootOpened"
      :depart-id="value"
      @ok="handleOK"
      @initComp="initComp"/>
  </div>
</template>

<script>
  import JSelectDepartModal from './modal/JSelectDepartModal'
  export default {
    name: 'JSelectDepart',
    components:{
      JSelectDepartModal
    },
    props:{
      modalWidth:{
        type:Number,
        default:500,
        required:false
      },
      multi:{
        type:Boolean,
        default:false,
        required:false
      },
      rootOpened:{
        type:Boolean,
        default:true,
        required:false
      },
      value:{
        type:String,
        required:false
      },
      triggerChange:{
        type: Boolean,
        required: false,
        default: false
      },
      disabled:{
        type: Boolean,
        required: false,
        default: false
      }
    },
    data(){
      return {
        visible:false,
        confirmLoading:false,
        departNames:"",
        departIds:''
      }
    },
    mounted(){
      this.departIds = this.value
    },
    watch:{
      value(val){
        this.departIds = val
      }
    },
    methods:{
      initComp(departNames){
        this.departNames = departNames
      },
      openModal(){
        this.$refs.innerDepartSelectModal.show()
      },
      handleOK(rows,idstr){
        console.log("当前选中部门",rows)
        console.log("当前选中部门ID",idstr)
        if(!rows){
          this.departNames = ''
          this.departIds=''
        }else{
          let temp = ''
          for(let item of rows){
            temp+=','+item.departName
          }
          this.departNames = temp.substring(1)
          this.departIds=idstr
        }

        if(this.triggerChange){
          this.$emit("change",this.departIds)
        }else{
          this.$emit("input",this.departIds)
        }
      },
      getDepartNames(){
        return this.departNames
      },
      handleEmpty(){
        this.handleOK('')
      }
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