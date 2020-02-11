package org.jeecg.modules.shza.mafk.mapper;

import java.util.List;
import org.jeecg.modules.shza.mafk.entity.ZzMafkPerson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 受害人
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface ZzMafkPersonMapper extends BaseMapper<ZzMafkPerson> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ZzMafkPerson> selectByMainId(@Param("mainId") String mainId, @Param("type") int type);
}
