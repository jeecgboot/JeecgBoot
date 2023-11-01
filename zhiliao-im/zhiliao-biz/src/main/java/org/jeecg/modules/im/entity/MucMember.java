package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 群组成员
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucMember.TABLE_NAME)
public class MucMember extends BaseModel<MucMember> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_member";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer mucId;

    /**
     * 群里昵称
     */
    private String nickname;
    //头衔
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String title;

    /**
     * 聊天背景
     */
    private String backImg;

    /**
     * 加入时间
     */
    private Long tsJoin;
    //加入方式
    private Integer joinType;

    /**
     * 聊天置顶时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long tsPin;

    /**
     * 消息免打扰
     */
    private Boolean isNoDisturb;
    //标记为未读
    private Boolean isUnread;
    //阅后即焚
    private Boolean isReadDel;

    /**
     * 角色；0：僵尸号，1：普通用户，2：管理员，3：群主
     */
    private Integer role;
    /**
     * 消息归档
     */
    private Boolean isMsgArchive;
    /**
     * 隐藏对话
     */
    private Boolean isHide;
    /**
     * 该时间之后的消息可见
     * 用户进群时，如果群设置不显示进群前的消息，则该值为入群时间，否则为空
     */
    private Long tsMsgVisible;
    /**
     * 群组等级
     */
    private Integer levelId;
    /**
     * 未读消息
     */
    private Integer unreadCount;
    /**
     * 积分
     */
    private Integer coin;
    /**
     * 更新时间
     */
    private Long tsUpdate;

    /**
     * 截止禁言时间
     */
    private Long tsMute;
    //禁言起始时间
    private Long tsMuteBegin;
    //禁言类型
    private Integer muteType;
    /**
     * 状态；0=正常，1=被踢除，2=自己退出，3=群组解散
     */
    private Integer status;
    //被谁踢了，成员id
    private Integer kicker;
    //被踢/退出时间
    private Long tsQuit;

    //群聊备注，仅自己可见
    private String remark;
    //最后一条已读消息id
    private Long lastAckId;


    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private MucPermission permission;

    public enum Role{
        Zombie(0),Member(1),Manager(2),Master(3);
        private final int code;
        Role(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
    public enum Status{
        Normal(0),//正常
        Kicked(1),//被踢
        Quit(2),//主动退出
        Dismiss(3);//群组解散
        private final int code;
        Status(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
    public enum JoinType{
        Invite(0),Create(1),ConsoleAdd(2);
        private int code;
        JoinType(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
