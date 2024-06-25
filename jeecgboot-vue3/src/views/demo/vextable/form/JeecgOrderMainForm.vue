<template>
  <div class="jeecg-flow-demo">
    <BasicForm @register="registerForm"></BasicForm>
    <a-tabs v-model:activeKey="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="客户信息" key="jeecgOrderCustomerForm" :forceRender="true">
        <JeecgOrderCustomerForm ref="jeecgOrderCustomerFormRef" :formData="formData"></JeecgOrderCustomerForm>
      </a-tab-pane>

      <a-tab-pane tab="机票信息" key="jeecgOrderTicket" :forceRender="true">
        <JVxeTable v-if="ok" ref="jeecgOrderTicketRef" stripe rowSelection keepSource :maxHeight="300" :loading="table2.loading" :columns="table2.columns" :dataSource="table2.dataSource"> </JVxeTable>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { computed, defineComponent, ref, reactive } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { getBpmFormSchema, jeecgOrderTicketColumns } from '../data';
  import JeecgOrderCustomerForm from './JeecgOrderCustomerForm.vue';

  export default defineComponent({
    name: 'JeecgOrderMainForm',
    components: {
      BasicForm,
      JeecgOrderCustomerForm,
    },
    props: {
      formData: propTypes.object.def({}),
      formBpm: propTypes.bool.def(true),
    },
    setup(props) {
      const [registerForm, { setFieldsValue, setProps, getFieldsValue, updateSchema }] = useForm({
        labelWidth: 150,
        schemas: getBpmFormSchema(props.formData),
        showActionButtonGroup: false,
        baseColProps: { span: 8 },
      });

      const formDisabled = computed(() => {
        if (props.formData.disabled === false) {
          return false;
        }
        return true;
      });

      const jeecgOrderCustomerFormRef = ref();
      const jeecgOrderTicketRef = ref();
      const ok = ref(false);

      let formData = {};
      const queryByIdUrl = '/test/jeecgOrderMain/queryById';
      async function initFormData() {
        console.log('props.formData', props.formData);
        let params = { id: props.formData.dataId };
        const data = await defHttp.get({ url: queryByIdUrl, params });
        console.log('data', data);
        formData = { ...data };
        //设置表单的值
        await setFieldsValue(formData);
        await setProps({ disabled: formDisabled.value });

        await jeecgOrderCustomerFormRef.value.initFormData(props.formData.dataId);
        await loadOrderTicketData(props.formData.dataId);
        ok.value = true;
      }

      async function submitForm() {
        let data = getFieldsValue();
        let params = Object.assign({}, formData, data);
        console.log('表单数据', params);
        await saveOrUpdate(params, true);
      }

      initFormData();

      const activeKey = ref('jeecgOrderCustomerForm');
      function handleChangeTabs() {}
      // 机票信息
      const table2 = reactive({
        loading: false,
        dataSource: [],
        columns: filterSubTableColnmns(jeecgOrderTicketColumns, 'order:'),
      });

      async function loadOrderTicketData(mainId) {
        const queryByIdUrl = '/test/jeecgOrderMain/queryOrderTicketListByMainId';
        let params = { id: mainId };
        table2.dataSource = [];
        const data = await defHttp.get({ url: queryByIdUrl, params });
        if (data && data.length > 0) {
          table2.dataSource = [...data];
        }
      }

      //新增权限处理方法
      function filterSubTableColnmns(columns, pre) {
        let authList = props.formData.permissionList;
        //注意：如果子表配置显示反向 这里不处理其逻辑  即隐藏无法在流程表单中实现，请使用全局表单权限实现
        let temp = columns.filter((item) => {
          let oneAuth = authList.find((auth) => {
            return auth.action === pre + item.key;
          });
          if (!oneAuth) {
            return true;
          }

          //代码严谨处理，防止一个授权标识，配置多次
          if (oneAuth instanceof Array) {
            oneAuth = oneAuth[0];
          }

          //禁用逻辑
          if (oneAuth.type == '2' && !oneAuth.isAuth) {
            item['disabled'] = true;
            return true;
          }
          //隐藏逻辑逻辑
          if (oneAuth.type == '1' && !oneAuth.isAuth) {
            return false;
          }
          return true;
        });
        return temp;
      }

      return {
        registerForm,
        formDisabled,
        submitForm,
        jeecgOrderCustomerFormRef,
        activeKey,
        handleChangeTabs,
        table2,
        jeecgOrderTicketRef,
        ok,
      };
    },
  });
</script>

<style lang="less">
  /*.jeecg-flow-demo{
    .vxe-header--row{
        .vxe-header--column .vxe-cell{
            width: 180px !important;
        }
        .vxe-header--column:first-child .vxe-cell{
            width: 40px !important;
        }
    }
}*/
</style>
