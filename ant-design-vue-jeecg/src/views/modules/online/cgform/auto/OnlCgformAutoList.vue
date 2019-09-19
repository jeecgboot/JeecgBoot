<template>
  <a-card :bordered="false" style="height: 100%">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24" v-if="queryInfo && queryInfo.length>0">
          <template v-for="(item,index) in queryInfo">
            <template v-if=" item.hidden==='1' ">
              <a-col v-if="item.view=='datetime'" :md="12" :sm="16" :key=" 'query'+index " v-show="toggleSearchStatus">
                <online-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></online-query-form-item>
              </a-col>
              <a-col v-else :md="6" :sm="8" :key=" 'query'+index " v-show="toggleSearchStatus">
                <online-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></online-query-form-item>
              </a-col>
            </template>
            <template v-else>
              <a-col v-if="item.view=='datetime'" :md="12" :sm="16" :key=" 'query'+index ">
                <online-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></online-query-form-item>
              </a-col>
              <a-col v-else :md="6" :sm="8" :key=" 'query'+index ">
                <online-query-form-item :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></online-query-form-item>
              </a-col>
            </template>
          </template>

          <a-col :md="6" :sm="8">
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

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-if="buttonSwitch.add" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button v-if="buttonSwitch.import" @click="handleImportXls" type="primary" icon="upload" style="margin-left:8px">导入</a-button>
      <a-button v-if="buttonSwitch.export" @click="handleExportXls" type="primary" icon="download" style="margin-left:8px">导出</a-button>
      <template v-if="cgButtonList && cgButtonList.length>0" v-for="(item,index) in cgButtonList">
        <a-button
          v-if=" item.optType=='js' "
          :key=" 'cgbtn'+index "
          @click="cgButtonJsHandler(item.buttonCode)"
          type="primary"
          :icon="item.buttonIcon"
          style="margin-left:8px">
          {{ item.buttonName }}
        </a-button>
        <a-button
          v-else-if=" item.optType=='action' "
          :key=" 'cgbtn'+index "
          @click="cgButtonActionHandler(item.buttonCode)"
          type="primary"
          :icon="item.buttonIcon"
          style="margin-left:8px">
          {{ item.buttonName }}
        </a-button>
      </template>

      <a-button
        v-if="buttonSwitch.batch_delete"
        @click="handleDelBatch"
        style="margin-left:8px"
        v-show="table.selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除</a-button>
    </div>

    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        已选择&nbsp;<a style="font-weight: 600">{{ table.selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="cgformAutoList"
        bordered
        size="middle"
        rowKey="id"
        :columns="table.columns"
        :dataSource="table.dataSource"
        :pagination="table.pagination"
        :loading="table.loading"
        :rowSelection="{selectedRowKeys:table.selectedRowKeys, onChange: handleChangeInTableSelect}"
        @change="handleTableChange"
        style="min-height: 300px">

        <template slot="dateSlot" slot-scope="text">
          <span>{{ getFormatDate(text) }}</span>
        </template>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>

        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>

        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <template v-if="hasBpmStatus">
            <template v-if="record.bpm_status == '1'||record.bpm_status == ''|| record.bpm_status == null">
              <template v-if="buttonSwitch.update">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical"/>
            </template>
            </template>
          </template>
          <template v-else>
            <template v-if="buttonSwitch.update">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical"/>
            </template>
          </template>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item >
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <template v-if="hasBpmStatus">
              </template>
              <template v-else>
                <a-menu-item v-if="buttonSwitch.delete">
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteOne(record)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
               </template>
              <template v-if="cgButtonLinkList && cgButtonLinkList.length>0" v-for="(btnItem,btnIndex) in cgButtonLinkList">
                <a-menu-item :key=" 'cgbtnLink'+btnIndex ">
                  <a href="javascript:void(0);" @click="cgButtonLinkHandler(record,btnItem.buttonCode,btnItem.optType)">
                    <a-icon v-if="btnItem.buttonIcon" :type="btnItem.buttonIcon" />
                    {{ btnItem.buttonName }}
                  </a>
                </a-menu-item>
              </template>

            </a-menu>
          </a-dropdown>
        </span>
      </a-table>

      <OnlCgformAutoModal @success="handleFormSuccess" ref="modal" :code="code"></OnlCgformAutoModal>

      <j-import-modal ref="importModal" :url="getImportUrl()" @ok="importOk"></j-import-modal>

    </div>
  </a-card>
</template>

<script>

  import { postAction,getAction,deleteAction,downFile } from '@/api/manage'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';
  import JImportModal from '@/components/jeecg/JImportModal'

  export default {
    name: 'OnlCgFormAutoList',
    components: {
      JImportModal
    },
    data() {
      return {
        code: '',
        description: '在线报表功能测试页面',
        currentTableName:"",
        url: {
          getQueryInfo:'/online/cgform/api/getQueryInfo/',
          getColumns: '/online/cgform/api/getColumns/',
          getData: '/online/cgform/api/getData/',
          optPre:"/online/cgform/api/form/",
          exportXls:'/online/cgform/api/exportXls/',
          buttonAction:'/online/cgform/api/doButton',
        },
        flowCodePre:"onl_",
        isorter:{
          column: 'createTime',
          order: 'desc',
        },
        //dictOptions:{fieldName:[]}
        dictOptions:{

        },
        cgButtonLinkList:[],
        cgButtonList:[],
        queryInfo:[],
        queryParam:{

        },
        toggleSearchStatus:false,
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
        actionColumn:{
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          fixed:"right",
          align:"center",
          width:150
        },
        formTemplate:"99",
        EnhanceJS:'',
        hideColumns:[],
        buttonSwitch:{
          add:true,
          update:true,
          delete:true,
          batch_delete:true,
          import:true,
          export:true
        },
        hasBpmStatus:false,
      }
    },
    created() {
      this.initAutoList();
    },
    mounted(){
      this.cgButtonJsHandler('mounted')
    },
    watch: {
      '$route'() {
        // 刷新参数放到这里去触发，就可以刷新相同界面了
        this.initAutoList()
      }
    },
    methods: {
      hasBpmStatusFilter(){
        var columnObjs = this.table.columns;
        let columns = [];
        for (var item of columnObjs) {
          columns.push(item.dataIndex);
        }
        if(columns.includes('bpm_status')||columns.includes('BPM_STATUS')){
          this.hasBpmStatus = true;
        }else{
          this.hasBpmStatus = false;
        }
      },
      initQueryInfo(){
        getAction(`${this.url.getQueryInfo}${this.code}`).then((res)=>{
          console.log("--onlineList-获取查询条件配置",res);
          if(res.success){
            this.queryInfo = res.result
          }else{
            this.$message.warning(res.message)
          }
        })
      },
      initAutoList(){
        if(!this.$route.params.code){
          return false
        }
        this.table.loading = true
        this.code = this.$route.params.code
        getAction(`${this.url.getColumns}${this.code}`).then((res)=>{
          console.log("--onlineList-加载动态列>>",res);
          if(res.success){
            this.dictOptions = res.result.dictOptions
            this.formTemplate = res.result.formTemplate
            this.description = res.result.description
            this.currentTableName = res.result.currentTableName
            this.initCgButtonList(res.result.cgButtonList)
            this.initCgEnhanceJs(res.result.enhanceJs)
            this.initButtonSwitch(res.result.hideColumns)
            let currColumns = res.result.columns
            for(let a=0;a<currColumns.length;a++){
              if(currColumns[a].customRender){
                let dictCode = currColumns[a].customRender;
                currColumns[a].customRender=(text)=>{
                  return filterMultiDictText(this.dictOptions[dictCode], text);
                }
              }
            }
            currColumns.push(this.actionColumn);
            this.table.columns = [...currColumns]
            this.hasBpmStatusFilter();
            this.loadData();
            this.initQueryInfo();
          }else{
            this.$message.warning(res.message)
          }
        })
      },
      loadData(arg){
        if(arg==1){
          this.table.pagination.current=1
        }
        let params = this.getQueryParams();//查询条件
        console.log("--onlineList-查询条件-->",params)
        getAction(`${this.url.getData}${this.code}`,params).then((res)=>{
          console.log("--onlineList-列表数据",res)
          if(res.success){
            let result = res.result;
            if(Number(result.total)>0){
              this.table.pagination.total = Number(result.total)
              this.table.dataSource = result.records
            }else{
              this.table.pagination.total=0;
              this.table.dataSource=[]
              //this.$message.warning("查无数据")
            }
          }else{
            this.$message.warning(res.message)
          }
          this.table.loading = false
        })
      },
      getQueryParams() {
        let param = Object.assign({}, this.queryParam,this.isorter);
        param.pageNo = this.table.pagination.current;
        param.pageSize = this.table.pagination.pageSize;
        return filterObj(param);
      },
      handleChangeInTableSelect(selectedRowKeys, selectionRows) {
        this.table.selectedRowKeys = selectedRowKeys
        this.table.selectionRows = selectionRows
      },
      handleTableChange(pagination, filters, sorter){
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.table.pagination = pagination;
        this.loadData();
      },
      handleAdd(){
        this.cgButtonJsHandler('beforeAdd')
        this.$refs.modal.add(this.formTemplate);
      },
      handleImportXls(){
        this.$refs.importModal.show()
      },
      importOk(){
        this.loadData(1)
      },
      handleExportXls2(){
        let param = this.queryParam;
        if(this.table.selectedRowKeys && this.table.selectedRowKeys.length>0){
          param['selections'] = this.table.selectedRowKeys.join(",")
        }
        let paramsStr = encodeURI(JSON.stringify(param));
        console.log('paramsStr: ' + paramsStr)
        let url = window._CONFIG['domianURL']+this.url.exportXls+this.code+"?paramsStr="+paramsStr
        window.location.href = url;
      },
      handleExportXls(){
        let param = this.queryParam;
        if(this.table.selectedRowKeys && this.table.selectedRowKeys.length>0){
          param['selections'] = this.table.selectedRowKeys.join(",")
        }
        console.log("导出参数",param)
        let paramsStr = JSON.stringify(filterObj(param));
        downFile(this.url.exportXls+this.code,{paramsStr:paramsStr}).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data]), this.description+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data]))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', this.description+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        })
      },
      handleEdit(record){
        this.cgButtonLinkHandler(record,"beforeEdit","js")
        this.$refs.modal.edit(this.formTemplate,record.id);
      },
      handleDetail(record){
        this.$refs.modal.detail(this.formTemplate,record.id);
      },
      handleDeleteOne(record){
        this.cgButtonLinkHandler(record,"beforeDelete","js")
        this.handleDelete(record.id)
      },
      handleDelete(id){
        deleteAction(this.url.optPre+this.code+"/"+id).then((res)=>{
          if(res.success){
            this.$message.success(res.message)
            this.loadData()
          }else{
            this.$message.warning(res.message)
          }
        })
      },

      handleFormSuccess(){
        this.loadData()
      },
      onClearSelected(){
        this.table.selectedRowKeys = []
        this.table.selectionRows = []
      },
      getImgView(text){
        if(text && text.indexOf(",")>0){
          text = text.substring(0,text.indexOf(","))
        }
        return window._CONFIG['imgDomainURL']+"/"+text
      },
      uploadFile(text){
        if(!text){
          this.$message.warning("未知的文件")
          return;
        }
        if(text.indexOf(",")>0){
          text = text.substring(0,text.indexOf(","))
        }
        window.open(window._CONFIG['imgDomainURL']+"/"+text);//TODO 下载的方法
      },
      handleDelBatch(){
        if(this.table.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return false;
        }else{
          let ids = "";
          let that = this;
          that.table.selectedRowKeys.forEach(function(val) {
            ids+=val+",";
          });
          that.$confirm({
            title:"确认删除",
            content:"是否删除选中数据?",
            onOk: function(){
              that.handleDelete(ids)
              that.onClearSelected();
            }
          });
        }
      },

      searchByquery(){
        this.loadData(1);
      },
      searchReset(){
        this.queryParam = {}
        this.loadData(1);
      },
      handleToggleSearch(){
        this.toggleSearchStatus = !this.toggleSearchStatus;
      },
      getFormatDate(text){
        if(!text){
          return ''
        }
        let a = text;
        if(a.length>10){
          a = a.substring(0,10);
        }
        return a;
      },
      getImportUrl(){
        return '/online/cgform/api/importXls/'+this.code
      },
      initCgEnhanceJs(enhanceJs){
        //console.log("--onlineList-js增强",enhanceJs)
        if(enhanceJs){
          let Obj = eval ("(" + enhanceJs + ")");
          this.EnhanceJS = new Obj(getAction,postAction,deleteAction);
          this.cgButtonJsHandler('created')
        }else{
          this.EnhanceJS = ''
        }
      },
      initCgButtonList(btnList){
        let linkArr = []
        let buttonArr = []
        if(btnList && btnList.length>0){
          for(let i=0;i<btnList.length;i++){
            let temp = btnList[i]
            if(temp.buttonStyle=='button'){
              buttonArr.push(temp)
            }else if(temp.buttonStyle=='link'){
              linkArr.push(temp)
            }
          }
        }
        this.cgButtonLinkList = [...linkArr]
        this.cgButtonList=[...buttonArr]
      },
      cgButtonJsHandler(buttonCode){
        if(this.EnhanceJS[buttonCode]){
          this.EnhanceJS[buttonCode](this)
        }
      },
      cgButtonActionHandler(buttonCode){
        //处理自定义button的 需要配置该button自定义sql
        if(!this.table.selectedRowKeys || this.table.selectedRowKeys.length==0){
          this.$message.warning("请先选中一条记录")
          return false
        }
        if(this.table.selectedRowKeys.length>1){
          this.$message.warning("请只选中一条记录")
          return false
        }
        let params = {
          formId:this.code,
          buttonCode:buttonCode,
          dataId:this.table.selectedRowKeys[0]
        }
        console.log("自定义按钮请求后台参数：",params)
        postAction(this.url.buttonAction,params).then(res=>{
          if(res.success){
            this.loadData()
            this.$message.success("处理完成!")
          }else{
            this.$message.warning("处理失败!")
          }
        })

      },
      cgButtonLinkHandler(record,buttonCode,optType){
        if(optType=="js"){
          if(this.EnhanceJS[buttonCode]){
            this.EnhanceJS[buttonCode](this,record)
          }
        }else if(optType=="action"){
          let params = {
            formId:this.code,
            buttonCode:buttonCode,
            dataId:record.id
          }
          console.log("自定义按钮link请求后台参数：",params)
          postAction(this.url.buttonAction,params).then(res=>{
            if(res.success){
              this.loadData()
              this.$message.success("处理完成!")
            }else{
              this.$message.warning("处理失败!")
            }
          })
        }
      },
      initButtonSwitch(hideColumns){
        if(hideColumns && hideColumns.length>0){
          Object.keys(this.buttonSwitch).forEach(key=>{
            if(hideColumns.indexOf(key)>=0){
              this.buttonSwitch[key]=false
            }
          })

        }
      }

    }
  }
</script>
<style>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .anty-img-wrap{height:25px;position: relative;}
  .anty-img-wrap > img{max-height:100%;}
  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>