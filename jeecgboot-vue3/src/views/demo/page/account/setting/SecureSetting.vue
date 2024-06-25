<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <template v-for="item in list" :key="item.key">
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ item.title }}
              <div class="extra" v-if="item.extra" @click="extraClick(item.key)">
                {{ item.extra }}
              </div>
            </template>
            <template #description>
              <div>{{ item.description }} </div>
            </template>
          </ListItemMeta>
        </ListItem>
      </template>
    </List>
  </CollapseContainer>
  <UpdatePassword ref="updatePasswordRef" />
</template>
<script lang="ts">
  import { List } from 'ant-design-vue';
  import { defineComponent, ref } from 'vue';
  import { CollapseContainer } from '/@/components/Container/index';

  import { secureSettingList } from './data';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useUserStore } from '/@/store/modules/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    components: {
      CollapseContainer,
      List,
      ListItem: List.Item,
      ListItemMeta: List.Item.Meta,
      UpdatePassword: createAsyncComponent(() => import('/@/layouts/default/header/components/user-dropdown/UpdatePassword.vue')),
    },
    setup() {
      const { createMessage } = useMessage();
      const userStore = useUserStore();
      const updatePasswordRef = ref();
      function extraClick(key) {
        if (key == '1') {
          updatePasswordRef.value.show(userStore.getUserInfo.username);
        } else {
          createMessage.warning('暂不支持');
        }
      }
      return {
        updatePasswordRef,
        extraClick,
        list: secureSettingList,
      };
    },
  });
</script>
<style lang="less" scoped>
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    font-weight: normal;
    color: #1890ff;
    cursor: pointer;
  }
</style>
