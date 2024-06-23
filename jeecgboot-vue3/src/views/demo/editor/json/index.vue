<template>
  <PageWrapper title="代码编辑器组件示例" contentFullHeight fixedHeight contentBackground>
    <template #extra>
      <a-space size="middle">
        <a-button @click="showData" type="primary">获取数据</a-button>
        <RadioGroup button-style="solid" v-model:value="modeValue" @change="handleModeChange">
          <RadioButton value="application/json"> json数据 </RadioButton>
          <RadioButton value="htmlmixed"> html代码 </RadioButton>
          <RadioButton value="javascript"> javascript代码 </RadioButton>
        </RadioGroup>
      </a-space>
    </template>
    <CodeEditor v-model:value="value" :mode="modeValue" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { CodeEditor } from '/@/components/CodeEditor';
  import { PageWrapper } from '/@/components/Page';
  import { Radio, Space, Modal } from 'ant-design-vue';

  const jsonData =
    '{"name":"BeJson","url":"http://www.xxx.com","page":88,"isNonProfit":true,"address":{"street":"科技园路.","city":"江苏苏州","country":"中国"},"links":[{"name":"Google","url":"http://www.xxx.com"},{"name":"Baidu","url":"http://www.xxx.com"},{"name":"SoSo","url":"http://www.xxx.com"}]}';

  const jsData = `
      (() => {
        var htmlRoot = document.getElementById('htmlRoot');
        var theme = window.localStorage.getItem('__APP__DARK__MODE__');
        if (htmlRoot && theme) {
          htmlRoot.setAttribute('data-theme', theme);
          theme = htmlRoot = null;
        }
      })();
  `;

  const htmlData = `
     <!DOCTYPE html>
<html lang="en" id="htmlRoot">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="renderer" content="webkit" />
    <meta
      name="viewport"
      content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0"
    />
    <title><%= title %></title>
    <link rel="icon" href="/favicon.ico" />
  </head>
  <body>
    <div id="app">
    </div>
  </body>
</html>
  `;
  export default defineComponent({
    components: {
      CodeEditor,
      PageWrapper,
      RadioButton: Radio.Button,
      RadioGroup: Radio.Group,
      ASpace: Space,
    },
    setup() {
      const modeValue = ref('application/json');
      const value = ref(jsonData);

      function handleModeChange(e: ChangeEvent) {
        const mode = e.target.value;
        if (mode === 'application/json') {
          value.value = jsonData;
          return;
        }
        if (mode === 'htmlmixed') {
          value.value = htmlData;
          return;
        }
        if (mode === 'javascript') {
          value.value = jsData;
          return;
        }
      }

      function showData() {
        Modal.info({ title: '编辑器当前值', content: value.value });
      }

      return { value, modeValue, handleModeChange, showData };
    },
  });
</script>
