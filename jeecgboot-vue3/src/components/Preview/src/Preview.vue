<template>
  <div :class="prefixCls">
    <PreviewGroup>
      <slot v-if="!imageList || $slots.default"></slot>
      <template v-else>
        <template v-for="item in getImageList" :key="item.src">
          <Image v-bind="item">
            <template #placeholder v-if="item.placeholder">
              <Image v-bind="item" :src="item.placeholder" :preview="false" />
            </template>
          </Image>
        </template>
      </template>
    </PreviewGroup>
  </div>
</template>
<script lang="ts">
  import type { PropType } from 'vue';
  import { defineComponent, computed } from 'vue';

  import { Image } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { propTypes } from '/@/utils/propTypes';
  import { isString } from '/@/utils/is';

  interface ImageProps {
    alt?: string;
    fallback?: string;
    src: string;
    width: string | number;
    height?: string | number;
    placeholder?: string | boolean;
    preview?:
      | boolean
      | {
          visible?: boolean;
          onVisibleChange?: (visible: boolean, prevVisible: boolean) => void;
          getContainer: string | HTMLElement | (() => HTMLElement);
        };
  }

  type ImageItem = string | ImageProps;

  export default defineComponent({
    name: 'ImagePreview',
    components: {
      Image,
      PreviewGroup: Image.PreviewGroup,
    },
    props: {
      functional: propTypes.bool,
      imageList: {
        type: Array as PropType<ImageItem[]>,
      },
    },
    setup(props) {
      const { prefixCls } = useDesign('image-preview');

      const getImageList = computed((): any[] => {
        const { imageList } = props;
        if (!imageList) {
          return [];
        }
        return imageList.map((item) => {
          if (isString(item)) {
            return {
              src: item,
              placeholder: false,
            };
          }
          return item;
        });
      });

      return {
        prefixCls,
        getImageList,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-image-preview';

  .@{prefix-cls} {
    .ant-image {
      margin-right: 10px;
    }

    .ant-image-preview-operations {
      background-color: rgba(0, 0, 0, 0.4);
    }
  }
</style>
