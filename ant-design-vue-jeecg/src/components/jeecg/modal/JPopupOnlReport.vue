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
      :rowSelection="{type:rowSelectionType,fixed:true,selectedRowKeys: table.selectedRowKeys, onChange: handleChangeInTableSelect}"
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
  import md5 from 'md5'

  const MODAL_WIDTH = 1200;
  export default {
    name: 'JPopupOnlReport',
    props: ['multi', 'code', 'sorter', 'groupId', 'param'],
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
        tableScroll:{x:true},
        dynamicParam:{},
        // 排序字段，默认无排序
        iSorter: null,
      }
    },
    mounted() {
      //this.loadColumnsInfo()
    },
    watch: {
      code() {
        this.loadColumnsInfo()
      },
      param:{
        deep:true,
        handler(){
          // update--begin--autor:liusq-----date:20210706------for：JPopup组件在modal中使用报错#2729------
          if(this.visible){
            this.dynamicParamHandler()
            this.loadData();
          }
          // update--begin--autor:liusq-----date:20210706------for：JPopup组件在modal中使用报错#2729------
        },
      },
      sorter: {
        immediate: true,
        handler() {
          if (this.sorter) {
            let arr = this.sorter.split('=')
            if (arr.length === 2 && ['asc', 'desc'].includes(arr[1].toLowerCase())) {
              this.iSorter = {column: arr[0], order: arr[1].toLowerCase()}
              // 排序字段受控
              this.table.columns.forEach(col => {
                if (col.dataIndex === this.iSorter.column) {
                  this.$set(col, 'sortOrder', this.iSorter.order === 'asc' ? 'ascend' : 'descend')
                } else {
                  this.$set(col, 'sortOrder', false)
                }
              })
            } else {
              console.warn('【JPopup】sorter参数不合法')
            }
          }
        },
      },
    },
    computed:{
      showSearchFlag(){
        return this.queryInfo && this.queryInfo.length>0
      },
      // 行选择框类型，根据是否多选来控制显示为单选框还是多选框
      rowSelectionType() {
        return this.multi ? 'checkbox' : 'radio'
      },
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
              // 排序字段受控
              if (this.iSorter && currColumns[a].dataIndex === this.iSorter.column) {
                currColumns[a].sortOrder = this.iSorter.order === 'asc' ? 'ascend' : 'descend'
              }
            }
            this.table.columns = [...currColumns]
            this.initQueryInfo()
          } else {
            this.$error({
              title: '出错了',
              content: (<p>Popup初始化失败，请检查你的配置或稍后重试！<br/>错误信息如下：{res.message}</p>),
              onOk: () => this.close(),
            })
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
            this.dynamicParamHandler(res.result)
            this.queryInfo = res.result
            //查询条件加载后再请求数据
            this.loadData(1)
          } else {
            this.$message.warning(res.message)
          }
        })
      },
      //处理动态参数
      dynamicParamHandler(arr){
        if(arr && arr.length>0){
          //第一次加载查询条件前 初始化queryParam为空对象
          let queryTemp = {}
          for(let item of arr){
            if(item.mode==='single'){
              queryTemp[item.field] = ''
            }
          }
          this.queryParam = {...queryTemp}
        }
        let dynamicTemp = {}
        if(this.param){
          Object.keys(this.param).map(key=>{
            let str = this.param[key]
            if(key in this.queryParam){
              if(str && str.startsWith("'") && str.endsWith("'")){
                str = str.substring(1,str.length-1)
              }
              //如果查询条件包含参数 设置值
              this.queryParam[key]=str
            }
            dynamicTemp[key] = this.param[key]
          })
        }
        this.dynamicParam = {...dynamicTemp}
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
        let paramTarget = {}
        if(this.dynamicParam){
          //处理自定义参数
         Object.keys(this.dynamicParam).map(key=>{
           paramTarget['self_'+key] = this.dynamicParam[key]
         })
        }
        let param = Object.assign(paramTarget, this.queryParam, this.iSorter);
        param.pageNo = this.table.pagination.current;
        param.pageSize = this.table.pagination.pageSize;
        return filterObj(param);
      },
      handleChangeInTableSelect(selectedRowKeys, selectionRows) {
        //update-begin-author:taoyan date:2020902 for:【issue】开源online的几个问题 LOWCOD-844
        if(!selectedRowKeys || selectedRowKeys.length==0){
          this.table.selectionRows = []
        }else if(selectedRowKeys.length == selectionRows.length){
          this.table.selectionRows = selectionRows
        }else{
          //当两者长度不一的时候 需要判断
          let keys = this.table.selectedRowKeys
          let rows = this.table.selectionRows;
          //这个循环 添加新的记录
          for(let i=0;i<selectionRows.length;i++){
            let combineKey = this.combineRowKey(selectionRows[i])
            if(keys.indexOf(combineKey)<0){
              //如果 原来的key 不包含当前记录 push
              rows.push(selectionRows[i])
            }
          }
          //这个循环 移除取消选中的数据
          this.table.selectionRows = rows.filter(item=>{
            let combineKey = this.combineRowKey(item)
            return selectedRowKeys.indexOf(combineKey)>=0
          })
        }
        //update-end-author:taoyan date:2020902 for:【issue】开源online的几个问题 LOWCOD-844
        this.table.selectedRowKeys = selectedRowKeys
      },
      handleChangeInTable(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        if (Object.keys(sorter).length > 0) {
          this.iSorter = {
            column: sorter.field,
            order: 'ascend' === sorter.order ? 'asc' : 'desc'
          }
          // 排序字段受控
          this.table.columns.forEach(col => {
            if (col.dataIndex === sorter.field) {
              this.$set(col, 'sortOrder',sorter.order)
            } else {
              this.$set(col, 'sortOrder', false)
            }
          })
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
        this.visible = true
        this.loadColumnsInfo()
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
           //update-begin---author:liusq   Date:20210203  for：pop选择器列主键问题 issues/I29P9Q------------
           if(key=='id'){
             res=record[key]+res
           }else{
             res+=record[key]
           }
           //update-end---author:liusq     Date:20210203  for：pop选择器列主键问题 issues/I29P9Q------------
         })
        // update-begin---author:taoyan   Date:20211025 for：jpopup 表格key重复BUG /issues/3121
        res = md5(res)
        /*if(res.length>50){
          res = res.substring(0,50)
        }*/
        // update-end---author:taoyan   Date:20211025 for：jpopup 表格key重复BUG /issues/3121
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
              // 判断是否允许多选，如果不允许多选，就只存储最后一个选中的行
              if (!this.multi && this.table.selectedRowKeys.length > 1) {
                this.table.selectionRows = [this.table.selectionRows.pop()]
                this.table.selectedRowKeys = [this.table.selectedRowKeys.pop()]
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