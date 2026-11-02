import { FormSchema, RenderCallbackParams } from '/@/components/Form';
import { computed, ref, watch } from 'vue';
import { getDictItemsByCode } from '/@/utils/dict';
import { filterMultiDictText, filterDictText } from '/@/utils/dict/JDictSelectUtil';
import { initDictOptions } from '/@/utils/dict/index';
import { loadDictItem, queryDepartTreeSync, getUserList } from '/@/api/common/api';
import { defHttp } from '/@/utils/http/axios';
import { getAreaTextByCodeAnyLevel } from '/@/components/Form/src/utils/Area';
import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
import { createImgPreview } from '/@/components/Preview/index';
import { useMessage } from '/@/hooks/web/useMessage';

/***
 * 表单字段的扩展配置解析结果
 */
interface FieldExtends {
  //上传数量
  uploadnum?: number | string;

  //限制大文本在列表页面的展示长度
  showLength?: number | string;

  //popup是否支持多选
  popupMulti?: boolean;

  //部门、用户组件 用于存储的字段名
  store?: string;

  //部门、用户组件 用于展示的字段名
  text?: string;

  //部门、用户组件 是否多选
  multiSelect?: boolean;

  //查询排序规则
  orderRule?: 'asc' | 'desc';
  //关联记录展示类型
  showType?:string;
}

export interface DetailFormSchema {
  field: string;
  label: string;
  span?: number;
  view?: string;
  isHtml?: boolean;
  isImage?: boolean;
  isFile?: boolean;
  isCard?: boolean;
  // update-begin--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
  imageField?: string;
  // update-end--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
  multi?:boolean;
  order?: any;
  dictTable?: string;
  dictText?: string;
  dictCode?: string;
  dict?: string;
  // 是否树表父节点字段 1是 0否
  pidComponent?: number;
  // 树根节点的父ID值，默认0
  pidValue?: string;
  fieldExtendJson?: string;
  ifShow?: boolean | ((renderCallbackParams: RenderCallbackParams) => boolean);
  // update-begin--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
  // js增强隐藏
  hidden?: boolean;
  // update-end--author:liaozhiyang---date:20240425---for：【issues/6139】online详情支持js增强loaded事件及设置值、获取值、隐藏功能
}

/*interface DetailFormProps {
  span?: number;
  schemas?: DetailFormSchema[];
  data?: any;
  containerClass?: string;
}*/

export function useDetailForm(props: any) {
  console.log(props);
  const dictOptionsMap = {};
  const currentLinkFields: string[] = [];
  const detailFormData = ref({});
  const { createMessage } = useMessage();

  const formContainerClass = computed(() => {
    if (props.containerClass) {
      return `jeecg-detail-form ${props.containerClass}`;
    } else {
      return 'jeecg-detail-form';
    }
  });

  watch(
    () => props.data,
    async (formData) => {
      if (formData) {
        let arr = props.schemas;
        let temp = {};
        if (arr && arr.length > 0) {
          for (let item of arr) {
            let field = item.field;
            try {
              temp[field] = await getItemContent(item);
            } catch (e) {
              console.error('字段【' + field + '】文本获取失败', e);
            }
          }
        }
        detailFormData.value = temp;
      }
    },
    { deep: true, immediate: true }
  );

  async function getItemContent(item) {
    let formData = props.data;
    if (formData) {
      let value = formData[item.field];
      if (!value && value !== '0' && value !== 0) {
        return '';
      }
      let str = value;
      let view = item.view;
      if (view == 'list' || view == 'radio' || view == 'checkbox' || view == 'list_multi') {
        str = await getSelectText(item, formData);
      } else if (view == 'sel_search') {
        str = await getTableDataText(item, formData);
      } else if (view == 'cat_tree') {
        //分类字典树
        str = await getCategoryDataText(item, formData);
      } else if (view == 'link_table') {
        str = await getLinkTableData(item, formData);
      } else if (view == 'sel_depart') {
        //部门选择
        str = await getDepartDataText(item, formData);
      } else if (view == 'sel_user') {
        // 用户选择
        str = await getUserDataText(item, formData);
      } else if (view == 'pca') {
        //省市区
        // update-begin--author:liaozhiyang---date:20260227---for:【QQYUN-14788】online详情单独的省市没显示
        let includeParent = true;
        let fieldExtendJson = item?.fieldExtendJson;
        let level = 3;
        if (fieldExtendJson) {
          fieldExtendJson = JSON.parse(fieldExtendJson);
          if (['province', 'city', 'region'].includes(fieldExtendJson.displayLevel)) {
            if (fieldExtendJson.displayLevel === 'province') {
              level = 1;
            } else if (fieldExtendJson.displayLevel === 'city') {
              level = 2;
            } else if (fieldExtendJson.displayLevel === 'region') {
              level = 3;
            }
            includeParent = false;
          }
        }
        str = getAreaTextByCodeAnyLevel(value, includeParent, level as 1 | 2 | 3);
        // update-end--author:liaozhiyang---date:20260227---for:【QQYUN-14788】online详情单独的省市没显示
      } else if (view == 'link_down') {
        //联动组件
        str = await getLinkDownDataText(item, formData);
      } else if (view == 'sel_tree') {
        //自定义树控件
        str = await getTreeDataText(item, formData);
      } else if (view == 'switch') {
        //开关组件
        str = await getSwitchDataText(item, formData);
      } else if (view == 'image' || view == 'file') {
        str = getFileList(item, formData);
      } else if (view == 'popup_dict') {
        // update-begin--author:liaozhiyang---date:20240402---for：【QQYUN-8833】JPopupDict的列表翻译
        const ditc = formData[`${item.field}_dictText`];
        if (ditc !== undefined) {
          str = ditc;
        }
        // update-end--author:liaozhiyang---date:20240402---for：【QQYUN-8833】JPopupDict的列表翻译
      } else {
        if (currentLinkFields.indexOf(item.field) >= 0) {
          let arr = dictOptionsMap[item.field];
          if (arr && arr.length > 0) {
            str = filterMultiDictText(arr, value);
          }
        }
      }
      return str;
    }
    return '';
  }

  // 数据字典/表字典
  async function getSelectText(item, formData) {
    // 先从缓存取
    let dictCode = getRequestDictCode(item);
    let value = formData[item.field];
    if (!dictCode) {
      return value;
    }
    let options = getDictItemsByCode(dictCode);
    if (options && options.length > 0) {
      return filterMultiDictText(options, value);
    } else {
      let dictRes = [];
      if (dictOptionsMap[dictCode]) {
        dictRes = dictOptionsMap[dictCode];
      } else {
        //取不到再请求
        dictRes = (await initDictOptions(dictCode)) || [];
      }
      if (dictRes && dictRes.length > 0) {
        dictOptionsMap[dictCode] = dictRes;
        return filterMultiDictText(dictRes, value);
      }
    }
    return '';
  }

  function getRequestDictCode(item) {
    let temp = '';
    let { dictCode, dictTable, dictText } = item;
    if (!dictTable) {
      temp = dictCode;
    } else {
      temp = encodeURI(`${dictTable},${dictText},${dictCode}`);
    }
    return temp;
  }

  // 表字典-下拉搜索
  async function getTableDataText(item, formData) {
    let dictCode = getRequestDictCode(item);
    let value = formData[item.field];
    if (!value) {
      return '';
    }

    let arr: any[] = [];
    // update-begin--author:liaozhiyang---date:20250813---for：【issues/8689】online下拉搜索框详情时无法读取数据字典
    // 系统字典
    if (dictCode.indexOf(',') === -1) {
      const options = await initDictOptions(dictCode);
      if (options && options.length > 0) {
        options.forEach((item: any) => {
          if (item.value === value) {
            arr.push(item.text || item.label);
          }
        });
      }
    } else {
      // 表字典
      if (dictOptionsMap[dictCode+value]) {
        arr = dictOptionsMap[dictCode+value];
      } else {
        //取不到再请求
        arr = (await defHttp.get({ url: `/sys/dict/loadDictItem/${dictCode}`, params: { key: value } })) || [];
      }
    }
    // update-end--author:liaozhiyang---date:20250813---for：【issues/8689】online下拉搜索框详情时无法读取数据字典
    if (arr && arr.length > 0) {
      dictOptionsMap[dictCode+value] = arr;
      return arr.join(',')
      //return filterMultiDictText(arr, value);
    }
    return '';
  }

  // 分类字典
  async function getCategoryDataText(item, formData) {
    let value = formData[item.field];
    if (!value) {
      return '';
    }
    let arr = (await loadDictItem({ ids: value })) || [];
    if (arr && arr.length > 0) {
      return arr.join(',');
    }
    return '';
  }

  // 部门数据
  async function getDepartDataText(item, formData) {
    let value = formData[item.field];
    if (!value) {
      return '';
    }
    let extend = getExtendConfig(item);
    let storeField = extend.store || 'id';
    let labelKey = extend.text || 'departName';
    let arr = (await queryDepartTreeSync({ ids: value, primaryKey: storeField })) || [];
    if (arr && arr.length > 0) {
      let temp: string[] = [];
      for (let item of arr) {
        if (item[labelKey]) {
          temp.push(item[labelKey]);
        } else {
          temp.push(item.title);
        }
      }
      return temp.join(',');
    }
    return '';
  }

  //用户数据
  async function getUserDataText(item, formData) {
    let value = formData[item.field];
    if (!value) {
      return '';
    }
    let extend = getExtendConfig(item);
    let storeField = extend.store || 'username';
    let params = {
      [storeField]: value,
    };
    let res = (await getUserList(params)) || {};
    let arr = res.records || [];
    if (arr && arr.length > 0) {
      let temp: string[] = [];
      console.log('getUserDataText', arr);
      let textField = extend.text || 'realname';
      for (let item of arr) {
        temp.push(item[textField]);
      }
      return temp.join(',');
    }
    return '';
  }

  function getExtendConfig(item) {
    let extend: FieldExtends = {};
    let { fieldExtendJson } = item;
    if (fieldExtendJson) {
      if (typeof fieldExtendJson == 'string') {
        try {
          let json = JSON.parse(fieldExtendJson);
          extend = { ...json };
        } catch (e) {
          console.error(e);
        }
      }
    }
    return extend;
  }

  // 联动组件
  async function getLinkDownDataText(item, formData) {
    let { dictTable, field } = item;
    let arr: any[] = [];
    if (dictOptionsMap[field]) {
      arr = dictOptionsMap[field];
    } else {
      if (dictTable) {
        let json = JSON.parse(dictTable);
        if (json) {
          let { table, txt, key, linkField } = json;
          let dictCode = `${table},${txt},${key}`;
          let temp: any[] = (await initDictOptions(dictCode)) || [];
          arr = [...temp];
          if (arr && arr.length > 0) {
            dictOptionsMap[field] = arr;
            if (linkField) {
              let fieldArray = linkField.split(',');
              for (let item of fieldArray) {
                dictOptionsMap[item] = arr;
                currentLinkFields.push(item);
              }
            }
          }
        }
      }
    }
    if (arr && arr.length > 0) {
      let value = formData[field];
      return filterMultiDictText(arr, value);
    }
    return '';
  }

  //自定义树
  async function getTreeDataText(item, formData) {
    let { dict, field } = item;
    // update-begin--author:liusq---date:20261102---for：online树表详情顶级节点的父节点显示0
    // 树表父节点字段：顶级节点的父ID是根节点值(默认0)，没有对应的节点，不显示（与编辑页JTreeSelect一致）
    if (item.pidComponent == 1 && formData[field] == (item.pidValue || '0')) {
      return '';
    }
    // update-end--author:liusq---date:20261102---for：online树表详情顶级节点的父节点显示0
    let arr = [];
    if (dictOptionsMap[field]) {
      arr = dictOptionsMap[field];
    } else {
      if (dict) {
        arr = await initDictOptions(dict);
      }
    }
    if (arr && arr.length > 0) {
      let value = formData[field];
      return filterMultiDictText(arr, value);
    }
    return '';
  }

  //开关
  async function getSwitchDataText(item, formData) {
    let { fieldExtendJson, field } = item;
    let options = ['Y', 'N'];
    if (fieldExtendJson) {
      //update-begin---author:wangshuai---date:2025-11-03---for:【issues/9036】online 表单开发， 设置字段 控件类型为开关时，查看详情页时 开关字段显示原始值---
      const extendConfig = JSON.parse(fieldExtendJson);
      // update-begin--author:liaozhiyang---date:20260813---for：【LHZP-4】online详情弹窗老数据开关没渲染出来
      if (Array.isArray(extendConfig)) {
        // 兼容旧数据：数组本身就是开关选项  现有数据：{switchOptions:[1,2]}   旧数据：[1,2]
        options = extendConfig;
      } else if (Array.isArray(extendConfig?.switchOptions)) {
        options = extendConfig.switchOptions;
      }
      // update-end--author:liaozhiyang---date:20260813---for：【LHZP-4】online详情弹窗老数据开关没渲染出来
      //update-end---author:wangshuai---date:2025-11-03---for:【issues/9036】online 表单开发， 设置字段 控件类型为开关时，查看详情页时 开关字段显示原始值---
    }
    let arr: any[] = [
      { value: options[0], text: '是' },
      { value: options[1], text: '否' },
      { value: options[0]+'', text: '是' },
      { value: options[1]+'', text: '否' },
    ];
    let value = formData[field];
    return filterDictText(arr, value);
  }

  function getItemSpan(item) {
    if (item.span) {
      return item.span;
    }
    return props.span;
  }

  function getFileList(item, formData) {
    let str = formData[item.field];
    if (!str) {
      return [];
    }
    let arr = str.split(',');
    let result: string[] = [];
    for (let item of arr) {
      let src = getFileAccessHttpUrl(item) || '';
      if (src) {
        result.push(src);
      }
    }
    return result;
  }

  function handleDownloadFile(url) {
    if (url) {
      window.open(url);
    }
  }

  function handleViewImage(field) {
    let values = detailFormData.value[field];
    if (!values || values.length == 0) {
      createMessage.warning('无图片!');
      return;
    }
    createImgPreview({ imageList: values });
  }

  function getFilename(url) {
    if (!url) {
      return '';
    }
    return url.substring(url.lastIndexOf('/') + 1);
  }

  /**
   * VUEN-1772【vue3 online 详情】ai—ai_control_single 开关组件未翻译、字段为一行时，未居左对齐
   */
  const span24ViewArray = ['file', 'image', 'markdown', 'umeditor'];
  function getLabelWidthClass(item) {
    if(span24ViewArray.indexOf(item.view)>=0){
      if(props.span==12){
        return 'span12';
      }else if(props.span==8){
        return 'span8';
      }else if(props.span==6){
        return 'span6';
      }else{
        return 'span24';
      }
    }
    return ''
  }
  
  // 关联记录
  async function getLinkTableData(item, formData) {
    let value = formData[item.field];
    let extend = getExtendConfig(item);
    if(extend.showType=='select'){
      if (!value) {
        return '';
      }
      return formData[item.field+'_dictText'];
    }else{
      if (!value) {
        return '';
      }
      return formData[item.field];
    }
    
    let storeField = extend.store || 'id';
    let arr = (await queryDepartTreeSync({ ids: value, primaryKey: storeField })) || [];
    if (arr && arr.length > 0) {
      let temp: string[] = [];
      for (let item of arr) {
        temp.push(item.title);
      }
      return temp.join(',');
    }
    return '';
  }

  return {
    formContainerClass,
    detailFormData,
    getItemSpan,
    handleDownloadFile,
    handleViewImage,
    getFilename,
    getLabelWidthClass
  };
}

/**
 * TODO 尚未实现
 * 获取 DetailFormSchema[自定义开发用]
 */
export function transDetailFormSchemas(formSchemas: FormSchema[]) {
  const detailFormSchemas = ref<DetailFormSchema[]>([]);
  console.log(formSchemas);
  return detailFormSchemas;
}
