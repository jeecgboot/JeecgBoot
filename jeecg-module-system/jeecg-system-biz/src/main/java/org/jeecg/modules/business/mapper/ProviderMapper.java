package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Provider;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: provider
 * @Author: jeecg-boot
 * @Date:   2024-02-14
 * @Version: V1.0
 */

@Repository
public interface ProviderMapper extends BaseMapper<Provider> {

    List<Provider> listByMabangIds(@Param("ids") List<String> ids);
}
