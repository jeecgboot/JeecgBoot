import { watch } from 'vue';
import { isFunction, isPromise, isArray } from '/@/utils/is';
import { JVxeColumn, JVxeDataProps, JVxeTableProps, JVxeLinkageConfig } from '../types';

/**
 * 多级联动
 */
export function useLinkage(props: JVxeTableProps, data: JVxeDataProps, methods) {
  // 整理多级联动配置
  watch(
    () => props.linkageConfig,
    (linkageConfig: JVxeLinkageConfig[]) => {
      data.innerLinkageConfig.clear();
      if (isArray(linkageConfig) && linkageConfig.length > 0) {
        linkageConfig.forEach((config) => {
          let keys = getLinkageKeys(config.key, []);
          // 多个key共享一个，引用地址
          let configItem = {
            ...config,
            keys,
            optionsMap: new Map(),
          };
          keys.forEach((k) => data.innerLinkageConfig.set(k, configItem));
        });
      }
    },
    { immediate: true }
  );

  // 获取联动的key顺序
  function getLinkageKeys(key: string, keys: string[]): string[] {
    let col = props.columns?.find((col: JVxeColumn) => col.key === key) as JVxeColumn;
    if (col) {
      keys.push(col.key);
      // 寻找下级
      if (col.linkageKey) {
        return getLinkageKeys(col.linkageKey, keys);
      }
    }
    return keys;
  }

  // 处理联动回显数据
  function handleLinkageBackData(row) {
    if (data.innerLinkageConfig.size > 0) {
      for (let configItem of data.innerLinkageConfig.values()) {
        autoSetLinkageOptionsByData(row, '', configItem, 0);
      }
    }
  }

  /** 【多级联动】获取同级联动下拉选项 */
  function getLinkageOptionsSibling(row, col, config, request) {
    // 如果当前列不是顶级列
    let key = '';
    if (col.key !== config.key) {
      // 就找出联动上级列
      let idx = config.keys.findIndex((k) => col.key === k);
      let parentKey = config.keys[idx - 1];
      key = row[parentKey];
      // 如果联动上级列没有选择数据，就直接返回空数组
      if (key === '' || key == null) {
        return [];
      }
    } else {
      key = 'root';
    }
    let options = config.optionsMap.get(key);
    if (!Array.isArray(options)) {
      if (request) {
        let parent = key === 'root' ? '' : key;
        return getLinkageOptionsAsync(config, parent);
      } else {
        options = [];
      }
    }
    return options;
  }

  /** 【多级联动】获取联动下拉选项（异步） */
  function getLinkageOptionsAsync(config, parent) {
    return new Promise((resolve) => {
      let key = parent ? parent : 'root';
      let options;
      if (config.optionsMap.has(key)) {
        options = config.optionsMap.get(key);
        if (isPromise(options)) {
          options.then((opt) => {
            config.optionsMap.set(key, opt);
            resolve(opt);
          });
        } else {
          resolve(options);
        }
      } else if (isFunction(config.requestData)) {
        // 调用requestData方法，通过传入parent来获取子级
        // noinspection JSVoidFunctionReturnValueUsed,TypeScriptValidateJSTypes
        let promise = config.requestData(parent);
        config.optionsMap.set(key, promise);
        promise.then((opt) => {
          config.optionsMap.set(key, opt);
          resolve(opt);
        });
      } else {
        resolve([]);
      }
    });
  }

  // 【多级联动】 用于回显数据，自动填充 optionsMap
  function autoSetLinkageOptionsByData(data, parent, config, level) {
    if (level === 0) {
      getLinkageOptionsAsync(config, '');
    } else {
      getLinkageOptionsAsync(config, parent);
    }
    if (config.keys.length - 1 > level) {
      let value = data[config.keys[level]];
      if (value) {
        autoSetLinkageOptionsByData(data, value, config, level + 1);
      }
    }
  }

  // 【多级联动】联动组件change时，清空下级组件
  function handleLinkageSelectChange(row, col, config, value) {
    if (col.linkageKey) {
      getLinkageOptionsAsync(config, value);
      let idx = config.keys.findIndex((k) => k === col.key);
      let values = {};
      for (let i = idx; i < config.keys.length; i++) {
        values[config.keys[i]] = '';
      }
      // 清空后几列的数据
      methods.setValues([{ rowKey: row.id, values }]);
    }
  }

  return {
    getLinkageOptionsAsync,
    getLinkageOptionsSibling,
    handleLinkageSelectChange,
    handleLinkageBackData,
  };
}
