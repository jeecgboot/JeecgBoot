import { h, ref, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { Input, Radio, RadioGroup } from 'ant-design-vue';
import { ActionItem, BasicColumn, FormSchema } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useDrawer } from '/@/components/Drawer';
import { CgformPageType } from '../types';
import { useListPage } from '/@/hooks/system/useListPage';
import {
  doBatchDelete,
  doBatchRemove,
  doSingleDelete,
  doSingleRemove,
  doCopyOnlineView,
  doDatabaseSync,
  doCopyTable,
  list,
  queryHeadById,
} from '../cgform.api';
// import { useCopyModal } from './useCopyCgformModal';
import { isArray } from '/@/utils/is';
import {useCgformStore} from "../store/cgformState";
import {showListDeleteConfirm} from "./useCgformWidgets.jsx";
import { getTenantId, getToken } from '/@/utils/auth';
import { useGlobSetting } from '/@/hooks/setting';

interface IOptions {
  // 页面类型
  pageType: CgformPageType;
  designScope: string;
  columns: BasicColumn[];
  formSchemas: FormSchema[];
}

export function useCgformList(options: IOptions) {
  const isNormalPage = options.pageType === CgformPageType.normal;
  const router = useRouter();
  const glob = useGlobSetting();
  const cgformStore = useCgformStore();
  const tableRef = ref();
  // 列表页面公共参数、方法
  const pageContext = useListPage({
    designScope: options.designScope,
    tableProps: {
      api: list,
      columns: options.columns,
      formConfig: {
        //labelWidth: 200,
        schemas: options.formSchemas,
      },
      beforeFetch: (params) => {
        let copyType = isNormalPage ? 0 : 1;
        let physicId = isNormalPage ? undefined : router.currentRoute.value.params.code;
        // TODO 等字典组件支持逗号分割后删除改代码【LOWCOD-2371】
        if (isArray(params.tableType_MultiString)) {
          params.tableType_MultiString = params.tableType_MultiString.join(',');
        }
        return Object.assign(params, { copyType, physicId });
      },
    },
  });
  const { tableContext, createMessage: $message, createConfirm: $confirm } = pageContext;
  // 注册table数据
  const [, { reload, setLoading }, { selectedRowKeys, selectedRows }] = tableContext;

  // 注册编辑弹窗
  const [registerCgformModal, cgformModal] = useModal();
  // 注册从数据库导入表单
  const [registerDbToOnlineModal, dbToOnlineModal] = useModal();
  // 注册ai建表弹窗
  const [registerAiToOnlineModal, aiToOnlineModal] = useModal();
  // 注册代码生成弹窗
  const [registerCodeGeneratorModal, codeGeneratorModal] = useModal();
  // 注册自定义按钮弹窗
  const [registerCustomButtonModal, customButtonModal] = useModal();
  // 注册JS增强弹窗
  const [registerEnhanceJsModal, enhanceJsModal] = useModal();
  // 注册SQL增强弹窗
  const [registerEnhanceSqlModal, enhanceSqlModal] = useModal();
  // 注册Java增强弹窗
  const [registerEnhanceJavaModal, enhanceJavaModal] = useModal();
  // 注册权限管理抽屉
  const [registerAuthManagerDrawer, authManagerDrawer] = useDrawer();
  // 注册角色授权弹窗
  const [registerAuthSetterModal, authSetterModal] = useModal();

  function onAdd() {
    cgformModal.openModal(true, { isUpdate: false });
  }
  function onAiCreateTable(){
    aiToOnlineModal.openModal(true);
  }
  function  onCreateAiTable() {
    reload();
  }
  let thatRecord: Nullable<Recordable> = null

  async function onEdit(record) {
    thatRecord = record
    // update-begin---author:liusq---date:20261013---for:【LHZP-1967】打印设计生成后表单扩展配置未带出报表地址
    // 列表行数据可能已过期（打印设计会在后台回写 extConfigJson 中的报表地址），报表地址为空时重新查询一次最新配置，
    // 避免弹窗显示旧值，且保存时用旧的 extConfigJson 覆盖掉后台已写入的报表地址
    let latest = record;
    if (!getReportPrintUrl(record)) {
      try {
        const head = await queryHeadById(record.id);
        if (head && head.id) {
          latest = { ...record, ...head };
        }
      } catch (e) {
        console.warn('查询表单最新配置失败，使用列表数据', e);
      }
    }
    cgformModal.openModal(true, { isUpdate: true, record: latest });
    // update-end---author:liusq---date:20261013---for:【LHZP-1967】打印设计生成后表单扩展配置未带出报表地址
  }

  /**
   * 获取扩展配置中的报表地址
   */
  function getReportPrintUrl(record) {
    try {
      return JSON.parse(record.extConfigJson || '{}')?.reportPrintUrl;
    } catch (e) {
      return '';
    }
  }

  function onSuccess() {
    if (thatRecord?.id) {
      cgformStore.addChangedTable(thatRecord.id)
      thatRecord = null
    }
    reload();
  }

  /**
   * 删除事件
   */
  async function handleDelete(id) {
    await doSingleDelete(id);
    reload();
  }

  /**
   * 移除事件
   */
  async function handleRemove(id) {
    await doSingleRemove(id);
    reload();
  }

  /**
   * 删除单条数据
   * @param record
   */
  function onDeleteRecord(record: Recordable) {
    return showListDeleteConfirm(() => handleDelete(record.id), () => handleRemove(record.id))
  }

  // 批量删除
  function onDeleteBatch() {
    let idList = selectedRowKeys.value as string[];
    if (idList.length <= 0) {
      $message.warning('请先选择一条记录！');
      return;
    }
    showListDeleteConfirm(
      () => executeDelete(doBatchDelete, idList, true),
      () => executeDelete(doBatchRemove, idList, true)
    )
  }

  /**
   * 执行删除操作
   * @param fn 删除方法
   * @param idList 删除参数
   * @param clearSelected 清空选择
   */
  async function executeDelete(fn: Fn, idList: string[], clearSelected = false) {
    try {
      setLoading(true);
      const res = await fn(idList);
      reload();
      if (clearSelected) {
        selectedRowKeys.value = [];
      }
      return res;
    } finally {
      setLoading(false);
    }
    return Promise.reject();
  }

  // 显示自定义按钮弹窗
  function onShowCustomButton() {
    getSelectedRows(([row]) => customButtonModal.openModal(true, { row }));
  }

  // 显示 js 增强弹窗
  function onShowEnhanceJs() {
    getSelectedRows(([row]) => enhanceJsModal.openModal(true, { row }));
  }

  // 显示 sql 增强弹窗
  function onShowEnhanceSql() {
    getSelectedRows(([row]) => enhanceSqlModal.openModal(true, { row }));
  }

  // 显示 java 增强弹窗
  function onShowEnhanceJava() {
    getSelectedRows(([row]) => enhanceJavaModal.openModal(true, { row }));
  }

  // 显示导入数据库表弹窗
  function onImportDbTable() {
    dbToOnlineModal.openModal(true, {});
  }

  function getSelectedRows(fn: Fn, min = 1, max = 1) {
    if (selectedRows.value.length < min) {
      $message.warning(`请先至少选中 ${min} 条记录`);
    } else if (selectedRows.value.length > max) {
      $message.warning(`最多只能选中 ${min} 条记录`);
    } else {
      fn(selectedRows.value);
    }
  }

  // 显示代码生成弹窗
  function onGenerateCode() {
    if (selectedRows.value.length === 0) {
      $message.warning('请先选中一条记录');
    } else if (selectedRows.value.length > 1) {
      $message.warning('代码生成只能选中一条记录');
    } else {
      let row = selectedRows.value[0];
      if (!row) {
        $message.warning('请选中当前页的数据！');
      } else if (row.isDbSynch != 'Y') {
        $message.warning('请先同步数据库！');
      } else if (row.tableType == 3) {
        $message.warning('请选中该表对应的主表');
      } else {
        codeGeneratorModal.openModal(true, { code: row.id });
      }
    }
  }

  // 功能测试
  function onGoToTest(record) {
    console.log(record);
    if (record.isTree == 'Y') {
      router.push({ path: '/online/cgformTreeList/' + record.id });
    } else {
      // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
      switch (record.themeTemplate) {
        case 'erp':
          router.push({ path: '/online/cgformErpList/' + record.id });
          break;
        case 'tab':
          router.push({ path: '/online/cgformTabList/' + record.id });
          break;
        case 'innerTable':
          router.push({ path: '/online/cgformInnerTableList/' + record.id });
          break;
        default:
          router.push({ path: '/online/cgformList/' + record.id });
          break;
      }
      // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
    }
  }

  // update-begin---author:wangshuai---date:20260831---for:【LHZP-1967】Online表单生成积木报表打印模板
  /**
   * 打开Online表单对应的积木报表设计页面。
   */
  function onPrintTemplate(record, regeneration = false) {
    const openTemplate = () => {
      const params = new URLSearchParams({
        onlineId: record.id,
        token: getToken() || '',
        regeneration: String(regeneration),
        tenantId: getTenantId() || '',
      });
      window.open(`${glob.domainUrl}/jmreport/generateTemplateByOnline?${params.toString()}`, '_blank');
    };
    if (!regeneration) {
      openTemplate();
      return;
    }
    $confirm({
      title: '重新生成打印报表',
      content: '重新生成后报表样式将会重置，是否继续？',
      iconType: 'warning',
      onOk: openTemplate,
    });
  }
  // update-end---author:wangshuai---date:20260831---for:【LHZP-1967】Online表单生成积木报表打印模板

  // 同步数据库
  function onSyncDatabase(record) {
    const syncMethod = ref('normal');
    const disabled = ref(false);
    const modalFunc = $confirm({
      iconType: 'info',
      title: '同步数据库',
      content: () =>
        h(
          'div',
          {
            style: 'margin: 20px 0;',
          },
          h(
            RadioGroup,
            {
              value: syncMethod.value,
              disabled: disabled.value,
              'onUpdate:value': (v) => (syncMethod.value = v),
            },
            () => [h(Radio, { value: 'normal' }, () => '普通同步（保留表数据）'), h(Radio, { value: 'force' }, () => '强制同步（删除表，重新生成）')]
          )
        ),
      maskClosable: true,
      okText: '开始同步',
      async onOk() {
        disabled.value = true;
        modalFunc.update({
          maskClosable: false,
          keyboard: false,
          okText: '同步中…',
          okButtonProps: { loading: disabled.value },
          cancelButtonProps: { disabled: disabled.value },
        });
        try {
          await doDatabaseSync(record.id, syncMethod.value);
        } catch (e: any) {
          // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-244】同步数据库没权限时提示两次
          // $message.warn(e.message || e);
          // update-end--author:liaozhiyang---date:20240521---for：【TV360X-244】同步数据库没权限时提示两次
        } finally {
          await reload();
          // update-begin--author:liaozhiyang---date:20250318---for：【issues/7931】勾选后再同步数据库，再点击代码生成无法生成代码
          nextTick(() => {
            if (selectedRows.value.length) {
              selectedRows.value.forEach((item) => {
                const dataSource = tableRef.value.getDataSource() ?? [];
                const findItem = dataSource.find((o) => o['id'] === item['id']);
                if (findItem) {
                  Object.assign(item, findItem);
                }
              });
            }
          });
          // update-end--author:liaozhiyang---date:20250318---for：【issues/7931】勾选后再同步数据库，再点击代码生成无法生成代码
        }
      },
    });
  }

  // const { createCopyModal } = useCopyModal();
  const [registerAddressModal, addressModal] = useModal();

  // 显示online地址弹窗
  function onShowOnlineUrl(record) {
    let onlineUrl: string;
    if (record.themeTemplate === 'erp') {
      onlineUrl = `/online/cgformErpList/${record.id}`;
    } else if (record.themeTemplate === 'innerTable') {
      onlineUrl = `/online/cgformInnerTableList/${record.id}`;
    } else if (record.themeTemplate === 'tab') {
      onlineUrl = `/online/cgformTabList/${record.id}`;
    } else if (record.isTree == 'Y') {
      onlineUrl = `/online/cgformTreeList/${record.id}`;
    } else {
      onlineUrl = `/online/cgformList/${record.id}`;
    }
    addressModal.openModal(true, {
      title: `菜单链接【${record.tableTxt}】`,
      content: onlineUrl,
      copyText: onlineUrl,
      copyTitle: `${record.tableTxt}`,
      record,
    });
  }

  /**
   * 显示复制表弹窗
   * @param record
   */
  function onCopyTable(record) {
    const tableName = ref(record.tableName + '_copy');
    $confirm({
      title: '复制表',
      content: () =>
        h(
          'div',
          {
            style: 'margin: 20px 0;',
          },
          [
            '请输入新表名：',
            h(Input, {
              value: tableName.value,
              'onUpdate:value': (v) => (tableName.value = v),
            }),
          ]
        ),
      iconType: 'info',
      closable: true,
      okText: '复制',
      onOk() {
        if (!tableName.value) {
          $message.warning('请输入新表名');
        //update-begin---wangshuai---date:20260713  for：【LHZP-465】点击复制之后，列表需要手动刷新一下才能看到数据------------
            return Promise.reject(); // 阻止 modal 关闭
        }
        if (tableName.value === record.tableName) {
          $message.warning('新表名和旧表名不能一致');
          return Promise.reject(); // 阻止 modal 关闭
        }
        return new Promise((resolve, reject) => {
          doCopyTable(record.id, tableName.value)
            .then(() => {
              $message.success('复制表成功');
              // 强制跳到第 1 页再刷新：默认排序是 createTime desc，新复制的表落在第 1 页，
              // 如果用户当前在第 N 页，普通 reload() 会沿用 current，导致新数据看不到。
              reload({ page: 1 });
              resolve(true);
            })
            .catch((err) => {
              // 失败时把 promise reject，让 modal 不关闭、错误由 defHttp 的 errorMessageMode:'message' 自动提示
              reject(err);
            });
        });
      //update-end---author:wangshuai ---date:20260713  for：【LHZP-465】点击复制之后，列表需要手动刷新一下才能看到数据------------
      },
    });
  }

  /**
   * 删除视图
   */
  function doDeleteView(record) {
    $confirm({
      title: '删除',
      content: '确定要删除该视图吗？',
      iconType: 'warning',
      closable: true,
      maskClosable: true,
      onOk: () => {
        handleRemove(record.id)
      }
    })
  }

  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: () => onEdit(record),
      },
    ];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record): ActionItem[] {
    return [
      {
        label: '同步数据库',
        onClick: () => onSyncDatabase(record),
        ifShow: () => isNormalPage && record.isDbSynch != 'Y',
      },
      {
        // TODO 功能测试
        label: '功能测试',
        class: ['low-app-hide'],
        onClick: () => onGoToTest(record),
        ifShow: () => (isNormalPage ? record.isDbSynch == 'Y' && record.tableType !== 3 : true),
      },
      {
        label: '配置地址',
        class: ['low-app-hide'],
        onClick: () => onShowOnlineUrl(record),
        ifShow: () => (isNormalPage ? record.isDbSynch == 'Y' && record.tableType !== 3 : true),
      },
      {
        label: '权限控制',
        onClick: () => authManagerDrawer.openDrawer(true, { cgformId: record.id, tableType: record.tableType, isTree: record.isTree }),
      },
      {
        label: '角色授权',
        onClick: () => authSetterModal.openModal(true, { cgformId: record.id }),
      },
      {
        label: '视图管理',
        class: ['low-app-hide'],
        onClick: () => router.push(`/online/copyform/${record.id}`),
        ifShow: () => isNormalPage && record.hascopy == 1,
      },
      {
        label: '生成视图',
        class: ['low-app-hide'],
        // @ts-ignore
        popConfirm: {
          title: '确定生成视图吗？',
          placement: 'left',
          confirm: () => {
            setLoading(true);
            doCopyOnlineView(record.id)
              .then(() => {
                $message.success('已成功生成视图');
              })
              .finally(() => {
                setLoading(false);
                reload();
              });
          },
        },
        ifShow: () => isNormalPage,
      },
      {
        label: '复制表',
        onClick: () => onCopyTable(record),
        ifShow: () => isNormalPage,
      },
      // update-begin---author:wangshuai---date:20260831---for:【LHZP-1967】Online表单生成积木报表打印模板
      {
        label: '打印设计',
        onClick: () => onPrintTemplate(record),
        ifShow: () => isNormalPage && record.isDbSynch == 'Y' && record.tableType !== 3,
      },
      {
        label: '生成打印',
        onClick: () => onPrintTemplate(record, true),
        ifShow: () => isNormalPage && record.isDbSynch == 'Y' && record.tableType !== 3,
      },
      // update-end---author:wangshuai---date:20260831---for:【LHZP-1967】Online表单生成积木报表打印模板
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】online删除提示优化
      {
        label: '删除',
        onClick: () => onDeleteRecord(record),
        ifShow: () => isNormalPage,
      },
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】online删除提示优化
      {
        label: '删除视图',
        onClick: () => doDeleteView(record),
        ifShow: () => !isNormalPage,
      },
    ];
  }

  return {
    router,
    pageContext,
    onAdd,
    onAiCreateTable,
    onSuccess,
    onDeleteBatch,
    onImportDbTable,
    onGenerateCode,
    onShowCustomButton,
    onShowEnhanceJs,
    onShowEnhanceSql,
    onShowEnhanceJava,
    onCreateAiTable,
    getTableAction,
    getDropDownAction,
    registerCustomButtonModal,
    registerEnhanceJsModal,
    registerEnhanceSqlModal,
    registerEnhanceJavaModal,
    registerAuthManagerDrawer,
    registerAuthSetterModal,
    registerCgformModal,
    registerDbToOnlineModal,
    registerCodeGeneratorModal,
    registerAiToOnlineModal,
    registerAddressModal,
    tableRef,
  };
}
