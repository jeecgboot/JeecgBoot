package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Quotation;
import java.util.List;

public interface QuotationMapper extends BaseMapper<Quotation> {

    IPage<Quotation> pageByStatus(Page<Quotation> page,
                                  @Param("q") Quotation q);

    Quotation getByIdAndStatus(@Param("id") String id,
                               @Param("status") String status);

    int updateInquiryFields(@Param("q") Quotation q);

    int addQuoteBasedOnInquiry(@Param("q") Quotation q);

    int updateQuoteFields(@Param("q") Quotation q);

    int revokeQuoteById(@Param("id") String id);

    int revokeQuoteBatch(@Param("ids") List<String> ids);
}
