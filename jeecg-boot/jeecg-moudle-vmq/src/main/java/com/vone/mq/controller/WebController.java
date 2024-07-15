package com.vone.mq.controller;

import com.google.gson.Gson;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vone.mq.config.EmailUtils;
import com.vone.mq.dto.CommonRes;
import com.vone.mq.dto.CreateOrderRes;
import com.vone.mq.entity.PayOrder;
import com.vone.mq.service.AdminService;
import com.vone.mq.service.WebService;
import com.vone.mq.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "开放接口",description = "API支付接口")
public class WebController {

    @Value("${token.expire.ip}")
    private int expireCount;

    @Autowired
    private WebService webService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/enQrcode")
    public void enQrcode(HttpServletResponse resp, String url) throws IOException {
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 200;//图片的宽度
                int height = 200;//高度
                stream = resp.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }

    @RequestMapping("/deQrcode")
    public CommonRes deQrcode(String base64) {
        if (base64 != null && !"".equals(base64)) {
            try {
                MultiFormatReader multiFormatReader = new MultiFormatReader();
                byte[] bytes1 = Base64.getDecoder().decode(base64);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                BufferedImage image = ImageIO.read(bais);
                //定义二维码参数
                Map hints = new HashMap();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                //获取读取二维码结果
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                Result result = multiFormatReader.decode(binaryBitmap, hints);
                //stream.print(result.getText());
                return ResUtil.success(result.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResUtil.error();
    }

    @RequestMapping("/deQrcode2")
    public CommonRes deQrcode2(@RequestParam("file") MultipartFile file) {
        if (file != null) {
            try {
                MultiFormatReader multiFormatReader = new MultiFormatReader();
                byte[] bytes1 = file.getBytes();
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
                BufferedImage image = ImageIO.read(bais);
                //定义二维码参数
                Map hints = new HashMap();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                //获取读取二维码结果
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                Result result = multiFormatReader.decode(binaryBitmap, hints);

                log.info(result.getText());
                return ResUtil.success(result.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResUtil.error();
    }

    @RequestMapping("/md5")
    public String md5(HttpSession session, String content) {
        String username = (String) session.getAttribute("username");
        return webService.getMd5(username,content);
    }

    @GetMapping(value = "/getToken")
    @ApiOperation(value = "获取token")
    public String getToken(@ApiIgnore HttpSession session, @RequestParam(required = false) String user, @RequestParam(required = false) String pass) {
        String token = (String) session.getAttribute("token");
        if (StringUtils.isEmpty(token)) {
            if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pass)) {
                return null;
            }
            CommonRes res = adminService.login(user,pass);
            if (res.getCode() == 1) {
                token = JWTUtil.getToken(user,pass);
            }
        }
        return token;
    }


    /**
     * 创建订单
     *
     * @param payOrder    0返回json数据 1跳转到支付页面
     * @return
     */
    @PostMapping("/createOrder")
    @ApiOperation(value = "创建订单")
    public String createOrder(@RequestBody PayOrder payOrder, HttpServletRequest request) {
        if (StringUtils.isEmpty(payOrder.getPayId())) {
            return new Gson().toJson(ResUtil.error("请传入商户订单号"));
        }
        if (StringUtils.isEmpty(payOrder.getType())) {
            return new Gson().toJson(ResUtil.error("请传入支付方式=>1|微信 2|支付宝"));
        }
        if (payOrder.getType()<1 || payOrder.getType()>2) {
            return new Gson().toJson(ResUtil.error("支付方式错误=>1|微信 2|支付宝"));
        }
        if (!StringUtils.isEmpty(payOrder.getEmail()) && !EmailUtils.checkEmail(payOrder.getEmail())) {
            return new Gson().toJson(ResUtil.error("请填写正确的邮箱地址"));
        }

        String ip = HttpRequest.getIpAddr(request);
        String temp = redisTemplate.opsForValue().get(ip);
        Long expire = redisTemplate.getExpire(ip, TimeUnit.SECONDS);
        log.info("{},{}",ip,expire);
        if(StringUtils.isNotBlank(temp)){
            return new Gson().toJson(ResUtil.error("您提交的太频繁了，请"+expire+"秒后再试"));
        }
        Double priceD;
        try {
            priceD = Double.valueOf(payOrder.getPrice());
        } catch (Exception e){
            return new Gson().toJson(ResUtil.error("请传入订单金额"));
        }

        if (priceD == null) {
            return new Gson().toJson(ResUtil.error("请传入订单金额"));
        }
        if (priceD < 0) {
            return new Gson().toJson(ResUtil.error("订单金额必须大于0"));
        }

        if (StringUtils.isEmpty(payOrder.getSign())) {
            return new Gson().toJson(ResUtil.error("请传入签名"));
        }
        int isHtml = payOrder.getIsHtml();
        CommonRes commonRes = webService.createOrder(payOrder);
        //记录缓存
        redisTemplate.opsForValue().set(ip,"added",expireCount, TimeUnit.SECONDS);
        if (isHtml == 0) {
            String res = new Gson().toJson(commonRes);
            return res;
        } else {
            CreateOrderRes c = (CreateOrderRes) commonRes.getData();
            if (c == null) {
                return commonRes.getMsg();
            } else {
                return "<script>window.location.href = '/payPage/pay.html?orderId=" + c.getOrderId() + "'</script>";
            }
        }
    }

    @PostMapping("/closeOrder")
    @ApiOperation(value = "关闭订单")
    public CommonRes closeOrder(String orderId, String sign) {
        if (orderId == null) {
            return ResUtil.error("请传入云端订单号");
        }
        if (sign == null) {
            return ResUtil.error("请传入签名");
        }
        return webService.closeOrder(orderId, sign);
    }

    @RequestMapping("/appHeart")
    public CommonRes appHeart(String t, String sign) {
        return webService.appHeart(t, sign);
    }

    @RequestMapping("/appPush")
    public CommonRes appPush(Integer type, String price, String t, String sign) {
        return webService.appPush(type, price, t, sign);
    }

    @GetMapping(value = "/getOrder")
    @ApiOperation(value = "查询订单信息")
    public CommonRes getOrder(String orderId) {
        if (orderId == null) {
            return ResUtil.error("请传入订单编号");
        }
        return webService.getOrder(orderId);
    }

    @GetMapping(value = "/checkOrder")
    @ApiOperation(value = "查询订单状态")
    public CommonRes checkOrder(String orderId) {
        if (orderId == null) {
            return ResUtil.error("请传入订单编号");
        }
        return webService.checkOrder(orderId);
    }

    @RequestMapping("/getState")
    public CommonRes getState(String t, String sign) {
        if (t == null) {
            return ResUtil.error("请传入t");
        }
        if (sign == null) {
            return ResUtil.error("请传入sign");
        }
        return webService.getState(t, sign);
    }
}
