<template>
  <tooltip v-if="tips !== ''">
    <template slot="title">{{ tips }}</template>
    <avatar :size="avatarSize" :src="src" />
  </tooltip>
  <avatar v-else :size="avatarSize" :src="src" />
</template>

<script>
  import Avatar from 'ant-design-vue/es/avatar'
  import Tooltip from 'ant-design-vue/es/tooltip'

  export default {
    name: "AvatarItem",
    components: {
      Avatar,
      Tooltip
    },
    props: {
      tips: {
        type: String,
        default: '',
        required: false
      },
      src: {
        type: String,
        default: ''
      }
    },
    data () {
      return {
        size: this.$parent.size
      }
    },
    computed: {
      avatarSize () {
        return this.size !== 'mini' && this.size || 20
      }
    },
    watch: {
      '$parent.size' (val) {
        this.size = val
      }
    }
  }
</script>