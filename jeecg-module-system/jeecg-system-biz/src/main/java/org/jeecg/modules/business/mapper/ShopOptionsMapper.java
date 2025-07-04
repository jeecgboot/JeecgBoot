package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ShopOptions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.entity.ShopWithOptions;
import org.springframework.stereotype.Repository;

/**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
@Repository
public interface ShopOptionsMapper extends BaseMapper<ShopOptions> {
    List<ShopOptions> getByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    List<ShopWithOptions> listWithFilters(@Param("offset") int offset, @Param("limit") Integer pageSize, @Param("shopIds") List<String> shopIds, @Param("clientId") String clientId, @Param("showAll") Boolean showAll, @Param("hasOptions") List<Integer> hasOptions, @Param("order") String order);

    int countWithFilters(@Param("shopIds") List<String> shopIds, @Param("clientId")String clientId, @Param("showAll")Boolean showAll, @Param("hasOptions")List<Integer> hasOptions);
}
