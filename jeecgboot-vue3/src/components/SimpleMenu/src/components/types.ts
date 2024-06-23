import { Ref } from 'vue';

export interface Props {
  theme: string;
  activeName?: string | number | undefined;
  openNames: string[];
  accordion: boolean;
  width: string;
  collapsedWidth: string;
  indentSize: number;
  collapse: boolean;
  activeSubMenuNames: (string | number)[];
}

export interface SubMenuProvider {
  addSubMenu: (name: string | number, update?: boolean) => void;
  removeSubMenu: (name: string | number, update?: boolean) => void;
  removeAll: () => void;
  sliceIndex: (index: number) => void;
  isRemoveAllPopup: Ref<boolean>;
  getOpenNames: () => (string | number)[];
  handleMouseleave?: Fn;
  level: number;
  props: Props;
}
