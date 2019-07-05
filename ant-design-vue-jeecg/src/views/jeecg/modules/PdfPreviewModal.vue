<template>
  <div style="display: none">
    <iframe
      :id="id"
      :src="url"
      frameborder="0"
      width="100%"
      height="550px"
      scrolling="auto">
    </iframe>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  export default {
    name: "PdfPreviewModal",
    data () {
      return {
        url:  window._CONFIG['pdfDomainURL'],
        id:"pdfPreviewIframe",
        headers:{}
      }
    },
    created () {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token}
    },
    computed:{

    },
    mounted(){
      window.addEventListener('message', this.handleScanFileMessage);
    },
    methods: {
      handleScanFileMessage (event) {
        // 根据上面制定的结构来解析iframe内部发回来的数据
        const data = event.data;
         console.log(data);
      },

      previewFiles (title,token) {
        var iframe = document.getElementById("pdfPreviewIframe");
        var json = {"title":title,"token":token};
        iframe.contentWindow.postMessage(json, "*");
      },

    }
  }
</script>

<style scoped>

</style>