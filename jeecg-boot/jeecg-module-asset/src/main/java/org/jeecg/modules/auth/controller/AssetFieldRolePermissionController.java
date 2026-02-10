package org.jeecg.modules.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.auth.dto.SetRoleFieldPermissionDTO;
import org.jeecg.modules.auth.entity.AssetFieldRolePermission;
import org.jeecg.modules.auth.service.IAssetFieldRolePermissionService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@RestController
@RequestMapping("/asset/field/role/permission")
@Slf4j
@Tag(name = "资产字段角色权限管理")
public class AssetFieldRolePermissionController
        extends JeecgController<AssetFieldRolePermission, IAssetFieldRolePermissionService> {

    /**
     * 分页列表查询
     *
     * @param assetFieldRolePermission
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "字段角色权限管理-分页列表查询")
    @Operation(summary = "字段角色权限管理-分页列表查询")
    @GetMapping("/list")
    public Result<?> queryPageList(AssetFieldRolePermission assetFieldRolePermission,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }
        QueryWrapper<AssetFieldRolePermission> queryWrapper = QueryGenerator.initQueryWrapper(assetFieldRolePermission,
                request.getParameterMap());
        return Result.ok(service.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 添加
     *
     * @param assetFieldRolePermission
     * @return
     */
    @AutoLog(value = "字段角色权限管理-添加")
    @Operation(summary = "字段角色权限管理-添加")
    @PostMapping("/add")
    public Result<?> add(@RequestBody AssetFieldRolePermission assetFieldRolePermission) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }

        assetFieldRolePermission
                .setPermissionBits(service.getPermissionBits(assetFieldRolePermission.getPermissionActions()));
        service.save(assetFieldRolePermission);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param assetFieldRolePermission
     * @return
     */
    @AutoLog(value = "字段角色权限管理-编辑")
    @Operation(summary = "字段角色权限管理-编辑")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody AssetFieldRolePermission assetFieldRolePermission) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }

        assetFieldRolePermission
                .setPermissionBits(service.getPermissionBits(assetFieldRolePermission.getPermissionActions()));

        service.updateById(assetFieldRolePermission);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "字段角色权限管理-通过id删除")
    @Operation(summary = "删除资产字段角色权限")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam("id") String id) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "字段角色权限管理-批量删除")
    @Operation(summary = "字段角色权限管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "字段角色权限管理-通过id查询")
    @Operation(summary = "字段角色权限管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        return Result.OK(service.getById(id));
    }

    /**
     * 导出excel
     *
     * @param request
     * @param assetFieldRolePermission
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AssetFieldRolePermission assetFieldRolePermission) {
        return super.exportXls(request, assetFieldRolePermission, AssetFieldRolePermission.class, "字段角色权限管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AssetFieldRolePermission.class);
    }

    /**
     * 配置角色字段可见范围
     *
     * @param setRoleFieldPermissionDTO 配置对象
     * @return
     */
    @AutoLog(value = "字段角色权限管理-配置角色字段可见范围")
    @Operation(summary = "字段角色权限管理-配置角色字段可见范围")
    @PostMapping("/config")
    public Result<?> configFieldPermission(@Valid @RequestBody SetRoleFieldPermissionDTO setRoleFieldPermissionDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return Result.OK(setRoleFieldPermissionDTO);
    }

    /**
     * 查询角色字段权限
     *
     * @param roleId
     * @return
     */
    @AutoLog(value = "字段角色权限管理-查询角色字段权限")
    @Operation(summary = "字段角色权限管理-查询角色字段权限")
    @GetMapping("/queryRoleFieldPermission")
    public Result<?> queryRoleFieldPermission(@RequestParam("roleId") String roleId) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }
        return Result.OK(service.lambdaQuery().eq(AssetFieldRolePermission::getRoleId, roleId).list());
    }
}
