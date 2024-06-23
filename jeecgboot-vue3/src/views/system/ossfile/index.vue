<template>
  <div>
    <!--引用表格-->
    <BasicTable @register="registerTable">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-upload name="file" :showUploadList="false" :action="ossAction" :headers="tokenHeader" :beforeUpload="beforeUpload" @change="handleChange">
          <a-button type="primary" preIcon="ant-design:upload-outlined">OSS文件上传</a-button>
        </a-upload>
        <a-upload
          name="file"
          :showUploadList="false"
          :action="minioAction"
          :headers="tokenHeader"
          :beforeUpload="beforeUpload"
          @change="handleChange"
        >
          <a-button type="primary" preIcon="ant-design:upload-outlined">MINIO文件上传</a-button>
        </a-upload>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
  </div>
</template>

<script lang="ts" name="system-ossfile" setup>
  //ts语法
  import { ref, computed, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { columns, searchFormSchema } from './ossfile.data';
  import { list, deleteFile, getOssUrl, getMinioUrl } from './ossfile.api';
  import { useGlobSetting } from '/@/hooks/setting';
  import { getToken } from '/@/utils/auth';
  import {encryptByBase64} from "@/utils/cipher";

  const { createMessage } = useMessage();
  const glob = useGlobSetting();
  const tokenHeader = { 'X-Access-Token': getToken() };
  //注册table数据
  const [registerTable, { reload }] = useTable({
    api: list,
    rowKey: 'id',
    columns,
    formConfig: {
      labelWidth: 120,
      schemas: searchFormSchema,
      autoSubmitOnEnter: true,
    },
    striped: true,
    useSearchForm: true,
    showTableSetting: true,
    clickToRowSelect: false,
    bordered: true,
    showIndexColumn: false,
    tableSetting: { fullScreen: true },
    beforeFetch: (params) => {
      return Object.assign({ column: 'createTime', order: 'desc' }, params);
    },
    actionColumn: {
      width: 80,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: undefined,
    },
  });
  /**
   * 上传url
   */
  const ossAction = computed(() => `${glob.uploadUrl}${getOssUrl}`);
  const minioAction = computed(() => `${glob.uploadUrl}${getMinioUrl}`);

  /**
   * 预览
   */
  function handleView(record) {
    if (record && record.url) {
      console.log('glob.onlineUrl', glob.viewUrl);
      //update-begin---author:scott ---date:2024-06-03  for：【TV360X-952】升级到kkfileview4.1.0---
      // let filePath = encodeURIComponent(record.url);
      let url = encodeURIComponent(encryptByBase64(record.url));
      // //文档采用pdf预览高级模式
      // if(filePath.endsWith(".pdf") || filePath.endsWith(".doc") || filePath.endsWith(".docx")){
      //   filePath = filePath
      // }
      let previewUrl = `${glob.viewUrl}?url=` + url;
      //update-end---author:scott ---date:2024-06-03  for：【TV360X-952】升级到kkfileview4.1.0---
      
      window.open(previewUrl, '_blank');
    }
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteFile({ id: record.id }, reload);
  }

  /**
   * 上传前事件
   */
  function beforeUpload(file) {
    var fileType = file.type;
    if (fileType === 'image') {
      if (fileType.indexOf('image') < 0) {
        createMessage.warning('请上传图片');
        return false;
      }
    } else if (fileType === 'file') {
      if (fileType.indexOf('image') >= 0) {
        createMessage.warning('请上传文件');
        return false;
      }
    }
    return true;
  }

  /**
   * 文件上传事件
   */
  function handleChange(info) {
    if (info.file.status === 'done') {
      if (info.file.response.success) {
        reload();
        createMessage.success(`${info.file.name} 上传成功!`);
      } else {
        createMessage.error(`${info.file.response.message}`);
      }
    } else if (info.file.status === 'error') {
      createMessage.error(`${info.file.response.message}`);
    }
  }

  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '预览',
        onClick: handleView.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>

<style scoped></style>
