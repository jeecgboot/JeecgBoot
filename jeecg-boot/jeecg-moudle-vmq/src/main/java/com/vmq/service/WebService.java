package com.vmq.service;

import cn.hutool.core.date.DateUtil;
import com.vmq.config.EmailUtils;
import com.vmq.constant.Constant;
import com.vmq.constant.PayTypeEnum;
import com.vmq.dao.PayOrderDao;
import com.vmq.dao.PayQrcodeDao;
import com.vmq.dao.SettingDao;
import com.vmq.dao.TmpPriceDao;
import com.vmq.dto.CommonRes;
import com.vmq.dto.CreateOrderRes;
import com.vmq.dto.PayInfo;
import com.vmq.entity.PayOrder;
import com.vmq.entity.PayQrcode;
import com.vmq.entity.TmpPrice;
import com.vmq.entity.VmqSetting;
import com.vmq.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WebService {

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${server.url}")
    private String url;

    private static final String default_user = "mctz";

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private PayOrderDao payOrderDao;

    @Autowired
    private TmpPriceDao tmpPriceDao;

    @Autowired
    private PayQrcodeDao payQrcodeDao;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    public RedisTemplate redisTemplate;

    public String getMd5(String username, String content) {
        if (StringUtils.isBlank(username)) {
            username = default_user;
        }
        VmqSetting vmqSetting = settingDao.getSettingByUserName(username);
        String key = vmqSetting.getMd5key();
        return md5(content + key);
    }

    /**
     * 外部调用必须传token
     * @param payOrder
     * @return CommonRes
     */
    public CommonRes createOrder(PayOrder payOrder){
        log.info("createOrder...");
        String username = JWTUtil.getUsername(payOrder.getParam());
        String msg = "自定义";
        if (StringUtils.isEmpty(username)) {
            username = default_user;
            msg = "使用";
        }
        VmqSetting vmqSetting = getDefaultSetting(username);
        log.info("{}【{}】密钥创建订单{}",msg,username,payOrder.getPayId());
        String key = vmqSetting.getMd5key();
        String content = payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice();
        if (!payOrder.getSign().equals(md5(content + key))){
            log.error("md5({})",content);
            return ResUtil.error("签名校验不通过");
        }

        if (payOrderDao.findByPayId(payOrder.getPayId()) != null) {
            return ResUtil.error("商户订单号已存在！");
        }

        // 保存实际支付价格
        if (!checkAddAmount(username,vmqSetting,payOrder)) {
            return ResUtil.error("所有金额均被占用");
        }
        String orderId = StringUtils.format(new Date(),"yyyyMMddHHmmss") + (int)(1000+Math.random()*(9999-1000+1));
        payOrder.setOrderId(orderId);

        // 获取支付链接
        String payUrl = getPayUrl(vmqSetting,payOrder);
        if (payUrl == "") {
            tmpPriceDao.delprice(username,payOrder.getType() + "-" + payOrder.getReallyPrice());
            return ResUtil.error("请您先进入后台配置程序");
        }

        payOrder.setUsername(username);
        payOrder.setCreateDate(new Date().getTime());
        payOrder.setPayDate(0);
        payOrder.setCloseDate(0);
        payOrder.setReallyPrice(payOrder.getReallyPrice());
        payOrder.setState(0);
        if (payOrder.getPayCodeId() != null) {
            payOrder.setPayUrl("");
        }
        payOrderDao.save(payOrder);
        String timeOut = vmqSetting.getClose();
        CreateOrderRes createOrderRes = new CreateOrderRes(payOrder.getPayId(),orderId,payOrder.getType(),payOrder.getPrice(),payOrder.getReallyPrice(),payUrl,payOrder.getIsAuto(),0,Integer.valueOf(timeOut),payOrder.getCreateDate());
        if (vmqSetting.getIsApprove() == 1) {
            PayInfo payInfo = new PayInfo(payOrder);
            String urlParam = "?orderId="+payInfo.getOrderId()+"&sign="+md5(payOrder.getOrderId()+key);
            payInfo.setPassUrl(url + "/passOrder" + urlParam);
            payInfo.setBackUrl(url + "/backOrder" + urlParam);
            payInfo.setDelUrl(url + "/delOrder" + urlParam);
            emailUtils.sendTemplateMail(sender, vmqSetting.getEmail(),"【码支付】待审核处理","pay-audit",payInfo);
        }
        return ResUtil.success(createOrderRes);
    }

    /**
     * 获取实际付款差额
     * @param username
     * @param vmqSetting
     * @param payOrder
     * @return boolean
     */
    public boolean checkAddAmount(String username, VmqSetting vmqSetting, PayOrder payOrder) {
        int type = payOrder.getType();
        if (type == 0) { // 手机app扫码后再处理(第二次根据app类型传值)
            payOrder.setReallyPrice(payOrder.getPrice());
            return true;
        }
        TmpPrice tmpPrice = tmpPriceDao.findByPayId(payOrder.getPayId());
        if (tmpPrice != null) { // 如果类型发生变化则重新添加付款金额
            if (tmpPrice.getPrice().startsWith(String.valueOf(type))) {
                return true;
            }
            tmpPriceDao.delete(tmpPrice);
        }
        int row = 0;
        int payQf = vmqSetting.getPayQf();
        double addPrice = 0D;
        while (row == 0){
            try {
                row = tmpPriceDao.checkPrice(username,payOrder.getPayId(),type+"-"+(payOrder.getPrice() + addPrice));
            }catch (Exception e){
                row = 0;
            }
            if (row == 0){
                if (payQf==1){
                    addPrice = ArithUtil.add(addPrice,0.01);
                }else{
                    addPrice = ArithUtil.sub(addPrice,0.01);
                }
            }else{
                break;
            }
            if (payOrder.getPrice() + addPrice <= 0){
                return false;
            }
        }
        payOrder.setReallyPrice(ArithUtil.add(payOrder.getPrice(),addPrice));
        return true;
    }

    public CommonRes closeOrder(String orderId,String sign){
        log.info("closeOrder...");
        if (StringUtils.isEmpty(sign)){
            return ResUtil.error("签名校验不通过");
        }
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        VmqSetting vmqSetting = settingDao.getSettingByUserName(payOrder.getUsername());
        String jssign = md5(orderId + vmqSetting.getMd5key());;
        if (!jssign.equals(sign)){
            return ResUtil.error("签名校验不通过");
        }
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }
        if (payOrder.getState()==1){
            return ResUtil.error("订单状态不允许关闭");
        }
        payOrder.setCloseDate(new Date().getTime());
        payOrder.setState(-1);
        payOrderDao.save(payOrder);
        return ResUtil.success();
    }

    public CommonRes appHeart(String t,String sign){
        log.info("appHeart...");
        if (StringUtils.isEmpty(sign)){
            return ResUtil.error("签名校验错误");
        }
        if (!validateTime(t)) {
            return ResUtil.error("客户端时间错误");
        }
        String jssign = "";
        VmqSetting vmqSetting = null;
        List<VmqSetting> vmqSettingList = settingDao.findAll();
        for (VmqSetting set : vmqSettingList) {
            jssign = md5(t + set.getMd5key());
            if (jssign.equals(sign)) {
                vmqSetting = set;
                break;
            }
        }
        if (Objects.isNull(vmqSetting)){
            return ResUtil.error("服务端未配置密钥");
        }
        vmqSetting.setLastheart(String.valueOf(System.currentTimeMillis()));
        vmqSetting.setJkstate("1");
        settingDao.save(vmqSetting);
        return ResUtil.success();
    }

    /**
     * 手机、PC端监控端回调
     * @param type
     * @param price
     * @param t
     * @param sign
     * @return
     */
    public CommonRes appPush(String username, Integer type,String price,String t,String sign){
        log.info("appPush...");
        if (StringUtils.isEmpty(sign)) {
            return ResUtil.error("签名校验错误");
        }
        if (!validateTime(t)) {
            ResUtil.error("客户端时间错误");
        }
        VmqSetting vmqSetting = settingDao.getSettingByUserName(username);

        if (vmqSetting == null) {
            return ResUtil.error("签名校验失败");
        }
        String key = vmqSetting.getMd5key();
        vmqSetting.setLastpay(String.valueOf(System.currentTimeMillis()));
        settingDao.save(vmqSetting);
        PayOrder tmp = payOrderDao.findByUsernameAndPayDate(username,Long.valueOf(t));
        if (tmp!=null){
            return ResUtil.error("重复推送");
        }

        PayOrder payOrder = payOrderDao.findByUsernameAndReallyPriceAndStateAndType(username,Double.valueOf(price),0,type);
        String errorMsg = "";
        if (payOrder == null) {
            payOrder = new PayOrder();
            payOrder.setPayId("无订单转账");
            payOrder.setOrderId("无订单转账");
            payOrder.setCreateDate(new Date().getTime());
            payOrder.setPayDate(new Date().getTime());
            payOrder.setCloseDate(new Date().getTime());
            payOrder.setParam("无订单转账");
            payOrder.setType(type);
            payOrder.setPrice(Double.valueOf(price));
            payOrder.setReallyPrice(Double.valueOf(price));
            payOrder.setState(1);
            payOrderDao.save(payOrder);
        } else {
            tmpPriceDao.deleteByPayId(payOrder.getPayId());
            payOrder.setState(1);
            payOrder.setPayDate(new Date().getTime());
            payOrder.setCloseDate(new Date().getTime());
            payOrderDao.save(payOrder);

            //执行通知
            String p = "payId=" + payOrder.getPayId() + "&orderId=" + payOrder.getOrderId() + "&param=" + payOrder.getParam() + "&type=" + payOrder.getType() + "&price=" + payOrder.getPrice() + "&reallyPrice=" + payOrder.getReallyPrice();
            sign = md5(payOrder.getPayId() + payOrder.getParam() + payOrder.getType() + payOrder.getPrice() + payOrder.getReallyPrice() + key);
            p = p + "&sign=" + sign;
            String url = payOrder.getNotifyUrl();
            if (StringUtils.isBlank(url)) {
                url = vmqSetting.getNotifyUrl();
                if (StringUtils.isBlank(url)) {
                    payOrderDao.setState(2, payOrder.getId());
                    errorMsg = "您还未配置异步通知地址，请先在系统配置中配置";
                }
            }

            if (StringUtils.isEmpty(errorMsg)) {
                String res = HttpRequest.sendGet(url, p);
                if (!(res != null && res.equals("success"))) {
                    //通知失败，设置状态为2
                    payOrderDao.setState(2, payOrder.getId());
                    errorMsg = "通知异步地址失败: " + res;
                }
            }
        }
        if (vmqSetting.getIsNotice() == 1 && EmailUtils.checkEmail(payOrder.getEmail())) {
            PayInfo payInfo = new PayInfo(payOrder);
            emailUtils.sendTemplateMail(sender, payOrder.getEmail(),Constant.PAY_SUCCESS_EMAIL_TITLE,Constant.PAY_SUCCESS_EMAIL_TEMPLATE, payInfo);
        }
        log.info("appPush end: {}",errorMsg);
        return StringUtils.isEmpty(errorMsg)?ResUtil.success():ResUtil.error(errorMsg);
    }

    /**
     * 校验客户端时间
     * @param t 时间戳，支持秒或者毫秒
     * @return 超过1分钟则返回false
     */
    private static boolean validateTime(String t) {
        if (t.length() == 10) {
            t += "000";
        }
        long cz = Long.valueOf(t)- DateUtil.current();
        if (cz<0){
            cz = cz*-1;
        }
        if (cz>Constant.MIN_UNIT){
            return false;
        }
        return true;
    }

    /**
     * 云端监控回调
     * @param username
     * @param type
     * @param price
     * @param outTradeNo
     * @return String
     */
    public String webPush(String username, int type, String price, String outTradeNo) {
        log.info("webPush...");
        PayOrder tmp = payOrderDao.findByUsernameAndPayId(username,outTradeNo);
        if (tmp!=null){
            return "已处理";
        }
        VmqSetting vmqSetting = settingDao.getSettingByUserName(username);
        String key = vmqSetting.getMd5key();
        PayOrder payOrder = payOrderDao.findByUsernameAndReallyPriceAndStateAndType(username,Double.valueOf(price),0,type);
        String message = Constant.SUCCESS;
        if (payOrder == null){
            payOrder = new PayOrder();
            payOrder.setPayId(outTradeNo);
            payOrder.setUsername(username);
            payOrder.setOrderId("无订单转账");
            payOrder.setCreateDate(new Date().getTime());
            payOrder.setPayDate(new Date().getTime());
            payOrder.setCloseDate(new Date().getTime());
            payOrder.setType(type);
            payOrder.setPrice(Double.valueOf(price));
            payOrder.setReallyPrice(Double.valueOf(price));
            payOrder.setState(1);
            payOrderDao.save(payOrder);
        }else{
            Object orderPush = redisTemplate.opsForValue().get(payOrder.getUnitCode());
            if (orderPush != null) {
                return "重复推送";
            }
            tmpPriceDao.deleteByPayId(payOrder.getPayId());
            payOrder.setState(1);
            payOrder.setPayDate(new Date().getTime());
            payOrder.setCloseDate(new Date().getTime());
            payOrder.setPayId(outTradeNo);
            payOrderDao.save(payOrder);

            //执行通知
            String p = "payId="+payOrder.getPayId()+"&orderId="+payOrder.getOrderId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
            String sign = md5(payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key);
            p = p+"&sign="+sign;
            String url = payOrder.getNotifyUrl();
            if (StringUtils.isBlank(url)){
                url = vmqSetting.getNotifyUrl();
                if (StringUtils.isBlank(url)){
                    payOrderDao.setState(2,payOrder.getId());
                    message = "您还未配置异步通知地址，请先在系统配置中配置";
                }
            }

            if (StringUtils.isEmpty(message)) {
                String res = HttpRequest.sendGet(url, p);
                if (!(res!=null && res.equals("success"))){
                    //通知失败，设置状态为2
                    payOrderDao.setState(2,payOrder.getId());
                    message = "通知异步地址失败: " + res;
                }
            }
        }
        if (vmqSetting.getIsNotice() == 1 && EmailUtils.checkEmail(payOrder.getEmail())) {
            PayInfo payInfo = new PayInfo(payOrder);
            emailUtils.sendTemplateMail(sender, payOrder.getEmail(),Constant.PAY_SUCCESS_EMAIL_TITLE,Constant.PAY_SUCCESS_EMAIL_TEMPLATE, payInfo);
        }
        log.info("webPush end: {}",message);
        return message;
    }

    public CommonRes getOrder(String orderId){
        log.info("getOrder...");
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }
        VmqSetting vmqSetting = settingDao.getSettingByUserName(payOrder.getUsername());
        String timeOut = vmqSetting.getClose();
        CreateOrderRes createOrderRes = new CreateOrderRes(
                payOrder.getPayId(),payOrder.getOrderId(),payOrder.getType(),payOrder.getPrice(),payOrder.getReallyPrice()
                ,payOrder.getPayUrl(),payOrder.getIsAuto(),payOrder.getState(),Integer.valueOf(timeOut),payOrder.getCreateDate());
        if (payOrder.getPayCodeId() != null) {
            PayQrcode qrcode = payQrcodeDao.getById(payOrder.getPayCodeId());
            if (qrcode != null) {
                createOrderRes.setPayUrl(qrcode.getPayUrl());
            }
        }
        return ResUtil.success(createOrderRes);
    }

    public CommonRes checkOrder(String orderId){
        log.info("checkOrder...");
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        } else if (payOrder.getState() != 1) {
            TmpPrice tmpPrice = tmpPriceDao.findByPayId(payOrder.getPayId());
            if (payOrder.getState() == -1) {
                return ResUtil.error("订单已过期");
            } else if (tmpPrice == null) {
                return ResUtil.error("手机未扫码");
            } else if (payOrder.getState()==0){
                return ResUtil.error("订单未支付");
            }
        }
        String username = payOrder.getUsername();
        VmqSetting vmqSetting = settingDao.getSettingByUserName(username);
        String key = vmqSetting.getMd5key();
        //执行通知
        String p = "payId="+payOrder.getPayId()+"&orderId="+payOrder.getOrderId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
        String sign = md5(payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key);
        p = p+"&sign="+sign;
        String url = payOrder.getReturnUrl();
        if (StringUtils.isBlank(url)){
            url = vmqSetting.getReturnUrl();
        }
        return ResUtil.success(url+"?"+p);
    }

    /**
     * 用户配置 -> 默认配置
     * @param username
     * @return
     */
    public VmqSetting getDefaultSetting(String username) {
        if (StringUtils.isEmpty(username)) {
            username = default_user;
        }
        VmqSetting vmqSetting = settingDao.getSettingByUserName(username);
        if (vmqSetting == null && !username.equals(default_user)) {
            vmqSetting = settingDao.getSettingByUserName(default_user);
        }
        return vmqSetting;
    }

    public CommonRes getState(String t,String sign){
        Map<String,String> map = new HashMap<>();
        return ResUtil.success(map);
    }

    public static String md5(String text) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5DigestAsHex(text.getBytes());
        return encodeStr;
    }

    public boolean checkRepeatPush(String username, int payType, String reallyPrice, Long payTime) {
        String unionKey = username + payType + reallyPrice;
        if (redisTemplate.opsForValue().get(unionKey) != null) {
            // 还需要校验该时间段是否只有一个临时价格
            return true;
        }
        redisTemplate.opsForValue().set(unionKey, Constant.ORDER_PUSH, Constant.NUMBER_2, TimeUnit.SECONDS);
        return false;
    }

    public String getUsernameBySign(Integer type, String price, String t, String sign) {
        List<VmqSetting> vmqSettingList = settingDao.findAll();
        String jssign = "";
        for (VmqSetting set : vmqSettingList) {
            String key = set.getMd5key();
            if (!StringUtils.isEmpty(key)) {
                jssign = md5(type + price + t + key);
                if (jssign.equals(sign)){
                    return set.getUsername();
                }
            }
        }
        return "";
    }

    /**
     * 根据金额和付款类型获取收款码
     * @param vmqSetting
     * @param payOrder
     * @return
     */
    public String getPayUrl(VmqSetting vmqSetting, PayOrder payOrder) {
        int isAuto = 0;
        Long payCodeId = null;
        String username = payOrder.getUsername();
        String payUrl = PayTypeEnum.getPayUrlByOrder(vmqSetting, payOrder);
        PayQrcode payQrcode = payQrcodeDao.findByUsernameAndPriceAndType(username, payOrder.getReallyPrice(), payOrder.getType());
        if (payQrcode == null) {
            isAuto = 1;
            payQrcode = payQrcodeDao.findByUsernameAndPriceAndType(username,0, payOrder.getType());
        }
        if (payQrcode != null){
            payCodeId = payQrcode.getId();
            payUrl = payQrcode.getPayUrl();
        }
        payOrder.setIsAuto(isAuto);
        payOrder.setPayCodeId(payCodeId);
        payOrder.setPayUrl(payUrl);
        return payUrl;
    }
}
