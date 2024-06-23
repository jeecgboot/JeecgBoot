import type { Ref } from 'vue';
import { ref, onBeforeUpdate } from 'vue';

export function useRefs(): [Ref<HTMLElement[]>, (index: number) => (el: HTMLElement) => void] {
  const refs = ref([]) as Ref<HTMLElement[]>;

  onBeforeUpdate(() => {
    refs.value = [];
  });

  const setRefs = (index: number) => (el: HTMLElement) => {
    refs.value[index] = el;
  };

  return [refs, setRefs];
}
