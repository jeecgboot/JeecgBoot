package org.jeecg.common.constant.enums;

import org.jeecg.common.util.oConvertUtils;

/**
 * 文件类型
 */
public enum FileTypeEnum {
    //    文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频 voice:语音）
//    FOLDER
    xls(".xls","excel","excel"),
    xlsx(".xlsx","excel","excel"),
    doc(".doc","doc","word"),
    docx(".docx","doc","word"),
    ppt(".ppt","pp","ppt"),
    pptx(".pptx","pp","ppt"),
    gif(".gif","image","图片"),
    jpg(".jpg","image","图片"),
    jpeg(".jpeg","image","图片"),
    png(".png","image","图片"),
    txt(".txt","text","文本"),
    avi(".avi","video","视频"),
    mov(".mov","video","视频"),
    rmvb(".rmvb","video","视频"),
    rm(".rm","video","视频"),
    flv(".flv","video","视频"),
    mp4(".mp4","video","视频"),
    zip(".zip","zip","压缩包"),
    pdf(".pdf","pdf","pdf"),
    mp3(".mp3","mp3","语音");

    private String type;
    private String value;
    private String text;
    private FileTypeEnum(String type,String value,String text){
        this.type = type;
        this.value = value;
        this.text = text;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static FileTypeEnum getByType(String type){
        if (oConvertUtils.isEmpty(type)) {
            return null;
        }
        for (FileTypeEnum val : values()) {
            if (val.getType().equals(type)) {
                return val;
            }
        }
        return null;
    }

}
