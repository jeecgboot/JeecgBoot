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

    <j-search-user-by-dep ref="JSearchUserByDep" @ok="modalFormOk"></j-search-user-by-dep>
  </div>
</template>

<script>
  import JSearchUserByDep from '@/components/jeecgbiz/JSearchUserByDep'

  export default {
    name: 'SearchUserByDepList',
    components: {
      JSearchUserByDep,
    },
    data() {
      return {
        selectedUserNames: '',
      }
    },
    methods: {
      onSearch() {
        this.$refs.JSearchUserByDep.showModal();
        this.selectedUserNames = '';
        this.$refs.JSearchUserByDep.title = '根据部门查询用户';
      },
      modalFormOk(selectedValue) {
        this.selectedUserNames = selectedValue;
      }
    }
  }
</script>
<style scoped>

</style>