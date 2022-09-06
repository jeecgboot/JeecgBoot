package org.jeecg.common.api.dto;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 文件下载
 * cloud api 用到的接口传输对象
 * @author: jeecg-boot
 */
@Data
public class FileDownDTO implements Serializable {

    private static final long serialVersionUID = 6749126258686446019L;

    private String filePath;
    private String uploadpath;
    private String uploadType;
    private HttpServletResponse response;

    public FileDownDTO(){}

    public FileDownDTO(String filePath, String uploadpath, String uploadType,HttpServletResponse response){
        this.filePath = filePath;
        this.uploadpath = uploadpath;
        this.uploadType = uploadType;
        this.response = response;
    }
}
