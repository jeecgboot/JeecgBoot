import type { VNode, FunctionalComponent } from 'vue';

import { h } from 'vue';
import { isString } from '@vue/shared';
import { Icon } from '/@/components/Icon';

export const TreeIcon: FunctionalComponent = ({ icon }: { icon: VNode | string }) => {
  if (!icon) return null;
  if (isString(icon)) {
    return h(Icon, { icon, class: 'mr-1' });
  }
  return Icon;
};
