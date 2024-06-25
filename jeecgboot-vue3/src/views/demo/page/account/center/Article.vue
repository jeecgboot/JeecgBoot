<template>
  <List item-layout="vertical" :class="prefixCls">
    <template v-for="item in list" :key="item.title">
      <ListItem>
        <ListItemMeta>
          <template #description>
            <div :class="`${prefixCls}__content`">
              {{ item.content }}
            </div>
          </template>
          <template #title>
            <p :class="`${prefixCls}__title`">
              {{ item.title }}
            </p>
            <div>
              <template v-for="tag in item.description" :key="tag">
                <Tag class="mb-2">
                  {{ tag }}
                </Tag>
              </template>
            </div>
          </template>
        </ListItemMeta>
        <div>
          <template v-for="action in actions" :key="action.text">
            <div :class="`${prefixCls}__action`">
              <Icon v-if="action.icon" :class="`${prefixCls}__action-icon`" :icon="action.icon" :color="action.color" />
              {{ action.text }}
            </div>
          </template>
          <span :class="`${prefixCls}__time`">{{ item.time }}</span>
        </div>
      </ListItem>
    </template>
  </List>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { List, Tag } from 'ant-design-vue';
  import Icon from '/@/components/Icon/index';
  import { actions, articleList } from './data';

  export default defineComponent({
    components: {
      List,
      ListItem: List.Item,
      ListItemMeta: List.Item.Meta,
      Tag,
      Icon,
    },
    setup() {
      return {
        prefixCls: 'account-center-article',
        list: articleList,
        actions,
      };
    },
  });
</script>
<style lang="less" scoped>
  .account-center-article {
    &__title {
      margin-bottom: 12px;
      font-size: 18px;
    }

    &__content {
      color: rgba(0, 0, 0, 0.65);
    }

    &__action {
      display: inline-block;
      padding: 0 16px;
      color: rgba(0, 0, 0, 0.45);

      &:nth-child(1),
      &:nth-child(2) {
        border-right: 1px solid rgba(206, 206, 206, 0.4);
      }

      &-icon {
        margin-right: 3px;
      }
    }

    &__time {
      position: absolute;
      right: 20px;
      color: rgba(0, 0, 0, 0.45);
    }
  }
</style>
