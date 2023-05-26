package org.jeecg.modules.order.mapper;

import java.util.List;
import org.jeecg.modules.order.entity.ResponseOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: Response Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
public interface ResponseOrderMapper extends BaseMapper<ResponseOrder> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ResponseOrder> selectByMainId(@Param("mainId") String mainId);
}
