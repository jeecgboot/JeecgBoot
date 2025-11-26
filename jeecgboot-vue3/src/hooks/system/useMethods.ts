import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
import { useGlobSetting } from '/@/hooks/setting';

const { createMessage, createWarningModal } = useMessage();
const glob = useGlobSetting();

/**
 * 导出文件xlsx的mime-type
 */
export const XLSX_MIME_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
/**
 * 导出文件xlsx的文件后缀
 */
export const XLSX_FILE_SUFFIX = '.xlsx';

export function useMethods() {
  /**
   * 导出xls
   * @param name
   * @param url
   * @param params
   * @param isXlsx
   * @param timeout 超时时间（毫秒），默认 60000
   */
  async function exportXls(name, url, params, isXlsx = false, timeout = 60000) {
    // 代码逻辑说明: 【JHHB-794】用户管理，跨页全选后，点击用户导出没反应---
    if(params?.selections){
      let split = params.selections.split(",");
      if(split && split.length > 100){
        createMessage.warning('最多可选择 100 项进行导出！');
        return;
      }
    }
    // 修改为返回原生 response，便于获取 headers
    const response = await defHttp.get(
      { url: url, params: params, responseType: 'blob', timeout: timeout },
      { isTransformResponse: false, isReturnNativeResponse: true }
    );
    if (!response || !response.data) {
      createMessage.warning('文件下载失败');
      return;
    }
    // 判断 header 中 content-disposition 是否包含 .xlsx
    let isXlsxByHeader = isXlsx;
    const disposition = response.headers && response.headers['content-disposition'];
    if (disposition && disposition.indexOf('.xlsx') !== -1) {
      isXlsxByHeader = true;
    }
    const data = response.data;
    // 代码逻辑说明: 导出excel失败提示，不进行导出---
    let reader = new FileReader()
    reader.readAsText(data, 'utf-8')
    reader.onload = async () => {
      if(reader.result){
        if(reader.result.toString().indexOf("success") !=-1){
          // 代码逻辑说明: 【issues/7738】文件中带"success"导出报错 ---
          try {
            const { success, message } = JSON.parse(reader.result.toString());
            if (!success) {
              createMessage.warning('导出失败，失败原因：' + message);
            } else {
              exportExcel(name, isXlsxByHeader, data);
            }
            return;
          } catch (error) {
            exportExcel(name, isXlsxByHeader, data);
          }
        }
      }
      exportExcel(name, isXlsxByHeader, data);
    }
  }

  /**
   * 导入xls
   * @param data 导入的数据
   * @param url
   * @param success 成功后的回调
   */
  async function importXls(data, url, success) {
    const isReturn = (fileInfo) => {
      try {
        if (fileInfo.code === 201) {
          let {
            message,
            result: { msg, fileUrl, fileName },
          } = fileInfo;
          let href = glob.uploadUrl + fileUrl;
          createWarningModal({
            title: message,
            centered: false,
            content: `<div>
                                <span>${msg}</span><br/> 
                                <span>具体详情请<a href = ${href} download = ${fileName}> 点击下载 </a> </span> 
                              </div>`,
          });
          // 代码逻辑说明: [VUEN-2827]导入无权限，提示图标错误------------
        } else if (fileInfo.code === 500 || fileInfo.code === 510) {
          createMessage.error(fileInfo.message || `${data.file.name} 导入失败`);
        } else {
          createMessage.success(fileInfo.message || `${data.file.name} 文件上传成功`);
        }
      } catch (error) {
        console.log('导入的数据异常', error);
      } finally {
        typeof success === 'function' ? success(fileInfo) : '';
      }
    };
    await defHttp.uploadFile({ url }, { file: data.file }, { success: isReturn });
  }

  return {
    handleExportXls: (name: string, url: string, params?: object, timeout?: number) => exportXls(name, url, params, false, timeout),
    handleImportXls: (data, url, success) => importXls(data, url, success),
    handleExportXlsx: (name: string, url: string, params?: object, timeout?: number) => exportXls(name, url, params, true, timeout),
  };

  /**
   * 导出excel
   * @param name
   * @param isXlsx
   * @param data
   */
  function exportExcel(name, isXlsx, data) {
    if (!name || typeof name != 'string') {
      name = '导出文件';
    }
    let blobOptions = { type: 'application/vnd.ms-excel' };
    let fileSuffix = '.xls';
    if (isXlsx) {
      blobOptions['type'] = XLSX_MIME_TYPE;
      fileSuffix = XLSX_FILE_SUFFIX;
    }
    if (typeof window.navigator.msSaveBlob !== 'undefined') {
      window.navigator.msSaveBlob(new Blob([data], blobOptions), name + fileSuffix);
    } else {
      let url = window.URL.createObjectURL(new Blob([data], blobOptions));
      let link = document.createElement('a');
      link.style.display = 'none';
      link.href = url;
      link.setAttribute('download', name + fileSuffix);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link); //下载完成移除元素
      window.URL.revokeObjectURL(url); //释放掉blob对象
    }
  }
}
