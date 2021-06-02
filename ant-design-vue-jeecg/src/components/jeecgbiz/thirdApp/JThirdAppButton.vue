<template>
  <span v-if="syncToApp || syncToLocal">
    <j-third-app-dropdown v-if="enabledTypes.wechatEnterprise" type="wechatEnterprise" name="企微" v-bind="bindAttrs" v-on="bindEvents"/>
    <j-third-app-dropdown v-if="enabledTypes.dingtalk" type="dingtalk" name="钉钉" v-bind="bindAttrs" v-on="bindEvents"/>
  </span>
  <span v-else>未设置任何同步方向</span>
</template>

<script>
import { getAction } from '@/api/manage'
import { cloneObject } from '@/utils/util'
import JThirdAppDropdown from './JThirdAppDropdown'

const backEndUrl = {
  // 获取启用的第三方App
  getEnabledType: '/sys/thirdApp/getEnabledType',
  // 企业微信
  wechatEnterprise: {
    user: '/sys/thirdApp/sync/wechatEnterprise/user',
    depart: '/sys/thirdApp/sync/wechatEnterprise/depart',
  },
  // 钉钉
  dingtalk: {
    user: '/sys/thirdApp/sync/dingtalk/user',
    depart: '/sys/thirdApp/sync/dingtalk/depart',
  }
}

export default {
  name: 'JThirdAppButton',
  components: {JThirdAppDropdown},
  props: {
    // 同步类型，可以是 user、depart
    bizType: {
      type: String,
      required: true,
    },
    // 是否允许同步到第三方APP
    syncToApp: Boolean,
    // 是否允许第三方APP同步到本地
    syncToLocal: Boolean,
    // 选择的行
    selectedRowKeys: Array,
  },
  data() {
    return {
      enabledTypes: {},
      attrs: {
        dingtalk: {},
      },
    }
  },
  computed: {
    bindAttrs() {
      return {
        syncToApp: this.syncToApp,
        syncToLocal: this.syncToLocal
      }
    },
    bindEvents() {
      return {
        'to-app': this.onToApp,
        'to-local': this.onToLocal,
      }
    },
  },
  created() {
    this.loadEnabledTypes()
  },
  methods: {
    handleMenuClick() {
      console.log(arguments)
    },
    onToApp(e) {
      this.doSync(e.type, '/toApp')
    },
    onToLocal(e) {
      this.doSync(e.type, '/toLocal')
    },
    // 获取启用的第三方App
    async loadEnabledTypes() {
      this.enabledTypes = await loadEnabledTypes()
    },
    // 开始同步第三方App
    doSync(type, direction) {
      let urls = backEndUrl[type]
      if (!(urls && urls[this.bizType])) {
        console.warn('配置出错')
        return
      }
      let url = urls[this.bizType] + direction

      let selectedRowKeys = this.selectedRowKeys
      let content = '确定要开始同步全部数据吗？可能花费较长时间！'
      if (Array.isArray(selectedRowKeys) && selectedRowKeys.length > 0) {
        content = `确定要开始同步这 ${selectedRowKeys.length} 项吗？`
      } else {
        selectedRowKeys = []
      }
      return new Promise((resolve, reject) => {
        let model = this.$confirm({
          title: '同步',
          content,
          onOk: () => {
            model.update({
              keyboard: false,
              okText: '同步中…',
              cancelButtonProps: {props: {disabled: true}}
            })
            return getAction(url, {
              ids: selectedRowKeys.join(',')
            }).then(res => {
              let options = null
              if (res.result) {
                options = {
                  width: 600,
                  title: res.message,
                  content: (h) => {
                    let nodes
                    let successInfo = [
                      `成功信息如下：`,
                      this.renderTextarea(h, res.result.successInfo.map((v, i) => `${i + 1}. ${v}`).join('\n')),
                    ]
                    if (res.success) {
                      nodes = [
                        ...successInfo,
                        h('br'),
                        `无失败信息！`,
                      ]
                    } else {
                      nodes = [
                        `失败信息如下：`,
                        this.renderTextarea(h, res.result.failInfo.map((v, i) => `${i + 1}. ${v}`).join('\n')),
                        h('br'),
                        ...successInfo,
                      ]
                    }
                    return nodes
                  }
                }
              }
              if (res.success) {
                if (options != null) {
                  this.$success(options)
                } else {
                  this.$message.success(res.message)
                }
                this.$emit('sync-ok')
              } else {
                if (options != null) {
                  this.$warning(options)
                } else {
                  this.$message.warning(res.message)
                }
                this.$emit('sync-error')
              }
            }).catch(() => model.destroy()).finally(() => {
              resolve()
              this.$emit('sync-finally', {
                type,
                direction,
                isToApp: direction === '/toApp',
                isToLocal: direction === '/toLocal',
              })
            })
          },
          onCancel() {
            resolve()
          },
        })
      })
    },
    renderTextarea(h, value) {
      return h('a-textarea', {
        props: {
          value: value,
          readOnly: true,
          autosize: {minRows: 5, maxRows: 10},
        },
        style: {
          // 关闭textarea的自动换行，使其可以左右滚动
          whiteSpace: 'pre',
          overflow: 'auto',
        }
      })
    }
  },
}

// 启用了哪些第三方App（在此缓存）
let enabledTypes = null

// 获取启用的第三方App
export async function loadEnabledTypes() {
  // 获取缓存
  if (enabledTypes != null) {
    return cloneObject(enabledTypes)
  } else {
    let {success, result} = await getAction(backEndUrl.getEnabledType)
    if (success) {
      // 在此缓存
      enabledTypes = cloneObject(result)
      return result
    } else {
      console.warn('getEnabledType查询失败：', res)
    }
  }
  return {}
}
</script>

<style scoped>

</style>