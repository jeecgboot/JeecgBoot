<template>
  <div style="width: 100%;">
    <a-select
      mode="multiple"
      placeholder="Please select"
      :value="nameList"
      style="width: calc(100% - 178px);">
    </a-select>
    <span style="display: inline-block;width:170px;float: right;overflow: hidden;">
      <a-button type="primary" @click="handleSelect" icon="search" style="width: 81px">选择</a-button>
      <a-button type="primary" @click="selectReset" icon="reload" style="margin-left: 8px;width: 81px">清空</a-button>
    </span>

    <!-- 选择多个用户支持排序 -->
    <j-select-multi-user-modal ref="selectModal" @selectFinished="selectOK"/>
  </div>
</template>

<script>
  import JSelectMultiUserModal from './modal/JSelectMultiUserModal'
  export default {
    name: 'JSelectMultiUser',
    components:{ JSelectMultiUserModal },
    props:{
      value:{
        type:String,
        required:false
      }
    },
    data(){
      return {
        selectList: [],
      }
    },
    computed: {
      nameList: function () {
        var names = [];
        for (var a = 0; a < this.selectList.length; a++) {
          names.push(this.selectList[a].name);
        }
        let nameStr = ''
        if(names.length>0){
          nameStr = names.join(",")
        }
        this.$emit("change",nameStr)
        return names;
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    },
    methods:{
      handleSelect: function () {
        this.$refs.selectModal.add();
      },
      selectReset() {
        this.selectList = [];
      },
      selectOK: function (data) {
        this.selectList = data;
      }
    }
  }
</script>
