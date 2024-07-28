package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.BusMessageDTO;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.constant.enums.SysAnnmentTypeEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.aop.TenantLog;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.service.ISysTenantPackService;
import org.jeecg.modules.system.service.ISysTenantService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.tenant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private ISysBaseAPI sysBaseApi;
    
    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysTenantPackMapper sysTenantPackMapper;
    
    @Autowired
    private SysPackPermissionMapper sysPackPermissionMapper;

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
        query.eq(SysUserTenant::getStatus,CommonConstant.STATUS_1);
        // 查找出已被关联的用户数量
        return userTenantMapper.selectCount(query);
    }

    @Override
    public boolean removeTenantById(String id) {
        // 查找出已被关联的用户数量
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
            //获取被邀请人是否已存在
            SysUserTenant userTenant = userTenantMapper.getUserTenantByTenantId(userId, Integer.valueOf(id));
            if(null == userTenant){
                SysUserTenant relation = new SysUserTenant();
                relation.setUserId(userId);
                relation.setTenantId(Integer.valueOf(id));
                relation.setStatus(CommonConstant.USER_TENANT_NORMAL);
                userTenantMapper.insert(relation);
            }else{
                //update-begin---author:wangshuai ---date:20230711  for：【QQYUN-5723】2、用户已经在租户里了，再次要求提示成功，应该提示用户已经存在------------
                //update-begin---author:wangshuai ---date:20230724  for：【QQYUN-5885】邀请用户加入提示不准确------------
                String tenantErrorInfo = getTenantErrorInfo(userTenant.getStatus());
                String errMsg = "手机号用户:" + userByPhone.getPhone() + " 昵称：" + userByPhone.getRealname() + "，" + tenantErrorInfo;
                //update-end---author:wangshuai ---date:20230724  for：【QQYUN-5885】邀请用户加入提示不准确------------
                throw new JeecgBootException(errMsg);
                //update-end---author:wangshuai ---date:20230711  for：【QQYUN-5723】2、用户已经在租户里了，再次要求提示成功，应该提示用户已经存在------------  
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
        //租户移除用户，直接删除用户租户产品包
        sysTenantPackUserMapper.deletePackUserByTenantId(Integer.valueOf(tenantId),Arrays.asList(userIds.split(SymbolConstant.COMMA)));
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
        return tenantId;
    }

    @Override
    public void saveTenant(SysTenant sysTenant){
        //获取租户id
        sysTenant.setId(this.tenantIdGenerate());
        sysTenant.setHouseNumber(RandomUtil.randomStringUpper(6));
        sysTenant.setDelFlag(CommonConstant.DEL_FLAG_0);
        this.save(sysTenant);
        //update-begin---author:wangshuai ---date:20230710  for：【QQYUN-5723】1、把当前创建人加入到租户关系里面------------
        //当前登录人的id
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        this.saveTenantRelation(sysTenant.getId(),loginUser.getId());
        //update-end---author:wangshuai ---date:20230710  for：【QQYUN-5723】1、把当前创建人加入到租户关系里面------------
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
                    throw new JeecgBootBizTipException("管理员已拒绝您加入租户,请联系租户管理员");
                }else if(CommonConstant.USER_TENANT_QUIT.equals(relation.getStatus())){
                    msg = ",状态:已离职";
                }
                throw new JeecgBootBizTipException("您已是该租户成员"+msg);
            }
            //用户加入门牌号审核中状态
            SysUserTenant tenant = new SysUserTenant();
            tenant.setTenantId(one.getId());
            tenant.setUserId(userId);
            tenant.setStatus(CommonConstant.USER_TENANT_UNDER_REVIEW);
            userTenantMapper.insert(tenant);

            // QQYUN-4526【应用】组织加入通知
            sendMsgForApplyJoinTenant(userId, one);
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
        //update-begin---author:wangshuai ---date:20230710  for：【QQYUN-5723】3、租户彻底删除，用户租户关系表也需要删除------------
        //删除租户下的用户
        userTenantMapper.deleteUserByTenantId(list);
        //update-ennd---author:wangshuai ---date:20230710  for：【QQYUN-5723】3、租户彻底删除，用户租户关系表也需要删除------------

        //update-begin---author:wangshuai ---date:20230710  for：【QQYUN-5723】3、租户彻底删除，用户租户关系表也需要删除------------
        //删除租户下的产品包
        this.deleteTenantPackByTenantId(list);
        //update-ennd---author:wangshuai ---date:20230710  for：【QQYUN-5723】3、租户彻底删除，用户租户关系表也需要删除------------
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
           //update-begin---author:wangshuai ---date:20230424  for：数据库没有租户的时候，如果为空的话会报错sql返回类型不匹配------------
           int maxTenantId = oConvertUtils.getInt(tenantMapper.getMaxTenantId(),0);
           //update-end---author:wangshuai ---date:20230424  for：数据库没有租户的时候，如果为空的话会报错sql返回类型不匹配------------
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
                //update-begin---author:wangshuai ---date:20230426  for：【QQYUN-5270】名下租户全部退出后，再次登录，提示租户全部冻结------------
                //只有拥有者的时候需要去注销租户
                throw new JeecgBootException("cancelTenant");
                //update-end---author:wangshuai ---date:20230426  for：【QQYUN-5270】名下租户全部退出后，再次登录，提示租户全部冻结------------
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
    public void changeOwenUserTenant(String userId, String tId) {
        //查询当前用户是否存在该租户下
        //update-begin---author:wangshuai ---date:20230705  for：租户id应该是传过来的，不应该是当前租户的------------
        int tenantId = oConvertUtils.getInt(tId, 0);
        SysTenant sysTenant = tenantMapper.selectById(tenantId);
        if(null == sysTenant){
            throw new JeecgBootException("退出租户失败，不存在此租户");
        }
        String createBy = sysTenant.getCreateBy();
        //update-end---author:wangshuai ---date:20230705  for：租户id应该是传过来的，不应该是当前租户的------------
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
        //update-begin---author:wangshuai ---date:20230705  for：旧拥有者退出后，需要将就拥有者的用户租户关系改成已离职------------
        //获取原创建人的用户id
        SysUser userByName = userService.getUserByName(createBy);
        LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysUserTenant::getUserId,userByName.getId());
        query.eq(SysUserTenant::getTenantId,tenantId);
        SysUserTenant userTenant = new SysUserTenant();
        userTenant.setStatus(CommonConstant.USER_TENANT_QUIT);
        userTenantMapper.update(userTenant,query);
        //update-end---author:wangshuai ---date:20230705  for：旧拥有者退出后，需要将就拥有者的用户租户关系改成已离职------------
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
    }

    @Override
    public Result<String> invitationUser(String phone, String departId) {
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
        SysUserTenant sysUserTenant = userTenantMapper.getUserTenantByTenantId(userByPhone.getId(), tenantId);
        //用户已存在
        if(null != sysUserTenant){
            result.setSuccess(false);
            String tenantErrorInfo = getTenantErrorInfo(sysUserTenant.getStatus());
            String msg = "手机号用户:" + userByPhone.getPhone() + " 昵称：" + userByPhone.getRealname() + "，" + tenantErrorInfo;
            result.setMessage(msg);
            return result;
        }

        //4.需要用户手动同意加入
        String status = CommonConstant.USER_TENANT_INVITE;

        //5.当前用户不存在租户中,就需要将用户添加到租户中
        SysUserTenant tenant = new SysUserTenant();
        tenant.setTenantId(tenantId);
        tenant.setUserId(userByPhone.getId());
        tenant.setStatus(status);
        userTenantMapper.insert(tenant);
        result.setSuccess(true);
        result.setMessage("邀请成员成功，成员同意后方可加入");

        //update-begin---author:wangshuai ---date:20230329  for：[QQYUN-4671]部门与用户，手机号邀请，没有在当前部门下，目前是在全组织中------------
        //6.保存用户部门关系
        if(oConvertUtils.isNotEmpty(departId)){
            //保存用户部门关系
            this.saveUserDepart(userByPhone.getId(),departId);
        }
        //update-end---author:wangshuai ---date:20230329  for：[QQYUN-4671]部门与用户，手机号邀请，没有在当前部门下，目前是在全组织中------------
        
        //  QQYUN-4527【应用】邀请成员加入组织，发送消息提醒
        sendMsgForInvitation(userByPhone, tenantId, sysUser.getRealname());
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

        // QQYUN-4525【组织管理】管理员 2.管理员权限被移除时，给移除人员发送消息
        sendMsgForDelete(sysTenantPackUser);
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
            // QQYUN-4524【组织关联】管理员 1.管理员权限申请-> 给相关管理员 发送通知消息
            sendMsgForApply(sysTenantPackUser.getUserId(), pack);
        }
    }

    /**
     * 申请管理员权限发消息
     * @param userId
     * @param pack
     */
    private void sendMsgForApply(String userId, SysTenantPack pack){
        // 发消息
        SysUser user = userService.getById(userId);
        Integer tenantId = pack.getTenantId();
        SysTenant sysTenant = this.baseMapper.querySysTenant(tenantId);
        String packCode = pack.getPackCode();

        List<String> packCodeList = Arrays.asList(packCode.split(","));
        List<String> userList = sysTenantPackUserMapper.queryTenantPackUserNameList(tenantId, packCodeList);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAll(false);
        messageDTO.setToUser(String.join(",", userList));
        messageDTO.setFromUser("system");
        String title = user.getRealname()+" 申请加入 "+sysTenant.getName()+" 的"+pack.getPackName()+"的成员。";
        messageDTO.setTitle(title);
        Map<String, Object> data = new HashMap<>();
        messageDTO.setData(data);
        messageDTO.setContent(title);
        messageDTO.setType("system");
        sysBaseApi.sendTemplateMessage(messageDTO);
    }

    /**
     * 移除管理员权限发消息
     * @param sysTenantPackUser
     */
    private void sendMsgForDelete(SysTenantPackUser sysTenantPackUser){
        // 发消息
        SysUser user = userService.getById(sysTenantPackUser.getUserId());
        SysTenant sysTenant = this.baseMapper.querySysTenant(sysTenantPackUser.getTenantId());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAll(false);
        messageDTO.setToUser(user.getUsername());
        String title = "您已被 "+loginUser.getRealname()+" 从 "+sysTenant.getName()+"的"+sysTenantPackUser.getPackName()+"中移除。";
        messageDTO.setTitle(title);
        messageDTO.setFromUser("system");
        Map<String, Object> data = new HashMap<>();
        data.put("realname", loginUser.getRealname());
        data.put("tenantName", sysTenant.getName());
        data.put("packName", sysTenantPackUser.getPackName());
        messageDTO.setData(data);
        messageDTO.setType("system");
        messageDTO.setContent(title);
        sysBaseApi.sendTemplateMessage(messageDTO);
    }

    /**
     * 加入组织申请 发消息
     * @param userId
     * @param sysTenant
     */
    private void sendMsgForApplyJoinTenant(String userId, SysTenant sysTenant){
        // 发消息
        SysUser user = userService.getById(userId);
        // 给超级管理员 和组织管理员发消息
        String codes = "superAdmin,accountAdmin";
        List<String> packCodeList = Arrays.asList(codes.split(","));
        List<String> userList = sysTenantPackUserMapper.queryTenantPackUserNameList(sysTenant.getId(), packCodeList);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAll(false);
        messageDTO.setToUser(String.join(",", userList));
        messageDTO.setFromUser("system");
        String title = user.getRealname()+" 申请加入 "+sysTenant.getName()+"。";
        messageDTO.setTitle(title);
        Map<String, Object> data = new HashMap<>();
        messageDTO.setData(data);
        messageDTO.setType("system");
        messageDTO.setContent(title);
        sysBaseApi.sendTemplateMessage(messageDTO);
    }

    /**
     *  邀请成员 发消息
     * @param user
     * @param tenantId
     * @param realname
     */
    private void sendMsgForInvitation(SysUser user, Integer tenantId, String realname){
        // 发消息
        SysTenant sysTenant = this.baseMapper.querySysTenant(tenantId);
        BusMessageDTO messageDTO = new BusMessageDTO();
        messageDTO.setToAll(false);
        messageDTO.setToUser(user.getUsername());
        messageDTO.setFromUser("system");
        //update-begin---author:wangshuai ---date:20230706  for：【QQYUN-5730】租户邀请加入提示消息应该显示邀请人的名字------------
        String title = realname + " 邀请您加入 "+sysTenant.getName()+"。";
        //update-end---author:wangshuai ---date:20230706  for：【QQYUN-5730】租户邀请加入提示消息应该显示邀请人的名字------------
        messageDTO.setTitle(title);
        Map<String, Object> data = new HashMap<>();
        messageDTO.setData(data);
        messageDTO.setContent(title);
        messageDTO.setType("system");
        //update-begin---author:wangshuai---date:2023-11-24---for:【QQYUN-7168】邀请成员时，会报错，但实际已经邀请成功了---
        messageDTO.setCategory(CommonConstant.MSG_CATEGORY_1);
        //update-end---author:wangshuai---date:2023-11-24---for:【QQYUN-7168】邀请成员时，会报错，但实际已经邀请成功了---
        //update-begin---author:wangshuai ---date:20230721  for：【QQYUN-5726】邀请加入租户加个按钮直接跳转过去------------
        messageDTO.setBusType(SysAnnmentTypeEnum.TENANT_INVITE.getType());
        sysBaseApi.sendBusAnnouncement(messageDTO);
        //update-end---author:wangshuai ---date:20230721  for：【QQYUN-5726】邀请加入租户加个按钮直接跳转过去------------
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
            
            //update-begin---author:wangshuai ---date:20230328  for：[QQYUN-4674]租户管理员同意或拒绝成员没有系统通知------------
            //超级管理员成功加入发送系统消息
            SysTenant sysTenant = tenantMapper.selectById(sysTenantPackUser.getTenantId());
            String content = " 您已成功加入"+sysTenant.getName()+"的超级管理员的成员。";
            SysUser sysUser = userService.getById(sysTenantPackUser.getUserId());
            this.sendMsgForAgreeAndRefuseJoin(sysUser,content);
            //update-end---author:wangshuai ---date:20230328  for：[QQYUN-4674]租户管理员同意或拒绝成员没有系统通知------------
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
            //update-begin---author:wangshuai ---date:20230328  for：[QQYUN-4674]租户管理员同意或拒绝成员没有系统通知------------
            //超级管理员拒绝加入发送系统消息
            SysTenant sysTenant = tenantMapper.selectById(sysTenantPackUser.getTenantId());
            String content = " 管理员已拒绝您加入"+sysTenant.getName()+"的超级管理员的成员请求。";
            SysUser sysUser = userService.getById(sysTenantPackUser.getUserId());
            this.sendMsgForAgreeAndRefuseJoin(sysUser,content);
            //update-end---author:wangshuai ---date:20230328  for：[QQYUN-4674]租户管理员同意或拒绝成员没有系统通知------------
        }
    }

    @Override
    public IPage<TenantPackUser> queryTenantPackUserList(String tenantId, String packId,Integer status, Page<TenantPackUser> page) {
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
            // 职位
            List<UserPosition> userPositions = baseMapper.queryUserPositionList(userIdList);
            // 遍历用户 往用户中添加 部门信息和职位信息
            for (TenantPackUser user : userList) {
                //添加部门
                for (UserDepart dep : depList) {
                    if (user.getId().equals(dep.getUserId())) {
                        user.addDepart(dep.getDepartName());
                    }
                }
                //添加职位
                for (UserPosition userPosition : userPositions) {
                    if (user.getId().equals(userPosition.getUserId())) {
                        user.addPosition(userPosition.getPositionName());
                    }
                }
            }
        }
        return userList;
    }


    /**
     * 保存用户部门关系
     * @param userId
     * @param departId
     */
    private void saveUserDepart(String userId, String departId) {
        //根据用户id和部门id获取数量,用于查看用户是否存在用户部门关系表中
        Long count = sysUserDepartMapper.getCountByDepartIdAndUserId(userId,departId);
        if(count == 0){
            SysUserDepart sysUserDepart = new SysUserDepart(userId,departId);
            sysUserDepartMapper.insert(sysUserDepart);
        }
    }
    
    @Override
    public Long getApplySuperAdminCount() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
        return baseMapper.getApplySuperAdminCount(sysUser.getId(),tenantId);
    }

    /**
     * 同意或拒绝加入超级管理员 发消息
     * @param user
     * @param content
     */
    public void sendMsgForAgreeAndRefuseJoin(SysUser user, String content){
        // 发消息
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setToAll(false);
        messageDTO.setToUser(user.getUsername());
        messageDTO.setFromUser("system");
        messageDTO.setTitle(content);
        Map<String, Object> data = new HashMap<>();
        messageDTO.setData(data);
        messageDTO.setContent(content);
        messageDTO.setType("system");
        sysBaseApi.sendTemplateMessage(messageDTO);
    }

    /**
     * 获取租户错误提示信息
     *
     * @param status
     * @return
     */
    private String getTenantErrorInfo(String status) {
        String content = "已在租户中，无需邀请！";
        if (CommonConstant.USER_TENANT_QUIT.equals(status)) {
            content = "已离职！";
        } else if (CommonConstant.USER_TENANT_UNDER_REVIEW.equals(status)) {
            content = "租户管理员审核中！";
        } else if (CommonConstant.USER_TENANT_REFUSE.equals(status)) {
            content = "租户管理员已拒绝！";
        } else if (CommonConstant.USER_TENANT_INVITE.equals(status)) {
            content = "已被邀请，待用户同意！";
        }
        return content;
    }

    /**
     * 删除租户下的产品包
     *
     * @param tenantIdList
     */
    private void deleteTenantPackByTenantId(List<Integer> tenantIdList) {
        //1.删除产品包下的用户
        sysTenantPackUserMapper.deletePackUserByTenantIds(tenantIdList);
        //2.删除产品包对应的菜单权限
        sysPackPermissionMapper.deletePackPermByTenantIds(tenantIdList);
        //3.删除产品包
        sysTenantPackMapper.deletePackByTenantIds(tenantIdList);
    }

    @Override
    public void deleteUserByPassword(SysUser sysUser, Integer tenantId) {
        //被删除人的用户id
        String userId = sysUser.getId();
        //被删除人的密码
        String password = sysUser.getPassword();
        //当前登录用户
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //step1 判断当前用户是否为当前租户的管理员(只有超级管理员和账号管理员可以删除)
        Long isHaveAdmin = sysTenantPackUserMapper.izHaveBuyAuth(user.getId(), tenantId);
        if(null == isHaveAdmin || 0 == isHaveAdmin){
            throw new JeecgBootException("您不是当前组织的管理员，无法删除用户！");
        }
        //step2 离职状态下，并且无其他组织情况下，可以删除
        SysUserTenant sysUserTenant = userTenantMapper.getUserTenantByTenantId(userId, tenantId);
        if(null == sysUserTenant || !CommonConstant.USER_TENANT_QUIT.equals(sysUserTenant.getStatus())){
            throw new JeecgBootException("用户没有离职，不允许删除！"); 
        }
        List<Integer> tenantIdsByUserId = userTenantMapper.getTenantIdsByUserId(userId);
        if(CollectionUtils.isNotEmpty(tenantIdsByUserId) && tenantIdsByUserId.size()>0){
            throw new JeecgBootException("用户尚有未退出的组织，无法删除！");
        }
        //step3 当天创建的用户和创建人可以删除
        SysUser sysUserData = userService.getById(userId);
        if(!sysUserData.getCreateBy().equals(user.getUsername())){
            throw new JeecgBootException("您不是该用户的创建人，无法删除！");
        }
        Date createTime = sysUserData.getCreateTime();
        boolean sameDay = DateUtils.isSameDay(createTime, new Date());
        if(!sameDay){
            throw new JeecgBootException("用户不是今天创建的，无法删除！"); 
        }
        //step4 验证密码
        String passwordEncode = PasswordUtil.encrypt(sysUserData.getUsername(), password, sysUserData.getSalt());
        if(!passwordEncode.equals(sysUserData.getPassword())){
            throw new JeecgBootException("您输入的密码不正确，无法删除该用户！");
        }
        //step5 逻辑删除用户
        userService.deleteUser(userId);
        //step6 真实删除用户
        userService.removeLogicDeleted(Collections.singletonList(userId));
    }

    @Override
    public List<SysTenant> getTenantListByUserId(String userId) {
        return tenantMapper.getTenantListByUserId(userId);
    }

}
