<template>
  <div>
    <a-input-search
      v-model="this.selectedUserNames"
      placeholder="请先选择用户"
      disabled
      @search="onSearch"
      size="large">
      <a-button slot="enterButton">选择用户</a-button>
    </a-input-search>

    <j-search-user-by-dep-modal ref="JSearchUserByDepModal" @ok="modalFormOk"></j-search-user-by-dep-modal>
  </div>
</template>

<script>
  import JSearchUserByDepModal from '@/components/jeecgbiz/JSearchUserByDepModal'

  export default {
    name: 'JSearchUserByDepList',
    components: {
      JSearchUserByDepModal,
    },
    data() {
      return {
        selectedUserNames: '',
      }
    },
    methods: {
      onSearch() {
        this.$refs.JSearchUserByDepModal.showModal();
        this.selectedUserNames = '';
        this.$refs.JSearchUserByDepModal.title = '根据部门查询用户';
      },
      modalFormOk(selectedValue) {
        this.selectedUserNames = selectedValue;
      }
    }
  }
</script>
<style scoped>

</style>