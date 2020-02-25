<template>
  <div class="zhongdian">
    <div class="head">
      <span class="left">重点人员</span>
      <span class="right">
        <a href="javascript:;"
           class="more">更多</a>
      </span>
    </div>
    <ul>
      <li>
        <div class="company">
          <span class="company-title puper">【重点青少年】</span>
          <span>管人英</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title yellow">【特殊人群】</span>
          <span>冯秀霞</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title blue">【校园周边】</span>
          <span>范长春</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title puper">【重点青少年】</span>
          <span>管人英</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title yellow">【特殊人群】</span>
          <span>冯秀霞</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title blue">【校园周边】</span>
          <span>范长春</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title puper">【重点青少年】</span>
          <span>管人英</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
      </li>
      <li>
        <div class="company">
          <span class="company-title yellow">【特殊人群】</span>
          <span>冯秀霞</span>
          <span>在家稳定</span>
          <span>汇报人：马国华</span>
        </div>
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
.zhongdian {
  padding: 20px;
  ul {
    li {
      height: 36px;
      line-height: 36px;
      padding-right: 10px;
      border-bottom: 1px solid #eee;
      //   padding-left: 28px;
      position: relative;
      //   &::before {
      //     content: '';
      //     position: absolute;
      //     top: 0;
      //     left: 0;
      //     background-image: url('../../assets/image/cheng.png');
      //     width: 18px;
      //     height: 18px;
      //   }
      .company {
        font-size: 14px;
        display: flex;
        justify-content: space-between;
        .company-title {
          width: 100px;
        }
        .puper {
          color: purple;
        }
        .yellow {
          color: yellowgreen;
        }
        .blue {
          color: skyblue;
        }
      }
    }
  }
}
</style>

