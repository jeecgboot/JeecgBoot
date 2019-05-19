<template>
  <a-card>
    <draggable @end="end" :options="{animation: 300}" v-model="dataSource" style="display: inline-block">
      <template v-for="(data,index) in dataSource">
        <div style="float: left;width:150px;height:150px;margin-right: 10px;margin: 0 8px 8px 0;" :key="index">
          <div style="width: 100%;height: 100%;position: relative;padding: 8px;border: 1px solid #d9d9d9;border-radius: 4px;">
            <img style="width: 100%;" :src="data.filePath" preview="index">
          </div>
        </div>
      </template>
      <a-button @click="sureChange" type="primary" style="margin-top: 115px">确定</a-button>
    </draggable>
    <br/>
    <a-row>
      <a-col :span="12">
        <p>拖拽前json数据：</p>
        <textarea rows="25" style="width: 780px">{{ oldDateSource }}</textarea>
      </a-col>
      <a-col :span="12">
        <p>拖拽后json数据：</p>
        <textarea rows="25" style="width: 780px">{{ newDateSource }}</textarea>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
  import draggable from 'vuedraggable'
  import ARow from 'ant-design-vue/es/grid/Row'
  import ACol from 'ant-design-vue/es/grid/Col'

  export default {
    name: 'ImgDragSort',
    components:{
      ACol,
      ARow,
      draggable
    },
    data() {
      return {
        description: '图片拖拽排序',
        spinning: false,
        //数据集
        dataSource: [
          {id:'000',sort: 0,filePath: 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2735633715,2749454924&fm=27&gp=0.jpg'},
          {id:'111',sort: 1,filePath: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3967239004,1951414302&fm=27&gp=0.jpg'},
          {id:'222',sort: 2,filePath: 'https://ss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=3660968530,985748925&fm=191&app=48&size=h300&n=0&g=4n&f=JPEG?sec=1853310920&t=5e64af964be378c6c2a3b0acc65dfe24'},
          {id:'333',sort: 3,filePath: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=889120611,3801177793&fm=27&gp=0.jpg'},
          {id:'444',sort: 4,filePath: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2445468140,2491956848&fm=27&gp=0.jpg'}
        ],
        oldDateSource:[],
        newDateSource:[],
      }
    },
    created(){
      this.oldDateSource = this.dataSource;
    },
    methods:{
      end: function (evt) {
        console.log("拖动前的位置"+evt.oldIndex);
        console.log("拖动后的位置"+evt.newIndex);
      },
      sureChange(){
        for(var i=0;i<this.dataSource.length;i++){
          this.dataSource[i].sort = i;
        }
        this.newDateSource = this.dataSource;
      }
    }
  }
</script>

<style scoped>

</style>