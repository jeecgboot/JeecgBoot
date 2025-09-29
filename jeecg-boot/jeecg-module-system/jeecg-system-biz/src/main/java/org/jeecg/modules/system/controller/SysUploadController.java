package org.jeecg.modules.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.MinioUtil;
import org.jeecg.common.util.filter.SsrfFileTypeFilter;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.oss.entity.OssFile;
import org.jeecg.modules.oss.service.IOssFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

/**
 * minio文件上传示例
 * @author: jeecg-boot
 */
@Slf4j
@RestController
@RequestMapping("/sys/upload")
public class SysUploadController {
    @Autowired
    private IOssFileService ossFileService;

    /**
     * 上传
     * @param request
     */
    @PostMapping(value = "/uploadMinio")
    public Result<?> uploadMinio(HttpServletRequest request) throws Exception {
        Result<?> result = new Result<>();
        // 获取业务路径
        String bizPath = request.getParameter("biz");
        // 获取上传文件对象
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        
        // 文件安全校验，防止上传漏洞文件
        SsrfFileTypeFilter.checkUploadFileType(file, bizPath);

        if(oConvertUtils.isEmpty(bizPath)){
            bizPath = "";
        }
        // 获取文件名
        String orgName = file.getOriginalFilename();
        orgName = CommonUtils.getFileName(orgName);
        String fileUrl =  MinioUtil.upload(file,bizPath);
        if(oConvertUtils.isEmpty(fileUrl)){
            return Result.error("上传失败,请检查配置信息是否正确!");
        }
        //保存文件信息
        OssFile minioFile = new OssFile();
        minioFile.setFileName(orgName);
        minioFile.setUrl(fileUrl);
        ossFileService.save(minioFile);
        result.setMessage(fileUrl);
        result.setSuccess(true);
        return result;
    }
}
