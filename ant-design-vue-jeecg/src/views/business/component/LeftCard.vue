<template>

  <a-card :title="title"  :hoverable="hoverable"  :headStyle = "{'font-size':'16px','text-align':'center'}"
          style="width: 90%;"  >
    <img
      slot="cover"
      alt="example"
      :src="picture"
    />
    <template slot="actions" class="ant-card-actions">
      <span @click="detail" v-if="hoverable" style="font-size: 16px">详情</span>
      <span @click="apply" style="font-size: 16px">申请</span>
    </template>
  </a-card>
</template>

<script>
    export default {
        name: "LeftCard",
        props:{
          hoverable:{
            type:Boolean,
            default:()=>{
                  return false;
              }
          },
          title:'',
          bgpic:{
            type:String,
            default: ()=>{ return 'https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png'}
          }

        },
      data(){
          return {
            picture: this.bgpic
          }
      },
        methods:{
          detail(){
             this.$emit("toDetail");
          },
          apply(){
            this.$emit("toApply");
          }
        },created() {

      }
    }
</script>

<style scoped>

</style>