<template>
  <a-dropdown :class="[prefixCls]" :trigger="trigger" v-bind="$attrs">
    <span>
      <slot></slot>
    </span>
    <template #overlay>
      <a-menu :class="[`${prefixCls}-menu`]" :selectedKeys="selectedKeys">
        <template v-for="item in dropMenuList" :key="`${item.event}`">
          <a-menu-item
            v-if="!item.hide"
            v-bind="getAttr(item.event)"
            @click="handleClickMenu(item)"
            :disabled="item.disabled"
            :class="[{ 'is-pop-confirm': item.popConfirm }, item.class ?? []]"
          >
            <a-popconfirm :disabled="item.disabled" v-if="popconfirm && item.popConfirm" v-bind="getPopConfirmAttrs(item.popConfirm)">
              <template #icon v-if="item.popConfirm.icon">
                <Icon v-if="item.iconColor" :icon="item.popConfirm.icon" :color="item.iconColor" />
                <Icon v-else :icon="item.popConfirm.icon" />
              </template>
              <div class="dropdown-event-area">
                <Icon :icon="item.icon" v-if="item.icon && item.iconColor" :color="item.iconColor" />
                <Icon :icon="item.icon" v-else-if="item.icon" />
                <span class="ml-1">{{ item.text }}</span>
              </div>
            </a-popconfirm>
            <!--  设置动态插槽   -->
            <template v-else-if="item.slot">
              <slot :name="item.slot" :label="item.text"></slot>
            </template>
            <template v-else>
              <Icon :icon="item.icon" v-if="item.icon && item.iconColor" :color="item.iconColor" />
              <Icon :icon="item.icon" v-else-if="item.icon" />
              <span class="ml-1">{{ item.text }}</span>
            </template>
          </a-menu-item>
          <a-menu-divider v-if="item.divider" :key="`d-${item.event}`" />
        </template>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script lang="ts" setup>
  import { computed, PropType, ref } from 'vue';
  import type { DropMenu } from './typing';
  import { Dropdown, Menu, Popconfirm } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { omit } from 'lodash-es';
  import { isFunction } from '/@/utils/is';
  import { useDesign } from '/@/hooks/web/useDesign';

  const ADropdown = Dropdown;
  const AMenu = Menu;
  const AMenuItem = Menu.Item;
  const AMenuDivider = Menu.Divider;
  const APopconfirm = Popconfirm;

  const { prefixCls } = useDesign('basic-dropdown');
  const props = defineProps({
    popconfirm: Boolean,
    /**
     * the trigger mode which executes the drop-down action
     * @default ['hover']
     * @type string[]
     */
    trigger: {
      type: [Array] as PropType<('contextmenu' | 'click' | 'hover')[]>,
      default: () => {
        return ['contextmenu'];
      },
    },
    dropMenuList: {
      type: Array as PropType<(DropMenu & Recordable)[]>,
      default: () => [],
    },
    selectedKeys: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
  });

  const emit = defineEmits(['menuEvent']);

  function handleClickMenu(item: DropMenu) {
    const { event } = item;
    const menu = props.dropMenuList.find((item) => `${item.event}` === `${event}`);
    emit('menuEvent', menu);
    item.onClick?.();
  }

  const getPopConfirmAttrs = computed(() => {
    return (attrs) => {
      const originAttrs = omit(attrs, ['confirm', 'cancel', 'icon']);
      if (!attrs.onConfirm && attrs.confirm && isFunction(attrs.confirm)) originAttrs['onConfirm'] = attrs.confirm;
      if (!attrs.onCancel && attrs.cancel && isFunction(attrs.cancel)) originAttrs['onCancel'] = attrs.cancel;
      return originAttrs;
    };
  });

  const getAttr = (key: string | number) => ({ key });
</script>

<style lang="less">
  @prefix-cls: ~'@{namespace}-basic-dropdown';

  .@{prefix-cls} {
    // 代码逻辑说明: 【VUEN-180】更多下拉菜单，只有点到字上才有效，点到空白处什么都不会发生，体验不好
    &-menu .ant-dropdown-menu-item.is-pop-confirm {
      padding: 0;

      .dropdown-event-area {
        padding: 5px 12px;
      }
    }
  }
</style>
