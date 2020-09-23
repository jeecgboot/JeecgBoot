<template>
  <div class="jeecg-editor-ty" :class="fullCoder?'jeecg-editor-max':'jeecg-editor-min'">
    <a-icon v-if="fullScreen" class="full-screen-icon" :type="iconType" @click="()=>fullCoder=!fullCoder"/>
    <textarea :id="dynamicId" />
    <span @click="nullTipClick" class="null-tip" :class="{'null-tip-hidden': hasCode}" :style="nullTipStyle">{{ placeholderShow }}</span>
  </div>
</template>

<script>

  import '@/assets/less/codemirror_idea.css'
  import './cm_hint.js'

  export default {
    name: 'JsCodeEditorDyn',
    props:{
      id: {
        type: String,
        default: function() {
          return 'vue-editor-' + new Date() + ((Math.random() * 1000).toFixed(0) + '')
        }
      },
      value: {
        type: String,
        default: ''
      },
      // 显示行号
      lineNumbers: {
        type: Boolean,
        default: true
      },
      placeholder: {
        type: String,
        default: ''
      },
      zIndex: {
        type: [Number, String],
        default: 999
      },
      autoHeight: {
        type: [String, Boolean],
        default: true
      },
      // 不自适应高度的情况下生效的固定高度
      height: {
        type: [String, Number],
        default: '240px'
      },
      autoHeight: {
        type: [String, Boolean],
        default: true
      },
      // 是否显示全屏按钮
      fullScreen: {
        type: Boolean,
        default: false
      },

    },
    data(){
      return {
        dynamicId: this.id,
        coder: '',
        hasCode: false,
        code: '',
        // code 编辑器 是否全屏
        fullCoder: false,
        iconType: 'fullscreen',
      }
    },
    computed:{
      placeholderShow() {
        if (this.placeholder == null) {
          return `请在此输入javascript代码`
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
      isAutoHeight() {
        let {autoHeight} = this
        if (typeof autoHeight === 'string' && autoHeight.toLowerCase().trim() === '!ie') {
          autoHeight = !(isIE() || isIE11())
        } else {
          autoHeight = true
        }
        return autoHeight
      },
      fullScreenParentProps() {
        let props = {
          class: {
            'full-screen-parent': true,
            'full-screen': this.fullCoder,
            'auto-height': this.isAutoHeight
          },
          style: {}
        }
        if (this.fullCoder) {
          props.style['z-index'] = this.zIndex
        }
        if (!this.isAutoHeight) {
          props.style['height'] = (typeof this.height === 'number' ? this.height + 'px' : this.height)
        }
        return props
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
      }
    },
    mounted() {
      this.init()
    },
    methods:{
      init(){
        this.main();
      },
      main(){
        let obj = document.getElementById(this.dynamicId);
        const that = this;
        let editor = CodeMirror.fromTextArea(obj,{
          theme:'idea',
          lineNumbers: this.lineNumbers,
          lineWrapping: true,
          mode: "javascript",
          indentUnit: 1,
          indentWithTabs: true,
          styleActiveLine: true,
          /* styleSelectedText: false, */
          extraKeys: {
            "F11": function(cm) {
              that.fullCoder = !that.fullCoder
              cm.setOption("fullScreen", !cm.getOption("fullScreen"));
            },
            "Esc": function(cm) {
              that.fullCoder = false
              if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
            },
            "Alt-/":  function(cm) {
              let a = cm.getValue()+""
              console.log('a',a)
              cm.showHint();
            },
            "Tab": (cm) => {
              if (cm.somethingSelected()) {
                cm.indentSelection('add');
              } else {
                //cm.indentLine(cm.getCursor().line, "add");
                //走两格 第三格输入
                cm.replaceSelection(Array(3).join(" "), "end", "+input");
              }
            },
            "Shift-Tab": (cm) => {
              if (cm.somethingSelected()) {
                cm.indentSelection('subtract');
              } else {
                // cm.indentLine(cm.getCursor().line, "subtract");
                const cursor = cm.getCursor();
                // 光标回退 indexUnit 字符
                cm.setCursor({line: cursor.line, ch: cursor.ch - 4});
              }
              return ;
            }
          }
        })
        this.coder = editor
        this.addEvent();
        this.setCoderValue();
      },
      setCoderValue(){
        if(this.value||this.code){
          this.hasCode=true
          this.setCodeContent(this.value || this.code)
        }else{
          this.coder.setValue('')
          this.hasCode=false
        }
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
      addEvent(){
        const that = this;
        this.coder.on('cursorActivity',function(wl) {
          let arr = wl.state.activeLines
          if(arr && arr.length>0){
            let text = arr[0].text
            if(text.lastIndexOf('that.')>=0){
              that.coder.showHint();
            }
          }
        });
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
        });
        this.coder.on('focus', () => {
          this.hasCode=true
        });
        this.coder.on('blur', () => {
          if(this.code){
            this.hasCode=true
          }else{
            this.hasCode=false
          }
        });
      },
      loadResource(src,type){
        return new Promise((resolve,reject)=>{
          load(src,type,(msg)=>{
            if(!msg){
              resolve();
            }else{
              reject(msg)
            }
          })
        })
      },
      nullTipClick(){
        this.coder.focus()
      },
      fullToggle(){
        this.fullCoder = !this.fullCoder
        this.coder.setOption("fullScreen", this.fullCoder);
      }

    }


  }
</script>
<style lang="less" >
  .jeecg-editor-ty{
    position: relative;

    .full-screen-icon {
      opacity: 0;
      color: black;
      width: 20px;
      height: 20px;
      line-height: 24px;
      background-color: white;
      position: absolute;
      top: 4px;
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

    .null-tip{
      position: absolute;
      top: 4px;
      left: 36px;
      z-index: 10;
      font-size:16px;
      color: #acaaaac9;
      line-height: initial;
    }
    .null-tip-hidden{
      display: none;
    }
  }
  .jeecg-editor-max{
    position: fixed;
    left: 0;
    top: 0;
    z-index: 999;
    height: 100%;
    width: 100% !important;

    .CodeMirror{
      position: inherit !important;
      width: 100%;
      height: 100%;
    }
    .full-screen-icon{
      z-index:9999;
    }
  }
</style>