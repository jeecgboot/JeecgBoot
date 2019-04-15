<template>
  <a-card :bordered="false" style="height: 100%">

    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24" v-if="queryInfo && queryInfo.length>0">
          <template v-for="(item,index) in queryInfo">
            <template v-if=" item.hidden==='1' ">
              <a-col :md="6" :sm="8" :key=" 'query'+index " v-show="toggleSearchStatus">
                <onl-cgreport-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></onl-cgreport-query-form-item>
              </a-col>
            </template>
            <template v-else>
              <a-col :md="6" :sm="8" :key=" 'query'+index ">
                <onl-cgreport-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></onl-cgreport-query-form-item>
              </a-col>
            </template>
          </template>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchByQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>


    <div class="table-operator" style="margin-bottom: 10px">
      <a-button type="primary" icon="plus" @click="exportExcel">导出</a-button>
    </div>


    <a-table
      ref="table"
      size="middle"
      bordered
      rowKey="id"
      :columns="table.columns"
      :dataSource="table.dataSource"
      :pagination="table.pagination"
      :loading="table.loading"
      :rowSelection="{selectedRowKeys: table.selectedRowKeys, onChange: handleChangeInTableSelect}"
      @change="handleChangeInTable"
      style="min-height: 300px"
    >

    </a-table>

  </a-card>
</template>

<script>
  import {getAction} from '@/api/manage'
  import OnlCgreportQueryFormItem from './OnlCgreportQueryFormItem.vue';
  import {filterObj} from '@/utils/util';

  export default {
    name: 'OnlCgreportAutoList',
    components: {
      OnlCgreportQueryFormItem
    },
    data() {
      return {
        // 查询参数
        queryInfo: [],
        queryParam: {},
        sorter: {
          column: 'createTime',
          order: 'desc',
        },
        dictOptions: {},
        toggleSearchStatus: false, // 高级搜索 展开/关闭
        reportCode: '',
        description: '在线报表功能测试页面',
        url: {
          getColumns: '/online/cgreport/api/getColumns/',
          getData: '/online/cgreport/api/getData/',
          getQueryInfo: '/online/cgreport/api/getQueryInfo/'
        },
        table: {
          loading: true,
          // 表头
          columns: [],
          //数据集
          dataSource: [],
          // 选择器
          selectedRowKeys: [],
          selectionRows: [],
          // 分页参数
          pagination: {
            current: 1,
            pageSize: 10,
            pageSizeOptions: ['10', '20', '30'],
            showTotal: (total, range) => {
              return range[0] + '-' + range[1] + ' 共' + total + '条'
            },
            showQuickJumper: true,
            showSizeChanger: true,
            total: 0
          }
        }
      }
    },
    mounted() {
      this.loadData()
      this.initQueryInfo();
    },
    watch: {
      '$route'(to, from) {
        // 刷新参数放到这里去触发，就可以刷新相同界面了
        this.loadData()
        this.initQueryInfo();
      }
    },
    methods: {
      initQueryInfo() {
        getAction(`${this.url.getQueryInfo}${this.reportCode}`).then((res) => {
          console.log("获取查询条件", res);
          if (res.success) {
            this.queryInfo = res.result
          } else {
            this.$message.warning(res.message)
          }
        })
      },
      loadData(arg) {
        if (arg == 1) {
          this.table.pagination.current = 1
        }
        let params = this.getQueryParams();//查询条件
        console.log(params)

        //获取报表ID
        this.reportCode = this.$route.params.code;
        console.log(' 动态报表 reportCode ： ' + this.reportCode);
        this.table.loading = true
        Promise.all([
          getAction(`${this.url.getColumns}${this.reportCode}`),
          getAction(`${this.url.getData}${this.reportCode}`, params)
        ]).then(results => {
          let [{result: {columns}}, {result: data}] = results
          this.table.columns = columns
          if (data) {
            this.table.pagination.total = Number(data.total)
            this.table.dataSource = data.records
          } else {
            this.table.pagination.total = 0
            this.table.dataSource = []
          }
        }).catch((e) => {
          console.error(e)
          this.$message.error('查询失败')
        }).then(() => {
          this.table.loading = false
        })
      },
      getQueryParams() {
        let param = Object.assign({}, this.queryParam, this.sorter);
        param.pageNo = this.table.pagination.current;
        param.pageSize = this.table.pagination.pageSize;
        return filterObj(param);
      },
      searchByQuery() {
        this.loadData(1);
      },
      searchReset() {
        this.queryParam = {}
        this.loadData(1);
      },
      handleToggleSearch() {
        this.toggleSearchStatus = !this.toggleSearchStatus;
      },
      exportExcel() {
        let url = `${window._CONFIG['domianURL']}/online/cgreport/api/exportXls/${this.reportCode}`;
        window.location.href = url;
      },
      handleChangeInTableSelect(selectedRowKeys, selectionRows) {
        this.table.selectedRowKeys = selectedRowKeys
        this.table.selectionRows = selectionRows
      },
      handleChangeInTable(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        if (Object.keys(sorter).length > 0) {
          this.sorter.column = sorter.field
          this.sorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.table.pagination = pagination
        this.loadData()
      }

    }
  }
</script>
<style scoped>
  .div {
    display: flex;
    align-items: center;
    height: 500px
  }

</style>