<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" :width="width" :title="title" :footer="null">
      <!--  嵌入表单    -->
      <div v-if="type === 'menu'">
        <a-form layout="vertical" :model="appData">
          <a-form-item label="菜单名称">
            <a-input v-model:value="appData.name" readonly/>
          </a-form-item>  
          <a-form-item label="菜单地址">
            <a-input v-model:value="appData.menu" readonly/>
          </a-form-item>
          <a-form-item style="text-align:right">
            <a-button @click.prevent="copyMenu">复制菜单</a-button>
            <a-button  type="primary" style="margin-left: 10px" @click="copySql">复制SQL</a-button>
          </a-form-item>
        </a-form>
      </div>
      <!--   嵌入网站   -->
      <div v-else-if="type === 'web'" class="web">
    
        <div style="display: flex;margin: 0 auto">
          <div :class="activeKey===1?'active':''" class="web-img" @click="handleImageClick(1)">
            <img  src="../img/webEmbedded.png" />
          </div>
          <div style="margin-left: 10px" :class="activeKey===2?'active':''" class="web-img" @click="handleImageClick(2)">
            <img  src="../img/iconWebEmbedded.png" />
          </div>
        </div>    
        <div class="web-title" v-if="activeKey === 1">
          将以下 iframe 嵌入到你的网站中的目标位置
        </div>  
        <div class="web-title" v-else>
          将以下 script 添加到网页的body区域中
        </div>
        <div class="web-code" v-if="activeKey === 1">
          <div class="web-code-title">
            <div class="web-code-desc">
              html
            </div>
            <Icon class="pointer" icon="ant-design:copy-outlined" @click="copyIframe(1)"></Icon>
          </div>
          <div class="web-code-iframe">
              <pre> {{getIframeText(1)}} </pre>
          </div>
        </div>
        
        <div class="web-code" v-if="activeKey === 2">
          <div class="web-code-title">
            <div class="web-code-desc">
              html
            </div>
            <Icon class="pointer" icon="ant-design:copy-outlined" @click="copyIframe(2)"></Icon>
          </div>
          <div class="web-code-iframe">
            <pre> {{getIframeText(2)}} </pre>
          </div>
        </div>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { ref, unref } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModalInner } from '@/components/Modal';

  import BasicForm from '@/components/Form/src/BasicForm.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { buildUUID } from '@/utils/uuid';
  import { copyTextToClipboard } from '@/hooks/web/useCopyToClipboard';
  import { isDevMode } from '/@/utils/env';

  export default {
    name: 'AiAppSendModal',
    components: {
      BasicForm,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      //标题
      const title = ref<string>('嵌入网站');
      const $message = useMessage();
      //类型
      const type = ref<string>('web');
      //应用信息
      const appData = ref<any>({});
      //弹窗宽度
      const width = ref<string>("800px");
      //选中的key
      const activeKey = ref<number>(1);
      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        type.value = data.type;
        appData.value = data.data;
        appData.value.menu = "/ai/chat/"+ data.data.id
        activeKey.value = 1;
        let minHeight = 220;
        if(data.type === 'web'){
          title.value = '嵌入网站';
          width.value = '640px';
          minHeight = 500
        }else{
          title.value = '配置菜单';
          width.value = '500px';
        }
        setModalProps({ height: minHeight, bodyStyle: { padding: '10px' } });
      });

      /**
       * 复制菜单
       */
      function copyMenu() {
        copyText(appData.value.menu);
      }
      
      /**
       * 复制sql
       */
      function copySql() {
        const insertMenuSql = `INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
                               VALUES ('${buildUUID()}', NULL, '${appData.value.name}', '${appData.value.menu}', '1', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 0, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', null, NULL, NULL, 0)`;
        copyText(insertMenuSql);
      }

      /**
       * 获取当前文本
       */
      function getIframeText(value) {
        let locationUrl = document.location.protocol +"//" + window.location.host;
        //update-begin---author:wangshuai---date:2025-03-20---for:【QQYUN-11649】【AI】应用嵌入，支持一个小图标点击出聊天---
        if(value === 1){
          return '<iframe\n' +
              '   src="'+locationUrl+'/ai/app/chat/'+appData.value.id+'"\n' +
              '   style="width: 100%; height: 100%;">\n' +
              '</iframe>';
        }else{
            //update-begin---author:wangshuai---date:2025-03-28---for:【QQYUN-11649】应用嵌入，支持一个小图标点击出聊天---
            let path = "/src/views/super/airag/aiapp/chat/js/chat.js"
            if(!isDevMode()){
              path = "/chat/chat.js";
            }
            let text ='<script src=' + locationUrl + path +' id="e7e007dd52f67fe36365eff636bbffbd">'+'<'+'/script>';
            text += '\n <'+'script>\n';
            text += '    createAiChat({\n' +
                    '       appId:"'+ appData.value.id +'",\n';
            text += '       // 支持top-left左上, top-right右上, bottom-left左下, bottom-right右下\n';
            text += '       iconPosition:"bottom-right"\n';
            text += '    })\n';
            text += ' <'+'/script>';
            return text;
            //update-end---author:wangshuai---date:2025-03-28---for:【QQYUN-11649】应用嵌入，支持一个小图标点击出聊天---
        }
        //update-end---author:wangshuai---date:2025-03-20---for:【QQYUN-11649】【AI】应用嵌入，支持一个小图标点击出聊天---
      }

      /**
       * 复制iframe
       */
      function copyIframe(value) {
        copyText(getIframeText(value));
      }

      // 复制文本到剪贴板
      function copyText(text: string) {
        const success = copyTextToClipboard(text);
        if (success) {
          $message.createMessage.success('复制成功！');
        } else {
          $message.createMessage.error('复制失败！');
        }
        return success;
      }

      /**
       * 图片点击事件
       * 
       * @param value
       */
      function handleImageClick(value) {
        activeKey.value = value;
      }
      
      return {
        registerModal,
        title,
        type,
        appData,
        copySql,
        copyMenu,
        width,
        copyIframe,
        getIframeText,
        activeKey,
        handleImageClick,
      };
    },
  };
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }

  .type-title {
    color: #1d2025;
    margin-bottom: 4px;
  }

  .type-desc {
    color: #8f959e;
    font-weight: 400;
  }
  
  .web{
   padding: 0 10px;
  }
  .web-title{
    font-size: 13px;
    font-weight: bold;
    line-height: 16px;
  }
  .web-img{
    border-width: 1.5px;
    width: 240px;
    margin-top: 20px;
    border-radius: 6px;
    img{
      border-radius: 6px;
      width: 240px;
      height: 150px;
    }
    margin-bottom: 10px;
  }
  .active{
    border-color: rgb(41 112 255);
  }
  .web-code{
    border-width: 1.5px;
    margin-top: 20px;
    background-color: #f9fafb;
    border-color: #10182814;
    width: 100%;
    border-radius: 5px;
    .web-code-title{
      width: 100%;
      padding:10px;
      background-color: #f2f4f7;
      display: inline-flex;
      justify-content: space-between;
      align-items: center;
    }
    .web-code-desc{
      color: #354052;
      font-size: 13px;
      font-weight: 500;
      line-height: 16px;
    }
    .web-code-iframe{
      padding: 15px;
      line-height: 1.5;
      font-size: 13px;
      display: grid;
      gap: 4px;
      color: #354052;
    }
  }
  .pointer{
    cursor: pointer;
  }
</style>
