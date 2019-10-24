<template>
  <a-drawer
    :title="title"
    :width="screenWidth"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">

    <a-spin :spinning="confirmLoading">

      <div
        :style="{
          padding:'10px',
          border: '1px solid #e9e9e9',
          background: '#fff',
        }">


        <div>
          <a-table
            ref="table"
            size="middle"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
            @change="handleTableChange">



            <template slot="businessType" slot-scope="text">
              <a-tag v-if="text === '进口'" color="green">{{ text }}</a-tag>
              <a-tag v-else-if="text === '出口'" color="cyan">{{ text }}</a-tag>
            </template>

            <template slot="takeBack" slot-scope="text">
              <a-tag v-if="text === '是'" color="#A52A2A">{{ text }}</a-tag>
              <a-tag v-else-if="text === '否'" color="#87d068">{{ text }}</a-tag>
              <span v-else>{{ text }} ..</span>
            </template>

          </a-table>
        </div>
      </div>
    </a-spin>
    <a-button type="primary" @click="handleCancel">关闭</a-button>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'

  export default {
    name: "conList",
    mixins: [JeecgListMixin],


    data () {
      return {
        title:"集装箱货物信息",
        //查询条件
        queryParam: {
          bolcodeQuery: '',
          customsReleaseDate: [],
          containerNoQuery: '',
        },
        isorter: {
          column: 'customsReleaseDate',
          order: 'desc',
        },
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 10,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '提单号',
            align: "center",
            dataIndex: 'blNo',
            // customRender: (value, row, index) => {
            //   const obj = {
            //     children: value,
            //     attrs: {},
            //   };
            //   debugger
            //   obj.attrs.rowSpan = this.spanArr[index]
            //   obj.attrs.colspan = obj.attrs.rowSpan > 0 ? 1 : 0;
            //   row.containerNums = obj.attrs.rowSpan;
            //   // obj.children = value +  " | 箱数:" + obj.attrs.rowSpan;
            //   obj.children = value +  " | 箱数:" + obj.attrs.rowSpan;
            //   return obj;
            //
            //
            // },
            // scopedSlots: {customRender: 'blNo'},
          },
          {
            title: '海关放行日期',
            align: "center",
            sorter: true,
            dataIndex: 'customsReleaseDate'
          },
          {
            title: '箱号（柜号）',
            align: "center",
            dataIndex: 'containerNo'
          },
          {
            title: '进出口',
            align: "center",
            dataIndex: 'businessType_dictText',
            filters: [
              {text: '进口', value: '1'},
              {text: '出口', value: '2'},
            ],
            filterMultiple: false,
            // customRender: (text) => {
            //   //字典值替换通用方法
            //   return filterDictText(this.businessTypeDictOptions, text);
            // },
            scopedSlots: {customRender: 'businessType'},
            // onFilter: (value, record) => record.businessType + '' === value,
          },
          {
            title: '尺寸',
            align: "center",
            dataIndex: 'containerSize'
          },
          {
            title: '规格',
            align: "center",
            dataIndex: 'containerSpec'
          },
          {
            title: '退载自拖',
            align: "center",
            dataIndex: 'takeBack_dictText',
            width:80,
            // filters: [
            //   {text: '退载', value: '1'}
            // ],
            // filterMultiple: false,
            scopedSlots: {customRender: 'takeBack'},
          },
          {
            title: '船东',
            align: "center",
            dataIndex: 'shipowner'
          },
          {
            title: '提箱地点',
            align: "center",
            dataIndex: 'takeContainerAddress'
          },
          {
            title: '拖箱地点',
            align: "center",
            dataIndex: 'dragContainerAddress'
          },
          {
            title: '返箱地点',
            align: "center",
            dataIndex: 'returnContainerAddress'
          },
          {
            title: '详细地址',
            align: "center",
            dataIndex: 'detailAddress'
          },
          {
            title: '接单备注',
            align: "center",
            dataIndex: 'orderRemark'
          }
        ],
        visible: false,
        model: {},
        screenWidth: '1800',

        confirmLoading: false,
        validatorRules:{
        },
        url: {
          list: "/dataReport/containerTatistics/list",
        },
      }
    },
    created () {
      // 当页面初始化时,根据屏幕大小来给抽屉设置宽度
      this.resetScreenSize();
    },
    methods: {
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      searchChild(record) {
        debugger
        this.queryParam = {}
        this.isorter = {}
        this.visible = true;
        if (record.blNo) {
          this.queryParam.bolcodeQuery = record.blNo
          this.loadData();
        }
      },
      // getQueryParams() {
      //   debugger
      //   var param = Object.assign({}, this.queryParam, this.isorter,this.filters);
      //   param.dictId = this.dictId;
      //   param.field = this.getQueryField();
      //   param.pageNo = this.ipagination.current;
      //   param.pageSize = this.ipagination.pageSize;
      //   return filterObj(param);
      // },
      resetScreenSize() {
        debugger
        let screenWidth = document.body.clientWidth;
        if (screenWidth < 600) {
          this.screenWidth = screenWidth;
        } else {
          this.screenWidth = screenWidth * 0.85;
          // this.screenWidth = 600;
        }
      },


    }
  }
</script>

<style lang="less" scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>