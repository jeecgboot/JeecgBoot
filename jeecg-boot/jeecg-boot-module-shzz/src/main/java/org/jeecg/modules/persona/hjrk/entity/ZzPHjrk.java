package org.jeecg.modules.persona.hjrk.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 户籍人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Data
@TableName("zz_p_hjrk")
public class ZzPHjrk implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**创建人*/
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**更新人*/
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
	/**所属部门*/
    private java.lang.String sysOrgCode;
	/**人户一致标识*/
    private java.lang.String rhyz;
	/**户号*/
    private java.lang.String hh;
	/**户主身份证*/
    private java.lang.String hzSfz;
	/**户主姓名*/
    private java.lang.String hzName;
	/**与户主关系*/
    private java.lang.String hzGx;
	/**户主联系方式*/
    private java.lang.String hzLxfs;
}
