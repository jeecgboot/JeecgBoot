<template>
  <div :class="boxClass">
    <!-- 工具按钮 -->
    <div class="j-vxe-tool-button div" :size="btnSize">
      <slot v-if="showPrefix" name="toolbarPrefix" :size="btnSize"/>

      <a-button v-if="showAdd" icon="plus" @click="trigger('add')" :disabled="disabled" type="primary">新增</a-button>
      <a-button v-if="showSave" icon="save" @click="trigger('save')" :disabled="disabled">保存</a-button>
      <template v-if="selectedRowIds.length > 0">
        <a-popconfirm
          v-if="showRemove"
          :title="`确定要删除这 ${selectedRowIds.length} 项吗?`"
          @confirm="trigger('remove')"
        >
          <a-button icon="minus" :disabled="disabled">删除</a-button>
        </a-popconfirm>
        <template v-if="showClearSelection">
          <a-button icon="delete" @click="trigger('clearSelection')">清空选择</a-button>
        </template>
      </template>

      <slot v-if="showSuffix" name="toolbarSuffix" :size="btnSize"/>
      <a v-if="showCollapse" @click="toggleCollapse" style="margin-left: 4px">
        <span>{{ collapsed ? '展开' : '收起' }}</span>
        <a-icon :type="collapsed ? 'down' : 'up'"/>
      </a>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'JVxeToolbar',
    props: {
      toolbarConfig: Object,
      excludeCode: Array,
      size: String,
      disabled: Boolean,
      disabledRows: Object,
      selectedRowIds: Array,
    },
    data() {
      return {
        // 是否收起
        collapsed: true,
      }
    },
    computed: {
      boxClass() {
        return {
          'j-vxe-toolbar': true,
          'j-vxe-toolbar-collapsed': this.collapsed,
        }
      },

      btns() {
        let arr = this.toolbarConfig.btn || ['add', 'remove', 'clearSelection']
        let exclude = [...this.excludeCode]
        // TODO 需要将remove替换batch_delete
        // 系统默认的批量删除编码配置为 batch_delete 此处需要转化一下
        if(exclude.indexOf('batch_delete')>=0){
          exclude.push('remove')
        }
        // 按钮权限 需要去掉不被授权的按钮
        return arr.filter(item=>{
          return exclude.indexOf(item)<0
        })
      },
      slots() {
        return this.toolbarConfig.slot || ['prefix', 'suffix']
      },
      showPrefix() {
        return this.slots.includes('prefix')
      },
      showSuffix() {
        return this.slots.includes('suffix')
      },
      showAdd() {
        return this.btns.includes('add')
      },
      showSave() {
        return this.btns.includes('save')
      },
      showRemove() {
        return this.btns.includes('remove')
      },
      showClearSelection() {
        if (this.btns.includes('clearSelection')) {
          // 有禁用行时才显示清空选择按钮
          // 因为禁用行会阻止选择行，导致无法取消全选
          let length = Object.keys(this.disabledRows).length
          return length > 0
        }
        return false
      },
      showCollapse() {
        return this.btns.includes('collapse')
      },

      btnSize() {
        return this.size === 'tiny' ? 'small' : null
      },
    },
    methods: {
      /** 触发事件 */
      trigger(name) {
        this.$emit(name)
      },
      // 切换展开收起
      toggleCollapse() {
        this.collapsed = !this.collapsed
      },
    },
  }
</script>

<style lang="less">
  .j-vxe-toolbar-collapsed {
    [data-collapse] {
      display: none;
    }
  }

  .j-vxe-tool-button.div .ant-btn {
    margin-right: 8px;
  }
</style>