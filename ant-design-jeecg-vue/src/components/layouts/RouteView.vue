<template>
  <keep-alive v-if="keepAlive">
    <router-view />
  </keep-alive>
  <router-view v-else />
</template>

<script>
  export default {
    name: "RouteView",
    computed: {
      keepAlive () {
        return this.$route.meta.keepAlive
      }
    },
  }
</script>