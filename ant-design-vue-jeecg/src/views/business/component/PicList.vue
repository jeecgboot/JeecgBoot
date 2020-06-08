<template >

  <a-list :grid="{ gutter: 16, column: 4 }" :data-source="imgsrc" >
    <a-list-item slot="renderItem" slot-scope="item, index" >
      <img   class="item" :src="item"  :preview="item"/>
    </a-list-item>
  </a-list>

</template>
<script>
  import {getFileAccessHttpUrl} from '@/api/manage';

  export default {
    name:"PicList",
    props:{
      images:{
        type:Array,
        default:[]
      }
    },
    data() {
      return {


      }
    },
    methods: {

    },computed:{
      imgsrc(){
        debugger
      let copy = [];
      this.images.forEach(
        element => {
          copy.push(getFileAccessHttpUrl(element));
          console.log(element)
        }
      );
      console.log(copy)
      return copy;
    }


    }
  }
</script>
<style>

  /* item 用来放置全部的子元素 */
  .item {
    margin-top: 30px;
    width: 25vh;
    height: 20vh;
    background: green;
    object-fit:cover;

  }
</style>