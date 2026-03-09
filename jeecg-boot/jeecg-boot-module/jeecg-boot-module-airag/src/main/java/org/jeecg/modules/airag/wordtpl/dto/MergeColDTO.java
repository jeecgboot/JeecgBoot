package org.jeecg.modules.airag.wordtpl.dto;

import lombok.Data;

/**
 * 合并列DTO
 * @author chenrui
 * @date 2025/7/4 18:36
 */
@Data
public class MergeColDTO {

	/**
	 * 合并列的行号
	 */
	private int row;

	/**
	 * 合并列的起始列号
	 */
	private int from;

	/**
	 * 合并列的结束列号
	 */
	private int to;
}
