export interface DropMenu {
  onClick?: Fn;
  to?: string;
  icon?: string;
  event: string | number;
  text: string;
  disabled?: boolean;
  // 是否隐藏
  hide?: boolean;
  divider?: boolean;
}
