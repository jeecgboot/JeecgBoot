package org.jeecg.modules.im.base.util;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;

public class ZipUtil {
	private static final Logger log  = LoggerFactory.getLogger(ZipUtil.class);
	private static final int buffer = 2048;

	/**
	 * 压缩文件夹下得所有文件，并删除，最后将压缩的文件全部添加到以该目录命名的压缩文件里
	 *
	 * @param path 文件目录
	 */
	public static void zipFolder(String path) throws Exception {
		File folder = new File(path);
		if (!folder.exists()||!folder.isDirectory()) {
			throw new Exception("file is not a folder");
		}
		File[] files = folder.listFiles();
		if(files==null){
			throw new Exception("folder is empty");
		}
		File[] subFile;
		for (File file : files) {
			subFile = file.listFiles();
			if(subFile!=null){
				new ZipFile(path+File.separator+file.getName()+".zip").addFile(subFile[0]);
				FileUtils.deleteDirectory(file);
			}
		}
		files = folder.listFiles();
		if(files==null){
			throw new Exception("folder is empty");
		}
		for (File file : files) {
			new ZipFile(path+".zip").addFile(file);
		}
		folder.deleteOnExit();
	}

	public static File[] unzipWebp(String path) throws Exception {
		File zipFile = new File(path);
		if (!zipFile.exists()||!zipFile.isFile()) {
			throw new Exception("file not found");
		}
		String extractPath = path.substring(0,path.lastIndexOf("."));
		File extractFolder = new File(extractPath);
		if(!extractFolder.exists()){
			extractFolder.mkdirs();
		}
		new ZipFile(path).extractAll(extractPath);
		File[] files = extractFolder.listFiles();
		if(files==null||files[0]==null){
			throw new Exception("file error");
		}
		return files[0].listFiles();
	}
	//压缩文件里包含多个zip文件
	public static List<File> unzipAnimated(String zipPath,String targetDir) throws Exception {
		String extractPath = zipPath.substring(0, zipPath.lastIndexOf("."));
		File extractFolder = new File(extractPath);
		File targetFolder = new File(targetDir);
		File zipFile = new File(zipPath);
		if (!zipFile.exists() || !zipFile.isFile()) {
			throw new Exception("file not found");
		}
		try {
			if (!extractFolder.exists()) {
				extractFolder.mkdirs();
			}
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			}
			new ZipFile(zipPath).extractAll(extractPath);
			return Arrays.asList(extractFolder.listFiles());
		}catch (Exception e){
			e.printStackTrace();
			return Collections.emptyList();
		}finally {
			FileUtils.deleteQuietly(zipFile);
		}
	}
	//压缩文件里只包含一个文件夹
	public static List<File> unzipAnimatedJson(String zipPath,String targetDir) throws Exception {
		String extractPath = zipPath.substring(0, zipPath.lastIndexOf("."));
		File extractFolder = new File(extractPath);
		File targetFolder = new File(targetDir);
		File zipFile = new File(zipPath);
		if (!zipFile.exists() || !zipFile.isFile()) {
			throw new Exception("file not found");
		}
		try {
			if (!extractFolder.exists()) {
				extractFolder.mkdirs();
			}
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			}
			new ZipFile(zipPath).extractAll(extractPath);
			File[] files = extractFolder.listFiles();
			if (files == null || files[0] == null) {
				throw new Exception("file error");
			}
			//返回该文件夹下的所有文件
			File[] tgsFiles = files[0].listFiles();
			List<File> jsonFileList = new ArrayList<>();
			File jsonFile;
			String fileName;
			PrintWriter pw;
			for (File tgsFile : tgsFiles) {
				fileName = tgsFile.getName().substring(0, tgsFile.getName().lastIndexOf("."));
				jsonFile = new File(targetDir + fileName + ".json");
				//gzip格式，字符串，写入json文件
				byte[] bytes = cn.hutool.core.util.ZipUtil.unGzip(Files.newInputStream(tgsFile.toPath()));
				pw = new PrintWriter(jsonFile);
				pw.print(new String(bytes));
				pw.flush();
				pw.close();
				jsonFileList.add(jsonFile);
			}
			return jsonFileList;
		}catch (Exception e){
			e.printStackTrace();
			return Collections.emptyList();
		}finally {
			FileUtils.deleteDirectory(extractFolder);
			FileUtils.deleteQuietly(zipFile);
		}
	}

	public static List<File> unzipGif(String path) throws Exception {
		File zipFile = new File(path);
		if (!zipFile.exists()||!zipFile.isFile()) {
			throw new Exception("file not found");
		}
		String extractPath = path.substring(0,path.lastIndexOf("."));
		File extractFolder = new File(extractPath);
		if(!extractFolder.exists()){
			extractFolder.mkdirs();
		}
		new ZipFile(path).extractAll(extractPath);
		File[] files = extractFolder.listFiles();
		if(files==null||files[0]==null){
			throw new Exception("file error");
		}
		return Arrays.asList(files);
	}

	public static void main(String[] args) throws Exception {
//		zipFolder("I:\\batch_tgs (9)\\743710627");
//		unzipWebp("I:\\pithon91_batch_webp.zip");
	}
}