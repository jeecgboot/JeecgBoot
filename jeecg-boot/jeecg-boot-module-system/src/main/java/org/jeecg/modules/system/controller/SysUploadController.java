package org.jeecg.modules.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.MinioUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.oss.entity.OSSFile;
import org.jeecg.modules.oss.service.IOSSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * minio文件上传示例
 */
@Slf4j
@RestController
@RequestMapping("/sys/upload")
public class SysUploadController {
    @Autowired
    private IOSSFileService ossFileService;

    /**
     * 上传
     * @param request
     */
    @PostMapping(value = "/uploadMinio")
    public Result<?> uploadMinio(HttpServletRequest request) {
        Result<?> result = new Result<>();
        String bizPath = request.getParameter("biz");

        //LOWCOD-2580 sys/common/upload接口存在任意文件上传漏洞
        if (oConvertUtils.isNotEmpty(bizPath) && (bizPath.contains("../") || bizPath.contains("..\\"))) {
            throw new JeecgBootException("上传目录bizPath，格式非法！");
        }

        if(oConvertUtils.isEmpty(bizPath)){
            bizPath = "";
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
        String orgName = file.getOriginalFilename();// 获取文件名
        orgName = CommonUtils.getFileName(orgName);
        String file_url =  MinioUtil.upload(file,bizPath);
        if(oConvertUtils.isEmpty(file_url)){
            return Result.error("上传失败,请检查配置信息是否正确!");
        }
        //保存文件信息
        OSSFile minioFile = new OSSFile();
        minioFile.setFileName(orgName);
        minioFile.setUrl(file_url);
        ossFileService.save(minioFile);
        result.setMessage(file_url);
        result.setSuccess(true);
        return result;
    }
}
