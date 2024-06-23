<template>
  <div :class="boxClass">
    <vxe-toolbar ref="xToolbarRef" :custom="custom">
      <!-- 工具按钮 -->
      <template #buttons>
        <div :class="`${prefixCls}-button div`" :size="btnSize">
          <slot v-if="showPrefix" name="toolbarPrefix" :size="btnSize" />
          <a-button v-if="showAdd" type="primary" preIcon="ant-design:plus-outlined" :disabled="disabled" @click="trigger('add')">
            <span>新增</span>
          </a-button>
          <a-button v-if="showSave" preIcon="ant-design:save-outlined" :disabled="disabled" @click="trigger('save')">
            <span>保存</span>
          </a-button>
          <template v-if="selectedRowIds.length > 0">
            <Popconfirm v-if="showRemove" :title="`确定要删除这 ${selectedRowIds.length} 项吗?`" @confirm="trigger('remove')">
              <a-button preIcon="ant-design:minus-outlined" :disabled="disabled">删除</a-button>
            </Popconfirm>
            <template v-if="showClearSelection">
              <a-button preIcon="ant-design:delete-outlined" @click="trigger('clearSelection')">清空选择</a-button>
            </template>
          </template>
          <slot v-if="showSuffix" name="toolbarSuffix" :size="btnSize" />
          <a v-if="showCollapse" style="margin-left: 4px" @click="toggleCollapse">
            <span>{{ collapsed ? '展开' : '收起' }}</span>
            <Icon :icon="collapsed ? 'ant-design:down-outlined' : 'ant-design:up-outlined'" />
          </a>
        </div>
      </template>
    </vxe-toolbar>
  </div>
</template>

<script lang="ts" setup>
  import { computed, inject, ref, onMounted } from 'vue';
  // noinspection ES6UnusedImports
  import { Popconfirm } from 'ant-design-vue';
  import { VxeToolbarInstance } from 'vxe-table';
  import { Icon } from '/@/components/Icon';
  import { propTypes } from '/@/utils/propTypes';

  const props = defineProps({
    size: propTypes.string,
    disabled: propTypes.bool.def(false),
    custom: propTypes.bool.def(false),
    toolbarConfig: propTypes.object,
    disabledRows: propTypes.object,
    hasBtnAuth: propTypes.func,
    selectedRowIds: propTypes.array.def(() => []),
  });
  const emit = defineEmits(['save', 'add', 'remove', 'clearSelection', 'register']);
  const xToolbarRef = ref({} as VxeToolbarInstance);
  const prefixCls = `${inject('prefixCls')}-toolbar`;
  const boxClass = computed(() => [
    prefixCls,
    {
      [`${prefixCls}-collapsed`]: collapsed.value,
    },
  ]);
  // 是否收起
  const collapsed = ref(true);
  // 配置的按钮
  const btns = computed(() => {
    let { btn, btns } = props.toolbarConfig || {};
    btns = btn || btns || ['add', 'remove', 'clearSelection'];
    // 排除掉没有授权的按钮
    return btns.filter((btn) => {
      // 系统默认的批量删除编码配置为 batch_delete 此处需要兼容一下
      if (btn === 'remove') {
        //update-begin-author:taoyan date:2022-6-1 for: VUEN-1162 子表按钮没控制
        return hasBtnAuth(btn) && hasBtnAuth('batch_delete');
        //update-end-author:taoyan date:2022-6-1 for: VUEN-1162 子表按钮没控制
      }
      return hasBtnAuth(btn);
    });
  });
  const showAdd = computed(() => btns.value.includes('add'));
  const showSave = computed(() => btns.value.includes('save'));
  const showRemove = computed(() => btns.value.includes('remove'));
  // 配置的插槽
  const slots = computed(() => props.toolbarConfig?.slot || ['prefix', 'suffix']);
  const showPrefix = computed(() => slots.value.includes('prefix'));
  const showSuffix = computed(() => slots.value.includes('suffix'));
  // 是否显示清除选择按钮
  const showClearSelection = computed(() => {
    if (btns.value.includes('clearSelection')) {
      // 有禁用行时才显示清空选择按钮
      // 因为禁用行会阻止选择行，导致无法取消全选
      // return Object.keys(props.disabledRows).length > 0
    }
    return false;
  });
  // 是否显示展开收起按钮
  const showCollapse = computed(() => btns.value.includes('collapse'));
  // 按钮 size
  const btnSize = computed(() => (props.size === 'tiny' ? 'small' : null));

  onMounted(() => {
    // 注册 vxe-toolbar
    emit('register', {
      xToolbarRef,
    });
  });

  // 判断按钮是否已授权
  function hasBtnAuth(key: string) {
    return props.hasBtnAuth ? props.hasBtnAuth(key) : true;
  }

  /** 触发事件 */
  function trigger(name) {
    emit(name);
  }

  // 切换展开收起
  function toggleCollapse() {
    collapsed.value = !collapsed.value;
  }
</script>
