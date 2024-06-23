<template>
  <div :class="[prefixCls, { fullscreen }]">
    <Upload
      name="file"
      multiple
      @change="handleChange"
      :action="uploadUrl"
      :showUploadList="false"
      :data="getBizData()"
      :headers="getheader()"
      accept=".jpg,.jpeg,.gif,.png,.webp"
    >
      <a-button type="primary" v-bind="{ ...getButtonProps }">
        {{ t('component.upload.imgUpload') }}
      </a-button>
    </Upload>
  </div>
</template>
<script lang="ts">
  import { defineComponent, computed } from 'vue';

  import { Upload } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useGlobSetting } from '/@/hooks/setting';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { getToken } from '/@/utils/auth';
  import { getFileAccessHttpUrl, getHeaders } from '/@/utils/common/compUtils';

  export default defineComponent({
    name: 'TinymceImageUpload',
    components: { Upload },
    props: {
      fullscreen: {
        type: Boolean,
      },
      disabled: {
        type: Boolean,
        default: false,
      },
    },
    emits: ['uploading', 'done', 'error'],
    setup(props, { emit }) {
      let uploading = false;

      //update-begin-author:taoyan date:2022-5-13 for: 富文本上传图片不支持
      function getheader() {
        return getHeaders();
      }

      function getBizData() {
        return {
          biz: 'jeditor',
          jeditor: '1',
        };
      }
      const { domainUrl } = useGlobSetting();
      const uploadUrl = domainUrl + '/sys/common/upload';
      //update-end-author:taoyan date:2022-5-13 for: 富文本上传图片不支持
      const { t } = useI18n();
      const { prefixCls } = useDesign('tinymce-img-upload');

      const getButtonProps = computed(() => {
        const { disabled } = props;
        return {
          disabled,
        };
      });

      function handleChange(info: Recordable) {
        const file = info.file;
        const status = file?.status;
        //const url = file?.response?.url;
        const name = file?.name;

        if (status === 'uploading') {
          if (!uploading) {
            emit('uploading', name);
            uploading = true;
          }
        } else if (status === 'done') {
          //update-begin-author:taoyan date:2022-5-13 for: 富文本上传图片不支持
          let realUrl = getFileAccessHttpUrl(file.response.message);
          emit('done', name, realUrl);
          //update-end-author:taoyan date:2022-5-13 for: 富文本上传图片不支持
          uploading = false;
        } else if (status === 'error') {
          emit('error');
          uploading = false;
        }
      }

      return {
        prefixCls,
        handleChange,
        uploadUrl,
        getheader,
        getBizData,
        t,
        getButtonProps,
      };
    },
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-tinymce-img-upload';

  .@{prefix-cls} {
    background-color: @primary-color;
    margin: 0 3px;
    &.fullscreen {
      position: fixed;
      z-index: 10000;
    }
    // update-begin--author:liaozhiyang---date:20230326---for：【QQYUN-8647】online tinymce组件上传图片按遮挡了控件栏的全屏按钮
    .ant-btn {
      padding: 2px 4px;
      font-size: 12px;
      height: 24px;
      // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-235】富文本禁用状态下图片上传按钮文字看不清
      &.is-disabled {
        color: rgba(255, 255, 255, 0.5);
      }
      // update-end--author:liaozhiyang---date:20240524---for：【TV360X-235】富文本禁用状态下图片上传按钮文字看不清
    }
    // update-end--author:liaozhiyang---date:20230326---for：【QQYUN-8647】online tinymce组件上传图片按遮挡了控件栏的全屏按钮
  }
</style>
