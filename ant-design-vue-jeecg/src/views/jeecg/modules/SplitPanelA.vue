<template>
  <a-card style="min-width: 500px;overflow-x: auto">
    <p>我是左侧页面</p>
    <img-turn-page></img-turn-page>
  </a-card>
</template>

<script>
  import ImgTurnPage from '../ImgTurnPage'
  export default {
    name: "SplitPanelAModal",
    components:{
      ImgTurnPage
    },
    data () {
      return {
      }
    },
    created () {
    },
    methods: {
    }
  }
</script>

<style scoped>
</style>