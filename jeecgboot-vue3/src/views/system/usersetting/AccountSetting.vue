<template>
  <div :class="[`${prefixCls}`]">
    <div class="my-account">账户</div>
    <div class="account-row-item clearfix">
      <div class="account-label gray-75">手机</div>
      <span class="gray" v-if="userDetail.phoneText">{{ userDetail.phoneText}}</span>
      <span class="pointer blue-e5 phone-margin" @click="updatePhone" v-if="userDetail.phone">修改</span>
      <span class="pointer blue-e5 phone-margin" @click="bindPhone" v-else>绑定</span>
      <!--      <span class="pointer blue-e5" @click="unbindPhone" v-if="userDetail.phone">解绑?</span>-->
      <!--      <span class="pointer blue-e5" @click="unbindPhone" v-else>绑定?</span>-->
    </div>
    <div class="account-row-item clearfix">
      <div class="account-label gray-75">邮箱</div>
      <span class="gray">{{ userDetail.email ? userDetail.email : '未填写' }}</span>
      <span class="pointer blue-e5 phone-margin" @click="updateEmail">修改</span>
      <!--      <span class="pointer blue-e5" @click="unbindEmail" v-if="userDetail.email">解绑?</span>-->
      <!--      <span class="pointer blue-e5" @click="unbindEmail" v-else>绑定?</span>-->
      <!--      <span class="pointer blue-e5" style="margin-left:5px" @click="checkEmail" v-if="userDetail.email">验证?</span>-->
    </div>
    <div class="account-row-item">
      <div class="account-label gray-75">密码</div>
      <Icon icon="ant-design:lock-outlined" style="color: #9e9e9e" />
      <span class="pointer blue-e5" style="margin-left: 10px" @click="updatePassWord">修改</span>
    </div>

    <!--    <div class="account-row-item clearfix">-->
    <!--      <div class="account-label gray-75">账户注销?</div>-->
    <!--      <span style="color: red" class="pointer" @click="cancellation">注销?</span>-->
    <!--    </div>-->
  </div>

  <UserReplacePhoneModal @register="registerModal" @success="initUserDetail" />
  <UserReplaceEmailModal @register="registerEmailModal" @success="initUserDetail" />
  <UserPasswordModal @register="registerPassModal" @success="initUserDetail" />
</template>
<script lang="ts" setup>
  import { onMounted, ref, reactive } from 'vue';
  import { CollapseContainer } from '/@/components/Container';
  import { getUserData } from './UserSetting.api';
  import { useUserStore } from '/@/store/modules/user';
  import UserReplacePhoneModal from './commponents/UserPhoneModal.vue';
  import UserReplaceEmailModal from './commponents/UserEmailModal.vue';
  import UserPasswordModal from './commponents/UserPasswordModal.vue';
  import { useModal } from '/@/components/Modal';
  import { WechatFilled } from '@ant-design/icons-vue';
  import { useDesign } from '/@/hooks/web/useDesign';

  const { prefixCls } = useDesign('j-user-account-setting-container');

  const userDetail = ref<any>([]);
  const userStore = useUserStore();
  const [registerModal, { openModal }] = useModal();
  const [registerEmailModal, { openModal: openEmailModal }] = useModal();
  const [registerPassModal, { openModal: openPassModal }] = useModal();

  const wechatData = reactive<any>({
    bindWechat: false,
    name: '昵称',
  });

  /**
   * 初始化用户数据
   */
  function initUserDetail() {
    //获取用户数据
    getUserData().then((res) => {
      if (res.success) {
        userDetail.value = res.result;
        if(res.result.phone){
          userDetail.value.phoneText = res.result.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
        }
      }
    });
  }

  /**
   * 修改手机号
   */
  function updatePhone() {
    openModal(true, {
      record: { phone: userDetail.value.phone, username: userDetail.value.username, id: userDetail.value.id, phoneText: userDetail.value.phoneText },
    });
  }
  
  /**
   * 绑定手机号
   */ 
  function bindPhone() {
    openModal(true, {
      record: { username: userDetail.value.username, id: userDetail.value.id },
    });
  }

  /**
   * 修改邮箱
   */
  function updateEmail() {
    openEmailModal(true, {
      record: { email: userDetail.value.email, id: userDetail.value.id },
    });
  }

  /**
   * 密码修改
   */
  function updatePassWord() {
    openPassModal(true, {
      record: { username: userDetail.value.username },
    });
  }

  /**
   * 手机号解绑
   */
  function unbindPhone() {
    console.log('手机号解绑');
  }

  /**
   * 邮箱解绑
   */
  function unbindEmail() {
    console.log('邮箱解绑');
  }

  /**
   * 邮箱验证
   */
  function checkEmail() {
    console.log('邮箱验证');
  }

  /**
   * 微信绑定解绑事件
   */
  function wechatBind() {
    console.log('微信绑定解绑事件');
  }

  /**
   * 注销事件
   */
  function cancellation() {}

  onMounted(() => {
    initUserDetail();
  });
</script>
<style lang="less">
    // update-begin-author:liusq date:20230625 for: [issues/563]暗色主题部分失效
  @prefix-cls: ~'@{namespace}-j-user-account-setting-container';

  .@{prefix-cls}{
     padding: 30px 40px 0 20px;
    .account-row-item {
      align-items: center;
      /*begin 兼容暗夜模式*/
      border-bottom: 1px solid @border-color-base;
      /*end 兼容暗夜模式*/
      box-sizing: border-box;
      display: flex;
      height: 71px;
      position: relative;
    }

    .account-label {
      text-align: left;
      width: 160px;
    }

    .gray-75 {
      /*begin 兼容暗夜模式*/
      color: @text-color !important;
      /*end 兼容暗夜模式*/
    }

    .pointer {
      cursor: pointer;
    }

    .blue-e5 {
      color: #1e88e5;
    }

    .phone-margin {
      margin-left: 24px;
      margin-right: 24px;
    }

    .clearfix:after {
      clear: both;
    }

    .clearfix:before {
      content: "";
      display: table;
    }
    .my-account{
      font-size: 17px;
      font-weight: 700!important;
      /*begin 兼容暗夜模式*/
      color: @text-color;
      /*end 兼容暗夜模式*/
      margin-bottom: 20px;
    }
  }
  // update-end-author:liusq date:20230625 for: [issues/563]暗色主题部分失效
</style>
