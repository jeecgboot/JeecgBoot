<template>
  <PageWrapper title="卡片列表示例" content="基础封装">
    <CardList :params="params" :api="demoListApi" @getMethod="getMethod" @delete="handleDel">
      <template #header>
        <Button type="primary" color="error"> 按钮1 </Button>
        <Button type="primary" color="success"> 按钮2 </Button>
      </template>
    </CardList>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { CardList } from '/@/components/CardList';
  import { Button } from '/@/components/Button';
  import { PageWrapper } from '/@/components/Page';
  import { demoListApi } from '/@/api/demo/table';
  import { useMessage } from '/@/hooks/web/useMessage';
  const { notification } = useMessage();
  // 请求api时附带参数
  const params = {};

  let reload = () => {};
  // 获取内部fetch方法;
  function getMethod(m: any) {
    reload = m;
  }
  //删除按钮事件
  function handleDel(id) {
    console.log(id);
    notification.success({ message: `成功删除${id}` });
    reload();
  }
</script>
