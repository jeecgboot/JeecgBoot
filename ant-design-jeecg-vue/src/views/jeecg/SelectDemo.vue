<template>
  <a-card :bordered="false" style="height:100%">

    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <!--  选择多个用户，可排序 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="人员列表">
              <a-select
                mode="multiple"
                placeholder="Please select"
                :value=nameList
                @change="handleChange"
              >
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="handleSelect" icon="search">选择</a-button>
              <a-button type="primary" @click="selectReset" icon="reload" style="margin-left: 8px">清空</a-button>
            </span>
          </a-col>
        </a-row>

        <!--  通过部门筛选，选择人 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="人员列表">
              <a-input-search
                v-model="this.selectedDepUsers"
                placeholder="请先选择用户"
                disabled
                @search="onSearchDepUser"
                size="large">
                <a-button slot="enterButton">选择用户</a-button>
              </a-input-search>
            </a-form-item>
          </a-col>
        </a-row>

      </a-form>
    </div>

    <!-- 选择多个用户支持排序 -->
    <select-multiple-user-modal ref="selectDemoModal" @selectFinished="selectOK"></select-multiple-user-modal>
    <!-- 通过部门筛选，选择人 -->
    <search-user-by-dep-modal ref="SearchUserByDepModal" @ok="onSearchDepUserCallBack"></search-user-by-dep-modal>
  </a-card>
</template>

<script>
  import SelectMultipleUserModal from '@/components/jeecgbiz/SelectMultipleUserModal'
  import SearchUserByDepModal from '@/components/jeecgbiz/SearchUserByDepModal'

  export default {
    name: "SelectDemo",
    components: {
      SelectMultipleUserModal,
      SearchUserByDepModal
    },
    data() {
      return {
        selectList: [],
        selectedDepUsers: ''
      }
    },
    computed: {
      nameList: function () {
        var names = [];
        for (var a = 0; a < this.selectList.length; a++) {
          names.push(this.selectList[a].name);
        }
        return names;
      }
    },
    methods: {
      handleChange() {
      },
      selectOK: function (data) {
        this.selectList = data;
      },
      handleSelect: function () {
        this.$refs.selectDemoModal.add();
      },
      selectReset() {
        this.selectList = [];
      },
      //通过组织机构筛选选择用户
      onSearchDepUser() {
        this.$refs.SearchUserByDepModal.showModal()
        this.selectedDepUsers = ''
        this.$refs.SearchUserByDepModal.selectedKeys = []
        this.$refs.SearchUserByDepModal.title = '根据部门查询用户'
      },
      onSearchDepUserCallBack(selectedDepUsers) {
        this.selectedDepUsers = selectedDepUsers
      }
    }
  }
</script>
<style lang="less" scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
</style>