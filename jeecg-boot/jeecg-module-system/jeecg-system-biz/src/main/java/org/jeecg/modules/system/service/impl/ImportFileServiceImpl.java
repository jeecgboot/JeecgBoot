package org.jeecg.modules.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.CommonUtils;
import org.jeecgframework.poi.excel.imports.base.ImportFileServiceI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * excel导入 实现类
 * @author: jeecg-boot
 */
@Slf4j
@Service
public class ImportFileServiceImpl implements ImportFileServiceI {

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @Value(value="${jeecg.uploadType}")
    private String uploadType;

    @Override
    public String doUpload(byte[] data) {
        return CommonUtils.uploadOnlineImage(data, upLoadPath, "import", uploadType);
    }

    @Override
    public String doUpload(byte[] data, String saveUrl) {
        //update-begin---author:chenrui ---date:20250114  for：[QQYUN-10902]AutoPoi Excel表格导入有问题，还会报个错。 #7703------------
        String bizPath = "import";
        if(null != saveUrl && !saveUrl.isEmpty()){
            bizPath = saveUrl;
        }
        return CommonUtils.uploadOnlineImage(data, upLoadPath, bizPath, uploadType);
        //update-end---author:chenrui ---date:20250114  for：[QQYUN-10902]AutoPoi Excel表格导入有问题，还会报个错。 #7703------------
    }
}
