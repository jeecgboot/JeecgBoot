package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: task history
 * @Author: jeecg-boot
 * @Date: 2023-08-22
 * @Version: V1.0
 */
@Data
@TableName("task_history")
@ApiModel(value = "task_history对象", description = "task history")
public class TaskHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * ongoing
     */
    @Excel(name = "ongoing", width = 15)
    @ApiModelProperty(value = "ongoing")
    private java.lang.Integer ongoing;
    /**
     * task code
     */
    @Excel(name = "task code", width = 15)
    @ApiModelProperty(value = "task id")
    private java.lang.String taskCode;

    public TaskHistory(String createBy, int ongoing, String taskCode) {
        this.createBy = createBy;
        this.ongoing = ongoing;
        this.taskCode = taskCode;
    }

    public TaskHistory() {
    }

    public enum TaskStatus {

        RUNNING(1),
        SUCCESS(0),
        CANCELLED(-1);
        private final int code;

        TaskStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
