import type { ExtConfigType } from '../../types';
import { Page, SpecialConfig, SETUP, ENHANCEJS } from '../../../cgform/types/onlineRender';
import { useRoute } from 'vue-router';
import { router } from '/@/router';
import { onBeforeUnmount, ref, toRaw, nextTick, provide } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
import { filterObj } from '/@/utils/common/compUtils';
import { useCustomHook, GET_FUN_BODY_REG } from './useCustomHook';
import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
import { useModal } from '/@/components/Modal';
import { ERP } from "../../util/constant";
import {useMultipleTabStore} from "/@/store/modules/multipleTab";
import {useCgformStore} from "../../store/cgformState";
import { getMenus } from '/@/router/menus';
/**
 * context对象属性的描述
 * 控制台写js增强的时候 打印console.log(this)可以获取到该对象说明
 */
const CONTEXT_PROP_DESCRIPTION = {
  acceptHrefParams: '<p> 跳转时获取的参数信息',
  currentPage: '<p> 当前页数',
  currentTableName: '<p> 当前表名',
  description: '<p> 当前表描述',
  hasChildrenField: '<p> 是否有子节点的字段名，仅树形表单下有效',
  isDesForm: '<p> xx',
  isTree: '<m> 是否是树形表单 ',
  loadData: '<m> 加载列表数据',
  pageSize: '<p> 每一页显示条数',
  queryParam: '<p> 查询条件对象，每次点击查询后才会更新此数据',
  selectedRowKeys: '<p> 选中的行的id数组',
  sortField: '<p> 排序字段',
  sortType: '<p> 排序规则',
  total: '<p> 总页数',
  foreignKeyValue: '<p> Erp一对多子表外键选中对应主表字段的值',
  isErpSubTable: '<p> 是否Erp一对多子表',
  foreignKeyField: '<p> Erp一对多子表外键字段',
  themeTemplate: '<p> 主题模板',
  isInnerSubTable: '<p> 是否内嵌一对多子表',
  innerSubTableId: '<p>内嵌一对多子表ID',
  innerSubTableName: '<p> 内嵌一对多子表名',
  mTableSelectedRcordId: '<p>内嵌主表展开行的id',
  innerSubTableFk: '<p>内嵌子表的外键字段',
  loading: '<p> 设置/获取loading',
};

/**
 * online地址-常量
 */
const onlineUrl = {
  getColumns: '/online/cgform/api/getColumns/',
  getQueryInfo: '/online/cgform/api/getQueryInfo/',
  getData: '/online/cgform/api/getData/',
  getTreeData: '/online/cgform/api/getTreeData/',
  optPre: '/online/cgform/api/form/',
  buttonAction: '/online/cgform/api/doButton',
  exportXls: '/online/cgform/api/exportXlsOld/',
  importXls: '/online/cgform/api/importXls/',
  startProcess: '/act/process/extActProcess/startMutilProcess',
  getErpColumns: '/online/cgform/api/getErpColumns/',
  // 内嵌主题一对多子表数据请求接口
  list: '/online/cgform/api/subform/list/',
};

// 没一张表配置的初始值
let config: SpecialConfig = {
  sortField: 'id',
  sortType: 'asc',
  currentPage: 1,
  pageSize: 10,
  total: 0,
  selectedRowKeys: [],
  queryParam: {},
  acceptHrefParams: {},
  description: '',
  currentTableName: '',
  isDesForm: false,
  desFormCode: '',
  cache: false,
  isTree: false,
  hasChildrenField: '',
};

/**
 * 分页配置
 */
const metaPagination = {
  current: 1,
  pageSize: 10,
  pageSizeOptions: ['10', '20', '30'],
  showTotal: (total, range) => {
    return range[0] + '-' + range[1] + ' 共' + total + '条';
  },
  showQuickJumper: true,
  showSizeChanger: true,
  total: 0,
};

/**
 * 获取online 列表上下文
 * 1.常量，全局使用的- 请求url
 * 2.特殊参数-查询条件，排序方式，分页信息，选中行的keys（待测试，是否只和key有关，如果和row有关需去掉此配置）
 * 3.loadData方法，这个方法很多地方调用
 * setup最开头执行一次即可
 */
const { createMessage: $message, createErrorModal } = useMessage();

export function useOnlineTableContext(params: any = {}) {
  console.log('-------------------------useOnlineTableContext----------------------->');
  // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
  const tableId = params.code ?? '';
  const ID = ref(tableId);
  provide('tableId', ID);
  // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
  const route = useRoute();
  // 列表页面查询表单的ref
  const onlineQueryFormOuter = ref<any>();
  // 高级查询按钮
  const superQueryButtonRef = ref<any>();
  // 分页配置
  const pagination = ref<Page | false>(false);
  // table 数据 切换路由即发生数据改变 正常情况下会走一遍setup 缓存情况下 走事件onActivated----问题是不用重新请求column吗？ 只需要加载数据？ 待测试
  const dataSource = ref<Array<object>>([]);
  // 当前列表数据是否由查询条件（普通查询或高级查询）查出
  const hasQueryCondition = ref(false);
  // 表格是否重载
  const tableReloading = ref(true);
  // online表单扩展配置
  const onlineExtConfigJson = ref<ExtConfigType>();
  // Online表单全局状态
  const cgformStore = useCgformStore();
  // 多Tab状态
  const tabStore = useMultipleTabStore();

  const isConfigCurRoute = ref(false);
  const pageLoading = ref(false);
  
  let specialConfigMap: { [key: string | symbol ]: SpecialConfig } = {};
  const methods = {
    execButtonEnhance: function (code, record) {
      if (onlineTableContext[ENHANCEJS][code]) {
        if (SETUP === code) {
          executeEnhanceJsHook(code);
        } else {
          let row = toRaw(record);
          return onlineTableContext[ENHANCEJS][code].call(onlineTableContext, onlineTableContext, row);
        }
      } else if (onlineTableContext[ENHANCEJS][code + '_hook']) {

        //update-begin-author:taoyan date:2023-5-15 for: issues/516 自定义按钮_hook后的参数row未定义问题（参见#410） #516
        if(record){
          let row = toRaw(record);
          executeEnhanceJsHook(code + '_hook', row);
        }else{
          executeEnhanceJsHook(code + '_hook');
        }
        //update-end-author:taoyan date:2023-5-15 for: issues/516 自定义按钮_hook后的参数row未定义问题（参见#410） #516
        
      } else {
        console.log('增强没找到!', code);
      }
    },
    /**
     * get 是否是树形表单
     * @param status 如果有值 则视为set方法
     */
    isTree: function (status?) {
      if (typeof status === 'boolean') {
        //传了参数则设置值
        onlineTableContext['isTreeTable'] = status;
        return status;
      } else {
        return onlineTableContext['isTreeTable'];
      }
    },
  };

  function executeEnhanceJsHook(code, row?) {
    let str = onlineTableContext[ENHANCEJS][code].toLocaleString();
    let arr = str.match(GET_FUN_BODY_REG);
    if (arr.length > 1) {
      let temp = arr[1];
      executeJsEnhanced(temp, row);
    }
  }
  /**
   * 定义数据代理 取值方便 onlineTableContext.queryParam
   * 直接读取onlineTableContext 取到的是{}
   */
  const onlineTableContext: any = new Proxy(CONTEXT_PROP_DESCRIPTION, {
    get(_target: any, prop: string): any {
      //console.log('从SpecialConfig中读取属性:'+prop)
      if (typeof methods[prop] === 'function') {
        return methods[prop];
      } else {
        let temp = specialConfigMap[ID.value];
        if (temp == null) {
          return temp;
        }
        return Reflect.get(temp, prop);
      }
    },
    set(_target: any, prop: string, value: any): boolean {
      //  console.log('设置SpecialConfig属性：'+ prop, value)
      let temp = getCurrentPageSpecialConfigMap();
      if (typeof value === 'function') {
        // 如果是函数放到methods中去
        return Reflect.set(methods, prop, value);
      } else {
        return Reflect.set(temp, prop, value);
      }
    },
    deleteProperty(_target, key) {
      // 在路由切换、关闭页面的时候需要调用一下这个方法清除配置
      if (key === ID.value) {
        delete specialConfigMap[key];
        return true;
      } else {
        return false;
      }
    },
  });

  // 新的js增强
  const { executeJsEnhanced } = useCustomHook({}, onlineTableContext);

  /**
   * 获取路由地址上的表单ID
   */
  function getTableId() {
    let idValue = route.params.id as string;
    if (!idValue) {
      idValue = '';
    }
    return idValue;
  }

  onMountedOrActivated(({type}) => {
    // 缓存路由走Activated，没缓存的走Mounted，均需走一次
    console.log('-------------------onMountedOrActivated-------------------');
    // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
    //erp一对多子表的id不能从页面路由获取（当tableId存在时，不从页面路由获取）
    !tableId && handlePageChange();
    // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格

    if (type === 'activated') {
      // 【QQYUN-7151】表单修改后，已经打开的功能测试页面不会自动刷新
      if (cgformStore.checkIsChanged(ID.value)) {
        tabStore.refreshPage(router)
      }
    }

    if (ID.value) {
      cgformStore.removeChangedTable(ID.value);
    }

  });

  // 路由关闭前 清空map里面的配置
  onBeforeUnmount(() => {
    console.log('-------------------onBeforeUnmount-------------------');
    delete specialConfigMap[ID.value];
    // 如果缓存了  关闭时会调用
    // 没有缓存 切换路由就会调用--这个没关系
    // 但是测试online无此效果
  });

  /**
   * 获取当前页面配置
   */
  function getCurrentPageSpecialConfigMap() {
    let temp = specialConfigMap[ID.value];
    if (!temp) {
      let obj = Object.assign({}, config, { onlineUrl });
      temp = JSON.parse(JSON.stringify(obj));
      // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      if (params['themeTemplate'] == ERP) {
        // update-begin--author:liaozhiyang---date:20240306---for：【QQYUN-8387】Erp风格和其他风格同时在，导致其他风格当前页码不正常
        temp.pageSize = 5;
        // update-end--author:liaozhiyang---date:20240306---for：【QQYUN-8387】Erp风格和其他风格同时在，导致其他风格当前页码不正常
      }
      // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      // update-begin--author:liaozhiyang---date:20250423---for：【issues/8117】js增强可设置获取loading
      // @ts-ignore
      temp.loading = pageLoading;
      // update-end--author:liaozhiyang---date:20250423---for：【issues/8117】js增强可设置获取loading
      specialConfigMap[ID.value] = temp;
    }
    return temp;
  }

  //接受URL参数
  function handleAcceptHrefParams() {
    let acceptHrefParams = {};
    let hrefParam = route.query;
    if (hrefParam) {
      Object.keys(hrefParam).map((key) => {
        acceptHrefParams[key] = hrefParam[key];
      });
      // queryParam.value raw对象
      onlineTableContext['acceptHrefParams'] = acceptHrefParams;
    }
  }

  /**
   * 查询table列信息 及其他配置
   */
  function getColumnList(themeTemplate = '') {
    let url;
    // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
    if (themeTemplate == ERP) {
      // Erp一对多获取主子表columns
      url = `${onlineTableContext.onlineUrl.getErpColumns}${ID.value}`;
    } else {
      url = `${onlineTableContext.onlineUrl.getColumns}${ID.value}`;
    }
    // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格

    //update-begin---author:wangshuai---date:2025-10-21---for:【issues/8933】内嵌子表主题（一对多）列表点+号展开明细提示：无权限访问(操作)---
    if(onlineTableContext['isInnerSubTable'] === true){
      url = url+ '?tabletype=3';
    }
    //update-end---author:wangshuai---date:2025-10-21---for:【issues/8933】内嵌子表主题（一对多）列表点+号展开明细提示：无权限访问(操作)---
    return new Promise((resolve, reject) => {
      defHttp
        .get(
          {
            url,
          },
          { isTransformResponse: false }
        )
        .then((res) => {
          // console.log(res)
          if (res.success) {
            resolve(res.result);
          } else {
            $message.warning(res.message);
            reject();
          }
        })
        .catch(() => {
          reject();
        });
    });
  }

  //查询数据
  /**
   * @param delNum number 删除的条数【批量删除，删除】调用会传
   */
  function loadData(options = {}) {
    const { delNum } = options;
    return new Promise((resolve, reject) => {
      // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-206】列表删除最后一页数据，页面跳到前一页数据为空
      if (delNum != null) {
        const { total, pageSize, current } = pagination.value;
        const lastPage = Math.ceil(total / pageSize);
        // 只有当前页是最后一页时删除数据才判断是否要跳到前一页
        if (current === lastPage) {
          pagination.value.current = Math.ceil((total - delNum) / pageSize);
        }
      }
      // update-end--author:liaozhiyang---date:20240528---for：【TV360X-206】列表删除最后一页数据，页面跳到前一页数据为空
      let params = getLoadDataParams();
      let url = `${onlineTableContext.onlineUrl.getData}${ID.value}`;
      if (onlineTableContext.isTree() === true) {
        url = `${onlineTableContext.onlineUrl.getTreeData}${ID.value}`;
      } else if (onlineTableContext['isInnerSubTable'] === true) {
        // update-begin--author:liaozhiyang---date:20230822---for：【QQYUN-6305】内嵌主题一对多
        url = `${onlineTableContext.onlineUrl.getData}${onlineTableContext['innerSubTableId']}`;
        params = Object.assign(params,{pageSize: -521 });
        // update-begin--author:liaozhiyang---date:20240514---for：【QQYUN-9340】内嵌子表数据都查出来了
        if (onlineTableContext['innerSubTableFk'] && onlineTableContext['mTableSelectedRcordId']) {
          params[onlineTableContext['innerSubTableFk']] = onlineTableContext['mTableSelectedRcordId'];
        }
        // update-end--author:liaozhiyang---date:20240514---for：【QQYUN-9340】内嵌子表数据都查出来了
        // update-end--author:liaozhiyang---date:20230822---for：【QQYUN-6305】内嵌主题一对多
        //update-begin---author:wangshuai---date:2025-10-21---for:【issues/8933】内嵌子表主题（一对多）列表点+号展开明细提示：无权限访问(操作)---
        url = url+ '?tabletype=3';
        //update-end---author:wangshuai---date:2025-10-21---for:【issues/8933】内嵌子表主题（一对多）列表点+号展开明细提示：无权限访问(操作)---
      }
      // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      // erp一对多子表查询需加参数
      if (onlineTableContext['isErpSubTable'] === true) {
        // update-begin--author:liaozhiyang---date:20250722---for：【issues/8575】erp默认选中第一个及没选中主表时子表不查询
        if (onlineTableContext['foreignKeyValue'] == undefined) {
          return;
        }
        // update-end--author:liaozhiyang---date:20250722---for：【issues/8575】erp默认选中第一个及没选中主表时子表不查询
        params[onlineTableContext['foreignKeyField']] = onlineTableContext['foreignKeyValue'];
        // 【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权查看从表的数据
        params['tabletype'] = 3;
        delete params.hasQuery;
      }
      // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      console.log('------查询参数-----', params);
      defHttp
        .get({ url, params }, { isTransformResponse: false })
        .then((res) => {
          console.log('--onlineList-查询列表数据', res);
          if (res.success) {
            handleDataResult(res.result);
            resolve(true);
          } else {
            if (res.message === 'NO_DB_SYNC') {
              createErrorModal({
                title: '数据库未同步',
                content: '请先同步数据库再查看此页面！',
                // 点击确定后自动返回上一页
                onOk: () => router.back(),
              });
            } else {
              $message.warning(res.message);
            }
            reject(false);
          }
        })
        .catch(() => {
          let error = '请求列表数据异常!';
          $message.warning(error);
          reject(false);
        });
    });
  }

  /**
   * 获取查询条件
   */
  function getLoadDataParams() {
    const { sortField, sortType, acceptHrefParams, queryParam } = onlineTableContext;
    // 树用到的参数
    const treeParam = {
      hasQuery: 'true',
    };
    if (onlineTableContext.isTree() === true) {
      // update-begin--author:liaozhiyang---date:20231205---for：【issues/888】online树表子节点搜索不生效且有警告
      // update-begin--author:liusq---date:20260807---for：【树表查询】命中子级数据时返回子级及完整祖先层级
      if (!hasTreeQueryCondition()) {
        treeParam['hasQuery'] = 'false';
      }
      // update-end--author:liusq---date:20260807---for：【树表查询】命中子级数据时返回子级及完整祖先层级
      // update-end--author:liaozhiyang---date:20231205---for：【issues/888】online树表子节点搜索不生效且有警告
    }
    let params = Object.assign({}, treeParam, acceptHrefParams, queryParam, { column: sortField, order: sortType });
    // TODO 范围查询 原固定值需要清楚 待删除
    /*let queryFields = queryFieldArray.value;
    for(let item of queryFields){
      if(item.mode!='single'){
        params[item.field] = ''
      }
    }*/
    if (pagination.value) {
      //如果分页
      params.pageNo = pagination.value.current;
      params.pageSize = pagination.value.pageSize;
    } else {
      // 不分页传一个固定值
      params['pageSize'] = -521;
    }

    let superQueryData = getSuperQueryData();
    //高级查询
    params.superQueryMatchType = superQueryData.matchType || '';
    params.superQueryParams = superQueryData.params || '';
    return filterObj(params);
  }

  /**
   * liaozhiyang
   * 20261026
   * 【QQYUN-11801】生成测试数据按钮区分是否有查询条件（普通查询/高级查询）
   * 当前是否有查询条件（普通查询 或 高级查询）
   */
  function checkHasQueryCondition() {
    const { queryParam, superQuery } = onlineTableContext;
    const isEmpty = (value) => value === undefined || value === null || value === '' || (Array.isArray(value) && value.length === 0);
    // 普通查询：任一查询字段有值
    const hasNormalQuery = !!queryParam && Object.values(queryParam).some((value) => !isEmpty(value));
    // 高级查询：有查询条件项
    const hasSuperQuery = Array.isArray(superQuery?.params) && superQuery.params.length > 0;
    return hasNormalQuery || hasSuperQuery;
  }

  // 查询玩数据后 获取页面数据、数据总数
  function handleDataResult(result) {
    hasQueryCondition.value = checkHasQueryCondition();
    let total = 0;
    if (Number(result.total) > 0) {
      if (onlineTableContext.isTree() === true) {
        dataSource.value = getTreeDataByResult(result.records);
        // update-begin--author:liusq---date:20260807---for：【树表查询】查询时不回填历史展开节点的无关子级数据
        if (hasTreeQueryCondition()) {
          expandedRowKeys.value = [];
        } else {
          nextTick(() => {
            loadDataByExpandedRows(dataSource.value);
          });
        }
        // update-end--author:liusq---date:20260807---for：【树表查询】查询时不回填历史展开节点的无关子级数据
      } else {
        // update-begin--author:liaozhiyang---date:20250508---for：【issues/8168】id重复排序数据重了
        dataSource.value = [];
        nextTick(() => {
          dataSource.value = result.records;
        });
        // update-end--author:liaozhiyang---date:20250508---for：【issues/8168】id重复排序数据重了
      }
      total = Number(result.total);
    } else {
      dataSource.value = [];
    }
    if (pagination.value) {
      pagination.value = { ...pagination.value, total };
    }
  }

  //分页、排序、筛选变化时触发
  function handleChangeInTable($pagination, _filters, sorter) {
    if (sorter && sorter.order) {
      // 需要排序，先获取排序规则
      onlineTableContext['sortField'] = sorter.field;
      onlineTableContext['sortType'] = 'ascend' == sorter.order ? 'asc' : 'desc';
    } else {
      // 没有规则 走默认排序
      onlineTableContext['sortField'] = 'id';
      onlineTableContext['sortType'] = 'asc';
    }
    if (pagination.value) {
      //console.log('$pagination111', $pagination)
      pagination.value = $pagination;
    }
    loadData();
  }

  /**
   * 页面id改变后,执行查询loadData之前会执行该方法
   * 根据查询的结果设置当前表信息、设置查询条件、设置高级查询条件、分页信息、排序信息
   * @param result
   */
  function handleSpecialConfig(result) {
    //1.根据查询的结果设置当前表信息
    onlineTableContext['description'] = result.description;
    onlineTableContext['currentTableName'] = result.currentTableName;
    onlineTableContext['isDesForm'] = result.isDesForm;
    onlineTableContext['desFormCode'] = result.desFormCode;
    onlineTableContext['ID'] = ID.value;
    //2.设置查询条件
    let { acceptHrefParams, queryParam, superQuery, currentPage, pageSize } = onlineTableContext;
    handleAcceptHrefParams();
    if (!queryParam) {
      onlineTableContext['queryParam'] = {};
    } else {
      // 加强判断，防止没有查询
      if (onlineQueryFormOuter.value) {
        onlineQueryFormOuter.value.initDefaultValues(queryParam, acceptHrefParams);
      }
    }
    //3.设置高级查询条件
    if (!superQuery) {
      onlineTableContext['superQuery'] = { params: '', matchType: '' };
    } else {
      // erp一对多子表没有高级查询按钮
      if (superQueryButtonRef.value) {
        superQueryButtonRef.value.initDefaultValues(superQuery);
      }
    }
    //4.分页信息
    if (result.paginationFlag == 'Y') {
      // update-begin--author:liaozhiyang---date:20240527---for：【TV360X-332】erp默认每页5条，切换之后每页5条没了
      let pageSizeOptions: any = metaPagination.pageSizeOptions;
      if (params['themeTemplate'] == ERP) {
        pageSizeOptions = ['5', '10', '30'];
      }
      // update-end--author:liaozhiyang---date:20240527---for：【TV360X-332】erp默认每页5条，切换之后每页5条没了
      pagination.value = { ...metaPagination, ...{ current: currentPage, pageSize, pageSizeOptions } };
    } else {
      pagination.value = false;
    }
    //5.排序信息 不需要设置 没有显示声明，所以缺点是：界面上看不出来哪一列被排序了
  }

  /** 重载表格，在columns等信息变化时需要调用 */
  async function reloadTable() {
    tableReloading.value = true;
    await nextTick();
    tableReloading.value = false;
  }

  const add2Context = {
    loadData,
    getLoadDataParams,
    reloadTable,
  };
  Object.keys(add2Context).map((key) => {
    onlineTableContext[key] = add2Context[key];
  });

  //----------------------------------- 以下为查询相关的--------------------------------

  // 查询加载状态
  let loading = ref(false);
  // 查询首页
  async function reload(parameter:any = {}) {
    if (pagination.value) {
      // update-begin--author:liaozhiyang---date:20231207---for：【QQYUN-7414】online操作除了查询其他数据刷新都是当前页（包括新增）
      pagination.value = { ...pagination.value, current: parameter.mode == 'search' || !pagination.value.current ? 1 : pagination.value.current };
      // update-end--author:liaozhiyang---date:20231207---for：【QQYUN-7414】online操作除了查询其他数据刷新都是当前页（包括新增）
    }
    // update-begin--author:liusq---date:20260713---for：【LHZP-118】搜索时清空主表选中，确保子表同步
    if (parameter.mode === 'search') {
      onlineTableContext.clearSelectedRow();
      // update-end--author:liaozhiyang---date:20260713---for：【LHZP-118】搜索时清空主表选中，确保子表同步
    } else if (params['themeTemplate'] !== ERP) {
      // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
      onlineTableContext.clearSelectedRow();
      // update-end--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
    }
    //loading.value = true
    await loadData();
    //loading.value = false
  }

  //------------------------树形列表--------------------------
  function hasTreeQueryCondition() {
    const { queryParam, superQuery } = onlineTableContext;
    const hasBasicQuery = !!queryParam && Object.values(queryParam).some((value) => value !== undefined && value !== null && value !== '');
    return hasBasicQuery || (Array.isArray(superQuery?.params) && superQuery.params.length > 0);
  }

  function getTreeDataByResult(result) {
    if (result) {
      return result.map((item) => {
        // 查询结果已包含祖先链时保留真实子节点，不能再替换成懒加载占位节点
        if (Array.isArray(item.children) && item.children.length > 0) {
          item.children = getTreeDataByResult(item.children);
          return item;
        }
        //判断是否标记了带有子节点
        let hasChildrenField = onlineTableContext['hasChildrenField'];
        if (item[hasChildrenField] == '1') {
          let loadChild = { id: item.id + '_loadChild', name: 'loading...', isLoading: true };
          loadChild['jeecg_row_key'] = loadChild.id;
          item.children = [loadChild];
        }
        return item;
      });
    }
  }

  const expandedRowKeys = ref<string[]>([]);

  function handleExpandedRowsChange(expandedRowKeysValue) {
    //console.log(a,b)
    //console.log('handleExpandedRowsChange', expandedRowKeysValue, toRaw(expandedRowKeys.value))
    expandedRowKeys.value = expandedRowKeysValue;
  }

  // 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
  function loadDataByExpandedRows(dataList) {
    let expandedRowKeysValue = expandedRowKeys.value;
    if (expandedRowKeysValue.length > 0) {
      const { sortField, sortType, pidField } = onlineTableContext;
      let params = Object.assign({}, { column: sortField, order: sortType });
      params['hasQuery'] = 'in';
      //已展开节点批量查询子节点
      let superParams = Object.assign({});
      superParams.rule = 'in';
      superParams.type = 'text';
      superParams.val = expandedRowKeysValue.join(',');
      superParams.field = pidField;
      superParams = [superParams];
      params['superQueryParams'] = encodeURI(JSON.stringify(superParams));
      params['superQueryMatchType'] = 'and';
      params['batchFlag'] = 'true';
      let url = `${onlineTableContext.onlineUrl.getTreeData}${ID.value}`;
      console.log('--onlineList-查询子节点参数', superParams);
      defHttp
        .get({ url, params }, { isTransformResponse: false })
        .then((res) => {
          console.log('--onlineList-查询子节点列表数据', res);
          if (res.success && res.result.records && res.result.records.length > 0) {
            //已展开的数据批量子节点
            let records = res.result.records;
            const listMap = new Map();
            for (let item of records) {
              let pid = item[pidField];
              if (expandedRowKeysValue.join(',').includes(pid)) {
                let mapList = listMap.get(pid);
                if (mapList == null) {
                  mapList = [];
                }
                mapList.push(item);
                listMap.set(pid, mapList);
              }
            }
            let childrenMap = listMap;
            let fn = (list) => {
              if (list) {
                list.forEach((data) => {
                  if (expandedRowKeysValue.includes(data.id)) {
                    data.children = getTreeDataByResult(childrenMap.get(data.id));
                    fn(data.children);
                  }
                });
              }
            };
            fn(dataList);
          }
        })
        .catch(() => {
          let error = 'loadDataByExpandedRows请求列表数据异常!';
          $message.warning(error);
        });
    } else {
      return Promise.resolve();
    }
  }

  /**
   * 获取高级查询条件
   */
  function getSuperQueryData() {
    if (!onlineTableContext.superQuery) {
      return {};
    }
    const {
      superQuery: { params, matchType },
      currentTableName,
    } = onlineTableContext;
    let pre = currentTableName + '@';
    let arr: any[] = [];
    if (params.length > 0) {
      for (let data of params) {
        let item = { ...data };
        let field = item.field;
        if (field.startsWith(pre)) {
          item.field = field.replace(pre, '');
        }
        arr.push(item);
      }
    }
    let str = arr.length > 0 ? JSON.stringify(arr) : '';
    console.log('高级查询条件', arr, matchType);
    return {
      params: encodeURIComponent(str),
      matchType,
    };
  }

  /**高级查询状态-是否有查询条件*/
  const superQueryStatus = ref(false);

  /**
   * 高级查询对象
   * 1.执行高级查询组件的search事件，需要将值赋值给context
   * 2.查询的时候从context中获取参数值
   * 3.id发生改变需要做点什么？nothing，状态值需要改变
   */
  function handleSuperQuery(params, matchType) {
    // params一定是个数组，可能size为0
    onlineTableContext['superQuery'] = {
      params,
      matchType,
    };
    // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7309】online高级查询第二次按钮没动画
    if (params.length == 0 || params.length == undefined) {
      superQueryStatus.value = false;
    } else {
      superQueryStatus.value = true;
    }
    // update-end--author:liaozhiyang---date:20231128---for：【QQYUN-7309】online高级查询第二次按钮没动画
    // update-begin--author:liaozhiyang---date:20231207---for：【QQYUN-7414】online操作除了查询其他数据刷新都是当前页（包括新增）
    pagination.value.current = 1;
    // update-end--author:liaozhiyang---date:20231207---for：【QQYUN-7414】online操作除了查询其他数据刷新都是当前页（包括新增）
    loadData();
  }

  /*------------------------自定义弹窗------------------------------*/
  const [registerCustomModal, { openModal: doOpenCustomModal }] = useModal();
  /**
   * 自定义按钮 触发弹框
   * @param param
   */
  function openCustomModal(param) {
    if (!param) {
      param = {};
    }
    if (!param.row) {
      let rows = onlineTableContext['selectedRows'];
      if (!rows || rows.length == 0 || rows.length > 1) {
        $message.warning('请选择一条数据');
        return;
      }
      param.row = rows[0];
    }
    param['code'] = ID.value;
    doOpenCustomModal(true, param);
  }
  onlineTableContext['openCustomModal'] = openCustomModal;
  /*------------------------自定义弹窗------------------------------*/

  /**
   * 页面发生改变的时候触发
   */
  function handlePageChange() {
    let idValue = getTableId();
    ID.value = idValue;
  }
  // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
  //erp子表的id不能从页面路由获取（当tableId存在时，不从页面路由获取）
  if (!tableId && !ID.value) {
    handlePageChange()
  }
  // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格

  /** 表单配置查询成功后触发的事件（不用等表单打开才触发） */
  function handleFormConfig(formConfig) {
    // 处理扩展参数
    let extConfigJson = formConfig.head.extConfigJson;
    if (extConfigJson) {
      onlineExtConfigJson.value = JSON.parse(extConfigJson);
    }
  }
  /**
   * liaozhiyang
   * 20250407
   * 【QQYUN-11801】生成测试数据
   *  判断是配置的菜单还是功能测试打开的
   * */
  async function isConfigUrl() {
    const getMatchingisConfigUrl = (menus, path) => {
      for (let i = 0, len = menus.length; i < len; i++) {
        const item = menus[i];
        if (item.path === path && !item.redirect && !item.paramPath) {
          return true;
        } else if (item.children?.length) {
          const result = getMatchingisConfigUrl(item.children, path);
          if (result) {
            return result;
          }
        }
      }
      return false;
    };
    const path = route.path;
    const menus = await getMenus();
    const result = getMatchingisConfigUrl(menus, path);
    isConfigCurRoute.value = result;
  }
  isConfigUrl();
  return {
    ID,
    onlineQueryFormOuter,
    superQueryButtonRef,
    loading,
    reload,
    dataSource,
    hasQueryCondition,
    pagination,
    tableReloading,
    handleSpecialConfig,
    onlineTableContext,
    handleChangeInTable,
    getColumnList,
    getTreeDataByResult,
    expandedRowKeys,
    handleExpandedRowsChange,
    onlineExtConfigJson,
    handleFormConfig,
    superQueryStatus,
    handleSuperQuery,
    registerCustomModal,
    isConfigCurRoute,
    pageLoading,
    ...add2Context,
  };
}

/**
 * 兼容老版js增强 封装对象--暂不支持
 *
 */
export function useCompatibleOldVersion(context) {
  Object.defineProperty(context, 'table', {
    get() {
      const arr = context['selectedRowKeys'];
      const arr2 = context['selectedRows'];
      return {
        selectedRowKeys: arr,
        selectedRows: arr2,
      };
    },
  });
}

/**
 * 链式调用？？
 */
export class AopSetup {
  before: Function = () => {};
  after: Function = () => {};

  constructor(before, after) {
    if (typeof before == 'function') {
      this.before = before;
    }
    if (typeof after == 'function') {
      this.after = after;
    }
  }

  addTarget(prop, context) {
    let key;
    if (typeof prop == 'function') {
      key = prop.name;
    } else if (typeof prop == 'string') {
      key = prop;
    } else {
      return;
    }
    if (typeof context == 'object') {
      context[key] = this.around(context[key], context);
    }
  }
  addTargets(array, context) {
    for (let item of array) {
      this.addTarget(item, context);
    }
  }

  around(targetFunction, context) {
    const _that = this;
    return async function () {
      //console.log('this2', this)
      let res1 = await _that.before(arguments);
      console.log('before返回值', res1);
      if (res1) {
        console.log('错误信息', res1);
        return;
      }
      let result = await targetFunction.apply(context, arguments);
      await _that.after(arguments);
      return result;
    };
  }
}
