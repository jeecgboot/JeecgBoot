<template>
  <a-card :bordered="false" style="width: 130%;text-align: center;margin-left:-10%">
    <a-steps class="steps" :current="currentTab">
      <a-step title="用户账户"/>
      <a-step title="手机验证"/>
      <a-step title="密码"/>
      <a-step title="完成"/>
    </a-steps>
    <div class="content">
      <step1 v-if="currentTab === 0" @nextStep="nextStep"/>
      <step2 v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep" :userList="userList"/>
      <step3 v-if="currentTab === 2" @nextStep="nextStep" @prevStep="prevStep" :userList="userList"/>
      <step4 v-if="currentTab === 3" @prevStep="prevStep" @finish="finish" :userList="userList"/>
    </div>
  </a-card>
</template>

<script>
  import Step1 from './Step1'
  import Step2 from './Step2'
  import Step3 from './Step3'
  import Step4 from './Step4'

  export default {
    name: "Alteration",
    components: {
      Step1,
      Step2,
      Step3,
      Step4
    },
    data() {
      return {
        description: '将一个冗长或用户不熟悉的表单任务分成多个步骤，指导用户完成。',
        currentTab: 0,
        userList: {},
        // form
        form: null,
      }
    },
    methods: {

      // handler
      nextStep(data) {
        this.userList = data;
        if (this.currentTab < 4) {
          this.currentTab += 1
        }
      },
      prevStep(data) {
        this.userList = data;
        if (this.currentTab > 0) {
          this.currentTab -= 1
        }
      },
      finish() {
        this.currentTab = 0
      }
    }
  }
</script>

<style lang="less" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
</style>