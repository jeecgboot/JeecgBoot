<template>
  <a-card title="树形结构图片翻页查看" style="min-width: 800px;overflow-x:auto ">
    <a-row>
      <!-- 左侧文件树 -->
      <a-col :span="5">
        <a-tree
          showLine
          :treeData="treeData"
          :expandedKeys="[expandedKeys[0]]"
          :selectedKeys="selectedKeys"
          :style="{'height':'500px','border-right':'2px solid #c1c1c1','overflow-y':'auto'}"
          @expand="onExpand"
          @select="this.onSelect"
        >
        </a-tree>
      </a-col>

      <!--右侧缩略图-->
      <a-col :span="19">
        <a-row style="margin-top: 10px">
          <a-col :span="24" style="padding-left: 2%;margin-bottom: 10px">
            <a-button @click="prev" type="primary"><a-icon type="left" />上一页</a-button>
            <a-button @click="next" type="primary" style="margin-left: 8px">下一页<a-icon type="right" /></a-button>
            <span style="margin-left: 15%;font-weight: bolder">{{ navName }}</span>
          </a-col>
          <a-col :span="24" style="padding-left: 2%;">
            <img :src="imgUrl" preview>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
  import draggable from 'vuedraggable'

  export default {
    name: 'ImgTurnPage',
    components:{
      draggable
    },
    data() {
      return {
        description: '图片翻页',
        //数据集
        treeData: [{
          title: '第一页',
          key: '0-0',
          children: [{
            title: '1页',
            key: '0-0-0',
            imgUrl:'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2735633715,2749454924&fm=27&gp=0.jpg'
          }, {
            title: '2页',
            key: '0-0-1',
            imgUrl:'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3967239004,1951414302&fm=27&gp=0.jpg'
          }]
        },{
          title: '第二页',
          key: '0-1',
          children: [{
            title: '1页',
            key: '0-1-0',
            imgUrl:'https://ss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=3660968530,985748925&fm=191&app=48&size=h300&n=0&g=4n&f=JPEG?sec=1853310920&t=5e64af964be378c6c2a3b0acc65dfe24'
          }, {
            title: '2页',
            key: '0-1-1',
            imgUrl:'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=889120611,3801177793&fm=27&gp=0.jpg'
          }]
        },{
          title: '第三页',
          key: '0-2',
          children: [{
            title: '1页',
            key: '0-2-0',
            imgUrl:'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2445468140,2491956848&fm=27&gp=0.jpg'
          }]
        }],
        selectedKeys:[],
        expandedKeys:[],
        sort:0,
        imgUrl:'',
        navName:'',
        imgList:[],
      }
    },
    created(){
      this.getImgList();
    },
    methods: {
      getImgList(){
        var count = 0;
        for(var i=0;i<this.treeData.length;i++){
          for(var j=0;j<this.treeData[i].children.length;j++){
            this.imgList.push({key:this.treeData[i].children[j].key,pkey:this.treeData[i].key,sort:count++,
              imgUrl:this.treeData[i].children[j].imgUrl,navName:this.treeData[i].title+"/"+this.treeData[i].children[j].title})
          }
        }
        this.setValue(this.imgList[this.sort]);
      },
      onSelect (selectedKeys, info) {
        for(var i=0;i<this.imgList.length;i++){
          if(this.imgList[i].key === selectedKeys[0]){
            this.sort = this.imgList[i].sort;
            this.setValue(this.imgList[i]);
            break;
          }
        }
      },
      onExpand (expandedKeys) {
        this.expandedKeys = [];
        if(expandedKeys !== null && expandedKeys !== ''){
          this.expandedKeys[0] = expandedKeys[1];
        }
      },
      prev(){
        if(this.sort === 0){
          this.sort = this.imgList.length-1;
        }else{
          this.sort = this.sort - 1;
        }
        this.setValue(this.imgList[this.sort]);
      },
      next(){
        if(this.sort === this.imgList.length-1){
          this.sort = 0;
        }else{
          this.sort = this.sort + 1;
        }
        this.setValue(this.imgList[this.sort]);
      },
      // 设置受控节点值
      setValue(value){
        this.selectedKeys = [];
        this.imgUrl = value.imgUrl;
        this.selectedKeys[0] = value.key;
        this.expandedKeys[0] = value.pkey;
        this.navName = value.navName;
      }
    }
  }
</script>

<style scoped>

</style>