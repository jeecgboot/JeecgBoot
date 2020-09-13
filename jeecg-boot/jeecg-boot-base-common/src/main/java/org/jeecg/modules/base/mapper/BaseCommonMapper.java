package org.jeecg.modules.base.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.dto.LogDTO;

public interface BaseCommonMapper {

    /**
     * 保存日志
     * @param dto
     */
    @SqlParser(filter=true)
    void saveLog(@Param("dto")LogDTO dto);

}
