<template>
  <div>
    {{ msg }}

    <i class="anticon-jeecg anticonjiangpai" style="font-size: 65px; color: #3300aa;"/>

    <div>
      <j-dict-select-tag v-model="queryParam.sex" placeholder="请输入用户性别" dictCode="sex"/>

    </div>
  </div>

</template>

<script>
  import { getAction } from '@/api/manage'

  export default {
    data() {
      return {
        msg: ''
      }
    },
    methods: {
      hello() {
        var url = '/hroop/comStatus/hello'
        getAction(url).then((res) => {
          console.log(res)
          if (res.success) {
            this.msg = res.result
          }
        })
      }
    },
    created() {
      this.hello()
    }
  }
</script>

<style scoped>

</style>