package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Credit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.CreditPage;
import org.springframework.stereotype.Repository;

/**
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Repository
public interface CreditMapper extends BaseMapper<Credit> {
    Credit getLastCredit(@Param("clientId") String clientId, @Param("currencyId") String currencyId);

    String getLatestInvoiceNumber();

    Credit getByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    void updateButAmount(@Param("credit") Credit credit, @Param("username") String username);
    Integer countAllWithFilters(@Param("clientId") String clientId, @Param("invoiceNumber") String invoiceNumber, @Param("currencyId") String currencyId);
    List<CreditPage> listWithFilters(@Param("clientId") String clientId, @Param("invoiceNumber") String invoiceNumber, @Param("currencyId") String currencyId, @Param("offset") int offset, @Param("limit") int limit);

    void cancelCredit(@Param("id") String id, @Param("username") String username);
}
