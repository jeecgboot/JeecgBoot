<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="查看详情" :minHeight="600" :showCancelBtn="false" :showOkBtn="false" :height="88" :destroyOnClose="true">
    <a-card class="daily-article">
      <a-card-meta :title="content.titile" :description="'发布人：' + content.sender + ' 发布时间： ' + content.sendTime"> </a-card-meta>
      <a-divider />
      <div v-html="content.msgContent" class="article-content"></div>
      <div>
        <a-button v-if="hasHref" @click="jumpToHandlePage">前往办理<ArrowRightOutlined /></a-button>
      </div>
    </a-card>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { ArrowRightOutlined } from '@ant-design/icons-vue';
  import { useRouter } from 'vue-router'
  import xss from 'xss'
  import { options } from './XssWhiteList'
  
  const router = useRouter()
  
  import { ref, unref } from 'vue';
  const isUpdate = ref(true);
  const content = ref({});
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //data.record.msgContent = '<p>2323</p><input onmouseover=alert(1)>xss test';
      //update-begin-author:taoyan date:2022-7-14 for: VUEN-1702 【禁止问题】sql注入漏洞
      if(data.record.msgContent){
        //update-begin---author:wangshuai---date:2023-11-15---for:【QQYUN-7049】3.6.0版本 通知公告中发布的富文本消息，在我的消息中查看没有样式---
        data.record.msgContent = xss(data.record.msgContent,options);
        //update-end---author:wangshuai---date:2023-11-15---for:【QQYUN-7049】3.6.0版本 通知公告中发布的富文本消息，在我的消息中查看没有样式---
      }
      //update-end-author:taoyan date:2022-7-14 for: VUEN-1702 【禁止问题】sql注入漏洞
      content.value = data.record;
      showHrefButton();
    }
  });
  
  const hasHref = ref(false)
  //查看消息详情可以跳转
  function showHrefButton(){
    if(content.value.busId){
      hasHref.value = true;
    }
  }
  //跳转至办理页面
  function jumpToHandlePage(){
    let temp:any = content.value
    if(temp.busId){
      //这个busId是 任务ID 
      let jsonStr = temp.msgAbstract;
      let query = {};
      try {
        if(jsonStr){
          let temp = JSON.parse(jsonStr)
          if(temp){
            Object.keys(temp).map(k=>{
              query[k] = temp[k]
            });
          }
        }
      }catch(e){
        console.log('参数解析异常', e)
      }
      
      console.log('query', query, jsonStr)
      console.log('busId', temp.busId)
      
      if(Object.keys(query).length>0){
        // taskId taskDefKey procInsId
        router.push({ path: '/task/handle/' + temp.busId, query: query })
      }else{
        router.push({ path: '/task/handle/' + temp.busId })
      }
    }
    closeModal();
  }
  
</script>

<style scoped lang="less">
  .detail-iframe {
    border: 0;
    width: 100%;
    height: 100%;
    min-height: 600px;
  }
</style>
