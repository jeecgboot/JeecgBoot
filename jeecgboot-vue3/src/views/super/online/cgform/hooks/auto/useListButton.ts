import type { Ref } from 'vue';
import type { ExtConfigType } from '../../types';
import { computed, reactive, toRaw, ref } from 'vue';
import { CgFormButton } from '../../types/onlineRender';
import { pick } from 'lodash-es';
import { useModal } from '/@/components/Modal';
import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
import { Modal } from 'ant-design-vue';
import { filterObj } from '/@/utils/common/compUtils';
import { useMethods } from '/@/hooks/system/useMethods';
import { getToken } from '/@/utils/auth';
import { goJmReportViewPage } from '/@/utils'
import { Tree } from '../../util/constant';

/**工作流编码前缀*/
const FLOW_CODE_PRE = 'onl_';
/**
 * 负责列表页面按钮及其事件--在initAutoList之后执行,必须先获取按钮信息
 * - 这个是所有风格都通用的且必须要有的
 */
export function useListButton(onlineTableContext, extConfigJson: Ref<ExtConfigType | undefined>, extraParameter = {}) {
  const buttonStatus = {
    add: true,
    addSub: true,
    // edit = 编辑按钮的code
    edit: true,
    // update = 编辑按钮的老code
    update: true,
    delete: true,
    batch_delete: true,
    import: true,
    export: true,
    print: true,
    detail: true,
    query: true,
    reset: true,
    super_query: true,
    bpm: true,
    form_confirm: true,
    // 子表新增
    form_sub_add: true,
    // 子表删除
    form_sub_batch_delete: true,
    // 子表新增
    form_sub_open_add: true,
    // 子表编辑
    form_sub_open_edit: true,
    // 生成测试数据
    aigc_mock_data: true,
  };

  // 弹框事件
  const [registerModal, { openModal }] = useModal();
  const [registerImportModal, { openModal: openImportModal }] = useModal();
  const [registerDetailModal, { openModal: openDetailModal }] = useModal();
  const [registerBpmModal, { openModal: openBpmModal }] = useModal();
  const { createMessage: $message } = useMessage();

  // 按钮相关
  const buttonSwitch = reactive(buttonStatus);
  const cgLinkButtonList = reactive<CgFormButton[]>([]);
  const cgTopButtonList = reactive<CgFormButton[]>([]);

  interface CgBIBtnType extends CgFormButton {
    enabled: boolean,
  }

  // Online表单内置按钮列表
  const cgBIBtnMap = reactive<Record<string, CgBIBtnType>>({});

  // 创建内置按钮配置（用于控制按钮权限）
  const createBIButtonCfg = (btnKey: string) => computed(() => buttonSwitch[btnKey] === true ? cgBIBtnMap[btnKey] : {enabled: false})
  // 查询按钮配置
  const getQueryButtonCfg = createBIButtonCfg('query')
  // 重置按钮配置
  const getResetButtonCfg = createBIButtonCfg('reset')
  // 表单弹窗的确定按钮配置
  const getFormConfirmButtonCfg = createBIButtonCfg('form_confirm')

  const testDataLoading = ref(false);
  const testDataBtnShow = ref(true);
  setTimeout(() => {
    testDataBtnShow.value = false;
  }, 4e3);
  /**
   * 根据配置获取 button和link
   */
  function initButtonList(btnList) {
    cgLinkButtonList.length = 0;
    cgTopButtonList.length = 0;
    if (btnList && btnList.length > 0) {
      for (let i = 0; i < btnList.length; i++) {
        let temp = pick(btnList[i], 'buttonCode', 'buttonName', 'buttonStyle', 'optType', 'exp', 'buttonIcon', 'buttonStatus', 'enabled');
        if (temp.buttonStyle == 'button') {
          cgTopButtonList.push(temp);
        } else if (temp.buttonStyle == 'link') {
          cgLinkButtonList.push(temp);
        } else if (temp.buttonStyle == 'built-in') {
          if (temp.buttonIcon) {
            temp.buttonIcon = 'ant-design:' + temp.buttonIcon;
          }
          temp.enabled = temp.buttonStatus === '1'
          cgBIBtnMap[temp.buttonCode] = temp;
        }
      }
    }
  }

  /**
   * 根据配置设置按钮的 显示/隐藏 状态
   */
  function initButtonSwitch(hideColumns) {
    Object.keys(buttonSwitch).forEach((key) => {
      buttonSwitch[key] = true;
    });
    if (hideColumns && hideColumns.length > 0) {
      Object.keys(buttonSwitch).forEach((key) => {
        if (hideColumns.indexOf(key) >= 0) {
          buttonSwitch[key] = false;
        }
      });
    }
  }

  // 增加事件
  function handleAdd(param) {
    let data = { isUpdate: false };
    if (param) {
      data['param'] = param;
    }
    openModal(true, data);
  }

  // 修改事件
  function handleEdit(record) {
    onlineTableContext
      .beforeEdit(record)
      .then(() => {
        openModal(true, {
          isUpdate: true,
          record,
        });
      })
      .catch((msg) => {
        $message.warning(msg);
      });
  }

  /**
   *
   * [更多]下拉项中的 [删除]
   */
  const getDeleteButton = (record) => {
    return {
      label: cgBIBtnMap['delete'].buttonName,
      ifShow: () => cgBIBtnMap['delete'].enabled,
      popConfirm: {
        title: '是否删除？',
        confirm: handleDeleteOne.bind(null, record),
      },
    };
  };

  // 删除事件
  function handleDeleteOne(record) {
    onlineTableContext
      .beforeDelete(record)
      .then(() => {
        handleDelete(record.id, false);
      })
      .catch((msg) => {
        $message.warning(msg);
      });
  }

  /**
   * 操作列定义
   * @param record
   */
  function getActions(record) {
    //update-begin-author:taoyan date:2022-10-17 for: VUEN-2351【vue3 online表单】online表单 发起流程后，仍然可以编辑数据
    let bpmStatusValue = getBpmStatusValue(record);
    // 允许编辑的情况--> bpm有值且值为1，bpm没有值, 
    //update-begin-author:taoyan date:2023-2-6 for:  QQYUN-4135【online】审批完成的流程和取回作废的流程，可以编辑
    let canEdit = (bpmStatusValue && (bpmStatusValue=='1' || bpmStatusValue=='3' || bpmStatusValue=='4')) || !bpmStatusValue;
    //update-end-author:taoyan date:2023-2-6 for:  QQYUN-4135【online】审批完成的流程和取回作废的流程，可以编辑
    if ((toRaw(buttonSwitch.edit) === true && toRaw(buttonSwitch.update) === true) && canEdit) {
    //update-end-author:taoyan date:2022-10-17 for: VUEN-2351【vue3 online表单】online表单 发起流程后，仍然可以编辑数据
      return [
        {
          label: cgBIBtnMap['edit'].buttonName,
          ifShow: () => cgBIBtnMap['edit'].enabled,
          onClick: (e) => {
            // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
            // erp主表点击编辑时需要防止当前选中数据反选
            extraParameter['editClickCallback'] && extraParameter['editClickCallback'](record.id, e);
            handleEdit(record);
            // update-end--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
          },
        },
      ];
    }
    return [];
  }

  /**
   *  操作列[提交流程]
   */
  function getSubmitFlowButton(record) {
    return {
      label: cgBIBtnMap['bpm'].buttonName,
      ifShow: () => cgBIBtnMap['bpm'].enabled,
      popConfirm: {
        title: '确认提交流程吗？',
        confirm: handleSubmitFlow.bind(null, record),
      },
    };
  }

  /**
   * 操作列[审批进度]
   * @param record
   */
  function getViewBpmGraphicButton(record){
    return {
      label: '审批进度',
      onClick: handleViewGraphic.bind(null, record),
    };
  }

  /**
   * 查看流程图
   * @param record
   */
  function handleViewGraphic(record){
    const { currentTableName } = onlineTableContext;
    
    //判断 currentTableName如果是视图，截掉后缀
    let currentTableNameVariable = currentTableName;
    if (currentTableName.includes('$')) {
      currentTableNameVariable = currentTableName.split('$')[0];
    }
    
    let flowCode = FLOW_CODE_PRE + currentTableNameVariable;
    let dataId = record.id;
    openBpmModal(true, {
      flowCode,
      dataId
    })
  }

  /**
   *  操作列[更多下拉项]
   */
  function getDropDownActions(record, params = {} ) {
    let arr: any = [];
    if (toRaw(buttonSwitch.detail) === true) {
      arr.push({
        label: cgBIBtnMap['detail'].buttonName,
        ifShow: () => cgBIBtnMap['detail'].enabled,
        onClick: handleDetail.bind(null, record),
      });
    }
    // update-begin--author:liaozhiyang---date:20240724---for：【TV360X-1101】树表放开提交流程按钮且加上审批详情弹窗
    if (onlineTableContext['hasBpmStatus'] === true && toRaw(buttonSwitch.bpm) === true) {
      // 提交流程按钮显示条件： 有bpm_status字段，并且有操作bpm按钮的权限
      let bpmStatusValue = getBpmStatusValue(record);
      if (!bpmStatusValue || bpmStatusValue == '1') {
        //并且 bpm_status的值为1 或者为空
        arr.push(getSubmitFlowButton(record));
      }else{
        arr.push(getViewBpmGraphicButton(record))
      }
    }
    // update-end--author:liaozhiyang---date:20240724---for：【TV360X-1101】树表放开提交流程按钮且加上审批详情弹窗
    // 对接积木报表打印
    if (extConfigJson.value) {
      let { reportPrintShow, reportPrintUrl } = extConfigJson.value;
      if (reportPrintShow && reportPrintUrl && toRaw(buttonSwitch.print) === true && cgBIBtnMap['print']?.enabled !== false) {
        arr.push({
          label: '打印',
          onClick() {
            //跳转至积木报表页面
            let url = reportPrintUrl;
            let id = record.id;
            let token = getToken();
            goJmReportViewPage(url, id, token);
          },
        });
      }
    }
    //update-begin-author:taoyan date:2022-10-17 for: VUEN-2351【vue3 online表单】online表单 发起流程后，仍然可以编辑数据
    // 允许删除的情况--> bpm有值且值为1，bpm没有值
    let bpmStatusValue = getBpmStatusValue(record);
    let canDelete = (bpmStatusValue && bpmStatusValue=='1') || !bpmStatusValue;
    if (toRaw(buttonSwitch.delete) === true && canDelete) {
    //update-end-author:taoyan date:2022-10-17 for: VUEN-2351【vue3 online表单】online表单 发起流程后，仍然可以编辑数据
      arr.push(getDeleteButton(record));
    }
    let buttonList = cgLinkButtonList;
    if (buttonList && buttonList.length > 0) {
      for (let item of buttonList) {
        if (showLinkButtonOfExpression(item.exp || '', record) === true) {
          arr.push({
            label: item.buttonName,
            onClick: cgButtonLinkHandler.bind(null, record, item.buttonCode, item.optType),
          });
        }
      }
    }
    return arr;
  }

  /**
   * 获取bpm_status的值 大小写都获取一遍
   * @param record
   */
  function getBpmStatusValue(record) {
    const key = 'bpm_status';
    let value = record[key];
    if (!value) {
      value = record[key.toUpperCase()];
    }
    return value;
  }

  /**
   * 查看详情
   * @param record
   */
  function handleDetail(record) {
    openDetailModal(true, {
      isUpdate: true,
      disableSubmit: true,
      record,
    });
  }

  /**
   * 提交流程请求
   * @param record
   */
  function startProcess(record) {
    const {
      currentTableName,
      onlineUrl: { startProcess },
    } = onlineTableContext;
    
    //判断 currentTableName如果是视图，截掉后缀
    let currentTableNameVariable = currentTableName;
    if (currentTableName.includes('$')) {
      currentTableNameVariable = currentTableName.split('$')[0];
    }
    
    let postConfig = {
      url: startProcess,
      params: {
        flowCode: FLOW_CODE_PRE + currentTableNameVariable,
        id: record.id,
        // TODO 流程表单没有
        formUrl: 'modules/bpm/task/form/OnlineFormDetail',
        formUrlMobile: 'check/onlineForm/detail',
      },
    };
    let postOption = { isTransformResponse: false };
    return new Promise((resolve, reject) => {
      defHttp.post(postConfig, postOption).then((res) => {
        if (res.success) {
          resolve(res);
          $message.success(res.message);
        } else {
          reject();
          $message.warning(res.message);
        }
      });
    });
  }

  /**
   * 提交流程按钮触发事件
   * @param record
   */
  async function handleSubmitFlow(record) {
    await startProcess(record);
    onlineTableContext.loadData();
  }

  //删除请求
  function handleDelete(dataId: String, isBatch = true) {
    console.log('删除数据id值', dataId);
    let url = `${onlineTableContext.onlineUrl.optPre}${onlineTableContext.ID}/${dataId}`;
    // update-begin--author:liaozhiyang---date:20240428---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权删除和导出从表数据
    if (onlineTableContext['isErpSubTable'] === true) {
      url = `${url}?tabletype=3`;
    }
    // update-end--author:liaozhiyang---date:20240428---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权删除和导出从表数据
    return new Promise((resolve, reject) => {
      defHttp
        .delete(
          {
            url,
          },
          { isTransformResponse: false }
        )
        .then((res) => {
          if (res.success) {
            $message.success(res.message);
            // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-206】列表删除最后一页数据，页面跳到前一页数据为空
            onlineTableContext.loadData({ delNum: dataId.split(',').length });
            // update-end--author:liaozhiyang---date:20240528---for：【TV360X-206】列表删除最后一页数据，页面跳到前一页数据为空
            // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
            // erp主表记录选中时被删除需要清空选中的key
            if (!isBatch) {
              extraParameter['singleDelCallback'] && extraParameter['singleDelCallback'](dataId);
            }
            // update-end--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
            resolve(true);
          } else {
            $message.warning(res.message);
            reject();
          }
        });
    });
  }

  // 批量删除事件
  async function handleBatchDelete() {
    let arr = onlineTableContext['selectedRowKeys'];
    if (arr.length <= 0) {
      $message.warning('请选择一条记录！');
      return false;
    } else {
      // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-839】批量删除与「更多-删除」一致，流程已发起的记录不可删
      const selectedRows = onlineTableContext['selectedRows'] || [];
      const rowMap = new Map();
      selectedRows.forEach((row) => {
        if (row && row.id != null) {
          rowMap.set(String(row.id), row);
        }
      });
      // update-end--author:liaozhiyang---date:20260804---for：【LHZP-839】批量删除与「更多-删除」一致，流程已发起的记录不可删
      let idSet: any = [];
      let skippedCount = 0;
      arr.forEach(function (val) {
        let temp = val;
        //树形列表 key后面会带有_loadChild
        if (temp && temp.endsWith('_loadChild')) {
          temp = temp.replace('_loadChild', '');
        }
        // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-839】批量删除与「更多-删除」一致，流程已发起的记录不可删
        // 允许删除：bpm_status 为空，或值为 1（待提交）
        const record = rowMap.get(String(temp));
        if (record) {
          const bpmStatusValue = getBpmStatusValue(record);
          const canDelete = (bpmStatusValue && bpmStatusValue == '1') || !bpmStatusValue;
          if (!canDelete) {
            skippedCount++;
            return;
          }
        }
        // update-end--author:liaozhiyang---date:20260804---for：【LHZP-839】批量删除与「更多-删除」一致，流程已发起的记录不可删
        // 去重
        if (idSet.indexOf(temp) < 0) {
          idSet.push(temp);
        }
      });
      // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-839】批量删除与「更多-删除」一致，流程已发起的记录不可删
      if (idSet.length <= 0) {
        $message.warning('选中数据均不可删除（流程已发起）！');
        return false;
      }
      if (skippedCount > 0) {
        $message.warning(`有 ${skippedCount} 条流程已发起的数据不可删除，将跳过`);
      }
      // update-end--author:liaozhiyang---date:20260804---for：【LHZP-839】批量删除与「更多-删除」一致，流程已发起的记录不可删
      // update-begin--author:liaozhiyang---date:20261028---for：【LHZP-226】批量删除也执行JS增强的删除前置事件beforeDelete
      const beforeDelete = onlineTableContext['beforeDelete'];
      if (typeof beforeDelete === 'function') {
        const results = await Promise.allSettled(
          idSet.map((id) => {
            // 跨页选中时当前页可能没有完整行数据，仅传id
            const record = rowMap.get(String(id)) || { id };
            return Promise.resolve().then(() => beforeDelete(record));
          })
        );
        const rejectMsgs: string[] = [];
        idSet = idSet.filter((_id, index) => {
          const result = results[index];
          if (result.status === 'rejected') {
            if (result.reason && rejectMsgs.indexOf(String(result.reason)) < 0) {
              rejectMsgs.push(String(result.reason));
            }
            return false;
          }
          return true;
        });
        const enhanceSkippedCount = results.filter((item) => item.status === 'rejected').length;
        if (enhanceSkippedCount > 0) {
          const reason = rejectMsgs.length > 0 ? `：${rejectMsgs.join('；')}` : '';
          if (idSet.length <= 0) {
            $message.warning(`选中数据均不可删除${reason}`);
            return false;
          }
          $message.warning(`有 ${enhanceSkippedCount} 条数据不可删除，将跳过${reason}`);
          skippedCount += enhanceSkippedCount;
        }
      }
      // update-end--author:liaozhiyang---date:20261028---for：【LHZP-226】批量删除也执行JS增强的删除前置事件beforeDelete
      let ids = idSet.join(',');
      Modal.confirm({
        title: '确认删除',
        content: skippedCount > 0 ? `是否删除可删除的 ${idSet.length} 条数据？` : '是否删除选中数据',
        okText: '确认',
        cancelText: '取消',
        onOk: async () => {
          await handleDelete(ids);
          onlineTableContext.clearSelectedRow();
        },
      });
    }
  }
  /*
   * liaozhiyang
   * 20250403
   * 【QQYUN-11801】生成测试数据
   * */
  const handleAddTestData = (currentTableName, reload) => {
    testDataLoading.value = true;
    defHttp
      .post({ url: `/online/cgform/api/aigc/mock/data/${currentTableName}`, timeout: 120000 }, { isTransformResponse: false })
      .then((res) => {
        if (res.code == 200) {
          $message.success('生成测试数据成功~');
          reload();
        } else {
          $message.warn(res.message);
        }
        testDataLoading.value = false;
      })
      .catch((err) => {
        testDataLoading.value = false;
        console.log(err);
      });
  }

  /**
   * 自定义按钮触发事件-link按钮
   * @param record
   * @param buttonCode
   * @param optType js/bus
   */
  function cgButtonLinkHandler(record, buttonCode, optType) {
    if (optType == 'js') {
      onlineTableContext['execButtonEnhance'](buttonCode, record);
    } else if (optType == 'action') {
      let params = {
        formId: onlineTableContext['ID'],
        buttonCode: buttonCode,
        dataId: record.id,
      };
      //console.log("自定义按钮link请求后台参数：",params)
      let url = `${onlineTableContext.onlineUrl.buttonAction}`;
      defHttp
        .post(
          {
            url,
            params,
          },
          { isTransformResponse: false }
        )
        .then((res) => {
          if (res.success) {
            onlineTableContext.loadData();
            $message.success('处理完成!');
          } else {
            $message.warning(res.message);
          }
        });
    }
  }

  /**
   * 列表上方按钮 -js事件
   * @param buttonCode
   */
  function cgButtonJsHandler(buttonCode) {
    // 待测
    onlineTableContext['execButtonEnhance'](buttonCode);
  }

  /**
   * 列表上方按钮 -action事件
   * @param buttonCode
   */
  function cgButtonActionHandler(buttonCode) {
    let arr = onlineTableContext['selectedRowKeys'];
    if (!arr || arr.length == 0) {
      $message.warning('请先选中一条记录');
      return false;
    }
    let dataId = arr.join(',');
    let params = {
      formId: onlineTableContext['ID'],
      buttonCode: buttonCode,
      dataId: dataId,
    };
    //console.log("自定义按钮请求后台参数：",params)
    let url = `${onlineTableContext.onlineUrl.buttonAction}`;
    defHttp
      .post(
        {
          url,
          params,
        },
        { isTransformResponse: false }
      )
      .then((res) => {
        if (res.success) {
          onlineTableContext.loadData();
          onlineTableContext.clearSelectedRow();
          $message.success('处理完成!');
        } else {
          $message.warning(res.message);
        }
      });
  }

  // 导入事件
  function onImportExcel() {
    // update-begin--author:liaozhiyang---date:20240429---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权导入从表数据
    if (onlineTableContext['foreignKeyField'] && onlineTableContext['foreignKeyValue']) {
      openImportModal(true, {
        [onlineTableContext['foreignKeyField']]: onlineTableContext['foreignKeyValue'],
      });
    } else {
      openImportModal(true);
    }
    // update-end--author:liaozhiyang---date:20240429---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权导入从表数据
  }

  // 导入地址
  const importUrl = () => {
    // update-begin--author:liaozhiyang---date:20240428---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权删除和导出从表数据
    let url = `${onlineTableContext.onlineUrl.importXls}${onlineTableContext.ID}`;
    if (onlineTableContext['isErpSubTable'] === true) {
      url = `${url}?tabletype=3`;
    } else if (onlineTableContext.isTree() === true) {
      url = `${url}?tabletype=2`;
    }
    return url;
    // update-end--author:liaozhiyang---date:20240428---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权删除和导出从表数据
  };

  // 导出事件
  const { handleExportXlsx } = useMethods();
  function onExportExcel() {
    let params = onlineTableContext.getLoadDataParams();
    let selections = onlineTableContext['selectedRowKeys'];
    if (selections && selections.length > 0) {
      params['selections'] = selections.join(',');
    }
    // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
    let tabletype = {};
    if (onlineTableContext['isErpSubTable'] === true) {
      // update-begin--author:liaozhiyang---date:20240428---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权删除和导出从表数据
      tabletype = { tabletype: 3 };
      // update-end--author:liaozhiyang---date:20240428---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权删除和导出从表数据
      if (onlineTableContext['foreignKeyField'] && onlineTableContext['foreignKeyValue']) {
        params[onlineTableContext['foreignKeyField']] = onlineTableContext['foreignKeyValue'];
      }
    } else if (onlineTableContext.isTree() === true) {
      // 树表导出需携带类型，后端据此保留节点层级并在文件中写入导入规则说明。
      tabletype = { tabletype: 2 };
    }
    // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-5803】online一对多Erp风格
    //console.log("导出参数",params)
    let paramsStr = JSON.stringify(filterObj(params));
    let url = `${onlineTableContext.onlineUrl.exportXls}${onlineTableContext.ID}`;
    const description = onlineTableContext.description;
    const imageExportMode = extConfigJson.value?.imageExportMode || 'path';
    return handleExportXlsx(description, url, { paramsStr: paramsStr, imageExportMode, ...tabletype });
  }

  /**
   * liaozhiyang
   * 20231008
   * 先把自定义表单是转成布尔值，再利用new Function换算真正的规则
   */
  function multipleLinkButtonOfExpression(expression, row) {
    const gather: any = [];
    expression.split('||').forEach(oItem => {
      const arr: any = [];
      oItem
        .trim()
        .split('&&')
        .forEach(nItem => {
          arr.push(oneLinkButtonOfExpression(nItem.trim(), row));
        });
      gather.push(arr.join('&&'));
    });
    const r = gather.join('||');
    console.log('---多个表达式---', r);
    return new Function(`return ${r}`)();
  }
  /**
   * liaozhiyang
   * 20231008
   * 区分有是否有表达式
   */
  function showLinkButtonOfExpression(expression, row) {
    if (!expression || expression == '') {
      return true;
    }
    if (expression.indexOf('||') == -1 && expression.indexOf('&&') == -1) {
      return oneLinkButtonOfExpression(expression, row);
    } else {
      return multipleLinkButtonOfExpression(expression, row);
    }
  }

  /**
   *  用于处理 link按钮的表达式 返回布尔值
   * @param expression 表达式
   * @param row 所在行的数据
   */
  function oneLinkButtonOfExpression(expression: string, row: any): boolean {
    if (!expression || expression == '') {
      return true;
    }
    // 字段名#条件#值
    let arr = expression.split('#');
    //获取字段值
    let fieldValue = row[arr[0]];
    //获取表达式
    let exp = arr[1].toLowerCase();
    //判断表达式
    if (exp === 'eq') {
      return fieldValue == arr[2];
    } else if (exp === 'ne') {
      return !(fieldValue == arr[2]);
    } else if (exp === 'empty') {
      if (arr[2] === 'true') {
        return !fieldValue || fieldValue == '';
      } else {
        return fieldValue && fieldValue.length > 0;
      }
    } else if (exp === 'in') {
      let arr2 = arr[2].split(',');
      return arr2.indexOf(String(fieldValue)) >= 0;
    }
    return false;
  }

  return {
    buttonSwitch,
    cgLinkButtonList,
    cgBIBtnMap,
    getQueryButtonCfg,
    getResetButtonCfg,
    getFormConfirmButtonCfg,
    cgTopButtonList,
    importUrl,
    registerModal,
    handleAdd,
    handleEdit,
    handleBatchDelete,
    handleAddTestData,
    testDataLoading,
    testDataBtnShow,
    registerImportModal,
    onImportExcel,
    onExportExcel,
    getDropDownActions,
    getActions,
    cgButtonJsHandler,
    cgButtonActionHandler,
    cgButtonLinkHandler,
    initButtonList,
    initButtonSwitch,
    getDeleteButton,
    handleSubmitFlow,
    getSubmitFlowButton,
    registerDetailModal,
    registerBpmModal,
    openDetailModal
  };
}
