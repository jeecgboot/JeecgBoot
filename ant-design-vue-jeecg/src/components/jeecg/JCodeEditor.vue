<template>
  <div v-bind="fullScreenParentProps">
    <a-icon v-if="fullScreen" class="full-screen-icon" :type="iconType" @click="()=>fullCoder=!fullCoder"/>

    <div class="code-editor-cust full-screen-child">
      <textarea ref="textarea"></textarea>
      <span @click="nullTipClick" class="null-tip" :class="{'null-tip-hidden':hasCode}" :style="nullTipStyle">{{ placeholderShow }}</span>
      <template v-if="languageChange">
        <a-select v-model="mode" size="small" class="code-mode-select" @change="changeMode" placeholder="请选择主题">
          <a-select-option
            v-for="mode in modes"
            :key="mode.value"
            :value="mode.value">
            {{ mode.label }}
          </a-select-option>
        </a-select>
      </template>

    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  // 引入全局实例
  import _CodeMirror from 'codemirror'

  // 核心样式
  import 'codemirror/lib/codemirror.css'
  // 引入主题后还需要在 options 中指定主题才会生效 darcula  gruvbox-dark hopscotch  monokai
  import 'codemirror/theme/panda-syntax.css'
  //提示css
  import "codemirror/addon/hint/show-hint.css";

  // 需要引入具体的语法高亮库才会有对应的语法高亮效果
  // codemirror 官方其实支持通过 /addon/mode/loadmode.js 和 /mode/meta.js 来实现动态加载对应语法高亮库
  // 但 vue 貌似没有无法在实例初始化后再动态加载对应 JS ，所以此处才把对应的 JS 提前引入
  import 'codemirror/mode/javascript/javascript.js'
  import 'codemirror/mode/css/css.js'
  import 'codemirror/mode/xml/xml.js'
  import 'codemirror/mode/clike/clike.js'
  import 'codemirror/mode/markdown/markdown.js'
  import 'codemirror/mode/python/python.js'
  import 'codemirror/mode/r/r.js'
  import 'codemirror/mode/shell/shell.js'
  import 'codemirror/mode/sql/sql.js'
  import 'codemirror/mode/swift/swift.js'
  import 'codemirror/mode/vue/vue.js'

  // 尝试获取全局实例
  const CodeMirror = window.CodeMirror || _CodeMirror

  export default {
    name: 'JCodeEditor',
    props: {
      // 外部传入的内容，用于实现双向绑定
      value: {
        type: String,
        default: ''
      },
      // 外部传入的语法类型
      language: {
        type: String,
        default: null
      },
     languageChange:{
       type: Boolean,
       default:false,
       required:false
     },
      placeholder: {
        type: String,
        default: null
      },
      // 显示行号
      lineNumbers: {
        type: Boolean,
        default: true
      },
      // 是否显示全屏按钮
      fullScreen: {
        type: Boolean,
        default: false
      },
      // 全屏以后的z-index
      zIndex: {
        type: [Number, String],
        default: 999
      }
    },
    data () {
      return {
        // 内部真实的内容
        code: '',
        iconType: 'fullscreen',
        hasCode:false,
        // 默认的语法类型
        mode: 'javascript',
        // 编辑器实例
        coder: null,
        // 默认配置
        options: {
          // 缩进格式
          tabSize: 2,
          // 主题，对应主题库 JS 需要提前引入
          theme: 'panda-syntax',
          line: true,
         // extraKeys: {'Ctrl': 'autocomplete'},//自定义快捷键
          hintOptions: {
            tables: {
              users: ['name', 'score', 'birthDate'],
              countries: ['name', 'population', 'size']
            }
          },
        },
        // 支持切换的语法高亮类型，对应 JS 已经提前引入
        // 使用的是 MIME-TYPE ，不过作为前缀的 text/ 在后面指定时写死了
        modes: [{
          value: 'css',
          label: 'CSS'
        }, {
          value: 'javascript',
          label: 'Javascript'
        }, {
          value: 'html',
          label: 'XML/HTML'
        }, {
          value: 'x-java',
          label: 'Java'
        }, {
          value: 'x-objectivec',
          label: 'Objective-C'
        }, {
          value: 'x-python',
          label: 'Python'
        }, {
          value: 'x-rsrc',
          label: 'R'
        }, {
          value: 'x-sh',
          label: 'Shell'
        }, {
          value: 'x-sql',
          label: 'SQL'
        }, {
          value: 'x-swift',
          label: 'Swift'
        }, {
          value: 'x-vue',
          label: 'Vue'
        }, {
          value: 'markdown',
          label: 'Markdown'
        }],
        // code 编辑器 是否全屏
        fullCoder: false
      }
    },
    watch: {
      fullCoder:{
        handler(value) {
          if(value){
            this.iconType="fullscreen-exit"
          }else{
            this.iconType="fullscreen"
          }
        }
      },
      // value: {
      //   immediate: false,
      //   handler(value) {
      //     this._getCoder().then(() => {
      //       this.coder.setValue(value)
      //     })
      //   }
      // },
      language: {
        immediate: true,
        handler(language) {
          this._getCoder().then(() => {
            // 尝试从父容器获取语法类型
            if (language) {
              // 获取具体的语法类型对象
              let modeObj = this._getLanguage(language)

              // 判断父容器传入的语法是否被支持
              if (modeObj) {
                this.mode = modeObj.label
                this.coder.setOption('mode', `text/${modeObj.value}`)
              }
            }
          })
        }
      }
    },
    computed: {
      placeholderShow() {
        if (this.placeholder == null) {
          return `请在此输入${this.language}代码`
        } else {
          return this.placeholder
        }
      },
      nullTipStyle(){
        if (this.lineNumbers) {
          return { left: '36px' }
        } else {
          return { left: '12px' }
        }
      },
      // coder 配置
      coderOptions() {
        return {
          tabSize: this.options.tabSize,
          theme: this.options.theme,
          lineNumbers: this.lineNumbers,
          line: true,
          hintOptions: this.options.hintOptions
        }
      },
      fullScreenParentProps(){
        let props = {
          class: ['full-screen-parent', this.fullCoder ? 'full-screen' : ''],
          style: {}
        }
        if (this.fullCoder) {
          props.style['z-index'] = this.zIndex
        }
        return props
      }
    },
    mounted () {
      // 初始化
      this._initialize()
    },
    methods: {
      // 初始化
      _initialize () {
        // 初始化编辑器实例，传入需要被实例化的文本域对象和默认配置
        this.coder = CodeMirror.fromTextArea(this.$refs.textarea, this.coderOptions)
        // 编辑器赋值
        if(this.value||this.code){
          this.hasCode=true
          this.coder.setValue(this.value || this.code)
        }else{
          this.coder.setValue('')
          this.hasCode=false
        }
        // 支持双向绑定
        this.coder.on('change', (coder) => {
          this.code = coder.getValue()
          if(this.code){
            this.hasCode=true
          }else{
            this.hasCode=false
          }
          if (this.$emit) {
            this.$emit('input', this.code)
          }
        })
        this.coder.on('focus', () => {
          this.hasCode=true
        })
        this.coder.on('blur', () => {
          if(this.code){
            this.hasCode=true
          }else{
            this.hasCode=false
          }
        })

       /* this.coder.on('cursorActivity',()=>{
          this.coder.showHint()
        })*/

      },
      getCodeContent(){
        return this.code
      },
      setCodeContent(val){
        setTimeout(()=>{
          if(!val){
            this.coder.setValue('')
          }else{
            this.coder.setValue(val)
          }
        },300)
      },
      // 获取当前语法类型
      _getLanguage (language) {
        // 在支持的语法类型列表中寻找传入的语法类型
        return this.modes.find((mode) => {
          // 所有的值都忽略大小写，方便比较
          let currentLanguage = language.toLowerCase()
          let currentLabel = mode.label.toLowerCase()
          let currentValue = mode.value.toLowerCase()

          // 由于真实值可能不规范，例如 java 的真实值是 x-java ，所以讲 value 和 label 同时和传入语法进行比较
          return currentLabel === currentLanguage || currentValue === currentLanguage
        })
      },
      _getCoder() {
        let _this = this
        return new Promise((resolve) => {
          (function get() {
            if (_this.coder) {
              resolve(_this.coder)
            } else {
              setTimeout(get, 10)
            }
          })()
        })
      },
      // 更改模式
      changeMode (val) {
        // 修改编辑器的语法配置
        this.coder.setOption('mode', `text/${val}`)

        // 获取修改后的语法
        let label = this._getLanguage(val).label.toLowerCase()

        // 允许父容器通过以下函数监听当前的语法值
        this.$emit('language-change', label)
      },
      nullTipClick(){
        this.coder.focus()
      }
    }
  }
</script>

<style lang="less">
  .code-editor-cust{
    flex-grow:1;
    display:flex;
    position:relative;
    height:100%;
    .CodeMirror{
      flex-grow:1;
      z-index:1;
      .CodeMirror-code{
        line-height:19px;
      }

    }
    .code-mode-select{
      position:absolute;
      z-index:2;
      right:10px;
      top:10px;
      max-width:130px;
    }
    .CodeMirror{
      height: auto;
      min-height:100%;
    }
    .null-tip{
      position: absolute;
      top: 4px;
      left: 36px;
      z-index: 10;
      color: #ffffffc9;
      line-height: initial;
    }
    .null-tip-hidden{
      display: none;
    }
  }

  /* 全屏样式 */
  .full-screen-parent {
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
      top: 10px;
      left: 10px;
      width: calc(100% - 20px);
      height: calc(100% - 20px);
      padding: 10px;
      background-color: #f5f5f5;

      .full-screen-icon {
        top: 12px;
        right: 12px;
      }
      .full-screen-child {
        height: 100%;
        max-height: 100%;
        min-height: 100%;
      }
    }

    .full-screen-child {
      min-height: 120px;
      max-height: 320px;
      overflow:hidden;
    }

  }

.CodeMirror-cursor{
  height:18.4px !important;
}
</style>