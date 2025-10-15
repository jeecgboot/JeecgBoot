import type { Ref } from 'vue';
import { HrefSlots, OnlineColumn } from '/@/components/jeecg/OnLine/types/onlineConfig';
import { filterMultiDictText } from '/@/utils/dict/JDictSelectUtil';
import { computed, defineAsyncComponent, h, reactive, ref, toRaw, unref, watch, markRaw } from 'vue';
import { useRouter } from 'vue-router';
import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
import { getAreaTextByCode } from '/@/components/Form/src/utils/Area';
import { createImgPreview } from '/@/components/Preview/index';
import { importViewsFile, _eval } from '/@/utils';
import { useModal } from '/@/components/Modal';
import { getToken } from '/@/utils/auth';
import { downloadFile } from '/@/api/common/api';
import { getWeekMonthQuarterYear, split } from '/@/utils';
/**
 * 获取实际列表需要的column配置
 * @param onlineTableContext 从数据库中查出来的数据
 * @param extConfigJson 扩展配置JSON
 */
export function useTableColumns(onlineTableContext, extConfigJson: Ref<any | undefined>) {
  // 获取路由器对象 href跳转用到
  let router = useRouter();

  // 列信息
  const columns = ref<Array<OnlineColumn>>([]);
  // 是否有bpm_status
  //const hasBpmStatus = ref<boolean>(false)
  // 字典信息
  const dictOptionInfo = ref<any>({});
  //已选择的值
  const selectedKeys = ref<any[]>([]);
  //选择的行记录
  //const selectRows = ref<Array<any>>([]);
  // 选择列配置 --computed有问题
  const rowSelection = ref<any>(null);
  // 是否有滚动条
  let enableScrollBar = ref(true);
  // table属性scroll
  let tableScroll = computed(() => {
    if (enableScrollBar.value == true) {
      return undefined;
    } else {
      // X轴没有滚动条
      return { x: false };
    }
  });

  //用于 online列表的 某列的点击弹窗事件-弹窗显示其他表单
  const [registerOnlineHrefModal, { openModal: openOnlineHrefModal }] = useModal();
  const hrefMainTableId = ref('')
  // 用于 online表单中 弹出别的表单
  const [registerPopModal, { openModal: openPopModal }] = useModal();
  const popTableId = ref('')

  // 对查询列信息的请求结果 处理方法
  function handleColumnResult(result, type = 'checkbox') {
    // 字典设置
    dictOptionInfo.value = result.dictOptions;
    // rowSelection设置
    if (result.checkboxFlag == 'Y') {
      rowSelection.value = {
        selectedRowKeys: selectedKeys,
        onChange: onSelectChange,
        type,
      };
    } else {
      rowSelection.value = null;
    }
    // 是否允许滚动条
    enableScrollBar.value = result.scrollFlag == 1;

    let dataColumns = result.columns;
    dataColumns.forEach((column) => {
      // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-4161】列支持固定功能
      if (column.fieldExtendJson) {
        const json = JSON.parse(column.fieldExtendJson);
        if (!!json.isFixed) {
          column.fixed = 'left';
        }
      }
      // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-4161】列支持固定功能
      // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-129】增加富文本控件配置href跳转
      if (column.hrefSlotName && column.scopedSlots) {
        const obj = result.fieldHrefSlots?.find((item) => item.slotName === column.hrefSlotName);
        if (obj) {
          column.fieldHref = obj;
        }
      }
      // update-end--author:liaozhiyang---date:20240517---for：【TV360X-129】增加富文本控件配置href跳转
      Object.keys(column).map((key) => {
        // 删掉空值的字段（不删除 空字符串('') 或 数字 0 ）
        if (column[key] == null) {
          delete column[key];
        }
      });
    });

    // href 跳转
    let fieldHrefSlots: HrefSlots[] = result.fieldHrefSlots;
    const fieldHrefSlotKeysMap = {};
    fieldHrefSlots.forEach((item) => (fieldHrefSlotKeysMap[item.slotName] = item));

    let tableColumns: OnlineColumn[] = [];
    // 处理列中的 href 跳转和 dict 字典，使两者可以兼容存在
    tableColumns = handleColumnHrefAndDict(dataColumns, fieldHrefSlotKeysMap);
    // 是否有 bpm_status字段 如果有，列表操作按钮需要增加提交流程按钮
    bpmStatusFilter(tableColumns);

    console.log('-----列表列配置----', tableColumns);
    // 如果是树列表 需要设置第一列字段 及 第一列align
    if (onlineTableContext.isTree() === true) {
      // 找到第一列的配置
      let firstField = result.textField;
      let index = -1;
      for (let i = 0; i < tableColumns.length; i++) {
        if (tableColumns[i].dataIndex == firstField) {
          index = i;
          break;
        }
      }
      if (index > 0) {
        //如果是0或是-1不需要处理
        let deleteColumns = tableColumns.splice(index, 1);
        tableColumns.unshift(deleteColumns[0]);
      }
      //第一列居左
      if (tableColumns.length > 0) {
        tableColumns[0].align = 'left';
      }
    }
    columns.value = tableColumns;
    // 列发生了变化，需要重新渲染表格
    onlineTableContext.reloadTable();
  }

  /**
   * 表格选择事件 [expose]
   * @param selectedRowKeys
   * @param selectRow
   */
  function onSelectChange(selectedRowKeys, selectedRows) {
    selectedKeys.value = selectedRowKeys;
    onlineTableContext['selectedRows'] = toRaw(selectedRows);
    onlineTableContext['selectedRowKeys'] = toRaw(selectedRowKeys);
  }

  /**
   * 处理列的href和字典翻译
   */
  function handleColumnHrefAndDict(columns: OnlineColumn[], fieldHrefSlotKeysMap: {}): OnlineColumn[] {
    for (let column of columns) {
      let { customRender, hrefSlotName, fieldType } = column;
      // online 报表中类型配置为日期（yyyy-MM-dd ），但是实际展示为日期时间格式(yyyy-MM-dd HH:mm:ss) issues/3042
      if (fieldType == 'date' || fieldType == 'Date') {
        column.customRender = ({ text }) => {
          if (!text) {
            return '';
          }
          if (text.length > 10) {
            return text.substring(0, 10);
          }
          return text;
        };
      } else if (fieldType == 'link_table') {
        // 关联记录列表展示
        // update-begin--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
        const fieldExtendJson = column.fieldExtendJson ?? '{}';
        const json = JSON.parse(fieldExtendJson);
        // update-end--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
        column.customRender = ({ text, record }) => {
          if (!text) {
            return '';
          }
          if(onlineTableContext.isPopList===true){
            // 如果是弹窗的列表，关联记录的列只支持数据翻译，不需要跳转逻辑
            return record[column.dataIndex+"_dictText"]
          }else{
            let tempIdArray = (text+'').split(',');
            //update-begin-author:taoyan date:2023-2-15 for: QQYUN-4286【online表单】主子表开启联合查询 功能测试报错打不开
            let tempLabelArray = [];
            if(record[column.dataIndex+"_dictText"]){
              tempLabelArray = record[column.dataIndex+"_dictText"].split(',');
            }
            //update-end-author:taoyan date:2023-2-15 for: QQYUN-4286【online表单】主子表开启联合查询 功能测试报错打不开
            let renderResult:any = []
            if(renderResult.length==0){
              return ''
            }
            //如果需要显示全，但是会换行：display: flex;width: 100%;flex-wrap: wrap;flex-direction: row;
            return h('div',{style:{'overflow':'hidden'}}, renderResult);
          }
        };
      } else if (fieldType === 'popup_dict') {
        // update-begin--author:liaozhiyang---date:20240402---for：【QQYUN-8833】JPopupDict的列表翻译
        column.customRender = ({ text, record }) => {
          const dict = record[column.dataIndex + '_dictText'];
          if (dict != undefined) {
            return record[column.dataIndex + '_dictText'];
          }
          return text;
        };
        // update-end--author:liaozhiyang---date:20240402---for：【QQYUN-8833】JPopupDict的列表翻译
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
          // 自定义渲染函数的列 需要手动配置ellipsis
          column.ellipsis = true;
          column.customRender = ({ text, record }) => {
            let value = text;
            // 如果 dictCode 有值，就进行字典转换
            if (dictCode) {
              if (dictCode.startsWith(replaceFlag)) {
                let textFieldName = dictCode.replace(replaceFlag, '');
                value = record[textFieldName];
              } else {
                value = filterMultiDictText(unref(dictOptionInfo)[dictCode], text + '');
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

        //  老版本叫scopedSlots 新版叫slots
        if (column.scopedSlots) {
          // slot的列 需要手动配置ellipsis
          column.ellipsis = true;
          let slots = column.scopedSlots;
          column['slots'] = slots;
          delete column.scopedSlots;
        }
      }
    }
    return columns;
  }

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
      if(href.startsWith('ONLINE:')){
        // ONLINE:tableId:fieldName
        let arr = href.split(':')
        hrefMainTableId.value = arr[1];
        let fieldName = arr[2];
        openOnlineHrefModal(true, {
          isUpdate: true,
          disableSubmit: true,
          hideSub: true,
          record:{id: record[fieldName]},
        })
      }else{
        href = href.trim().replace(/\${([^}]+)?}/g, (_s1, s2) => record[s2]);
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
  }

  // 样式
  const dialogStyle = {
    top: 0,
    left: 0,
    height: '100%',
    margin: 0,
    padding: 0,
  };

  // update-begin--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
  // 弹窗属性配置
  const hrefComponent = reactive({
    model: {
      title: '',
      okText: '关闭',
      width: '100%',
      open: false,
      destroyOnClose: true,
      style: dialogStyle,
      // dialogStyle: dialogStyle,
      bodyStyle: { padding: '8px', height: 'calc(100vh - 108px)', overflow: 'auto', overflowX: 'hidden' },
      // 隐藏掉取消按钮
      cancelButtonProps: { style: { display: 'none' } },
    },
    on: {
      ok: () => (hrefComponent.model.open = false),
      cancel: () => (hrefComponent.model.open = false),
    },
    is: <any>null,
    params: {},
  });
  // update-end--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x

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
      hrefComponent.params = params;
    } else {
      hrefComponent.params = {};
    }
    // update-begin--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
    hrefComponent.model.open = true;
    // update-end--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
    hrefComponent.model.title = '操作';
    hrefComponent.is = markRaw(defineAsyncComponent(() => importViewsFile(path)));
  }

  //如果是树列表 操作列只能右侧固定
  let fixedAction:any = 'left';
  if(onlineTableContext.isTree()){
    fixedAction = 'right'
  }
  const actionColumn = reactive<OnlineColumn>({
    title: '操作',
    dataIndex: 'action',
    slots: { customRender: 'action' },
    fixed: fixedAction,
    align: 'center',
    width: 150,
  });

  // 监听扩展参数的固定列配置，动态改变操作列的固定方式
  watch(() => extConfigJson?.value, () => {
    if (extConfigJson?.value?.tableFixedAction === 1) {
      actionColumn.fixed = extConfigJson?.value?.tableFixedActionType || 'right';
      // 如果是树列表 操作列只能右侧固定
      if(onlineTableContext.isTree()){
        actionColumn.fixed = 'right'
      }
    }
  });

  // 流程按钮状态
  function bpmStatusFilter(tableColumns: OnlineColumn[]): boolean {
    let flag = false;
    for (let i = 0; i < tableColumns.length; i++) {
      let item = tableColumns[i];
      let fieldName = item.dataIndex;
      if (fieldName!.toLowerCase() == 'bpm_status') {
        flag = true;
        break;
      }
    }
    onlineTableContext['hasBpmStatus'] = flag;
    return flag;
  }

  /**
   * 文件
   * @param text
   */
  function downloadRowFile(text, record, column, id) {
    if (!text) {
      return;
    }
    // update-begin--author:liaozhiyang---date:20240124---for：【QQYUN-8020】online 表单有多个文件走下载接口
    if (text.indexOf(',') > 0) {
      downloadFile(`/online/cgform/field/download/${id}/${record.id}/${column.dataIndex}`, `文件_${record.id}.zip`);
    } else {
      const url = getFileAccessHttpUrl(text);
      window.open(url);
    }
    // update-end--author:liaozhiyang---date:20240124---for：【QQYUN-8020】online 表单有多个文件走下载接口
  }

  /**
   * 图片
   * @param text
   */
  function getImgView(text) {
    if (text && text.indexOf(',') > 0) {
      // update-begin--author:liaozhiyang---date:20250325---for：【issues/7990】图片参数中包含逗号会错误的识别成多张图
      text = split(text)[0];
      // update-end--author:liaozhiyang---date:20250325---for：【issues/7990】图片参数中包含逗号会错误的识别成多张图
    }
    return getFileAccessHttpUrl(text);
  }

  /**
   * 根据编码获取省市区文本
   * @param code
   */
  function getPcaText(code) {
    if (!code) {
      return '';
    }
    return getAreaTextByCode(code);
  }

  /**
   * 日期格式化
   * @param text
   */
  function getFormatDate(text, column) {
    if (!text) {
      return '';
    }
    let a = text;
    if (a.length > 10) {
      a = a.substring(0, 10);
    }
    // update-begin--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
    let fieldExtendJson = column?.fieldExtendJson;
    if (fieldExtendJson) {
      fieldExtendJson = JSON.parse(fieldExtendJson);
      if (fieldExtendJson.picker && fieldExtendJson.picker != 'default') {
        const result = getWeekMonthQuarterYear(a);
        return result[fieldExtendJson.picker];
      }
    }
    // update-end--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
    return a;
  }

  watch(selectedKeys, () => {
    onlineTableContext['selectedRowKeys'] = toRaw(selectedKeys.value);
  });

  onlineTableContext['clearSelectedRow'] = () => {
    selectedKeys.value = [];
    onlineTableContext['selectedRows'] = [];
    onlineTableContext['selectedRowKeys'] = [];
  };

  /**
   * 预览列表 cell 图片
   * @param text
   */
  function viewOnlineCellImage(text) {
    if (text) {
      let imgList: any = [];
      // update-begin--author:liaozhiyang---date:20250325---for：【issues/7990】图片参数中包含逗号会错误的识别成多张图
      const arr = split(text);
      // update-end--author:liaozhiyang---date:20250325---for：【issues/7990】图片参数中包含逗号会错误的识别成多张图
      for (let str of arr) {
        if (str) {
          imgList.push(getFileAccessHttpUrl(str));
        }
      }
      createImgPreview({ imageList: imgList });
    }
  }

  /**
   * link table控件在列表上显示 支持点击跳转表单
   * @param id
   * @param hrefTableName
   */
  const onlinePopModalRef = ref();
  async function handleClickLinkTable(id, hrefTableName, isListReadOnly){
    popTableId.value = hrefTableName;
    let formStatus =  await onlinePopModalRef.value.getFormStatus();
    // 判断当前表单是否支持编辑，不能编辑跳详情表单
    if(formStatus==true){
      hrefMainTableId.value = hrefTableName;
      openOnlineHrefModal(true, {
        isUpdate: true,
        disableSubmit: true,
        hideSub: true,
        record:{id: id},
      })
    }else{
      openPopModal(true, {
        isUpdate: true,
        // update-begin--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
        disableSubmit: isListReadOnly ? true : false,
        // update-end--author:liaozhiyang---date:20250318---for：【issues/7930】表格列表中支持关联记录配置是否只读
        record: {
          id: id
        }
      });
    }
  }
  
  return {
    columns,
    actionColumn,
    selectedKeys,
    rowSelection,
    enableScrollBar,
    tableScroll,
    downloadRowFile,
    getImgView,
    getPcaText,
    getFormatDate,
    handleColumnResult,
    onSelectChange,
    hrefComponent,
    viewOnlineCellImage,
    hrefMainTableId,
    registerOnlineHrefModal,
    registerPopModal,
    openPopModal,
    openOnlineHrefModal,
    onlinePopModalRef,
    popTableId,
    handleClickFieldHref,
  };
}
