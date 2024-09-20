import { onMounted, onUnmounted, nextTick } from 'vue';
import { JVxeTableMethods, JVxeTableProps } from '/@/components/jeecg/JVxeTable/src/types';
import Sortable from 'sortablejs';
import { isEnabledVirtualYScroll } from '/@/components/jeecg/JVxeTable/utils';

export function useDragSort(props: JVxeTableProps, methods: JVxeTableMethods) {
  if (props.dragSort) {
    let sortable2: Sortable;
    let initTime: any;

    onMounted(() => {
      // 加载完成之后再绑定拖动事件
      initTime = setTimeout(createSortable, 300);
    });

    onUnmounted(() => {
      clearTimeout(initTime);
      if (sortable2) {
        sortable2.destroy();
      }
    });

    function createSortable() {
      let xTable = methods.getXTable();
      // let dom = xTable.$el.querySelector('.vxe-table--fixed-wrapper .vxe-table--body tbody')
      let dom = xTable.$el.querySelector('.body--wrapper>.vxe-table--body tbody');
      let startChildren = [];
      sortable2 = Sortable.create(dom as HTMLElement, {
        handle: '.drag-btn',
        // update-begin--author:liaozhiyang---date:20240417---for:【QQYUN-8785】online表单列位置的id未做限制，拖动其他列到id列上面，同步数据库时报错
        filter: '.not-allow-drag',
        draggable: ".allow-drag",
        // update-end--author:liaozhiyang---date:20240417---for:【QQYUN-8785】online表单列位置的id未做限制，拖动其他列到id列上面，同步数据库时报错
        direction: 'vertical',
        animation: 300,
        onStart(e) {
          let from = e.from;
          // @ts-ignore
          startChildren = [...from.children];
        },
        onEnd(e: any) {
          // -update-begin--author:liaozhiyang---date:20240619---for：【TV360X-585】拖动字段虚拟滚动不好使
          const isRealEnabledVirtual = isEnabledVirtualYScroll(props, xTable);
          let newIndex;
          let oldIndex;
          // 滚动排序需要区分当前行编辑是否启动了虚拟滚动(底层loadData方法对是否真实开启了虚拟滚动处理不一样导致需要区分)
          if (isRealEnabledVirtual) {
            // e.clone的元素才是真实拖动的元素(虚拟滚动也不会变)
            const dragNode = e.clone;
            const dragRowInfo = xTable.getRowNode(dragNode);
            // e.item的元素只有没虚拟滚动时才是拖动的元素(如果虚拟滚动了则会变)
            const itemNode = e.item;
            const itemRowInfo = xTable.getRowNode(itemNode);
            // e.newIndex是当前可视区内元素的索引(不是数据实际的索引)、e.oldIndex 是拖动时可视区内元素的索引(不是数据实际的索引)
            if (dragRowInfo!.rowid === itemRowInfo!.rowid) {
              // e.clone和e.item相同说明拖拽的元素在DOM中，没被虚拟滚动给remove掉。
              if (e.newIndex === e.oldIndex) {
                // 此时新旧index一样就可认为没拖动
                return;
              }
            } else {
            }
            // 此时真实DOM元素顺序已排(通过拖拽元素的前后元素确定拖拽元素在真实数据中是往前还是往后拖)
            oldIndex = dragRowInfo!.index;
            const len = e.from.childNodes.length;
            let referenceIndex;
            let referenceNode;
            if (e.newIndex + 1 < len) {
              // 拖拽DOM交换之后，后面还有元素（参考物是后面的元素）
              referenceNode = e.from.childNodes[e.newIndex + 1];
              referenceIndex = xTable.getRowNode(referenceNode)!.index;
              if (oldIndex > referenceIndex) {
                newIndex = referenceIndex;
              } else {
                newIndex = referenceIndex - 1;
              }
            } else {
              // 拖拽DOM交换之后，后面没有元素了（参考物是前面的元素）
              referenceNode = e.from.childNodes[e.newIndex - 1];
              referenceIndex = xTable.getRowNode(referenceNode)!.index;
              newIndex = referenceIndex;
            }
          } else {
            oldIndex = e.oldIndex;
            newIndex = e.newIndex;
            if (oldIndex === newIndex) {
              return;
            }
            const from = e.from;
            const element = startChildren[oldIndex];
            let target = null;
            if (oldIndex > newIndex) {
              // 向上移动
              if (oldIndex + 1 < startChildren.length) {
                target = startChildren[oldIndex + 1];
              }
            } else {
              // 向下移动
              target = startChildren[oldIndex + 1];
            }
            from.removeChild(element);
            from.insertBefore(element, target);
          }
          // -update-end--author:liaozhiyang---date:20240620---for：【TV360X-585】拖动字段虚拟滚动不好使
          nextTick(() => {
            methods.doSort(oldIndex, newIndex);
            methods.trigger('dragged', { oldIndex: oldIndex, newIndex: newIndex });
          });
        },
      });
    }
  }
}
