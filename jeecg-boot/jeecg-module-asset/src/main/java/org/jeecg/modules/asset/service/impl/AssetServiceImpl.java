package org.jeecg.modules.asset.service.impl;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.asset.dto.AssetFiledConvertDto;
import org.jeecg.modules.asset.util.AssetFieldParserUtil;
import org.jeecg.modules.asset.entity.Asset;
import org.jeecg.modules.asset.mapper.AssetMapper;
import org.jeecg.modules.asset.service.IAssetService;
import org.jeecg.modules.asset.vo.AssetVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 资产服务实现
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
@Service
@Slf4j
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService {

    private final AssetFieldParserUtil assetFieldParserUtil;

    public AssetServiceImpl(AssetFieldParserUtil assetFieldParserUtil) {
        this.assetFieldParserUtil = assetFieldParserUtil;
    }

    @Override
    public IPage<AssetVo> getAssetList(Page<Asset> page, int tenantId) {

        IPage<Asset> assetPage = lambdaQuery().page(page);

        if (assetPage.getTotal() == 0) {
            return new Page<>(page.getCurrent(), page.getSize(), assetPage.getTotal());
        }

        String userId = getCurrentUserId();

        List<Long> assetIds = assetPage.getRecords()
                .stream()
                .map(Asset::getId)
                .toList();

        AssetFiledConvertDto assetFiledConvertDto = assetFieldParserUtil.getAssetFiledConvertDto(userId, tenantId, assetIds);

        return assetPage.convert(asset -> assetFieldParserUtil.parse(asset, assetFiledConvertDto));
    }

    private String getCurrentUserId() {
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            return principal instanceof LoginUser ? ((LoginUser) principal).getId() : null;
        } catch (Exception e) {
            log.warn("Failed to get current user: {}", e.getMessage());
            return null;
        }
    }


}
