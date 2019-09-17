<template>
  <a-card :bordered="false" style="height: 100%">
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
        v-show="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除</a-button>
    </div>

    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="cgformTreeList"
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        v-bind="tableProps"
        @expand="handleExpand"
        style="min-height: 300px"
        :expandedRowKeys="expandedRowKeys">

        <template slot="dateSlot" slot-scope="text">
          <span>{{ getDateNoTime(text) }}</span>
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
          <template v-if="buttonSwitch.update">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical"/>
          </template>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item >
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-if="buttonSwitch.delete">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteOne(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <!-- 自定义按钮 -->
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

      <onl-cgform-auto-modal @success="handleFormSuccess" ref="modal" :code="code"></onl-cgform-auto-modal>

      <j-import-modal ref="importModal" :url="getImportUrl()" @ok="importOk"></j-import-modal>
    </div>
  </a-card>
</template>

<script>

  import { getAction,postAction,deleteAction,downFile } from '@/api/manage'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';
  import JImportModal from '@/components/jeecg/JImportModal'

  export default {
    name: 'OnlCgformTreeList',
    components: {
      JImportModal
    },
    data() {
      return {
        code: '87b55a515d3441b6b98e48e5b35474a6',
        description: '在线报表功能测试页面',
        currentTableName:"",
        pidField:"",
        hasChildrenField:"",
        textField:'',
        loading: false,
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
        },

        url: {
          getColumns: '/online/cgform/api/getColumns/',
          getTreeData: '/online/cgform/api/getTreeData/',
          optPre:"/online/cgform/api/form/",
          exportXls:'/online/cgform/api/exportXls/',
          buttonAction:'/online/cgform/api/doButton',
          startProcess: "/process/extActProcess/startMutilProcess",
        },
        isorter:{
          column: 'create_time',
          order: 'desc',
        },
        dictOptions:{

        },

        queryParam:{

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

        /*自定义按钮-link*/
        cgButtonLinkList:[],
        /*自定义按钮-button*/
        cgButtonList:[],
        /*JS增强*/
        EnhanceJS:'',
        /*操作按钮权限*/
        buttonSwitch:{
          add:true,
          update:true,
          delete:true,
          batch_delete:true,
          import:true,
          export:true
        },
        expandedRowKeys:[]

      }
    },
    created() {
      this.initAutoListConfig().then(()=>{
        this.loadData(1)
      }).catch(msg=>{
        console.log(msg)
      })
    },
    mounted(){
      //this.cgButtonJsHandler('mounted')
    },
    watch: {
      '$route'() {
        // 刷新参数放到这里去触发，就可以刷新相同界面了
        this.initAutoListConfig().then(()=>{
          this.loadData(1)
        }).catch(msg=>{
          console.log(msg)
        })
      }
    },
    computed: {
      tableProps() {
        let _this = this
        return {
          // 列表项是否可选择
          // https://vue.ant.design/components/table-cn/#rowSelection
          rowSelection: {
            selectedRowKeys: _this.selectedRowKeys,
            onChange: (selectedRowKeys) => _this.selectedRowKeys = selectedRowKeys
          }
        }
      }
    },
    methods: {
      resetData(){
        this.description=''
        this.currentTableName=''
        this.pidField=''
        this.hasChildrenField=''
        this.textField=''
        this.columns = []
        this.dataSource = []
        this.selectedRowKeys=[]
        this.selectionRows=[]
      },
      initAutoListConfig() {
        return new Promise((resolve, reject) => {
          if (!this.$route.params.code) {
            reject("列表加载需要参数CODE为空!")
          } else {
            this.resetData()
            this.loading = true
            this.code = this.$route.params.code
            getAction(`${this.url.getColumns}${this.code}`)
              .then(res => {
                console.log("--onlineList-加载动态列>>", res);
                if(res.success){
                  this.configInfohandler(res)
                  resolve();
                }else{
                  reject("onlineList-加载表配置信息失败")
                }
                this.loading = false
              })
              .catch(err => {
                reject(err)
              })
          }
        })
      },
      configInfohandler(res){
        this.dictOptions = res.result.dictOptions
        this.formTemplate = res.result.formTemplate
        this.description = res.result.description
        this.currentTableName = res.result.currentTableName
        this.pidField = res.result.pidField
        this.hasChildrenField = res.result.hasChildrenField
        this.textField = res.result.textField
        //自定义按钮
        this.initCgButtonList(res.result.cgButtonList)
        //JS增强
        this.initCgEnhanceJs(res.result.enhanceJs)
        //操作按钮权限
        this.initButtonSwitch(res.result.hideColumns)
        let currColumns = res.result.columns
        let textFieldIndex = -1
        for(let a=0;a<currColumns.length;a++){
          currColumns[a].align = 'left'
          if(this.textField==currColumns[a].dataIndex){
            textFieldIndex = a
          }
          if(currColumns[a].customRender){
            let dictCode = currColumns[a].customRender;
            currColumns[a].customRender=(text)=>{
              return filterMultiDictText(this.dictOptions[dictCode], text);
            }
          }
        }
        if(textFieldIndex!=-1){
          let textFieldColumn = currColumns.splice(textFieldIndex,1)
          currColumns.unshift(textFieldColumn[0])
        }
        currColumns.push(this.actionColumn);
        this.columns = [...currColumns]
      },
      //加载根节点
      loadData(arg){
        if(arg==1){
          this.pagination.current=1
        }
        this.loading = true
        this.expandedRowKeys=[]
        let params = this.getQueryParams();//查询条件
        params[this.pidField]='0'
        console.log("--onlineList-查询条件-->",params)
        getAction(`${this.url.getTreeData}${this.code}`,params).then((res)=>{
          console.log("--onlineList-列表数据",res)
          if(res.success){
            let result = res.result;
            if(Number(result.total)>0){
              this.pagination.total = Number(result.total)
              let dataSource = res.result.records.map(item => {
                // 判断是否标记了带有子级
                if (item[this.hasChildrenField] === true || item[this.hasChildrenField]=='1') {
                  let loadChild = { id: `${item.id}_loadChild`, name: 'loading...', isLoading: true }
                  item.children = [loadChild]
                }
                return item
              })
              this.dataSource = dataSource
            }else{
              this.pagination.total=0;
              this.dataSource=[]
            }
          }else{
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      //加载叶子节点
      handleExpand(expanded, record) {
        // 判断是否是展开状态
        if (expanded) {
          this.expandedRowKeys.push(record.id)
          if (record.children.length>0 && record.children[0].isLoading === true) {
            let params = this.getQueryParams();//查询条件
            params[this.pidField] = record.id
            getAction(`${this.url.getTreeData}${this.code}`,params).then((res)=>{
              if(res.success){
                if(Number(res.result.total)>0){
                  let dataSource = res.result.records.map(item => {
                    // 判断是否标记了带有子级
                    if (item[this.hasChildrenField] === true || item[this.hasChildrenField]=='1') {
                      let loadChild = { id: `${item.id}_loadChild`, name: 'loading...', isLoading: true }
                      item.children = [loadChild]
                    }
                    return item
                  })
                  record.children = dataSource
                }else{
                  record.children=''
                  record.hasChildrenField='0'
                }
              }else{
                this.$message.warning(res.message)
              }
            })
          }
        }else{
          let keyIndex = this.expandedRowKeys.indexOf(record.id)
          if(keyIndex>=0){
            this.expandedRowKeys.splice(keyIndex, 1);
          }
        }
      },
      getQueryParams() {
        let param = Object.assign({}, this.queryParam,this.isorter);
        param.pageNo = this.pagination.current;
        param.pageSize = this.pagination.pageSize;
        return filterObj(param);
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
      initButtonSwitch(hideColumns){
        if(hideColumns && hideColumns.length>0){
          Object.keys(this.buttonSwitch).forEach(key=>{
            if(hideColumns.indexOf(key)>=0){
              this.buttonSwitch[key]=false
            }
          })

        }
      },
      onClearSelected(){
        this.selectedRowKeys = []
        this.selectionRows = []
      },
      handleTableChange(pagination, filters, sorter){
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.pagination = pagination;
        this.loadData();
      },
      /*-------数据格式化-begin----------*/
      getDateNoTime(text){
        if(!text){
          return ''
        }
        let a = text;
        if(a.length>10){
          a = a.substring(0,10);
        }
        return a;
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
        window.open(window._CONFIG['domianURL'] + "/sys/common/download/"+text);
      },
      /*-------数据格式化-end----------*/

      /*-------功能按钮触发事件-begin----------*/
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
      handleAdd(){
        this.cgButtonJsHandler('beforeAdd')
        this.$refs.modal.add(this.formTemplate);
      },
      handleFormSuccess(){
        this.loadData()
      },
      handleImportXls(){
        this.$refs.importModal.show()
      },
      importOk(){
        this.loadData(1)
      },
      getImportUrl(){
        return '/online/cgform/api/importXls/'+this.code
      },
      handleExportXls(){
        let param = this.queryParam;
        if(this.selectedRowKeys && this.selectedRowKeys.length>0){
          param['selections'] = this.selectedRowKeys.join(",")
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
      handleDelBatch(){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return false;
        }else{
          let ids = "";
          let that = this;
          that.selectedRowKeys.forEach(function(val) {
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
      /*-------功能按钮触发事件-begin----------*/

      /*-------JS增强-begin----------*/
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
      cgButtonJsHandler(buttonCode){
        if(this.EnhanceJS[buttonCode]){
          this.EnhanceJS[buttonCode](this)
        }
      },
      cgButtonActionHandler(buttonCode){
        //处理自定义button的 需要配置该button自定义sql
        if(!this.selectedRowKeys || this.selectedRowKeys.length==0){
          this.$message.warning("请先选中一条记录")
          return false
        }
        if(this.selectedRowKeys.length>1){
          this.$message.warning("请只选中一条记录")
          return false
        }
        let params = {
          formId:this.code,
          buttonCode:buttonCode,
          dataId:this.selectedRowKeys[0]
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
      /*-------JS增强-end----------*/

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