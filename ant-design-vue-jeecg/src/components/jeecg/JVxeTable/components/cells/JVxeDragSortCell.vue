<template>
  <a-dropdown :trigger="['click']">
    <div class="j-vxe-ds-icons">
      <a-icon type="align-left"/>
      <a-icon type="align-right"/>
    </div>

    <!--    <div class="j-vxe-ds-btns">-->
    <!--      <a-button icon="caret-up" size="small" :disabled="disabledMoveUp" @click="handleRowMoveUp"/>-->
    <!--      <a-button icon="caret-down" size="small" :disabled="disabledMoveDown" @click="handleRowMoveDown"/>-->
    <!--    </div>-->

    <a-menu slot="overlay">
      <a-menu-item key="0" :disabled="disabledMoveUp" @click="handleRowMoveUp">向上移</a-menu-item>
      <a-menu-item key="1" :disabled="disabledMoveDown" @click="handleRowMoveDown">向下移</a-menu-item>
      <a-menu-divider/>
      <a-menu-item key="3" @click="handleRowInsertDown">插入一行</a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxeDragSortCell',
    mixins: [JVxeCellMixins],
    computed: {
      // 排序结果保存字段
      dragSortKey() {
        return this.renderOptions.dragSortKey || 'orderNum'
      },
      disabledMoveUp() {
        return this.rowIndex === 0
      },
      disabledMoveDown() {
        return this.rowIndex === (this.rows.length - 1)
      },
    },
    methods: {
      /** 向上移 */
      handleRowMoveUp(event) {
        // event.target.blur()
        if (!this.disabledMoveUp) {
          this.trigger('rowMoveUp', this.rowIndex)
        }
      },
      /** 向下移 */
      handleRowMoveDown(event) {
        // event.target.blur()
        if (!this.disabledMoveDown) {
          this.trigger('rowMoveDown', this.rowIndex)
        }
      },
      /** 插入一行 */
      handleRowInsertDown() {
        this.trigger('rowInsertDown', this.rowIndex)
      },
    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      // 【功能开关】
      switches: {
        editRender: false
      },
    },
  }
</script>

<style lang="less">
  .j-vxe-ds-icons {
    position: relative;
    /*cursor: move;*/
    cursor: pointer;
    width: 14px;
    height: 100%;
    display: inline-block;

    .anticon-align-left,
    .anticon-align-right {
      position: absolute;
      top: 30%;
    }

    .anticon-align-left {
      left: 0;
    }

    .anticon-align-right {
      right: 0;
    }
  }

  .j-vxe-ds-btns {
    position: relative;
    cursor: pointer;
    width: 24px;
    height: 100%;
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-content: center;

    .ant-btn {
      border: none;

      z-index: 0;
      padding: 0;
      width: 100%;
      /*height: 30%;*/
      height: 40%;
      display: block;
      border-radius: 0;

      &:hover {
        z-index: 1;
        /*  height: 40%;*/

        /*  & .anticon-caret-up,*/
        /*  & .anticon-caret-down {*/
        /*    top: 2px;*/
        /*  }*/
      }

      &:last-child {
        margin-top: -1px;
      }

      & .anticon-caret-up,
      & .anticon-caret-down {
        vertical-align: top;
        position: relative;
        top: 0;
        transition: top 0.3s;
      }
    }
  }
</style>