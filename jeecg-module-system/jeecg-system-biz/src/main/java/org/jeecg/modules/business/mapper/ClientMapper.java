package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Client;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客户
 * @Author: jeecg-boot
 * @Date:   2021-04-02
 * @Version: V1.0
 */
@Repository
public interface ClientMapper extends BaseMapper<Client> {

    String getClientEntity(@Param("id") String id);
    Map<String, String> getClientsEntity(@Param("ids") List<String> ids);
    String getClientIdByCode(@Param("code") String code);
    String getActiveClientIdByCode(@Param("code") String code);
    List<Client> getClientByType(@Param("type") String type);
    Client getClientByCode(@Param("code") String internalCode);
    Client getClientById(@Param("id") String clientId);
    Client getClientFromOrder(@Param("orderId")String orderId);

    Client getClientFromPurchase(@Param("purchaseId") String purchaseId);

    List<String> getClientsFromPurchases(@Param("purchaseIds") List<String> purchaseIds);

    Client getClientBySku(@Param("skuId") String skuId);

    Client getClientFromInvoice(@Param("invoiceNumber") String invoiceNumber);

    void anonymizePersonalData(@Param("period") int directClientAnonymizationPeriod);

    Client getByShopId(@Param("shopId") String shopId);

    Client getClientFromCredit(@Param("invoiceNumber") String invoiceNumber);

    List<String> getClientsByCode(@Param("codes") List<String> clientCodes);
}
