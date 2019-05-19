package org.jeecg.modules.message.entity;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sms")
public class SysMessage extends JeecgEntity {
	/**推送内容*/
	@Excel(name = "推送内容", width = 15)
	private java.lang.Object esContent;
	/**推送所需参数Json格式*/
	@Excel(name = "推送所需参数Json格式", width = 15)
	private java.lang.String esParam;
	/**接收人*/
	@Excel(name = "接收人", width = 15)
	private java.lang.String esReceiver;
	/**推送失败原因*/
	@Excel(name = "推送失败原因", width = 15)
	private java.lang.String esResult;
	/**发送次数*/
	@Excel(name = "发送次数", width = 15)
	private java.lang.Integer esSendNum;
	/**推送状态 0未推送 1推送成功 2推送失败*/
	@Excel(name = "推送状态 0未推送 1推送成功 2推送失败", width = 15)
	@Dict(dicCode = "msgSendStatus")
	private java.lang.String esSendStatus;
	/**推送时间*/
	@Excel(name = "推送时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date esSendTime;
	/**消息标题*/
	@Excel(name = "消息标题", width = 15)
	private java.lang.String esTitle;
	/**推送方式：1短信 2邮件 3微信*/
	@Excel(name = "推送方式：1短信 2邮件 3微信", width = 15)
	@Dict(dicCode = "msgType")
	private java.lang.String esType;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String remark;
}
