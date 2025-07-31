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

                <div class="search-date">
                  <div class="date-label">类型：</div>
                  <div class="date-tags">
                    <div class="tags-container">
                      <div v-for="item in noticeTypeOption" :class="item.active == true ? 'tag active flex' : 'tag flex'" @click="handleClickNoticeType(item)">
                        <img class="notice-type-img" v-if="item.img" :src="item.img" />
                        {{ item.text }}
                      </div>
                    </div>
                  </div>
                </div>
              </template>

              <span v-if="conditionStr" class="anticon filtera">
                <img v-if="noticeImg" :src="noticeImg" class="notice-type-header-img">
                <filter-outlined v-else/>
                <span style="font-size:12px;margin-left: 3px;position: relative;">{{conditionStr}}</span>
                <span style="display: flex;margin:0 5px;">
                  <svg @click="clearAll" t="1715689724802" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="10694" width="14" height="14"><path d="M624.593455 23.272727a93.090909 93.090909 0 0 1 93.090909 93.090909v168.587637l143.406545 0.023272a116.363636 116.363636 0 0 1 116.247273 111.313455l0.116363 5.050182V861.090909a116.363636 116.363636 0 0 1-116.363636 116.363636H162.909091a116.363636 116.363636 0 0 1-116.363636-116.363636V401.338182a116.363636 116.363636 0 0 1 116.363636-116.363637l146.664727-0.023272V116.363636a93.090909 93.090909 0 0 1 88.459637-92.974545l4.654545-0.116364zM139.636364 581.818182v279.272727a23.272727 23.272727 0 0 0 23.272727 23.272727h302.545454v-162.909091a46.545455 46.545455 0 1 1 93.09091 0v162.909091h93.090909v-162.909091a46.545455 46.545455 0 1 1 93.090909 0v162.909091h116.363636a23.272727 23.272727 0 0 0 23.272727-23.272727V581.818182H139.636364z m0-93.090909h744.727272v-87.389091a23.272727 23.272727 0 0 0-23.272727-23.272727h-166.679273a69.818182 69.818182 0 0 1-69.818181-69.818182V116.363636h-221.905455v191.883637a69.818182 69.818182 0 0 1-69.818182 69.818182H162.909091a23.272727 23.272727 0 0 0-23.272727 23.272727V488.727273z" fill="currentColor" p-id="10695"></path></svg>
                </span>
              </span>
              <div v-else style="align-content: center;">
                 <img v-if="noticeImg" :src="noticeImg" class="notice-type-header-img" style="position: relative;top: -2px">
                 <filter-outlined v-else />
              </div>
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
          <sys-message-list :isLowApp="isLowApp" ref="allMessageRef" @close="hrefThenClose" @detail="showDetailModal" @clear="clearAll" :messageCount="messageCount" @closeModal="closeModal"/>
        </a-tab-pane>

        <!-- 标星 -->
        <a-tab-pane tab="标星消息" key="star" forceRender>
          <sys-message-list :isLowApp="isLowApp" ref="starMessageRef" star @close="hrefThenClose" @detail="showDetailModal" @clear="clearAll" :messageCount="messageCount" @closeModal="closeModal" :cancelStarAfterDel="true"/>
        </a-tab-pane>
      </a-tabs>
    </div>
  </BasicModal>

  <user-select-modal isRadioSelection :showButton="false" labelKey="realname" rowKey="username" @register="regModal" @getSelectResult="getSelectedUser"></user-select-modal>

  <DetailModal @register="registerDetail" :zIndex="1001" @close="handleDetailColse"/>
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
  import calendar from '/@/assets/icons/calendarNotice.png';
  import folder from '/@/assets/icons/folderNotice.png';
  import system from '/@/assets/icons/systemNotice.png';
  import flow from '/@/assets/icons/flowNotice.png';
  export default {
    name: 'SysMessageModal',
    components: {
      BasicModal,
      FilterOutlined,
      CloseOutlined,
      BellFilled,
      ExclamationOutlined,
      JSelectUser,
      // update-begin--author:liaozhiyang---date:20240308---for：【QQYUN-8241】emoji-mart-vue-fast库异步加载
      SysMessageList: createAsyncComponent(() => import('./SysMessageList.vue')),
      // SysMessageList,
      // update-end--author:liaozhiyang---date:20240308---for：【QQYUN-8241】emoji-mart-vue-fast库异步加载
      UserSelectModal,
      PlusOutlined,
      DetailModal
    },
    props:{
      messageCount: {
        type: Number,
        default: 0
      }
    },
    emits:['register', 'refresh'],
    setup(_p, {emit}) {
      const allMessageRef = ref();
      const starMessageRef = ref();
      const activeKey = ref('all');
      //通知类型
      const noticeType = ref('');
      //通知类型数组
      const noticeTypeOption = reactive([
        { key: 'system', text: '系统通知', active: false, img: system },
        { key: 'flow', text: '流程通知', active: false, img: flow },
        { key: 'plan', text: '日程通知', active: false, img: calendar },
        { key: 'file', text: '知识通知', active: false, img: folder },
      ]);
      const noticeImg = ref('');
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
          noticeType: noticeType.value ? noticeType.value:''
        }
        if(activeKey.value == 'all'){
          getRefPromise(allMessageRef).then(() => {
            allMessageRef.value.reload(params);
          });
        }else{
          starMessageRef.value.reload(params);
        }
      }

      const isLowApp = ref(false);
      //useModalInner
      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        showSearch.value = false
        if(data.noticeType){
          noticeType.value = data.noticeType;
          //update-begin---author:wangshuai---date:2025-07-01---for:【QQYUN-12998】点击完聊天的系统图标，再点击系统上面的铃铛就不出数据了---
          for (const item of noticeTypeOption) {
            if(item.key === data.noticeType){
              item.active = true;
              noticeImg.value = item.img;
            }else{
              item.active = false;
            }
          }
          //update-end---author:wangshuai---date:2025-07-01---for:【QQYUN-12998】点击完聊天的系统图标，再点击系统上面的铃铛就不出数据了---
          delete data.noticeType;
        }
        //每次弹窗打开 加载最新的数据
        loadData();
        if(data){
          isLowApp.value = data.isLowApp||false
        }else{
          isLowApp.value = false;
        }
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

      function clearAll(){
        searchParams.fromUser='';
        searchParams.realname='';
        searchParams.rangeDateKey='';
        searchParams.rangeDate=[];
        for (let a of dateTags) {
          a.active = false;
        }
        noticeType.value = "";
        noticeImg.value = "";
        for (const item of noticeTypeOption) {
          item.active = false;
        }
        loadData();
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
      const [registerDetail, { openModal: openDetailModal }] = useModal();
      function showDetailModal(record){
        openDetailModal(true, {record: unref(record), isUpdate: true})
      }

      function handleDetailColse(){
        loadData();
      }

      /**
       * 只是通知
       * @param item
       */
      function handleClickNoticeType(item) {
        for (let a of noticeTypeOption) {
          if(a.key !== item.key){
            a.active = false;
          }
        }
        item.active = !item.active;
        if(item.active === false){
          noticeType.value = "";
          noticeImg.value = "";
        }else{
          noticeType.value = item.key;
          noticeImg.value = item.img;
        }
        loadData();
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
        showDetailModal,
        isLowApp,
        handleDetailColse,
        noticeTypeOption,
        handleClickNoticeType,
        noticeImg,
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
<style lang="less" scoped>
  .notice-type-img{
      width: 16px;
      height: 16px;
      position: relative;
      top: 2px;
      margin-right: 1px;
  }
  .notice-type-header-img{
    width: 16px;
    height: 16px;
    position: relative;
    left: 2px;
    top: 1px;
  }
  .flex{
    display: flex;
    flex: unset;
  }
</style>
