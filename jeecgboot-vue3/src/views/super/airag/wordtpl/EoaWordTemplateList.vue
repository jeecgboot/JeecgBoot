<template>
  <div>
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" v-auth="'wordtpl:template:add'" @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增 </a-button>

        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined" />
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button v-auth="'wordtpl:template:deleteBatch'"
            >批量操作
            <Icon icon="mdi:chevron-down" />
          </a-button>
        </a-dropdown>
        <!-- 高级查询 -->
        <super-query :config="superQueryConfig" @search="handleSuperQuery" />
        <a-button type="default" v-auth="'wordtpl:template:add'" @click="handleGenResume" preIcon="ant-design:plus-outlined"> 生成简历 </a-button>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
      <!--字段回显插槽-->
      <template #bodyCell="{ column, record, index, text }"></template>
    </BasicTable>
    <!-- 表单区域 -->
    <EoaWordTemplateModal @register="registerModal" @success="handleSuccess" />

    <BasicModal
      v-bind="$attrs"
      :footer="null"
      :showOkBtn="false"
      :canFullscreen="false"
      wrapClassName="jee-doc-design-modal no-bottom-scroll"
      :style="{ top: '0', padding: '0' }"
      :defaultFullscreen="true"
      @register="designModal"
      destroyOnClose
    >
      <DocDesign :content="wordDesignContent" :upload-file-url="parseFileUrl" @save="handleDesignSave" @download="handleDesignDownload" />
    </BasicModal>

    <BasicModal
      v-bind="$attrs"
      :canFullscreen="false"
      @register="generateModal"
      title="测试模版生成"
      :width="528"
      destroyOnClose
      @ok="handleGenOk"
    >
      <a-form :model="genWordData" :label-col="{ span: 6 }" :wrapper-col="{ span: 14 }">
        <a-form-item class="field-clos" :label="field" :name="field" v-for="field in tplFieldList">
          <a-input v-model:value="genWordData[field]" style="width:90%" :placeholder="'请输入'+field"></a-input>
        </a-form-item>
      </a-form>
    </BasicModal>

    <BasicModal :loading="resumeLoading" v-bind="$attrs" :canFullscreen="false" @register="resumeModal" title="生成简历" :width="628" destroyOnClose @ok="handleGenResumeOk">
      <a-form :model="genResumeData" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
        <a-form-item class="field-clos" label="基础信息" name="content">
          <a-input v-model:value="genResumeData.content" style="width: 90%" placeholder="请输入基础信息" />
        </a-form-item>
        <a-form-item class="field-clos" label="个人介绍" name="profile">
          <a-textarea rows="8" v-model:value="genResumeData.profile" style="width: 90%" placeholder="请输入个人介绍" />
        </a-form-item>
      </a-form>
    </BasicModal>
  </div>
</template>

<script lang="ts" name="wordtpl-eoaWordTemplate" setup>
  import { ref, reactive, computed, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/src/components/Table';
  import { BasicModal, useModal } from '/src/components/Modal';
  import { useListPage } from '/src/hooks/system/useListPage';
  import EoaWordTemplateModal from './components/EoaWordTemplateModal.vue';
  import { columns, searchFormSchema, superQuerySchema } from './EoaWordTemplate.data';
  import { list, deleteOne, batchDelete, saveOrUpdate, downloadTpl, parseFileUrl, generateWord, generateResume } from './EoaWordTemplate.api';
  import { useUserStore } from '/src/store/modules/user';
  import { useMessage } from '/src/hooks/web/useMessage';
  import { getDateByPicker } from '/src/utils';
  import DocDesign from '/src/components/wordtpl/DocDesign.vue';
  import { BasicForm } from '@/components/Form';
  //日期个性化选择
  const fieldPickers = reactive({});
  const queryParam = reactive<any>({});
  const checkedKeys = ref<Array<string | number>>([]);
  const userStore = useUserStore();
  const { createMessage } = useMessage();
  //注册model
  const [registerModal, { openModal }] = useModal();
  const [designModal, { openModal: openDesignModal, closeModal: closeDesignModal }] = useModal();
  const [generateModal, { openModal: openGenModal, closeModal: closeGenModal }] = useModal();
  const [resumeModal, { openModal: openResumeModal, closeModal: closeResumeModal }] = useModal();
  //注册table数据
  const { prefixCls, tableContext } = useListPage({
    tableProps: {
      title: 'word模版管理',
      api: list,
      columns,
      canResize: true,
      formConfig: {
        //labelWidth: 120,
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
        showAdvancedButton: true,
        fieldMapToNumber: [],
        fieldMapToTime: [],
      },
      actionColumn: {
        width: 200,
      },
      beforeFetch: (params) => {
        if (params && fieldPickers) {
          for (let key in fieldPickers) {
            if (params[key]) {
              params[key] = getDateByPicker(params[key], fieldPickers[key]);
            }
          }
        }
        return Object.assign(params, queryParam);
      },
    },
  });

  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

  const wordDesignContent = reactive({});

  const currentTpl = reactive({});
  const genWordData = reactive({});
  const tplFieldList = reactive([]);
  const genResumeData = reactive({
    content: '张三；电话：186xxxxxxxx；邮箱：zhangsan@ex.com',
    profile: '拥有13年开发经验，8年项目管理经验，6年系统架构经验，曾在多个平台级产品中担任核心负责人，具备从0到1搭建平台、从1到100推动演进的能力。',
  });
  const resumeLoading = ref(false);

  // 高级查询配置
  const superQueryConfig = reactive(superQuerySchema);

  /**
   * 高级查询事件
   */
  function handleSuperQuery(params) {
    Object.keys(params).map((k) => {
      queryParam[k] = params[k];
    });
    reload();
  }

  /**
   * 新增事件
   */
  function handleAdd() {
    openModal(true, {
      isUpdate: false,
      showFooter: true,
    });
  }

  /**
   * 生成简历事件
   */
  function handleGenResume() {
    openResumeModal(true, {
      isUpdate: false,
      showFooter: true,
    });
  }

  /**
   * 编辑事件
   */
  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      showFooter: true,
    });
  }

  function handleDesign(record: Recordable) {
    Object.keys(record).map((k) => {
      wordDesignContent[k] = record[k];
    });
    openDesignModal(true, {
      record,
    });
  }

  /**
   * 详情
   */
  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      showFooter: false,
    });
  }

  function handleGenWord(record: Recordable) {
    extractTplFields(record);
    if(tplFieldList.length > 0){
      // 如果有字段需要填写，则打开生成模版的弹窗
      currentTpl.id = record.id;
      currentTpl.name = record.name;
      Object.keys(genWordData).forEach((key) => delete genWordData[key]);
      openGenModal(true, {
        record,
      });
      return;
    } else {
      // 如果没有字段需要填写，则直接生成word
      const params = {
        templateId: record.id,
        data: {},
      };
      generateWord(record.name + '.docx', params);
    }
  }

  function handleGenOk() {
    const params = {
      templateId: currentTpl.id,
      data: genWordData,
    };
    generateWord(currentTpl.name + '.docx', params);
    closeGenModal();
  }

  function handleGenResumeOk() {
    let params = {
      flowId: '1952634605517447170',
      inputParams: genResumeData,
      responseMode: 'blocking',
    };
    resumeLoading.value = true;
    generateResume(params, (resp) => {
      if(resp && resp.success){
        closeResumeModal();
        resumeLoading.value = false;
        reload();
      }else{
        createMessage.error('生成简历失败: ' + resp.message);
        resumeLoading.value = false;
      }
    });
  }

  function extractTplFields(record) {
    const fields = new Set<string>();
    if (record.main) {
      let mainContent = [];
      // 如果record.main是string
      if (typeof record.main === 'string') {
        mainContent = JSON.parse(record.main);
      } else {
        mainContent = record.main;
      }
      // 先拼接所有 value
      let contentStr = '';
      mainContent.forEach((item) => {
        if (typeof item.value === 'string' && (!item.type || item.type === '')) {
          contentStr += item.value;
        }
      });
      // 统一正则提取
      const matches = contentStr.match(/{{(.*?)}}/g);
      if (matches) {
        matches.forEach((m) => {
          const field = m.replace(/[{}]/g, '');
          fields.add(field);
        });
      }
    }
    tplFieldList.splice(0, tplFieldList.length, ...Array.from(fields));
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteOne({ id: record.id }, handleSuccess);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDelete({ ids: selectedRowKeys.value }, handleSuccess);
  }

  /**
   * 成功回调
   */
  function handleSuccess() {
    (selectedRowKeys.value = []) && reload();
  }

  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
        auth: 'wordtpl:template:edit',
      },
      {
        label: '设计',
        onClick: handleDesign.bind(null, record),
        auth: 'wordtpl:template:design',
      },
    ];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record) {
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '生成word',
        onClick: handleGenWord.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
          placement: 'topLeft',
        },
        auth: 'wordtpl:template:delete',
      },
    ];
  }

  /**
   * 设计器保存事件
   * @param content
   */
  function handleDesignSave(content) {
    console.log('表单数据', content);
    saveOrUpdate(content, true)
      .then((data) => {
        // closeDesignModal();
        reload();
      })
      .catch((err) => {
        createMessage.error('保存失败: ' + err.message);
      });
  }

  /**
   * 设计器下载事件
   * @param content
   */
  function handleDesignDownload(content) {
    downloadTpl(content);
  }
</script>

<style lang="less" scoped>
  :deep(.ant-picker),
  :deep(.ant-input-number) {
    width: 100%;
  }

  .jee-doc-design-modal {
    &.no-bottom-scroll {
      .scrollbar__bar {
        &.is-horizontal {
          display: none;
        }
      }
    }
    .ant-modal-header {
      padding: 0 !important;
    }
    .ant-modal-body > .scrollbar {
      padding-top: 0;
    }
    .jeecg-modal-content > .scroll-container {
      padding: 0 !important;
    }
  }
</style>
<style lang="less">
  .jee-doc-design-modal .scroll-container {
    height: 100vh !important;
  }
</style>
