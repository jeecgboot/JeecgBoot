<template>
  <div class="j-markdown-editor" :id="dynamicId"/>
</template>

<script>
  import load from './load'
  import { md_js, md_zh_cn_js } from './Resource'
  import defaultOptions from '@/components/jeecg/JMarkdownEditor/default-options.js'

  export default {
    name: 'JMdEditorDyn',
    props: {
      value: {
        type: String,
        default: ''
      },
      id: {
        type: String,
        required: false,
        default() {
          return 'markdown-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
        }
      },
      options: {
        type: Object,
        default() {
          return defaultOptions
        }
      },
      mode: {
        type: String,
        default: 'markdown'
      },
      height: {
        type: String,
        required: false,
        default: '300px'
      },
      language: {
        type: String,
        required: false,
        default: 'zh-CN'
      }
    },
    data() {
      return {
        editor: null,
        dynamicId: this.id
      }
    },
    computed: {
      editorOptions() {
        const options = Object.assign({}, defaultOptions, this.options)
        options.initialEditType = this.mode
        options.height = this.height
        options.language = this.language
        return options
      }
    },
    watch: {
      value(newValue, preValue) {
        if (newValue !== preValue && newValue !== this.editor.getMarkdown()) {
          this.editor.setMarkdown(newValue)
        }
      },
      language(val) {
        this.destroyEditor()
        this.initEditor()
      },
      height(newValue) {
        this.editor.height(newValue)
      },
      mode(newValue) {
        this.editor.changeMode(newValue)
      }
    },
    mounted() {
      this.init()
    },
    destroyed() {
      this.destroyEditor()
    },
    methods: {
      init(){

        this.initEditor()
       /* load(md_js,'',()=>{
          load(md_zh_cn_js,'',()=>{

          })
        })*/
      },
      initEditor() {
        const Editor = toastui.Editor
        this.editor = new Editor({
          el: document.getElementById(this.dynamicId),
          ...this.editorOptions
        })
        if (this.value) {
          this.editor.setMarkdown(this.value)
        }
        this.editor.on('change', () => {
          this.$emit('change', this.editor.getMarkdown())
        })
      },
      destroyEditor() {
        if (!this.editor) return
        this.editor.off('change')
        this.editor.remove()
      },
      setMarkdown(value) {
        this.editor.setMarkdown(value)
      },
      getMarkdown() {
        return this.editor.getMarkdown()
      },
      setHtml(value) {
        this.editor.setHtml(value)
      },
      getHtml() {
        return this.editor.getHtml()
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
<style scoped lang="less">

  .j-markdown-editor {
    /deep/ .tui-editor-defaultUI {
      .te-mode-switch,
      .tui-scrollsync
      {
        line-height: 1.5;
      }
    }
  }

</style>