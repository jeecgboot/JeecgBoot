<template>
  <div class="user-avatar-info">
      <a-popover title="" :overlayStyle="{width: '250px'}">
          <template #content>
             <div style="display: flex;flex-direction: row;align-items: center">
                 <div style="width: 60px;text-align: center">
                     <a-avatar v-if="userAvatar" :src="userAvatar" :size="47"/>
                     <a-avatar v-else :size="47">{{ getAvatarText() }}</a-avatar>
                 </div>
                 <div style="flex: 1;display: flex;flex-direction: column;margin-left: 12px">
                     <div style="color: #000;display: inline-block;font-size: 15px;font-weight: 700;margin-top: 3px;vertical-align: top;width: 170px;">
                         {{ userLabel }}
                     </div>
                     <div style="color: #757575;display: block;margin-top: 4px;">
                         {{ phone }}
                     </div>
                 </div>
             </div>
          </template>
          <span style="cursor: pointer">
              <a-avatar v-if="userAvatar" :src="userAvatar" :loadError="loadError"/>
              <a-avatar v-else>{{ getAvatarText() }}</a-avatar>
          </span>
      </a-popover>
      <span class="realname-ellipsis">
          {{ userLabel }}
      </span>
  </div>
</template>

<script lang="ts">
  import { ref, watchEffect, defineComponent } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  
  export default defineComponent({
    name: 'UserAvatar',
    props: {
      username: {
        type: String,
        default: '',
      },
      detail:{
        type: Object,
        default: ()=>{},
      }
    },
    setup(props) {
      const userAvatar = ref('');
      const userLabel = ref('');
      const phone = ref('');

      watchEffect(async ()=>{
        userAvatar.value = '';
        userLabel.value = '';
        phone.value = '';
        let username = props.username;
        if(username){
          await initUserInfo(username);
        }
        let userInfo = props.detail;
        if(userInfo){
          if(userInfo.avatar){
            userAvatar.value = getFileAccessHttpUrl(userInfo.avatar);
          }
          if(userInfo.realname){
            userLabel.value = userInfo.realname;
          }
          if(userInfo.phone){
            phone.value = userInfo.phone;
          }
        }
      });

      async function initUserInfo(val) {
        const params = {
          username: val,
        };
        const url = '/sys/user/getMultiUser';
        const data = await defHttp.get({ url, params }, {isTransformResponse: false});
        if(data && data.length > 0){
          let temp = data[0].avatar;
          if (temp) {
            userAvatar.value = getFileAccessHttpUrl(temp)
          }
          userLabel.value = data[0].realname;
          phone.value = data[0].phone;
        }else{
          console.log(data)
        }
      }

      function getAvatarText() {
        let text = userLabel.value;
        if (!text) {
          text = props.username;
        }
        if (text) {
          if (text.length > 2) {
            return text.substr(0, 2);
          } else {
            return text;
          }
        }
        return '';
      }
      
      function loadError() {
        userAvatar.value = '';
        return true;
      }
      

      return {
        userAvatar,
        userLabel,
        getAvatarText,
        phone,
        loadError
      };
    },
  });
</script>

<style scoped lang="less">
    .user-avatar-info{
        .ant-avatar-image{
            cursor: pointer;
        }
        .realname-ellipsis {
            overflow: hidden;
            text-overflow: ellipsis;
            vertical-align: top;
            white-space: nowrap;
            height: 32px;
            line-height: 32px;
            display: inline-block;
            margin-left: 10px;
        }
    }
    
</style>
