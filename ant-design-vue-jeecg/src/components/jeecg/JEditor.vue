<template>
  <div class="tinymce-editor">
    <editor
      v-if="!reloading"
      v-model="myValue"
      :init="init"
      :disabled="disabled"
      @onClick="onClick">
    </editor>
  </div>
</template>

<script>
  import tinymce from 'tinymce/tinymce'
  import Editor from '@tinymce/tinymce-vue'
  import 'tinymce/themes/silver/theme'
  import 'tinymce/plugins/image'
  import 'tinymce/plugins/link'
  import 'tinymce/plugins/media'
  import 'tinymce/plugins/table'
  import 'tinymce/plugins/lists'
  import 'tinymce/plugins/contextmenu'
  import 'tinymce/plugins/wordcount'
  import 'tinymce/plugins/colorpicker'
  import 'tinymce/plugins/textcolor'
  import 'tinymce/plugins/fullscreen'
  import 'tinymce/icons/default'
  import { uploadAction,getFileAccessHttpUrl } from '@/api/manage'
  import { getVmParentByName } from '@/utils/util'
  export default {
    components: {
      Editor
    },
    props: {
      value: {
        type: String,
        required:false
      },
      triggerChange:{
        type: Boolean,
        default: false,
        required:false
      },
      disabled: {
        type: Boolean,
        default: false
      },
      plugins: {
        type: [String, Array],
        default: 'lists image link media table textcolor wordcount contextmenu fullscreen'
      },
      toolbar: {
        type: [String, Array],
        default: 'undo redo |  formatselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists link unlink image media table | removeformat | fullscreen',
        branding:false
      }
    },
    data() {
      return {
        //初始化配置
        init: {
          language_url: '/tinymce/langs/zh_CN.js',
          language: 'zh_CN',
          skin_url: '/tinymce/skins/lightgray',
          height: 300,
          plugins: this.plugins,
          toolbar: this.toolbar,
          branding: false,
          menubar: false,
          toolbar_drawer: false,
          images_upload_handler: (blobInfo, success) => {
            let formData = new FormData()
            formData.append('file', blobInfo.blob(), blobInfo.filename());
            formData.append('biz', "jeditor");
            formData.append("jeditor","1");
            uploadAction(window._CONFIG['domianURL']+"/sys/common/upload", formData).then((res) => {
              if (res.success) {
                if(res.message == 'local'){
                  const img = 'data:image/jpeg;base64,' + blobInfo.base64()
                  success(img)
                }else{
                  let img = getFileAccessHttpUrl(res.message)
                  success(img)
                }
              }
            })
          }
        },
        myValue: this.value,
        reloading: false,
      }
    },
    mounted() {
      this.initATabsChangeAutoReload()
    },
    methods: {

      reload() {
        this.reloading = true
        this.$nextTick(() => this.reloading = false)
      },

      onClick(e) {
        this.$emit('onClick', e, tinymce)
      },
      //可以添加一些自己的自定义事件，如清空内容
      clear() {
        this.myValue = ''
      },

      /**
       * 自动判断父级是否是 <a-tabs/> 组件，然后添加事件监听，自动触发reload()
       *
       * 由于 tabs 组件切换会导致 tinymce 无法输入，
       * 只有重新加载才能使用（无论是vue版的还是jQuery版tinymce都有这个通病）
       */
      initATabsChangeAutoReload() {
        // 获取父级
        let tabs = getVmParentByName(this, 'ATabs')
        let tabPane = getVmParentByName(this, 'ATabPane')
        if (tabs && tabPane) {
          // 用户自定义的 key
          let currentKey = tabPane.$vnode.key
          // 添加事件监听
          tabs.$on('change', (key) => {
            // 切换到自己时执行reload
            if (currentKey === key) {
              this.reload()
            }
          })
          //update--begin--autor:liusq-----date:20210316------for：富文本编辑器tab父组件可能导致的赋值问题------
          this.reload()
          //update--end--autor:liusq-----date:20210316------for：富文本编辑器tab父组件可能导致的赋值问题------
        }else{
          //update--begin--autor:wangshuai-----date:20200724------for：富文本编辑器切换tab无法修改------
          let tabLayout = getVmParentByName(this, 'TabLayout')
          //update--begin--autor:liusq-----date:20210713------for：处理特殊情况excuteCallback不能使用------
          try {
            tabLayout.excuteCallback(() => {
              this.reload()
            })
          } catch (error) {
            if (tabLayout) {
              this.reload()
            }
          }
          //update--end--autor:liusq-----date:20210713------for：处理特殊情况excuteCallback不能使用------
          //update--begin--autor:wangshuai-----date:20200724------for：文本编辑器切换tab无法修改------
        }
      },

    },
    watch: {
      value(newValue) {
        this.myValue = (newValue == null ? '' : newValue)
      },
      myValue(newValue) {
        if(this.triggerChange){
          this.$emit('change', newValue)
        }else{
          this.$emit('input', newValue)
        }
      }
    }
  }

</script>
<style scoped>
</style>