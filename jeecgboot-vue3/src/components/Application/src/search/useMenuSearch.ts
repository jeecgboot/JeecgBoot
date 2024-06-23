import type { Menu } from '/@/router/types';
import { ref, onBeforeMount, unref, Ref, nextTick } from 'vue';
import { getMenus } from '/@/router/menus';
import { cloneDeep } from 'lodash-es';
import { filter, forEach } from '/@/utils/helper/treeHelper';
import { useGo } from '/@/hooks/web/usePage';
import { useScrollTo } from '/@/hooks/event/useScrollTo';
import { onKeyStroke, useDebounceFn } from '@vueuse/core';
import { useI18n } from '/@/hooks/web/useI18n';
import { URL_HASH_TAB } from '/@/utils';

export interface SearchResult {
  name: string;
  path: string;
  icon?: string;
  internalOrExternal: boolean;
}

// Translate special characters
function transform(c: string) {
  const code: string[] = ['$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'];
  return code.includes(c) ? `\\${c}` : c;
}

function createSearchReg(key: string) {
  const keys = [...key].map((item) => transform(item));
  const str = ['', ...keys, ''].join('.*');
  return new RegExp(str, 'i');
}

export function useMenuSearch(refs: Ref<HTMLElement[]>, scrollWrap: Ref<ElRef>, emit: EmitType) {
  const searchResult = ref<SearchResult[]>([]);
  const keyword = ref('');
  const activeIndex = ref(-1);

  let menuList: Menu[] = [];

  const { t } = useI18n();
  const go = useGo();
  const handleSearch = useDebounceFn(search, 200);

  onBeforeMount(async () => {
    const list = await getMenus();
    menuList = cloneDeep(list);
    forEach(menuList, (item) => {
      item.name = t(item.name);
    });
  });

  function search(e: ChangeEvent) {
    e?.stopPropagation();
    const key = e.target.value;
    keyword.value = key.trim();
    if (!key) {
      searchResult.value = [];
      return;
    }
    const reg = createSearchReg(unref(keyword));
    const filterMenu = filter(menuList, (item) => {
      // 【issues/33】包含子菜单时，不添加到搜索队列
      if (Array.isArray(item.children)) {
        return false;
      }
      return reg.test(item.name) && !item.hideMenu;
    });
    searchResult.value = handlerSearchResult(filterMenu, reg);
    activeIndex.value = 0;
  }

  function handlerSearchResult(filterMenu: Menu[], reg: RegExp, parent?: Menu) {
    const ret: SearchResult[] = [];
    filterMenu.forEach((item) => {
      const { name, path, icon, children, hideMenu, meta, internalOrExternal } = item;
      if (!hideMenu && reg.test(name) && (!children?.length || meta?.hideChildrenInMenu)) {
        ret.push({
          name: parent?.name ? `${parent.name} > ${name}` : name,
          path,
          icon,
          internalOrExternal
        });
      }
      if (!meta?.hideChildrenInMenu && Array.isArray(children) && children.length) {
        ret.push(...handlerSearchResult(children, reg, item));
      }
    });
    return ret;
  }

  // Activate when the mouse moves to a certain line
  function handleMouseenter(e: any) {
    const index = e.target.dataset.index;
    activeIndex.value = Number(index);
  }

  // Arrow key up
  function handleUp() {
    if (!searchResult.value.length) return;
    activeIndex.value--;
    if (activeIndex.value < 0) {
      activeIndex.value = searchResult.value.length - 1;
    }
    handleScroll();
  }

  // Arrow key down
  function handleDown() {
    if (!searchResult.value.length) return;
    activeIndex.value++;
    if (activeIndex.value > searchResult.value.length - 1) {
      activeIndex.value = 0;
    }
    handleScroll();
  }

  // When the keyboard up and down keys move to an invisible place
  // the scroll bar needs to scroll automatically
  function handleScroll() {
    const refList = unref(refs);
    if (!refList || !Array.isArray(refList) || refList.length === 0 || !unref(scrollWrap)) {
      return;
    }

    const index = unref(activeIndex);
    const currentRef = refList[index];
    if (!currentRef) {
      return;
    }
    const wrapEl = unref(scrollWrap);
    if (!wrapEl) {
      return;
    }
    const scrollHeight = currentRef.offsetTop + currentRef.offsetHeight;
    const wrapHeight = wrapEl.offsetHeight;
    const { start } = useScrollTo({
      el: wrapEl,
      duration: 100,
      to: scrollHeight - wrapHeight,
    });
    start();
  }

  // enter keyboard event
  async function handleEnter() {
    if (!searchResult.value.length) {
      return;
    }
    const result = unref(searchResult);
    const index = unref(activeIndex);
    if (result.length === 0 || index < 0) {
      return;
    }
    const to = result[index];
    handleClose();
    await nextTick();

    // update-begin--author:liaozhiyang---date:20230803---for：【QQYUN-8369】搜索区分大小写，外部链接新页打开
    if (to.internalOrExternal) {
      // update-begin--author:liaozhiyang---date:20240402---for:【QQYUN-8773】配置外部网址在顶部菜单模式和搜索打不开
      const path = to.path.replace(URL_HASH_TAB, '#');
      window.open(path, '_blank');
      // update-end--author:liaozhiyang---date:20240402---for:【QQYUN-8773】配置外部网址在顶部菜单模式和搜索打不开
    } else {
      go(to.path);
    }
    // update-end--author:liaozhiyang---date:20230803---for：【QQYUN-8369】搜索区分大小写，外部链接新页打开
  }

  // close search modal
  function handleClose() {
    searchResult.value = [];
    emit('close');
  }

  // enter search
  onKeyStroke('Enter', handleEnter);
  // Monitor keyboard arrow keys
  onKeyStroke('ArrowUp', handleUp);
  onKeyStroke('ArrowDown', handleDown);
  // esc close
  onKeyStroke('Escape', handleClose);

  return { handleSearch, searchResult, keyword, activeIndex, handleMouseenter, handleEnter };
}
