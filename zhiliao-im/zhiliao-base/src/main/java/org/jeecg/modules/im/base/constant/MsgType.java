package org.jeecg.modules.im.base.constant;

import lombok.Getter;

/**
 * xmpp消息类型
 */
public enum MsgType {
    text(1, "文字", true, true),
    image(2, "图片", true, true),
    voice(3, "语音", true, true),
    location(4, "位置", true, true),
    gif(5, "gif", true, true),
    video(6, "视频", true, true),
    audio(7, "音频", true, true),
    card(8, "名片", true, true),
    file(9, "文件", true, true),
    link(10, "链接", true, true),
    shareLink(11, "分享链接", true, true),
    shake(12, "抖一抖", true, false),
    tip(13, "提示", false, true),
    read(14, "已读回执", false, false),
    customEmoji(15, "自定义表情", true, true),
    beat(16, "拍了拍", true, false),
    sticker(17, "贴纸", true, true),
    pin(18, "消息置顶", false, false),
    quote(19, "引用", false, true),
    redPack(20, "红包", true, true),
    openRedPack(21, "红包领取", true, true),
    memberOpenRedPack(22, "某个群成员领取了红包", false, true),
    redEnvelopBack(23, "红包退回", true, true),
    accountTransfer(30, "转账", true, true),
    transferReceive(31, "转账领取", true, true),
    transferTimeout(32, "转账超时退回", true, true),
    transferBack(33, "转账拒收", true, true),
    imageText(40, "单条图文消息", false, true),
    mulImageText(41, "多条图文消息", false, true),
    chatHistory(51, "聊天记录", true, true),
    codePayment(61, "付款码已付款通知", true, true),
    codeArrival(62, "付款码已到账通知", true, true),
    updateSetting(73, "更新个人设置", false, false),
    updateInfo(74, "更新个人资料", false, false),
    updateGoogleCode(75, "更新谷歌验证", false, false),
    updateFriend(76, "更新好友资料", false, false),
    readDel(77, "阅后即焚", false, false),
    delMsg(78, "删除消息", false, true),
    clearHistory(79, "清空聊天记录", false, true),
    revokeMsg(82, "撤回消息", false, false),
    offlineKick(83, "踢下线", false, false),
    terminate(84, "终止特定会话", false, false),
    deviceOnline(85, "设备上线", false, false),
    addMeToMuc(86, "拉我进群", false, false),
    updateNotifySetting(88, "更新通知设置", false, false),

    ////////////////////////////音视频通话/////////////////////////////////
    call(100, "发起音视频通话", true, true),
    callCancel(101, "取消音视频通话", true, false),
    callTimeout(102, "音视频通话超时未接通", true, false),
    callAccept(103, "接受音视频通话", true, false),
    callReject(104, "拒绝音视频通话", true, false),
    callSuspend(105, "中断音视频通话", true, false),
    callEnd(106, "挂断音视频通话", true, false),
    callConnected(107, "接受并接通", true, false),
    callNotConnected(108, "接受但未接通", true, false),

    calling(133, "通话中", false, false),
    busy(134, "忙线中", false, false),


    activityPraise(201, "动态点赞", false, true),
    activityComment(202, "动态评论", false, true),
    activityAtMe(203, "动态提醒", false, true),

    ////////////////////////////控制台操作/////////////////////////////////
    mute(301, "控制台禁言/取消", false, false),
    lock(302, "控制台锁定/取消", false, false),
    noConnectXmpp(303, "控制台禁止连接聊天服务/取消", false, false),

    ////////////////////////////新朋友消息/////////////////////////////////
    sayHello(500, "打招呼", true, true),
    sayHelloReply(501, "打招呼回话", false, true),
    follow(502, "关注", false, true),
    passFriendAddRequest(503, "同意加好友", true, true),
    del(504, "双向删除", false, false),
    star(505, "星标/取消", false, false),
    black(506, "拉黑/取消", false, false),
    makeFriendDirectly(507, "直接成为好友", true, true),
    dialogueDel(508, "单向删除对话", true, false),
    dialogueBothDel(509, "双向删除对话", true, false),
    dialoguePin(510, "对话置顶", false, false),
    dialogueRead(511, "对话已读", false, false),
    dialogueNoDisturb(512, "对话免打扰", false, false),
    dialogueArchive(513, "对话归档", false, false),
    hideDialogue(514, "隐藏对话", false, false),


    contactBeFriend(510, "手机联系人添加我直接成为好友", false, false),
    newContactRegister(511, "上传的联系人里有人注册了，更新好友", false, false),


    ////////////////////////////群组协议/////////////////////////////////
    changeMemberNickName(900, "修改成员昵称", false, false),
    changeMucName(901, "修改群组名", false, true),
    destroyMuc(902, "解散群组", true, false),
    leaveMuc(903, "退出群组", false, false),
    kickMember(904, "移除群聊", false, true),
    newNotice(905, "新公告", false, false),
    mucMute(906, "全体禁言/全体取消禁言", false, false),
    setMucNotice(907, "设置 退群/禁言/移除/撤回 通知", false, false),
    newMember(908, "增加新成员", false, false),
    mucCreate(909, "群聊已创建", false, false),
    changeMucQrCode(910, "重置群聊二维码", false, false),
    changeMucAvatar(911, "修改群聊头像", false, true),
    setMucManager(912, "设置/取消管理员", false, false),
    mucQuote(913, "引用", false, true),
    mucPin(914, "群消息置顶", false, false),
    showRead(915, "设置群已读消息", false, false),
    mucQuit(916, "退出群聊", false, false),
    msgRevokeDuration(917, "消息撤回时限", false, false),
    mucShowMemberList(918, "显示群成员列表", false, false),
    mucSendCard(919, "群组是否允许发送名片", false, false),
    allowInvite(921, "允许普通成员邀请人入群", false, false),
    allowUpload(922, "允许普通成员上传群文件", false, false),
    allowConference(923, "允许普通成员发起会议", false, false),
    mucInvite(924, "申请邀请新成员", false, false),
    mucTransfer(925, "转让群组", false, false),
    memberChangeInfo(927, "群成员修改个人信息", false, false),
    changeMucInfo(928, "修改群群聊介绍", false, false),
    changeMucWelcomes(929, "修改群聊欢迎语", false, false),
    changeMemberPermission(930, "修改群成员权限", false, false),
    mucLock(931, "锁定/解锁群组", false, false),
    muteOne(932, "单独禁言", false, false),
    mucJoinVerify(933, "群聊邀请确认", false, false),
    mucAllowTalkAfterJoin(934, "进群后允许发言", false, false),
    mucShowNickname(935, "显示成员群聊昵称", false, false),
    mucRevokeWhenKicked(936, "移除成员时撤回其历史发言", false, false),
    mucShowMsgBeforeJoin(937, "入群前的消息可见", false, false),
    modifyPermission(938, "修改群管理员权限", false, false),
    modifyTitle(939, "修改群头衔", false, false),


    undefined(-1, "未定义消息类型", false, false);

    @Getter
    int type;
    @Getter
    String name;
    //是否离线推送
    @Getter
    boolean offlinePush;
    //是否存储或需要处理的
    @Getter
    boolean store;

    MsgType(int type, String name, boolean offlinePush, boolean store) {
        this.type = type;
        this.name = name;
        this.offlinePush = offlinePush;
        this.store = store;
    }

    public static MsgType getByType(int type) {
        for (MsgType value : MsgType.values()) {
            if (type==value.getType()) {
                return value;
            }
        }
        return undefined;
    }

    //判断指定类型是否进行离线推送
    public static boolean isOffline(int type) {
        for (MsgType value : MsgType.values()) {
            if (type==value.getType()) {
                return value.isOfflinePush();
            }
        }
        return false;
    }

    //判断指定类型是否进行存储
    public static boolean isStore(int type) {
        for (MsgType value : MsgType.values()) {
            if (type==value.getType()) {
                return value.isStore();
            }
        }
        return false;
    }
}
