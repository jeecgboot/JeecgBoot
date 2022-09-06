package org.jeecg.modules.system.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description: 系统部门VO
 * @author: jeecg-boot
 */
@Data
public class SysDepartUsersVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**部门id*/
	private String depId;
	/**对应的用户id集合*/
	private List<String> userIdList;
	public SysDepartUsersVO(String depId, List<String> userIdList) {
		super();
		this.depId = depId;
		this.userIdList = userIdList;
	}
    //update-begin--Author:kangxiaolin  Date:20190908 for：[512][部门管理]点击添加已有用户失败修复--------------------

	public SysDepartUsersVO(){

	}
    //update-begin--Author:kangxiaolin  Date:20190908 for：[512][部门管理]点击添加已有用户失败修复--------------------

}
