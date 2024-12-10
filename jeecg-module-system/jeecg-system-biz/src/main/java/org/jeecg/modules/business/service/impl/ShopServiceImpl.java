package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.mapper.ShopMapper;
import org.jeecg.modules.business.service.IShopService;
import org.jeecg.modules.business.vo.ShopPage;
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
	@Override
	public List<String> listIdByClient(String clientID) {
		return shopMapper.selectShopIdByClient(clientID);
	}

	@Override
	public List<String> getShopIdsByClientAndType(List<String> clientIds, String clientType) {
		return shopMapper.getShopIdsByClientAndType(clientIds, clientType);
	}

	@Override
	public String getNameById(String shopId) {
		return shopMapper.getNameById(shopId);
	}

	@Override
	public String getCodeById(String shopId) {
		return shopMapper.getCodeById(shopId);
	}

	@Override
	public List<ShopPage> listShopGroupedByClient() {
		return shopMapper.listShopGroupedByClient();
	}

	@Override
	public String getIdByCode(String erpCode) {
		return shopMapper.getIdByCode(erpCode);
	}
}
