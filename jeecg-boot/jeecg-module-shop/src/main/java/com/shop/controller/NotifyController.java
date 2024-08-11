package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.easysdk.factory.Factory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.dto.NotifyDTO;
import com.shop.entity.Cards;
import com.shop.entity.XunhuNotIfy;
import com.shop.service.CardsService;
import com.shop.common.core.pays.budpay.BudpayUtil;
import com.shop.common.core.pays.epay.EpayUtil;
import com.shop.common.core.pays.epusdt.entity.EpusdtNotify;
import com.shop.common.core.pays.epusdt.sendPay;
import com.shop.common.core.pays.mqpay.VmqPay;
import com.shop.common.core.pays.payjs.SignUtil;
import com.shop.common.core.pays.paypal.PaypalSend;
import com.shop.common.core.pays.xunhupay.PayUtils;
import com.shop.common.core.utils.DateUtil;
import com.shop.common.core.utils.FormCheckUtil;
import com.shop.common.core.utils.RequestParamsUtil;
import com.shop.common.core.utils.StringUtil;
import com.shop.common.core.web.JsonResult;
import com.shop.service.EmailService;
import com.shop.entity.Orders;
import com.shop.service.OrdersService;
import com.shop.entity.Products;
import com.shop.service.ProductsService;
import com.shop.common.result.Budpay;
import com.shop.common.util.SynchronizedByKeyService;
import com.shop.entity.Pays;
import com.shop.entity.ShopSettings;
import com.shop.service.PaysService;
import com.shop.service.ShopSettingsService;
import com.shop.entity.Website;
import com.shop.service.WebsiteService;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Controller
@Transactional
public class NotifyController {

    @Autowired
    public HttpServletRequest request;

    @Autowired
    private PaysService paysService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private SynchronizedByKeyService synchronizedByKeyService;

    /**
     * 返回成功xml
     */
    private final String WxpayresXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    private final String WxpayH5resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    private final String resFailXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";

    @RequestMapping("/budpay/notifyUrl")
    @ResponseBody
    public String budpayNotify(Budpay budpay) {

        String driver = "";
        if (budpay.getPay_type().equals("wechat")) {
            driver = "budpay_wechat";
        } else if (budpay.getPay_type().equals("alipay")) {
            driver = "budpay_alipay";
        }
        /**
         * 防止破解
         */
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", budpay.getOut_trade_no()));
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver).eq("username",orders.getUsername()));
        if (!orders.getPayType().equals(pays.getDriver())) {
            return "不支持该支付类型";
        }

        Map mapTypes = JSON.parseObject(pays.getConfig());

        // 你的key 在后台获取
        String secret_key = mapTypes.get("key").toString();
        String json = JSON.toJSONString(budpay);
        Map<String, Object> map = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {});

        String sign1 = BudpayUtil.createSign(map, secret_key).toUpperCase();
        if (sign1.equals(budpay.getSign())) {
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(budpay.getOut_trade_no(), () -> {
                String returnBig1 = returnBig(budpay.getAmount(), budpay.getAmount(), budpay.getOut_trade_no(), budpay.getTrade_no(), budpay.getName(), "success", "final");
                notifyText.set(returnBig1);
            });
            return notifyText.get();
        } else {
            return "签名错误！";
        }
    }

    /**
     * 虎皮椒支付通知
     *
     * @param response
     * @return
     */
    @RequestMapping("/budpay/returnUrl")
    @ResponseBody
    public void budpayReturnUrl(HttpServletResponse response) throws IOException {
        // 记得 map 第二个泛型是数组 要取 第一个元素 即[0]
        String contextPath = request.getContextPath();
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String url = contextPath + "/pay/state/" + params.get("out_trade_no");
        response.sendRedirect(url);
    }

    @RequestMapping("/mqpay/notifyUrl")
    @ResponseBody
    public String notifyUrl() {
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String param = params.get("param");
        String price = params.get("price");
        String money = params.get("reallyPrice");
        String sign = params.get("sign");
        String payId = params.get("payId");
        String type = params.get("type");
        String key = null;
        String driver = "";
        Orders orders = ordersService.selectByMember(payId);
        if (orders.getPayType().equals("vmqpay")) { // 码支付
            driver = "vmqpay";
        } else if (Integer.parseInt(type) == 1) { // wxpay
            driver = "mqpay_wxpay";
        } else if (Integer.parseInt(type) == 2) { // alipay
            driver = "mqpay_alipay";
        } else if (Integer.parseInt(type) == 3) { // qqpay
            driver = "mqpay_alipay";
        }
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver).eq("username",orders.getUsername()));
        if (!orders.getPayType().equals(pays.getDriver())) {
            return "不支持该支付类型";
        }

        Map mapTypes = JSON.parseObject(pays.getConfig());
        key = mapTypes.get("key").toString();
        String mysign = VmqPay.md5(payId + param + type + Double.valueOf(price) + Double.valueOf(money) + key);

        if (mysign.equals(sign)) {
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String seconds = new SimpleDateFormat("HHmmss").format(new Date());
            String number = StringUtil.getRandomNumber(6);
            String payNo = date + seconds + number;

            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(payId, () -> {
                String returnBig1 = returnBig(money, price, payId, payNo, param, "success", "fiald");
                notifyText.set(returnBig1);
            });
            return notifyText.get();

        } else {
            return "签名错误！";
        }
    }

    @RequestMapping("/mqpay/returnUrl")
    public void returnUrl(HttpServletResponse response) throws IOException {
        /**
         *验证通知 处理自己的业务
         */
        String contextPath = request.getContextPath();
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String param = params.get("param");
        String price = params.get("price");
        String reallyPrice = params.get("reallyPrice");
        String sign = params.get("sign");
        String payId = params.get("payId");
        String type = params.get("type");
        String key = null;
        String driver = "";
        Orders orders = ordersService.selectByMember(payId);
        if (orders.getPayType().equals("vmqpay")) { // 码支付
            driver = "vmqpay";
        } else if (Integer.parseInt(type) == 1) { // wxpay
            driver = "mqpay_wxpay";
        } else if (Integer.parseInt(type) == 2) { // alipay
            driver = "mqpay_alipay";
        } else if (Integer.parseInt(type) == 3) { // qqpay
            driver = "mqpay_alipay";
        }
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver).eq("username",orders.getUsername()));
        Map mapTypes = JSON.parseObject(pays.getConfig());
        key = mapTypes.get("key").toString();
        String mysign = VmqPay.md5(payId + param + type + price + reallyPrice + key);
        if (mysign.equals(sign)) {
            String url = contextPath + "/pay/state/" + payId;
            response.sendRedirect(url);
        } else {
            log.info("签名校验不通过");
        }
    }

    @RequestMapping("/epay/notifyUrl")
    @ResponseBody
    public String zlianpNotify() {
        Map<String, String> parameterMap = RequestParamsUtil.getParameterMap(request);

        String pid = parameterMap.get("pid");
        String type = parameterMap.get("type");
        String out_trade_no = parameterMap.get("out_trade_no");

        String driver = "";
        if (type.equals("wxpay")) {
            driver = "epay_wxpay";
        } else if (type.equals("alipay")) {
            driver = "epay_alipay";
        } else if (type.equals("qqpay")) {
            driver = "epay_qqpay";
        }
        /**
         * 防止破解
         */
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", out_trade_no));
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver).eq("username",orders.getUsername()));

        if (!orders.getPayType().equals(pays.getDriver())) {
            return "不支持该支付类型";
        }

        Map mapTypes = JSON.parseObject(pays.getConfig());

        // 你的key 在后台获取
        String secret_key = mapTypes.get("key").toString();
        String trade_no = parameterMap.get("trade_no");
        String name = parameterMap.get("name");
        String money = parameterMap.get("money");
        String trade_status = parameterMap.get("trade_status");
        String return_url = parameterMap.get("return_url");
        String notify_url = parameterMap.get("notify_url");
        String sign = parameterMap.get("sign");
        String sign_type = parameterMap.get("sign_type");

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", out_trade_no);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
        params.put("return_url", return_url);
        params.put("notify_url", notify_url);
        params.put("trade_status", trade_status);

        String sign1 = EpayUtil.createSign(params, secret_key);

        if (sign1.equals(sign)) {
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(out_trade_no, () -> {
                String returnBig1 = returnBig(money, money, out_trade_no, trade_no, name, "success", "final");
                notifyText.set(returnBig1);
            });
            return notifyText.get();
        } else {
            return "签名错误！！";
        }
    }

    @RequestMapping("/epay/returnUrl")
    @ResponseBody
    public void zlianpReturnUrl(HttpServletResponse response) throws IOException {

        /**
         *验证通知 处理自己的业务
         */
        String contextPath = request.getContextPath();
        Map<String, String> parameterMap = RequestParamsUtil.getParameterMap(request);

        String pid = parameterMap.get("pid");
        String type = parameterMap.get("type");

        String driver = "";
        if (type.equals("wxpay")) {
            driver = "epay_wxpay";
        } else if (type.equals("alipay")) {
            driver = "epay_alipay";
        } else if (type.equals("qqpay")) {
            driver = "epay_qqpay";
        }

        Orders orders = ordersService.selectByMember(parameterMap.get("out_trade_no"));
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", driver).eq("username",orders.getUsername()));
        Map mapTypes = JSON.parseObject(pays.getConfig());

        // 你的key 在后台获取
        String secret_key = mapTypes.get("key").toString();
        String trade_no = parameterMap.get("trade_no");
        String out_trade_no = parameterMap.get("out_trade_no");
        String name = parameterMap.get("name");
        String money = parameterMap.get("money");
        String trade_status = parameterMap.get("trade_status");
        String return_url = parameterMap.get("return_url");
        String notify_url = parameterMap.get("notify_url");
        String sign = parameterMap.get("sign");
        String sign_type = parameterMap.get("sign_type");

        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        params.put("trade_no", trade_no);
        params.put("out_trade_no", out_trade_no);
        params.put("type", type);
        params.put("name", name);
        params.put("money", money);
        params.put("return_url", return_url);
        params.put("notify_url", notify_url);
        params.put("trade_status", trade_status);

        String sign1 = EpayUtil.createSign(params, secret_key);

        if (sign1.equals(sign)) {
            String url = contextPath + "/pay/state/" + out_trade_no;
            response.sendRedirect(url);
        }
    }

    /**
     * @return
     */
    @ResponseBody
    @RequestMapping("/yungouos/notify")
    public String notify(HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String payNo = params.get("payNo");
        String code = params.get("code");
        String mchId = params.get("mchId");
        String orderNo = params.get("orderNo");
        String money = params.get("money");
        String outTradeNo = params.get("outTradeNo");
        String sign = params.get("sign");
        String payChannel = params.get("payChannel");
        String attach = params.get("attach");

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("orderNo", orderNo);
        map.put("outTradeNo", outTradeNo);
        map.put("payNo", payNo);
        map.put("money", money);
        map.put("mchId", mchId);

        String key = null;
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", outTradeNo));
        switch (payChannel) {
            //此处因为没启用独立密钥 支付密钥支付宝与微信支付是一样的 （密钥获取：登录 yungouos.com-》我的账户-》商户管理-》商户密钥）
            case "wxpay":
                Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "yungouos_wxpay").eq("username",orders.getUsername()));

                /**
                 * 防止恶意回调
                 */
                if (!orders.getPayType().equals(wxPays.getDriver())) {
                    return "不支持该支付类型";
                }

                Map wxMap = JSON.parseObject(wxPays.getConfig());
                key = wxMap.get("key").toString();
                break;
            case "alipay":
                Pays alipays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "yungouos_alipay").eq("username",orders.getUsername()));

                /**
                 * 防止恶意回调
                 */
                if (!orders.getPayType().equals(alipays.getDriver())) {
                    return "不支持该支付类型";
                }

                Map aliMap = JSON.parseObject(alipays.getConfig());
                key = aliMap.get("key").toString();
                break;
            default:
                break;
        }

        String mySign = createSign(map, key);
        if (mySign.equals(sign) && Integer.parseInt(code) == 1) {
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(outTradeNo, () -> {
                String returnBig1 = returnBig(money, money, outTradeNo, payNo, attach, "SUCCESS", "FIALD");
                notifyText.set(returnBig1);
            });
            return notifyText.get();
        } else {
            //签名错误
            return "FIALD";
        }
    }

    /**
     * 虎皮椒支付通知
     *
     * @return
     */
    @RequestMapping("/xunhupay/notifyUrl")
    @ResponseBody
    public String xunhuNotifyUrl(XunhuNotIfy xunhuNotIfy) {
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", xunhuNotIfy.getTrade_order_id()));
        String key = null;
        if (orders.getPayType().equals("xunhupay_wxpay")) {
            Pays xunhuwxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "xunhupay_wxpay").eq("username",orders.getUsername()));

            /**
             * 防止恶意回调
             */
            if (!orders.getPayType().equals(xunhuwxPays.getDriver())) {
                return "不支持该支付类型";
            }

            Map xunhuwxMap = JSON.parseObject(xunhuwxPays.getConfig());
            key = xunhuwxMap.get("appsecret").toString();
        } else if (orders.getPayType().equals("xunhupay_alipay")) {
            Pays xunhualiPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "xunhupay_alipay").eq("username",orders.getUsername()));

            /**
             * 防止恶意回调
             */
            if (!orders.getPayType().equals(xunhualiPays.getDriver())) {
                return "不支持该支付类型";
            }

            Map xunhualiMap = JSON.parseObject(xunhualiPays.getConfig());
            key = xunhualiMap.get("appsecret").toString();
        }

        Map map = JSON.parseObject(JSON.toJSONString(xunhuNotIfy), Map.class);

        String sign = PayUtils.createSign(map, key);
        if (sign.equals(xunhuNotIfy.getHash()) && "OD".equals(xunhuNotIfy.getStatus())) {
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(xunhuNotIfy.getTrade_order_id(), () -> {
                String returnBig = returnBig(xunhuNotIfy.getTotal_fee().toString(), xunhuNotIfy.getTotal_fee().toString(), xunhuNotIfy.getTrade_order_id(), xunhuNotIfy.getTransaction_id(), xunhuNotIfy.getPlugins(), "success", "fiald");
                notifyText.set(returnBig);
            });
            return notifyText.get();
        } else {
            return "fiald";
        }
    }

    /**
     * 虎皮椒支付通知
     *
     * @param request
     * @return
     */
    @RequestMapping("/xunhupay/returnUrl")
    @ResponseBody
    public void xunhuReturnUrl(HttpServletResponse response) throws IOException {
        // 记得 map 第二个泛型是数组 要取 第一个元素 即[0]
        String contextPath = request.getContextPath();
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        String url = contextPath + "/pay/state/" + params.get("trade_order_id");
        response.sendRedirect(url);
    }

    /**
     * 异步通知
     *
     * @param notifyDTO
     * @return
     */
    @RequestMapping("/payjs/notify")
    @ResponseBody
    public Object payjsNotify(NotifyDTO notifyDTO) {
        Map<String, Object> notifyData = new HashMap<>();
        notifyData.put("return_code", notifyDTO.getReturn_code());
        notifyData.put("total_fee", notifyDTO.getTotal_fee());
        notifyData.put("out_trade_no", notifyDTO.getOut_trade_no());
        notifyData.put("payjs_order_id", notifyDTO.getPayjs_order_id());
        notifyData.put("transaction_id", notifyDTO.getTransaction_id());
        notifyData.put("time_end", notifyDTO.getTime_end());
        notifyData.put("openid", notifyDTO.getOpenid());
        notifyData.put("mchid", notifyDTO.getMchid());

        // options
        if (notifyDTO.getAttach() != null) {
            notifyData.put("attach", notifyDTO.getAttach());
        }
        if (notifyDTO.getType() != null) {
            notifyData.put("type", notifyDTO.getType());
        }
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", notifyDTO.getOut_trade_no()));
        String key = null;
        if (notifyDTO.getType() != null) { // 支付宝
            Pays aliPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "payjs_alipay").eq("username",orders.getUsername()));

            /**
             * 防止恶意回调
             */
            if (!orders.getPayType().equals(aliPays.getDriver())) {
                return "不支持该支付类型";
            }

            Map wxMap = JSON.parseObject(aliPays.getConfig());
            key = wxMap.get("key").toString();
        } else { // 微信
            Pays wxPays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "payjs_wxpay").eq("username",orders.getUsername()));

            /**
             * 防止恶意回调
             */
            if (!orders.getPayType().equals(wxPays.getDriver())) {
                return "不支持该支付类型";
            }

            Map wxMap = JSON.parseObject(wxPays.getConfig());
            key = wxMap.get("key").toString();
        }

        String sign = SignUtil.sign(notifyData, key);
        if (sign.equals(notifyDTO.getSign())) {
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(notifyDTO.getOut_trade_no(), () -> {
                String returnBig = returnBig(notifyDTO.getTotal_fee(), notifyDTO.getTotal_fee(), notifyDTO.getOut_trade_no(), notifyDTO.getTransaction_id(), notifyDTO.getAttach(), "success", "failure");
                notifyText.set(returnBig);
            });
            return notifyText.get();
        } else {
            return "failure";
        }
    }

    /**
     * 微信官方异步通知
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/wxpay/notify")
    @ResponseBody
    public String wxPayNotify(HttpServletResponse response) {
        String resXml = "";
        InputStream inStream;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            System.out.println("wxnotify:微信支付----start----");
            // 获取微信调用我们notify_url的返回信息
            String result = outSteam.toString(String.valueOf(StandardCharsets.UTF_8));
            System.out.println("wxnotify:微信支付----result----=" + result);

            // 关闭流
            outSteam.close();
            inStream.close();

            // xml转换为map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            boolean isSuccess = false;
            String result_code = resultMap.get("result_code");
            String out_trade_no = resultMap.get("out_trade_no");// 商户系统内部订单号
            if ("SUCCESS".equals(result_code)) {
                /**
                 * 防止恶意回调
                 */
                Orders orders1 = ordersService.getOne(new QueryWrapper<Orders>().eq("member", out_trade_no));
                Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "wxpay").eq("username",orders1.getUsername()));
                if (!orders1.getPayType().equals(pays.getDriver())) {
                    return "不支持该支付类型";
                }

                Map mapTypes = JSON.parseObject(pays.getConfig());
                String key = mapTypes.get("key").toString(); // 密钥

                /**
                 * 签名成功
                 */
                if (WXPayUtil.isSignatureValid(resultMap, key)) {
                    String total_fee = resultMap.get("total_fee");// 订单总金额，单位为分
                    String transaction_id = resultMap.get("transaction_id");// 微信支付订单号
                    String attach = resultMap.get("attach");// 商家数据包，原样返回
                    String appid = resultMap.get("appid");// 微信分配的小程序ID

                    BigDecimal bigDecimal = new BigDecimal(total_fee);
                    BigDecimal multiply = bigDecimal.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    String money = new DecimalFormat("0.##").format(multiply);
                    Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", out_trade_no));
                    if (member.getPayType().equals("wxpay")) {
                        AtomicReference<String> notifyText = new AtomicReference<>();
                        synchronizedByKeyService.exec(out_trade_no, () -> {
                            String returnBig = returnBig(money, money, out_trade_no, transaction_id, attach, WxpayresXml, resFailXml);
                            notifyText.set(returnBig);
                        });
                        resXml = notifyText.get();
                    } else {
                        AtomicReference<String> notifyText = new AtomicReference<>();
                        synchronizedByKeyService.exec(out_trade_no, () -> {
                            String returnBig = returnBig(money, money, out_trade_no, transaction_id, attach, WxpayH5resXml, resFailXml);
                            notifyText.set(returnBig);
                        });
                        resXml = notifyText.get();
                    }
                } else {
                    System.out.println("签名判断错误！！");
                }
            }
        } catch (Exception e) {
            System.out.println("wxnotify:支付回调发布异常：" + e);
        } finally {
            try {
                // 处理业务完毕
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                System.out.println("wxnotify:支付回调发布异常:out：" + e);
            }
        }
        return resXml;
    }

    /**
     * 支付宝当面付 异步通知
     *
     * @param request 接收
     * @return 返回
     */
    @RequestMapping("/alipay/notify")
    @ResponseBody
    @SneakyThrows(Exception.class)
    public String alipayNotifyUrl() {
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        String out_trade_no = params.get("out_trade_no");// 商户订单号
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", out_trade_no));

        if ("alipay".equals(orders.getPayType())) {
            Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "alipay").eq("username",orders.getUsername()));
            /**
             * 防止恶意回调
             */
            if (!orders.getPayType().equals(pays.getDriver())) {
                return "不支持该支付类型";
            }
        } else if ("alipay_pc".equals(orders.getPayType())) {
            Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "alipay_pc").eq("username",orders.getUsername()));
            /**
             * 防止恶意回调
             */
            if (!orders.getPayType().equals(pays.getDriver())) {
                return "不支持该支付类型";
            }
        }

        Boolean verifyNotify = Factory.Payment.Common().verifyNotify(params);
        if (verifyNotify) {
            String total_amount = params.get("total_amount");// 付款金额
            String trade_no = params.get("trade_no");// 流水
            String receipt_amount = params.get("receipt_amount");// 实际支付金额
            String body = params.get("subject");// 状态
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(out_trade_no, () -> {
                String returnBig1 = returnBig(receipt_amount, total_amount, out_trade_no, trade_no, body, "success", "failure");
                notifyText.set(returnBig1);
            });
            return notifyText.get();
        } else {
            System.out.println("签名错误！！");
            return "failure";
        }
    }

    /**
     * 支付宝PC支付返回接口
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/alipay/return_url")
    @SneakyThrows(Exception.class)
    public void alipayReturnUrl(HttpServletResponse response) {
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        Boolean verifyNotify = Factory.Payment.Common().verifyNotify(params);
        // 验签成功
        if (verifyNotify) {
            String pay_no = params.get("trade_no"); // 流水号
            String member = params.get("out_trade_no");// 商户订单号
            if (pay_no != null || pay_no != "") {
                String url = "/search/order/" + member;
                response.sendRedirect(url);
            }
        } else {
            System.out.println("支付, 验签失败...");
        }
    }

    /**
     * 取消订单
     *
     * @return
     */
    @GetMapping("/paypal/cancel")
    @ResponseBody
    public String cancelPay() {
        return "cancel";
    }

    /**
     * 完成支付
     *
     * @param paymentId
     * @param payerId
     * @param response
     * @return
     */
    @GetMapping("/paypal/success")
    @ResponseBody
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) {
        try {
            Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "paypal"));
            Map mapTypes = JSON.parseObject(pays.getConfig());
            String clientId = mapTypes.get("clientId").toString();
            String clientSecret = mapTypes.get("clientSecret").toString();
            Payment payment = PaypalSend.executePayment(clientId, clientSecret, paymentId, payerId);
            if (payment.getState().equals("approved")) {
                String member = null; // 订单号
                String total = null;  // 金额
                String pay_no = payment.getId();
                List<Transaction> transactions = payment.getTransactions();
                for (Transaction transaction : transactions) {
                    member = transaction.getDescription();
                    total = transaction.getAmount().getTotal(); // 实际付款金额
                }
                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("member", member));
                /**
                 * 防止恶意回调
                 */
                if (!orders.getPayType().equals(pays.getDriver())) {
                    return "不支持该支付类型";
                }
                String returnBig = returnBig(total, orders.getPrice().toString(), member, pay_no, orders.getProductId().toString(), "success", "failure");
                if (returnBig.equals("success")) {
                    response.sendRedirect("/search/order/" + member);
                } else {
                    response.sendRedirect("/search/order/" + member);
                }
            }
        } catch (PayPalRESTException | IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    /**
     * epusdt
     *
     * @param epusdtNotify
     * @return
     */
    @PostMapping("/epusdt/notifyUrl")
    @ResponseBody
    public String epusdt(@RequestBody EpusdtNotify epusdtNotify) {
        epusdtNotify.setAmount(epusdtNotify.getAmount().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().stripTrailingZeros());
        Orders orders = ordersService.getOne(Wrappers.<Orders>lambdaQuery().eq(Orders::getMember, epusdtNotify.getOrder_id()));
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "epusdt").eq("username",orders.getUsername()));
        if (!orders.getPayType().equals(pays.getDriver())) {
            return "error!";
        }
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String key = mapTypes.get("key").toString();
        String sign = sendPay.createSign(epusdtNotify, key);
        if (sign.equals(epusdtNotify.getSignature())) {
            AtomicReference<String> notifyText = new AtomicReference<>();
            synchronizedByKeyService.exec(epusdtNotify.getOrder_id(), () -> {
                String returnBig = returnBig(orders.getMoney().toString(), orders.getMoney().toString(), epusdtNotify.getOrder_id(),
                        epusdtNotify.getTrade_id(), orders.getProductId().toString(), "ok", "fail");
                notifyText.set(returnBig);
            });
            return notifyText.get();
        }
        return "fail";
    }

    @RequestMapping("/epusdt/returnUrl")
    @ResponseBody
    public void epusdtReturnUrl(String order_id, HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        String url = contextPath + "/pay/state/" + order_id;
        response.sendRedirect(url);
    }

    /**
     * 业务处理
     *
     * @param money   实收款金额
     * @param price   订单金额
     * @param payId   订单号
     * @param pay_no  流水号
     * @param param   自定义内容
     * @param success 返回成功
     * @param fiald   返回失败
     * @return this
     */
    private String returnBig(String money, String price, String payId, String pay_no, String param, String success, String fiald) {
        /**
         * 通过订单号查询
         */
        Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", payId));
        if (member == null) {
            return "没有找到这个订单"; // 本地没有这个订单
        }

        if (member.getStatus() > 0) {
            return success;
        }

        boolean empty = StringUtils.isEmpty(member.getCardsInfo());
        if (!empty) {
            return success;
        }

        Products products = productsService.getById(param);
        if (products == null) {
            return "商品找不到了"; // 商品没了
        }

        Website website = websiteService.getById(1);
        ShopSettings shopSettings = shopSettingsService.getById(1);

        Orders orders = new Orders();
        orders.setId(member.getId());
        orders.setPayTime(new Date());
        orders.setPayNo(pay_no);
        orders.setPrice(new BigDecimal(price));
        orders.setMoney(new BigDecimal(money));

        if (products.getShipType() == 0) { // 自动发货的商品

            /**
             * 卡密信息列表
             * 通过商品购买数量来获取对应商品的卡密数量
             */
            if (products.getSellType() == 0) { // 一次性卡密类型

                List<Cards> cardsList = cardsService.getBaseMapper().selectList(new QueryWrapper<Cards>()
                        .eq("status", 0)
                        .eq("product_id", products.getId())
                        .eq("sell_type", 0)
                        .orderBy(true, false, "rand()")
                        .last("LIMIT " + member.getNumber() + ""));

                if (cardsList == null) return fiald; // 空值的话直接返回错误提示

                StringBuilder orderInfo = new StringBuilder(); // 订单关联的卡密信息
                List<Cards> updateCardsList = new ArrayList<>();
                for (Cards cards : cardsList) {
                    orderInfo.append(cards.getCardInfo()).append("\n"); // 通过StringBuilder 来拼接卡密信息

                    /**
                     * 设置每条被购买的卡密的售出状态
                     */
                    Cards cards1 = new Cards();
                    cards1.setId(cards.getId());
                    cards1.setStatus(1);
                    cards1.setNumber(0);
                    cards1.setSellNumber(1);
                    cards1.setUpdatedAt(new Date());

                    updateCardsList.add(cards1);
                }

                // 去除多余尾部的逗号
                String result = orderInfo.deleteCharAt(orderInfo.length() - 1).toString();

                orders.setStatus(1); // 设置已售出
                orders.setCardsInfo(result);

                // 更新售出的订单
                if (ordersService.updateById(orders)) {
                    // 设置售出的卡密
                    cardsService.updateBatchById(updateCardsList);
                } else {
                    return fiald;
                }
            } else if (products.getSellType() == 1) { // 重复销售的卡密
                StringBuilder orderInfo = new StringBuilder(); // 订单关联的卡密信息

                Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 1));
                if (cards == null) {
                    return fiald; // 空值的话直接返回错误提示
                }

                /**
                 * 设置每条被购买的卡密的售出状态
                 */
                Cards cards1 = new Cards();
                cards1.setId(cards.getId());
                cards1.setUpdatedAt(new Date());
                if (cards.getNumber() == 1) { // 还剩下一个卡密
                    cards1.setSellNumber(cards.getSellNumber() + member.getNumber());
                    cards1.setNumber(cards.getNumber() - member.getNumber()); // 减完之后等于0
                    cards1.setStatus(1); // 设置状态为已全部售出
                } else {
                    cards1.setSellNumber(cards.getSellNumber() + member.getNumber());
                    cards1.setNumber(cards.getNumber() - member.getNumber());
                }

                /**
                 * 看用户购买了多少个卡密
                 * 正常重复的卡密不会购买1个以上
                 * 这里做个以防万一呀（有钱谁不赚）
                 */
                for (int i = 0; i < member.getNumber(); i++) {
                    orderInfo.append(cards.getCardInfo()).append("\n");
                }

                // 去除多余尾部的逗号
                String result = orderInfo.deleteCharAt(orderInfo.length() - 1).toString();
                orders.setStatus(1); // 设置已售出
                orders.setCardsInfo(result);

                // 设置售出的商品
                if (ordersService.updateById(orders)) {
                    cardsService.updateById(cards1);
                } else {
                    return fiald;
                }
            }

            /**
             * 微信的 wxpush 通知
             * 本通知只针对站长
             * 当用户购买成功后会给您设置的
             * wxpush 微信公众号发送订单购买成功后的通知
             */
            if (shopSettings.getIsWxpusher() == 1) {
                Message message = new Message();
                message.setContent(website.getWebsiteName() + "新订单提醒<br>订单号：<span style='color:red;'>" + member.getMember() + "</span><br>商品名称：<span>" + products.getName() + "</span><br>购买数量：<span>" + member.getNumber() + "</span><br>订单金额：<span>" + member.getMoney() + "</span><br>支付状态：<span style='color:green;'>成功</span><br>");
                message.setContentType(Message.CONTENT_TYPE_HTML);
                message.setUid(shopSettings.getWxpushUid());
                message.setAppToken(shopSettings.getAppToken());
                WxPusher.send(message);
            }

            /**
             * 邮件通知
             * 后台开启邮件通知，
             * 这里会给下单用户的邮箱发送一条邮件
             */
            if (shopSettings.getIsEmail() == 1) {
                if (!StringUtils.isEmpty(member.getEmail())) {
                    if (FormCheckUtil.isEmail(member.getEmail())) {
                        Map<String, Object> map = new HashMap<>();  // 页面的动态数据
                        map.put("title", website.getWebsiteName());
                        map.put("member", member.getMember());
                        map.put("date", DateUtil.getDate());
                        map.put("password", member.getPassword());
                        map.put("url", website.getWebsiteUrl() + "/search/order/" + member.getMember());
                        try {
                            emailService.sendHtmlEmail(String.format("【%s】发货提醒", website.getWebsiteName()), "email/sendShip.html", map, new String[]{member.getEmail()});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else { // 手动发货商品
            Products products1 = new Products();
            products1.setId(products.getId());
            products1.setInventory(products.getInventory() - member.getNumber());
            products1.setSales(products.getSales() + member.getNumber());

            orders.setStatus(2); // 手动发货模式 为待处理
            if (ordersService.updateById(orders)) {
                // 更新售出
                productsService.updateById(products1);
            } else {
                return fiald;
            }

            /**
             * 微信的 wxpush 通知
             * 本通知只针对站长
             * 当用户购买成功后会给您设置的
             * wxpush 微信公众号发送订单购买成功后的通知
             */
            if (shopSettings.getIsWxpusher() == 1) {
                Message message = new Message();
                message.setContent(website.getWebsiteName() + "新订单提醒<br>订单号：<span style='color:red;'>" + member.getMember() + "</span><br>商品名称：<span>" + products.getName() + "</span><br>购买数量：<span>" + member.getNumber() + "</span><br>订单金额：<span>" + member.getMoney() + "</span><br>支付状态：<span style='color:green;'>成功</span><br>");
                message.setContentType(Message.CONTENT_TYPE_HTML);
                message.setUid(shopSettings.getWxpushUid());
                message.setAppToken(shopSettings.getAppToken());
                WxPusher.send(message);
            }

            /**
             * 邮件通知
             * 后台开启邮件通知，
             * 这里会给下单用户的邮箱发送一条邮件
             */
            if (shopSettings.getIsEmail() == 1) {
                if (FormCheckUtil.isEmail(member.getEmail())) {
                    try {
                        emailService.sendTextEmail(website.getWebsiteName() + " 订单提醒", "您的订单号为：" + member.getMember() + "  本商品为手动发货，请耐心等待！", new String[]{member.getEmail()});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return success;
    }

    @GetMapping("/order/state/{orderid}")
    @ResponseBody
    public JsonResult state(@PathVariable("orderid") String orderid) {
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("id", orderid));
        if (!StringUtils.isEmpty(orders.getPayNo())) {
            return JsonResult.ok().setCode(200).setData(1);
        } else {
            return JsonResult.ok().setData(0);
        }
    }


    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            String value = param.getValue();
            if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(param.getKey()).append("=");
            if (urlEncoder) {
                try {
                    value = urlEncode(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
            sb.append(value);
        }
        return sb.toString();
    }

    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
    }

    public static String createSign(Map<String, String> params, String partnerKey) throws NoSuchAlgorithmException {
        // 生成签名前先去除sign
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + partnerKey;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((stringSignTemp).getBytes());
        String mySign = new BigInteger(1, md.digest()).toString(16).toUpperCase();
        if (mySign.length() != 32) {
            mySign = "0" + mySign;
        }
        return mySign;
    }

}
