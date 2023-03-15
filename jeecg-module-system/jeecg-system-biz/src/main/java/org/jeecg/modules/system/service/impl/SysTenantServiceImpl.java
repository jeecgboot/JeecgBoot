package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.aop.TenantLog;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysTenantMapper;
import org.jeecg.modules.system.mapper.SysTenantPackUserMapper;
import org.jeecg.modules.system.mapper.SysUserTenantMapper;
import org.jeecg.modules.system.service.ISysTenantPackService;
import org.jeecg.modules.system.service.ISysTenantService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.tenant.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 租户实现类
 * @author: jeecg-boot
 */
@Service("sysTenantServiceImpl")
@Slf4j
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    ISysUserService userService;
    @Autowired
    private SysUserTenantMapper userTenantMapper;
    @Autowired
    private SysTenantMapper tenantMapper;
    @Autowired
    private ISysTenantPackService sysTenantPackService;

    @Autowired
    private SysTenantPackUserMapper sysTenantPackUserMapper;

    @Override
    public List<SysTenant> queryEffectiveTenant(Collection<Integer> idList) {
        if(oConvertUtils.listIsEmpty(idList)){
            return null;
        }
        
        LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysTenant::getId, idList);
        queryWrapper.eq(SysTenant::getStatus, Integer.valueOf(CommonConstant.STATUS_1));
        //此处查询忽略时间条件
        return super.list(queryWrapper);
    }

    @Override
    public Long countUserLinkTenant(String id) {
        LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysUserTenant::getTenantId,id);
        query.eq(SysUserTenant::getStatus, CommonConstant.STATUS_1);
        // 查找出已被关联的用户数量
        return userTenantMapper.selectCount(query);
    }

    @Override
    public boolean removeTenantById(String id) {
        // 查找出已被关联的用户数量
        Long userCount = this.countUserLinkTenant(id);
        if (userCount > 0) {
            throw new JeecgBootException("该租户已被引用，无法删除！");
        }
        return super.removeById(Integer.parseInt(id));
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public void invitationUserJoin(String ids, String phone) {
        String[] idArray = ids.split(SymbolConstant.COMMA);
        //update-begin---author:wangshuai ---date:20230313  for：【QQYUN-4605】后台的邀请谁加入租户，没办法选不是租户下的用户，通过手机号邀请------------
        SysUser userByPhone = userService.getUserByPhone(phone);
        //说明用户不存在
        if(null == userByPhone){
            throw new JeecgBootException("当前用户不存在，请核对手机号");
        }
        String userId = userByPhone.getId();
        //循环租户id
        for (String id:idArray) {
            //update-begin---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
            LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
            query.eq(SysUserTenant::getTenantId,id);
            query.eq(SysUserTenant::getUserId,userId);
            long count = userTenantMapper.selectCount(query);
            if(count == 0){
                SysUserTenant relation = new SysUserTenant();
                relation.setUserId(userId);
                relation.setTenantId(Integer.valueOf(id));
                relation.setStatus(CommonConstant.USER_TENANT_NORMAL);
                userTenantMapper.insert(relation);
            }
            //update-end---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
        //update-end---author:wangshuai ---date:20230313  for：【QQYUN-4605】后台的邀请谁加入租户，没办法选不是租户下的用户，通过手机号邀请------------
        }
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public void leaveTenant(String userIds, String tenantId) {
        String[] userIdArray = userIds.split(SymbolConstant.COMMA);
        for (String userId:userIdArray) {
            //update-begin---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
            LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
            query.eq(SysUserTenant::getTenantId,tenantId);
            query.eq(SysUserTenant::getUserId,userId);
            userTenantMapper.delete(query);
            //update-end---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
        }
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public Integer saveTenantJoinUser(SysTenant sysTenant, String userId) {
        //添加租户
        this.saveTenant(sysTenant);
        
        // 添加租户产品包
        Integer tenantId = sysTenant.getId();
        sysTenantPackService.addDefaultTenantPack(tenantId);
        
        //添加租户到关系表
        return this.saveTenantRelation(sysTenant.getId(), userId);
    }

    @Override
    public void saveTenant(SysTenant sysTenant){
        //获取租户id
        sysTenant.setId(this.tenantIdGenerate());
        sysTenant.setHouseNumber(RandomUtil.randomStringUpper(6));
        this.save(sysTenant);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public Integer joinTenantByHouseNumber(SysTenant sysTenant, String userId) {
        LambdaQueryWrapper<SysTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysTenant::getHouseNumber,sysTenant.getHouseNumber());
        SysTenant one = this.getOne(query);
        //需要返回租户id(用于前台更新缓存),返回0则代表当前租户门牌号不存在
        if(null == one){
            return 0;
        }else{
            LambdaQueryWrapper<SysUserTenant> relationQuery = new LambdaQueryWrapper<>();
            relationQuery.eq(SysUserTenant::getTenantId,one.getId());
            relationQuery.eq(SysUserTenant::getUserId,userId);
            SysUserTenant relation = userTenantMapper.selectOne(relationQuery);
            if(relation != null){
                String msg = "";
                if(CommonConstant.USER_TENANT_UNDER_REVIEW.equals(relation.getStatus())){
                    msg = ",状态:审核中";
                }else if(CommonConstant.USER_TENANT_REFUSE.equals(relation.getStatus())){
                    throw new JeecgBootException("管理员已拒绝您加入租户,请联系租户管理员");
                }else if(CommonConstant.USER_TENANT_QUIT.equals(relation.getStatus())){
                    msg = ",状态:已离职";
                }
                throw new JeecgBootException("您已是该租户成员"+msg);
            }
            //用户加入门牌号审核中状态
            SysUserTenant tenant = new SysUserTenant();
            tenant.setTenantId(one.getId());
            tenant.setUserId(userId);
            tenant.setStatus(CommonConstant.USER_TENANT_UNDER_REVIEW);
            userTenantMapper.insert(tenant);
            return tenant.getTenantId();
        }
    }

    @Override
    public Integer countCreateTenantNum(String userId) {
        return this.userTenantMapper.countCreateTenantNum(userId);
    }

    @Override
    public IPage<SysTenant> getRecycleBinPageList(Page<SysTenant> page, SysTenant sysTenant) {
        return page.setRecords(tenantMapper.getRecycleBinPageList(page,sysTenant));
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public void deleteTenantLogic(String ids) {
        String[] idArray = ids.split(SymbolConstant.COMMA);
        List<Integer> list = new ArrayList<>();
        //转成int类型
        for (String id:idArray) {
            list.add(Integer.valueOf(id));
        }
        //删除租户
        tenantMapper.deleteByTenantId(list);
    }

    @Override
    public void revertTenantLogic(String ids) {
        String[] idArray = ids.split(SymbolConstant.COMMA);
        List<Integer> list = new ArrayList<>();
        //转成int类型
        for (String id:idArray) {
            list.add(Integer.valueOf(id));
        }
        //还原租户
        tenantMapper.revertTenantLogic(list);
    }

    /**
     * 添加租户到关系表
     * @param tenantId
     * @param userId
     */
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public Integer saveTenantRelation(Integer tenantId,String userId) {
        SysUserTenant relation = new SysUserTenant();
        relation.setTenantId(tenantId);
        relation.setUserId(userId);
        relation.setStatus(CommonConstant.USER_TENANT_NORMAL);
        userTenantMapper.insert(relation);
        return relation.getTenantId();
    }

    /**
     * 获取租户id
     * @return
     */
   public int tenantIdGenerate(){
       synchronized (this){
           //获取最大值id
           int maxTenantId = tenantMapper.getMaxTenantId();
           if(maxTenantId >= 1000){
               return maxTenantId + 1;
           }else{
               return 1000;
           }
       }
   }


    @Override
    public void exitUserTenant(String userId, String username, String tenantId) {
        int tId = Integer.parseInt(tenantId);
        //获取所有租户信息
        List<String> userIdsByTenantId = userTenantMapper.getUserIdsByTenantId(tId);
        //查询当前租户是否为拥有者
        SysTenant sysTenant = tenantMapper.selectById(tId);
        //如果是拥有着
        if (username.equals(sysTenant.getCreateBy())) {
            //判断当前租户信息位数
            if (null != userIdsByTenantId && userIdsByTenantId.size() > 1) {
                //需要指配拥有者
                throw new JeecgBootException("assignedOwen");
            } else if (null != userIdsByTenantId && userIdsByTenantId.size() == 1) {
                //最后一位直接退出租户即可
                //逻辑删除,不删除关系表租户一个户最后一位，保证回收站取回还有管理员
                tenantMapper.deleteById(tenantId);
                this.leveUserProcess(userId, tenantId);
            } else {
                throw new JeecgBootException("退出租户失败，租户信息已不存在");
            }
        } else {
            //不是拥有者直接删除
            this.leaveTenant(userId, tenantId);
            this.leveUserProcess(userId, tenantId);
        }
    }

    @Override
    public void changeOwenUserTenant(String userId) {
        //查询当前用户是否存在该租户下
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
        Integer count = userTenantMapper.userTenantIzExist(userId, tenantId);
        if (count == 0) {
            throw new JeecgBootException("退出租户失败，此租户下没有该用户");
        }
        //获取用户信息
        SysUser user = userService.getById(userId);
        //变更拥有者
        SysTenant tenant = new SysTenant();
        tenant.setCreateBy(user.getUsername());
        tenant.setId(tenantId);
        tenantMapper.updateById(tenant);
        //删除当前登录用户的租户信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        this.leaveTenant(loginUser.getId(), String.valueOf(tenantId));
        //离职流程
        this.leveUserProcess(userId, String.valueOf(tenantId));
    }

    /**
     * 触发离职流程
     *
     * @param userId
     * @param tenantId
     * @param tenantId
     */
    private void leveUserProcess(String userId, String tenantId) {
        LoginUser userInfo = new LoginUser();
        SysUser user = userService.getById(userId);
        //触发入职流程
        BeanUtils.copyProperties(user, userInfo);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }

    @Override
    public Result<String> invitationUser(String phone) {
        Result<String> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //1、查询用户信息,判断用户是否存在
        SysUser userByPhone = userService.getUserByPhone(phone);
        if(null == userByPhone){
            result.setSuccess(false);
            result.setMessage("用户不存在");
            return result;
        }
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);

        //2.判断当前邀请人是否存在租户列表中
        Integer userCount = userTenantMapper.userTenantIzExist(sysUser.getId(), tenantId);
        if(userCount == 0){
            result.setSuccess(false);
            result.setMessage("当前管理员没有邀请权限");
            return result;
        }

        //3.插入到租户信息，已存在的不予许插入
        //获取被邀请人是否已存在
        Integer count = userTenantMapper.userTenantIzExist(userByPhone.getId(), tenantId);
        //用户已存在
        if(count>0){
            result.setSuccess(false);
            result.setMessage("用户名(" + userByPhone.getRealname() + ")" + "手机号(" + userByPhone.getPhone() + ")" + "已存在该租户中,不能重复邀请");
            return result;
        }

        //4.查询当前租户是否是拥有者,拥有者直接可以邀请，否则需要审核
        LambdaQueryWrapper<SysTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysTenant::getCreateBy, sysUser.getUsername());
        query.eq(SysTenant::getId, tenantId);
        Long tenantCount = tenantMapper.selectCount(query);
        //默认审核中
        String status = CommonConstant.USER_TENANT_UNDER_REVIEW;
        if (tenantCount > 0) {
            //是拥有者直接通过审核
            status = CommonConstant.USER_TENANT_NORMAL;
        }

        //5.当前用户不存在租户中,就需要将用户添加到租户中
        SysUserTenant tenant = new SysUserTenant();
        tenant.setTenantId(tenantId);
        tenant.setUserId(userByPhone.getId());
        tenant.setStatus(status);
        userTenantMapper.insert(tenant);
        this.addUserProcess(userByPhone, sysUser, tenantId);
        result.setSuccess(true);
        result.setMessage("邀请成员成功");
        return result;
    }

    @Override
    public TenantDepartAuthInfo getTenantDepartAuthInfo(Integer tenantId) {
        SysTenant sysTenant = this.getById(tenantId);
        if(sysTenant==null) {
            return null;
        }

        TenantDepartAuthInfo info = new TenantDepartAuthInfo();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        boolean superAdmin = false;
        // 查询pack表
        List<String> packCodeList = baseMapper.queryUserPackCode(tenantId, userId);
        if(packCodeList==null || packCodeList.size()==0){
            //如果没有数据 判断租户创建人是不是当前用户
            if(sysTenant.getCreateBy().equals(sysUser.getUsername())){
                sysTenantPackService.addDefaultTenantPack(tenantId);
                superAdmin = true;
            }else{
                superAdmin = false;
            }
        }
        if(superAdmin == false){
            List<TenantPackUserCount> packCountList = baseMapper.queryTenantPackUserCount(tenantId);
            info.setPackCountList(packCountList);
        }
        info.setSysTenant(sysTenant);
        info.setSuperAdmin(superAdmin);
        info.setPackCodes(packCodeList);
        return info;
    }

    @Override
    public List<TenantPackUserCount> queryTenantPackUserCount(Integer tenantId) {
        return baseMapper.queryTenantPackUserCount(tenantId);
    }

    @Override
    public TenantPackModel queryTenantPack(TenantPackModel model) {
        Integer tenantId = model.getTenantId();
        String packCode = model.getPackCode();

        SysTenantPack sysTenantPack = sysTenantPackService.getSysTenantPack(tenantId, packCode);
        if(sysTenantPack!=null){
            TenantPackModel tenantPackModel = new TenantPackModel();
            tenantPackModel.setPackName(sysTenantPack.getPackName());
            tenantPackModel.setPackId(sysTenantPack.getId());
            // 查询用户
            List<TenantPackUser> userList = getTenantPackUserList(tenantId, sysTenantPack.getId(), 1);
            tenantPackModel.setUserList(userList);
            return tenantPackModel;
        }
        return null;
    }

    @Override
    public void addBatchTenantPackUser(SysTenantPackUser sysTenantPackUser) {
        String userIds = sysTenantPackUser.getUserId();
        if(oConvertUtils.isNotEmpty(userIds)){
            ISysTenantService currentService = SpringContextUtils.getApplicationContext().getBean(ISysTenantService.class);
            String realNames = sysTenantPackUser.getRealname();
            String[] userIdArray = userIds.split(",");
            String[] realNameArray = realNames.split(",");
            for(int i=0;i<userIdArray.length;i++){
                String userId = userIdArray[i];
                String realName = realNameArray[i];
                SysTenantPackUser entity = new SysTenantPackUser(sysTenantPackUser, userId, realName);
                LambdaQueryWrapper<SysTenantPackUser> query = new LambdaQueryWrapper<SysTenantPackUser>()
                        .eq(SysTenantPackUser::getTenantId, entity.getTenantId())
                        .eq(SysTenantPackUser::getPackId, entity.getPackId())
                        .eq(SysTenantPackUser::getUserId, entity.getUserId());
                SysTenantPackUser packUser = sysTenantPackUserMapper.selectOne(query);
                if(packUser==null || packUser.getId()==null){
                    currentService.addTenantPackUser(entity);
                }else{
                    if(packUser.getStatus()==0){
                        packUser.setPackName(entity.getPackName());
                        packUser.setRealname(realName);
                        currentService.addTenantPackUser(packUser);
                    }
                }
            }
        }
    }

    @TenantLog(2)
    @Override
    public void addTenantPackUser(SysTenantPackUser sysTenantPackUser) {
        if(sysTenantPackUser.getId()==null){
            sysTenantPackUserMapper.insert(sysTenantPackUser);
        }else{
            sysTenantPackUser.setStatus(1);
            sysTenantPackUserMapper.updateById(sysTenantPackUser);
        }
    }

    @TenantLog(4)
    @Override
    public void deleteTenantPackUser(SysTenantPackUser sysTenantPackUser) {
        LambdaQueryWrapper<SysTenantPackUser> query = new LambdaQueryWrapper<SysTenantPackUser>()
                .eq(SysTenantPackUser::getUserId, sysTenantPackUser.getUserId())
                .eq(SysTenantPackUser::getPackId, sysTenantPackUser.getPackId());
        sysTenantPackUserMapper.delete(query);

    }

    @Override
    public List<TenantPackUser> getTenantPackApplyUsers(Integer tenantId) {
        return getTenantPackUserList(tenantId, null, 0);
    }

    /**
     * 获取租户下 某个产品包的用户
     * 或者是 租户下产品包的申请用户
     * @param tenantId
     * @param packId
     * @param packUserStatus
     * @return
     */
    private List<TenantPackUser> getTenantPackUserList(Integer tenantId, String packId, Integer packUserStatus){
        // 查询用户
        List<TenantPackUser> userList = baseMapper.queryPackUserList(tenantId, packId, packUserStatus);
        if(userList!=null && userList.size()>0){
            List<String> userIdList = userList.stream().map(i->i.getId()).collect(Collectors.toList());
            // 部门
            List<UserDepart> depList = baseMapper.queryUserDepartList(userIdList);
            // 职位 TODO
            // 遍历用户 往用户中添加 部门信息和职位信息
            for(TenantPackUser user: userList){
                for(UserDepart dep: depList){
                    if(user.getId().equals(dep.getUserId())){
                        user.addDepart(dep.getDepartName());
                    }
                }
            }
        }
        return userList;
    }

    @Override
    public void doApplyTenantPackUser(SysTenantPackUser sysTenantPackUser) {
        LambdaQueryWrapper<SysTenantPack> query1 = new LambdaQueryWrapper<SysTenantPack>()
                .eq(SysTenantPack::getTenantId, sysTenantPackUser.getTenantId())
                .eq(SysTenantPack::getPackCode, sysTenantPackUser.getPackCode());
        SysTenantPack pack = sysTenantPackService.getOne(query1);
        if(pack!=null){
            sysTenantPackUser.setStatus(0);
            sysTenantPackUser.setPackId(pack.getId());
            LambdaQueryWrapper<SysTenantPackUser> query = new LambdaQueryWrapper<SysTenantPackUser>()
                    .eq(SysTenantPackUser::getTenantId, sysTenantPackUser.getTenantId())
                    .eq(SysTenantPackUser::getPackId, sysTenantPackUser.getPackId())
                    .eq(SysTenantPackUser::getUserId, sysTenantPackUser.getUserId());
            Long count = sysTenantPackUserMapper.selectCount(query);
            if(count==null || count==0){
                sysTenantPackUserMapper.insert(sysTenantPackUser);
            }
        }
    }

    @Override
    public void passApply(SysTenantPackUser sysTenantPackUser) {
        LambdaQueryWrapper<SysTenantPackUser> query = new LambdaQueryWrapper<SysTenantPackUser>()
                .eq(SysTenantPackUser::getTenantId, sysTenantPackUser.getTenantId())
                .eq(SysTenantPackUser::getPackId, sysTenantPackUser.getPackId())
                .eq(SysTenantPackUser::getUserId, sysTenantPackUser.getUserId());
        SysTenantPackUser packUser = sysTenantPackUserMapper.selectOne(query);
        if(packUser!=null && packUser.getId()!=null && packUser.getStatus()==0){
            ISysTenantService currentService = SpringContextUtils.getApplicationContext().getBean(ISysTenantService.class);
            packUser.setPackName(sysTenantPackUser.getPackName());
            packUser.setRealname(sysTenantPackUser.getRealname());
            currentService.addTenantPackUser(packUser);
        }
    }

    @Override
    public void deleteApply(SysTenantPackUser sysTenantPackUser) {
        LambdaQueryWrapper<SysTenantPackUser> query = new LambdaQueryWrapper<SysTenantPackUser>()
                .eq(SysTenantPackUser::getTenantId, sysTenantPackUser.getTenantId())
                .eq(SysTenantPackUser::getPackId, sysTenantPackUser.getPackId())
                .eq(SysTenantPackUser::getUserId, sysTenantPackUser.getUserId());
        SysTenantPackUser packUser = sysTenantPackUserMapper.selectOne(query);
        if(packUser!=null && packUser.getId()!=null && packUser.getStatus()==0){
            sysTenantPackUserMapper.deleteById(packUser.getId());
        }
    }

    @Override
    public IPage<TenantPackUser> queryTenantPackUserList(String tenantId, String packId, Integer status, Page<TenantPackUser> page) {
        // 查询用户
        List<TenantPackUser> userList = baseMapper.queryTenantPackUserList(page,tenantId, packId,status);
        // 获取产品包下用户部门和职位
        userList = getPackUserPositionAndDepart(userList);
        return page.setRecords(userList);
    }

    /**
     * 获取用户职位和部门
     * @param userList
     * @return
     */
    private List<TenantPackUser> getPackUserPositionAndDepart(List<TenantPackUser> userList) {
        if(userList!=null && userList.size()>0){
            List<String> userIdList = userList.stream().map(i->i.getId()).collect(Collectors.toList());
            // 部门
            List<UserDepart> depList = baseMapper.queryUserDepartList(userIdList);
//            // 职位
//            List<UserPosition> userPositions = baseMapper.queryUserPositionList(userIdList);
            // 遍历用户 往用户中添加 部门信息和职位信息
            for (TenantPackUser user : userList) {
                //添加部门
                for (UserDepart dep : depList) {
                    if (user.getId().equals(dep.getUserId())) {
                        user.addDepart(dep.getDepartName());
                    }
                }
//                //添加职位
//                for (UserPosition userPosition : userPositions) {
//                    if (user.getId().equals(userPosition.getUserId())) {
//                        user.addPosition(userPosition.getPositionName());
//                    }
//                }
            }
        }
        return userList;
    }

    /**
     * 触发入职流程
     *
     * @param user
     * @param loginUser
     * @param tenantId
     */
    private void addUserProcess(SysUser user, LoginUser loginUser, Integer tenantId) {
        LoginUser userInfo = new LoginUser();
        //触发入职流程
        BeanUtils.copyProperties(user, userInfo);
    }
}
