package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Balance;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.BalanceData;
import org.jeecg.modules.business.vo.InvoiceMetaData;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
public interface IBalanceService extends IService<Balance> {

    BigDecimal getBalanceByClientIdAndCurrency(String clientId, String currency);

    void initBalance(String clientId);

    void updateBalance(String clientId, String invoiceCode, String invoiceType);
    void updateBalance(String clientId, String CreditId, BigDecimal amount, String currencyId);

    /**
     * Delete balance record
     * @param operationId operation id : invoice id or credit id
     * @param operationType operation type : invoice or credit
     */
    void deleteBalance(String operationId, String operationType);

    void deleteBalanceByClientId(String clientId);

    /**
     * Edit balance record
     * @param operationId operation id : invoice id or credit id
     * @param operationType operation type : invoice or credit
     * @param clientId
     * @param amount
     * @param currencyId
     */
    void editBalance(String operationId, String operationType, String clientId, BigDecimal amount, String currencyId) throws Exception;

    void deleteBatchBalance(List<String> operationIds, String operationType);

    /**
     * Get low balance clients from client list
     * @param metaDataList list of meta data
     * @return
     */
    List<BalanceData> getLowBalanceClients(List<InvoiceMetaData> metaDataList);

    void cancelBalance(String invoiceId, String originalOperationType, String operationType, BigDecimal amount, String currencyId, String clientId);
}