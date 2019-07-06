<template>
  <a-tooltip placement="topLeft">
    <template slot="title">
      <span>{{value}}</span>
    </template>
    {{ value | ellipsis(length) }}
  </a-tooltip>
</template>

<script>
  export default {
    name: 'JEllipsis',
    props: {
      value: {
        type: String,
        required: false,
      },
      length: {
        type: Number,
        required: false,
        default: 25,
      }
    }
  }
</script>

<style scoped>

</style>
