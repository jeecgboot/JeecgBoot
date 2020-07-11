<template>
  <j-modal
    :title="title"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    wrapClassName="j-popup-modal"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭">

    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchByquery">
        <a-row :gutter="24" v-if="showSearchFlag">
          <template v-for="(item,index) in queryInfo">
            <template v-if=" item.hidden==='1' ">
              <a-col :md="8" :sm="24" :key=" 'query'+index " v-show="toggleSearchStatus">
                <online-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></online-query-form-item>
              </a-col>
            </template>
            <template v-else>
              <a-col :md="8" :sm="24" :key=" 'query'+index ">
                <online-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></online-query-form-item>
              </a-col>
            </template>
          </template>

          <a-col :md="8" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchByquery" icon="search">查询</a-button>
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

    <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
      <i class="anticon anticon-info-circle ant-alert-icon"></i>
      已选择&nbsp;<a style="font-weight: 600">{{ table.selectedRowKeys.length }}</a>项&nbsp;&nbsp;
      <a style="margin-left: 24px" @click="onClearSelected">清空</a>

      <a v-if="!showSearchFlag" style="margin-left: 24px" @click="onlyReload">刷新</a>
    </div>

    <a-table
      ref="table"
      size="middle"
      bordered
      :rowKey="combineRowKey"
      :columns="table.columns"
      :dataSource="table.dataSource"
      :pagination="table.pagination"
      :loading="table.loading"
      :rowSelection="{fixed:true,selectedRowKeys: table.selectedRowKeys, onChange: handleChangeInTableSelect}"
      @change="handleChangeInTable"
      style="min-height: 300px"
      :scroll="tableScroll"
      :customRow="clickThenCheck">
    </a-table>


  </j-modal>
</template>

<script>
  import { getAction } from '@/api/manage'
  import {filterObj} from '@/utils/util'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
  import { httpGroupRequest } from '@/api/GroupRequest.js'

  const MODAL_WIDTH = 1200;
  export default {
    name: 'JPopupOnlReport',
    props: ['multi', 'code', 'groupId'],
    components:{
    },
    data(){
      return {
        visible:false,
        title:"",
        confirmLoading:false,
        queryInfo:[],
        toggleSearchStatus:false,
        queryParam:{

        },
        dictOptions: {},
        url: {
          getColumns: '/online/cgreport/api/getRpColumns/',
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
        },
        cgRpConfigId:"",
        modalWidth:MODAL_WIDTH,
        tableScroll:{x:MODAL_WIDTH-100}

      }
    },
    mounted() {
      this.loadColumnsInfo()
    },
    watch: {
      code() {
        this.loadColumnsInfo()
      }
    },
    computed:{
      showSearchFlag(){
        return this.queryInfo && this.queryInfo.length>0
      }
    },
    methods:{
      loadColumnsInfo(){
        let url = `${this.url.getColumns}${this.code}`
        //缓存key
        let groupIdKey
        if (this.groupId) {
          groupIdKey = this.groupId + url
        }
        httpGroupRequest(() => getAction(url), groupIdKey).then(res => {
          if(res.success){
            this.initDictOptionData(res.result.dictOptions);
            this.cgRpConfigId = res.result.cgRpConfigId
            this.title = res.result.cgRpConfigName
            let currColumns = res.result.columns
            for(let a=0;a<currColumns.length;a++){
              if(currColumns[a].customRender){
                let dictCode = currColumns[a].customRender;
                currColumns[a].customRender=(text)=>{
                  return filterMultiDictText(this.dictOptions[dictCode], text+"");
                }
              }
            }
            this.table.columns = [...currColumns]
            this.initQueryInfo()
            this.loadData(1)
          }
        })
      },
      initQueryInfo() {
        let url = `${this.url.getQueryInfo}${this.cgRpConfigId}`
        //缓存key
        let groupIdKey
        if (this.groupId) {
          groupIdKey = this.groupId + url
        }
        httpGroupRequest(() => getAction(url), groupIdKey).then((res) => {
          // console.log("获取查询条件", res);
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
        this.table.loading = true
        let url = `${this.url.getData}${this.cgRpConfigId}`
        //缓存key
        let groupIdKey
        if (this.groupId) {
          groupIdKey = this.groupId + url + JSON.stringify(params)
        }
        httpGroupRequest(() => getAction(url, params), groupIdKey).then(res => {
          this.table.loading = false
          // console.log("daa",res)
          let data = res.result
          if (data) {
            this.table.pagination.total = Number(data.total)
            this.table.dataSource = data.records
          } else {
            this.table.pagination.total = 0
            this.table.dataSource = []
          }
        })
      },
      getQueryParams() {
        let param = Object.assign({}, this.queryParam, this.sorter);
        param.pageNo = this.table.pagination.current;
        param.pageSize = this.table.pagination.pageSize;
        return filterObj(param);
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
      },
      handleCancel() {
        this.close()
      },
      handleSubmit() {
        if(!this.multi){
          if(this.table.selectionRows && this.table.selectionRows.length>1){
            this.$message.warning("请选择一条记录")
            return false
          }
        }
        if(!this.table.selectionRows || this.table.selectionRows.length==0){
          this.$message.warning("请选择一条记录")
          return false
        }
        this.$emit('ok', this.table.selectionRows);
        this.close()
      },
      close() {
        this.$emit('close');
        this.visible = false;
        this.onClearSelected()
      },
      show(){
        this.visible = true;
      },
      handleToggleSearch(){
        this.toggleSearchStatus = !this.toggleSearchStatus;
      },
      searchByquery(){
        this.loadData(1);
      },
      onlyReload(){
        this.loadData();
      },
      searchReset(){
        Object.keys(this.queryParam).forEach(key=>{
          this.queryParam[key]=""
        })
        this.loadData(1);
      },
      onClearSelected(){
        this.table.selectedRowKeys = []
        this.table.selectionRows = []
      },
      combineRowKey(record){
        let res = ''
         Object.keys(record).forEach(key=>{
           res+=record[key]
         })
        if(res.length>50){
          res = res.substring(0,50)
        }
        return res
      },

      clickThenCheck(record){
        return {
          on: {
            click: () => {
              let rowKey = this.combineRowKey(record)
              if(!this.table.selectedRowKeys || this.table.selectedRowKeys.length==0){
                let arr1=[],arr2=[]
                arr1.push(record)
                arr2.push(rowKey)
                this.table.selectedRowKeys=arr2
                this.table.selectionRows=arr1
              }else{
                if(this.table.selectedRowKeys.indexOf(rowKey)<0){
                  this.table.selectedRowKeys.push(rowKey)
                  this.table.selectionRows.push(record)
                }else{
                  let rowKey_index = this.table.selectedRowKeys.indexOf(rowKey)
                  this.table.selectedRowKeys.splice(rowKey_index,1);
                  this.table.selectionRows.splice(rowKey_index,1);
                }
              }
            }
          }
        }
      },
      //防止字典中有垃圾数据
      initDictOptionData(dictOptions){
        let obj = { }
        Object.keys(dictOptions).map(k=>{
          obj[k] = dictOptions[k].filter(item=>{
            return item!=null
          });
        });
        this.dictOptions  = obj
      }

    }
  }
</script>

<style scoped>

</style>