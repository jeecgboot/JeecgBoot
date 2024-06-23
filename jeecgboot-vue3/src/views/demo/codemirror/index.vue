<template>
  <div>
    <textarea ref="textarea">
白日依山尽，黄河入海流。
欲穷千里目，更上一层楼。
        </textarea
    >
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref, reactive } from 'vue';
  // 引入全局实例
  import _CodeMirror from 'codemirror';

  // 核心样式
  import 'codemirror/lib/codemirror.css';
  // 引入主题后还需要在 options 中指定主题才会生效
  import 'codemirror/theme/cobalt.css';

  // 需要引入具体的语法高亮库才会有对应的语法高亮效果
  // codemirror 官方其实支持通过 /addon/mode/loadmode.js 和 /mode/meta.js 来实现动态加载对应语法高亮库
  // 但 vue 貌似没有无法在实例初始化后再动态加载对应 JS ，所以此处才把对应的 JS 提前引入
  import 'codemirror/mode/javascript/javascript.js';
  import 'codemirror/mode/css/css.js';
  import 'codemirror/mode/xml/xml.js';
  import 'codemirror/mode/clike/clike.js';
  import 'codemirror/mode/markdown/markdown.js';
  import 'codemirror/mode/python/python.js';
  import 'codemirror/mode/r/r.js';
  import 'codemirror/mode/shell/shell.js';
  import 'codemirror/mode/sql/sql.js';
  import 'codemirror/mode/swift/swift.js';
  import 'codemirror/mode/vue/vue.js';
  // 尝试获取全局实例

  export default defineComponent({
    components: {},
    setup() {
      const CodeMirror = window.CodeMirror || _CodeMirror;

      const textarea = ref(null);
      const options = reactive({
        // 缩进格式
        tabSize: 2,
        // 主题，对应主题库 JS 需要提前引入
        theme: 'cobalt',
        // 显示行号
        lineNumbers: true,
        line: true,
      });
      onMounted(() => {
        init();
      });

      function init() {
        CodeMirror.fromTextArea(textarea.value, options);
      }

      return {
        textarea,
      };
    },
  });
</script>

<style lang="css" scoped></style>
