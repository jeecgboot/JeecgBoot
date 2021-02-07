<template>
  <a-modal
    :width="1000"
    :title="title"
    :visible="innerVisible"
    @cancel="handleCancel"
    cancelText="关闭"
    :okButtonProps="{style:{display:'none'}}"
  >
    <a-alert type="info" showIcon style="margin-bottom: 16px;">
      <template slot="message">
        <span>已选择</span>
        <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
        <span>项</span>
        <template v-if="selectedRowKeys.length>0">
          <a-divider type="vertical"/>
          <a @click="handleClearSelection">清空选择</a>
          <a-divider type="vertical"/>
          <a @click="handleRevertBatch">批量还原</a>
          <a-divider type="vertical"/>
          <a @click="handleDeleteBatch">批量删除</a>
        </template>
      </template>
    </a-alert>

    <a-table
      ref="table"
      rowKey="id"
      size="middle"
      bordered
      :columns="columns"
      :loading="loading"
      :dataSource="dataSource"
      :pagination="false"
      :rowSelection="{selectedRowKeys, onChange: handleTableSelectChange}"
    >

      <!-- 显示头像 -->
      <template slot="avatarslot" slot-scope="text, record, index">
        <div class="anty-img-wrap">
          <a-avatar shape="square" :src="url.getAvatar(record.avatar)" icon="user"/>
        </div>
      </template>

      <span slot="action" slot-scope="text, record">
        <a @click="handleRevert([record.id])"><a-icon type="redo"/> 还原用户</a>
        <a-divider type="vertical"/>
        <a @click="handleDelete([record.id])"><a-icon type="delete"/> 彻底删除</a>
      </span>
    </a-table>

  </a-modal>
</template>

<script>
  import { putAction,deleteAction,getFileAccessHttpUrl } from "@/api/manage"

  // 高度封装的请求，请务必使用 superRequest.call(this,{}) 的方式调用
  function superRequest(options) {
    this.loading = !!options.loading
    options.promise.then(res => {
      if (res.success && typeof options.success === 'function') {
        options.success(res)
      } else {
        throw new Error(res.message)
      }
    }).catch(e => {
      console.error('查询已删除的用户失败：', e)
      this.$message.warning('查询已删除的用户失败：' + (e.message || e))
    }).finally(() => {
      this.loading = false
    })
  }

  export default {
    name: 'UserRecycleBinModal',
    props: {
      visible: {
        type: Boolean,
        default: false
      },
    },
    data() {
      return {
        title: '用户回收站',
        loading: false,
        innerVisible: false,
        selectedRowKeys: [],
        dataSource: [],
        columns: [
          { title: '#', align: 'center', key: 'rowIndex', width: 80, customRender: (t, r, i) => i + 1 },
          { title: '账号', align: 'center', dataIndex: 'username' },
          { title: '姓名', align: 'center', dataIndex: 'realname', },
          { title: '头像', align: 'center', dataIndex: 'avatar', scopedSlots: { customRender: 'avatarslot' } },
          { title: '部门', align: 'center', dataIndex: 'orgCode' },
          { title: '操作', align: 'center', dataIndex: 'action', width: 200, scopedSlots: { customRender: 'action' } }
        ],
        url: {
          getAvatar: (path) => getFileAccessHttpUrl(`${path}`),
          // 回收站操作，get = 获取列表；put = 取回；delete = 彻底删除
          recycleBin: '/sys/user/recycleBin',
          putRecycleBin: '/sys/user/putRecycleBin',
          deleteRecycleBin: '/sys/user/deleteRecycleBin',
        },
      }
    },
    watch: {
      visible: {
        immediate: true,
        handler(val) {
          if (val) {
            this.loadData()
          }
          this.innerVisible = val
        }
      },
      innerVisible(val) {
        this.$emit('update:visible', val)
      },
    },
    methods: {
      loadData() {
        superRequest.call(this, {
          loading: true,
          promise: this.$http.get(this.url.recycleBin),
          success: res => this.dataSource = res.result
        })
      },
      handleOk() {
        this.loadData()
        this.$emit('ok')
      },
      handleCancel() {
        this.innerVisible = false
      },
      // 还原用户
      handleRevert(userIds) {
        this.$confirm({
          title: '恢复用户',
          content: `您确定要恢复这 ${userIds.length} 个用户吗？`,
          centered: true,
          onOk: () => {
            putAction(this.url.putRecycleBin,{userIds:userIds.join(',')}).then((res)=>{
              if(res.success){
                this.handleOk()
                this.handleClearSelection()
                this.$message.success(`还原 ${userIds.length} 个用户成功！`)
              }
            })
          }
        })
      },
      // 彻底删除用户
      handleDelete(userIds) {
        this.$confirm({
          title: '彻底删除用户',
          content: (<div>
            <p>您确定要彻底删除这 {userIds.length} 个用户吗？</p>
            <p style="color:red;">注意：彻底删除后将无法恢复，请谨慎操作！</p>
          </div>),
          centered: true,
          onOk: () => {
            var that = this;
            deleteAction(that.url.deleteRecycleBin, {userIds: userIds.join(',')}).then((res) => {
              if (res.success) {
                this.loadData()
                this.handleClearSelection()
                this.$message.success(`彻底删除 ${userIds.length} 个用户成功！`)
              } else {
                that.$message.warning(res.message);
              }
            });
          },
        })
      },
      handleRevertBatch() {
        this.handleRevert(this.selectedRowKeys)
      },
      handleDeleteBatch() {
        this.handleDelete(this.selectedRowKeys)
      },
      handleClearSelection() {
        this.handleTableSelectChange([], [])
      },
      handleTableSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys
        this.selectionRows = selectionRows
      },
    }
  }
</script>

<style lang="less" scoped></style>