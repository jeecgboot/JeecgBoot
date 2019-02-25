import Menu from 'ant-design-vue/es/menu'
import Icon from 'ant-design-vue/es/icon'

const { Item, SubMenu } = Menu

export default {
  name: 'SMenu',
  props: {
    menu: {
      type: Array,
      required: true
    },
    theme: {
      type: String,
      required: false,
      default: 'dark'
    },
    mode: {
      type: String,
      required: false,
      default: 'inline'
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data () {
    return {
      openKeys: [],
      selectedKeys: [],
      cachedOpenKeys: []
    }
  },
  computed: {
    rootSubmenuKeys: (vm) => {
      let keys = []
      vm.menu.forEach(item => keys.push(item.path))
      return keys
    }
  },
  created () {
    this.updateMenu()
  },
  watch: {
    collapsed (val) {
      if (val) {
        this.cachedOpenKeys = this.openKeys
        this.openKeys = []
      } else {
        this.openKeys = this.cachedOpenKeys
      }
    },
    '$route': function () {
      this.updateMenu()
    }
  },
  methods: {
    renderIcon: function (h, icon) {
      return icon === 'none' || icon === undefined ? null
        : h(Icon, { props: { type: icon !== undefined ? icon : '' } })
    },
    renderMenuItem: function (h, menu, pIndex, index) {
      return h(Item, { key: menu.path ? menu.path : 'item_' + pIndex + '_' + index },
        [
          h(
            'router-link',
            { attrs: { to: { name: menu.name } } },
            [
              this.renderIcon(h, menu.meta.icon),
              h('span', [ menu.meta.title ])
            ]
          )
        ]
      )
    },
    renderSubMenu: function (h, menu, pIndex, index) {
      const this2_ = this;
      let subItem = [ h('span',
        { slot: 'title' },
        [
          this.renderIcon(h, menu.meta.icon),
          h('span', [ menu.meta.title ])
        ]
      ) ]
      let itemArr = []
      let pIndex_ = pIndex + '_' + index
      if (!menu.alwaysShow) {
        menu.children.forEach(function (item, i) {
          itemArr.push(this2_.renderItem(h, item, pIndex_, i))
        })
      }
      return h(
        SubMenu,
        { key: menu.path ? menu.path : 'submenu_' + pIndex + '_' + index },
        subItem.concat(itemArr)
      )
    },
    renderItem: function (h, menu, pIndex, index) {
      if (!menu.hidden) {
        return menu.children && !menu.alwaysShow ? this.renderSubMenu(h, menu, pIndex, index) : this.renderMenuItem(h, menu, pIndex, index)
      }
    },
    renderMenu: function (h, menuTree) {
      const this2_ = this
      let menuArr = []
      menuTree.forEach(function (menu, i) {
        if (!menu.hidden) {
          menuArr.push(this2_.renderItem(h, menu, '0', i))
        }
      })
      return menuArr
    },
    onOpenChange (openKeys) {
      const latestOpenKey = openKeys.find(key => this.openKeys.indexOf(key) === -1)
      if (this.rootSubmenuKeys.indexOf(latestOpenKey) === -1) {
        this.openKeys = openKeys
      } else {
        this.openKeys = latestOpenKey ? [ latestOpenKey ] : []
      }
    },
    updateMenu () {
      let routes = this.$route.matched.concat()
      if (routes.length >= 4 && this.$route.meta.hidden) {
        routes.pop()
        this.selectedKeys = [ routes[2].path ]
      } else {
        this.selectedKeys = [ routes.pop().path ]
      }

      let openKeys = []
      if (this.mode === 'inline') {
        routes.forEach((item) => {
          openKeys.push(item.path)
        })
      }

      this.collapsed ? this.cachedOpenKeys = openKeys : this.openKeys = openKeys
    }
  },
  render (h) {
    return h(
      Menu,
      {
        props: {
          theme: this.$props.theme,
          mode: this.$props.mode,
          openKeys: this.openKeys,
          selectedKeys: this.selectedKeys
        },
        on: {
          openChange: this.onOpenChange,
          select: (obj) => {
            this.selectedKeys = obj.selectedKeys
            this.$emit('select', obj)
          }
        }
      }, this.renderMenu(h, this.menu)
    )
  }
}