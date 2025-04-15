<template>
    <BasicModal
        @register="register"
        :getContainer="getContainer"
        :canFullscreen="false"
        :title="title"
        :width="500"
        destroyOnClose
        @ok="handleOk"
        wrapClassName="j-user-select-modal2" >

        <div style="position: relative; min-height: 350px">
            <div style="width: 100%">
                <a-input v-model:value="searchText" allowClear style="width: 100%" placeholder="搜索">
                    <template #prefix>
                        <SearchOutlined style="color: #c0c0c0" />
                    </template>
                </a-input>
            </div>

            <!-- tabs -->
            <div class="modal-select-list-container">
                <div class="scroll">
                    <div class="content" style="right: -10px">
                        
                        <label class="item" v-for="item in showDataList" @click="(e)=>onSelect(e, item)">
                            <a-checkbox v-model:checked="item.checked">
                                <span class="text">{{ item.name }}</span>
                            </a-checkbox>
                        </label>
                    </div>
                    
                </div>
            </div>

            <!-- 选中用户 -->
            <div class="selected-users" style="width: 100%; overflow-x: hidden">
                <SelectedUserItem v-for="item in selectedList" :info="item" @unSelect="unSelect" />
            </div>
        </div>
    </BasicModal>
</template>

<script lang="ts">
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { SearchOutlined, CloseOutlined } from '@ant-design/icons-vue';
  import SelectedUserItem from '../userSelect/SelectedUserItem.vue';
  import { defHttp } from '/@/utils/http/axios';

  import { computed, ref, toRaw, watch } from 'vue';
  export default {
    name: 'RoleSelectModal',
    components: {
      BasicModal,
      SearchOutlined,
      CloseOutlined,
      SelectedUserItem,
    },
    props: {
      multi: {
        type: Boolean,
        default: true,
      },
      getContainer: {
        type: Function,
        default: null,
      },
      title:{
        type: String,
        default: '',
      },
      type: {
        type: String,
        default: 'sys_role',
      },
      appId: {
        type: String,
        default: '',
      }
    },
    emits: ['selected', 'register'],
    setup(props, { emit }) {

      const searchText = ref('');
      const selectedIdList = computed(() => {
        let arr = selectedList.value;
        if (!arr || arr.length == 0) {
          return [];
        } else {
          return arr.map((k) => k.id);
        }
      });

      watch(()=>props.appId, async (val)=>{
        if(val){
          await loadDataList();
        }
      }, {immediate: true});
      
      
      // 弹窗事件
      const [register] = useModalInner((data) => {
        let list = dataList.value;
        if(!list || list.length ==0 ){
        }else{
          let selectedIdList = data.list || [];
          for(let item of list){
            if(selectedIdList.indexOf(item.id)>=0){
              item.checked = true;
            }else{
              item.checked = false;
            }
          }
        }
      });

      // 确定事件
      function handleOk() {
        let arr = toRaw(selectedIdList.value);
        emit('selected', arr, toRaw(selectedList.value));
      }
      
      const dataList = ref<any[]>([]);
      const showDataList = computed(()=>{
        let list = dataList.value;
        if(!list || list.length ==0 ){
          return []
        }
        let text = searchText.value;
        if(!text){
          return list
        }
        return list.filter(item=>item.name.indexOf(text)>=0)
      });

      const selectedKeys = ref<string[]>([]);
      const selectedList = computed(()=>{
        let list = dataList.value;
        if(!list || list.length ==0 ){
          return []
        }
        list = list.filter(item=>item.checked)
        // 根据 selectedKeys 的顺序排序
        let arr: any[] = [];
        for (let key of selectedKeys.value) {
          let item = list.find(item => item.id == key);
          if (item) {
            arr.push(item);
          }
        }
        return arr;
      });

      function unSelect(id) {
        let list = dataList.value;
        if(!list || list.length ==0 ){
          return;
        }
        // update-begin--author:liaozhiyang---date:20250414--for：【issues/8078】角色选择组件点击文字部分会一直选中
        let findItem = list.find((item) => item.id == id);
        findItem.checked = false;
        selectedKeys.value = selectedKeys.value.filter((key) => key != id);
        // update-end--author:liaozhiyang---date:20250414--for：【issues/8078】角色选择组件点击文字部分会一直选中
      }
      
      async function loadDataList() {
        let params = {
          pageNo: 1,
          pageSize: 200,
          column: 'createTime',
          order: 'desc'
        };
        const url = '/sys/role/listByTenant';
        const data = await defHttp.get({ url, params }, { isTransformResponse: false });
        if (data.success) {
          const { records } = data.result;
          let arr:any[] = [];
          if(records && records.length>0){
            for(let item of records){
              arr.push({
                id: item.id,
                name: item.name || item.roleName,
                code: item.roleCode,
                selectType: props.type,
                checked: false
              })
            }
          }
          dataList.value = arr;
        } else {
          console.error(data.message);
        }
        console.log('loadDataList', data);
      }

      function onSelect(e, item) {
        prevent(e);
        // update-begin--author:liaozhiyang---date:20250414--for：【issues/8078】角色选择组件点击文字部分会一直选中
        // 单选模式下，先清除所有选中状态
        if (!props.multi) {
          dataList.value.forEach(dataItem => {
            if (dataItem.id != item.id) {
              dataItem.checked = false;
            }
          });
          // 清空已选择的keys
          selectedKeys.value = [];
        }

        // 切换当前项的选中状态
        item.checked = !item.checked;

        // 更新selectedKeys数组
        if (item.checked) {
          selectedKeys.value.push(item.id);
        } else {
          selectedKeys.value = selectedKeys.value.filter(key => key !== item.id);
        }
        // update-end--author:liaozhiyang---date:20250414--for：【issues/8078】角色选择组件点击文字部分会一直选中
      }

      function prevent(e) {
        e.preventDefault();
        e.stopPropagation();
      }

      return {
        register,
        showDataList,
        searchText,
        handleOk,
        selectedList,
        selectedIdList,
        unSelect,
        onSelect
      
      };
    },
  };
</script>
<style scoped lang="less">
    .modal-select-list-container{
        height: 352px;
        margin-top: 12px;
        overflow: auto;
        .scroll{
            height: 100%;
            position: relative;
            width: 100%;
            overflow: hidden;
            .content{
                bottom: 0;
                left: 0;
                overflow: scroll;
                overflow-x: hidden;
                position: absolute;
                right: 0;
                top: 0;
                .item{
                    padding: 7px 5px;
                    cursor: pointer;
                    display: block;
                    &:hover{
                        background-color: #f5f5f5;
                    }
                }
               
            }
        }

       
    }
</style>

<style lang="less">
    .j-user-select-modal2 {
        .depart-select {
            .ant-select-selector {
                color: #fff !important;
                background-color: #409eff !important;
                border-radius: 5px !important;
            }
            .ant-select-selection-item,
            .ant-select-arrow {
                color: #fff !important;
            }
        }
        .my-search {
            position: absolute;
            top: 14px;
            z-index: 1;
            &.all-width {
                width: 100%;
            }

            .anticon {
                cursor: pointer;
                &:hover {
                    color: #0a8fe9 !important;
                }
            }
            .hidden {
                display: none;
            }
        }

        .my-tabs {
        }

        .selected-users {
            display: flex;
            flex-wrap: wrap;
            flex-direction: row;
            padding-top: 15px;
        }

        .scroll-container {
            padding-bottom: 0 !important;
        }
    }
</style>
