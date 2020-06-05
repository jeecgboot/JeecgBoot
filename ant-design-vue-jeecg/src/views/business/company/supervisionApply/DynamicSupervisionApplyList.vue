<template>

   <company-dynamic-supervision-list ref="modalList" :company-id="companyid" role="monitor"></company-dynamic-supervision-list>
</template>

<script>
  import CompanyDynamicSupervisionList from '../oneCompayOneRecord/routeView/CompanyDynamicSupervisionList'
    export default {
      name: "",
      components: {CompanyDynamicSupervisionList},
      computed: {},
      data(){
        return{
          companyid:this.$store.getters.userInfo.companyIds[0]
        }
      },
      methods:{

      },
      mounted()
      {
          this.$refs.modalList.columns = [{
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
            {
              title: '材料名称',
              align: "center",
              dataIndex: 'documentName'
            },
            {
              title: '材料类型',
              align: "center",
              dataIndex: 'documentType_dictText'
            },
            {
              title: '企业名称',
              align: "center",
              dataIndex: 'companyName'
            },
            {
              title: '所属年份',
              align: "center",
              dataIndex: 'reportYear'
            },
            {
              title:'申报状态',
              align:"center",
              dataIndex: 'status_dictText'
            },
            {
              title:'申报时间',
              align:"center",
              dataIndex: 'createTime',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'申报人',
              align:"center",
              dataIndex: 'createBy'
            },
            {
              title:'审核时间',
              align:"center",
              dataIndex: 'updateTime',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'审核人',
              align:"center",
              dataIndex: 'updateBy'
            },
            {
              title: '操作',
              dataIndex: 'action',
              align:"center",
              // fixed:"right",
              width:147,
              scopedSlots: { customRender: 'action' }
            }
            ];
      },
      created(){
        console.log(this.$store.getters.userInfo)
        if(this.companyid==null) {

          this.companyid = this.$store.getters.userInfo.companyIds[0]
        }
      }

    }
</script>

<style scoped>

</style>