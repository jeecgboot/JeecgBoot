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
    String UNPUBLISHED_STATUS_0 = "0";

    /**
     * 已发布
     */
    String PUBLISHED_STATUS_1 = "1";

    /**
     * 撤销
     */
    String REVOKE_STATUS_2 = "2";

    /**
     * app端推送会话标识后缀
     */
    String  APP_SESSION_SUFFIX = "_app";


	/**流程催办——系统通知消息模板*/
    String TZMB_BPM_CUIBAN = "bpm_cuiban";
	/**流程催办——邮件通知消息模板*/
    String TZMB_BPM_CUIBAN_EMAIL = "bpm_cuiban_email";
	/**标准模板—系统消息通知*/
    String TZMB_SYS_TS_NOTE = "sys_ts_note";
	/**流程超时提醒——系统通知消息模板*/
    String TZMB_BPM_CHAOSHI_TIP = "bpm_chaoshi_tip";
}
