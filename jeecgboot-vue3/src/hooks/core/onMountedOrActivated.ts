import { nextTick, onMounted, onActivated } from 'vue';

type HookArgs = {
  type: 'mounted' | 'activated';
}

export function onMountedOrActivated(hook: Fn<HookArgs, any>) {
  let mounted: boolean;

  onMounted(() => {
    hook({type: 'mounted'});
    nextTick(() => {
      mounted = true;
    });
  });

  onActivated(() => {
    if (mounted) {
      hook({type: 'activated'});
    }
  });
}
