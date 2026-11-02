package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.vo.SysPositionVO;

import java.util.List;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
public interface ISysPositionService extends IService<SysPosition> {

    /**
     * 通过code查询
     * @param code 职务编码
     * @return SysPosition
     */
    SysPosition getByCode(String code);

    /**
     * 通过用户id获取职位名称列表
     * @param userId
     * @return
     */
    List<SysPosition> getPositionList(String userId);

    /**
     * 获取职位名称
     * @param postList
     * @return
     */
    String getPositionName(List<String> postList);

    /**
     * 批量通过用户id列表查询职位VO（含userId字段，用于批量同步场景消除N+1查询）
     *
     * @param userIds 用户id列表
     * @return 职位VO列表（每条记录含userId字段，供调用方分组）
     */
    List<SysPositionVO> getPositionListByUserIds(List<String> userIds);

    //update-begin---author:wangshuai ---date:2026-10-26  for：【GHSA-p4f8-h832-99pw】职务增删改、成员管理补充权限校验-----------
    /**
     * 职务管理鉴权：admin、租户管理员(zuhuadmin)角色或拥有对应按钮权限时放行（成员管理沿用 system:position:edit）；
     * 否则只有敲敲云「组织管理-职位」页面用到的接口可以继续：须为当前租户的组织账户管理员或超级管理员，且只能操作本租户的职务和用户。
     * 注册用户可以自建组织成为超级管理员，所以这条路径必须限定在当前租户内
     *
     * @param permission       按钮权限编码
     * @param allowTenantAdmin 是否放行敲敲云租户管理员
     * @return 需要限定在当前租户内操作时返回当前租户ID；admin、zuhuadmin、有按钮权限时返回null
     */
    Integer checkPositionAuth(String permission, boolean allowTenantAdmin);

    /**
     * 职务是否属于指定租户
     *
     * @param positionId 职务ID
     * @param tenantId   租户ID
     * @return 属于返回true
     */
    boolean isPositionInTenant(String positionId, Integer tenantId);
    //update-end---author:wangshuai ---date:2026-10-26  for：【GHSA-p4f8-h832-99pw】职务增删改、成员管理补充权限校验-----------

}
