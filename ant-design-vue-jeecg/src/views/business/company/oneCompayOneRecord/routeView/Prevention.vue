<template>
  <div>
    <a-tabs default-active-key="1">
      <a-tab-pane v-for="item in sites" :key="item.key" :tab="item.text">
        <company-prevention-list :type="item.key" :headerText="item.text" :companyId="companyId" :operationShow="operationShow"
                                 :listType="listType"></company-prevention-list>
      </a-tab-pane>
    </a-tabs>

  </div>
</template>

<script>
  //一企一档 污染防治
  //设计方案
  import CompanyPreventionList from "./CompanyPreventionList";

  export default {
    name: "Prevention",
    props: {
      companyId: '',
      operationShow: '',
      listType: ''
    },
    components: {CompanyPreventionList},
    data() {
      return {
        companyImage: '',
        controlCrafts: '',
        businessLicense: '',
        floorPlan: '',
        produceCrafts: '',
        sites: [{
          key: "1",
          text: "设计方案"
        }, {
          key: "2",
          text: "工艺流程图"
        }, {
          key: "3",
          text: "现场图片"
        }, {
          key: "4",
          text: "雨污管道路线图"
        }, {
          key: "5",
          text: "排污口现场图片"
        }
        ]
      };
    },
    methods: {},
  }
</script>

<style scoped>

</style>