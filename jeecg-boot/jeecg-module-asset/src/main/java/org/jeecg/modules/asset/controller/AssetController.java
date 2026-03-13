package org.jeecg.modules.asset.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.asset.dto.QueryPageListDto;
import org.jeecg.modules.asset.service.IAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/asset/test")
@RestController
@Tag(name = "资产")
class AssetController {

    private ISysBaseAPI sysBaseAPI;

    private CommonAPI commonAPI;

    private IAssetService assetService;

    @Autowired
    public void setSysBaseAPI(ISysBaseAPI sysBaseAPI) {
        this.sysBaseAPI = sysBaseAPI;
    }

    @Autowired
    public void setCommonAPI(CommonAPI commonAPI) {
        this.commonAPI = commonAPI;
    }

    @Autowired
    public void setAssetService(IAssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    @Operation(summary = "测试")
    public Result<?> index(HttpServletRequest request) {
        return Result.OK(commonAPI.queryUserRoles(JwtUtil.getUserNameByToken(request)));
    }

    @GetMapping("/2")
    @Operation(summary = "测试2")
    public Result<?> index2(HttpServletRequest request) {
        return Result.OK(sysBaseAPI.getRoleIdsByUsername(JwtUtil.getUserNameByToken(request)));
    }

    @PostMapping("/pageList")
    @Operation(summary = "获取租户资产")
    public Result<?> pageList(@RequestBody(required = false) QueryPageListDto queryPageListDto, HttpServletRequest request) {
        if (queryPageListDto == null) {
            queryPageListDto = new QueryPageListDto(1, 20);
        }
        int tenantId = oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(request), 0);
        return Result.OK(assetService.getAssetList(new Page<>(queryPageListDto.getPageNo(), queryPageListDto.getPageSize()), tenantId));
    }
}