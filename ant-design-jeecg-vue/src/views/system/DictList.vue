<template>
  <a-card :bordered="false">

    <!-- 右侧面板 -->
    <a-col :span="5">
      <div class="table-operator" style="margin-bottom: 18px;">
        <a-row :gutter="24">
          <a-col :span="9">
            <a-button @click="addDict" type="primary" icon="plus">添加字典</a-button>
          </a-col>

          <a-col :span="9">
            <a-dropdown>
              <a-menu slot="overlay">
                <a-menu-item key="1" @click="editDict">编辑字典</a-menu-item>
                <a-menu-item key="2" @click="delDict">删除字典</a-menu-item>
                <a-menu-item key="3" @click="refreshDict">刷新</a-menu-item>
              </a-menu>
              <a-button style="margin-left: 8px">更多操作 <a-icon type="down" /></a-button>
            </a-dropdown>
          </a-col>
        </a-row>
      </div>

      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;padding: 5px">
        <a-icon type="select"/>&nbsp;
        当前选中：
        <span v-if="visible">
          <span style="color: #40a9ff;font-size: 12px;font-weight: 600;">{{ dict.dictName }}【{{ dict.dictCode }}】</span>
          <a><span @click="cancelDict" style="color:#2d8cf0"> 取消选择</span></a>
        </span>

      </div>

      <div>
        <a-input-search
          placeholder="输入搜索字典"
          @search="onSearch"
          style="margin-bottom: 16px;"
        />
        <a-tree
          :treeData="treeData"
          @select="this.onSelect">
        </a-tree>
      </div>
    </a-col>

    <!-- 中间面板 -->
    <a-col :span="1"/>

    <!-- 右侧面板 -->
    <a-col :span="18">
      <div class="table-operator">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="名称">
                <a-input placeholder="请输入名称" v-model="queryParam.itemText"></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item label="状态">
                <a-select
                  style="width: 200px"
                  v-model="queryParam.status"
                  placeholder="请选择状态"
                >
                  <a-select-option value="1">正常</a-select-option>
                  <a-select-option value="0">禁用</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" icon="search" @click="searchByquery">查询</a-button>
                <a-button type="primary" icon="reload" @click="searchReset" style="margin-left: 8px">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <div style="margin-bottom: 16px;">
        <a-button type="primary" icon="plus" @click="addDictItem">新增</a-button>
        <a-button type="primary" :loading="loadrefresh" icon="reload" @click="refreshDictItem" style="margin-left: 8px">刷新</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay" @click="handleMenuClick">
            <a-menu-item key="1"><a-icon type="delete"/>删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">批量操作 <a-icon type="down" /></a-button>
        </a-dropdown>
      </div>

      <!-- table区域-begin -->
      <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
          <i class="anticon anticon-info-circle ant-alert-icon"></i>
          已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          bordered
          rowKey="id"
          size="middle"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange"
        >

          <span slot="action" slot-scope="text, record">
            <a @click="editDictItem(record)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm title="确定删除吗?" @confirm="() => delDictItem(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>

        </a-table>
      </div>
      <!-- table区域-end -->
    </a-col>

    <dict-modal ref="modal" @ok="modalFormOk"></dict-modal>  <!-- 字典类型 -->
    <dict-item-modal ref="itemModal" @ok="modalFormOk"></dict-item-modal> <!-- 字典数据 -->
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import DictModal from './modules/DictModal'
  import DictItemModal from './modules/DictItemModal'
  import {delDict,getDictItemList,delDictItem,delDictItemList,treeList} from '@/api/api'

  export default {
    name: "DictList",
    components: {DictModal,DictItemModal},
    data () {
      return {
        description: '这是数据字典页面',
        visible:false,
        // 查询条件
        queryParam: {
          dictId:"",
          dictName:null,
          delFlag:"1",
          status:[],
        },
        // 表头
        columns: [
          {
            title: '排序',
            align: "center",
            dataIndex: 'sortOrder',
            width: "100px",
            sorter: true
          },
          {
            title: '名称',
            align: "center",
            dataIndex: 'itemText',
          },
          {
            title: '数据值',
            align: "center",
            dataIndex: 'itemValue',
          },
          {
            title: '描述',
            align: "center",
            dataIndex: 'description',
          },
          {
            title: '状态',
            align: "center",
            dataIndex: 'status',
            customRender:function (text) {
              if(text==1){
                return "正常";
              }else if(text==0){
                return "禁用";
              }else{
                return text;
              }
            },
          },
          {
            title: '创建时间',
            dataIndex: 'createTime',
            align: "center",
            sorter: true
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            width: "120px",
            scopedSlots: {customRender: 'action'},
          }
        ],
        //字典数据集
        dataSource: [],
        //字典集
        treeData:[],
        dict:"",
        // 分页参数
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter: {
          column: 'createTime',
          order: 'asc',
        },
        loading: false,
        loadrefresh: false,
        selectedRowKeys: [],
        selectedRows: [],
      }
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData (arg){
        if(arg===1){
          this.ipagination.current = 1;
        }
        this.loading=true;
        // 查询字典
        treeList({dictName:this.queryParam.dictName,delFlag:this.queryParam.delFlag}).then((res)=>{
          if(res.success){
            this.treeData=[];
            for(let a=0;a<res.result.length;a++){
              let temp = res.result[a];
              this.treeData.push(temp);
            }
          }
        })
        // 查询字典数据
        var params = this.getQueryParams();//查询条件
        getDictItemList(params).then((res)=>{
          if(res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
            this.loadrefresh=false;
            this.loading=false;
          }
        })
      },
      getQueryParams(){
        var param = Object.assign({}, this.queryParam,this.isorter);
        param.dictId = this.dictId;
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField(){
        var str = "id,";
        for(var a = 0;a<this.columns.length;a++){
          str+=","+this.columns[a].dataIndex;
        }
        return str;
      },

      //添加字典
      addDict(){
        this.$refs.modal.add();
        this.$refs.modal.title="新增";
      },
      //编辑字典
      editDict(){
        if(!this.dict.id){
          this.$message.warning("您还未选择要编辑的字典");
        }else{
          this.$refs.modal.edit(this.dict);
          this.$refs.modal.title="编辑";
        }
      },
      //删除字典
      delDict(){
        let that=this;
        if(!this.dict.id){
          this.$message.warning("您还未选择要删除的字典");
        }else{
          that.$confirm({
            title:"确认删除",
            content:"您确定要删除 "+this.dict.dictName+" 字典类型?",
            onOk: function(){
              delDict({id:that.dict.id}).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.loadData();
                  that.cancelDict();
                }else{
                  that.$message.warning(res.message);
                }
              });
            },
            onCancel() {},
          });
        }
      },
      //刷新字典
      refreshDict(){
        this.loadData();
      },
      //选择字典
      onSelect (selectedKeys, info) {
        this.dict = info.selectedNodes[0].data.props;
        this.dict.dictName = info.selectedNodes[0].data.props.title;
        this.dictId = this.dict.id;
        this.visible = true;
        this.loadData();
      },
      //取消选择
      cancelDict(){
        this.dict="";
        this.dictId = "";
        this.visible = false;
        this.loadData();
      },
      //查询字典
      onSearch (value) {
        if(value){
          this.queryParam.dictName = value;
        }else{
          this.queryParam.dictId = null;
          this.queryParam.dictName = null;
        }
        this.loadData();
      },


      // 添加字典数据
      addDictItem(){
        if(!this.dict.id){
          this.$message.warning("请选择一个字典类别");
        }else{
          this.$refs.itemModal.add(this.dict.id);
          this.$refs.itemModal.title="新增 "+this.dict.dictName+"("+this.dict.dictCode+") 的数据";
        }
      },
      //编辑字典数据
      editDictItem(record){
        if(!this.dict.id){
          this.$refs.itemModal.title="编辑字典的数据";
        }else{
          this.$refs.itemModal.title="编辑 "+this.dict.dictName+"("+this.dict.dictCode+") 的数据";
        }
        this.$refs.itemModal.edit(record);
      },
      handleMenuClick(e){
        if(e.key==1){
          this.batchdel();
        }
      },
      // 批量删除字典数据
      batchdel: function(){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning("您还未选择要删除的数据");
        }else{
          let ids="";
          let that = this;
          that.selectedRowKeys.forEach(function(val){
            ids+=val+",";
          });
          that.$confirm({
            title:"确认删除",
            content:"您确定要删除所选的"+that.selectedRowKeys.length+"条数据？",
            onOk: function(){
              delDictItemList({ids:ids}).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                }else{
                  that.$message.warning(res.message);
                }
              });
            },
            onCancel() {},
          });
        }
      },
      //删除字典
      delDictItem(id){
        delDictItem({id:id}).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.loadData();
          }else{
            this.$message.warning(res.message);
          }
        });
      },
      // 查询字典数据
      searchByquery(){
        this.loadData();
      },
      // 重置字典数据
      searchReset(){
        var that = this;
        that.queryParam.status=[];
        that.queryParam.itemText="";
        that.loadData(this.ipagination.current);
      },
      // 刷新
      refreshDictItem(){
        this.loadrefresh=true;
        this.loadData();
      },

      handleTableChange(pagination, filters, sorter){
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
      onSelectChange (selectedRowKeys,selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onClearSelected(){
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      modalFormOk () {
        this.loadData();
      },
    },
    watch: {
      openKeys (val) {
        console.log('openKeys', val)
      },
    },
  }
</script>

<style scoped>
  .table-operator{margin-bottom: 10px}
  .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle{width:0px}
</style>