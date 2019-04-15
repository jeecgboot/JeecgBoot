<template>
  <div>
    <a-modal
      :title="title"
      :width="800"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleSubmit"
      @cancel="handleCancel">
      <div>
        <a-form-item label="用户名：" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-input-search placeholder="点击右侧按钮选择用户" disabled @search="onSearch" v-model="userNames">
            <a-button slot="enterButton" icon="search">选择</a-button>
          </a-input-search>
        </a-form-item>
      </div>
    </a-modal>

    <!-- 用户查询列表 -->
    <select-user-list-modal ref="selectUserListModal" @ok="getUserCallBack"></select-user-list-modal>
  </div>
</template>

<script>
  import {getUserList} from '@/api/api'
  import SelectUserListModal from './modal/SelectUserListModal'

  export default {
    name: "SelectUserModal",
    components: {
      SelectUserListModal,
    },
    props: ['taskId'],
    data() {
      return {
        title: "操作",
        visible: false,
        selectUserListVisible: false,
        model: {},
        confirmLoading: false,
        userNames: '',
        userKeys: '',
      }
    },
    created() {
    },
    methods: {
      open() {
        this.userNames = ''
        this.userKeys = ''
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel() {
        this.close()
      },
      onSearch() {
        this.$refs.selectUserListModal.open();
      },
      getUserCallBack(selectionRows) {
        console.log(selectionRows)
        let names = ''
        let keys = ''
        for (let row of selectionRows) {
          names = row.realname + "," + names
          keys = row.username + "," + keys
        }
        this.userNames = names
        this.userKeys = keys

        console.log('--userkeys--' + this.userKeys)
      },
      handleSubmit() {
        console.log("taskId: "+ this.taskId)
        this.$emit('ok', this.userKeys,this.taskId);
        this.close()
      },
    }
  }
</script>

<style>

</style>
