package org.jeecg.config.oss;

import java.io.InputStream;

/**
 * 简单上传,删除对象接口.
 * 可扩展
 */
public interface OSSManager {

	void upload(String fileName, InputStream inputStream);

	void delete(String fileName);

}
