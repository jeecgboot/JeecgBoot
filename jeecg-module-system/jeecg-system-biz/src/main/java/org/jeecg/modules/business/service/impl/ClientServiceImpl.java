package org.jeecg.modules.business.service.impl;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.mapper.ClientUserMapper;
import org.jeecg.modules.business.mapper.ShopMapper;
import org.jeecg.modules.business.mapper.ClientSkuMapper;
import org.jeecg.modules.business.mapper.ClientMapper;
import org.jeecg.modules.business.service.IClientService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * @Description: 客户
 * @Author: jeecg-boot
 * @Date: 2021-04-02
 * @Version: V1.0
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

    private final ClientMapper clientMapper;
    private final ShopMapper shopMapper;
    private final ClientSkuMapper clientSkuMapper;
    private final ClientUserMapper clientUserMap;

    @Autowired
    public ClientServiceImpl(ClientMapper clientMapper, ShopMapper shopMapper,
                             ClientSkuMapper clientSkuMapper, ClientUserMapper clientUserMap) {
        this.clientMapper = clientMapper;
        this.shopMapper = shopMapper;
        this.clientSkuMapper = clientSkuMapper;
        this.clientUserMap = clientUserMap;
    }

    @Override
    @Transactional
    public void saveMain(Client client, List<Shop> shopList, List<ClientSku> clientSkuList) {
        clientMapper.insert(client);
        if (shopList != null && shopList.size() > 0) {
            for (Shop entity : shopList) {
                //外键设置
                entity.setOwnerId(client.getId());
                shopMapper.insert(entity);
            }
        }
        if (clientSkuList != null && clientSkuList.size() > 0) {
            for (ClientSku entity : clientSkuList) {
                //外键设置
                entity.setClientId(client.getId());
                clientSkuMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(Client client, List<Shop> shopList, List<ClientSku> clientSkuList) {
        clientMapper.updateById(client);

        //1.先删除子表数据
        shopMapper.deleteByMainId(client.getId());
        clientSkuMapper.deleteByMainId(client.getId());

        //2.子表数据重新插入
        if (shopList != null && shopList.size() > 0) {
            for (Shop entity : shopList) {
                //外键设置
                entity.setOwnerId(client.getId());
                shopMapper.insert(entity);
            }
        }
        if (clientSkuList != null && clientSkuList.size() > 0) {
            for (ClientSku entity : clientSkuList) {
                //外键设置
                entity.setClientId(client.getId());
                clientSkuMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        shopMapper.deleteByMainId(id);
        clientSkuMapper.deleteByMainId(id);
        clientMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            shopMapper.deleteByMainId(id.toString());
            clientSkuMapper.deleteByMainId(id.toString());
            clientMapper.deleteById(id);
        }
    }

    /**
     * Find client information for current system user.
     *
     * @return client information of the current system user, or null in case of current is not a client
     */
    @Override
    public Client getCurrentClient() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return clientUserMap.selectClientByUserId(sysUser.getId());
    }

    @Override
    public List<Client> getClientsByType(String type) {
        return clientMapper.getClientByType(type);
    }

    @Override
    public Client getClientFromOrder(String orderId) {
        return clientMapper.getClientFromOrder(orderId);
    }

    @Override
    public Client getClientFromPurchase(String purchaseId) {
        return clientMapper.getClientFromPurchase(purchaseId);
    }

    @Override
    public List<String> getClientsFromPurchases(List<String> purchaseIds) {
        return clientMapper.getClientsFromPurchases(purchaseIds);
    }

    @Override
    public Client getClientBySku(String skuId) {
        return clientMapper.getClientBySku(skuId);
    }

    @Override
    public Client getClientFromInvoice(String invoiceNumber) {
        return clientMapper.getClientFromInvoice(invoiceNumber);
    }

    @Override
    public String getClientEntity(String id) {
        return clientMapper.getClientEntity(id);
    }
    @Override
    public Map<String, String> getClientsEntity(List<String> ids) {
        return clientMapper.getClientsEntity(ids);
    }

    @Override
    public String getClientIdByCode(String code) {
        return clientMapper.getClientIdByCode(code);
    }

    @Override
    public void anonymizePersonalData(int directClientAnonymizationPeriod) {
        clientMapper.anonymizePersonalData(directClientAnonymizationPeriod);
    }

    @Override
    public Client getByShopId(String shopId) {
        return clientMapper.getByShopId(shopId);
    }
}
