import { nextTick } from 'vue';
import $printJS, { Configuration } from 'print-js';
import Print from 'vue-print-nb-jeecg/src/printarea';

/**
 * 调用 printJS，如果type = html，就走 printNB 的方法
 */
export function printJS(configuration: Configuration) {
  if (configuration?.type === 'html') {
    printNb(configuration.printable);
  } else {
    return $printJS(configuration);
  }
}

/** 调用 printNB 打印 */
export function printNb(domId) {
  if (domId) {
    localPrint(domId);
  } else {
    window.print();
  }
}

let closeBtn = true;

function localPrint(domId) {
  if (typeof domId === 'string' && !domId.startsWith('#')) {
    domId = '#' + domId;
  }
  nextTick(() => {
    if (closeBtn) {
      closeBtn = false;
      new Print({
        el: domId,
        endCallback() {
          closeBtn = true;
        },
      });
    }
  });
}
