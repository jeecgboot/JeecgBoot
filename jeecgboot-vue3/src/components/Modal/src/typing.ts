import type { ButtonProps } from 'ant-design-vue/lib/button/buttonTypes';
import type { CSSProperties, VNodeChild, ComputedRef } from 'vue';
/**
 * @description: 弹窗对外暴露的方法
 */
export interface ModalMethods {
  setModalProps: (props: Partial<ModalProps>) => void;
  emitVisible?: (visible: boolean, uid: number) => void;
  redoModalHeight?: () => void;
}

export type RegisterFn = (modalMethods: ModalMethods, uuid?: string) => void;

export interface ReturnMethods extends ModalMethods {
  openModal: <T = any>(props?: boolean, data?: T, openOnSet?: boolean) => void;
  closeModal: () => void;
  getVisible?: ComputedRef<boolean>;
  getOpen?: ComputedRef<boolean>;
}

export type UseModalReturnType = [RegisterFn, ReturnMethods];

export interface ReturnInnerMethods extends ModalMethods {
  closeModal: () => void;
  changeLoading: (loading: boolean) => void;
  changeOkLoading: (loading: boolean) => void;
  getVisible?: ComputedRef<boolean>;
  getOpen?: ComputedRef<boolean>;
  redoModalHeight: () => void;
}

export type UseModalInnerReturnType = [RegisterFn, ReturnInnerMethods];

export interface ModalProps {
  minHeight?: number;
  height?: number;
  // 启用wrapper后 底部可以适当增加高度
  wrapperFooterOffset?: number;
  draggable?: boolean;
  scrollTop?: boolean;

  // 是否可以进行全屏
  canFullscreen?: boolean;
  defaultFullscreen?: boolean;
  visible?: boolean;
  open?: boolean;
  // 温馨提醒信息
  helpMessage: string | string[];

  // 是否使用modalWrapper
  useWrapper: boolean;

  loading: boolean;
  loadingTip?: string;

  wrapperProps: Omit<ModalWrapperProps, 'loading'>;

  showOkBtn: boolean;
  showCancelBtn: boolean;
  closeFunc: () => Promise<any>;

  /**
   * Specify a function that will be called when modal is closed completely.
   * @type Function
   */
  afterClose?: () => any;

  /**
   * Body style for modal body element. Such as height, padding etc.
   * @default {}
   * @type object
   */
  bodyStyle?: CSSProperties;

  /**
   * Text of the Cancel button
   * @default 'cancel'
   * @type string
   */
  cancelText?: string;

  /**
   * Centered Modal
   * @default false
   * @type boolean
   */
  centered?: boolean;

  /**
   * Whether a close (x) button is visible on top right of the modal dialog or not
   * @default true
   * @type boolean
   */
  closable?: boolean;
  /**
   * Whether a close (x) button is visible on top right of the modal dialog or not
   */
  closeIcon?: VNodeChild | JSX.Element;

  /**
   * Whether to apply loading visual effect for OK button or not
   * @default false
   * @type boolean
   */
  confirmLoading?: boolean;

  /**
   * Whether to unmount child components on onClose
   * @default false
   * @type boolean
   */
  destroyOnClose?: boolean;

  /**
   * Footer content, set as :footer="null" when you don't need default buttons
   * @default OK and Cancel buttons
   * @type any (string | slot)
   */
  footer?: VNodeChild | JSX.Element;

  /**
   * Return the mount node for Modal
   * @default () => document.body
   * @type Function
   */
  getContainer?: (instance: any) => HTMLElement;

  /**
   * Whether show mask or not.
   * @default true
   * @type boolean
   */
  mask?: boolean;

  /**
   * Whether to close the modal dialog when the mask (area outside the modal) is clicked
   * @default true
   * @type boolean
   */
  maskClosable?: boolean;

  /**
   * Style for modal's mask element.
   * @default {}
   * @type object
   */
  maskStyle?: CSSProperties;

  /**
   * Text of the OK button
   * @default 'OK'
   * @type string
   */
  okText?: string;

  /**
   * Button type of the OK button
   * @default 'primary'
   * @type string
   */
  okType?: 'primary' | 'danger' | 'dashed' | 'ghost' | 'default';

  /**
   * The ok button props, follow jsx rules
   * @type object
   */
  okButtonProps?: ButtonProps;

  /**
   * The cancel button props, follow jsx rules
   * @type object
   */
  cancelButtonProps?: ButtonProps;

  /**
   * The modal dialog's title
   * @type any (string | slot)
   */
  title?: VNodeChild | JSX.Element;

  /**
   * Width of the modal dialog
   * @default 520
   * @type string | number
   */
  width?: string | number;

  /**
   * The class name of the container of the modal dialog
   * @type string
   */
  wrapClassName?: string;

  /**
   * The z-index of the Modal
   * @default 1000
   * @type number
   */
  zIndex?: number;

  enableComment?: boolean;
}

export interface ModalWrapperProps {
  footerOffset?: number;
  loading: boolean;
  modalHeaderHeight: number;
  modalFooterHeight: number;
  minHeight: number;
  height: number;
  visible: boolean;
  fullScreen: boolean;
  useWrapper: boolean;
}
