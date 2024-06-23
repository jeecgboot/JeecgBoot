<template>
  <div class="p-4">
    <ChartGroupCard class="enter-y" :loading="loading" type="bdc" />
    <BdcTabCard class="!my-4 enter-y" :loading="loading" />
    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :class="{ 'anty-list-cust': true }" :bordered="false">
          <a-tabs v-model:activeKey="indexBottomTab" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
            <template #rightExtra>
              <div class="extra-wrapper">
                <a-radio-group v-model:value="indexRegisterType" @change="changeRegisterType">
                  <a-radio-button value="转移登记">转移登记</a-radio-button>
                  <a-radio-button value="抵押登记">抵押登记</a-radio-button>
                  <a-radio-button value="">所有</a-radio-button>
                </a-radio-group>
              </div>
            </template>

            <a-tab-pane tab="业务流程限时监管" key="1">
              <a-table
                :dataSource="dataSource"
                size="default"
                rowKey="reBizCode"
                :columns="table.columns"
                :pagination="ipagination"
                @change="tableChange"
              >
                <template #bodyCell="{ column, record }">
                  <template v-if="column.dataIndex === 'flowRate'">
                    <Progress
                      :strokeColor="getPercentColor(record.flowRate)"
                      :format="getPercentFormat"
                      :percent="getFlowRateNumber(record.flowRate)"
                      style="width: 80px"
                    />
                  </template>
                </template>
              </a-table>
            </a-tab-pane>

            <a-tab-pane loading="true" tab="业务节点限时监管" key="2">
              <a-table
                :dataSource="dataSource1"
                size="default"
                rowKey="reBizCode"
                :columns="table1.columns"
                :pagination="ipagination1"
                @change="tableChange1"
              >
                <template #bodyCell="{ column, record }">
                  <template v-if="column.dataIndex === 'flowRate'">
                    <span style="color: red">{{ record.flowRate }}小时</span>
                  </template>
                </template>
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { Progress } from 'ant-design-vue';
  import ChartGroupCard from '../components/ChartGroupCard.vue';
  import BdcTabCard from '../components/BdcTabCard.vue';
  import LineMulti from '/@/components/chart/LineMulti.vue';
  import HeadInfo from '/@/components/chart/HeadInfo.vue';
  import { table, table1 } from '../data';

  const loading = ref(true);

  setTimeout(() => {
    loading.value = false;
  }, 500);

  const indexBottomTab = ref('1');
  const indexRegisterType = ref('转移登记');
  const dataSource = ref([]);
  const dataSource1 = ref([]);
  const ipagination = ref(table.ipagination);
  const ipagination1 = ref(table1.ipagination);

  function changeRegisterType(e) {
    indexRegisterType.value = e.target.value;
    if (unref(indexBottomTab) == '1') {
      loadDataSource();
    } else {
      loadDataSource1();
    }
  }

  function tableChange(pagination) {
    ipagination.value.current = pagination.current;
    ipagination.value.pageSize = pagination.pageSize;
    loadDataSource();
  }

  function tableChange1(pagination) {
    ipagination1.value.current = pagination.current;
    ipagination1.value.pageSize = pagination.pageSize;
    loadDataSource1();
  }

  function getFlowRateNumber(value) {
    return +value;
  }

  function getPercentFormat(value) {
    if (value == 100) {
      return '超时';
    } else {
      return value + '%';
    }
  }

  function getPercentColor(value) {
    let p = +value;
    if (p >= 90 && p < 100) {
      return 'rgb(244, 240, 89)';
    } else if (p >= 100) {
      return 'red';
    } else {
      return 'rgb(16, 142, 233)';
    }
  }

  function loadDataSource() {
    dataSource.value = table.dataSource.filter((item) => {
      if (!unref(indexRegisterType)) {
        return true;
      }
      return item.type == unref(indexRegisterType);
    });
  }

  function loadDataSource1() {
    dataSource1.value = table1.dataSource.filter((item) => {
      if (!unref(indexRegisterType)) {
        return true;
      }
      return item.type == unref(indexRegisterType);
    });
  }

  loadDataSource();
  loadDataSource1();
</script>

<style lang="less" scoped>
  .ant-table-wrapper {
    :deep(.ant-table){
      td,th {
        padding: 10px;
      }
    }
  }
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  .item-group {
    padding: 20px 0 8px 24px;
    font-size: 0;

    a {
      color: rgba(0, 0, 0, 0.65);
      display: inline-block;
      font-size: 14px;
      margin-bottom: 13px;
      width: 25%;
    }
  }

  .item-group {
    .more-btn {
      margin-bottom: 13px;
      text-align: center;
    }
  }

  .list-content-item {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    vertical-align: middle;
    font-size: 14px;
    margin-left: 40px;
  }

  @media only screen and (min-width: 1600px) {
    .list-content-item {
      margin-left: 60px;
    }
  }

  @media only screen and (max-width: 1300px) {
    .list-content-item {
      margin-left: 20px;
    }

    .width-hidden4 {
      display: none;
    }
  }

  .list-content-item {
    span {
      line-height: 20px;
    }
  }

  .list-content-item {
    p {
      margin-top: 4px;
      margin-bottom: 0;
      line-height: 22px;
    }
  }

  .anty-list-cust {
    .ant-list-item-meta {
      flex: 0.3 !important;
    }
  }

  .anty-list-cust {
    .ant-list-item-content {
      flex: 1 !important;
      justify-content: flex-start !important;
      margin-left: 20px;
    }
  }
</style>
