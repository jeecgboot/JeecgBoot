package org.jeecg.modules.persona.hjrk.mapper;

import java.util.List;
import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 综治人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
public interface ZzPersonaMapper extends BaseMapper<ZzPersona> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ZzPersona> selectByMainId(@Param("mainId") String mainId);
}
