package org.jeecg.modules.airag.video.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.video.service.IVideoGenerationService;
import org.jeecg.modules.airag.video.vo.VideoGenerateVo;
import org.jeecg.modules.airag.video.vo.VideoTaskResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AI视频生成Controller
 */
@Slf4j
@RestController
@RequestMapping("/airag/video")
public class VideoGenerationController {

    @Autowired
    private IVideoGenerationService videoGenerationService;

    /**
     * 提交视频生成任务
     */
    @PostMapping("/submit")
    public Result<VideoTaskResultVo> submitTask(@RequestBody VideoGenerateVo vo) {
        VideoTaskResultVo result = videoGenerationService.submitTask(vo);
        if ("FAIL".equals(result.getStatus())) {
            return Result.error(result.getMessage());
        }
        return Result.OK(result);
    }

    /**
     * 查询视频生成任务状态
     */
    @GetMapping("/query/{taskId}")
    public Result<VideoTaskResultVo> queryTask(@PathVariable String taskId) {
        VideoTaskResultVo result = videoGenerationService.queryTask(taskId);
        return Result.OK(result);
    }

    /**
     * 为已完成的视频添加AI配音
     * 流程：生成旁白文案 → TTS语音合成 → FFmpeg合并视频和音频
     */
    @PostMapping("/voiceover")
    public Result<VideoTaskResultVo> addVoiceover(@RequestBody VideoGenerateVo vo) {
        if (vo.getTaskId() == null || vo.getTaskId().isBlank()) {
            return Result.error("taskId不能为空");
        }
        if (vo.getPrompt() == null || vo.getPrompt().isBlank()) {
            return Result.error("prompt不能为空");
        }
        VideoTaskResultVo result = videoGenerationService.addVoiceover(vo.getTaskId(), vo.getPrompt());
        if ("FAIL".equals(result.getStatus())) {
            return Result.error(result.getMessage());
        }
        return Result.OK(result);
    }

    /**
     * 获取预设提示词
     */
    @GetMapping("/prompts")
    public Result<Map<String, List<String>>> getPresetPrompts() {
        return Result.OK(videoGenerationService.getPresetPrompts());
    }

    /**
     * 查询当前用户的视频生成记录
     */
    @GetMapping("/listByUser")
    public Result<List<JSONObject>> getVideoRecords() {
        List<JSONObject> records = videoGenerationService.getVideoRecords();
        return Result.OK(records);
    }

    /**
     * 删除视频生成记录
     */
    @DeleteMapping("/deleteVideoRecord")
    public Result<String> deleteVideoRecord(@RequestParam String recordId) {
        boolean deleted = videoGenerationService.deleteVideoRecord(recordId);
        return deleted ? Result.OK("删除成功") : Result.error("记录不存在");
    }
}
