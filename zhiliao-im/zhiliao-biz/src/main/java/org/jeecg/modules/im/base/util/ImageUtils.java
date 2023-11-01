package org.jeecg.modules.im.base.util;

import com.gif4j.*;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

public class ImageUtils {

    public static final String WEBP = "webp";
    public static final String WEBP_SUFFIX = ".webp";
    /**
     * jpg文件格式
     */
    public static String JPG = "jpg";

    /**
     * 图片裁剪工具
     *
     * @param in              ******************输入流
     * @param readImageFormat *****图片格式
     * @param x               *******************起始点x坐标
     * @param y               *******************起始点y坐标
     * @param w               *******************裁剪宽度
     * @param h               *******************裁剪高度
     * @return is*****************输出流
     * @throws IOException
     * @author Jieve
     */
    public static InputStream cutImage(InputStream in, String readImageFormat, int x, int y, int w, int h) throws IOException {

        Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(readImageFormat);
        ImageReader reader = (ImageReader) iterator.next();
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, w, h);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, readImageFormat, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }

    /**
     * png文件格式
     */
    public static String PNG = "png";

    /**
     * 根据图片对象获取对应InputStream
     *
     * @param image
     * @param readImageFormat
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(BufferedImage image, String readImageFormat) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, readImageFormat, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return is;
    }

    /**
     * 生成缩略图
     *
     * @param in
     * @param imageFormat
     * @return
     * @throws IOException
     */
    public static void createThumbnailsResize(InputStream in, int width, int height, String imageFormat, String targetPath) throws IOException {
        if (imageFormat.equalsIgnoreCase("jpg")) {
            createThumbnailsResize(in, width, height, targetPath);
        } else if (imageFormat.equalsIgnoreCase("png")) {
            BufferedImage image = Thumbnails.of(in).scale(1f).outputFormat(JPG).asBufferedImage();
            InputStream is = getInputStream(image, JPG);
            createThumbnailsResize(is, width, height, targetPath);
        }
    }

    /**
     * 转化为jpg格式
     *
     * @return
     * @throws IOException
     */
    public static BufferedImage toJPG(BufferedImage image) throws IOException {

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage image_ = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = image_.createGraphics();
        graphic.setColor(Color.WHITE);
        graphic.fillRect(0, 0, width, height);
        graphic.drawRenderedImage(image, null);
        graphic.dispose();
        return image_;
    }

    /**
     * 生成缩略图
     *
     * @param in
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    private static InputStream createThumbnails(InputStream in, int width, int height) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnailator.createThumbnail(in, os, width, height);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }

    /**
     * 生成缩略图
     *
     * @param in
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    private static void createThumbnailsResize(InputStream in, int width, int height, String targetPath) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnailator.createThumbnail(in, os, width, height);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        FileOutputStream fos = new FileOutputStream(targetPath);
        byte[] b = new byte[1024];
        while ((is.read(b)) != -1) {
            fos.write(b);// 写入数据
        }
        is.close();
        fos.close();// 保存数据
    }
    private static void doCreateThumbnailsScale(File origin, int width,  String targetPath) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String format = JPG;
        if(origin.getName().toUpperCase().endsWith("WEBP")){
            format = WEBP;
        }
        BufferedImage image = Thumbnails.of(origin).scale(1f).outputFormat(format).asBufferedImage();
        InputStream in  = new FileInputStream(origin);
        if(image.getWidth()<width){
            width = image.getWidth()*10/10;
        }
        Thumbnailator.createThumbnail(in, os, width, width*image.getHeight()/image.getWidth());
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        FileOutputStream fos = new FileOutputStream(targetPath);
        byte[] b = new byte[1024];
        while ((is.read(b)) != -1) {
            fos.write(b);// 写入数据
        }
        is.close();
        fos.close();// 保存数据
    }
    //指定宽高
    public static void createThumbnailsResize(File originFile, int width,int height, String targetPath) throws IOException {
        if(targetPath.endsWith(".gif")){
            gifReSize(originFile,new File(targetPath),width,height);
        }else {
            createThumbnailsResize(new FileInputStream(originFile), width, height, targetPath);
        }
    }
    //根据宽度获取缩放值计算高度
    public static void createThumbnailsScale(File originFile, int width, String targetPath) throws IOException {
        if(targetPath.endsWith(".gif")){
            gifScale(originFile,new File(targetPath),width);
        }else {
            doCreateThumbnailsScale(originFile, width, targetPath);
        }
    }

    /**
     * 添加水印
     *
     * @param in
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static InputStream addWaterMark(InputStream in, InputStream waterMark, int width, int height) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(in).size(width, height).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(waterMark), 0.4f).outputQuality(0.8f).toOutputStream(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        waterMark.close();
        os.close();
        return is;
    }

    /**
     * 居中裁剪
     *
     * @param image
     * @return
     */
    public static BufferedImage clipCenter(BufferedImage image) {

        int height = image.getHeight();
        int width = image.getWidth();
        int size = height >= width ? width : height;
        int temp = 0;
        if (height >= width) {
            temp = (height - width) / 2;
            image = image.getSubimage(0, temp, size, size);
        } else {
            temp = (width - height) / 2;
            image = image.getSubimage(temp, 0, size, size);
        }

        return image;
    }

    /**
     * 裁剪图片
     *
     * @param image
     * @param x
     * @param y
     * @param size
     * @return
     */
    public static BufferedImage cutImage(BufferedImage image, int x, int y, int size) {

        int height = image.getHeight();
        int width = image.getWidth();
        if ((width >= (x + size)) && (height >= (y + size))) {
            image = image.getSubimage(x, y, size, size);
        } else {
            int temp = ((height - y) >= (width - x)) ? (width - x) : (height - y);
            image = image.getSubimage(x, y, temp, temp);
        }

        return image;
    }

    /**
     * 检查格式是否合法
     *
     * @param imageType
     * @return
     */
    public static boolean checkType(String imageType) {

        boolean flag = false;
        if (JPG.equalsIgnoreCase(imageType) || PNG.equalsIgnoreCase(imageType)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 压缩图片
     * 默认输出50%质量图片
     *
     * @param image
     * @return
     * @throws IOException
     */
    public static InputStream compress(BufferedImage image, String readImageFormat) throws IOException {

        InputStream in = ImageUtils.getInputStream(image, readImageFormat);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(in).size(image.getWidth(), image.getHeight()).outputQuality(0.5f).toOutputStream(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        in.close();
        os.close();
        return is;
    }

    /**
     * cwebp [options] input_file -o output_file.webp
     *
     * @param inputFile
     * @param outputFile
     * @param quality
     * @return
     */
    public static boolean executeCWebp(String inputFile, String outputFile, Integer quality) {
        boolean result = false;
        String cwebpPath = "cwebp";
        if (inputFile.endsWith(".gif"))
            cwebpPath = "gif2webp";
        try {
//            String chmodCommand = "chmod 755 " + cwebpPath;
//            Runtime.getRuntime().exec(chmodCommand).waitFor();

            StringBuilder command = new StringBuilder(cwebpPath);
            command.append(" -q " + (quality == 0 ? 75 : quality));
            command.append(" " + inputFile);
            command.append(" -o " + outputFile);
//            command.append(" -lossy -m 4 ");

            Runtime.getRuntime().exec(command.toString());
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将gif转为webm
     * ffmpeg -i my-animation.gif -c vp9 -b:v 0 -crf 41 my-animation.webm
     * @param inputFile
     * @param outputFile
     * @param quality
     * @return
     */
    public static boolean gif2Webm(String inputFile, String outputFile) {
        if (!inputFile.endsWith(".gif"))
            return false;
        boolean result = false;
        String cmd = "ffmpeg";
        try {
            StringBuilder command = new StringBuilder(cmd);
            command.append(" -i "+ inputFile);
            command.append("  -c vp9 -b:v 0 -crf 41 " + outputFile);
            Runtime.getRuntime().exec(command.toString());
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将gif转为mp4
     * ffmpeg -i my-animation.gif -vf "crop=trunc(iw/2)*2:trunc(ih/2)*2" -b:v 0 -crf 25 -f mp4 -vcodec libx264 -pix_fmt yuv420p my-animation.mp4
     * @param inputFile
     * @param outputFile
     * @return
     */
    public static boolean gif2Mp4(String inputFile, String outputFile) {
        if (!inputFile.endsWith(".gif"))
            return false;
        boolean result = false;
        String cmd = "ffmpeg";
        try {
            StringBuilder command = new StringBuilder(cmd);
            command.append(" -i "+ inputFile);
            command.append(" -vf \"crop=trunc(iw/2)*2:trunc(ih/2)*2\" -b:v 0 -crf 25 -f mp4 -vcodec libx264 -pix_fmt yuv420p " + outputFile);
            Runtime.getRuntime().exec(command.toString());
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将gif动图重新调整大小并且输出
     * @param src 源文件
     * @param dest 生成文件
     * @param width 生成文件的宽度
     * @param height 生成文件的高度
     * @throws IOException
     */
    public static void gifReSize(File src, File dest, int width, int height) throws IOException {
        GifImage srcImage = GifDecoder.decode(src);
//主要调用的就是GifTransformer种的resize方法进行图片尺寸的调整
        GifImage resizeImage = GifTransformer.resize(srcImage, width, height, true);
        GifEncoder.encode(resizeImage, dest);
    }


    /**
     * 将gif动图重新调整大小并且输出
     * @param src 源文件
     * @param dest 生成文件
     * @param wQuotiety 调整的比例，比如80%就是0.8
     * @param hQuotiety 调整的比例，比如80%就是0.8
     * @throws IOException
     */

    public static void gifScale(File src, File dest, double width) throws IOException {
        GifImage srcImage = GifDecoder.decode(src);
        double wQuotiety = ((int)(width/srcImage.getCurrentLogicWidth()*100))/10*0.1;
        double hQuotiety = wQuotiety;
        GifImage scaleImg = GifTransformer.scale(srcImage, wQuotiety, hQuotiety, true);
        GifEncoder.encode(scaleImg, dest);
    }


//    /**
//     * 对gif图剪切，参数是坐标和宽高
//     */
//    public static void gifCut(File src, File dest, int x, int y, int w, int h) throws IOException {
//        Rectangle rectangle = new Rectangle(x, y, w, h);
//        GifImage srcImg = GifDecoder.decode(src);
//        GifImage cropImg = GifTransformer.crop(srcImg, rectangle);
//        GifEncoder.encode(cropImg, dest);
//    }


    /**
     * 加水印，参数是文字
     */

    public static void shuiyin(File src, File dest, String text) throws IOException {
        GifImage srcImg = GifDecoder.decode(src);
        TextPainter textPainter = new TextPainter(new Font("微软雅黑", Font.BOLD, 12));
        textPainter.setOutlinePaint(Color.WHITE);
        BufferedImage renderedWatermarkText = textPainter.renderString(text, true);
        Watermark watermark = new Watermark(renderedWatermarkText, Watermark.LAYOUT_TOP_LEFT);
        GifImage applyImg = watermark.apply(srcImg, true);
        GifEncoder.encode(applyImg, dest);
    }


    public static void main(String[] args) {
        System.out.println(executeCWebp("I:/1.jpg","I:/2.webp",75));;
        try {
            gifReSize(new File("I:\\data\\sgim\\resource\\sticker\\o\\5\\1f79ce4f90eb4db3ae8d382da99a679a.gif"),new File("I:\\data\\sgim\\resource\\sticker\\o\\5\\1f79ce4f90eb4db3ae8d382da99a679a-2.gif"),64,64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1. 传入图片文件路径，返回file对象
     * @param imgFilePath 图片文件路径(比如转换图片为F:/1.png 那么转换后的webp文件为：F:/1.png.webp)
     * @return
     */
    public static File toWebpFile(String imgFilePath){
        File imgFile = new File(imgFilePath);
        File webpFile = new File(imgFilePath + WEBP_SUFFIX);
        try {
            BufferedImage bufferedImage = ImageIO.read(imgFile);
            ImageIO.write(bufferedImage, WEBP, webpFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return webpFile;
    }

    /**
     * 2. 传入图片url，返回file对象
     * @param imgUrlPath 图片路径url
     * @param webpFilePath 生成的webp文件路径
     * @return
     */
    public static File toWebpFile(String imgUrlPath, String webpFilePath){
        File webpFile = new File(webpFilePath);
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imgUrlPath));
            ImageIO.write(bufferedImage, WEBP, webpFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return webpFile;
    }

    /**
     * 3. 传入图片文件路径，返回InputStream
     * @param imgFilePath 图片文件路径(比如转换图片为F:/1.png 那么转换后的webp文件为：F:/1.png.webp)
     * @return
     */
    public static InputStream toWebpStream(String imgFilePath){
        File imgFile = new File(imgFilePath);
        File webpFile = new File(imgFilePath + WEBP_SUFFIX);
        FileInputStream fis = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imgFile);
            ImageIO.write(bufferedImage, WEBP, webpFile);
            fis = new FileInputStream(webpFile);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fis;
    }

    /**
     * 4. 传入图片url，返回InputStream
     * @param imgUrlPath 图片路径url
     * @param webpFilePath 生成的webp文件路径
     * @return
     */
    public static InputStream toWebpStream(String imgUrlPath, String webpFilePath){
        File webpFile = new File(webpFilePath);
        FileInputStream fis = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imgUrlPath));
            ImageIO.write(bufferedImage, WEBP, webpFile);
            fis = new FileInputStream(webpFile);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fis;
    }
}