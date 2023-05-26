package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.entity.ImportedInventory;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 导入采购清单
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Repository
public interface ImportedInventoryMapper extends BaseMapper<ImportedInventory> {

    List<ImportedInventory> selectByClientId(@Param("clientId") String clientId);
}
