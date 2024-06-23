<template>
  <li :class="getClass" @click.stop="handleClickItem" :style="getCollapse ? {} : getItemStyle">
    <Tooltip placement="right" v-if="showTooptip">
      <template #title>
        <slot name="title"></slot>
      </template>
      <div :class="`${prefixCls}-tooltip`">
        <slot></slot>
      </div>
    </Tooltip>

    <template v-else>
      <slot></slot>
      <slot name="title"></slot>
    </template>
  </li>
</template>

<script lang="ts">
  import { PropType } from 'vue';
  import { defineComponent, ref, computed, unref, getCurrentInstance, watch } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { propTypes } from '/@/utils/propTypes';
  import { useMenuItem } from './useMenu';
  import { Tooltip } from 'ant-design-vue';
  import { useSimpleRootMenuContext } from './useSimpleMenuContext';
  import { useLocaleStore } from '/@/store/modules/locale';

  export default defineComponent({
    name: 'MenuItem',
    components: { Tooltip },
    props: {
      name: {
        type: [String, Number] as PropType<string | number>,
        required: true,
      },
      disabled: propTypes.bool,
    },
    setup(props, { slots }) {
      const instance = getCurrentInstance();
      const localeStore = useLocaleStore();

      const active = ref(false);

      const { getItemStyle, getParentList, getParentMenu, getParentRootMenu } = useMenuItem(instance);

      const { prefixCls } = useDesign('menu');

      const { rootMenuEmitter, activeName } = useSimpleRootMenuContext();

      const getClass = computed(() => {
        return [
          `${prefixCls}-item`,
          {
            [`${prefixCls}-item-active`]: unref(active),
            [`${prefixCls}-item-selected`]: unref(active),
            [`${prefixCls}-item-disabled`]: !!props.disabled,
          },
        ];
      });

      const getCollapse = computed(() => unref(getParentRootMenu)?.props.collapse);

      const showTooptip = computed(() => {
        return unref(getParentMenu)?.type.name === 'Menu' && unref(getCollapse) && slots.title;
      });

      function handleClickItem() {
        const { disabled } = props;
        if (disabled) {
          return;
        }

        rootMenuEmitter.emit('on-menu-item-select', props.name);
        if (unref(getCollapse)) {
          return;
        }
        const { uidList } = getParentList();

        rootMenuEmitter.emit('on-update-opened', {
          opend: false,
          parent: instance?.parent,
          uidList: uidList,
        });
      }
      watch(
        () => activeName.value,
        (name: string) => {
          if (name === props.name) {
            const { list, uidList } = getParentList();
            active.value = true;
            list.forEach((item) => {
              if (item.proxy) {
                (item.proxy as any).active = true;
              }
            });

            //存储路径和标题的关系
            storePathTitle(props.name);
            rootMenuEmitter.emit('on-update-active-name:submenu', uidList);
          } else {
            active.value = false;
          }
        },
        { immediate: true }
      );

      //update-begin-author:taoyan date:2022-6-1 for: VUEN-1144 online 配置成菜单后，打开菜单，显示名称未展示为菜单名称
      function storePathTitle(path) {
        console.log('storePathTitle', path);
        let title = '';
        if (instance!.attrs) {
          let item: any = instance!.attrs.item;
          if (item) {
            title = item.title;
          }
        }
        if (localeStore) {
          localeStore.setPathTitle(path, title);
        }
      }
      //update-end-author:taoyan date:2022-6-1 for: VUEN-1144 online 配置成菜单后，打开菜单，显示名称未展示为菜单名称

      return { getClass, prefixCls, getItemStyle, getCollapse, handleClickItem, showTooptip };
    },
  });
</script>
