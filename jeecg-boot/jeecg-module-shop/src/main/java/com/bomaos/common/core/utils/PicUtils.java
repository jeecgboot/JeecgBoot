package com.bomaos.common.core.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 图片压缩Utils
 */
public class PicUtils {

    private static final Logger logger = LoggerFactory.getLogger(PicUtils.class);

    /*public static void main(String[] args) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(new File("D:/a.png"));
        long l = System.currentTimeMillis();
        bytes = PicUtils.compressPicForScale(bytes, 30, "x");// 图片小于300kb
        System.out.println(System.currentTimeMillis() - l);
        FileUtils.writeByteArrayToFile(new File("D:/aa.png"), bytes);
    }*/

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @param imageId     影像编号
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize, String imageId) {
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        try {
            while (imageBytes.length > desFileSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            logger.info("【图片压缩】imageId={} | 图片原大小={}kb | 压缩后大小={}kb", imageId, srcSize / 1024, imageBytes.length / 1024);
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * 图片指定宽度高度
     *
     * @param path   图片地址
     * @param width  图片宽度
     * @param height 图片高度
     * @return
     */
    public static void changeSize(String path, int width, int height, HttpServletResponse response) {
        try {
            File file = new File(FileUploadUtil.UPLOAD_FILE_DIR + path);
            if (!file.exists()) {
                // 文件不存在
                return;
            }
            byte[] imageBytes = FileUtils.readFileToByteArray(file);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = Thumbnails.of(inputStream).size(width, height).keepAspectRatio(false).asBufferedImage();
            //以JPEG格式向客户端发送
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "JPG", os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
