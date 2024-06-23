<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" width="70%">
    <a-form ref="formRef" :model="orderMainModel" :label-col="labelCol" :wrapper-col="wrapperCol" :rules="validatorRules">
      <a-row class="form-row" :gutter="16">
        <a-col :lg="8">
          <a-form-item label="订单号" name="orderCode">
            <a-input v-model:value="orderMainModel.orderCode" placeholder="请输入订单号" />
          </a-form-item>
        </a-col>
        <a-col :lg="8">
          <a-form-item label="订单类型">
            <a-select placeholder="请选择订单类型" v-model:value="orderMainModel.ctype">
              <a-select-option value="1">国内订单</a-select-option>
              <a-select-option value="2">国际订单</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :lg="8">
          <a-form-item label="订单日期">
            <a-date-picker showTime valueFormat="YYYY-MM-DD HH:mm:ss" v-model:value="orderMainModel.orderDate" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row class="form-row" :gutter="16">
        <a-col :lg="8">
          <a-form-item label="订单金额">
            <a-input v-model:value="orderMainModel.orderMoney" placeholder="请输入订单金额" />
          </a-form-item>
        </a-col>
        <a-col :lg="8">
          <a-form-item label="订单备注">
            <a-input v-model:value="orderMainModel.content" placeholder="请输入订单备注" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-tabs defaultActiveKey="1">
        <a-tab-pane tab="客户信息" key="1">
          <div>
            <a-row type="flex" style="margin-bottom: 10px" :gutter="16">
              <a-col :span="5">客户名</a-col>
              <a-col :span="5">性别</a-col>
              <a-col :span="6">身份证号码</a-col>
              <a-col :span="6">手机号</a-col>
              <a-col :span="2">操作</a-col>
            </a-row>
            <a-row type="flex" style="margin-bottom: 10px" :gutter="16" v-for="(item, index) in orderMainModel.jeecgOrderCustomerList" :key="index">
              <a-col :span="6" style="display: none">
                <a-form-item>
                  <a-input placeholder="id" v-model:value="item.id" />
                </a-form-item>
              </a-col>
              <a-col :span="5">
                <a-form-item>
                  <a-input placeholder="客户名" v-model:value="item.name" />
                </a-form-item>
              </a-col>
              <a-col :span="5">
                <a-form-item>
                  <a-select placeholder="性别" v-model:value="item.sex">
                    <a-select-option value="1">男</a-select-option>
                    <a-select-option value="2">女</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item
                  :name="['jeecgOrderCustomerList', index, 'idcard']"
                  :rules="[{ required: true, message: '请输入身份证号', trigger: 'blur' }]"
                  :key="index"
                >
                  <a-input placeholder="身份证号" v-model:value="item.idcard" />
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item :name="['jeecgOrderCustomerList', index, 'telphone']">
                  <a-input placeholder="手机号" v-model:value="item.telphone" />
                </a-form-item>
              </a-col>
              <a-col :span="2">
                <a-form-item>
                  <Icon icon="ant-design:minus-outlined" @click="delRowCustom(index)" style="fontsize: 20px" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-button type="dashed" style="width: 98%; margin-top: 10px" @click="addRowCustom">
              <Icon icon="ph:plus-bold" />
              添加客户信息
            </a-button>
          </div>
        </a-tab-pane>

        <a-tab-pane tab="机票信息" key="2" forceRender>
          <a-row type="flex" style="margin-bottom: 10px" :gutter="16">
            <a-col :span="6">航班号</a-col>
            <a-col :span="6">航班时间</a-col>
            <a-col :span="6">操作</a-col>
          </a-row>
          <a-row type="flex" style="margin-bottom: 10px" :gutter="16" v-for="(item, index) in orderMainModel.jeecgOrderTicketList" :key="index">
            <a-col :span="6" style="display: none">
              <a-form-item>
                <a-input placeholder="id" v-model:value="item.id" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item
                :name="['jeecgOrderTicketList', index, 'ticketCode']"
                :rules="{ required: true, message: '请输入航班号', trigger: 'blur' }"
              >
                <a-input placeholder="航班号" v-model:value="item.ticketCode" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item>
                <a-date-picker placeholder="航班时间" valueFormat="YYYY-MM-DD" v-model:value="item.tickectDate" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item>
                <Icon icon="ant-design:minus-outlined" @click="delRowTicket(index)" style="fontsize: 20px" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-button type="dashed" style="width: 98%; margin-top: 10px" @click="addRowTicket">
            <Icon icon="ph:plus-bold" />
            添加机票信息
          </a-button>
        </a-tab-pane>
      </a-tabs>
    </a-form>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { ValidateErrorEntity } from 'ant-design-vue/es/form/interface';
  import { saveOrUpdate } from './jvxetable/jvxetable.api';
  import { orderCustomerList, orderTicketList } from './api';

  export default defineComponent({
    name: 'tableModal',
    components: { BasicModal },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const formRef = ref();
      const labelCol = reactive({
        xs: { span: 24 },
        sm: { span: 5 },
      });
      const wrapperCol = reactive({
        xs: { span: 24 },
        sm: { span: 16 },
      });
      const validatorRules = {
        orderCode: [{ required: true, message: '订单号不能为空', trigger: 'blur' }],
      };
      const orderMainModel = reactive({
        id: null,
        orderCode: '',
        orderMoney: '',
        ctype: '',
        content: '',
        jeecgOrderCustomerList: [],
        jeecgOrderTicketList: [],
      });
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        reset();
        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          Object.assign(orderMainModel, data.record);
          let params = { id: orderMainModel.id };
          const customerList = await orderCustomerList(params);
          orderMainModel.jeecgOrderCustomerList = customerList;
          const ticketList = await orderTicketList(params);
          orderMainModel.jeecgOrderTicketList = ticketList;
        }
      });
      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

      //动态添加行
      function addRowCustom() {
        orderMainModel.jeecgOrderCustomerList.push({});
      }

      //删除行
      function delRowCustom(index) {
        orderMainModel['jeecgOrderCustomerList'].splice(index, 1);
        orderMainModel.jeecgOrderCustomerList.splice(index, 1);
      }
      function reset() {
        orderMainModel.id = null;
        orderMainModel.orderCode = '';
        orderMainModel.orderMoney = '';
        orderMainModel.orderDate = null;
        orderMainModel.ctype = '';
        orderMainModel.content = '';
        orderMainModel.jeecgOrderCustomerList = [];
        orderMainModel.jeecgOrderTicketList = [];
      }
      function addRowTicket() {
        orderMainModel.jeecgOrderTicketList.push({});
      }

      //删除机票
      function delRowTicket(index) {
        orderMainModel['jeecgOrderTicketList'].splice(index, 1);
        orderMainModel.jeecgOrderTicketList.splice(index, 1);
      }

      async function handleSubmit() {
        formRef.value
          .validate()
          .then(async () => {
            try {
              console.log('formData', JSON.stringify(orderMainModel));
              setModalProps({ confirmLoading: true });
              await saveOrUpdate(orderMainModel, unref(isUpdate));
              closeModal();
              emit('success');
            } finally {
              setModalProps({ confirmLoading: false });
            }
          })
          .catch((error: ValidateErrorEntity<any>) => {
            console.log('error', error);
          });
      }

      return {
        formRef,
        validatorRules,
        orderMainModel,
        registerModal,
        getTitle,
        labelCol,
        wrapperCol,
        addRowCustom,
        delRowCustom,
        addRowTicket,
        delRowTicket,
        handleSubmit,
      };
    },
  });
</script>
<style scoped>
  .ant-btn {
    padding: 0 10px;
    margin-left: 3px;
  }

  .ant-form-item-control {
    line-height: 0px;
  }

  /** 主表单行间距 */
  .ant-form .ant-form-item {
    margin-bottom: 10px;
  }

  /** Tab页面行间距 */
  .ant-tabs-content .ant-form-item {
    margin-bottom: 0px;
  }
</style>
