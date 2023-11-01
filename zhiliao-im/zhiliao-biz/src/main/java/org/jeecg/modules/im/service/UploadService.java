package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Upload;
import org.jeecg.modules.im.entity.query_helper.QUpload;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 上传记录 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
public interface UploadService extends IService<Upload> {
    IPage<Upload> pagination(MyPage<Upload> page, QUpload q);

    /**
     * 上传用户头像
     * 原图+缩略图
     * 图片大小限制2M以内
     * bmp、jpeg、jpg、png
     */
    Result<Object> saveAvatar(MultipartFile file);
    /**
     * 上传群组头像
     * 原图+缩略图
     * 图片大小限制2M以内
     * bmp、jpeg、jpg、png
     */
    Result<Object> saveMucAvatar(MultipartFile file, String mucId);

    /**
     * 上传音频文件
     *
     */
    Result<Object> saveAudio(MultipartFile file);
    /**
     * 上传语音文件
     *
     */
    Result<Object> saveVoice(MultipartFile file);
    /**
     * 上传视频文件
     *
     */
    Result<Object> saveVideo(MultipartFile file);
    /**
     * 上传消息图片
     * 原图+缩略图
     */
    Result<Object> saveMsgImg(MultipartFile file);

    /**
     * 上传自定义表情
     * 原图+缩略图
     */
    Result<Object> saveCustomEmoji(MultipartFile file);

    /**
     * 上传普通图片
     */
    Result<Object> saveImg(MultipartFile file,Integer w);

    /**
     * 上传贴纸
     */
    Result<Object> saveSticker(MultipartFile file,Integer stickerId,Integer w);
    Result<Object> saveStickerBatch(MultipartFile file,Integer stickerId);

    //上传动画贴纸包的压缩文件
    Result<Object> saveStickerAnimatedPack(MultipartFile file);
    //上传单个动画贴纸
    Result<Object> saveStickerAnimated(MultipartFile file,Integer stickerId);
    //批量导入
    Result<Object> saveStickerAnimatedBatch(MultipartFile file,Integer stickerId);
    /**
     * 上传gif
     */
    Result<Object> saveGif(MultipartFile file,Integer gifAlbumId,Integer w);
    //批量导入
    Result<Object> saveGifBatch(MultipartFile file,Integer gifAlbumId);

    /**
     * 上传普通文件
     */
    Result<Object> saveFile(MultipartFile file);

    /**
     * 根据链接设置记录为待删除状态
     */
    void deleteByHref(String href);
}
