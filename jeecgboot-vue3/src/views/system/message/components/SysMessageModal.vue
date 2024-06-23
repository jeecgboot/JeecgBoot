<template>
  <BasicModal
    :canFullscreen="false"
    :draggable="false"
    :closable="false"
    @register="registerModal"
    wrapClassName="sys-msg-modal"
    :width="800"
    :footer="null"
    destroyOnClose
  >
    <template #title>
      <div class="sys-msg-modal-title">
        <div class="title"></div>
        <div class="ant-tabs-nav-wrap">
          <div class="ant-tabs-nav-scroll">
            <div class="ant-tabs-nav ant-tabs-nav-animated">
              <div>
                <div
                  @click="(e) => handleChangeTab(e, 'all')"
                  role="tab"
                  aria-disabled="false"
                  aria-selected="false"
                  class="ant-tabs-tab"
                  :class="{ 'ant-tabs-tab-active': activeKey == 'all' }"
                >
                  全部消息
                </div>
                <div
                  @click="(e) => handleChangeTab(e, 'star')"
                  role="tab"
                  aria-disabled="false"
                  aria-selected="true"
                  class="ant-tabs-tab"
                  :class="{ 'ant-tabs-tab-active': activeKey == 'star' }"
                >
                  标星消息
                </div>
              </div>
              <div
                class="ant-tabs-ink-bar ant-tabs-ink-bar-animated"
                :style="{
                  transform: activeKey == 'all' ? 'translate3d(130px, 0px, 0px)' : 'translate3d(215px, 0px, 0px)',
                  display: 'block',
                  width: '88px',
                  height: '1px'
                }"
              ></div>
            </div>
          </div>
        </div>

        <!-- 头部图标 -->
        <div class="icon-right">
          <div class="icons">
            <a-popover placement="bottomRight" :overlayStyle="{ width: '400px' }" trigger="click" v-model:open="showSearch">
              <template #content>
                <div>
                  <span class="search-label">回复、提到我的人?：</span>
                  <span style="display: inline-block;">
                    <div v-if="searchParams.fromUser" class="selected-user">
                      <span>{{searchParams.realname}}</span>
                      <span class="clear-user-icon"><close-outlined style="font-size: 12px" @click="clearSearchParamsUser"/></span>
                    </div>
                    <a-button v-else type="dashed" shape="circle" @click="openSelectPerson">
                      <plus-outlined />
                    </a-button>
                    
                  </span>
                </div>
                <div class="search-date">
                  <div class="date-label">时间：</div>
                  <div class="date-tags">
                    <div class="tags-container">
                      <div v-for="item in dateTags" :class="item.active == true ? 'tag active' : 'tag'" @click="handleClickDateTag(item)">{{
                        item.text
                      }}</div>
                    </div>
                    <div class="cust-range-date" v-if="showRangeDate">
                      <a-range-picker v-model:value="searchRangeDate" @change="handleChangeSearchDate" />
                    </div>
                  </div>
                </div>
              </template>
              
              <span v-if="conditionStr" class="anticon filtera">
                <filter-outlined />
                <span style="font-size:12px;margin-left: 3px">{{conditionStr}}</span>
                <span style="display: flex;margin:0 5px;"><close-outlined style="font-size: 12px" @click="clearAll"/></span>
              </span>
              <filter-outlined v-else/>
            </a-popover>
            <close-outlined @click="closeModal" />
          </div>
        </div>
      </div>
    </template>

    <div class="sys-message-card">
      <a-tabs :activeKey="activeKey" center @tabClick="handleChangePanel" animated>
        <template #renderTabBar>
          <div></div>
        </template>

        <a-tab-pane tab="全部消息" key="all" forceRender>
          <sys-message-list ref="allMessageRef" @close="hrefThenClose" @detail="showDetailModal"/>
        </a-tab-pane>

        <!-- 标星 -->
        <a-tab-pane tab="标星消息" key="star" forceRender>
          <sys-message-list ref="starMessageRef" star @close="hrefThenClose" @detail="showDetailModal"/>
        </a-tab-pane>
      </a-tabs>
    </div>
  </BasicModal>

  <user-select-modal isRadioSelection :showButton="false" labelKey="realname" rowKey="username" @register="regModal" @getSelectResult="getSelectedUser"></user-select-modal>

  <DetailModal @register="registerDetail" :zIndex="1001"/>
</template>

<script>
  import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
  import { FilterOutlined, CloseOutlined, BellFilled, ExclamationOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import JSelectUser from '/@/components/Form/src/jeecg/components/JSelectUser.vue';
  import { ref, unref, reactive, computed } from 'vue';
  // import SysMessageList from './SysMessageList.vue'
  import UserSelectModal from '/@/components/Form/src/jeecg/components/modal/UserSelectModal.vue'
  import DetailModal from '/@/views/monitor/mynews/DetailModal.vue';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  
  export default {
    name: 'SysMessageModal',
    components: {
      BasicModal,
      FilterOutlined,
      CloseOutlined,
      BellFilled,
      ExclamationOutlined,
      JSelectUser,
      SysMessageList: createAsyncComponent(() => import('./SysMessageList.vue')),
      UserSelectModal,
      PlusOutlined,
      DetailModal
    },
    emits:['register', 'refresh'],
    setup(_p, {emit}) {
      const allMessageRef = ref();
      const starMessageRef = ref();
      const activeKey = ref('all');
      function handleChangeTab(e, key) {
        activeKey.value = key;
        loadData();
      }
      function handleChangePanel(key) {
        activeKey.value = key;
      }
      
      // 查询区域存储值
      const searchParams = reactive({
        fromUser: '',
        realname: '',
        rangeDateKey: '7day',
        rangeDate: [],
      });

      function loadData(){
        let params = {
          fromUser: searchParams.fromUser,
          rangeDateKey: searchParams.rangeDateKey,
          rangeDate: searchParams.rangeDate,
        }
        if(activeKey.value == 'all'){
          getRefPromise(allMessageRef).then(() => {
            allMessageRef.value.reload(params);
          });
        }else{
          starMessageRef.value.reload(params);
        }
      }
      
      //useModalInner
      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        //每次弹窗打开 加载最新的数据
        loadData();
      });
      
      const showSearch = ref(false);
      
      function handleChangeSearchPerson(value, a) {
        console.log('选择改变', value, a);
        showSearch.value = true;
      }

      const dateTags = reactive([
        { key: 'jt', text: '今天', active: false },
        { key: 'zt', text: '昨天', active: false },
        { key: 'qt', text: '前天', active: false },
        { key: 'bz', text: '本周', active: false },
        { key: 'sz', text: '上周', active: false },
        { key: 'by', text: '本月', active: false },
        { key: 'sy', text: '上月', active: false },
        { key: '7day', text: '七日', active: true },
        { key: 'zdy', text: '自定义', active: false },
      ]);
      function handleClickDateTag(item) {
        for (let a of dateTags) {
          if(a.key != item.key){
            a.active = false;
          }
        }
        item.active = !item.active;
        if(item.active == false){
          searchParams.rangeDateKey = ''
        }else{
          searchParams.rangeDateKey = item.key;
        }
        if (item.key == 'zdy') {
          // 自定义日期查询走的是 handleChangeSearchDate
          if(item.active == false){
            searchParams.rangeDate = []
            loadData();
          }
        }else{
          loadData();
        }
      }
      const showRangeDate = computed(() => {
        let temp = dateTags.filter((i) => i.active == true);
        if (temp && temp.length > 0) {
          if (temp[0].text == '自定义') {
            return true;
          }
        }
        return false;
      });
      const searchRangeDate = ref([]);
      function handleChangeSearchDate(_value, dateStringArray) {
        searchParams.rangeDate = [...dateStringArray]
        loadData();
      }
      
      function hrefThenClose(id){
        emit('refresh', id)
       // closeModal();
      }
      
      // 有查询条件值的时候显示该字符串
      const conditionStr = computed(()=>{
        const { fromUser, rangeDateKey, realname } = searchParams;
        if(!fromUser && !rangeDateKey){
          return ''
        }
        let arr = [];
        if(fromUser){
          arr.push(realname)
        }
        if(rangeDateKey){
          let rangDates = dateTags.filter(item=>item.key == rangeDateKey);
          if(rangDates && rangDates.length>0){
            arr.push(rangDates[0].text)
          }
        }
        return arr.join('、')
      });

      //注册model
      const [regModal, { openModal }] = useModal();

      function getSelectedUser(options, value){
        if(options && options.length>0){
          searchParams.fromUser = value
          searchParams.realname = options[0].label;
        }
      }
      
      function openSelectPerson(){
        openModal(true, {})
      }
      
      function clearSearchParamsUser(){
        searchParams.fromUser = ''
        searchParams.realname = ''
      }

      function getRefPromise(componentRef) {
        return new Promise((resolve) => {
          (function next() {
            let ref = componentRef.value;
            if (ref) {
              resolve(ref);
            } else {
              setTimeout(() => {
                next();
              }, 100);
            }
          })();
        });
      }
      
      function clearAll(){
        searchParams.fromUser='';
        searchParams.realname='';
        searchParams.rangeDateKey='';
        searchParams.rangeDate=[];
        for (let a of dateTags) {
          a.active = false;
        }
        loadData();
      }

      const [registerDetail, { openModal: openDetailModal }] = useModal();
      function showDetailModal(record){
        openDetailModal(true, {record: unref(record), isUpdate: true})
      }
      return {
        conditionStr,
        regModal,
        getSelectedUser,
        openSelectPerson,
        clearSearchParamsUser,
        clearAll,
        
        registerModal,
        activeKey,
        handleChangePanel,
        handleChangeTab,

        showSearch,
        searchParams,
        handleChangeSearchPerson,
        dateTags,
        handleClickDateTag,
        showRangeDate,
        searchRangeDate,
        handleChangeSearchDate,
        closeModal,
        hrefThenClose,

        allMessageRef,
        starMessageRef,
        registerDetail,
        showDetailModal
     
      };
    },
  };
</script>

<style lang="less">

  @keyframes move22{
    0%{ transform:translateY(0px); }
    100%{transform:translateY(600px);}
  }

  
  .sys-msg-modal {
    .ant-modal-header {
      padding-bottom: 0;
      padding-top: 6px;
      padding-right: 12px;
    }
    .ant-modal-body {
      height: 550px;
      overflow-y: auto;
    }
    .ant-modal {
      position: absolute;
      right: 10px;
      top: calc(100% - 600px);
/*      animation-name: move22;
      animation-duration:0.8s;
      animation-timing-function:ease;*/
    }
  }
  .sys-msg-modal-title {
    .title {
      display: inline-block;
      width: 120px;
    }
    .icon-right {
      display: inline-block;
      width: 220px;
      vertical-align: top;
      
      .icons {
        display: flex;
        height: 100%;
        flex-direction: row;
        justify-content: flex-end;
  
        > span.anticon {
          padding: 10px;
          display: inline-block;
          cursor: pointer;
        }

        > span.filtera{
          //background-color: #d3eafd;
          background-color: #eff1f2;
          border-radius: 4px;
          cursor: pointer;
          height: 27px;
          padding-top: 7px;
          margin-top: 3px;
          line-height: 25px;
          //color: #2196f3;
          display: flex;
          
          >span.anticon{
            height: auto;
            line-height: 9px;
            display: inline-block;
          }
        }

    
      }
    }
    .ant-tabs-nav-wrap {
      display: inline-block;
      width: calc(100% - 340px);
      .ant-tabs-tab {
        position: relative;
        display: inline-flex;
        align-items: center;
        padding: 12px 0;
        font-size: 14px;
        background: transparent;
        border: 0;
        outline: none;
        cursor: pointer;
      }
      .ant-tabs-tab {
        position: relative;
        display: inline-flex;
        align-items: center;
        padding: 12px 0;
        font-size: 14px;
        background: transparent;
        border: 0;
        outline: none;
        cursor: pointer;
      }
      .ant-tabs-tab+.ant-tabs-tab {
         margin: 0 0 0 32px;
      }
      .ant-tabs-ink-bar {
        background: @primary-color;
      }
    }
    .ant-tabs-nav-scroll {
      text-align: center;
      font-size: 14px;
      font-weight: normal;
    }
  }

  .sys-message-card {
  }

  .search-label {
    font-weight: 500;
    font-size: 14px !important;
    color: #757575 !important;
    display: inline-block;
    margin-right: 15px !important;
  }
  .search-date {
    display: flex;
    min-width: 0;
    margin-top: 15px;
    .date-label {
      margin-top: 4px;
      font-weight: 500;
      font-size: 14px !important;
      color: #757575 !important;
      margin-right: 15px !important;
    }

    .date-tags {
      display: -ms-flexbox;
      display: flex;
      display: -webkit-flex;
      -ms-flex-direction: column;
      -webkit-flex-direction: column;
      flex-direction: column;
      -webkit-flex: 1;
      flex: 1;
      -ms-flex: 1;
      .tags-container {
        display: flex;
        min-width: 0;
        -webkit-flex: 1;
        flex: 1;
        -ms-flex: 1;
        flex-wrap: wrap;
        .tag {
          background-color: #f5f5f5;
          border-radius: 17px;
          font-size: 13px;
          margin-bottom: 10px;
          margin-right: 10px;
          padding: 6px 12px;
          cursor: pointer;
          &.active {
            background-color: #d3eafd !important;
          }
        }
      }
    }
  }

  .selected-user{
    background: #f5f5f5;
    padding: 2px 14px;
    border-radius: 30px;
    .clear-user-icon{
      margin-left: 5px;
    }
  }
</style>
