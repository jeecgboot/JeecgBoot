package org.jeecg.modules.test.seata.mapper;

/**
 * @Description: TODO
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.test.seata.entity.SeataOrder;

@Mapper
public interface SeataOrderMapper extends BaseMapper<SeataOrder> {

}