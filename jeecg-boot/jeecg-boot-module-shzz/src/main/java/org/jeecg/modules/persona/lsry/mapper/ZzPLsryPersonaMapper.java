package org.jeecg.modules.persona.lsry.mapper;

import java.util.List;
import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 留守人员附表
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
public interface ZzPLsryPersonaMapper extends BaseMapper<ZzPLsryPersona> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ZzPLsryPersona> selectByMainId(@Param("mainId") String mainId);
}
