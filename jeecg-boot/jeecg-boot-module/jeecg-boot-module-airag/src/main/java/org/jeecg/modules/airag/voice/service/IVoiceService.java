package org.jeecg.modules.airag.voice.service;

import com.alibaba.fastjson2.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.voice.vo.VoiceGenerateVo;
import org.jeecg.modules.airag.voice.vo.VoiceResultVo;

import java.util.List;

/**
 * 文生语音服务接口
 */
public interface IVoiceService {
    /**
     * 文本转语音
     * @param vo 请求参数
     * @return 生成结果
     */
    VoiceResultVo textToSpeech(VoiceGenerateVo vo);

    //update-begin---author:wangshuai ---date:2026-04-15  for：【QQYUN-14568】语音生成改为异步，支持切换菜单后重新获取结果-----------
    /**
     * 异步提交语音生成任务，立即返回 taskId
     * @param vo 请求参数
     * @return taskId
     */
    String generateAsync(VoiceGenerateVo vo);

    /**
     * 查询异步语音任务结果
     * @param taskId 任务ID
     * @return 结果（pending / success / failed）
     */
    Result<?> getVoiceTaskResult(String taskId);
    //update-end---author:wangshuai ---date:2026-04-15  for：【QQYUN-14568】语音生成改为异步，支持切换菜单后重新获取结果-----------

    /**
     * 查询用户语音生成记录列表
     * @return 记录列表
     */
    List<JSONObject> getVoiceRecords();

    /**
     * 删除用户语音生成记录
     * @param recordId 记录ID
     * @return 是否删除成功
     */
    boolean deleteVoiceRecord(String recordId);
}
