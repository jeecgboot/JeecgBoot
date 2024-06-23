<template>
  <a-list :class="prefixCls" :pagination="getPagination">
    <template v-for="item in getData" :key="item.id">
      <a-list-item class="list-item" @click="handleTitleClick(item)" :style="{ cursor: isTitleClickable ? 'pointer' : '' }">
        <a-list-item-meta>
          <template #title>
            <div class="title">
              <a-typography-paragraph
                style="width: 100%; margin-bottom: 0 !important"
                :delete="!!item.titleDelete"
                :ellipsis="$props.titleRows && $props.titleRows > 0 ? { rows: $props.titleRows, tooltip: !!item.title } : false"
                :content="item.title"
              />
              <div class="extra" v-if="item.extra">
                <a-tag class="tag" :color="item.color">
                  {{ item.extra }}
                </a-tag>
              </div>
            </div>
          </template>

          <template #avatar>
            <a-avatar v-if="item.avatar" class="avatar" :src="item.avatar" />
            <template v-else-if="item.priority">
              <a-avatar v-if="item.priority === PriorityTypes.L" class="avatar priority-L" title="一般消息">
                <template #icon>
                  <Icon icon="entypo:info" />
                </template>
              </a-avatar>
              <a-avatar v-if="item.priority === PriorityTypes.M" class="avatar priority-M" title="重要消息">
                <template #icon>
                  <Icon icon="bi:exclamation-lg" />
                </template>
              </a-avatar>
              <a-avatar v-if="item.priority === PriorityTypes.H" class="avatar priority-H" title="紧急消息">
                <template #icon>
                  <Icon icon="ant-design:warning-filled" />
                </template>
              </a-avatar>
            </template>
            <span v-else> {{ item.avatar }}</span>
          </template>

          <template #description>
            <div>
              <div class="description" v-if="item.description">
                <a-typography-paragraph
                  style="width: 100%; margin-bottom: 0 !important"
                  :ellipsis="$props.descRows && $props.descRows > 0 ? { rows: $props.descRows, tooltip: !!item.description } : false"
                  :content="item.description"
                />
              </div>
              <div class="datetime">
                <Time :value="item.datetime" :title="item.datetime" />
              </div>
            </div>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>
<script lang="ts">
  import { computed, defineComponent, PropType, ref, watch, unref } from 'vue';
  import { PriorityTypes, ListItem } from './data';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { List, Avatar, Tag, Typography } from 'ant-design-vue';
  import { Time } from '/@/components/Time';
  import { isNumber } from '/@/utils/is';
  export default defineComponent({
    components: {
      [Avatar.name]: Avatar,
      [List.name]: List,
      [List.Item.name]: List.Item,
      AListItemMeta: List.Item.Meta,
      ATypographyParagraph: Typography.Paragraph,
      [Tag.name]: Tag,
      Time,
    },
    props: {
      list: {
        type: Array as PropType<ListItem[]>,
        default: () => [],
      },
      pageSize: {
        type: [Boolean, Number] as PropType<Boolean | Number>,
        default: 5,
      },
      currentPage: {
        type: Number,
        default: 1,
      },
      titleRows: {
        type: Number,
        default: 1,
      },
      descRows: {
        type: Number,
        default: 1,
      },
      onTitleClick: {
        type: Function as PropType<(Recordable) => void>,
      },
    },
    emits: ['update:currentPage'],
    setup(props, { emit }) {
      const { prefixCls } = useDesign('header-notify-list');
      const current = ref(props.currentPage || 1);
      const getData = computed(() => {
        const { pageSize, list } = props;
        if (pageSize === false) return [];
        let size = isNumber(pageSize) ? pageSize : 5;
        return list.slice(size * (unref(current) - 1), size * unref(current));
      });
      watch(
        () => props.currentPage,
        (v) => {
          current.value = v;
        }
      );
      const isTitleClickable = computed(() => !!props.onTitleClick);
      const getPagination = computed(() => {
        const { list, pageSize } = props;
        if (pageSize > 0 && list && list.length > pageSize) {
          return {
            total: list.length,
            pageSize,
            //size: 'small',
            current: unref(current),
            onChange(page) {
              current.value = page;
              emit('update:currentPage', page);
            },
          };
        } else {
          return false;
        }
      });

      function handleTitleClick(item: ListItem) {
        props.onTitleClick && props.onTitleClick(item);
      }

      return {
        prefixCls,
        getPagination,
        getData,
        handleTitleClick,
        isTitleClickable,
        PriorityTypes,
      };
    },
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-header-notify-list';

  .@{prefix-cls} {
    width: 340px;

    &::-webkit-scrollbar {
      display: none;
    }

    :deep(.ant-pagination-disabled) {
      display: inline-block !important;
    }

    &-item {
      padding: 6px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s;

      .title {
        margin-bottom: 8px;
        font-weight: normal;

        .extra {
          float: right;
          margin-top: -1.5px;
          margin-right: 0;
          font-weight: normal;

          .tag {
            margin-right: 0;
          }
        }

        .avatar {
          margin-top: 4px;
        }

        .description {
          font-size: 12px;
          line-height: 18px;
        }

        .datetime {
          margin-top: 4px;
          font-size: 12px;
          line-height: 18px;
        }
      }
    }

    .list-item {
      .priority-L,
      .priority-M,
      .priority-H {
        font-size: 12px;
      }

      .priority-L {
        background-color: #7cd1ff;
      }

      .priority-M {
        background-color: #ffa743;
      }

      .priority-H {
        background-color: #f8766c;
      }

      .description {
        font-size: 12px;
        line-height: 18px;
      }
    }
  }
</style>
