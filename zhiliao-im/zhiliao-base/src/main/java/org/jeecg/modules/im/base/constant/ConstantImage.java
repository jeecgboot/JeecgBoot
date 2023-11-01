package org.jeecg.modules.im.base.constant;

public interface ConstantImage {
    //头像缩略图尺寸
    Integer avatar_thumbnail_size = 48;
    //贴纸小图
    Integer sticker_thumbnail_size = 64;
    //动图缩略图大小
    Integer gif_thumbnail_size = 96;
    //消息图片缩略图大小
    Integer msg_img_thumbnail_size = 120;
    //头像原图大小
    Integer avatar_file_length = 2 * 1024 * 1024;
    //视频文件大小
    Integer video_file_length = 500 * 1024 * 1024;
    //音频文件大小
    Integer audio_file_length = 50 * 1024 * 1024;
    //贴纸原图大小
    Integer sticker_file_length = 5 * 1024 * 1024;
    //单个动画贴纸压缩文件大小
    Integer sticker_animated_item_file_length = 1024 * 1024;
    //动画贴纸包压缩文件大小
    Integer sticker_animated_zip_file_length = 50*1024 * 1024;
    //gif原图大小
    Integer gif_file_length = 5 * 1024 * 1024;
    //普通图片大小
    Integer common_img_length = 10 * 1024 * 1024;
    //消息图片大小
    Integer msg_img_length = 20 * 1024 * 1024;
    //自定义表情大小
    Integer custom_emoji_length = 10 * 1024 * 1024;
    //自定义表情缩略图大小
    Integer custom_emoji_thumbnail_size = 120;

    //普通文件大小
    Integer file_length = 1024 * 1024 * 1024;


}
