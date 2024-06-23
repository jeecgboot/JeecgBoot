import type { BasicColumn, ActionItem } from '/@/components/Table';
import { FileItem, PreviewFileItem, UploadResultStatus } from './typing';
import {
  // checkImgType,
  isImgTypeByName,
} from './helper';
import { Progress, Tag } from 'ant-design-vue';
import TableAction from '/@/components/Table/src/components/TableAction.vue';
import ThumbUrl from './ThumbUrl.vue';
import { useI18n } from '/@/hooks/web/useI18n';

const { t } = useI18n();

// 文件上传列表
export function createTableColumns(): BasicColumn[] {
  return [
    {
      dataIndex: 'thumbUrl',
      title: t('component.upload.legend'),
      width: 100,
      customRender: ({ record }) => {
        const { thumbUrl } = (record as FileItem) || {};
        return thumbUrl && <ThumbUrl fileUrl={thumbUrl} />;
      },
    },
    {
      dataIndex: 'name',
      title: t('component.upload.fileName'),
      align: 'left',
      customRender: ({ text, record }) => {
        const { percent, status: uploadStatus } = (record as FileItem) || {};
        let status: 'normal' | 'exception' | 'active' | 'success' = 'normal';
        if (uploadStatus === UploadResultStatus.ERROR) {
          status = 'exception';
        } else if (uploadStatus === UploadResultStatus.UPLOADING) {
          status = 'active';
        } else if (uploadStatus === UploadResultStatus.SUCCESS) {
          status = 'success';
        }
        return (
          <span>
            <p class="truncate mb-1" title={text}>
              {text}
            </p>
            <Progress percent={percent} size="small" status={status} />
          </span>
        );
      },
    },
    {
      dataIndex: 'size',
      title: t('component.upload.fileSize'),
      width: 100,
      customRender: ({ text = 0 }) => {
        return text && (text / 1024).toFixed(2) + 'KB';
      },
    },
    // {
    //   dataIndex: 'type',
    //   title: '文件类型',
    //   width: 100,
    // },
    {
      dataIndex: 'status',
      title: t('component.upload.fileStatue'),
      width: 100,
      customRender: ({ text }) => {
        if (text === UploadResultStatus.SUCCESS) {
          return <Tag color="green">{() => t('component.upload.uploadSuccess')}</Tag>;
        } else if (text === UploadResultStatus.ERROR) {
          return <Tag color="red">{() => t('component.upload.uploadError')}</Tag>;
        } else if (text === UploadResultStatus.UPLOADING) {
          return <Tag color="blue">{() => t('component.upload.uploading')}</Tag>;
        }

        return text;
      },
    },
  ];
}
export function createActionColumn(handleRemove: Function): BasicColumn {
  return {
    width: 120,
    title: t('component.upload.operating'),
    dataIndex: 'action',
    fixed: false,
    customRender: ({ record }) => {
      const actions: ActionItem[] = [
        {
          label: t('component.upload.del'),
          color: 'error',
          onClick: handleRemove.bind(null, record),
        },
      ];
      // if (checkImgType(record)) {
      //   actions.unshift({
      //     label: t('component.upload.preview'),
      //     onClick: handlePreview.bind(null, record),
      //   });
      // }
      return <TableAction actions={actions} outside={true} />;
    },
  };
}
// 文件预览列表
export function createPreviewColumns(): BasicColumn[] {
  return [
    {
      dataIndex: 'url',
      title: t('component.upload.legend'),
      width: 100,
      customRender: ({ record }) => {
        const { url } = (record as PreviewFileItem) || {};
        return isImgTypeByName(url) && <ThumbUrl fileUrl={url} />;
      },
    },
    {
      dataIndex: 'name',
      title: t('component.upload.fileName'),
      align: 'left',
    },
  ];
}

export function createPreviewActionColumn({ handleRemove, handleDownload }: { handleRemove: Fn; handleDownload: Fn }): BasicColumn {
  return {
    width: 160,
    title: t('component.upload.operating'),
    dataIndex: 'action',
    fixed: false,
    customRender: ({ record }) => {
      const actions: ActionItem[] = [
        {
          label: t('component.upload.del'),
          color: 'error',
          onClick: handleRemove.bind(null, record),
        },
        {
          label: t('component.upload.download'),
          onClick: handleDownload.bind(null, record),
        },
      ];

      return <TableAction actions={actions} outside={true} />;
    },
  };
}
