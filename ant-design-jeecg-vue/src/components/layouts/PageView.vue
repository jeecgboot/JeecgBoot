<template>
  <page-layout :desc="description" :title="getTitle" :link-list="linkList" :search="search" :tabs="tabs">
    <div slot="extra" class="extra-img">
      <img :src="extraImage"/>
    </div>
    <!-- keep-alive  -->
    <route-view ref="content"></route-view>
  </page-layout>
</template>

<script>
  import PageLayout from '../page/PageLayout'
  import RouteView from './RouteView'

  export default {
    name: "PageContent",
    components: {
      RouteView,
      PageLayout
    },
    data () {
      return {
        title: '',
        description: '',
        linkList: [],
        extraImage: '',
        search: false,
        tabs: {}
      }
    },
    mounted () {
      this.getPageHeaderInfo()
    },
    updated () {
      this.getPageHeaderInfo()
    },
    computed: {

      getTitle () {
        return this.$route.meta.title
      }

    },
    methods: {
      getPageHeaderInfo () {
        // eslint-disable-next-line
        this.title = this.$route.meta.title
        // 因为套用了一层 route-view 所以要取 ref 对象下的子节点的第一个对象
        const content = this.$refs.content && this.$refs.content.$children[0]

        if (content) {
          this.description = content.description
          this.linkList = content.linkList
          this.extraImage = content.extraImage
          this.search = content.search == true ? true : false
          this.tabs = content.tabs
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .extra-img {
    margin-top: -60px;
    text-align: center;
    width: 195px;

    img {
      width: 100%;
    }
  }

  .mobile {
    .extra-img{
      margin-top: 0;
      text-align: center;
      width: 96px;

      img{
        width: 100%;
      }
    }
  }
</style>