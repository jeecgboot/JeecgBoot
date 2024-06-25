<template>
  <a-input :disabled="disabled" :style="{ width }" readOnly :placeholder="t('component.icon.placeholder')" :class="prefixCls" v-model:value="currentSelect">
    <template #addonAfter>
      <span class="cursor-pointer px-2 py-1 flex items-center" v-if="isSvgMode && currentSelect">
        <SvgIcon :name="currentSelect" @click="currentSelectClick"/>
      </span>
      <Icon :icon="currentSelect || 'ion:apps-outline'" class="cursor-pointer px-2 py-1" v-else @click="currentSelectClick"/>
    </template>
  </a-input>
  <a-modal :bodyStyle="{ padding: '24px'}" v-bind="$attrs" v-model:open="iconOpen" :keyboard="false" :width="800" @ok="handleOk" :ok-text="t('common.okText')" :cancel-text="t('common.cancelText')">
    <a-tabs style="padding-left: 15px;padding-right: 15px">
      <a-tab-pane tab="方向性图标" key="1">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-svg-mode="isSvgMode" :current-list="directionIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
      <a-tab-pane tab="指示性图标" key="2">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-svg-mode="isSvgMode" :current-list="suggestionIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
      <a-tab-pane tab="编辑类图标" key="3">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-svg-mode="isSvgMode" :current-list="editIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
      <a-tab-pane tab="数据类图标" key="4">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-svg-mode="isSvgMode" :current-list="dataIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
      <a-tab-pane tab="网站通用图标" key="5">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-svg-mode="isSvgMode" :current-list="webIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
      <a-tab-pane tab="品牌和标识" key="6">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-svg-mode="isSvgMode" :current-list="logoIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
      <a-tab-pane tab="其他" key="7">
        <a-form-item-rest>
          <icon-list ref="iconListRef" :clear-select="clearSelect" :copy="copy" :is-page="true" :is-search="true" :is-svg-mode="isSvgMode" :current-list="otherIcons" :value="currentSelect" />
        </a-form-item-rest>
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>
<script lang="ts" setup name="icon-picker">
  import { ref, watchEffect, watch, unref, onMounted } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Input } from 'ant-design-vue';
  import Icon from './Icon.vue';
  import SvgIcon from './SvgIcon.vue';

  import iconsData from '../data/icons.data';
  import { propTypes } from '/@/utils/propTypes';
  import { useI18n } from '/@/hooks/web/useI18n';
  import svgIcons from 'virtual:svg-icons-names';
  import IconList from "./IconList.vue";

  // 没有使用别名引入，是因为WebStorm当前版本还不能正确识别，会报unused警告
  const AInput = Input;

  function getIcons() {
    const data = iconsData as any;
    const prefix: string = data?.prefix ?? '';
    let result: string[] = [];
    if (prefix) {
      result = (data?.icons ?? []).map((item) => `${prefix}:${item}`);
    } else if (Array.isArray(iconsData)) {
      result = iconsData as string[];
    }
    return result;
  }

  function getSvgIcons() {
    return svgIcons.map((icon) => icon.replace('icon-', ''));
  }

  const props = defineProps({
    value: propTypes.string,
    width: propTypes.string.def('100%'),
    copy: propTypes.bool.def(false),
    mode: propTypes.oneOf<('svg' | 'iconify')[]>(['svg', 'iconify']).def('iconify'),
    disabled: propTypes.bool.def(true),
    clearSelect: propTypes.bool.def(false),
    iconPrefixSave: propTypes.bool.def(true),
  });

  const emit = defineEmits(['change', 'update:value']);

  const isSvgMode = props.mode === 'svg';
  const icons = isSvgMode ? getSvgIcons() : getIcons();

  const currentSelect = ref('');

  const { t } = useI18n();
  const { prefixCls } = useDesign('icon-picker');

  //update-begin---author:wangshuai---date:2024-05-08---for:【QQYUN-8924】图标库选择组件不如vue2的方便---
  const iconOpen = ref<boolean>(false);
  //方向性图标
  const directionIcons = ['ant-design:step-backward-outlined', 'ant-design:step-forward-outlined', 'ant-design:fast-backward-outlined', 'ant-design:fast-forward-outlined', 'ant-design:shrink-outlined', 'ant-design:arrows-alt-outlined', 'ant-design:down-outlined', 'ant-design:up-outlined', 'ant-design:left-outlined', 'ant-design:right-outlined', 'ant-design:caret-up-outlined', 'ant-design:caret-down-outlined', 'ant-design:caret-left-outlined', 'ant-design:caret-right-outlined', 'ant-design:up-circle-outlined', 'ant-design:down-circle-outlined', 'ant-design:left-circle-outlined', 'ant-design:right-circle-outlined',  'ant-design:double-right-outlined', 'ant-design:double-left-outlined', 'ant-design:vertical-left-outlined', 'ant-design:vertical-right-outlined', 'ant-design:forward-outlined', 'ant-design:backward-outlined', 'ant-design:rollback-outlined', 'ant-design:enter-outlined', 'ant-design:retweet-outlined', 'ant-design:swap-outlined', 'ant-design:swap-left-outlined', 'ant-design:swap-right-outlined', 'ant-design:arrow-up-outlined', 'ant-design:arrow-down-outlined', 'ant-design:arrow-left-outlined', 'ant-design:arrow-right-outlined', 'ant-design:play-circle-outlined', 'ant-design:up-square-outlined', 'ant-design:down-square-outlined', 'ant-design:left-square-outlined', 'ant-design:right-square-outlined', 'ant-design:login-outlined', 'ant-design:logout-outlined', 'ant-design:menu-fold-outlined', 'ant-design:menu-unfold-outlined', 'ant-design:border-bottom-outlined', 'ant-design:border-horizontal-outlined', 'ant-design:border-inner-outlined', 'ant-design:border-left-outlined', 'ant-design:border-right-outlined', 'ant-design:border-top-outlined', 'ant-design:border-verticle-outlined', 'ant-design:pic-center-outlined', 'ant-design:pic-left-outlined', 'ant-design:pic-right-outlined', 'ant-design:radius-bottomleft-outlined', 'ant-design:radius-bottomright-outlined', 'ant-design:radius-upleft-outlined', 'ant-design:radius-upright-outlined', 'ant-design:fullscreen-outlined', 'ant-design:fullscreen-exit-outlined']
  //提示建议性图标
  const suggestionIcons = ['ant-design:question-outlined', 'ant-design:question-circle-outlined', 'ant-design:plus-outlined', 'ant-design:plus-circle-outlined', 'ant-design:pause-outlined', 'ant-design:pause-circle-outlined', 'ant-design:minus-outlined', 'ant-design:minus-circle-outlined', 'ant-design:plus-square-outlined', 'ant-design:minus-square-outlined', 'ant-design:info-outlined', 'ant-design:info-circle-outlined', 'ant-design:exclamation-outlined', 'ant-design:exclamation-circle-outlined', 'ant-design:close-outlined', 'ant-design:close-circle-outlined', 'ant-design:close-square-outlined', 'ant-design:check-outlined', 'ant-design:check-circle-outlined', 'ant-design:check-square-outlined', 'ant-design:clock-circle-outlined', 'ant-design:warning-outlined', 'ant-design:issues-close-outlined', 'ant-design:stop-outlined']
  //编辑类图标
  const editIcons = ['ant-design:edit-outlined', 'ant-design:form-outlined', 'ant-design:copy-outlined', 'ant-design:scissor-outlined', 'ant-design:delete-outlined', 'ant-design:snippets-outlined', 'ant-design:diff-outlined', 'ant-design:highlight-outlined', 'ant-design:align-center-outlined', 'ant-design:align-left-outlined', 'ant-design:align-right-outlined', 'ant-design:bg-colors-outlined', 'ant-design:bold-outlined', 'ant-design:italic-outlined', 'ant-design:underline-outlined', 'ant-design:strikethrough-outlined', 'ant-design:redo-outlined', 'ant-design:undo-outlined', 'ant-design:zoom-in-outlined', 'ant-design:zoom-out-outlined', 'ant-design:font-colors-outlined', 'ant-design:font-size-outlined', 'ant-design:line-height-outlined', 'ant-design:dash-outlined', 'ant-design:small-dash-outlined', 'ant-design:sort-ascending-outlined', 'ant-design:sort-descending-outlined', 'ant-design:drag-outlined', 'ant-design:ordered-list-outlined', 'ant-design:radius-setting-outlined']
  //数据类图标
  const dataIcons = ['ant-design:area-chart-outlined', 'ant-design:pie-chart-outlined', 'ant-design:bar-chart-outlined', 'ant-design:dot-chart-outlined', 'ant-design:line-chart-outlined', 'ant-design:radar-chart-outlined', 'ant-design:heat-map-outlined', 'ant-design:fall-outlined', 'ant-design:rise-outlined', 'ant-design:stock-outlined', 'ant-design:box-plot-outlined', 'ant-design:fund-outlined', 'ant-design:sliders-outlined']
  //站通用图标
  const webIcons = ['ant-design:lock-outlined', 'ant-design:unlock-outlined', 'ant-design:bars-outlined', 'ant-design:book-outlined', 'ant-design:calendar-outlined', 'ant-design:cloud-outlined', 'ant-design:cloud-download-outlined', 'ant-design:code-outlined', 'ant-design:copy-outlined', 'ant-design:credit-card-outlined', 'ant-design:delete-outlined', 'ant-design:desktop-outlined', 'ant-design:download-outlined', 'ant-design:ellipsis-outlined', 'ant-design:file-outlined', 'ant-design:file-text-outlined', 'ant-design:file-unknown-outlined', 'ant-design:file-pdf-outlined', 'ant-design:file-word-outlined', 'ant-design:file-excel-outlined', 'ant-design:file-jpg-outlined', 'ant-design:file-ppt-outlined', 'ant-design:file-markdown-outlined', 'ant-design:file-add-outlined', 'ant-design:folder-outlined', 'ant-design:folder-open-outlined', 'ant-design:folder-add-outlined', 'ant-design:hdd-outlined', 'ant-design:frown-outlined', 'ant-design:meh-outlined', 'ant-design:smile-outlined', 'ant-design:inbox-outlined', 'ant-design:laptop-outlined', 'ant-design:appstore-outlined', 'ant-design:link-outlined', 'ant-design:mail-outlined', 'ant-design:mobile-outlined', 'ant-design:notification-outlined', 'ant-design:paper-clip-outlined', 'ant-design:picture-outlined', 'ant-design:poweroff-outlined', 'ant-design:reload-outlined', 'ant-design:search-outlined', 'ant-design:setting-outlined', 'ant-design:share-alt-outlined', 'ant-design:shopping-cart-outlined', 'ant-design:tablet-outlined', 'ant-design:tag-outlined', 'ant-design:tags-outlined', 'ant-design:to-top-outlined', 'ant-design:upload-outlined', 'ant-design:user-outlined', 'ant-design:video-camera-outlined', 'ant-design:home-outlined', 'ant-design:loading-outlined', 'ant-design:loading-3-quarters-outlined', 'ant-design:cloud-upload-outlined', 'ant-design:star-outlined', 'ant-design:heart-outlined', 'ant-design:environment-outlined', 'ant-design:eye-outlined', 'ant-design:camera-outlined', 'ant-design:save-outlined', 'ant-design:team-outlined', 'ant-design:solution-outlined', 'ant-design:phone-outlined', 'ant-design:filter-outlined', 'ant-design:exception-outlined', 'ant-design:export-outlined', 'ant-design:customer-service-outlined', 'ant-design:qrcode-outlined', 'ant-design:scan-outlined', 'ant-design:like-outlined', 'ant-design:dislike-outlined', 'ant-design:message-outlined', 'ant-design:pay-circle-outlined', 'ant-design:calculator-outlined', 'ant-design:pushpin-outlined', 'ant-design:bulb-outlined', 'ant-design:select-outlined', 'ant-design:switcher-outlined', 'ant-design:rocket-outlined', 'ant-design:bell-outlined', 'ant-design:disconnect-outlined', 'ant-design:database-outlined', 'ant-design:compass-outlined', 'ant-design:barcode-outlined', 'ant-design:hourglass-outlined', 'ant-design:key-outlined', 'ant-design:flag-outlined', 'ant-design:layout-outlined', 'ant-design:printer-outlined', 'ant-design:sound-outlined', 'ant-design:usb-outlined', 'ant-design:skin-outlined', 'ant-design:tool-outlined', 'ant-design:sync-outlined', 'ant-design:wifi-outlined', 'ant-design:car-outlined', 'ant-design:schedule-outlined', 'ant-design:user-add-outlined', 'ant-design:user-delete-outlined', 'ant-design:usergroup-add-outlined', 'ant-design:usergroup-delete-outlined', 'ant-design:man-outlined', 'ant-design:woman-outlined', 'ant-design:shop-outlined', 'ant-design:gift-outlined', 'ant-design:idcard-outlined', 'ant-design:medicine-box-outlined', 'ant-design:red-envelope-outlined', 'ant-design:coffee-outlined', 'ant-design:copyright-outlined', 'ant-design:trademark-outlined', 'ant-design:safety-outlined', 'ant-design:wallet-outlined', 'ant-design:bank-outlined', 'ant-design:trophy-outlined', 'ant-design:contacts-outlined', 'ant-design:global-outlined', 'ant-design:shake-outlined', 'ant-design:api-outlined', 'ant-design:fork-outlined', 'ant-design:dashboard-outlined', 'ant-design:table-outlined', 'ant-design:profile-outlined', 'ant-design:alert-outlined', 'ant-design:audit-outlined', 'ant-design:branches-outlined', 'ant-design:build-outlined', 'ant-design:border-outlined', 'ant-design:crown-outlined', 'ant-design:experiment-outlined', 'ant-design:fire-outlined', 'ant-design:money-collect-outlined', 'ant-design:property-safety-outlined', 'ant-design:read-outlined', 'ant-design:reconciliation-outlined', 'ant-design:rest-outlined', 'ant-design:security-scan-outlined', 'ant-design:insurance-outlined', 'ant-design:interation-outlined', 'ant-design:safety-certificate-outlined', 'ant-design:project-outlined', 'ant-design:thunderbolt-outlined', 'ant-design:block-outlined', 'ant-design:cluster-outlined', 'ant-design:deployment-unit-outlined', 'ant-design:dollar-outlined', 'ant-design:euro-outlined', 'ant-design:pound-outlined', 'ant-design:file-done-outlined', 'ant-design:file-exclamation-outlined', 'ant-design:file-protect-outlined', 'ant-design:file-search-outlined', 'ant-design:file-sync-outlined', 'ant-design:gateway-outlined', 'ant-design:gold-outlined', 'ant-design:robot-outlined', 'ant-design:shopping-outlined']
  //品牌和标识网
  const logoIcons = ['ant-design:android-outlined', 'ant-design:apple-outlined', 'ant-design:windows-outlined', 'ant-design:ie-outlined', 'ant-design:chrome-outlined', 'ant-design:github-outlined', 'ant-design:aliwangwang-outlined', 'ant-design:dingding-outlined', 'ant-design:weibo-square-outlined', 'ant-design:weibo-circle-outlined', 'ant-design:taobao-circle-outlined', 'ant-design:html5-outlined', 'ant-design:weibo-outlined', 'ant-design:twitter-outlined', 'ant-design:wechat-outlined', 'ant-design:youtube-outlined', 'ant-design:alipay-circle-outlined', 'ant-design:taobao-outlined', 'ant-design:skype-outlined', 'ant-design:qq-outlined', 'ant-design:medium-workmark-outlined', 'ant-design:gitlab-outlined', 'ant-design:medium-outlined', 'ant-design:linkedin-outlined', 'ant-design:google-plus-outlined', 'ant-design:dropbox-outlined', 'ant-design:facebook-outlined', 'ant-design:codepen-outlined', 'ant-design:amazon-outlined', 'ant-design:google-outlined', 'ant-design:codepen-circle-outlined', 'ant-design:alipay-outlined', 'ant-design:ant-design-outlined', 'ant-design:aliyun-outlined', 'ant-design:zhihu-outlined', 'ant-design:slack-outlined', 'ant-design:slack-square-outlined', 'ant-design:behance-outlined', 'ant-design:behance-square-outlined', 'ant-design:dribbble-outlined', 'ant-design:dribbble-square-outlined', 'ant-design:instagram-outlined', 'ant-design:yuque-outlined', 'ant-design:alibaba-outlined', 'ant-design:yahoo-outlined']
  //其他
  const otherIcons = ref<any>([]);
  //update-end---author:wangshuai---date:2024-05-08---for:【QQYUN-8924】图标库选择组件不如vue2的方便---

  watchEffect(() => {
    // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
    let value = props.value;
    if (!props.iconPrefixSave && value) {
      value = `ant-design:${value}`;
    }
    // update-end--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
    currentSelect.value = value;
  });

  watch(
    () => currentSelect.value,
    (v) => {
      // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
      let value = v;
      if (!props.iconPrefixSave && value) {
        value = value.split('ant-design:')[1];
      }
      // update-end--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
      emit('update:value', value);
      return emit('change', value);
    }
  );


  //update-begin---author:wangshuai ---date:20230522  for：【issues/4947】菜单编辑页面菜单图标选择模板，每页显示数量切换无效，输入框后面的图标点击之后清空数据------------
  //update-begin---author:wangshuai---date:2024-05-08---for:【QQYUN-8924】图标库选择组件不如vue2的方便---
  /**
   * 图标点击重置页数
   */
  function currentSelectClick() {
    iconOpen.value = true;
    setTimeout(()=>{
      iconListRef.value.currentSelectClick();
    },100)
  }
  //update-begin---author:wangshuai ---date:20230522  for：【issues/4947】菜单编辑页面菜单图标选择模板，每页显示数量切换无效，输入框后面的图标点击之后清空数据------------

  function initOtherIcon() {
    otherIcons.value = icons.filter(item => {
      if(directionIcons.indexOf(item) === -1 && suggestionIcons.indexOf(item) === -1 && editIcons.indexOf(item) === -1
         && dataIcons.indexOf(item) === -1 && webIcons.indexOf(item) === -1 && logoIcons.indexOf(item) === -1) {
          return true;
      }
    })
  }

  const iconListRef = ref();

  /**
   * 图标弹窗确定事件
   */
  function handleOk() {
    currentSelect.value = iconListRef.value.getIcon();
    iconOpen.value = false;
  }

  onMounted(()=>{
    //初始化加载图标
    initOtherIcon();
  })
  //update-end---author:wangshuai---date:2024-05-08---for:【QQYUN-8924】图标库选择组件不如vue2的方便---
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-icon-picker';

  .@{prefix-cls} {
    .ant-input-group-addon {
      padding: 0;
    }

    &-popover {
      width: 400px;

      .ant-popover-inner-content {
        padding: 0;
      }

      .scrollbar {
        height: 220px;
      }
    }
  }
</style>
