<!--用户选择框-->
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="register" title="数据对比" width="50%" destroyOnClose :showOkBtn="false">
      <a-row :gutter="6" v-if="dataVersionList" style="margin-left: 2px">
        <span style="margin-top: 5px; margin-right: 3px; margin-left: 4px">版本对比:</span>
        <a-select placeholder="版本号" @change="handleChange1" v-model:value="params.dataId1">
          <a-select-option v-for="(log, logindex) in dataVersionList" :key="log.value" :value="log.value">
            {{ log.text }}
          </a-select-option>
        </a-select>

        <a-select placeholder="版本号" @change="handleChange2" style="padding-left: 10px" v-model:value="params.dataId2">
          <a-select-option v-for="(log, logindex) in dataVersionList" :key="log.value" :value="log.value">
            {{ log.text }}
          </a-select-option>
        </a-select>
      </a-row>
      <BasicTable
        :columns="columns"
        v-bind="getBindValue"
        :rowClassName="setDataCss"
        :striped="false"
        :showIndexColumn="false"
        :pagination="false"
        :canResize="false"
        :bordered="true"
        :dataSource="dataSource"
        :searchInfo="searchInfo"
        v-if="isUpdate"
      >
        <template #dataVersionTitle1="{ record }"> <Icon icon="icon-park-outline:grinning-face" /> 版本:{{ dataVersion1Num }} </template>
        <template #dataVersionTitle2="{ record }"> <Icon icon="icon-park-outline:grinning-face" /> 版本:{{ dataVersion2Num }} </template>
        <template #avatarslot="{ record }">
          <div class="anty-img-wrap" v-if="record.dataVersion1 != record.dataVersion2">
            <Icon icon="mdi:arrow-right-bold" style="color: red"></Icon>
          </div>
        </template>
      </BasicTable>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, unref, ref, reactive, watch } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { queryCompareList, queryDataVerList } from './datalog.api';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { selectProps } from '/@/components/Form/src/jeecg/props/props';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'DataLogCompareModal',
    components: {
      //此处需要异步加载BasicTable
      BasicModal,
      BasicTable: createAsyncComponent(() => import('/@/components/Table/src/BasicTable.vue'), { loading: true }),
    },
    props: {
      ...selectProps,
    },
    emits: ['register', 'btnOk'],
    setup(props, { emit, refs }) {
      const { createMessage } = useMessage();
      const attrs = useAttrs();
      const getBindValue = Object.assign({}, unref(props), unref(attrs));
      const dataSource = ref([]);
      const dataVersion1Num = ref('');
      const dataVersion2Num = ref('');
      const isUpdate = ref(true);
      const searchInfo = {};
      const dataId1 = ref('');
      const dataId2 = ref('');
      const dataId = ref('');
      const dataTable1 = ref('');
      const dataID3 = ref('');
      const dataTable = ref('');
      const confirmLoading = ref(false);
      const dataVersionList = ref([]);
      let params = reactive({ dataId1: '', dataId2: '' });
      let dataLog = reactive({});
      const [register, { setModalProps, closeModal }] = useModalInner(async (data) => {
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          let checkedRows = data.selectedRows;
          dataTable.value = checkedRows[0].dataTable;
          dataId.value = checkedRows[0].dataId;
          dataId1.value = checkedRows[0].id;
          dataId2.value = checkedRows[1].id;
          params.dataId1 = dataId1.value;
          params.dataId2 = dataId2.value;
          await initDataVersionList();
          await initTableData();
        }
      });

      //定义表格列
      const columns = [
        {
          title: '字段名',
          dataIndex: 'code',
          width: 20,
          align: 'left',
        },
        {
          dataIndex: 'dataVersion1',
          align: 'left',
          width: 60,
          slots: { title: 'dataVersionTitle1' },
        },
        {
          title: '',
          dataIndex: 'imgshow',
          align: 'center',
          slots: { customRender: 'avatarslot' },
          width: 5,
        },
        {
          align: 'left',
          dataIndex: 'dataVersion2',
          width: 60,
          filters: [],
          filterMultiple: false,
          slots: { title: 'dataVersionTitle2' },
        },
      ];
      async function initTableData() {
        console.info('params', params);
        queryCompareList(unref(params)).then((res) => {
          console.info('test', res);
          dataVersion1Num.value = res[0].dataVersion;
          dataVersion2Num.value = res[1].dataVersion;
          let json1 = JSON.parse(res[0].dataContent);
          let json2 = JSON.parse(res[1].dataContent);
          let data = [];
          for (var item1 in json1) {
            for (var item2 in json2) {
              if (item1 == item2) {
                data.push({
                  code: item1,
                  imgshow: '',
                  dataVersion1: json1[item1],
                  dataVersion2: json2[item2],
                });
              }
            }
          }
          dataSource.value = data;
        });
      }
      function handleChange1(value) {
        if (params.dataId2 == value) {
          createMessage.warning('相同版本号不能比较');
          return;
        }
        params.dataId1 = value;
        initTableData();
      }
      function handleChange2(value) {
        if (params.dataId1 == value) {
          createMessage.warning('相同版本号不能比较');
          return;
        }
        params.dataId2 = value;
        initTableData();
      }
      function setDataCss(record) {
        let className = 'trcolor';
        const dataVersion1 = record.dataVersion1;
        const dataVersion2 = record.dataVersion2;
        if (dataVersion1 != dataVersion2) {
          return className;
        }
      }
      async function initDataVersionList() {
        queryDataVerList({ dataTable: dataTable.value, dataId: dataId.value }).then((res) => {
          dataVersionList.value = res.map((value, key, arr) => {
            let item = {};
            item['text'] = value['dataVersion'];
            item['value'] = value['id'];
            return item;
          });
        });
      }

      return {
        //config,
        searchInfo,
        dataSource,
        setDataCss,
        isUpdate,
        dataVersionList,
        dataVersion1Num,
        dataVersion2Num,
        queryCompareList,
        initDataVersionList,
        register,
        handleChange1,
        handleChange2,
        params,
        getBindValue,
        columns,
      };
    },
  });
</script>
<style scoped>
  .anty-img-wrap {
    height: 25px;
    position: relative;
  }

  .anty-img-wrap > img {
    max-height: 100%;
  }

  .marginCss {
    margin-top: 20px;
  }
</style>
