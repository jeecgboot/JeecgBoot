package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
public interface IShopService extends IService<Shop> {

	public List<Shop> selectByMainId(String mainId);

    List<Shop> listByClient(String clientID);
}
