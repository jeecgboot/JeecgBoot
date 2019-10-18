<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    width=650
    placement="right"
    :closable="true"
    @close="close"
    :visible="visible"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">

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
          :checkStrictly="checkStrictly">
          <span slot="hasDatarule" slot-scope="{slotTitle,ruleFlag}">
            {{ slotTitle }}<a-icon v-if="ruleFlag" type="align-left" style="margin-left:5px;color: red;"></a-icon>
          </span>
        </a-tree>
      </a-form-item>
    </a-form>

    <div class="drawer-bootom-button">
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
      <a-popconfirm title="确定放弃编辑？" @confirm="close" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
    </div>

    <role-datarule-modal ref="datarule"></role-datarule-modal>

  </a-drawer>

</template>
<script>
  import {queryTreeListForRole,queryRolePermission,saveRolePermission} from '@/api/api'
  import RoleDataruleModal from './RoleDataruleModal.vue'

  export default {
    name: "RoleModal",
    components:{
      RoleDataruleModal
    },
    data(){
      return {
        roleId:"",
        treeData: [],
        defaultCheckedKeys:[],
        checkedKeys:[],
        halfCheckedKeys:[],
        expandedKeysss:[],
        allTreeKeys:[],
        autoExpandParent: true,
        checkStrictly: false,
        title:"角色权限配置",
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
        this.$refs.datarule.show(this.selectedKeys[0],this.roleId)
      },
      onCheck (checkedKeys, { halfCheckedKeys }) {
        // 保存选中的和半选中的，后面保存的时候合并提交
        this.checkedKeys = checkedKeys
        this.halfCheckedKeys = halfCheckedKeys
      },
      show(roleId){
        this.roleId=roleId
        this.visible = true;
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
      handleSubmit(){
        let that = this;
        let checkedKeys = [...that.checkedKeys, ...that.halfCheckedKeys]
        const permissionIds = checkedKeys.join(",")
        let params =  {
          roleId:that.roleId,
          permissionIds,
          lastpermissionIds:that.defaultCheckedKeys.join(","),
        };
        that.loading = true;
        console.log("请求参数：",params);
        saveRolePermission(params).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.loading = false;
            that.close();
          }else {
            that.$message.error(res.message);
            that.loading = false;
            that.close();
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
    },
  watch: {
    visible () {
      if (this.visible) {
        queryTreeListForRole().then((res) => {
          this.treeData = res.result.treeList
          this.allTreeKeys = res.result.ids
          const keyLeafPairs = this.convertTreeListToKeyLeafPairs(this.treeData)
          queryRolePermission({roleId:this.roleId}).then((res)=>{
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
          })
        })
      }
    }
  }
  }

</script>
<style lang="scss" scoped>
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