package org.jeecg.modules.airag.video.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONObject;
import org.jeecg.modules.airag.video.vo.VideoGenerateVo;
import org.jeecg.modules.airag.video.vo.VideoTaskResultVo;

import java.util.List;
import java.util.Map;

/**
 * AI视频生成服务接口
 */
public interface IVideoGenerationService {

    /**
     * 提交视频生成任务
     */
    VideoTaskResultVo submitTask(VideoGenerateVo vo);

    /**
     * 查询任务状态
     */
    VideoTaskResultVo queryTask(String taskId);

    /**
     * 为已完成的视频添加AI配音
     * @param taskId 视频任务ID
     * @param prompt 原始提示词（用于生成旁白）
     * @return 包含配音视频URL的结果
     */
    VideoTaskResultVo addVoiceover(String taskId, String prompt);

    /**
     * 获取预设提示词
     */
    Map<String, List<String>> getPresetPrompts();

    /**
     * 查询用户视频生成记录列表
     */
    List<JSONObject> getVideoRecords();

    /**
     * 删除用户视频生成记录
     */
    boolean deleteVideoRecord(String recordId);
}
