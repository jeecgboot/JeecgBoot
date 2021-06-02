<template>
  <div class="input-cron">
    <a-input :placeholder="placeholder" v-model="editCronValue" :disabled="disabled">
      <a slot="addonAfter" @click="showConfigDlg" class="config-btn" :disabled="disabled">
        <a-icon type="setting"></a-icon>
        选择
      </a>
    </a-input>
    <j-modal :visible.sync="show" title="Cron表达式" width="800px">
      <easy-cron
        v-model="editCronValue"
        :exeStartTime="exeStartTime"
        :hideYear="hideYear"
        :remote="remote"
        :hideSecond="hideSecond"
        style="width: 100%"
      ></easy-cron>
    </j-modal>
  </div>
</template>

<script>
import EasyCron from './EasyCron.vue'

export default {
  name: 'input-cron',
  components: {EasyCron},
  model: {
    prop: 'cronValue',
    event: 'change'
  },
  props: {
    cronValue: {
      type: String,
      default: ''
    },
    width: {
      type: String,
      default: '800px'
    },
    placeholder: {
      type: String,
      default: '请输入cron表达式'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    exeStartTime: {
      type: [Number, String, Object],
      default: 0
    },
    hideSecond: {
      type: Boolean,
      default: false
    },
    hideYear: {
      type: Boolean,
      default: false
    },
    remote: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      editCronValue: this.cronValue,
      show: false,
    }
  },
  watch: {
    cronValue(newVal, oldVal) {
      if (newVal === this.editCronValue) {
        return
      }
      this.editCronValue = newVal
    },
    editCronValue(newVal, oldVal) {
      this.$emit('change', newVal)
    }
  },
  methods: {
    showConfigDlg() {
      if (!this.disabled) {
        this.show = true
      }
    }
  }
}
</script>

<style scoped>

  .config-btn {
    cursor: pointer;
  }

</style>
