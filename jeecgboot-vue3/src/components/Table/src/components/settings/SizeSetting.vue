<template>
  <Tooltip placement="top" v-bind="getBindProps">
    <template #title>
      <span>{{ t('component.table.settingDens') }}</span>
    </template>

    <Dropdown placement="bottom" :trigger="['click']" :getPopupContainer="getPopupContainer">
      <ColumnHeightOutlined />
      <template #overlay>
        <Menu @click="handleTitleClick" selectable v-model:selectedKeys="selectedKeysRef">
          <MenuItem key="large">
            <span>{{ t('component.table.settingDensLarge') }}</span>
          </MenuItem>
          <MenuItem key="middle">
            <span>{{ t('component.table.settingDensMiddle') }}</span>
          </MenuItem>
          <MenuItem key="small">
            <span>{{ t('component.table.settingDensSmall') }}</span>
          </MenuItem>
        </Menu>
      </template>
    </Dropdown>
  </Tooltip>
</template>
<script lang="ts">
  import type { SizeType } from '../../types/table';
  import { computed, defineComponent, ref } from 'vue';
  import { Tooltip, Dropdown, Menu } from 'ant-design-vue';
  import { ColumnHeightOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useTableContext } from '../../hooks/useTableContext';
  import { getPopupContainer } from '/@/utils';
  import { useRoute } from 'vue-router';
  import { createLocalStorage } from '/@/utils/cache';

  export default defineComponent({
    name: 'SizeSetting',
    props: {
      isMobile: Boolean,
    },
    components: {
      ColumnHeightOutlined,
      Tooltip,
      Dropdown,
      Menu,
      MenuItem: Menu.Item,
    },
    setup(props) {
      const table = useTableContext();
      const { t } = useI18n();
      const $ls = createLocalStorage();
      const route = useRoute();

      const selectedKeysRef = ref<SizeType[]>([table.getSize()]);
      const getBindProps = computed(() => {
        let obj = {};
        if (props.isMobile) {
          obj['visible'] = false;
        }
        return obj;
      });
      function handleTitleClick({ key }: { key: SizeType }) {
        selectedKeysRef.value = [key];
        table.setProps({
          size: key,
        });
        // update-begin--author:liaozhiyang---date:20240604---for：【TV360X-100】缓存表格密度
        $ls.set(cacheKey.value, key);
        // update-end--author:liaozhiyang---date:20240604---for：【TV360X-100】缓存表格密度
      }
      // update-begin--author:liaozhiyang---date:20240604---for：【TV360X-100】缓存表格密度
      const cacheKey = computed(() => {
        const path = route.path;
        let key = path.replace(/[\/\\]/g, '_');
        let cacheKey = table.getBindValues.value.tableSetting?.cacheKey;
        if (cacheKey) {
          key += ':' + cacheKey;
        }
        return 'tableSizeCache:' + key;
      });
      const local: SizeType | null = $ls.get(cacheKey.value);
      if (local) {
        selectedKeysRef.value = [local];
        table.setProps({
          size: local,
        });
      }
      // update-end--author:liaozhiyang---date:20240604---for：【TV360X-100】缓存表格密度

      return {
        getBindProps,
        handleTitleClick,
        selectedKeysRef,
        getPopupContainer,
        t,
      };
    },
  });
</script>
