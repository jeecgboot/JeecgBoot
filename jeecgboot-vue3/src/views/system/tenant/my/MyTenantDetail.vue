<!--我的租户详情-->
<template>
  <div class="message-set-container">
    <div class="message-set-box">
      <div class="message-set-header">
        <span class="font17">组织信息</span>
      </div>
      <a-form :model="formState">
        <div class="message-set-content">
          <div class="common-info">
            <div class="common-info-row">
              <div class="common-info-row-label">组织LOGO</div>
              <div class="common-info-row-content">
                <JImageUpload v-model:value="formState.companyLogo" @change="handleCompanyLogoChange"></JImageUpload>
              </div>
            </div>
            <div class="common-info-row m-top24">
              <div class="common-info-row-label">组织名称</div>
              <span class="m-right16">{{ formState.name }}</span>
              <span class="edit-name" @click="goUpdate('name')">修改</span>
            </div>
            <div class="common-info-row m-top24">
              <div class="common-info-row-label">组织门牌号</div>
              <div class="common-info-row-content">
                <span class="pointer">
                  <span>{{ formState.houseNumber }}</span>
                </span>
              </div>
            </div>
            <div class="common-info-row m-top24">
              <div class="common-info-row-label">组织编号(ID)</div>
              <div class="common-info-row-content">
                <span class="pointer">
                  <span>{{ formState.id }}</span>
                </span>
              </div>
            </div>
            <div class="split-line"></div>
            <div class="common-info-row">
              <div class="common-info-row-label">所在地</div>
              <span class="m-right16">{{ formState.companyAddress_dictText }}</span>
              <span class="edit-name" @click="goUpdate('companyAddress')">修改</span>
            </div>
            <div class="common-info-row m-top24">
              <div class="common-info-row-label">所在行业</div>
              <span class="m-right16">{{ formState.trade_dictText }}</span>
              <span class="edit-name" @click="goUpdate('trade')">修改</span>
            </div>
            <div class="common-info-row m-top24">
              <div class="common-info-row-label">工作地点</div>
              <span class="m-right16">{{ formState.workPlace }}</span>
              <span class="edit-name" @click="goUpdate('workPlace')">修改</span>
            </div>
            <div class="cancel-split-line"></div>
          </div>
        </div>
      </a-form>
    </div>
  </div>
  <!-- 组织名称修改弹窗 -->
  <a-modal v-model:open="modalVisible.name" title="修改组织名称" width="500" destroy-on-close @ok="doUpdate('name')">
    <a-form ref="manageNameRef" :model="updateInfo" :rules="getManageNameRules">
      <a-form-item name="name" class="form-item-padding">
        <div class="form-group">
              <span class="form-label">
                组织名称
                <span class="txt-middle red">*</span>
              </span>
          <a-input v-model:value="updateInfo.name" />
        </div>
      </a-form-item>
    </a-form>
  </a-modal>
  
  <!-- 组织所在地弹窗 -->
  <a-modal v-model:open="modalVisible.companyAddress" title="所在地" width="500" destroy-on-close @ok="doUpdate('companyAddress')">
    <a-form :model="updateInfo">
      <a-form-item name="companyAddress" class="form-item-padding">
        <div style="margin-top: 20px">
          <j-area-select v-model:value="updateInfo.companyAddress" />
        </div>
      </a-form-item>
    </a-form>
  </a-modal>

  <!-- 组织所在行业弹窗 -->
  <a-modal v-model:open="modalVisible.trade" title="设置所在行业" width="500" destroy-on-close @ok="doUpdate('trade')">
    <a-form :model="updateInfo">
      <a-form-item name="trade" class="form-item-padding">
        <div style="margin-top: 20px">
          <j-dict-select-tag v-model:value="updateInfo.trade" dictCode="trade" />
        </div>
      </a-form-item>
    </a-form>
  </a-modal>

  <!-- 工作地点弹窗 -->
  <a-modal v-model:open="modalVisible.workPlace" title="设置工作地点" width="500" destroy-on-close @ok="doUpdate('workPlace')">
    <a-form ref="workPlaceRef" :model="updateInfo">
      <a-form-item name="name" class="form-item-padding">
        <div style="margin-top: 20px">
          <a-textarea placeholder="请填写工作地点" v-model:value="updateInfo.workPlace" />
        </div>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts" name="tenant-my-tenant-list" setup>
  import { onMounted, reactive, ref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {getFileAccessHttpUrl, tenantSaasMessage} from '@/utils/common/compUtils';
  import { getTenantById, saveOrUpdateTenant } from '@/views/system/tenant/tenant.api';
  import { getTenantId } from '@/utils/auth';
  import { getDataByCode, getRealCode, provinceOptions } from '@/components/Form/src/utils/areaDataUtil';
  import { initDictOptions } from '@/utils/dict';
  import {createImgPreview} from "@/components/Preview";
  import { JImageUpload } from "@/components/Form";
  // import {updateTenantInfo} from "@/views/super/myapps/organization/organization.api";
  import { defHttp } from "/@/utils/http/axios";
  import JAreaSelect from "/@/components/Form/src/jeecg/components/JAreaSelect.vue";
  import JDictSelectTag from "/@/components/Form/src/jeecg/components/JDictSelectTag.vue";

  const { createMessage } = useMessage();
  const formState = reactive({
    id: '',
    name: '',
    houseNumber: '',
    companyAddress_dictText: '',
    trade_dictText: '',
    workPlace: '',
    createBy: '',
    companyLogo: '',
  });
  let tradeOptions: any[] = [];
  //组织名称ref
  const manageNameRef= ref();
  // modal显示
  const modalVisible = reactive<any>({
    name: false,
    trade: false,
    companyAddress: false
  });

  // 组织名称检验规则
  const getManageNameRules =  {
    name: [{ required: true, message: '组织名称不能为空', trigger: 'blur' }],
  };

  //修改对象
  const updateInfo = reactive<any>({
    name: '',
    trade:'',
    companyAddress: '',
    workPlace: '',
  });
  
  /**
   * 初始化租户信息
   */
  async function initTenant() {
    let result = await getTenantById({ id: getTenantId() });
    if (result) {
      if (result.companyAddress) {
        formState.companyAddress_dictText = getPcaText(result.companyAddress);
      } else {
        formState.companyAddress_dictText = '';
      }
      if (result.trade) {
        formState.trade_dictText = await getTradeText(result.trade);
      } else {
        formState.trade_dictText = '';
      }
      Object.assign(formState, result);
    }
  }

  /**
   * 获取省市区文本
   * @param code
   */
  function getPcaText(code) {
    let arr = getRealCode(code, 3);
    console.log("arr:::",arr)
    let provinces: any = provinceOptions.filter((item) => item.value == arr[0]);
    let cities: any[] = getDataByCode(arr[0]);
    let areas: any[] = getDataByCode(arr[1]);
    let str = '';
    if (provinces && provinces.length > 0) {
      str = provinces[0].label;
      if (cities && cities.length > 0) {
        let temp1 = cities.filter((item) => item.value == arr[1]);
        str = str + '/' + temp1[0].label;
        if (areas && areas.length > 0) {
          let temp2 = areas.filter((item) => item.value == arr[2]);
          str = str + '/' + temp2[0].label;
        }
      }
    }
    return str;
  }

  /**
   * 获取行业文本
   *
   * @param trade
   */
  async function getTradeText(trade) {
    if (tradeOptions.length == 0) {
      let options: any = await initDictOptions('trade');
      tradeOptions = options;
    }
    let arr = tradeOptions.filter((item) => item.value == trade);
    if (arr.length > 0) {
      return arr[0].label;
    }
    return '';
  }

  /**
   * 公司logo上传成功事件
   * 
   * @param val
   */
  function handleCompanyLogoChange(val) {
    if(val){
      saveOrUpdateTenant({ id: formState.id, companyLogo: val }, true)
    }
  }

  /**
   * 更新打开弹窗
   * 
   * @param key
   */
  function goUpdate(key){
    modalVisible[key] = true;
    updateInfo[key] = formState[key];
  }

  /**
   * 编辑租户信息
   * @param params
   */
  async function updateTenantInfo(params){
    return defHttp.put({ url: '/sys/tenant/editOwnTenant', params });
  }

  /**
   * 更新数据
   * @param key
   */
  async function doUpdate(key) {
    if(key=='name'){
      await manageNameRef.value.validateFields();
    }
    //所在地为空报错
    if(key == 'companyAddress'){
      if(updateInfo[key] instanceof Array){
        updateInfo[key] = '';
      }
    }
    let params = {
      id: formState.id,
      [key]: updateInfo[key]
    };
    await updateTenantInfo(params);
    initTenant();
    modalVisible[key] = false
  }

  onMounted(() => {
    //提示信息
    tenantSaasMessage('我的租户');
    initTenant();
  });
</script>
<style lang="less" scoped>
  .message-set-container {
    box-sizing: border-box;
    flex: 1;
    margin: 16px;
    min-height: 0;
  }
  .message-set-box {
    background: #fff;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    min-width: 750px;
    position: relative;
    height: 100%;
  }
  .message-set-header {
    align-items: center;
    background: #fff;
    border-bottom: 1px solid #eaeaea;
    box-sizing: border-box;
    color: #333;
    display: flex;
    font-weight: 600;
    height: 57px;
    line-height: 57px;
    padding: 0 18px 0 24px;
  }
  .message-set-content {
    box-sizing: border-box;
    flex: 1;
    min-height: 0;
    overflow-x: hidden;
  }
  .font17 {
    font-size: 17px;
  }
  .common-info {
    padding: 20px 24px;
    background: #ffffff;
    margin-bottom: 20px;
  }
  .common-info-row {
    color: #333;
    display: flex;
    font-size: 13px;
  }
  .common-info-row-label {
    color: #757575;
    display: flex;
    justify-content: flex-start;
    width: 140px;
  }
  .common-info-row-content {
    display: flex;
    flex: 1;
    flex-direction: column;
  }
  .pointer {
    cursor: pointer;
  }
  .delete-color {
    color: #f51744;
    cursor: pointer;
  }
  .set-describe {
    color: #757575;
    margin-top: 10px !important;
  }
  .m-top24 {
    margin-top: 24px;
  }
  .edit-name {
    border: none;
    border-radius: 3px;
    box-sizing: border-box;
    color: #1e88e5;
    cursor: pointer;
    display: inline-block;
    outline: none;
    text-shadow: none;
    user-select: none;
    vertical-align: middle;
  }
  .m-right16 {
    margin-right: 16px;
  }
  .split-line {
    background: #eaeaea;
    height: 1px;
    margin: 40px 0;
    width: 100%;
  }
  .cancel-split-line {
    background: #eaeaea;
    height: 1px;
    margin: 40px 0 20px;
    width: 100%;
  }
  .form-item-padding {
    padding: 0 24px 22px;
  }
  .form-group {
    display: table;
    font-size: 13px;
    position: relative;
    width: 100%;
    .form-label {
      color: #333;
      font-weight: 600;
      line-height: 29px;
    }
    .txt-middle {
      vertical-align: middle !important;
    }
  }
  .red {
    color: red;
  }
  .domain-background {
    height: 56px;
    margin-top: 6px;
    width: 100px;
    margin-left: 142px;
  }
  .cancellation {
    color: #333333;
    font-size: 20px;
    font-weight: 700;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  :deep(.ant-upload.ant-upload-select){
    width: 80px !important;
    height: 80px !important;
    border: unset !important;
  }
  :deep(.ant-upload-list-item-container){
    width: 80px !important;
    height: 80px !important;
    border: unset !important;
  }
  .edit-name {
    border: none;
    border-radius: 3px;
    box-sizing: border-box;
    color: #1e88e5;
    cursor: pointer;
    display: inline-block;
    outline: none;
    text-shadow: none;
    user-select: none;
    vertical-align: middle;
  }
</style>
