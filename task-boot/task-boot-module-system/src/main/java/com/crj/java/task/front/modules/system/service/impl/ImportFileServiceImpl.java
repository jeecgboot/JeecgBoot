package com.crj.java.task.front.modules.system.service.impl;

import com.crj.java.task.front.common.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecgframework.poi.excel.imports.base.ImportFileServiceI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * excel导入 实现类
 */
@Slf4j
@Service
public class ImportFileServiceImpl implements ImportFileServiceI {

    @Value("${task.path.upload}")
    private String upLoadPath;

    @Value(value="${task.uploadType}")
    private String uploadType;

    @Override
    public String doUpload(byte[] data) {
        return CommonUtils.uploadOnlineImage(data, upLoadPath, "import", uploadType);
    }
}
