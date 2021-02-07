<template>
  <a-card :bordered="false">
    <template v-if="this.departId">
      <a-spin :spinning="loading">
        <a-form>
          <a-form-item label='所拥有的权限'>
            <a-tree
              checkable
              @check="onCheck"
              :checkedKeys="checkedKeys"
              :treeData="treeData"
              @expand="onExpand"
              @select="onTreeNodeSelect"
              :selectedKeys="selectedKeys"
              :expandedKeys="expandedKeysss"
              :checkStrictly="checkStrictly"
              style="height:500px;overflow: auto;">
              <span slot="hasDatarule" slot-scope="{slotTitle,ruleFlag}">
                {{ slotTitle }}
                <a-icon v-if="ruleFlag" type="align-left" style="margin-left:5px;color: red;"></a-icon>
              </span>
            </a-tree>
          </a-form-item>
        </a-form>
      </a-spin>
      <div class="anty-form-btn">
        <a-dropdown style="float: left" :trigger="['click']" placement="topCenter">
          <a-menu slot="overlay">
            <!-- 简化Tree逻辑，使用默认checkStrictly为false的行为，即默认父子关联
            <a-menu-item key="1" @click="switchCheckStrictly(1)">父子关联</a-menu-item>
            <a-menu-item key="2" @click="switchCheckStrictly(2)">取消关联</a-menu-item>
            -->
            <a-menu-item key="3" @click="checkALL">全部勾选</a-menu-item>
            <a-menu-item key="4" @click="cancelCheckALL">取消全选</a-menu-item>
            <a-menu-item key="5" @click="expandAll">展开所有</a-menu-item>
            <a-menu-item key="6" @click="closeAll">合并所有</a-menu-item>
          </a-menu>
          <a-button>
            树操作 <a-icon type="up" />
          </a-button>
        </a-dropdown>
        <a-button style="float: right" @click="handleSubmit" type="primary" htmlType="button" icon="form">保存</a-button>
      </div>
    </template>
    <a-card v-else :bordered="false" style="height:200px">
      <a-empty>
        <span slot="description"> 请先选择一个部门! </span>
      </a-empty>
    </a-card>
    <depart-datarule-modal ref="datarule"/>
  </a-card>
</template>

<script>
  import {queryTreeListForRole,queryDepartPermission,saveDepartPermission} from '@/api/api'
  import DepartDataruleModal from './DepartDataruleModal'

  export default {
    name: 'DepartAuthModal',
    components: { DepartDataruleModal },
    data(){
      return {
        departId:"",
        treeData: [],
        defaultCheckedKeys:[],
        checkedKeys:[],
        halfCheckedKeys:[],
        expandedKeysss:[],
        allTreeKeys:[],
        autoExpandParent: true,
        checkStrictly: false,
        title:"部门权限配置",
        visible: false,
        loading: false,
        selectedKeys:[]
      }
    },
    methods: {
      onTreeNodeSelect(id){
        if(id && id.length>0){
          this.selectedKeys = id
        }
        this.$refs.datarule.show(this.selectedKeys[0],this.departId)
      },
      onCheck (checkedKeys, { halfCheckedKeys }) {
        // 保存选中的和半选中的，后面保存的时候合并提交
        this.checkedKeys = checkedKeys
        this.halfCheckedKeys = halfCheckedKeys
      },
      show(departId){
        this.departId=departId
        this.loadData();
      },
      close () {
        this.reset()
        this.$emit('close');
        this.visible = false;
      },
      onExpand(expandedKeys){
        this.expandedKeysss = expandedKeys;
        this.autoExpandParent = false
      },
      reset () {
        this.expandedKeysss = []
        this.checkedKeys = []
        this.defaultCheckedKeys = []
        this.loading = false
      },
      expandAll () {
        this.expandedKeysss = this.allTreeKeys
      },
      closeAll () {
        this.expandedKeysss = []
      },
      checkALL () {
        this.checkedKeys = this.allTreeKeys
      },
      cancelCheckALL () {
        this.checkedKeys = []
      },
      handleCancel () {
        this.close()
      },
      handleSubmit() {
        let that = this;
        if(!that.departId){
          this.$message.warning('请点击选择一个部门!')
        }
        let checkedKeys = [...that.checkedKeys, ...that.halfCheckedKeys]
        const permissionIds = checkedKeys.join(",")
        let params =  {
          departId:that.departId,
          permissionIds,
          lastpermissionIds:that.defaultCheckedKeys.join(","),
        };
        that.loading = true;
        saveDepartPermission(params).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.loading = false;
            that.loadData();
          }else {
            that.$message.error(res.message);
            that.loading = false;
          }
        })
      },
      convertTreeListToKeyLeafPairs(treeList, keyLeafPair = []) {
        for(const {key, isLeaf, children} of treeList) {
          keyLeafPair.push({key, isLeaf})
          if(children && children.length > 0) {
            this.convertTreeListToKeyLeafPairs(children, keyLeafPair)
          }
        }
        return keyLeafPair;
      },
      emptyCurrForm() {
        this.form.resetFields()
      },
      loadData(){
        this.loading = true;
        queryTreeListForRole().then((res) => {
          this.treeData = res.result.treeList
          this.allTreeKeys = res.result.ids
          const keyLeafPairs = this.convertTreeListToKeyLeafPairs(this.treeData)
          queryDepartPermission({departId:this.departId}).then((res)=>{
            // 过滤出 leaf node 即可，即选中的
            // Tree组件中checkStrictly默认为false的时候，选中子节点，父节点会自动设置选中或半选中
            // 保存 checkedKeys 以及 halfCheckedKeys 以便于未做任何操作时提交表单数据
            const checkedKeys = [...res.result].filter(key => {
              const keyLeafPair = keyLeafPairs.filter(item => item.key === key)[0]
              return keyLeafPair && keyLeafPair.isLeaf
            })
            const halfCheckedKeys = [...res.result].filter(key => {
              const keyLeafPair = keyLeafPairs.filter(item => item.key === key)[0]
              return keyLeafPair && !keyLeafPair.isLeaf
            })
            this.checkedKeys = [...checkedKeys];
            this.halfCheckedKeys = [...halfCheckedKeys]
            this.defaultCheckedKeys = [...halfCheckedKeys, ...checkedKeys];
            this.expandedKeysss = this.allTreeKeys;
            this.loading = false;
          })
        })
      }
    },
  }
</script>

<style scoped>

</style>