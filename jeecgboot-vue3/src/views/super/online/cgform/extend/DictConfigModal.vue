<template>
  <BasicModal
    wrapClassName="dict-config-modal"
    v-bind="$attrs"
    title="字典配置"
    :width="560"
    @register="registerModal"
    keyboard
    :canFullscreen="false"
    cancelText="关闭"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
    <div class="dict-config-tip">
      <p>· <b>系统字典</b>：仅填 Code（如 <code>sex</code>）</p>
      <p>· <b>表字典</b>：Table=表名，Code=存值字段，Text=显示字段；带条件可在 Table 后加 <code>where ...</code></p>
      <p>· <b>Popup 弹框</b>：Table=报表 code，Code=报表字段，Text=本表回填字段，多个用逗号</p>
      <p>· <b>Popup 字典</b>：Table=报表 code，Code/Text 各一个字段</p>
      <p>· <b>自定义树</b>：Text=<code>id,pid,显示字段,有子节点字段</code></p>
      <p>· <b>联动下拉</b>：Table 填 JSON 串，Code/Text 留空</p>
      <p>· <b>关联记录</b>：Table=关联表名，Code=存值字段（一般为 <code>id</code>），Text=标题字段,其他显示字段（如 <code>name,age,sex</code>，第一个为标题字段）</p>
      <p>· <b>他表字段</b>：Table=本表关联记录字段名，Code 留空，Text=关联表中要显示的字段（如 <code>name</code>）</p>
    </div>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';

  export default defineComponent({
    name: 'DictConfigModal',
    components: {
      BasicModal,
      BasicForm,
    },
    emits: ['success', 'register'],
    setup(_p, { emit }) {
      const rowKey = ref('');
      const fieldShowType = ref('');

      const formSchemas: any[] = [
        {
          label: '字典Table',
          field: 'dictTable',
          component: 'InputTextArea',
          componentProps: {
            placeholder: '',
            rows: 3,
            class: 'dict-config-table-input',
            allowClear: true,
          },
        },
        {
          label: '字典Code',
          field: 'dictField',
          component: 'Input',
          componentProps: {
            placeholder: '',
            allowClear: true,
          },
          rules: [
            {
              validator: (_, value) => {
                if (fieldShowType.value === 'popup_dict' && value && value.indexOf(',') !== -1) {
                  return Promise.reject('popup字典组件只允许填写一个字段');
                }
                return Promise.resolve();
              },
              trigger: 'blur',
            },
          ],
        },
        {
          label: '字典Text',
          field: 'dictText',
          component: 'Input',
          componentProps: {
            placeholder: '',
            allowClear: true,
          },
          rules: [
            {
              validator: (_, value) => {
                if (fieldShowType.value === 'popup_dict' && value && value.indexOf(',') !== -1) {
                  return Promise.reject('popup字典组件只允许填写一个字段');
                }
                return Promise.resolve();
              },
              trigger: 'blur',
            },
          ],
        },
      ];

      const [registerForm, { validate, setFieldsValue, resetFields }] = useForm({
        schemas: formSchemas,
        showActionButtonGroup: false,
        labelAlign: 'right',
        labelWidth: 90,
      });

      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        rowKey.value = data?.rowKey ?? '';
        fieldShowType.value = data?.fieldShowType ?? '';
        await resetFields();
        await setFieldsValue({
          dictTable: data?.dictTable ?? '',
          dictField: data?.dictField ?? '',
          dictText: data?.dictText ?? '',
        });
      });

      async function handleSubmit() {
        const data = await validate();
        emit('success', {
          rowKey: rowKey.value,
          dictTable: (data.dictTable ?? '').trim(),
          dictField: (data.dictField ?? '').trim(),
          dictText: (data.dictText ?? '').trim(),
        });
        closeModal();
      }

      return {
        registerModal,
        registerForm,
        handleSubmit,
      };
    },
  });
</script>

<style lang="less" scoped>
  :deep(.dict-config-table-input) {
    min-height: 64px;
    max-height: 240px !important;
    resize: vertical;
  }

  .dict-config-tip {
    margin-top: 4px;
    padding: 8px 12px;
    background-color: #fafafa;
    border-radius: 4px;
    color: rgba(0, 0, 0, 0.55);
    font-size: 12px;
    line-height: 1.7;
    p {
      margin: 0;
    }
    b {
      color: rgba(0, 0, 0, 0.75);
      font-weight: 600;
    }
    code {
      padding: 0 4px;
      margin: 0 1px;
      background-color: rgba(0, 0, 0, 0.06);
      border-radius: 3px;
      font-family: SFMono-Regular, Consolas, monospace;
      font-size: 11px;
      color: #c7254e;
    }
    u {
      text-decoration: none;
      color: #d4380d;
      font-weight: 500;
    }
  }
</style>
