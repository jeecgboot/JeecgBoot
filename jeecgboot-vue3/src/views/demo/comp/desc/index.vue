<template>
  <PageWrapper title="详情组件示例">
    <Description title="基础示例" :collapseOptions="{ canExpand: true, helpMessage: 'help me' }" :column="3" :data="mockData" :schema="schema" />

    <Description
      class="mt-4"
      title="垂直示例"
      layout="vertical"
      :collapseOptions="{ canExpand: true, helpMessage: 'help me' }"
      :column="2"
      :data="mockData"
      :schema="schema"
    />

    <Description @register="register" class="mt-4" />
    <Description @register="register1" class="mt-4" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Description, DescItem, useDescription } from '/@/components/Description/index';
  import { PageWrapper } from '/@/components/Page';

  const mockData: Recordable = {
    username: 'test',
    nickName: 'VB',
    age: '123',
    phone: '15695909xxx',
    email: '190848757@qq.com',
    addr: '厦门市思明区',
    sex: '男',
    certy: '3504256199xxxxxxxxx',
    tag: 'orange',
  };
  const schema: DescItem[] = [
    {
      field: 'username',
      label: '用户名',
    },
    {
      field: 'nickName',
      label: '昵称',
      render: (curVal, data) => {
        return `${data.username}-${curVal}`;
      },
    },
    {
      field: 'phone',
      label: '联系电话',
    },
    {
      field: 'email',
      label: '邮箱',
    },
    {
      field: 'addr',
      label: '地址',
    },
  ];
  export default defineComponent({
    components: { Description, PageWrapper },
    setup() {
      const [register] = useDescription({
        title: 'useDescription',
        data: mockData,
        schema: schema,
      });

      const [register1] = useDescription({
        title: '无边框',
        bordered: false,
        data: mockData,
        schema: schema,
      });

      return { mockData, schema, register, register1 };
    },
  });
</script>
