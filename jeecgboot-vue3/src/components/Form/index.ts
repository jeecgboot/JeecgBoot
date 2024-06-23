import BasicForm from './src/BasicForm.vue';

export * from './src/types/form';
export * from './src/types/formItem';

export { useComponentRegister } from './src/hooks/useComponentRegister';
export { useForm } from './src/hooks/useForm';

export { default as ApiSelect } from './src/components/ApiSelect.vue';
export { default as RadioButtonGroup } from './src/components/RadioButtonGroup.vue';
export { default as ApiTreeSelect } from './src/components/ApiTreeSelect.vue';
export { default as ApiRadioGroup } from './src/components/ApiRadioGroup.vue';
//Jeecg自定义组件
export { default as JAreaLinkage } from './src/jeecg/components/JAreaLinkage.vue';
export { default as JSelectUser } from './src/jeecg/components/JSelectUser.vue';
export { default as JSelectDept } from './src/jeecg/components/JSelectDept.vue';
export { default as JCodeEditor } from './src/jeecg/components/JCodeEditor.vue';
export { default as JCategorySelect } from './src/jeecg/components/JCategorySelect.vue';
export { default as JSelectMultiple } from './src/jeecg/components/JSelectMultiple.vue';
export { default as JPopup } from './src/jeecg/components/JPopup.vue';
export { default as JAreaSelect } from './src/jeecg/components/JAreaSelect.vue';
export { JEasyCron, JEasyCronInner, JEasyCronModal } from '/@/components/Form/src/jeecg/components/JEasyCron';
export { default as JCheckbox } from './src/jeecg/components/JCheckbox.vue';
export { default as JInput } from './src/jeecg/components/JInput.vue';
export { default as JEllipsis } from './src/jeecg/components/JEllipsis.vue';
export { default as JDictSelectTag } from './src/jeecg/components/JDictSelectTag.vue';
export { default as JTreeSelect } from './src/jeecg/components/JTreeSelect.vue';
export { default as JSearchSelect } from './src/jeecg/components/JSearchSelect.vue';
export { default as JSelectUserByDept } from './src/jeecg/components/JSelectUserByDept.vue';
export { default as JEditor } from './src/jeecg/components/JEditor.vue';
export { default as JImageUpload } from './src/jeecg/components/JImageUpload.vue';
// Jeecg自定义校验
export { JCronValidator } from '/@/components/Form/src/jeecg/components/JEasyCron';

export { BasicForm };
