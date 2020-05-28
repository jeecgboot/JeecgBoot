<template>
  <a-menu :mode="mode" :style="style" :default-selected-keys="['1']">
    <a-menu-item :key="item.key" v-for="item in itemList" @click = 'menuClick(item.key)'>
      {{ item.text }}
    </a-menu-item>
  </a-menu>
</template>

<script>
  // 左边菜单
    export default {
      name: "BusinessMenu",
      props: {
        itemList: {
          type: Array,
          required: true,
          default: () => []
        },
        menuStyle:{
          default: () => {}
        },
        mode:'',
        call:{
          type: String,
          default:'headHandle'
        }
      },
      data () {
        return {

          target: null,
          selectedKeys: []
        }
      },
      computed: {
        style () {
         return  this.menuStyle;
        }
      },
      methods:{
        menuClick(key){
          console.log("headHandle")
          this.$emit('headHandle',key);
        }
      },
      created() {
        console.log("itemList",this.itemList)
      }
    }
</script>

<style scoped>

</style>