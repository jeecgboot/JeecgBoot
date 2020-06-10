<template>
    <company-admin-penalties-list ref="modalList" :company-id="companyid" role="monitor"></company-admin-penalties-list>
</template>

<script>
  import CompanyAdminPenaltiesList from '../oneCompayOneRecord/CompanyAdminPenaltiesList'

    export default {
        name: "adminPenaltiesApplyList",
      components: {CompanyAdminPenaltiesList},
      computed: {},
      data(){
        return{
          companyid:this.$store.getters.userInfo.companyIds[0]
        }
      },
      methods:{

      },
      mounted(){
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
              title:'文件名称',
              align:"center",
              dataIndex: 'documentName'
            },
            {
              title:'文件编号',
              align:"center",
              dataIndex: 'documentNo'
            },
            {
              title:'企业名称',
              align:"center",
              dataIndex: 'companyName'
            },
            {
              title:'发文日期',
              align:"center",
              dataIndex: 'reportDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'申报状态',
              align:"center",
              dataIndex: 'status_dictText'
            },
            // {
            //   title:'企业id',
            //   align:"center",
            //   dataIndex: 'companyId'
            // },
            // {
            //   title:'文件上传',
            //   align:"center",
            //   dataIndex: 'content',
            //   scopedSlots: {customRender: 'fileSlot'}
            // },
            {
              title:'申报人',
              align:"center",
              dataIndex: 'createBy'
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
              title:'审核人',
              align:"center",
              dataIndex: 'updateBy'
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