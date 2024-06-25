import { reactive, ref, Ref, unref } from 'vue';
import { merge } from 'lodash-es';
import { DynamicProps } from '/#/utils';
import { BasicTableProps, TableActionType, useTable } from '/@/components/Table';
import { ColEx } from '/@/components/Form/src/types';
import { FormActionType } from '/@/components/Form';
import { useMessage } from '/@/hooks/web/useMessage';
import { useMethods } from '/@/hooks/system/useMethods';
import { useDesign } from '/@/hooks/web/useDesign';
import { filterObj } from '/@/utils/common/compUtils';
const { handleExportXls, handleImportXls } = useMethods();

// 定义 useListPage 方法所需参数
interface ListPageOptions {
  // 样式作用域范围
  designScope?: string;
  // 【必填】表格参数配置
  tableProps: TableProps;
  // 是否分页
  pagination?: boolean;
  // 导出配置
  exportConfig?: {
    url: string | (() => string);
    // 导出文件名
    name?: string | (() => string);
    //导出参数
    params?: object;
  };
  // 导入配置
  importConfig?: {
    //update-begin-author:taoyan date:20220507 for: erp代码生成 子表 导入地址是动态的
    url: string | (() => string);
    //update-end-author:taoyan date:20220507 for: erp代码生成 子表 导入地址是动态的
    // 导出成功后的回调
    success?: (fileInfo?: any) => void;
  };
}

interface IDoRequestOptions {
  // 是否显示确认对话框，默认 true
  confirm?: boolean;
  // 是否自动刷新表格，默认 true
  reload?: boolean;
  // 是否自动清空选择，默认 true
  clearSelection?: boolean;
}

/**
 * listPage页面公共方法
 *
 * @param options
 */
export function useListPage(options: ListPageOptions) {
  const $message = useMessage();
  let $design = {} as ReturnType<typeof useDesign>;
  if (options.designScope) {
    $design = useDesign(options.designScope);
  }

  const tableContext = useListTable(options.tableProps);

  const [, { getForm, reload, setLoading }, { selectedRowKeys }] = tableContext;

  // 导出 excel
  async function onExportXls() {
    //update-begin---author:wangshuai ---date:20220411  for：导出新增自定义参数------------
    let { url, name, params } = options?.exportConfig ?? {};
    let realUrl = typeof url === 'function' ? url() : url;
    if (realUrl) {
      let title = typeof name === 'function' ? name() : name;
      //update-begin-author:taoyan date:20220507 for: erp代码生成 子表 导出报错，原因未知-
      let paramsForm:any = {};
      try {
        paramsForm = await getForm().validate();
      } catch (e) {
        console.error(e);
      }
      //update-end-author:taoyan date:20220507 for: erp代码生成 子表 导出报错，原因未知-
      
      //update-begin-author:liusq date:20230410 for:[/issues/409]导出功能没有按排序结果导出,设置导出默认排序，创建时间倒序
      if(!paramsForm?.column){
         Object.assign(paramsForm,{column:'createTime',order:'desc'});
      }
      //update-begin-author:liusq date:20230410 for: [/issues/409]导出功能没有按排序结果导出,设置导出默认排序，创建时间倒序
      
      //如果参数不为空，则整合到一起
      //update-begin-author:taoyan date:20220507 for: erp代码生成 子表 导出动态设置mainId
      if (params) {
        Object.keys(params).map((k) => {
          let temp = (params as object)[k];
          if (temp) {
            paramsForm[k] = unref(temp);
          }
        });
      }
      //update-end-author:taoyan date:20220507 for: erp代码生成 子表 导出动态设置mainId
      if (selectedRowKeys.value && selectedRowKeys.value.length > 0) {
        paramsForm['selections'] = selectedRowKeys.value.join(',');
      }
      console.log()
      return handleExportXls(title as string, realUrl, filterObj(paramsForm));
      //update-end---author:wangshuai ---date:20220411  for：导出新增自定义参数--------------
    } else {
      $message.createMessage.warn('没有传递 exportConfig.url 参数');
      return Promise.reject();
    }
  }

  // 导入 excel
  function onImportXls(file) {
    let { url, success } = options?.importConfig ?? {};
    //update-begin-author:taoyan date:20220507 for: erp代码生成 子表 导入地址是动态的
    let realUrl = typeof url === 'function' ? url() : url;
    if (realUrl) {
      return handleImportXls(file, realUrl, success || reload);
      //update-end-author:taoyan date:20220507 for: erp代码生成 子表 导入地址是动态的
    } else {
      $message.createMessage.warn('没有传递 importConfig.url 参数');
      return Promise.reject();
    }
  }

  /**
   * 通用请求处理方法，可自动刷新表格，自动清空选择
   * @param api 请求api
   * @param options 是否显示确认框
   */
  function doRequest(api: () => Promise<any>, options?: IDoRequestOptions) {
    return new Promise((resolve, reject) => {
      const execute = async () => {
        try {
          setLoading(true);
          const res = await api();
          if (options?.reload ?? true) {
            reload();
          }
          if (options?.clearSelection ?? true) {
            selectedRowKeys.value = [];
          }
          resolve(res);
        } catch (e) {
          reject(e);
        } finally {
          setLoading(false);
        }
      };
      if (options?.confirm ?? true) {
        $message.createConfirm({
          iconType: 'warning',
          title: '删除',
          content: '确定要删除吗？',
          onOk: () => execute(),
          onCancel: () => reject(),
        });
      } else {
        execute();
      }
    });
  }

  /** 执行单个删除操作 */
  function doDeleteRecord(api: () => Promise<any>) {
    return doRequest(api, { confirm: false, clearSelection: false });
  }

  return {
    ...$design,
    ...$message,
    onExportXls,
    onImportXls,
    doRequest,
    doDeleteRecord,
    tableContext,
  };
}

// 定义表格所需参数
type TableProps = Partial<DynamicProps<BasicTableProps>>;
type UseTableMethod = TableActionType & {
  getForm: () => FormActionType;
};

/**
 * useListTable 列表页面标准表格参数
 *
 * @param tableProps 表格参数
 */
export function useListTable(tableProps: TableProps): [
  (instance: TableActionType, formInstance: UseTableMethod) => void,
  TableActionType & {
    getForm: () => FormActionType;
  },
  {
    rowSelection: any;
    selectedRows: Ref<Recordable[]>;
    selectedRowKeys: Ref<any[]>;
  }
] {
  // 自适应列配置
  const adaptiveColProps: Partial<ColEx> = {
    xs: 24, // <576px
    sm: 12, // ≥576px
    md: 12, // ≥768px
    lg: 8, // ≥992px
    xl: 8, // ≥1200px
    xxl: 6, // ≥1600px
  };
  const defaultTableProps: TableProps = {
    rowKey: 'id',
    // 使用查询条件区域
    useSearchForm: true,
    // 查询条件区域配置
    formConfig: {
      // 紧凑模式
      compact: true,
      // label默认宽度
      // labelWidth: 120,
      // 按下回车后自动提交
      autoSubmitOnEnter: true,
      // 默认 row 配置
      rowProps: { gutter: 8 },
      // 默认 col 配置
      baseColProps: {
        ...adaptiveColProps,
      },
      labelCol: {
        xs: 24,
        sm: 8,
        md: 6,
        lg: 8,
        xl: 6,
        xxl: 6,
      },
      wrapperCol: {},
      // 是否显示 展开/收起 按钮
      showAdvancedButton: true,
      // 超过指定列数默认折叠
      autoAdvancedCol: 3,
      // 操作按钮配置
      actionColOptions: {
        ...adaptiveColProps,
        style: { textAlign: 'left' },
      },
    },
    // 斑马纹
    striped: false,
    // 是否可以自适应高度
    canResize: true,
    // 表格最小高度
    // update-begin--author:liaozhiyang---date:20240603---for【TV360X-861】列表查询区域不可往上滚动
    minHeight: 300,
    // update-end--author:liaozhiyang---date:20240603---for【TV360X-861】列表查询区域不可往上滚动
    // 点击行选中
    clickToRowSelect: false,
    // 是否显示边框
    bordered: true,
    // 是否显示序号列
    showIndexColumn: false,
    // 显示表格设置
    showTableSetting: true,
    // 表格全屏设置
    tableSetting: {
      fullScreen: false,
    },
    // 是否显示操作列
    showActionColumn: true,
    // 操作列
    actionColumn: {
      width: 120,
      title: '操作',
      //是否锁定操作列取值 right ,left,false
      fixed: false,
      dataIndex: 'action',
      slots: { customRender: 'action' },
    },
  };
  // 合并用户个性化配置
  if (tableProps) {
    //update-begin---author:wangshuai---date:2024-04-28---for:【issues/6180】前端代码配置表变查询条件显示列不生效---
    if(tableProps.formConfig){
      setTableProps(tableProps.formConfig);
    }
    //update-end---author:wangshuai---date:2024-04-28---for:【issues/6180】前端代码配置表变查询条件显示列不生效---
    // merge 方法可深度合并对象
    merge(defaultTableProps, tableProps);
  }

  // 发送请求之前调用的方法
  function beforeFetch(params) {
    // 默认以 createTime 降序排序
    return Object.assign({ column: 'createTime', order: 'desc' }, params);
  }

  // 合并方法
  Object.assign(defaultTableProps, { beforeFetch });
  if (typeof tableProps.beforeFetch === 'function') {
    defaultTableProps.beforeFetch = function (params) {
      params = beforeFetch(params);
      // @ts-ignore
      tableProps.beforeFetch(params);
      return params;
    };
  }

  // 当前选择的行
  const selectedRowKeys = ref<any[]>([]);
  // 选择的行记录
  const selectedRows = ref<Recordable[]>([]);

  // 表格选择列配置
  const rowSelection: any = tableProps?.rowSelection ?? {};
  const defaultRowSelection = reactive({
    ...rowSelection,
    type: rowSelection.type ?? 'checkbox',
    // 选择列宽度，默认 50
    columnWidth: rowSelection.columnWidth ?? 50,
    selectedRows: selectedRows,
    selectedRowKeys: selectedRowKeys,
    onChange(...args) {
      selectedRowKeys.value = args[0];
      selectedRows.value = args[1];
      if (typeof rowSelection.onChange === 'function') {
        rowSelection.onChange(...args);
      }
    },
  });
  delete defaultTableProps.rowSelection;

  /**
   * 设置表格参数
   *
   * @param formConfig
   */
  function setTableProps(formConfig: any) {
    const replaceAttributeArray: string[] = ['baseColProps','labelCol'];
    for (let item of replaceAttributeArray) {
      if(formConfig && formConfig[item]){
        if(defaultTableProps.formConfig){
          let defaultFormConfig:any = defaultTableProps.formConfig;
          defaultFormConfig[item] = formConfig[item];
        }
        formConfig[item] = {};
      }
    }
  }

  return [
    ...useTable(defaultTableProps),
    {
      selectedRows,
      selectedRowKeys,
      rowSelection: defaultRowSelection,
    },
  ];
}
