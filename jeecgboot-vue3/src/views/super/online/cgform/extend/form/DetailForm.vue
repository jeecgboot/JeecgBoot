<template>
  <div :class="formContainerClass">
    <a-row>
      <a-col v-for="(item, index) in schemas" :key="index" :span="getItemSpan(item)">
        <template v-if="item.hidden"></template>
        <div v-else class="detail-item">
          <div class="item-title" :class="getLabelWidthClass(item)" :title="item.label"> {{ filterLable(item) }}： </div>
          <div class="item-content" v-if="item.view === 'markdown'">
             <MarkdownViewer v-model:value="detailFormData[item.field]" placeholder="" />
          </div>
          <!-- update-begin--author:scott---date:20261031---for：【GHSA-867f-2922-wj26】详情直接 v-html 字段值导致存储型XSS，富文本先净化 -->
          <div class="item-content" :class="item.view" v-else-if="item.isHtml" v-html="sanitizeRichText(detailFormData[item.field])"></div>
          <!-- update-end--author:scott---date:20261031---for：【GHSA-867f-2922-wj26】详情直接 v-html 字段值导致存储型XSS，富文本先净化 -->
          <div class="item-content" v-else-if="item.view == 'textarea'" v-html="textareaLineBreak(detailFormData[item.field])"></div>
          <div class="item-content" style="display: block;padding-top:10px" v-else-if="item.isCard">
            <span v-if="!detailFormData[item.field]"></span>
            <link-table-card v-else disabled detail :value="detailFormData[item.field]" :valueField="item.dictCode" :textField="item.dictText" :tableName="item.dictTable" :multi="item.multi" :imageField="item.imageField"></link-table-card>
          </div>
          <div class="item-content" v-else-if="item.isImage">
            <div class="ant-upload-list ant-upload-list-picture-card" style="display: flex">
              <template v-for="url in detailFormData[item.field]">
                <div class="ant-upload-list-picture-card-container" style="margin-top: 8px;">
                  <span>
                    <div class="ant-upload-list-width ant-upload-list-picture-border ant-upload-list-item ant-upload-list-item-done ant-upload-list-item-list-type-picture-card" data-has-actions="true">
                      <div class="ant-upload-list-item-thumbnail ant-upload-list-item-info">
                        <img :src="url" alt="图片不存在" class="ant-upload-list-item-image" @click="handleViewImage(item.field)" />
                      </div>
                      <span class="ant-upload-list-item-actions">
                        <eye-outlined @click="handleViewImage(item.field)" />
                      </span>
                    </div>
                  </span>
                </div>
              </template>
            </div>
          </div>
          <div class="item-content" v-else-if="item.isFile">
            <div class="ant-upload-list ant-upload-list-text">
              <template v-for="url in detailFormData[item.field]">
                <div class="">
                  <span>
                    <div class="ant-upload-list-item ant-upload-list-item-done ant-upload-list-item-list-type-text">
                      <div class="ant-upload-list-item-info">
                        <span class="ant-upload-span">
                          <div class="ant-upload-text-icon">
                            <paper-clip-outlined />
                          </div>
                          <a :href="url" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-name">
                            {{ getFilename(url) }}
                          </a>
                          <span class="ant-upload-list-item-card-actions">
                            <download-outlined @click="handleDownloadFile(url)" />
                          </span>
                        </span>
                      </div>
                    </div>
                  </span>
                </div>
              </template>
            </div>
          </div>
          <div v-else class="item-content" >
            {{ filter(detailFormData[item.field], item.view, item) }}
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useDetailForm } from './useDetailForm';
  import { DownloadOutlined, EyeOutlined, PaperClipOutlined } from '@ant-design/icons-vue';
  import LinkTableCard from '../linkTable/LinkTableCard.vue'
  import { MarkdownViewer } from '/@/components/Markdown';
  import { getWeekMonthQuarterYear } from '/@/utils';
  import { sanitizeRichText, escapeHtml } from '/@/utils/htmlSanitizer';

  export default defineComponent({
    name: 'DetailForm',
    components: {
      DownloadOutlined,
      EyeOutlined,
      PaperClipOutlined,
      LinkTableCard,
      MarkdownViewer,
    },
    props: {
      span: propTypes.number.def(24),
      //表单配置
      schemas: propTypes.array.def([]),
      //表单数据
      data: propTypes.object.def({}),
      containerClass: propTypes.string.def(''),
    },
    setup(props) {
      const { formContainerClass, detailFormData, getItemSpan, handleDownloadFile, handleViewImage, getFilename, getLabelWidthClass } = useDetailForm(props);
      // update-begin--author:liaozhiyang---date:20230925---for：【QQYUN-6647】页面配置中高级配置中设置的label长度，在列表与详情界面无效
      const filterLable = (item) => {
        if (item.fieldExtendJson) {
          const json = JSON.parse(item.fieldExtendJson);
          if (!!json.labelLength && item.label.length > 4) {
            return item.label.substr(0, json.labelLength);
          }
        }
        return item.label;
      };
      // update-end--author:liaozhiyang---date:20230925---for：【QQYUN-6647】页面配置中高级配置中设置的label长度，在列表与详情界面无效
      // update-begin--author:liusq---date:20240527---for：【TV360X-464】多行文本换行显示
      const textareaLineBreak = (content) => {
        // update-begin--author:scott---date:20261031---for：【GHSA-867f-2922-wj26】多行文本只换行不转义，v-html 渲染导致存储型XSS，先转义再换行
        if (content == null || content === '') {
          return content;
        }
        content = escapeHtml(content);
        // update-end--author:scott---date:20261031---for：【GHSA-867f-2922-wj26】多行文本只换行不转义，v-html 渲染导致存储型XSS，先转义再换行
        if (content && content.includes('\n')) {
          content = content.replace(/\n/g, '<br>');
        }
        return content;
      };
      // update-end--author:liusq---date:20240527---for：【TV360X-464】多行文本换行显示
      // update-begin--author:liaozhiyang---date:20230110---for：【QQYUN-7636】online详情oracle数据库日期类型字段会默认带上时分秒
      const filter = (value, type, item) => {
        if (type == 'date' && typeof value === 'string') {
          // update-begin--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
          // 【TV360X-29】数据是空的，日期如果是年、年月、年周、年季度详情里面是NAN
          if (!value) {
            return '';
          }
          let fieldExtendJson = item.fieldExtendJson;
          if (fieldExtendJson) {
            fieldExtendJson = JSON.parse(fieldExtendJson);
            if (fieldExtendJson.picker && fieldExtendJson.picker != 'default') {
              const result = getWeekMonthQuarterYear(value);
              return result[fieldExtendJson.picker];
            }
          }
          // update-end--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
          // oracle数据库日期类型字段会带上时分秒
          return value.split(' ').shift();
        } else {
          return value;
        }
      };
      // update-end--author:liaozhiyang---date:20230110---for：【QQYUN-7636】online详情oracle数据库日期类型字段会默认带上时分秒
      return {
        formContainerClass,
        detailFormData,
        getItemSpan,
        handleDownloadFile,
        handleViewImage,
        getFilename,
        getLabelWidthClass,
        filterLable,
        filter,
        textareaLineBreak,
        sanitizeRichText,
      };
    },
  });
</script>

<style scoped lang="less">
  .jeecg-detail-form {
    border: 1px solid @border-color-split;
    border-left: none;
    border-bottom: none;
    .detail-item {
      display: flex;
      flex-direction: row;
      align-items: stretch;
      line-height: 24px;
      border-bottom: 1px solid @border-color-split;
      height: 100%;
      // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-21】多行文本框在详情弹框中不换行
      word-break: break-all;
      // update-end--author:liaozhiyang---date:20240517---for：【TV360X-21】多行文本框在详情弹框中不换行
      .item-title {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        flex-shrink: 0;
        flex-grow: 0;
        min-width: 100px;
        width: 24%;
        max-width: 220px;
        border-right: 1px solid @border-color-split;
        /* border-left: 1px solid #f0f0f0;*/
        padding: 10px 0;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        &.span24{
          width: 24%;
        }
        &.span12{
          width: 12%;
        }
        &.span8{
          width: 8%;
        }
        &.span6{
          width: 6%;
        }
        // update-begin--author:liaozhiyang---date:20231228---for：【QQYUN-7769】详情label加上背景色和字体颜色
        background: #fafafa;
        color: #000;
        font-weight: 500;
        // update-end--author:liaozhiyang---date:20231228---for：【QQYUN-7769】详情label加上背景色和字体颜色
      }
      .item-content {
        border-right: 1px solid @border-color-split;
        flex-grow: 1;
        padding-left: 10px;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        // update-begin--author:liaozhiyang---date:20231226---for：【QQYUN-7470】富文本详情中纵向显示
        &.umeditor {
          display: block;
        }
        // update-end--author:liaozhiyang---date:20231226---for：【QQYUN-7470】富文本详情中纵向显示
        .anticon {
          &:hover {
            color: #40a9ff;
          }
        }

        .detail-image-container {
          box-sizing: border-box;
          margin: 0;
          padding: 0;
          color: #000000d9;
          font-size: 14px;
          font-variant: tabular-nums;
          list-style: none;
          font-feature-settings: 'tnum';
          line-height: 1.5715;
          .image-item {
            display: inline-block;
            width: 104px;
            height: 104px;
            margin: 5px;
            border: 1px solid @border-color-split;
            vertical-align: top;
            img {
            }
          }
        }
      }
    }
  }
  // update-begin--author:liaozhiyang---date:20231228---for：【QQYUN-7769】详情label加上背景色和字体颜色
  @darkBorderColor: #3a3a3a;
  html[data-theme='dark'] {
    .jeecg-detail-form {
      border: 1px solid @darkBorderColor;
      .detail-item {
        border-bottom-color: @darkBorderColor;
        .item-title {
          border-right-color: @darkBorderColor;
          background: #1d1d1d;
          color: rgba(255, 255, 255, 0.65);
          
        }
      }
    }
  }
  // update-end--author:liaozhiyang---date:20231228---for：【QQYUN-7769】详情label加上背景色和字体颜色
  // update-begin--author:liaozhiyang---date:20240516---for：【TV360X-15】详情文件和图片样式美化
  .ant-upload-list-text {
    .ant-upload-span {
      display: flex;
      // update-begin--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
      .ant-upload-list-item-name {
        margin: 0 6px;
        min-width: 0;
        flex: 1;
      }
      .ant-upload-list-item-card-actions {
        display: inline-flex;
        align-items: center;
        margin-left: auto;
        padding-right: 8px;
        cursor: pointer;
      }
      // update-end--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
      a {
        color: rgba(51, 51, 51, 0.88);
      }
    }
  }
  .ant-upload-list-picture-card {
      padding-bottom: 8px;
      .ant-upload-list-item {
        display: flex;
        align-items: center;
        .ant-upload-list-item-image {
          max-height: 120px;
          max-width: 120px;
          cursor: pointer;
        }
        // update-begin--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
        .ant-upload-list-item-actions {
          display: none;
          position: absolute;
          inset: 0;
          align-items: center;
          justify-content: center;
          color: #fff;
          font-size: 18px;
          background: rgba(0, 0, 0, 0.45);
          cursor: pointer;
        }
        &:hover .ant-upload-list-item-actions {
          display: flex;
        }
        // update-end--author:liaozhiyang---date:20260811---for:【LHZP-4】online详情关联记录加上图片显示及文件下载及图片预览
      }
    }
    // update-begin--author:liaozhiyang---date:20231228---for：【TV360X-15】详情文件和图片样式美化

  //update-begin---author:wangshuai---date:2025-08-26---for:【issues/8759】online表单的图片控件多图片显示问题---
  .ant-upload-list-picture-border {
    position: relative;
    height: 100%;
    margin: 0;
    padding: 8px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    display: flex;
    align-items: center;
    transition: background-color 0.3s;
    text-align: center;
  }
  
  .ant-upload-list-item-thumbnail,.ant-upload-list-item-thumbnail img{
    position: static;
    display: block;
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
  
  .ant-upload-list-width{
    display: inline-block;
    width: 102px;
    height: 102px;
    margin-block: 0 8px;
    margin-inline: 0 8px;
    vertical-align: top;
  }
  //update-end---author:wangshuai---date:2025-08-26---for:【issues/8759】online表单的图片控件多图片显示问题---
</style>
