<template>
  <a-layout style="height:100%" :class="{ 'anty-node-layout':true }">
    <a-layout-header class="header">
      <div class="anty-opt-btn">
        <!-- 按钮操作区域 -->
        <a-button @click="handleAdd(2)" style="margin-left: 18px" type="primary" icon="plus">添加子部门</a-button>
        <a-button @click="handleAdd(1)" type="default" icon="plus">添加一级部门</a-button>
        <a-button title="删除多条数据" @click="batchDel" type="default" icon="delete">批量删除</a-button>
        <a-button @click="refresh" type="default" icon="reload">刷新</a-button>
      </div>
    </a-layout-header>

    <a-layout-content>
      <a-layout style="background: #fff">
        <a-layout-sider width="400" style="background: #fff;padding-left:16px;height: 100%;">
          <a-alert type="info" :showIcon="true" >
            <div slot="message">
              当前选择：
              <a v-if="this.currSelected.title">{{ getCurrSelectedTitle() }}</a>
              <a v-if="this.currSelected.title" style="margin-left: 10px" @click="onClearSelected">取消选择</a>
            </div>
          </a-alert>
          <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入部门名称进行搜索" />
          <!-- 树-->
          <a-tree
            checkable
            multiple
            @select="onSelect"
            @check="onCheck"
            :selectedKeys="selectedKeys"
            :checkedKeys="checkedKeys"
            :treeData="departTree"
            :checkStrictly="true"
            :expandedKeys="iExpandedKeys"
            :autoExpandParent="autoExpandParent"
            @expand="onExpand"/>
        </a-layout-sider>

        <a-layout-content :style="{ padding: '16px 24px 30px 24px', height: '100%' }">
          <a-form :form="form">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="机构名称">
              <a-input placeholder="请输入机构/部门名称" v-decorator="['departName', validatorRules.departName ]"/>
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上级部门">
              <a-tree-select
                style="width:100%"
                :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
                :treeData="treeData"
                :disabled="disable"
                v-model="model.parentId"
                placeholder="无">
              </a-tree-select>
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="排序">
              <a-input-number v-decorator="[ 'departOrder',{'initialValue':0}]" />
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="传真">
              <a-input placeholder="请输入传真" v-decorator="['fax', {}]" />
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="地址">
              <a-input placeholder="请输入地址" v-decorator="['address', {}]" />
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="备注">
              <a-textarea placeholder="请输入备注" v-decorator="['memo', {}]" />
            </a-form-item>
          </a-form>

          <div class="anty-form-btn">
            <a-button @click="emptyCurrForm" type="default" htmlType="button" icon="sync">重置</a-button>
            <a-button @click="submitCurrForm" type="primary" htmlType="button" icon="form">修改并保存</a-button>
          </div>
        </a-layout-content>
      </a-layout>
    </a-layout-content>

    <Depart-modal ref="DepartModal" @ok="loadTree"></Depart-modal>
  </a-layout>
</template>
<script>
  import DepartModal from './modules/DepartModal'
  import pick from 'lodash.pick'
  import {queryDepartTreeList, searchByKeywords} from '@/api/api'
  import { httpAction,deleteAction } from '@/api/manage'
  // 表头
  const columns = [
    {
      title: '机构名称',
      dataIndex: 'departName',
    },
    {
      title: '机构类型',
      align:"center",
      dataIndex: 'orgType'
    },
    {
      title: '机构编码',
      dataIndex: 'orgCode'
    },
    {
      title: '手机号',
      dataIndex: 'mobile'
    },
    {
      title: '传真',
      dataIndex: 'fax'
    },
    {
      title: '地址',
      dataIndex: 'address'
    },
    {
      title:'排序',
      align:'center',
      dataIndex:'departOrder'
    },
    {
      title: '操作',
      align:"center",
      dataIndex: 'action',
      scopedSlots: { customRender: 'action' },
    }
  ];
  export default {
    name: "DepartList",
    components: {
      DepartModal
    },
    data(){
      return {
        iExpandedKeys:[],
        autoExpandParent:true,
        currFlowId:"",
        currFlowName:"",
        disable:true,
        treeData: [],
        departTree:[],
        hiding:true,
        model:{},
        depart:{},
        columns:columns,
        disableSubmit:false,
        checkedKeys:[],
        selectedKeys:[],
        currSelected:{},
        form: this.$form.createForm(this),
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        graphDatasource:{
          nodes:[],
          edges:[]
        },
        validatorRules:{
          departName:{rules: [{ required: true, message: '请输入机构/部门名称!' }]},
          orgCode:{rules: [{ required: true, message: '请输入机构编码!' }]},
          mobile:{rules: [{validator:this.validateMobile}]}
        },
        url: {
          delete: "/sysdepart/sysDepart/delete",
          edit: "/sysdepart/sysDepart/edit",
          deleteBatch: "/sysdepart/sysDepart/deleteBatch",
        },
      }
    },
    methods:{
      loadTree(){
        var that = this;
        that.treeData = [];
        that.departTree = [];
        queryDepartTreeList().then((res)=>{
          if(res.success){
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i];
              that.treeData.push(temp);
              that.departTree.push(temp);
              that.setThisExpandedKeys(temp);
              console.log(temp.id)
            }
          }
        });
      },
      setThisExpandedKeys(node){
        if(node.children && node.children.length>0){
          this.iExpandedKeys.push(node.key);
          for(let a=0;a<node.children.length;a++){
            this.setThisExpandedKeys(node.children[a]);
          }
        }
      },
      refresh(){
        this.loadTree();
      },
      onExpand (expandedKeys) {
        console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        this.autoExpandParent = false
      },
      backFlowList(){
        this.$router.back(-1);
      },
      addRootNode(){
        this.$refs.nodeModal.add(this.currFlowId,'');
      },
      batchDel: function(){
        console.log(this.checkedKeys)
        if(this.checkedKeys.length<=0){
          this.$message.warning('请选择一条记录！');
        }else{
          var ids = "";
          for(var a =0;a<this.checkedKeys.length;a++){
            ids+=this.checkedKeys[a]+",";
          }
          var that = this;
          this.$confirm({
            title:"确认删除",
            content:"确定要删除所选中的 "+this.checkedKeys.length+" 条数据?",
            onOk: function(){
              deleteAction(that.url.deleteBatch,{ids: ids}).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.loadTree();
                  that.onClearSelected();
                }else{
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      onSearch(value){
        let that = this;
        if(value){
          searchByKeywords({keyWord:value}).then((res) =>{
            if(res.success){
              that.departTree = [];
              for (let i = 0; i < res.result.length; i++) {
                let temp = res.result[i];
                that.departTree.push(temp);
              }
            }else{
              that.$message.warning(res.message);
            }
          })
        }else{
          that.loadTree();
        }

      },
      nodeModalOk(){
        this.loadTree();
      },
      nodeModalClose(){
      },
      onCheck (checkedKeys,info) {
        console.log('onCheck', checkedKeys, info)
        this.hiding = false;
        this.checkedKeys = checkedKeys.checked;
      },
      onSelect (selectedKeys,e) {
        console.log('selected', selectedKeys, e)
        this.hiding = false;
        let record = e.node.dataRef;
        console.log("onSelect-record",record);
        this.currSelected = Object.assign({},record);
        this.model = this.currSelected;
        this.selectedKeys = [record.key];
        this.model.parentId = record.parentId;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(record,'departName','departOrder','mobile','fax','address','memo'))
        });

      },
      getCurrSelectedTitle(){
        return !this.currSelected.title?"":this.currSelected.title;
      },
      onClearSelected(){
        this.hiding = true;
        this.model = {};
        this.checkedKeys={};
        this.currSelected={};
        this.selectedKeys = [];
      },
      handleNodeTypeChange(val){
        this.currSelected.nodeType = val;
      },
      notifyTriggerTypeChange(value){
        this.currSelected.notifyTriggerType = value;
      },
      receiptTriggerTypeChange(value){
        this.currSelected.receiptTriggerType = value;
      },
      submitCurrForm(){
        this.form.validateFields((err, values) =>{
          if (!err) {
            if(!this.currSelected.id){
              this.$message.warning("请点击选择要修改部门!");
              return
            }

            let formData = Object.assign(this.currSelected, values);
            console.log('Received values of form: ', formData)
            httpAction(this.url.edit, formData, "put").then((res) => {
              if (res.success) {
                this.$message.success("保存成功!");
                this.loadTree();
                this.selectedKeys = [];
              }
            })
          }
        });
      },
      emptyCurrForm(){
        this.form.setFieldsValue({
          parentId:"",
          departName:"",
          departNameEn:"",
          departNameAbbr:"",
          departOrder:"",
          orgCode:"",
          mobile:"",
          fax:"",
          address:"",
          memo:"",
          description:"",
        });
      },
      nodeSettingFormSubmit(){
        this.form.validateFields((err, values) =>{
          if (!err) {
            console.log('Received values of form: ', values)
          }
        });
      },
      openSelect(){
        this.$refs.sysDirectiveModal.show();
      },
      handleAdd(num){
        if(num == 1){
          this.$refs.DepartModal.add();
          this.$refs.DepartModal.title="新增";
        }else{
          let key = this.currSelected.key;
          if(!key){
            this.$message.warning("请先选中一条记录!");
            return false;
          }
          this.$refs.DepartModal.add(this.selectedKeys);
          this.$refs.DepartModal.title="新增";
        }
      },
      selectDirectiveOk(record){
        console.log("选中指令数据",record);
        this.nodeSettingForm.setFieldsValue({directiveCode:record.directiveCode});
        this.currSelected.sysCode = record.sysCode;
      },
      getFlowGraphData(node){
        this.graphDatasource.nodes.push({
          id:node.id,
          text:node.flowNodeName
        })
        if(node.children.length>0){
          for(let a=0;a<node.children.length;a++){
            let temp = node.children[a]
            this.graphDatasource.edges.push({
              source:node.id,
              target:temp.id
            });
            this.getFlowGraphData(temp);
          }
        }
      }
    },
    created(){
      this.currFlowId = this.$route.params.id;
      this.currFlowName = this.$route.params.name;
      this.loadTree();
    },

  }
</script>
<style scoped>
  .anty-form-btn{width: 100%;text-align: center;}
  .anty-form-btn button{margin:0 5px;}
  .anty-node-layout .ant-layout-content{margin:0 !important;}
  .anty-node-layout .ant-layout-header{padding-right:0}
  .header{padding:0 8px;}
  .header button{margin:0 3px}
  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
  #app .desktop {
    height: auto !important;
  }
</style>