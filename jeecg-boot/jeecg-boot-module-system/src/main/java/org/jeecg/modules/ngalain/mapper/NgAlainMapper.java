package org.jeecg.modules.ngalain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface NgAlainMapper extends BaseMapper {
    public List<Map<String,String>> getDictByTable(@Param("table") String table, @Param("key") String key, @Param("value") String value);

}
