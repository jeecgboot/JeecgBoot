package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.mapper.PlatformOrderMapper;
import org.jeecg.modules.business.mapper.PurchaseOrderMapper;
import org.jeecg.modules.business.mapper.ShippingInvoiceMapper;
import org.jeecg.modules.business.service.DashboardService;
import org.jeecg.modules.business.service.IParcelStatusService;
import org.jeecg.modules.business.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private ShippingInvoiceMapper shippingInvoiceMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private PlatformOrderMapper platformOrderMapper;
    @Override
    public Map<String,?> getKpis(LocalDateTime start, LocalDateTime end, List<String> roles, String username) {
        boolean showAllData = roles.contains("admin") || roles.contains("dev");
        LocalDateTime startLag = start.minusYears(1);
        LocalDateTime endLag = end.minusYears(1);

        InvoiceKpi shippingInvoices = shippingInvoiceMapper.countShippingInvoices(start, end, showAllData, username);
        InvoiceKpi purchaseInvoices = purchaseOrderMapper.countPurchaseInvoices(start, end, showAllData, username);
        OrderKpi platformOrders = platformOrderMapper.countPlatformOrders(start, end, showAllData, username);
        InvoiceKpi shippingInvoicesLag = shippingInvoiceMapper.countShippingInvoices(startLag, endLag, showAllData, username);
        InvoiceKpi purchaseInvoicesLag = purchaseOrderMapper.countPurchaseInvoices(startLag, endLag, showAllData, username);
        OrderKpi platformOrdersLag = platformOrderMapper.countPlatformOrders(startLag, endLag, showAllData, username);

        BigDecimal shipTotal = BigDecimal.valueOf(shippingInvoices.getTotal().doubleValue());
        BigDecimal shipTotalLag = BigDecimal.valueOf(shippingInvoicesLag.getTotal().doubleValue());
        BigDecimal purchaseTotal = BigDecimal.valueOf(purchaseInvoices.getTotal().doubleValue());
        BigDecimal purchaseTotalLag = BigDecimal.valueOf(purchaseInvoicesLag.getTotal().doubleValue());

        BigDecimal growthShippingInvoicesTotal = BigDecimal.ZERO;
        BigDecimal growthShippingInvoicesQty = BigDecimal.ZERO;
        BigDecimal growthPurchaseInvoicesTotal = BigDecimal.ZERO;
        BigDecimal growthPurchaseInvoicesQty = BigDecimal.ZERO;
        BigDecimal growthPlatformOrders = BigDecimal.ZERO;

        if (shipTotalLag.compareTo(BigDecimal.ZERO) > 0)
            growthShippingInvoicesTotal = shipTotal.subtract(shipTotalLag).divide(shipTotalLag, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        if (shippingInvoicesLag.getQty() > 0)
            growthShippingInvoicesQty = BigDecimal.valueOf(shippingInvoices.getQty()).subtract(BigDecimal.valueOf(shippingInvoicesLag.getQty())).divide(BigDecimal.valueOf(shippingInvoicesLag.getQty()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        if (purchaseTotalLag.compareTo(BigDecimal.ZERO) > 0)
            growthPurchaseInvoicesTotal = purchaseTotal.subtract(purchaseTotalLag).divide(purchaseTotalLag, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        if (purchaseInvoicesLag.getQty() > 0)
            growthPurchaseInvoicesQty = BigDecimal.valueOf(purchaseInvoices.getQty()).subtract(BigDecimal.valueOf(purchaseInvoicesLag.getQty())).divide(BigDecimal.valueOf(purchaseInvoicesLag.getQty()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        if (platformOrdersLag.getProcessed() > 0)
            growthPlatformOrders = BigDecimal.valueOf(platformOrders.getProcessed()).subtract(BigDecimal.valueOf(platformOrdersLag.getProcessed())).divide(BigDecimal.valueOf(platformOrdersLag.getProcessed()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

        shippingInvoices.setGrowthTotal(growthShippingInvoicesTotal);
        shippingInvoices.setGrowthQty(growthShippingInvoicesQty);
        purchaseInvoices.setGrowthTotal(growthPurchaseInvoicesTotal);
        purchaseInvoices.setGrowthQty(growthPurchaseInvoicesQty);
        platformOrders.setGrowth(growthPlatformOrders);

        Map<String, Kpi> kpis = new HashMap<>();
        kpis.put("shippingInvoices", shippingInvoices);
        kpis.put("purchaseInvoices", purchaseInvoices);
        kpis.put("platformOrders", platformOrders);
        return kpis;
    }

    @Override
    public Map<String, ParcelQtyAndInfos> getPackageStatuses(int period) {
        List<ParcelInfos> packageStatusesAndTracking = shippingInvoiceMapper.getPackageStatusInPeriod(period);
        System.out.println("got package statuses");
        System.out.println(packageStatusesAndTracking);
        return packageStatusesAndTracking.stream().collect(
                groupingBy(ParcelInfos::getStatus, mapping(Function.identity(), toList()))).entrySet().stream().collect(
                toMap(Map.Entry::getKey, e -> {
                    ParcelQtyAndInfos parcelQtyAndInfos = new ParcelQtyAndInfos();
                    parcelQtyAndInfos.setQuantity(e.getValue().size());
                    parcelQtyAndInfos.setParcelInfos(e.getValue());
                    return parcelQtyAndInfos;
                }));
    }
}
