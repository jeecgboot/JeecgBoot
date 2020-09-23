<template>
  <j-modal
    title="选择部门"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    switchFullscreen
    cancelText="关闭">
    <a-spin tip="Loading..." :spinning="false">
      <a-input-search style="margin-bottom: 1px" placeholder="请输入部门名称按回车进行搜索" @search="onSearch" />
      <a-tree
        checkable
        class="my-dept-select-tree"
        :treeData="treeData"
        :checkStrictly="true"
        @check="onCheck"
        @select="onSelect"
        @expand="onExpand"
        :autoExpandParent="autoExpandParent"
        :expandedKeys="expandedKeys"
        :checkedKeys="checkedKeys">

        <template slot="title" slot-scope="{title}">
          <span v-if="title.indexOf(searchValue) > -1">
            {{title.substr(0, title.indexOf(searchValue))}}
            <span style="color: #f50">{{searchValue}}</span>
            {{title.substr(title.indexOf(searchValue) + searchValue.length)}}
          </span>
          <span v-else>{{title}}</span>
        </template>
      </a-tree>

    </a-spin>
  </j-modal>
</template>

<script>
  import { queryDepartTreeList } from '@/api/api'
  export default {
    name: 'JSelectDepartModal',
    props:['modalWidth','multi','rootOpened','departId'],
    data(){
      return {
        visible:false,
        confirmLoading:false,
        treeData:[],
        autoExpandParent:true,
        expandedKeys:[],
        dataList:[],
        checkedKeys:[],
        checkedRows:[],
        searchValue:""
      }
    },
    created(){
      this.loadDepart();
    },
    watch:{
      departId(){
        this.initDepartComponent()
      },
      visible: {
        handler() {
          if (this.departId) {
            this.checkedKeys = this.departId.split(",");
            // console.log('this.departId', this.departId)
          } else {
            this.checkedKeys = [];
          }
        }
      }
    },
    methods:{
      show(){
        this.visible=true
        this.checkedRows=[]
        this.checkedKeys=[]
      },
      loadDepart(){
        queryDepartTreeList().then(res=>{
          if(res.success){
            let arr = [...res.result]
            this.reWriterWithSlot(arr)
            this.treeData = arr
            this.initDepartComponent()
            if(this.rootOpened){
              this.initExpandedKeys(res.result)
            }
          }
        })
      },
      initDepartComponent(){
        let names = ''
        if(this.departId){
          let currDepartId = this.departId
          for(let item of this.dataList){
            if(currDepartId.indexOf(item.key)>=0){
              names+=","+item.title
            }
          }
          if(names){
            names = names.substring(1)
          }
        }
        this.$emit("initComp",names)
      },
      reWriterWithSlot(arr){
        for(let item of arr){
          if(item.children && item.children.length>0){
            this.reWriterWithSlot(item.children)
            let temp = Object.assign({},item)
            temp.children = {}
            this.dataList.push(temp)
          }else{
            this.dataList.push(item)
            item.scopedSlots={ title: 'title' }
          }
        }
      },
      initExpandedKeys(arr){
        if(arr && arr.length>0){
          let keys = []
          for(let item of arr){
            if(item.children && item.children.length>0){
              keys.push(item.id)
            }
          }
          this.expandedKeys=[...keys]
        }else{
          this.expandedKeys=[]
        }
      },
      onCheck (checkedKeys,info) {
        if(!this.multi){
          let arr = checkedKeys.checked.filter(item => this.checkedKeys.indexOf(item) < 0)
          this.checkedKeys = [...arr]
          this.checkedRows = (this.checkedKeys.length === 0) ? [] : [info.node.dataRef]
        }else{
          this.checkedKeys = checkedKeys.checked
          this.checkedRows = this.getCheckedRows(this.checkedKeys)
        }
      },
      onSelect(selectedKeys,info) {
        let keys = []
        keys.push(selectedKeys[0])
        if(!this.checkedKeys || this.checkedKeys.length===0 || !this.multi){
          this.checkedKeys = [...keys]
          this.checkedRows=[info.node.dataRef]
        }else{
          let currKey = info.node.dataRef.key
          if(this.checkedKeys.indexOf(currKey)>=0){
            this.checkedKeys = this.checkedKeys.filter(item=> item !==currKey)
          }else{
            this.checkedKeys.push(...keys)
          }
        }
        this.checkedRows = this.getCheckedRows(this.checkedKeys)
      },
      onExpand (expandedKeys) {
        this.expandedKeys = expandedKeys
        this.autoExpandParent = false
      },
      handleSubmit(){
        if(!this.checkedKeys || this.checkedKeys.length==0){
          this.$emit("ok",'')
        }else{
          this.$emit("ok",this.checkedRows,this.checkedKeys.join(","))
        }
        this.handleClear()
      },
      handleCancel(){
        this.handleClear()
      },
      handleClear(){
        this.visible=false
        this.checkedKeys=[]
      },
      getParentKey(currKey,treeData){
        let parentKey
        for (let i = 0; i < treeData.length; i++) {
          const node = treeData[i]
          if (node.children) {
            if (node.children.some(item => item.key === currKey)) {
              parentKey = node.key
            } else if (this.getParentKey(currKey, node.children)) {
              parentKey = this.getParentKey(currKey, node.children)
            }
          }
        }
        return parentKey
      },
      onSearch(value){
        const expandedKeys = this.dataList.map((item) => {
          if (item.title.indexOf(value) > -1) {
            return this.getParentKey(item.key,this.treeData)
          }
          return null
        }).filter((item, i, self) => item && self.indexOf(item) === i)

        Object.assign(this, {
          expandedKeys,
          searchValue: value,
          autoExpandParent: true,
        })


      },
      // 根据 checkedKeys 获取 rows
      getCheckedRows(checkedKeys) {
        const forChildren = (list, key) => {
          for (let item of list) {
            if (item.id === key) {
              return item
            }
            if (item.children instanceof Array) {
              let value = forChildren(item.children, key)
              if (value != null) {
                return value
              }
            }
          }
          return null
        }

        let rows = []
        for (let key of checkedKeys) {
          let row = forChildren(this.treeData, key)
          if (row != null) {
            rows.push(row)
          }
        }
        return rows
      }
    }
  }

</script>

<style lang="less" scoped>
  // 限制部门选择树高度，避免部门太多时点击确定不便
  .my-dept-select-tree{
    height: 350px;
    overflow-y: scroll;
  }

</style>