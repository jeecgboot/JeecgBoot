<template>
  <global-layout>
    <transition name="page-transition">
      <keep-alive v-if="keepAlive">
        <router-view />
      </keep-alive>
      <router-view v-else />
    </transition>
  </global-layout>
</template>

<script>
  import GlobalLayout from '@/components/page/GlobalLayout'

  export default {
    name: "BasicLayout",
    components: {
      GlobalLayout
    },
    data () {
      return {

      }
    },
    computed: {
      keepAlive () {
        return this.$route.meta.keepAlive
      }
    },
    methods: {

    },
  }
</script>

<style lang="scss">

  /*
 * The following styles are auto-applied to elements with
 * transition="page-transition" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the page transition by editing
 * these styles.
 */

  .page-transition-enter {
    opacity: 0;
  }

  .page-transition-leave-active {
    opacity: 0;
  }

  .page-transition-enter .page-transition-container,
  .page-transition-leave-active .page-transition-container {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
  }
</style>