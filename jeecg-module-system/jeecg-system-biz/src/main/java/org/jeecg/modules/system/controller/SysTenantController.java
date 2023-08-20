package org.jeecg.modules.system.controller;


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
	public Result<IPage<SysTenant>> queryPageList(SysTenant sysTenant, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
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
    public Result<IPage<SysTenant>> recycleBinPageList(SysTenant sysTenant, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req){
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
                
                Long userCount = sysTenantService.countUserLinkTenant(id);
                if (userCount == 0) {
                    idList.add(Integer.parseInt(id));
                }
            }
            if (idList.size() > 0) {
                sysTenantService.removeByIds(idList);
                if (ls.length == idList.size()) {
                    result.success("删除成功！");
                } else {
                    result.success("部分删除成功！（被引用的租户无法删除）");
                }
            }else  {
                result.error500("选择的租户都已被引用，无法删除！");
            }
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
    @DeleteMapping("/deletePackPermissions")
    @RequiresPermissions("system:tenant:delete:pack")
    public Result<String> deletePackPermissions(@RequestParam(value = "ids") String ids) {
        sysTenantPackService.deletePackPermissions(ids);
        return Result.ok("删除租户产品包成功");
    }
    



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
    public Result<String> exitUserTenant(@RequestBody SysTenant sysTenant, HttpServletRequest request){
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
    public Result<String> changeOwenUserTenant(@RequestParam("userId") String userId){
        sysTenantService.changeOwenUserTenant(userId);
        return Result.ok("退出租户成功");
    }

    /**
     * 邀请用户到租户,通过手机号匹配 【低代码应用专用接口】
     * @param phone
     * @return
     */
    @PostMapping("/invitationUser")
    public Result<String> invitationUser(@RequestParam(name="phone") String phone){
        return sysTenantService.invitationUser(phone);
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
}
