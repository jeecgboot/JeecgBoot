package org.jeecg.common.bpm.api;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;

import com.alibaba.fastjson.JSONObject;

/**
 * 流程接口
 *
 * @author scott
 */
public interface IBpmBaseExtAPI {
    /**
     *  23. 流程提交接口（online，自定义开发）
     * @param flowCode 流程业务关联 例如：joa_leave_01
     * @param id 表单业务数据data id
     * @param formUrl 	流程审批时附件页面默认展示的PC端表单组件（地址）
     * @param formUrlMobile  流程审批时附件页面默认展示的移动端表单组件（地址）
     * @param username  流程发起人账号
     * @param jsonData  Json串，额外扩展的流程变量值  【非必填】
     * @return
     * @throws Exception
     */
    Result<String> startMutilProcess(String flowCode, String id, String formUrl, String formUrlMobile,String username, String jsonData) throws Exception;

    /**
     *  24. 流程提交接口（自定义表单设计器）
     * @param flowCode 流程业务关联 例如：joa_leave_01
     * @param id 表单业务数据data id
     * @param formUrl 	流程审批时附件页面默认展示的PC端表单组件（地址）
     * @param formUrlMobile  流程审批时附件页面默认展示的移动端表单组件（地址）
     * @param username  流程发起人账号
     * @param jsonData  Json串，额外扩展的流程变量值  【非必填】
     * @return
     * @throws Exception
     */
    Result<String> startDesFormMutilProcess(String flowCode, String id, String formUrl, String formUrlMobile,String username,String jsonData) throws Exception;
    /**
     * 25. 保存流程草稿箱接口（自定义开发表单、online表单）
     * @param flowCode 流程业务关联 例如：joa_leave_01
     * @param id 表单业务数据data id
     * @param formUrl 	流程审批时附件页面默认展示的PC端表单组件（地址） 【非必填】
     * @param formUrlMobile  流程审批时附件页面默认展示的移动端表单组件（地址）  【非必填】
     * @param username  流程发起人账号
     * @param jsonData  Json串，额外扩展的流程变量值  【非必填】
     * @return
     * @throws Exception
     */
    Result<String> saveMutilProcessDraft(String flowCode, String id, String formUrl, String formUrlMobile,String username,String jsonData) throws Exception;

}
