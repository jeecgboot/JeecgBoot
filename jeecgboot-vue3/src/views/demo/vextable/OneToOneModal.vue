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
          <a-row class="form-row" :gutter="16">
            <a-col :lg="8">
              <a-form-item label="客户姓名">
                <a-input v-model:value="orderMainModel.jeecgOrderCustomerList.name" placeholder="请输入客户姓名" />
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="手机号">
                <a-input v-model:value="orderMainModel.jeecgOrderCustomerList.telphone" placeholder="请输入手机号" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-tab-pane>

        <a-tab-pane tab="机票信息" key="2" forceRender>
          <a-row class="form-row" :gutter="16">
            <a-col :lg="8">
              <a-form-item label="航班号">
                <a-input v-model:value="orderMainModel.jeecgOrderTicketList.ticketCode" placeholder="请输入航班号" />
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="起飞时间">
                <a-date-picker showTime valueFormat="YYYY-MM-DD HH:mm:ss" v-model:value="orderMainModel.jeecgOrderTicketList.tickectDate" />
              </a-form-item>
            </a-col>
          </a-row>
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
    name: 'OneToOneModal',
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
        jeecgOrderCustomerList: {
          name: '',
          telphone: '',
        },
        jeecgOrderTicketList: {
          ticketCode: '',
          tickectDate: '',
        },
      });
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false });
        reset();
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          rowId.value = data.record.id;
          Object.assign(orderMainModel, data.record);
          let params = { id: orderMainModel.id };
          const customerList = await orderCustomerList(params);
          //update-begin---author:wangshuai ---date:20220629  for：[VUEN-1484]在一对多示例页面，编辑一行（青岛订单A0001），客户信息无法填入------------
          orderMainModel.jeecgOrderCustomerList = customerList[0]?customerList[0]:{};
          //update-end---author:wangshuai ---date:20220629  for：[VUEN-1484]在一对多示例页面，编辑一行（青岛订单A0001），客户信息无法填入--------------
          const ticketList = await orderTicketList(params);
          //update-begin---author:wangshuai ---date:20220629  for：[VUEN-1484]在一对多示例页面，编辑一行（青岛订单A0001），客户信息无法填入------------
          orderMainModel.jeecgOrderTicketList = ticketList[0]?ticketList[0]:{};
          //update-end---author:wangshuai ---date:20220629  for：[VUEN-1484]在一对多示例页面，编辑一行（青岛订单A0001），客户信息无法填入--------------
        }
      });
      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

      function reset() {
        orderMainModel.id = null;
        orderMainModel.orderCode = '';
        orderMainModel.orderMoney = '';
        orderMainModel.orderDate = null;
        orderMainModel.ctype = '';
        orderMainModel.content = '';
        orderMainModel.jeecgOrderCustomerList = {};
        orderMainModel.jeecgOrderTicketList = {};
      }
      async function handleSubmit() {
        formRef.value
          .validate()
          .then(async () => {
            try {
              console.log('formData', JSON.stringify(orderMainModel));
              setModalProps({ confirmLoading: true });
              orderMainModel.jeecgOrderCustomerList =
                Object.keys(orderMainModel.jeecgOrderCustomerList).length > 0 ? [orderMainModel.jeecgOrderCustomerList] : [];
              orderMainModel.jeecgOrderTicketList =
                Object.keys(orderMainModel.jeecgOrderTicketList).length > 0 ? [orderMainModel.jeecgOrderTicketList] : [];
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

      return { formRef, validatorRules, orderMainModel, registerModal, getTitle, labelCol, wrapperCol, handleSubmit };
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
