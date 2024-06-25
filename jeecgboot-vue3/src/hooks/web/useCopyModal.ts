import { isRef, unref, watch, Ref, ComputedRef } from 'vue';
import Clipboard from 'clipboard';
import { ModalOptionsEx, useMessage } from '/@/hooks/web/useMessage';

/** 带复制按钮的弹窗 */
interface IOptions extends ModalOptionsEx {
  // 要复制的文本，可以是一个 ref 对象，动态更新
  copyText: string | Ref<string> | ComputedRef<string>;
}

const COPY_CLASS = 'copy-this-text';
const CLIPBOARD_TEXT = 'data-clipboard-text';

export function useCopyModal() {
  return { createCopyModal };
}

const { createMessage, createConfirm } = useMessage();

/** 创建复制弹窗 */
function createCopyModal(options: Partial<IOptions>) {
  let modal = createConfirm({
    ...options,
    iconType: options.iconType ?? 'info',
    width: options.width ?? 500,
    title: options.title ?? '复制',
    maskClosable: options.maskClosable ?? true,
    okText: options.okText ?? '复制',
    okButtonProps: {
      ...options.okButtonProps,
      class: COPY_CLASS,
      [CLIPBOARD_TEXT]: unref(options.copyText),
    } as any,
    onOk() {
      return new Promise((resolve: any) => {
        const clipboard = new Clipboard('.' + COPY_CLASS);
        clipboard.on('success', () => {
          clipboard.destroy();
          createMessage.success('复制成功');
          resolve();
        });
        clipboard.on('error', () => {
          createMessage.error('该浏览器不支持自动复制');
          clipboard.destroy();
          resolve();
        });
      });
    },
  });

  // 动态更新 copyText
  if (isRef(options.copyText)) {
    watch(options.copyText, (copyText) => {
      modal.update({
        okButtonProps: {
          ...options.okButtonProps,
          class: COPY_CLASS,
          [CLIPBOARD_TEXT]: copyText,
        } as any,
      });
    });
  }
  return modal;
}
