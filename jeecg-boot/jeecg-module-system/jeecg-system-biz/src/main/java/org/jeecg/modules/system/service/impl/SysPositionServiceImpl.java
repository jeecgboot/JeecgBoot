package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.enums.SysRoleEnum;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.mapper.SysPositionMapper;
import org.jeecg.modules.system.service.ISysPositionService;
import org.jeecg.modules.system.service.ISysTenantPackService;
import org.jeecg.modules.system.vo.SysPositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {

    @Autowired
    private ISysTenantPackService sysTenantPackService;

    @Override
    public SysPosition getByCode(String code) {
        LambdaQueryWrapper<SysPosition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPosition::getCode, code);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<SysPosition> getPositionList(String userId) {
        return this.baseMapper.getPositionList(userId);
    }

    @Override
    public String getPositionName(List<String> postList) {
        List<SysPosition> positionNameList = this.baseMapper.getPositionName(postList);
        if (null != positionNameList && positionNameList.size()>0) {
            return positionNameList.stream().map(SysPosition::getName).collect(Collectors.joining(SymbolConstant.COMMA));
        }
        return "";
    }

    //update-begin---author:sjlei ---date:2026-04-17  for：【#9496】全量同步N+1查询性能优化-----------
    @Override
    public List<SysPositionVO> getPositionListByUserIds(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        return this.baseMapper.getPositionListByUserIds(userIds);
    }
    //update-end---author:sjlei ---date:2026-04-17  for：【#9496】全量同步N+1查询性能优化-----------

    @Override
    public Integer checkPositionAuth(String permission, boolean allowTenantAdmin) {
        //当前用户有没有 admin 或 zuhuadmin 角色或者有对应按钮权限直接放行
          if (SysRoleEnum.hasAnyRole(SysRoleEnum.ADMIN, SysRoleEnum.TENANT_ADMIN) || SecurityUtils.getSubject().isPermitted(permission)) {
            return null;
        }
        //是否需要判断租户管理员，比如导入、导出就不需要敲敲云没有用的地方
        String tenantId = TenantContext.getTenant();
        if (!allowTenantAdmin || oConvertUtils.isEmpty(tenantId)) {
            throw new UnauthorizedException("没有权限，请联系管理员分配权限！");
        }
        // 非当前租户的组织账户管理员或超级管理员时抛出异常
        try {
            sysTenantPackService.izHaveManageUserAuth(tenantId);
        } catch (Exception e) {
            throw new JeecgBootBizTipException("没有权限，请联系管理员分配权限！");
        }
        return oConvertUtils.getInt(tenantId, 0);
    }

    @Override
    public boolean isPositionInTenant(String positionId, Integer tenantId) {
        SysPosition position = oConvertUtils.isEmpty(positionId) ? null : this.getById(positionId);
        return position != null && tenantId.equals(position.getTenantId());
    }
}
