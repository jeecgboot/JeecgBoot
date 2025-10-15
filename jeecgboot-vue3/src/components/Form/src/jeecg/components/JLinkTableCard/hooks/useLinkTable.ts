import { defHttp } from '/@/utils/http/axios';
import { ref, watchEffect, computed, reactive } from 'vue';
import { pick } from 'lodash-es';
import { filterMultiDictText } from '/@/utils/dict/JDictSelectUtil';
import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';

function queryTableData(tableName, params) {
  const url = '/online/cgform/api/getData/' + tableName;
  return defHttp.get({ url, params });
}

function queryTableColumns(tableName, params) {
  const url = '/online/cgform/api/getColumns/' + tableName;
  return defHttp.get({ url, params });
}

export function useLinkTable(props) {
  //TODO 目前只支持查询第一页的数据，可以输入关键字搜索
  const pageNo = ref('1');
  // 查询列
  const baseParam = ref<any>({});
  // 搜素条件
  const searchParam = ref<any>({});
  // 第一个文本列
  const mainContentField = ref('');
  //权限数据
  const auths = reactive({
    add: true,
    update: true,
  });

  //显示列
  const textFieldArray = computed(() => {
    if (props.textField) {
      return props.textField.split(',');
    }
    return [];
  });
  const otherColumns = ref<any[]>([]);
  // 展示的列 配置的很多列，但是只展示三行
  const realShowColumns = computed(() => {
    const columns = otherColumns.value;
    if (props.multi == true) {
      return columns.slice(0, 3);
    } else {
      return columns.slice(0, 6);
    }
  });

  watchEffect(async () => {
    const table = props.tableName;
    if (table) {
      const valueField = props.valueField || '';
      const textField = props.textField || '';
      const arr: any[] = [];
      if (valueField) {
        arr.push(valueField);
      }
      if (textField) {
        const temp = textField.split(',');
        mainContentField.value = temp[0];
        for (const field of temp) {
          arr.push(field);
        }
      }
      const imageField = props.imageField || '';
      if (imageField) {
        arr.push(imageField);
      }
      baseParam.value = {
        linkTableSelectFields: arr.join(','),
      };
      await resetTableColumns();
      await reloadTableLinkOptions();
    }
  });

  const otherFields = computed(() => {
    const textField = props.textField || '';
    const others: any[] = [];
    let labelField = '';
    if (textField) {
      const temp = textField.split(',');
      labelField = temp[0];
      for (let i = 0; i < temp.length; i++) {
        if (i > 0) {
          others.push(temp[i]);
        }
      }
    }
    return {
      others,
      labelField,
    };
  });

  // 选项
  const selectOptions = ref<any[]>([]);
  const tableColumns = ref<any[]>([]);
  const dictOptions = ref<any>({});

  async function resetTableColumns() {
    const params = baseParam.value;
    const data = await queryTableColumns(props.tableName, params);
    tableColumns.value = data.columns;
    if (data.columns) {
      const imageField = props.imageField;
      const arr = data.columns.filter((c) => c.dataIndex != mainContentField.value && c.dataIndex != imageField);
      otherColumns.value = arr;
    }
    dictOptions.value = data.dictOptions;
    // 权限数据
    console.log('隐藏的按钮', data.hideColumns);
    if (data.hideColumns) {
      const hideCols = data.hideColumns;
      if (hideCols.indexOf('add') >= 0) {
        auths.add = false;
      } else {
        auths.add = true;
      }
      if (hideCols.indexOf('update') >= 0) {
        auths.update = false;
      } else {
        auths.update = true;
      }
    }
  }

  async function reloadTableLinkOptions() {
    const params = getLoadDataParams();
    const data = await queryTableData(props.tableName, params);
    const records = data.records;
    //tableTitle.value = data.head.tableTxt;
    const dataList: any[] = [];
    const { others, labelField } = otherFields.value;
    const imageField = props.imageField;
    if (records && records.length > 0) {
      for (const rd of records) {
        const temp = { ...rd };
        transData(temp);
        const result = Object.assign({}, pick(temp, others), { id: temp.id, label: temp[labelField], value: temp[props.valueField] });
        if (imageField) {
          result[imageField] = temp[imageField];
        }
        dataList.push(result);
      }
    }
    //添加一个空对象 为add操作占位
    // update-begin--author:liaozhiyang---date:20240607---for：【TV360X-1095】高级查询关联记录去掉编辑按钮及去掉记录按钮
    props.editBtnShow && dataList.push({});
    // update-end--author:liaozhiyang---date:20240607---for：【TV360X-1095】高级查询关联记录去掉编辑按钮及去掉记录按钮
    selectOptions.value = dataList;
  }

  /**
   * 数据简单翻译-字典
   * @param data
   */
  function transData(data) {
    const columns = tableColumns.value;
    const dictInfo = dictOptions.value;
    for (const c of columns) {
      const { dataIndex, customRender } = c;
      if (data[dataIndex] || data[dataIndex] === 0) {
        if (customRender && customRender == dataIndex) {
          //这样的就是 字典数据了 可以直接翻译
          if (dictInfo[customRender]) {
            data[dataIndex] = filterMultiDictText(dictInfo[customRender], data[dataIndex]);
            continue;
          }
        }
      }
      // 兼容后台翻译字段
      const dictText = data[dataIndex + '_dictText'];
      if (dictText) {
        data[dataIndex] = dictText;
      }
    }
  }

  //获取加载数据的查询条件
  function getLoadDataParams() {
    const params = Object.assign({ pageSize: 100, pageNo: pageNo.value }, baseParam.value, searchParam.value);
    return params;
  }

  //设置查询条件
  function addQueryParams(text) {
    if (!text) {
      searchParam.value = {};
    } else {
      const arr = textFieldArray.value;
      const params: any[] = [];
      const fields: any[] = [];
      for (let i = 0; i < arr.length; i++) {
        if (i <= 1) {
          fields.push(arr[i]);
          params.push({ field: arr[i], rule: 'like', val: text });
        }
      }
      // params[arr[i]] = `*${text}*`
      // params['selectConditionFields'] = fields.join(',')
      // searchParam.value = params;
      params['superQueryMatchType'] = 'or';
      params['superQueryParams'] = encodeURI(JSON.stringify(params));
      searchParam.value = params;
    }
  }

  async function loadOne(value) {
    if (!value) {
      return [];
    }
    let valueFieldName = props.valueField;
    let params = {
      ...baseParam.value,
      pageSize: 100,
      pageNo: pageNo.value,
    };
    params['superQueryMatchType'] = 'and';
    let valueCondition = [{ field: valueFieldName, rule: 'in', val: value }];
    params['superQueryParams'] = encodeURI(JSON.stringify(valueCondition));
    const data = await queryTableData(props.tableName, params);
    let records = data.records;
    //tableTitle.value = data.head.tableTxt;
    let dataList: any[] = [];
    if (records && records.length > 0) {
      for (let item of records) {
        let temp = { ...item };
        transData(temp);
        dataList.push(temp);
      }
    }
    return dataList;
  }

  /**
   * true:数据一致；false:数据不一致
   * @param arr
   * @param value
   */
  function compareData(arr, value) {
    if (!arr || arr.length == 0) {
      return false;
    }
    const valueArray = value.split(',');
    if (valueArray.length != arr.length) {
      return false;
    }
    let flag = true;
    for (const item of arr) {
      const temp = item[props.valueField];
      if (valueArray.indexOf(temp) < 0) {
        flag = false;
      }
    }
    return flag;
  }

  function formatData(formData) {
    Object.keys(formData).map((k) => {
      if (formData[k] instanceof Array) {
        formData[k] = formData[k].join(',');
      }
    });
  }

  function initFormData(formData, linkFieldArray, record) {
    if (!record) {
      record = {};
    }
    if (linkFieldArray && linkFieldArray.length > 0) {
      for (const str of linkFieldArray) {
        const arr = str.split(',');
        //["表单字段,表字典字段"]
        const field = arr[0];
        const dictField = arr[1];
        if (!formData[field]) {
          const value = record[dictField] || '';
          formData[field] = [value];
        } else {
          formData[field].push(record[dictField]);
        }
      }
    }
  }

  // 获取图片地址
  function getImageSrc(item) {
    if (props.imageField) {
      let url = item[props.imageField];
      // update-begin--author:liaozhiyang---date:20250517---for：【TV360X-38】关联记录空间，被关联数据优多个图片时，封面图片不展示
      if (typeof url === 'string') {
        // 有多张图时默认取第一张
        url = url.split(',')[0];
      }
      // update-end--author:liaozhiyang---date:20250517---for：【TV360X-38】关联记录空间，被关联数据优多个图片时，封面图片不展示
      return getFileAccessHttpUrl(url);
    }
    return '';
  }
  const showImage = computed(() => {
    if (props.imageField) {
      return true;
    } else {
      return false;
    }
  });

  return {
    pageNo,
    otherColumns,
    realShowColumns,
    selectOptions,
    reloadTableLinkOptions,
    textFieldArray,
    addQueryParams,
    tableColumns,
    transData,
    mainContentField,
    loadOne,
    compareData,
    formatData,
    initFormData,
    getImageSrc,
    showImage,
    auths,
  };
}

/**
 * 使用固定高度的modal
 */
export function useFixedHeightModal() {
  const minWidth = 800;
  const popModalFixedWidth = ref(800);
  let tempWidth = window.innerWidth - 300;
  if (tempWidth < minWidth) {
    tempWidth = minWidth;
  }
  popModalFixedWidth.value = tempWidth;

  // 弹窗高度控制
  const popBodyStyle = ref({});
  function resetBodyStyle() {
    const height = window.innerHeight - 210;
    popBodyStyle.value = {
      height: height + 'px',
      overflowY: 'auto',
    };
  }

  return {
    popModalFixedWidth,
    popBodyStyle,
    resetBodyStyle,
  };
}
