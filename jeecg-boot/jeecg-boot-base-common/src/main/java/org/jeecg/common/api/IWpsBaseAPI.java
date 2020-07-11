package org.jeecg.common.api;

import org.jeecg.common.api.vo.OaWpsModel;

/**
 * @Description: WPS通用接口
 * @Author: wangshuai
 * @Date:20200709
 * @Version:V1.0
 */
public interface IWpsBaseAPI {

  /*根据模板id获取模板信息*/
  OaWpsModel getById(String id);

  /*根据文件路径下载文件*/
 void downloadOosFiles(String objectName, String basePath,String fileName);

 /*WPS 设置数据存储，用于逻辑判断*/
 void context(String type,String text);

 /*删除WPS模板相关信息*/
 void deleteById(String id);
}
