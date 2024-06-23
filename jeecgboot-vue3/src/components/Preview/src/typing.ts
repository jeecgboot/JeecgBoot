export interface Options {
  show?: boolean;
  imageList: string[];
  index?: number;
  scaleStep?: number;
  defaultWidth?: number;
  maskClosable?: boolean;
  rememberState?: boolean;
  onImgLoad?: ({ index: number, url: string, dom: HTMLImageElement }) => void;
  onImgError?: ({ index: number, url: string, dom: HTMLImageElement }) => void;
}

export interface Props {
  show: boolean;
  instance: Props;
  imageList: string[];
  index: number;
  scaleStep: number;
  defaultWidth: number;
  maskClosable: boolean;
  rememberState: boolean;
}

export interface PreviewActions {
  resume: () => void;
  close: () => void;
  prev: () => void;
  next: () => void;
  setScale: (scale: number) => void;
  setRotate: (rotate: number) => void;
}

export interface ImageProps {
  alt?: string;
  fallback?: string;
  src: string;
  width: string | number;
  height?: string | number;
  placeholder?: string | boolean;
  preview?:
    | boolean
    | {
        visible?: boolean;
        onVisibleChange?: (visible: boolean, prevVisible: boolean) => void;
        getContainer: string | HTMLElement | (() => HTMLElement);
      };
}

export type ImageItem = string | ImageProps;
