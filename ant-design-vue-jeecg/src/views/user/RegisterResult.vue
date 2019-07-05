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
        form: {},
      }
    },
    computed: {
      email () {
        let v = this.form && this.form.username || 'xxx'
        let title = `你的账户：${v} 注册成功`
        this.username = v;
        return title
      }
    },
    created () {
      this.form = this.$route.params
    },
    methods: {
      goHomeHandle () {
        let params={};
        params.username=this.form.username;
        params.password=this.form.password;
        console.log(params);
        this.$router.push({name:'login',params})
      },
    }
  }
</script>

<style scoped>

</style>