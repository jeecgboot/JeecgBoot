package org.jeecg.modules.test.seata.order.mapper;

/**
 * @Description: TODO
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.test.seata.order.entity.SeataOrder;

@Mapper
public interface SeataOrderMapper extends BaseMapper<SeataOrder> {

}