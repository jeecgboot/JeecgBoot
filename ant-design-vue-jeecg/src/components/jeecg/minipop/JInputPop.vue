<template>
  <a-popover trigger="contextmenu" v-model="visible" :placement="position">
    <!--"(node) => node.parentNode.parentNode"-->
    <div slot="title">
      <span>{{ title }}</span>
      <span style="float: right" title="关闭">
        <a-icon type="close" @click="visible=false"/>
      </span>
    </div>
    <a-input :value="inputContent" @change="handleInputChange">
      <a-icon slot="suffix" type="fullscreen" @click.stop="pop" />
    </a-input>
    <div slot="content">
      <textarea :value="inputContent" @input="handleInputChange" :style="{ height: height + 'px', width: width + 'px' }"></textarea>
    </div>
  </a-popover>
</template>

<script>
  export default {
    name: 'JInputPop',
    props:{
      title:{
        type:String,
        default:'',
        required:false
      },
      position:{
        type:String,
        default:'right',
        required:false
      },
      height:{
        type:Number,
        default:200,
        required:false
      },
      width:{
        type:Number,
        default:150,
        required:false
      },
      value:{
        type:String,
        required:false
      },
      popContainer:{
        type:String,
        default:'',
        required:false
      }

    },
    data(){
      return {
        visible:false,
        inputContent:''

      }
    },

    watch:{
      value:{
        immediate:true,
        handler:function(){
          if(this.value && this.value.length>0){
            this.inputContent = this.value;
          }
        }
      },
    },
    model: {
      prop: 'value',
      event: 'change'
    },
    methods:{
      handleInputChange(event){
        this.inputContent = event.target.value
        this.$emit('change',this.inputContent)
      },
      pop(){
        this.visible=true
      },
      getPopupContainer(node){
        if(!this.popContainer){
          return node.parentNode
        }else{
          return document.getElementById(this.popContainer)
        }

      }
    }
  }
</script>

<style scoped>

</style>