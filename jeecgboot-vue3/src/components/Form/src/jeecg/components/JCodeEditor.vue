<template>
  <div ref="containerRef" v-bind="boxBindProps">
    <!-- 全屏按钮 -->
    <a-icon v-if="fullScreen" class="full-screen-icon" :type="fullScreenIcon" @click="onToggleFullScreen" />
    <textarea ref="textarea" v-bind="getBindValue"></textarea>
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, reactive, ref, watch, unref, computed } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  // 引入全局实例
  import _CodeMirror, { EditorFromTextArea } from 'codemirror';
  // 核心样式
  import 'codemirror/lib/codemirror.css';
  // 引入主题后还需要在 options 中指定主题才会生效
  import 'codemirror/theme/idea.css';
  // 需要引入具体的语法高亮库才会有对应的语法高亮效果
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
  // 折叠资源引入:开始
  import 'codemirror/addon/fold/foldgutter.css';
  import 'codemirror/addon/fold/foldcode.js';
  import 'codemirror/addon/fold/brace-fold.js';
  import 'codemirror/addon/fold/comment-fold.js';
  import 'codemirror/addon/fold/indent-fold.js';
  import 'codemirror/addon/fold/foldgutter.js';
  // 折叠资源引入:结束
  //光标行背景高亮，配置里面也需要styleActiveLine设置为true
  import 'codemirror/addon/selection/active-line.js';
  // 支持代码自动补全
  import 'codemirror/addon/hint/show-hint.css';
  import 'codemirror/addon/hint/show-hint.js';
  import 'codemirror/addon/hint/anyword-hint.js';
  // 匹配括号
  import 'codemirror/addon/edit/matchbrackets';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { isJsonObjectString } from '/@/utils/is.ts';
  // 代码提示
  import { useCodeHinting } from '../hooks/useCodeHinting';

  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { ThemeEnum } from '/@/enums/appEnum';
  export default defineComponent({
    name: 'JCodeEditor',
    // 不将 attrs 的属性绑定到 html 标签上
    inheritAttrs: false,
    components: {},
    props: {
      value: propTypes.string.def(''),
      height: propTypes.string.def('auto'),
      disabled: propTypes.bool.def(false),
      // 是否显示全屏按钮
      fullScreen: propTypes.bool.def(false),
      // 全屏以后的z-index
      zIndex: propTypes.any.def(1500),
      theme: propTypes.string.def('idea'),
      language: propTypes.string.def(''),
      // 代码提示
      keywords: propTypes.array.def([]),
    },
    emits: ['change', 'update:value'],
    setup(props, { emit }) {
      const { getDarkMode } = useRootSetting();
      const containerRef = ref(null);
      const { prefixCls } = useDesign('code-editer');
      const CodeMirror = window.CodeMirror || _CodeMirror;
      const emitData = ref<object>();
      //表单值
      const [state] = useRuleFormItem(props, 'value', 'change', emitData);
      const textarea = ref<HTMLTextAreaElement>();
      let coder: Nullable<EditorFromTextArea> = null;
      const attrs = useAttrs();
      const height = ref(props.height);
      const options = reactive({
        // 缩进格式
        tabSize: 2,
        // 主题，对应主题库 JS 需要提前引入
        // update-begin--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
        theme: getDarkMode.value == ThemeEnum.DARK ? 'monokai' : props.theme,
        // update-end--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
        smartIndent: true, // 是否智能缩进
        // 显示行号
        lineNumbers: true,
        line: true,
        // 启用代码折叠相关功能:开始
        foldGutter: true,
        lineWrapping: true,
        gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter', 'CodeMirror-lint-markers'],
        // 启用代码折叠相关功能:结束
        // 光标行高亮
        styleActiveLine: true,
        // update-begin--author:liaozhiyang---date:20231201---for：【issues/869】JCodeEditor组件初始化时没有设置mode
        mode: props.language,
        // update-begin--author:liaozhiyang---date:20231201---for：【issues/869】JCodeEditor组件初始化时没有设置mode
        // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-898】代码生成之后的预览改成只读
        readOnly: props.disabled,
        // update-end--author:liaozhiyang---date:20240603---for：【TV360X-898】代码生成之后的预览改成只读
        // 匹配括号
        matchBrackets: true,
        extraKeys: {
          // Tab: function autoFormat(editor) {
          //   //var totalLines = editor.lineCount();
          //   //editor.autoFormatRange({line:0, ch:0}, {line:totalLines});
          //   setValue(innerValue, false);
          // },
          'Cmd-/': (cm) => comment(cm),
          'Ctrl-/': (cm) => comment(cm),
        },
      });
      // 内部存储值，初始为 props.value
      let innerValue = props.value ?? '';
      // 全屏状态
      const isFullScreen = ref(false);
      const fullScreenIcon = computed(() => (isFullScreen.value ? 'fullscreen-exit' : 'fullscreen'));
      // 外部盒子参数
      const boxBindProps = computed(() => {
        let _props = {
          class: [
            prefixCls,
            'full-screen-parent',
            'auto-height',
            {
              'full-screen': isFullScreen.value,
            },
          ],
          style: {},
        };
        if (isFullScreen.value) {
          _props.style['z-index'] = props.zIndex;
        }
        return _props;
      });
      // update-begin--author:liaozhiyang---date:20230904---for：【QQYUN-5955】online js增强，加入代码提示
      const { codeHintingMount, codeHintingRegistry } = useCodeHinting(CodeMirror, props.keywords, props.language);
      codeHintingRegistry();
      // update-end--author:liaozhiyang---date:20230904---for：【QQYUN-5955】online js增强，加入代码提示
      /**
       * 监听组件值
       */
      watch(
        () => props.value,
        () => {
          if (innerValue != props.value) {
            setValue(props.value, false);
          }
        }
      );
      onMounted(() => {
        initialize();
        // update-begin--author:liaozhiyang---date:20240318---for：【QQYUN-8473】代码编辑器首次加载会有遮挡
        setTimeout(() => {
          refresh();
        }, 150);
        // update-end--author:liaozhiyang---date:20240318---for：【QQYUN-8473】代码编辑器首次加载会有遮挡
      });

      /**
       * 组件赋值
       * @param value
       * @param trigger 是否触发 change 事件
       */
      function setValue(value: string, trigger = true) {
        if (value && isJsonObjectString(value)) {
          value = JSON.stringify(JSON.parse(value), null, 2);
        }
        coder?.setValue(value ?? '');
        innerValue = value;
        trigger && emitChange(innerValue);
        // update-begin--author:liaozhiyang---date:20240510---for：【QQYUN-9231】代码编辑器有遮挡
        setTimeout(() => {
          refresh();
          // 再次刷下防止小概率下遮挡问题
          setTimeout(() => {
            refresh();
          }, 600);
        }, 400);
        // update-end--author:liaozhiyang---date:20240510---for：【QQYUN-9231】代码编辑器有遮挡
      }

      //编辑器值修改事件
      function onChange(obj) {
        let value = obj.getValue();
        innerValue = value || '';
        if (props.value != innerValue) {
          emitChange(innerValue);
        }
      }

      function emitChange(value) {
        emit('change', value);
        emit('update:value', value);
      }

      //组件初始化
      function initialize() {
        coder = CodeMirror.fromTextArea(textarea.value!, options);
        //绑定值修改事件
        coder.on('change', onChange);
        // 初始化成功时赋值一次
        setValue(innerValue, false);
        // update-begin--author:liaozhiyang---date:20230904---for：【QQYUN-5955】online js增强，加入代码提示
        codeHintingMount(coder);
        // update-end--author:liaozhiyang---date:20230904---for：【QQYUN-5955】online js增强，加入代码提示
      }

      // 切换全屏状态
      function onToggleFullScreen() {
        isFullScreen.value = !isFullScreen.value;
      }

      //update-begin-author:taoyan date:2022-5-9 for: codeEditor禁用功能
      watch(
        () => props.disabled,
        (val) => {
          if (coder) {
            coder.setOption('readOnly', val);
          }
        }
      );
      //update-end-author:taoyan date:2022-5-9 for: codeEditor禁用功能
      
      // 支持动态设置语言
      watch(()=>props.language, (val)=>{
        if(val && coder){
          coder.setOption('mode', val);
        }
      });

      const getBindValue = Object.assign({}, unref(props), unref(attrs));

      //update-begin-author:taoyan date:2022-10-18 for: VUEN-2480【严重bug】online vue3测试的问题 8、online js增强样式问题
      function refresh(){
        if(coder){
          coder.refresh();
        }
      }
      //update-end-author:taoyan date:2022-10-18 for: VUEN-2480【严重bug】online vue3测试的问题 8、online js增强样式问题

      /**
       * 2024-04-01
       * liaozhiyang
       * 代码批量注释
       */
      function comment(cm) {
        var selection = cm.getSelection();
        var start = cm.getCursor('start');
        var end = cm.getCursor('end');
        var isCommented = selection.startsWith('//');
        if (isCommented) {
          // 如果已经被注释，取消注释
          cm.replaceRange(selection.replace(/\n\/\/\s/g, '\n').replace(/^\/\/\s/, ''), start, end);
        } else {
          // 添加注释
          cm.replaceRange('// ' + selection.replace(/\n(?=.)/g, '\n// '), start, end);
        }
      }

      return {
        state,
        textarea,
        boxBindProps,
        getBindValue,
        setValue,
        isFullScreen,
        fullScreenIcon,
        onToggleFullScreen,
        refresh,
        containerRef,
      };
    },
  });
</script>

<style lang="less">
  //noinspection LessUnresolvedVariable
  @prefix-cls: ~'@{namespace}-code-editer';
  .@{prefix-cls} {
    &.auto-height {
      .CodeMirror {
        height: v-bind(height) !important;
        min-height: 100px;
      }
    }

    /* 全屏样式 */

    &.full-screen-parent {
      position: relative;

      .full-screen-icon {
        opacity: 0;
        color: black;
        width: 20px;
        height: 20px;
        line-height: 24px;
        background-color: white;
        position: absolute;
        top: 2px;
        right: 2px;
        z-index: 9;
        cursor: pointer;
        transition: opacity 0.3s;
        padding: 2px 0 0 1.5px;
      }

      &:hover {
        .full-screen-icon {
          opacity: 1;

          &:hover {
            background-color: rgba(255, 255, 255, 0.88);
          }
        }
      }

      &.full-screen {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        padding: 8px;
        background-color: #f5f5f5;

        .full-screen-icon {
          top: 12px;
          right: 12px;
        }

        .full-screen-child,
        .CodeMirror {
          height: 100%;
          max-height: 100%;
          min-height: 100%;
        }
      }

      .full-screen-child {
        height: 100%;
      }
    }
    
    /** VUEN-2344【vue3】这个样式有问题，是不是加个边框 */
    .CodeMirror{
      border: 1px solid #ddd;
    }
  }
  .CodeMirror-hints.idea,
  .CodeMirror-hints.monokai {
    z-index: 1001;
    max-width: 600px;
    max-height: 300px;
  }
  // update-begin--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
  html[data-theme='dark'] {
    .@{prefix-cls} {
      .CodeMirror {
        border: 1px solid #3a3a3a;
      }
    }
  }
  // update-end--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
</style>
