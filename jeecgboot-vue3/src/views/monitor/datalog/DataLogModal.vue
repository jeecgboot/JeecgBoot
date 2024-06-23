<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="数据对比窗口" :minHeight="300" width="800px" @ok="handleSubmit">
      <a-spin :spinning="confirmLoading">
        <a-form @submit="handleSubmit" :form="form" class="form">
          <a-row class="form-row" :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="数据库表名" :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }" name="dataTable">
                <a-input placeholder="请输入数据库表名" v-model:value="dataTable" disabled />
              </a-form-item>
            </a-col>

            <a-col :md="12" :sm="8">
              <a-form-item label="数据ID" :label-col="{ span: 5 }" :wrapper-col="{ span: 15 }">
                <a-input placeholder="请输入数据ID" v-model:value="dataId" disabled />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row class="form-row" :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="版本号1" :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
                <a-select placeholder="请选择版本号" @change="handleChange1" v-model:value="dataVersion1">
                  <a-select-option v-for="(log, logindex) in dataVersionList" :key="logindex.toString()" :value="log.id">
                    {{ log.dataVersion }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
              <a-form-item label="版本号2" :label-col="{ span: 5 }" :wrapper-col="{ span: 15 }">
                <a-select placeholder="请选择版本号" @change="handleChange2" v-model:value="dataVersion2">
                  <a-select-option v-for="(log, logindex) in dataVersionList" :key="logindex.toString()" :value="log.id">
                    {{ log.dataVersion }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </BasicModal>
    <DataLogCompareModal @register="registerDataLogCompareModal"></DataLogCompareModal>
  </div>
</template>
<script lang="ts" setup>
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { queryDataVerList } from './datalog.api';
  import { reactive, ref, unref } from 'vue';
  import DataLogCompareModal from './DataLogCompareModal.vue';
  const dataId1 = ref('');
  const dataId2 = ref('');
  const dataId = ref('');
  const dataTable1 = ref('');
  const dataID3 = ref('');
  const dataTable = ref('');
  const confirmLoading = ref(false);
  const isUpdate = ref(true);
  const dataVersionList = ref([]);
  let dataLog = reactive({});
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      let checkedRows = data.selectedRows;
      dataTable.value = checkedRows[0].dataTable;
      dataId.value = checkedRows[0].dataId;
      dataId1.value = checkedRows[0].id;
      dataId2.value = checkedRows[1].id;
      initDataVersionList();
    }
  });

  const [registerDataLogCompareModal, { openModal }] = useModal();

  function handleChange1(value) {
    dataId1.value = value;
  }

  function handleChange2(value) {
    dataId2.value = value;
  }

  function handleSubmit() {
    let result = { dataId1: dataId1.value, dataId2: dataId2.value };
    openModal(true, {
      result,
      isUpdate: true,
    });
    closeModal();
  }

  function initDataVersionList() {
    queryDataVerList({ dataTable: dataTable.value, dataId: dataId.value }).then((res) => {
      dataVersionList.value = res.map((value, key, arr) => {
        arr['label'] = value;
        return arr;
      });
      console.info(dataVersionList.value);
    });
  }
</script>

<style scoped lang="less">
  .detail-iframe {
    border: 0;
    width: 100%;
    height: 100%;
    min-height: 600px;
  }
</style>
