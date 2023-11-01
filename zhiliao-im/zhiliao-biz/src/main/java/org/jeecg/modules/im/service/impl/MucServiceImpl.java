package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.ConstantXmpp;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.util.im.AccountUtil;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QMuc;
import org.jeecg.modules.im.mapper.MucMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 群组 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
@Slf4j
public class MucServiceImpl extends BaseServiceImpl<MucMapper, Muc> implements MucService {
    @Autowired
    private MucMapper mucMapper;
    @Resource
    private MucConfigService mucConfigService;
    @Resource
    private XMPPService xmppService;
    @Resource
    private UserService userService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserSettingService userSettingService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private MucMemberService mucMemberService;
    @Resource
    private MucInviteService mucInviteService;
    @Resource
    private ParamService paramService;
    @Resource
    private MucService mucService;
    @Resource
    private MucPermissionService mucPermissionService;

    @Override
    public IPage<Muc> pagination(MyPage<Muc> page, QMuc q) {
        return mucMapper.pagination(page, q);
    }

    //将群组状态改为已解散，踢出所有用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> consoleDestroy(Integer mucId) {
        try{
            Muc muc = getById(mucId);
            if(muc ==null){
                return fail("该群组不存在");
            }
            if(muc.getTsDelete()>0){
                return fail("该群组已被解散");
            }
            //将群变为已删除状态
            muc.setTsDelete(getTs());
            muc.setMemberCount(0);
            //将群成员踢出
            mucMemberService.kickAll(muc, MucMember.Status.Dismiss);
            //xmpp解散群组
            if(muc.getUserId()==null|| muc.getUserId()<= ConstantXmpp.sysAccountEnd&& muc.getUserId()>= ConstantXmpp.sysAccountBegin){
                if (!xmppService.consoleDestroyMuc(mucId)) {
                    throw new BusinessException("xmpp解散群组失败");
                }
            }else{
                User user = userService.findById(muc.getUserId());
                if (!xmppService.destroyMuc(user.getId(),mucId)) {
                    throw new BusinessException("xmpp解散群组失败");
                }
            }
            if(!updateById(muc)){
                throw new BusinessException("解散群组失败");
            }
            log.info("后台解散群组成功：mucId={}", mucId);
            return success();
        }catch (Exception e){
            log.error("后台解散群组异常：mucId={},e={}", mucId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    public boolean lock(Integer mucId) {
        return false;
    }

    @Override
    public boolean unlock(Integer mucId) {
        return false;
    }

    @Override
    public Muc findByName(String name) {
        return mucMapper.findByName(name);
    }

    @Override
    public List<Muc> getByIds(String ids) {
        if(isEmpty(ids)){
            return Collections.emptyList();
        }
        return mucMapper.getByIds(ids);
    }

    @Override
    public Muc findByIdOfUser(Integer id,Integer userId) {
        return mucMapper.findByIdOfUser(id,userId);
    }

    @Override
    public Muc findByAccount(String account) {
        return mucMapper.findByAccount(account);
    }

    /**
     * 保存群组，复制群组配置，
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> createOrUpdate(Muc muc) {
        if(muc.getId()==null){
            return consoleCreate(muc);
        }
        return consoleUpdate(muc);
    }

    Result<Object> consoleUpdate(Muc muc){
        return success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<Object> consoleCreate(Muc muc){
        ClientConfig config = clientConfigService.get();
        if(config.getMucNameUnique()&&findByName(muc.getName())!=null){
            return fail("群组名称已存在");
        }
        try {
            muc.setUserId(ConstantXmpp.SystemNo.system.getAccount());
            muc.setQrCode(UUIDTool.getUUID());
            muc.setTsCreate(getTs());
            String account = AccountUtil.getMucAccount();
            while (findByAccount(account) != null) {
                account = AccountUtil.getMucAccount();
            }
            muc.setAccount(account);
            if(!save(muc)){
                return fail("创建群组失败 ");
            }
            MucConfig mucConfig = mucConfigService.findDefault();
            mucConfig.setMucId(muc.getId());
            mucConfig.setId(muc.getId());
            if(!mucConfigService.save(mucConfig)){
                throw new BusinessException("保存群组配置失败");
            }
            List<MucMember> members = new ArrayList<>();
            //创建群主
            MucMember master = new MucMember();
            master.setMucId(muc.getId());
            master.setUserId(ConstantXmpp.SystemNo.system.getAccount());
            master.setNickname(ConstantXmpp.SystemNo.system.getName());
            master.setTsJoin(getTs());
            master.setRole(MucMember.Role.Master.getCode());
            master.setJoinType(MucMember.JoinType.Create.getCode());
            members.add(master);

            if(!mucMemberService.saveBatch(members)){
                throw new BusinessException("保存群成员失败");
            }

            muc.setMasterId(master.getId());
            muc.setMemberCount(members.size());
            updateById(muc);
            //创建xmpp群聊
            boolean result = xmppService.consoleCreatMuc(muc, mucConfig);
            if(result){
                return success();
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail();
        } catch (Exception e) {
            log.error("后台创建群组异常：muc={},e={}", muc, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> create(Muc muc,String inviteAccounts) {
        ClientConfig config = clientConfigService.get();
        if(config.getMucNameUnique()&&findByName(muc.getName())!=null){
            return fail("群组名称已存在");
        }
        User user = userService.findById(muc.getUserId());
        if(user.getType()==User.Type.common.getCode()){
            if(!config.getAllowCommonUserCreateMuc()){
                return fail("已禁止普通用户创建群聊");
            }
            if(config.getMaxMucCreate()>0&&config.getMaxMucCreate()<= mucService.getCountOfRole(muc.getUserId(), MucMember.Role.Master)){
                return fail("你创建的群组数量已达限制");
            }
        }
        String account = AccountUtil.getMucAccount();
        while (findByAccount(account) != null) {
            account = AccountUtil.getMucAccount();
        }
        muc.setAccount(account);
        try {
            //保存群组
            if(!save(muc)){
                return fail("创建群组失败 ");
            }
            //保存群组默认配置
            MucConfig mucConfig = mucConfigService.findDefault();
            mucConfig.setMucId(muc.getId());
            mucConfig.setId(muc.getId());
            if(!mucConfigService.save(mucConfig)){
                throw new BusinessException("保存群组配置失败");
            }
            //更新用户
            List<MucMember> members = new ArrayList<>();
            //创建群主
            MucMember master = new MucMember();
            master.setMucId(muc.getId());
            master.setUserId(muc.getUserId());
            master.setNickname(user.getNickname());
            master.setTsJoin(getTs());
            master.setRole(MucMember.Role.Master.getCode());
            master.setJoinType(MucMember.JoinType.Create.getCode());
            members.add(master);
            StringBuilder realInviteAccounts = new StringBuilder();
            //创建成员
            if(!isEmpty(inviteAccounts)){
                List<MucInvite> invites = new ArrayList<>();
                MucInvite mucInvite;
                MucMember member;
                for (String uid : StringUtils.split(inviteAccounts, ",")) {
                    User tempUser = userService.findById(Integer.parseInt(uid));
                    UserSetting userSetting = userSettingService.findByUserId(Integer.parseInt(uid));
                    //用户不存在，用户冻结，用户不允许加群
                    if(tempUser==null||tempUser.getTsLocked()>0||userSetting.getIsNoJoinMuc()){
                        continue;
                    }
                    member = new MucMember();
                    member.setMucId(muc.getId());
                    member.setUserId(tempUser.getId());
                    member.setTsJoin(getTs());
                    member.setNickname(tempUser.getNickname());
                    member.setRole(MucMember.Role.Member.getCode());
                    member.setJoinType(MucMember.JoinType.Invite.getCode());
                    members.add(member);
                    realInviteAccounts.append(tempUser.getId()).append(",");
                    //邀请记录
                    mucInvite = new MucInvite();
                    mucInvite.setMucId(muc.getId());
                    mucInvite.setTsCreate(getTs());
                    mucInvite.setStatus(MucInvite.Status.Accept.getCode());
                    mucInvite.setHandler(master.getId());
                    mucInvite.setInviter(master.getId());
                    mucInvite.setInvitee(tempUser.getId());
                    mucInvite.setIsNeedVerify(false);
                    mucInvite.setTsDeal(getTs());
                    invites.add(mucInvite);
                }
                if(!mucInviteService.saveBatch(invites)){
                    throw new BusinessException("保存群成员邀请失败");
                }
            }

            if(!mucMemberService.saveBatch(members)){
                throw new BusinessException("保存群成员失败");
            }

            muc.setMasterId(master.getId());
            muc.setMemberCount(members.size());
            updateById(muc);
            //创建xmpp群聊
            boolean result = xmppService.creatMuc(user.getId(),muc, mucConfig, realInviteAccounts.toString());
            if(result){
                log.info("创建群组成功：muc={},inviteAccounts={}", muc, realInviteAccounts);
                return success(muc);
            }else{
                throw new BusinessException("xmpp创建群聊失败");
            }
        } catch (Exception e) {
            log.error("创建群组异常：muc={},e={}", muc, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("创建失败,请重试！");
        }
    }
    @Override
    public Result<Object> findMyAll(Integer userId) {
        List<Muc> mucs = mucMapper.findMyAll(userId, MucMember.Role.Zombie.getCode(),MucMember.Status.Normal.getCode());
        List create = new ArrayList();
        List manager = new ArrayList();
        List member = new ArrayList();
        Kv data = Kv.create();
        for (Muc muc : mucs) {
            if(muc.getRole()== MucMember.Role.Master.getCode()){
                create.add(muc);
            }
            if(muc.getRole()== MucMember.Role.Member.getCode()){
                member.add(muc);
            }
            if(muc.getRole()== MucMember.Role.Manager.getCode()){
                manager.add(muc);
            }
        }
        data.set("create",create).set("manager",manager).set("member",member);
        return success(data);
    }
    @Override
    public Integer getCountOfRole(Integer userId, MucMember.Role role) {
        return mucMapper.getCountOfRole(userId,role.getCode());
    }

    @Override
    public Result<Object> updateQrcode(Muc temp) {
        try {
            Muc muc = getById(temp.getId());
            if (muc == null || muc.getTsDelete() > 0) {
                return fail("群组不存在");
            }
            if (!isEmpty(temp.getQrCode())) {
                muc.setQrCode(temp.getQrCode());
                if (updateById(muc)) {
                    //发送群消息
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(getCurrentUserId());
                    messageBean.setMucId(muc.getId());
                    messageBean.setType(MsgType.changeMucQrCode.getType());
                    messageBean.setContent(muc.getQrCode());
                    xmppService.sendMucMsg(messageBean);
                }
            }
            return success();
        }catch (Exception e){
            return fail();
        }
    }
    @Override
    public Result<Object> updateName(Muc temp) {
        try {
            Muc muc = getById(temp.getId());
            if (muc == null || muc.getTsDelete() > 0) {
                return fail("群组不存在");
            }
            if (!isEmpty(temp.getName())&&!equals(muc.getName(),temp.getName())) {
                Kv data = Kv.by("old",muc.getName()).set("new",temp.getName());
                muc.setName(temp.getName());
                if (updateById(muc)) {
                    User user = userService.getById(muc.getUserId());
                    xmppService.updateMuc(temp,user.getPassword());
                    //发送群消息
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(getCurrentUserId());
                    messageBean.setMucId(muc.getId());
                    messageBean.setType(MsgType.changeMucName.getType());
                    messageBean.setContent(JSONObject.toJSONString(data));
                    xmppService.sendMucMsg(messageBean);
                }
                return success();
            }
        }catch (Exception e){
        }
        return fail();
    }
    @Override
    public Result<Object> updateInfo(Muc temp) {
        try {
            Muc muc = getById(temp.getId());
            if (muc == null || muc.getTsDelete() > 0) {
                return fail("群组不存在");
            }
            Kv data = Kv.create();
            if (!isEmpty(temp.getInfo())&&!equals(temp.getInfo(),muc.getInfo())) {
                muc.setInfo(temp.getInfo());
                data.set("info",muc.getInfo());
            }
            if (temp.getFakeMember()!=null&&!equals(temp.getFakeMember(),muc.getFakeMember())) {
                muc.setFakeMember(temp.getFakeMember());
                data.set("fakeMember",muc.getFakeMember());
            }
            if (temp.getFakeOnline()!=null&&!equals(temp.getFakeOnline(),muc.getFakeOnline())) {
                muc.setFakeOnline(temp.getFakeOnline());
                data.set("fakeOnline",muc.getFakeOnline());
            }
            if(!data.isEmpty()){
                if (updateById(muc)) {
                    User user = userService.getById(muc.getUserId());
                    xmppService.updateMuc(temp,user.getPassword());
                    //发送群消息
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(getCurrentUserId());
                    messageBean.setMucId(muc.getId());
                    messageBean.setType(MsgType.changeMucInfo.getType());
                    messageBean.setContent(JSONObject.toJSONString(data));
                    xmppService.sendMucMsg(messageBean);
                }
                return success();
            }
            return fail();
        }catch (Exception e){
        }
        return fail();
    }
    @Override
    public Result<Object> updateWelcomes(QMuc temp) {
        try {
            Muc muc = getById(temp.getId());
            if (muc == null || muc.getTsDelete() > 0) {
                return fail("群组不存在");
            }
            MucConfig config = mucConfigService.findByMuc(muc.getId());
            if (!isEmpty(temp.getWelcomes())&&!equals(temp.getWelcomes(),config.getWelcomes())) {
                config.setWelcomes(temp.getWelcomes());
                if (mucConfigService.updateById(config)) {
                    //发送群消息
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(getCurrentUserId());
                    messageBean.setMucId(muc.getId());
                    messageBean.setType(MsgType.changeMucWelcomes.getType());
                    messageBean.setContent(config.getWelcomes());
                    xmppService.sendMucMsg(messageBean);
                }
                return success();
            }
        }catch (Exception e){
        }
        return fail();
    }
    @Override
    public Result<Object> updateAvatar(Muc temp) {
        try {
            Muc muc = getById(temp.getId());
            if (muc == null || muc.getTsDelete() > 0) {
                return fail("群组不存在");
            }
            if (!isEmpty(temp.getAvatar())) {
                Kv data = Kv.by("old",muc.getSmallAvatar()).set("newOrigin",temp.getAvatar()).set("newThumb",temp.getSmallAvatar());
                muc.setAvatar(temp.getAvatar());
                muc.setSmallAvatar(temp.getSmallAvatar());
                if (updateById(muc)) {
                    //发送群消息
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(getCurrentUserId());
                    messageBean.setMucId(muc.getId());
                    messageBean.setType(MsgType.changeMucAvatar.getType());
                    messageBean.setContent(JSONObject.toJSONString(data));
                    xmppService.sendMucMsg(messageBean);
                }
                return success();
            }
        }catch (Exception e){
        }
        return fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> setManagers(Integer mucId, String memberIds,Integer flag) {
        try {
            Muc muc = getById(mucId);
            if (muc == null || muc.getTsDelete() > 0) {
                return fail("群组不存在");
            }
            //群主或管理员
            MucMember manager = mucMemberService.findByMucIdOfUser(mucId,getCurrentUserId());
            if(manager==null||manager.getStatus()!=MucMember.Status.Normal.getCode()) {
                return fail("权限不足");
            }

            if(manager.getRole()!=MucMember.Role.Master.getCode()){
                MucPermission permission  = mucPermissionService.findByManager(manager.getId());
                if(permission==null){
                    return fail("权限不足");
                }
                if(flag==1&&!permission.getAddManager()||flag==0&&!permission.getRevokeManager()){
                    return fail("权限不足");
                }
                return fail("你不是群主，无法执行该操作！");
            }
            StringBuilder userIds = new StringBuilder();
            MucMember member;
            for (String memberId : StringUtils.split(memberIds,",")) {
                if(isEmpty(memberId)){
                    continue;
                }
                member = mucMemberService.findById(Integer.parseInt(memberId));
                if(member==null||member.getStatus()!=MucMember.Status.Normal.getCode()){
                    continue;
                }
                if(flag==0&&member.getRole()!=MucMember.Role.Manager.getCode()){
                    log.error("不是管理员取消个毛");
                    continue;
                }
                if(flag==1&&member.getRole()==MucMember.Role.Manager.getCode()){
                    log.error("已经是管理员");
                    continue;
                }
                member.setRole(flag==1?MucMember.Role.Manager.getCode():MucMember.Role.Member.getCode());
                mucMemberService.updateMember(member);
                if(flag==1){
                   MucPermission permission = mucPermissionService.findByManager(member.getId());
                   if(permission==null){
                       //查询群默认权限
                       MucPermission defaultPermission = mucPermissionService.findDefaultOfMuc(mucId);
                       permission = new MucPermission();
                       BeanUtils.copyProperties(defaultPermission,permission);
                       permission.setManagerId(member.getId());
                       permission.setId(null);
                       mucPermissionService.save(permission);
                   }
                }else{
                    mucPermissionService.deleteOne(mucId,member.getId());
                }
                userIds.append(member.getUserId()).append(",");
            }
            if(StringUtils.isBlank(userIds)){
                return fail();
            }
            boolean result;
            //授权或取消
            if(flag==1){
                result = xmppService.grantManagers(mucId,userIds.toString());
            }else{
                result = xmppService.revokeManagers(mucId,userIds.toString());
            }
            if(!result) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return fail();
            }
            //发送单聊通知即可
            MessageBean messageBean;
            for (String toUserId : StringUtils.split(userIds.toString(),",")) {
                messageBean = new MessageBean();
                messageBean.setUserId(manager.getUserId());
                messageBean.setToUserId(Integer.parseInt(toUserId));
                messageBean.setMucId(mucId);
                messageBean.setType(MsgType.setMucManager.getType());
                messageBean.setContent(flag+"");
                result = xmppService.sendMucMsg(messageBean);
                if(!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return fail();
                }
            }
            return success();
        }catch (Exception e){
            log.error("设置管理员异常：mucId={},memberIds={},flag={},e={}", mucId,memberIds,flag, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
}
