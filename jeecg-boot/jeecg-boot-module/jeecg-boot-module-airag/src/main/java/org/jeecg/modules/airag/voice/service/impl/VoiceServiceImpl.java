package org.jeecg.modules.airag.voice.service.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.config.AiChatConfig;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.airag.voice.util.VoiceApiHelper;
import org.jeecg.modules.airag.voice.service.IVoiceService;
import org.jeecg.modules.airag.voice.vo.VoiceGenerateVo;
import org.jeecg.modules.airag.voice.vo.VoiceResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文生语音服务实现（智谱AI TTS）
 */
@Slf4j
@Service
public class VoiceServiceImpl implements IVoiceService {

    private static final String REDIS_KEY_PREFIX = "airag:voice:";
    private static final String VOICE_TASK_PREFIX = "airag:voice:task:";
    private static final long VOICE_TASK_TTL = 3600L;

    @Autowired
    private AiChatConfig aiChatConfig;

    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;

    @Autowired
    private VoiceApiHelper ttsApiHelper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public VoiceResultVo textToSpeech(VoiceGenerateVo vo) {
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            log.warn("获取登录用户失败", e);
        }
        return textToSpeechWithUser(vo, loginUser);
    }

    /**
     * 核心 TTS 逻辑，接受显式传入的 loginUser（兼容同步调用和异步线程）
     */
    private VoiceResultVo textToSpeechWithUser(VoiceGenerateVo vo, LoginUser loginUser) {
        AiChatConfig.VoiceModelConfig config = aiChatConfig.getAiModelVoice();

        // 合并参数：前端传值优先，未传则取yml默认值
        String voice = vo.getVoice() != null ? vo.getVoice() : config.getVoice();
        double speed = vo.getSpeed() != null ? vo.getSpeed() : config.getSpeed();

        try {
            // 准备输出目录
            String uploadPath = jeecgBaseConfig.getPath().getUpload();
            String bizPath = "voice";
            Path outputDir = Paths.get(uploadPath, bizPath);
            Files.createDirectories(outputDir);

            String fileName = "voice_" + System.currentTimeMillis() + ".wav";
            Path audioFile = outputDir.resolve(fileName);

            // 调用公共TTS API生成音频
            ttsApiHelper.generateAudio(vo.getContent(), audioFile, voice, speed);

            // 返回结果
            String voiceUrl = bizPath + "/" + fileName;
            VoiceResultVo result = new VoiceResultVo();
            result.setVoiceUrl(voiceUrl);
            result.setFileName(fileName);

            // 存入Redis
            saveToRedis(vo, fileName, voiceUrl, loginUser);

            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("语音生成异常", e);
            throw new RuntimeException("语音生成异常: " + e.getMessage(), e);
        }
    }

    //update-begin---author:wangshuai ---date:2026-04-15  for：【QQYUN-14568】语音生成改为异步，支持切换菜单后重新获取结果-----------
    @Override
    public String generateAsync(VoiceGenerateVo vo) {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        String taskKey = VOICE_TASK_PREFIX + taskId;

        JSONObject pending = new JSONObject();
        pending.put("status", "pending");
        redisUtil.set(taskKey, pending.toJSONString(), VOICE_TASK_TTL);

        // 在异步线程执行前先获取登录用户，避免子线程中 Shiro 上下文丢失
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            log.warn("异步语音任务获取登录用户失败", e);
        }
        final LoginUser capturedUser = loginUser;

        CompletableFuture.runAsync(() -> {
            JSONObject result = new JSONObject();
            try {
                VoiceResultVo voiceResult = textToSpeechWithUser(vo, capturedUser);
                result.put("status", "success");
                result.put("voiceUrl", voiceResult.getVoiceUrl());
                result.put("fileName", voiceResult.getFileName());
            } catch (Exception e) {
                log.error("异步语音生成失败: taskId={}", taskId, e);
                result.put("status", "failed");
                result.put("message", e.getMessage());
            }
            redisUtil.set(taskKey, result.toJSONString(), VOICE_TASK_TTL);
        });

        return taskId;
    }

    @Override
    public Result<?> getVoiceTaskResult(String taskId) {
        Object val = redisUtil.get(VOICE_TASK_PREFIX + taskId);
        if (val == null) {
            return Result.error("任务不存在或已过期");
        }
        JSONObject task = JSONObject.parseObject(val.toString());
        String status = task.getString("status");
        if ("success".equals(status)) {
            JSONObject data = new JSONObject();
            data.put("voiceUrl", task.getString("voiceUrl"));
            data.put("fileName", task.getString("fileName"));
            return Result.OK(data);
        }
        if ("failed".equals(status)) {
            return Result.error(task.getString("message"));
        }
        return Result.OK("pending", null);
    }
    //update-end---author:wangshuai ---date:2026-04-15  for：【QQYUN-14568】语音生成改为异步，支持切换菜单后重新获取结果-----------

    @Override
    public List<JSONObject> getVoiceRecords() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Collections.emptyList();
        }
        String redisKey = REDIS_KEY_PREFIX + loginUser.getId();
        List<Object> list = redisUtil.lGet(redisKey, 0, -1);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(item -> JSONObject.parseObject(item.toString()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean deleteVoiceRecord(String recordId) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return false;
        }
        String redisKey = REDIS_KEY_PREFIX + loginUser.getId();
        List<Object> list = redisUtil.lGet(redisKey, 0, -1);
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (Object item : list) {
            String json = item.toString();
            JSONObject obj = JSONObject.parseObject(json);
            if (recordId.equals(obj.getString("id"))) {
                redisUtil.lRemove(redisKey, 1, item);
                return true;
            }
        }
        return false;
    }

    /**
     * 将语音生成记录存入Redis
     */
    private void saveToRedis(VoiceGenerateVo vo, String fileName, String voiceUrl, LoginUser loginUser) {
        try {
            if (loginUser == null) {
                log.warn("未获取到登录用户，跳过Redis存储");
                return;
            }
            String redisKey = REDIS_KEY_PREFIX + loginUser.getId();

            JSONObject record = new JSONObject();
            record.put("id", UUID.randomUUID().toString().replace("-", ""));
            record.put("content", vo.getContent());
            record.put("voice", vo.getVoice());
            record.put("speed", vo.getSpeed());
            record.put("volume", vo.getVolume());
            record.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            record.put("fileName", fileName);
            record.put("voiceUrl", voiceUrl);

            redisUtil.lSet(redisKey, record.toJSONString());
            log.info("语音记录已存入Redis列表: key={}", redisKey);
        } catch (Exception e) {
            log.warn("语音记录存入Redis失败，不影响主流程", e);
        }
    }
}
