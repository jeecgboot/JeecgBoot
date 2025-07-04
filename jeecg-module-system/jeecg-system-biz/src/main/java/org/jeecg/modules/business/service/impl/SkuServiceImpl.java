package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.SkuName;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuUpdate;
import org.jeecg.modules.business.vo.inventory.InventoryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.1
 */
@Slf4j
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuPriceMapper skuPriceMapper;
    @Autowired
    private ShippingDiscountMapper shippingDiscountMapper;
    @Autowired
    private SkuDeclaredValueMapper skuDeclaredValueMapper;
    @Autowired
    private IClientService clientService;
    @Autowired
    private PlatformOrderContentMapper platformOrderContentMapper;
    @Autowired
    private SkuMeasureMapper skuMeasureMapper;
    @Autowired
    private SkuProductNameMapper skuProductNameMapper;
    @Autowired
    private UserSkuMapper userSkuMapper;
    @Autowired
    private PlatformOrderMapper platformOrderMapper;
    @Autowired
    private ILogisticChannelPriceService logisticChannelPriceService;
    @Autowired
    private IPlatformOrderContentService platformOrderContentService;
    @Autowired
    private ISkuLogisticChoiceService skuLogisticChoiceService;
    @Autowired
    private LogisticChannelPriceMapper logisticChannelPriceMapper;
    @Autowired
    private LogisticChannelMapper logisticChannelMapper;
    @Autowired
    private ClientSkuMapper clientSkuMapper;

    @Override
    @Transactional
    public void saveMain(Sku sku, List<SkuPrice> skuPriceList, List<ShippingDiscount> shippingDiscountList, List<SkuDeclaredValue> skuDeclaredValueList) {
        skuMapper.insert(sku);
        if (skuPriceList != null && skuPriceList.size() > 0) {
            for (SkuPrice entity : skuPriceList) {
                //外键设置
                entity.setSkuId(sku.getId());
                skuPriceMapper.insert(entity);
            }
        }
        if (shippingDiscountList != null && shippingDiscountList.size() > 0) {
            for (ShippingDiscount entity : shippingDiscountList) {
                //外键设置
                entity.setSkuId(sku.getId());
                shippingDiscountMapper.insert(entity);
            }
        }
        if (skuDeclaredValueList != null && skuDeclaredValueList.size() > 0) {
            for (SkuDeclaredValue entity : skuDeclaredValueList) {
                //外键设置
                entity.setSkuId(sku.getId());
                skuDeclaredValueMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(Sku sku, List<SkuPrice> skuPriceList, List<ShippingDiscount> shippingDiscountList, List<SkuDeclaredValue> skuDeclaredValueList) {
        skuMapper.updateById(sku);

        //1.先删除子表数据
        skuPriceMapper.deleteByMainId(sku.getId());
        shippingDiscountMapper.deleteByMainId(sku.getId());
        skuDeclaredValueMapper.deleteByMainId(sku.getId());

        //2.子表数据重新插入
        if (skuPriceList != null && skuPriceList.size() > 0) {
            for (SkuPrice entity : skuPriceList) {
                //外键设置
                entity.setSkuId(sku.getId());
                skuPriceMapper.insert(entity);
            }
        }
        if (shippingDiscountList != null && shippingDiscountList.size() > 0) {
            for (ShippingDiscount entity : shippingDiscountList) {
                //外键设置
                entity.setSkuId(sku.getId());
                shippingDiscountMapper.insert(entity);
            }
        }
        if (skuDeclaredValueList != null && skuDeclaredValueList.size() > 0) {
            for (SkuDeclaredValue entity : skuDeclaredValueList) {
                //外键设置
                entity.setSkuId(sku.getId());
                skuDeclaredValueMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        skuPriceMapper.deleteByMainId(id);
        shippingDiscountMapper.deleteByMainId(id);
        skuDeclaredValueMapper.deleteByMainId(id);
        skuMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            skuPriceMapper.deleteByMainId(id.toString());
            shippingDiscountMapper.deleteByMainId(id.toString());
            skuDeclaredValueMapper.deleteByMainId(id.toString());
            skuMapper.deleteById(id);
        }
    }

    @Override
    public List<Sku> listSkus() {
        return skuMapper.listSkus();
    }

    @Override
    public void fillPageForCurrentClient(Page<InventoryRecord> page) {
        // search client id for current user
        Client client = clientService.getCurrentClient();
        // in case of other roles
        String clientId = null;
        if (null != client) {
            clientId = client.getId();
        }
        List<InventoryRecord> orders = skuMapper.pageSkuByClientId(clientId, page.offset(), page.getSize());
        page.setRecords(orders);
        page.setTotal(skuMapper.countTotal(clientId));

    }

    @Override
    public void addInventory(List<SkuQuantity> skuQuantities) {
        addInventory(skuQuantities, Collections.emptyList());
    }


    @Override
    public void addInventory(List<SkuQuantity> skuQuantities, List<String> platformOrderIDs) {
        Objects.requireNonNull(skuQuantities);
        Objects.requireNonNull(platformOrderIDs);
        Map<String, Integer> quantityPurchased = skuQuantities.stream()
                .collect(
                        Collectors.toMap(
                                SkuQuantity::getID,
                                SkuQuantity::getQuantity
                        )
                );
        // Add surplus of purchased quantity to SKU's "purchasing amount"
        if (!platformOrderIDs.isEmpty()) {
            List<SkuQuantity> used = platformOrderContentMapper.searchOrderContent(platformOrderIDs);
            for (SkuQuantity sq : used) {
                if(!quantityPurchased.containsKey(sq.getID())) {
                    break;
                }
                int quantity = quantityPurchased.get(sq.getID());
                quantityPurchased.put(sq.getID(), quantity - sq.getQuantity());
            }
        }

        skuMapper.addSkuQuantity(quantityPurchased);
    }

    @Override
    @Transactional
    public void batchUpdateSku(List<SkuUpdate> list) {
        skuMapper.batchUpdateStock(list);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<SkuDeclaredValue> newDeclaredValues = new ArrayList<>();
        Map<String, BigDecimal> latestDeclaredValues = skuDeclaredValueMapper.getLatestDeclaredValues().stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (SkuUpdate skuUpdate : list) {
            BigDecimal latestDeclaredValue = latestDeclaredValues.get(skuUpdate.getErpCode());
            if (latestDeclaredValue.compareTo(skuUpdate.getDeclaredValue()) != 0) {
                SkuDeclaredValue skuDeclaredValue = new SkuDeclaredValue();
                skuDeclaredValue.setCreateBy(sysUser.getId());
                skuDeclaredValue.setCreateTime(new Date());
                skuDeclaredValue.setSkuId(skuUpdate.getErpCode());
                skuDeclaredValue.setDeclaredValue(skuUpdate.getDeclaredValue());
                skuDeclaredValue.setEffectiveDate(skuUpdate.getEffectiveDate());
                newDeclaredValues.add(skuDeclaredValue);
            }
        }
        skuDeclaredValueMapper.insertNewDeclaredValues(newDeclaredValues);
    }

    @Override
    public List<SkuMeasure> measureSku(Collection<String> skuIds) {
        return skuMeasureMapper.selectBatchIds(skuIds);
    }

    @Override
    public List<SkuName> all() {
        return skuProductNameMapper.selectList(null);
    }

    @Override
    public List<UserSku> findSkuForCurrentUser() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        Map<String, Object> condition = new HashMap<>();
        condition.put("user_id", userId);
        return userSkuMapper.selectByMap(condition);
    }

    @Override
    public List<ClientSku> findSkuForUser(String userId) {
        return clientSkuMapper.selectByMainId(userId);
    }

    @Override
    public List<SkuChannelHistory> findHistoryBySkuId(String skuId) {
        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        for (SkuWeightDiscountServiceFees skuWeightDiscountServiceFees : platformOrderContentService.getAllSKUWeightsDiscountsServiceFees()) {
            if (skuWeightDiscountServiceFees.getWeight() != null) {
                skuRealWeights.put(skuWeightDiscountServiceFees.getSkuId(),
                        skuWeightDiscountServiceFees.getDiscount().multiply(BigDecimal.valueOf(skuWeightDiscountServiceFees.getWeight())));
            }
        }
        BigDecimal skuWeight = skuRealWeights.get(skuId);

        // Find all orders containing this sku
        List<PlatformOrder> all = platformOrderMapper.findBySku(skuId);

        // group by channel name
        Map<String, List<PlatformOrder>> channelNameToOrder = all.stream()
                .collect(
                        Collectors.groupingBy(
                                PlatformOrder::getLogisticChannelName
                        )
                );

        List<SkuChannelHistory> histories = new ArrayList<>();

        // For each channel
        for (Map.Entry<String, List<PlatformOrder>> entry : channelNameToOrder.entrySet()) {
            String channelName = entry.getKey();
            // Group by country
            Map<String, List<PlatformOrder>> countryEnNameToOrder = entry.getValue().stream().collect(Collectors.groupingBy(PlatformOrder::getCountry));

            // for each country
            for (Map.Entry<String, List<PlatformOrder>> countryEntry : countryEnNameToOrder.entrySet()) {
                String countryEnName = countryEntry.getKey();
                List<PlatformOrder> orders = countryEntry.getValue();
                if (orders.isEmpty()) {
                } else if (orders.size() == 1) {
                    PlatformOrder newestOrder = orders.get(0);
                    LogisticChannelPrice price = null;
                    try {
                        price = logisticChannelPriceService.findPriceForPlatformOrder(newestOrder);
                    } catch (UserException e) {
                        continue;
                    }
                    SkuPriceHistory now = new SkuPriceHistory(skuId, price.getEffectiveDate(), price.getRegistrationFee(), price.getCalUnitPrice().multiply(skuWeight));
                    SkuChannelHistory channelHistory = new SkuChannelHistory("", channelName, countryEnName, "", now, null);
                    histories.add(channelHistory);
                } else {
                    SkuPriceHistory newHistory;
                    PlatformOrder newOrder = orders.get(0);
                    LogisticChannelPrice newPrice;
                    try {
                        newPrice = logisticChannelPriceService.findPriceForPlatformOrder(newOrder);

                        newHistory = new SkuPriceHistory(skuId, newPrice.getEffectiveDate(), newPrice.getRegistrationFee(), newPrice.getCalUnitPrice().multiply(skuWeight));
                    } catch (UserException ignored) {
                        continue;
                    }

                    SkuPriceHistory oldHistory;
                    PlatformOrder oldOrder = orders.get(1);
                    LogisticChannelPrice oldPrice = null;
                    try {
                        oldPrice = logisticChannelPriceService.findPriceForPlatformOrder(oldOrder);
                        oldHistory = new SkuPriceHistory(skuId, oldPrice.getEffectiveDate(), oldPrice.getRegistrationFee(), oldPrice.getCalUnitPrice().multiply(skuWeight));
                    } catch (UserException e) {
                        oldHistory = null;
                    }
                    SkuChannelHistory channelHistory = new SkuChannelHistory("", channelName, countryEnName, "", newHistory, oldHistory);
                    histories.add(channelHistory);
                }
            }
        }
        return histories;
    }

    @Override
    public List<SkuChannelHistory> findHistoryBySkuIdsAndCountryCode(List<String> skuIds, String countryCode) {
        List<SkuChannelHistory> histories = new ArrayList<>();

        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        Map<String, String> skuErpCodes = new HashMap<>();
        for (SkuWeightDiscountServiceFees skuWeightDiscountServiceFees : platformOrderContentService.getAllSKUWeightsDiscountsServiceFees()) {
            if (skuWeightDiscountServiceFees.getWeight() != null) {
                skuRealWeights.put(skuWeightDiscountServiceFees.getSkuId(),
                        skuWeightDiscountServiceFees.getDiscount().multiply(BigDecimal.valueOf(skuWeightDiscountServiceFees.getWeight())));
                skuErpCodes.put(skuWeightDiscountServiceFees.getSkuId(), skuWeightDiscountServiceFees.getErpCode());
            }
        }

        for (String skuId : skuIds) {
            BigDecimal skuWeight = skuRealWeights.get(skuId);
            SkuLogisticChoice latestChoice = skuLogisticChoiceService.findLatestChoice(skuId, countryCode);
            if (latestChoice == null) {
                //ignore SKUs without logistic choices, probably no longer active
                continue;
            }
            Map<String, Object> condition = new HashMap<>();
            String channelId = latestChoice.getLogisticChannelId();
            LogisticChannel logisticChannel = logisticChannelMapper.selectById(channelId);
            condition.put("channel_id", channelId);
            List<LogisticChannelPrice> logisticChannelPrices = logisticChannelPriceMapper.selectByMap(condition);
            List<Date> priceChangeDates = logisticChannelPrices.stream()
                    .map(LogisticChannelPrice::getEffectiveDate)
                    .sorted(Comparator.reverseOrder())
                    .distinct()
                    .collect(Collectors.toList());

            assert priceChangeDates.size() > 1;
            Date latestDate = priceChangeDates.get(0);
            Date secondLatestDate = priceChangeDates.get(1);

            LogisticChannelPrice latestPrice = null;
            LogisticChannelPrice secondLatestPrice = null;
            try {
                latestPrice = logisticChannelPriceService.findPriceOfSkuByCountryAndChannelAndDate(skuId, countryCode, channelId, latestDate);
                secondLatestPrice = logisticChannelPriceService.findPriceOfSkuByCountryAndChannelAndDate(skuId, countryCode, channelId, secondLatestDate);
            } catch (UserException e) {
                log.error("Could not find price", e);
            }

            if (latestPrice == null) {
                continue;
            }
            assert secondLatestPrice != null;
            SkuPriceHistory now = new SkuPriceHistory(skuId, latestPrice.getEffectiveDate(), latestPrice.getRegistrationFee(),
                    latestPrice.getCalUnitPrice().multiply(skuWeight).setScale(2, RoundingMode.UP));
            SkuPriceHistory then = new SkuPriceHistory(skuId, secondLatestPrice.getEffectiveDate(), secondLatestPrice.getRegistrationFee(),
                    secondLatestPrice.getCalUnitPrice().multiply(skuWeight).setScale(2, RoundingMode.UP));
            SkuChannelHistory channelHistory = new SkuChannelHistory(logisticChannel.getEnName(), "", countryCode, skuErpCodes.get(skuId), now, then);
            histories.add(channelHistory);
        }
        return histories;
    }

    @Override
    public List<Sku> selectByErpCode(Collection<String> erpCodes) {
        return skuMapper.selectByErpCode(erpCodes);
    }

    @Override
    public String searchFirstMissingPriceSku(List<String> skuIds) {
        return skuMapper.searchFirstMissingPriceSku(skuIds);
    }

    @Override
    public List<String> listErpCodesByIds(List<String> skuIds) {
        return skuMapper.listErpCodesByIds(skuIds);
    }
    @Override
    public List<Sku> findMissingSkusInNotShippedOrders(LocalDateTime start) {
        return skuMapper.findMissingSkusInNotShippedOrders(start);
    }

    @Override
    public List<SkuQuantity> getSkuQuantitiesFromOrderIds(List<String> orderIds) {
        return skuMapper.getSkuQuantitiesFromOrderIds(orderIds);
    }
    @Override
    public Integer countAllSkus() {
        return skuMapper.countAllSkus();
    }
    @Override
    public List<SkuOrderPage> fetchSkuWeights(Integer pageNo, Integer pageSize, String column, String order) {
        int offset = (pageNo - 1) * pageSize;
        return skuMapper.fetchSkuWeights(offset, pageSize, column, order);
    }

    @Override
    public Integer countAllClientSkus(String clientId) {
        return skuMapper.countAllClientSkus(clientId);
    }
    @Override
    public List<SkuOrderPage> fetchSkusByClient(String clientId, Integer pageNo, Integer pageSize, String column, String order) {
        int offset = (pageNo - 1) * pageSize;
        return skuMapper.fetchSkusByClient(clientId, offset, pageSize, column, order);
    }
    @Override
    public List<SkuOrderPage> listSkuOrdersByClient(String clientID){
        return skuMapper.listSkuOrdersByClient(clientID);
    }
    @Override
    public Integer countAllSkuWeightsWithFilters(List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        StringBuilder erpCodesRegex= new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();
        if(erpCodes != null){
            erpCodesRegex.append("^");
            for(String name : erpCodes){
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }
        if(enNames != null){
            enNamesRegex.append("^");
            for(String name : enNames){
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }
        if(zhNames != null){
            zhNamesRegex.append("^");
            for(String name : zhNames){
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }
        return skuMapper.countAllSkuWeightsWithFilters(erpCodesRegex.toString(), zhNamesRegex.toString(), enNamesRegex.toString());
    }
    @Override
    public Integer countAllSkuWeightHistoryWithFilters(List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        StringBuilder erpCodesRegex = new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();

        if (erpCodes != null) {
            erpCodesRegex.append("^");
            for (String name : erpCodes) {
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }

        if (enNames != null) {
            enNamesRegex.append("^");
            for (String name : enNames) {
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }

        if (zhNames != null) {
            zhNamesRegex.append("^");
            for (String name : zhNames) {
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }

        return skuMapper.countAllSkuWeightHistoryWithFilters(
                erpCodesRegex.toString(),
                zhNamesRegex.toString(),
                enNamesRegex.toString()
        );
    }
    @Override
    public List<SkuOrderPage> fetchSkuWeightsWithFilters(Integer pageNo, Integer pageSize, String column, String order, List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        int offset = (pageNo - 1) * pageSize;
        StringBuilder erpCodesRegex= new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();
        if(erpCodes != null){
            erpCodesRegex.append("^");
            for(String name : erpCodes){
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }
        if(enNames != null){
            enNamesRegex.append("^");
            for(String name : enNames){
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }
        if(zhNames != null){
            zhNamesRegex.append("^");
            for(String name : zhNames){
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }
        return skuMapper.fetchSkuWeightsWithFilters(offset, pageSize, column, order, erpCodesRegex.toString(), zhNamesRegex.toString(), enNamesRegex.toString());
    }

    @Override
    public List<SkuOrderPage> fetchAllSkuWeightsWithFilters(Integer pageNo, Integer pageSize, String column, String order, List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        int offset = (pageNo - 1) * pageSize;
        StringBuilder erpCodesRegex= new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();
        if (erpCodes != null) {
            erpCodesRegex.append("^");
            for (String name : erpCodes) {
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }
        if (enNames != null) {
            enNamesRegex.append("^");
            for (String name : enNames) {
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }
        if (zhNames != null) {
            zhNamesRegex.append("^");
            for (String name : zhNames) {
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }

        return skuMapper.fetchAllSkuWeightsWithFilters(
                offset,
                pageSize,
                column,
                order,
                erpCodesRegex.toString(),
                zhNamesRegex.toString(),
                enNamesRegex.toString()
        );
    }




    @Override
    public Integer countAllClientSkusWithFilters(String clientId, List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        StringBuilder erpCodesRegex= new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();
        if(erpCodes != null){
            erpCodesRegex.append("^");
            for(String name : erpCodes){
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }
        if(enNames != null){
            enNamesRegex.append("^");
            for(String name : enNames){
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }
        if(zhNames != null){
            zhNamesRegex.append("^");
            for(String name : zhNames){
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }
        return skuMapper.countAllClientSkusWithFilters(clientId, erpCodesRegex.toString(), zhNamesRegex.toString(), enNamesRegex.toString());
    }

    @Override
    public List<SkuOrderPage> fetchSkusByClientWithFilters(String clientId, Integer pageNo, Integer pageSize, String column, String order, List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        int offset = (pageNo - 1) * pageSize;
        StringBuilder erpCodesRegex= new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();
        if(erpCodes != null){
            erpCodesRegex.append("^");
            for(String name : erpCodes){
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }
        if(enNames != null){
            enNamesRegex.append("^");
            for(String name : enNames){
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }
        if(zhNames != null){
            zhNamesRegex.append("^");
            for(String name : zhNames){
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }
        return skuMapper.fetchSkusByClientWithFilters(clientId, offset, pageSize, column, order, erpCodesRegex.toString(), zhNamesRegex.toString(), enNamesRegex.toString());
    }

    @Override
    public List<SkuOrderPage> listSelectableSkuIdsWithFilters(String clientId, List<String> erpCodes, List<String> zhNames, List<String> enNames) {
        StringBuilder erpCodesRegex= new StringBuilder(), zhNamesRegex = new StringBuilder(), enNamesRegex = new StringBuilder();
        if(erpCodes != null){
            erpCodesRegex.append("^");
            for(String name : erpCodes){
                erpCodesRegex.append("(?=.*").append(name).append(")");
            }
            erpCodesRegex.append(".*");
        }
        if(enNames != null){
            enNamesRegex.append("^");
            for(String name : enNames){
                enNamesRegex.append("(?=.*").append(name).append(")");
            }
            enNamesRegex.append(".*");
        }
        if(zhNames != null){
            zhNamesRegex.append("^");
            for(String name : zhNames){
                zhNamesRegex.append("(?=.*").append(name).append(")");
            }
            zhNamesRegex.append(".*$");
        }
        return skuMapper.listSelectableSkuIdsWithFilters(clientId, erpCodesRegex.toString(), zhNamesRegex.toString(), enNamesRegex.toString());
    }

    @Override
    public List<SkuOrderPage> listSelectableSkuIds(String clientId) {
        return skuMapper.listSelectableSkuIds(clientId);
    }

    @Override
    public void addSkuQuantity(Map<String, Integer> quantityPurchased) {
        skuMapper.addSkuQuantity(quantityPurchased);
    }

    @Override
    public String getIdFromErpCode(String erpCode) {
        return skuMapper.getIdFromErpCode(erpCode);
    }

    @Override
    public Sku getByErpCode(String erpCode) {
        return skuMapper.getByErpCode(erpCode);
    }

    @Override
    public void updateBatchStockByIds(List<Sku> skuToUpdate) {
        skuMapper.updateBatchStockByIds(skuToUpdate);
    }

    @Override
    public List<SkuOrderPage> getInventoryByInvoiceNumber(String invoiceNumber) {
        return skuMapper.getInventoryByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<SkuOrderPage> getInventory(List<String> erpCodes, String invoiceNumber) {
        return skuMapper.getInventory(erpCodes, invoiceNumber);
    }

    @Override
    public Map<String, Sku> listInUninvoicedOrders(String clientId, List<String> erpStatuses) {
        return skuMapper.listInUninvoicedOrders(clientId, erpStatuses);
    }

    @Override
    public List<SkuOrderPage> searchExistingSkuByKeywords(List<String> keywords) {
        return skuMapper.searchExistingSkuByKeywords(keywords);
    }

    @Override
    public List<Sku> listImgUrls() {
        return skuMapper.listImgUrls();
    }

    @Override
    public List<String> fetchAllClientSkuCodes(String clientCode) {
        return skuMapper.fetchAllClientSkuCodes(clientCode);
    }

    @Override
    public List<UnpairedSku> fetchUnpairedSkus(String shopId) {
        return skuMapper.fetchUnpairedSkus(shopId);
    }

    @Override
    public int latestSkuCounter(String userCode, String clientCode, String date) {
        List<String> skus = skuMapper.latestSkuCounter(userCode, clientCode, date);
        List<Integer> counters = skus.stream().map(sku -> {
            String counter = sku.split("-")[0].substring(8 + userCode.length());
            return Integer.parseInt(counter);
        }).collect(Collectors.toList());
        return counters.stream().max(Integer::compareTo).orElse(0) + 1;
    }

    @Override
    public void setIsSynced(List<String> erpCodes, boolean isSynced) {
        skuMapper.setIsSynced(erpCodes, isSynced);
    }

    @Override
    public Result<?> parseExcelToSkuList(String clientId,MultipartFile file) {
        List<Map<String, Object>> validSkuList = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int headerRowIndex = -1;
            int skuCol = -1, qtyCol = -1;
            Map<String, Integer> colMap = new HashMap<>();
            // find header row with required columns
            for (int i = 0; i <= 5; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null || cell.getCellType() != CellType.STRING) continue;
                    String val = cell.getStringCellValue().trim().toLowerCase();
                    if (val.equals("sku")) skuCol = j;
                    else if (val.contains("quantité") || val.contains("quantity")) qtyCol = j;
                }
                if (skuCol != -1 && qtyCol != -1) {
                    headerRowIndex = i;
                    break;
                }
            }
            if (headerRowIndex == -1) {
                throw new RuntimeException("Missing required columns: SKU and Quantity");
            }
            List<String> erpCodes = new ArrayList<>();
            Map<String, Integer> skuQuantityMap = new HashMap<>();
            // parse data rows
            for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell skuCell = row.getCell(skuCol);
                Cell qtyCell = row.getCell(qtyCol);
                if (skuCell == null || qtyCell == null) continue;

                String erpCode = "";
                if (skuCell.getCellType() == CellType.NUMERIC) {
                    erpCode = new BigDecimal(skuCell.getNumericCellValue()).toPlainString().trim();
                } else {
                    erpCode = skuCell.toString().trim();
                }
                Integer quantity = null;
                try {
                    if (qtyCell.getCellType() == CellType.NUMERIC) {
                        quantity = (int) qtyCell.getNumericCellValue();
                    } else {
                        quantity = Integer.parseInt(qtyCell.toString().trim());
                    }
                } catch (NumberFormatException ignored) {}

                if (!erpCode.isEmpty() && quantity != null && quantity > 0) {
                    erpCodes.add(erpCode);
                    skuQuantityMap.put(erpCode, quantity);
                }
            }
            if (erpCodes.isEmpty()) {
                return Result.error("No valid SKU and quantity found in the Excel file.");
            }
            //extract SKU details from the database
            String erpRegex = String.join("|", erpCodes);
            List<SkuOrderPage> skuPages = listSelectableSkuIdsWithFilters(clientId, Collections.singletonList(erpRegex), null, null);
            // Check for SKUs not found in DB
            Set<String> fetchedErpCodes = skuPages.stream()
                    .map(SkuOrderPage::getErpCode)
                    .collect(Collectors.toSet());
            erpCodes.stream()
                    .filter(code -> !fetchedErpCodes.contains(code))
                    .forEach(code -> warnings.add("SKU " + code + " is invalid: it may not exist, not belong to the client, or have missing/zero price."
                    ));

            for (SkuOrderPage sku : skuPages) {
                Integer quantity = skuQuantityMap.get(sku.getErpCode());
                if (quantity == null) continue;
                Map<String, Object> skuData = new HashMap<>();
                skuData.put("skuId", sku.getId());
                skuData.put("erpCode", sku.getErpCode());
                skuData.put("enName", sku.getEnName());
                skuData.put("zhName", sku.getZhName());
                skuData.put("stock", sku.getStock());
                skuData.put("skuPrice", sku.getSkuPrice());
                skuData.put("sales7d", sku.getSalesLastWeek());
                skuData.put("sales28d", sku.getSalesFourWeeks());
                skuData.put("sales42d", sku.getSalesSixWeeks());
                skuData.put("quantity", quantity);
                validSkuList.add(skuData);
            }
            log.info("Parsed and fetched SKU details: {}, warnings: {}",validSkuList, warnings.size());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("validSkuList", validSkuList);
            responseData.put("warnings", warnings);
            return Result.OK(responseData);
        } catch (Exception e) {
            log.error("Excel parsing failed", e);
            throw new RuntimeException("Excel parsing error: " + e.getMessage(), e);
        }
    }
}