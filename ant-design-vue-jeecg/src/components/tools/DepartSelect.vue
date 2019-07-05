<template>
  <a-modal
    :title="title"
    :width="450"
    :visible="visible"
    :closable="false"
    :maskClosable="closable">
    <template slot="footer">
      <a-button v-if="closable" @click="close">关闭</a-button>
      <a-button type="primary" @click="departOk">确认</a-button>
    </template>

    <a-form>
      <a-form-item
        :labelCol="{span:4}"
        :wrapperCol="{span:20}"
        style="margin-bottom:10px"
        :validate-status="validate_status">
        <a-tooltip placement="topLeft" >
          <template slot="title">
            <span>您隶属于多部门，请选择当前所在部门</span>
          </template>
          <a-avatar style="backgroundColor:#87d068" icon="gold" />
        </a-tooltip>
        <a-select v-model="departSelected" :class="{'valid-error':validate_status=='error'}" placeholder="请选择登录部门" style="margin-left:10px;width: 80%">
          <a-icon slot="suffixIcon" type="gold" />
          <a-select-option
            v-for="d in departList"
            :key="d.id"
            :value="d.orgCode">
            {{ d.departName }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>


  </a-modal>
    
</template>

<script>
  import { getAction,putAction } from '@/api/manage'

  export default {
    name: 'DepartSelect',
    props:{
      title:{
        type:String,
        default:"部门选择",
        required:false
      },
      closable:{
        type:Boolean,
        default:false,
        required:false
      },
      username:{
        type:String,
        default:"",
        required:false
      }
    },
    watch:{
      username(val){
        if(val){
          this.loadDepartList()
        }
      }
    },
    data(){
      return {
        visible:false,
        departList:[],
        departSelected:"",
        validate_status:"",
        currDepartName:"",
      }
    },
    created(){
      //this.loadDepartList()
    },
    methods:{
      loadDepartList(){
        return new Promise(resolve => {
          let url = "/sys/user/getCurrentUserDeparts"
          this.currDepartName=''
          getAction(url).then(res=>{
            if(res.success){
              let departs = res.result.list
              let orgCode = res.result.orgCode
              if(departs && departs.length>0){
                for(let i of departs){
                  if(i.orgCode == orgCode){
                    this.currDepartName = i.departName
                    break
                  }
                }
              }
              this.departSelected = orgCode
              this.departList  = departs
              if(this.currDepartName){
                this.title ="部门切换（当前部门 : "+this.currDepartName+"）"
              }

            }
            resolve()
          })
        })
      },
      close(){
        this.departClear()
      },
      departOk(){
        if(!this.departSelected){
          this.validate_status='error'
          return false
        }
        let obj = {
          orgCode:this.departSelected,
          username:this.username
        }
        putAction("/sys/selectDepart",obj).then(res=>{
          if(res.success){
            this.departClear()
          }
        })
      },
      show(){
        //如果组件传值username此处就不用loadDepartList了
        this.loadDepartList().then(()=>{
          this.visible=true
          if(!this.departList || this.departList.length<=0){
            this.$message.warning("您尚未设置部门信息!")
            this.departClear()
          }
        })
      },
      departClear(){
        this.departList=[]
        this.departSelected=""
        this.visible=false
        this.validate_status=''
        this.currDepartName=""
      },
    }

  }
</script>
<style scoped>
  .valid-error .ant-select-selection__placeholder{
    color: #f5222d;
  }
</style>