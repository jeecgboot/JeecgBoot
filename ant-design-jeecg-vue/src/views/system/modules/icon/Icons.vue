<template>
  <a-modal
    v-model="show"
    :width="900"
    :keyboard="false"
    :closable="false"
    :centered="true"
    @ok="ok"
    @cancel="cancel"
    :maskClosable="false"
    :mask="false"
    okText="确认"
    cancelText="取消">
    <a-tabs>
      <a-tab-pane tab="方向性图标" key="1">
        <ul>
          <li v-for="icon in icons.directionIcons" :key="icon">
            <a-icon :type="icon" :title="icon" @click="chooseIcon(icon)" :class="{'active':activeIndex === icon}"/>
          </li>
        </ul>
      </a-tab-pane>
      <a-tab-pane tab="指示性图标" key="2">
        <ul>
          <li v-for="icon in icons.suggestionIcons" :key="icon">
            <a-icon :type="icon" :title="icon" @click="chooseIcon(icon)" :class="{'active':activeIndex === icon}"/>
          </li>
        </ul>
      </a-tab-pane>
      <a-tab-pane tab="编辑类图标" key="3">
        <ul>
          <li v-for="icon in icons.editIcons" :key="icon">
            <a-icon :type="icon" :title="icon" @click="chooseIcon(icon)" :class="{'active':activeIndex === icon}"/>
          </li>
        </ul>
      </a-tab-pane>
      <a-tab-pane tab="数据类图标" key="4">
        <ul>
          <li v-for="icon in icons.dataIcons" :key="icon">
            <a-icon :type="icon" :title="icon" @click="chooseIcon(icon)" :class="{'active':activeIndex === icon}"/>
          </li>
        </ul>
      </a-tab-pane>
      <a-tab-pane tab="网站通用图标" key="5">
        <ul>
          <li v-for="icon in icons.webIcons" :key="icon">
            <a-icon :type="icon" :title="icon" @click="chooseIcon(icon)" :class="{'active':activeIndex === icon}"/>
          </li>
        </ul>
      </a-tab-pane>
      <a-tab-pane tab="品牌和标识" key="6">
        <ul>
          <li v-for="icon in icons.logoIcons" :key="icon">
            <a-icon :type="icon" :title="icon" @click="chooseIcon(icon)" :class="{'active':activeIndex === icon}"/>
          </li>
        </ul>
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>
<script>
const directionIcons = ['step-backward', 'step-forward', 'fast-backward', 'fast-forward', 'shrink', 'arrows-alt', 'down', 'up', 'left', 'right', 'caret-up', 'caret-down', 'caret-left', 'caret-right', 'up-circle', 'down-circle', 'left-circle', 'right-circle', 'up-circle-o', 'down-circle-o', 'right-circle-o', 'left-circle-o', 'double-right', 'double-left', 'vertical-left', 'vertical-right', 'forward', 'backward', 'rollback', 'enter', 'retweet', 'swap', 'swap-left', 'swap-right', 'arrow-up', 'arrow-down', 'arrow-left', 'arrow-right', 'play-circle', 'play-circle-o', 'up-square', 'down-square', 'left-square', 'right-square', 'up-square-o', 'down-square-o', 'left-square-o', 'right-square-o', 'login', 'logout', 'menu-fold', 'menu-unfold', 'border-bottom', 'border-horizontal', 'border-inner', 'border-left', 'border-right', 'border-top', 'border-verticle', 'pic-center', 'pic-left', 'pic-right', 'radius-bottomleft', 'radius-bottomright', 'radius-upleft', 'radius-upright', 'fullscreen', 'fullscreen-exit']
const suggestionIcons = ['question', 'question-circle', 'plus', 'plus-circle', 'pause', 'pause-circle', 'minus', 'minus-circle', 'plus-square', 'minus-square', 'info', 'info-circle', 'exclamation', 'exclamation-circle', 'close', 'close-circle', 'close-square', 'check', 'check-circle', 'check-square', 'clock-circle', 'warning', 'issues-close', 'stop']
const editIcons = ['edit', 'form', 'copy', 'scissor', 'delete', 'snippets', 'diff', 'highlight', 'align-center', 'align-left', 'align-right', 'bg-colors', 'bold', 'italic', 'underline', 'strikethrough', 'redo', 'undo', 'zoom-in', 'zoom-out', 'font-colors', 'font-size', 'line-height', 'colum-height', 'dash', 'small-dash', 'sort-ascending', 'sort-descending', 'drag', 'ordered-list', 'radius-setting']
const dataIcons = ['area-chart', 'pie-chart', 'bar-chart', 'dot-chart', 'line-chart', 'radar-chart', 'heat-map', 'fall', 'rise', 'stock', 'box-plot', 'fund', 'sliders']
const webIcons = ['lock', 'unlock', 'bars', 'book', 'calendar', 'cloud', 'cloud-download', 'code', 'copy', 'credit-card', 'delete', 'desktop', 'download', 'ellipsis', 'file', 'file-text', 'file-unknown', 'file-pdf', 'file-word', 'file-excel', 'file-jpg', 'file-ppt', 'file-markdown', 'file-add', 'folder', 'folder-open', 'folder-add', 'hdd', 'frown', 'meh', 'smile', 'inbox', 'laptop', 'appstore', 'link', 'mail', 'mobile', 'notification', 'paper-clip', 'picture', 'poweroff', 'reload', 'search', 'setting', 'share-alt', 'shopping-cart', 'tablet', 'tag', 'tags', 'to-top', 'upload', 'user', 'video-camera', 'home', 'loading', 'loading-3-quarters', 'cloud-upload', 'star', 'heart', 'environment', 'eye', 'camera', 'save', 'team', 'solution', 'phone', 'filter', 'exception', 'export', 'customer-service', 'qrcode', 'scan', 'like', 'dislike', 'message', 'pay-circle', 'calculator', 'pushpin', 'bulb', 'select', 'switcher', 'rocket', 'bell', 'disconnect', 'database', 'compass', 'barcode', 'hourglass', 'key', 'flag', 'layout', 'printer', 'sound', 'usb', 'skin', 'tool', 'sync', 'wifi', 'car', 'schedule', 'user-add', 'user-delete', 'usergroup-add', 'usergroup-delete', 'man', 'woman', 'shop', 'gift', 'idcard', 'medicine-box', 'red-envelope', 'coffee', 'copyright', 'trademark', 'safety', 'wallet', 'bank', 'trophy', 'contacts', 'global', 'shake', 'api', 'fork', 'dashboard', 'table', 'profile', 'alert', 'audit', 'branches', 'build', 'border', 'crown', 'experiment', 'fire', 'money-collect', 'property-safety', 'read', 'reconciliation', 'rest', 'security-scan', 'insurance', 'interation', 'safety-certificate', 'project', 'thunderbolt', 'block', 'cluster', 'deployment-unit', 'dollar', 'euro', 'pound', 'file-done', 'file-exclamation', 'file-protect', 'file-search', 'file-sync', 'gateway', 'gold', 'robot', 'shopping']
const logoIcons = ['android', 'apple', 'windows', 'ie', 'chrome', 'github', 'aliwangwang', 'dingding', 'weibo-square', 'weibo-circle', 'taobao-circle', 'html5', 'weibo', 'twitter', 'wechat', 'youtube', 'alipay-circle', 'taobao', 'skype', 'qq', 'medium-workmark', 'gitlab', 'medium', 'linkedin', 'google-plus', 'dropbox', 'facebook', 'codepen', 'amazon', 'google', 'codepen-circle', 'alipay', 'ant-design', 'aliyun', 'zhihu', 'slack', 'slack-square', 'behance', 'behance-square', 'dribbble', 'dribbble-square', 'instagram', 'yuque', 'alibaba', 'yahoo']
export default {
  name: 'Icons',
  props: {
    iconChooseVisible: {
      default: false
    }
  },
  data () {
    return {
      icons: {
        directionIcons,
        suggestionIcons,
        editIcons,
        dataIcons,
        webIcons,
        logoIcons
      },
      choosedIcon: '',
      activeIndex: ''
    }
  },
  computed: {
    show: {
      get: function () {
        return this.iconChooseVisible
      },
      set: function () {
      }
    }
  },
  methods: {
    reset () {
      this.activeIndex = ''
    },
    chooseIcon (icon) {
      this.activeIndex = icon
      this.choosedIcon = icon
      this.$message.success(`选中 ${icon}`)
    },
    ok () {
      if (this.choosedIcon === '') {
        this.$message.warning('尚未选择任何图标')
        return
      }
      this.reset()
      this.$emit('choose', this.choosedIcon)
    },
    cancel () {
      this.reset()
      this.$emit('close')
    }
  }
}
</script>
<style lang="less" scoped>
@import "Icon";
</style>
