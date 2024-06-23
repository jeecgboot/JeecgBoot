<template>
  <div class="index-container-ty">
    <a-row type="flex" justify="start" :gutter="3">
      <a-col :sm="24" :lg="12">
        <a-card>
          <template #title>
            <div class="index-md-title">
              <img src="../../../../assets/images/daiban.png" />
              我的待办【{{ dataSource1.length }}】
            </div>
          </template>

          <template v-if="dataSource1 && dataSource1.length > 0" #extra>
            <a @click="goPage"
              >更多
              <Icon icon="ant-design:double-right-outlined" />
            </a>
          </template>

          <a-table
            :class="'my-index-table tytable1'"
            ref="table1"
            size="small"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource1"
            :pagination="false"
          >
            <template #ellipsisText="{ text }">
              <JEllipsis :value="text" :length="textMaxLength"></JEllipsis>
            </template>

            <template #dayWarnning="{ text, record }">
              <BellTwoTone style="font-size: 22px" :twoToneColor="getTipColor(record)" />
            </template>

            <template #action="{ text, record }">
              <a @click="handleData">办理</a>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <a-col :sm="24" :lg="12">
        <a-card>
          <template #title>
            <div class="index-md-title">
              <img src="../../../../assets/images/zaiban.png" />
              我的在办【{{ dataSource2.length }}】
            </div>
          </template>

          <template v-if="dataSource2 && dataSource2.length > 0" #extra>
            <a @click="goPage"
              >更多
              <Icon icon="ant-design:double-right-outlined" />
            </a>
          </template>

          <a-table
            :class="'my-index-table tytable2'"
            ref="table1"
            size="small"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource2"
            :pagination="false"
          >
            <template #ellipsisText="{ text }">
              <JEllipsis :value="text" :length="textMaxLength"></JEllipsis>
            </template>

            <template #dayWarnning="{ text, record }">
              <BellTwoTone style="font-size: 22px" :twoToneColor="getTipColor(record)" />
            </template>

            <template #action="{ text, record }">
              <a @click="handleData">办理</a>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <a-col :span="24">
        <div style="height: 5px"></div>
      </a-col>

      <a-col :sm="24" :lg="12">
        <a-card>
          <template #title>
            <div class="index-md-title">
              <img src="../../../../assets/images/guaz.png" />
              我的挂账【{{ dataSource4.length }}】
            </div>
          </template>

          <a-table
            :class="'my-index-table tytable4'"
            ref="table1"
            size="small"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource4"
            :pagination="false"
          >
            <template #ellipsisText="{ text }">
              <JEllipsis :value="text" :length="textMaxLength"></JEllipsis>
            </template>

            <template #dayWarnning="{ text, record }">
              <BellTwoTone style="font-size: 22px" :twoToneColor="getTipColor(record)" />
            </template>

            <template #action="{ text, record }">
              <a @click="handleData">办理</a>
            </template>
          </a-table>
        </a-card>
      </a-col>

      <a-col :sm="24" :lg="12">
        <a-card>
          <template #title>
            <div class="index-md-title">
              <img src="../../../../assets/images/duban.png" />
              我的督办【{{ dataSource3.length }}】
            </div>
          </template>

          <a-table
            :class="'my-index-table tytable3'"
            ref="table1"
            size="small"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource3"
            :pagination="false"
          >
            <template #ellipsisText="{ text }">
              <JEllipsis :value="text" :length="textMaxLength"></JEllipsis>
            </template>

            <template #dayWarnning="{ text, record }">
              <BellTwoTone style="font-size: 22px" :twoToneColor="getTipColor(record)" />
            </template>

            <template #action="{ text, record }">
              <a @click="handleData">办理</a>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import noDataPng from '/@/assets/images/nodata.png';
  import { useMessage } from '/@/hooks/web/useMessage';
  import JEllipsis from '/@/components/Form/src/jeecg/components/JEllipsis.vue';
  import { BulbTwoTone, BellTwoTone } from '@ant-design/icons-vue';

  const tempSs1 = [
    {
      id: '001',
      orderNo: '电[1]1267102',
      orderTitle: '药品出问题了',
      restDay: 1,
    },
    {
      id: '002',
      orderNo: '电[4]5967102',
      orderTitle: '吃了xxx医院的药，病情越来越严重',
      restDay: 0,
    },
    {
      id: '003',
      orderNo: '电[3]5988987',
      orderTitle: '今天去超市买鸡蛋，鸡蛋都是坏的',
      restDay: 7,
    },
    {
      id: '004',
      orderNo: '电[2]5213491',
      orderTitle: 'xx宝实体店高价售卖xx',
      restDay: 5,
    },
    {
      id: '005',
      orderNo: '电[1]1603491',
      orderTitle: '以红利相诱，答应退保后扣一年费用',
      restDay: 0,
    },
  ];

  const tempSs2 = [
    {
      id: '001',
      orderTitle: '我要投诉这个大超市',
      orderNo: '电[1]10299456',
      restDay: 6,
    },
    {
      id: '002',
      orderTitle: 'xxx医院乱开药方,售卖假药',
      orderNo: '电[2]20235691',
      restDay: 0,
    },
    {
      id: '003',
      orderTitle: '我想问问这家店是干啥的',
      orderNo: '电[3]495867322',
      restDay: 7,
    },
    {
      id: '004',
      orderTitle: '我要举报朝阳区奥森公园酒店',
      orderNo: '电[2]1193849',
      restDay: 3,
    },
    {
      id: '005',
      orderTitle: '我今天吃饭吃到一个石头子',
      orderNo: '电[4]56782344',
      restDay: 9,
    },
  ];

  //4-7天
  const tip_green = 'rgba(0, 255, 0, 1)';
  //1-3天
  const tip_yellow = 'rgba(255, 255, 0, 1)';
  //超期
  const tip_red = 'rgba(255, 0, 0, 1)';

  const textMaxLength = 8;
  const $message = useMessage();

  const dataSource1 = ref([]);
  const dataSource2 = ref([]);
  const dataSource3 = ref([]);
  const dataSource4 = ref([]);
  const columns = [
    {
      title: '',
      dataIndex: '',
      key: 'rowIndex',
      width: 50,
      fixed: 'left',
      align: 'center',
      slots: { customRender: 'dayWarnning' },
    },
    {
      title: '剩余天数',
      align: 'center',
      dataIndex: 'restDay',
      width: 80,
    },
    {
      title: '工单标题',
      align: 'center',
      dataIndex: 'orderTitle',
      slots: { customRender: 'ellipsisText' },
    },
    {
      title: '工单编号',
      align: 'center',
      dataIndex: 'orderNo',
    },
    {
      title: '操作',
      dataIndex: 'action',
      align: 'center',
      slots: { customRender: 'action' },
    },
  ];

  function getTipColor(rd) {
    let num = rd.restDay;
    if (num <= 0) {
      return tip_red;
    } else if (num >= 1 && num < 4) {
      return tip_yellow;
    } else if (num >= 4) {
      return tip_green;
    }
  }

  function mock() {
    dataSource1.value = tempSs1;
    dataSource2.value = tempSs2;
    dataSource3.value = tempSs1;
    dataSource4.value = tempSs2;
    ifNullDataSource(dataSource4, '.tytable4');
  }

  function ifNullDataSource(ds, tb) {
    if (!ds || ds.length == 0) {
      var tmp = document.createElement('img');
      tmp.src = noDataPng;
      tmp.width = 300;
      let tbclass = `${tb} .ant-table-placeholder`;
      document.querySelector(tbclass).innerHTML = '';
      document.querySelector(tbclass).appendChild(tmp);
    }
  }

  function handleData() {
    $message.createMessage.success('办理完成');
  }

  function goPage() {
    $message.createMessage.success('请根据具体业务跳转页面');
  }

  mock();
</script>

<style scoped lang="less">
  .my-index-table {
    height: 270px;

    table {
      font-size: 14px !important;
    }
  }

  .index-container-ty {
    margin: 12px 12px 0;

    :deep(.ant-card-body) {
      padding: 10px 12px 0 12px;
    }

    :deep(.ant-card-head) {
      line-height: 24px;
      min-height: 24px;
      background: #7196fb !important;

      .ant-card-head-title {
        padding-top: 6px;
        padding-bottom: 6px;
      }

      .ant-card-extra {
        padding: 0;

        a {
          color: #fff;
        }

        a:hover {
          color: #152ede;
        }
      }
    }

    :deep(.ant-table-footer) {
      text-align: right;
      padding: 6px 12px 6px 6px;
      background: #fff;
      border-top: 2px solid #f7f1f1;
    }

    .index-md-title {
      position: relative;
      width: 100%;
      color: #fff;
      font-size: 21px;
      font-family: cursive;
      padding-left: 25px;

      img {
        position: absolute;
        height: 25px;
        left: 0;
      }
    }

    :deep(.ant-table-thead > tr > th),
    :deep(.ant-table-tbody > tr > td) {
      border-bottom: 1px solid #90aeff;
    }

    :deep(
      .ant-table-small
      > .ant-table-content
      > .ant-table-fixed-left
      > .ant-table-body-outer
      > .ant-table-body-inner
      > table
      > .ant-table-thead
      > tr
      > th),
    :deep(
      .ant-table-small
      > .ant-table-content
      > .ant-table-fixed-right
      > .ant-table-body-outer
      > .ant-table-body-inner
      > table
      > .ant-table-thead
      > tr
      > th) {
      border-bottom: 1px solid #90aeff;
    }

    :deep(.ant-table-small > .ant-table-content > .ant-table-scroll > .ant-table-body > table > .ant-table-thead > tr > th) {
      border-bottom: 1px solid #90aeff;
    }

    :deep(.ant-table-small) {
      border: 1px solid #90aeff;
    }

    :deep(.ant-table-placeholder) {
      padding: 0;
      height: 215px;
    }
  }
</style>
