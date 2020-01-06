<template>
  <a-row class="j-select-biz-component-box" type="flex" :gutter="8">
    <a-col class="left" :class="{'full': !buttons}">
      <slot name="left">
        <a-select
          mode="multiple"
          :placeholder="placeholder"
          v-model="selectValue"
          :options="selectOptions"
          allowClear
          :disabled="disabled"
          :open="false"
          style="width: 100%;"
          @click.native="visible=(buttons?visible:true)"
        />
      </slot>
    </a-col>

    <a-col v-if="buttons" class="right">
      <a-button type="primary" icon="search" :disabled="disabled" @click="visible=true">{{selectButtonText}}</a-button>
    </a-col>

    <j-select-biz-component-modal
      v-model="selectValue"
      :visible.sync="visible"
      v-bind="modalProps"
      @options="handleOptions"
    />
  </a-row>
</template>

<script>
  import JSelectBizComponentModal from './JSelectBizComponentModal'

  export default {
    name: 'JSelectBizComponent',
    components: { JSelectBizComponentModal },
    props: {
      value: {
        type: String,
        default: ''
      },
      /** 是否返回 id，默认 false，返回 code */
      returnId: {
        type: Boolean,
        default: false
      },
      placeholder: {
        type: String,
        default: '请选择'
      },
      disabled: {
        type: Boolean,
        default: false
      },
      // 是否支持多选，默认 true
      multiple: {
        type: Boolean,
        default: true
      },
      // 是否显示按钮，默认 true
      buttons: {
        type: Boolean,
        default: true
      },
      // 显示的 Key
      displayKey: {
        type: String,
        default: null
      },
      // 返回的 key
      returnKeys: {
        type: Array,
        default: () => ['id', 'id']
      },
      // 选择按钮文字
      selectButtonText: {
        type: String,
        default: '选择'
      },

    },
    data() {
      return {
        selectValue: [],
        selectOptions: [],
        dataSourceMap: {},
        visible: false
      }
    },
    computed: {
      valueKey() {
        return this.returnId ? this.returnKeys[0] : this.returnKeys[1]
      },
      modalProps() {
        return Object.assign({
          valueKey: this.valueKey,
          multiple: this.multiple,
          returnKeys: this.returnKeys,
          displayKey: this.displayKey || this.valueKey
        }, this.$attrs)
      },
    },
    watch: {
      value: {
        immediate: true,
        handler(val) {
          if (val) {
            this.selectValue = val.split(',')
          } else {
            this.selectValue = []
          }
        }
      },
      selectValue: {
        deep: true,
        handler(val) {
          let rows = val.map(key => this.dataSourceMap[key])
          this.$emit('select', rows)
          let data = val.join(',')
          this.$emit('input', data)
          this.$emit('change', data)
        }
      }
    },
    methods: {
      handleOptions(options, dataSourceMap) {
        this.selectOptions = options
        this.dataSourceMap = dataSourceMap
      },
    }
  }
</script>

<style lang="scss" scoped>
  .j-select-biz-component-box {

    $width: 82px;

    .left {
      width: calc(100% - #{$width} - 8px);
    }

    .right {
      width: #{$width};
    }

    .full {
      width: 100%;
    }

    /deep/ {
      .ant-select-search__field {
        display: none !important;
      }
    }
  }
</style>