export interface BarMapItem {
  offset: string;
  scroll: string;
  scrollSize: string;
  size: string;
  key: string;
  axis: string;
  client: string;
  direction: string;
}
export interface BarMap {
  vertical: BarMapItem;
  horizontal: BarMapItem;
}

export interface ScrollbarType {
  wrap: ElRef;
}
