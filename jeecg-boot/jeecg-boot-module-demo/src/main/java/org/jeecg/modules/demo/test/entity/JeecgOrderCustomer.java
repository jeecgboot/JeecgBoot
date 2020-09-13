package org.jeecg.modules.demo.test.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 订单客户
 * @Author: jeecg-boot
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Data
@TableName("jeecg_order_customer")
public class JeecgOrderCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
    @TableId(type = IdType.ASSIGN_ID)
	private java.lang.String id;
	/**客户名*/
	@Excel(name="客户名字",width=15)
	private java.lang.String name;
	/**性别*/
	private java.lang.String sex;
	/**身份证号码*/
	@Excel(name="身份证号码",width=15)
	private java.lang.String idcard;
	/**身份证扫描件*/
	private java.lang.String idcardPic;
	/**电话1*/
	@Excel(name="电话",width=15)
	private java.lang.String telphone;
	/**外键*/
	private java.lang.String orderId;
	/**创建人*/
	private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**修改人*/
	private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
