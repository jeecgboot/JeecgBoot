<template>
  <a-spin :spinning="confirmLoading">
    <a-row :span="24" style="margin-bottom: 10px">
      <a-col :span="12" v-for="item in apiList" @click="handleSelect(item)">
        <a-card :style="item.checked ? { border: '1px solid #3370ff' } : {}" hoverable class="checkbox-card" :body-style="{ width: '100%', padding: '10px' }">
          <div class="checkbox-name" style="display: flex; width: 100%; justify-content: space-between">
            <span>接口名称: {{ item.name }}</span>
            <a-checkbox v-model:checked="item.checked" @click.stop class="quantum-checker" @change="(e) => handleChange(e, item)"> </a-checkbox>
          </div>
          <div class="checkbox-name" style="margin-top: 4px">
            请求方式: <span>{{item.requestMethod}}</span>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <Pagination
      v-if="apiList.length > 0"
      :current="pageNo"
      :page-size="pageSize"
      :page-size-options="pageSizeOptions"
      :total="total"
      :showQuickJumper="true"
      :showSizeChanger="true"
      @change="handlePageChange"
      class="list-footer"
      size="small"
    />
  </a-spin>
</template>

<script lang="ts" setup>
import { computed, defineExpose, defineProps, nextTick, reactive, ref } from "vue";
import { useMessage } from '/@/hooks/web/useMessage';
import { getApiList, getPermissionList, permissionAddFunction } from '../OpenApiAuth.api';
import { Form, Pagination } from 'ant-design-vue';

const props = defineProps({
    formDisabled: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    formBpm: { type: Boolean, default: true },
  });
  const useForm = Form.useForm;
  const emit = defineEmits(['register', 'ok']);
  const { createMessage } = useMessage();
  const confirmLoading = ref<boolean>(false);
  //认证ID
  const apiAuthId = ref<string>('');
  //表单验证
  const validatorRules = reactive({});
  //api列表
  const apiList = ref<any>([]);
  //选中的值
  const selectedRowKeys = ref<any>([]);
  //选中的数据
  const selectedRows = ref<any>([]);
  //当前页数
  const pageNo = ref<number>(1);
  //每页条数
  const pageSize = ref<number>(10);
  //总条数
  const total = ref<number>(0);
  //可选择的页数
  const pageSizeOptions = ref<any>(['10', '20', '30']);

  // 表单禁用
  const disabled = computed(() => {
    if (props.formBpm === true) {
      if (props.formData.disabled === false) {
        return false;
      } else {
        return true;
      }
    }
    return props.formDisabled;
  });

  /**
   * 加载数据
   */
  function reload() {
    getApiList({ pageNo: pageNo.value, pageSize: pageSize.value, column: 'createTime', order: 'desc'}).then((res)=>{
      if (res.success) {
        for (const item of res.result.records) {
          item.checked = false;
        }
        apiList.value = res.result.records;
        total.value = res.result.total;
        setChecked();
      } else {
        apiList.value = [];
        total.value = 0;
      }
    });
  }
  
  /**
   * 新增
   */
  function add() {
    edit({});
  }

  /**
   * 编辑
   */
  async function edit(record) {
    selectedRowKeys.value = [];
    selectedRows.value = [];
    pageNo.value = 1;
    pageSize.value = 10;
    apiAuthId.value = record.id;
    await nextTick(() => {
      // 获取当前已授权的项目
      getPermissionList({ apiAuthId: record.id }).then((res) => {
        if (res.length > 0) {
          res.forEach((item) => {
            if(item.ifCheckBox == "1"){
              selectedRowKeys.value.push(item.id);
              selectedRows.value.push(item);
            }
          });
          //设置选中
          setChecked();
        }
      });
      reload();
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    confirmLoading.value = true;
    //时间格式化
    let model = {};
    let apiId = ""
    selectedRowKeys.value.forEach((item) => {
      apiId += item +",";
    })
    model['apiId'] = apiId;
    model['apiAuthId'] = apiAuthId.value;
    await permissionAddFunction(model)
      .then((res) => {
        if (res.success) {
          createMessage.success(res.message);
          emit('ok');
          cleanData()
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }
  const cleanData = () => {
    selectedRows.value = []
    selectedRowKeys.value = []
  };

  /**
   * 复选框选中事件
   * @param item
   */
  function handleSelect(item) {
    let id = item.id;
    const target = apiList.value.find((item) => item.id === id);
    if (target) {
      target.checked = !target.checked;
    }
    //存放选中的知识库的id
    if (!selectedRowKeys.value || selectedRowKeys.value.length == 0) {
      selectedRowKeys.value.push(id);
      selectedRows.value.push(item);
      return;
    }
    let findIndex = selectedRowKeys.value.findIndex((item) => item === id);
    if (findIndex === -1) {
      selectedRowKeys.value.push(id);
      selectedRows.value.push(item);
    } else {
      selectedRowKeys.value.splice(findIndex, 1);
      selectedRows.value.splice(findIndex, 1);
    }
  }

  /**
   * 复选框选中事件
   *
   * @param e
   * @param item
   */
  function handleChange(e, item: any) {
    if (e.target.checked) {
      selectedRowKeys.value.push(item.id);
      selectedRows.value.push(item);
    } else {
      let findIndex = selectedRowKeys.value.findIndex((val) => val === item.id);
      if (findIndex != -1) {
        selectedRowKeys.value.splice(findIndex, 1);
        selectedRows.value.splice(findIndex, 1);
      }
    }
  }

  /**
   * 分页改变事件
   * @param page
   * @param current
   */
  function handlePageChange(page, current) {
    pageNo.value = page;
    pageSize.value = current;
    reload();
  }

  /**
   * 设置选装状态
   */
  function setChecked() {
    if (apiList.value && apiList.value.length > 0){
      let value = selectedRowKeys.value.join(',');
      apiList.value = apiList.value.map((item) => {
        if (value.indexOf(item.id) !== -1) {
          item.checked = true;
        } else {
          item.checked = false;
        }
        return item;
      });
    }
  }

  defineExpose({
    add,
    edit,
    submitForm,
    cleanData
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    padding: 14px;
  }
  .list-footer {
    position: absolute;
    bottom: -22px;
    right: 10px;
    text-align: center;
  }
  .checkbox-card {
    margin-bottom: 10px;
    margin-right: 10px;
  }
  .checkbox-img {
    width: 30px;
    height: 30px;
  }
  .checkbox-name {
    margin-left: 4px;
    font-size: 13px;
  }
  .use-select {
    color: #646a73;
    position: absolute;
    bottom: 0;
    left: 20px;
  }
</style>
