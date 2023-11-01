package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.UploadService;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 动画贴纸上传
 * json的zip文件
 */
@RestController
@RequestMapping("/u/stickerAnimated")
@Slf4j
public class StickerAnimatedUploadController extends BaseUploadCtrl {

    @Resource
    private UploadService uploadService;


    @PostMapping({"","/"})
    public Result<Object> index() {
        return success();
    }

    @PostMapping("/uploadPack")
    public @ResponseBody
    Result<Object> uploadPack(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadService.saveStickerAnimatedPack(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("贴纸包上传失败", e);
            return fail("上传失败，请重试");
        }
    }
    @PostMapping("/upload")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile,@RequestParam Integer stickerId) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadService.saveStickerAnimated(multipartFile, stickerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("贴纸上传失败", e);
            return fail("上传失败，请重试");
        }
    }

    //批量导入
    @PostMapping("/import")
    public @ResponseBody
    Result<Object> batchImport(@RequestParam("file") MultipartFile multipartFile,@RequestParam Integer stickerId) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadService.saveStickerAnimatedBatch(multipartFile, stickerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量导入失败", e);
            return fail("导入失败，请重试");
        }
    }
}
