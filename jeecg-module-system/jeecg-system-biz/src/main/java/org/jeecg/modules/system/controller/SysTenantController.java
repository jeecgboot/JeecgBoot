package org.jeecg.modules.system.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.service.ISysTenantPackService;
import org.jeecg.modules.system.service.ISysTenantService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.service.ISysUserTenantService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.vo.SysUserTenantVo;
import org.jeecg.modules.system.vo.tenant.TenantDepartAuthInfo;
import org.jeecg.modules.system.vo.tenant.TenantPackModel;
import org.jeecg.modules.system.vo.tenant.TenantPackUser;
import org.jeecg.modules.system.vo.tenant.TenantPackUserCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 租户配置信息
 * @author: jeecg-boot
 */
@Slf4j
@RestController
@RequestMapping("/sys/tenant")
public class SysTenantController {

    @Autowired
    private ISysTenantService sysTenantService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserTenantService relationService;
    
    @Autowired
    private ISysTenantPackService sysTenantPackService;
    
    @Autowired
    private BaseCommonService baseCommonService;

    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 获取列表数据
     * @param sysTenant
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequiresPermissions("system:tenant:list")
    @PermissionData(pageComponent = "system/TenantList")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysTenant>> queryPageList(SysTenant sysTenant,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysTenant>> result = new Result<IPage<SysTenant>>();
        //---author:zhangyafei---date:20210916-----for: 租户管理添加日期范围查询---
        Date beginDate=null;
        Date endDate=null;
        if(oConvertUtils.isNotEmpty(sysTenant)) {
            beginDate=sysTenant.getBeginDate();
            endDate=sysTenant.getEndDate();
            sysTenant.setBeginDate(null);
            sysTenant.setEndDate(null);
        }
        //---author:zhangyafei---date:20210916-----for: 租户管理添加日期范围查询---
        QueryWrapper<SysTenant> queryWrapper = QueryGenerator.initQueryWrapper(sysTenant, req.getParameterMap());
        //---author:zhangyafei---date:20210916-----for: 租户管理添加日期范围查询---
        if(oConvertUtils.isNotEmpty(sysTenant)){
            queryWrapper.ge(oConvertUtils.isNotEmpty(beginDate),"begin_date",beginDate);
            queryWrapper.le(oConvertUtils.isNotEmpty(endDate),"end_date",endDate);
        }
        //---author:zhangyafei---date:20210916-----for: 租户管理添加日期范围查询---
		Page<SysTenant> page = new Page<SysTenant>(pageNo, pageSize);
		IPage<SysTenant> pageList = sysTenantService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

    /**
     * 获取租户删除的列表
     * @param sysTenant
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping("/recycleBinPageList")
    @RequiresPermissions("system:tenant:recycleBinPageList")
    public Result<IPage<SysTenant>> recycleBinPageList(SysTenant sysTenant,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req){
        Result<IPage<SysTenant>> result = new Result<IPage<SysTenant>>();
        Page<SysTenant> page = new Page<SysTenant>(pageNo, pageSize);
        IPage<SysTenant> pageList = sysTenantService.getRecycleBinPageList(page, sysTenant);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
    
    /**
     *   添加
     * @param
     * @return
     */
    @RequiresPermissions("system:tenant:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysTenant> add(@RequestBody SysTenant sysTenant) {
        Result<SysTenant> result = new Result();
        if(sysTenantService.getById(sysTenant.getId())!=null){
            return result.error500("该编号已存在!");
        }
        try {
            sysTenantService.saveTenant(sysTenant);
            //添加默认产品包
            sysTenantPackService.addTenantDefaultPack(sysTenant.getId());
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     *  编辑
     * @param
     * @return
     */
    @RequiresPermissions("system:tenant:edit")
    @RequestMapping(value = "/edit", method ={RequestMethod.PUT, RequestMethod.POST})
    public Result<SysTenant> edit(@RequestBody SysTenant tenant) {
        Result<SysTenant> result = new Result();
        SysTenant sysTenant = sysTenantService.getById(tenant.getId());
        if(sysTenant==null) {
           return result.error500("未找到对应实体");
        }
        if(oConvertUtils.isEmpty(sysTenant.getHouseNumber())){
            tenant.setHouseNumber(RandomUtil.randomStringUpper(6));
        }
        boolean ok = sysTenantService.updateById(tenant);
        if(ok) {
            result.success("修改成功!");
        }
        return result;
    }

    /**
     *   通过id删除
     * @param id
     * @return
     */
    @RequiresPermissions("system:tenant:delete")
    @RequestMapping(value = "/delete", method ={RequestMethod.DELETE, RequestMethod.POST})
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        //------------------------------------------------------------------
        //如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            //获取当前用户
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            SysTenant sysTenant = sysTenantService.getById(id);

            String username = "admin";
            String createdBy = sysUser.getUsername();
            if (!sysTenant.getCreateBy().equals(createdBy) && !username.equals(createdBy)) {
                baseCommonService.addLog("未经授权，不能删除非自己创建的租户，租户ID：" + id + "，操作人：" + sysUser.getUsername(), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
                return Result.error("删除租户失败,当前操作人不是租户的创建人！");
            }
        }
        //------------------------------------------------------------------
                
        sysTenantService.removeTenantById(id);
        return Result.ok("删除成功");
    }

    /**
     *  批量删除
     * @param ids
     * @return
     */
    @RequiresPermissions("system:tenant:deleteBatch")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        Result<?> result = new Result<>();
        if(oConvertUtils.isEmpty(ids)) {
            result.error500("未选中租户！");
        }else {
            String[] ls = ids.split(",");
            // 过滤掉已被引用的租户
            List<Integer> idList = new ArrayList<>();
            for (String id : ls) {
                //------------------------------------------------------------------
                //如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
                if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
                    //获取当前用户
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    SysTenant sysTenant = sysTenantService.getById(id);

                    String username = "admin";
                    String createdBy = sysUser.getUsername();
                    if (!sysTenant.getCreateBy().equals(createdBy) && !username.equals(createdBy)) {
                        baseCommonService.addLog("未经授权，不能删除非自己创建的租户，租户ID：" + id + "，操作人：" + sysUser.getUsername(), CommonConstant.LOG_TYPE_2, CommonConstant.OPERATE_TYPE_3);
                        return Result.error("删除租户失败,当前操作人不是租户的创建人！");
                    }
                }
                //------------------------------------------------------------------
                
                idList.add(Integer.parseInt(id));
            }
            //update-begin---author:wangshuai ---date:20230710  for：【QQYUN-5723】3、租户删除直接删除，不删除中间表------------
            sysTenantService.removeByIds(idList);
            result.success("删除成功！");
            //update-end---author:wangshuai ---date:20220523  for：【QQYUN-5723】3、租户删除直接删除，不删除中间表------------
        }
        return result;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysTenant> queryById(@RequestParam(name="id",required=true) String id) {
        Result<SysTenant> result = new Result<SysTenant>();
        if(oConvertUtils.isEmpty(id)){
            result.error500("参数为空！");
        }
        //------------------------------------------------------------------------------------------------
        //获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】, admin给特权可以管理所有租户
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL && !"admin".equals(sysUser.getUsername())){
            Integer loginSessionTenant = oConvertUtils.getInt(TenantContext.getTenant());
            if(loginSessionTenant!=null && !loginSessionTenant.equals(Integer.valueOf(id))){
                result.error500("无权限访问他人租户！");
                return result;
            }
        }
        //------------------------------------------------------------------------------------------------
        SysTenant sysTenant = sysTenantService.getById(id);
        if(sysTenant==null) {
            result.error500("未找到对应实体");
        }else {
            result.setResult(sysTenant);
            result.setSuccess(true);
        }
        return result;
    }


    /**
     * 查询有效的 租户数据
     * @return
     */
    @RequiresPermissions("system:tenant:queryList")
    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
    public Result<List<SysTenant>> queryList(@RequestParam(name="ids",required=false) String ids) {
        Result<List<SysTenant>> result = new Result<List<SysTenant>>();
        LambdaQueryWrapper<SysTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysTenant::getStatus, 1);
        if(oConvertUtils.isNotEmpty(ids)){
            query.in(SysTenant::getId, ids.split(","));
        }
        //此处查询忽略时间条件
        List<SysTenant> ls = sysTenantService.list(query);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }

    /**
     * 产品包分页列表查询
     *
     * @param sysTenantPack
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/packList")
    @RequiresPermissions("system:tenant:packList")
    public Result<IPage<SysTenantPack>> queryPackPageList(SysTenantPack sysTenantPack,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        QueryWrapper<SysTenantPack> queryWrapper = QueryGenerator.initQueryWrapper(sysTenantPack, req.getParameterMap());
        Page<SysTenantPack> page = new Page<SysTenantPack>(pageNo, pageSize);
        IPage<SysTenantPack> pageList = sysTenantPackService.page(page, queryWrapper);
        List<SysTenantPack> records = pageList.getRecords();
        if (null != records && records.size() > 0) {
            pageList.setRecords(sysTenantPackService.setPermissions(records));
        }
        return Result.OK(pageList);
    }

    /**
     * 创建租户产品包
     *
     * @param sysTenantPack
     * @return
     */
    @PostMapping(value = "/addPackPermission")
    @RequiresPermissions("system:tenant:add:pack")
    public Result<String> addPackPermission(@RequestBody SysTenantPack sysTenantPack) {
        sysTenantPackService.addPackPermission(sysTenantPack);
        return Result.ok("创建租户产品包成功");
    }

    /**
     * 创建租户产品包
     *
     * @param sysTenantPack
     * @return
     */
    @PutMapping(value = "/editPackPermission")
    @RequiresPermissions("system:tenant:edit:pack")
    public Result<String> editPackPermission(@RequestBody SysTenantPack sysTenantPack) {
        sysTenantPackService.editPackPermission(sysTenantPack);
        return Result.ok("修改租户产品包成功");
    }

    /**
     * 批量删除用户菜单
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteTenantPack")
    @RequiresPermissions("system:tenant:delete:pack")
    public Result<String> deleteTenantPack(@RequestParam(value = "ids") String ids) {
        sysTenantPackService.deleteTenantPack(ids);
        return Result.ok("删除租户产品包成功");
    }
    


    //===========【低代码应用，前端专用接口 —— 加入限制只能维护和查看自己拥有的租户】==========================================================
    /**
     *  查询当前用户的所有有效租户【低代码应用专用接口】
     * @return
     */
    @RequestMapping(value = "/getCurrentUserTenant", method = RequestMethod.GET)
    public Result<Map<String,Object>> getCurrentUserTenant() {
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //update-begin---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
            List<Integer> tenantIdList = relationService.getTenantIdsByUserId(sysUser.getId());
            Map<String,Object> map = new HashMap(5);
            if (null!=tenantIdList && tenantIdList.size()>0) {
            //update-end---author:wangshuai ---date:20221223  for：[QQYUN-3371]租户逻辑改造，改成关系表------------
                // 该方法仅查询有效的租户，如果返回0个就说明所有的租户均无效。
                List<SysTenant> tenantList = sysTenantService.queryEffectiveTenant(tenantIdList);
                map.put("list", tenantList);
            }
            result.setSuccess(true);
            result.setResult(map);
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("查询失败！");
        }
        return result;
    }

    /**
     * 邀请用户【低代码应用专用接口】
     * @param ids
     * @param phone
     * @return
     */
    @PutMapping("/invitationUserJoin")
    @RequiresPermissions("system:tenant:invitation:user")
    public Result<String> invitationUserJoin(@RequestParam("ids") String ids,@RequestParam("phone") String phone){
        sysTenantService.invitationUserJoin(ids,phone);
        return Result.ok("邀请用户成功");
    }

    /**
     * 获取用户列表数据【低代码应用专用接口】
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/getTenantUserList", method = RequestMethod.GET)
    @RequiresPermissions("system:tenant:user:list")
    public Result<IPage<SysUser>> getTenantUserList(SysUser user,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                    @RequestParam(name="userTenantId") String userTenantId,
                                                    HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<>();
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        Page<SysUser> pageList = relationService.getPageUserList(page,Integer.valueOf(userTenantId),user);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 请离用户租户【低代码应用专用接口】
     * @param userIds
     * @param tenantId
     * @return
     */
    @PutMapping("/leaveTenant")
    @RequiresPermissions("system:tenant:leave")
    public Result<String> leaveTenant(@RequestParam("userIds") String userIds,
                                      @RequestParam("tenantId") String tenantId){
        Result<String> result = new Result<>();
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL && !"admin".equals(sysUser.getUsername())){
            Integer loginSessionTenant = oConvertUtils.getInt(TenantContext.getTenant());
            if(loginSessionTenant!=null && !loginSessionTenant.equals(Integer.valueOf(tenantId))){
                result.error500("无权限访问他人租户！");
                return result;
            }
        }
        sysTenantService.leaveTenant(userIds,tenantId);
        return Result.ok("请离成功");
    }

    /**
     *  编辑（只允许修改自己拥有的租户）【低代码应用专用接口】
     * @param
     * @return
     */
    @RequestMapping(value = "/editOwnTenant", method ={RequestMethod.PUT, RequestMethod.POST})
    public Result<SysTenant> editOwnTenant(@RequestBody SysTenant tenant,HttpServletRequest req) {
        Result<SysTenant> result = new Result();
        String tenantId = TokenUtils.getTenantIdByRequest(req);
        if(!tenantId.equals(tenant.getId().toString())){
            return result.error500("无权修改他人租户！");
        }

        SysTenant sysTenant = sysTenantService.getById(tenant.getId());
        if(sysTenant==null) {
            return result.error500("未找到对应实体");
        }
        if(oConvertUtils.isEmpty(sysTenant.getHouseNumber())){
            tenant.setHouseNumber(RandomUtil.randomStringUpper(6));
        }
        boolean ok = sysTenantService.updateById(tenant);
        if(ok) {
            result.success("修改成功!");
        }
        return result;
    }
    
    /**
     * 创建租户并且将用户保存到中间表【低代码应用专用接口】
     * @param sysTenant
     */
    @PostMapping("/saveTenantJoinUser")
    public Result<Integer> saveTenantJoinUser(@RequestBody SysTenant sysTenant){
        Result<Integer> result = new Result<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Integer tenantId = sysTenantService.saveTenantJoinUser(sysTenant, sysUser.getId());
        result.setSuccess(true);
        result.setMessage("创建成功");
        result.setResult(tenantId);
        return result;
    }

    /**
     * 加入租户通过门牌号【低代码应用专用接口】
     * @param sysTenant
     */
    @PostMapping("/joinTenantByHouseNumber")
    public Result<Integer> joinTenantByHouseNumber(@RequestBody SysTenant sysTenant){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Integer tenantId = sysTenantService.joinTenantByHouseNumber(sysTenant, sysUser.getId());
        Result<Integer> result = new Result<>();
        if(tenantId != 0){
            result.setMessage("申请加入组织成功");
            result.setSuccess(true);
            result.setResult(tenantId);
            return result;
        }else{
            result.setMessage("该门牌号不存在");
            result.setSuccess(false);
            return result;
        }
    }
    
    //update-begin---author:wangshuai ---date:20230107  for：[QQYUN-3725]申请加入租户，审核中状态增加接口------------
    /**
     * 分页获取租户用户数据(vue3用户租户页面)【低代码应用专用接口】
     *
     * @param pageNo
     * @param pageSize
     * @param userTenantStatus
     * @param type
     * @param req
     * @return
     */
    @GetMapping("/getUserTenantPageList")
    //@RequiresPermissions("system:tenant:tenantPageList")
    public Result<IPage<SysUserTenantVo>> getUserTenantPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                @RequestParam(name = "userTenantStatus") String userTenantStatus,
                                                                @RequestParam(name = "type", required = false) String type,
                                                                SysUser user,
                                                                HttpServletRequest req) {
        Page<SysUserTenantVo> page = new Page<SysUserTenantVo>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String tenantId = oConvertUtils.getString(TenantContext.getTenant(), "0");
        IPage<SysUserTenantVo> list = relationService.getUserTenantPageList(page, Arrays.asList(userTenantStatus.split(SymbolConstant.COMMA)), user, Integer.valueOf(tenantId));
        return Result.ok(list);
    }

    /**
     * 通过用户id获取租户列表【低代码应用专用接口】
     *
     * @param userTenantStatus 关系表的状态
     * @return
     */
    @GetMapping("/getTenantListByUserId")
    //@RequiresPermissions("system:tenant:getTenantListByUserId")
    public Result<List<SysUserTenantVo>> getTenantListByUserId(@RequestParam(name = "userTenantStatus", required = false) String userTenantStatus) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> list = null;
        if (oConvertUtils.isNotEmpty(userTenantStatus)) {
            list = Arrays.asList(userTenantStatus.split(SymbolConstant.COMMA));
        }
        //租户状态，用户id,租户用户关系状态
        List<SysUserTenantVo> sysTenant = relationService.getTenantListByUserId(sysUser.getId(), list);
        return Result.ok(sysTenant);
    }

    /**
     * 更新用户租户关系状态【低代码应用专用接口】
     */
    @PutMapping("/updateUserTenantStatus")
    //@RequiresPermissions("system:tenant:updateUserTenantStatus")
    public Result<String> updateUserTenantStatus(@RequestBody SysUserTenant userTenant) {
        String tenantId = TenantContext.getTenant();
        if (oConvertUtils.isEmpty(tenantId)) {
            return Result.error("未找到当前租户信息"); 
        }
        relationService.updateUserTenantStatus(userTenant.getUserId(), tenantId, userTenant.getStatus());
        return Result.ok("更新用户租户状态成功");
    }

    /**
     * 注销租户【低代码应用专用接口】
     *
     * @param sysTenant
     * @return
     */
    @PutMapping("/cancelTenant")
    //@RequiresPermissions("system:tenant:cancelTenant")
    public Result<String> cancelTenant(@RequestBody SysTenant sysTenant,HttpServletRequest request) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysTenant tenant = sysTenantService.getById(sysTenant.getId());
        if (null == tenant) {
            return Result.error("未找到当前租户信息");
        }
        if (!sysUser.getUsername().equals(tenant.getCreateBy())) {
            return Result.error("无权限，只能注销自己创建的租户！");
        }
        SysUser userById = sysUserService.getById(sysUser.getId());
        String loginPassword = request.getParameter("loginPassword");
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(),loginPassword, userById.getSalt());
        if (!passwordEncode.equals(userById.getPassword())) {
            return Result.error("密码不正确");
        }
        sysTenantService.removeById(sysTenant.getId());
        return Result.ok("注销成功");
    }
    //update-end---author:wangshuai ---date:20230107  for：[QQYUN-3725]申请加入租户，审核中状态增加接口------------

    /**
     * 获取租户用户不同状态下的数量【低代码应用专用接口】
     * @return
     */
    @GetMapping("/getTenantStatusCount")
    public Result<Long> getTenantStatusCount(@RequestParam(value = "status",defaultValue = "1") String status, HttpServletRequest req){
        String tenantId = TokenUtils.getTenantIdByRequest(req);
        if (null == tenantId) {
            return Result.error("未找到当前租户信息");
        }
        LambdaQueryWrapper<SysUserTenant> query = new LambdaQueryWrapper<>();
        query.eq(SysUserTenant::getTenantId,tenantId);
        query.eq(SysUserTenant::getStatus,status);
        long count = relationService.count(query);
        return Result.ok(count);
    }

    /**
     * 用户取消租户申请【低代码应用专用接口】
     * @param tenantId
     * @return
     */
    @PutMapping("/cancelApplyTenant")
    public Result<String> cancelApplyTenant(@RequestParam("tenantId") String tenantId){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        sysTenantService.leaveTenant(sysUser.getId(),tenantId);
        return Result.ok("取消申请成功");
    }

    //===========【低代码应用，前端专用接口 —— 加入限制只能维护和查看自己拥有的租户】==========================================================

    /**
     * 彻底删除租户
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteLogicDeleted")
    @RequiresPermissions("system:tenant:deleteTenantLogic")
    public Result<String> deleteTenantLogic(@RequestParam("ids") String ids){
        sysTenantService.deleteTenantLogic(ids);
        return Result.ok("彻底删除成功");
    }

    /**
     * 还原删除的租户
     * @param ids
     * @return
     */
    @PutMapping("/revertTenantLogic")
    @RequiresPermissions("system:tenant:revertTenantLogic")
    public Result<String> revertTenantLogic(@RequestParam("ids") String ids){
        sysTenantService.revertTenantLogic(ids);
        return Result.ok("还原成功");
    }

    /**
     * 退出租户【低代码应用专用接口】
     * @param sysTenant
     * @param request
     * @return
     */
    @DeleteMapping("/exitUserTenant")
    public Result<String> exitUserTenant(@RequestBody SysTenant sysTenant,HttpServletRequest request){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //验证用户是否已存在
        Integer count = relationService.userTenantIzExist(sysUser.getId(),sysTenant.getId());
        if (count == 0) {
            return Result.error("此租户下没有当前用户");
        }
        //验证密码
        String loginPassword = request.getParameter("loginPassword");
        SysUser userById = sysUserService.getById(sysUser.getId());
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(),loginPassword, userById.getSalt());
        if (!passwordEncode.equals(userById.getPassword())) {
            return Result.error("密码不正确");
        }
        //退出登录
        sysTenantService.exitUserTenant(sysUser.getId(),sysUser.getUsername(),String.valueOf(sysTenant.getId()));
        return Result.ok("退出租户成功");
    }

    /**
     * 变更租户拥有者【低代码应用专用接口】
     * @param userId
     * @return
     */
    @PostMapping("/changeOwenUserTenant")
    public Result<String> changeOwenUserTenant(@RequestParam("userId") String userId,
                                               @RequestParam("tenantId") String tenantId){
        sysTenantService.changeOwenUserTenant(userId,tenantId);
        return Result.ok("退出租户成功");
    }

    /**
     * 邀请用户到租户,通过手机号匹配 【低代码应用专用接口】
     * @param phone
     * @param departId
     * @return
     */
    @PostMapping("/invitationUser")
    public Result<String> invitationUser(@RequestParam(name="phone") String phone,
                                         @RequestParam(name="departId",defaultValue = "") String departId){
        return sysTenantService.invitationUser(phone,departId);
    }


    /**
     * 获取 租户产品包-3个默认admin的人员数量
     * @param tenantId
     * @return
     */
    @GetMapping("/loadAdminPackCount")
    public Result<List<TenantPackUserCount>> loadAdminPackCount(@RequestParam("tenantId") Integer tenantId){
        List<TenantPackUserCount> list = sysTenantService.queryTenantPackUserCount(tenantId);
        return Result.ok(list);
    }

    /**
     * 查询租户产品包信息
     * @param packModel
     * @return
     */
    @GetMapping("/getTenantPackInfo")
    public Result<TenantPackModel> getTenantPackInfo(TenantPackModel packModel){
        TenantPackModel tenantPackModel = sysTenantService.queryTenantPack(packModel);
        return Result.ok(tenantPackModel);
    }


    /**
     * 添加用户和产品包的关系数据
     * @param sysTenantPackUser
     * @return
     */
    @PostMapping("/addTenantPackUser")
    public Result<?> addTenantPackUser(@RequestBody SysTenantPackUser sysTenantPackUser){
        sysTenantService.addBatchTenantPackUser(sysTenantPackUser);
        return Result.ok("操作成功！");
    }

    /**
     * 从产品包移除用户
     * @param sysTenantPackUser
     * @return
     */
    @PutMapping("/deleteTenantPackUser")
    public Result<?> deleteTenantPackUser(@RequestBody SysTenantPackUser sysTenantPackUser){
        sysTenantService.deleteTenantPackUser(sysTenantPackUser);
        return Result.ok("操作成功！");
    }


    /**
     * 修改申请状态
     * @param sysTenant
     * @return
     */
    @PutMapping("/updateApplyStatus")
    public Result<?> updateApplyStatus(@RequestBody SysTenant sysTenant){
        SysTenant entity = this.sysTenantService.getById(sysTenant.getId());
        if(entity==null){
            return Result.error("租户不存在!");
        }
        entity.setApplyStatus(sysTenant.getApplyStatus());
        sysTenantService.updateById(entity);
        return Result.ok("");
    }


    /**
     * 获取产品包人员申请列表
     * @param tenantId
     * @return
     */
    @GetMapping("/getTenantPackApplyUsers")
    public Result<?> getTenantPackApplyUsers(@RequestParam("tenantId") Integer tenantId){
        List<TenantPackUser> list = sysTenantService.getTenantPackApplyUsers(tenantId);
        return Result.ok(list);
    }

    /**
     * 个人 申请成为管理员
     * @param sysTenantPackUser
     * @return
     */
    @PostMapping("/doApplyTenantPackUser")
    public Result<?> doApplyTenantPackUser(@RequestBody SysTenantPackUser sysTenantPackUser){
        sysTenantService.doApplyTenantPackUser(sysTenantPackUser);
        return Result.ok("申请成功！");
    }

    /**
     * 申请通过 成为管理员
     * @param sysTenantPackUser
     * @return
     */
    @PutMapping("/passApply")
    public Result<?> passApply(@RequestBody SysTenantPackUser sysTenantPackUser){
        sysTenantService.passApply(sysTenantPackUser);
        return Result.ok("操作成功！");
    }

    /**
     *  拒绝申请 成为管理员
     * @param sysTenantPackUser
     * @return
     */
    @PutMapping("/deleteApply")
    public Result<?> deleteApply(@RequestBody SysTenantPackUser sysTenantPackUser){
        sysTenantService.deleteApply(sysTenantPackUser);
        return Result.ok("");
    }

    /**
     * 查看是否已经申请过了超级管理员
     * @return
     */
    @GetMapping("/getApplySuperAdminCount")
    public Result<Long> getApplySuperAdminCount(){
        Long count = sysTenantService.getApplySuperAdminCount();
        return Result.ok(count);
    }

    /**
     * 进入应用组织页面 查询租户信息及当前用户是否有 管理员的权限--
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryTenantAuthInfo", method = RequestMethod.GET)
    public Result<TenantDepartAuthInfo> queryTenantAuthInfo(@RequestParam(name="id",required=true) String id) {
        TenantDepartAuthInfo info = sysTenantService.getTenantDepartAuthInfo(Integer.parseInt(id));
        return Result.ok(info);
    }

    /**
     * 获取产品包下的用户列表(分页)
     * @param tenantId
     * @param packId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/queryTenantPackUserList")
    public Result<IPage<TenantPackUser>> queryTenantPackUserList(@RequestParam("tenantId") String tenantId,
                                                                 @RequestParam("packId") String packId,
                                                                 @RequestParam("status") Integer status,
                                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        Page<TenantPackUser> page = new Page<>(pageNo,pageSize);
        IPage<TenantPackUser> pageList = sysTenantService.queryTenantPackUserList(tenantId,packId,status,page);
        return Result.ok(pageList);
    }

    /**
     * 获取当前租户下的部门和成员数量
     */
    @GetMapping("/getTenantCount")
    public Result<Map<String,Long>> getTenantCount(HttpServletRequest request){
        Map<String,Long> map = new HashMap<>();
        //update-begin---author:wangshuai---date:2023-11-24---for:【QQYUN-7177】用户数量显示不正确---
        if(oConvertUtils.isEmpty(TokenUtils.getTenantIdByRequest(request))){
            return Result.error("当前租户为空，禁止访问！");
        }
        Integer tenantId = oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(request));
        Long userCount = relationService.getUserCount(tenantId,CommonConstant.USER_TENANT_NORMAL);
        //update-end---author:wangshuai---date:2023-11-24---for:【QQYUN-7177】用户数量显示不正确---
        map.put("userCount",userCount);
        LambdaQueryWrapper<SysDepart> departQuery = new LambdaQueryWrapper<>();
        departQuery.eq(SysDepart::getDelFlag,String.valueOf(CommonConstant.DEL_FLAG_0));
        departQuery.eq(SysDepart::getTenantId,tenantId);
        //部门状态暂时没用，先注释掉
        //departQuery.eq(SysDepart::getStatus,CommonConstant.STATUS_1);
        long departCount = sysDepartService.count(departQuery);
        map.put("departCount",departCount);
        return Result.ok(map);
    }

    /**
     * 通过用户id获取租户列表（分页）
     *
     * @param sysUserTenantVo
     * @return
     */
    @GetMapping("/getTenantPageListByUserId")
    public Result<IPage<SysTenant>> getTenantPageListByUserId(SysUserTenantVo sysUserTenantVo,
                                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> list = null;
        String userTenantStatus = sysUserTenantVo.getUserTenantStatus();
        if (oConvertUtils.isNotEmpty(userTenantStatus)) {
            list = Arrays.asList(userTenantStatus.split(SymbolConstant.COMMA));
        }
        Page<SysTenant> page = new Page<>(pageNo,pageSize);
        IPage<SysTenant> pageList = relationService.getTenantPageListByUserId(page,sysUser.getId(),list,sysUserTenantVo);
        return Result.ok(pageList);
    }

    /**
     * 同意或拒绝加入租户
     */
    @PutMapping("/agreeOrRefuseJoinTenant")
    public Result<String> agreeOrRefuseJoinTenant(@RequestParam("tenantId") Integer tenantId, 
                                                  @RequestParam("status") String status){
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        SysTenant tenant = sysTenantService.getById(tenantId);
        if(null == tenant){
            return Result.error("不存在该组织");
        }
        SysUserTenant sysUserTenant = relationService.getUserTenantByTenantId(userId, tenantId);
        if (null == sysUserTenant) {
            return Result.error("该用户不存在该组织中，无权修改");
        }
        String content = "";
        SysUser user = new SysUser();
        user.setUsername(sysUserTenant.getCreateBy());
        String realname = oConvertUtils.getString(sysUser.getRealname(),sysUser.getUsername());
        //成功加入
        if(CommonConstant.USER_TENANT_NORMAL.equals(status)){
            //修改租户状态
            relationService.agreeJoinTenant(userId,tenantId);
            content = content + realname + "已同意您发送的加入 " + tenant.getName() + " 的邀请";
            sysTenantService.sendMsgForAgreeAndRefuseJoin(user, content);
            return Result.OK("您已同意该组织的邀请");
        }else if(CommonConstant.USER_TENANT_REFUSE.equals(status)){
            //直接删除关系表即可
            relationService.refuseJoinTenant(userId,tenantId);
            content = content + realname + "拒绝了您发送的加入 " + tenant.getName() + " 的邀请";
            sysTenantService.sendMsgForAgreeAndRefuseJoin(user, content);
            return Result.OK("您已成功拒绝该组织的邀请");
        }
        return Result.error("类型不匹配，禁止修改数据");
    }
    
    /**
     * 目前只给敲敲云租户下删除用户使用
     * 
     * 根据密码删除用户
     */
    @DeleteMapping("/deleteUserByPassword")
    public Result<String> deleteUserByPassword(@RequestBody SysUser sysUser,HttpServletRequest request){
        Integer tenantId = oConvertUtils.getInteger(TokenUtils.getTenantIdByRequest(request), null);
        sysTenantService.deleteUserByPassword(sysUser, tenantId);
        return Result.ok("删除用户成功");
    }

    /**
     *  查询当前用户的所有有效租户【知识库专用接口】
     * @return
     */
    @RequestMapping(value = "/getCurrentUserTenantForFile", method = RequestMethod.GET)
    public Result<Map<String,Object>> getCurrentUserTenantForFile() {
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            List<SysTenant> tenantList = sysTenantService.getTenantListByUserId(sysUser.getId());
            Map<String,Object> map = new HashMap<>(5);
            //在开启saas租户隔离的时候并且租户数据不为空，则返回租户信息
            if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL && CollectionUtil.isNotEmpty(tenantList)) {
                map.put("list", tenantList);
            }
            result.setSuccess(true);
            result.setResult(map);
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("查询失败！");
        }
        return result;
    }
}
