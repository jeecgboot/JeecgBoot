package org.jeecg.modules.business.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ExtraFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.ExtraFeeResult;
import org.springframework.stereotype.Repository;

/**
 * @Description: extra fees content
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Repository
public interface ExtraFeeMapper extends BaseMapper<ExtraFee> {

    List<ExtraFeeResult> listWithFilters(@Param("shop") String shop,
                                         @Param("status") String status,
                                         @Param("offset") int offset,
                                         @Param("size") Integer pageSize,
                                         @Param("column") String column,
                                         @Param("order") String order);
    Integer countAllFees(@Param("shop") String shop,
                         @Param("status") String status);

    void updateFee(@Param("id") String id, @Param("description") String description, @Param("qty") Integer quantity, @Param("price") BigDecimal unitPrice);
}

