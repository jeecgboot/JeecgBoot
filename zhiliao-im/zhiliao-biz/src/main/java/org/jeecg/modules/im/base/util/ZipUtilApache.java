package org.jeecg.modules.im.base.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;

/**
 * chentao
 */
public class ZipUtilApache {
	private static final Logger log  = LoggerFactory.getLogger(ZipUtilApache.class);
	private static final int buffer = 2048;

	/**
	 * 解压Zip文件
	 *
	 * @param path 文件目录
	 */
	public static void unZip(String path) throws Exception {
		int count = -1;
		String savepath = "";
		File file = null;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		savepath = new File(path).getParent() + File.separator; //保存解压文件目录
		new File(savepath).mkdir(); //创建保存目录
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(path, "gbk"); //解决中文乱码问题
			Enumeration<?> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				byte buf[] = new byte[buffer];
				ZipArchiveEntry entry = (ZipArchiveEntry) entries.nextElement();
				String filename = entry.getName();
				boolean ismkdir = false;
				if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
					ismkdir = true;
				}
				filename = savepath + filename;
				if (entry.isDirectory()) { //如果是文件夹先创建
					file = new File(filename);
					file.mkdirs();
					continue;
				}
				file = new File(filename);
				if (!file.exists()) { //如果是目录先创建
					if (ismkdir) {
						new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
					}
				}
				file.createNewFile(); //创建文件
				is = zipFile.getInputStream(entry);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, buffer);
				while ((count = is.read(buf)) > -1) {
					bos.write(buf, 0, count);
				}
				bos.flush();
				bos.close();
				fos.close();
				is.close();
			}
			zipFile.close();
		} catch (IOException ioe) {
			log.error("ZipUtilApache-unZip-IOException",ioe);
			throw new Exception("解压缩文件["+path+"]失败");
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (Exception e) {
				log.error("ZipUtilApache-unZip-Exception",e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		unZip("C:\\Users\\trayc\\Downloads\\Telegram Desktop\\animatedemojies_tgs\\animatedemojies\\tgs\\AnimatedEmojies_file_121683666.tgs");
	}
}