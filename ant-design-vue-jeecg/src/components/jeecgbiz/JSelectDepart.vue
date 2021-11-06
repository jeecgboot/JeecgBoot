<template>
  <div class="components-input-demo-presuffix">
    <!---->
    <a-input @click="openModal" placeholder="请点击选择部门" v-model="textVals" readOnly :disabled="disabled">
      <a-icon slot="prefix" type="cluster" title="部门选择控件"/>
      <a-icon v-if="storeVals" slot="suffix" type="close-circle" @click="handleEmpty" title="清空"/>
    </a-input>

    <j-select-depart-modal
      ref="innerDepartSelectModal"
      :modal-width="modalWidth"
      :multi="multi"
      :rootOpened="rootOpened"
      :depart-id="value"
      :store="storeField"
      :text="textField"
      :treeOpera="treeOpera"
      @ok="handleOK"
      @initComp="initComp"/>
  </div>
</template>

<script>
  import JSelectDepartModal from './modal/JSelectDepartModal'
  import { underLinetoHump } from '@/components/_util/StringUtil'
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
      disabled:{
        type: Boolean,
        required: false,
        default: false
      },
      // 自定义返回字段，默认返回 id
      customReturnField: {
        type: String,
        default: ''
      },
      backDepart: {
        type: Boolean,
        default: false,
        required: false
      },
      // 存储字段 [key field]
      store: {
        type: String,
        default: 'id',
        required: false
      },
      // 显示字段 [label field]
      text: {
        type: String,
        default: 'departName',
        required: false
      },
      treeOpera: {
        type: Boolean,
        default: false,
        required: false
      }
      
    },
    data(){
      return {
        visible:false,
        confirmLoading:false,
        storeVals: '', //[key values]
        textVals: '' //[label values]
      }
    },
    computed:{
      storeField(){
        let field = this.customReturnField
        if(!field){
          field = this.store;
        }
        return underLinetoHump(field)
      },
      textField(){
        return underLinetoHump(this.text)
      }
    },
    mounted(){
      this.storeVals = this.value
    },
    watch:{
      value(val){
        this.storeVals = val
      }
    },
    methods:{
      initComp(textVals){
        this.textVals = textVals
      },
      //返回选中的部门信息
      backDeparInfo(){
        if(this.backDepart===true){
          //LOWCOD-2147 【用户管理】选择部门和上级以后，负责部门没有数据可选 (陶炎改造自定义返回字段导致)
          if(this.storeVals && this.storeVals.length>0){
            let arr1 = this.storeVals.split(',')
            let arr2 = this.textVals.split(',')
            let info = []
            for(let i=0;i<arr1.length;i++){
              info.push({
                value: arr1[i],
                text: arr2[i]
              })
            }
            this.$emit('back', info)
          }
        }
      },
      openModal(){
        this.$refs.innerDepartSelectModal.show()
      },
      handleOK(rows) {
        if (!rows && rows.length <= 0) {
          this.textVals = ''
          this.storeVals = ''
        } else {
          let arr1 = []
          let arr2 = []
          for(let dep of rows){
            arr1.push(dep[this.storeField])
            arr2.push(dep[this.textField])
          }
          this.storeVals = arr1.join(',')
          this.textVals = arr2.join(',')
        }
        this.$emit("change", this.storeVals)
        this.backDeparInfo()
      },
      getDepartNames(){
        return this.departNames
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