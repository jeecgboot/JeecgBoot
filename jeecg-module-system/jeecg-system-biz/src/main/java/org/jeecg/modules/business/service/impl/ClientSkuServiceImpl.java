package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.mapper.ClientSkuMapper;
import org.jeecg.modules.business.service.IClientSkuService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客户名下SKU
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
@Service
public class ClientSkuServiceImpl extends ServiceImpl<ClientSkuMapper, ClientSku> implements IClientSkuService {
	
	@Autowired
	private ClientSkuMapper clientSkuMapper;
	
	@Override
	public List<ClientSku> selectByMainId(String mainId) {
		return clientSkuMapper.selectByMainId(mainId);
	}
}
