package org.jeecg.modules.oss.service;

import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.oss.entity.OSSFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: OOS云存储service接口
 * @author: jeecg-boot
 */
public interface IOSSFileService extends IService<OSSFile> {

    /**
     * oss文件上传
     * @param multipartFile
     * @throws IOException
     */
	void upload(MultipartFile multipartFile) throws IOException;

    /**
     * oss文件删除
     * @param ossFile OSSFile对象
     * @return
     */
	boolean delete(OSSFile ossFile);

}
