<template>
  <a-form-item v-if="item.view=='date'" :label="item.label">
    <template v-if="single_mode===item.mode">
      <j-date :placeholder=" '请选择'+item.label " v-model="queryParam[item.field]"></j-date>
    </template>
    <template v-else>
      <j-date placeholder="请选择开始日期" v-model="queryParam[item.field+'_begin']" style="width:calc(50% - 15px);"></j-date>
      <span class="group-query-strig">~</span>
      <j-date placeholder="请选择结束日期" v-model="queryParam[item.field+'_end']" style="width:calc(50% - 15px);"></j-date>
    </template>
  </a-form-item>

  <a-form-item v-else-if="item.view=='datetime'" :label="item.label">
    <template v-if="single_mode===item.mode">
      <j-date :placeholder=" '请选择'+item.label " :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" v-model="queryParam[item.field]"></j-date>
    </template>
    <template v-else>
      <j-date placeholder="请选择开始时间" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" v-model="queryParam[item.field+'_begin']" style="width:calc(50% - 15px);"></j-date>
      <span class="group-query-strig">~</span>
      <j-date placeholder="请选择开始时间" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" v-model="queryParam[item.field+'_end']" style="width:calc(50% - 15px);"></j-date>
    </template>
  </a-form-item>

  <a-form-item v-else-if=" item.view=='list' || item.view=='radio' " :label="item.label">
    <a-select :placeholder=" '请选择'+item.label " v-model="queryParam[item.field]">
      <a-select-option
        v-for="(op,opIndex) in dictOptions[item.field]"
        :key="opIndex"
        :value="op.value">
        {{ op.text }}
      </a-select-option>
    </a-select>
  </a-form-item>

  <a-form-item v-else-if=" item.view=='checkbox'" :label="item.label">
    <j-select-multiple :placeholder=" '请选择'+item.label " :options="dictOptions[item.field]" v-model="queryParam[item.field]"></j-select-multiple>
  </a-form-item>

  <a-form-item v-else :label="item.label">
    <template v-if="single_mode===item.mode">
      <a-input :placeholder=" '请输入'+item.label " v-model="queryParam[item.field]"></a-input>
    </template>
    <template v-else>
      <a-input :placeholder=" '请输入开始'+item.label " v-model="queryParam[item.field+'_begin']" style="width:calc(50% - 15px);"></a-input>
      <span class="group-query-strig">~</span>
      <a-input :placeholder=" '请输入结束'+item.label " v-model="queryParam[item.field+'_end']" style="width:calc(50% - 15px);"></a-input>
    </template>
  </a-form-item>

</template>

<script>
  import JDate from '@/components/jeecg/JDate.vue'
  import JSelectMultiple from '@/components/jeecg/JSelectMultiple.vue';

  export default {
    name: 'OnlineQueryFormItem',
    props:{
      item:{
        type:Object,
        default:()=>{},
        required:true
      },
      dictOptions:{
        type:Object,
        default:()=>{},
        required:true
      },
      queryParam:{
        type:Object,
        default:()=>{},
        required:true
      }
    },
    components:{
      JDate,
      JSelectMultiple
    },
    data(){
      return {
        single_mode:"single"
      }
    },
  }
</script>

<style scoped>
  .group-query-strig{
    width:30px;text-align: center;display: inline-block;
  }
</style>