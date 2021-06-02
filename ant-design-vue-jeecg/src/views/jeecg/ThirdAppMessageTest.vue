<template>
  <a-card :bordered="false">
    <a-spin :spinning="loading">
      <a-form-model ref="form" :model="model" :rules="rules">
        <a-tabs>
          <a-tab-pane tab="消息选项" key="1">
            <a-form-model-item label="测试APP" prop="app" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-model="model.app" placeholder="请选择测试APP" :options="appOptions"/>
            </a-form-model-item>
            <a-form-model-item label="发送给所有人" prop="sendAll" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-switch checkedChildren="是" unCheckedChildren="否" v-model="model.sendAll" @change="onSendAllChange"/>
            </a-form-model-item>
            <a-form-model-item label="接收人" prop="receiver" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-multi-user v-model="model.receiver" :disabled="model.sendAll" placeholder="请选择接收人"/>
            </a-form-model-item>
            <a-form-model-item label="消息内容" prop="content" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea :rows="5" v-model="model.content" placeholder="请输入消息内容"/>
            </a-form-model-item>
            <div style="text-align: center;">
              <a-button type="primary" size="large" @click="onSend" style="width: 120px;">发送</a-button>
            </div>
          </a-tab-pane>
        </a-tabs>

        <a-tabs>
          <a-tab-pane tab="测试结果（刷新自动清空）" key="1">
            <a-table
              rowKey="id"
              bordered
              size="middle"
              :columns="columns"
              :dataSource="dataSource"
            >
              <div slot="action" slot-scope="text, record">
                <template v-if="record.app==='DINGTALK'">
                  <a-popconfirm v-if="!record.recalled" title="确定吗？" @confirm="handleRecall(record)">
                    <a @click="">撤回</a>
                  </a-popconfirm>
                  <span v-else>已撤回</span>
                </template>
                <template v-else>-</template>
              </div>
            </a-table>

          </a-tab-pane>
        </a-tabs>
      </a-form-model>
    </a-spin>
  </a-card>
</template>

<script>
import { loadEnabledTypes } from '@/components/jeecgbiz/thirdApp/JThirdAppButton'
import { postAction } from '@/api/manage'
import { randomUUID } from '@/utils/util'

export default {
  name: 'ThirdAppMessageTest',
  data() {
    return {
      loading: false,
      labelCol: {span: 6},
      wrapperCol: {span: 12},
      model: {
        sendAll: false,
      },
      enabledTypes: {},
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: (t, r, index) => {
            return this.dataSource.length - index
          }
        },
        {
          title: '测试APP',
          align: 'center',
          dataIndex: 'app',
          customRender: (t, r, index) => {
            if (t === 'WECHAT_ENTERPRISE') {
              return '企业微信'
            }
            if (t === 'DINGTALK') {
              return '钉钉'
            } else {
              return t
            }
          }
        },
        {
          title: '接收人',
          align: 'center',
          dataIndex: 'receiver',
          customRender: (t, r, index) => {
            return r.sendAll ? '【全体】' : t
          }
        },
        {
          title: '消息内容',
          align: 'center',
          dataIndex: 'content'
        },
        {
          title: 'response',
          align: 'center',
          dataIndex: 'response'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 80,
          scopedSlots: {customRender: 'action'}
        }
      ],
      dataSource: [],
    }
  },
  computed: {
    rules() {
      return {
        app: [{required: true, message: '请选择测试APP'}],
        url: [{required: this.show, message: '请输入菜单路径!'}],
        receiver: [{required: !this.model.sendAll, message: '请选择接收人'}],
        content: [{required: true, message: '消息内容不能为空'}]
      }
    },
    appOptions() {
      return [
        {
          label: `企业微信${this.enabledTypes.wechatEnterprise ? '' : '（已禁用）'}`,
          value: 'WECHAT_ENTERPRISE',
          disabled: !this.enabledTypes.wechatEnterprise
        },
        {
          label: `钉钉${this.enabledTypes.dingtalk ? '' : '（已禁用）'}`,
          value: 'DINGTALK',
          disabled: !this.enabledTypes.dingtalk
        },
      ]
    },
  },
  created() {
    this.loadEnabledTypes()
  },
  methods: {

    // 获取启用的第三方App
    async loadEnabledTypes() {
      this.enabledTypes = await loadEnabledTypes()
    },

    onSendAllChange() {
      this.$refs.form.clearValidate('receiver')
    },

    onSend() {
      this.$refs.form.validate((ok, err) => {
        if (ok) {
          this.loading = true
          postAction('/sys/thirdApp/sendMessageTest', this.model).then(({success, result, message}) => {
            if (success) {
              let response = ''
              try {
                response = JSON.stringify(result)
              } catch (e) {
                response = result
              }
              this.dataSource.unshift(Object.assign({id: randomUUID()}, this.model, {response}))
            } else {
              this.$message.warning(message)
            }
          }).finally(() => this.loading = false)
        }
      })
    },

    handleRecall(record) {
      try {
        let response = JSON.parse(record.response)
        postAction('/sys/thirdApp/recallMessageTest', {
          app: record.app,
          msg_task_id: response.result
        }).then(res => {
          if (res.success) {
            this.$set(record, 'recalled', true)
            this.$message.success(res.message)
          } else {
            this.$message.warning(res.message)
          }
        }).catch(e => this.$message.warning(e.message || e))
      } catch (e) {
        this.$message.warning(e.message || e)
      }
    },

  },
}
</script>

<style scoped>

</style>