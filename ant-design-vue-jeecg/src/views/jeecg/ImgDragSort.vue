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
          {id:'000',sort: 0,filePath: 'https://static.jeecg.com/upload/test/1_1588149743473.jpg'},
          {id:'111',sort: 1,filePath: 'https://static.jeecg.com/upload/test/u27356337152749454924fm27gp0_1588149731821.jpg'},
          {id:'222',sort: 2,filePath: 'https://static.jeecg.com/upload/test/u24454681402491956848fm27gp0_1588149712663.jpg'},
          {id:'333',sort: 3,filePath: 'https://static.jeecg.com/temp/国炬软件logo_1606575029126.png'},
          {id:'444',sort: 4,filePath: 'https://static.jeecg.com/upload/test/u8891206113801177793fm27gp0_1588149704459.jpg'}
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