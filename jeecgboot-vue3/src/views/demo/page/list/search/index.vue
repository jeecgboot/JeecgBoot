<template>
  <PageWrapper :class="prefixCls" title="搜索列表">
    <template #headerContent>
      <BasicForm :class="`${prefixCls}__header-form`" :labelWidth="100" :schemas="schemas" :showActionButtonGroup="false" />
    </template>

    <div :class="`${prefixCls}__container`">
      <a-list>
        <template v-for="item in list" :key="item.id">
          <a-list-item>
            <a-list-item-meta>
              <template #description>
                <div :class="`${prefixCls}__content`">
                  {{ item.content }}
                </div>
                <div :class="`${prefixCls}__action`">
                  <template v-for="action in actions" :key="action.icon">
                    <div :class="`${prefixCls}__action-item`">
                      <Icon v-if="action.icon" :class="`${prefixCls}__action-icon`" :icon="action.icon" :color="action.color" />
                      {{ action.text }}
                    </div>
                  </template>
                  <span :class="`${prefixCls}__time`">{{ item.time }}</span>
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
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { Tag } from 'ant-design-vue';
  import { defineComponent } from 'vue';
  import Icon from '/@/components/Icon/index';
  import { BasicForm } from '/@/components/Form/index';
  import { actions, searchList, schemas } from './data';
  import { PageWrapper } from '/@/components/Page';
  import { List } from 'ant-design-vue';

  export default defineComponent({
    components: {
      Icon,
      Tag,
      BasicForm,
      PageWrapper,
      [List.name]: List,
      [List.Item.name]: List.Item,
      AListItemMeta: List.Item.Meta,
    },
    setup() {
      return {
        prefixCls: 'list-search',
        list: searchList,
        actions,
        schemas,
      };
    },
  });
</script>
<style lang="less" scoped>
  .list-search {
    &__header {
      &-form {
        margin-bottom: -16px;
      }
    }

    &__container {
      padding: 12px;
      background-color: @component-background;
    }

    &__title {
      margin-bottom: 12px;
      font-size: 18px;
    }

    &__content {
      color: @text-color-secondary;
    }

    &__action {
      margin-top: 10px;

      &-item {
        display: inline-block;
        padding: 0 16px;
        color: @text-color-secondary;

        &:nth-child(1) {
          padding-left: 0;
        }

        &:nth-child(1),
        &:nth-child(2) {
          border-right: 1px solid @border-color-base;
        }
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
