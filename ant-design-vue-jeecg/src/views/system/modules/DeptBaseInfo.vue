<template>
  <div :visible="visible">
    <a-descriptions size="small" bordered :column="1">
      <a-descriptions-item label="机构名称">{{model.departName}}</a-descriptions-item>
      <a-descriptions-item label="上级部门"><span>{{model.parentId}}</span></a-descriptions-item>
      <a-descriptions-item label="机构编码"><span>{{model.orgCode}}</span></a-descriptions-item>
      <a-descriptions-item label="机构类型"><span>{{model.orgCategory}}</span></a-descriptions-item>
      <a-descriptions-item label="排序"><span>{{model.departOrder}}</span></a-descriptions-item>
      <a-descriptions-item label="手机号"><span>{{model.mobile}}</span></a-descriptions-item>
      <a-descriptions-item label="地址"><span>{{model.address}}</span></a-descriptions-item>
      <a-descriptions-item label="备注"><span>{{model.memo}}</span></a-descriptions-item>
    </a-descriptions>
  </div>
</template>
<script>
  import { queryIdTree } from '@/api/api'

  export default {
    name: 'DeptBaseInfo',
    components: {},
    data() {
      return {
        departTree: [],
        id: '',
        model: {},
        visible: false,
        disable: true,
        treeData: [],
        labelCol: {
          xs: {span: 24},
          sm: {span: 3}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
      }
    },
    created() {
      this.loadTreeData();
    },
    methods: {
      loadTreeData() {
        queryIdTree().then((res) => {
          if (res.success) {
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i];
              this.treeData.push(temp);
            }
          }

        })
      },
      open(record) {
        this.visible = true;
        //update-begin---author:wangshuai ---date:20220211  for：[JTC-174]部门管理界面参考vue3的改改------------
        this.model = Object.assign({}, record)
        this.model.parentId = this.findTree(this.treeData,record.parentId);
        this.model.orgCategory = this.orgCategoryText(record.orgCategory)
        //update-end---author:wangshuai ---date:20220211  for：[JTC-174]部门管理界面参考vue3的改改------------
        },
      clearForm() {
        this.treeData = [];
      },
      /**
       * 通过父id查找部门名称
       * @param treeList 树数组
       * @param id 父id
       * @return id对应的部门名称
       */
      findTree(treeList,id){
        for (let i = 0; i < treeList.length; i++) {
          let item = treeList[i];
          //如果当前id和父id相同则返回部门名称
          if (item.key == id) {
            return item.title;
          }
          let children = item.children
          //存在子部门进行递归查询
          if(children){
            let findResult = this.findTree(children, id);
            //返回的数据不为空，结束递归，返回结果
            if (findResult) {
              return findResult
            }
          }
        }
      },
      /**
       * 将机构类型数值翻译成文本
       * @param orgCategory 部门类别
       * @return 部门类别对应的文本
       */
      orgCategoryText(orgCategory) {
        if(orgCategory == 1){
          return "公司";
        }else if(orgCategory == 2){
          return "部门";
        }else{
          return "岗位";
        }
      }
    }
  }
</script>
<style scoped lang="less">
  .ant-descriptions-view{
    border: 1px solid #f0f0f0;
  }
  /deep/ .ant-descriptions-item-label{
    width:180px
  }
  /deep/ .ant-descriptions-item-content span{
    color:#000000d9;
  }
  /deep/ .ant-descriptions-bordered .ant-descriptions-row{
    border-bottom: 1px solid #f0f0f0 !important;
  }
  /deep/ .ant-descriptions-bordered .ant-descriptions-item-label{
    border-right:  1px solid #f0f0f0;
  }
</style>