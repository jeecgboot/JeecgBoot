<template>
  <a-list item-layout="horizontal" :data-source="showDataList">
    <template #renderItem="{ item }">
      <a-list-item style="padding: 3px 0">
        <div class="user-select-user-info" @click="(e) => onClickUser(e, item)">
          <div style="margin-left: 10px">
            <a-checkbox v-model:checked="checkStatus[item.id]" v-if="multi" />
            <a-radio v-model:checked="checkStatus[item.id]" v-else />
          </div>
          <div>
            <a-avatar v-if="item.avatar" :src="getFileAccessHttpUrl(item.avatar)"></a-avatar>
            <a-avatar v-else-if="item.avatarIcon" class="ant-btn-primary">
              <template #icon>
                <Icon :icon=" 'ant-design:'+item.avatarIcon " style="margin-top: 4px;font-size: 24px;"/>
              </template>
            </a-avatar>
            <a-avatar v-else>
              <template #icon><UserOutlined /></template>
            </a-avatar>
          </div>
          <div :style="nameStyle">
            {{ item.realname }}
          </div>
          <div :style="departStyle" class="ellipsis" :title="item.orgCodeTxt">
            {{ item.orgCodeTxt }}
          </div>
          <div style="width: 1px"></div>
        </div>
      </a-list-item>
    </template>
  </a-list>
</template>

<script lang="ts">
  import { UserOutlined } from '@ant-design/icons-vue';
  import { computed, toRaw, reactive, watchEffect, ref } from 'vue';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  
  export default {
    name: 'UserList',
    props: {
      multi: {
        type: Boolean,
        default: false,
      },
      dataList: {
        type: Array,
        default: () => [],
      },
      // 是否显示部门文本
      depart: {
        type: Boolean,
        default: false,
      },
      selectedIdList: {
        type: Array,
        default: () => [],
      },
      excludeUserIdList:{
        type: Array,
        default: () => [],
      }
    },
    components: {
      UserOutlined,
    },
    emits: ['selected', 'unSelect'],
    setup(props, { emit }) {
      function onClickUser(e, user) {
        e && prevent(e);
        let status = checkStatus[user.id];
        if (status === true) {
          emit('unSelect', user.id);
        } else {
          emit('selected', toRaw(user));
        }
      }

      function getTwoText(text) {
        if (!text) {
          return '';
        } else {
          return text.substr(0, 2);
        }
      }

      const departStyle = computed(() => {
        if (props.depart === true) {
          // 如果显示部门信息
          return {
            flex: 1,
          };
        } else {
          return {
            display: 'none',
          };
        }
      });

      const nameStyle = computed(() => {
        if (props.depart === true) {
          // 如果显示部门信息
          return {
            width: '200px',
          };
        } else {
          return {
            flex: 1,
          };
        }
      });

      function onChangeChecked(e) {
        console.error('onChangeChecked', e);
      }

     // const showDataList = ref<any[]>([])
      const checkStatus = reactive<any>({});
      watchEffect(() => {
        let arr1 = props.dataList;
        if (!arr1 || arr1.length === 0) {
          return;
        }
        let idList = props.selectedIdList;
        for (let item of arr1) {
          if (idList.indexOf(item.id) >= 0) {
            checkStatus[item.id] = true;
          } else {
            checkStatus[item.id] = false;
          }
        }
        
      
      });

      function prevent(e) {
        e.preventDefault();
        e.stopPropagation();
      }
//update-begin---author:wangshuai---date:2024-02-02---for:【QQYUN-8239】用户角色，添加用户 返回2页数据，实际只显示一页---
/*      function records2DataList() {
        let arr:any[] = [];
        let excludeList = props.excludeUserIdList;
        let records = props.dataList;
        if(records && records.length>0){
          for(let item of records){
            if(excludeList.indexOf(item.id)<0){
              arr.push({...item})
            }
          }
        }
        return arr;
      }*/
      
      const showDataList = computed(()=>{
/*        let excludeList = props.excludeUserIdList;
        if(excludeList && excludeList.length>0){
          return records2DataList();
        }*/
//update-end---author:wangshuai---date:2024-02-02---for:【QQYUN-8239】用户角色，添加用户 返回2页数据，实际只显示一页---
        return props.dataList;
      });

      return {
        onClickUser,
        getTwoText,
        departStyle,
        nameStyle,
        onChangeChecked,
        checkStatus,
        showDataList,
        getFileAccessHttpUrl
      };
    },
  };
</script>

<style lang="less">
  .user-select-user-info {
    display: flex;
    width: 100%;
    > div {
      height: 36px;
      line-height: 36px;
      margin-right: 10px;
    }
    .ellipsis {
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
  }
</style>
