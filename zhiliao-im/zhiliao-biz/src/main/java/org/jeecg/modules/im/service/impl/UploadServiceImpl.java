package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.MinioUtil;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.util.ZipUtil;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantImage;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.*;
import org.jeecg.modules.im.base.util.oss.AliyunOss;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QUpload;
import org.jeecg.modules.im.mapper.UploadMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * <p>
 * 上传记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Service
@Slf4j
public class UploadServiceImpl extends BaseServiceImpl<UploadMapper, Upload> implements UploadService {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private StickerItemService stickerItemService;
    @Resource
    private GifService gifService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private UserAvatarService userAvatarService;
    @Resource
    private CustomEmojiService customEmojiService;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private BaseConfig baseConfig;

    @Override
    public IPage<Upload> pagination(MyPage<Upload> page, QUpload q) {
        return uploadMapper.pagination(page, q);
    }

    @Override
    public Result<Object> saveAvatar(MultipartFile multipartFile) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.avatar_file_length) {
            return fail("头像大小限制"+(ConstantImage.avatar_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        SysConfig sysConfig = sysConfigService.get();
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=userId +"/"+uuid + suffix;
        if(userId==null){
            fileName = UUIDTool.getUUID2() +"/"+uuid + suffix;
        }
        String fileName2 = userId +"/"+uuid + ".webp";
        if(userId==null){
            fileName2 = UUIDTool.getUUID2() +"/"+uuid + ".webp";
        }
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.头像.getType();
            //保存原图到本地
            String originPath = targetPath + "/o/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            String originThumbPath = targetPath + "/t/" + fileName;
            tempFile = new File(originThumbPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File originThumbFile = new File(originThumbPath);
            if (originThumbFile.exists()) {
                originThumbFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantImage.avatar_thumbnail_size, originThumbPath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originPath,destPath,0)){
                int retryTimes = 0;
                File o = new File(destPath);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(destFile);
                FileUtils.deleteQuietly(originFile);
            }
            if(ImageUtils.executeCWebp(originThumbPath,destThumbPath,0)){
                int retryTimes = 0;
                File o = new File(destThumbPath);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f2 = new FileInputStream(destThumbFile);
                FileUtils.deleteQuietly(originThumbFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.头像.getType() +"/o/"+fileName2, destFile);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.头像.getType() + "/t/" + fileName2, destThumbFile);
            }else {
                originUrl =  MinioUtil.upload(f1,Upload.FileType.头像.getType() + "/o/"+fileName2,false);
                thumbnailUrl =  MinioUtil.upload(f2,Upload.FileType.头像.getType() + "/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.头像.getType(),
                        ".webp"
                );
            }
            if(!isEmpty(thumbnailUrl)&&f2!=null){
                addUpload(null,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destThumbPath,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.头像.getType(),
                        ".webp"
                );
            }
            UserAvatar userAvatar = new UserAvatar();
            userAvatar.setUserId(getCurrentUserId());
            userAvatar.setThumb(thumbnailUrl);
            userAvatar.setOrigin(originUrl);
            userAvatar.setTsCreate(getTs());
            userAvatar.setTsAudit(getTs());
            userAvatarService.save(userAvatar);
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传头像异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
            FileUtils.deleteQuietly(destThumbFile);
        }
    }

    /**
     * 上传群头像
     * @param multipartFile
     * @param mucId
     * @return
     */
    @Override
    public Result<Object> saveMucAvatar(MultipartFile multipartFile, String mucId) {
        long size = multipartFile.getSize();
        if (size >= ConstantImage.avatar_file_length) {
            return fail("头像大小限制"+(ConstantImage.avatar_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=uuid + suffix,fileName2 = uuid + ".webp";
        if (mucId != null) {
            fileName = mucId + suffix;
            fileName2 = mucId + ".webp";
        }
        Kv data = Kv.create();
        SysConfig sysConfig = sysConfigService.get();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.群组头像.getType();
            //保存原图到本地
            String originPath = targetPath + "/o/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            String originThumbPath = targetPath + "/t/" + fileName;
            tempFile = new File(originThumbPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File originThumbFile = new File(originThumbPath);
            if (originThumbFile.exists()) {
                originThumbFile.delete();
            }
//            fis = new FileInputStream(originFile);
            ImageUtils.createThumbnailsScale(originFile, ConstantImage.avatar_thumbnail_size, originThumbPath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originPath,destPath,0)){
                int retryTimes = 0;
                File o = new File(destPath);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(o);
                FileUtils.deleteQuietly(originFile);
            }
            if(ImageUtils.executeCWebp(originThumbPath,destThumbPath,0)){
                int retryTimes = 0;
                File o = new File(destThumbPath);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f2 = new FileInputStream(o);
                FileUtils.deleteQuietly(originThumbFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.群组头像.getType() +"/o/"+fileName2, destFile);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.群组头像.getType() + "/t/" + fileName2, destThumbFile);
            }else{
                originUrl =  MinioUtil.upload(f1,Upload.FileType.群组头像.getType() + "/o/"+fileName2,false);
                thumbnailUrl =  MinioUtil.upload(f2,Upload.FileType.群组头像.getType() + "/t/"+fileName2,false);
            }
            Integer userId = getCurrentUserId();
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.群组头像.getType(),
                        ".webp"
                );
            }
            if(!isEmpty(thumbnailUrl)&&f2!=null){
                addUpload(null,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destThumbPath,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.群组头像.getType(),
                        ".webp"
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传群组头像异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
            FileUtils.deleteQuietly(destThumbFile);
        }
    }

    /**
     * 音频文件
     * @param multipartFile
     * @return
     */
    @Override
    public Result<Object> saveAudio(MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        if (size >= ConstantImage.audio_file_length) {
            return fail("音频大小限制"+(ConstantImage.audio_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
//        Set<String> types = new HashSet<>();
//        types.add("audio/amr");
//        types.add("audio/mp3");
//        types.add("audio/wav");
//        if (!types.contains(contentType)) {
//            return fail("格式不支持");
//        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = uuid + suffix;
        Kv data = Kv.create();
        SysConfig sysConfig = sysConfigService.get();
        FileInputStream f1 = null;
        File originFile = null;
        try {
            String originUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.音频.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            f1 = new FileInputStream(originFile);

            //上传到阿里云oss
            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.音频.getType() + "/" + fileName, originFile);
            }else {
                originUrl =  MinioUtil.upload(f1,Upload.FileType.音频.getType() + "/" + fileName,false);
            }
            Integer userId = getCurrentUserId();
            if(!isEmpty(originUrl)){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + suffix,
                        originFilePath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.音频.getType(),
                        suffix
                );
            }
            data.put("url", originUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传音频文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            FileUtils.deleteQuietly(originFile);
        }
    }
    //上传语音文件
    @Override
    public Result<Object> saveVoice(MultipartFile multipartFile) {
        return fail();
    }
    //上传视频文件
    @Override
    public Result<Object> saveVideo(MultipartFile multipartFile) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.video_file_length) {
            return fail("视频大小限制"+(ConstantImage.video_file_length/1024/1024)+"M以内");
        }
//        String contentType = multipartFile.getContentType();//类型
//        Set<String> types = new HashSet<>();
//        types.add("video/x-ms-wmv");
//        types.add("video/mpeg4");
//        types.add("video/avi");
//        types.add("video/wma");
//        types.add("video/mp4");
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        if (!ToolFile.isVideoFile(originalFileName)) {
            return fail("格式不支持");
        }
        String suffix = ToolFile.getExtension(originalFileName);
        String fileName = userId+"/" +UUIDTool.getUUID() + suffix;
        Kv data = Kv.create();
        SysConfig sysConfig = sysConfigService.get();
        try {
            String url;

            //上传到阿里云oss
            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                url = aliyunOss.uploadLocalFile(basePath + Upload.FileType.视频.getType() + "/"+ fileName, multipartFile.getInputStream());
            }else {
                url =  MinioUtil.upload(multipartFile.getInputStream(),Upload.FileType.视频.getType() + "/" + fileName,false);
            }
            data.put("url", url);
            return success(data);
        } catch (Exception e) {
            log.error("上传视频文件异常", e);
            return fail();
        }
    }

    @Override
    public Result<Object> saveMsgImg(MultipartFile multipartFile) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.msg_img_length) {
            return fail("图片大小限制"+(ConstantImage.msg_img_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "msg/"+userId+"/"+uuid + suffix;
        String fileName2 = "msg/"+userId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.消息图片.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantImage.msg_img_thumbnail_size, thumbnailFilePath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }

            String originWebp = targetPath + "/o/" + fileName2;
            String destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originFilePath,originWebp,0)){
                int retryTimes = 0;
                File o = new File(originWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(o);
                FileUtils.deleteQuietly(originFile);
            }
            if(ImageUtils.executeCWebp(thumbnailFilePath,destWebp,0)){
                int retryTimes = 0;
                File o = new File(destWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f2 = new FileInputStream(o);
                FileUtils.deleteQuietly(thumbnailFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.消息图片.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.消息图片.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  MinioUtil.upload(f1, Upload.FileType.消息图片.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  MinioUtil.upload(f2, Upload.FileType.消息图片.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.消息图片.getType(),
                        ".webp"
                );
            }
            if(!isEmpty(thumbnailUrl)&&f2!=null){
                addUpload(null,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.消息图片.getType(),
                        ".webp"
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传消息图片异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
            FileUtils.deleteQuietly(destThumbFile);
        }
    }

    @Override
    public Result<Object> saveCustomEmoji(MultipartFile multipartFile) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.custom_emoji_length) {
            return fail("图片大小限制"+(ConstantImage.custom_emoji_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "custom_emoji/"+userId+"/"+uuid + suffix;
        String fileName2 = "custom_emoji/"+userId +"/"+uuid + ".webp";
        FileInputStream f1 = null,f2=null;
        File destFile = null,destThumbFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.自定义表情.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            File originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            File thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, ConstantImage.custom_emoji_thumbnail_size, thumbnailFilePath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }

            String originWebp = targetPath + "/o/" + fileName2;
            String destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originFilePath,originWebp,0)){
                int retryTimes = 0;
                File o = new File(originWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(o);
                FileUtils.deleteQuietly(originFile);
            }
            if(ImageUtils.executeCWebp(thumbnailFilePath,destWebp,0)){
                int retryTimes = 0;
                File o = new File(destWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f2 = new FileInputStream(o);
                FileUtils.deleteQuietly(thumbnailFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.自定义表情.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.自定义表情.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  MinioUtil.upload(f1, Upload.FileType.自定义表情.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  MinioUtil.upload(f2, Upload.FileType.自定义表情.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.自定义表情.getType(),
                        ".webp"
                );
            }
            if(!isEmpty(thumbnailUrl)&&f2!=null){
                addUpload(null,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.自定义表情.getType(),
                        ".webp"
                );
            }
            CustomEmoji emoji = new CustomEmoji();
            emoji.setOrigin(originUrl);
            emoji.setThumb(thumbnailUrl);
            emoji.setUserId(getCurrentUserId());
            emoji.setTsCreate(getTs());
            customEmojiService.save(emoji);
            return success(emoji);
        } catch (Exception e) {
            log.error("上传自定义表情异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
            FileUtils.deleteQuietly(destThumbFile);
        }
    }

    @Override
    public Result<Object> saveSticker(MultipartFile multipartFile, Integer stickerId,Integer width) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.sticker_file_length) {
            return fail("图片大小限制"+(ConstantImage.sticker_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = stickerId +"/"+uuid + suffix;
        String fileName2 = stickerId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        File originFile = null,thumbnailFile = null;
        SysConfig sysConfig = sysConfigService.get();
        String originWebp = null,destWebp = null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.贴纸.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, width!=null?width:ConstantImage.sticker_thumbnail_size, thumbnailFilePath);

            originWebp = targetPath + "/o/" + fileName2;
            destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originFilePath,originWebp,0)){
                int retryTimes = 0;
                File o = new File(originWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(o);
                FileUtils.deleteQuietly(originFile);
            }
            if(ImageUtils.executeCWebp(thumbnailFilePath,destWebp,0)){
                int retryTimes = 0;
                File o = new File(destWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f2 = new FileInputStream(o);
                FileUtils.deleteQuietly(thumbnailFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.贴纸.getType() +"/o/"+fileName2, f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.贴纸.getType() + "/t/" + fileName2, f2);
            }else {
                originUrl =  MinioUtil.upload(f1,Upload.FileType.贴纸.getType() +"/o/"+fileName2,false);
                thumbnailUrl =  MinioUtil.upload(f2,Upload.FileType.贴纸.getType() +"/t/"+fileName2,false);
            }
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.贴纸.getType(),
                        ".webp"
                );
            }
            if(!isEmpty(thumbnailUrl)&&f2!=null){
                addUpload(null,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.贴纸.getType(),
                        ".webp"
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(new File(originWebp));
            FileUtils.deleteQuietly(new File(destWebp));
        }
    }
    @Override
    public Result<Object> saveStickerBatch(MultipartFile multipartFile, Integer stickerId) {
        Integer userId = getCurrentUserId();
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String zipFileName = stickerId +"/"+uuid + suffix;
        try {
            SysConfig sysConfig = sysConfigService.get();
            //物理路径
            String targetPhysicalPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.贴纸.getType();
            //保存原图到本地
            String targetZipFilePath = targetPhysicalPath+"/" + zipFileName;
            File targetZipFile = new File(targetZipFilePath);
            if (!targetZipFile.exists()) {
                targetZipFile.mkdirs();
            }
            //存储压缩文件
            multipartFile.transferTo(targetZipFile);

            //解压zip
            File[] files = ZipUtil.unzipWebp(targetZipFilePath);
            if(files==null||files.length==0){
                return fail("子文件为空");
            }
            StickerItem stickerItem;
            List<StickerItem> stickerItems = new ArrayList<>();
            String originUrl,thumbnailUrl;
            //目标文件夹
            targetPhysicalPath = targetPhysicalPath +"/" + stickerId;
            int errorCount=0,okCount=0;
            for (File webp : files) {
                String uuid2 = UUIDTool.getUUID();
                String suffix2 = ToolFile.getExtension(webp.getName());
                String fileName = uuid2 + suffix2;
                FileInputStream f1=null,f2=null;
                File originFile = null,thumbnailFile = null;
                try {
                    //目标原图
                    String originFilePath = targetPhysicalPath + "/o/" + fileName;
                    File tempFile = new File(originFilePath);
                    if (!tempFile.getParentFile().exists()) {
                        tempFile.getParentFile().mkdirs();
                    }
                    //目标缩略图
                    String thumbnailFilePath = targetPhysicalPath + "/t/" + fileName;
                    tempFile = new File(thumbnailFilePath);
                    if (!tempFile.getParentFile().exists()) {
                        tempFile.getParentFile().mkdirs();
                    }
                    originFile = new File(originFilePath);
                    if (originFile.exists()) {
                        originFile.delete();
                    }
                    try (FileChannel from = new FileInputStream(webp).getChannel();
                         FileChannel to = new FileOutputStream(originFile).getChannel()) {
                        long size = from.size();
                        for (long left = size; left > 0; ) {
                            log.info("position:{},left:{}", size - left, left);
                            left -= from.transferTo((size - left), left, to);
                        }
                    } catch (Exception e) {
                        throw new BusinessException("转存异常");
                    }
                    //保存缩略图到本地
                    thumbnailFile = new File(thumbnailFilePath);
                    if (thumbnailFile.exists()) {
                        thumbnailFile.delete();
                    }
                    ImageUtils.createThumbnailsScale(originFile, ConstantImage.sticker_thumbnail_size, thumbnailFilePath);

                    f1 = new FileInputStream(originFilePath);
                    f2 = new FileInputStream(thumbnailFilePath);

                    if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                        AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                        String basePath = sysConfig.getOssBasePath();
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "";
                        }
                        originUrl = aliyunOss.uploadLocalFile(basePath + originFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1), f1);
                        thumbnailUrl = aliyunOss.uploadLocalFile(basePath + thumbnailFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1), f2);
                    } else {
                        originUrl =  MinioUtil.upload(f1,originFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1),false);
                        thumbnailUrl =  MinioUtil.upload(f2, thumbnailFilePath.replace(baseConfig.getBaseUploadPath(), "").substring(1),false);
                    }
                    okCount++;
                    stickerItem = new StickerItem();
                    stickerItem.setStickerId(stickerId);
                    stickerItem.setOrigin(originUrl);
                    stickerItem.setThumb(thumbnailUrl);
                    stickerItem.setTsCreate(getTs());
                    stickerItems.add(stickerItem);
                }catch (Exception e){
                    log.error("静态贴纸导入异常，name="+webp.getName(),e);
                    errorCount ++;
                }finally {
                    assert f1!=null;
                    try {
                        f1.close();
                    } catch (Exception e) {
                    }
                    assert f2!=null;
                    try {
                        f2.close();
                    } catch (Exception e) {
                    }
                    //删除本地webp
                    FileUtils.deleteQuietly(originFile);
                    FileUtils.deleteQuietly(thumbnailFile);
                }
            }
            stickerItemService.saveBatch(stickerItems);
            //删除解压的同名文件夹
            FileUtils.deleteQuietly(new File(targetZipFilePath.substring(0,targetZipFilePath.lastIndexOf("."))));
            //删除压缩文件
            FileUtils.deleteQuietly(targetZipFile);
            return success(Kv.by("ok",okCount).set("error",errorCount));
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        }
    }
    //动态贴纸包，保存zip
    @Override
    public Result<Object> saveStickerAnimatedPack(MultipartFile multipartFile) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.sticker_animated_zip_file_length) {
            return fail("文件大小限制("+ConstantImage.sticker_animated_zip_file_length /1024/1024+")M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = "temp/"+uuid + suffix;
        Kv data = Kv.create();
        FileInputStream f1 = null;
        SysConfig sysConfig = sysConfigService.get();
        File originFile = null;
        try {
            String lottie;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动画贴纸包.getType();
            //保存上传文件到本地
            String originFilePath = targetPath + "/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            f1 = new FileInputStream(originFilePath);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                lottie = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动画贴纸包.getType() +"/"+fileName, originFile);
            }else{
                lottie =  MinioUtil.upload(f1,Upload.FileType.动画贴纸包.getType() +"/"+fileName,false);
            }
            if(!isEmpty(lottie)){
                addUpload(null,
                        userId,
                        contentType,
                        lottie,
                        DigestUtils.md5Hex(f1),
                        uuid + ".zip",
                        lottie,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动画贴纸包.getType(),
                        ".zip"
                );
            }

            data.put("f", lottie);
            return success(data);
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(originFile);
        }
    }
    //动态贴纸，保存zip
    @Override
    public Result<Object> saveStickerAnimated(MultipartFile multipartFile, Integer stickerId) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.sticker_animated_item_file_length) {
            return fail("文件大小限制("+ConstantImage.sticker_animated_item_file_length /1024/1024+")M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = stickerId +"/"+uuid + suffix;
        Kv data = Kv.create();
        FileInputStream f1 = null;
        SysConfig sysConfig = sysConfigService.get();
        File originFile = null;
        try {
            String lottie;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动画贴纸.getType();
            //保存上传文件到本地
            String originFilePath = targetPath + "/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            f1 = new FileInputStream(originFilePath);

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                lottie = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动画贴纸.getType() +"/"+fileName, originFile);
            }else{
                lottie =  MinioUtil.upload(f1,Upload.FileType.动画贴纸.getType() +"/"+fileName,false);
            }
            if(!isEmpty(lottie)){
                addUpload(null,
                        userId,
                        contentType,
                        lottie,
                        DigestUtils.md5Hex(f1),
                        uuid + ".zip",
                        lottie,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动画贴纸.getType(),
                        ".zip"
                );
            }

            data.put("f", lottie);
            return success(data);
        } catch (Exception e) {
            log.error("上传贴纸文件异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(originFile);
        }
    }
    //上传压缩文件,子文件列表均为zip
    @Override
    public Result<Object> saveStickerAnimatedBatch(MultipartFile multipartFile, Integer stickerId) {
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = stickerId +"/"+uuid + suffix;
        try {
            SysConfig sysConfig = sysConfigService.get();
            //物理路径
            String targetPhysicalPath = baseConfig.getBaseUploadPath() + File.separator + Upload.FileType.动画贴纸.getType();
            //保存zip文件到本地
            String zipFilePath = targetPhysicalPath + File.separator+"tgs_zip"+File.separator + fileName;
            String targetDir = targetPhysicalPath +File.separator+ stickerId+File.separator;
            File tempZipFile = new File(zipFilePath);
            if (!tempZipFile.exists()) {
                tempZipFile.mkdirs();
            }
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            multipartFile.transferTo(zipFile);
            //解压zip
            List<File> animateds = ZipUtil.unzipAnimated(zipFilePath,targetDir);
            if(animateds.isEmpty()){
                return fail("文件列表为空");
            }
            String lottie;
            StickerItem stickerItem;
            List<StickerItem> stickerItems = new ArrayList<>();
            targetPhysicalPath = targetPhysicalPath +File.separator + stickerId;
            int okCount = 0,errorCount = 0;
            for (File animatedZip : animateds) {
                FileInputStream f1 = null;
                try {
                    f1 = new FileInputStream(animatedZip);
                    if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                        AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                        String basePath = sysConfig.getOssBasePath();
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "";
                        }
                        lottie = aliyunOss.uploadLocalFile(basePath + animatedZip.getAbsolutePath().replace(baseConfig.getBaseUploadPath(), "").substring(1), animatedZip);
                    } else{
                        lottie =  MinioUtil.upload(f1,animatedZip.getAbsolutePath().replaceAll("\\\\","/").replace(baseConfig.getBaseUploadPath(), "").substring(1),false);
                    }
                    okCount++;
                    stickerItem = new StickerItem();
                    stickerItem.setStickerId(stickerId);
                    stickerItem.setLottie(lottie);
                    stickerItem.setZipName(animatedZip.getName());
                    stickerItem.setTsCreate(getTs());
                    stickerItems.add(stickerItem);
                }catch (Exception e){
                    log.error("批量导入动态贴纸异常", e);
                    errorCount ++;
                }finally {
                    assert f1!=null;
                    try {
                        f1.close();
                    } catch (Exception e) {
                    }
                }
            }
            FileUtils.deleteQuietly(zipFile);
            FileUtils.deleteDirectory(new File(targetPhysicalPath));
            FileUtils.deleteQuietly(new File(zipFile.getAbsolutePath().substring(0,zipFile.getAbsolutePath().lastIndexOf("."))));
            stickerItemService.saveBatch(stickerItems);
            return success(Kv.by("ok",okCount).set("error",errorCount));
        }catch (Exception e){
            log.error("批量导入动画贴纸异常", e);
            return fail();
        }
    }

    @Override
    public Result<Object> saveGif(MultipartFile multipartFile, Integer gifAlbumId,Integer width) {
        Integer userId = getCurrentUserId();
        String adminId = getCurrentAdminId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.gif_file_length) {
            return fail("图片大小限制"+(ConstantImage.gif_file_length/1024/1024)+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = gifAlbumId +"/"+uuid + suffix;
        String fileName2 = gifAlbumId +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null,f2=null;
        SysConfig sysConfig = sysConfigService.get();
        File originFile,thumbnailFile;
        String originWebp=null,destWebp=null;
        try {
            String originUrl,thumbnailUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动图.getType();
            //保存原图到本地
            String originFilePath = targetPath + "/o/" + fileName;
            File tempFile = new File(originFilePath);
            if (!tempFile.getParentFile().exists()) {
                tempFile.mkdirs();
            }
            String thumbnailFilePath = targetPath + "/t/" + fileName;
            tempFile = new File(thumbnailFilePath);
            if (!tempFile.getParentFile().exists()) {
                tempFile.mkdirs();
            }
            originFile = new File(originFilePath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);
            //保存缩略图到本地
            thumbnailFile = new File(thumbnailFilePath);
            if (thumbnailFile.exists()) {
                thumbnailFile.delete();
            }
            ImageUtils.createThumbnailsScale(originFile, width!=null?width:ConstantImage.gif_thumbnail_size, thumbnailFilePath);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/o/" + fileName2;
            File destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            String destThumbPath = targetPath + "/t/" + fileName2;
            File destThumbFile = new File(destThumbPath);
            if (!destThumbFile.exists()) {
                destThumbFile.getParentFile().mkdirs();
            }

            originWebp = targetPath + "/o/" + fileName2;
            destWebp = targetPath + "/t/" + fileName2;
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originFilePath,originWebp,0)){
                int retryTimes = 0;
                File o = new File(originWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(originWebp);
                FileUtils.deleteQuietly(originFile);
            }
            if(ImageUtils.executeCWebp(thumbnailFilePath,destWebp,0)){
                int retryTimes = 0;
                File o = new File(destWebp);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f2 = new FileInputStream(destWebp);
                FileUtils.deleteQuietly(thumbnailFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动图.getType()+"/"+gifAlbumId+"/o/"+uuid+".webp", f1);
                thumbnailUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.动图.getType() +"/"+gifAlbumId+"/t/"+uuid+".webp", f2);
            } else {
                originUrl = MinioUtil.upload(f1,Upload.FileType.动图.getType() +"/"+gifAlbumId+"/o/"+uuid+".webp",false);
                thumbnailUrl = MinioUtil.upload(f2,Upload.FileType.动图.getType() +"/"+gifAlbumId+"/t/"+uuid+".webp",false);
            }
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(adminId,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        originWebp,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动图.getType(),
                        ".webp"
                );
            }
            if(!isEmpty(thumbnailUrl)&&f2!=null){
                addUpload(adminId,
                        userId,
                        contentType,
                        thumbnailUrl,
                        DigestUtils.md5Hex(f2),
                        uuid + ".webp",
                        destWebp,
                        fileName,
                        f2.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.动图.getType(),
                        ".webp"
                );
            }
            data.put("origin", originUrl);
            data.put("thumb", thumbnailUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传gif动图异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            assert f2!=null;
            try {
                f2.close();
            } catch (Exception e) {
            }
            try{
                FileUtils.deleteQuietly(new File(originWebp));
                FileUtils.deleteQuietly(new File(destWebp));
            }catch (Exception e){}

        }
    }

    @Override
    public Result<Object> saveGifBatch(MultipartFile multipartFile, Integer gifAlbumId) {
        Integer userId = getCurrentUserId();
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("application/zip");
        types.add("application/x-zip-compressed");
        if (!types.contains(contentType)) {
            return fail("文件格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName = gifAlbumId +"/"+uuid + suffix;

        File zipFile = null;
        List<Gif> gifs = null;
        try {
            SysConfig sysConfig = sysConfigService.get();
            //物理路径
            String targetPhysicalPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动图.getType();
            String zipFilePath = targetPhysicalPath + "/" + fileName;
            File tempZipFile = new File(zipFilePath);
            if (!tempZipFile.getParentFile().exists()) {
                tempZipFile.getParentFile().mkdirs();
            }
            zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            //保存zip文件到本地
            multipartFile.transferTo(zipFile);
            //解压zip
            List<File> files = ZipUtil.unzipGif(zipFilePath);
            if(files.size()==0){
                return fail("文件数量为0");
            }
            Gif gif;
            gifs = new ArrayList<>();
            targetPhysicalPath = targetPhysicalPath + "/" + gifAlbumId;
            String gifFileName,targetGifPath,targetGifName,thumbnailGifPath,thumbnailGifName,destGif,destThumbGif,uuid2,gifMd5;
            FileInputStream fis;
            int okCount=0,errorCount=0;
            for (File g : files) {
                fis = new FileInputStream(g);
                gifMd5 = DigestUtils.md5Hex(fis);
//                if(gifService.findByMd5(gifMd5)!=null){
//                    try {
//                        fis.close();
//                    } catch (Exception e) {
//                    }
//                    continue;
//                }
                uuid2 = UUIDTool.getUUID();
                gifFileName = uuid2 +".gif";
                targetGifName = targetPhysicalPath + "/o/" + gifFileName;
                targetGifPath = targetPhysicalPath + "/o/" + uuid2;
                File tempFile = new File(targetGifPath);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                thumbnailGifName = targetPhysicalPath + "/t/" + gifFileName;
                thumbnailGifPath = targetPhysicalPath + "/t/" + uuid2;
                tempFile = new File(thumbnailGifPath);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                FileInputStream f1 = null,f2 = null;
                File destFile,destThumbFile;
                try (FileChannel from = fis.getChannel();
                     FileChannel to = new FileOutputStream(targetGifName).getChannel()){
                    long size = from.size();
                    for (long left = size; left > 0; ) {
                        left -= from.transferTo((size - left), left, to);
                    }
                    //保存缩略图到本地
                    ImageUtils.createThumbnailsScale(new File(targetGifName), ConstantImage.gif_thumbnail_size, thumbnailGifName);
                    String gifFileName2 = UUIDTool.getUUID()+".webp";
                    //webp 压缩后的图和缩略图
                    String destPath = targetPhysicalPath + "/o/" + gifFileName2;
                    destFile = new File(destPath);
                    if (!destFile.exists()) {
                        destFile.getParentFile().mkdirs();
                    }
                    String destThumbPath = targetPhysicalPath + "/t/" + gifFileName2;
                    destThumbFile = new File(destThumbPath);
                    if (!destThumbFile.exists()) {
                        destThumbFile.getParentFile().mkdirs();
                    }
                    //原图和缩略图都转成webp,转换成功后删除原图
                    if(ImageUtils.executeCWebp(targetGifName,destPath,0)){
                        int retryTimes = 0;
                        File o = new File(destPath);
                        while (!o.exists()){
                            if(retryTimes++>=10){
                                break;
                            }
                            Thread.sleep(1000);
                        }
                        f1 = new FileInputStream(o);
                        FileUtils.deleteQuietly(new File(targetGifPath));
                    }
                    if(ImageUtils.executeCWebp(thumbnailGifName,destThumbPath,0)){
                        int retryTimes = 0;
                        File o = new File(destThumbPath);
                        while (!o.exists()){
                            if(retryTimes++>=10){
                                break;
                            }
                            Thread.sleep(1000);
                        }
                        f2 = new FileInputStream(o);
                        FileUtils.deleteQuietly(new File(thumbnailGifPath));
                    }
                    if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                        AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                        String basePath = sysConfig.getOssBasePath();
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "";
                        }
                        destGif = aliyunOss.uploadLocalFile(basePath + destPath.replace(baseConfig.getBaseUploadPath(),"").substring(1), f1);
                        destThumbGif = aliyunOss.uploadLocalFile(basePath + destThumbPath.replace(baseConfig.getBaseUploadPath(),"").substring(1), f2);
                    }else{
                        destGif = MinioUtil.upload(f1,destPath.replace(baseConfig.getBaseUploadPath(),"").substring(1),false);
                        destThumbGif = MinioUtil.upload(f2,destThumbPath.replace(baseConfig.getBaseUploadPath(),"").substring(1),false);
                    }
                    okCount++;
                    gif = new Gif();
                    gif.setAlbumId(gifAlbumId);
                    gif.setOrigin(destGif);
                    gif.setThumb(destThumbGif);
                    gif.setTsCreate(getTs());
                    gif.setMd5(gifMd5);
                    gifs.add(gif);
                }catch (Exception e){
                    log.error("gif导入转存异常，name"+g.getName(),e);
                    errorCount ++;
                }finally {
                    fis.close();
                    assert f1!=null;
                    try {
                        f1.close();
                    } catch (Exception e) {
                    }
                    assert f2!=null;
                    try {
                        f2.close();
                    } catch (Exception e) {
                    }
                }
            }
            return success(Kv.by("ok",okCount).set("error",errorCount));
        } catch (Exception e) {
            log.error("批量导入gif动图异常", e);
            return fail();
        } finally {
            if(gifs!=null&&!gifs.isEmpty()){
                gifService.saveBatch(gifs);
            }
            if(zipFile!=null){
                FileUtils.deleteQuietly(zipFile);
                FileUtils.deleteQuietly(new File(zipFile.getAbsolutePath().substring(0,zipFile.getAbsolutePath().lastIndexOf("."))));
                //删掉临时文件夹
                FileUtils.deleteQuietly(new File(baseConfig.getBaseUploadPath() + "/" + Upload.FileType.动图.getType()+"/"+gifAlbumId));
            }
        }
    }

    @Override
    public Result<Object> saveImg(MultipartFile multipartFile,Integer width) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.common_img_length) {
            return fail("图片大小限制"+ConstantImage.common_img_length/1024/1024+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        Set<String> types = new HashSet<>();
        types.add("image/bmp");
        types.add("image/png");
        types.add("image/jpg");
        types.add("image/jpeg");
        types.add("image/webp");
        types.add("image/gif");
        types.add("application/octet-stream");
        if (!types.contains(contentType)) {
            return fail("图片格式不支持");
        }
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String uuid = UUIDTool.getUUID();
        String fileName=UUIDTool.getUUID2() +"/"+uuid + suffix;
        String fileName2 = UUIDTool.getUUID2() +"/"+uuid + ".webp";
        Kv data = Kv.create();
        FileInputStream f1 = null;
        File destFile = null;
        SysConfig sysConfig = sysConfigService.get();
        try {
            String originUrl;
            //物理路径
            String targetPath = baseConfig.getBaseUploadPath() + "/" + Upload.FileType.普通图片.getType();
            //保存原图到本地
            String originPath = targetPath + "/" + fileName;
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.getParentFile().mkdirs();
            }
            File originFile = new File(originPath);
            if (originFile.exists()) {
                originFile.delete();
            }
            multipartFile.transferTo(originFile);

            //webp 压缩后的图和缩略图
            String destPath = targetPath + "/" + fileName2;
            destFile = new File(destPath);
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            //原图和缩略图都转成webp,转换成功后删除原图
            if(ImageUtils.executeCWebp(originPath,destPath,0)){
                int retryTimes = 0;
                File o = new File(destPath);
                while (!o.exists()){
                    if(retryTimes++>=10){
                        break;
                    }
                    Thread.sleep(1000);
                }
                f1 = new FileInputStream(o);
                FileUtils.deleteQuietly(originFile);
            }

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                originUrl = aliyunOss.uploadLocalFile(basePath + Upload.FileType.普通图片.getType() +"/"+fileName2, f1);
            }else{
                originUrl = MinioUtil.upload(f1,Upload.FileType.普通图片.getType() +"/"+fileName2,false);
            }
            if(!isEmpty(originUrl)&&f1!=null){
                addUpload(null,
                        userId,
                        contentType,
                        originUrl,
                        DigestUtils.md5Hex(f1),
                        uuid + ".webp",
                        destPath,
                        fileName,
                        f1.available(),
                        sysConfig.getStorageType(),
                        Upload.FileType.普通图片.getType(),
                        ".webp"
                );
            }
            data.put("img", originUrl);
            return success(data);
        } catch (Exception e) {
            log.error("上传图片异常", e);
            return fail();
        } finally {
            assert f1!=null;
            try {
                f1.close();
            } catch (Exception e) {
            }
            //删除本地webp
            FileUtils.deleteQuietly(destFile);
        }
    }
    @Override
    public Result<Object> saveFile(MultipartFile multipartFile) {
        Integer userId = getCurrentUserId();
        long size = multipartFile.getSize();
        if (size >= ConstantImage.file_length) {
            return fail("文件大小限制"+ConstantImage.common_img_length/1024/1024+"M以内");
        }
        String contentType = multipartFile.getContentType();//类型
        String originalFileName = multipartFile.getOriginalFilename();//文件名
        String suffix = ToolFile.getExtension(originalFileName);
        String fileName = "/"+suffix+"/"+userId+"/"+UUIDTool.getUUID()+"."+suffix;
        Kv data = Kv.create();
        SysConfig sysConfig = sysConfigService.get();
        try {
            String file;

            if (equals(sysConfig.getStorageType(),SysConfig.StorageType.aliyun_oss.name())) {
                AliyunOss aliyunOss = new AliyunOss(sysConfig.getOssAliyunEndpoint(), sysConfig.getOssAliyunAccessKeyId(), sysConfig.getOssAliyunAccessKeySecret(), sysConfig.getOssAliyunBucketName(), sysConfig.getCdnDomain());
                String basePath = sysConfig.getOssBasePath();
                if (StringUtils.isBlank(basePath)) {
                    basePath = "";
                }
                file = aliyunOss.uploadLocalFile(basePath + Upload.FileType.文件.getType() +fileName, multipartFile.getInputStream());
            }else{
                file = MinioUtil.upload(multipartFile.getInputStream(),Upload.FileType.文件.getType() +fileName);
            }
            return success(file);
        } catch (Exception e) {
            log.error("上传图片异常", e);
            return fail();
        } finally {
        }
    }

    @Override
    public void deleteByHref(String href) {
        Upload upload = findByHref(href);
        if(upload==null){
            return;
        }
        upload.setStatus(Upload.Status.FutureDelete.getCode());
        upload.setTsDelete(ToolDateTime.getDateByDatePlusDays(new Date(),3).getTime());
        uploadMapper.updateById(upload);
    }

    private Upload findByHref(String href) {
        return uploadMapper.findByHref(href);
    }

    private boolean addUpload(String adminId,Integer userId,String contentType,String href,String md5,String name,String path,String sourceName,long size,String storageType,String suffix,String type){
        Upload upload = new Upload();
        upload.setAdminId(adminId);
        upload.setUserId(userId);
        upload.setContentType(contentType);
        upload.setHref(href);
        upload.setMd5(md5);
        upload.setName(name);
        upload.setPath(path);
        upload.setSourceName(sourceName);
        upload.setSize(size);
        upload.setStorageType(storageType);
        upload.setSuffix(suffix);
        upload.setType(type);
        upload.setTsCreate(getTs());
        return save(upload);
    }
}
