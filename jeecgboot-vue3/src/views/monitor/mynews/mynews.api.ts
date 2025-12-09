import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/sys/sysAnnouncementSend/getMyAnnouncementSend',
  editCementSend = '/sys/sysAnnouncementSend/editByAnntIdAndUserId',
  readAllMsg = '/sys/sysAnnouncementSend/readAll',
  syncNotic = '/sys/annountCement/syncNotic',
  getOne = '/sys/sysAnnouncementSend/getOne',
  delete = '/sys/sysAnnouncementSend/delete',
  deleteBatch = '/sys/sysAnnouncementSend/deleteBatch',
}

/**
 * 查询消息列表
 * @param params
 */
export const getMyNewsList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 更新用户系统消息阅读状态
 * @param params
 */
export const editCementSend = (params) => {
  return defHttp.put({ url: Api.editCementSend, params });
};

/**
 * 一键已读
 * @param params
 */
export const readAllMsg = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认操作',
    content: '是否全部标注已读?',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.put({ url: Api.readAllMsg, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};

/**
 * 同步消息
 * @param params
 */
export const syncNotic = (params) => {
  return defHttp.get({ url: Api.syncNotic, params });
};

/**
 * 根据消息发送记录ID获取消息内容
 * @param sendId
 */
export const getOne = (sendId) => {
  return defHttp.get({ url: Api.getOne, params:{sendId} });
};

/**
 * 删除用户通告阅读标记的数据
 * @param params
 * @param handleSuccess
 */
export const deleteAnnSend = (params, handleSuccess) =>{
  return defHttp.delete({ url: Api.delete, params }, { joinParamsToUrl: true }).then(()=>{
    handleSuccess();
  })
}

/**
 * 批量删除用户通告阅读标记的数据
 * @param params
 * @param handleSuccess
 */
export const deleteBatchAnnSend = (params, handleSuccess) =>{
  Modal.confirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, params }, { joinParamsToUrl: true }).then(()=>{
        handleSuccess();
      })
    },
  });
}

