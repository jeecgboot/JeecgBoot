package org.jeecg.modules.persona.lsry.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 留守人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
@TableName("zz_p_lsry")
public class ZzPLsry implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**创建人*/
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**更新人*/
    private java.lang.String updateBy;
	/**更新日期*/
    private java.lang.String updateTime;
	/**所属部门*/
    private java.lang.String sysOrgCode;
	/**姓名*/
    private java.lang.String name;
	/**身份证*/
    private java.lang.String sfz;
	/**曾用名*/
    private java.lang.String oldName;
	/**性别*/
    private java.lang.String sex;
	/**照片*/
    private java.lang.String photo;
	/**出生日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date birthday;
	/**民族*/
    private java.lang.String zm;
	/**籍贯*/
    private java.lang.String jg;
	/**婚姻状况*/
    private java.lang.String hyzk;
	/**政治面貌*/
    private java.lang.String zzmm;
	/**学历*/
    private java.lang.String xl;
	/**宗教信仰*/
    private java.lang.String zjxy;
	/**职业类别*/
    private java.lang.String zylb;
	/**职业*/
    private java.lang.String zy;
	/**户籍门（楼）祥*/
    private java.lang.String hjxq;
	/**现住门（楼）祥*/
    private java.lang.String xzdxq;
	/**联系方式*/
    private java.lang.String lxfs;
	/**健康状况*/
    private java.lang.String jkzk;
	/**个人年收入*/
    private java.lang.String grnsr;
	/**家庭年收入*/
    private java.lang.String jtnsr;
	/**人户一致标识*/
    private java.lang.String rhyzbz;
	/**留守人员类型*/
    private java.lang.String lsrylx;
	/**困难及诉求*/
    private java.lang.String knjsq;
	/**帮扶情况*/
    private java.lang.String bfqk;
}
