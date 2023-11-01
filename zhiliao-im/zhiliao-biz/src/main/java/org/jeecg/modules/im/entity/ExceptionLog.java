package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统异常日志
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(ExceptionLog.TABLE_NAME)
public class ExceptionLog extends BaseModel<ExceptionLog> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_exception_log";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ip;

    private String ipInfo;

    private Integer userId;

    /**
     * 请求资源
     */
    private String uri;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String reqParam;

    /**
     * 异常名称
     */
    private String excName;
    /**
     * 异常信息
     */
    private String excMessage;

    private Long tsCreate;

    @TableField(exist = false)
    private User user;
}
