<template>
  <div class="tenant-padding" :class="[`${prefixCls}`]">
    <div class="my-tenant">
      <span style="flex: 1">我的组织</span>
      <span class="invited" @click="invitedClick">我的受邀信息<span class="approved-count" v-if="invitedCount>0">{{invitedCount}}</span></span>
    </div>
    <div class="tenant-list" v-if="dataSource.length>0">
      <div v-for="item in dataSource" class="tenant-list-item" @click="drownClick(item)">
        <div class="tenant-title">
          <div class="item-left">
            <div class="item-name">{{ item.name }}</div>
            <div class="vip-message">
              <div class="item-house" @click.stop="copyClick(item.houseNumber)">
                <span>
                  组织门牌号：{{ item.houseNumber }}
                  <Icon icon="ant-design:copy-outlined" style="font-size: 13px; margin-left: 2px" />
                </span>
              </div>
            </div>
          </div>
          <div class="item-right">
            <span v-if="item.userTenantStatus === '3'">
              <span class="pointer examine">待审核</span>
              <span class="pointer cancel-apply" @click.stop="cancelApplyClick(item.tenantUserId)">取消申请</span>
            </span>
            <span v-else-if="item.userTenantStatus === '5'">
              <span class="pointer examine" @click="joinOrRefuseClick(item.tenantUserId,'1')">加入</span>
              <span class="pointer cancel-apply" @click.stop="joinOrRefuseClick(item.tenantUserId,'4')">拒绝</span>
            </span>
            <div v-else style="width: 75px"></div>
            <span style="margin-left: 24px">
              <Icon v-if="item.show" icon="ant-design:down-outlined" style="font-size: 13px; color: #707070" />
              <Icon v-else icon="ant-design:right-outlined" style="font-size: 13px; color: #707070" />
            </span>
          </div>
        </div>
        <div class="item-content" v-show="item.show">
          <div class="content-box">
            <div class="content-name"> 组织名片 </div>
            <div class="content-desc">
              <div class="flex-flow">
                <div class="content-des-text">姓名</div>
                <div style="font-size: 13px;color: #000000">
                  {{ userDetail.realname }}
                </div>
              </div>
              <div class="flex-flow">
                <div class="content-des-text">部门</div>
                <div style="font-size: 13px">
                  {{ userDetail.orgCodeTxt ? userDetail.orgCodeTxt : '未填写' }}
                </div>
              </div>
              <div class="flex-flow">
                <div class="content-des-text">职业</div>
                <div style="font-size: 13px">
                  {{ userDetail.postText ? userDetail.postText : '未填写' }}
                </div>
              </div>
            </div>
          </div>
          <div class="footer-box">
            <span
              v-if="item.userTenantStatus !== '3'"
              @click.stop="footerClick('editTenant', item)"
              class="font-color333 flex-center margin-right40 font-size13 pointer"
            >
              <Icon icon="ant-design:edit-outlined" class="footer-icon" />
              <span>查看租户名片</span>
            </span>
            <span v-else class="font-color9e flex-center margin-right40 font-size13">
              <Icon icon="ant-design:edit-outlined" class="footer-icon" />
              <span>查看租户名片</span>
            </span>
            <span
              v-if="item.userTenantStatus !== '3'"
              @click.stop="footerClick('exitTenant', item)"
              class="font-color333 flex-center margin-right40 font-size13 pointer"
            >
              <Icon icon="ant-design:export-outlined" class="footer-icon" />
              <span>退出租户</span>
            </span>
            <span v-else class="font-color9e flex-center margin-right40 font-size13">
              <Icon icon="ant-design:export-outlined" class="footer-icon" />
              <span>退出租户</span>
            </span>
          </div>
        </div>
      </div>
    </div>
    <a-empty v-else description="暂无数据" style="position: relative;top: 50px;"/>
  </div>
  <a-modal v-model:open="tenantVisible" width="400px" wrapClassName="edit-tenant-setting">
    <template #title>
      <div style="font-size: 17px; font-weight: 700">查看名片</div>
      <div style="color: #9e9e9e; margin-top: 10px; font-size: 13px"> 名片是您在该组织下的个人信息，只在本组织中展示。 </div>
    </template>
    <div style="margin-top: 24px; font-size: 13px; padding: 0 24px">
      <div class="font-color75">姓名</div>
      <div class="margin-top6 margin-bottom-16">{{ userDetail.realname }}</div>
      <div>部门</div>
      <div class="margin-top6 margin-bottom-16">{{ userDetail.orgCodeTxt ? userDetail.orgCodeTxt : '未填写' }}</div>
      <div>职位</div>
      <div class="margin-top6 margin-bottom-16">{{ userDetail.postText ? userDetail.postText : '未填写' }}</div>
      <div>工作地点</div>
      <div class="margin-top6 margin-bottom-16">{{ userData.workPlace ? userData.workPlace : '未填写' }}</div>
      <div>工号</div>
      <div class="margin-top6 margin-bottom-16">{{ userDetail.workNo ? userDetail.workNo : '未填写' }}</div>
    </div>
  </a-modal>

  <!-- 退出租户 -->
  <a-modal v-model:open="cancelVisible" width="800" destroy-on-close>
    <template #title>
      <div class="cancellation">
        <Icon icon="ant-design:warning-outlined" style="font-size: 20px;color: red"/>
        退出租户 {{myTenantInfo.name}}
      </div>
    </template>
    <a-form :model="formCancelState" ref="cancelTenantRef">
      <a-form-item name="tenantName">
        <a-row :span="24" style="padding: 20px 20px 0;font-size: 13px">
          <a-col :span="24">
            请输入租户名称
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <a-input v-model:value="formCancelState.tenantName" @change="tenantNameChange"/>
          </a-col>
        </a-row>
      </a-form-item>
      <a-form-item name="loginPassword">
        <a-row :span="24" style="padding: 0 20px;font-size: 13px">
          <a-col :span="24">
            请输入您的登录密码
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <a-input-password v-model:value="formCancelState.loginPassword" />
          </a-col>
        </a-row>
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button type="primary" @click="handleOutClick" :disabled="outBtnDisabled">确定</a-button>
      <a-button @click="handleCancelOutClick">取消</a-button>
    </template>
  </a-modal>

  <a-modal
    title="变更拥有者"
    v-model:open="owenVisible"
    width="800"
    destroy-on-close
    :cancelButtonProps="{display:'none'}"
    @ok="changeOwen">
      <div style="padding: 20px">
        <a-row :span="24">
          <div class="change-owen">
            只有变更拥有着之后,才能退出
          </div>
        </a-row>
        <a-row :span="24" style="margin-top: 10px">
          <UserSelect v-model:value="tenantOwen" izExcludeMy/>
        </a-row>
      </div>
  </a-modal>
  
  <!-- begin 我的受邀信息 -->
  <a-modal title="我的受邀信息" v-model:open="invitedVisible" :footer="null">
      <a-row :span="24" class="invited-row">
        <a-col :span="16">
          组织
        </a-col>
        <a-col :span="8">
          操作
        </a-col>
      </a-row>
    <a-row :span="24" class="invited-row-list" v-for="item in invitedList">
      <a-col :span="16">
        {{item.name}}
      </a-col>
      <a-col :span="8">
        <span class="common" @click="joinOrRefuseClick(item.tenantUserId,'1')">加入</span>
        <span class="common refuse" @click="joinOrRefuseClick(item.tenantUserId,'4')">拒绝</span>
      </a-col>
    </a-row>
    <div style="height: 20px"></div>
  </a-modal>
  <!-- end 我的受邀信息 -->
</template>

<script lang="ts" name="tenant-setting" setup>
import { onMounted, ref, unref } from "vue";
import { getTenantListByUserId, cancelApplyTenant, exitUserTenant, changeOwenUserTenant, agreeOrRefuseJoinTenant } from "./UserSetting.api";
import { useUserStore } from "/@/store/modules/user";
import { CollapseContainer } from "/@/components/Container";
import { getFileAccessHttpUrl, userExitChangeLoginTenantId } from "/@/utils/common/compUtils";
import headerImg from "/@/assets/images/header.jpg";
import {useMessage} from "/@/hooks/web/useMessage";
import { initDictOptions } from '/@/utils/dict';
import { uniqWith } from 'lodash-es';
import { Modal } from 'ant-design-vue';
import UserSelect from '/@/components/Form/src/jeecg/components/userSelect/index.vue';
import {router} from "/@/router";
import { useDesign } from '/@/hooks/web/useDesign';

const { prefixCls } = useDesign('j-user-tenant-setting-container');
//数据源
const dataSource = ref<any>([]);
const userStore = useUserStore();

//数据源
const { createMessage } = useMessage();
//部门字典
const departOptions = ref<any>([]);
//租户编辑是或否隐藏
const tenantVisible = ref<boolean>(false);
//用户数据
const userData = ref<any>([]);
//用户
const userDetail = ref({
  realname: userStore.getUserInfo.realname,
  workNo: userStore.getUserInfo.workNo,
  orgCodeTxt: userStore.getUserInfo.orgCodeTxt,
  postText: userStore.getUserInfo.postText,
});
/**
 * 初始化租户数据
 */
  async function initDataSource() {
  //获取用户数据
    //update-begin---author:wangshuai ---date:20230109  for: [QQYUN-3645]个人设置我的租户查询审核中和正常的------------
    //update-begin---author:wangshuai ---date:202307049  for：[QQYUN-5608]用户导入后，邀请后,被导入人同意即可,新增被邀信息-----------
    getTenantListByUserId({ userTenantStatus: '1,3,5' }).then((res) => {
      if (res.success) {
        if(res.result && res.result.length>0){
          let result = res.result;
          //存放正常和审核中的数组
          let normal:any = [];
          //存放受邀的信息
          let invited:any = [];
          for (let i = 0; i < result.length; i++) {
            let status = result[i].userTenantStatus;
            //状态为邀请的放入invited数组中
            if(status === '5'){
              invited.push(result[i]);
            }
            normal.push(result[i]);
          }
          dataSource.value = normal;
          invitedList.value = invited;
          invitedCount.value = invited.length;
        }else{
          setInitedValue();
        }
      } else {
        setInitedValue();
    //update-end---author:wangshuai ---date:202307049  for：[QQYUN-5608]用户导入后，邀请后,被导入人同意即可,新增被邀信息------------
      }
    });
    //update-end---author:wangshuai ---date:20230109  for：[QQYUN-3645]个人设置我的租户查询审核中和正常的------------
  }
  function setInitedValue() {
    dataSource.value = [];
    invitedList.value = [];
    invitedCount.value = 0;  
  }

  /**
   * 复制门户
   * @param value
   */
  function copyClick(value) {
    // 创建input元素
    const el = document.createElement('input');
    // 给input元素赋值需要复制的文本
    el.setAttribute('value', value);
    // 将input元素插入页面
    document.body.appendChild(el);
    // 选中input元素的文本
    el.select();
    // 复制内容到剪贴板
    document.execCommand('copy');
    // 删除input元素
    document.body.removeChild(el);
    createMessage.success('复制成功');
  };

  /**
   * 取消申请
   * @param id
   */
  function cancelApplyClick(id) {
    Modal.confirm({
      title: '取消申请',
      content: '是否取消申请',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        cancelApplyTenant({ tenantId: id }).then((res) => {
          if (res.success) {
            createMessage.success('取消申请成功');
            initDataSource();
          }else{
            createMessage.warning(res.message);
          }
        }).catch((e)=>{
           createMessage.warning(e.message);
        });
      },
    });
  };

  /**
   * 展开关闭事件
   */
  function drownClick(value) {
    if (!value.show) {
      value.show = true;
    } else {
      value.show = false;
    }
  };

  /**
   * 获取部门文本
   * @param value
   */
  function getDepartText(value) {
    let arr = departOptions.value.filter((item) => {
      item.value == value;
    });
    if (arr && arr.length > 0) {
      return arr[0].label;
    }
    return '未填写';
  };

  /**
   * 底部文本点击事件
   */
  function footerClick(type, item) {
    userData.value = item;
    //编辑组织名片
    if (type === 'editTenant') {
      tenantVisible.value = true;
    }else if(type === 'exitTenant'){
      //退出租户
      formCancelState.value = {loginPassword:'', tenantName:''};
      outBtnDisabled.value = true;
      cancelVisible.value = true;
      myTenantInfo.value = item;
    }
  }

  //退出租户弹窗
  const cancelVisible = ref<boolean>(false);
  //退出租户数据
  const formCancelState = ref<any>({});
  //租户数据
  const myTenantInfo = ref<any>({});
  //注销租户弹窗确定按钮是否可以点击
  const outBtnDisabled = ref<boolean>(true);
  //拥有者
  const tenantOwen = ref<string>('');
  //拥有者弹窗
  const owenVisible = ref<boolean>(false);

  /**
   * 租户名称值改变事件
   */
  function tenantNameChange() {
    let name = unref(myTenantInfo).name;
    let tenantName = unref(formCancelState).tenantName;
    if(name === tenantName){
      outBtnDisabled.value = false;
    }else{
      outBtnDisabled.value = true;
    }
  }

  /**
   * 退出确定点击事件
   */
  async function handleOutClick() {
    if(!unref(formCancelState).loginPassword){
        createMessage.warning("请输入登录密码");
        return;
    }
    console.log("myTenantInfo::::",myTenantInfo);
    await exitUserTenant({ id: unref(myTenantInfo).tenantUserId, loginPassword: unref(formCancelState).loginPassword }).then((res) => {
      if (res.success) {
        createMessage.success(res.message);
        cancelVisible.value = false;
        initDataSource();
        userExitChangeLoginTenantId(unref(myTenantInfo).tenantUserId);
      } else {
        if (res.message === 'assignedOwen') {
          //需要指定变更者
          owenVisible.value = true;
          cancelVisible.value = false;
        //update-begin---author:wangshuai ---date:20230426  for：【QQYUN-5270】名下租户全部退出后，再次登录，提示租户全部冻结。拥有者提示前往注销------------
        }else if(res.message === 'cancelTenant'){
          cancelVisible.value = false;
          let fullPath = router.currentRoute.value.fullPath;
          Modal.confirm({
            title: '您是该组织的拥有者',
            content: '该组织下没有其他成员，需要您前往注销',
            okText: '前往注销',
            okType: 'danger',
            cancelText: '取消',
            onOk: () => {
              if(fullPath === '/system/usersetting'){
                return;
              }
              router.push('/myapps/settings/organization/organMessage/'+unref(myTenantInfo).tenantUserId)
            }
          })
        //update-end---author:wangshuai ---date:20230426  for：【QQYUN-5270】名下租户全部退出后，再次登录，提示租户全部冻结。拥有者提示前往注销------------
        } else {
          createMessage.warning(res.message);
        }
      }
    }).catch((res) => {
      createMessage.warning(res.message);
    })
  }

  /**
   * 退出租户取消事件
   */
  function handleCancelOutClick() {
    cancelVisible.value = false;
    outBtnDisabled.value = true;
  }

  /**
   * 变更拥有着
   */
  function changeOwen() {
    if(!unref(tenantOwen)){
      createMessage.warning("请选择变更拥有者");
      return;
    }
    changeOwenUserTenant({ userId:unref(tenantOwen), tenantId:unref(myTenantInfo).tenantUserId }).then((res) =>{
      if(res.success){
        createMessage.success(res.message);
        initDataSource();
        //update-begin---author:wangshuai---date:2023-10-23---for:【QQYUN-6822】7、登录拥有多个租户身份的用户，退出租户，只剩下一个租户后显示为空---
        userExitChangeLoginTenantId(unref(myTenantInfo).tenantUserId);
        //update-end---author:wangshuai---date:2023-10-23---for:【QQYUN-6822】7、登录拥有多个租户身份的用户，退出租户，只剩下一个租户后显示为空---
      } else {
        createMessage.warning(res.message);
      }
    })
  }
  
  //邀请数量
  const invitedCount = ref<number>(0);
  //受邀信息
  const invitedList = ref<any>([]);
  //受邀信息弹窗
  const invitedVisible = ref<boolean>(false);

  /**
   * 受邀信息点击事件
   */
  function invitedClick() {
    invitedVisible.value = true;
  }

  /**
   * 加入组织点击事件
   */
  async function joinOrRefuseClick(tenantId,status) {
    await agreeOrRefuseJoinTenant( { tenantId:Number.parseInt(tenantId), status:status });
    initDataSource();
  }

  onMounted(() => {
    initDataSource();
  });

</script>

<style lang="less" scoped>
.tenant-padding{
  padding: 30px 40px 0 20px;
}
.my-tenant{
  display: flex;
  font-size: 17px;
  font-weight: 700!important;
  /*begin 兼容暗夜模式*/
  color: @text-color;
  /*end 兼容暗夜模式*/
  margin-bottom: 20px;
  .invited{
    font-size: 14px;
    text-align: right;
    cursor: pointer;
  }
}
.tenant-list{
  box-sizing: border-box;
  flex: 1;
  min-height: 0;
  overflow-x: hidden;
}
.tenant-list-item{
  /*begin 兼容暗夜模式*/
  border: 1px solid @border-color-base;
  /*end 兼容暗夜模式*/
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  overflow: hidden;
  padding: 0 25px;
  width: 100%;
  .item-name{
    align-items: center;
    box-sizing: border-box;
    display: flex;
    justify-content: space-between;
    padding: 14px 0;
    cursor: pointer;
    font-size:17px;
    /*begin 兼容暗夜模式*/
    color: @text-color;
    /*end 兼容暗夜模式*/
    font-weight: 700!important;
  }
}
.tenant-list-item:hover{
  box-shadow: 0 1px 2px 0 rgba(0,0,0,0.2);
}
.pointer {
  cursor: pointer;
}

.examine {
  color: #2c9cff;
  font-size: 13px;
}

.cancel-apply {
  margin-left: 24px;
  color: red;
  font-size: 13px;
}

.item-content {
  transition: ease-in 2s;

  .content-box {
    /*begin 兼容暗夜模式*/
    border-top: 1px solid @border-color-base;
    /*end 兼容暗夜模式*/
    box-sizing: border-box;
    display: flex;
    padding: 24px 0;
  }

  .content-name {
    /*begin 兼容暗夜模式*/
    color: @text-color;
    /*end 兼容暗夜模式*/
    text-align: center;
    width: 100px;
    font-size: 13px;
  }

  .content-desc {
    flex: 1;
    min-width: 0;
  }

  .content-des-text {
    /*begin 兼容暗夜模式*/
    color: @text-color;
    /*end 兼容暗夜模式*/
    text-align: left;
    width: 76px;
    font-size: 13px;
  }
}

.flex-flow {
  display: flex;
  min-width: 0;
}

.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 0;
}

.footer-box {
  /*begin 兼容暗夜模式*/
  border-top: 1px solid @border-color-base;
  /*end 兼容暗夜模式*/
  box-sizing: border-box;
  display: flex;
  padding: 24px 0;
  color: #757575;
}

.margin-right40 {
  margin-right: 40px;
}

/*begin 兼容暗夜模式*/
.font-color333 {
  color: @text-color;
  font-weight: normal;
}

.font-color9e {
  color: @text-color;
}

.font-color75 {
  color: @text-color;
}
/*end 兼容暗夜模式*/

.font-size13 {
  font-size: 13px;
}

.footer-icon {
  font-size: 13px !important;
  margin-right: 13px;
  position: relative;
  top: 0px;
}
:deep(.edit-tenant-setting) {
  color: #0a8fe9;
}
.margin-top6 {
  margin-top: 6px;
}
.margin-bottom-16 {
  margin-bottom: 16px;
}
.item-right {
  align-items: center;
  display: flex;
  .buy-margin{
    margin-left: 10px;
    width: 66px;
    border-radius: 20px;
    background: rgba(255, 154, 0, 1);
    height: 28px;
    line-height: 28px;
    cursor: pointer;
    text-align: center;
    span{
      font-size: 14px;
      font-weight: 400;
      color: #ffffff;
    }
  }
  .ordinary-user{
    margin-left: 10px;
    width: 66px;
    span{
      font-size: 14px;
      font-weight: 400;
      color: #9e9e9e;
    }
  }
}
.tenant-title {
  align-items: center;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  padding: 24px 0;
  .vip-message{
    display: flex;
    .vip-message-margin{
      margin-right: 20px;
    }
  }
}
.change-owen{
  font-size: 14px;
  font-weight: 700;
}
//update-begin---author:wangshuai ---date:20230704  for：被邀弹窗样式------------
.approved-count{
  background: #ffd2d2;
  border-radius: 19px;
  color: red;
  display: inline-block;
  font-weight: 500;
  height: 19px;
  line-height: 18px;
  margin-left: 8px;
  min-width: 19px;
  padding: 0 6px;
  text-align: center;
}

.invited-row{
  padding: 10px 34px;
}
.invited-row-list{
  padding: 0px 34px;
  .common{
    color: #1e88e5;
    cursor: pointer;
  }
  .refuse{
    color: red;
    margin-left: 20px;
  }
}
.pointer{
  cursor: pointer;
}
//update-end---author:wangshuai ---date:20230704  for：被邀弹窗样式------------
</style>

<style lang="less">
  // update-begin-author:liusq date:20230625 for: [issues/563]暗色主题部分失效
@prefix-cls: ~'@{namespace}-j-user-tenant-setting-container';
/*begin 兼容暗夜模式*/
.@{prefix-cls} {

  .my-tenant{
    color: @text-color;
  }

  .tenant-list-item{
    border: 1px solid @border-color-base;

    .item-name{
      color: @text-color;
    }
  }

  .item-content {

    .content-box {
      border-top: 1px solid @border-color-base;
    }

    .content-name {
      color: @text-color;
    }

    .content-des-text {
      color: @text-color;
    }
  }
  .footer-box {
    border-top: 1px solid @border-color-base;
  }

  /*begin 兼容暗夜模式*/
  .font-color333 {
    color: @text-color;
  }

  .font-color9e {
    color: @text-color;
  }

  .font-color75 {
    color: @text-color;
  }
}
/*end 兼容暗夜模式*/
  // update-end-author:liusq date:20230625 for: [issues/563]暗色主题部分失效
</style>
