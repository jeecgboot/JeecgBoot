<!--手动录入text-->
<template>
  <BasicModal title="段落详情" destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" :footer="null">
    <div class="p-2">
      <div class="header">
        <a-tag color="#a9c8ff">
          <span>{{hitTextDescData.source}}</span>
        </a-tag>
      </div>
      <div class="content">
        <MarkdownViewer :value="hitTextDescData.content" />
      </div>
    </div>

  </BasicModal>
</template>

<script lang="ts">
  import { ref } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModalInner } from '@/components/Modal';

  import BasicForm from '@/components/Form/src/BasicForm.vue';
  import { MarkdownViewer } from '@/components/Markdown';

  export default {
    name: 'AiTextDescModal',
    components: {
      MarkdownViewer,
      BasicForm,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      let hitTextDescData = ref<any>({})
      
      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        hitTextDescData.value.source = 'score' + ' ' + data.score.toFixed(2);
        hitTextDescData.value.content = data.content;
        setModalProps({ header: '300px' })
      });

      return {
        registerModal,
        hitTextDescData
      };
    },
  };
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }
  .header {
    font-size: 16px;
    font-weight: bold;
    margin-top: 10px;
  }
  .content {
    margin-top: 20px;
    max-height: 600px;
    overflow-y: auto;
    overflow-x: auto;
  }
  .title-tag {
    color: #477dee;
  }
</style>
