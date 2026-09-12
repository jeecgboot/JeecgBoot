package org.jeecg.modules.airag.voice.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.voice.service.IVoiceService;
import org.jeecg.modules.airag.voice.vo.VoiceGenerateVo;
import org.jeecg.modules.airag.voice.vo.VoiceResultVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * 文生语音控制器
 */
@Slf4j
@RestController
@RequestMapping("/airag/voice")
public class VoiceController {

    @Autowired
    private IVoiceService voiceService;

    /**
     * 文本生成语音
     */
    @PostMapping("/generate")
    public Result<VoiceResultVo> generate(@RequestBody VoiceGenerateVo vo) {
        // 参数校验
        if (vo.getContent() == null || vo.getContent().isBlank()) {
            return Result.error("合成文本不能为空");
        }
        if (vo.getSpeed() != null && (vo.getSpeed() < 0.25 || vo.getSpeed() > 4.0)) {
            return Result.error("倍速范围须在0.25~4.0之间");
        }

        try {
            VoiceResultVo result = voiceService.textToSpeech(vo);
            return Result.OK(result);
        } catch (Exception e) {
            log.error("文生语音失败", e);
            return Result.error("语音生成失败: " + e.getMessage());
        }
    }

    //update-begin---author:wangshuai ---date:2026-04-15  for：【QQYUN-14568】语音生成改为异步，支持切换菜单后重新获取结果-----------
    /**
     * 异步提交语音生成任务，立即返回 taskId
     */
    @PostMapping("/generateAsync")
    public Result<String> generateAsync(@RequestBody VoiceGenerateVo vo) {
        if (vo.getContent() == null || vo.getContent().isBlank()) {
            return Result.error("合成文本不能为空");
        }
        if (vo.getSpeed() != null && (vo.getSpeed() < 0.25 || vo.getSpeed() > 4.0)) {
            return Result.error("倍速范围须在0.25~4.0之间");
        }
        String taskId = voiceService.generateAsync(vo);
        return Result.OK(taskId);
    }

    /**
     * 查询异步语音任务结果
     */
    @GetMapping("/queryTask/{taskId}")
    public Result<?> queryVoiceTask(@PathVariable String taskId) {
        return voiceService.getVoiceTaskResult(taskId);
    }
    //update-end---author:wangshuai ---date:2026-04-15  for：【QQYUN-14568】语音生成改为异步，支持切换菜单后重新获取结果-----------

    /**
     * 查询当前用户的语音生成记录
     */
    @GetMapping("/listByUser")
    public Result<List<JSONObject>> getVoiceRecords() {
        List<JSONObject> records = voiceService.getVoiceRecords();
        return Result.OK(records);
    }

    /**
     * 删除语音生成记录
     */
    @DeleteMapping("/deleteVoiceRecord")
    public Result<String> deleteVoiceRecord(@RequestParam String recordId) {
        boolean deleted = voiceService.deleteVoiceRecord(recordId);
        return deleted ? Result.OK("删除成功") : Result.error("记录不存在");
    }
}
