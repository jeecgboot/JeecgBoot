import { computed, ComponentInternalInstance, unref } from 'vue';
import type { CSSProperties } from 'vue';

export function useMenuItem(instance: ComponentInternalInstance | null) {
  const getParentMenu = computed(() => {
    return findParentMenu(['Menu', 'SubMenu']);
  });

  const getParentRootMenu = computed(() => {
    return findParentMenu(['Menu']);
  });

  const getParentSubMenu = computed(() => {
    return findParentMenu(['SubMenu']);
  });

  const getItemStyle = computed((): CSSProperties => {
    let parent = instance?.parent;
    if (!parent) return {};
    const indentSize = (unref(getParentRootMenu)?.props.indentSize as number) ?? 20;
    let padding = indentSize;

    if (unref(getParentRootMenu)?.props.collapse) {
      padding = indentSize;
    } else {
      while (parent && parent.type.name !== 'Menu') {
        if (parent.type.name === 'SubMenu') {
          padding += indentSize;
        }
        parent = parent.parent;
      }
    }
    return { paddingLeft: padding + 'px' };
  });

  function findParentMenu(name: string[]) {
    let parent = instance?.parent;
    if (!parent) return null;
    while (parent && name.indexOf(parent.type.name!) === -1) {
      parent = parent.parent;
    }
    return parent;
  }

  function getParentList() {
    let parent = instance;
    if (!parent)
      return {
        uidList: [],
        list: [],
      };
    const ret: any[] = [];
    while (parent && parent.type.name !== 'Menu') {
      if (parent.type.name === 'SubMenu') {
        ret.push(parent);
      }
      parent = parent.parent;
    }
    return {
      uidList: ret.map((item) => item.uid),
      list: ret,
    };
  }

  function getParentInstance(instance: ComponentInternalInstance, name = 'SubMenu') {
    let parent = instance.parent;
    while (parent) {
      if (parent.type.name !== name) {
        return parent;
      }
      parent = parent.parent;
    }
    return parent;
  }

  return {
    getParentMenu,
    getParentInstance,
    getParentRootMenu,
    getParentList,
    getParentSubMenu,
    getItemStyle,
  };
}
