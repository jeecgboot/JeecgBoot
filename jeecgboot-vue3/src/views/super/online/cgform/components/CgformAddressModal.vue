<!-- Online表单，配置地址弹窗 -->
<template>
  <BasicModal
      @register="registerModal"
      title="配置地址"
      :width="750"
      :canFullscreen="false"
      :showOkBtn="false"
      cancelText="关闭"
  >
    <div class="content">
      <a-collapse v-model:activeKey="collapseKey" class="j-collapse" :bordered="false" ghost>
        <a-collapse-panel key="def" header="配置地址" class="j-collapse-panel no-header">
          <a-row style="margin-bottom: 8px">
            <a-col :span="24">
              <a-input :readOnly="true" addonBefore="数据列表地址" :value="getAddress.list">
                <template #addonAfter>
                  <a :href="getAddress.list" target="_blank">打开</a>
                </template>
              </a-input>
            </a-col>
          </a-row>

          <a-button class="copy-sql" type="primary" size="small" @click="copySqlClick">复制菜单SQL</a-button>
        </a-collapse-panel>
        <a-collapse-panel v-if="enableExternalLink" key="external" header="外部链接">
          <a-row v-if="hasExtAction('add')" style="margin-bottom: 8px">
            <a-col :span="24">
              <a-input :readOnly="true" addonBefore="外部新增地址" :value="getAddress.extLink.add">
                <template #addonAfter>
                  <a :href="getAddress.extLink.add + '?token=' + accessToken" target="_blank">打开</a>
                </template>
              </a-input>
            </a-col>
          </a-row>

          <a-row v-if="hasExtAction('edit')" style="margin-bottom: 8px">
            <a-col :span="24">
              <a-input :readOnly="true" addonBefore="外部修改地址" :value="getAddress.extLink.edit">
                <template #addonAfter>
                  <a @click="openPage(getAddress.extLink.edit)">打开</a>
                </template>
              </a-input>
            </a-col>
          </a-row>

          <a-row v-if="hasExtAction('detail')" style="margin-bottom: 8px">
            <a-col :span="24">
              <a-input :readOnly="true" addonBefore="外部详情地址" :value="getAddress.extLink.detail">
                <template #addonAfter>
                  <a @click="openPage(getAddress.extLink.detail)">打开</a>
                </template>
              </a-input>
            </a-col>
          </a-row>
          <div v-if="externalLinkActions.length === 0" style="color: rgba(0, 0, 0, 0.45)">未勾选允许的操作，请在表单配置【允许的操作】中勾选新增/编辑/详情</div>
          <div v-if="hasExtAction('edit') || hasExtAction('detail')" style="text-align: right; color: red">注意：<span style="font-weight: bold">{dataId}</span> 为数据id</div>
        </a-collapse-panel>
      </a-collapse>
    </div>
  </BasicModal>

  <!-- 让用户输入 dataId 的弹窗 -->
  <BasicModal v-model:visible="dataIdProps.visible" v-bind="dataIdBind">
    <a-input placeholder="请输入dataId" v-model:value="dataIdProps.value"></a-input>
    <template #footer>
      <a :href="dataIdHref" target="_blank">
        <a-button type="primary" @click="onClickDataId">确定</a-button>
      </a>
    </template>
  </BasicModal>
</template>

<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import {BasicModal, useModalInner} from "@/components/Modal";
import {parseExtConfigJson} from "../util/utils";
import {copyTextToClipboard} from "@/hooks/web/useCopyToClipboard";
import {getToken} from "@/utils/auth";
import {useMessage} from "@/hooks/web/useMessage";
import {buildUUID} from "@/utils/uuid";
const emit = defineEmits(['register'])
const {createMessage: $message} = useMessage();

const accessToken = computed(() => getToken());

const collapseKey = ref(['def'])
const enableExternalLink = ref(false);
// 外部链接允许的操作：add、edit、detail
const externalLinkActions = ref<string[]>([]);
const themeTemplate = ref('normal');
const isTree = ref(false);
const model = reactive({
  title: '',
  content: '',
  copyText: '',
  copyTitle: '',
  formId: '',
})

const [registerModal] = useModalInner((data: Recordable) => {
  Object.assign(model, data, {
    formId: data.record.id,
  })
  // 解析扩展JSON
  const extConfigJson = parseExtConfigJson(data.record);

  enableExternalLink.value = extConfigJson.enableExternalLink === 1;
  // update-begin--author:liaozhiyang---date:20261026---for：【LHZP-29】按钮根据详情/编辑/新增权限显示
  externalLinkActions.value = (extConfigJson.externalLinkActions || '').split(',').filter((item) => !!item);
  // update-end--author:liaozhiyang---date:20261026---for：【LHZP-29】按钮根据详情/编辑/新增权限显示
  themeTemplate.value = data.record.themeTemplate;
  isTree.value = data.record.isTree === 'Y';
  if (enableExternalLink.value) {
    collapseKey.value = ['def', 'external'];
  } else {
    collapseKey.value = ['def'];
  }
});

// 是否允许某个外部链接操作
function hasExtAction(action: string) {
  return externalLinkActions.value.includes(action);
}

const getAddress = computed(() => {

  const extLink: Recordable = {}
  if (enableExternalLink.value) {
    const before = `/online/cgform/share/${model.formId}`;
    extLink.add = `${before}/add`;
    extLink.edit = `${before}/u/{dataId}`;
    extLink.detail = `${before}/d/{dataId}`;
  }

  return {
    list: model.content,
    extLink,
  };
});

/**
 * 复制sql
 */
  function copySqlClick() {
    // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-12348】online生成的菜单sql 自动带上组件名称
    let component_name = 'OnlineAutoList';
    if (themeTemplate.value === 'normal') {
      component_name = 'OnlineAutoList';
    } else if (themeTemplate.value === 'erp') {
      component_name = 'CgformErpList';
    } else if (themeTemplate.value === 'innerTable') {
      component_name = 'OnlCgformInnerTableList';
    } else if (themeTemplate.value === 'tab') {
      component_name = 'OnlCgformTabList';
    }
    if (isTree.value) {
      component_name = 'DefaultOnlineList';
    }
    // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-12348】online生成的菜单sql 自动带上组件名称
    const insertMenuSql = `-- 插入菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('${buildUUID()}', NULL, '${model.copyTitle}', '${model.copyText}', '1', '${component_name}', NULL, 0, NULL, '1', 0.00, 0, NULL, 0, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', null, NULL, NULL, 0)
`;
  copyText(insertMenuSql);
}

// 复制文本到剪贴板
function copyText(text: string) {
  const success = copyTextToClipboard(text)
  if (success) {
    $message.success('复制成功！')
  } else {
    $message.error('复制失败！')
  }
  return success
}

const dataIdProps = reactive({
  base: '',
  value: '',
  visible: false,
});
const dataIdHref = computed(() => {
  return dataIdProps.value ? dataIdProps.base.replace(/{dataId}/, dataIdProps.value) + '?token=' + accessToken.value : undefined;
});
const dataIdBind = computed(() => {
  return {
    title: '请输入dataId',
    minHeight: 120,
    centered: true,
    canFullscreen: false,
    onOk: () => (dataIdProps.visible = false),
    onCancel: () => (dataIdProps.visible = false),
  };
});

function openPage(url: string) {
  dataIdProps.base = url;
  dataIdProps.value = '';
  dataIdProps.visible = true;
}

function onClickDataId() {
  if (!dataIdProps.value) {
    $message.warn('请输入dataId');
    return
  }
  dataIdProps.visible = false;
}

</script>

<style scoped lang="less">
.j-collapse {
  //background: red;
  :deep(.ant-collapse-header) {
    padding-left: 0;
  }

  :deep(.ant-collapse-content-box) {
    padding: 0;
  }

  .j-collapse-panel {
    &.no-header {
      :deep(.ant-collapse-header) {
        display: none;
      }
    }
  }

}

.content {
  padding: 20px;
}

.copy-sql {
  float: right;
  margin-top: 6px;
}
</style>
