import { ref, computed, watch } from 'vue';

import {getTenantId, getToken} from '/@/utils/auth';
import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
import { JVxeComponent } from '../../types/JVxeComponent';
import { useJVxeComponent } from '../useJVxeComponent';

/**
 * use 公共上传组件
 * @param props
 * @param options 组件选项，token：默认是否传递token，action：默认上传路径，multiple：是否允许多文件
 */
export function useJVxeUploadCell(props: JVxeComponent.Props, options?) {
  const setup = useJVxeComponent(props);
  const { innerValue, originColumn, handleChangeCommon } = setup;

  const innerFile = ref<any>(null);

  /** upload headers */
  const uploadHeaders = computed(() => {
    let headers = {};
    if ((originColumn.value.token ?? options?.token ?? false) === true) {
      headers['X-Access-Token'] = getToken();
    }
    let tenantId = getTenantId();
    headers['X-Tenant-Id'] = tenantId ? tenantId : '0';
    return headers;
  });

  /** 上传请求地址 */
  const uploadAction = computed(() => {
    if (!originColumn.value.action) {
      return options?.action ?? '';
    } else {
      return originColumn.value.action;
    }
  });
  const hasFile = computed(() => innerFile.value != null);
  const responseName = computed(() => originColumn.value.responseName ?? 'message');

  watch(
    innerValue,
    (val) => {
      if (val) {
        innerFile.value = val;
      } else {
        innerFile.value = null;
      }
    },
    { immediate: true }
  );

  function handleChangeUpload(info) {
    let { file } = info;
    let value = {
      name: file.name,
      type: file.type,
      size: file.size,
      status: file.status,
      percent: file.percent,
      path: innerFile.value?.path ?? '',
    };
    if (file.response) {
      value['responseName'] = file.response[responseName.value];
    }
    let paths: string[] = [];
    if (options?.multiple && innerFile.value && innerFile.value.path) {
      paths = innerFile.value.path.split(',');
    }
    if (file.status === 'done') {
      if (typeof file.response.success === 'boolean') {
        if (file.response.success) {
          paths.push(file.response[responseName.value]);
          value['path'] = paths.join(',');
          handleChangeCommon(value);
        } else {
          value['status'] = 'error';
          value['message'] = file.response.message || '未知错误';
        }
      } else {
        // 考虑到如果设置action上传路径为非jeecg-boot后台，可能不会返回 success 属性的情况，就默认为成功
        paths.push(file.response[responseName.value]);
        value['path'] = paths.join(',');
        handleChangeCommon(value);
      }
    } else if (file.status === 'error') {
      value['message'] = file.response.message || '未知错误';
    }
    innerFile.value = value;
  }

  function handleClickDownloadFile() {
    let { url, path } = innerFile.value || {};
    if (!url || url.length === 0) {
      if (path && path.length > 0) {
        url = getFileAccessHttpUrl(path.split(',')[0]);
      }
    }
    if (url) {
      window.open(url);
    }
  }

  function handleClickDeleteFile() {
    handleChangeCommon(null);
  }

  return {
    ...setup,
    innerFile,
    uploadAction,
    uploadHeaders,
    hasFile,
    responseName,
    handleChangeUpload,
    handleClickDownloadFile,
    handleClickDeleteFile,
  };
}

export function fileGetValue(value) {
  if (value && value.path) {
    return value.path;
  }
  return value;
}

export function fileSetValue(value) {
  if (value) {
    let first = value.split(',')[0];
    let name = first.substring(first.lastIndexOf('/') + 1);
    return {
      name: name,
      path: value,
      status: 'done',
    };
  }
  return value;
}
