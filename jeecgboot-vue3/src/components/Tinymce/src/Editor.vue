<template>
  <div ref="editorRootRef" :class="prefixCls" :style="{ width: containerWidth }">
    <Teleport v-if="imgUploadShow" :to="targetElem">
      <ImgUpload
        :fullscreen="fullscreen"
        @uploading="handleImageUploading"
        @loading="handleLoading"
        @done="handleDone"
        v-show="editorRef"
        :disabled="disabled"
      />
    </Teleport>
    <Editor :id="tinymceId" ref="elRef" :disabled="disabled" :init="initOptions" :style="{ visibility: 'hidden' }" v-if="!initOptions.inline"></Editor>
    <slot v-else></slot>
    <ProcessMask ref="processMaskRef" :show="showUploadMask"/>
  </div>
</template>

<script lang="ts">
  import type { RawEditorOptions } from 'tinymce';
  import tinymce from 'tinymce/tinymce';
  import Editor from '@tinymce/tinymce-vue'
  import 'tinymce/themes/silver';
  import 'tinymce/icons/default/icons';
  import 'tinymce/models/dom';

  // tinymce插件可按自己的需要进行导入
  // 更多插件参考：https://www.tiny.cloud/docs/plugins/
  import 'tinymce/plugins/fullscreen';
  import 'tinymce/plugins/link';
  import 'tinymce/plugins/lists';
  import 'tinymce/plugins/preview';
  import 'tinymce/plugins/image';
  import { defineComponent, computed, nextTick, ref, unref, watch, onDeactivated, onBeforeUnmount, onMounted } from 'vue';
  import ImgUpload from './ImgUpload.vue';
  import ProcessMask from './ProcessMask.vue';
  import {simpleToolbar, menubar, simplePlugins} from './tinymce';
  import { buildShortUUID } from '/@/utils/uuid';
  import { bindHandlers } from './helper';
  import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { isNumber } from '/@/utils/is';
  import { useLocale } from '/@/locales/useLocale';
  import { useAppStore } from '/@/store/modules/app';
  import { uploadFile } from '/@/api/common/api';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { ThemeEnum } from '/@/enums/appEnum';
  import { defHttp } from "@/utils/http/axios";
  const tinymceProps = {
    options: {
      type: Object as PropType<Partial<RawEditorOptions>>,
      default: {},
    },
    value: {
      type: String,
    },

    toolbar: {
      type: [Array as PropType<string[]>, String],
      default: simpleToolbar,
    },
    plugins: {
      type: [Array as PropType<string[]>, String],
      default: simplePlugins,
    },
    menubar: {
      type: [Object, String],
      default: menubar,
    },
    modelValue: {
      type: String,
    },
    height: {
      type: [Number, String] as PropType<string | number>,
      required: false,
      default: 400,
    },
    width: {
      type: [Number, String] as PropType<string | number>,
      required: false,
      default: 'auto',
    },
    showImageUpload: {
      type: Boolean,
      default: true,
    },
    showUploadMask: {
      type: Boolean,
      default: false,
    },
    //是否聚焦
    autoFocus:{
      type: Boolean,
      default: true,
    }
  };

  export default defineComponent({
    name: 'Tinymce',
    components: { ImgUpload,Editor,ProcessMask },
    inheritAttrs: false,
    props: tinymceProps as any,
    emits: ['change', 'update:modelValue', 'inited', 'init-error'],
    setup(props, { emit, attrs }) {
      console.log("---Tinymce---初始化---")

      const editorRef = ref<Nullable<any>>(null);
      const fullscreen = ref(false);
      const tinymceId = ref<string>(buildShortUUID('tiny-vue'));
      const elRef = ref<Nullable<HTMLElement>>(null);
      const editorRootRef = ref<Nullable<HTMLElement>>(null);
      const processMaskRef = ref<any>(null);
      const imgUploadShow = ref(false);
      const targetElem = ref<null | HTMLDivElement>(null);

      const { prefixCls } = useDesign('tinymce-container');

      const appStore = useAppStore();

      const tinymceContent = computed(() => props.modelValue);

      const containerWidth = computed(() => {
        const width = props.width;
        if (isNumber(width)) {
          return `${width}px`;
        }
        return width;
      });

      const skinName = computed(() => {
        return appStore.getDarkMode === 'light' ? 'jeecg' : 'oxide-dark';
      });

      const langName = computed(() => {
        const lang = useLocale().getLocale.value;
        return ['zh_CN', 'en'].includes(lang) ? lang : 'zh_CN';
      });

      const initOptions = computed(() => {
        const { height, options, toolbar, plugins, menubar } = props;
        let publicPath = import.meta.env.VITE_PUBLIC_PATH || '/';
        // 代码逻辑说明: 【QQYUN-8571】发布路径不以/结尾资源会加载失败
        if (!publicPath.endsWith('/')) {
          publicPath += '/';
        }
        return {
          selector: `#${unref(tinymceId)}`,
          height,
          toolbar,
          menubar: false,
          plugins,
          // 添加以下粘贴相关配置
          paste_data_images: true, // 允许粘贴图片
          paste_as_text: false, // 不以纯文本粘贴
          paste_retain_style_properties: 'all', // 保留所有样式属性
          paste_webkit_styles: 'all', // 保留webkit样式
          paste_merge_formats: true, // 合并格式
          paste_block_drop: true, // 允许拖放粘贴
          paste_preprocess: (plugin, args) => {
            // 可以在这里对粘贴的内容进行预处理
            //console.log('粘贴的内容:', args.content);
          },
          paste_postprocess: (plugin, args) => {
            // 可以在这里对粘贴的内容进行后处理
            //console.log('处理后的内容:', args.node);
          },
          // 放宽内容过滤规则
          valid_elements: '*[*]',
          extended_valid_elements: '*[*]',
          valid_children: '+body[style]',
          allow_conditional_comments: true,
          allow_html_in_named_anchor: true,

          language_url: publicPath + 'resource/tinymce/langs/' + langName.value + '.js',
          language: langName.value,
          branding: false,
          default_link_target: '_blank',
          link_title: false,
          object_resizing: true,
          toolbar_mode: 'sliding',
          // 代码逻辑说明: 【TV360X-416】单表代码生成，表单打开时，会先聚焦富文本组件，并滚动到富文本组件所在的位置---
          auto_focus: props.autoFocus,
          // toolbar_groups: true,
          skin: skinName.value,
          skin_url: publicPath + 'resource/tinymce/skins/ui/' + skinName.value,
          images_upload_handler: (blobInfo, process) =>
            new Promise((resolve, reject) => {
            let params = {
              file: blobInfo.blob(),
              filename: blobInfo.filename(),
              data: { biz: 'jeditor', jeditor: '1' },
            };
            const uploadSuccess = (res) => {
              if (res.success) {
                if (res.message == 'local') {
                  const img = 'data:image/jpeg;base64,' + blobInfo.base64();
                      resolve(img);
                } else {
                  let img = getFileAccessHttpUrl(res.message);
                  resolve(img);
                }
              } else {
                  reject('上传失败！');
              }
            };
            uploadFile(params, uploadSuccess);
        }),
          content_css: publicPath + 'resource/tinymce/skins/ui/' + skinName.value + '/content.min.css',
          ...options,
          setup: (editor: any) => {
            editorRef.value = editor;
            editor.on('init', (e) => initSetup(e));
            //update-begin-author:liusq---date:2025-11-19--for: JHHB-1070 从word复制的表格不能对齐
            // 表格对齐功能
            initTableAlignment(editor);
            //update-end-author:liusq---date:2025-11-19--for: JHHB-1070 从word复制的表格不能对齐
            // 在编辑器内部处理粘贴事件，确保能正确替换选中内容
            editor.on('paste', async (e: any) => {
              try {
                e.preventDefault();
                e.stopPropagation();
                const clipboardData = e.clipboardData as DataTransfer | undefined;
                if (!clipboardData) return false;

                // 先处理剪贴板中的图片（截图/粘贴图片）
                const items = clipboardData.items as DataTransferItemList | undefined;
                if (items && items.length > 0) {
                  const imageFiles: File[] = [];
                  for (let i = 0; i < items.length; i++) {
                    const it = items[i];
                    if (it && it.type && it.type.indexOf('image') !== -1) {
                      const file = it.getAsFile();
                      if (file) imageFiles.push(file);
                    }
                  }
                  // 若存在图片，逐一上传后替换
                  if (imageFiles.length > 0) {
                    for (const file of imageFiles) {
                      await new Promise<void>((resolve) => {
                        const params = {
                          file,
                          filename: file.name || 'pasted-image.png',
                          data: { biz: 'jeditor', jeditor: '1' },
                        };
                        const uploadSuccess = (res: any) => {
                          try {
                            if (res && res.success) {
                              if (res.message === 'local') {
                                const reader = new FileReader();
                                reader.onload = () => {
                                  const base64 = reader.result as string;
                                  editor.selection.setContent(`<img src="${base64}"/>`);
                                  resolve();
                                };
                                reader.readAsDataURL(file);
                              } else {
                                const imgUrl = getFileAccessHttpUrl(res.message);
                                editor.selection.setContent(`<img src="${imgUrl}"/>`);
                                resolve();
                              }
                            } else {
                              resolve();
                            }
                          } catch (_) {
                            resolve();
                          }
                        };
                        uploadFile(params, uploadSuccess);
                      });
                    }
                    return false;
                  }
                }

                // 处理 HTML/纯文本
                const pasteContent = clipboardData.getData('text/html') || clipboardData.getData('text');
                if (!pasteContent) return false;
                
                if (pasteContent.includes('<img')) {
                  const processedHtml = await preprocess(pasteContent);
                  editor.selection.setContent(processedHtml);
                } else {
                  editor.selection.setContent(pasteContent);
                }
              } catch (_) {}
              return false;
            });
          },
        };
      });

      const disabled = computed(() => {
        const { options } = props;
        const getdDisabled = options && Reflect.get(options, 'readonly');
        const editor = unref(editorRef);
        // 代码逻辑说明: 设置disabled，图片上传没有被禁用
        if (editor && editor?.setMode) {
          editor.setMode(getdDisabled || attrs.disabled === true ? 'readonly' : 'design');
        }
        if (attrs.disabled === true) {
          return true;
        }
        return getdDisabled ?? false;
      });

      watch(
        () => attrs.disabled,
        () => {
          const editor = unref(editorRef);
          if (!editor) {
            return;
          }
         editor?.setMode && editor.setMode(attrs.disabled ? 'readonly' : 'design');
        }
      );

      onMountedOrActivated(() => {
        if (!initOptions.value.inline) {
          tinymceId.value = buildShortUUID('tiny-vue');
        }
        nextTick(() => {
          setTimeout(() => {
            initEditor();
          }, 30);
        });
      });

      onBeforeUnmount(() => {
        destory();
      });

      onDeactivated(() => {
        destory();
      });

      function destory() {
        if (tinymce !== null) {
          tinymce?.remove?.(unref(initOptions).selector!);
        }
      }

      function initEditor() {
        const el = unref(elRef);
        if (el && el?.style && el?.style?.visibility) {
          el.style.visibility = '';
        }
        tinymce
          .init(unref(initOptions))
          .then((editor) => {
            changeColor();
            emit('inited', editor);
          })
          .catch((err) => {
            emit('init-error', err);
          });
      }

      function initSetup(e) {
        const editor = unref(editorRef);
        if (!editor) {
          return;
        }
        const value = props.modelValue || '';

        editor.setContent(value);
        bindModelHandlers(editor);
        bindHandlers(e, attrs, unref(editorRef));
      }

      function setValue(editor: Recordable, val: string, prevVal?: string) {
        if (editor && typeof val === 'string' && val !== prevVal && val !== editor.getContent({ format: attrs.outputFormat })) {
          editor.setContent(val);
        }
      }

      function bindModelHandlers(editor: any) {
        const modelEvents = attrs.modelEvents ? attrs.modelEvents : null;
        const normalizedEvents = Array.isArray(modelEvents) ? modelEvents.join(' ') : modelEvents;

        watch(
          () => props.modelValue,
          (val: string, prevVal: string) => {
            setValue(editor, val, prevVal);
          }
        );

        watch(
          () => props.value,
          (val: string, prevVal: string) => {
            setValue(editor, val, prevVal);
          },
          {
            immediate: true,
          }
        );

        editor.on(normalizedEvents ? normalizedEvents : 'change keyup undo redo', () => {
          const content = editor.getContent({ format: attrs.outputFormat });
          emit('update:modelValue', content);
          emit('change', content);
        });

        editor.on('FullscreenStateChanged', (e) => {
          fullscreen.value = e.state;
        });
      }

      function handleImageUploading(name: string) {
        const editor = unref(editorRef);
        if (!editor) {
          return;
        }
        editor.execCommand('mceInsertContent', false, getUploadingImgName(name));
        const content = editor?.getContent() ?? '';
        setValue(editor, content);
      }

      async function handleDone(name: string, url: string) {
        const editor = unref(editorRef);
        if (!editor) {
          return;
        }
        await handleImageUploading(name);
        const content = editor?.getContent() ?? '';
        const val = content?.replace(getUploadingImgName(name), `<img src="${url}"/>`) ?? '';
        setValue(editor, val);
      }

      /**
       * 上传进度计算
       * @param file
       * @param fileList
       */
      function handleLoading(fileLength,showMask){
        if(fileLength && fileLength > 0){
          setTimeout(() => {
              props?.showUploadMask && processMaskRef.value.calcProcess(fileLength)
          },100)
        }else{
           props?.showUploadMask && (processMaskRef.value.showMask = showMask);
        }
      }
      function getUploadingImgName(name: string) {
        return `[uploading:${name}]`;
      }
      // 代码逻辑说明: 【TV360X-35】富文本，图片上传遮挡其他按钮
      let executeCount = 0;
      watch(
        () => props.showImageUpload,
        () => {
          mountElem();
        }
      );
      onMounted(() => {
        mountElem();
      });
      const mountElem = () => {
        if (executeCount > 20) return;
        setTimeout(() => {
          if (targetElem.value) {
            imgUploadShow.value = props.showImageUpload;
          } else {
            const toxToolbar = editorRootRef.value?.querySelector('.tox-toolbar__group');
            if (toxToolbar) {
              const divElem = document.createElement('div');
              divElem.setAttribute('style', `width:64px;height:39px;display:flex;align-items:center;`);
              toxToolbar!.appendChild(divElem);
              targetElem.value = divElem;
              imgUploadShow.value = props.showImageUpload;
              executeCount = 0;
            } else {
              mountElem();
            }
          }
          executeCount++;
        }, 100);
      };
      // 代码逻辑说明: 【QQYUN-8639】暗黑主题适配
      function changeColor() {
        setTimeout(() => {
          const iframe = editorRootRef.value?.querySelector('iframe');
          const body = iframe?.contentDocument?.querySelector('body');
          if (body) {
            if (appStore.getDarkMode == ThemeEnum.DARK) {
              body.style.color = '#fff';
            } else {
              body.style.color = '#000';
            }
          }
        }, 300);
      }
      watch(
        () => appStore.getDarkMode,
        () => {
          changeColor();
        }
      );

      /**
       * 处理图片链接地址
       *
       * @param pasteContent
       */
      async function preprocess(pasteContent) {
        // 正则提取所有img标签（包含src和其他属性，避免丢失样式/alt等）
        const imgTagRegex = /<img([^>]+)src="([^">]+)"([^>]*)/g;

        // 收集所有需要替换的图片信息（索引、原始src、完整标签）
        const imgReplaceList:any = [];
        let match;
        while ((match = imgTagRegex.exec(pasteContent)) !== null) {
          imgReplaceList.push({
            // 完整的img标签（如<img class="xxx" src="xxx" alt="xxx">）
            fullTag: match[0],
            // 原始图片URL
            src: match[2],
            // src前的属性（如class="xxx" ）
            prefix: match[1],
            // src后的属性（如 alt="xxx"）
            suffix: match[3]
          });
        }
   
        // 替换所有图片URL为服务器地址
        let processedContent = pasteContent;
        // 获取当前域名（协议+域名+端口）
        const currentOrigin = window.location.hostname;
        console.log("当前域名："+ currentOrigin)
        // 处理图片
        for (const imgInfo of imgReplaceList) {
          try {
            if(imgInfo.src.startsWith('http')){
              // 判断是否为当前域名下的图片
              let isOwnServer = false;
              try {
                const imgHost = new URL(imgInfo.src, window.location.href).hostname;
                isOwnServer = imgHost === currentOrigin;
                console.log("图片中的域名：" + imgHost)
              } catch (e) {
                // 如果URL解析失败，使用简单的hostname检查作为后备
                isOwnServer = imgInfo.src.includes(window.location.hostname);
                console.log("图片中的域名：" + window.location.hostname)
              }
              if(!isOwnServer){
                // 非当前域名的网络图片，需要上传
                const filename = getFilenameFromUrl(imgInfo.src) || 'pasted-image.jpg';
                let newUrl = await uploadImage(imgInfo.src, filename);
                if(newUrl){
                  const newImgTag = `<img${imgInfo.prefix}src="${newUrl}"${imgInfo.suffix}`;
                  processedContent = processedContent.replace(imgInfo.fullTag, newImgTag);
                }
              }
              // 如果是当前域名下的图片，不做任何处理，保持原样
            }
          } catch (error) {}
        }
        return processedContent;
      }
      
      
      /**
       * 根据路径获取文件名
       * 
       * @param url
       */
      function getFilenameFromUrl(url) {
        return url.split('/').pop().split('?')[0];
      }

      /**
       * 上传文件并返回文件路径
       * 
       * @param fileUrl
       * @param filename
       */
      async function uploadImage(fileUrl, filename) {
        let params = {
          fileUrl: fileUrl,
          filename: filename,
        };
        try {
          let { message } = await defHttp.post({ url: "/sys/common/uploadImgByHttp", params },{ isTransformResponse: false });
          return getFileAccessHttpUrl(message);
        } catch (e) {
          return "";
        }
      }
      //update-begin-author:liusq---date:2025-11-19--for: JHHB-1070 从word复制的表格不能对齐
      /**
       * 处理表格居中
       * @param editor
       */
      function initTableAlignment(editor) {
        // 表格左对齐 - 带表格图标的左对齐
        editor.ui.registry.addIcon('table-align-left', '<svg width="24" height="24"><rect x="2" y="8" width="16" height="8" fill="currentColor"/><path d="M2 4h20v2H2zm0 14h12v2H2z" fill="currentColor"/></svg>');

        // 表格居中对齐 - 带表格图标的居中对齐
        editor.ui.registry.addIcon('table-align-center', '<svg width="24" height="24"><rect x="4" y="8" width="16" height="8" fill="currentColor"/><path d="M2 4h20v2H2zm4 14h12v2H6z" fill="currentColor"/></svg>');

        // 表格右对齐 - 带表格图标的右对齐
        editor.ui.registry.addIcon('table-align-right', '<svg width="24" height="24"><rect x="6" y="8" width="16" height="8" fill="currentColor"/><path d="M2 4h20v2H2zm8 14h12v2H10z" fill="currentColor"/></svg>');

        // 添加表格专用对齐按钮
        editor.ui.registry.addButton('tablealignleft', {
          tooltip: '表格左对齐',
          icon: 'table-align-left',
          onAction: () => {
            const table = getSelectedTable(editor);
            if (table) alignTable(editor, table, 'JustifyLeft');
          }
        });
        editor.ui.registry.addButton('tablealigncenter', {
          tooltip: '表格居中',
          icon: 'table-align-center',
          onAction: () => {
            const table = getSelectedTable(editor);
            if (table) alignTable(editor, table, 'JustifyCenter');
          }
        });

        editor.ui.registry.addButton('tablealignright', {
          tooltip: '表格右对齐',
          icon: 'table-align-right',
          onAction: () => {
            const table = getSelectedTable(editor);
            if (table) alignTable(editor, table, 'JustifyRight');
          }
        });
      }

      // 获取当前选中的表格
      function getSelectedTable(editor) {
        const selectedNode = editor.selection.getNode();
        return selectedNode.closest('table');
      }

      // 使用包装器的表格对齐函数
      function alignTable(editor, table, command) {
        // 检查表格是否已经被包装
        let wrapper = table.parentNode;
        const isWrapped = wrapper && wrapper.classList && wrapper.classList.contains('table-wrapper');

        switch (command) {
          case 'JustifyLeft':
          case 'JustifyRight':
            // 对于左右对齐，移除包装器（如果有）
            if (isWrapped) {
              editor.dom.remove(wrapper, true); // 移除包装器但保留表格
            }
            // 直接设置表格的浮动
            editor.dom.setStyle(table, 'float', command === 'JustifyLeft' ? 'left' : 'right');
            editor.dom.setStyle(table, 'margin', '0');
            break;

          case 'JustifyCenter':
            // 对于居中对齐，使用包装器
            if (!isWrapped) {
              // 创建包装器
              wrapper = editor.dom.create('div', {
                'class': 'table-wrapper',
                style: 'text-align: center; margin: 0 auto;'
              });

              // 包装表格
              table.parentNode.insertBefore(wrapper, table);
              wrapper.appendChild(table);
            }

            // 设置包装器样式
            editor.dom.setStyles(wrapper, {
              'text-align': 'center',
              'margin': '0 auto',
              'display': 'block'
            });
            // 移除表格的浮动和边距
            editor.dom.setStyles(table, {
              'float': '',
              'margin': '0',
              'display': 'inline-table' // 保持表格显示特性
            });
            break;

          case 'JustifyFull':
            // 移除包装器（如果有）
            if (isWrapped) {
              editor.dom.remove(wrapper, true);
            }
            editor.dom.setStyle(table, 'width', '100%');
            editor.dom.setStyle(table, 'float', '');
            break;
        }

        // 触发变更事件
        editor.fire('change');
        editor.undoManager.add();
      }
      //update-end-author:liusq---date:2025-11-19--for: JHHB-1070 从word复制的表格不能对齐 

      return {
        prefixCls,
        containerWidth,
        initOptions,
        tinymceContent,
        elRef,
        tinymceId,
        handleImageUploading,
        handleDone,
        editorRef,
        fullscreen,
        disabled,
        editorRootRef,
        imgUploadShow,
        targetElem,

        handleLoading,
        processMaskRef,
      };
    },
  });
</script>

<style lang="less" scoped>
/* 表格包装器样式 */
.table-wrapper {
  text-align: center;
  margin: 0 auto;
  display: block;
}

/* 确保在编辑器中正确显示 */
.tox-edit-area__iframe {
  .table-wrapper {
    text-align: center !important;
    margin: 0 auto !important;
    display: block !important;
  }

  .table-wrapper table {
    display: inline-table !important;
    margin: 0 !important;
    float: none !important;
  }
}
</style>

<style lang="less">
  @prefix-cls: ~'@{namespace}-tinymce-container';

  .@{prefix-cls} {
    position: relative;
    line-height: normal;

    textarea {
      z-index: -1;
      visibility: hidden;
    }
    .tox:not(.tox-tinymce-inline) .tox-editor-header {
      padding:0;
    }
    // 代码逻辑说明: 【TV360X-329】富文本禁用状态下工具栏划过边框丢失
    .tox .tox-tbtn--disabled,
    .tox .tox-tbtn--disabled:hover,
    .tox .tox-tbtn:disabled,
    .tox .tox-tbtn:disabled:hover {
      background-image: url("data:image/svg+xml;charset=utf8,%3Csvg height='39px' viewBox='0 0 40 39px' width='40' xmlns='http://www.w3.org/2000/svg'%3E%3Crect x='0' y='38px' width='100' height='1' fill='%23d9d9d9'/%3E%3C/svg%3E");
      background-position: left 0;
    }
  }

  html[data-theme='dark'] {
    .@{prefix-cls} {
      .tox .tox-edit-area__iframe {background-color: #141414;}
    }
  }
</style>
