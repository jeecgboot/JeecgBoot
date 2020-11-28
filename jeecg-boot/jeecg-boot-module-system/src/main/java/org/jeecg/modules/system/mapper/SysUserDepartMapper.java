package org.jeecg.modules.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{
	
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);
}
