import { inject, reactive, ref, watch, unref, Ref } from 'vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { isEmpty } from '@/utils/is';

export function useSelectBiz(getList, props, emit) {
  //接收下拉框选项
  const selectOptions = inject('selectOptions', ref<Array<object>>([]));
  //接收已选择的值
  const selectValues = <object>inject('selectValues', reactive({ value: [], change: false }));
  // 是否正在加载回显
  const loadingEcho = inject<Ref<boolean>>('loadingEcho', ref(false));
  //数据集
  const dataSource = ref<Array<object>>([]);
  //已选择的值
  const checkedKeys = ref<Array<string | number>>([]);
  //选则的行记录
  const selectRows = ref<Array<object>>([]);
  //提示弹窗
  const $message = useMessage();
  // 是否是首次加载回显，只有首次加载，才会显示 loading
  let isFirstLoadEcho = true;

  /**
   * 监听selectValues变化
   */
  watch(
    selectValues,
    () => {
      //update-begin-author:liusq---date:2023-10-19--for: [issues/788]判断有设置数值才去加载
      //if (selectValues['change'] == false && !isEmpty(selectValues['value'])) {
      if (selectValues['change'] == false && !isEmpty(selectValues['value'])) {
        //update-end-author:liusq---date:2023-10-19--for: [issues/788]判断有设置数值才去加载
        //update-begin---author:wangshuai ---date:20220412  for：[VUEN-672]发文草稿箱编辑时拟稿人显示用户名------------
        let params = { isMultiTranslate: 'true' };
        params[props.rowKey] = selectValues['value'].join(',');
        //update-end---author:wangshuai ---date:20220412  for：[VUEN-672]发文草稿箱编辑时拟稿人显示用户名--------------
        loadingEcho.value = isFirstLoadEcho;
        isFirstLoadEcho = false;
        getDataSource(params, true)
          .then()
          .finally(() => {
            loadingEcho.value = isFirstLoadEcho;
          });
      }
      //设置列表默认选中
      checkedKeys['value'] = selectValues['value'];
    },
    { immediate: true }
  );

  async function onSelectChange(selectedRowKeys: (string | number)[], selectRow) {
    checkedKeys.value = selectedRowKeys;
    //判断全选的问题checkedKeys和selectRows必须一致
    if (props.showSelected && unref(checkedKeys).length !== unref(selectRow).length) {
      let { records } = await getList({
        code: unref(checkedKeys).join(','),
        pageSize: unref(checkedKeys).length,
      });
      selectRows.value = records;
    } else {
      selectRows.value = selectRow;
    }
  }

  /**
   * 选择列配置
   */
  const rowSelection = {
    //update-begin-author:liusq---date:20220517--for: 动态设置rowSelection的type值,默认是'checkbox' ---
    type: props.isRadioSelection ? 'radio' : 'checkbox',
    //update-end-author:liusq---date:20220517--for: 动态设置rowSelection的type值,默认是'checkbox' ---
    columnWidth: 20,
    selectedRowKeys: checkedKeys,
    onChange: onSelectChange,
    //update-begin-author:wangshuai---date:20221102--for: [VUEN-2562]用户选择，跨页选择后，只有当前页人员 ---
    //table4.4.0新增属性选中之后是否清空上一页下一页的数据，默认false
    preserveSelectedRowKeys:true,
    //update-end-author:wangshuai---date:20221102--for: [VUEN-2562]用户选择，跨页选择后，只有当前页人员 ---
  };

  /**
   * 序号列配置
   */
  const indexColumnProps = {
    dataIndex: 'index',
    width: 50,
  };

  /**
   * 加载列表数据集
   * @param params
   * @param flag 是否是默认回显模式加载
   */
  async function getDataSource(params, flag) {
    let { records } = await getList(params);
    dataSource.value = records;
    if (flag) {
      let options = <any[]>[];
      records.forEach((item) => {
        options.push({ label: item[props.labelKey], value: item[props.rowKey] });
      });
      selectOptions.value = options;
    }
  }
  async function initSelectRows() {
    let { records } = await getList({
      code: selectValues['value'].join(','),
      pageSize: selectValues['value'].length,
    });
    checkedKeys['value'] = selectValues['value'];
    selectRows['value'] = records;
  }

  /**
   * 弹出框显示隐藏触发事件
   */
  async function visibleChange(visible) {
    if (visible) {
      //设置列表默认选中
      props.showSelected && initSelectRows();
    } else {
      // update-begin--author:liaozhiyang---date:20240517---for：【QQYUN-9366】用户选择组件取消和关闭会把选择数据带入
      emit('close');
      // update-end--author:liaozhiyang---date:20240517---for：【QQYUN-9366】用户选择组件取消和关闭会把选择数据带入
    }
  }

  /**
   * 确定选择
   */
  function getSelectResult(success) {
    let options = <any[]>[];
    let values = <any[]>[];
    selectRows.value.forEach((item) => {
      options.push({ label: item[props.labelKey], value: item[props.rowKey] });
    });
    checkedKeys.value.forEach((item) => {
      values.push(item);
    });
    selectOptions.value = options;
    if (props.maxSelectCount && values.length > props.maxSelectCount) {
      $message.createMessage.warning(`最多只能选择${props.maxSelectCount}条数据`);
      return false;
    }
    success && success(options, values);
  }
  //删除已选择的信息
  function handleDeleteSelected(record) {
    //update-begin---author:wangshuai ---date:20230404  for：【issues/424】开启右侧列表后，在右侧列表中删除用户时，逻辑有问题------------
    checkedKeys.value = checkedKeys.value.filter((item) => item != record[props.rowKey]);
    selectRows.value = selectRows.value.filter((item) => item[props.rowKey] !== record[props.rowKey]);
    //update-end---author:wangshuai ---date:20230404  for：【issues/424】开启右侧列表后，在右侧列表中删除用户时，逻辑有问题------------
  }
  //清空选择项
  function reset() {
    checkedKeys.value = [];
    selectRows.value = [];
  }
  return [
    {
      onSelectChange,
      getDataSource,
      visibleChange,
      selectOptions,
      selectValues,
      rowSelection,
      indexColumnProps,
      checkedKeys,
      selectRows,
      dataSource,
      getSelectResult,
      handleDeleteSelected,
      reset,
    },
  ];
}
