<script lang="tsx">
  import type { ContextMenuItem, ItemContentProps, Axis } from './typing';
  import type { FunctionalComponent, CSSProperties } from 'vue';
  import { defineComponent, nextTick, onMounted, computed, ref, unref, onUnmounted } from 'vue';
  import Icon from '/@/components/Icon';
  import { Menu, Divider } from 'ant-design-vue';

  const prefixCls = 'context-menu';

  const props = {
    width: { type: Number, default: 156 },
    customEvent: { type: Object as PropType<Event>, default: null },
    styles: { type: Object as PropType<CSSProperties> },
    showIcon: { type: Boolean, default: true },
    axis: {
      // The position of the right mouse button click
      type: Object as PropType<Axis>,
      default() {
        return { x: 0, y: 0 };
      },
    },
    items: {
      // The most important list, if not, will not be displayed
      type: Array as PropType<ContextMenuItem[]>,
      default() {
        return [];
      },
    },
  };

  const ItemContent: FunctionalComponent<ItemContentProps> = (props) => {
    const { item } = props;
    return (
      <span style="display: inline-block; width: 100%; " class="px-4" onClick={props.handler.bind(null, item)}>
        {props.showIcon && item.icon && <Icon class="mr-2" icon={item.icon} />}
        <span>{item.label}</span>
      </span>
    );
  };

  export default defineComponent({
    name: 'ContextMenu',
    props,
    setup(props) {
      const wrapRef = ref(null);
      const showRef = ref(false);

      const getStyle = computed((): CSSProperties => {
        const { axis, items, styles, width } = props;
        const { x, y } = axis || { x: 0, y: 0 };
        const menuHeight = (items || []).length * 40;
        const menuWidth = width;
        const body = document.body;

        const left = body.clientWidth < x + menuWidth ? x - menuWidth : x;
        const top = body.clientHeight < y + menuHeight ? y - menuHeight : y;
        return {
          ...styles,
          width: `${width}px`,
          left: `${left + 1}px`,
          top: `${top + 1}px`,
        };
      });

      onMounted(() => {
        nextTick(() => (showRef.value = true));
      });

      onUnmounted(() => {
        const el = unref(wrapRef);
        el && document.body.removeChild(el);
      });

      function handleAction(item: ContextMenuItem, e: MouseEvent) {
        const { handler, disabled } = item;
        if (disabled) {
          return;
        }
        showRef.value = false;
        e?.stopPropagation();
        e?.preventDefault();
        handler?.();
      }

      function renderMenuItem(items: ContextMenuItem[]) {
        return items.map((item) => {
          const { disabled, label, children, divider = false } = item;

          const contentProps = {
            item,
            handler: handleAction,
            showIcon: props.showIcon,
          };

          if (!children || children.length === 0) {
            return (
              <>
                <Menu.Item disabled={disabled} class={`${prefixCls}__item`} key={label}>
                  <ItemContent {...contentProps} />
                </Menu.Item>
                {divider ? <Divider key={`d-${label}`} /> : null}
              </>
            );
          }
          if (!unref(showRef)) return null;

          return (
            <Menu.SubMenu key={label} disabled={disabled} popupClassName={`${prefixCls}__popup`}>
              {{
                title: () => <ItemContent {...contentProps} />,
                default: () => renderMenuItem(children),
              }}
            </Menu.SubMenu>
          );
        });
      }
      return () => {
        if (!unref(showRef)) {
          return null;
        }
        const { items } = props;
        return (
          <Menu inlineIndent={12} mode="vertical" class={prefixCls} ref={wrapRef} style={unref(getStyle)}>
            {renderMenuItem(items)}
          </Menu>
        );
      };
    },
  });
</script>
<style lang="less">
  @default-height: 42px !important;

  @small-height: 36px !important;

  @large-height: 36px !important;

  .item-style() {
    li {
      display: inline-block;
      width: 100%;
      height: @default-height;
      margin: 0 !important;
      line-height: @default-height;

      span {
        line-height: @default-height;
      }

      > div {
        margin: 0 !important;
      }

      &:not(.ant-menu-item-disabled):hover {
        color: @text-color-base;
        background-color: @item-hover-bg;
      }
    }
  }

  .context-menu {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 200;
    display: block;
    width: 156px;
    margin: 0;
    list-style: none;
    background-color: @component-background;
    border: 1px solid rgba(0, 0, 0, 0.08);
    border-radius: 0.25rem;
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.1), 0 1px 5px 0 rgba(0, 0, 0, 0.06);
    background-clip: padding-box;
    user-select: none;

    .item-style();

    .ant-divider {
      margin: 0 0;
    }

    &__popup {
      .ant-divider {
        margin: 0 0;
      }

      .item-style();
    }

    .ant-menu-submenu-title,
    .ant-menu-item {
      padding: 0 !important;
    }
  }
</style>
