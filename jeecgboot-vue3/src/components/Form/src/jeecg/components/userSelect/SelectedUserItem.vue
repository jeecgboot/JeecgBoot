<template>
  <div class="user-selected-item">
    <div
      style="
        display: flex;
        flex-direction: row;
        height: 24px;
        border-radius: 12px;
        padding-right: 10px;
        vertical-align: middle;
        background-color: #f5f5f5;
      "
    >
      <span style="width: 24px; height: 24px; line-height: 20px; margin-right: 3px; display: inline-block">
        <a-avatar v-if="info.avatar" :src="getFileAccessHttpUrl(info.avatar)" :size="24"></a-avatar>
        
        <a-avatar v-else-if="info.avatarIcon" class="ant-btn-primary" :size="24" >
          <template #icon>
            <Icon :icon=" 'ant-design:'+info.avatarIcon " style="font-size: 16px;margin-top: 4px"/>
          </template>
        </a-avatar>
        
        <a-avatar v-else-if="info.selectType == 'sys_role'" :size="24" style="background-color: rgb(255, 173, 0);">
          <template #icon>
            <team-outlined style="font-size: 16px"/>
          </template>
        </a-avatar>
        <a-avatar v-else-if="info.selectType == 'sys_position'" :size="24" style="background-color: rgb(245, 34, 45);">
          <template #icon>
            <TagsOutlined style="font-size: 16px"/>
          </template>
        </a-avatar>
        
        <a-avatar :size="24" v-else>
          <template #icon><UserOutlined /></template>
        </a-avatar>
      </span>

      <div style="height: 24px; line-height: 24px" class="ellipsis">
        {{ info.realname || info.name }}
      </div>

      <div v-if="showClose" class="icon-close">
        <CloseOutlined @click="removeSelect"/>
      </div>
    </div>

    <div v-if="!showClose" class="icon-remove">
      <MinusCircleFilled @click="removeSelect" />
    </div>
  </div>
</template>

<script>
  import { UserOutlined, CloseOutlined, MinusCircleFilled, TagsOutlined, TeamOutlined } from '@ant-design/icons-vue';
  import {computed} from 'vue'
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  
  export default {
    name: 'SelectedUserItem',
    components: {
      UserOutlined,
      MinusCircleFilled,
      CloseOutlined,
      TagsOutlined,
      TeamOutlined
    },
    props: {
      info: {
        type: Object,
        default: () => {},
      },
      // 是否作为查询条件
      query:{
        type: Boolean,
        default: false,
      }
    },
    emits: ['unSelect'],
    setup(props, { emit }) {
      function removeSelect(e) {
        e.preventDefault();
        e.stopPropagation();
        emit('unSelect', props.info.id);
      }

      const showClose = computed(()=>{
        if(props.query===true){
          return true;
        }else{
          return false;
        }
      });
      
      return {
        showClose,
        removeSelect,
        getFileAccessHttpUrl
      };
    },
  };
</script>

<style lang="less">
  .user-selected-item {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-right: 8px;
    height: 30px;
    border-radius: 12px;
    line-height: 30px;
    vertical-align: middle;

    .ellipsis {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .icon-remove {
      position: absolute;
      top: -10px;
      right: -4px;
      font-size: 18px;
      width: 15px;
      height: 15px;
      cursor: pointer;
      display: none;
    }
    
    .icon-close{
      height: 22px;
      line-height: 24px;
      font-size: 10px;
      font-weight: bold;
      margin-left: 7px;
      &:hover{
        color: #0a8fe9;
      }
    }

    &:hover {
      .icon-remove {
        display: block;
      }
    }
  }
</style>
