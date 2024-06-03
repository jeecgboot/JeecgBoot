package org.jeecg.modules.system.vo.lowapp;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
* @Description: 应用用户与部门 用户导出/导入实体类
*
* @author: wangshuai
* @date: 2023/6/14 16:42
*/
@Data
public class AppExportUserVo {

    /**用户编号*/
    @Excel(name="用户编号",width=30)
    private String id;

    /**姓名*/
    @Excel(name="姓名",width=30)
    private String realname;
    
    /**职位*/
    @Excel(name = "职位",width = 30)
    private String position;

    /**部门*/
    @Excel(name = "部门",width = 30)
    private String depart;

    /**工号*/
    @Excel(name = "工号",width = 30)
    private String workNo;
    
    /**手机号*/
    @Excel(name = "手机号",width = 30)
    private String phone;
    
    /**邮箱*/
    @Excel(name = "邮箱",width = 30)
    private String email;

    /**加入时间*/
    @Excel(name = "加入时间",width = 30, format = "yyyy-MM-dd")
    private Date createTime;
}
