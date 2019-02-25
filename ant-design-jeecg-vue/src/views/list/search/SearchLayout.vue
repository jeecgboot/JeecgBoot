<template>
  <div class="search-content">
    <router-view />
  </div>
</template>

<script>
  export default {
    name: "SearchLayout",
    data () {
      return {
        tabs: {
          items: [
            {
              key: '1',
              title: '文章'
            },
            {
              key: '2',
              title: '项目'
            },
            {
              key: '3',
              title: '应用'
            },
          ],
          active: () => {
            switch (this.$route.path) {
              case '/list/search/article':
                return '1'
              case '/list/search/project':
                return '2'
              case '/list/search/application':
                return '3'
              default:
                return '1'
            }
          },
          callback: (key) => {
            switch (key) {
              case '1':
                this.$router.push('/list/search/article')
                break
              case '2':
                this.$router.push('/list/search/project')
                break
              case '3':
                this.$router.push('/list/search/application')
                break
              default:
                this.$router.push('/workplace')
            }
          }
        },
        search: true
      }
    },
    computed: {

    },
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
  .search-head{
    background-color: #fff;
    margin: -25px -24px -24px;
    .search-input{
      text-align: center;
      margin-bottom: 16px;
    }
  }
  .search-content{
    margin-top: 48px;
  }
</style>