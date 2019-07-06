<template>
  <a-card :bordered="false">
    <!-- table区域-begin -->
    <a-table
      ref="table"
      size="default"
      bordered
      rowKey="id"
      :columns="columns"
      :pagination="false"
      :dataSource="dataSource">

    </a-table>
    <!-- table区域-end -->


  </a-card>
</template>

<script>

  export default {
    name: "RowspanTable",
    components: {
    },
    data() {
      return {
        description: '存放位置设置表管理页面',
        levelNum:{},
        gridNum:0,
        boxNum:0,
        cabinetNo:"",
        // 表头
        columns: [ {
          title: '分组一',
          align: "center",
          dataIndex: 'cabinetNo',
          customRender: (value, row, index) => {
            const obj = {
              children: value,
              attrs: {},
            };
            if(index===0){
              obj.attrs.rowSpan = this.dataSource.length;
            }else{
              obj.attrs.rowSpan = 0;
            }
            return obj;
          },
        },
          {
            title: '分组二',
            align: "center",
            dataIndex: 'levelNo',
            customRender: (value, row, index) => {
              const obj = {
                children: value,
                attrs: {},
              };
              //当前列跨行的条数
              var a = parseInt(this.levelNum);
              var b = parseInt(this.gridNum)*parseInt(this.boxNum);
              console.log(a);
              for(var c=0;c<=a;c++){
                if(index === (c*b)){
                  console.log(1);
                  console.log(c*b);
                  obj.attrs.rowSpan = b;
                  break;
                }else{
                  obj.attrs.rowSpan = 0;
                }
              }
              return obj;
            }
          },
          {
            title: '分组三',
            align: "center",
            dataIndex: 'gridNo',
            customRender: (value, row, index) => {
              const obj = {
                children: value,
                attrs: {},
              };
              var a = parseInt(this.levelNum)*parseInt(this.gridNum);
              var b = parseInt(this.boxNum);
              for(var c=0;c<=a;c++){
                if(index === (c*b)){
                  obj.attrs.rowSpan = b;
                  break;
                }else{
                  obj.attrs.rowSpan = 0;
                }
              }
              return obj;
            },
          }, {
            title: '字段一',
            align: "center",
            dataIndex: 'boxNo'
          }, {
            title: '字段二',
            align: 'center',
            dataIndex: 'storedNum'
          }, {
            title: '字段三',
            align: "center",
            dataIndex: 'maxNum'
          },],
        //数据集
        dataSource: [{
          "id": "cb1dfd12cbeca3f8ba121439ee7e2411",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "1",
          "gridNo": "1",
          "boxNo": "1",
          "storedNum": 2,
          "maxNum": 2,
          "unitNum": 2,
          "assignStatus": "1",
          "storageStatus": "1",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-02"
        }, {
          "id": "f903d50d02904b14175dccf2a7948777",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "1",
          "gridNo": "1",
          "boxNo": "2",
          "storedNum": 2,
          "maxNum": 2,
          "unitNum": 2,
          "assignStatus": "1",
          "storageStatus": "1",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-02"
        }, {
          "id": "4f04c0ca4202535d678871b07e706cf6",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "1",
          "gridNo": "2",
          "boxNo": "1",
          "storedNum": 2,
          "maxNum": 2,
          "unitNum": 2,
          "assignStatus": "1",
          "storageStatus": "1",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-02"
        }, {
          "id": "d0c91dabedfc03efad0126e50ea72e80",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "1",
          "gridNo": "2",
          "boxNo": "2",
          "storedNum": 2,
          "maxNum": 2,
          "unitNum": 2,
          "assignStatus": "1",
          "storageStatus": "1",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-08"
        }, {
          "id": "1e8bfcbe4352afbab8878f9fd368e007",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "2",
          "gridNo": "1",
          "boxNo": "1",
          "storedNum": 1,
          "maxNum": 2,
          "unitNum": 1,
          "assignStatus": "1",
          "storageStatus": "0",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-08"
        }, {
          "id": "d76087d8d3ebc7a59d43458588f26941",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "2",
          "gridNo": "1",
          "boxNo": "2",
          "storedNum": 0,
          "maxNum": 2,
          "unitNum": 0,
          "assignStatus": "1",
          "storageStatus": "0",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-02"
        }, {
          "id": "7bf7754f12e1bf95edcd501cc6b85e62",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "2",
          "gridNo": "2",
          "boxNo": "1",
          "storedNum": 0,
          "maxNum": 2,
          "unitNum": 0,
          "assignStatus": "1",
          "storageStatus": "0",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-02"
        }, {
          "id": "9cd08d733657d5b286bec870f12f6ecf",
          "attributeId": "e62831f314e1390edbd884e9d9e6aca6",
          "cabinetNo": "1",
          "levelNo": "2",
          "gridNo": "2",
          "boxNo": "2",
          "storedNum": 0,
          "maxNum": 2,
          "unitNum": 0,
          "assignStatus": "1",
          "storageStatus": "0",
          "remark": null,
          "createBy": "admin",
          "createTime": "2019-04-02",
          "updateBy": "admin",
          "updateTime": "2019-04-02"
        }],
        isorter: {
          column: 'createTime',
          order: 'desc',
        },
        url: {
        },
      }
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData(){
        this.levelNum=4;
        this.gridNum = 2;
        this.boxNum = 2;

      }
    }
  }
</script>
<style scoped>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>

