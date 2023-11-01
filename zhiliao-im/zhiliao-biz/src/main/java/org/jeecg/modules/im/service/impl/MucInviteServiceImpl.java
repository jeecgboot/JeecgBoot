package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QMucInvite;
import org.jeecg.modules.im.mapper.MucInviteMapper;
import org.jeecg.modules.im.mapper.MucMemberMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * <p>
 * 加群邀请 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
@Service
@Slf4j
public class MucInviteServiceImpl extends BaseServiceImpl<MucInviteMapper, MucInvite> implements MucInviteService {

    @Autowired
    private MucInviteMapper mucInviteMapper;
    @Resource
    private MucMemberService mucMemberService;
    @Resource
    private MucService mucService;
    @Resource
    private UserService userService;
    @Resource
    private MucConfigService mucConfigService;
    @Resource
    private ClientConfigService clientConfigService;
    @Autowired
    private MucMemberMapper mucMemberMapper;
    @Resource
    private MucInviteService mucInviteService;
    @Resource
    private UserSettingService userSettingService;
    @Resource
    private XMPPService xmppService;

    @Override
    public IPage<MucInvite> pagination(MyPage<MucInvite> page, QMucInvite q) {
        return mucInviteMapper.pagination(page,q);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> send(Integer userId, Integer mucId, Integer toUserId) {
        try {
            User toUser = userService.findById(toUserId);
            Muc muc = mucService.getById(mucId);
            if (muc == null) {
                return fail("群组不存在");
            }
            MucConfig config = mucConfigService.findByMuc(mucId);
            if (muc.getMemberCount() >= config.getMaxMemberCount()) {
                return fail("该群成员人数已达上限");
            }
            MucMember inviter = mucMemberService.findByMucIdOfUser(mucId, userId);
            if (inviter == null|| inviter.getStatus()!= MucMember.Status.Normal.getCode()) {
                return fail("你不是该群的成员，无法邀请");
            }

            //如果关闭了邀请功能，并且当前成员不是群主或者管理员
            boolean isManager = inviter.getRole() == MucMember.Role.Master.getCode() || inviter.getRole() == MucMember.Role.Manager.getCode();
            if (!isManager ) {
                return fail("已关闭邀请新用户功能");
            }
            MucMember invitee = mucMemberService.findByMucIdOfUser(mucId, toUserId);
            if (invitee != null) {
                if(invitee.getStatus()== MucMember.Status.Normal.getCode()){
                    return fail("该用户已在群里");
                }
            }
            MucInvite invite = findLatestUnDeal(userId, mucId, toUserId);
            if (invite != null) {
                return success(invite);
            }
            invite = new MucInvite();
            invite.setMucId(mucId);
            invite.setInviter(inviter.getId());
            invite.setInvitee(toUserId);
            invite.setTsCreate(getTs());
            invite.setStatus(MucInvite.Status.Waiting.getCode());
            //如果进群需要验证并且当前用户不是管理者
            if (!isManager && config.getIsJoinVerify()) {
                invite.setIsNeedVerify(true);
            } else {
                invite.setIsNeedVerify(false);
            }
            if(!save(invite)){
                throw new BusinessException("保存邀请失败");
            }
            Kv data = Kv.by("isNeedVerify", invite.getIsNeedVerify()).set("member", inviter);
            return success(data);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邀请用户进群异常：userId={},mucId={},toUserId={},e={}", userId, mucId, toUserId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("邀请失败，请稍后再试");
        }
    }

    public static void main(String[] args) {
        System.out.println(UUIDTool.getUUID());
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> passBySelf(Integer userId, Integer fromId, Integer mucId) {
        try {
            Muc muc = mucService.getById(mucId);
            if (muc == null) {
                return fail("群组不存在");
            }
            MucConfig config = mucConfigService.findByMuc(mucId);
            if (muc.getMemberCount() >= config.getMaxMemberCount()) {
                return fail("该群成员人数已达上限");
            }
            MucMember handler = mucMemberService.findByMucIdOfUser(mucId, userId);
            if (handler != null&& handler.getStatus()== MucMember.Status.Normal.getCode()) {
                return fail("你已在群里");
            }
            MucInvite invite = findLatestUnDeal(fromId, mucId,userId);
            if (invite == null) {
                return fail("邀请不存在或无效");
            }
            User toUser = userService.findById(userId);
            if(handler !=null){
                handler.setStatus(MucMember.Status.Normal.getCode());
                handler.setTsJoin(getTs());
                handler.setRole(MucMember.Role.Member.getCode());
            }else {
                //保存群成员
                handler = new MucMember();
                handler.setMucId(mucId);
                handler.setUserId(userId);
                handler.setRole(MucMember.Role.Member.getCode());
                handler.setTsJoin(getTs());
                handler.setNickname(toUser.getNickname());
            }
            //入群前的消息不可见
            if (!config.getIsShowMsgBeforeJoin()) {
                //设置可见消息时间为入群时间
                handler.setTsMsgVisible(getTs());
            }
            if (!config.getIsAllowTalkAfterJoin()) {
                handler.setTsMuteBegin(getTs());
                handler.setTsMute(1L);
                handler.setMuteType(User.MuteType.newMember.getCode());
            }
            if(handler.getId()!=null){
                if(!mucMemberService.updateById(handler)){
                    throw new BusinessException("更新群成员失败");
                }
            }else {
                if (!mucMemberService.save(handler)) {
                    throw new BusinessException("保存群成员失败");
                }
            }
            muc.setMemberCount(mucMemberService.getCount(mucId, MucMember.Status.Normal.getCode()));
            if(!mucService.updateById(muc)){
                throw new BusinessException("更新群组失败");
            }
            invite.setStatus(MucInvite.Status.Accept.getCode());
            invite.setTsDeal(getTs());
            invite.setHandler(handler.getId());
            //更新邀请
            if(!updateById(invite)){
                throw new BusinessException("更新邀请失败");
            }
            return success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("进群邀请通过异常：userId={},mucId={},fromId={},e={}", userId, mucId, fromId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail("通过邀请失败，请稍后再试");
        }
    }

    @Override
    public MucInvite findLatestUnDeal(Integer fromId, Integer mucId, Integer toId) {
        return mucInviteMapper.findLatestUnDeal(fromId, mucId, toId);
    }

    /**
     * 当前用户是管理员或群主，可邀请
     * 管理员需要判断是否有邀请的权限以及直接添加群成员的权限
     * 当前用户是普通成员，且开启了普通成员可以邀请
     * @param inviterId
     * @param mucId
     * @param userIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> inviteBatch(Integer inviterId, Integer mucId, String userIds) {
        try{
            Muc muc = mucService.getById(mucId);
            if (muc == null) {
                return fail("群组不存在");
            }
            MucMember inviter = mucMemberService.findByMucIdOfUser(mucId, inviterId);
            if (inviter == null) {
                return fail("你不是该群的成员，无法操作");
            }
            MucConfig mucConfig = mucConfigService.findByMuc(muc.getId());
            boolean isManager = inviter.getRole() == MucMember.Role.Master.getCode() || inviter.getRole() == MucMember.Role.Manager.getCode();
            if(!isManager&&!(inviter.getRole()==MucMember.Role.Member.getCode())){
                return fail("你没有权限，无法操作");
            }

            int count = 0;
            String[] userIdArr = StringUtils.split(userIds, ",");
            for (String userId : userIdArr) {
                if(inviteOne(mucId, Integer.valueOf(userId),inviter,mucConfig).isSuccess()){
                    count++;
                }
            }
            return success(count);
        }catch (Exception e){
            log.error("邀请成员异常", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("邀请失败，请重试！");
        }
    }

    @Override
    public int invalidOfUserByMuc(Integer userId, Integer mucId) {
        return mucInviteMapper.invalidOfUserByMuc(userId,mucId);
    }

    private Result<Object> inviteOne(Integer mucId,Integer userId,MucMember inviter,MucConfig mucConfig) throws Exception {
        boolean isManager = inviter.getRole() == MucMember.Role.Master.getCode() || inviter.getRole() == MucMember.Role.Manager.getCode();
        //判断用户是否已在群里
        MucMember member = mucMemberService.findByMucIdOfUser(mucId,userId);
        if(member!=null&&member.getStatus().equals(MucMember.Status.Normal.getCode())){
            throw new BusinessException("该用户已在群里！");
        }
        //删除该群对该用户之前的邀请记录
        invalidOfUserByMuc(userId,mucId);
        //普通成员邀请，且开启了邀请验证
        if(!isManager&&mucConfig.getIsJoinVerify()){
            //生成邀请记录
            MucInvite invite = new MucInvite();
            invite.setInviter(inviter.getId());
            invite.setInvitee(userId);
            invite.setMucId(mucId);
            invite.setTsCreate(getTs());
            invite.setIsNeedVerify(true);
            invite.setStatus(MucInvite.Status.Waiting.getCode());
            invite.setIsValid(true);
            save(invite);
            //发送邀请新成员申请
            Kv data = Kv.by("invite",invite);
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(inviter.getUserId());
            messageBean.setContent(JSONObject.toJSONString(data));
            messageBean.setType(MsgType.mucInvite.getType());
            messageBean.setMucId(mucId);
            if(xmppService.sendMucMsg(messageBean)){
                return success();
            }
            return fail();
        }else{
            //生成邀请记录
            MucInvite invite = new MucInvite();
            invite.setInviter(inviter.getId());
            invite.setInvitee(userId);
            invite.setMucId(mucId);
            invite.setTsCreate(getTs());
            invite.setTsDeal(getTs());
            invite.setIsNeedVerify(false);
            invite.setStatus(MucInvite.Status.Accept.getCode());
            invite.setInviter(inviter.getId());
            invite.setIsValid(true);
            save(invite);
            xmppService.inviteUsers(inviter.getUserId(),mucId,userId.toString());
            //保存群成员
            User user = userService.getById(userId);
            if(member!=null){
                member.setStatus(MucMember.Status.Normal.getCode());
                member.setKicker(0);
                member.setTsQuit(0L);
            }else{
                member = new MucMember();
                member.setUserId(userId);
                member.setTsJoin(getTs());
                member.setNickname(user.getNickname());
                member.setJoinType(MucMember.JoinType.Invite.getCode());
                member.setRole(MucMember.Role.Member.getCode());
                member.setMucId(mucId);
            }
            //入群前的消息不可见
            if (!mucConfig.getIsShowMsgBeforeJoin()) {
                //设置可见消息时间为入群时间
                member.setTsMsgVisible(getTs());
            }
            if (!mucConfig.getIsAllowTalkAfterJoin()) {
                member.setTsMuteBegin(getTs());
                member.setTsMute(1L);
                member.setMuteType(User.MuteType.newMember.getCode());
            }
            mucMemberService.saveOrUpdate(member);
            //发送新的成员入群
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(inviter.getUserId());
            messageBean.setContent(JSONObject.toJSONString(invite));
            messageBean.setType(MsgType.newMember.getType());
            messageBean.setMucId(mucId);
            if(!xmppService.sendMucMsg(messageBean)){
                return fail();
            }
            //发送给被邀请人  因为此时被邀请的还没有加入群聊，所以还需要单独发送邀请消息
            MessageBean messageBean2 = new MessageBean();
            messageBean2.setUserId(inviter.getUserId());
            messageBean2.setToUserId(userId);
            messageBean2.setContent(JSONObject.toJSONString(invite));
            messageBean2.setType(MsgType.addMeToMuc.getType());
            if(!xmppService.sendMsgToOne(messageBean2)){
                return fail();
            }
            return success();
        }
    }

}
