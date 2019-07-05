<template>
  <a-card :bordered="false">
    <a-col :span="18">
      <a-spin tip="Loading..." :spinning="spinning">
        <div>
          <a-row>
            <a-col :span="18">
              <p>
                <a-divider orientation="left">组一</a-divider>
              </p>
            </a-col>
            <a-col :span="6"></a-col>
            <!-- 预览区域 -->
            <a-col :span="12">
              <template>
                <div v-for="(fileDetail,index) in dataSource[0].fileDetails" :key="index">
                  <div style="float: left;width:104px;height:104px;margin-right: 10px;margin: 0 8px 8px 0;">
                    <div
                      style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 4px;">
                      <img style="width: 100%;" :src="fileDetail.imgUrl"  :preview="dataSource[0].key">
                    </div>
                  </div>
                </div>
              </template>
            </a-col>
          </a-row>
        </div>
        <div>
          <a-row>
            <a-col :span="18">
              <p>
                <a-divider orientation="left">组二</a-divider>
              </p>
            </a-col>
            <a-col :span="6"></a-col>
            <!-- 预览区域 -->
            <a-col :span="12">
              <template>
                <div v-for="(fileDetail,index) in dataSource[1].fileDetails" :key="index">
                  <div style="float: left;width:104px;height:104px;margin-right: 10px;margin: 0 8px 8px 0;">
                    <div
                      style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 4px;">
                      <img style="width: 100%;" :src="fileDetail.imgUrl"  :preview="dataSource[1].key">
                    </div>
                  </div>
                </div>
              </template>
            </a-col>
          </a-row>
        </div>
      </a-spin>
      <p></p>
    </a-col>
  </a-card>
</template>

<script>

  import ARow from 'ant-design-vue/es/grid/Row'

  export default {
    name: 'ImagPreview',
    components: {
      ARow
    },
    data() {
      return {
        description: '电子档补扫页面',
        spinning:false,
        //数据集
        dataSource: [{
          key:0,
          fileDetails:[
            {
              imgUrl:"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2735633715,2749454924&fm=27&gp=0.jpg"
            },
            {
              imgUrl:"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3967239004,1951414302&fm=27&gp=0.jpg"
            },
            {
              imgUrl:"https://ss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=3660968530,985748925&fm=191&app=48&size=h300&n=0&g=4n&f=JPEG?sec=1853310920&t=5e64af964be378c6c2a3b0acc65dfe24"
            }
          ]
        },{
          key:1,
          fileDetails:[
            {
              imgUrl:"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=889120611,3801177793&fm=27&gp=0.jpg"
            },
            {
              imgUrl:"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2445468140,2491956848&fm=27&gp=0.jpg"
            }
          ]
        },
    ],
        url: {
        },

      }
    },
    created() {
    },
    methods: {
    }
  }
</script>
<style scoped>
  .table-operator {
    margin-bottom: 10px
  }

  .clName .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle {
    width: 10px !important;
  }

  .clName .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
    background-color: #1890FF !important;
  }
</style>