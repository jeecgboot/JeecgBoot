export const useCodeHinting = (CodeMirror, keywords, language) => {
  const currentKeywords: any = [...keywords];
  const codeHintingMount = (coder) => {
    if (keywords.length) {
      coder.setOption('mode', language);
      setTimeout(() => {
        coder!.on('cursorActivity', function () {
          coder?.showHint({
            completeSingle: false,
            // container: containerRef.value
          });
        });
      }, 1e3);
    }
  };

  const codeHintingRegistry = () => {
    // 自定义关键词(.的上一级)
    const customKeywords: string[] = [];

    currentKeywords.forEach((item) => {
      if (item.superiors) {
        customKeywords.push(item.superiors);
      }
    });
    const funcsHint = (cm, callback) => {
      // 获取光标位置
      const cur = cm.getCursor();
      // 获取当前单词的信息
      const token = cm.getTokenAt(cur);
      const start = token.start;
      const end = cur.ch;
      const str = token.string;
      let recordKeyword = null;
      console.log('光标位置：', cur, '单词信息：', token, `start:${start},end:${end},str:${str}`);

      if (str.length) {
        if (str === '.') {
          // 查找.前面是否有定义的关键词
          const curLineCode = cm.getLine(cur.line);
          for (let i = 0, len = customKeywords.length; i < len; i++) {
            const k = curLineCode.substring(-1, customKeywords[i].length);
            if (customKeywords.includes(k)) {
              recordKeyword = k;
              break;
            }
          }
        }
        const findIdx = (a, b) => a.toLowerCase().indexOf(b.toLowerCase());
        let list = currentKeywords.filter((item) => {
          if (recordKeyword) {
            // 查特定对象下的属性or方法
            return item.superiors === recordKeyword;
          } else {
            // 查全局属性或者方法
            return item.superiors == undefined;
          }
        });
        if (str === '.') {
          if (recordKeyword == null) {
            list = [];
          }
        } else {
          list = list
            .filter((item) => {
              const { text } = item;
              const index = findIdx(text, str);
              let result = text.startsWith('.') ? index === 1 : index === 0;
              return result;
            })
            .sort((a, b) => {
              if (findIdx(a.text, str) < findIdx(b.text, str)) {
                return -1;
              } else {
                return 1;
              }
            });
        }

        if (list.length === 1) {
          // 只有一个时可能是自己输入，输到最后需要去掉提示。
          const item = list[0];
          if (item.text === str || item.text.substring(1) === str) {
            list = [];
          }
        }
        if (list.length) {
          // 当str不是点时去掉点
          if (str != '.') {
            list = list.map((item) => {
              if (item.text.indexOf('.') === 0) {
                return { ...item, text: item.text.substring(1) };
              }
              return item;
            });
          }
          callback({
            list: list,
            from: CodeMirror.Pos(cur.line, start),
            to: CodeMirror.Pos(cur.line, end),
          });
          // update-begin--author:liaozhiyang---date:20240429---for：【QQYUN-8865】js增强加上鼠标移入提示
          const item = currentKeywords[0];
          if (item?.desc) {
            setTimeout(() => {
              const elem: HTMLUListElement = document.querySelector('.CodeMirror-hints')!;
              if (elem) {
                const childElems = elem.children;
                Array.from(childElems).forEach((item) => {
                  const displayText = item.textContent;
                  const findItem = currentKeywords.find((item) => item.displayText === displayText);
                  if (findItem) {
                    item.setAttribute('title', findItem.desc);
                  }
                });
              }
            }, 0);
          }
          // update-end--author:liaozhiyang---date:20240429---for：【QQYUN-8865】js增强加上鼠标移入提示
        } else {
        }
      }
    };
    funcsHint.async = true;
    funcsHint.supportsSelection = true;
    // 自动补全
    keywords.length && CodeMirror.registerHelper('hint', language, funcsHint);
  };
  return {
    codeHintingRegistry,
    codeHintingMount,
  };
};
