<template>
  <div :class="[prefixCls, getAlign]" @click="onCellClick">
    <template v-for="(action, index) in getActions" :key="`${index}-${action.label}`">
      <template v-if="action.slot">
        <slot name="customButton"></slot>
      </template>
      <template v-else>
        <Tooltip v-if="action.tooltip" v-bind="getTooltip(action.tooltip)">
          <PopConfirmButton v-bind="action">
            <Icon :icon="action.icon" :class="{ 'mr-1': !!action.label }" v-if="action.icon" />
            <template v-if="action.label">{{ action.label }}</template>
          </PopConfirmButton>
        </Tooltip>
        <PopConfirmButton v-else v-bind="action">
          <Icon :icon="action.icon" :class="{ 'mr-1': !!action.label }" v-if="action.icon" />
          <template v-if="action.label">{{ action.label }}</template>
        </PopConfirmButton>
      </template>

      <Divider type="vertical" class="action-divider" v-if="divider && index < getActions.length - 1" />
    </template>
    <Dropdown
      :overlayClassName="dropdownCls"
      :trigger="['hover']"
      :dropMenuList="getDropdownList"
      popconfirm
      v-if="dropDownActions && getDropdownList.length > 0"
    >
      <slot name="more"></slot>
      <!--  设置插槽   -->
      <template v-slot:[item.slot] v-for="(item, index) in getDropdownSlotList" :key="`${index}-${item.label}`">
        <slot :name="item.slot"></slot>
      </template>

      <a-button type="link" size="small" v-if="!$slots.more"> 更多 <Icon icon="mdi-light:chevron-down"></Icon> </a-button>
    </Dropdown>
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType, computed, toRaw, unref } from 'vue';
  import { MoreOutlined } from '@ant-design/icons-vue';
  import { Divider, Tooltip, TooltipProps } from 'ant-design-vue';
  import Icon from '/@/components/Icon/index';
  import { ActionItem, TableActionType } from '/@/components/Table';
  import { PopConfirmButton } from '/@/components/Button';
  import { Dropdown } from '/@/components/Dropdown';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useTableContext } from '../hooks/useTableContext';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { isBoolean, isFunction, isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { ACTION_COLUMN_FLAG } from '../const';

  export default defineComponent({
    name: 'TableAction',
    components: { Icon, PopConfirmButton, Divider, Dropdown, MoreOutlined, Tooltip },
    props: {
      actions: {
        type: Array as PropType<ActionItem[]>,
        default: null,
      },
      dropDownActions: {
        type: Array as PropType<ActionItem[]>,
        default: null,
      },
      divider: propTypes.bool.def(true),
      outside: propTypes.bool,
      stopButtonPropagation: propTypes.bool.def(false),
    },
    setup(props) {
      const { prefixCls } = useDesign('basic-table-action');
      const dropdownCls = `${prefixCls}-dropdown`;
      let table: Partial<TableActionType> = {};
      if (!props.outside) {
        table = useTableContext();
      }

      const { hasPermission } = usePermission();
      function isIfShow(action: ActionItem): boolean {
        const ifShow = action.ifShow;

        let isIfShow = true;

        if (isBoolean(ifShow)) {
          isIfShow = ifShow;
        }
        if (isFunction(ifShow)) {
          isIfShow = ifShow(action);
        }
        return isIfShow;
      }

      const getActions = computed(() => {
        return (toRaw(props.actions) || [])
          .filter((action) => {
            return hasPermission(action.auth) && isIfShow(action);
          })
          .map((action) => {
            const { popConfirm } = action;
            // update-begin--author:liaozhiyang---date:20240105---for：【issues/951】table删除记录时按钮显示错位
            if (popConfirm) {
              const overlayClassName = popConfirm.overlayClassName;
              popConfirm.overlayClassName = `${overlayClassName ? overlayClassName : ''} ${prefixCls}-popconfirm`;
            }
            // update-end--author:liaozhiyang---date:20240105---for：【issues/951】table删除记录时按钮显示错位
            return {
              getPopupContainer: () => unref((table as any)?.wrapRef.value) ?? document.body,
              type: 'link',
              size: 'small',
              ...action,
              ...(popConfirm || {}),
              // update-begin--author:liaozhiyang---date:20240108---for：【issues/936】表格操作栏删除当接口失败时，气泡确认框不会消失
              onConfirm: handelConfirm(popConfirm?.confirm),
              // update-end--author:liaozhiyang---date:20240108---for：【issues/936】表格操作栏删除当接口失败时，气泡确认框不会消失
              onCancel: popConfirm?.cancel,
              enable: !!popConfirm,
            };
          });
      });

      const getDropdownList = computed((): any[] => {
        //过滤掉隐藏的dropdown,避免出现多余的分割线
        const list = (toRaw(props.dropDownActions) || []).filter((action) => {
          return hasPermission(action.auth) && isIfShow(action);
        });
        return list.map((action, index) => {
          const { label, popConfirm } = action;
          // update-begin--author:liaozhiyang---date:20240105---for：【issues/951】table删除记录时按钮显示错位
          if (popConfirm) {
            const overlayClassName = popConfirm.overlayClassName;
            popConfirm.overlayClassName = `${overlayClassName ? overlayClassName : ''} ${prefixCls}-popconfirm`;
          }
          // update-end--author:liaozhiyang---date:20240105---for：【issues/951】table删除记录时按钮显示错位
          // update-begin--author:liaozhiyang---date:20240108---for：【issues/936】表格操作栏删除当接口失败时，气泡确认框不会消失
          if (popConfirm) {
            popConfirm.confirm = handelConfirm(popConfirm?.confirm);
          }
          // update-end--author:liaozhiyang---date:20240108---for：【issues/936】表格操作栏删除当接口失败时，气泡确认框不会消失
          return {
            ...action,
            ...popConfirm,
            onConfirm: handelConfirm(popConfirm?.confirm),
            onCancel: popConfirm?.cancel,
            text: label,
            divider: index < list.length - 1 ? props.divider : false,
          };
        });
      });
      /*
      2023-01-08
      liaozhiyang
      给传进来的函数包一层promise
      */
      const handelConfirm = (fn) => {
        if (typeof fn !== 'function') return fn;
        const anyc = () => {
          return new Promise<void>((resolve) => {
            const result = fn();
            if (Object.prototype.toString.call(result) === '[object Promise]') {
              result
                .finally(() => {
                  resolve();
                })
                .catch((err) => {
                  console.log(err);
                });
            } else {
              resolve();
            }
          });
        };
        return anyc;
      };
      const getDropdownSlotList = computed((): any[] => {
        return unref(getDropdownList).filter((item) => item.slot);
      });
      const getAlign = computed(() => {
        const columns = (table as TableActionType)?.getColumns?.() || [];
        const actionColumn = columns.find((item) => item.flag === ACTION_COLUMN_FLAG);
        return actionColumn?.align ?? 'left';
      });

      function getTooltip(data: string | TooltipProps): TooltipProps {
        return {
          getPopupContainer: () => unref((table as any)?.wrapRef.value) ?? document.body,
          placement: 'bottom',
          ...(isString(data) ? { title: data } : data),
        };
      }

      function onCellClick(e: MouseEvent) {
        if (!props.stopButtonPropagation) return;
        const path = e.composedPath() as HTMLElement[];
        const isInButton = path.find((ele) => {
          return ele.tagName?.toUpperCase() === 'BUTTON';
        });
        isInButton && e.stopPropagation();
      }
     
      return { prefixCls, getActions, getDropdownList, getDropdownSlotList, getAlign, onCellClick, getTooltip, dropdownCls };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-basic-table-action';

  .@{prefix-cls} {
    display: flex;
    align-items: center;
    /* update-begin-author:taoyan date:2022-11-18 for: 表格默认行高比官方示例多出2px*/
    height: 22px;
    /* update-end-author:taoyan date:2022-11-18 for: 表格默认行高比官方示例多出2px*/

    .action-divider {
      display: table;
    }

    &.left {
      justify-content: flex-start;
    }

    &.center {
      justify-content: center;
    }

    &.right {
      justify-content: flex-end;
    }

    button {
      display: flex;
      align-items: center;

      span {
        margin-left: 0 !important;
      }
    }

    button.ant-btn-circle {
      span {
        margin: auto !important;
      }
    }

    .ant-divider,
    .ant-divider-vertical {
      margin: 0 2px;
    }

    .icon-more {
      transform: rotate(90deg);

      svg {
        font-size: 1.1em;
        font-weight: 700;
      }
    }
    &-popconfirm {
      .ant-popconfirm-buttons {
        min-width: 120px;
        // update-begin--author:liaozhiyang---date:20240124---for：【issues/1019】popConfirm确认框待端后端返回过程中（处理中）样式错乱
        display: flex;
        align-items: center;
        justify-content: center;
        // update-end--author:liaozhiyang---date:20240124---for：【issues/1019】popConfirm确认框待端后端返回过程中（处理中）样式错乱
      }
    }
    // update-begin--author:liaozhiyang---date:20240407---for：【QQYUN-8762】调整table操作栏ant-dropdown样式
    &-dropdown {
      .ant-dropdown-menu .ant-dropdown-menu-item-divider {
        margin: 2px 0;
      }
      .ant-dropdown-menu .ant-dropdown-menu-item {
        padding: 3px 8px;
        font-size: 13.6px;
      }
      .dropdown-event-area {
        padding: 0 !important;
      }
    }
    // update-end--author:liaozhiyang---date:20240407---for：【QQYUN-8762】调整table操作栏ant-dropdown样式
  }
</style>
