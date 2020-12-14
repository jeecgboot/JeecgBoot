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
      :depart-id="departIds"
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
      disabled:{
        type: Boolean,
        required: false,
        default: false
      },
      // 自定义返回字段，默认返回 id
      customReturnField: {
        type: String,
        default: 'id'
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
        //update-begin-author:wangshuai date:20201124 for:组件 JSelectDepart.vue不是默认id时新内容编辑问题 gitee I247X2
        // if (this.customReturnField === 'id') {
          this.departIds = val
        // }
        //update-end-author:wangshuai date:20201124 for:组件 JSelectDepart.vue不是默认id时新内容编辑问题 gitee I247X2
      }
    },
    methods:{
      initComp(departNames){
        this.departNames = departNames
        //update-begin-author:lvdandan date:20200513 for:TESTA-438 部门选择组件自定义返回值，数据无法回填
        //TODO 当返回字段为部门名称时会有问题,因为部门名称不唯一
        //返回字段不为id时，根据返回字段获取id
        if(this.customReturnField !== 'id' && this.value){
          const dataList = this.$refs.innerDepartSelectModal.dataList;
          console.log('this.value',this.value)
          this.departIds = this.value.split(',').map(item => {
            const data = dataList.filter(d=>d[this.customReturnField] === item)
            return data.length > 0 ? data[0].id : ''
          }).join(',')
        }
        //update-end-author:lvdandan date:20200513 for:TESTA-438 部门选择组件自定义返回值，数据无法回填
      },
      openModal(){
        this.$refs.innerDepartSelectModal.show()
      },
      handleOK(rows, idstr) {
        let value = ''
        if (!rows && rows.length <= 0) {
          this.departNames = ''
          this.departIds = ''
        } else {
          value = rows.map(row => row[this.customReturnField]).join(',')
          this.departNames = rows.map(row => row['departName']).join(',')
          this.departIds = idstr
        }
        this.$emit("change", value)
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