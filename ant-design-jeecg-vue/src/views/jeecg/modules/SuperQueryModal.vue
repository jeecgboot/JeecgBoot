<template>
  <a-modal
    title="高级查询构造器"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    :mask="false"
    okText="查询"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form>
        <div>
          <a-row type="flex" style="margin-bottom:10px" :gutter="16" v-for="(item, index) in queryParamsModel" :key="index">
            <a-col :span="6">
              <a-select  placeholder="选择查询字段" v-model="item.field">
                <a-select-option value="name">用户名</a-select-option>
                <a-select-option value="key_word">关键词</a-select-option>
                <a-select-option value="birthday">生日</a-select-option>
                <a-select-option value="age">年龄</a-select-option>
              </a-select>
            </a-col>
            <a-col :span="6">
              <a-select placeholder="选择匹配规则"  v-model="item.rule">
                <a-select-option value="=">等于</a-select-option>
                <a-select-option value="!=">不等于</a-select-option>
                <a-select-option value=">">大于</a-select-option>
                <a-select-option value=">=">大于等于</a-select-option>
                <a-select-option value="<">小于</a-select-option>
                <a-select-option value="<=">小于等于</a-select-option>
                <a-select-option value="LEFT_LIKE">以..开始</a-select-option>
                <a-select-option value="RIGHT_LIKE">以..结尾</a-select-option>
                <a-select-option value="LIKE">包含</a-select-option>
                <a-select-option value="IN">在...中</a-select-option>
              </a-select>
            </a-col>

            <a-col :span="6"><a-input placeholder="请输入值" v-model="item.val"/></a-col>
            <a-col :span="6">
              <a-button @click="handleAdd"  icon="plus"></a-button>&nbsp;
              <a-button @click="handleDel( index )"  icon="minus"></a-button>
            </a-col>
          </a-row>
        </div>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'

  export default {
    name: "SuperQueryModal",
    data () {
      return {
        visible: false,
        queryParamsModel: [{},{}],
        confirmLoading: false
      }
    },
    created () {
    },
    methods: {
      show () {
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        console.log(this.queryParamsModel)
        // 子组件中触发父组件方法ee并传值cc12345
        this.$emit('handleSuperQuery', this.queryParamsModel)
      },
      handleCancel () {
        this.close()
      },
      handleAdd () {
        this.queryParamsModel.push({});
      },
      handleDel (index) {
        console.log(index)
        this.queryParamsModel.splice(index,1);
      }
    }
  }
</script>

<style scoped>
</style>