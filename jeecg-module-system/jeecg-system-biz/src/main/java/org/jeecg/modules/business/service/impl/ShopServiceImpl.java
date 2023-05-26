package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.mapper.ShopMapper;
import org.jeecg.modules.business.service.IShopService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 店铺
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
	
	@Autowired
	private ShopMapper shopMapper;
	
	@Override
	public List<Shop> selectByMainId(String mainId) {
		return shopMapper.selectByMainId(mainId);
	}

	@Override
	public List<Shop> listByClient(String clientID) {
		return shopMapper.selectByClient(clientID);
	}
}
