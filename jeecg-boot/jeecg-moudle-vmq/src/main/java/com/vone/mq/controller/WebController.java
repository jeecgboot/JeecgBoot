package com.vone.mq.controller;

import com.google.gson.Gson;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vone.mq.dto.CommonRes;
import com.vone.mq.dto.CreateOrderRes;
import com.vone.mq.service.WebService;
import com.vone.mq.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController {

    @Autowired
    private WebService webService;

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

                //stream.print(result.getText());
                System.out.println(result.getText());
                return ResUtil.success(result.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResUtil.error();
    }

    /**
     * 创建订单
     *
     * @param payId     商户订单号
     * @param param     订单保存的信息
     * @param type      支付方式 1|微信 2|支付宝
     * @param price     订单价格
     * @param notifyUrl 异步通知地址，如果为空则使用系统后台设置的地址
     * @param returnUrl 支付完成后同步跳转地址，将会携带参数跳转
     * @param sign      签名认证 签名方式为 md5(payId + param + type + price + 通讯密钥)
     * @param isHtml    0返回json数据 1跳转到支付页面
     * @return
     */
    @RequestMapping("/createOrder")
    public String createOrder(String payId, String param, Integer type, String price, String notifyUrl, String returnUrl, String sign, Integer isHtml) {
        if (payId == null || payId.equals("")) {
            return new Gson().toJson(ResUtil.error("请传入商户订单号"));
        }
        if (type == null) {
            return new Gson().toJson(ResUtil.error("请传入支付方式=>1|微信 2|支付宝"));
        }
        if (type != 1 && type != 2) {
            return new Gson().toJson(ResUtil.error("支付方式错误=>1|微信 2|支付宝"));
        }


        Double priceD;
        try {
            priceD = Double.valueOf(price);
        } catch (Exception e){
            return new Gson().toJson(ResUtil.error("请传入订单金额"));
        }

        if (priceD == null) {
            return new Gson().toJson(ResUtil.error("请传入订单金额"));
        }
        if (priceD < 0) {
            return new Gson().toJson(ResUtil.error("订单金额必须大于0"));
        }


        if (sign == null || sign.equals("")) {
            return new Gson().toJson(ResUtil.error("请传入签名"));
        }
        if (param == null) {
            param = "";
        }
        if (isHtml == null) {
            isHtml = 0;
        }
        CommonRes commonRes = webService.createOrder(payId, param, type, price, notifyUrl, returnUrl, sign);
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

    @RequestMapping("/closeOrder")
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

    @RequestMapping("/getOrder")
    public CommonRes getOrder(String orderId) {
        if (orderId == null) {
            return ResUtil.error("请传入订单编号");
        }
        return webService.getOrder(orderId);
    }

    @RequestMapping("/checkOrder")
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
