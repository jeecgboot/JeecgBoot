package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 上传记录
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Upload.TABLE_NAME)
public class Upload extends BaseModel<Upload> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_upload";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String md5;

    /**
     * 类型
     */
    private String type;

    private String contentType;

    /**
     * 重命名名称
     */
    private String name;

    /**
     * 源文件名称
     */
    private String sourceName;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 本地上传路径
     */
    private String path;

    /**
     * 存储方式
     */
    private String storageType;

    /**
     * 外网下载地址
     */
    private String href;

    private Long size;

    private String adminId;

    private Integer userId;
    //待删除时间
    private Long tsDelete;
    //0:正常，1：待删除，2：已删除
    private Integer status;

    /**
     * 临时文件
     */
    private Boolean isTemp;

    private Long tsCreate;

    public enum FileType{
        视频("video"),
        音频("audio"),
        消息图片("image"),
        普通图片("img"),
        压缩文件("zip"),
        头像("avatar"),
        群组头像("muc_avatar"),
        贴纸("sticker"),
        动画贴纸("animated_sticker"),
        动画贴纸包("animated_sticker_pack"),
        动图("gif"),
        自定义表情("custom_emoji"),
        文件("file"),
        其他("other");
        private String type;
        FileType(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public enum Status{
        Normal(0),//正常
        FutureDelete(1),//计划删除
        Deleted(2);//已删除
        private int code;
        Status(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

}
