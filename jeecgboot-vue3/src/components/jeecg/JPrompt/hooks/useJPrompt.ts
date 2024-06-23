import type { JPromptProps } from '../typing';
import { render, createVNode, nextTick } from 'vue';
import { error } from '/@/utils/log';
import JPrompt from '../JPrompt.vue';

export function useJPrompt() {

  function createJPrompt(options: JPromptProps) {
    let instance = null;
    const box = document.createElement('div');
    const vm = createVNode(JPrompt, {
      // 注册
      async onRegister(ins) {
        instance = ins;
        await nextTick();
        ins.openModal(options);
      },
      // 销毁
      afterClose() {
        render(null, box);
        document.body.removeChild(box);
      },
    });
    // 挂载到 body
    render(vm, box);
    document.body.appendChild(box);

    function getInstance(): any {
      if (instance == null) {
        error('useJPrompt instance is undefined!');
      }
      return instance;
    }

    function updateModal(options: JPromptProps) {
      getInstance()?.updateModal(options);
    }

    function closeModal() {
      getInstance()?.closeModal();
    }

    function setLoading(loading) {
      getInstance()?.setLoading(loading);
    }

    return {
      closeModal,
      updateModal,
      setLoading,
    };
  }

  return {
    createJPrompt,
  };
}
