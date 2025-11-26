<template>
  <div class="account-padding" :class="[`${prefixCls}`]">
    <div class="user-setting-top">
      <div class="account-avatar">
        <CropperAvatar
          :uploadApi="uploadImg"
          :showBtn="false"
          :value="avatar"
          :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
          @change="updateAvatar"
          width="80"
        />
        <div class="account-right border-bottom">
          <div v-if="!isEdit">
            <span class="font-size-17 account-name">{{ userInfo.realname }}</span>
            <a-tooltip content="编辑姓名">
              <Icon class="pointer font-size-17 gray-bd account-icon" icon="ant-design:edit-outlined"
                    @click="editHandleClick" />
            </a-tooltip>
          </div>
          <div v-else>
            <a-input ref="accountNameEdit" :maxlength="100" v-model:value="userInfo.realname" @blur="editRealName" />
          </div>
          <div class="use-day">
            使用：<span>{{userInfo.createTimeText}}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="account-data border-bottom">
      <!-- 详细资料 -->
      <div class="account-detail">
        <div class="font-size-15 font-bold font-color-gray" style="margin-bottom: 16px">详细资料</div>
        <div class="margin-bottom-10 font-size-13">
          <span class="gray-75 item-label">生日</span>
          <span class="gray-3">{{ userInfo.birthday }}</span>
        </div>
        <div class="margin-bottom-10 font-size-13">
          <span class="gray-75 item-label">性别</span>
          <span class="gray-3">{{ userInfo.sexText }}</span>
        </div>
        <div class="margin-bottom-10 nowarp font-size-13">
          <span class="gray-75 item-label">职位</span>
          <span class="gray-3">{{ userInfo.postText ? userInfo.postText : "未填写" }}</span>
        </div>
        <div class="font-size-13">
          <span class="item-label"></span>
          <span class="item-label pointer" style="color:#1e88e5" @click="openEditModal">编辑</span>
        </div>
      </div>
      <!-- 联系信息 -->
      <div class="account-info">
        <div class="font-size-15 font-bold font-color-gray" style="margin-bottom: 16px">联系信息</div>
        <div class="margin-bottom-10 font-size-13">
          <span class="gray-75 item-label">邮箱</span>
          <span class="gray-3">{{ userInfo.email ? userInfo.email : "未填写" }}</span>
        </div>
        <div class="margin-bottom-10 font-size-13">
          <span class="gray-75 item-label">手机</span>
          <span class="gray-3">{{ userInfo.phone ? userInfo.phone : "未填写" }}</span>
        </div>
      </div>
    </div>
    <div class="account-data">
      <!-- 个性签名 -->
      <div class="account-detail">
        <div class="font-size-15 font-bold font-color-gray" style="margin-bottom: 16px">个性签名</div>
        <div class="font-size-13 flex">
          <span class="gray-75 item-label">签名</span>
          <a-upload
            accept="jpg,jpeg,png"  
            :max-count="1"
            :multiple="false"
            name = "file"
            :headers="uploadHeader"
            :action="uploadUrl"
            v-model:fileList="uploadFileList"
            @beforeUpload="beforeUpload"
            @change="handleChange"
            list-type="picture-card"
            @preview="handlePreview"
          >
            <div v-if="uploadVisible">
              <UploadOutlined></UploadOutlined>
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
          <a-modal :width="500" :open="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </div>
        <div style="font-size: 12px;color:#93a6aa" class="margin-bottom-10">
          <p>建议上传尺寸为200*80，大小不超过1M且格式为png或jpeg的图片</p>
          <p>生成签名方法一：手写扫描进行上传。</p>
          <p>生成签名方法二：使用在线转换，生成后进行上传。
            <a href="http://www.diyiziti.com/qianming" target="_blank">http://www.diyiziti.com/qianming</a>
          </p>
        </div>
        <div class="margin-bottom-10 font-size-13 flex" style="margin-top: 10px">
          <span class="gray-75 item-label">开启状态</span>
          <a-switch v-model:checked="userInfo.signEnable" :checkedValue="1" :unCheckedValue="0" @change="handleEnableSignChange"></a-switch>
        </div>
      </div>
    </div>
  </div>
  <UserAccountModal @register="registerModal" @success="getUserDetail"></UserAccountModal>
</template>
<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { CollapseContainer } from '/@/components/Container';
import { CropperAvatar } from '/@/components/Cropper';
import { useMessage } from '/@/hooks/web/useMessage';
import headerImg from '/@/assets/images/header.jpg';
import { defHttp } from '/@/utils/http/axios';
import { useUserStore } from '/@/store/modules/user';
import { uploadImg } from '/@/api/sys/upload';
import { getFileAccessHttpUrl, getRandom } from '/@/utils/common/compUtils';
import dayjs from 'dayjs';
import { ajaxGetDictItems, getDictItemsByCode, initDictOptions } from '/@/utils/dict';
import { userEdit, getUserData, queryNameByCodes } from './UserSetting.api';
import UserAccountModal from './commponents/UserAccountModal.vue';
import { useModal } from '/@/components/Modal';
import { cloneDeep } from 'lodash-es';
import { useDesign } from '/@/hooks/web/useDesign';
import { getToken } from "@/utils/auth";
import { uploadUrl } from "@/api/common/api";
import { UploadOutlined } from "@ant-design/icons-vue";

//TODO 当字典租户隔离时，数据会查不到，默认一个
const sexOption = getDictItemsByCode("sex") || [{text:'男',value:'1'},{text:'女',value:'2'}];
const { createMessage } = useMessage();
const userStore = useUserStore();
  const { prefixCls } = useDesign('j-base-setting-container');
//是否编辑
const isEdit = ref<boolean>(false);
//用户信息
const userInfo = ref<any>({});
//编辑时input触发事件
const accountNameEdit = ref();
const [registerModal, { openModal }] = useModal();
//头像动态计算
const avatar = computed(() => {
  return getFileAccessHttpUrl(userInfo.value.avatar) || headerImg;
});
//headers
const uploadHeader = computed(() => {
  let headers = {};
  headers['X-Access-Token'] = getToken();
  return headers;
});
const { createMessage: $message } = useMessage();
//上传列表
const uploadFileList = ref<any>([]);
//计算是否可以继续上传
const uploadVisible = computed(() => {
  if(!uploadFileList){
    return true;
  }
  return uploadFileList.value.length < 1;
});
//图片预览
const previewVisible = ref<boolean>(false);
//图片预览路径
const previewImage = ref<string>('');

/**
 * 更新用户头像
 */
function updateAvatar(src: string, data: string) {
  const userinfo = userStore.getUserInfo;
  userinfo.avatar = data;
  userStore.setUserInfo(userinfo);
  if (data) {
    updateUserInfo({ avatar: data, id: userinfo.id });
  }
}

/**
 * 更新用户信息
 * @params 参数
 */
function updateUserInfo(params) {
  userEdit(params).then((res) => {
    if (!res.success) {
      createMessage.warn(res.message);
    }
  });
}

/**
 * 编辑按钮点击事件
 */
function editHandleClick() {
  isEdit.value = true;
  setTimeout(() => {
    accountNameEdit.value.focus();
  }, 100);
}

/**
 * 修改真实姓名
 */
function editRealName() {
  if (userInfo.value.realname) {
    updateUserInfo({ realname: userInfo.value.realname, id: userInfo.value.id });
    userStore.setUserInfo(userInfo.value);
  } else {
    createMessage.warn("请输入姓名");
  }
  isEdit.value = false;
}

/**
 * 获取生日信息
 */
function getBirthDay(val) {
  if (val) {
    return dayjs(val).format("YYYY-MM-DD");
  } else {
    return "未填写";
  }
}

/**
 * 获取性别
 * @param val
 */
function getSex(val) {
  let findOption = sexOption.find(item => parseInt(item.value) === val);
  let sex = "未填写";
  if (findOption) {
    sex = findOption.text;
  }
  return sex;
}

/**
 * 打开编辑弹窗
 */
function openEditModal() {
  let value = cloneDeep(userInfo.value);
  openModal(true, {
    record: value
  });
}

/**
 * 获取用户信息
 */
function getUserDetail() {
  uploadFileList.value = [];
  getUserData().then((async res => {
    if (res.success) {
      if (res.result) {
        res.result.sexText = getSex(res.result.sex);
        res.result.birthday = getBirthDay(res.result.birthday);
        res.result.createTimeText = getDiffDay(res.result.createTime);
        userInfo.value = res.result;
        if(userInfo.value.sign){
          let sign = userInfo.value.sign;
          let url = getFileAccessHttpUrl(sign);
          uploadFileList.value.push({
            uid: getRandom(10),
            name: getFileName(sign),
            status: 'done',
            url: url,
            response: {
              status: 'history',
              message: sign,
            },
          })
        }
      } else {
        userInfo.value = {};
      }
    }
  }));
}

/**
 * 获取使用时间
 * @param date
 */
function getDiffDay(date) {
  // 计算两个日期之间的天数差值
  let totalDays, diffDate
  let createDate = Date.parse(date);
  let nowDate = new Date().getTime();
  // 将两个日期都转换为毫秒格式，然后做差
  diffDate = Math.abs(nowDate - createDate) // 取相差毫秒数的绝对值
  totalDays = Math.floor(diffDate / (1000 * 3600 * 24)) // 向下取整
  return totalDays+" 天";
}

/**
 * 上传图片之前进行验证
 * 
 * @param file
 */
function beforeUpload({ file }) {
  const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png" || file.type === "image/jpg";
  if (!isJpgOrPng) {
    $message.error("上传文件格式只能是jpg/png");
    return false;
  }
  const isLimit = file.size / 1024 / 1024 < 1;
  if (!isLimit) {
    $message.error("上传图片大小不能超过 1MB!");
    return false;
  }
  return true;
}

/**
 * 上传成功事件
 */
function handleChange({ file, fileList }) {
  if (file.status === 'error') {
    createMessage.error(`${file.name} 上传失败.`);
  }
  if (file.status === 'done' && file.response.success === false) {
    const failIndex = uploadFileList.value.findIndex((item) => item.uid === file.uid);
    if (failIndex != -1) {
      uploadFileList.value.splice(failIndex, 1);
    }
    createMessage.warning(file.response.message);
    return;
  }
  if (file.status != 'uploading') {
    fileList.forEach((file) => {
      if (file.status === 'done') {
        //上传成功事件
        uploadSuccess(file.response.message);
      }
    });
    if (file.status === 'removed') {
      handleDelete();
    }
  }
}

/**
 * 移除
 */
function handleDelete() {
  updateUser({ sign: "", id: userInfo.value.id },"删除个性签名成功")
}

/**
 * 上传成功事件
 * @param url
 */
function uploadSuccess(url) {
   updateUser({ sign: url, id: userInfo.value.id },"上传个性签名成功")
}

function updateUser(params,message) {
  userEdit(params).then((res) => {
    if(res.success){
      createMessage.success(message);
    }
  });
}

/**
 * 图片预览
 * @param file
 */
function handlePreview(file) {
  previewImage.value = file.url || file.thumbUrl;
  previewVisible.value = true;
}

/**
 * 获取文件名
 * @param path
 */
function getFileName(path) {
  if (path.lastIndexOf('\\') >= 0) {
    let reg = new RegExp('\\\\', 'g');
    path = path.replace(reg, '/');
  }
  return path.substring(path.lastIndexOf('/') + 1);
}

/**
 * 图片预览关闭
 */
function handleCancel() {
  previewVisible.value = false;
}

/**
 * 个性签名开启状态更新
 */
function handleEnableSignChange() {
  updateUser({ signEnable: userInfo.value.signEnable, id: userInfo.value.id },"修改成功")
}

onMounted(async () => {
  getUserDetail();
});
</script>

<style lang="less">
    // 代码逻辑说明: [issues/563]暗色主题部分失效
  @prefix-cls: ~'@{namespace}-j-base-setting-container';

  .@{prefix-cls}{
    .user-setting-top {
      padding-top: 40px;
      width: 100%;
      border-bottom: 1px solid @border-color-base;
      display: flex;
      padding-bottom: 40px;
    }

    .account-avatar {
      align-items: center;
      display: flex;
      margin-right: 30px;
      flex: 1;
    }

    .change-avatar {
      img {
        display: block;
        margin-bottom: 15px;
        border-radius: 50%;
      }
    }

    .account-right {
      margin-left: 25px !important;
    }

    .font-size-15 {
      font-size: 15px;
    }

    .font-size-17 {
      font-size: 17px;
    }

    .pointer {
      cursor: pointer;
    }

    .account-name {
      white-space: nowrap;
      overflow: hidden;
      width: 200px;
      text-overflow: ellipsis;
      line-height: 32px !important;
      /*begin 兼容暗夜模式*/
      color: @text-color;
      /*end 兼容暗夜模式*/
      font-weight: 500;
    }

    .gray-bd {
      color: #bdbdbd;
    }

    .account-icon {
      margin-left: 4px;
    }

    .account-data {
      width: 100% !important;
      display: flex;
      min-width: 0;
    }

    .account-detail {
      width: 40%;
      display: flex;
      flex-direction: column;
      padding: 40px 0;

      .item-label {
        display: inline-block;
        text-align: left;
        width: 80px;
      }
    }

    .font-bold {
      font-weight: 700 !important;
    }

    .margin-bottom-10 {
      margin-bottom: 10px;
    }

    .account-info {
      width: 60%;
      display: flex;
      flex-direction: column;
      padding: 40px 0;
      box-sizing: border-box;
      margin-left: 10px;

      .item-label {
        display: inline-block;
        text-align: left;
        width: 80px;
      }
    }

    .nowarp {
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    .account-padding {
      padding-left: 20px !important;
      padding-right: 40px !important;
    }

    .use-day {
      /*begin 兼容暗夜模式*/
      color: @text-color;
      /*end 兼容暗夜模式*/
      margin-top: 10px;
      font-size: 13px;
      span {
        color: #1e88e5;
        margin-left: 5px;
      }
    }
    .font-size-13 {
      font-size: 13px;
    }
    .ant-upload-select,.ant-upload-list-item-container{
      width: 200px !important;
      height: 80px !important;
    }
    .ant-upload-list-item-thumbnail .ant-upload-list-item-image {
      object-fit: cover !important;
    }
    p{
      margin-bottom: 5px;
    }
    .border-bottom{
      border-bottom: 1px solid @border-color-base;
    }
  }
</style>
