package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMucMember;
import org.jeecg.modules.im.mapper.MucMemberMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 群组成员 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
@Slf4j
public class MucMemberServiceImpl extends BaseServiceImpl<MucMemberMapper, MucMember> implements MucMemberService {
    @Autowired
    private MucMemberMapper mucMemberMapper;
    @Resource
    private MucService mucService;
    @Resource
    private MucConfigService mucConfigService;
    @Resource
    private MucInviteService mucInviteService;
    @Resource
    private UserSettingService userSettingService;
    @Resource
    private UserService userService;
    @Resource
    private XMPPService xmppService;

    @Override
    public IPage<MucMember> pagination(MyPage<MucMember> page, QMucMember q) {
        return mucMemberMapper.pagination(page, q);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> consoleInvite(Integer mucId, String userIds) {
        Muc muc = mucService.getById(mucId);
        if (muc == null || muc.getTsLocked()>0) {
            return fail("群组不存在或已被锁定");
        }
        String[] userIdArr = StringUtils.split(userIds, ",");
        if (userIdArr == null || userIdArr.length == 0) {
            return fail("请选择要邀请的用户");
        }
        try {
            MucConfig mucConfig = mucConfigService.findByMuc(mucId);
            muc.setMemberCount(muc.getMemberCount() + userIds.split(",").length);
            if (muc.getMemberCount() > mucConfig.getMaxMemberCount()) {
                return fail("已超出本群最高人数：" + muc.getMemberCount() + ",当前人数：" + mucConfig.getMaxMemberCount());
            }
            MucMember master = getMaster(mucId);
            int count = 0;
            MucMember member;
            MucInvite invite;
            User user;
            for (String userId : userIdArr) {
                member = findByMucIdOfUser(mucId, Integer.parseInt(userId));
                //已存在该成员
                if (member != null&&member.getStatus()== MucMember.Status.Normal.getCode()) {
                    continue;
                }
                user = userService.findById(Integer.parseInt(userId));
                //用户被锁定或不允许加群
                if (user == null || user.getTsLocked()>0) {
                    continue;
                }
                UserSetting setting = userSettingService.findByUserId(user.getId());
                if(setting.getIsNoJoinMuc()){
                    continue;
                }
                //保存群成员
                member = new MucMember();
                member.setUserId(Integer.parseInt(userId));
                member.setTsJoin(getTs());
                member.setNickname(user.getNickname());
                member.setJoinType(MucMember.JoinType.ConsoleAdd.getCode());
                member.setRole(MucMember.Role.Member.getCode());
                member.setMucId(mucId);
                if (!save(member)) {
                    throw new BusinessException("保存群组成员失败");
                }
                //保存邀请记录
                invite = new MucInvite();
                invite.setInviter(master.getId());
                invite.setInvitee(member.getUserId());
                invite.setHandler(master.getId());
                invite.setMucId(mucId);
                invite.setTsCreate(getTs());
                invite.setIsNeedVerify(false);
                invite.setStatus(MucInvite.Status.Accept.getCode());
                invite.setIsValid(true);
                invite.setTsDeal(getTs());
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
                if (!mucInviteService.save(invite)) {
                    throw new BusinessException("保存群组成员邀请记录失败");
                }
                //发送邀请入群消息给用户
                MessageBean messageBean = new MessageBean();
                messageBean.setToUserId(user.getId());
                messageBean.setToUserName(user.getNickname());
                messageBean.setContent(JSONObject.toJSONString(invite));
                messageBean.setType(MsgType.newMember.getType());
                messageBean.setMucId(mucId);
                xmppService.sendMsgToOneAndMucBySys(messageBean);
                //后台邀请进群
                if(!xmppService.consoleInviteUser(member.getUserId(),mucId)){
                    throw new BusinessException("xmpp邀请用户进群失败");
                }
                count++;
            }
            return success(count);
        }catch (Exception e){
            log.error("后台邀请用户进群异常：mucId={},userIds={},e={}", mucId,userIds, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    public MucMember findByMucIdOfUser(Integer mucId, Integer userId) {
        return mucMemberMapper.findByMucIdOfUser(mucId,userId);
    }
    @Override
    public MucMember findById(Integer id) {
        return mucMemberMapper.findById(id);
    }

    @Override
    public List<MucMember> findAll(Integer mucId) {
        return mucMemberMapper.findAll(mucId,MucMember.Status.Normal.getCode());
    }

    @Override
    public MucMember getMaster(Integer mucId) {
        LambdaQueryWrapper<MucMember> wrapper = new LambdaQueryWrapper();
        wrapper.eq(MucMember::getMucId,mucId);
        wrapper.eq(MucMember::getRole,MucMember.Role.Master.getCode());
        wrapper.eq(MucMember::getStatus,MucMember.Status.Normal.getCode());
        return getOne(wrapper);
    }

    @Override
    public List<MucMember> getManagers(Integer mucId) {
        LambdaQueryWrapper<MucMember> wrapper = new LambdaQueryWrapper();
        wrapper.eq(MucMember::getMucId,mucId);
        wrapper.eq(MucMember::getRole,MucMember.Role.Manager.getCode());
        wrapper.eq(MucMember::getStatus,MucMember.Status.Normal.getCode());
        return list(wrapper);
    }

    @Override
    public List<MucMember> getMembers(Integer mucId) {
        LambdaQueryWrapper<MucMember> wrapper = new LambdaQueryWrapper();
        wrapper.eq(MucMember::getMucId,mucId);
        wrapper.eq(MucMember::getRole,MucMember.Role.Member.getCode());
        wrapper.eq(MucMember::getStatus,MucMember.Status.Normal.getCode());
        return list(wrapper);
    }

    @Override
    public List<MucMember> findMine(Integer userId) {
        return mucMemberMapper.findMine(userId,null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> kick(Integer userId, Integer mucId, String memberIds) {
        try{
            Muc muc = mucService.getById(mucId);
            if (muc == null) {
                return fail("群组不存在");
            }
            MucMember kicker = findByMucIdOfUser(mucId, userId);
            if (kicker == null) {
                return fail("你不是该群的成员，无法操作");
            }
            //当前成员不是群主或者管理员
            boolean isManager = kicker.getRole() == MucMember.Role.Master.getCode() || kicker.getRole() == MucMember.Role.Manager.getCode();
            if (!isManager) {
                return fail("你没有权限，无法操作");
            }
            int count = 0;
            String[] memberIdArr = StringUtils.split(memberIds, ",");
            for (String memberId : memberIdArr) {
                if(kickOne(mucId, Integer.valueOf(memberId),kicker).isSuccess()){
                    count++;
                }
            }
            muc.setMemberCount(getCount(mucId, MucMember.Status.Normal.getCode()));
            if(!mucService.updateById(muc)){
                throw new BusinessException("踢出成员失败");
            }
            if(count!=memberIdArr.length){
                throw new BusinessException("要移除的数量与实际移除的不一致");
            }
            return success();
        }catch (Exception e){
            log.error("踢除成员异常：mucId={},userId={},memberIds={},e={}", mucId,userId,memberIds, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
    private Result<Object> kickOne(Integer mucId, Integer memberId,MucMember kicker){
        MucMember mucMember = findById(memberId);
        if (mucMember == null) {
            return fail("该用户不在群里");
        }
        mucMember.setStatus(MucMember.Status.Kicked.getCode());
        mucMember.setKicker(kicker.getId());
        mucMember.setTsQuit(getTs());
        if(!updateById(mucMember)){
            return fail();
        }
        //移除群成员
        xmppService.kickMembers(mucId,mucMember.getUserId().toString());
        Kv data = Kv.by("memberId",memberId).set("memberNickname",mucMember.getNickname()).set("kickerId",kicker.getId()).set("kickerNickname",kicker.getNickname()).set("isMaster",kicker.getRole() == MucMember.Role.Master.getCode() );
        //发送一条移除成员的信息
        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(kicker.getUserId());
        messageBean.setContent(JSONObject.toJSONString(data));
        messageBean.setType(MsgType.kickMember.getType());
        messageBean.setMucId(mucId);
        if(xmppService.sendMucMsg(messageBean)){
            return success();
        }
        return fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> kickAll(Muc muc, MucMember.Status status) {
        try{
            List<MucMember> mucMembers = findAll(muc.getId(), MucMember.Status.Normal.getCode());
            for (MucMember member : mucMembers) {
                member.setStatus(status.getCode());
                member.setKicker(muc.getMasterId());
                member.setTsQuit(getTs());
                if(!updateById(member)){
                    throw new BusinessException("踢出群成员失败");
                }
                //发送踢群消息
                //发送一条解散群组的消息
                MessageBean messageBean = new MessageBean();
                messageBean.setContent(muc.getName());
                if(status.equals(MucMember.Status.Dismiss)){
                    messageBean.setType(MsgType.destroyMuc.getType());
                }else if(status.equals(MucMember.Status.Kicked)){
                    messageBean.setType(MsgType.kickMember.getType());
                }
                messageBean.setMucId(muc.getId());
                if (xmppService.sendMsgBySys(messageBean)) {
                    return success();
                }
            }
            return success();
        }catch (Exception e){
            log.error("踢除成员异常：mucId={},e={}", muc.getId(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> quit(Integer userId, Integer mucId) {
        try{
            Muc muc = mucService.getById(mucId);
            if (muc == null) {
                return fail("群组不存在");
            }
            MucMember member = findByMucIdOfUser(mucId, userId);
            if (member == null||!member.getStatus().equals(MucMember.Status.Normal.getCode())) {
                return fail("你不是该群的成员，无法操作");
            }
            member.setTsQuit(getTs());
            member.setStatus(MucMember.Status.Quit.getCode());
            updateById(member);

            muc.setMemberCount(getCount(mucId, MucMember.Status.Normal.getCode()));
            mucService.updateById(muc);

            //发送退群消息
            Kv data = Kv.by("mucId",mucId).set("memberId",member.getId());
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(userId);
            messageBean.setMucId(mucId);
            messageBean.setContent(JSONObject.toJSONString(data));
            messageBean.setType(MsgType.mucQuit.getType());
            xmppService.sendMucMsg(messageBean);
            return success();
        }catch (Exception e){
            log.error("退群异常：mucId={},userId={},e={}", mucId,userId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    private List<MucMember> findAll(Integer mucId, int status) {
        return mucMemberMapper.findAll(mucId,status);
    }

    @Override
    public Integer getCount(Integer mucId, Integer status) {
        return mucMemberMapper.getCount(mucId,status);
    }

    //修改群聊备注
    @Override
    public Result<Object> updateMember(MucMember temp) {
        MucMember member = getById(temp.getId());
        if(member==null){
            return  fail();
        }
        Kv data = Kv.by("id",member.getId());
        if(!isEmpty(temp.getRemark())&&!equals(temp.getRemark(),member.getRemark())){
            member.setRemark(temp.getRemark());
            data.set("remark",member.getRemark());
        }
        if(!isEmpty(temp.getNickname())&&!equals(temp.getNickname(),member.getNickname())){
            member.setNickname(temp.getNickname());
            data.set("nickname",member.getNickname());
        }
        if(!isEmpty(temp.getBackImg())&&!equals(temp.getBackImg(),member.getBackImg())){
            member.setBackImg(temp.getBackImg());
            data.set("backImg",member.getBackImg());
        }
        if(updateById(member)){
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setContent(JSONObject.toJSONString(data));
            messageBean.setType(MsgType.memberChangeInfo.getType());
            xmppService.sendMsgToSelf(messageBean);
        }
        //是否开启修改昵称通知所有人
        MucConfig mucConfig = mucConfigService.findByMuc(member.getMucId());
        if(mucConfig.getIsUpdateNicknameNotify()&&!isEmpty(temp.getNickname())&&!equals(temp.getNickname(),member.getNickname())){
            Kv data2 = Kv.by("id",member.getId());
            data2.set("nickname",member.getNickname());
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(getCurrentUserId());
            messageBean.setMucId(member.getMucId());
            messageBean.setContent(JSONObject.toJSONString(data2));
            messageBean.setType(MsgType.changeMemberNickName.getType());
            xmppService.sendMucMsg(messageBean);
        }
        return success();
    }


    @Override
    public MyPage<MucMember> pageApi(MyPage<MucMember> page, QMucMember q) {
        return mucMemberMapper.pageApi(page,q);
    }
}
