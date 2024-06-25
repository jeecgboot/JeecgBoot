<template>
  <PageWrapper
    contentFullHeight
    title="基础组件"
    content=" 基础组件依赖于ant-design-vue,组件库已有的基础组件,项目中不会再次进行demo展示（二次封装组件除外）"
  >
    <a-row :gutter="[20, 20]">
      <a-col :xl="10" :lg="24">
        <a-card title="BasicButton Color">
          <div class="my-2">
            <h3>success</h3>
            <div class="py-2">
              <a-button color="success"> 成功 </a-button>
              <a-button color="success" class="ml-2" disabled> 禁用 </a-button>
              <a-button color="success" class="ml-2" loading> loading </a-button>
              <a-button color="success" type="link" class="ml-2"> link </a-button>
              <a-button color="success" type="link" class="ml-2" loading> loading link </a-button>
              <a-button color="success" type="link" class="ml-2" disabled> disabled link </a-button>
            </div>
          </div>

          <div class="my-2">
            <h3>warning</h3>
            <a-button color="warning"> 警告 </a-button>
            <a-button color="warning" class="ml-2" disabled> 禁用 </a-button>
            <a-button color="warning" class="ml-2" loading> loading </a-button>
            <a-button color="warning" type="link" class="ml-2"> link </a-button>
            <a-button color="warning" type="link" class="ml-2" loading> loading link </a-button>
            <a-button color="warning" type="link" class="ml-2" disabled> disabled link </a-button>
          </div>

          <div class="my-2">
            <h3>error</h3>
            <a-button color="error"> 错误 </a-button>
            <a-button color="error" class="ml-2" disabled> 禁用 </a-button>
            <a-button color="error" class="ml-2" loading> loading </a-button>
            <a-button color="error" type="link" class="ml-2"> link </a-button>
            <a-button color="error" type="link" class="ml-2" loading> loading link </a-button>
            <a-button color="error" type="link" class="ml-2" disabled> disabled link </a-button>
          </div>

          <div class="my-2">
            <h3>ghost</h3>
            <a-button ghost color="success" class="ml-2"> 幽灵成功 </a-button>
            <a-button ghost color="warning" class="ml-2"> 幽灵警告 </a-button>
            <a-button ghost color="error" class="ml-2"> 幽灵错误 </a-button>
            <a-button ghost type="dashed" color="warning" class="ml-2"> 幽灵警告dashed </a-button>
            <a-button ghost danger class="ml-2"> 幽灵危险 </a-button>
          </div>
        </a-card>
      </a-col>
      <a-col :xl="14" :lg="24">
        <a-card title="BasicButton Types">
          <div class="my-2">
            <h3>primary</h3>
            <a-button type="primary" preIcon="mdi:page-next-outline"> 主按钮 </a-button>
            <a-button type="primary" class="ml-2" disabled> 禁用 </a-button>
            <a-button type="primary" class="ml-2" danger preIcon="mdi:page-next-outline"> 危险 </a-button>
            <a-button type="primary" class="ml-2" loading> loading </a-button>
            <a-button type="link" class="ml-2"> link </a-button>
            <a-button type="link" class="ml-2" loading> loading link </a-button>
            <a-button type="link" class="ml-2" disabled> disabled link </a-button>
          </div>

          <div class="my-2">
            <h3>default</h3>
            <a-button type="default"> 默认 </a-button>
            <a-button type="default" class="ml-2" disabled> 禁用 </a-button>
            <a-button type="default" class="ml-2" danger> 危险 </a-button>
            <a-button type="default" class="ml-2" loading> loading </a-button>
            <a-button type="link" class="ml-2"> link </a-button>
            <a-button type="link" class="ml-2" loading> loading link </a-button>
            <a-button type="link" class="ml-2" disabled> disabled link </a-button>
          </div>

          <div class="my-2">
            <h3>dashed</h3>
            <a-button type="dashed"> dashed </a-button>
            <a-button type="dashed" class="ml-2" disabled> 禁用 </a-button>
            <a-button type="dashed" class="ml-2" danger> 危险 </a-button>
            <a-button type="dashed" class="ml-2" loading> loading </a-button>
          </div>

          <div class="my-2">
            <h3>ghost 常规幽灵按钮通常用于有色背景下</h3>
            <div class="bg-gray-400 py-2">
              <a-button ghost type="primary" class="ml-2"> 幽灵主要 </a-button>
              <a-button ghost type="default" class="ml-2"> 幽灵默认 </a-button>
              <a-button ghost type="dashed" class="ml-2"> 幽灵dashed </a-button>
              <a-button ghost type="primary" class="ml-2" disabled> 禁用 </a-button>
              <a-button ghost type="primary" class="ml-2" loading> loading </a-button>
            </div>
            <!-- antd 按钮不能同时使用ghost和link -->
            <!--      <a-button ghost type="link" class="ml-2"> link </a-button>-->
            <!--      <a-button ghost type="link" class="ml-2" loading> loading link </a-button>-->
            <!--      <a-button ghost type="link" class="ml-2" disabled> disabled link </a-button>-->
          </div>
        </a-card>
      </a-col>
    </a-row>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { PageWrapper } from '/@/components/Page';
  import { Card, Row, Col } from 'ant-design-vue';
  export default defineComponent({
    components: { PageWrapper, ACard: Card, ARow: Row, ACol: Col },
  });
</script>
