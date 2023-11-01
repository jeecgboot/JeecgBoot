package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 加好友回话
 * </p>
 *
 * @author junko
 * @since 2021-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(SayHelloReply.TABLE_NAME)
public class SayHelloReply extends BaseModel<SayHelloReply> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_say_hello_reply";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 请求id
     */
    private Integer helloId;

    /**
     * 回复内容
     */
    private String msg;

    /**
     * 是请求方发送的
     */
    private Boolean isSend;

    /**
     * 发送时间
     */
    private Long tsCreate;

    @TableField(exist = false)
    private User sender;

}
