<template>
    <div class="data-log-scroll" :style="{'height': height+'px'}">
        <div class="data-log-content">
            <div class="logbox">
                
                <div class="log-item" v-for="(item, index) in dataList">
                    <span class="log-item-icon">
                        <plus-outlined v-if="lastIndex == index" style="margin-top:3px"/>
                        <edit-outlined v-else/>
                    </span>
                    <span class="log-item-content">
                        <a @click="handleClickPerson">@{{item.createName || item.createBy}}</a>
                        {{ item.dataContent }}
                    </span>
                    <div class="log-item-date">
                        <Tooltip :title="item.createTime">
                            <span>{{ getDateDiff(item) }}</span>
                        </Tooltip>
                    </div>
                </div>

                
            </div>
        </div>
    </div>
</template>

<script>
  import { PlusOutlined, EditOutlined } from '@ant-design/icons-vue';
  import { getModalHeight, getLogList } from './useComment'
  import {inject, ref, watchEffect} from 'vue'
  import { propTypes } from '/@/utils/propTypes';
  import { Tooltip } from 'ant-design-vue';
  // import dayjs from 'dayjs';
  // import relativeTime from 'dayjs/plugin/relativeTime';
  // import customParseFormat from 'dayjs/plugin/customParseFormat';
  // dayjs.locale('zh');
  // dayjs.extend(relativeTime);
  // dayjs.extend(customParseFormat);
  
  export default {
    name: "DataLogList",
    components:{
      PlusOutlined,
      EditOutlined,
      Tooltip
    },
    props: {
      tableName: propTypes.string.def(''),
      dataId: propTypes.string.def(''),
      datetime:  propTypes.number.def(1),
    },
    setup(props){
      const dayjs = inject('$dayjs')
      const winHeight = getModalHeight();
      const height = ref(300);
      height.value = winHeight - 46 - 57 -53 - 30;

      const dataList = ref([]);
      const lastIndex = ref(0);
      /**
       * 加载数据
       * @returns {Promise<void>}
       */
      async function loadData() {
        const params = {
          dataTable: props.tableName,
          dataId: props.dataId,
          type: 'comment'
        };
        const res = await getLogList(params);
        if (!res || !res.result || res.result.length == 0) {
          dataList.value = [];
          lastIndex.value = -1;
        } else {
          let arr = res.result;
          lastIndex.value = arr.length-1;
          console.log('log-list', arr);
          dataList.value = arr;
        }
      }
      
      watchEffect(() => {
        if(props.datetime){
          if (props.tableName && props.dataId) {
            console.log(props.tableName, props.dataId)
            loadData();
          }
        }
      });
      
      

      function getDateDiff(item) {
        if (item.createTime) {
          const temp = dayjs(item.createTime, 'YYYY-MM-DD hh:mm:ss');
          return temp.fromNow();
        }
        return '';
      }
      
      function handleClickPerson() {
        console.log('此功能未开放')
      }
      
      return {
        height,
        lastIndex,
        dataList,
        getDateDiff,
        handleClickPerson
      }
    }
  }
</script>

<style lang="less" scoped>
.data-log-scroll{
    box-sizing: border-box;
    height: 100%;
    padding-bottom: 16px;
    width: 100%;
    overflow: hidden;
    position: relative;
    overflow-y: auto;
    .data-log-content{
 /*       right: -10px;
        bottom: 0;
        left: 0;
        overflow: scroll;
        overflow-x: hidden;
        position: absolute;
        top: 0;*/
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        .logbox{
            box-sizing: border-box;
            padding-left: 16px;
            .log-item{
                box-sizing: border-box;
                color: #9e9e9e;
                margin-bottom: 20px;
                padding-left: 20px;
                padding-right: 25px;
                position: relative;
                .log-item-icon{
                    left: 0;
                    line-height: 16px;
                    position: absolute;
                    top: 3px;
                    vertical-align: middle;
                }
                .log-item-content{
                    word-wrap: break-word;
                    display: inline-block;
                    font-size: 13px;
                    vertical-align: middle;
                    width: 100%;
                    word-break: break-word;
                    box-sizing: border-box;
                }
                .log-item-date{
                    word-wrap: break-word;
                    display: inline-block;
                    font-size: 13px;
                    vertical-align: middle;
                    width: 100%;
                    word-break: break-word;
                    box-sizing: border-box;
                    margin-top: 5px;
                }
            }
        }
    }

}
</style>
