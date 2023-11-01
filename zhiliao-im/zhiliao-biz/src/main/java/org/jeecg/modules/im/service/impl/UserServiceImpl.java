package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantZhiLiao;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.util.*;
import org.jeecg.modules.im.base.util.google.GoogleAuthenticator;
import org.jeecg.modules.im.base.util.im.AccountUtil;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.config.XMPPConfig;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QUser;
import org.jeecg.modules.im.mapper.UserMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-18
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Resource
    private VerifyCodeService verifyCodeService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ParamService paramService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private XMPPService xmppService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private UserSettingService userSettingService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private FriendService friendService;
    @Resource
    private InviteCodeService inviteCodeService;
    @Resource
    private InvitationService invitationService;
    @Resource
    private MucService mucService;
    @Resource
    private MucInviteService mucInviteService;
    @Resource
    private MucMemberService mucMemberService;
    @Resource
    private SecretAnswerService secretAnswerService;
    @Lazy
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private XMPPConfig xmppConfig;

    @Override
    public IPage<User> pagination(MyPage<User> page, QUser q) {
        //后台群组邀请用户需要排除该群组已存在的用户id
        if (q.getMucId() != null) {
            q.setExcludeIds(ToolString.rejoin(getIdsByMucId(q.getMucId()), ","));
        }
        return userMapper.pagination(page, q);
    }

    @Override
    public List<Integer> getIdsByMucId(Integer mucId) {
        return userMapper.getIdsByMucId(mucId);
    }

    @Override
    public List<User> getByIds(String userIds) {
        if(isEmpty(userIds)){
            return Collections.emptyList();
        }
        return userMapper.getByIds(userIds);
    }

    @Override
    public User findByIdWithInfo(Integer id) {
        return userMapper.findByIdWithInfo(id);
    }
    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }
    @Override
    public String getPassword(Integer id) {
        return userMapper.getPassword(id);
    }

    @Override
    public User findBaseById(Integer id) {
        LambdaQueryWrapper<User> q = new LambdaQueryWrapper<>();
        q.select(User::getId,User::getAccount,User::getSmallAvatar,User::getNickname).eq(User::getId,id);
        return getOne(q);
    }

    @Override
    public Kv getBasicInfoById(Integer id) {
        return getBasicInfoWithoutSettingById(id).set("setting", userSettingService.findByUserId(id)).set("info", userInfoService.findByUserId(id));
    }
    public Kv getBasicInfoWithoutSettingById(Integer id) {
        User user = findByIdWithInfo(id);
        if(user==null){
            return null;
        }
        String json = JSONObject.toJSONString(user);
        Kv kv = Kv.create();
        kv.putAll(JSONObject.parseObject(json));
        kv.set("id",id);
//        kv.set("nickname",user.getNickname());
//        kv.set("smallAvatar",user.getSmallAvatar());
//        kv.set("avatar",user.getAvatar());
//        kv.set("account",user.getAccount());
//        kv.set("username",user.getUsername());
//        kv.set("tsCreate",user.getTsCreate());
//        kv.set("isOnline",user.getIsOnline());
//        kv.set("type",user.getType());
//        kv.set("tsOnline",user.getTsOnline());
//        kv.set("tsOffline",user.getTsOffline());
//        kv.set("tsMute",user.getTsMute());
//        kv.set("tsNoConnect",user.getTsNoConnect());
//        kv.set("qrCode",user.getQrCode());
        //手机号过敏
//        kv.set("mobile", ToolString.encryptMobile(user.getMobile()));
        kv.set("payPasswordSet", StringUtils.isNotBlank(user.getPayPassword())?1:0);
        kv.set("salt","");
        kv.set("password","");
        return kv;
    }
    public Kv getBasicInfo(Integer id) {
        User user = findByIdWithInfo(id);
        if(user==null){
            return null;
        }
        String json = JSONObject.toJSONString(user);
        Kv kv = Kv.create();
        kv.putAll(JSONObject.parseObject(json));
        kv.set("id",id);
//        kv.set("nickname",user.getNickname());
//        kv.set("smallAvatar",user.getSmallAvatar());
//        kv.set("avatar",user.getAvatar());
//        kv.set("account",user.getAccount());
//        kv.set("username",user.getUsername());
//        kv.set("tsCreate",user.getTsCreate());
//        kv.set("isOnline",user.getIsOnline());
//        kv.set("type",user.getType());
//        kv.set("tsOnline",user.getTsOnline());
//        kv.set("tsOffline",user.getTsOffline());
//        kv.set("tsMute",user.getTsMute());
//        kv.set("tsNoConnect",user.getTsNoConnect());
//        kv.set("qrCode",user.getQrCode());
        //手机号过敏
//        kv.set("mobile", ToolString.encryptMobile(user.getMobile()));
        kv.set("payPasswordSet", StringUtils.isNotBlank(user.getPayPassword())?1:0);
        kv.set("salt","");
        kv.set("password","");
        return kv;
    }

    //手机号注册
    @Override
    public Result<Object> register(QUser user, String verifyCode,String inviteCode) {
        Result<Object> result = inviteCodeService.checkCode(inviteCode);
        if(!result.isSuccess()){
            return result;
        }
        InviteCode code = (InviteCode) result.getResult();
        if(!isEmpty(user.getMobile())){
            return registerByMobile(user,verifyCode,code);
        }else if(!isEmpty(user.getEmail())){
            return registerByEmail(user,verifyCode,code);
        }else if(!isEmpty(user.getUsername())){
            return registerByUsername(user,code);
        }
        return fail();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<Object> registerByMobile(QUser user, String code,InviteCode inviteCode){
        user.setResource(User.Resource.MOBILE_REG.getCode());
        if (userMapper.findByMobile(user.getMobile()) != null) {
            return fail("该手机号已被注册");
        }
        //校验短信验证码
        VerifyCode verifyCode = verifyCodeService.findLatestByMobileAndType(user.getMobile(), VerifyCode.Type.Register.name());
        if (verifyCode == null || !equals(verifyCode.getCode(), code)) {
            return fail("验证码错误");
        }
        if (ToolDateTime.getDateSecondSpace(verifyCode.getTsCreate(), getTs()) > Integer.parseInt(paramService.getByName(Param.Name.verify_code_invalid_minutes, 15 * 60 + ""))) {
            return fail("验证码失效,请重新获取");
        }
        return registerUser(user,LoginLog.Way.Mobile,inviteCode);
    }
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> registerByEmail(QUser user, String code,InviteCode inviteCode){
        user.setResource(User.Resource.EMAIL_REG.getCode());
        if (userMapper.findByEmail(user.getEmail()) != null) {
            return fail("该邮箱已被注册");
        }
        //校验邮箱验证码
        VerifyCode verifyCode = verifyCodeService.findLatestByMobileAndType(user.getMobile(), VerifyCode.Type.Register.name());
        if (verifyCode == null || !equals(verifyCode.getCode(), code)) {
            return fail("验证码错误");
        }
        if (ToolDateTime.getDateSecondSpace(verifyCode.getTsCreate(), getTs()) > Integer.parseInt(paramService.getByName(Param.Name.verify_code_invalid_minutes, 15 * 60 + ""))) {
            return fail("验证码失效,请重新获取");
        }
        return registerUser(user,LoginLog.Way.Email,inviteCode);
    }
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> registerByUsername(QUser user,InviteCode inviteCode){
        user.setResource(User.Resource.USERNAME_REG.getCode());
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return fail("该用户名已存在");
        }
        return registerUser(user,LoginLog.Way.Username,inviteCode);
    }
    //注册用户
    @Transactional(rollbackFor = Exception.class)
    Result<Object> registerUser(QUser u,LoginLog.Way way,InviteCode inviteCode){
        try {
            User user = new User();
            BeanUtils.copyProperties(u,user);
            //密码加盐
            user.setSalt(PasswordEncoder.createSalt(32));
            //手机号未注册的通过验证码登录时，密码是空的,设置为初始密码
            if(StringUtils.isBlank(user.getPassword())){
                ClientConfig config = clientConfigService.get();
                user.setPassword(isEmpty(config.getDefaultPassword())?"888888":config.getDefaultPassword());
                user.setPasswordIsInit(true);
            }
            user.setPassword(ToolPassword.getEncPassword(user.getSalt(), user.getPassword()));

            String account = AccountUtil.getUserAccount();
            while (findByAccount(account) != null) {
                account = AccountUtil.getUserAccount();
            }
            user.setAccount(account);
            //设置二维码
            user.setQrCode(UUIDTool.getUUID());
            user.setRegNo(getTotalUser()+1);
            //默认昵称
            user.setTsCreate(getTs());
            if (!save(user)) {
                throw new BusinessException("保存用户失败");
            }
            UserInfo info = new UserInfo();
            info.setUserId(user.getId());
            info.setGender(u.getGender());
            info.setBirthday(u.getBirthday());
            if (!userInfoService.save(info)) {
                throw new BusinessException("保存用户失败");
            }
            ClientConfig config = clientConfigService.get();
            UserSetting setting = new UserSetting();
            setting.setUserId(user.getId());
            setting.setAccountSearch(config.getAccountSearch());
            setting.setMobileSearch(config.getMobileSearch());
            setting.setNicknameSearch(config.getNicknameSearch());
            setting.setUsernameSearch(config.getUsernameSearch());
            setting.setAccountAdd(config.getAccountAdd());
            setting.setMobileAdd(config.getMobileAdd());
            setting.setCardAdd(config.getCardAdd());
            setting.setMucAdd(config.getMucAdd());
            setting.setNicknameAdd(config.getNicknameAdd());
            setting.setScanAdd(config.getScanAdd());
            setting.setUsernameAdd(config.getUsernameAdd());
            setting.setMaxFriend(config.getMaxFriend());
            setting.setMaxFriendPerDay(config.getMaxFriendPerDay());
            setting.setMaxMucCreate(config.getMaxMucCreate());
            setting.setMaxMucJoin(config.getMaxMucJoin());
            setting.setMaxMucManage(config.getMaxMucManage());
            if(!userSettingService.save(setting)){
                throw new BusinessException("保存用户设置失败");
            }
            //查找设备
            Device device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), user.getId());
            device.setIsOnline(true);
            device.setPlatform(getDevicePlatform());
            device.setClientVer(getClientVer());
            device.setName(getDeviceName());
            device.setDetail(getDeviceDetail());
            device.setJpushId(getJPushId());
            device.setSysVer(getDeviceSystemVersion());
            //保存注册日志
            LoginLog registerLog = new LoginLog();
            registerLog.setDeviceId(device.getId());
            registerLog.setDeviceNo(device.getNo());
            registerLog.setDeviceName(device.getName());
            registerLog.setDevicePlatform(device.getPlatform());
            registerLog.setDeviceSysVer(device.getSysVer());
            registerLog.setDeviceClientVer(device.getClientVer());
            registerLog.setUserId(user.getId());
            registerLog.setLongitude(getLocationLongitude());
            registerLog.setLatitude(getLocationLatitude());
            registerLog.setIp(getIp());
            registerLog.setIpInfo(getIpInfo());
            registerLog.setIsRegister(true);
            registerLog.setTsCreate(getTs());
            registerLog.setWay(way.name());
            if (!loginLogService.save(registerLog)) {
                throw new BusinessException("保存注册日志失败");
            }
            //生成邀请码
            InviteCode myInviteCode = new InviteCode();
            myInviteCode.setUserId(user.getId());
            myInviteCode.setTsCreate(getTs());
            myInviteCode.setCode(InvitationCodeUtil.gen(user.getTsCreate(),6));
            inviteCodeService.save(myInviteCode);
            //注册xmpp用户
            boolean result = xmppService.registerUser(user.getId(), user.getPassword(),user.getNickname());
            if (!result) {
                throw new BusinessException("注册xmpp用户失败");
            }
            //添加默认好友
            List<User> defaultUsers = findDefaultAddUsers();
            batchAddFriend(user.getId(),defaultUsers);
            //邀请码
            if(inviteCode!=null){
                inviteCode.setTimes(inviteCode.getTimes()+1);
                inviteCode.setTsLast(getTs());
                inviteCodeService.updateById(inviteCode);
                if(!isEmpty(inviteCode.getUserToAdd())){
                    batchAddFriend(user.getId(),getByIds(inviteCode.getUserToAdd()));
                }
                if(!isEmpty(inviteCode.getMucToJoin())){
                    batchJoinMuc(user.getId(),mucService.getByIds(inviteCode.getMucToJoin()));
                }
                //生成邀请记录
                Invitation invitation = new Invitation();
                invitation.setUserId(user.getId());
                invitation.setTsCreate(getTs());
                invitation.setInviterId(inviteCode.getUserId());
                invitationService.save(invitation);
            }
            //关注系统号
            batchFollowSys(user.getId(),findSysUser());
            //返回token
            String token = JwtUtilApp.sign(user.getId(), user.getPassword(),user.getAccount());
            device.setToken(token);
            deviceService.updateById(device);
            Kv data = Kv.create();
            data.put("token",token);
            data.put("user",getBasicInfoById(user.getId()));
            return success(data);
        } catch (Exception e) {
            log.error("用户名注册用户异常：user={},e={}", u, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("注册失败，请稍后再试");
        }
    }


    //默认添加的用户
    private List<User> findDefaultAddUsers() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getIsDefaultFriend,true);
        query.select(User::getId,User::getNickname,User::getWelcomes);
        query.orderByAsc(true, User::getTsCreate);
        return list(query);
    }
    @Override
    public int batchAddFriend(Integer userId, List<User> users){
        int count = 0;
        for (User defaultUser : users) {
            Result addResult = friendService.addFriend(defaultUser.getId(), userId,true);
            if(addResult.isSuccess()) {
                addResult = friendService.addFriend(userId,defaultUser.getId(), false);
                if(addResult.isSuccess()){
                    count++;
                    //发送欢迎语
                    sendWelcomes(defaultUser,userId);
                }
            }
        }
        return count;
    }
    @Override
    public int batchFollowSys(Integer userId, List<User> users){
        if(users.isEmpty()){
            return 0;
        }
        int count = 0;
        for (User sysUser : users) {
            Result followResult = friendService.followUser(userId,sysUser.getId(),true);
            if(followResult.isSuccess()){
                count++;
                //发送欢迎语
                if(!isEmpty(sysUser.getWelcomes())){
                    sendWelcomes(sysUser,userId);
                }
            }
        }
        return count;
    }
    //发送欢迎语
    private void sendWelcomes(User fromUser,Integer toUserId){
        User toUser = findById(toUserId);
        if(isEmpty(fromUser.getWelcomes())) {
            return;
        }
        String[] lines = StringUtils.split(fromUser.getWelcomes(),"\r\n");
        for (String line : lines) {
            if(!isEmpty(line)){
                String[] welcomes = StringUtils.split(line,";");
                for (String welcome : welcomes) {
                    if(StringUtils.isNotBlank(welcome)){
                        if(welcome.equals("{Register No}")){
                            welcome = "您是本站第"+toUser.getRegNo()+"个注册的会员,感谢使用,祝您生活愉快~";
                        }
                        MessageBean messageBean = new MessageBean();
                        messageBean.setUserId(fromUser.getId());
                        messageBean.setToUserId(toUserId);
                        messageBean.setContent(welcome);
                        messageBean.setType(MsgType.text.getType());
                        xmppService.sendMsgToOne(messageBean);
                    }
                }
            }
        }
    }
    private int batchJoinMuc(Integer userId,List<Muc> mucs){
        int count = 0;
        for (Muc muc : mucs) {
            MucMember master = mucMemberService.getMaster(muc.getId());
            Result<Object> result = mucInviteService.inviteBatch(master.getUserId(),muc.getId(),userId.toString());
            count += (int)result.getResult();
        }
        return count;
    }

    //手机号找回密码
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> resetPwdByMobile(String mobile, String password, String code) {
        User user = userMapper.findByMobile(mobile);
        if (user == null) {
            return fail("该手机号未注册");
        }
        //校验短信验证码
        VerifyCode verifyCode = verifyCodeService.findLatestByMobileAndType(mobile, VerifyCode.Type.FindPwd.name());
        if (verifyCode == null || !equals(verifyCode.getCode(), code)) {
            return fail("验证码错误");
        }
        if (ToolDateTime.getDateSecondSpace(verifyCode.getTsCreate(), getTs()) > Integer.parseInt(paramService.getByName(Param.Name.verify_code_invalid_minutes, 15 * 60 + ""))) {
            return fail("验证码失效,请重新获取");
        }
        //更新用户密码
        try {
            String oldPwd = user.getPassword();
            //密码加盐
            user.setSalt(PasswordEncoder.createSalt(32));
            user.setPassword(ToolPassword.getEncPassword(user.getSalt(), password));
            if (!updateById(user)) {
                return fail("重置失败，请重试");
            }
            //注销已登录的设备
            int count = deviceService.clearAllTokenOfUser(user.getId());
            log.info("用户重置密码时注销设备数量：{}",count);
            //修改xmpp账号密码
            boolean result = xmppService.modifyXmppPassword(user.getId(),oldPwd, user.getPassword());
            if (!result) {
                throw new BusinessException("修改xmpp用户密码失败");
            }
            return success(user.getId());
        } catch (Exception e) {
            log.error("密码找回重置密码异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }
    //密保问题找回密码
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> resetPwdBySecretQuestion(String account, String password, String questions) {
        User user = userMapper.findByMobile(account);
        if (user == null) {
            user = userMapper.findByAccount(account);
            if (user == null) {
                user = userMapper.findByUsername(account);
                if (user == null) {
                    return fail("账号不存在,请注册");
                }
            }
        }
        if(!secretAnswerService.check(account,questions).isSuccess()){
            return fail("密保问题校验失败");
        }
        //更新用户密码
        try {
            String oldPwd = user.getPassword();
            //密码加盐
            user.setSalt(PasswordEncoder.createSalt(32));
            user.setPassword(ToolPassword.getEncPassword(user.getSalt(), password));
            if (!updateById(user)) {
                return fail("重置失败，请重试");
            }
            //注销已登录的设备
            int count = deviceService.clearAllTokenOfUser(user.getId());
            log.info("用户重置密码时注销设备数量：{}",count);
            //修改xmpp账号密码
            boolean result = xmppService.modifyXmppPassword(user.getId(),oldPwd, user.getPassword());
            if (!result) {
                throw new BusinessException("修改xmpp用户密码失败");
            }
            return success(user.getId());
        } catch (Exception e) {
            log.error("密码找回重置密码异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }

    @Override
    public User findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    @Override
    public User findByQrCode(String qrCode) {
        return userMapper.findByQrCode(qrCode);
    }

    //登录
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> login(String account, String password) {
        LoginLog loginLog = new LoginLog();
        loginLog.setWay(LoginLog.Way.Mobile.name());
        //查找用户
        User user = userMapper.findByMobile(account);
        if (user == null) {
            user = userMapper.findByUsername(account);
            if(user!=null){
                loginLog.setWay(LoginLog.Way.Username.name());
            }
        }
        if (user == null) {
            user = userMapper.findByEmail(account);
            if(user!=null){
                loginLog.setWay(LoginLog.Way.Email.name());
            }
        }
        if (user == null) {
            user = userMapper.findByAccount(account);
            if(user!=null){
                loginLog.setWay(LoginLog.Way.Account.name());
            }
        }
        if(user==null){
            return fail("账号不存在");
        }
        if(user.getPasswordIsInit()){
            return fail("该账号的密码为初始密码，请通过短信验证码登录，并尽快修改密码后再使用！");
        }
        Device device = null;
        if(!isEmpty(getDeviceDetail())){
            device = deviceService.findByDetail(getDeviceDetail(),user.getId());
        }
        if(device==null){
            device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), user.getId());
        }
        if(device==null||device.getTsDisabled()>0){
            return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"当前设备已被禁用");
        }
        try {
            if(isEmpty(user.getPassword())){
                return fail("此账号未设置密码，请通过短信验证码进行登录");
            }
            ///密码校验
            if (!ToolPassword.checkPassword(user.getSalt(), user.getPassword(), password)) {
                return fail("账号或密码错误");
            }
            if (user.getTsLocked()>0) {
                return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"账号已被锁定");
            }
            if(!isEmpty(user.getGoogleCode())&&user.getEnableGoogleCode()){
                return success(Kv.by("needGoogleCode",true));
            }

            return success(loginSuccess(user,loginLog,device));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户登录异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("登录异常,请重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> checkGoogleCode(String account,String googleCode) {
        LoginLog loginLog = new LoginLog();
        loginLog.setWay(LoginLog.Way.Mobile.name());
        //查找用户
        User user = userMapper.findByMobile(account);
        if (user == null) {
            user = userMapper.findByAccount(account);
            if (user == null) {
                user = userMapper.findByUsername(account);
                if (user == null) {
                    return fail("账号不存在,请注册");
                }
            }
            loginLog.setWay(LoginLog.Way.Account.name());
        }
        try {
            if (user.getTsLocked()>0) {
                return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"账号已被锁定");
            }
            Device device = null;
            if(!isEmpty(getDeviceDetail())){
                device = deviceService.findByDetail(getDeviceDetail(),user.getId());
            }
            if(device==null){
                device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), user.getId());
            }
            if(device==null||device.getTsDisabled()>0){
                return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"当前设备已被禁用");
            }
            if(isEmpty(user.getGoogleCode())||!GoogleAuthenticator.authcode(googleCode,user.getGoogleCode())){
                return fail("谷歌验证码错误");
            }
            return success(loginSuccess(user,loginLog,device));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户登录异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("登录异常,请重试");
        }
    }

    /**
     * 手机验证码登录
     * @param mobile
     * @param code
     * @param type
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> loginBySmsCode(String mobile, String code, String type) {
        User user = userMapper.findByMobile(mobile);
        //校验短信验证码
        VerifyCode verifyCode = verifyCodeService.findLatestByMobileAndType(mobile, type);
        if (verifyCode == null || !equals(verifyCode.getCode(), code)) {
            return fail("验证码错误");
        }
        if (ToolDateTime.getDateSecondSpace(verifyCode.getTsCreate(), getTs()) > Integer.parseInt(paramService.getByName(Param.Name.verify_code_invalid_minutes, 15 * 60 + ""))) {
            return fail("验证码失效,请重新获取");
        }
        //未注册的直接注册并告知前台需设置密码后才能用
        if (user == null) {
//            return fail("该手机号未注册");
            QUser u = new QUser();
            u.setMobile(mobile);
            String nickname = NameUtil.getName();
            ClientConfig config = clientConfigService.get();
            if(config.getNicknameUnique()){
                while(getCountOfNickname(nickname)>0){
                    nickname = NameUtil.getName();
                }
            }
            u.setNickname(nickname);
            try {
                return registerUser(u,LoginLog.Way.Mobile,null);
            }catch (Exception e){
                return fail();
            }
        }
        if (user.getTsLocked() > 0) {
            return fail(ConstantZhiLiao.ACCOUNT_LOCKED, "账号已被锁定");
        }
        Device device = null;
        if(!isEmpty(getDeviceDetail())){
            device = deviceService.findByDetail(getDeviceDetail(),user.getId());
        }
        if(device==null){
            device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), user.getId());
        }
        if(device==null||device.getTsDisabled()>0){
            return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"当前设备已被禁用");
        }
        LoginLog loginLog = new LoginLog();
        loginLog.setWay(LoginLog.Way.SmsCode.name());

        try {
            return success(loginSuccess(user,loginLog,device));
        } catch (Exception e) {
            log.error("短信验证码登录异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    /**
     * token自动登录
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> tokenLogin(Boolean isScan) {
        String accessToken = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        try {
            Integer userId = JwtUtilApp.getUserIdByToken(accessToken);
            User user = findById(userId);
            if (user == null) {
                return fail("账号不存在");
            }
            if (user.getTsLocked()>0) {
                return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"账号已被锁定");
            }
            Device device = null;
            if(!isEmpty(getDeviceDetail())){
                device = deviceService.findByDetail(getDeviceDetail(),user.getId());
            }
            if(device==null){
                device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), user.getId());
            }
            if(device==null){
                return fail("设备未登录过");
            }
            if(device.getTsDisabled()>0){
                return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"当前设备已被禁用");
            }
            //自动登录时校验设备token
            if(!equals(accessToken,device.getToken())&&(isScan==null||!isScan)){
                return fail("令牌过期，请重新登录！");
            }
            LoginLog loginLog = new LoginLog();
            loginLog.setWay(isScan!=null&&isScan?LoginLog.Way.Scan.name():LoginLog.Way.AutoLogin.name());
            return  success(loginSuccess(user,loginLog,device));
        } catch (Exception e) {
            log.error("自动登录异常：token={},e={}", accessToken, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail(e.getMessage());
        }
    }

    private Kv loginSuccess(User user,LoginLog loginLog,Device device) throws BusinessException {
        user.setTsOnline(getTs());
        updateById(user);
        device.setIsOnline(true);
        device.setPlatform(getDevicePlatform());
        device.setClientVer(getClientVer());
        device.setDetail(getDeviceDetail());
        device.setName(getDeviceName());
        device.setSysVer(getDeviceSystemVersion());
        device.setJpushId(getJPushId());
        //将其他相同jpushid的设备清空jpushid
        Result result = deviceService.cleanJpushId(device.getJpushId(),device.getId());
        if(!result.isSuccess()){
            throw new BusinessException("解绑jpushId失败");
        }
        //保存登录日志
        loginLog.setDeviceId(device.getId());
        loginLog.setDeviceNo(device.getNo());
        loginLog.setDeviceName(device.getName());
        loginLog.setDevicePlatform(device.getPlatform());
        loginLog.setDeviceSysVer(device.getSysVer());
        loginLog.setDeviceClientVer(device.getClientVer());
        loginLog.setUserId(user.getId());
        loginLog.setLongitude(getLocationLongitude());
        loginLog.setLatitude(getLocationLatitude());
        loginLog.setIp(getIp());
        loginLog.setIpInfo(getIpInfo());
        loginLog.setTsCreate(getTs());

        if (!loginLogService.save(loginLog)) {
            throw new BusinessException("保存登录日志失败");
        }

        //返回token
        String token = JwtUtilApp.sign(user.getId(), user.getPassword(),user.getAccount());
        device.setToken(token);
        deviceService.updateById(device);

        device.setLoginLog(loginLogService.findLatestByDeviceId(device.getId()));
        device.setDetail("");

        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(user.getId());
        messageBean.setContent(JSONObject.toJSONString(Kv.by("device",device)));
        messageBean.setType(MsgType.deviceOnline.getType());
        xmppService.sendMsgToSelf(messageBean);

        Kv data = Kv.create();
        data.put("token",token);
        data.put("user",getBasicInfoById(user.getId()));
        return data;
    }

    @Override
    public Result<Object> updateInfo(QUser param) {
        User user = getCurrentUser();
        UserInfo info = userInfoService.findBasicByUserId(user.getId());
        try {
            //修改昵称
            if (!isEmpty(param.getNickname()) && !user.getNickname().equals(param.getNickname())) {
                int nicknameLen = ToolString.getNicknameLength(param.getNickname());
                if (nicknameLen == 0 || nicknameLen > 36) {
                    return fail("昵称为1~36个字符,由字母、数字、中文和特殊符号组成");
                }
                //昵称唯一
                ClientConfig config = clientConfigService.get();
                if (config.getNicknameUnique() && getCountOfNickname(param.getNickname()) > 0) {
                    return fail("昵称已存在");
                }
                user.setNickname(param.getNickname());
                //记录修改昵称日志

            }
            //用户名修改
            if(param.getUsername()!=null){
                if(isEmpty(param.getUsername())){
                    user.setUsername("");
                }
                if (!isEmpty(param.getUsername()) && !user.getUsername().equals(param.getUsername())) {
                    if (!ToolString.regExpVali(ToolString.pattern_username, param.getUsername())) {
                        return fail("用户名格式错误");
                    }
                    user.setUsername(param.getUsername());
                }
            }
            //修改地区
            if (!isEmpty(param.getCountry())) {
                info.setCountry(param.getCountry());
            }
            if (!isEmpty(param.getCountryCode())) {
                info.setCountryCode(param.getCountryCode());
            }
            if (!isEmpty(param.getCountryDialCode())) {
                info.setCountryDialCode(param.getCountryDialCode());
            }
            if (!isEmpty(param.getProvince())) {
                info.setProvince(param.getProvince());
            }
            if (!isEmpty(param.getCity())) {
                info.setCity(param.getCity());
            }
            if (!isEmpty(param.getDistrict())) {
                info.setDistrict(param.getDistrict());
            }
            if (!isEmpty(param.getAddress())) {
                info.setAddress(param.getAddress());
            }
            //修改签名
            if (param.getSignature() != null) {
                info.setSignature(param.getSignature());
            }
            //性别
            if (!isEmpty(param.getGender())) {
                info.setGender(param.getGender());
            }
            //头像
            if (!isEmpty(param.getSmallAvatar())) {
                user.setSmallAvatar(param.getSmallAvatar());
            }
            if (!isEmpty(param.getAvatar())) {
                user.setAvatar(param.getAvatar());
            }
            //二维码
            if (!isEmpty(param.getQrCode())) {
                user.setQrCode(param.getQrCode());
            }
            //更新
            if (!updateById(user) || !userInfoService.updateById(info)) {
                return fail();
            }
            //发送用户资料更新给其他设备
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(user.getId());
            messageBean.setContent(JSONObject.toJSONString(getBasicInfoWithoutSettingById(user.getId())));
            messageBean.setType(MsgType.updateInfo.getType());
            xmppService.sendMsgToSelf(messageBean);
            return success();
        }catch (Exception e){
            log.error("修改用户资料异常：{0}",e);
            return fail("操作失败，请重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updatePwd(Integer userId,String oldPwd,String newPwd) {
        User user = findById(userId);
        if(user==null){
            return fail("用户不存在");
        }
        if(equals(oldPwd,newPwd)){
            return fail("新密码不能与旧密码相同！");
        }
        if(!ToolPassword.checkPassword(user.getSalt(),user.getPassword(),oldPwd)){
            return fail("旧密码错误");
        }
        try{
            String oldXmppPwd = user.getPassword();
            user.setSalt(PasswordEncoder.createSalt(32));
            user.setPassword(ToolPassword.getEncPassword(user.getSalt(),newPwd));
            if(!updateById(user)){
                return fail();
            }
            //注销已登录的设备
            int count = deviceService.clearAllTokenOfUser(user.getId());
            log.info("更新用户密码时注销设备数量：{}",count);
            //更新xmpp账号密码
            boolean result = xmppService.modifyXmppPassword(user.getId(), oldXmppPwd, user.getPassword());
            if (!result) {
                throw new Exception("xmpp update password error");
            }
            return success();
        }catch (Exception e){
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail();
        }
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public Result<Object> search(Integer userId,String keyword,Integer type) {
        ClientConfig clientConfig = clientConfigService.get();
        return success(userMapper.search(userId,keyword,type,clientConfig.getAccountSearch(),clientConfig.getMobileSearch(),clientConfig.getNicknameSearch(),clientConfig.getUsernameSearch(),clientConfig.getNicknameSearchExact()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleLogin(Integer id, String resource) {
        try {
            User user = findById(id);
            user.setIsOnline(true);
            user.setTsOnline(getTs());
            updateById(user);
            String platform = resource.split("-")[0];
            String deviceNo = resource.substring(resource.indexOf("-")+1);
            Device device = deviceService.findByPlatform(deviceNo,platform,user.getId());
            device.setIsOnline(true);
            device.setTsOnline(getTs());

            deviceService.updateById(device);
            device.setLoginLog(loginLogService.findLatestByDeviceId(device.getId()));
            device.setDetail("");

            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(user.getId());
            Kv data = Kv.by("device",device);
            messageBean.setContent(JSONObject.toJSONString(data));
            messageBean.setType(MsgType.deviceOnline.getType());
            xmppService.sendMsgToSelf(messageBean);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeConnection(Integer userId, String resource,boolean userOffline)  {
        try {
            User user = findById(userId);
            if(userOffline){
                user.setIsOnline(false);
                user.setTsOffline(getTs());
                updateById(user);
            }
            String platform = resource.split("-")[0];
            String deviceNo = resource.substring(resource.indexOf("-")+1);
            Device device = deviceService.findByPlatform(deviceNo,platform,user.getId());
            device.setIsOnline(false);
            device.setTsOffline(getTs());
            deviceService.updateById(device);

            device.setLoginLog(loginLogService.findLatestByDeviceId(device.getId()));
            device.setDetail("");

            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(user.getId());
            Kv data = Kv.by("device",device);
            messageBean.setContent(JSONObject.toJSONString(data));
            messageBean.setType(MsgType.deviceOnline.getType());
            xmppService.sendMsgToSelf(messageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> logout()  {
        try {
            String token = request.getHeader("X-Access-Token");
            User user = getCurrentUser();
            if(user!=null) {
                log.info(" 用户:  "+user.getNickname()+"->"+user.getAccount()+",退出成功！ ");
                //清空用户登录Token缓存
                redisUtil.del(CommonConstant.PREFIX_USER_TOKEN_API + token);
                //查找设备
                //查找设备
                Device device = null;
                if(!isEmpty(getDeviceDetail())){
                    device = deviceService.findByDetail(getDeviceDetail(),user.getId());
                }
                if(device==null){
                    device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), user.getId());
                }
//                device.setIsOnline(false);
//                device.setTsOffline(getTs());
                device.setToken(null);
                deviceService.updateById(device);
//                device.setLoginLog(loginLogService.findLatestByDeviceId(device.getId()));
//
//                MessageBean messageBean = new MessageBean();
//                messageBean.setUserId(getCurrentUserId());
//                device.setDetail("");
//                device.setToken("");
//                Kv data = Kv.by("device",device);
//                messageBean.setContent(JSONObject.toJSONString(data));
//                messageBean.setType(MsgType.DEVICE_ONLINE.getType());
//                xmppService.sendMsgToSelf(messageBean);
//                //查找在线设备数量
//                if(deviceService.getCountOfUser(user.getId())==0){
//                    user.setIsOnline(false);
//                    user.setTsOffline(getTs());
//                    updateById(user);
//                }
                return success();
            }else {
                return fail("Token无效!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return fail();
        }
    }

    @Override
    public Result<Object> consoleCreateOrUpdate(User user) {
        if (user.getId() == null) {
            return consoleCreate(user);
        }
        return consoleUpdate(user);
    }
    @Override
    public Result<Object> consoleKickOut(Integer userId) {
        try {
            User user = findById(userId);
            MessageBean messageBean = new MessageBean();
            messageBean.setToUserId(user.getId());
            messageBean.setToUserName(user.getNickname());
            messageBean.setType(MsgType.offlineKick.getType());
            boolean result = xmppService.sendMsgBySys(messageBean);
            if(result){
                return success();
            }
            return fail();
        } catch (Exception e) {
            log.error(e.getMessage());
            return fail();
        }
    }
    @Override
    public Result<Object> setDefaultFriend(Integer userId) {
        try {
            User user = findById(userId);
            user.setIsDefaultFriend(!user.getIsDefaultFriend());
            boolean result = updateById(user);
            if(result){
                return success();
            }
            return fail();
        } catch (Exception e) {
            log.error(e.getMessage());
            return fail();
        }
    }
    @Override
    public Result<Object> consoleMute(Integer id,Long tsMute) {
        try {
            User user = findById(id);
            user.setTsMute(tsMute);
            updateById(user);
            MessageBean messageBean = new MessageBean();
            messageBean.setToUserId(user.getId());
            messageBean.setToUserName(user.getNickname());
            messageBean.setType(MsgType.mute.getType());
            messageBean.setFlag(tsMute>0?1:0);
            messageBean.setContent(tsMute.toString());
            boolean result = xmppService.sendMsgBySys(messageBean);
            if(result){
                return success();
            }
            return fail();
        } catch (Exception e) {
            log.error(e.getMessage());
            return fail();
        }
    }
    @Override
    public Result<Object> consoleLock(Integer id,Long tsLocked) {
        try {
            User user = findById(id);
            user.setTsLocked(tsLocked);
            updateById(user);
            MessageBean messageBean = new MessageBean();
            messageBean.setToUserId(user.getId());
            messageBean.setToUserName(user.getNickname());
            messageBean.setType(MsgType.lock.getType());
            messageBean.setFlag(tsLocked>0?1:0);
            messageBean.setContent(tsLocked.toString());
            boolean result = xmppService.sendMsgBySys(messageBean);
            if(result){
                return success();
            }
            return fail();
        } catch (Exception e) {
            log.error(e.getMessage());
            return fail();
        }
    }
    @Override
    public Result<Object> consoleNoConnect(Integer id,Long tsNoConnect) {
        try {
            User user = findById(id);
            user.setTsNoConnect(tsNoConnect);
            updateById(user);
            if(tsNoConnect>0){
                MessageBean messageBean = new MessageBean();
                messageBean.setToUserId(user.getId());
                messageBean.setToUserName(user.getNickname());
                messageBean.setType(MsgType.noConnectXmpp.getType());
                messageBean.setFlag(1);
                messageBean.setContent(tsNoConnect.toString());
                boolean result = xmppService.sendMsgBySys(messageBean);
                if(result){
                    return success();
                }
            }else{
                return success();
            }

            return fail();
        } catch (Exception e) {
            log.error(e.getMessage());
            return fail();
        }
    }

    @Override
    public Integer getOnlineCount() {
        return userMapper.getOnlineCount();
    }

    @Override
    public Integer getTotalUser() {
        return userMapper.getTotalUser();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<Object> consoleCreate(User user) {
        if (userMapper.findByMobile(user.getMobile()) != null) {
            return fail("该手机号已被注册");
        }
        if(!ToolString.regExpVali(Pattern.compile("^[a-zA-Z0-9_]{1,16}$"),user.getAccount())){
            return fail("账号不符合要求");
        }
        if(userMapper.findByAccount(user.getAccount())!=null){
            return fail("账号已存在");
        }
        //注册新用户，保存注册日志，注册xmpp用户
        try {
            //密码加盐
            user.setSalt(PasswordEncoder.createSalt(32));
            user.setPassword(ToolPassword.getEncPassword(user.getSalt(), user.getPassword()));
            //设置二维码
            user.setQrCode(UUIDTool.getUUID());
            user.setResource(User.Resource.CONSOLE_CREATE.getCode());
            user.setTsCreate(getTs());
            if (!save(user)) {
                throw new BusinessException("保存用户失败");
            }
            UserInfo info = new UserInfo();
            info.setUserId(user.getId());
            if (!userInfoService.save(info)) {
                throw new BusinessException("保存用户失败");
            }
            ClientConfig config = clientConfigService.get();
            int nicknameLen = ToolString.getNicknameLength(user.getNickname());
            if (nicknameLen==0||nicknameLen>36) {
                return fail("昵称为1~36个字符,由字母、数字、中文和特殊符号组成");
            }
            //昵称唯一
            if(config.getNicknameUnique()&&getCountOfNickname(user.getNickname())>0){
                throw new BusinessException("昵称已存在");
            }
            UserSetting setting = new UserSetting();
            setting.setUserId(user.getId());
            setting.setAccountSearch(config.getAccountSearch());
            setting.setMobileSearch(config.getMobileSearch());
            setting.setNicknameSearch(config.getNicknameSearch());
            setting.setUsernameSearch(config.getUsernameSearch());
            setting.setAccountAdd(config.getAccountAdd());
            setting.setMobileAdd(config.getMobileAdd());
            setting.setCardAdd(config.getCardAdd());
            setting.setMucAdd(config.getMucAdd());
            setting.setNicknameAdd(config.getNicknameAdd());
            setting.setScanAdd(config.getScanAdd());
            setting.setUsernameAdd(config.getUsernameAdd());
            setting.setMaxFriend(config.getMaxFriend());
            setting.setMaxFriendPerDay(config.getMaxFriendPerDay());
            setting.setMaxMucCreate(config.getMaxMucCreate());
            setting.setMaxMucJoin(config.getMaxMucJoin());
            setting.setMaxMucManage(config.getMaxMucManage());
            if(!userSettingService.save(setting)){
                throw new BusinessException("保存用户设置失败");
            }
            //注册xmpp用户
            boolean result = xmppService.registerUser(user.getId(), user.getPassword(),user.getNickname());
            if (!result) {
                throw new BusinessException("注册xmpp用户失败");
            }
            return success();
        } catch (Exception e) {
            log.error("手机号注册用户异常：user={},e={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("注册失败，请稍后再试");
        }
    }

    public Integer getCountOfNickname(String nickname) {
        return getCountOfNicknameExceptUserId(nickname,null);
    }

    @Override
    public Result<Object> setPayPassword(User user, String oldPwd, String pwd) {
        //如已设置，则校验
        if(StringUtils.isNotBlank(user.getPayPassword())){
            if(!ToolPassword.checkPassword(user.getPaySalt(),user.getPayPassword(),oldPwd)){
                return fail("旧密码错误");
            }
        }
        //密码加盐
        user.setPaySalt(PasswordEncoder.createSalt(32));
        user.setPayPassword(ToolPassword.getEncPassword(user.getPaySalt(), pwd));
        updateById(user);
        //发送用户资料更新给其他设备
        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(user.getId());
        messageBean.setContent(JSONObject.toJSONString(getBasicInfoWithoutSettingById(user.getId())));
        messageBean.setType(MsgType.updateInfo.getType());
        xmppService.sendMsgToSelf(messageBean);
        return success();
    }

    @Override
    public Result<Object> setGoogleCode(User user, String code,Boolean enable) {
        //解绑
        if(isEmpty(code)&&enable==null){
            user.setGoogleCode("");
            user.setEnableGoogleCode(false);
        }else{
            //启用/关闭
            if(enable!=null){
                user.setEnableGoogleCode(enable);
            }else{
                //设置、修改
                user.setGoogleCode(code);
                user.setEnableGoogleCode(!isEmpty(code));
            }
        }
        updateById(user);
        //发送用户资料更新给其他设备
        MessageBean messageBean = new MessageBean();
        messageBean.setUserId(user.getId());
        messageBean.setContent(JSONObject.toJSONString(Kv.by("googleCode",user.getGoogleCode()).set("enable",user.getEnableGoogleCode())));
        messageBean.setType(MsgType.updateGoogleCode.getType());
        xmppService.sendMsgToSelf(messageBean);
        return success();
    }

    @Override
    public List<User> findSysUser() {
        return userMapper.findSysUser(User.Type.sysAccount.getCode());
    }

    public int getCountOfNicknameExceptUserId(String nickname,Integer exceptUserId) {
        QUser user = new QUser();
        user.setNickname(nickname);
        user.setId(exceptUserId);
        return userMapper.getCountOfNickname(user);
    }

    public Result<Object> consoleUpdate(User user) {
        try {
            User temp;
            if(!isEmpty(user.getMobile())){
                temp = userMapper.findByMobile(user.getMobile());
                if (temp != null&&temp.getId()-user.getId()!=0) {
                    return fail("该手机号已被注册");
                }
            }
            if(!isEmpty(user.getAccount())){
                if(!ToolString.regExpVali(Pattern.compile("^[a-zA-Z0-9_]{1,16}$"),user.getAccount())){
                    return fail("账号不符合要求");
                }
                temp = userMapper.findByAccount(user.getAccount());
                if(temp!=null&&temp.getId()-user.getId()!=0){
                    return fail("账号已存在");
                }
            }
            if(!isEmpty(user.getNickname())){
                int nicknameLen = ToolString.getNicknameLength(user.getNickname());
                if (nicknameLen==0||nicknameLen>36) {
                    return fail("昵称为1~36个字符,由字母、数字、中文和特殊符号组成");
                }
                ClientConfig config = clientConfigService.get();
                //昵称唯一
                if(config.getNicknameUnique()&&getCountOfNicknameExceptUserId(user.getNickname(),user.getId())>0){
                    return fail("昵称已存在");
                }
            }
            if(!isEmpty(user.getPassword())) {
                if (!ToolString.valiPasswordQQ(user.getPassword())) {
                    return fail("密码由6~20位数字、大小写字母或符号组成，至少含有2种以上字符");
                }
                //密码加盐
                String oldPwd = findById(user.getId()).getPassword();
                user.setSalt(PasswordEncoder.createSalt(32));
                user.setPassword(ToolPassword.getEncPassword(user.getSalt(), user.getPassword()));
                //注销已登录的设备
                int count = deviceService.clearAllTokenOfUser(user.getId());
                log.info("后台更新用户密码时注销设备数量：{}",count);
                //更新xmpp账号密码
                boolean result = xmppService.modifyXmppPassword(user.getId(), oldPwd, user.getPassword());
                if (!result) {
                    return fail("更新失败");
                }
            }
            if(!isEmpty(user.getPayPassword())){
                //密码加盐
                user.setPaySalt(PasswordEncoder.createSalt(32));
                user.setPayPassword(ToolPassword.getEncPassword(user.getPaySalt(), user.getPayPassword()));
            }
            if (!updateById(user)) {
                return fail();
            }
            return success();
        }catch (Exception e){
            log.error("更新用户失败",e);
            return fail();
        }
    }

    @Override
    public List<User> findByTypes(List<Integer> types) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.in(User::getType,types);
        query.eq(User::getTsLocked,0);
        return  list(query);
    }
}
