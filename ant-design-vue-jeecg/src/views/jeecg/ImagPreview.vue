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
                      <img style="width: 100%;" :src="fileDetail.imgUrl" :preview="dataSource[0].key">
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
        description: '图片预览页面',
        spinning:false,
        //数据集
        dataSource: [{
          key:0,
          fileDetails:[
            {
              imgUrl:"https://static.jeecg.com/upload/test/3a4490d5d1cd495b826e528537a47cc1.jpg"
            },
            {
              imgUrl:"https://static.jeecg.com/temp/国炬软件logo_1606575029126.png"
            }
          ]
        },{
          key:1,
          fileDetails:[
            {
              imgUrl:"https://static.jeecg.com/upload/test/u27356337152749454924fm27gp0_1588149731821.jpg"
            },
            {
              imgUrl:"https://static.jeecg.com/upload/test/1_1588149743473.jpg"
            }
          ]
        }
    ],
    url: {
    }
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