package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 部门角色人员信息
 * @Author: jeecg-boot
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
@TableName("sys_depart_role_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description="sys_depart_role_user对象")
public class SysDepartRoleUser {
    
	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @Schema(description = "用户id")
	private java.lang.String userId;
	/**角色id*/
	@Excel(name = "角色id", width = 15)
    @Schema(description = "角色id")
	private java.lang.String droleId;

	public SysDepartRoleUser() {

	}

	public SysDepartRoleUser(String userId, String droleId) {
		this.userId = userId;
		this.droleId = droleId;
	}
}
