<template>
  <div>
    <div v-if="isDetailsMode">
      <p class="detailStr" :title="detailStr">{{ detailStr }}</p>
    </div>
    <a-row v-else class="j-select-row" type="flex" :gutter="8">
      <a-col class="left" :class="{ full: !showButton }">
        <!-- 显示加载效果 -->
        <a-input v-if="loading" readOnly placeholder="加载中…">
          <template #prefix>
            <LoadingOutlined />
          </template>
        </a-input>
        <a-select
          v-else
          ref="select"
          v-model:value="selectValues.value"
          :placeholder="placeholder"
          :mode="multiple"
          :open="false"
          :disabled="disabled"
          :options="options"
          :maxTagCount="maxTagCount"
          @change="handleChange"
          style="width: 100%"
          @click="!disabled && openModal(false)"
          v-bind="attrs"
        >
          <template v-if="isCustomRenderTag" #tagRender="{ label, value, option}">
            <a-tag class="ant-select-selection-item" style="margin-right: 4px">
              <span class="ant-select-selection-item-content" style="font-size: 14px;max-width: 300px" :title="tagRender(label, value, false)">{{ tagRender(label, value, true) }}</span>
              <span class="ant-select-selection-item-remove">
                <Icon icon="ant-design:close-outlined" size="12" @click="handleRemoveClick(value)"></Icon>
              </span>
            </a-tag>
          </template>
        </a-select>
      </a-col>
      <a-col v-if="showButton" class="right">
        <a-button v-if="buttonIcon" :preIcon="buttonIcon" type="primary" @click="openModal(true)" :disabled="disabled">
          {{ buttonText }}
        </a-button>
        <a-button v-else type="primary" @click="openModal(true)" :disabled="disabled">
          {{ buttonText }}
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, inject, reactive, watch } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { getDepartPathNameByOrgCode } from "@/utils/common/compUtils";

  export default defineComponent({
    name: 'JSelectBiz',
    components: { LoadingOutlined },
    inheritAttrs: false,
    props: {
      showButton: propTypes.bool.def(true),
      buttonText: propTypes.string.def('选择'),
      disabled: propTypes.bool.def(false),
      placeholder: {
        type: String,
        default: '请选择',
      },
      // 是否支持多选，默认 true
      multiple: {
        type: String,
        default: 'multiple',
      },
      // 是否正在加载
      loading: propTypes.bool.def(false),
      // 最多显示多少个 tag
      maxTagCount: propTypes.number,
      // buttonIcon
      buttonIcon: propTypes.string.def(''),
      // 【TV360X-1002】是否是详情模式
      isDetailsMode: propTypes.bool.def(false),
      //是否自定义渲染tag
      isCustomRenderTag: propTypes.bool.def(false),
      rowKey: propTypes.string.def('id'),
    },
    emits: ['handleOpen', 'change'],
    setup(props, { emit, refs }) {
      //接收下拉框选项
      const options = inject('selectOptions') || ref([]);
      //接收选择的值
      const selectValues = inject('selectValues') || ref({});
      const attrs = useAttrs();
      const detailStr = ref('');

      //存放部门名称
      const departNamePath = ref<Record<string, string>>({});
      
      /**
       * 打开弹出框
       */
      function openModal(isButton) {
        if (props.showButton && isButton) {
          emit('handleOpen');
        }
        if (!props.showButton && !isButton) {
          emit('handleOpen');
        }
      }

      /**
       * 下拉框值改变事件
       */
      function handleChange(value) {
        selectValues.value = value;
        selectValues.change = true;
        emit('change', value);
      }
      
      /**
       * 多选tag自定义渲染
       *
       * @param label
       * @param value
       * @param isEllipsis 是否省略
       */
      function tagRender(label, value, isEllipsis) {
        if (departNamePath.value[value]) {
           //是否需要省略显示
           if(!isEllipsis){
             return departNamePath.value[value];
           } else {
             let departName = departNamePath.value[value];
             //超过20则截取后20位的字符，前面加省略号
             if(departName && departName.length >= 20){
               const name:any = departName.substring(departName.length - 20);
               return '...' + name;
             } else {
               return departName;
             }
           }
        }
        //判断rowKey是否为orgCode
        if(props?.rowKey && props?.rowKey === 'orgCode'){
          getDepartPathNameByOrgCode(value, label, '').then((data) => {
            departNamePath.value[value] = data;
          });
        } else {
          //否则按照id处理
          getDepartPathNameByOrgCode('', label, value).then((data) => {
            departNamePath.value[value] = data;
          });
        }
      }

      /**
       * tag删除
       * 
       * @param value
       */
      function handleRemoveClick(value) {
        if(selectValues?.value){
          let values = selectValues?.value.filter(item => item !== value);
          handleChange(values);
        }
      }
      // -update-begin--author:liaozhiyang---date:20240617---for：【TV360X-1002】详情页面行编辑用户组件和部门组件显示方式优化
      watch(
        [selectValues, options],
        () => {
          if (props.isDetailsMode) {
            if (Array.isArray(selectValues.value) && Array.isArray(options.value)) {
              const result = options.value.map((item) => item.label);
              detailStr.value = result.join(',');
            }
          }
        },
        { immediate: true }
      );
      // -update-end--author:liaozhiyang---date:20240617---for：【TV360X-1002】详情页面行编辑用户组件和部门组件显示方式优化

      return {
        attrs,
        selectValues,
        options,
        handleChange,
        openModal,
        detailStr,
        tagRender,
        handleRemoveClick,
      };
    },
  });
</script>
<style lang="less" scoped>
  .j-select-row {
    @width: 82px;

    .left {
      width: calc(100% - @width - 8px);
    }

    .right {
      width: @width;
    }

    .full {
      width: 100%;
    }

    :deep(.ant-select-search__field) {
      display: none !important;
    }
  }
  .detailStr {
    margin: 0;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
