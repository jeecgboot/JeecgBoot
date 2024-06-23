import type { AppRouteModule } from '/@/router/types';

import { getParentLayout, LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const comp: AppRouteModule = {
  path: '/comp',
  name: 'Comp',
  component: LAYOUT,
  redirect: '/comp/basic',
  meta: {
    orderNo: 30,
    icon: 'ion:layers-outline',
    title: t('routes.demo.comp.comp'),
  },

  children: [
    {
      path: 'jeecg',
      name: 'JeecgDemo',
      redirect: '/comp/jeecg/basic',
      component: getParentLayout('JeecgDemo'),
      meta: {
        title: t('routes.demo.comp.jeecg'),
      },
      children: [
        {
          path: 'basic',
          name: 'JAreaLinkage',
          component: () => import('/@/views/demo/jeecg/JeecgComponents.vue'),
          meta: {
            title: t('routes.demo.jeecg.JAreaLinkage'),
          },
        },
        {
          path: 'oneToMore',
          name: 'oneToMoreDemo',
          component: () => import('/@/views/demo/vextable/index.vue'),
          meta: {
            title: t('routes.demo.comp.oneToMore'),
          },
        },
      ],
    },
    {
      path: 'basic',
      name: 'BasicDemo',
      component: getParentLayout('BasicDemo'),
      meta: {
        title: t('routes.demo.comp.basic'),
      },
      children: [
        {
          path: 'button',
          name: 'ButtonDemo',
          component: () => import('/@/views/demo/comp/button/index.vue'),
          meta: {
            title: t('routes.demo.basic.button'),
          },
        },
        {
          path: 'icon',
          name: 'IconDemo',
          component: () => import('/@/views/demo/feat/icon/index.vue'),
          meta: {
            title: t('routes.demo.feat.icon'),
          },
        },
        {
          path: 'msg',
          name: 'MsgDemo',
          component: () => import('/@/views/demo/feat/msg/index.vue'),
          meta: {
            title: t('routes.demo.feat.msg'),
          },
        },
        {
          path: 'tabs',
          name: 'TabsDemo',
          component: () => import('/@/views/demo/feat/tabs/index.vue'),
          meta: {
            title: t('routes.demo.feat.tabs'),
            hideChildrenInMenu: true,
          },
          children: [
            {
              path: 'detail/:id',
              name: 'TabDetail',
              component: () => import('/@/views/demo/feat/tabs/TabDetail.vue'),
              meta: {
                currentActiveMenu: '/comp/basic/tabs',
                title: t('routes.demo.feat.tabDetail'),
                hideMenu: true,
                dynamicLevel: 3,
                realPath: '/comp/basic/tabs/detail',
              },
            },
          ],
        },
      ],
    },

    {
      path: 'form',
      name: 'FormDemo',
      redirect: '/comp/form/basic',
      component: getParentLayout('FormDemo'),
      meta: {
        // icon: 'mdi:form-select',
        title: t('routes.demo.form.form'),
      },
      children: [
        {
          path: 'basic',
          name: 'FormBasicDemo',
          component: () => import('/@/views/demo/form/index.vue'),
          meta: {
            title: t('routes.demo.form.basic'),
          },
        },
        {
          path: 'useForm',
          name: 'UseFormDemo',
          component: () => import('/@/views/demo/form/UseForm.vue'),
          meta: {
            title: t('routes.demo.form.useForm'),
          },
        },
        {
          path: 'refForm',
          name: 'RefFormDemo',
          component: () => import('/@/views/demo/form/RefForm.vue'),
          meta: {
            title: t('routes.demo.form.refForm'),
          },
        },
        {
          path: 'advancedForm',
          name: 'AdvancedFormDemo',
          component: () => import('/@/views/demo/form/AdvancedForm.vue'),
          meta: {
            title: t('routes.demo.form.advancedForm'),
          },
        },
        {
          path: 'ruleForm',
          name: 'RuleFormDemo',
          component: () => import('/@/views/demo/form/RuleForm.vue'),
          meta: {
            title: t('routes.demo.form.ruleForm'),
          },
        },
        {
          path: 'dynamicForm',
          name: 'DynamicFormDemo',
          component: () => import('/@/views/demo/form/DynamicForm.vue'),
          meta: {
            title: t('routes.demo.form.dynamicForm'),
          },
        },
        {
          path: 'customerForm',
          name: 'CustomerFormDemo',
          component: () => import('/@/views/demo/form/CustomerForm.vue'),
          meta: {
            title: t('routes.demo.form.customerForm'),
          },
        },
        {
          path: 'appendForm',
          name: 'appendFormDemo',
          component: () => import('/@/views/demo/form/AppendForm.vue'),
          meta: {
            title: t('routes.demo.form.appendForm'),
          },
        },
      ],
    },
    {
      path: 'table',
      name: 'TableDemo',
      redirect: '/comp/table/basic',
      component: getParentLayout('TableDemo'),
      meta: {
        // icon: 'carbon:table-split',
        title: t('routes.demo.table.table'),
      },

      children: [
        {
          path: 'basic',
          name: 'TableBasicDemo',
          component: () => import('/@/views/demo/table/Basic.vue'),
          meta: {
            title: t('routes.demo.table.basic'),
          },
        },
        {
          path: 'treeTable',
          name: 'TreeTableDemo',
          component: () => import('/@/views/demo/table/TreeTable.vue'),
          meta: {
            title: t('routes.demo.table.treeTable'),
          },
        },
        {
          path: 'fetchTable',
          name: 'FetchTableDemo',
          component: () => import('/@/views/demo/table/FetchTable.vue'),
          meta: {
            title: t('routes.demo.table.fetchTable'),
          },
        },
        {
          path: 'fixedColumn',
          name: 'FixedColumnDemo',
          component: () => import('/@/views/demo/table/FixedColumn.vue'),
          meta: {
            title: t('routes.demo.table.fixedColumn'),
          },
        },
        {
          path: 'customerCell',
          name: 'CustomerCellDemo',
          component: () => import('/@/views/demo/table/CustomerCell.vue'),
          meta: {
            title: t('routes.demo.table.customerCell'),
          },
        },
        {
          path: 'formTable',
          name: 'FormTableDemo',
          component: () => import('/@/views/demo/table/FormTable.vue'),
          meta: {
            title: t('routes.demo.table.formTable'),
          },
        },
        {
          path: 'useTable',
          name: 'UseTableDemo',
          component: () => import('/@/views/demo/table/UseTable.vue'),
          meta: {
            title: t('routes.demo.table.useTable'),
          },
        },
        {
          path: 'refTable',
          name: 'RefTableDemo',
          component: () => import('/@/views/demo/table/RefTable.vue'),
          meta: {
            title: t('routes.demo.table.refTable'),
          },
        },
        {
          path: 'multipleHeader',
          name: 'MultipleHeaderDemo',
          component: () => import('/@/views/demo/table/MultipleHeader.vue'),
          meta: {
            title: t('routes.demo.table.multipleHeader'),
          },
        },
        {
          path: 'mergeHeader',
          name: 'MergeHeaderDemo',
          component: () => import('/@/views/demo/table/MergeHeader.vue'),
          meta: {
            title: t('routes.demo.table.mergeHeader'),
          },
        },
        {
          path: 'nestedTable',
          name: 'nestedTableDemo',
          component: () => import('/@/views/demo/table/NestedTable.vue'),
          meta: {
            title: t('routes.demo.table.nestedTable'),
          },
        },
        {
          path: 'expandTable',
          name: 'ExpandTableDemo',
          component: () => import('/@/views/demo/table/ExpandTable.vue'),
          meta: {
            title: t('routes.demo.table.expandTable'),
          },
        },
        {
          path: 'fixedHeight',
          name: 'FixedHeightDemo',
          component: () => import('/@/views/demo/table/FixedHeight.vue'),
          meta: {
            title: t('routes.demo.table.fixedHeight'),
          },
        },
        {
          path: 'footerTable',
          name: 'FooterTableDemo',
          component: () => import('/@/views/demo/table/FooterTable.vue'),
          meta: {
            title: t('routes.demo.table.footerTable'),
          },
        },
        {
          path: 'editCellTable',
          name: 'EditCellTableDemo',
          component: () => import('/@/views/demo/table/EditCellTable.vue'),
          meta: {
            title: t('routes.demo.table.editCellTable'),
          },
        },
        {
          path: 'editRowTable',
          name: 'EditRowTableDemo',
          component: () => import('/@/views/demo/table/EditRowTable.vue'),
          meta: {
            title: t('routes.demo.table.editRowTable'),
          },
        },
        {
          path: 'authColumn',
          name: 'AuthColumnDemo',
          component: () => import('/@/views/demo/table/AuthColumn.vue'),
          meta: {
            title: t('routes.demo.table.authColumn'),
          },
        },
      ],
    },
    {
      path: 'modal',
      name: 'ModalDemo',
      redirect: '/comp/modal/basic',
      component: getParentLayout('ModalDemo'),
      meta: {
        title: t('routes.demo.comp.modal'),
      },
      children: [
        {
          path: 'basic',
          name: 'ModalBasicDemo',
          component: () => import('/@/views/demo/comp/modal/index.vue'),
          meta: {
            title: t('routes.demo.comp.modal.basic'),
          },
        },
        {
          path: 'drawer',
          name: 'DrawerDemo',
          component: () => import('/@/views/demo/comp/drawer/index.vue'),
          meta: {
            title: t('routes.demo.comp.modal.drawer'),
          },
        },
      ],
    },

    {
      path: 'third',
      name: 'ThirdDemo',
      redirect: '/comp/third/basic',
      component: getParentLayout('ModalDemo'),
      meta: {
        title: t('routes.demo.comp.third'),
      },
      children: [
        {
          path: 'basic',
          name: 'CropperDemo',
          component: () => import('/@/views/demo/comp/cropper/index.vue'),
          meta: {
            title: t('routes.demo.comp.cropperImage'),
          },
        },
        {
          path: 'qrcode',
          name: 'QrCodeDemo',
          component: () => import('/@/views/demo/comp/qrcode/index.vue'),
          meta: {
            title: t('routes.demo.comp.qrcode'),
          },
        },
        {
          path: 'strength-meter',
          name: 'StrengthMeterDemo',
          component: () => import('/@/views/demo/comp/strength-meter/index.vue'),
          meta: {
            title: t('routes.demo.comp.strength'),
          },
        },
        {
          path: 'upload',
          name: 'UploadDemo',
          component: () => import('/@/views/demo/comp/upload/index.vue'),
          meta: {
            title: t('routes.demo.comp.upload'),
          },
        },
        {
          path: 'loading',
          name: 'LoadingDemo',
          component: () => import('/@/views/demo/comp/loading/index.vue'),
          meta: {
            title: t('routes.demo.comp.loading'),
          },
        },
        {
          path: 'timestamp',
          name: 'TimeDemo',
          component: () => import('/@/views/demo/comp/time/index.vue'),
          meta: {
            title: t('routes.demo.comp.time'),
          },
        },
        {
          path: 'countTo',
          name: 'CountTo',
          component: () => import('/@/views/demo/comp/count-to/index.vue'),
          meta: {
            title: t('routes.demo.comp.countTo'),
          },
        },
        {
          path: 'transition',
          name: 'transitionDemo',
          component: () => import('/@/views/demo/comp/transition/index.vue'),
          meta: {
            title: t('routes.demo.comp.transition'),
          },
        },
        {
          path: 'print',
          name: 'Print',
          component: () => import('/@/views/demo/feat/print/index.vue'),
          meta: {
            title: t('routes.demo.feat.print'),
          },
        },
        {
          path: 'img-preview',
          name: 'ImgPreview',
          component: () => import('/@/views/demo/feat/img-preview/index.vue'),
          meta: {
            title: t('routes.demo.feat.imgPreview'),
          },
        },
        {
          path: 'download',
          name: 'DownLoadDemo',
          component: () => import('/@/views/demo/feat/download/index.vue'),
          meta: {
            title: t('routes.demo.feat.download'),
          },
        },
        {
          path: 'click-out-side',
          name: 'ClickOutSideDemo',
          component: () => import('/@/views/demo/feat/click-out-side/index.vue'),
          meta: {
            title: t('routes.demo.feat.clickOutSide'),
          },
        },
        {
          path: 'copy',
          name: 'CopyDemo',
          component: () => import('/@/views/demo/feat/copy/index.vue'),
          meta: {
            title: t('routes.demo.feat.copy'),
          },
        },
        {
          path: 'ripple',
          name: 'RippleDemo',
          component: () => import('/@/views/demo/feat/ripple/index.vue'),
          meta: {
            title: t('routes.demo.feat.ripple'),
          },
        },
      ],
    },
    {
      path: 'tree',
      name: 'TreeDemo',
      redirect: '/comp/tree/basic',
      component: getParentLayout('TreeDemo'),
      meta: {
        // icon: 'clarity:tree-view-line',
        title: t('routes.demo.comp.tree'),
      },
      children: [
        {
          path: 'basic',
          name: 'BasicTreeDemo',
          component: () => import('/@/views/demo/tree/index.vue'),
          meta: {
            title: t('routes.demo.comp.treeBasic'),
          },
        },
        {
          path: 'editTree',
          name: 'EditTreeDemo',
          component: () => import('/@/views/demo/tree/EditTree.vue'),
          meta: {
            title: t('routes.demo.comp.editTree'),
          },
        },
        {
          path: 'actionTree',
          name: 'ActionTreeDemo',
          component: () => import('/@/views/demo/tree/ActionTree.vue'),
          meta: {
            title: t('routes.demo.comp.actionTree'),
          },
        },
      ],
    },
    {
      path: 'editor',
      name: 'EditorDemo',
      redirect: '/comp/editor/markdown',
      component: getParentLayout('EditorDemo'),
      meta: {
        // icon: 'carbon:table-split',
        title: t('routes.demo.editor.editor'),
      },
      children: [
        {
          path: 'json',
          component: () => import('/@/views/demo/editor/json/index.vue'),
          name: 'JsonEditorDemo',
          meta: {
            title: t('routes.demo.editor.jsonEditor'),
          },
        },
        {
          path: 'markdown',
          component: getParentLayout('MarkdownDemo'),
          name: 'MarkdownDemo',
          meta: {
            title: t('routes.demo.editor.markdown'),
          },
          redirect: '/comp/editor/markdown/index',
          children: [
            {
              path: 'index',
              name: 'MarkDownBasicDemo',
              component: () => import('/@/views/demo/editor/markdown/index.vue'),
              meta: {
                title: t('routes.demo.editor.tinymceBasic'),
              },
            },
            {
              path: 'editor',
              name: 'MarkDownFormDemo',
              component: () => import('/@/views/demo/editor/markdown/Editor.vue'),
              meta: {
                title: t('routes.demo.editor.tinymceForm'),
              },
            },
          ],
        },

        {
          path: 'tinymce',
          component: getParentLayout('TinymceDemo'),
          name: 'TinymceDemo',
          meta: {
            title: t('routes.demo.editor.tinymce'),
          },
          redirect: '/comp/editor/tinymce/index',
          children: [
            {
              path: 'index',
              name: 'TinymceBasicDemo',
              component: () => import('/@/views/demo/editor/tinymce/index.vue'),
              meta: {
                title: t('routes.demo.editor.tinymceBasic'),
              },
            },
            {
              path: 'editor',
              name: 'TinymceFormDemo',
              component: () => import('/@/views/demo/editor/tinymce/Editor.vue'),
              meta: {
                title: t('routes.demo.editor.tinymceForm'),
              },
            },
          ],
        },
      ],
    },
    {
      path: 'scroll',
      name: 'ScrollDemo',
      redirect: '/comp/scroll/basic',
      component: getParentLayout('ScrollDemo'),
      meta: {
        title: t('routes.demo.comp.scroll'),
      },
      children: [
        {
          path: 'basic',
          name: 'BasicScrollDemo',
          component: () => import('/@/views/demo/comp/scroll/index.vue'),
          meta: {
            title: t('routes.demo.comp.scrollBasic'),
          },
        },
        {
          path: 'action',
          name: 'ActionScrollDemo',
          component: () => import('/@/views/demo/comp/scroll/Action.vue'),
          meta: {
            title: t('routes.demo.comp.scrollAction'),
          },
        },
        {
          path: 'virtualScroll',
          name: 'VirtualScrollDemo',
          component: () => import('/@/views/demo/comp/scroll/VirtualScroll.vue'),
          meta: {
            title: t('routes.demo.comp.virtualScroll'),
          },
        },
      ],
    },

    {
      path: 'desc',
      name: 'DescDemo',
      component: () => import('/@/views/demo/comp/desc/index.vue'),
      meta: {
        title: t('routes.demo.comp.desc'),
      },
    },

    {
      path: 'lazy',
      name: 'LazyDemo',
      component: getParentLayout('LazyDemo'),
      redirect: '/comp/lazy/basic',
      meta: {
        title: t('routes.demo.comp.lazy'),
      },
      children: [
        {
          path: 'basic',
          name: 'BasicLazyDemo',
          component: () => import('/@/views/demo/comp/lazy/index.vue'),
          meta: {
            title: t('routes.demo.comp.lazyBasic'),
          },
        },
        {
          path: 'transition',
          name: 'BasicTransitionDemo',
          component: () => import('/@/views/demo/comp/lazy/Transition.vue'),
          meta: {
            title: t('routes.demo.comp.lazyTransition'),
          },
        },
      ],
    },
    {
      path: 'verify',
      name: 'VerifyDemo',
      component: getParentLayout('VerifyDemo'),
      redirect: '/comp/verify/drag',
      meta: {
        title: t('routes.demo.comp.verify'),
      },
      children: [
        {
          path: 'drag',
          name: 'VerifyDragDemo',
          component: () => import('/@/views/demo/comp/verify/index.vue'),
          meta: {
            title: t('routes.demo.comp.verifyDrag'),
          },
        },
        {
          path: 'rotate',
          name: 'VerifyRotateDemo',
          component: () => import('/@/views/demo/comp/verify/Rotate.vue'),
          meta: {
            title: t('routes.demo.comp.verifyRotate'),
          },
        },
      ],
    },
  ],
};

export default comp;
