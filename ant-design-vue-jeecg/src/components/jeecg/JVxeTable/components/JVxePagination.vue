<template>
  <div :class="boxClass">
    <a-pagination
      :disabled="disabled"
      v-bind="bindProps"
      @change="handleChange"
      @showSizeChange="handleShowSizeChange"
    />
  </div>
</template>

<script>
  import PropTypes from 'ant-design-vue/es/_util/vue-types'

  export default {
    name: 'JVxePagination',
    props: {
      size: String,
      disabled: PropTypes.bool,
      pagination: PropTypes.object.def({}),
    },
    data() {
      return {
        defaultPagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共 ' + total + ' 条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 100
        }
      }
    },
    computed: {
      bindProps() {
        return {
          ...this.defaultPagination,
          ...this.pagination,
          size: this.size === 'tiny' ? 'small' : ''
        }
      },
      boxClass() {
        return {
          'j-vxe-pagination': true,
          'show-quick-jumper': !!this.bindProps.showQuickJumper
        }
      },
    },
    methods: {
      handleChange(current, pageSize) {
        this.$set(this.pagination, 'current', current)
        this.$emit('change', {current, pageSize})
      },
      handleShowSizeChange(current, pageSize) {
        this.$set(this.pagination, 'pageSize', pageSize)
        this.$emit('change', {current, pageSize})
      },
    },
  }
</script>

<style lang="less" scoped>

</style>