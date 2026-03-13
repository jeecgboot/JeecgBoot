package org.jeecg.modules.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.auth.entity.AssetFieldDefinition;
import org.jeecg.modules.auth.service.IAssetFieldDefinitionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@RestController
@RequestMapping("/asset/field/definition")
@Slf4j
@Tag(name = "资产字段管理")
public class AssetFieldDefinitionController
        extends JeecgController<AssetFieldDefinition, IAssetFieldDefinitionService> {

    /**
     * 分页列表查询
     *
     * @param assetFieldDefinition
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "资产字段管理-分页列表查询")
    @Operation(summary = "资产字段管理-分页列表查询")
    @GetMapping("/list")
    public Result<?> queryPageList(AssetFieldDefinition assetFieldDefinition,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest request) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }
        QueryWrapper<AssetFieldDefinition> queryWrapper = QueryGenerator.initQueryWrapper(assetFieldDefinition,
                request.getParameterMap());
        return Result.ok(service.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * 添加
     *
     * @param assetFieldDefinition
     * @return
     */
    @AutoLog(value = "资产字段管理-添加")
    @Operation(summary = "资产字段管理-添加")
    @PostMapping("/add")
    public Result<?> add(@RequestBody AssetFieldDefinition assetFieldDefinition) {
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);

        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }

        if (StringUtils.isBlank(assetFieldDefinition.getFieldKey())) {
            assetFieldDefinition.setFieldKey(service.generateFieldKey(tenantId));
        } else {
            if (service.fieldKeyExist(assetFieldDefinition.getFieldKey())) {
                return Result.error("The key:" + assetFieldDefinition.getFieldKey() + " already exist.");
            }
        }

        service.save(assetFieldDefinition);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param assetFieldDefinition
     * @return
     */
    @AutoLog(value = "资产字段管理-编辑")
    @Operation(summary = "资产字段管理-编辑")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody AssetFieldDefinition assetFieldDefinition) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
            if (tenantId == -1) {
                return Result.error("Access denied.");
            }
        }
        service.updateById(assetFieldDefinition);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "资产字段管理-通过id删除")
    @Operation(summary = "资产字段管理-通过id删除")
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
    @AutoLog(value = "资产字段管理-批量删除")
    @Operation(summary = "资产字段管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "资产字段管理-通过id查询")
    @Operation(summary = "资产字段管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") Integer id) {
        return Result.OK(service.getById(id));
    }

    /**
     * 导出excel
     *
     * @param request
     * @param assetFieldDefinition
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AssetFieldDefinition assetFieldDefinition) {
        return super.exportXls(request, assetFieldDefinition, AssetFieldDefinition.class, "资产字段管理");
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
        return super.importExcel(request, response, AssetFieldDefinition.class);
    }

}
