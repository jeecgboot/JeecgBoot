package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户设置
 * </p>
 *
 * @author junko
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(UserSetting.TABLE_NAME)
public class UserSetting extends BaseModel<UserSetting> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_user_setting";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    //手机号搜索到我
    private Boolean mobileSearch;
    //账号搜索到我
    private Boolean accountSearch;
    //用户名搜索到我
    private Boolean usernameSearch;
    //昵称搜索到我
    private Boolean nicknameSearch;
    //好友上限数量
    private Integer maxFriend;
    //每日添加好友上限
    private Integer maxFriendPerDay;
    //群聊创建上限
    private Integer maxMucCreate;
    //群聊管理上限
    private Integer maxMucManage;
    //群聊加入上限
    private Integer maxMucJoin;
    //允许账号添加
    private Boolean accountAdd;
    //允许手机号添加
    private Boolean mobileAdd;
    //允许通过昵称加我
    private Boolean nicknameAdd;
    //允许通过用户名加我
    private Boolean usernameAdd;
    //允许名片添加
    private Boolean cardAdd;
    //允许群聊添加
    private Boolean mucAdd;
    //允许扫码添加
    private Boolean scanAdd;
    //全局禁止加好友
    private Boolean isNoAddFriend;
    //全局禁止被加好友
    private Boolean isNoBeAddFriend;
    //全局禁止加群
    private Boolean isNoJoinMuc;
    //允许修改昵称
    private Boolean isNicknameCanModify;
    //允许发送红包
    private Boolean allowSendRedPack;
    //允许转账
    private Boolean allowTransfer;
    //允许视频通话
    private Boolean enableVideoCall;
    //消息通知
    private Boolean msgNotify;
    //消息预览
    private Boolean msgPreview;
    //消息声音
    private int msgSound;
    //群组消息通知
    private Boolean mucMsgNotify;
    //群组消息预览
    private Boolean mucMsgPreview;
    //群组消息声音
    private int mucMsgSound;
    //应用内提示音
    private Boolean appInnerAlert;
    //应用内消息预览
    private Boolean appInnerPreview;
    //应用内振动
    private Boolean appInnerVibration;
    //在线可见
    private  Integer onlineVisibility;

    public enum OnlineVisibility{
        None(0),//没有人
        Contact(1),//联系人
        All(2);//所有人
        private int code;
        OnlineVisibility(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
