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
 * 贴纸上传
 * 主图：GIF 格式，尺寸为 240 * 240 像素，大小不超过 100KB
 * 缩略图：PNG 格式，尺寸为 240 * 240 像素，大小不超过 60KB
 */
@RestController
@RequestMapping("/u/sticker")
@Slf4j
public class StickerUploadController extends BaseUploadCtrl {

    @Resource
    private UploadService uploadService;


    @PostMapping({"","/"})
    public Result<Object> index() {
        return success();
    }

    @PostMapping("/upload")
    public @ResponseBody
    Result<Object> index(@RequestParam("file") MultipartFile multipartFile,@RequestParam Integer stickerId,Integer w) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadService.saveSticker(multipartFile, stickerId,w);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("贴纸上传失败", e);
            return fail("上传失败，请重试");
        }
    }
    @PostMapping("/import")
    public @ResponseBody
    Result<Object> importBatch(@RequestParam("file") MultipartFile multipartFile,@RequestParam Integer stickerId) {
        if (multipartFile.isEmpty()) {
            return fail("请选择要上传的文件");
        }
        try {
            return uploadService.saveStickerBatch(multipartFile, stickerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("贴纸批量导入上传失败", e);
            return fail("导入失败，请重试");
        }
    }
}
