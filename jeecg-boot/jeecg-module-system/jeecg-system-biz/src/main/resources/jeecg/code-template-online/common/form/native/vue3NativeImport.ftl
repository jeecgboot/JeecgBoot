<#if need_select_tag>
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
</#if>
<#if need_switch>
  import JSwitch from '/@/components/Form/src/jeecg/components/JSwitch.vue';
</#if>
<#if need_multi>
  import JSelectMultiple from '/@/components/Form/src/jeecg/components/JSelectMultiple.vue';
</#if>
<#if need_search>
  import JSearchSelect from '/@/components/Form/src/jeecg/components/JSearchSelect.vue';
</#if>
<#if need_popup>
  import JPopup from '/@/components/Form/src/jeecg/components/JPopup.vue';
</#if>
<#if need_popup_dict>
  import JPopupDict from '/@/components/Form/src/jeecg/components/JPopupDict.vue';
</#if>
<#if need_category>
  import JCategorySelect from '/@/components/Form/src/jeecg/components/JCategorySelect.vue';
</#if>
<#if need_dept>
  import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
</#if>
<#if need_dept_user>
<#-- update-begin---author:chenrui ---date:20240102  for：[issue/#5711]修复用户选择组件在生成代码后变成部门用户选择组件---------- -->
  import JSelectUser from '/@/components/Form/src/jeecg/components/JSelectUser.vue';
<#-- update-end---author:chenrui ---date:20240102  for：[issue/#5711]修复用户选择组件在生成代码后变成部门用户选择组件---------- -->
</#if>
<#if need_select_tree>
  import JTreeSelect from '/@/components/Form/src/jeecg/components/JTreeSelect.vue';
</#if>
<#if need_time>
  import { TimePicker } from 'ant-design-vue';
</#if>
<#if need_pca>
  import JAreaLinkage from '/@/components/Form/src/jeecg/components/JAreaLinkage.vue';
</#if>
<#if need_upload>
  import JUpload from '/@/components/Form/src/jeecg/components/JUpload/JUpload.vue';
</#if>
<#if need_image_upload>
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';
</#if>
<#if need_markdown>
  import JMarkdownEditor from '/@/components/Form/src/jeecg/components/JMarkdownEditor.vue';
</#if>
<#if need_editor>
  import JEditor from '/@/components/Form/src/jeecg/components/JEditor.vue';
</#if>
<#if need_checkbox>
  import JCheckbox from "/@/components/Form/src/jeecg/components/JCheckbox.vue";
</#if>
<#if need_range_number>
  import JRangeNumber from "/@/components/Form/src/jeecg/components/JRangeNumber.vue";
</#if>