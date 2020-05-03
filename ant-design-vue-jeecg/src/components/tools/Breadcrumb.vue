<template>
  <a-breadcrumb class="breadcrumb">
    <a-breadcrumb-item v-for="(item, index) in breadList" :key="index">
      <router-link v-if="item.name != name" :to="{ path: item.path }">
        {{ item.meta.title }}
      </router-link>
      <span v-else>{{ item.meta.title }}</span>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script>
export default {
    data() {
      return {
        name: '',
        breadList: [],
      }
    },
  created () {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {

      console.log('this.$route.matched', this.$route.matched)

      this.breadList = []
      this.breadList.push({ name: 'dashboard-analysis', path: '/dashboard/analysis', meta: { title: '首页' } })

      this.name = this.$route.name
      this.$route.matched.forEach((item) => {
        // item.meta.name === 'dashboard' ? item.path = '/dashboard' : this.$route.path === item.path
          this.breadList.push(item)
      })
    }
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  }
}
</script>

<style scoped>

</style>