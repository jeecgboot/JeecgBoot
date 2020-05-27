<template>
  <a-menu :style="style" class="contextmenu" v-show="visible" @click="handleClick" :selectedKeys="selectedKeys">
    <a-menu-item :key="item.key" v-for="item in itemList">
      <a-icon role="menuitemicon" v-if="item.icon" :type="item.icon" />{{ item.text }}
    </a-menu-item>
  </a-menu>
</template>

<script>
  export default {
    name: 'Contextmenu',
    props: {
      visible: {
        type: Boolean,
        required: false,
        default: false
      },
      itemList: {
        type: Array,
        required: true,
        default: () => []
      }
    },
    data () {
      return {
        left: 0,
        top: 0,
        target: null,
        selectedKeys: []
      }
    },
    computed: {
      style () {
        return {
          left: this.left + 'px',
          top: this.top + 'px'
        }
      }
    },
    created () {
      window.addEventListener('mousedown', e => this.closeMenu(e))
      window.addEventListener('contextmenu', e => this.setPosition(e))
    },
    methods: {
      closeMenu (e) {
        if (this.visible === true && ['menuitemicon', 'menuitem'].indexOf(e.target.getAttribute('role')) < 0) {
          this.$emit('update:visible', false)
        }
      },
      setPosition (e) {
        this.left = e.clientX
        this.top = e.clientY
        this.target = e.target
      },
      handleClick ({key}) {
        this.$emit('select', key, this.target)
        this.$emit('update:visible', false)
      }
    }
  }
</script>

<style lang="less" scoped>
  .contextmenu{
    position: fixed;
    z-index: 1;
    border: 1px solid #9e9e9e;
    border-radius: 4px;
    box-shadow: 2px 2px 10px #aaaaaa !important;
  }
</style>
