package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeecg.dingtalk.api.base.JdtBaseAPI;
import com.jeecg.dingtalk.api.core.response.Response;
import com.jeecg.dingtalk.api.core.vo.AccessToken;
import com.jeecg.dingtalk.api.user.JdtUserAPI;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysRoleMapper;
import org.jeecg.modules.system.mapper.SysThirdAccountMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.model.ThirdLoginModel;
import org.jeecg.modules.system.service.ISysThirdAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 第三方登录账号表
 * @Author: jeecg-boot
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysThirdAccountServiceImpl extends ServiceImpl<SysThirdAccountMapper, SysThirdAccount> implements ISysThirdAccountService {
    
    @Autowired
    private  SysThirdAccountMapper sysThirdAccountMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    
    @Value("${justauth.type.DINGTALK.client-id:}")
    private String dingTalkClientId;   
    @Value("${justauth.type.DINGTALK.client-secret:}")
    private String dingTalkClientSecret;
    
    @Override
    public void updateThirdUserId(SysUser sysUser,String thirdUserUuid) {
        //修改第三方登录账户表使其进行添加用户id
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getThirdUserUuid,thirdUserUuid);
        //扫码登录更新用户创建的时候存的是默认租户，更新的时候也需要根据默认租户来查询，同一个公司下UUID是一样的，不同应用需要区分租户。
        query.eq(SysThirdAccount::getTenantId,CommonConstant.TENANT_ID_DEFAULT_VALUE);
        SysThirdAccount account = sysThirdAccountMapper.selectOne(query);
        SysThirdAccount sysThirdAccount = new SysThirdAccount();
        sysThirdAccount.setSysUserId(sysUser.getId());
        //根据当前用户id和登录方式查询第三方登录表
        LambdaQueryWrapper<SysThirdAccount> thirdQuery = new LambdaQueryWrapper<>();
        thirdQuery.eq(SysThirdAccount::getSysUserId,sysUser.getId());
        thirdQuery.eq(SysThirdAccount::getThirdType,account.getThirdType());
        thirdQuery.eq(SysThirdAccount::getThirdUserUuid,thirdUserUuid);
        thirdQuery.eq(SysThirdAccount::getTenantId,CommonConstant.TENANT_ID_DEFAULT_VALUE);
        SysThirdAccount sysThirdAccounts = sysThirdAccountMapper.selectOne(thirdQuery);
        if(sysThirdAccounts!=null){
            sysThirdAccount.setThirdUserId(sysThirdAccounts.getThirdUserId());
            sysThirdAccountMapper.deleteById(sysThirdAccounts.getId());
        }
        //更新用户账户表sys_user_id
        sysThirdAccountMapper.update(sysThirdAccount,query);
    }
    
    @Override
    public SysUser createUser(String phone, String thirdUserUuid, Integer tenantId) {
       //先查询第三方，获取登录方式
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getThirdUserUuid,thirdUserUuid);
        query.eq(SysThirdAccount::getTenantId,tenantId);
        SysThirdAccount account = sysThirdAccountMapper.selectOne(query);
        //通过用户名查询数据库是否已存在
        SysUser userByName = sysUserMapper.getUserByName(thirdUserUuid);
        if(null!=userByName){
            //如果账号存在的话，则自动加上一个时间戳
            String format = DateUtils.yyyymmddhhmmss.get().format(new Date());
            thirdUserUuid = thirdUserUuid + format;
        }
        //添加用户
        SysUser user = new SysUser();
        user.setActivitiSync(CommonConstant.ACT_SYNC_1);
        user.setDelFlag(CommonConstant.DEL_FLAG_0);
        user.setStatus(1);
        user.setUsername(thirdUserUuid);
        user.setPhone(phone);
        //设置初始密码
        String salt = oConvertUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), "123456", salt);
        user.setPassword(passwordEncode);
        user.setRealname(account.getRealname());
        user.setAvatar(account.getAvatar());
        String s = this.saveThirdUser(user);
        //更新用户第三方账户表的userId
        SysThirdAccount sysThirdAccount = new SysThirdAccount();
        sysThirdAccount.setSysUserId(s);
        sysThirdAccount.setTenantId(tenantId);
        sysThirdAccountMapper.update(sysThirdAccount,query);
        return user;
    }
    
    public String saveThirdUser(SysUser sysUser) {
        //保存用户
        String userid = UUIDGenerator.generate();
        sysUser.setId(userid);
        sysUserMapper.insert(sysUser);
        //获取第三方角色
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
        //保存用户角色
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(sysRole.getId());
        userRole.setUserId(userid);
        sysUserRoleMapper.insert(userRole);
        return userid;
    }

    @Override
    public SysThirdAccount getOneBySysUserId(String sysUserId, String thirdType) {
        LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
        log.info("getSysUserId: {} ,getThirdType: {}",sysUserId,thirdType);
        queryWrapper.eq(SysThirdAccount::getSysUserId, sysUserId);
        queryWrapper.eq(SysThirdAccount::getThirdType, thirdType);
        return super.getOne(queryWrapper);
    }

    @Override
    public SysThirdAccount getOneByThirdUserId(String thirdUserId, String thirdType) {
        LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThirdAccount::getThirdUserId, thirdUserId);
        queryWrapper.eq(SysThirdAccount::getThirdType, thirdType);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<SysThirdAccount> listThirdUserIdByUsername(String[] sysUsernameArr, String thirdType, Integer tenantId) {
        return sysThirdAccountMapper.selectThirdIdsByUsername(sysUsernameArr, thirdType,tenantId);
    }

    @Override
    public SysThirdAccount saveThirdUser(ThirdLoginModel tlm, Integer tenantId) {
        SysThirdAccount user = new SysThirdAccount();
        user.setDelFlag(CommonConstant.DEL_FLAG_0);
        user.setStatus(1);
        user.setThirdType(tlm.getSource());
        user.setAvatar(tlm.getAvatar());
        user.setRealname(tlm.getUsername());
        user.setThirdUserUuid(tlm.getUuid());
        user.setTenantId(tenantId);
        //update-begin---author:wangshuai ---date:20230306  for：判断如果是钉钉的情况下，需要将第三方的用户id查询出来，发送模板的时候有用------------
        //=============begin 判断如果是钉钉的情况下，需要将第三方的用户id查询出来，发送模板的时候有用==========
        if(CommonConstant.DINGTALK.toLowerCase().equals(tlm.getSource())){
            AccessToken accessToken = JdtBaseAPI.getAccessToken(dingTalkClientId, dingTalkClientSecret);
            Response<String> getUserIdRes = JdtUserAPI.getUseridByUnionid(tlm.getUuid(), accessToken.getAccessToken());
            if (getUserIdRes.isSuccess()) {
                user.setThirdUserId(getUserIdRes.getResult());
            }else{
                user.setThirdUserId(tlm.getUuid());
            }
        //=============end 判断如果是钉钉的情况下，需要将第三方的用户id查询出来，发送模板的时候有用==========
        }else{
            user.setThirdUserId(tlm.getUuid());
        }
        //update-end---author:wangshuai ---date:20230306  for：判断如果是钉钉的情况下，需要将第三方的用户id查询出来，发送模板的时候有用------------
        super.save(user);
        return user;
    }

    @Override
    public SysThirdAccount bindThirdAppAccountByUserId(SysThirdAccount sysThirdAccount) {
        String thirdUserUuid = sysThirdAccount.getThirdUserUuid();
        String thirdType = sysThirdAccount.getThirdType();
        //获取当前登录用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前第三方用户已被其他用户所绑定
        SysThirdAccount oneByThirdUserId = this.getOneByUuidAndThirdType(thirdUserUuid, thirdType,CommonConstant.TENANT_ID_DEFAULT_VALUE, null);
        if(null != oneByThirdUserId){
            //如果不为空，并且第三方表和当前登录的用户一致，直接返回
            if(oConvertUtils.isNotEmpty(oneByThirdUserId.getSysUserId()) && oneByThirdUserId.getSysUserId().equals(sysUser.getId())){
                return oneByThirdUserId;
            }else if(oConvertUtils.isNotEmpty(oneByThirdUserId.getSysUserId())){
                //如果第三方表的用户id不为空，那就说明已经绑定过了
                throw new JeecgBootException("该敲敲云账号已被其它第三方账号绑定,请解绑或绑定其它敲敲云账号");
            }else{
                //更新第三方表信息用户id
                oneByThirdUserId.setSysUserId(sysUser.getId());
                oneByThirdUserId.setThirdType(thirdType);
                sysThirdAccountMapper.updateById(oneByThirdUserId);
                return oneByThirdUserId;
            }
        }else{
            throw new JeecgBootException("账号绑定失败,请稍后重试");
        }
    }

    @Override
    public SysThirdAccount getOneByUuidAndThirdType(String unionid, String thirdType,Integer tenantId,String thirdUserId) {
        LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThirdAccount::getThirdType, thirdType);
        //update-begin---author:wangshuai---date:2023-12-04---for: 如果第三方用户id为空那么就不走第三方用户查询逻辑，因为扫码登录third_user_id是唯一的，没有重复的情况---
        if(oConvertUtils.isNotEmpty(thirdUserId)){
            queryWrapper.and((wrapper) ->wrapper.eq(SysThirdAccount::getThirdUserUuid,unionid).or().eq(SysThirdAccount::getThirdUserId,thirdUserId));
        }else{
            queryWrapper.eq(SysThirdAccount::getThirdUserUuid, unionid);
        }
        //update-end---author:wangshuai---date:2023-12-04---for:如果第三方用户id为空那么就不走第三方用户查询逻辑，因为扫码登录third_user_id是唯一的，没有重复的情况---
        queryWrapper.eq(SysThirdAccount::getTenantId, tenantId);
        return super.getOne(queryWrapper);
    }

}
