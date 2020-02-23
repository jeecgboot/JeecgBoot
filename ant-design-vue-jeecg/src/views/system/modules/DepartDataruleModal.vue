<template>
  <a-drawer
    title="数据规则/按钮权限配置"
    width="365"
    :closable="false"
    @close="onClose"
    :visible="visible"
  >
    <a-tabs defaultActiveKey="1">
      <a-tab-pane tab="数据规则" key="1">

        <a-checkbox-group v-model="dataruleChecked" v-if="dataruleList.length>0">
          <a-row>
            <a-col :span="24" v-for="(item,index) in dataruleList" :key=" 'dr'+index ">
              <a-checkbox :value="item.id">{{ item.ruleName }}</a-checkbox>
            </a-col>

            <a-col :span="24">
              <div style="width: 100%;margin-top: 15px">
                <a-button @click="saveDataruleForRole" type="primary" size="small" icon="save">点击保存</a-button>
              </div>
            </a-col>
          </a-row>
        </a-checkbox-group>
        <div v-else><h3>无配置信息!</h3></div>

      </a-tab-pane>
    </a-tabs>

  </a-drawer>
</template>

<script>
  import ARow from 'ant-design-vue/es/grid/Row'
  import ACol from 'ant-design-vue/es/grid/Col'
  import { getAction,postAction } from '@/api/manage'

  export default {
    name: 'DepartDataruleModal',
    components: { ACol, ARow },
    data(){
      return {
        functionId:'',
        departId:'',
        visible:false,
        tabList: [{
          key: '1',
          tab: '数据规则',
        }, {
          key: '2',
          tab: '按钮权限',
        }],
        activeTabKey: '1',
        url:{
          datarule:"/sys/sysDepartPermission/datarule",
        },
        dataruleList:[],
        dataruleChecked:[]
      }
    },
    methods:{
      loadData(){
        getAction(`${this.url.datarule}/${this.functionId}/${this.departId}`).then(res=>{
          if(res.success){
            this.dataruleList = res.result.datarule
            let drChecked = res.result.drChecked
            if(drChecked){
              this.dataruleChecked = drChecked.split(",")
            }
          }
        })
      },
      saveDataruleForRole(){
        if(!this.dataruleChecked || this.dataruleChecked.length==0){
          this.$message.warning("请注意，现未勾选任何数据权限!")
        }
        let params = {
          permissionId:this.functionId,
          departId:this.departId,
          dataRuleIds:this.dataruleChecked.join(",")
        }
        postAction(this.url.datarule,params).then(res=>{
          if(res.success){
            this.$message.success(res.message)
          }else{
            this.$message.error(res.message)
          }
        })
      },
      show(functionId,departId){
        this.onReset()
        this.functionId = functionId
        this.departId = departId
        this.visible=true
        this.loadData()
      },
      onClose(){
        this.visible=false
        this.onReset()
      },
      onTabChange (key) {
        this.activeTabKey = key
      },
      onReset(){
        this.functionId=''
        this.departId=''
        this.dataruleList=[]
        this.dataruleChecked=[]
      }
    }
  }
</script>

<style scoped>

</style>