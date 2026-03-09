package org.jeecg.modules.airag.wordtpl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: word模版管理
 * @Author: jeecg-boot
 * @Date: 2025-07-04
 * @Version: V1.0
 */
@Data
@TableName("aigc_word_template")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "word模版管理")
public class EoaWordTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private String sysOrgCode;
    /**
     * 模版名称
     */
    @Excel(name = "模版名称", width = 15)
    @Schema(description = "模版名称")
    private String name;
    /**
     * 模版编码
     */
    @Excel(name = "模版编码", width = 15)
    @Schema(description = "模版编码")
    private String code;
    /**
     * 页眉
     */
    @Excel(name = "页眉", width = 15)
    @Schema(description = "页眉")
    private String header;
    /**
     * 页脚
     */
    @Excel(name = "页脚", width = 15)
    @Schema(description = "页脚")
    private String footer;
    /**
     * 主体内容
     */
    @Excel(name = "主体内容", width = 15)
    @Schema(description = "主体内容")
    private String main;
    /**
     * 页边距
     */
    @Excel(name = "页边距", width = 15)
    @Schema(description = "页边距")
    private String margins;
    /**
     * 宽度
     */
    @Excel(name = "宽度", width = 15)
    @Schema(description = "宽度")
    private Integer width;
    /**
     * 高度
     */
    @Excel(name = "高度", width = 15)
    @Schema(description = "高度")
    private Integer height;
    /**
     * 纸张方向 vertical纵向 horizontal横向
     */
    @Excel(name = "纸张方向 vertical纵向 horizontal横向", width = 15)
    @Schema(description = "纸张方向 vertical纵向 horizontal横向")
    private String paperDirection;
    /**
     * 水印
     */
    @Excel(name = "水印", width = 15)
    @Schema(description = "水印")
    private String watermark;
}
