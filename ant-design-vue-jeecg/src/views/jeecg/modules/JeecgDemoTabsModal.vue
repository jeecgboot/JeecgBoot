<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-card class="card"  :bordered="false">
          <a-row class="form-row" :gutter="16">
            <a-col :lg="8">
              <a-form-item label="任务名">
                <a-input placeholder="请输入任务名称"  v-decorator="[ 'task.name', {rules: [{ required: true, message: '请输入任务名称', whitespace: true}]} ]"/>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="任务描述">
                <a-input placeholder="请输入任务描述"  v-decorator="['task.description', {rules: [{ required: true, message: '请输入任务描述', whitespace: true}]} ]"/>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="执行人">
                <a-select placeholder="请选择执行人" v-decorator="['task.executor',{rules: [{ required: true, message: '请选择执行人'}]}  ]">
                  <a-select-option value="黄丽丽">黄丽丽</a-select-option>
                  <a-select-option value="李大刀">李大刀</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="16">
            <a-col :lg="8">
              <a-form-item label="责任人">
                <a-select placeholder="请选择责任人" v-decorator="['task.manager',  {rules: [{ required: true, message: '请选择责任人'}]} ]">
                  <a-select-option value="王伟">王伟</a-select-option>
                  <a-select-option value="李红军">李红军</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="提醒时间">
                <a-time-picker style="width: 100%" v-decorator="['task.time', {rules: [{ required: true, message: '请选择提醒时间'}]} ]"/>
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item
                label="任务类型">
                <a-select placeholder="请选择任务类型" v-decorator="['task.type', {rules: [{ required: true, message: '请选择任务类型'}]} ]">
                  <a-select-option value="定时执行">定时执行</a-select-option>
                  <a-select-option value="周期执行">周期执行</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>

        <a-tabs defaultActiveKey="1" >
          <a-tab-pane tab="Tab 1" key="1">

            <a-table :columns="columns" :dataSource="data" :pagination="false" size="middle">
              <template v-for="(col, i) in ['name', 'workId', 'department']" :slot="col" slot-scope="text, record, index">
                <a-tooltip  title="必填项" :defaultVisible="false" :overlayStyle="{ color: 'red' }">
                  <a-input :key="col" v-if="record.editable" style="margin: -5px 0"  :value="text" :placeholder="columns[i].title" @change="e => handlerRowChange(e.target.value, record.key, col)"/>
                <template v-else>{{ text }}</template>
                </a-tooltip>
              </template>
              <template slot="operation" slot-scope="text, record, index">
                <template v-if="record.editable">
                  <span v-if="record.isNew">
                    <a @click="saveRow(record.key)">添加</a>
                    <a-divider type="vertical"/>
                    <a-popconfirm title="是否要删除此行？" @confirm="removeRow(record.key)"><a>删除</a></a-popconfirm>
                  </span>
                  <span v-else>
                    <a @click="saveRow(record.key)">保存</a>
                    <a-divider type="vertical"/>
                    <a @click="cancelEditRow(record.key)">取消</a>
                  </span>
                </template>
                <span v-else>
                  <a @click="editRow(record.key)">编辑</a>
                  <a-divider type="vertical"/>
                  <a-popconfirm title="是否要删除此行？" @confirm="removeRow(record.key)"><a>删除</a></a-popconfirm>
                </span>
              </template>
            </a-table>

            <a-button style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newRow">新增成员</a-button>
          </a-tab-pane>
          <a-tab-pane tab="Tab 2" key="2" forceRender>
            Content of Tab Pane 2
          </a-tab-pane>
          <a-tab-pane tab="Tab 3" key="3">Content of Tab Pane 3</a-tab-pane>
        </a-tabs>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "JeecgDemoTabsModal",
    data() {
      return {
        title: "操作",
        visible: false,
        model: {},
        // table
        columns: [
          {
            title: '成员姓名',
            dataIndex: 'name',
            key: 'name',
            width: '20%',
            scopedSlots: {customRender: 'name'}
          },
          {
            title: '工号',
            dataIndex: 'workId',
            key: 'workId',
            width: '20%',
            scopedSlots: {customRender: 'workId'}
          },
          {
            title: '所属部门',
            dataIndex: 'department',
            key: 'department',
            width: '40%',
            scopedSlots: {customRender: 'department'}
          },
          {
            title: '操作',
            key: 'action',
            scopedSlots: {customRender: 'operation'}
          }
        ],
        data: [
          {
            key: '1',
            name: '小明',
            workId: '001',
            editable: false,
            department: '行政部'
          },
          {
            key: '2',
            name: '李莉',
            workId: '002',
            editable: false,
            department: 'IT部'
          },
          {
            key: '3',
            name: '王小帅',
            workId: '003',
            editable: false,
            department: '财务部'
          }
        ],

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {},
        url: {
          add: "/test/jeecgDemo/add",
          edit: "/test/jeecgDemo/edit",
        },
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'name', 'keyWord', 'sex', 'age', 'email', 'content'))
          //时间格式化
          this.form.setFieldsValue({punchTime: this.model.punchTime ? moment(this.model.punchTime, 'YYYY-MM-DD HH:mm:ss') : null})
          this.form.setFieldsValue({birthday: this.model.birthday ? moment(this.model.birthday) : null})
        });

      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.punchTime = formData.punchTime ? formData.punchTime.format('YYYY-MM-DD HH:mm:ss') : null;
            formData.birthday = formData.birthday ? formData.birthday.format() : null;

            console.log(formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel() {
        this.close()
      },
      newRow () {
        // 通过时间戳生成 UUID
        let uuid = Math.round(new Date().getTime()).toString();
        console.log('uuid: '+ uuid)
        this.data.push({
          key: uuid,
          name: '',
          workId: '',
          department: '',
          editable: true,
          isNew: true
        })
      },
      removeRow (key) {
        const newData = this.data.filter(item => item.key !== key)
        this.data = newData
      },
      saveRow (key) {
        let target = this.data.filter(item => item.key === key)[0]
        target.editable = false
        target.isNew = false
      },
      handlerRowChange (value, key, column) {
        const newData = [...this.data]
        const target = newData.filter(item => key === item.key)[0]
        if (target) {
          target[column] = value
          this.data = newData
        }
      },
      editRow (key) {
        let target = this.data.filter(item => item.key === key)[0]
        target.editable = !target.editable
      },
      cancelEditRow (key) {
        let target = this.data.filter(item => item.key === key)[0]
        target.editable = false
      },
    }
  }
</script>

<style scoped>
  .ant-modal-body {
    padding: 8px!important;
  }
</style>