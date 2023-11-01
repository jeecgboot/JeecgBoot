package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 群聊设置
 * </p>
 *
 * @author junko
 * @since 2023-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_config")
public class MucConfig extends BaseModel<MucConfig> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer mucId;

    /**
     * 最大群人数
     */
    private Integer maxMemberCount;

    /**
     * 禁言通知 0：不通知，1：全部通知，2：通知群主管理员及本人
     */
    private Integer muteNotice;

    /**
     * 踢人通知 0：不通知，1：全部通知，2：通知群主管理员及本人
     */
    private Integer kickNotice;

    /**
     * 退出通知 0：不通知 1：全部通知 2：通知群主管理员
     */
    private Integer quitNotice;
    /**
     * 撤回通知 0：不通知，1：全部通知，2：通知群主管理员及本人
     */
    private Integer revokeNotice;
    /**
     * 消息撤回时限
     */
    private Integer revokeDuration;

    /**
     * 1：公:0：私
     */
    private Boolean isPublic;

    /**
     * 显示消息已读人数
     */
    private Boolean isShowRead;

    /**
     * 显示群昵称
     */
    private Boolean isShowNickname;

    /**
     * 新成员进群后允许发言
     */
    private Boolean isAllowTalkAfterJoin;

    /**
     * 入群之前的消息可见
     */
    private Boolean isShowMsgBeforeJoin;

    /**
     * 新用户注册后加入该群
     */
    private Boolean isDefaultJoin;
    //移除成员时撤回他的历史发言
    private Boolean isRevokeAllWhenKicked;

    /**
     * 加群确认
     */
    private Boolean isJoinVerify;

    /**
     * 显示群成员列表
     */
    private Boolean isShowMemberList;

    /**
     * 群成员修改昵称通知所有人
     */
    private Boolean isUpdateNicknameNotify;
    //查看群成员信息
    private Boolean viewMember;
    //显示群人数
    private Boolean showMemberCount;
    //显示在线人数
    private Boolean showOnlineCount;
    //邀请新成员
    private Boolean invite;
    //修改群聊中的昵称
    private Boolean modifyNickname;
    //置顶消息
    private Boolean pin;
    //聊天、消息页截图
    private Boolean capture;
    //消息频率，单位：秒
    private Integer msgRate;
    //消息数
    private Integer msgCount;
    //发送图片
    private Boolean sendImage;
    //发送视频
    private Boolean sendVideo;
    //发送gif
    private Boolean sendGif;
    //发送贴纸
    private Boolean sendSticker;
    //发送语音
    private Boolean sendVoice;
    //发送位置
    private Boolean sendLocation;
    //发送红包
    private Boolean sendRedPack;
    //发送链接
    private Boolean sendLink;
    //发送名片
    private Boolean sendCard;
    //发送文件
    private Boolean sendFile;

    /**
     * 欢迎语
     */
    private String welcomes;

    public enum Allow{
        none(0), all(1), manager(2);
        private Integer code;

        public Integer getCode() {
            return code;
        }
        Allow(Integer code){
            this.code = code;
        }
    }
}
