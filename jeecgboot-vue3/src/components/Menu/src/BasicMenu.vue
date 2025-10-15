<template>
  <Menu
    :selectedKeys="selectedKeys"
    :defaultSelectedKeys="defaultSelectedKeys"
    :mode="mode"
    :openKeys="getOpenKeys"
    :inlineIndent="inlineIndent"
    :theme="theme"
    @openChange="handleOpenChange"
    :class="getMenuClass"
    @click="handleMenuClick"
    :subMenuOpenDelay="0.2"
    v-bind="getInlineCollapseOptions"
  >
    <template v-for="item in items" :key="item.path">
      <BasicSubMenuItem :item="item" :theme="theme" :isHorizontal="isHorizontal" />
    </template>
  </Menu>
</template>
<script lang="ts">
  import type { MenuState } from './types';
  import { computed, defineComponent, unref, reactive, watch, toRefs, ref } from 'vue';
  import { Menu } from 'ant-design-vue';
  import BasicSubMenuItem from './components/BasicSubMenuItem.vue';
  import { MenuModeEnum, MenuTypeEnum } from '/@/enums/menuEnum';
  import { useOpenKeys } from './useOpenKeys';
  import { RouteLocationNormalizedLoaded, useRouter } from 'vue-router';
  import { isFunction, isUrl } from '/@/utils/is';
  import { basicProps } from './props';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { REDIRECT_NAME } from '/@/router/constant';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { getCurrentParentPath } from '/@/router/menus';
  import { listenerRouteChange } from '/@/logics/mitt/routeChange';
  import { getAllParentPath } from '/@/router/helper/menuHelper';
  import { createBasicRootMenuContext } from './useBasicMenuContext';
  import { URL_HASH_TAB } from '/@/utils';
  import { getMenus } from '/@/router/menus';

  export default defineComponent({
    name: 'BasicMenu',
    components: {
      Menu,
      BasicSubMenuItem,
    },
    props: basicProps,
    emits: ['menuClick'],
    setup(props, { emit }) {
      const isClickGo = ref(false);
      const currentActiveMenu = ref('');

      const menuState = reactive<MenuState>({
        defaultSelectedKeys: [],
        openKeys: [],
        selectedKeys: [],
        collapsedOpenKeys: [],
      });
      // update-begin--author:liaozhiyang---date:20230326---for：【QQYUN-8691】顶部菜单模式online不显示菜单名显示默认的auto在线表单
      createBasicRootMenuContext({ menuState: menuState });
      // update-end--author:liaozhiyang---date:20230326---for：【QQYUN-8691】顶部菜单模式online不显示菜单名显示默认的auto在线表单
      const { prefixCls } = useDesign('basic-menu');
      const { items, mode, accordion } = toRefs(props);

      const { getCollapsed, getTopMenuAlign, getSplit } = useMenuSetting();

      const { currentRoute } = useRouter();

      const { handleOpenChange, setOpenKeys, getOpenKeys } = useOpenKeys(menuState, items, mode as any, accordion);

      const getIsTopMenu = computed(() => {
        const { type, mode } = props;

        return (type === MenuTypeEnum.TOP_MENU && mode === MenuModeEnum.HORIZONTAL) || (props.isHorizontal && unref(getSplit));
      });

      const getMenuClass = computed(() => {
        const align = props.isHorizontal && unref(getSplit) ? 'start' : unref(getTopMenuAlign);
        return [
          prefixCls,
          `justify-${align}`,
          {
            [`${prefixCls}__second`]: !props.isHorizontal && unref(getSplit),
            [`${prefixCls}__sidebar-hor`]: unref(getIsTopMenu),
          },
        ];
      });

      const getInlineCollapseOptions = computed(() => {
        const isInline = props.mode === MenuModeEnum.INLINE;

        const inlineCollapseOptions: { inlineCollapsed?: boolean } = {};
        if (isInline) {
          inlineCollapseOptions.inlineCollapsed = props.mixSider ? false : unref(getCollapsed);
        }
        return inlineCollapseOptions;
      });

      listenerRouteChange((route) => {
        if (route.name === REDIRECT_NAME) return;
        handleMenuChange(route);
        currentActiveMenu.value = route.meta?.currentActiveMenu as string;

        if (unref(currentActiveMenu)) {
          menuState.selectedKeys = [unref(currentActiveMenu)];
          setOpenKeys(unref(currentActiveMenu));
        }
      });

      !props.mixSider &&
        watch(
          () => props.items,
          () => {
            handleMenuChange();
          }
        );

      //update-begin-author:taoyan date:2022-6-1 for: VUEN-1144 online 配置成菜单后，打开菜单，显示名称未展示为菜单名称
      async function handleMenuClick({ item, key }: { item: any; key: string; keyPath: string[] }) {
        const { beforeClickFn } = props;
        // update-begin--author:liaozhiyang---date:20240402---for:【QQYUN-8773】配置外部网址在顶部菜单模式和搜索打不开
        if (isUrl(key)) {
          key = key.replace(URL_HASH_TAB, '#');
          window.open(key);
          return;
        }
        // update-begin--author:liaozhiyang---date:20250114---for:【issues/7706】顶部栏导航内部路由也可以支持采用新浏览器tab打开
        const menus = await getMenus();
        const findItem = getMatchingPath(menus, key);
        if (findItem?.internalOrExternal == true && !findItem?.children?.length) {
          // 一级菜单当设置了外部打开，只有没有子菜单时才生效
          window.open(location.origin + key);
          return;
        }
        // update-end--author:liaozhiyang---date:20250114---for:【issues/7706】顶部栏导航内部路由也可以支持采用新浏览器tab打开
        // update-end--author:liaozhiyang---date:20240402---for:【QQYUN-8773】配置外部网址在顶部菜单模式和搜索打不开
        if (beforeClickFn && isFunction(beforeClickFn)) {
          const flag = await beforeClickFn(key);
          if (!flag) return;
        }
        // update-begin--author:liaozhiyang---date:20240418---for:【QQYUN-8773】顶部混合导航(顶部左侧组合菜单)一级菜单没有配置redirect默认跳子菜单的第一个
        if (props.type === MenuTypeEnum.MIX) {
          const menuItem = getMatchingPath(menus, key);
          // 没有重定向且originComponent不是系统默认的就当做是组件，否则就跳子菜单的第一个
          if (menuItem && !menuItem.redirect && menuItem.originComponent == '/layouts/default/index' && menuItem.children?.length) {
            const subMenuItem = getSubMenu(menuItem.children);
            if (subMenuItem?.path) {
              const path = subMenuItem.redirect ?? subMenuItem.path;
              let _key = path;
              if (isUrl(path)) {
                // window.open(path);
                // 外部打开emit出去的key不能是url，否则左侧菜单出不来
                _key = key;
              }
              
              // update-begin--author:liaozhiyang---date:20250825---for:【QQYUN-13593】敲敲云首页菜单外部打开
              // =====================================================================
              // TODO: 临时代码 - 需要删除！！！
              // 这是针对敲敲云首页菜单的特殊处理，后续需要重构或删除
              // =====================================================================
              // 是外部打开且是白名单内的菜单，则直接打开
              if (subMenuItem?.internalOrExternal == true && ['/myapps/index'].includes(path)) {
                window.open(location.origin + path);
                return;
              }
              // =====================================================================
              // update-end--author:liaozhiyang---date:20250825---for:【QQYUN-13593】敲敲云首页菜单外部打开
              
              emit('menuClick', _key, { title: subMenuItem.title });
            } else {
              emit('menuClick', key, item);
            }
          } else {
            emit('menuClick', key, item);
          }
        } else {
          emit('menuClick', key, item);
        }
        // emit('menuClick', key, item);
        // update-begin--author:liaozhiyang---date:20240418---for:【QQYUN-8773】顶部混合导航(顶部左侧组合菜单)一级菜单没有配置redirect默认跳子菜单的第一个
        //update-end-author:taoyan date:2022-6-1 for: VUEN-1144 online 配置成菜单后，打开菜单，显示名称未展示为菜单名称

        isClickGo.value = true;
        // const parentPath = await getCurrentParentPath(key);

        // menuState.openKeys = [parentPath];
        menuState.selectedKeys = [key];
      }

      async function handleMenuChange(route?: RouteLocationNormalizedLoaded) {
        if (unref(isClickGo)) {
          isClickGo.value = false;
          return;
        }
        const path = (route || unref(currentRoute)).meta?.currentActiveMenu || (route || unref(currentRoute)).path;
        setOpenKeys(path);
        if (unref(currentActiveMenu)) return;
        if (props.isHorizontal && unref(getSplit)) {
          const parentPath = await getCurrentParentPath(path);
          menuState.selectedKeys = [parentPath];
        } else {
          const parentPaths = await getAllParentPath(props.items, path);
          menuState.selectedKeys = parentPaths;
        }
      }
      /**
       * liaozhiyang
       * 2024-05-18
       * 获取指定菜单下的第一个菜单(忽略隐藏路由)
       */
      function getSubMenu(menus) {
        for (let i = 0, len = menus.length; i < len; i++) {
          const item = menus[i];
          if (item.path && !item.hideMenu && !item.children?.length) {
            return item;
          } else if (item.children?.length) {
            const result = getSubMenu(item.children);
            if (result) {
              return result;
            }
          }
        }
        return null;
      }

      /**
       * liaozhiyang
       * 2024-05-18
       * 获取匹配path的菜单
       */
      function getMatchingPath(menus, path) {
        for (let i = 0, len = menus.length; i < len; i++) {
          const item = menus[i];
          if (item.path === path) {
            return item;
          } else if (item.children?.length) {
            const result = getMatchingPath(item.children, path);
            if (result) {
              return result;
            }
          }
        }
        return null;
      }
      /**
       * 2025-01-14
       * liaozhiyang
       * 获取菜单中匹配的path所在的项
       */
      const getMatchingMenu = (menus, path) => {
        for (let i = 0, len = menus.length; i < len; i++) {
          const item = menus[i];
          if (item.path === path && !item.redirect && !item.paramPath) {
            return item;
          } else if (item.children?.length) {
            const result = getMatchingMenu(item.children, path);
            if (result) {
              return result;
            }
          }
        }
        return '';
      };

      return {
        handleMenuClick,
        getInlineCollapseOptions,
        getMenuClass,
        handleOpenChange,
        getOpenKeys,
        ...toRefs(menuState),
      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
</style>
