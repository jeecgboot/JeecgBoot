package org.jeecg.config.oss.tencent;

import java.io.InputStream;

import com.qcloud.cos.COS;
import com.qcloud.cos.model.ObjectMetadata;
import org.jeecg.config.oss.OSSManager;
import org.jeecg.config.oss.OSSProperties;

/**
 * Object Storage Service of Tencent cloud.
 */
public class QcCOSManager implements OSSManager {

	private COS client;

	private OSSProperties properties;

	QcCOSManager(COS client, OSSProperties properties) {
		this.client = client;
		this.properties = properties;
	}

	@Override
	public void upload(String fileName, InputStream inputStream) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(10);
		objectMetadata.setContentType("application/octet-stream");
		this.client.putObject(this.properties.getBucketName(),
				this.properties.getProperties().get("qc.prefix") + "/" + fileName, inputStream, objectMetadata);
	}

	@Override
	public void delete(String fileName) {
		this.client.deleteObject(this.properties.getBucketName(),
				this.properties.getProperties().get("qc.prefix") + "/" + fileName);
	}

}
