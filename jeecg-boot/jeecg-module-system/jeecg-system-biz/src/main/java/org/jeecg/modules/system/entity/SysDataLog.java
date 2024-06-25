package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 系统数据日志
 * @author: jeecg-boot
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Slf4j
public class SysDataLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.ASSIGN_ID)
    /**
     * id
     */
	private String id;

    /**
     * 创建人登录名称
     */
	private String createBy;

    /**
     * 创建人真实名称
     */
	private String createName;

    /**
     * 创建日期
     */
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

    /**
     * 更新人登录名称
     */
	private String updateBy;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")

    /**
     * 更新日期
     */
    private Date updateTime;

    /**
     * 表名
     */
    private String dataTable;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 数据内容
     */
    private String dataContent;

    /**
     * 版本号
     */
    private String dataVersion;


    //update-begin-author:taoyan date:2022-7-26 for: 用于表单评论记录日志 区分数据
    /**
     * 类型
     */
    private String type;
    //update-end-author:taoyan date:2022-7-26 for: 用于表单评论记录日志 区分数据

    /**
     * 通过 loginUser 设置 createName
     */
    public void autoSetCreateName() {
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            this.setCreateName(sysUser.getRealname());
        } catch (Exception e) {
            log.warn("SecurityUtils.getSubject() 获取用户信息异常：" + e.getMessage());
        }
    }

}
