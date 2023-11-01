package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 加好友的回话记录
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(SayHello.TABLE_NAME)
public class SayHello extends BaseModel<SayHello> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_say_hello";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送方
     */
    private Integer fromId;

    /**
     * 接收方
     */
    private Integer toId;

    private String msg;
    /**
     * 来源，对应friend的addType
     */
    private Integer resource;
    /**
     * 有效的
     */
    private Boolean isValid;
    /**
     * 状态：0：待验证，1：接受，2：拒绝
     */
    private Integer status;

    private Integer type;

    private Long tsCreate;
    private Long tsDeal;
    //回话列表
    @TableField(exist = false)
    private List<SayHelloReply> replies;
    //发送人
    @TableField(exist = false)
    private User fromUser;
    //接收人
    @TableField(exist = false)
    private User toUser;
    //状态
    public enum Status{
        Waiting(0),Accept(1),Reject(2);
        int code;
        String name;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
    //类型
    public enum Type{
        AddFriend(0,"添加好友"),FollowUser(1,"关注用户");
        int code;
        String name;

        Type(int code,String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
    }

}
