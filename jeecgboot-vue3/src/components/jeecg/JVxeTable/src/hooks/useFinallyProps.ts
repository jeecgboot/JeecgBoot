import { unref, computed, ref, watch, nextTick } from 'vue';
import { merge, debounce } from 'lodash-es';
import { isArray } from '/@/utils/is';
import { useAttrs } from '/@/hooks/core/useAttrs';
import { useKeyboardEdit } from '../hooks/useKeyboardEdit';
import { JVxeDataProps, JVxeTableMethods, JVxeTableProps } from '../types';

export function useFinallyProps(props: JVxeTableProps, data: JVxeDataProps, methods: JVxeTableMethods) {
  const attrs = useAttrs();
  // vxe 键盘操作配置
  const { keyboardEditConfig } = useKeyboardEdit(props);
  // vxe 最终 editRules
  const vxeEditRules = computed(() => merge({}, props.editRules, data.innerEditRules));
  // vxe 最终 events
  const vxeEvents = computed(() => {
    let listeners = { ...unref(attrs) };
    let events = {
      onScroll: methods.handleVxeScroll,
      onCellClick: methods.handleCellClick,
      onEditClosed: methods.handleEditClosed,
      onEditActived: methods.handleEditActived,
      onRadioChange: methods.handleVxeRadioChange,
      onCheckboxAll: methods.handleVxeCheckboxAll,
      onCheckboxChange: methods.handleVxeCheckboxChange,
      // 代码逻辑说明: 【QQYUN-8566】JVXETable无法记住列设置
      onCustom: methods.handleCustom,
    };
    // 用户传递的事件，进行合并操作
    Object.keys(listeners).forEach((key) => {
      let listen = listeners[key];
      if (events.hasOwnProperty(key)) {
        if (isArray(listen)) {
          listen.push(events[key]);
        } else {
          listen = [events[key], listen];
        }
      }
      events[key] = listen;
    });
    return events;
  });

  // vxe 最终 props
  const vxePropsMerge = computed(() => {
    // 代码逻辑说明: 【QQYUN-8785】online表单列位置的id未做限制，拖动其他列到id列上面，同步数据库时报错
    let rowClass = {};
    if (props.dragSort) {
      rowClass = {
        rowClassName: (params) => {
          let { row } = params;
          const find = props.notAllowDrag?.find((item:any) => {
            const {key, value} = item;
            return row[key] == value;
          });
          // 业务传进的来的rowClassName
          const popsRowClassName = props.rowClassName ?? '';
          let outClass = '';
          if(typeof popsRowClassName==='string'){
            popsRowClassName && (outClass = popsRowClassName);
          }else if(typeof popsRowClassName==='function'){
            outClass = popsRowClassName(params)
          }
          return find ? `not-allow-drag ${outClass}` : `allow-drag ${outClass}`;
        },
      };
    }
    return merge(
      {},
      data.defaultVxeProps,
      {
        showFooter: data.statistics.has,
      },
      unref(attrs),
      {
        ref: 'gridRef',
        size: props.size,
        loading: false,
        disabled: props.disabled,
        // columns: unref(data.vxeColumns),
        editRules: unref(vxeEditRules),
        height: props.height === 'auto' ? null : props.height,
        maxHeight: props.maxHeight,
        // 代码逻辑说明: 【QQYUN-5133】JVxeTable 行编辑升级
        scrollY: props.scrollY,
        scrollX: props.scrollX,
        border: props.bordered,
        footerMethod: methods.handleFooterMethod,
        // 展开行配置
        expandConfig: {
          toggleMethod: methods.handleExpandToggleMethod,
        },
        // 可编辑配置
        editConfig: {
          //activeMethod: methods.handleActiveMethod,
          beforeEditMethod: methods.handleActiveMethod,
        },
        radioConfig: {
          checkMethod: methods.handleCheckMethod,
        },
        checkboxConfig: {
          checkMethod: methods.handleCheckMethod,
        },
        ...rowClass
        // rowClassName:(params)=>{
        //   const { row } = params;
        //   return row.dbFieldName=='id'?"not-allow-drag":"allow-drag"
        // }
      },
      unref(vxeEvents),
      unref(keyboardEditConfig)
    );
  });

  // 代码逻辑说明: 【issues/8593】修复列改变后内容不刷新
  const vxeColumnsRef = ref(data.vxeColumns!.value || [])
  const watchColumnsDebounce = debounce(async () => {
    vxeColumnsRef.value = []
    await nextTick()
    vxeColumnsRef.value = data.vxeColumns!.value
  }, 50)
  watch(data.vxeColumns!, watchColumnsDebounce)

  const vxeProps = computed(() => {
    return {
      ...unref(vxePropsMerge),
      // 【issue/8695】单独抽出 columns，防止性能问题
      columns: unref(vxeColumnsRef),
    }
  });

  return {
    vxeProps,
    prefixCls: data.prefixCls,
  };
}
