<template>
  <a-modal
    ref="modal"
    class="j-modal-box"
    :class="{'fullscreen':innerFullscreen,'no-title':isNoTitle,'no-footer':isNoFooter,}"
    :visible="visible"
    v-bind="_attrs"
    v-on="$listeners"
    @ok="handleOk"
    @cancel="handleCancel"
  >

    <slot></slot>

    <template v-if="!isNoTitle" slot="title">
      <a-row class="j-modal-title-row" type="flex">
        <a-col class="left">
          <slot name="title">{{ title }}</slot>
        </a-col>
        <a-col v-if="switchFullscreen" class="right" @click="toggleFullscreen">
          <a-button class="ant-modal-close ant-modal-close-x" ghost type="link" :icon="fullscreenButtonIcon"/>
        </a-col>
      </a-row>
    </template>

    <!-- 处理 scopedSlots -->
    <template v-for="slotName of scopedSlotsKeys" :slot="slotName">
      <slot :name="slotName"></slot>
    </template>

    <!-- 处理 slots -->
    <template v-for="slotName of slotsKeys" v-slot:[slotName]>
      <slot :name="slotName"></slot>
    </template>

  </a-modal>
</template>

<script>

  export default {
    name: 'JModal',
    props: {
      title: String,
      // 可使用 .sync 修饰符
      visible: Boolean,
      // 是否全屏弹窗，当全屏时无论如何都会禁止 body 滚动。可使用 .sync 修饰符
      fullscreen: {
        type: Boolean,
        default: true
      },
      // 是否允许切换全屏（允许后右上角会出现一个按钮）
      switchFullscreen: {
        type: Boolean,
        default: false
      },
    },
    data() {
      return {
        // 内部使用的 slots ，不再处理
        usedSlots: ['title'],
        // 实际控制是否全屏的参数
        innerFullscreen: this.fullscreen,
      }
    },
    computed: {
      // 一些未处理的参数或特殊处理的参数绑定到 a-modal 上
      _attrs() {
        let attrs = { ...this.$attrs }
        // 如果全屏就将宽度设为 100%
        if (this.innerFullscreen) {
          attrs['width'] = '100%'
        }
        return attrs
      },
      isNoTitle() {
        return !this.title && !this.allSlotsKeys.includes('title')
      },
      isNoFooter() {
        return this._attrs['footer'] === null
      },
      slotsKeys() {
        return Object.keys(this.$slots).filter(key => !this.usedSlots.includes(key))
      },
      scopedSlotsKeys() {
        return Object.keys(this.$scopedSlots).filter(key => !this.usedSlots.includes(key))
      },
      allSlotsKeys() {
        return this.slotsKeys.concat(this.scopedSlotsKeys)
      },
      // 切换全屏的按钮图标
      fullscreenButtonIcon() {
        return this.innerFullscreen ? 'fullscreen' : 'fullscreen-exit'
      },
    },
    watch: {
      visible() {
        if (this.visible) {
          this.innerFullscreen = this.fullscreen
        }
      },
      innerFullscreen(val) {
        this.$emit('update:fullscreen', val)
      },
    },
    methods: {

      close() {
        this.$emit('update:visible', false)
      },

      handleOk() {
        this.close()
      },
      handleCancel() {
        this.close()
      },

      /** 切换全屏 */
      toggleFullscreen() {
        this.innerFullscreen = !this.innerFullscreen
      },

    }
  }
</script>

<style lang="less">
  .j-modal-box {

    &.fullscreen {
      top: 0;
      left: 0;
      padding: 0;

      height: 100vh;

      & .ant-modal-content {
        height: 100vh;
        border-radius: 0;

        & .ant-modal-body {
          /* title 和 footer 各占 55px */
          height: calc(100% - 55px - 55px);
          overflow: auto;
        }
      }

      &.no-title, &.no-footer {
        .ant-modal-body {
          height: calc(100% - 55px);
        }
      }

      &.no-title.no-footer {
        .ant-modal-body {
          height: 100%;
        }
      }

    }

    .j-modal-title-row {
      .left {
        width: calc(100% - 56px - 56px);
      }

      .right {
        width: 56px;

        .ant-modal-close {
          right: 56px;
          color: rgba(0, 0, 0, 0.45);

          &:hover {
            color: rgba(0, 0, 0, 0.75);
          }

        }
      }
    }

  }
</style>