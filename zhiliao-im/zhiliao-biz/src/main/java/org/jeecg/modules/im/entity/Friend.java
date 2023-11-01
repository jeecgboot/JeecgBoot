package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 好友
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Friend.TABLE_NAME)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Friend extends BaseModel<Friend> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_friend";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户id
     */
    private Integer userId;

    /**
     * 对方id
     */
    private Integer toUserId;

    /**
     * 星标朋友
     */
    private Boolean isStar;

    /**
     * 备注
     */
    private String remark;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 描述
     */
    private String info;

    /**
     * 朋友标签，最多10个，逗号隔开
     */
    private String tagIds;
    private String tagNames;
    //翻译
    private String tr;

    /**
     * 消息归档
     */
    private Boolean isMsgArchive;
    /**
     * 隐藏对话
     */
    private Boolean isHide;
    /**
     * 免打扰
     */
    private Boolean isNoDisturb;
    //阅后即焚
    private Boolean isReadDel;
    /**
     * 添加时间
     */
    private Long tsCreate;
    /**
     * 置顶时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long tsPin;
    /**
     * 成为好友时间
     */
    private Long tsFriend;
    private Long tsMsgVisible;

    /**
     * 更新时间
     */
    private Long tsUpdate;

    /**
     * 最后一次聊天时间
     */
    private Long tsLastTalk;

    /**
     * 添加方式：0=账号，1=手机号，2=昵称，3=用户名，4=名片，5=群聊，6=扫一扫，7=系统添加，8=未添加
     */
    private Integer addType;

    /**
     * 状态：状态：0=关注；1=好友；2=陌生人 ；3=黑名单；4=单向删除；5=双向删除；6=粉丝
     */
    private Integer status;

    /**
     * 权限：0=仅聊天；1=朋友圈；2：不让ta看，3：不看ta
     */
    private Integer privilege;
    //标记为未读
    private Boolean isUnread;
    //拉黑时间
    private Long tsBlack;
    //被拉黑时间
    private Long tsBeenBlack;

    //最后一条已读消息id
    private Long lastAckId;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private User toUser;

    //状态
    public enum Status{
        Follow(0,"关注"),
        Friend(1,"好友"),
        Stranger(2,"陌生人"),
        Delete(5,"双向删除"),
        Fans(6,"粉丝"),
        Ask(7,"等待通过");
        int code;
        String name;
        Status(int code,String name) {
            this.code = code;
            this.name = name;
        }

        public String getName() {
            return name;
        }
        public int getCode() {
            return code;
        }
    }
    //添加方式
    public enum AddType{
        Account(0,"账号"),
        Mobile(1,"手机号"),
        Nickname(2,"昵称"),
        Username(3,"用户名"),
        Card(4,"名片"),
        Muc(5,"群聊"),
        Scan(6,"扫一扫"),
        System(7,"系统添加"),
        Not(8,"未添加");
        int code;
        String type;
        AddType(int code,String type) {
            this.code = code;
            this.type = type;
        }

        public String getType() {
            return type;
        }
        public int getCode() {
            return code;
        }


    }
}