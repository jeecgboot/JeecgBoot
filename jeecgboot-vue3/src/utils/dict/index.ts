import { defHttp } from '/@/utils/http/axios';
import { useUserStore } from '/@/store/modules/user';
import { getAuthCache } from '/@/utils/auth';
import { DB_DICT_DATA_KEY } from '/@/enums/cacheEnum';

/**
 * 从缓存中获取字典配置
 * @param code
 */
export const getDictItemsByCode = (code) => {
  // 代码逻辑说明: 【QQYUN-6417】生产环境字典慢的问题
  const userStore = useUserStore();
  const dictItems = userStore.getAllDictItems;
  if (null != dictItems && typeof dictItems === 'object' && dictItems[code]) {
    return dictItems[code];
  }
  //兼容以前的旧写法
  if (getAuthCache(DB_DICT_DATA_KEY) && getAuthCache(DB_DICT_DATA_KEY)[code]) {
    return getAuthCache(DB_DICT_DATA_KEY)[code];
  }


};
/**
 * 从缓存中获取Pop字典配置
 * @param text
 * @param code
 */
export const getPopDictByCode = (text, codeStr) => {
  const [code, dictCode, dictText] = codeStr.split(',');
  if (!code || !dictCode || !dictText) {
    return [];
  }
  return defHttp.get(
    { url: `/online/api/cgreportGetDataPackage`, params: { code, dictText, dictCode, dataList: text } },
    { isTransformResponse: false }
  );
};
/**
 * 获取字典数组
 * @param dictCode 字典Code
 * @return List<Map>
 */
export const initDictOptions = (code) => {
  //1.优先从缓存中读取字典配置
  if (getDictItemsByCode(code)) {
    return new Promise((resolve, reject) => {
      resolve(getDictItemsByCode(code));
    });
  }
  //2.获取字典数组
  // 代码逻辑说明: 字典数据请求前将参数编码处理，但是不能直接编码，因为可能之前已经编码过了
  if (code.indexOf(',') > 0 && code.indexOf(' ') > 0) {
    // 编码后类似sys_user%20where%20username%20like%20xxx' 是不包含空格的,这里判断如果有空格和逗号说明需要编码处理
    code = encodeURI(code);
  }
  return defHttp.get({ url: `/sys/dict/getDictItems/${code}` });
};
/**
 * 获取字典数组
 * @param code 字典Code
 * @param params 查询参数
 * @param options 查询配置
 * @return List<Map>
 */
export const ajaxGetDictItems = (code, params, options?) => defHttp.get({ url: `/sys/dict/getDictItems/${code}`, params }, options);
