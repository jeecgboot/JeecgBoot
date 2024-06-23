<template>
  <Modal v-bind="getBindValue" @cancel="handleCancel">
    <template #closeIcon v-if="!$slots.closeIcon">
      <ModalClose :canFullscreen="getProps.canFullscreen" :fullScreen="fullScreenRef" :commentSpan="commentSpan" :enableComment="getProps.enableComment" @comment="handleComment" @cancel="handleCancel" @fullscreen="handleFullScreen" />
    </template>

    <template #title v-if="!isNoTitle">
      <ModalHeader :helpMessage="getProps.helpMessage" :title="getMergeProps.title" @dblclick="handleTitleDbClick" />
    </template>

    <template #footer v-if="!$slots.footer">
      <ModalFooter v-bind="getBindValue" @ok="handleOk" @cancel="handleCancel">
        <template #[item]="data" v-for="item in Object.keys($slots)">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
      </ModalFooter>
    </template>

    <!-- update-begin-author:taoyan date:2022-7-18 for:  modal弹窗 支持评论 slot -->
    <a-row class="jeecg-modal-wrapper">
      <a-col :span="24-commentSpan" class="jeecg-modal-content">
        <ModalWrapper
          :useWrapper="getProps.useWrapper"
          :footerOffset="wrapperFooterOffset"
          :fullScreen="fullScreenRef"
          ref="modalWrapperRef"
          :loading="getProps.loading"
          :loading-tip="getProps.loadingTip"
          :minHeight="getProps.minHeight"
          :maxHeight="getProps.maxHeight"
          :height="getWrapperHeight"
          :visible="visibleRef"
          :modalFooterHeight="footer !== undefined && !footer ? 0 : undefined"
          v-bind="omit(getProps.wrapperProps, 'visible', 'height', 'modalFooterHeight')"
          @ext-height="handleExtHeight"
          @height-change="handleHeightChange">
          <slot></slot>
        </ModalWrapper>
      </a-col>
      
      <a-col :span="commentSpan" class="jeecg-comment-outer">
        <slot name="comment"></slot>
      </a-col>
      
    </a-row>
    <!-- update-end-author:taoyan date:2022-7-18 for:  modal弹窗 支持评论 slot -->
    
    <template #[item]="data" v-for="item in Object.keys(omit($slots, 'default'))">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </Modal>
</template>
<script lang="ts">
  import type { ModalProps, ModalMethods } from './typing';

  import { defineComponent, computed, ref, watch, unref, watchEffect, toRef, getCurrentInstance, nextTick } from 'vue';
  import Modal from './components/Modal';
  import ModalWrapper from './components/ModalWrapper.vue';
  import ModalClose from './components/ModalClose.vue';
  import ModalFooter from './components/ModalFooter.vue';
  import ModalHeader from './components/ModalHeader.vue';
  import { isFunction } from '/@/utils/is';
  import { deepMerge } from '/@/utils';
  import { basicProps } from './props';
  import { useFullScreen } from './hooks/useModalFullScreen';
  import { omit } from 'lodash-es';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useAppInject } from '/@/hooks/web/useAppInject';


  export default defineComponent({
    name: 'BasicModal',
    components: { Modal, ModalWrapper, ModalClose, ModalFooter, ModalHeader },
    inheritAttrs: false,
    props: basicProps,
    emits: ['visible-change', 'open-change', 'height-change', 'cancel', 'ok', 'register', 'update:visible', 'update:open', 'fullScreen','comment-open'],
    setup(props, { emit, attrs , slots}) {
      const visibleRef = ref(false);
      const propsRef = ref<Partial<ModalProps> | null>(null);
      const modalWrapperRef = ref<any>(null);
      const { prefixCls } = useDesign('basic-modal');
      // modal   Bottom and top height
      const extHeightRef = ref(0);
      const modalMethods: ModalMethods = {
        setModalProps,
        emitVisible: undefined,
        redoModalHeight: () => {
          nextTick(() => {
            if (unref(modalWrapperRef)) {
              (unref(modalWrapperRef) as any).setModalHeight();
            }
          });
        },
      };

      const instance = getCurrentInstance();
      if (instance) {
        emit('register', modalMethods, instance.uid);
      }
      const { getIsMobile } = useAppInject();

      // Custom title component: get title
      const getMergeProps = computed((): Recordable => {
        const result = {
          ...props,
          ...(unref(propsRef) as any),
        };
        // update-begin--author:liaozhiyang---date:20240326---for：【QQYUN-8643】弹窗移动端弹窗统一全屏
        if (getIsMobile.value) {
          result.canFullscreen = false;
          result.defaultFullscreen = true;
        }
        // update-end--author:liaozhiyang---date:20240326---for：【QQYUN-8643】弹窗移动端弹窗统一全屏
        return result;
      });
        //update-begin-author:liusq date:2023-05-25 for:【issues/4856】Modal控件设置 :title = null 无效
        //是否未设置标题
        const isNoTitle = computed(() => {
            //标题为空并且不含有标题插槽
            return !unref(getMergeProps).title && !slots.title;
        });
        //update-end-author:liusq date:2023-05-25 for:【issues/4856】Modal控件设置 :title = null 无效

      const { handleFullScreen, getWrapClassName, fullScreenRef } = useFullScreen({
        modalWrapperRef,
        extHeightRef,
        wrapClassName: toRef(getMergeProps.value, 'wrapClassName'),
      });

      // modal component does not need title and origin buttons
      const getProps = computed((): Recordable => {
        const opt = {
          ...unref(getMergeProps),
          visible: unref(visibleRef),
          okButtonProps: undefined,
          cancelButtonProps: undefined,
          title: undefined,
        };
        return {
          ...opt,
          wrapClassName: unref(getWrapClassName),
        };
      });

      const getBindValue = computed((): Recordable => {
        // update-begin--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
        const attr = {
          ...attrs,
          ...unref(getMergeProps),
          open: unref(visibleRef),
          wrapClassName: unref(getWrapClassName),
        };
        if (unref(fullScreenRef)) {
          return omit(attr, ['height', 'title', 'visible']);
        }
        return omit(attr, ['title', 'visible']);
        // update-end--author:liaozhiyang---date:20231218---for：【QQYUN-6366】升级到antd4.x
      });

      const getWrapperHeight = computed(() => {
        if (unref(fullScreenRef)) return undefined;
        return unref(getProps).height;
      });

      watchEffect(() => {
        fullScreenRef.value = !!props.defaultFullscreen;
        // update-begin--author:liaozhiyang---date:20240326---for：【QQYUN-8643】弹窗移动端弹窗统一全屏
        if (getIsMobile.value) {
          fullScreenRef.value = true
        }
        // update-end--author:liaozhiyang---date:20240326---for：【QQYUN-8643】弹窗移动端弹窗统一全屏
      });

      watchEffect(() => {
        visibleRef.value = !!props.visible;
      });

      watchEffect(() => {
        visibleRef.value = !!props.open;
      });

      watch(
        () => unref(visibleRef),
        (v) => {
          emit('visible-change', v);
          emit('open-change', v);
          emit('update:visible', v);
          emit('update:open', v);
          instance && modalMethods.emitVisible?.(v, instance.uid);
          nextTick(() => {
            if (props.scrollTop && v && unref(modalWrapperRef)) {
              (unref(modalWrapperRef) as any).scrollTop();
            }
          });
        },
        {
          immediate: false,
        }
      );

      // 取消事件
      async function handleCancel(e: Event) {
        e?.stopPropagation();
        // 过滤自定义关闭按钮的空白区域
        if ((e.target as HTMLElement)?.classList?.contains(prefixCls + '-close--custom')) return;
        if (props.closeFunc && isFunction(props.closeFunc)) {
          const isClose: boolean = await props.closeFunc();
          visibleRef.value = !isClose;
          return;
        }

        visibleRef.value = false;
        emit('cancel', e);
      }

      /**
       * @description: 设置modal参数
       */
      function setModalProps(props: Partial<ModalProps>): void {
        // Keep the last setModalProps
        propsRef.value = deepMerge(unref(propsRef) || ({} as any), props);
        if (Reflect.has(props, 'visible')) {
          visibleRef.value = !!props.visible;
        }
        if (Reflect.has(props, 'open')) {
          visibleRef.value = !!props.open;
        }
        if (Reflect.has(props, 'defaultFullscreen')) {
          fullScreenRef.value = !!props.defaultFullscreen;
           // update-begin--author:liaozhiyang---date:20240326---for：【QQYUN-8643】弹窗移动端弹窗统一全屏
          if (getIsMobile.value) {
            fullScreenRef.value = true
          }
          // update-end--author:liaozhiyang---date:20240326---for：【QQYUN-8643】弹窗移动端弹窗统一全屏
        }
      }

      function handleOk(e: Event) {
        emit('ok', e);
      }

      function handleHeightChange(height: string) {
        emit('height-change', height);
      }

      function handleExtHeight(height: number) {
        extHeightRef.value = height;
      }

      function handleTitleDbClick(e) {
        if (!props.canFullscreen) return;
        e.stopPropagation();
        handleFullScreen(e);
      }

      //update-begin-author:taoyan date:2022-7-18 for: modal支持评论 slot
      const commentSpan = ref(0);
      watch(()=>props.enableComment, (flag)=>{
        handleComment(flag)
      }, {immediate:true});
      function handleComment(flag){
        if(flag=== true){
          commentSpan.value = 6
        }else{
          commentSpan.value = 0
        }
        // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-485】开启评论之后弹窗按钮居右隔一个评论的距离
        emit('comment-open', commentSpan.value === 0, commentSpan.value);
        // update-end--author:liaozhiyang---date:20240528---for：【TV360X-485】开启评论之后弹窗按钮居右隔一个评论的距离
      }
      //update-end-author:taoyan date:2022-7-18 for: modal支持评论 slot

      // update-begin--author:liaozhiyang---date:20230804---for：【QQYUN-5866】放大行数自适应
      watch(fullScreenRef,(val)=>{
        emit('fullScreen',val);
      });
      // update-begin--author:liaozhiyang---date:20230804---for：【QQYUN-5866】放大行数自适应

      return {
        handleCancel,
        getBindValue,
        getProps,
        handleFullScreen,
        fullScreenRef,
        getMergeProps,
        handleOk,
        visibleRef,
        omit,
        modalWrapperRef,
        handleExtHeight,
        handleHeightChange,
        handleTitleDbClick,
        getWrapperHeight,
        commentSpan,
        handleComment,
        isNoTitle
      };
    },
  });
</script>
<style lang="less">
  /*update-begin-author:taoyan date:2022-7-27 for:modal评论区域样式*/
  .jeecg-comment-outer {
    border-left: 1px solid #f0f0f0;
    .ant-tabs-nav-wrap{
    /*  text-align: center;*/
    }
  }
  .jeecg-modal-content{
    >.scroll-container{
      //update-begin---author:wangshuai---date:2023-12-05---for:【QQYUN-7297】表单讨论弹窗放大按钮时只显示一部分---
      padding: 6px;
      //update-end---author:wangshuai---date:2023-12-05---for:【QQYUN-7297】表单讨论弹窗放大按钮时只显示一部分---
    }
  }
  /*update-end-author:taoyan date:2022-7-27 for:modal评论区域样式*/

  // wrapper设为100%，兼容之前写过的弹窗自定义样式
  .jeecg-modal-wrapper,
  .jeecg-modal-content {
    height: 100%;
  }
  html[data-theme='dark'] {
    .jeecg-comment-outer {
      border-left: 1px solid rgba(253, 253, 253, 0.12);
    }
  }
</style>
