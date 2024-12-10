package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ExtraFee;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.ExtraFeeParam;
import org.jeecg.modules.business.vo.ExtraFeeResult;

import java.util.List;

/**
 * @Description: extra fees content
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
public interface IExtraFeeService extends IService<ExtraFee> {

    List<ExtraFeeResult> listWithFilters(String shop, String status, Integer pageNo, Integer pageSize, String column, String order);

    Integer countAllFees(String shop, String status);

    void updateFee(ExtraFeeParam feeParam) throws Exception;

    List<ExtraFeeResult> findNotInvoicedByShops(List<String> shopIds);

    void updateInvoiceNumberByIds(List<String> feeIds, String invoiceCode);

    List<ExtraFeeResult> findByInvoiceNumber(String invoiceCode);

    void cancelInvoice(String invoiceNumber, String clientId);
}
