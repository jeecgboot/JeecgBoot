package org.jeecg.modules.asset.service;

import org.jeecg.modules.asset.entity.Asset;
import org.jeecg.modules.asset.vo.AssetVo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 资产服务接口
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
public interface IAssetService extends IService<Asset> {
    IPage<AssetVo> getAssetList(Page<Asset> page, int tenantId);
}
