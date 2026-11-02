<template>
  <div v-if="cgformRef != null" :class="[prefixCls]">
    <template v-if="showResult!=='none'">
      <a-result
          status="success"
          :title="`${isUpdate ? '修改': '新增'}成功`"
          subTitle="请选择下一步操作"
      >
        <template #extra>
          <a-space>
            <a-button type="primary" @click="onAddContinue">
              <span>继续{{ isUpdate ? '修改' : '新增' }}</span>
            </a-button>
            <!-- 有详情权限：主按钮为【查看数据】，其它操作放下拉 -->
            <a-space-compact v-if="canView" block>
              <a-button @click="onOtherAction({key:'view'})">
                <span>查看</span>
                <span v-if="!isUpdate">新</span>
                <span>数据</span>
              </a-button>
              <a-dropdown v-if="otherAction" :trigger="['click', 'hover']">
                <a-button preIcon="ant-design:down">
                </a-button>
                <template #overlay>
                  <a-menu @click="onOtherAction">
                    <a-menu-item :key="otherAction.key">{{ otherAction.label }}</a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </a-space-compact>
            <!-- 无详情权限：其它操作直接平铺为按钮 -->
            <a-button v-else-if="otherAction" @click="onOtherAction({key: otherAction.key})">{{ otherAction.label }}</a-button>
            <a-button @click="onClose">关闭</a-button>
          </a-space>
        </template>
      </a-result>
    </template>
    <!-- 单表页面 -->
    <!--    <template v-else-if="isSingle">-->
    <!--    </template>-->
    <!-- 主子表页面 -->
    <template v-else>
      <SingleView
          :ID="ID"
          :formName="formName"
          :isUpdate="isUpdate"
          :isDetail="isDetail"
          :isMobile="getIsMobile"
          :buttonSwitch="buttonSwitch"
          :cgBIBtnMap="cgBIBtnMap"
          :confirmBtnCfg="confirmBtnCfg"

          @ok="onOk"
          @goEdit="goEdit"
      />
    </template>
    <div v-show="false">
      <OnlineAutoList ref="listRef"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue';
import {useShareStore} from "../store/shareStore";
import OnlineAutoList from "../../auto/default/OnlineAutoList.vue";
import {useDesign} from "@/hooks/web/useDesign";

import SingleView from "./SingleView.vue";
import {useGo} from "@/hooks/web/usePage";
import {useAppInject} from "@/hooks/web/useAppInject";
import {parseExtConfigJson} from "../../util/utils";

const {prefixCls} = useDesign('online-share-view-box');

const props = defineProps({
  isUpdate: Boolean,
  isDetail: Boolean,
})

const {getIsMobile} = useAppInject();

const shareStore = useShareStore()

const cgformRef = computed(() => shareStore.getCgformRecord);
const ID = computed(() => cgformRef.value?.id);
const formName = computed(() => cgformRef.value?.tableTxt);
// 是否是单表
// const isSingle = computed(() => cgformRef.value?.tableType == 1);

const listRef = ref()
const buttonSwitch = computed(() => listRef.value?.buttonSwitch);
const cgBIBtnMap = computed(() => listRef.value?.cgBIBtnMap);
const confirmBtnCfg = computed(() => listRef.value?.getFormConfirmButtonCfg);

// update-begin--author:liaozhiyang---date:20261026---for：【LHZP-29】按钮根据详情/编辑/新增权限显示
/**
 * 是否有某个内置按钮的权限
 * @param keys 按钮编码，多个需同时满足
 */
function hasBtnAuth(...keys: string[]) {
  return keys.every((key) => buttonSwitch.value?.[key] === true && cgBIBtnMap.value?.[key]?.enabled !== false);
}

// 外部链接允许的操作：add、edit、detail
const externalLinkActions = computed<string[]>(() => {
  if (!cgformRef.value) {
    return [];
  }
  const extJson = parseExtConfigJson(cgformRef.value);
  return (extJson.externalLinkActions || '').split(',').filter((item) => !!item);
});
// 外部链接是否允许某个操作
function hasExtAction(action: string) {
  return externalLinkActions.value.includes(action);
}

// 详情：需要详情按钮权限，且外部链接允许详情
const canView = computed(() => hasBtnAuth('detail') && hasExtAction('detail'));
// 下拉/平铺的其它操作：修改后为【添加数据】，新增后为【编辑新数据】
const otherAction = computed(() => {
  if (props.isUpdate) {
    return hasBtnAuth('add') && hasExtAction('add') ? { key: 'add', label: '添加数据' } : null;
  }
  return hasBtnAuth('edit', 'update') && hasExtAction('edit') ? { key: 'edit', label: '编辑新数据' } : null;
});
// update-end--author:liaozhiyang---date:20261026---for：【LHZP-29】按钮根据详情/编辑/新增权限显示

const showResult = ref<'none' | 'ok'>('none');
const lastActionDataId = ref<string>();

function onOk(dataId: string) {
  showResult.value = 'ok';
  lastActionDataId.value = dataId;
  if (!props.isUpdate) {
    shareStore.setDataRecord(null);
  }
}

// 继续添加
function onAddContinue() {
  // window.location.reload();
  showResult.value = 'none';
  lastActionDataId.value = '';
}

const go = useGo();

function onOtherAction({key}) {
  const dataId = lastActionDataId.value!;
  if (key === 'add') {
    goDataAction('add', '');
  } else if (key === 'view') {
    goDetail(dataId);
  } else if (key === 'edit') {
    goEdit(dataId);
  }
  lastActionDataId.value = '';
  showResult.value = 'none';
}

function goEdit(dataId: string) {
  goDataAction('u', dataId);
}

function goDetail(dataId: string) {
  goDataAction('d', dataId);
}

function goDataAction(action, dataId) {
  if (action === 'add') {
    go(`/online/cgform/share/${ID.value}/${action}`);
    return;
  }
  if (!dataId) {
    dataId = shareStore.getDataRecord?.id;
  }

  go(`/online/cgform/share/${ID.value}/${action}/${dataId}`);
}

function onClose() {
  window.close()
  setTimeout(() => alert('当前页面无法通过按钮关闭，请手动关闭'), 1000)
}

</script>

<style scoped lang="less">

</style>
