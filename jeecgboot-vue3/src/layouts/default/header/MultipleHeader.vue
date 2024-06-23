<template>
  <div :style="getPlaceholderDomStyle" v-if="getIsShowPlaceholderDom"></div>
  <div :style="getWrapStyle" :class="getClass">
    <LayoutHeader v-if="getShowInsetHeaderRef" />
    <MultipleTabs v-if="getShowTabs" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, unref, computed, CSSProperties } from 'vue';

  import LayoutHeader from './index.vue';
  import MultipleTabs from '../tabs/index.vue';

  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useFullContent } from '/@/hooks/web/useFullContent';
  import { useMultipleTabSetting } from '/@/hooks/setting/useMultipleTabSetting';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useLayoutHeight } from '../content/useContentViewHeight';
  import { TabsThemeEnum } from '/@/enums/appEnum';
  // update-begin--author:liaozhiyang---date:20240407---for：【QQYUN-8774】网站header区域加高
  const HEADER_HEIGHT = 60;
  // update-begin--author:liaozhiyang---date:20240407---for：【【QQYUN-8774】网站header区域加高

  // updateBy:sunjianlei---updateDate:2021-09-03---修改tab切换栏样式：更改高度
  const TABS_HEIGHT = 32;
  const TABS_HEIGHT_CARD = 50;
  const TABS_HEIGHT_SMOOTH = 50;

  export default defineComponent({
    name: 'LayoutMultipleHeader',
    components: { LayoutHeader, MultipleTabs },
    setup() {
      const { setHeaderHeight } = useLayoutHeight();
      const { prefixCls } = useDesign('layout-multiple-header');

      const { getCalcContentWidth, getSplit } = useMenuSetting();
      const { getIsMobile } = useAppInject();
      const { getFixed, getShowInsetHeaderRef, getShowFullHeaderRef, getHeaderTheme, getShowHeader } = useHeaderSetting();

      const { getFullContent } = useFullContent();

      const { getShowMultipleTab, getTabsTheme } = useMultipleTabSetting();

      const getShowTabs = computed(() => {
        return unref(getShowMultipleTab) && !unref(getFullContent);
      });

      const getIsShowPlaceholderDom = computed(() => {
        return unref(getFixed) || unref(getShowFullHeaderRef);
      });

      const getWrapStyle = computed((): CSSProperties => {
        const style: CSSProperties = {};
        if (unref(getFixed)) {
          style.width = unref(getIsMobile) ? '100%' : unref(getCalcContentWidth);
        }
        if (unref(getShowFullHeaderRef)) {
          style.top = `${HEADER_HEIGHT}px`;
        }
        return style;
      });

      const getIsFixed = computed(() => {
        return unref(getFixed) || unref(getShowFullHeaderRef);
      });

      // updateBy:sunjianlei---updateDate:2021-09-08---根据主题的不同，动态计算tabs高度
      const getTabsThemeHeight = computed(() => {
        let tabsTheme = unref(getTabsTheme);
        if (tabsTheme === TabsThemeEnum.CARD) {
          return TABS_HEIGHT_CARD;
        } else if (tabsTheme === TabsThemeEnum.SMOOTH) {
          return TABS_HEIGHT_SMOOTH;
        } else {
          return TABS_HEIGHT;
        }
      });

      const getPlaceholderDomStyle = computed((): CSSProperties => {
        let height = 0;
        if ((unref(getShowFullHeaderRef) || !unref(getSplit)) && unref(getShowHeader) && !unref(getFullContent)) {
          height += HEADER_HEIGHT;
        }
        if (unref(getShowMultipleTab) && !unref(getFullContent)) {
          height += unref(getTabsThemeHeight);
        }
        setHeaderHeight(height);
        return {
          height: `${height}px`,
        };
      });

      const getClass = computed(() => {
        return [prefixCls, `${prefixCls}--${unref(getHeaderTheme)}`, { [`${prefixCls}--fixed`]: unref(getIsFixed) }];
      });

      return {
        getClass,
        prefixCls,
        getPlaceholderDomStyle,
        getIsFixed,
        getWrapStyle,
        getIsShowPlaceholderDom,
        getShowTabs,
        getShowInsetHeaderRef,
      };
    },
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-layout-multiple-header';

  .@{prefix-cls} {
    transition: width 0.2s;
    flex: 0 0 auto;

    &--dark {
      margin-left: -1px;
    }

    &--fixed {
      position: fixed;
      top: 0;
      z-index: @multiple-tab-fixed-z-index;
      width: 100%;
    }
  }
</style>
