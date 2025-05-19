// iframe-widget.js
(function () {
  let widgetInstance = null;
  const defaultConfig = {
    // 支持'top-left'左上, 'top-right'右上, 'bottom-left'左下, 'bottom-right'右下
    iconPosition: 'bottom-right',
    //图标的大小
    iconSize: '30px',
    //图标的颜色
    iconColor: '#155eef',
    //必填不允许修改
    appId: '',
    //聊天弹窗的宽度
    chatWidth: '800px',
    //聊天弹窗的高度
    chatHeight: '700px',
  };

  /**
   * 创建ai图标
   * @param config
   */
  function createAiChat(config) {
    // 单例模式，确保只存在一个实例
    if (widgetInstance) {
      return;
    }

    // 合并配置
    const finalConfig = { ...defaultConfig, ...config };

    if (!finalConfig.appId) {
      console.error('appId为空！');
      return;
    }
    // 创建容器
    const container = document.createElement('div');
    container.style.cssText = `
            position: fixed;
            z-index: 998;
            ${getPositionStyles(finalConfig.iconPosition)}
            cursor: pointer;
        `;
    // 创建图标
    const icon = document.createElement('div');
    icon.style.cssText = `
            width: ${finalConfig.iconSize};
            height: ${finalConfig.iconSize};
            background-color: ${finalConfig.iconColor};
            border-radius: 50%;
            box-shadow: rgba(0, 0, 0, 0.2) 0 4px 8px 0;
            padding: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        `;
    icon.innerHTML =
      '<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 1024 1024" class="iconify iconify--ant-design"><path fill="currentColor" d="M573 421c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40m-280 0c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40"></path><path fill="currentColor" d="M894 345c-48.1-66-115.3-110.1-189-130v.1c-17.1-19-36.4-36.5-58-52.1c-163.7-119-393.5-82.7-513 81c-96.3 133-92.2 311.9 6 439l.8 132.6c0 3.2.5 6.4 1.5 9.4c5.3 16.9 23.3 26.2 40.1 20.9L309 806c33.5 11.9 68.1 18.7 102.5 20.6l-.5.4c89.1 64.9 205.9 84.4 313 49l127.1 41.4c3.2 1 6.5 1.6 9.9 1.6c17.7 0 32-14.3 32-32V753c88.1-119.6 90.4-284.9 1-408M323 735l-12-5l-99 31l-1-104l-8-9c-84.6-103.2-90.2-251.9-11-361c96.4-132.2 281.2-161.4 413-66c132.2 96.1 161.5 280.6 66 412c-80.1 109.9-223.5 150.5-348 102m505-17l-8 10l1 104l-98-33l-12 5c-56 20.8-115.7 22.5-171 7l-.2-.1C613.7 788.2 680.7 742.2 729 676c76.4-105.3 88.8-237.6 44.4-350.4l.6.4c23 16.5 44.1 37.1 62 62c72.6 99.6 68.5 235.2-8 330"></path><path fill="currentColor" d="M433 421c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40"></path></svg>';

    // 创建iframe容器
    const iframeContainer = document.createElement('div');
    iframeContainer.style.cssText = `
            position: absolute;
            right: 10px;
            bottom: 10px;
            width: ${finalConfig.chatWidth} !important;
            height: ${finalConfig.chatHeight} !important;
            background: white;
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0,0,0,0.2);
            display: none;
            z-index: 10000;
        `;

    // 创建iframe
    const iframe = document.createElement('iframe');
    iframe.style.cssText = `
            width: 100%;
            height: 100%;
            border: none;
            border-radius: 8px;
        `;

    iframe.id = 'ai-app-chat-document';
    iframe.src = getIframeSrc(finalConfig) + '/ai/app/chat/' + finalConfig.appId;
    // 创建关闭按钮
    const closeBtn = document.createElement('div');
    closeBtn.innerHTML =
      '<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1em" height="1em" viewBox="0 0 1024 1024" class="iconify iconify--ant-design"><path fill="currentColor" fill-rule="evenodd" d="M799.855 166.312c.023.007.043.018.084.059l57.69 57.69c.041.041.052.06.059.084a.1.1 0 0 1 0 .069c-.007.023-.018.042-.059.083L569.926 512l287.703 287.703c.041.04.052.06.059.083a.12.12 0 0 1 0 .07c-.007.022-.018.042-.059.083l-57.69 57.69c-.041.041-.06.052-.084.059a.1.1 0 0 1-.069 0c-.023-.007-.042-.018-.083-.059L512 569.926L224.297 857.629c-.04.041-.06.052-.083.059a.12.12 0 0 1-.07 0c-.022-.007-.042-.018-.083-.059l-57.69-57.69c-.041-.041-.052-.06-.059-.084a.1.1 0 0 1 0-.069c.007-.023.018-.042.059-.083L454.073 512L166.371 224.297c-.041-.04-.052-.06-.059-.083a.12.12 0 0 1 0-.07c.007-.022.018-.042.059-.083l57.69-57.69c.041-.041.06-.052.084-.059a.1.1 0 0 1 .069 0c.023.007.042.018.083.059L512 454.073l287.703-287.702c.04-.041.06-.052.083-.059a.12.12 0 0 1 .07 0Z"></path></svg>';
    closeBtn.style.cssText = `
            position: absolute;
            right: -3px;
            top: -10px;
            cursor: pointer;
            background: white;
            width: 25px;
            height: 25px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        `;

    // 组装元素
    iframeContainer.appendChild(closeBtn);
    iframeContainer.appendChild(iframe);
    document.body.appendChild(iframeContainer);
    container.appendChild(icon);
    document.body.appendChild(container);

    // 事件监听
    icon.addEventListener('click', () => {
      iframeContainer.style.display = 'block';
    });

    closeBtn.addEventListener('click', () => {
      iframeContainer.style.display = 'none';
    });

    // 保存实例引用
    widgetInstance = {
      remove: () => {
        container.remove();
        iframeContainer.remove();
      },
    };
  }

  /**
   * 获取位置信息
   *
   * @param position
   * @returns {*|string}
   */
  function getPositionStyles(position) {
    const positions = {
      'top-left': 'top: 20px; left: 20px;',
      'top-right': 'top: 20px; right: 20px;',
      'bottom-left': 'bottom: 20px; left: 20px;',
      'bottom-right': 'bottom: 20px; right: 20px;',
    };
    return positions[position] || positions['bottom-right'];
  }

  /**
   * 获取src地址
   */
  function getIframeSrc(finalConfig) {
    const specificScript = document.getElementById("e7e007dd52f67fe36365eff636bbffbd");
    if (specificScript) {
      return specificScript.src.substring(0, specificScript.src.indexOf('/', specificScript.src.indexOf('://') + 3));
    }
  }

  // 暴露全局方法
  window.createAiChat = createAiChat;
})();
