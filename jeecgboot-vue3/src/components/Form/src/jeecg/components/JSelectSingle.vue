<!-- 字典下拉单选 / 远程加载下拉 -->
<template>
  <a-select
    :value="innerValue"
    :mode="mode"
    :disabled="disabled"
    :placeholder="placeholder"
    :filter-option="filterOptionComputed"
    :showSearch="showSearch"
    :getPopupContainer="getPopupContainer"
    :notFoundContent="loading ? undefined : null"
    allowClear
    @change="handleChange"
    :onSearch="showSearch ? handleSearch : undefined"
    @popupScroll="handlePopupScroll"
    @dropdownVisibleChange="handleDropdownVisibleChange"
    v-bind="$attrs"
  >
    <template #notFoundContent>
      <a-spin v-if="loading" size="small" />
    </template>
    <template v-for="item in selectOptions" :key="item.key">
      <a-select-option :value="item.value" :getPopupContainer="getPopupContainer">
        <span :class="item.class" :style="item.style">{{ item.text }}</span>
      </a-select-option>
    </template>
  </a-select>
</template>

<script setup lang="ts">
  import { computed, ref, watch, unref } from 'vue';
  import { debounce } from 'lodash-es';
  import { initDictOptions } from '/@/utils/dict/index';
  import { setPopContainer } from '/@/utils';
  import { defHttp } from '/@/utils/http/axios';

  defineOptions({ name: 'JSelectSingle' });

  // --- Props & Emits ---
  interface DictOption {
    value: string | number;
    label?: string;
    text?: string;
    color?: string;
  }

  interface SelectOptionItem {
    key: string;
    text: string;
    value: string | number;
    class: string[];
    style: { backgroundColor: string };
  }

  export type SelectSingleValue = string | number | undefined;

  const props = withDefaults(
    defineProps<{
      value?: SelectSingleValue;
      placeholder?: string;
      readOnly?: boolean;
      mode?: 'multiple' | 'tags' | 'combobox';
      options?: DictOption[];
      triggerChange?: boolean;
      popContainer?: string;
      dictCode?: string;
      disabled?: boolean;
      useDicColor?: boolean;
      pageSize?: number;
      scrollLoad?: boolean;
      showSearch?: boolean;
      filterOption?: (input: string, option: any) => boolean;
      onSearch?: (keyword: string) => void;
      /** 远程加载模式：启用滚动加载+远程搜索，通过 onLoad 回调获取分页数据 */
      remoteLoad?: boolean;
      onLoad?: (params: { keyword: string; pageNo: number; pageSize: number }) => Promise<DictOption[]>;
    }>(),
    {
      placeholder: '请选择',
      readOnly: false,
      options: () => [],
      triggerChange: true,
      popContainer: '',
      disabled: false,
      useDicColor: false,
      pageSize: 10,
      scrollLoad: false,
      showSearch: false,
      remoteLoad: false,
    }
  );
  const emit = defineEmits<{
    (e: 'change', value: SelectSingleValue): void;
    (e: 'update:value', value: SelectSingleValue): void;
    (e: 'input', value: SelectSingleValue): void;
    (e: 'options-change'): void;
  }>();

  // --- 选项数据 ---
  const innerValue = ref<SelectSingleValue>();
  const dictOptions = ref<DictOption[]>([]);
  const loading = ref(false);
  const searchKeyword = ref('');

  // --- 分页滚动状态 ---
  const scrollState = {
    pageNo: 1,
    hasMore: true,
    loading: false,
  };
  /** 缓存上次滚动的目标元素，关闭下拉时复位其滚动条 */
  let lastScrollTarget: HTMLElement | null = null;

  const isDictTable = computed(() => {
    if (!props.dictCode) return false;
    return props.dictCode.split(',').length >= 2;
  });

  const useLoadDict = computed(() => props.scrollLoad && isDictTable.value);

  const useRemoteLoad = computed(() => props.remoteLoad && typeof props.onLoad === 'function');

  const showSearch = computed(() => useLoadDict.value || useRemoteLoad.value || props.showSearch);

  const selectOptions = computed<SelectOptionItem[]>(() => {
    if (!Array.isArray(dictOptions.value)) return [];
    return dictOptions.value.map((item, index) => {
      const text = item.text ?? item.label ?? '';
      return {
        key: `${item.value}_${text}_${index}`,
        text,
        value: typeof innerValue.value === 'number' ? Number(item.value) : item.value,
        class: [props.useDicColor && item.color ? 'colorText' : ''],
        style: { backgroundColor: props.useDicColor && item.color ? String(item.color) : '' },
      };
    });
  });

  // --- 字典加载 ---
  function fetchDictPage(pageNo: number, append: boolean) {
    return defHttp
      .get({
        url: `/sys/dict/loadDict/${props.dictCode}`,
        params: { pageNo, pageSize: props.pageSize, keyword: searchKeyword.value, order: 'asc' },
      })
      .then((res: any[]) => {
        const items: DictOption[] = (res ?? []).map((it: any) => ({
          value: it.value,
          label: it.text ?? it.label,
        }));
        processPageResult(items, pageNo, append);
      });
  }

  // --- 远程加载 ---
  async function fetchRemotePage(pageNo: number, append: boolean) {
    if (!props.onLoad) return;
    try {
      const items = await props.onLoad({
        keyword: searchKeyword.value,
        pageNo,
        pageSize: props.pageSize,
      });
      processPageResult(items, pageNo, append);
    } catch {
      dictOptions.value = [];
    }
  }

  function processPageResult(items: DictOption[], pageNo: number, append: boolean) {
    if (!items || items.length === 0) {
      if (!append) dictOptions.value = [];
      scrollState.hasMore = false;
      return;
    }
    if (append) {
      const existValues = new Set(dictOptions.value.map((o) => String(o.value)));
      const newItems = items.filter((it) => !existValues.has(String(it.value)));
      if (newItems.length > 0) {
        dictOptions.value = [...dictOptions.value, ...newItems];
      }
    } else {
      if (unref(innerValue) && unref(dictOptions).length && !searchKeyword.value) {
        const existItem = unref(dictOptions).find((item) => String(item.value) === String(unref(innerValue)));
        if (existItem && !items.some((item) => String(item.value) === String(existItem.value))) {
          items.push(existItem);
        }
      }
      dictOptions.value = items;
    }
    scrollState.pageNo = pageNo + 1;
    scrollState.hasMore = items.length >= (props.pageSize || 10);
  }

  async function loadDictOptions() {
    if (useLoadDict.value) {
      scrollState.pageNo = 1;
      scrollState.hasMore = true;
      loading.value = true;
      fetchDictPage(1, false)
        .catch(() => { dictOptions.value = []; })
        .finally(() => { loading.value = false; ensureValueInOptions(); });
    } else if (useRemoteLoad.value) {
      scrollState.pageNo = 1;
      scrollState.hasMore = true;
      loadRemoteOptions();
    } else {
      let code = props.dictCode ?? '';
      try {
        const dictData = await initDictOptions(code);
        dictOptions.value = dictData;
      } catch (error) {
        console.error('initDictOptions error:', error);
        dictOptions.value = [];
      }
    }
  }

  async function loadRemoteOptions() {
    loading.value = true;
    searchKeyword.value = '';
    scrollState.pageNo = 1;
    scrollState.hasMore = true;
    fetchRemotePage(1, false)
      .finally(() => { loading.value = false; });
  }

  function fetchDictItemByValue(val: SelectSingleValue): Promise<DictOption | null> {
    if (val == null) return Promise.resolve(null);
    return defHttp
      .get({ url: `/sys/dict/loadDictItem/${props.dictCode}`, params: { key: val } })
      .then((res: any) => {
        if (Array.isArray(res)) return { value: val, label: res[0] };
        return null;
      })
      .catch(() => null);
  }

  function ensureValueInOptions() {
    if (!useLoadDict.value) return;
    const val = innerValue.value;
    if (val == null) return;
    const exists = dictOptions.value.some((o) => String(o.value) === String(val));
    if (exists) return;
    fetchDictItemByValue(val).then((item) => {
      if (item) dictOptions.value = [item].concat(dictOptions.value);
    });
  }

  const handleSearch = debounce((keyword: string) => {
    if (useLoadDict.value) {
      searchKeyword.value = keyword ?? '';
      scrollState.pageNo = 1;
      scrollState.hasMore = true;
      loading.value = true;
      fetchDictPage(1, false)
        .catch(() => { dictOptions.value = []; })
        .finally(() => { loading.value = false; ensureValueInOptions(); });
    } else if (useRemoteLoad.value) {
      searchKeyword.value = keyword ?? '';
      scrollState.pageNo = 1;
      scrollState.hasMore = true;
      loading.value = true;
      fetchRemotePage(1, false)
        .finally(() => { loading.value = false; });
    } else if (props.onSearch) {
      props.onSearch(keyword ?? '');
    }
  }, 300);

  function handleDropdownVisibleChange(open: boolean) {
    if (!open) {
      // 关闭时：复位缓存的滚动元素，下次打开从顶部开始
      if (lastScrollTarget) {
        lastScrollTarget.scrollTop = 0;
        lastScrollTarget = null;
      }
      return;
    }
    // 打开时：清空缓存
    lastScrollTarget = null;
    if (useLoadDict.value) {
      scrollState.pageNo = 1;
      scrollState.hasMore = true;
      searchKeyword.value = '';
      loading.value = true;
      fetchDictPage(1, false)
        .finally(() => { loading.value = false; ensureValueInOptions(); });
    } else if (useRemoteLoad.value) {
      loadRemoteOptions();
    }
  }

  function handlePopupScroll(e: Event) {
    lastScrollTarget = e.target as HTMLElement;
    if (useLoadDict.value) {
      if (scrollState.loading || !scrollState.hasMore) return;
      const target = e.target as HTMLElement;
      const { scrollTop, scrollHeight, clientHeight } = target;
      if (scrollTop + clientHeight < scrollHeight - 10) return;
      scrollState.loading = true;
      fetchDictPage(scrollState.pageNo, true)
        .finally(() => { scrollState.loading = false; })
        .catch(() => { if (scrollState.pageNo > 1) scrollState.pageNo--; });
    } else if (useRemoteLoad.value) {
      if (scrollState.loading || !scrollState.hasMore) return;
      const target = e.target as HTMLElement;
      const { scrollTop, scrollHeight, clientHeight } = target;
      if (scrollTop + clientHeight < scrollHeight - 10) return;
      scrollState.loading = true;
      fetchRemotePage(scrollState.pageNo, true)
        .finally(() => { scrollState.loading = false; });
    }
  }

  // --- 弹层挂载 & 搜索过滤 ---
  function getPopupContainer(node: HTMLElement) {
    return props.popContainer ? setPopContainer(node, props.popContainer) : node?.parentNode;
  }

  function filterOption(input: string, option: any) {
    const node = option.children?.();
    const text = (node?.[0]?.children ?? '').toString().toLowerCase();
    return text.indexOf(input.toLowerCase()) >= 0;
  }
  const filterOptionComputed = computed(() => (input: string, option: any) => {
    if (useLoadDict.value || useRemoteLoad.value) return true;
    return props.filterOption?.(input, option) ?? filterOption(input, option);
  });

  // --- 选择变更 ---
  function handleChange(value: SelectSingleValue) {
    const val = value ?? undefined;
    if (props.triggerChange) {
      emit('change', val);
    } else {
      emit('input', val);
    }
    emit('update:value', val);
  }

  // --- 初始化与监听 ---
  function syncOptionsFromProps() {
    if (props.dictCode || props.remoteLoad) {
      loadDictOptions();
    } else {
      dictOptions.value = props.options ?? [];
    }
  }
  watch(() => props.dictCode, syncOptionsFromProps);

  watch(
    () => props.value,
    (val) => {
      let parsedVal = val ?? undefined;
      let isMulti = props.mode === 'multiple' || props.mode === 'tags';
      if (isMulti && typeof parsedVal === 'string') {
        parsedVal = parsedVal.split(',').map((v) => v.trim()).filter((v) => v !== '');
      }
      innerValue.value = parsedVal;
      if (useLoadDict.value) ensureValueInOptions();
    },
    {
      immediate: true,
    }
  );

  watch(
    () => props.options,
    () => {
      if (!props.dictCode && !props.remoteLoad) dictOptions.value = props.options ?? [];
    }
  );

  syncOptionsFromProps();
</script>

<style scoped lang="less">
  .colorText {
    display: inline-block;
    height: 20px;
    line-height: 20px;
    padding: 0 6px;
    border-radius: 8px;
    background-color: red;
    color: #fff;
    font-size: 12px;
  }
</style>
