package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
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
public class SysThirdAccountServiceImpl extends ServiceImpl<SysThirdAccountMapper, SysThirdAccount> implements ISysThirdAccountService {
    
    @Autowired
    private  SysThirdAccountMapper sysThirdAccountMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    
    @Override
    public void updateThirdUserId(SysUser sysUser,String thirdUserUuid) {
        //修改第三方登录账户表使其进行添加用户id
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getThirdUserUuid,thirdUserUuid);
        SysThirdAccount account = sysThirdAccountMapper.selectOne(query);
        SysThirdAccount sysThirdAccount = new SysThirdAccount();
        sysThirdAccount.setSysUserId(sysUser.getId());
        //根据当前用户id和登录方式查询第三方登录表
        LambdaQueryWrapper<SysThirdAccount> thirdQuery = new LambdaQueryWrapper<>();
        thirdQuery.eq(SysThirdAccount::getSysUserId,sysUser.getId());
        thirdQuery.eq(SysThirdAccount::getThirdType,account.getThirdType());
        SysThirdAccount sysThirdAccounts = sysThirdAccountMapper.selectOne(thirdQuery);
        if(sysThirdAccounts!=null){
            sysThirdAccount.setThirdUserId(sysThirdAccounts.getThirdUserId());
            sysThirdAccountMapper.deleteById(sysThirdAccounts.getId());
        }
        //更新用户账户表sys_user_id
        sysThirdAccountMapper.update(sysThirdAccount,query);
    }
    
    @Override
    public SysUser createUser(String phone, String thirdUserUuid) {
       //先查询第三方，获取登录方式
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getThirdUserUuid,thirdUserUuid);
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
        user.setActivitiSync(CommonConstant.ACT_SYNC_0);
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
    public List<SysThirdAccount> listThirdUserIdByUsername(String[] sysUsernameArr, String thirdType) {
        return sysThirdAccountMapper.selectThirdIdsByUsername(sysUsernameArr, thirdType);
    }

    @Override
    public SysThirdAccount saveThirdUser(ThirdLoginModel tlm) {
        SysThirdAccount user = new SysThirdAccount();
        user.setDelFlag(CommonConstant.DEL_FLAG_0);
        user.setStatus(1);
        user.setThirdType(tlm.getSource());
        user.setAvatar(tlm.getAvatar());
        user.setRealname(tlm.getUsername());
        user.setThirdUserUuid(tlm.getUuid());
        user.setThirdUserId(tlm.getUuid());
        super.save(user);
        return user;
    }

}
