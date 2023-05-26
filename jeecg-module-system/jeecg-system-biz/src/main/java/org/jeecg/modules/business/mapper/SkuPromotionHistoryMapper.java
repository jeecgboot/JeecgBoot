package org.jeecg.modules.business.mapper;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.business.entity.Promotion;
import org.jeecg.modules.business.entity.SkuPromotionHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.vo.PromotionCouple;
import org.jeecg.modules.business.vo.PromotionDetail;
import org.jeecg.modules.business.vo.PromotionHistoryEntry;
import org.springframework.stereotype.Repository;

/**
 * @Description: SKU采购折扣历史
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@Repository
public interface SkuPromotionHistoryMapper extends BaseMapper<SkuPromotionHistory> {

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<PromotionCouple> selectByMainId(@Param("mainId") String mainId);


    void addAll(@Param("creator") String creator,
                @Param("entries") List<PromotionHistoryEntry> entries,
                @Param("purchaseID") String purchaseID);

    List<PromotionDetail> selectPromotionByPurchase(String purchaseID);
}
