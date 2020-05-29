<template>
  <a-menu :mode="mode" :style="style" :default-selected-keys="['1']">
    <a-menu-item :key="item.key" v-for="item in itemList" @click = 'menuClick(item.key)'>

        {{ item.text }} <a-badge :count="item.point" :number-style="{ backgroundColor: '#52c41a' }" />

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
          this.$emit("clickHandle",key);
        }
      },
      created() {
        console.log("itemList",this.itemList)
      }
    }
</script>

<style scoped>

</style>