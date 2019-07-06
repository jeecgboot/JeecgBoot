<template>
  <a-modal
    :width="modalWidth"
    :visible="visible"
    :footer="null"
    @cancel="handleCancel"
    cancelText="关闭">
    <!--table区 -->
    <div class="marginCss">
      <a-table
        ref="table"
        size="small"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :rowClassName="setdataCss"
        :loading="loading"
        :scroll="{ y: 700 }"
        :pagination="false">
        <span slot="dataVersionTitle1"><a-icon type="smile-o" /> 版本:{{dataVersion1Num}}</span>
        <span slot="dataVersionTitle2"><a-icon type="smile-o" /> 版本:{{dataVersion2Num}}</span>
        <template slot="avatarslot" slot-scope="text, record">
          <div class="anty-img-wrap">
            <img :src="getAvatarView(record)"/>
          </div>

        </template>
      </a-table>
    </div>
  </a-modal>
</template>

<script>
  import {getAction} from '@/api/manage'

  export default {
    name: 'DataLogCompareModal',
    data() {
      return {
        modalWidth: 1000,
        modaltoggleFlag: true,
        confirmDirty: false,
        title: '操作',
        visible: false,
        model: {},
        confirmLoading: false,
        headers: {},
        //版本号
        dataVersion1Num:'',
        dataVersion2Num:'',
        //表头
        columns: [
          {
            title: '字段名',
            align: 'left',
            dataIndex: 'code',
            width: '30%',
          }, {
            align: 'left',
            dataIndex: 'dataVersion1',
            width: '30%',
            slots: { title: 'dataVersionTitle1' },
          }, {
            title: '',
            dataIndex: 'imgshow',
            align: 'center',
            scopedSlots: {customRender: "avatarslot"},
            width: '10%',
          }, {
            align: 'left',
            dataIndex: 'dataVersion2',
            width: '30%',
            slots: { title: 'dataVersionTitle2' },
          }
        ],
        //数据集
        dataSource: [],
        loading: false,
        url: {
          queryCompareUrl: "/sys/dataLog/queryCompareList",
        },
      }
    },
    created() {

    },
    methods: {
      loadData(dataId1, dataId2) {
        this.dataSource = [];
        let that = this;
        getAction(that.url.queryCompareUrl, {dataId1: dataId1, dataId2: dataId2}).then((res) => {
          if (res.success) {
            that.dataVersion1Num = res.result[0].dataVersion;
            that.dataVersion2Num = res.result[1].dataVersion;
            let json1 = JSON.parse(res.result[0].dataContent);
            let json2 = JSON.parse(res.result[1].dataContent);
            for (var item1 in json1) {
              for (var item2 in json2) {
                if (item1 == item2) {
                  this.dataSource.push({
                    code: item1,
                    imgshow: '',
                    dataVersion1: json1[item1],
                    dataVersion2: json2[item2],
                  })
                }
              }
            }
          } else {
            console.log(res.message);
          }
        })
      },
      compareModal(dataId1, dataId2) {
        this.visible = true
        this.loadData(dataId1, dataId2);
      },
      handleCancel() {
        this.close()
      },
      modalFormOk() {
      },
      close() {
        this.$emit('close');
        this.visible = false;
        this.disableSubmit = false;
      },
      setdataCss(record) {
        let className = 'trcolor';
        const dataVersion1 = record.dataVersion1;
        const dataVersion2 = record.dataVersion2;
        if (dataVersion1 != dataVersion2) {
          return className;
        }
      },
      getAvatarView: function (avatar) {
        if (avatar.dataVersion1 != avatar.dataVersion2) {
          return "/goright.png";
        } else {
          return "";
        }
      },
    }
  }
</script>

<style scoped>
  .anty-img-wrap {
    height: 25px;
    position: relative;
  }

  .anty-img-wrap > img {
    max-height: 100%;
  }

  .marginCss {
    margin-top: 20px;
  }

  @import '../../../assets/less/index.less';
</style>