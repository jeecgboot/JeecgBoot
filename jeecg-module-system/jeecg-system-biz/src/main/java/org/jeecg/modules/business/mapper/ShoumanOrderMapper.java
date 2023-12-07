package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 首曼订单
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
@Repository
public interface ShoumanOrderMapper extends BaseMapper<ShoumanOrder> {

    List<ShoumanOrder> findShoumanOrderToSend();
}
