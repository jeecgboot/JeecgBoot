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
import java.util.*;
import java.util.stream.Collectors;

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
        if (shopList != null && !shopList.isEmpty()) {
            for (Shop entity : shopList) {
                //外键设置
                entity.setOwnerId(client.getId());
                shopMapper.insert(entity);
            }
        }
        if (clientSkuList != null && !clientSkuList.isEmpty()) {
            for (ClientSku entity : clientSkuList) {
                //外键设置
                entity.setClientId(client.getId());
                clientSkuMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(Client client, List<Shop> shopList) {
        // 1. update the main client record
        clientMapper.updateById(client);
        // 2. Fetch all existing shop records related to this client from db
        List<Shop> dbShops = shopMapper.selectByMainId(client.getId());
        Map<String, Shop> dbShopMap = dbShops.stream()
                .collect(Collectors.toMap(Shop::getId, s -> s));
        // 3. Insert or update each incoming shop
        Set<String> incomingIds = new HashSet<>();
        for (Shop shop : shopList) {
            shop.setOwnerId(client.getId());
            incomingIds.add(shop.getId());
            if (dbShopMap.containsKey(shop.getId())) {
                shopMapper.updateById(shop);
            } else {
                shopMapper.insert(shop);
            }
        }
        // 4. Remove shops from the db that are no longer included in the incoming list
        for (Shop dbShop : dbShops) {
            if (!incomingIds.contains(dbShop.getId())) {
                shopMapper.deleteById(dbShop.getId());
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
    public String getActiveClientIdByCode(String code) {
        return clientMapper.getActiveClientIdByCode(code);
    }

    @Override
    public void anonymizePersonalData(int directClientAnonymizationPeriod) {
        clientMapper.anonymizePersonalData(directClientAnonymizationPeriod);
    }

    @Override
    public Client getByShopId(String shopId) {
        return clientMapper.getByShopId(shopId);
    }

    @Override
    public Client getClientFromCredit(String invoiceNumber) {
        return clientMapper.getClientFromCredit(invoiceNumber);
    }

    @Override
    public List<String> getClientsByCode(List<String> clientCodes) {
        return clientMapper.getClientsByCode(clientCodes);
    }

    @Override
    public void updateClientEmailPreference(String clientId, Boolean receiveInvoiceByEmail) {
        clientMapper.updateClientEmailPreference(clientId, receiveInvoiceByEmail);
    }

    @Override
    public List<Client> getActiveClientsToReceiveInventory() {
        return clientMapper.getActiveClientsToReceiveInventory();
    }

    @Override
    public List<Client> getClientsByCodes(List<String> clientCodes) {
        return clientMapper.getClientsByCodes(clientCodes);
    }
    @Override
    public List<Client> getClientsWithAutoInvoice() {
        return clientMapper.getClientsWithAutoInvoice();
    }

    @Override
    public List<String> findClientIdsBySalesId(String salesId) {
        return clientMapper.findClientIdsBySalesId(salesId);
    }
}
