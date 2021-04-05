<template>
  <result
    :isSuccess="true"
    :content="false"
    :title="email">

    <template slot="action">
      <a-button size="large" style="margin-left: 8px" @click="goHomeHandle">返回首页</a-button>
    </template>

  </result>
</template>

<script>
  import Result from '@/views/result/Result'

  export default {
    name: "RegisterResult",
    components: {
      Result
    },
    data () {
      return {
        model: {},
      }
    },
    computed: {
      email () {
        let v = this.model ? this.model.username || this.model.mobile : ' XXX '
        let title = `你的账户：${v} 注册成功`
        this.username = v;
        return title
      }
    },
    created () {
      this.model = this.$route.params
    },
    methods: {
      goHomeHandle () {
        let params={};
        params.username=this.model.username;
        params.password=this.model.password;
        console.log(params);
        this.$router.push({name:'login',params})
      },
    }
  }
</script>

<style scoped>

</style>