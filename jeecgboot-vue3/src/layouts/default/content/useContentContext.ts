import type { InjectionKey, ComputedRef } from 'vue';
import { createContext, useContext } from '/@/hooks/core/useContext';

export interface ContentContextProps {
  contentHeight: ComputedRef<number>;
  setPageHeight: (height: number) => Promise<void>;
}

const key: InjectionKey<ContentContextProps> = Symbol();

export function createContentContext(context: ContentContextProps) {
  return createContext<ContentContextProps>(context, key, { native: true });
}

export function useContentContext() {
  return useContext<ContentContextProps>(key);
}
