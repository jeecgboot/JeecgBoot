package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.vo.FactureDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureDetailMapper extends BaseMapper<FactureDetail> {

    List<FactureDetail> selectByShopsAndPeriod(@Param("shopIds") List<String> shopIds, @Param("start") String startDate, @Param("end") String endDate, @Param("type") String type);
}
