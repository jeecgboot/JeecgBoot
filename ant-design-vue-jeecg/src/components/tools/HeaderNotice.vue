<template>
  <a-popover
    trigger="click"
    placement="bottomRight"
    :autoAdjustOverflow="true"
    :arrowPointAtCenter="true"
    overlayClassName="header-notice-wrapper"
    @visibleChange="handleHoverChange"
    :overlayStyle="{ width: '300px', top: '50px' }">
    <template slot="content">
      <a-spin :spinning="loadding">
        <a-tabs>
          <a-tab-pane :tab="msg1Title" key="1">
            <!--<a-list>
              <a-list-item>
                <a-list-item-meta title="你收到了 14 份新周报" description="一年前">
                  <a-avatar style="background-color: white" slot="avatar" src="https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png"/>
                </a-list-item-meta>
              </a-list-item>
              <a-list-item>
                <a-list-item-meta title="你推荐的 IT大牛 已通过第三轮面试" description="一年前">
                  <a-avatar style="background-color: white" slot="avatar" src="https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png"/>
                </a-list-item-meta>
              </a-list-item>
              <a-list-item>
                <a-list-item-meta title="这种模板可以区分多种通知类型" description="一年前">
                  <a-avatar style="background-color: white" slot="avatar" src="https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png"/>
                </a-list-item-meta>
              </a-list-item>
            </a-list>-->
            <a-list>
              <a-list-item :key="index" v-for="(record, index) in announcement1">
                <div style="margin-left: 5%;width: 80%">
                  <p><a @click="showAnnouncement(record)">标题：{{ record.titile }}</a></p>
                  <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
                </div>
                <div style="text-align: right">
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
                </div>
              </a-list-item>
              <div style="margin-top: 5px;text-align: center">
                <a-button @click="toMyAnnouncement()" type="dashed" block>查看更多</a-button>
              </div>
            </a-list>
          </a-tab-pane>
          <a-tab-pane :tab="msg2Title" key="2">
            <a-list>
              <a-list-item :key="index" v-for="(record, index) in announcement2">
                <div style="margin-left: 5%;width: 80%">
                  <p><a @click="showAnnouncement(record)">标题：{{ record.titile }}</a></p>
                  <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
                </div>
                <div style="text-align: right">
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
                </div>
              </a-list-item>
              <div style="margin-top: 5px;text-align: center">
                <a-button @click="toMyAnnouncement()" type="dashed" block>查看更多</a-button>
              </div>
            </a-list>
          </a-tab-pane>
        </a-tabs>
      </a-spin>
    </template>
    <span @click="fetchNotice" class="header-notice">
      <a-badge :count="msgTotal">
        <a-icon style="font-size: 16px; padding: 4px" type="bell" />
      </a-badge>
    </span>
    <show-announcement ref="ShowAnnouncement" @ok="modalFormOk"></show-announcement>
  </a-popover>
</template>

<script>
  import { getAction,putAction } from '@/api/manage'
  import ShowAnnouncement from './ShowAnnouncement'

  export default {
    name: "HeaderNotice",
    components: {
      ShowAnnouncement,
    },
    data () {
      return {
        loadding: false,
        url:{
          listCementByUser:"/sys/annountCement/listByUser",
          editCementSend:"/system/sysAnnouncementSend/editByAnntIdAndUserId",
        },
        hovered: false,
        announcement1:[],
        announcement2:[],
        msg1Count:"3",
        msg2Count:"0",
        msg1Title:"通知(3)",
        msg2Title:"",
      }
    },
    computed:{
      msgTotal () {
        return parseInt(this.msg1Count)+parseInt(this.msg2Count);
      }
    },
    created() {
      this.loadData();
      this.timer();
    },
    methods: {
      timer() {
        return setInterval(()=>{
          this.loadData()
        },60000)
      },
      loadData (){
        try {
          // 获取系统消息
          getAction(this.url.listCementByUser).then((res) => {
            if (res.success) {
              this.announcement1 = res.result.anntMsgList;
              this.msg1Count = res.result.anntMsgTotal;
              this.msg1Title = "通知(" + res.result.anntMsgTotal + ")";
              this.announcement2 = res.result.sysMsgList;
              this.msg2Count = res.result.sysMsgTotal;
              this.msg2Title = "系统消息(" + res.result.sysMsgTotal + ")";
            }
          });
        } catch (err) {
        }
      },
      fetchNotice () {
        if (this.loadding) {
          this.loadding = false
          return
        }
        this.loadding = true
        setTimeout(() => {
          this.loadding = false
        }, 200)
      },
      showAnnouncement(record){
        putAction(this.url.editCementSend,{anntId:record.id}).then((res)=>{
          if(res.success){
            this.loadData();
          }
        });
        this.hovered = false;
        this.$refs.ShowAnnouncement.detail(record);
      },
      toMyAnnouncement(){

        this.$router.push({
          path: '/isps/userAnnouncement',
          name: 'isps-userAnnouncement'
        });
      },
      modalFormOk(){
      },
      handleHoverChange (visible) {
        this.hovered = visible;
      },

    }
  }
</script>

<style lang="css">
  .header-notice-wrapper {
    top: 50px !important;
  }
</style>
<style lang="scss" scoped>
  .header-notice{
    display: inline-block;
    transition: all 0.3s;

    span {
      vertical-align: initial;
    }
  }
</style>