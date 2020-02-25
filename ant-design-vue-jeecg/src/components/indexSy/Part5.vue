<template>
  <div class="Part5">
    <div class="head">
      <span class="left">事件列表</span>
      <span class="right">
        <a href="javascript:;"
           class="more">更多</a>
      </span>
    </div>
    <ul>
      <li>
        <a><span class="square color1">1</span>小区门前有几颗老树，由于大风，快要刮倒了</a>
      </li>
      <li>
        <a><span class="square color2">2</span>住宅小区3号楼4单元501住户与房东产生矛盾</a>
      </li>
      <li>
        <a><span class="square color3">3</span>小区门口道路有小区居民反映落得很久没亮了</a>
      </li>
      <li>
        <a><span class="square color4">4</span>群众咨询在什么地方，如何办理困难证明手续</a>
      </li>
      <li>
        <a><span class="square color4">5</span>南环小区15号楼居民到社区反映，楼下顺风车</a>
      </li>
      <li>
        <a><span class="square color4">6</span>村民咨询独生子女证明丢失如何补办，是否有别的办法</a>
      </li>
      <li>
        <a><span class="square color4">7</span>小区门前有几颗老树，由于大风，快要刮倒了</a>
      </li>
      <li>
        <a><span class="square color4">8</span>住宅小区3号楼4单元501住户与房东产生矛盾</a>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "zhongdian",
  data () {
    return {};
  }
};
</script>
<style lang="less" scoped>
ul,
li {
  list-style: none;
  margin: 0;
  padding: 0;
}
.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #297dcc;
}

.head .left {
  font-size: 18px;
  /* border-bottom: 3px solid #297dcc; */
  padding: 10px 0;
  font-weight: 600;
}
.head-small .left {
  font-size: 16px !important;
}

.head .right {
  font-size: 16px;
  cursor: pointer;
}

a {
  text-decoration: none;
  color: #999;
  font-size: 14px;
}

.more:hover {
  color: #297dcc;
}
.contentP {
  line-height: 1.4;
}
.Part5 {
  padding: 20px;
  ul {
    li {
      height: 36px;
      line-height: 36px;
      border-bottom: 1px solid #eee;
      a {
        display: inline-block;
        width: 100%;
        color: #333;
        cursor: pointer;
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        .square {
          display: inline-block;
          width: 20px;
          height: 20px;
          line-height: 20px;
          text-align: center;
          margin-right: 10px;
          color: #fff;
          font-size: 12px;
        }
        .color1 {
          background-color: #ff3c00;
        }
        .color2 {
          background-color: #f1b51e;
        }
        .color3 {
          background-color: #a07811;
        }
        .color4 {
          background-color: #999;
        }
      }
    }
  }
}
</style>

