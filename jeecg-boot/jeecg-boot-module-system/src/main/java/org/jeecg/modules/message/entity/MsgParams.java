package org.jeecg.modules.message.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 发送消息实体
 * @author: jeecg-boot
 */
@Data
public class MsgParams implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	/**
     * 消息类型
     */
	private String msgType;

    /**
     * 消息接收方
     */
	private String receiver;

    /**
     * 消息模板码
     */
	private String templateCode;

    /**
     * 测试数据
     */
	private String testData;
	
}
