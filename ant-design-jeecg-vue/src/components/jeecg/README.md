# JDate 日期组件 使用文档
  
###### 说明: antd-vue日期组件需要用moment中转一下，用起来不是很方便，特二次封装，使用时只需要传字符串即可
## 参数配置
| 参数           | 类型      | 必填 |说明|
|--------------|---------|----|---------|
| placeholder      |string   | | placeholder      |
| readOnly   | boolean   | | true/false 默认false                 |
| value      | string | | 绑定v-model或是v-decorator后不需要设置    |
| showTime | boolean | | 是否展示时间true/false 默认false  |
| dateFormat    | string | |日期格式 默认'YYYY-MM-DD' 若showTime设置为true则需要将其设置成对应的时间格式(如:YYYY-MM-DD HH:mm:ss)               |
| triggerChange | string | |触发组件值改变的事件是否是change,当使用v-decorator时且没有设置decorator的option.trigger为input需要设置该值为true               |
使用示例
----
1.组件带有v-model的使用方法
```vue
<j-date v-model="dateStr"></j-date>
```

2.组件带有v-decorator的使用方法  
  a).设置trigger-change属性为true
  ```vue
    <j-date :trigger-change="true" v-decorator="['dateStr',{}]"></j-date>
  ```
    
  b).设置decorator的option.trigger为input
   ```vue
    <j-date v-decorator="['dateStr',{trigger:'input'}]"></j-date>
   ```

3.其他使用
添加style
```vue
<j-date v-model="dateStr" style="width:100%"></j-date>
```
添加placeholder
```vue
<j-date v-model="dateStr" placeholder="请输入dateStr"></j-date>
```
添加readOnly
```vue
<j-date v-model="dateStr" :read-only="true"></j-date>
```

备注:
script内需引入jdate
```vue
<script>
  import JDate from '@/components/jeecg/JDate'
  export default {
    name: "demo",
    components: {
      JDate
    }
    //...
  }
</script>
```


 ---


# JSuperQuery 高级查询 使用文档
## 参数配置
| 参数           | 类型      | 必填 | 说明                   |
|--------------|---------|----|----------------------|
| fieldList      | array   |✔| 需要查询的列集合示例如下，type类型有:date/datetime/string/int/number      |
| callback   | array   |  | 回调函数名称(非必须)默认handleSuperQuery                |

fieldList结构示例：
```vue
  const superQueryFieldList=[{
    type:"date",
    value:"birthday",
    text:"生日"
  },{
    type:"string",
    value:"name",
    text:"用户名"
  },{
    type:"int",
    value:"age",
    text:"年龄"
  }]
```
页面代码概述:  
----
1.import之后再components之内声明
```vue
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue';
  export default {
    name: "JeecgDemoList",
    components: {
      JSuperQuery
    },

```
2.页面引用
```vue
  <!-- 高级查询区域 -->
  <j-super-query :fieldList="fieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
```
3.list页面data中需要定义三个属性：
```vue
  fieldList:superQueryFieldList,
  superQueryFlag:false,
  superQueryParams:""
```
4.list页面声明回调事件handleSuperQuery(与组件的callback对应即可)
```vue
//高级查询方法
handleSuperQuery(arg) {
  if(!arg){
    this.superQueryParams=''
    this.superQueryFlag = false
  }else{
    this.superQueryFlag = true
    this.superQueryParams=JSON.stringify(arg)
  }
  this.loadData()
},
```
5.改造list页面方法
```vue
  // 获取查询条件
  getQueryParams() {
    let sqp = {}
    if(this.superQueryParams){
      sqp['superQueryParams']=encodeURI(this.superQueryParams)
    }
    var param = Object.assign(sqp, this.queryParam, this.isorter);
    param.field = this.getQueryField();
    param.pageNo = this.ipagination.current;
    param.pageSize = this.ipagination.pageSize;
    return filterObj(param);
  },
```
6.打开弹框调用show方法：
```vue
this.$refs.superQueryModal.show();
```

# JEllipsis 字符串超长截取省略号显示
  
###### 说明: 遇到超长文本展示，通过此标签可以截取省略号显示，鼠标放置会提示全文本
## 参数配置
| 参数  | 类型     | 必填 |    说明      |
|--------|---------|----|----------------|
| value  |string   | 必填   |  字符串文本|
| length | number  | 非必填 |  默认25    |
使用示例
----
1.组件带有v-model的使用方法
```vue
<j-ellipsis :value="text"/>


# Modal弹框实现最大化功能  

1.定义modal的宽度：
```vue
  <a-modal
    :width="modalWidth"
    
    
    />
```
2.自定义modal的title,居右显示切换图标
```vue
  <template slot="title">
    <div style="width: 100%;">
      <span>{{ title }}</span>
      <span style="display:inline-block;width:calc(100% - 51px);padding-right:10px;text-align: right">
        <a-button @click="toggleScreen" icon="appstore" style="height:20px;width:20px;border:0px"></a-button>
      </span>
    </div>
  </template>
```
3.定义toggleScreen事件,用于切换modal宽度
```vue
  toggleScreen(){
      if(this.modaltoggleFlag){
        this.modalWidth = window.innerWidth;
      }else{
        this.modalWidth = 800;
      }
      this.modaltoggleFlag = !this.modaltoggleFlag;
    },
```
4.data中声明上述用到的属性
```vue
    data () {
      return {
        modalWidth:800,
        modaltoggleFlag:true,
```

# <a-select/> 下拉选项滚动错位的解决方法

## 问题描述

当使用了 `a-modal` 或其他带有滚动条的组件时，使用`a-select`组件并打开下拉框时滚动滚动条，就会导致错位的问题产生。

## 解决方法

大多数情况下，在 `a-select` 上添加一个 `getPopupContainer` 属性，值为`node => node.parentNode`即可解决。
但是如果遇到 `a-select` 标签层级过深的情况，可能仍然会显示异常，只需要多加几个`.parentNode` （例：node => node.parentNode.parentNode.parentNode）多尝试几次直到解决问题即可。

### 代码示例

```html
<a-select
    placeholder="请选择展示模板"
    :options="dicts.displayTemplate"
    :getPopupContainer="node => node.parentNode"
/>
```

# JAsyncTreeList 异步数列表组件使用说明

## 引入组件

```js
import JTreeTable from '@/components/jeecg/JTreeTable'
export default {
  components: { JTreeTable }
}
```

## 所需参数

| 参数          | 类型     | 必填  | 说明                                                 |
|-------------|--------|-----|------------------------------------------------------------|
| rowKey      | String | 非必填 | 表格行 key 的取值，默认为"id" |
| columns     | Array  | 必填   | 表格列的配置描述，具体见Antd官方文档                   |
| url         | String | 必填   | 数据查询url                                            |
| childrenUrl | String | 非必填 | 查询子级时的url，若不填则使用url参数查询子级           |
| tableProps  | Object | 非必填 | 自定义给内部table绑定的props                           |

## 代码示例

```html
<template>
  <a-card :bordered="false">
    <j-tree-table :url="url" :columns="columns" :tableProps="tableProps"/>
  </a-card>
</template>

<script>
  import JTreeTable from '@/components/jeecg/JTreeTable'

  export default {
    name: 'AsyncTreeTable',
    components: { JTreeTable },
    data() {
      return {
        url: '/api/asynTreeList',
        columns: [
          { title: '菜单名称', dataIndex: 'name' },
          { title: '组件', dataIndex: 'component' },
          { title: '排序', dataIndex: 'orderNum' }
        ],
        selectedRowKeys: []
      }
    },
     computed: {
       tableProps() {
         let _this = this
         return {
           // 列表项是否可选择
           // 配置项见：https://vue.ant.design/components/table-cn/#rowSelection
           rowSelection: {
             selectedRowKeys: _this.selectedRowKeys,
             onChange: (selectedRowKeys) => _this.selectedRowKeys = selectedRowKeys
           }
         }
       }
     }
  }
</script>
```