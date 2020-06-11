<template>
  <div>
    <company-apply-list v-if="!listshow" :company-id="companyId" :from-table="fromTable"
                        :hoverable="latestArchived" @toDetail="detail" @toApply="apply"
                        @applyDetail="viewApply"></company-apply-list>
    <company-env-tax-list v-if="listshow" :company-id="companyId" :operationShow="operationShow" :listType="listType"
    ></company-env-tax-list>
    <companyApply-modal ref="applyInfoForm"></companyApply-modal>
  </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import CompanyEnvTaxList from "../oneCompayOneRecord/routeView/CompanyEnvTaxList";
  import {queryComparisonData, queryLatestArchivedData} from "../../requestAction/request"
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";

  export default {
    name: "AcceptanceApply",
    components: {
      CompanyEnvTaxList,
      CompanyApplyList,
      queryLatestArchivedData,
      CompanyApplyModal
    },
    data() {
      return {
        listshow: false,
        fromTable: "company_env_tax",
        hoverable: true,
        //最新归档信息数据
        latestArchived: false,
        companyId: this.$store.getters.userInfo.companyIds[0],
        operationShow: false,
        listType: "0"
      }
    },
    //计算属性
    computed: {},
    methods: {
      //详情
      detail() {
        this.title = "详情";
        this.listshow = true;
        this.operationShow = false;
        this.listType = "1";
      },
      //申报
      apply() {
        this.title = "申报";
        this.listshow = true;
        this.operationShow = true;
        this.listType = "0";
      },
      //查看
      viewApply(record) {

        console.log(record)
        //查询详情数据
        this.$refs.applyInfoForm.detail(record);
        //单个表比较
        this.$refs.applyInfoForm.compareDetail(this.fromTable);
      }


    },
    created() {

      let that = this;
      //查询最新归档信息
      queryLatestArchivedData({companyId: this.companyId, fromTable: this.fromTable}).then((res) => {
        if (res.success) {
          that.latestArchived = res.result;
        } else {
          that.$message.warning(res.message);
        }

      })


    }
  }
</script>

<style scoped>

</style>