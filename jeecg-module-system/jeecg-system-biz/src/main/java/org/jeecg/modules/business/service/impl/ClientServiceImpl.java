package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.ClientSalesperson;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.InvoiceEntity;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.IClientService;
import org.springframework.beans.BeanUtils;
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
@Slf4j
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

    private final ClientMapper clientMapper;
    private final ShopMapper shopMapper;
    private final ClientSkuMapper clientSkuMapper;
    private final InvoiceEntityMapper invoiceEntityMapper;
    private final ClientUserMapper clientUserMap;
    private final ClientSalespersonMapper clientSalespersonMapper;

    @Autowired
    public ClientServiceImpl(ClientMapper clientMapper, ShopMapper shopMapper,
                             ClientSkuMapper clientSkuMapper, InvoiceEntityMapper invoiceEntityMapper,
                             ClientUserMapper clientUserMap,ClientSalespersonMapper clientSalespersonMapper) {
        this.clientMapper = clientMapper;
        this.shopMapper = shopMapper;
        this.clientSkuMapper = clientSkuMapper;
        this.invoiceEntityMapper = invoiceEntityMapper;
        this.clientUserMap = clientUserMap;
        this.clientSalespersonMapper = clientSalespersonMapper;
    }

    @Override
    @Transactional
    public void saveMain(Client client, List<Shop> shopList, List<ClientSku> clientSkuList, List<InvoiceEntity> invoiceEntityList) {
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
        if (invoiceEntityList != null && !invoiceEntityList.isEmpty()) {
            enforceSingleDefault(invoiceEntityList);
            for (InvoiceEntity entity : invoiceEntityList) {
                entity.setClientId(client.getId());
                invoiceEntityMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(Client client, List<Shop> shopList, List<InvoiceEntity> invoiceEntityList) {
        // 1. update the main client record
        clientMapper.updateById(client);
        // 2. Fetch all existing shop records related to this client from db
        List<Shop> dbShops = shopMapper.selectByMainId(client.getId());
        Map<String, Shop> dbShopMap = dbShops.stream()
                .collect(Collectors.toMap(Shop::getId, s -> s));
        // 3. Insert or update each incoming shop
        Set<String> incomingIds = new HashSet<>();
        if (shopList != null) {
            for (Shop shop : shopList) {
                shop.setOwnerId(client.getId());
                incomingIds.add(shop.getId());
                if (shop.getId() != null && dbShopMap.containsKey(shop.getId())) {
                    shopMapper.updateById(shop);
                } else {
                    shopMapper.insert(shop);
                }
            }
        }
        // 4. Remove shops from the db that are no longer included in the incoming list
        for (Shop dbShop : dbShops) {
            if (!incomingIds.contains(dbShop.getId())) {
                shopMapper.deleteById(dbShop.getId());
            }
        }

        List<InvoiceEntity> dbInvoiceEntities = invoiceEntityMapper.selectByMainId(client.getId());
        Map<String, InvoiceEntity> dbInvoiceEntityMap = dbInvoiceEntities.stream()
                .collect(Collectors.toMap(InvoiceEntity::getId, e -> e));
        Set<String> incomingInvoiceEntityIds = new HashSet<>();
        if (invoiceEntityList != null) {
            enforceSingleDefault(invoiceEntityList);
            for (InvoiceEntity invoiceEntity : invoiceEntityList) {
                invoiceEntity.setClientId(client.getId());
                incomingInvoiceEntityIds.add(invoiceEntity.getId());
                if (invoiceEntity.getId() != null && dbInvoiceEntityMap.containsKey(invoiceEntity.getId())) {
                    invoiceEntityMapper.updateById(invoiceEntity);
                } else {
                    invoiceEntityMapper.insert(invoiceEntity);
                }
            }
        }
        for (InvoiceEntity dbInvoiceEntity : dbInvoiceEntities) {
            if (!incomingInvoiceEntityIds.contains(dbInvoiceEntity.getId())) {
                invoiceEntityMapper.deleteById(dbInvoiceEntity.getId());
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        clientSalespersonMapper.deleteByClientId(id);
        shopMapper.deleteByMainId(id);
        clientSkuMapper.deleteByMainId(id);
        invoiceEntityMapper.deleteByMainId(id);
        clientMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            clientSalespersonMapper.deleteByClientId(id.toString());
            shopMapper.deleteByMainId(id.toString());
            clientSkuMapper.deleteByMainId(id.toString());
            invoiceEntityMapper.deleteByMainId(id.toString());
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
    public Client buildInvoiceClient(String clientId, String invoiceEntityId) {
        Client client = clientMapper.getClientById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }
        InvoiceEntity invoiceEntity;
        if (invoiceEntityId != null && !invoiceEntityId.isBlank()) {
            invoiceEntity = invoiceEntityMapper.selectById(invoiceEntityId);
            if (invoiceEntity == null) {
                throw new IllegalArgumentException("Invoice entity not found: " + invoiceEntityId);
            }
            if (!clientId.equals(invoiceEntity.getClientId())) {
                throw new IllegalArgumentException("Invoice entity does not belong to client: " + invoiceEntityId);
            }
            log.info("[INVOICE_ENTITY] client={}, branch=EXPLICIT, resolvedEntity={}", clientId, invoiceEntityId);
        } else {
            invoiceEntity = resolveFallbackEntity(clientId);
        }
        if (invoiceEntity == null) {
            log.info("[INVOICE_ENTITY] client={}, branch=NO_ENTITY_FALLBACK_TO_CLIENT_FIELDS", clientId);
            return client;
        }
        return overlayInvoiceEntity(client, invoiceEntity);
    }

    /**
     * Picks an entity for a client when none was explicitly specified: the only one if
     * there's just one, the one flagged default if there are several, or an arbitrary
     * one if several exist but none is flagged default. Returns null only if the client
     * has no invoice entity at all, in which case callers fall back to the client's own
     * (legacy) fields.
     */
    private InvoiceEntity resolveFallbackEntity(String clientId) {
        List<InvoiceEntity> entities = invoiceEntityMapper.selectByMainId(clientId);
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        if (entities.size() == 1) {
            log.info("[INVOICE_ENTITY] client={}, branch=SINGLE_ENTITY, resolvedEntity={}", clientId, entities.get(0).getId());
            return entities.get(0);
        }
        for (InvoiceEntity entity : entities) {
            if ("1".equals(entity.getIsDefault())) {
                log.info("[INVOICE_ENTITY] client={}, branch=DEFAULT_ENTITY, resolvedEntity={}, totalEntities={}", clientId, entity.getId(), entities.size());
                return entity;
            }
        }
        log.info("[INVOICE_ENTITY] client={}, branch=ARBITRARY_PICK_NO_DEFAULT, resolvedEntity={}, totalEntities={}", clientId, entities.get(0).getId(), entities.size());
        return entities.get(0);
    }

    private Client overlayInvoiceEntity(Client client, InvoiceEntity invoiceEntity) {
        Client invoiceClient = new Client();
        BeanUtils.copyProperties(client, invoiceClient);
        invoiceClient.setInvoiceEntityId(invoiceEntity.getId());
        invoiceClient.setInvoiceEntity(invoiceEntity.getInvoiceEntity());
        invoiceClient.setEmail(invoiceEntity.getEmail());
        invoiceClient.setPhone(invoiceEntity.getPhone());
        invoiceClient.setStreetNumber(invoiceEntity.getStreetNumber());
        invoiceClient.setStreetName(invoiceEntity.getStreetName());
        invoiceClient.setAdditionalAddress(invoiceEntity.getAdditionalAddress());
        invoiceClient.setPostcode(invoiceEntity.getPostcode());
        invoiceClient.setCity(invoiceEntity.getCity());
        invoiceClient.setCountry(invoiceEntity.getCountry());
        invoiceClient.setCurrency(invoiceEntity.getCurrency());
        invoiceClient.setCompanyIdType(invoiceEntity.getCompanyIdType());
        invoiceClient.setCompanyIdValue(invoiceEntity.getCompanyIdValue());
        invoiceClient.setIossNumber(invoiceEntity.getIossNumber());
        invoiceClient.setVatPercentage(invoiceEntity.getVatPercentage());
        return invoiceClient;
    }

    /**
     * At most one invoice entity per client can be flagged as default; if the incoming
     * list has more than one, the last one wins and the rest are unset.
     */
    private void enforceSingleDefault(List<InvoiceEntity> invoiceEntityList) {
        InvoiceEntity lastDefault = null;
        for (InvoiceEntity entity : invoiceEntityList) {
            if ("1".equals(entity.getIsDefault())) {
                lastDefault = entity;
            }
        }
        if (lastDefault == null) {
            return;
        }
        for (InvoiceEntity entity : invoiceEntityList) {
            if (entity != lastDefault) {
                entity.setIsDefault("0");
            }
        }
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
        return clientSalespersonMapper.getActiveClientIdsBySalespersonId(salesId);
    }

    @Override
    @Transactional
    public void saveClientSalespersons(String clientId, List<String> salespersonIds) {
        clientSalespersonMapper.deleteByClientId(clientId);
        if (salespersonIds != null) {
            for (String userId : salespersonIds) {
                ClientSalesperson cs = new ClientSalesperson();
                cs.setClientId(clientId);
                cs.setSalespersonId(userId);
                clientSalespersonMapper.insert(cs);
            }
        }
    }
}
