package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SkuPrice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: SKU价格表
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
public interface ISkuPriceService extends IService<SkuPrice> {

	public List<SkuPrice> selectByMainId(String mainId);
}
