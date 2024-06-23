<template>
  <div class="comment-tabs-warp" v-if="showStatus">
    <a-tabs v-if="show" @change="handleChange" :animated="false">
      <a-tab-pane v-if="showComment" tab="评论" key="comment" class="comment-list-tab">
        <comment-list :tableName="tableName" :dataId="dataId" :datetime="datetime1" :otherHeight="otherHeight"></comment-list>
      </a-tab-pane>
      <a-tab-pane v-if="showFiles" tab="文件" key="file">
        <comment-files :tableName="tableName" :dataId="dataId" :datetime="datetime2"></comment-files>
      </a-tab-pane>
      <a-tab-pane v-if="showDataLog" tab="日志" key="log">
        <data-log-list :tableName="tableName" :dataId="dataId" :datetime="datetime3"></data-log-list>
      </a-tab-pane>
    </a-tabs>
  </div>
  <a-empty v-else description="新增页面不支持评论" />
</template>

<script>
  /**
   * 评论区域
   */
  import { propTypes } from '/@/utils/propTypes';
  import { computed, ref, nextTick } from 'vue';
  import CommentList from './CommentList.vue';
  import CommentFiles from './CommentFiles.vue';
  import DataLogList from './DataLogList.vue';

  export default {
    name: 'CommentPanel',
    components: {
      CommentList,
      CommentFiles,
      DataLogList,
    },
    props: {
      tableName: propTypes.string.def(''),
      dataId: propTypes.string.def(''),
      // 显示评论
      showComment: propTypes.bool.def(true),
      // 显示文件
      showFiles: propTypes.bool.def(true),
      // 显示日志
      showDataLog: propTypes.bool.def(true),
      // 其他需要减去的高度
      otherHeight: propTypes.number.def(0),
    },
    setup(props) {
      const showStatus = computed(() => {
        if (props.dataId && props.tableName) {
          return true;
        }
        return false;
      });

      const datetime1 = ref(1);
      const datetime2 = ref(1);
      const datetime3 = ref(1);
      const show = ref(true);
      function handleChange(e) {
        let temp = new Date().getTime();
        if (e == 'comment') {
          datetime1.value = temp;
        } else if (e == 'file') {
          datetime2.value = temp;
        } else {
          datetime3.value = temp;
        }
      }

      // VUEN-1978【bug】online关联记录和他表字段存在问题  20 修改完数据，再次打开不切换tab的时候，修改日志没有变化
      function reload() {
        let temp = new Date().getTime();
        datetime1.value = temp;
        datetime2.value = temp;
        datetime3.value = temp;
        // update-begin--author:liaozhiyang---date:20240527---for：【TV360X-486】再次打开重置组件内的状态
        // 再次打开重置组件内的状态
        show.value = false;
        nextTick(() => {
          show.value = true;
        });
        // update-end--author:liaozhiyang---date:20240527---for：【TV360X-486】再次打开重置组件内的状态
      }

      return {
        showStatus,
        handleChange,
        datetime1,
        datetime2,
        datetime3,
        reload,
        show,
      };
    },
  };
</script>

<style lang="less" scoped>
  .comment-tabs-warp {
    height: 100%;
    overflow: visible;
    > .ant-tabs {
      overflow: visible;
    }
  }
  //antd3升级后，表单右侧讨论样式调整
  :deep(.ant-tabs-top  .ant-tabs-nav, .ant-tabs-bottom  .ant-tabs-nav, .ant-tabs-top  div  .ant-tabs-nav, .ant-tabs-bottom  div  .ant-tabs-nav) {
    margin: 0 16px 0;
  }
</style>
