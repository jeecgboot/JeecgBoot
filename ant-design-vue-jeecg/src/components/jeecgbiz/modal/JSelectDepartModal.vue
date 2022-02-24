<template>
  <j-modal
    title="选择部门"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    @update:fullscreen="isFullscreen"
    wrapClassName="j-depart-select-modal"
    switchFullscreen
    cancelText="关闭">
    <a-spin tip="Loading..." :spinning="false">
      <a-input-search v-model="searchValue" style="margin-bottom: 1px" placeholder="请输入部门名称按回车进行搜索" />
      <a-empty v-if="filterTreeData.length===0"></a-empty>
      <a-tree
        v-else
        checkable
        :class="treeScreenClass"
        :treeData="filterTreeData"
        :checkStrictly="checkStrictly"
        @check="onCheck"
        @select="onSelect"
        @expand="onExpand"
        :autoExpandParent="autoExpandParent"
        :expandedKeys="expandedKeys"
        :checkedKeys="checkedKeys">

      </a-tree>
    </a-spin>
    <!--底部父子关联操作和确认取消按钮-->
    <template slot="footer" v-if="treeOpera && multi">
      <div class="drawer-bootom-button">
        <a-dropdown style="float: left" :trigger="['click']" placement="topCenter">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="switchCheckStrictly(1)">父子关联</a-menu-item>
            <a-menu-item key="2" @click="switchCheckStrictly(2)">取消关联</a-menu-item>
          </a-menu>
          <a-button>
            树操作 <a-icon type="up" />
          </a-button>
        </a-dropdown>
        <a-button @click="handleCancel" type="primary" style="margin-right: 0.8rem">关闭</a-button>
        <a-button @click="handleSubmit" type="primary" >确认</a-button>
      </div>
    </template>
  </j-modal>
</template>

<script>
  import { queryDepartTreeList } from '@/api/api'
  export default {
    name: 'JSelectDepartModal',
    props:['modalWidth','multi','rootOpened','departId', 'store', 'text','treeOpera'],
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
        searchValue:"",
        checkStrictly: true,
        fullscreen:false
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
          this.initDepartComponent(true)
        }
      }
    },
    computed:{
      treeScreenClass() {
        return {
          'my-dept-select-tree': true,
          'fullscreen': this.fullscreen,
        }
      },
      filterTreeData(){
        if(!this.searchValue){
          return this.treeData
        }
        let filter = []
        this.dataList.forEach((item) => {
          if (item.title.includes(this.searchValue)) {
            filter.push(Object.assign({}, item, {children: null, isLeaf: true}))
          }
        })
        return filter
      },
    },
    methods:{
      show(){
        this.visible=true
        this.checkedRows=[]
        this.checkedKeys=[]
      },
      loadDepart(){
        // 这个方法是找到所有的部门信息
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
      initDepartComponent(flag){
        let arr = []
        //该方法两个地方用 1.visible改变事件重新设置选中项 2.组件编辑页面回显
        let fieldName = flag==true?'key':this.text
        if(this.departId){
          let arr2 = this.departId.split(',')
          for(let item of this.dataList){
            if(arr2.indexOf(item[this.store])>=0){
              arr.push(item[fieldName])
            }
          }
        }
        if(flag==true){
          this.checkedKeys = [...arr]
        }else{
          this.$emit("initComp", arr.join(','))
        }
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
          //全部keys
          //this.allTreeKeys = [...keys]
        }else{
          this.expandedKeys=[]
          //this.allTreeKeys = []
        }
      },
      onCheck (checkedKeys,info) {
        if(!this.multi){
          let arr = checkedKeys.checked.filter(item => this.checkedKeys.indexOf(item) < 0)
          this.checkedKeys = [...arr]
          this.checkedRows = (this.checkedKeys.length === 0) ? [] : [info.node.dataRef]
        }else{
          if(this.checkStrictly){
            this.checkedKeys = checkedKeys.checked
          }else{
            this.checkedKeys = checkedKeys
          }
          this.checkedRows = this.getCheckedRows(this.checkedKeys)
        }
      },
      onSelect(selectedKeys,info) {
        //取消关联的情况下才走onSelect的逻辑
        if(this.checkStrictly){
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
        }
      },
      onExpand (expandedKeys) {
        this.expandedKeys = expandedKeys
        this.autoExpandParent = false
      },
      handleSubmit(){
        if(!this.checkedKeys || this.checkedKeys.length==0){
          this.$emit("ok",'')
        }else{
          let checkRow = this.getCheckedRows(this.checkedKeys)
          let keyStr = this.checkedKeys.join(",")
          this.$emit("ok", checkRow, keyStr)
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
      },
      switchCheckStrictly (v) {
        if(v==1){
          this.checkStrictly = false
        }else if(v==2){
          this.checkStrictly = true
        }
      },
      isFullscreen(val){
        this.fullscreen=val
      }
    }
  }

</script>

<style lang="less" scoped>
  // 限制部门选择树高度，避免部门太多时点击确定不便
  .my-dept-select-tree{
    height:350px;
    
    &.fullscreen{
      height: calc(100vh - 250px);
    }
    overflow-y: scroll;
  }
  .drawer-bootom-button {
    position: absolute;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>