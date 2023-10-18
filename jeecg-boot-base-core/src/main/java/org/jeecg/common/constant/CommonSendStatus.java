package org.jeecg.common.constant;

/**
 * 	系统通告 - 发布状态
 * @Author LeeShaoQing
 *
 */
public interface CommonSendStatus {

    /**
     * 未发布
     */
    public static final String UNPUBLISHED_STATUS_0 = "0";

    /**
     * 已发布
     */
	public static final String PUBLISHED_STATUS_1 = "1";

    /**
     * 撤销
     */
	public static final String REVOKE_STATUS_2 = "2";

    /**
     * app端推送会话标识后缀
     */
	public static final String  APP_SESSION_SUFFIX = "_app";


	/**-----【流程相关通知模板code】------------------------------------------------------------*/
	/**流程催办——系统通知消息模板*/
	public static final String TZMB_BPM_CUIBAN = "bpm_cuiban";
	/**流程抄送——系统通知消息模板*/
	public static final String TZMB_BPM_CC = "bpm_cc";
	/**流程催办——邮件通知消息模板*/
	public static final String TZMB_BPM_CUIBAN_EMAIL = "bpm_cuiban_email";
	/**标准模板—系统消息通知*/
	public static final String TZMB_SYS_TS_NOTE = "sys_ts_note";
	/**流程超时提醒——系统通知消息模板*/
	public static final String TZMB_BPM_CHAOSHI_TIP = "bpm_chaoshi_tip";
	/**-----【流程相关通知模板code】-----------------------------------------------------------*/

	/**
	 * 系统通知拓展参数（比如：用于流程抄送和催办通知，这里额外传递流程跳转页面所需要的路由参数）
	 */
	public static final String MSG_ABSTRACT_JSON = "msg_abstract";
}
