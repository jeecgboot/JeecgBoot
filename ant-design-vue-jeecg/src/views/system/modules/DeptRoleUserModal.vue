<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    width=600
    placement="right"
    :closable="true"
    @close="close"
    :visible="visible"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">

    <a-spin :spinning="confirmLoading">

      <a-form :form="form" v-if="designNameOption.length>0">
        <a-form-item label=''>
          <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
            <a-card :style="{ marginTop: '12px',height:'auto' }">
              <a-checkbox-group @change="designNameChange" v-model="designNameValue" style="width: 100%">
                <a-row>
                  <template v-for="(des) in designNameOption">
                    <a-col :span="6">
                      <a-checkbox :value="des.value">{{ des.text }}</a-checkbox>
                    </a-col>
                  </template>
                </a-row>
              </a-checkbox-group>
            </a-card>
          </a-col>
        </a-form-item>
      </a-form>
      <div v-else><h3>无可配置角色!</h3></div>
    </a-spin>
    <div class="drawer-bootom-button">
      <a-dropdown style="float: left" :trigger="['click']" placement="topCenter">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="checkALL">全部勾选</a-menu-item>
          <a-menu-item key="2" @click="cancelCheckALL">取消全选</a-menu-item>
        </a-menu>
        <a-button>
          操作 <a-icon type="up" />
        </a-button>
      </a-dropdown>
      <a-popconfirm  title="确定放弃编辑？" @confirm="close" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit(true)" type="primary">保存</a-button>
    </div>
  </a-drawer>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import  JEllipsis  from '@/components/jeecg/JEllipsis'
  import {initDictOptions} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'DeptRoleUserModal',
    components: {
      JEllipsis
    },
    data() {
      return {
        currentDeptId:"",
        title: "部门角色分配",
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {},
        url: {
          add: "/sys/sysDepartRole/deptRoleUserAdd",
          getDeptRoleList:"/sys/sysDepartRole/getDeptRoleList",
          getDeptRoleByUserId:"/sys/sysDepartRole/getDeptRoleByUserId"
        },
        designNameOption: [],
        userId: "",
        newRoleId:"",
        oldRoleId:"",
        designNameValue:[],
        desformList: [],
      }
    },
    created() {

    },
    methods: {
      add(record,departId) {
        this.userId = record.id;
        this.currentDeptId = departId;
        this.loadDesformList();
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        getAction(this.url.getDeptRoleByUserId,{userId:this.userId}).then((res) => {
          if (res.success) {
            var designName = [];
            for (let value of res.result) {
              designName.push(value.droleId)
            }
            this.oldRoleId=designName.join(",");
            this.designNameValue = designName;
            this.newRoleId = designName.join(",");
          }
        });
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleSubmit() {
        const that = this;
        // 触发表单验证
        that.confirmLoading = true;
        let  httpurl = this.url.add;
        let  method = 'post';
        let formData = Object.assign(this.model, {});
        //时间格式化
        formData.userId = this.userId;
        formData.newRoleId=this.newRoleId;
        formData.oldRoleId=this.oldRoleId;
        httpAction(httpurl, formData, method).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.$emit('reload');
            that.$emit('ok');
          } else {
            that.$message.warning(res.message);
          }
        }).finally(() => {
          that.confirmLoading = false;
          that.close();
        })
      },
      handleCancel() {
        this.designNameOption=[];
        this.designNameValue=[];
        this.close()
      },
      designNameChange(selectedValue) {
        this.newRoleId=selectedValue.join(",");
      },
      checkALL(){
        var designName = [];
        for (let value of this.desformList) {
          designName.push(
            value.id
          )
        }
        this.designNameValue = designName;
        this.newRoleId=designName.join(",");
      },
      cancelCheckALL(){
        this.designNameValue=[];
        this.newRoleId="";
      },
      /** 加载desform */
      loadDesformList() {
        getAction(this.url.getDeptRoleList, { departId: this.currentDeptId, userId:this.userId }).then((res) => {
          if (res.success) {
            this.desformList = res.result
            var designName = [];
            for (let value of this.desformList) {
              designName.push({
                value: value.id,
                text: value.roleName,
              })
            }
            this.designNameOption = designName;
          }
        });
      },
    }

  }
</script>

<style scoped>
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