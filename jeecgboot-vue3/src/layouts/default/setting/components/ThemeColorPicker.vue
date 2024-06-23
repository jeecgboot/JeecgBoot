<template>
  <div :class="prefixCls">
    <template v-for="color in colorList || []" :key="color">
      <span
        @click=" !isDisabledColor && handleClick(color)"
        :class="[
          `${prefixCls}__item`,
          {
            [`${prefixCls}__item--active`]: def === color,
            [`${prefixCls}__item--black`]: color == '#ffffff',
            disabledColor: isDisabledColor,
          },
        ]"
        :style="{ background: color }"
      >
        <CheckOutlined />
      </span>
    </template>
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType, watch, ref } from 'vue';
  import { CheckOutlined } from '@ant-design/icons-vue';

  import { useDesign } from '/@/hooks/web/useDesign';

  import { baseHandler } from '../handler';
  import { HandlerEnum } from '../enum';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { ThemeEnum } from '/@/enums/appEnum';

  export default defineComponent({
    name: 'ThemeColorPicker',
    components: { CheckOutlined },
    props: {
      colorList: {
        type: Array as PropType<string[]>,
        defualt: [],
      },
      event: {
        type: Number as PropType<HandlerEnum>,
      },
      def: {
        type: String,
      },
    },
    setup(props) {
      const { prefixCls } = useDesign('setting-theme-picker');
      const { getDarkMode } = useRootSetting();
      const isDisabledColor = ref(false);

      function handleClick(color: string) {
        props.event && baseHandler(props.event, color);
      }
      // update-begin--author:liaozhiyang---date:20240417---for:【QQYUN-8927】暗黑主题下不允许切换顶栏主题和菜单主题
      watch(
        () => getDarkMode.value,
        (newValue) => {
          isDisabledColor.value = props.event === 1 ? false : newValue === ThemeEnum.DARK;
        },
        { immediate: true }
      );
      // update-end--author:liaozhiyang---date:20240417---for:【QQYUN-8927】暗黑主题下不允许切换顶栏主题和菜单主题
      return {
        prefixCls,
        handleClick,
        isDisabledColor
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-setting-theme-picker';

  .@{prefix-cls} {
    display: flex;
    flex-wrap: wrap;
    margin: 16px 0;
    justify-content: space-around;
    // update-begin--author:liaozhiyang---date:20231220---for：【QQYUN-7677】antd4兼容改造，勾选垂直居中
    line-height: 1.3;
    // update-end--author:liaozhiyang---date:20231220---for：【QQYUN-7677】antd4兼容改造，勾选垂直居中
    &__item {
      width: 20px;
      height: 20px;
      cursor: pointer;
      border: 1px solid #ddd;
      border-radius: 2px;
      &.disabledColor {
        cursor: not-allowed;
        opacity: 0.5;
      }
      svg {
        display: none;
      }

      &--active {
        svg {
          display: inline-block;
          margin: 0 0 3px 3px;
          font-size: 12px;
          fill: @white !important;
        }
      }
      &--black {
        svg {
          fill: #000 !important;
        }
      }
    }
  }
</style>
