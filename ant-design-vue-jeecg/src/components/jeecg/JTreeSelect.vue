<template>
  <a-tree-select
    allowClear
    labelInValue
    style="width: 100%"
    :disabled="disabled"
    :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
    :placeholder="placeholder"
    :loadData="asyncLoadTreeData"
    :value="treeValue"
    :treeData="treeData"
    @change="onChange"
    @search="onSearch">
  </a-tree-select>
</template>
<script>

  /*
  * 异步树加载组件 通过传入表名 显示字段 存储字段 加载一个树控件
  * <j-tree-select dict="aa_tree_test,aad,id" pid-field="pid" ></j-tree-select>
  * */
  import { getAction } from '@/api/manage'

  export default {
    name: 'JTreeSelect',
    props: {
      value:{
        type: String,
        required: false
      },
      placeholder:{
        type: String,
        default: '请选择',
        required: false
      },
      dict:{
        type: String,
        default: '',
        required: false
      },
      pidField:{
        type: String,
        default: 'pid',
        required: false
      },
      pidValue:{
        type: String,
        default: '0',
        required: false
      },
      disabled:{
        type:Boolean,
        default:false,
        required:false
      },
      hasChildField:{
        type: String,
        default: '',
        required: false
      }
    },
    data () {
      return {
        treeValue:"",
        treeData:[],
        url:"/sys/dict/loadTreeData",
        view:'/sys/dict/loadDictItem/',
        tableName:"",
        text:"",
        code:"",

      }
    },
    watch: {
      value () {
        this.loadItemByCode()
      },
      dict(){
        this.initDictInfo()
        this.loadRoot();
      }
    },
    created(){
      this.initDictInfo()
      this.loadRoot()
      this.loadItemByCode()
    },
    methods: {
      loadItemByCode(){
        if(!this.value || this.value=="0"){
          this.treeValue = ""
        }else{
          getAction(`${this.view}${this.dict}`,{key:this.value}).then(res=>{
            if(res.success){
              this.treeValue = {
                key:this.value,
                value:this.value,
                label:res.result
              }
            }
          })
        }
      },
      initDictInfo(){
        let arr = this.dict.split(",")
        this.tableName = arr[0]
        this.text = arr[1]
        this.code = arr[2]
      },
      asyncLoadTreeData (treeNode) {
        return new Promise((resolve) => {
          if (treeNode.$vnode.children) {
            resolve()
            return
          }
          let pid = treeNode.$vnode.key
          let param = {
            pid:pid,
            tableName:this.tableName,
            text:this.text,
            code:this.code,
            pidField:this.pidField,
            hasChildField:this.hasChildField
          }
          getAction(this.url,param).then(res=>{
            if(res.success){
              for(let i of res.result){
                i.value = i.key
                if(i.leaf==false){
                  i.isLeaf=false
                }else if(i.leaf==true){
                  i.isLeaf=true
                }
              }
              this.addChildren(pid,res.result,this.treeData)
              this.treeData = [...this.treeData]
            }
            resolve()
          })
        })
      },
      addChildren(pid,children,treeArray){
        if(treeArray && treeArray.length>0){
          for(let item of treeArray){
            if(item.key == pid){
              if(!children || children.length==0){
                item.isLeaf=true
              }else{
                item.children = children
              }
              break
            }else{
              this.addChildren(pid,children,item.children)
            }
          }
        }
      },
      loadRoot(){
        let param = {
          pid:this.pidValue,
          tableName:this.tableName,
          text:this.text,
          code:this.code,
          pidField:this.pidField,
          hasChildField:this.hasChildField
        }
        getAction(this.url,param).then(res=>{
          if(res.success && res.result){
            for(let i of res.result){
              i.value = i.key
              if(i.leaf==false){
                i.isLeaf=false
              }else if(i.leaf==true){
                i.isLeaf=true
              }
            }
            this.treeData = [...res.result]
          }else{
            console.log("数根节点查询结果-else",res)
          }
        })
      },
      onChange(value){
        if(!value){
          this.$emit('change', '');
          this.treeValue = ''
        }else{
          this.$emit('change', value.value);
          this.treeValue = value
        }

      },
      onSearch(value){
        console.log(value)
      },
      getCurrTreeData(){
        return this.treeData
      }
    },
    //2.2新增 在组件内定义 指定父组件调用时候的传值属性和事件类型 这个牛逼
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
