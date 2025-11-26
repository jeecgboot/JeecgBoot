import type { Ref } from 'vue';
import type { BasicTableProps, TableActionType } from '../types/table';
import { provide, inject, ComputedRef } from 'vue';

// 为每个表格实例创建唯一的 Symbol key，避免父子组件或同级组件间的 context 冲突
// Vue 的 provide/inject 是按组件树传递的，使用唯一 Symbol 可以确保每个 BasicTable 实例独立
let tableIdCounter = 0;

type Instance = TableActionType & {
  wrapRef: Ref<Nullable<HTMLElement>>;
  getBindValues: ComputedRef<Recordable>;
};

type RetInstance = Omit<Instance, 'getBindValues'> & {
  getBindValues: ComputedRef<BasicTableProps>;
};

export function createTableContext(instance: Instance) {
  // 每次创建 context 时都生成新的唯一 Symbol
  const key = Symbol(`basic-table-${++tableIdCounter}`);
  provide(key, instance);
  // 同时提供一个内部标记，让子组件能获取到这个 key
  provide('__BASIC_TABLE_CONTEXT_KEY__', key);
}

export function useTableContext(): RetInstance {
  // 从最近的父组件获取 context key
  const key = inject<symbol>('__BASIC_TABLE_CONTEXT_KEY__');
  if (!key) {
    throw new Error('useTableContext must be used after createTableContext');
  }
  return inject(key) as RetInstance;
}
