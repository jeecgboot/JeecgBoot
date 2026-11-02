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
          @click="handleSelectClick"
          v-bind="attrs"
          :class="{ 'j-select-biz-wrap': isMultipleMode, 'j-select-biz-has-omitted-tags': hasOmittedTags }"
        >
          <template v-if="isCustomRenderTag" #tagRender="{ label, value, option}">
            <!-- update-begin--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开  -->
            <a-tag class="ant-select-selection-item j-select-biz-tag" style="margin-right: 4px">
              <JLeftEllipsis
                content-class="ant-select-selection-item-content j-select-biz-tag-content"
                :text="getTagFullText(label, value)"
                container-selector=".ant-select-selector"
                :reserve-width="tagReserveWidth"
              />
              <span v-if="!disabled" class="ant-select-selection-item-remove">
                <Icon icon="ant-design:close-outlined" size="12" @click="handleRemoveClick(value)"></Icon>
              </span>
            </a-tag>
            <!-- update-end--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开  -->
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
  import { computed, defineComponent, ref, inject, watch } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { getDepartPathNameByOrgCode } from '@/utils/common/compUtils';
  import JLeftEllipsis from '../JLeftEllipsis.vue';

  export default defineComponent({
    name: 'JSelectBiz',
    components: { LoadingOutlined, JLeftEllipsis },
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
      // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-1820】查询区域部门和岗位组件多选时传maxTagCount
      // 最多显示多少个 tag
      maxTagCount: propTypes.oneOfType([propTypes.number, propTypes.string]),
      // update-end--author:liaozhiyang---date:20260826---for:【LHZP-1820】查询区域部门和岗位组件多选时传maxTagCount
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

      const isMultipleMode = computed(() => props.multiple === 'multiple' || props.multiple === 'tags');
      // update-begin--author:liaozhiyang---date:20260811---for：【LHZP-513】online一对多部门的渲染样式有重叠
      const hasOmittedTags = computed(
        () => typeof props.maxTagCount === 'number' && Array.isArray(selectValues.value) && selectValues.value.length > props.maxTagCount
      );
      const tagReserveWidth = computed(() => {
        const baseWidth = props.disabled ? 24 : 44;
        return hasOmittedTags.value ? baseWidth + 40 : baseWidth;
      });
      // update-end--author:liaozhiyang---date:20260811---for：【LHZP-513】online一对多部门的渲染样式有重叠

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
       * 多选tag自定义渲染（完整路径，省略交给 JLeftEllipsis）
       *
       * @param label
       * @param value
       * @param _isEllipsis 兼容旧调用
       */
      function tagRender(label, value, _isEllipsis) {
        return getTagFullText(label, value);
      }

      // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开
      // 岗位层级过深时右侧文字完整、左侧省略
      function toDisplayText(val) {
        if (val == null || val === '') {
          return '';
        }
        if (typeof val === 'string') {
          return val;
        }
        if (typeof val === 'number' || typeof val === 'boolean') {
          return String(val);
        }
        if (Array.isArray(val)) {
          return val.map((item) => toDisplayText(item)).filter(Boolean).join('');
        }
        if (typeof val === 'object' && typeof val.children === 'string') {
          return val.children;
        }
        return '';
      }

      /** 获取完整部门/岗位路径（异步回填后响应式更新） */
      function getTagFullText(label, value) {
        if (departNamePath.value[value]) {
          return toDisplayText(departNamePath.value[value]);
        }
        const fallback = toDisplayText(label);
        if (props?.rowKey && props?.rowKey === 'orgCode') {
          getDepartPathNameByOrgCode(value, fallback, '').then((data) => {
            departNamePath.value[value] = toDisplayText(data) || fallback;
          });
        } else {
          getDepartPathNameByOrgCode('', fallback, value).then((data) => {
            departNamePath.value[value] = toDisplayText(data) || fallback;
          });
        }
        return fallback;
      }
      // update-end--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开

      /**
       * 点击选择框时打开弹窗
       * 排除点击清除按钮的情况，避免清除时误触发弹窗
       */
      function handleSelectClick(event) {
        if (props.disabled) {
          return;
        }
        // 点击清除按钮（内置clear或tag移除按钮）时不打开弹窗
        if (event.target.closest('.ant-select-selection-item-remove') || event.target.closest('.ant-select-clear')) {
          return;
        }
        openModal(false);
      }

      /**
       * tag删除
       *
       * @param value
       */
      function handleRemoveClick(value) {
        // update-begin--author:liaozhiyang---date:20260714---for：【LHZP-583】部门组件设置为只读之后还可以删除
        if (props.disabled) {
          return;
        }
        // update-end--author:liaozhiyang---date:20260714---for：【LHZP-583】部门组件设置为只读之后还可以删除
        if (selectValues?.value) {
          let values = selectValues?.value.filter((item) => item !== value);
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
        getTagFullText,
        handleRemoveClick,
        handleSelectClick,
        isMultipleMode,
        hasOmittedTags,
        tagReserveWidth,
      };
    },
  });
</script>
<style lang="less" scoped>
  .j-select-row {
    @width: 82px;
    flex-wrap: nowrap;
    align-items: flex-start;

    .left {
      // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开
      flex: 1 1 auto;
      width: calc(100% - @width - 8px);
      max-width: calc(100% - @width - 8px);
      min-width: 0;
      // update-end--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开
    }

    .right {
      flex: 0 0 @width;
      width: @width;
    }

    .full {
      width: 100%;
      max-width: 100%;
    }

    :deep(.ant-select-search__field) {
      display: none !important;
    }

    // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开
    :deep(.ant-select) {
      max-width: 100%;
    }

    :deep(.ant-select-selector) {
      max-width: 100%;
      height: auto !important;
      min-height: 32px;
      overflow: hidden;
    }

    /* 多选：tag 换行展开，不挤在一行 */
    :deep(.j-select-biz-wrap .ant-select-selection-overflow) {
      flex-wrap: wrap;
      overflow: visible;
      max-width: 100%;
    }

    :deep(.ant-select-selection-overflow-item) {
      max-width: 100%;
      min-width: 0;
    }

    // update-begin--author:liaozhiyang---date:20260811---for：【LHZP-513】online一对多部门的渲染样式有重叠
    :deep(.j-select-biz-has-omitted-tags .ant-select-selection-overflow) {
      flex-wrap: nowrap;
    }

    :deep(.j-select-biz-has-omitted-tags .ant-select-selection-overflow-item) {
      max-width: calc(100% - 48px);
    }

    :deep(.j-select-biz-has-omitted-tags .ant-select-selection-overflow-item-rest),
    :deep(.j-select-biz-has-omitted-tags .ant-select-selection-overflow-item-suffix) {
      max-width: none;
    }
    // update-end--author:liaozhiyang---date:20260811---for：【LHZP-513】online一对多部门的渲染样式有重叠

    :deep(.j-select-biz-tag) {
      display: inline-flex !important;
      align-items: center;
      max-width: 100%;
      margin-inline-end: 4px;
      margin-bottom: 2px;
      overflow: hidden;
      box-sizing: border-box;
    }

    :deep(.j-select-biz-tag-content) {
      flex: 1 1 0 !important;
      min-width: 0 !important;
      display: block !important;
      font-size: 14px;
      overflow: hidden;
      white-space: nowrap !important;
      text-overflow: clip !important;
    }

    :deep(.j-select-biz-tag .ant-select-selection-item-remove) {
      flex-shrink: 0;
    }
    // update-end--author:liaozhiyang---date:20260804---for：【LHZP-602】岗位层级过深时右侧文字完整、左侧省略；多选换行展开
  }
  .detailStr {
    margin: 0;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
