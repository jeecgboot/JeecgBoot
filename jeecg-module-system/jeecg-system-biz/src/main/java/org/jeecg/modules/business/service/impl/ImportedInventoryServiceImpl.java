package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.ImportedInventory;
import org.jeecg.modules.business.mapper.ImportedInventoryMapper;
import org.jeecg.modules.business.service.IImportedInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 导入采购清单
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Service
@Slf4j
public class ImportedInventoryServiceImpl extends ServiceImpl<ImportedInventoryMapper, ImportedInventory> implements IImportedInventoryService {

    @Autowired
    private final ImportedInventoryMapper importedInventoryMapper;

    public ImportedInventoryServiceImpl(ImportedInventoryMapper importedInventoryMapper) {
        this.importedInventoryMapper = importedInventoryMapper;
    }

    @Override
    public List<ImportedInventory> selectByClientId(String clientId) {
        return importedInventoryMapper.selectByClientId(clientId);
    }
}
