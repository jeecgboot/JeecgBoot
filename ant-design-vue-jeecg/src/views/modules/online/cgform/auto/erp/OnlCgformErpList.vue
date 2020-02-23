<template>
  <a-card :bordered="false" style="height: 100%">
    <online-common-list
      :ref="'onl_'+mainModel.currentTableName"
      :code="code"
      :model="mainModel"
      @seleted="onSelected">
    </online-common-list>

    <a-tabs defaultActiveKey="0">
      <a-tab-pane v-for="(item,index) in subList" :tab="item.description" :key="index+''" :forceRender="true" >
        <online-common-list
          :ref="item.currentTableName"
          :code="item.code"
          :model="item"
          :main="selectedRow">
        </online-common-list>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
  import { getAction } from '@/api/manage'

  export default {
    name: 'OnlCgformErpList',
    components:{
    },
    data(){
      return {
        code:'',
        url: {
          getColumns: '/online/cgform/api/getErpColumns/',
        },
        mainModel:{},
        subList:[],
        mainId:'',
        selectedRow:{}

      }
    },
    watch: {
      '$route'() {
        // 刷新参数放到这里去触发，就可以刷新相同界面了
        this.initColumnConfig()
      }
    },

    created() {
      this.initColumnConfig();
    },
    methods:{
      getSubIndex(index){
        return index+1 + ''
      },
      getSubRef(item){
        let ref = item.currentTableName
        console.log("ref string",ref)
        return ref;
      },
      initColumnConfig(){
        if(!this.$route.params.code){
          return false
        }
        this.code = this.$route.params.code
        getAction(`${this.url.getColumns}${this.code}`).then((res)=>{
          console.log("erp表单配置",res)
          if(res.success){
            this.mainModel = res.result.main
            this.subList = res.result.subList

            this.$nextTick(()=>{
              this.$refs['onl_'+this.mainModel.currentTableName].initListByModel();
              if(this.subList && this.subList.length>0){
                for(let item of this.subList){
                  this.$refs[item.currentTableName][0].initListByModel();
                }
              }
            });

          }
        })
      },
      onSelected(row){
        console.log("onSelected",row)
        this.selectedRow = row;
      }


    }
  }
</script>

<style>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .anty-img-wrap{height:25px;position: relative;}
  .anty-img-wrap > img{max-height:100%;}
  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>