package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.ImportedInventory;

import java.util.List;

/**
 * @Description: 导入采购清单
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface IImportedInventoryService extends IService<ImportedInventory> {

    List<ImportedInventory> selectByClientId(String clientId);
}
