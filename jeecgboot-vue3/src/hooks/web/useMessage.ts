import type { ModalFunc, ModalFuncProps } from 'ant-design-vue/lib/modal/Modal';

import { Modal, message as Message, notification } from 'ant-design-vue';
import { InfoCircleFilled, CheckCircleFilled, CloseCircleFilled } from '@ant-design/icons-vue';

import { NotificationArgsProps, ConfigProps } from 'ant-design-vue/lib/notification';
import { useI18n } from './useI18n';
import { isString } from '/@/utils/is';
import { h } from 'vue';

export interface NotifyApi {
  info(config: NotificationArgsProps): void;
  success(config: NotificationArgsProps): void;
  error(config: NotificationArgsProps): void;
  warn(config: NotificationArgsProps): void;
  warning(config: NotificationArgsProps): void;
  open(args: NotificationArgsProps): void;
  close(key: String): void;
  config(options: ConfigProps): void;
  destroy(): void;
}

export declare type NotificationPlacement = 'topLeft' | 'topRight' | 'bottomLeft' | 'bottomRight';
export declare type IconType = 'success' | 'info' | 'error' | 'warning';
export interface ModalOptionsEx extends Omit<ModalFuncProps, 'iconType'> {
  iconType: 'warning' | 'success' | 'error' | 'info';
}
export type ModalOptionsPartial = Partial<ModalOptionsEx> & Pick<ModalOptionsEx, 'content'>;

interface ConfirmOptions {
  info: ModalFunc;
  success: ModalFunc;
  error: ModalFunc;
  warn: ModalFunc;
  warning: ModalFunc;
}

function getIcon(iconType: string) {
  try {
    if (iconType === 'warning') {
      return  h(InfoCircleFilled,{"class":"modal-icon-warning"})
    } else if (iconType === 'success') {
      return h(CheckCircleFilled,{"class": "modal-icon-success"});
    } else if (iconType === 'info') {
      return h(InfoCircleFilled,{"class": "modal-icon-info"});
    } else {
      return h(CloseCircleFilled,{"class":"modal-icon-error"});
    }
  } catch (e) {
    console.log(e);
  }
}

function renderContent({ content }: Pick<ModalOptionsEx, 'content'>) {
  try {
    if (isString(content)) {
      return h('div', h('div', {'innerHTML':content as string}));
    } else {
      return content;
    }
  } catch (e) {
    console.log(e);
    return content;
  }
}

/**
 * @description: Create confirmation box
 */
function createConfirm(options: ModalOptionsEx): ReturnType<ModalFunc> {
  const iconType = options.iconType || 'warning';
  Reflect.deleteProperty(options, 'iconType');
  const opt: ModalFuncProps = {
    centered: true,
    icon: getIcon(iconType),
    ...options,
    content: renderContent(options),
  };
  return Modal.confirm(opt);
}

const getBaseOptions = () => {
  const { t } = useI18n();
  return {
    okText: t('common.okText'),
    centered: true,
  };
};

function createModalOptions(options: ModalOptionsPartial, icon: string): ModalOptionsPartial {
  //update-begin-author:taoyan date:2023-1-10 for: 可以自定义图标 
  let titleIcon:any = ''
  if(options.icon){
    titleIcon = options.icon;
  }else{
    titleIcon = getIcon(icon)
  }
  //update-end-author:taoyan date:2023-1-10 for: 可以自定义图标 
  return {
    ...getBaseOptions(),
    ...options,
    content: renderContent(options),
    icon: titleIcon
  };
}

function createSuccessModal(options: ModalOptionsPartial) {
  return Modal.success(createModalOptions(options, 'success'));
}

function createErrorModal(options: ModalOptionsPartial) {
  return Modal.error(createModalOptions(options, 'close'));
}

function createInfoModal(options: ModalOptionsPartial) {
  return Modal.info(createModalOptions(options, 'info'));
}

function createWarningModal(options: ModalOptionsPartial) {
  return Modal.warning(createModalOptions(options, 'warning'));
}

interface MOE extends Omit<ModalOptionsEx, 'iconType'> {
  iconType?: ModalOptionsEx['iconType'];
}

// 提示框，无需传入iconType，默认为warning
function createConfirmSync(options: MOE) {
  return new Promise((resolve) => {
    createConfirm({
      iconType: 'warning',
      ...options,
      onOk: () => resolve(true),
      onCancel: () => resolve(false),
    });
  });
}

notification.config({
  placement: 'topRight',
  duration: 3,
});

/**
 * @description: message
 */
export function useMessage() {
  return {
    createMessage: Message,
    notification: notification as NotifyApi,
    createConfirm: createConfirm,
    createConfirmSync,
    createSuccessModal,
    createErrorModal,
    createInfoModal,
    createWarningModal,
  };
}
