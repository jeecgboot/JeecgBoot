import { reactive, ref, unref, defineAsyncComponent, toRaw, markRaw, isRef, watch, onUnmounted } from 'vue';
import { httpGroupRequest } from '/@/components/Form/src/utils/GroupRequest';
import { defHttp } from '/@/utils/http/axios';
import { filterMultiDictText } from '/@/utils/dict/JDictSelectUtil.js';
import { useMessage } from '/@/hooks/web/useMessage';
import { OnlineColumn } from '/@/components/jeecg/OnLine/types/onlineConfig';
import { h } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useMethods } from '/@/hooks/system/useMethods';
import { importViewsFile, _eval } from '/@/utils';
import {getToken} from "@/utils/auth";

export function usePopBiz(ob, tableRef?) {
  // update-begin--author:liaozhiyang---date:20230811---for：【issues/675】子表字段Popup弹框数据不更新
  let props: any;
  if (isRef(ob)) {
    props = ob.value;
    const stopWatch = watch(ob, (newVal) => {
      props = newVal;
    });
    onUnmounted(() => stopWatch());
  } else {
    props = ob;
  }
  // update-end--author:liaozhiyang---date:20230811---for：【issues/675】子表字段Popup弹框数据不更新
  const { createMessage } = useMessage();
  //弹窗可视状态
  const visible = ref(false);
  //表格加载
  const loading = ref(false);
  //cgRpConfigId
  const cgRpConfigId = ref('');
  //标题
  const title = ref('列表');
  // 排序字段，默认无排序
  const iSorter = ref<any>('');
  // 查询对象
  const queryInfo = ref([]);
  // 查询参数
  const queryParam = ref<any>({});
  // 动态参数
  const dynamicParam = ref<any>({});
  //字典配置项
  const dictOptions = ref({});
  //数据集
  const dataSource = ref<Array<object>>([]);
  //定义表格信息
  const columns = ref<Array<object>>([]);
  // 当前路由
  const route = useRoute();
  //定义请求url信息
  const configUrl = reactive({
    //列表页加载column和data
    getColumnsAndData: '/online/cgreport/api/getColumnsAndData/',
    getColumns: '/online/cgreport/api/getRpColumns/',
    getData: '/online/cgreport/api/getData/',
    getQueryInfo: '/online/cgreport/api/getQueryInfo/',
    export: '/online/cgreport/api/exportManySheetXls/',
  });
  //已选择的值
  const checkedKeys = ref<Array<string | number>>([]);
  //选择的行记录
  const selectRows = ref<Array<any>>([]);
  // 点击单元格选中行 popup需要 但是报表预览不需要
  let clickThenCheckFlag = true;
  if (props.clickToRowSelect === false) {
    clickThenCheckFlag = false;
  }

  /**
   * 选择列配置
   */
  const rowSelection = {
    fixed: true,
    type: props.multi ? 'checkbox' : 'radio',
    selectedRowKeys: checkedKeys,
    selectionRows: selectRows,
    onChange: onSelectChange,
  };

  /**
   * 序号列配置
   */
  const indexColumnProps = {
    dataIndex: 'index',
    width: '15px',
  };
  /**
   * 分页配置
   */
  const pagination = reactive({
    current: 1,
    pageSize: 10,
    pageSizeOptions: ['10', '20', '30'],
    // showTotal: (total, range) => {
    //     return range[0] + '-' + range[1] + ' 共' + total + '条'
    // },
    showQuickJumper: true,
    showSizeChanger: true,
    total: 0,
    // 合计逻辑 [待优化 3.0]
    showTotal: (total) => onShowTotal(total),
    realPageSize: 10,
    realTotal: 0,
    // 是否有合计列，默认为""，在第一次获取到数据之后会设计为ture或者false
    isTotal: <string | boolean>'',
    onShowSizeChange: (current, pageSize) => onSizeChange(current, pageSize),
  });

  /**
   * 表格选择事件
   * @param selectedRowKeys
   * @param selectRow
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7514】popup单选显示radio
    if (!props.multi) {
      selectRows.value = [];
      checkedKeys.value = [];
      // update-begin--author:liaozhiyang---date:20240717---for：【issues/6883】单选模式第二次打开已勾选
      // selectedRowKeys = [selectedRowKeys[selectedRowKeys.length - 1]];
      // update-end--author:liaozhiyang---date:20240717---for：【issues/6883】单选模式第二次打开已勾选
    }
    // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7514】popup单选显示radio
    // update-begin--author:liaozhiyang---date:20230919---for：【QQYUN-4263】跨页选择导出问题
    if (!selectedRowKeys || selectedRowKeys.length == 0) {
      selectRows.value = [];
      checkedKeys.value = [];
    } else {
      if (selectRows.value.length > selectedRowKeys.length) {
        // 取消
        selectRows.value.forEach((item, index) => {
          const rowKey = combineRowKey(item);
          if (!selectedRowKeys.find((key) => key === rowKey)) {
            selectRows.value.splice(index, 1);
          }
        });
      } else {
        // 新增
        const append: any = [];
        const beforeRowKeys = selectRows.value.map((item) => combineRowKey(item));
        selectedRowKeys.forEach((key) => {
          if (!beforeRowKeys.find((item) => item === key)) {
            // 那就是新增选中的行
            const row = getRowByKey(key);
            row && append.push(row);
          }
        });
        selectRows.value = [...selectRows.value, ...append];
      }
      checkedKeys.value = [...selectedRowKeys];
    }
    // update-end--author:liaozhiyang---date:20230919---for：【QQYUN-4263】跨页选择导出问题
  }
  /**
   * 过滤没用选项
   * @param selectedRowKeys
   */
  function filterUnuseSelect() {
    selectRows.value = unref(selectRows).filter((item) => {
      let combineKey = combineRowKey(item);
      return unref(checkedKeys).indexOf(combineKey) >= 0;
    });
  }

  /**
   * 根据key获取row信息
   * @param key
   */
  function getRowByKey(key) {
    let row = unref(dataSource).filter((record) => combineRowKey(record) === key);
    return row && row.length > 0 ? row[0] : '';
  }

  /**
   * 加载rowKey
   */
  function combineRowKey(record) {
    let res = record?.id || '';
    Object.keys(record).forEach((key) => {
      res = key == 'rowIndex' ? record[key] + res : res + record[key];
    });
    res = res.length > 50 ? res.substring(0, 50) : res;
    return res;
  }

  /**
   * 加载列信息
   */
  function loadColumnsInfo() {
    let url = `${configUrl.getColumns}${props.code}`;
    //缓存key
    let groupIdKey = props.groupId ? `${props.groupId}${url}` : '';
    httpGroupRequest(() => defHttp.get({ url }, { isTransformResponse: false, successMessageMode: 'none' }), groupIdKey).then((res) => {
      if (res.success) {
        initDictOptionData(res.result.dictOptions);
        cgRpConfigId.value = res.result.cgRpConfigId;
        title.value = res.result.cgRpConfigName;
        let currColumns = res.result.columns;
        for (let a = 0; a < currColumns.length; a++) {
          if (currColumns[a].customRender) {
            let dictCode = currColumns[a].customRender;
            currColumns[a].customRender = ({ text }) => {
              return filterMultiDictText(unref(dictOptions)[dictCode], text + '');
            };
          }
          // 排序字段受控
          if (unref(iSorter) && currColumns[a].dataIndex === unref(iSorter).column) {
            currColumns[a].sortOrder = unref(iSorter).order === 'asc' ? 'ascend' : 'descend';
          }
        }
        if (currColumns[0].key !== 'rowIndex') {
          currColumns.unshift({
            title: '序号',
            dataIndex: 'rowIndex',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function ({ text }) {
              // update-begin--author:liaozhiyang---date:20231226---for：【QQYUN-7584】popup有合计时序号列会出现NaN
              if (text == undefined) {
                return '';
              } else {
                return parseInt(text) + 1;
              }
              // update-end--author:liaozhiyang---date:20231226---for：【QQYUN-7584】popup有合计时序号列会出现NaN
            },
          });
        }
        columns.value = [...currColumns];
        initQueryInfo(null);
      }
    });
  }

  /**
   * 加载列和数据[列表专用]
   */
  function loadColumnsAndData() {
    // 第一次加载 置空isTotal 在这里调用确保 该方法只是进入页面后 加载一次 其余查询不走该方法
    pagination.isTotal = '';
    let url = `${configUrl.getColumnsAndData}${props.id}`;
    //缓存key
    let groupIdKey = props.groupId ? `${props.groupId}${url}` : '';
    httpGroupRequest(() => defHttp.get({ url }, { isTransformResponse: false, successMessageMode: 'none' }), groupIdKey).then((res) => {
      if (res.success) {
        initDictOptionData(res.result.dictOptions);
        cgRpConfigId.value = props.id;
        let { columns: metaColumnList, cgreportHeadName, fieldHrefSlots, isGroupTitle } = res.result;
        title.value = cgreportHeadName;
        // href 跳转
        const fieldHrefSlotKeysMap = {};
        fieldHrefSlots.forEach((item) => (fieldHrefSlotKeysMap[item.slotName] = item));
        let currColumns = handleColumnHrefAndDict(metaColumnList, fieldHrefSlotKeysMap);

        // popup需要序号， 普通列表不需要
        if (clickThenCheckFlag === true) {
          currColumns.unshift({
            title: '序号',
            dataIndex: 'rowIndex',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function ({ text }) {
              return parseInt(text) + 1;
            },
          });
        }

        // 合并表头
        if (isGroupTitle === true) {
          currColumns = handleGroupTitle(currColumns);
        }
        columns.value = [...currColumns];
        initQueryInfo(res.result.data);
      } else {
        //update-begin-author:taoyan date:20220401 for: VUEN-583【vue3】JeecgBootException: sql黑名单校验不通过,请联系管理员!,前台无提示
        createMessage.warning(res.message);
        //update-end-author:taoyan date:20220401 for: VUEN-583【vue3】JeecgBootException: sql黑名单校验不通过,请联系管理员!,前台无提示
      }
    });
  }

  /**
   * 处理求和的列 合计逻辑 [待优化 3.0]
   */
  function handleSumColumn(metaColumnList: OnlineColumn[], dataTotal: number): void {
    // 获取需要合计列的dataIndex
    let sumColumnList = getNeedSumColumns(metaColumnList);
    // 判断是否为第一次获取数据，如果是的话，则需要重新设置pageSize
    if (pagination.isTotal == '') {
      if (sumColumnList.length > 0) {
        pagination.isTotal = true;
        // 有合计字段时，每次最多查询原pageSize-1条记录，另外需要第一次时将查询的10条中删除最后一条
        // 删除最后一条数据 如果第一次得到的数据长度等于pageSize的话，则删除最后一条
        if (dataSource.value.length == pagination.pageSize) {
          let remove_data = dataSource.value.pop();
        }
        pagination.realPageSize = pagination.pageSize - 1;
      } else {
        pagination.isTotal = false;
      }
    }
    // 需要添加合计字段
    if (pagination.isTotal) {
      let totalRow = {};
      sumColumnList.forEach((dataIndex) => {
        let count = 0;
        dataSource.value.forEach((row) => {
          // 统计去除null及空数据
          if (row[dataIndex] != null && row[dataIndex] != '') {
            count += parseFloat(row[dataIndex]);
          }
        });
        totalRow[dataIndex] = isNaN(count) ? '包含非数字内容' : count.toFixed(2);

        // 长整形时合计不显示.00后缀
        let v = metaColumnList.find((v) => v.dataIndex == dataIndex);
        if (v && v.fieldType == 'Long') {
          totalRow[dataIndex] = parseInt(totalRow[dataIndex]);
        }
      });
      dataSource.value.push(totalRow);
      pagination.realTotal = dataTotal;
      pagination.total = Number(dataTotal) + Number(Math.floor(dataTotal / pagination.realPageSize));
    }
  }

  /**
   * 获取需要求和的列 dataIndex
   * @param columns
   */
  function getNeedSumColumns(columns: OnlineColumn[]): string[] {
    let arr: string[] = [];
    for (let column of columns) {
      if (column.isTotal === '1') {
        arr.push(column.dataIndex!);
      }
        // 【VUEN-1569】【online报表】合计无效
      if (column.children && column.children.length > 0) {
        let subArray = getNeedSumColumns(column.children);
        if (subArray.length > 0) {
          arr.push(...subArray);
        }
      }
    }
    return arr;
  }

  /**
   * 处理列的href和字典翻译
   */
  function handleColumnHrefAndDict(columns: OnlineColumn[], fieldHrefSlotKeysMap: {}): OnlineColumn[] {
    for (let column of columns) {
      let { customRender, hrefSlotName, fieldType } = column;
      // online 报表中类型配置为日期（yyyy-MM-dd ），但是实际展示为日期时间格式(yyyy-MM-dd HH:mm:ss) issues/3042
      if (fieldType == 'Date') {
        column.customRender = ({ text }) => {
          if (!text) {
            return '';
          }
          if (text.length > 10) {
            return text.substring(0, 10);
          }
          return text;
        };
      } else {
        if (!hrefSlotName && column.scopedSlots && column.scopedSlots.customRender) {
          //【Online报表】字典和href互斥 这里通过fieldHrefSlotKeysMap 先找到是href的列
          if (fieldHrefSlotKeysMap.hasOwnProperty(column.scopedSlots.customRender)) {
            hrefSlotName = column.scopedSlots.customRender;
          }
        }
        // 如果 customRender 有值则代表使用了字典
        // 如果 hrefSlotName 有值则代表使用了href跳转
        // 两者可以兼容。兼容的具体思路为：先获取到字典替换的值，再添加href链接跳转
        if (customRender || hrefSlotName) {
          let dictCode = customRender as string;
          let replaceFlag = '_replace_text_';
          column.customRender = ({ text, record }) => {
            let value = text;
            // 如果 dictCode 有值，就进行字典转换
            if (dictCode) {
              if (dictCode.startsWith(replaceFlag)) {
                let textFieldName = dictCode.replace(replaceFlag, '');
                value = record[textFieldName];
              } else {
                value = filterMultiDictText(unref(dictOptions)[dictCode], text + '');
              }
            }
            // 扩展参数设置列的内容长度
            if (column.showLength) {
              if (value && value.length > column.showLength) {
                value = value.substr(0, column.showLength) + '...';
              }
            }
            // 如果 hrefSlotName 有值，就生成一个 a 标签，包裹住字典替换后（或原生）的值
            if (hrefSlotName) {
              let field = fieldHrefSlotKeysMap[hrefSlotName];
              if (field) {
                return h(
                  'a',
                  {
                    onClick: () => handleClickFieldHref(field, record),
                  },
                  value
                );
              }
            }
            return value;
          };
        }
      }
    }
    return columns;
  }

  /**
   * 处理合并表头
   * @param columns
   */
  function handleGroupTitle(columns: OnlineColumn[]): OnlineColumn[] {
    let newColumns: OnlineColumn[] = [];
    for (let column of columns) {
      //排序字段受控  ---- 此逻辑为新增逻辑 待
      if (unref(iSorter) && column.dataIndex === unref(iSorter).column) {
        column.sortOrder = unref(iSorter).order === 'asc' ? 'ascend' : 'descend';
      }
      //判断字段是否需要合并表头
      if (column.groupTitle) {
        let clIndex = newColumns.findIndex((im) => im.title === column.groupTitle);
        if (clIndex !== -1) {
          //表头已存在直接push children
          newColumns[clIndex].children!.push(column);
        } else {
          //表头不存在组装表头信息
          let clGroup: OnlineColumn = {},
            child: OnlineColumn[] = [];
          child.push(column);
          clGroup.title = column.groupTitle;
          clGroup.align = 'center';
          clGroup.children = child;
          newColumns.push(clGroup);
        }
      } else {
        newColumns.push(column);
      }
    }
    return newColumns;
  }

  // 获取路由器对象 href跳转用到
  let router = useRouter();
  /**
   * href 点击事件
   * @param field
   * @param record
   */
  function handleClickFieldHref(field, record) {
    let href = field.href;
    let urlPattern = /(ht|f)tp(s?)\:\/\/[0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*(:(0-9)*)*(\/?)([a-zA-Z0-9\-\.\?\,\'\/\\\+&amp;%\$#_]*)?/;
    let compPattern = /\.vue(\?.*)?$/;
    let jsPattern = /{{([^}]+)}}/g; // {{ xxx }}
    if (typeof href === 'string') {
      href = href.trim().replace(/\${([^}]+)?}/g, (s1, s2) => record[s2]);
      // 执行 {{...}} JS增强语句
      if (jsPattern.test(href)) {
        href = href.replace(jsPattern, function (text, s0) {
          try {
            // 支持 {{ ACCESS_TOKEN }} 占位符
            if (s0.trim() === 'ACCESS_TOKEN') {
              return getToken()
            }

            // update-begin--author:liaozhiyang---date:20230904---for：【QQYUN-6390】eval替换成new Function，解决build警告
            return _eval(s0);
            // update-end--author:liaozhiyang---date:20230904---for：【QQYUN-6390】eval替换成new Function，解决build警告
          } catch (e) {
            console.error(e);
            return text;
          }
        });
      }
      if (urlPattern.test(href)) {
        window.open(href, '_blank');
      } else if (compPattern.test(href)) {
        // 处理弹框
        openHrefCompModal(href);
      } else {
        router.push(href);
      }
    }
  }

  /**
   * 导出
   */
  function handleExport() {
    const { handleExportXls } = useMethods();
    let url = `${configUrl.export}${cgRpConfigId.value}`;
    let params = getQueryParams(); //查询条件
    // 【VUEN-1568】如果选中了某些行，就只导出选中的行
    let keys = unref(checkedKeys);
    if (keys.length > 0) {
      params['force_id'] = keys
        .map((i) => selectRows.value.find((item) => combineRowKey(item) === i)?.id)
        .filter((i) => i != null && i !== '')
        .join(',');
    }
    handleExportXls(title.value, url, params);
  }

  /**
   * 合计逻辑 [待优化 3.0]
   * 分页 大小改变事件
   * @param _current
   * @param size
   */
  function onSizeChange(_current, size) {
    pagination.isTotal = '';
    pagination.pageSize = size;
    if (pagination.isTotal) {
      pagination.realPageSize = size - 1;
    } else {
      pagination.realPageSize = size;
    }
    pagination.current = 1;
  }

  /**
   *  合计逻辑 [待优化 3.0]
   * 显示总条数
   * @param total
   */
  function onShowTotal(total) {
    // 重新根据是否有合计计算每页显示的数据
    let start = (pagination.current - 1) * pagination.realPageSize + 1;
    let end = start + (pagination.isTotal ? dataSource.value.length - 1 : dataSource.value.length) - 1;
    let realTotal = pagination.isTotal ? pagination.realTotal : total;
    return start + '-' + end + ' 共' + realTotal + '条';
  }

  /**
   * 弹出框显示隐藏触发事件
   */
  async function visibleChange($event) {
    visible.value = $event;
    $event && loadColumnsInfo();
  }

  /**
   * 初始化查询条件
   * @param data 数据结果集
   */
  function initQueryInfo(data) {
    let url = `${configUrl.getQueryInfo}${unref(cgRpConfigId)}`;
    //缓存key
    let groupIdKey = props.groupId ? `${props.groupId}${url}` : '';
    httpGroupRequest(() => defHttp.get({ url }, { isTransformResponse: false, successMessageMode: 'none' }), groupIdKey).then((res) => {
      // console.log("获取查询条件", res);
      if (res.success) {
        dynamicParamHandler(res.result);
        queryInfo.value = res.result;
        console.log('queryInfo==>', queryInfo.value);
        //查询条件加载后再请求数据
        if (data) {
          setDataSource(data);
          //传递路由参数和动态参数，不生效，
          loadData(1);
        } else {
          //没有传递data时查询数据
          loadData(1);
        }
      } else {
        createMessage.warning(res.message);
      }
    });
  }

  /**
   * 加载表格数据
   * @param arg
   */
  function loadData(arg?) {
    if (arg == 1) {
      pagination.current = 1;
    }
    let params = getQueryParams(); //查询条件
    params['onlRepUrlParamStr'] = getUrlParamString();
    console.log('params', params);
    loading.value = true;
    // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-578】online报表SQL翻译，第二页不翻页数据
    let url = `${configUrl.getColumnsAndData}${unref(cgRpConfigId)}`;
    // update-end--author:liaozhiyang---date:20240603---for：【TV360X-578】online报表SQL翻译，第二页不翻页数据
    //缓存key
    let groupIdKey = props.groupId ? `${props.groupId}${url}${JSON.stringify(params)}` : '';
    httpGroupRequest(() => defHttp.get({ url, params }, { isTransformResponse: false, successMessageMode: 'none' }), groupIdKey).then((res) => {
      // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-578】online报表SQL翻译，第二页不翻页数据
      res.result.dictOptions && initDictOptionData(res.result.dictOptions);
      // update-end--author:liaozhiyang---date:20240603---for：【TV360X-578】online报表SQL翻译，第二页不翻页数据
      loading.value = false;
      // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-578】online报表SQL翻译，第二页不翻页数据
      let data = res.result.data;
      // update-end--author:liaozhiyang---date:20240603---for：【TV360X-578】online报表SQL翻译，第二页不翻页数据
      console.log('表格信息:', data);
      setDataSource(data);
    });
  }

  /**
   * 获取地址栏的参数
   */
  function getUrlParamString() {
   let query = route.query;
   let arr:any[] = []
   if(query && Object.keys(query).length>0){
     Object.keys(query).map(k=>{
       arr.push(`${k}=${query[k]}`)
     })
   }
   return arr.join('&')
  }

  /**
   * 设置dataSource
   */
  function setDataSource(data) {
    if (data) {
      pagination.total = Number(data.total);
      let currentPage = pagination?.current ?? 1;
      for (let a = 0; a < data.records.length; a++) {
        if (!data.records[a].rowIndex) {
          data.records[a].rowIndex = a + (currentPage - 1) * 10;
        }
      }
      dataSource.value = data.records;
      //update-begin-author:taoyan date:2023-2-11 for:issues/356 在线报表分页有问题
      //update-begin-author:liusq date:2023-4-04 for:issues/426 修复356时候引入的回归错误 JPopupOnlReportModal.vue 中未修改
      tableRef?.value && tableRef?.value?.setPagination({
        total: Number(data.total)
      })
      //update-end-author:liusq date:2023-4-04  for:issues/426 修复356时候引入的回归错误 JPopupOnlReportModal.vue 中未修改
      //update-end-author:taoyan date:2023-2-11 for:issues/356 在线报表分页有问题
    } else {
      pagination.total = 0;
      dataSource.value = [];
    }
    // 合计逻辑 [待优化 3.0]
    handleSumColumn(columns.value, pagination.total);
  }

  /**
   * 获取查询参数
   */
  function getQueryParams() {
    let paramTarget = {};
    if (unref(dynamicParam)) {
      //处理自定义参数
      Object.keys(unref(dynamicParam)).map((key) => {
        paramTarget['self_' + key] = unref(dynamicParam)[key];
      });
    }
    let param = Object.assign(paramTarget, unref(queryParam), unref(iSorter));
    param.pageNo = pagination.current;
    // 合计逻辑 [待优化 3.0]
    //  实际查询时不使用table组件的pageSize，而使用自定义的realPageSize,realPageSize会在第一次获取到数据后变化
    param.pageSize = pagination.realPageSize;
    return filterObj(param);
  }

  /**
   * 处理动态参数
   */
  function dynamicParamHandler(arr?) {
    if (arr && arr.length > 0) {
      //第一次加载查询条件前 初始化queryParam为空对象
      let queryTemp = {};
      for (let item of arr) {
        if (item.mode === 'single') {
          queryTemp[item.field] = '';
        }
      }
      queryParam.value = { ...queryTemp };
    }
    // 合并路由参数
    if (props.routeQuery) {
      queryParam.value = Object.assign(queryParam.value, props.routeQuery);
    }

    let dynamicTemp = {};
    if (props.param) {
      Object.keys(props.param).map((key) => {
        let str = props.param[key];
        if (key in queryParam) {
          if (str && str.startsWith("'") && str.endsWith("'")) {
            str = str.substring(1, str.length - 1);
          }
          //如果查询条件包含参数 设置值
          unref(queryParam)[key] = str;
        }
        dynamicTemp[key] = props.param[key];
      });
    }
    dynamicParam.value = { ...dynamicTemp };
  }

  /**
   * 分页
   * @param page
   * @param filters
   * @param sorter
   */
  function handleChangeInTable(page, filters, sorter) {
    console.log(page, filters, sorter);
    //分页、排序、筛选变化时触发
    if (Object.keys(sorter).length > 0) {
      iSorter.value = {
        column: sorter.field,
        order: 'ascend' === sorter.order ? 'asc' : 'desc',
      };
      // 排序字段受控
      unref(columns).forEach((col) => {
        if (col['dataIndex'] === sorter.field) {
          col['sortOrder'] = sorter.order;
        }
      });
    }
    pagination.current = page.current;
    pagination.pageSize = page.pageSize;
    loadData();
  }

  /**
   * 行点击事件
   * @param record
   */
  function clickThenCheck(record) {
    if (clickThenCheckFlag === true) {
      // update-begin--author:liaozhiyang---date:20240104---for：【QQYUN-7514】popup单选显示radio
      if (!props.multi) {
        selectRows.value = [];
        checkedKeys.value = [];
      }
      // update-end--author:liaozhiyang---date:20240104---for：【QQYUN-7514】popup单选显示radio
      let rowKey = combineRowKey(record);
      if (!unref(checkedKeys) || unref(checkedKeys).length == 0) {
        let arr1: any[] = [],
          arr2: any[] = [];
        arr1.push(record);
        arr2.push(rowKey);
        checkedKeys.value = arr2;
        //selectRows.value = arr1;
      } else {
        if (unref(checkedKeys).indexOf(rowKey) < 0) {
          //不存在就选中
          checkedKeys.value.push(rowKey);
          //selectRows.value.push(record);
        } else {
          //已选中就取消
          let rowKey_index = unref(checkedKeys).indexOf(rowKey);
          checkedKeys.value.splice(rowKey_index, 1);
          //selectRows.value.splice(rowKey_index, 1);
        }
      }
      // update-begin--author:liaozhiyang---date:20230914---for：【issues/5357】点击行选中
      tableRef.value.setSelectedRowKeys([...checkedKeys.value]);
      // update-end--author:liaozhiyang---date:20230914---for：【issues/5357】点击行选中
    }
  }

  //防止字典中有垃圾数据
  function initDictOptionData(arr) {
    let obj = {};
    Object.keys(arr).map((k) => {
      obj[k] = arr[k].filter((item) => {
        return item != null;
      });
    });
    dictOptions.value = obj;
  }

  /**
   * 过滤对象中为空的属性
   * @param obj
   * @returns {*}
   */
  function filterObj(obj) {
    if (!(typeof obj == 'object')) {
      return;
    }

    for (let key in obj) {
      if (obj.hasOwnProperty(key) && (obj[key] == null || obj[key] == undefined || obj[key] === '')) {
        delete obj[key];
      }
    }
    return obj;
  }

  // 样式
  const dialogStyle = {
    top: 0,
    left: 0,
    height: '100%',
    margin: 0,
    padding: 0,
  };

  // 弹窗属性配置
  const hrefComponent = ref({
    model: {
      title: '',
      okText: '关闭',
      width: '100%',
      open: false,
      destroyOnClose: true,
      style: dialogStyle,
      // dialogStyle: dialogStyle,
      bodyStyle: {
        padding: '8px',
        height: 'calc(100vh - 108px)',
        overflow: 'auto',
        overflowX: 'hidden',
      },
      // 隐藏掉取消按钮
      cancelButtonProps: { style: { display: 'none' } },
    },
    on: {
      ok: () => (hrefComponent.value.model.open = false),
      cancel: () => (hrefComponent.value.model.open = false),
    },
    is: <any>null,
    params: {},
  });

  // 超链点击事件--> 打开一个modal窗口
  function openHrefCompModal(href) {
    // 解析 href 参数
    let index = href.indexOf('?');
    let path = href;
    if (index !== -1) {
      path = href.substring(0, index);
      let paramString = href.substring(index + 1, href.length);
      let paramArray = paramString.split('&');
      let params = {};
      paramArray.forEach((paramObject) => {
        let paramItem = paramObject.split('=');
        params[paramItem[0]] = paramItem[1];
      });
      hrefComponent.value.params = params;
    } else {
      hrefComponent.value.params = {};
    }
    hrefComponent.value.model.open = true;
    hrefComponent.value.model.title = '操作';
    hrefComponent.value.is = markRaw(defineAsyncComponent(() => importViewsFile(path)));
  }

  //update-begin-author:taoyan date:2022-5-31 for: VUEN-1155 popup 选择数据时，会选择多条重复数据
  /**
   * emit事件 获取选中的行数据
   */
  function getOkSelectRows(): any[] {
    let arr = unref(selectRows);
    let selectedRowKeys = checkedKeys.value;
    console.log('arr', arr);
    if (!selectedRowKeys || selectedRowKeys.length <= 0) {
      return [];
    }
    if (!arr || arr.length <= 0) {
      return [];
    }
    let rows: any = [];
    for (let key of selectedRowKeys) {
      for (let i = 0; i < arr.length; i++) {
        let combineKey = combineRowKey(arr[i]);
        if (key === combineKey) {
          rows.push(toRaw(arr[i]));
          break;
        }
      }
    }
    return rows;
  }
  //update-end-author:taoyan date:2022-5-31 for: VUEN-1155 popup 选择数据时，会选择多条重复数据

  return [
    {
      visibleChange,
      loadColumnsInfo,
      loadColumnsAndData,
      dynamicParamHandler,
      loadData,
      handleChangeInTable,
      combineRowKey,
      clickThenCheck,
      filterUnuseSelect,
      handleExport,
      getOkSelectRows,
    },
    {
      hrefComponent,
      visible,
      rowSelection,
      checkedKeys,
      selectRows,
      pagination,
      dataSource,
      columns,
      indexColumnProps,
      loading,
      title,
      iSorter,
      queryInfo,
      queryParam,
      dictOptions,
    },
  ];
}
