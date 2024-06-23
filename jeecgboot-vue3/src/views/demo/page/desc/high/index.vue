<template>
  <PageWrapper title="单号：234231029431" contentBackground>
    <template #extra>
      <a-button> 操作一 </a-button>
      <a-button> 操作二 </a-button>
      <a-button type="primary"> 主操作 </a-button>
    </template>

    <template #footer>
      <a-tabs default-active-key="1">
        <a-tab-pane key="1" tab="详情" />
        <a-tab-pane key="2" tab="规则" />
      </a-tabs>
    </template>

    <div class="pt-4 m-4 desc-wrap">
      <a-descriptions size="small" :column="2">
        <a-descriptions-item label="创建人"> 曲丽丽 </a-descriptions-item>
        <a-descriptions-item label="订购产品"> XX 服务 </a-descriptions-item>
        <a-descriptions-item label="创建时间"> 2017-01-10 </a-descriptions-item>
        <a-descriptions-item label="关联单据">
          <a>12421</a>
        </a-descriptions-item>
        <a-descriptions-item label="生效日期"> 2017-07-07 ~ 2017-08-08 </a-descriptions-item>
        <a-descriptions-item label="备注"> 请于两个工作日内确认 </a-descriptions-item>
      </a-descriptions>
      <a-card title="流程进度" :bordered="false">
        <a-steps :current="1" progress-dot size="small">
          <a-step title="创建项目">
            <template #description> <div>Jeecg</div> <p>2016-12-12 12:32</p> </template>
          </a-step>
          <a-step title="部门初审">
            <template #description>
              <p>Chad</p>
            </template>
          </a-step>
          <a-step title="财务复核" />
          <a-step title="完成" />
        </a-steps>
      </a-card>

      <a-card title="用户信息" :bordered="false" class="mt-5">
        <a-descriptions :column="3">
          <a-descriptions-item label="用户姓名"> 付小小 </a-descriptions-item>
          <a-descriptions-item label="会员卡号"> XX 32943898021309809423 </a-descriptions-item>
          <a-descriptions-item label="身份证"> 3321944288191034921 </a-descriptions-item>
          <a-descriptions-item label="联系方式"> 18100000000 </a-descriptions-item>
          <a-descriptions-item label="联系地址" :span="2"> 曲丽丽 18100000000 浙江省杭州市西湖区黄姑山路工专路交叉路口 </a-descriptions-item>
        </a-descriptions>

        <a-descriptions title="信息组" :column="3">
          <a-descriptions-item label="某某数据"> 111 </a-descriptions-item>
          <a-descriptions-item label="该数据更新时间"> 2017-08-08 </a-descriptions-item>
          <a-descriptions-item label="某某数据"> 725 </a-descriptions-item>
          <a-descriptions-item label="该数据更新时间"> 2017-08-08 </a-descriptions-item>
        </a-descriptions>

        <h4>信息组</h4>
        <a-card title="多层级信息组">
          <a-descriptions title="组名称" :column="3">
            <a-descriptions-item label="负责人"> 林东东 </a-descriptions-item>
            <a-descriptions-item label="角色码"> 1234567 </a-descriptions-item>
            <a-descriptions-item label="所属部门"> XX公司 - YY部 </a-descriptions-item>
            <a-descriptions-item label="过期时间"> 2017-08-08 </a-descriptions-item>
            <a-descriptions-item label="描述" :span="2"> 这段描述很长很长很长很长很长很长很长很长很长很长很长很长很长很长... </a-descriptions-item>
          </a-descriptions>
          <a-divider />
          <a-descriptions title="组名称" :column="1">
            <a-descriptions-item label="学名">
              Citrullus lanatus (Thunb.) Matsum. et Nakai一年生蔓生藤本；茎、枝粗壮，具明显的棱。卷须较粗..
            </a-descriptions-item>
          </a-descriptions>
          <a-divider />
          <a-descriptions title="组名称" :column="1">
            <a-descriptions-item label="负责人"> 付小小 </a-descriptions-item>
            <a-descriptions-item label="角色码"> 1234568 </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-card>
      <a-card title="用户近半年来电记录" class="my-5">
        <Empty />
      </a-card>
      <BasicTable @register="registerTimeTable" />
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { PageWrapper } from '/@/components/Page';
  import { Divider, Card, Empty, Descriptions, Steps, Tabs } from 'ant-design-vue';

  import { refundTimeTableSchema, refundTimeTableData } from './data';
  export default defineComponent({
    components: {
      BasicTable,
      PageWrapper,
      [Divider.name]: Divider,
      [Card.name]: Card,
      Empty,
      [Descriptions.name]: Descriptions,
      [Descriptions.Item.name]: Descriptions.Item,
      [Steps.name]: Steps,
      [Steps.Step.name]: Steps.Step,
      [Tabs.name]: Tabs,
      [Tabs.TabPane.name]: Tabs.TabPane,
    },
    setup() {
      const [registerTimeTable] = useTable({
        title: '退货进度',
        columns: refundTimeTableSchema,
        pagination: false,
        dataSource: refundTimeTableData,
        showIndexColumn: false,
        scroll: { y: 300 },
      });

      return {
        registerTimeTable,
      };
    },
  });
</script>
