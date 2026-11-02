package org.jeecg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("jeecgAiChatConfig")
@ConfigurationProperties(prefix = "jeecg.ai-chat")
public class AiChatConfig {

    /**
     * 默认聊天模型名称（用于判断是否支持Tool Calling等）
     */
    private String model;

    /**
     * skills配置文件路径
     */
    private String skillsDir;
    /**
     * shell命令行配置文件路径
     */
    private String skillsShellDir;
    
    /**
     * AI绘图(文生图)
     */
    private ModelConfig aiModelDraw = new ModelConfig();

    /**
     * AI图生(图绘画)
     */
    private ModelConfig aiModelPicDraw = new ModelConfig();

    /**
     * AI语音
     */
    private VoiceModelConfig aiModelVoice = new VoiceModelConfig();

    /**
     * AI视频
     */
    private VideoModelConfig aiModelVideo = new VideoModelConfig();

    /**
     * AI默认向量模型
     */
    private ModelConfig aiModelEmbed = new ModelConfig();

	/**
	 * AI 聊天助手访问限流配置
	 */
	private RateLimitConfig rateLimit = new RateLimitConfig();

	/**
	 * AI 聊天助手访问限流参数
	 *
	 * @author scott
	 * @since 2026-09-01 issues/9871 AI聊天助手访问限流配置优化
	 */
	@Data
	public static class RateLimitConfig {
		/** 每会话每分钟最多发送消息次数，为空或小于等于 0 时不限制 */
		private Integer sendPerSessionPerMinute;

		/** 每 IP 每分钟最多发送消息次数，为空或小于等于 0 时不限制 */
		private Integer sendPerIpPerMinute;

		/** 每会话每小时最多上传文件次数，为空或小于等于 0 时不限制 */
		private Integer uploadPerSessionPerHour;
	}

    @Data
    public static class ModelConfig {
        /**
         * 使用的模型
         */
        private String model;
        /**
         * api秘钥
         */
        private String apiKey;
        /**
         * api域名
         */
        private String apiHost;
        /**
         * 超时时间
         */
        private int timeout = 60;

        /**
         * 供应商
         */
        private String provider = "QWEN"; //AiModelFactory.AIMODEL_TYPE_QWEN
    }

    @Data
    public static class VideoModelConfig extends ModelConfig {
        /**
         * ffmpeg 可执行文件路径，为空时自动查找
         */
        private String ffmpegPath;
        /**
         * edge-tts 可执行文件路径，为空时自动查找
         */
        private String edgeTtsPath;
    }

    @Data
    public static class VoiceModelConfig extends ModelConfig {
        /**
         * 默认声色
         */
        private String voice = "alloy";
        /**
         * 默认倍速，范围0.25~4.0
         */
        private double speed = 1.0;
        /**
         * 默认音量增益(dB)
         */
        private double volume = 0.0;
    }

}
