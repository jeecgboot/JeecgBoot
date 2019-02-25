日期组件
说明：antd-vue日期组件需要用moment中转一下，用起来不是很方便，特二次封装，使用时只需要传字符串即可
====

参数说明
----
placeholder：placeholder
readOnly：true/false
value：绑定v-model或是v-decorator后不需要设置
showTime：是否展示时间true/false
dateFormat：日期格式 默认'YYYY-MM-DD' 若showTime设置为true则需要将其设置成对应的时间格式(如：YYYY-MM-DD HH:mm:ss)
triggerChange：触发组件值改变的事件是否是change,当使用v-decorator时且没有设置decorator的option.trigger为input需要设置该值为true

使用示例
----
1.组件带有v-model的使用方法
<j-date v-model="dateStr"></j-date>

2.组件带有v-decorator的使用方法
  a).设置trigger-change属性为true
    <j-date :trigger-change="true" v-decorator="['dateStr',{}]"></j-date>
  b).设置decorator的option.trigger为input
    <j-date v-decorator="['dateStr',{trigger:'input'}]"></j-date>

3.其他使用
添加style
    <j-date v-model="dateStr" style="width:100%"></j-date>
添加placeholder
    <j-date v-model="dateStr" placeholder="请输入dateStr"></j-date>
添加readOnly
    <j-date v-model="dateStr" :read-only="true"></j-date>

备注:
script内需引入jdate
<script>
  import JDate from '@/components/jeecg/JDate'
  export default {
    name: "demo",
    components: {
      JDate
    },
....
</script>