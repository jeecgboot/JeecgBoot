package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.mapper.ClientSkuMapper;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IClientSkuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客户名下SKU
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
@Slf4j
@Service
public class ClientSkuServiceImpl extends ServiceImpl<ClientSkuMapper, ClientSku> implements IClientSkuService {
	
	@Autowired
	private ClientSkuMapper clientSkuMapper;
	@Autowired
	private IClientService clientService;
	
	@Override
	public List<ClientSku> selectByMainId(String mainId) {
		return clientSkuMapper.selectByMainId(mainId);
	}
	@Override
	public List<String> saveClientSku(List<Sku> newSkus) {
		List<String> unknownSkuErpCode = new ArrayList<>(); // sku erp code that we couldn't find client for
		for(Sku sku : newSkus) {
			String erpCode = sku.getErpCode();
			if(erpCode == null) {
				log.info("Couldn't associate sku \"{}\" with any client. ErpCode is NULL", sku.getId());
				unknownSkuErpCode.add("Sku ID : " + sku.getId() + " - ErpCode is NULL");
				continue;
			}
			int index = erpCode.lastIndexOf("-");
			if(index == -1) {
				log.info("Couldn't associate sku \"{}\" with any client. Wrong format", erpCode);
				unknownSkuErpCode.add(erpCode + " - Wrong format");
				continue;
			}
			String internalCode = erpCode.substring(index+1);
			String clientId = clientService.getClientIdByCode(internalCode);
            if (clientId != null) {
				log.info("Associating sku \"{}\" with client \"{}\" : \"{}\". ", sku.getErpCode(), internalCode, clientId);
				addClientSku(clientId, sku.getId());
				continue;
            }
			if(!internalCode.equals("MD")) {
				log.info("Couldn't associate sku \"{}\" with any client. ", erpCode);
				unknownSkuErpCode.add(erpCode);
				continue;
			}
			if(sku.getZhName().contains("灯")) {
				clientId = clientService.getClientIdByCode("MD-Lumiart");
				log.info("Associating sku \"{}\" with client \"{}\" : \"{}\". ", sku.getErpCode(), internalCode, "MD-Lumiart");
				addClientSku(clientId, sku.getId());
			}
			if(sku.getZhName().contains("袜")) {
				clientId = clientService.getClientIdByCode("MD-Lou");
				log.info("Associating sku \"{}\" with client \"{}\" : \"{}\". ", sku.getErpCode(), internalCode, "MD-Lou");
				addClientSku(clientId, sku.getId());
			}
        }
		return unknownSkuErpCode;
	}

	@Override
	public void addClientSku(String clientId, String skuId) {
		ClientSku clientSku = new ClientSku();
		clientSku.setClientId(clientId);
		clientSku.setSkuId(skuId);
		clientSkuMapper.insert(clientSku);
	}

	@Override
	public List<Sku> getUnpairedSkus() {
		return clientSkuMapper.getUnpairedSkus();
	}
}
