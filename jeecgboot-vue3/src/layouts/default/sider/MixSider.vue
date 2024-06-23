<template>
  <div :class="`${prefixCls}-dom`" :style="getDomStyle"></div>
  <div
    v-click-outside="handleClickOutside"
    :style="getWrapStyle"
    :class="[
      prefixCls,
      getMenuTheme,
      {
        open: openMenu,
        mini: getCollapsed,
        bright: isThemeBright,
      },
    ]"
    v-bind="getMenuEvents"
  >
    <AppLogo :showTitle="false" :class="`${prefixCls}-logo`" />

    <LayoutTrigger :class="`${prefixCls}-trigger`" />

    <ScrollContainer>
      <ul :class="`${prefixCls}-module`">
        <li
          :class="[
            `${prefixCls}-module__item `,
            {
              [`${prefixCls}-module__item--active`]: item.path === activePath,
            },
          ]"
          v-bind="getItemEvents(item)"
          v-for="item in menuModules"
          :key="item.path"
        >
          <SimpleMenuTag :item="item" collapseParent dot />
          <Icon :class="`${prefixCls}-module__icon`" :size="getCollapsed ? 16 : 20" :icon="item.icon || (item.meta && item.meta.icon)" />
          <p :class="`${prefixCls}-module__name`">
            {{ t(item.name) }}
          </p>
        </li>
      </ul>
    </ScrollContainer>

    <div :class="`${prefixCls}-menu-list`" ref="sideRef" :style="getMenuStyle">
      <div
        v-show="openMenu"
        :class="[
          `${prefixCls}-menu-list__title`,
          {
            show: openMenu,
          },
        ]"
      >
        <span class="text"> {{ shortTitle }}</span>
        <Icon :size="16" :icon="getMixSideFixed ? 'ri:pushpin-2-fill' : 'ri:pushpin-2-line'" class="pushpin" @click="handleFixedMenu" />
      </div>
      <ScrollContainer :class="`${prefixCls}-menu-list__content`">
        <SimpleMenu :items="childrenMenus" :theme="getMenuTheme" mixSider @menuClick="handleMenuClick" />
      </ScrollContainer>
      <div v-show="getShowDragBar && openMenu" :class="`${prefixCls}-drag-bar`" ref="dragBarRef"></div>
    </div>
  </div>
</template>
<script lang="ts">
  import type { Menu } from '/@/router/types';
  import type { CSSProperties } from 'vue';
  import { computed, defineComponent, onMounted, ref, unref, watch} from 'vue';
  import type { RouteLocationNormalized } from 'vue-router';
  import { ScrollContainer } from '/@/components/Container';
  import { SimpleMenu, SimpleMenuTag } from '/@/components/SimpleMenu';
  import { Icon } from '/@/components/Icon';
  import { AppLogo } from '/@/components/Application';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useDragLine } from './useLayoutSider';
  import { useGlobSetting } from '/@/hooks/setting';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useGo } from '/@/hooks/web/usePage';
  import { SIDE_BAR_MINI_WIDTH, SIDE_BAR_SHOW_TIT_MINI_WIDTH } from '/@/enums/appEnum';
  import clickOutside from '/@/directives/clickOutside';
  import { getChildrenMenus, getCurrentParentPath, getShallowMenus } from '/@/router/menus';
  import { listenerRouteChange } from '/@/logics/mitt/routeChange';
  import LayoutTrigger from '../trigger/index.vue';
  import { useAppStore } from '/@/store/modules/app';

  export default defineComponent({
    name: 'LayoutMixSider',
    components: {
      ScrollContainer,
      AppLogo,
      SimpleMenu,
      Icon,
      LayoutTrigger,
      SimpleMenuTag,
    },
    directives: {
      clickOutside,
    },
    setup() {
      let menuModules = ref<Menu[]>([]);
      const activePath = ref('');
      const childrenMenus = ref<Menu[]>([]);
      const openMenu = ref(false);
      const dragBarRef = ref<ElRef>(null);
      const sideRef = ref<ElRef>(null);
      const currentRoute = ref<Nullable<RouteLocationNormalized>>(null);
      const appStore = useAppStore();
      const isThemeBright = ref(false);

      const { prefixCls } = useDesign('layout-mix-sider');
      const go = useGo();
      const { t } = useI18n();
      const {
        getMenuWidth,
        getCanDrag,
        getCloseMixSidebarOnChange,
        getMenuTheme,
        getMixSideTrigger,
        getRealWidth,
        getMixSideFixed,
        mixSideHasChildren,
        setMenuSetting,
        getIsMixSidebar,
        getCollapsed,
      } = useMenuSetting();

      const { shortTitle } = useGlobSetting();

      useDragLine(sideRef, dragBarRef, true);

      const getMenuStyle = computed((): CSSProperties => {
        return {
          width: unref(openMenu) ? `${unref(getMenuWidth) - 60}px` : 0,
          left: `${unref(getMixSideWidth)}px`,
        };
      });

      const getIsFixed = computed(() => {
        /* eslint-disable-next-line */
        mixSideHasChildren.value = unref(childrenMenus).length > 0;
        const isFixed = unref(getMixSideFixed) && unref(mixSideHasChildren);
        if (isFixed) {
          /* eslint-disable-next-line */
          openMenu.value = true;
        }
        return isFixed;
      });

      const getMixSideWidth = computed(() => {
        return unref(getCollapsed) ? SIDE_BAR_MINI_WIDTH : SIDE_BAR_SHOW_TIT_MINI_WIDTH;
      });

      const getDomStyle = computed((): CSSProperties => {
        const fixedWidth = unref(getIsFixed) ? unref(getRealWidth) : 0;
        const width = `${unref(getMixSideWidth) + fixedWidth}px`;
        return getWrapCommonStyle(width);
      });

      const getWrapStyle = computed((): CSSProperties => {
        const width = `${unref(getMixSideWidth)}px`;
        return getWrapCommonStyle(width);
      });

      const getMenuEvents = computed(() => {
        return !unref(getMixSideFixed)
          ? {
              onMouseleave: () => {
                setActive(true);
                closeMenu();
              },
            }
          : {};
      });

      const getShowDragBar = computed(() => unref(getCanDrag));

      onMounted(async () => {
        menuModules.value = await getShallowMenus();
      });

      listenerRouteChange((route) => {
        currentRoute.value = route;
        setActive(true);
        if (unref(getCloseMixSidebarOnChange)) {
          closeMenu();
        }
      });

      function getWrapCommonStyle(width: string): CSSProperties {
        return {
          width,
          maxWidth: width,
          minWidth: width,
          flex: `0 0 ${width}`,
        };
      }

      // Process module menu click
      async function handleModuleClick(path: string, hover = false) {
        const children = await getChildrenMenus(path);
        if (unref(activePath) === path) {
          if (!hover) {
            if (!unref(openMenu)) {
              openMenu.value = true;
            } else {
              closeMenu();
            }
          } else {
            if (!unref(openMenu)) {
              openMenu.value = true;
            }
          }
          if (!unref(openMenu)) {
            setActive();
          }
        } else {
          openMenu.value = true;
          activePath.value = path;
        }

        if (!children || children.length === 0) {
          if (!hover) go(path);
          childrenMenus.value = [];
          closeMenu();
          return;
        }
        childrenMenus.value = children;
      }

      // Set the currently active menu and submenu
      async function setActive(setChildren = false) {
        const path = currentRoute.value?.path;
        if (!path) return;
        activePath.value = await getCurrentParentPath(path);
        // hanldeModuleClick(parentPath);
        if (unref(getIsMixSidebar)) {
          const activeMenu = unref(menuModules).find((item) => item.path === unref(activePath));
          const p = activeMenu?.path;
          if (p) {
            const children = await getChildrenMenus(p);
            if (setChildren) {
              childrenMenus.value = children;

              if (unref(getMixSideFixed)) {
                openMenu.value = children.length > 0;
              }
            }
            if (children.length === 0) {
              childrenMenus.value = [];
            }
          }
        }
      }

      function handleMenuClick(path: string) {
        go(path);
      }

      function handleClickOutside() {
        setActive(true);
        closeMenu();
      }

      function getItemEvents(item: Menu) {
        if (unref(getMixSideTrigger) === 'hover') {
          return {
            onMouseenter: () => handleModuleClick(item.path, true),
            onClick: async () => {
              const children = await getChildrenMenus(item.path);
              if (item.path && (!children || children.length === 0)) go(item.path);
            },
          };
        }
        return {
          onClick: () => handleModuleClick(item.path),
        };
      }

      function handleFixedMenu() {
        setMenuSetting({
          mixSideFixed: !unref(getIsFixed),
        });
      }

      // Close menu
      function closeMenu() {
        if (!unref(getIsFixed)) {
          openMenu.value = false;
        }
      }

      // update-begin--author:liaozhiyang---date:20240417---for：【QQYUN-8927】侧边折叠导航模式区分彩色模式
      watch(
        () => appStore.getProjectConfig.menuSetting,
        (menuSetting) => {
          isThemeBright.value = !!menuSetting?.isThemeBright;
        },
        { immediate: true, deep: true }
      );
      // update-end--author:liaozhiyang---date:20240417---for：【QQYUN-8927】侧边折叠导航模式区分彩色模式

      return {
        t,
        prefixCls,
        menuModules,
        handleModuleClick: handleModuleClick,
        activePath,
        childrenMenus: childrenMenus,
        getShowDragBar,
        handleMenuClick,
        getMenuStyle,
        handleClickOutside,
        sideRef,
        dragBarRef,
        shortTitle,
        openMenu,
        getMenuTheme,
        getItemEvents,
        getMenuEvents,
        getDomStyle,
        handleFixedMenu,
        getMixSideFixed,
        getWrapStyle,
        getCollapsed,
        isThemeBright,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-layout-mix-sider';
  @width: 80px;
  .@{prefix-cls} {
    position: fixed;
    top: 0;
    left: 0;
    z-index: @layout-mix-sider-fixed-z-index;
    height: 100%;
    overflow: hidden;
    background-color: @sider-dark-bg-color;
    transition: all 0.2s ease 0s;

    &-dom {
      height: 100%;
      overflow: hidden;
      transition: all 0.2s ease 0s;
    }

    &-logo {
      display: flex;
      height: @header-height;
      padding-left: 0 !important;
      justify-content: center;

      img {
        width: @logo-width;
        height: @logo-width;
      }
    }

    &.light {
      .@{prefix-cls}-logo {
        border-bottom: 1px solid rgb(238, 238, 238);
      }

      &.open {
        > .scrollbar {
          border-right: 1px solid rgb(238, 238, 238);
        }
      }

      .@{prefix-cls}-module {
        &__item {
          font-weight: normal;
          color: rgba(0, 0, 0, 0.65);

          &--active {
            color: @primary-color;
            background-color: unset;
          }
        }
      }
      .@{prefix-cls}-menu-list {
        &__content {
          box-shadow: 0 0 4px 0 rgba(0, 0, 0, 0.1);
        }

        &__title {
          .pushpin {
            color: rgba(0, 0, 0, 0.35);

            &:hover {
              color: rgba(0, 0, 0, 0.85);
            }
          }
        }
      }
    }
    @border-color: @sider-dark-lighten-bg-color;

    &.dark {
      &.open {
        .@{prefix-cls}-logo {
          // border-bottom: 1px solid @border-color;
        }

        > .scrollbar {
          border-right: 1px solid @border-color;
        }
      }
      .@{prefix-cls}-menu-list {
        background-color: @sider-dark-bg-color;

        &__title {
          color: @white;
          border-bottom: none;
          border-bottom: 1px solid @border-color;
        }
      }
      // 侧边折叠导航彩色模式文字颜色
      &.bright {
        .@{prefix-cls}-module {
          &__item {
            font-weight: normal;
            color: rgba(255, 255, 255, 1);
            &:hover {
              color: rgba(255, 255, 255, 0.8);
            }
          }
        }
      }
    }

    > .scrollbar {
      height: calc(100% - @header-height - 38px);
    }

    &.mini &-module {
      &__name {
        display: none;
      }

      &__icon {
        margin-bottom: 0;
      }
    }

    &-module {
      position: relative;
      padding-top: 1px;

      &__item {
        position: relative;
        padding: 12px 0;
        color: rgba(255, 255, 255, 0.65);
        text-align: center;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          color: @white;
        }
        // &:hover,
        &--active {
          font-weight: 700;
          color: @white;
          background-color: @sider-dark-darken-bg-color;

          &::before {
            position: absolute;
            top: 0;
            left: 0;
            width: 3px;
            height: 100%;
            background-color: @primary-color;
            content: '';
          }
        }
      }

      &__icon {
        margin-bottom: 8px;
        font-size: 24px;
        transition: all 0.2s;
      }

      &__name {
        margin-bottom: 0;
        font-size: 12px;
        transition: all 0.2s;
      }
    }

    &-trigger {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.65);
      text-align: center;
      cursor: pointer;
      background-color: @trigger-dark-bg-color;
      height: 36px;
      line-height: 36px;
    }

    &.light &-trigger {
      color: rgba(0, 0, 0, 0.65);
      background-color: #fff;
      border-top: 1px solid #eee;
    }

    &-menu-list {
      position: fixed;
      top: 0;
      width: 200px;
      height: calc(100%);
      background-color: #fff;
      transition: all 0.2s;

      &__title {
        display: flex;
        height: @header-height;
        // margin-left: -6px;
        font-size: 18px;
        color: @primary-color;
        border-bottom: 1px solid rgb(238, 238, 238);
        opacity: 0;
        transition: unset;
        align-items: center;
        justify-content: space-between;

        text-align: center;
        .text {flex: 1;}

        &.show {
          min-width: 130px;
          opacity: 1;
          transition: all 0.5s ease;
        }

        .pushpin {
          margin-right: 6px;
          color: rgba(255, 255, 255, 0.65);
          cursor: pointer;

          &:hover {
            color: #fff;
          }
        }
      }

      .@{namespace}-simple-menu-sub-title {
        font-size: 14px;
      }

      &__content {
        height: calc(100% - @header-height) !important;

        .scrollbar__wrap {
          height: 100%;
          overflow-x: hidden;
        }

        .scrollbar__bar.is-horizontal {
          display: none;
        }

        .ant-menu {
          height: 100%;
        }

        .ant-menu-inline,
        .ant-menu-vertical,
        .ant-menu-vertical-left {
          border-right: 1px solid transparent;
        }
      }
    }

    &-drag-bar {
      position: absolute;
      top: 50px;
      right: -1px;
      width: 1px;
      height: calc(100% - 50px);
      cursor: ew-resize;
      background-color: #f8f8f9;
      border-top: none;
      border-bottom: none;
      box-shadow: 0 0 4px 0 rgba(28, 36, 56, 0.15);
    }
  }
</style>
