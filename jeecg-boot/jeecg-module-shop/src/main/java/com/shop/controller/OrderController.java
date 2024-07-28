package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.common.PaysEnmu;
import com.shop.entity.Cards;
import com.shop.service.CardsService;
import com.shop.common.core.Constants;
import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.pays.alipay.AlipayUtil;
import com.shop.common.core.pays.budpay.BudpayUtil;
import com.shop.common.core.pays.epay.EpayUtil;
import com.shop.common.core.pays.epusdt.entity.EpusdtEntity;
import com.shop.common.core.pays.epusdt.sendPay;
import com.shop.common.core.pays.mqpay.VmqPay;
import com.shop.common.core.pays.payjs.sendPayjs;
import com.shop.common.core.pays.paypal.PaypalSend;
import com.shop.common.core.pays.paypal.config.PaypalPaymentIntent;
import com.shop.common.core.pays.paypal.config.PaypalPaymentMethod;
import com.shop.common.core.pays.wxpay.SendWxPay;
import com.shop.common.core.pays.xunhupay.PayUtils;
import com.shop.common.core.pays.yungouos.YunGouosConfig;
import com.shop.common.core.utils.*;
import com.shop.common.core.web.BaseController;
import com.shop.common.core.web.JsonResult;
import com.shop.service.EmailService;
import com.shop.common.util.DateStrUtil;
import com.shop.entity.Orders;
import com.shop.service.OrdersService;
import com.shop.entity.Products;
import com.shop.service.ProductsService;
import com.shop.entity.Coupon;
import com.shop.entity.Pays;
import com.shop.entity.ShopSettings;
import com.shop.service.CouponService;
import com.shop.service.PaysService;
import com.shop.service.ShopSettingsService;
import com.shop.entity.Theme;
import com.shop.service.ThemeService;
import com.shop.entity.Website;
import com.shop.service.WebsiteService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.shop.controller.DashboardController.getQueryWrapper;

@Controller
@Transactional
public class OrderController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private PaysService paysService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CardsService cardsService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ShopSettingsService shopSettingsService;

    /**
     * 添加
     */
    @ResponseBody
    @RequestMapping("/buy")
    public JsonResult save(HttpServletResponse response) {

        // 记得 map 第二个泛型是数组 要取 第一个元素 即[0]
        Map<String, String> params = RequestParamsUtil.getParameterMap(request);
        Integer goodsId = Integer.parseInt(params.get("goodsId"));
        Integer number = Integer.parseInt(params.get("number"));
        String contact = params.get("contact");
        String coupon = params.get("coupon");
        String payType = params.get("payType");
        String password = params.get("password");

        if (StringUtils.isEmpty(goodsId)) {
            return JsonResult.error("商品不能为空");
        } else if (StringUtils.isEmpty(contact)) {
            return JsonResult.error("联系方式不能为空！");
        } else if (StringUtils.isEmpty(number)) {
            return JsonResult.error("商品数量不能小于或等于0");
        } else if (StringUtils.isEmpty(payType)) {
            return JsonResult.error("请选择付款方式！");
        }

        boolean isMobile = DeviceUtils.isMobileDevice(request);
        if (isMobile) {
            Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", payType).eq("is_mobile", 1));
            if (ObjectUtils.isEmpty(pays)) {
                return JsonResult.error("不支持该支付类型");
            }
        } else {
            Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", payType).eq("is_pc", 1));
            if (ObjectUtils.isEmpty(pays)) {
                return JsonResult.error("不支持该支付类型");
            }
        }

        Products products = productsService.getById(goodsId);

        /**
         * 检查附加内容是否为已填写
         */
        Integer isCustomize = products.getIsCustomize();
        if (isCustomize == 1) {
            String customizeInput = products.getCustomizeInput();
            String[] customize = customizeInput.split("\\n");
            for (String s : customize) {
                String[] split = s.split("=");
                String s1 = params.get(split[0]); // get到提交过来的字段内容
                if ("true".equals(split[2])) {
                    if (StringUtils.isEmpty(s1)) {
                        return JsonResult.error(split[1] + "没有填写、请填写后再下单。");
                    }
                }
            }
        }

        UserAgentGetter agentGetter = new UserAgentGetter(request);
        Integer restricts = products.getRestricts();
        // 判断是不是限购
        if (restricts >= 1) {
            JsonResult jsonResult = restricts(goodsId, number, restricts, agentGetter.getIp());
            if (jsonResult != null) {
                return jsonResult;
            }
        }

        if (!StringUtils.isEmpty(products.getIsPassword())) {
            if (products.getIsPassword() == 1) {
                if (StringUtils.isEmpty(password)) {
                    return JsonResult.error("商品查询密码不能为空！！");
                }
            }
        }

        if (products.getShipType() == 0) { // 自动发货模式
            long count = 0;
            if (products.getSellType() == 1) {
                Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 1));
                if (ObjectUtils.isEmpty(cards)) {
                    count = 0;
                } else {
                    count = cards.getNumber();
                }
            } else {
                count = cardsService.count(new QueryWrapper<Cards>().eq("product_id", goodsId).eq("status", 0).eq("sell_type", 0));
            }
            if (count == 0) {
                return JsonResult.error("本商品已售空，请联系店长补货！");
            } else if (number > count) {
                return JsonResult.error("商品购买数量不能大于商品剩余数量！");
            }
        } else { // 手动发货模式
            if (products.getInventory() == 0) {
                return JsonResult.error("本商品已售空，请联系店长补货！");
            } else if (number > products.getInventory()) {
                return JsonResult.error("商品购买数量不能大于商品剩余数量！");
            }
        }

        try {
            Integer couponId = null;
            if (!StringUtils.isEmpty(coupon)) {
                QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("product_id", goodsId) // 商品id
                        .eq("coupon", coupon) // 优惠券代码
                        .eq("status", 0); // 没有使用的

                Coupon coupon1 = couponService.getOne(queryWrapper);
                if (!ObjectUtils.isEmpty(coupon1)) { // 判断 coupon1 是否不为空
                    /**
                     * 拿到优惠券 entity
                     */
                    couponId = coupon1.getId();
                } else {
                    /**
                     * 给前端返回优惠券失效或者为空的信息
                     */
                    return JsonResult.error("该优惠券代码已被使用过，或不能使用在本商品，请核对后再试！");
                }
            }
            /**
             * 处理订单业务
             */
            Map<String, String> buy = ordersService.buy(goodsId, number, contact, couponId, payType, password, request);

            Cookie[] cookies = request.getCookies();
            if (ObjectUtils.isEmpty(cookies)) {
                /**
                 * 创建 cookie
                 * 将订单信息保存到浏览器
                 */
                Cookie cookie1 = new Cookie("BROWSER_ORDERS_CACHE", buy.get("member"));
                cookie1.setMaxAge(24 * 60 * 60); // 1天过期
                // 将cookie对象加入response响应
                response.addCookie(cookie1);
            } else {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if ("BROWSER_ORDERS_CACHE".equals(cookieName)) {
                        String cookieValue = cookie.getValue();
                        /**
                         * 创建 cookie
                         * 将订单信息保存到浏览器
                         */
                        Cookie cookie1 = new Cookie("BROWSER_ORDERS_CACHE", cookieValue + "=" + buy.get("member"));
                        cookie1.setMaxAge(24 * 60 * 60); // 1天过期
                        // 将cookie对象加入response响应
                        response.addCookie(cookie1);
                        break;
                    } else {
                        /**
                         * 创建 cookie
                         * 将订单信息保存到浏览器
                         */
                        Cookie cookie1 = new Cookie("BROWSER_ORDERS_CACHE", buy.get("member"));
                        cookie1.setMaxAge(24 * 60 * 60); // 1天过期
                        // 将cookie对象加入response响应
                        response.addCookie(cookie1);
                    }
                }
            }

            return JsonResult.ok("订单创建成功！").setCode(200).setData(buy);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("订单创建失败");
        }
    }

    @OperLog(value = "支付", desc = "提交支付")
    @ResponseBody
    @RequestMapping(value = "/alipayPc/{member}", produces = "text/html")
    public String payAlipayPc(@PathVariable("member") String member, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        Orders orders = ordersService.selectByMember(member);
        Products products = productsService.getById(orders.getProductId());

        String productDescription = products.getId().toString(); // 订单备注
        String ordersMember = orders.getMember(); // 订单号
        String goodsName = products.getName(); // 订单主题
        String price = orders.getMoney().toString();

        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", orders.getPayType()).eq("username",orders.getUsername()));

        if (price.equals("0.00")) { // 0元商品 直接完成支付
            String currentTime = Long.toString(System.currentTimeMillis());
            boolean big = returnBig(price, price, orders.getMember(), currentTime, productDescription);
            if (big) {
                response.sendRedirect("/search/order/" + orders.getMember());
                return null;
            }
        }
        /**
         * 创建支付接口
         * 使用枚举加switch
         */
        switch (Objects.requireNonNull(PaysEnmu.getByValue(orders.getPayType()))) {
            case ALIPAY_PC: // 支付宝pc支付
                try {
                    String pcPage = AlipayUtil.getPcPage(pays, productDescription, ordersMember, price);
                    // String payAlipayPc = SendAlipay.payAlipayPc(pays, price, ordersMember, goodsName, productDescription);
                    return pcPage;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return null;
    }

    @OperLog(value = "支付", desc = "提交支付")
    @RequestMapping(value = "/pay/{member}", produces = "text/html")
    public String pay(Model model, @PathVariable("member") String member, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        Orders orders = ordersService.selectByMember(member);
        Products products = productsService.getById(orders.getProductId());

        String productDescription = products.getId().toString(); // 订单备注
        String ordersMember = orders.getMember(); // 订单号
        String goodsName = products.getName(); // 订单主题
        String cloudPayid = orders.getCloudPayid();
        String price = orders.getMoney().toString();
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        Pays pays = paysService.getOne(new QueryWrapper<Pays>().eq("driver", orders.getPayType()).eq("username",orders.getUsername()));

        if ("0.00".equals(price)) { // 0元商品 直接完成支付
            String currentTime = Long.toString(System.currentTimeMillis());
            boolean big = returnBig(price, price, orders.getMember(), currentTime, productDescription);
            if (big) {
                response.sendRedirect("/search/order/" + orders.getMember());
                return null;
            } else {
                return null;
            }
        }

        model.addAttribute("goodsName", goodsName);
        model.addAttribute("price", price);
        model.addAttribute("ordersMember", ordersMember);
        model.addAttribute("orderId", orders.getId());

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);
        ShopSettings shopSettings = shopSettingsService.getById(1);
        model.addAttribute("isBackground", shopSettings.getIsBackground());
        model.addAttribute("shop", shopSettings);
        Theme theme = themeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getEnable, 1));

        /**
         * 创建支付接口
         * 使用枚举加switch
         */
        switch (Objects.requireNonNull(PaysEnmu.getByValue(orders.getPayType()))) {
            case MQPAY_ALIPAY: // V免签支付宝接口
            case MQPAY_WXPAY: // V免签微信接口
                String createMqPay = VmqPay.sendCreateMqPay(pays, Double.valueOf(price), ordersMember, cloudPayid, productDescription);
                response.sendRedirect(createMqPay);
                break;
            case EPAY_ALIPAY: // 易支付支付宝
            case EPAY_QQPAY: // 易支付QQ钱包
            case EPAY_WXPAY: // 易支付微信
                String zlianSendPay = EpayUtil.epaySendPay(pays, price, ordersMember, productDescription);
                response.sendRedirect(zlianSendPay);
                break;
            case BUDPAY_ALIPAY: // 易支付支付宝
            case BUDPAY_WECHAT: // 易支付微信
                String budpaySendPay = BudpayUtil.budpaySendPay(pays, price, ordersMember, productDescription);
                response.sendRedirect(budpaySendPay);
                break;
            case YUNGOUOS_WXPAY: // yungouos微信
            case YUNGOUOS_ALIPAY: // yungouos支付宝
                String gouos = "";
                if (orders.getPayType().equals("yungouos_wxpay")) {
                    model.addAttribute("type", 1);
                    gouos = YunGouosConfig.yunGouosWxPay(pays, price, ordersMember, goodsName, productDescription);
                } else if (orders.getPayType().equals("yungouos_alipay")) {
                    model.addAttribute("type", 2);
                    gouos = YunGouosConfig.yunGouosAliPay(pays, price, ordersMember, goodsName, productDescription);
                }
                model.addAttribute("result", JSON.toJSONString(gouos));
                return "theme/" + theme.getDriver() + "/yunpay.html";
            case XUNHUPAY_WXPAY: // 虎皮椒微信
            case XUNHUPAY_ALIPAY: // 虎皮椒支付宝
                Map pay = PayUtils.pay(getWebName(), pays, goodsName, price, ordersMember, productDescription);
                if (pay != null) {
                    response.sendRedirect(pay.get("url1").toString());
                } else {
                    return "theme/" + theme.getDriver() + "/pay-error.html";
                }
                break;
            case PAYJS_WXPAY: // payjs 微信
            case PAYJS_ALIPAY: // payjs 支付宝
                String payjs = "";
                if (orders.getPayType().equals("payjs_wxpay")) {
                    model.addAttribute("type", 1);
                    payjs = sendPayjs.pay(pays, price, ordersMember, goodsName, productDescription);
                } else if (orders.getPayType().equals("payjs_alipay")) {
                    model.addAttribute("type", 2);
                    payjs = sendPayjs.pay(pays, price, ordersMember, goodsName, productDescription);
                }
                model.addAttribute("result", JSON.toJSONString(payjs));
                return "theme/" + theme.getDriver() + "/yunpay.html";
            case WXPAY: // 微信官方扫码
                String payNattve = SendWxPay.payNattve(pays, price, ordersMember, goodsName, productDescription, agentGetter.getIp());
                model.addAttribute("type", 1); // 微信支付
                model.addAttribute("result", JSON.toJSONString(payNattve));
                return "theme/" + theme.getDriver() + "/yunpay.html";
            case ALIPAY: // 支付宝当面付
                String faceToFace = AlipayUtil.getFaceToFace(pays, productDescription, ordersMember, price);
                model.addAttribute("type", 2); // 支付宝当面付
                model.addAttribute("result", JSON.toJSONString(faceToFace));
                return "theme/" + theme.getDriver() + "/yunpay.html";
            case WXPAU_H5: // 微信h5支付
                String payMweb = SendWxPay.payMweb(pays, price, ordersMember, goodsName, productDescription, agentGetter.getIp());
                response.sendRedirect(payMweb);
                break;
            case PAYPAL: // paypal贝宝国际化收款
                try {
                    Payment payment = PaypalSend.createPayment(pays, price, "USD", PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale, ordersMember);
                    for (Links links : payment.getLinks()) {
                        if (links.getRel().equals("approval_url")) {
                            return "redirect:" + links.getHref();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EPUSDT:
                EpusdtEntity epusdtEntity = sendPay.createPayment(pays, price, ordersMember, productDescription);
                model.addAttribute("title", "支付出错");
                model.addAttribute("msg", epusdtEntity.getMessage());
                switch (epusdtEntity.getStatus_code()) {
                    case 400:
                    case 401:
                    case 10002:
                    case 10003:
                    case 10004:
                    case 10005:
                    case 10006:
                    case 10007:
                    case 10008:
                    case 10009:
                        return "theme/" + theme.getDriver() + "/common/payError.html";
                    case 200:
                        response.sendRedirect(epusdtEntity.getData().getPayment_url());
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 业务处理
     *
     * @param money  实收款金额
     * @param price  订单金额
     * @param payId  订单号
     * @param pay_no 流水号
     * @param param  自定义内容
     * @return this
     */
    private boolean returnBig(String money, String price, String payId, String pay_no, String param) {

        /**
         * 通过订单号查询
         */
        Orders member = ordersService.getOne(new QueryWrapper<Orders>().eq("member", payId));
        if (member == null) {
            return false; // 本地没有这个订单
        }

        if (member.getStatus() > 0) {
            return true;
        }

        boolean empty = StringUtils.isEmpty(member.getCardsInfo());
        if (!empty) {
            return true;
        }

        Products products = productsService.getById(param);
        if (products == null) {
            return false; // 商品没了
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

                if (cardsList == null) return false; // 空值的话直接返回错误提示

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
                    return false;
                }
            } else if (products.getSellType() == 1) { // 重复销售的卡密
                StringBuilder orderInfo = new StringBuilder(); // 订单关联的卡密信息

                Cards cards = cardsService.getOne(new QueryWrapper<Cards>().eq("product_id", products.getId()).eq("status", 0).eq("sell_type", 1));
                if (cards == null) {
                    return false; // 空值的话直接返回错误提示
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
                    return false;
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
                return false;
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

        return true;
    }

    /**
     * 限制购买
     *
     * @param goodsId   商品id
     * @param number    用户购买数量
     * @param restricts 限制购买的数量
     * @param userIp    当前用户的ip
     * @return
     */
    public JsonResult restricts(Integer goodsId, Integer number, Integer restricts, String userIp) {

        /**
         * 通过商品id和当前用户
         * 的ip来查询用户今天所购买的数量
         */
        QueryWrapper queryWrapper = getQueryWrapper(DateStrUtil.getDayBegin(), DateStrUtil.getDayEnd());
        queryWrapper.eq("product_id", goodsId);
        queryWrapper.eq("ip", userIp);
        List<Orders> orderList = ordersService.list(queryWrapper);

        /* 统计已付款的商品数 */
        long payNumber = orderList.stream()
                .filter(orders -> orders.getStatus() == 1)
                .mapToLong(Orders::getNumber).sum();
        if (payNumber >= restricts) {
            return JsonResult.error("已达到每天限购的" + restricts + "个,每天0点重置！");
        }

        /* 判断已付款 + 待购买商品数是不是大于限购数 */
        if ((number + payNumber) > restricts) {
            long remain = restricts - payNumber;
            return JsonResult.error("每天限购" + restricts + "个,当前还可购买" + remain + "个,每天0点重置！");
        }

        /* 统计待付款未超时商品数 */
        long waitPayNumber = orderList.stream()
                .filter(order -> {
                    if (order.getStatus() == 0) {
                        return (System.currentTimeMillis() - order.getCreateTime().getTime()) < Constants.PAY_TIMEOUT_MINUTES * 60 * 1000;
                    }
                    return false;
                })
                .mapToLong(Orders::getNumber).sum();
        /* 判断购买商品数+已购买数+等待付款数是不是大于限购数 */
        if ((number + payNumber + waitPayNumber) > restricts) {
            long remain = restricts - payNumber;
            long createTime = orderList.stream()
                    .filter(order -> {
                        if (order.getStatus() == 0) {
                            return (System.currentTimeMillis() - order.getCreateTime().getTime()) < Constants.PAY_TIMEOUT_MINUTES * 60 * 1000L;
                        }
                        return false;
                    })
                    .mapToLong(orders -> orders.getCreateTime().getTime())
                    .min().getAsLong();
            long expireTime = createTime + Constants.PAY_TIMEOUT_MINUTES * 60 * 1000L;
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH点mm分ss秒");
            String format = dateFormat.format(new Date(expireTime));

            return JsonResult.error("每天限购" + restricts + "个,当天还剩" + remain + "个</br>" +
                    "等付款中的商品数为" + waitPayNumber + "个," + format + "后支付超时释放商品");
        }
        return null;
    }
}
