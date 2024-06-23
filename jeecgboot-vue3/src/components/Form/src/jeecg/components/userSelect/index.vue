<template>
  <div>
    <div v-if="isSearchFormComp" @click="click2Add" :class="disabled?'disabled-user-select':''" style="padding:0 5px;background-color: #fff;border: 1px solid #ccc;border-radius: 3px;box-sizing: border-box;display:flex;color: #9e9e9e;font-size: 14px;flex-wrap: wrap;min-height: 32px;">
      <template v-if="selectedUserList.length > 0">
        <SelectedUserItem v-for="item in showUserList" :info="item" @unSelect="unSelectUser" query />
      </template>
      <span v-else style="height: 30px;line-height: 30px;display: inline-block;margin-left: 7px;color: #bfbfbf;">请选择用户</span>
      <div v-if="ellipsisInfo.status" class="user-selected-item">
        <div class="user-select-ellipsis">
          <span style="color: red">+{{ellipsisInfo.count}}...</span>
        </div>
      </div>
    </div>    
    
    <div v-else style="display: flex; flex-wrap: wrap; flex-direction: row" >
      <template v-if="selectedUserList.length > 0">
        <SelectedUserItem v-for="item in selectedUserList" :info="item" @unSelect="unSelectUser" />
      </template>
      <a-button v-if="showAddButton" shape="circle" @click="onShowModal"><PlusOutlined /></a-button>
    </div>

    <user-select-modal :inSuperQuery="inSuperQuery" :multi="multi" :getContainer="getContainer" @register="registerModal" @selected="onSelected" :izExcludeMy="izExcludeMy"></user-select-modal>
  </div>
</template>

<script lang="ts">
  import { defineComponent, watch, ref, computed, toRaw } from 'vue';
  import { Form } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { useModal } from '/@/components/Modal';
  import UserSelectModal from './UserSelectModal.vue';
  import { defHttp } from '/@/utils/http/axios';
  import SelectedUserItem from './SelectedUserItem.vue';
  import { mySelfExpress, mySelfData } from './useUserSelect'

  export default defineComponent({
    name: 'UserSelect',
    components: {
      PlusOutlined,
      UserSelectModal,
      SelectedUserItem,
    },
    props: {
      store: {
        type: String,
        default: 'id',
      },
      value: {
        type: String,
        default: '',
      },
      multi: {
        type: Boolean,
        default: false,
      },
      getContainer: {
        type: Function,
        default: null,
      },
      // 是否作为查询条件
      query:{
        type: Boolean,
        default: false,
      },
      //最多显示几个人员-query为true有效
      maxCount:{
        type: Number,
        default: 2
      },
      disabled:{
        type: Boolean,
        default: false,
      },
      //是否排除我自己
      izExcludeMy:{
        type: Boolean,
        default: false,
      },
      //是否在高级查询中作为条件 可以选择当前用户
      inSuperQuery:{
        type: Boolean,
        default: false,
      }
    },
    emits: ['update:value', 'change'],
    setup(props, { emit }) {
      const formItemContext = Form.useInjectFormItemContext();
      const loading = ref(true);
      const selectedUserList = ref<any[]>([]);
      const showUserList = computed(()=>{
        let list = selectedUserList.value
        let max = props.maxCount;
        if(list.length<=max){
          return list;
        }
        return list.filter((_item, index)=>index<max);
      });
      const ellipsisInfo = computed(()=>{
        let max = props.maxCount;
        let len = selectedUserList.value.length
        if(len > max){
          return {status: true, count: len-max};
        }else{
          return {status: false}
        }
      });

      // 注册弹窗
      const [registerModal, { openModal, closeModal }] = useModal();
      function onShowModal() {
        if(props.disabled===true){
          return ;
        }
        let list = toRaw(selectedUserList.value);
        openModal(true, {
          list,
        });
      }

      function onSelected(arr) {
        console.log('onSelected', arr);
        selectedUserList.value = arr;
        onSelectedChange();
        closeModal();
      }

      function onSelectedChange() {
        loading.value = false;
        let temp: any[] = [];
        let arr = selectedUserList.value;
        if (arr && arr.length > 0) {
          temp = arr.map((k) => {
            return k[props.store];
          });
        }
        let str = temp.join(',');
        emit('update:value', str);
        emit('change', str);
        formItemContext.onFieldChange();
        console.log('选中数据', str);
      }

      watch(
        () => props.value,
        async (val) => {
          if (val) {
            if (loading.value === true) {
              await getUserList(val);
            }
          } else {
            selectedUserList.value = [];
          }
          loading.value = true;
        },
        { immediate: true }
      );

      async function getUserList(ids) {
        let hasUserExpress = false;
        let paramIds = ids;
        let idList = [];
        selectedUserList.value = [];
        if(ids){
          // update-begin-author:sunjianlei date:20230330 for: 修复用户选择器逗号分割回显不生效的问题
          let tempArray = ids.split(',').map(s => s.trim()).filter(s => s != '');
          if (tempArray.includes(mySelfExpress)) {
            hasUserExpress = true;
            idList = tempArray.filter(item => item != mySelfExpress);
          } else {
            idList = tempArray;
          }
          // update-end-author:sunjianlei date:20230330 for: 修复用户选择器逗号分割回显不生效的问题
        }

        if(idList.length>0){
          paramIds = idList.join(',')
          const url = '/sys/user/list';
          let params = {
            [props.store]: paramIds,
          };
          const data = await defHttp.get({ url, params }, { isTransformResponse: false });
          console.log('getUserList', data);
          if (data.success) {
            const { records } = data.result;
            selectedUserList.value = records;
          } else {
            console.error(data.message);
          }
        }
        if(hasUserExpress){
          let temp = selectedUserList.value;
          temp.push({...mySelfData})
        }
      }

      const showAddButton = computed(() => {
        if(props.disabled === true){
          return false;
        }
        if (props.multi === true) {
          return true;
        } else {
          if (selectedUserList.value.length > 0) {
            return false;
          } else {
            return true;
          }
        }
      });

      function unSelectUser(id) {
        console.log('unSelectUser', id);
        let arr = selectedUserList.value;
        let index = -1;
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].id == id) {
            index = i;
            break;
          }
        }
        if (index >= 0) {
          arr.splice(index, 1);
          selectedUserList.value = arr;

          onSelectedChange();
        }
      }
      
      function click2Add(e) {
        e.preventDefault();
        e.stopPropagation();
        onShowModal();
      }
      
      const isSearchFormComp = computed(()=>{
        if(props.query===true){
          return true;
        }else{
          return false
        }
      });
      
      return {
        registerModal,
        onShowModal,
        isSearchFormComp,
        onSelected,
        showAddButton,
        unSelectUser,
        selectedUserList,
        showUserList,
        ellipsisInfo,
        click2Add
      };
    },
  });
</script>

<style lang="less" scoped>
  .user-select-ellipsis{
    width: 40px;
    height: 24px;
    text-align: center;
    line-height: 22px;
    border-radius: 8px;
    background: #f5f5f5;
    border: 1px solid #f0f0f0;
  }
  .disabled-user-select{
    cursor: not-allowed;
    background-color: #f5f5f5 !important;
  }
</style>
