
<template>
  <component ref="compModel" :is="comp" :formData="formData" v-if="comp" @ok="callBackOk" @close="callBackClose"></component>
</template>
<script>
  export default {
    name: 'DynamicComponent',
    data () {
      return {
        compName: this.path
      }
    },
    computed: {
      comp: function () {
        return () => import(`@/views/${this.compName}.vue`)
      }
    },
    props: ['path','formData'],
    methods: {
      add () {
        this.$refs.compModel.add();
      },
      callBackClose () {
        this.$emit('close');
      },
      handleOk () {
        this.$refs.compModel.handleOk();
      },
      callBackOk(){
        this.$emit('ok');
        this.close();
      },
    }
  }
</script>