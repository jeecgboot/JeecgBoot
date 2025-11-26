package org.jeecg.modules.system.util;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * 登录验证码工具类
 * @date  2025-09-11
 * @author AI优化
 */
public class RandImageUtil {

    // 静态初始化块，解决无头环境字体配置问题
    static {
        System.setProperty("java.awt.headless", "true");
    }

    public static final String KEY = "JEECG_LOGIN_KEY";

    /** 验证码图片宽度 */
    private static final int WIDTH = 105;
    
    /** 验证码图片高度 */
    private static final int HEIGHT = 35;
    
    /** 干扰线数量 */
    private static final int INTERFERENCE_LINE_COUNT = 200;
    
    /** 干扰线宽度 */
    private static final int LINE_WIDTH = 2;
    
    /** 图片格式 */
    private static final String IMG_FORMAT = "JPEG";
    
    /** base64 图片前缀 */
    private static final String BASE64_PREFIX = "data:image/jpg;base64,";
    
    /** 字符间距 */
    private static final int CHAR_SPACING = 23;
    
    /** 字体大小 */
    private static final int FONT_SIZE = 24;
    
    /** 字符Y轴偏移 */
    private static final int CHAR_Y_OFFSET = 26;
    
    /** 字符X轴起始偏移 */
    private static final int CHAR_X_OFFSET = 8;

    /**
     * 直接通过response输出验证码图片
     * 
     * @param response HTTP响应对象
     * @param verifyCode 验证码字符串
     * @throws IOException 输出异常
     */
    public static void generate(HttpServletResponse response, String verifyCode) throws IOException {
        if (response == null || verifyCode == null || verifyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("参数不能为空");
        }
        
        try {
            BufferedImage image = createVerifyCodeImage(verifyCode);
            ImageIO.write(image, IMG_FORMAT, response.getOutputStream());
        } catch (Exception e) {
            throw new IOException("生成验证码图片失败", e);
        }
    }

    /**
     * 生成验证码的base64字符串
     * 
     * @param verifyCode 验证码字符串
     * @return base64编码的图片字符串
     * @throws IOException 生成异常
     */
    public static String generate(String verifyCode) throws IOException {
        if (verifyCode == null || verifyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("验证码不能为空");
        }
        
        try {
            BufferedImage image = createVerifyCodeImage(verifyCode);
            
            try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
                ImageIO.write(image, IMG_FORMAT, byteStream);
                byte[] bytes = byteStream.toByteArray();
                String base64 = Base64.getEncoder().encodeToString(bytes).trim();
                // 清理换行符
                base64 = base64.replaceAll("[\r\n]", "");
                return BASE64_PREFIX + base64;
            }
        } catch (Exception e) {
            throw new IOException("生成验证码base64失败", e);
        }
    }

    /**
     * 创建验证码图像
     * 
     * @param verifyCode 验证码字符串
     * @return 验证码图像
     */
    private static BufferedImage createVerifyCodeImage(String verifyCode) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = null;
        
        try {
            graphics = (Graphics2D) image.getGraphics();
            
            // 设置图形渲染质量
            setupRenderingHints(graphics);
            
            // 绘制背景
            drawBackground(graphics);
            
            // 绘制边框
            drawBorder(graphics);
            
            // 获取安全的随机数生成器
            SecureRandom random = createSecureRandom();
            
            // 绘制干扰线
            drawInterferenceLines(graphics, random);
            
            // 绘制验证码字符
            drawVerifyCodeText(graphics, verifyCode);
            
        } catch (Exception e) {
            // 如果绘制失败，创建简单的错误图像
            return createErrorImage(verifyCode);
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
        
        return image;
    }

    /**
     * 设置图形渲染质量
     */
    private static void setupRenderingHints(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * 绘制白色背景
     */
    private static void drawBackground(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * 绘制边框
     */
    private static void drawBorder(Graphics2D graphics) {
        graphics.setColor(Color.GRAY);
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
    }

    /**
     * 创建安全的随机数生成器
     */
    private static SecureRandom createSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            return new SecureRandom();
        }
    }

    /**
     * 绘制干扰线
     */
    private static void drawInterferenceLines(Graphics2D graphics, SecureRandom random) {
        for (int i = 0; i < INTERFERENCE_LINE_COUNT; i++) {
            graphics.setColor(getRandomColor(150, 200, random));
            
            // 确保干扰线在边框内
            int x1 = random.nextInt(WIDTH - LINE_WIDTH - 1) + 1;
            int y1 = random.nextInt(HEIGHT - LINE_WIDTH - 1) + 1;
            int x2 = x1 + random.nextInt(LINE_WIDTH);
            int y2 = y1 + random.nextInt(LINE_WIDTH);
            
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 绘制验证码文本
     */
    private static void drawVerifyCodeText(Graphics2D graphics, String verifyCode) {
        graphics.setColor(Color.BLACK);
        Font font = createSafeFont();
        graphics.setFont(font);
        
        for (int i = 0; i < verifyCode.length(); i++) {
            char character = verifyCode.charAt(i);
            int x = i * CHAR_SPACING + CHAR_X_OFFSET;
            graphics.drawString(String.valueOf(character), x, CHAR_Y_OFFSET);
        }
    }

    /**
     * 创建安全的字体，避免字体配置问题
     */
    private static Font createSafeFont() {
        // 使用逻辑字体名称，在所有平台都可用
        String[] safeFontNames = {Font.SERIF, Font.SANS_SERIF, Font.MONOSPACED, "Dialog"};
        
        for (String fontName : safeFontNames) {
            try {
                Font font = new Font(fontName, Font.BOLD, FONT_SIZE);
                if (font.getFamily() != null) {
                    return font;
                }
            } catch (Exception e) {
                // 继续尝试下一个字体
            }
        }
        
        // 最后的回退方案
        return new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE);
    }

    /**
     * 创建错误图像（当正常绘制失败时使用）
     */
    private static BufferedImage createErrorImage(String verifyCode) {
        BufferedImage errorImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = null;
        
        try {
            graphics = (Graphics2D) errorImage.getGraphics();
            
            // 白色背景
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, WIDTH, HEIGHT);
            
            // 黑色边框
            graphics.setColor(Color.BLACK);
            graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
            
            // 尝试绘制验证码
            try {
                graphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
                graphics.setColor(Color.BLUE);
                for (int i = 0; i < verifyCode.length(); i++) {
                    graphics.drawString(String.valueOf(verifyCode.charAt(i)), 
                                      i * CHAR_SPACING + CHAR_X_OFFSET, CHAR_Y_OFFSET);
                }
            } catch (Exception fontException) {
                // 如果连基本字体都失败，显示ERROR
                graphics.setColor(Color.RED);
                graphics.drawString("ERROR", 10, 20);
            }
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
        
        return errorImage;
    }

    /**
     * 获取指定范围内的随机颜色
     * 
     * @param minColorValue 最小颜色值
     * @param maxColorValue 最大颜色值
     * @param random 随机数生成器
     * @return 随机颜色
     */
    private static Color getRandomColor(int minColorValue, int maxColorValue, Random random) {
        // 确保颜色值在有效范围内
        int min = Math.max(0, Math.min(minColorValue, 255));
        int max = Math.max(min, Math.min(maxColorValue, 255));
        
        int range = max - min;
        if (range == 0) {
            return new Color(min, min, min);
        }
        
        int red = min + random.nextInt(range);
        int green = min + random.nextInt(range);
        int blue = min + random.nextInt(range);
        
        return new Color(red, green, blue);
    }
}
