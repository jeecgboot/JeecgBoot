package org.jeecg.modules.airag.video.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.jeecg.config.AiChatConfig;
import org.jeecg.modules.airag.voice.util.VoiceApiHelper;
import org.jeecg.modules.airag.video.service.IVideoGenerationService;
import org.jeecg.modules.airag.video.vo.VideoGenerateVo;
import org.jeecg.modules.airag.video.vo.VideoTaskResultVo;
import org.jeecg.config.JeecgBaseConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

/**
 * AI视频生成服务实现
 */
@Slf4j
@Service
public class VideoGenerationServiceImpl implements IVideoGenerationService {

    /** apiHost 从 yml 配置 jeecg.ai-chat.ai-model-video.api-host 读取，不再硬编码 */

    private static final String REDIS_KEY_PREFIX = "airag:video:";

    private static final Map<String, List<String>> PRESET_PROMPTS = new LinkedHashMap<>();

    static {
        PRESET_PROMPTS.put("通用演示", List.of(
                "一只金毛犬在金色的沙滩上奔跑，海浪轻轻拍打着岸边，阳光明媚，慢动作镜头",
                "航拍壮丽的山脉全景，云雾缭绕在山峰之间，镜头缓缓推进",
                "樱花树下，花瓣随风飘落，一条小溪静静流淌，春日午后的宁静氛围"
        ));
        PRESET_PROMPTS.put("产品营销", List.of(
                "一杯咖啡被缓缓倒入透明玻璃杯中，咖啡与牛奶融合形成美丽的纹理，微距特写",
                "一款高端智能手表在旋转展示台上缓缓旋转，灯光打在表面上反射出金属光泽，黑色背景",
                "一双运动鞋踩入水洼溅起水花，慢动作特写，动感活力的画面"
        ));
        PRESET_PROMPTS.put("教育培训", List.of(
                "地球从太空视角缓缓旋转，可以看到大气层和云层的细节，星空背景",
                "一本书的书页被风吹动快速翻动，文字和插图若隐若现，知识流动的意象",
                "显微镜下的细胞分裂过程，色彩鲜明的科学可视化风格"
        ));
        PRESET_PROMPTS.put("创意设计", List.of(
                "一座未来主义的城市在日落时分，霓虹灯光倒映在雨水的路面上，赛博朋克风格",
                "水墨在水中缓缓扩散，形成抽象的山水画意境，中国风艺术效果",
                "星空下的极光在天空中舞动，色彩绚烂，延时摄影效果"
        ));
    }

    @Autowired
    private AiChatConfig aiChatConfig;

    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;

    @Autowired
    private VoiceApiHelper voiceApiHelper;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** 实际使用的ffmpeg路径，优先yml配置，其次自动查找 */
    private String ffmpegPath;
    /** 实际使用的edge-tts路径，优先yml配置，其次自动查找 */
    private String edgeTtsPath;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    @PostConstruct
    public void init() {
        // 从yml配置读取，若为空则自动查找
        AiChatConfig.VideoModelConfig videoConfig = aiChatConfig.getAiModelVideo();
        String configFfmpeg = videoConfig.getFfmpegPath();
        String configEdgeTts = videoConfig.getEdgeTtsPath();

        if (configFfmpeg != null && !configFfmpeg.isBlank()) {
            this.ffmpegPath = configFfmpeg;
        } else {
            this.ffmpegPath = findCommand(new String[]{"-version"}, "ffmpeg", "ffmpeg.exe", "C:/tools/ffmpeg/ffmpeg.exe");
        }
        if (configEdgeTts != null && !configEdgeTts.isBlank()) {
            this.edgeTtsPath = configEdgeTts;
        } else {
            this.edgeTtsPath = findCommand(new String[]{"--version"}, "edge-tts", "" +
                    "", "D:/ProgramFiles/miniconda3/Scripts/edge-tts.exe");
        }

        log.info("=== AI视频配音工具检测 ===");
        if (ffmpegPath != null) {
            log.info("  ffmpeg   : 已找到 -> {}", ffmpegPath);
        } else {
            log.warn("  ffmpeg   : 未安装，视频配音功能将不可用");
        }
        if (edgeTtsPath != null) {
            log.info("  edge-tts : 已找到 -> {}", edgeTtsPath);
        } else {
            log.warn("  edge-tts : 未安装，视频配音功能将不可用");
        }
        if (isToolsAvailable()) {
            log.info("  视频配音功能: 已启用");
        } else {
            log.warn("  视频配音功能: 已禁用（缺少依赖工具，调用配音接口将直接返回无声视频）");
        }
        log.info("===========================");
    }

    @Override
    public VideoTaskResultVo submitTask(VideoGenerateVo vo) {
        AiChatConfig.ModelConfig config = aiChatConfig.getAiModelVideo();
        String apiKey = config.getApiKey();
        String model = config.getModel();
        String apiHost = config.getApiHost();
        String baseUrl = apiHost.endsWith("/") ? apiHost.substring(0, apiHost.length() - 1) : apiHost;

        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("prompt", vo.getPrompt());
        //生成质量
        body.put("quality", "quality");

        // 前端传递 izAiAudio：1=使用AI合成音效，0=不使用
        boolean aiAutoAudio = vo.getIzAiAudio() != null && vo.getIzAiAudio() == 1;

        if (model.contains("vidu2")) {
            // vidu2系列模型使用 aspect_ratio 参数，将 size 转为宽高比
            if (vo.getSize() != null) {
                body.put("aspect_ratio", convertSizeToAspectRatio(vo.getSize()));
            }
        } else {
            if (vo.getSize() != null) {
                body.put("size", vo.getSize());
            }
            if (vo.getFps() != null) {
                body.put("fps", vo.getFps());
            }
            if (vo.getDuration() != null) {
                body.put("duration", vo.getDuration());
            }
            if (aiAutoAudio) {
                body.put("with_audio", true);
            }
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/videos/generations"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("视频生成任务提交响应: status={}, body={}", response.statusCode(), response.body());

            if (response.statusCode() != 200) {
                VideoTaskResultVo result = new VideoTaskResultVo();
                result.setStatus("FAIL");
                String errorMsg = "提交任务失败，状态码: " + response.statusCode();
                try {
                    JSONObject errorJson = JSON.parseObject(response.body());
                    JSONObject errorObj = errorJson.getJSONObject("error");
                    if (errorObj != null && errorObj.getString("message") != null) {
                        errorMsg += "，" + errorObj.getString("message");
                    }
                } catch (Exception ignored) {
                }
                result.setMessage(errorMsg);
                return result;
            }

            JSONObject respJson = JSON.parseObject(response.body());
            String taskId = respJson.getString("id");

            // 缓存prompt到Redis，供视频完成后自动生成语音使用
            redisUtil.set(REDIS_KEY_PREFIX + "prompt:" + taskId, vo.getPrompt(), 86400);
            // 缓存是否AI自动生成音效标记
            redisUtil.set(REDIS_KEY_PREFIX + "aiAutoAudio:" + taskId, String.valueOf(aiAutoAudio), 86400);

            VideoTaskResultVo result = new VideoTaskResultVo();
            result.setTaskId(taskId);
            result.setStatus("PROCESSING");
            return result;
        } catch (Exception e) {
            log.error("提交视频生成任务异常", e);
            VideoTaskResultVo result = new VideoTaskResultVo();
            result.setStatus("FAIL");
            result.setMessage("提交任务异常: " + e.getMessage());
            return result;
        }
    }

    @Override
    public VideoTaskResultVo queryTask(String taskId) {
        AiChatConfig.ModelConfig config = aiChatConfig.getAiModelVideo();
        String apiKey = config.getApiKey();
        String apiHost = config.getApiHost();
        String baseUrl = apiHost.endsWith("/") ? apiHost.substring(0, apiHost.length() - 1) : apiHost;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/async-result/" + taskId))
                    .header("Authorization", "Bearer " + apiKey)
                    .GET()
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("查询视频任务HTTP失败: taskId={}, httpStatus={}", taskId, response.statusCode());
                VideoTaskResultVo result = new VideoTaskResultVo();
                result.setTaskId(taskId);
                result.setStatus("FAIL");
                result.setMessage("查询任务失败，状态码: " + response.statusCode());
                return result;
            }

            JSONObject respJson = JSON.parseObject(response.body());
            String taskStatus = respJson.getString("task_status");
            log.info("查询视频任务: taskId={}, 任务状态={}", taskId, taskStatus);

            VideoTaskResultVo result = new VideoTaskResultVo();
            result.setTaskId(taskId);

            if ("SUCCESS".equals(taskStatus)) {
                result.setStatus("SUCCESS");
                JSONArray videoResult = respJson.getJSONArray("video_result");
                if (videoResult != null && !videoResult.isEmpty()) {
                    JSONObject firstVideo = videoResult.getJSONObject(0);
                    String remoteVideoUrl = firstVideo.getString("url");
                    String remoteCoverUrl = firstVideo.getString("cover_image_url");
                    log.info("视频生成完成: taskId={}, videoUrl={}", taskId, remoteVideoUrl);

                    // 删除prompt缓存，只有删除成功的请求才执行后续处理（防止并发轮询重复存储）
                    String promptKey = REDIS_KEY_PREFIX + "prompt:" + taskId;
                    String aiAutoAudioKey = REDIS_KEY_PREFIX + "aiAutoAudio:" + taskId;
                    //update-begin---author:wangshuai---date:2026-03-23---for:【QQYUN-14960】【AI生成视频】报错了，实际视频已经生成完了---
                    Object cachedPrompt = redisTemplate.opsForValue().get(promptKey);
                    if (cachedPrompt != null) {
                        redisTemplate.delete(promptKey);
                    }
                    String prompt = cachedPrompt != null ? cachedPrompt.toString() : null;
                    // 判断是否采用AI自动生成音效
                    Object cachedAiAutoAudio = redisTemplate.opsForValue().get(aiAutoAudioKey);
                    if (cachedAiAutoAudio != null) {
                        redisTemplate.delete(aiAutoAudioKey);
                    }
                    //update-end---author:wangshuai---date:2026-03-23---for:【QQYUN-14960】【AI生成视频】报错了，实际视频已经生成完了---
                    boolean aiAutoAudio = cachedAiAutoAudio != null && "true".equals(cachedAiAutoAudio.toString());

                    // 下载视频和封面到本地，避免远程链接失效
                    if (remoteCoverUrl != null && !remoteCoverUrl.isBlank()) {
                        String localCoverPath = downloadToLocal(remoteCoverUrl, "cover_" + taskId + ".jpg");
                        result.setCoverUrl(localCoverPath);
                    }
                    String localVideoPath = downloadToLocal(remoteVideoUrl, "video_" + taskId + ".mp4");
                    result.setVideoUrl(localVideoPath);

                    // 仅在prompt缓存存在时执行一次（getAndDelete保证只有一个请求能获取到）
                    if (prompt != null && !prompt.isBlank()) {
                        if (aiAutoAudio) {
                            log.info("AI自动生成音效模式，跳过autoAddVoiceover: taskId={}", taskId);
                        } else {
                            log.info("非AI自动音效模式，开始autoAddVoiceover: taskId={}, prompt={}", taskId, prompt);
                            autoAddVoiceover(taskId, prompt, localVideoPath, result);
                        }
                        // 存入Redis记录
                        saveVideoToRedis(result, prompt);
                    } else {
                        log.info("未检测到prompt缓存（已处理过或旧任务），跳过自动配音: taskId={}", taskId);
                    }
                }
            } else if ("FAIL".equals(taskStatus)) {
                result.setStatus("FAIL");
                result.setMessage(respJson.getString("message"));
                log.error("视频生成失败: taskId={}, message={}", taskId, result.getMessage());
            } else {
                result.setStatus("PROCESSING");
                log.info("视频生成中: taskId={}, 等待完成...", taskId);
            }

            return result;
        } catch (Exception e) {
            log.error("查询视频任务异常, taskId={}", taskId, e);
            VideoTaskResultVo result = new VideoTaskResultVo();
            result.setTaskId(taskId);
            result.setStatus("FAIL");
            result.setMessage("查询任务异常: " + e.getMessage());
            return result;
        }
    }

    @Override
    public VideoTaskResultVo addVoiceover(String taskId, String prompt) {
        // 1. 先查询任务状态，获取视频URL
        VideoTaskResultVo queryResult = queryTask(taskId);
        if (!"SUCCESS".equals(queryResult.getStatus())) {
            queryResult.setMessage("视频任务尚未完成，当前状态: " + queryResult.getStatus());
            return queryResult;
        }
        String videoUrl = queryResult.getVideoUrl();
        if (videoUrl == null || videoUrl.isBlank()) {
            queryResult.setStatus("FAIL");
            queryResult.setMessage("视频URL为空");
            return queryResult;
        }

        // 2. 检测依赖工具是否可用，不可用则直接返回无声视频
        if (!isToolsAvailable()) {
            log.warn("服务器未安装 edge-tts 或 ffmpeg，跳过配音，直接返回无声视频。" +
                    "edge-tts={}, ffmpeg={}", edgeTtsPath, ffmpegPath);
            queryResult.setMessage("服务器未安装配音依赖（edge-tts/ffmpeg），返回原始无声视频");
            return queryResult;
        }

        try {
            String uploadPath = jeecgBaseConfig.getPath().getUpload();
            String bizPath = "ai_video";
            Path outputDir = Paths.get(uploadPath, bizPath);
            Files.createDirectories(outputDir);

            String timestamp = String.valueOf(System.currentTimeMillis());

            // 3. 下载无声视频
            Path silentVideo = downloadFile(videoUrl, outputDir.resolve("silent_" + timestamp + ".mp4"));
            log.info("无声视频已下载: {}", silentVideo);

            // 4. 生成旁白文案
            String narration = generateNarration(prompt);
            log.info("旁白文案: {}", narration);

            // 5. TTS生成语音
            Path audioPath = outputDir.resolve("voiceover_" + timestamp + ".mp3");
            generateTtsAudio(narration, audioPath);
            log.info("语音已生成: {}", audioPath);

            // 6. FFmpeg合成有声视频
            String finalFileName = "video_" + timestamp + ".mp4";
            Path finalVideo = outputDir.resolve(finalFileName);
            mergeVideoAudio(silentVideo, audioPath, finalVideo);
            log.info("合成视频已生成: {}", finalVideo);

            // 7. 清理临时文件
            Files.deleteIfExists(silentVideo);
            Files.deleteIfExists(audioPath);

            // 8. 返回结果（使用相对路径，通过静态资源映射访问）
            String dbPath = bizPath + "/" + finalFileName;
            queryResult.setVoiceoverVideoUrl(dbPath);
            queryResult.setNarration(narration);
            return queryResult;
        } catch (Exception e) {
            log.error("添加配音异常, taskId={}，降级返回无声视频", taskId, e);
            queryResult.setMessage("配音处理异常（已降级返回无声视频）: " + e.getMessage());
            return queryResult;
        }
    }

    /**
     * 检测 edge-tts 和 ffmpeg 是否可用
     */
    private boolean isToolsAvailable() {
        return edgeTtsPath != null && ffmpegPath != null;
    }

    @Override
    public Map<String, List<String>> getPresetPrompts() {
        return PRESET_PROMPTS;
    }

    @Override
    public List<JSONObject> getVideoRecords() {
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
    public boolean deleteVideoRecord(String recordId) {
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
            JSONObject obj = JSONObject.parseObject(item.toString());
            if (recordId.equals(obj.getString("id"))) {
                redisUtil.lRemove(redisKey, 1, item);
                return true;
            }
        }
        return false;
    }

    /**
     * 将视频生成记录存入Redis
     *
     * @param result 视频结果
     * @param prompt 用户原始prompt
     */
    private void saveVideoToRedis(VideoTaskResultVo result, String prompt) {
        try {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (loginUser == null) {
                log.warn("未获取到登录用户，跳过Redis存储");
                return;
            }
            String redisKey = REDIS_KEY_PREFIX + loginUser.getId();

            JSONObject record = new JSONObject();
            record.put("id", UUID.randomUUID().toString().replace("-", ""));
            record.put("taskId", result.getTaskId());
            record.put("videoUrl", result.getVideoUrl());
            record.put("coverUrl", result.getCoverUrl());
            record.put("status", result.getStatus());
            record.put("content", prompt);
            record.put("createTime", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            redisUtil.lSet(redisKey, record.toJSONString());
            log.info("视频记录已存入Redis列表: key={}", redisKey);
        } catch (Exception e) {
            log.warn("视频记录存入Redis失败，不影响主流程", e);
        }
    }

    /**
     * 将远程文件下载到本地上传目录，返回相对路径（ai_video/xxx）
     */
    private String downloadToLocal(String remoteUrl, String fileName) {
        try {
            String uploadPath = jeecgBaseConfig.getPath().getUpload();
            String bizPath = "video";
            Path outputDir = Paths.get(uploadPath, bizPath);
            Files.createDirectories(outputDir);

            Path localFile = outputDir.resolve(fileName);
            downloadFile(remoteUrl, localFile);
            log.info("远程文件已下载到本地: {} -> {}", remoteUrl, localFile);
            return bizPath + "/" + fileName;
        } catch (Exception e) {
            log.warn("下载远程文件到本地失败，返回原始URL: {}", remoteUrl, e);
            return remoteUrl;
        }
    }

    /**
     * 下载文件到本地
     */
    private Path downloadFile(String url, Path outputPath) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofMinutes(5))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofFile(outputPath));
        return outputPath;
    }

    /**
     * 调用智谱GLM生成旁白文案
     */
    private String generateNarration(String videoPrompt) throws IOException, InterruptedException {
        AiChatConfig.ModelConfig config = aiChatConfig.getAiModelVideo();
        String apiKey = config.getApiKey();
        String apiHost = config.getApiHost();
        String baseUrl = apiHost.endsWith("/") ? apiHost.substring(0, apiHost.length() - 1) : apiHost;

        JSONObject body = new JSONObject();
        body.put("model", "glm-4-flash");
        JSONArray messages = new JSONArray();

        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你是一位专业的视频旁白撰写者。根据用户给出的视频画面描述，"
                + "撰写一段简短的旁白配音文案（30-50字），语言优美、富有感染力，适合作为视频解说词。"
                + "只输出旁白文案本身，不要加引号或其他说明。");

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", "视频画面：" + videoPrompt);

        messages.add(systemMsg);
        messages.add(userMsg);
        body.put("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("旁白生成失败，状态码: " + response.statusCode());
        }

        JSONObject respJson = JSON.parseObject(response.body());
        return respJson.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
    }

    /**
     * 使用 edge-tts 将文本转为语音
     */
    private void generateTtsAudio(String text, Path audioPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                edgeTtsPath,
                "--voice", "zh-CN-YunyangNeural",
                "--text", text,
                "--write-media", audioPath.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("edge-tts 执行失败: {}", output);
            throw new RuntimeException("edge-tts 执行失败，退出码: " + exitCode);
        }
    }

    /**
     * 视频生成成功后，自动生成语音并合并（参照 addVoiceover 逻辑）
     * 使用 TTS API（VoiceApiHelper）生成语音，ffmpeg 合并
     * 失败时降级返回无声视频，不影响主流程
     *
     * @param taskId         任务ID
     * @param prompt         用户原始prompt
     * @param localVideoPath 本地无声视频相对路径（如 video/video_xxx.mp4）
     * @param result         结果对象，成功时更新 videoUrl 和 narration
     */
    private void autoAddVoiceover(String taskId, String prompt, String localVideoPath, VideoTaskResultVo result) {
        log.info(">>> autoAddVoiceover 开始: taskId={}, ffmpeg={}, localVideoPath={}", taskId, ffmpegPath, localVideoPath);
        if (ffmpegPath == null) {
            log.info("ffmpeg不可用，跳过自动配音: taskId={}", taskId);
            return;
        }

        try {
            String uploadPath = jeecgBaseConfig.getPath().getUpload();
            String bizPath = "video";
            Path outputDir = Paths.get(uploadPath, bizPath);
            Files.createDirectories(outputDir);

            String timestamp = String.valueOf(System.currentTimeMillis());

            // 1. 生成旁白文案
            log.info("自动配音第1步-生成旁白文案: taskId={}", taskId);
            String narration = generateNarration(prompt);
            log.info("自动配音旁白文案: {}", narration);

            // 2. TTS API 生成语音
            Path audioPath = outputDir.resolve("auto_voice_" + taskId + "_" + timestamp + ".wav");
            log.info("自动配音第2步-TTS生成语音: taskId={}, audioPath={}", taskId, audioPath);
            voiceApiHelper.generateAudio(narration, audioPath);
            log.info("自动配音语音已生成: {}, 文件存在={}", audioPath, Files.exists(audioPath));

            // 3. FFmpeg 合并视频和音频
            Path silentVideoFile = outputDir.resolve("video_" + taskId + ".mp4");
            log.info("自动配音第3步-FFmpeg合并: silentVideo={} (存在={})", silentVideoFile, Files.exists(silentVideoFile));
            String mergedFileName = "video_voiced_" + taskId + "_" + timestamp + ".mp4";
            Path mergedVideo = outputDir.resolve(mergedFileName);
            mergeVideoAudio(silentVideoFile, audioPath, mergedVideo);
            log.info("自动配音合成完成: {}, 文件存在={}", mergedVideo, Files.exists(mergedVideo));

            // 4. 更新结果为有声视频
            result.setVideoUrl(bizPath + "/" + mergedFileName);
            result.setNarration(narration);

            // 5. 清理临时音频文件
            Files.deleteIfExists(audioPath);

            log.info(">>> autoAddVoiceover 完成: taskId={}, videoUrl={}", taskId, result.getVideoUrl());
        } catch (Exception e) {
            log.error(">>> autoAddVoiceover 失败，降级返回无声视频: taskId={}", taskId, e);
        }
    }

    /**
     * 使用 FFmpeg 合并视频和音频
     */
    private void mergeVideoAudio(Path videoPath, Path audioPath, Path outputPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                ffmpegPath,
                "-i", videoPath.toAbsolutePath().toString(),
                "-i", audioPath.toAbsolutePath().toString(),
                "-c:v", "copy",
                "-c:a", "aac",
                "-shortest",
                "-y",
                outputPath.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("FFmpeg 合成失败: {}", output);
            throw new RuntimeException("FFmpeg 合成失败，退出码: " + exitCode);
        }
    }

    /**
     * 将 size（如 "1920x1080"）转为 vidu2 的 aspect_ratio 格式（如 "16:9"）
     * 支持的比例：16:9、9:16、1:1，无法识别时默认 16:9
     */
    private String convertSizeToAspectRatio(String size) {
        if (size == null || size.isBlank()) {
            return "16:9";
        }
        // 已经是比例格式则直接返回
        if (size.matches("\\d+:\\d+")) {
            return size;
        }
        // 解析 WxH 格式
        String[] parts = size.toLowerCase().split("x");
        if (parts.length == 2) {
            try {
                int w = Integer.parseInt(parts[0].trim());
                int h = Integer.parseInt(parts[1].trim());
                if (w == h) {
                    return "1:1";
                } else if (w > h) {
                    return "16:9";
                } else {
                    return "9:16";
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return "16:9";
    }

    /**
     * 查找可用的命令路径，找不到返回null
     */
    private static String findCommand(String[] versionFlag, String... candidates) {
        for (String path : candidates) {
            try {
                List<String> cmd = new ArrayList<>();
                cmd.add(path);
                cmd.addAll(List.of(versionFlag));
                Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
                p.getInputStream().readAllBytes();
                p.waitFor();
                if (p.exitValue() == 0) return path;
            } catch (Exception ignored) {}
        }
        return null;
    }
}
