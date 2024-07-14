package com.vone.mq.service;

import com.vone.mq.dao.PayOrderDao;
import com.vone.mq.dao.PayQrcodeDao;
import com.vone.mq.dao.SettingDao;
import com.vone.mq.dao.TmpPriceDao;
import com.vone.mq.dto.CommonRes;
import com.vone.mq.dto.CreateOrderRes;
import com.vone.mq.entity.PayOrder;
import com.vone.mq.entity.PayQrcode;
import com.vone.mq.entity.Setting;
import com.vone.mq.utils.Arith;
import com.vone.mq.utils.HttpRequest;
import com.vone.mq.utils.JWTUtil;
import com.vone.mq.utils.ResUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class WebService {

    private static final String default_user = "guest";

    @Autowired
    private SettingDao settingDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private TmpPriceDao tmpPriceDao;
    @Autowired
    private PayQrcodeDao payQrcodeDao;

    public String getMd5(String username, String content) {
        String key = getDefaultMd5Key(username);
        return md5(content + key);
    }

    /**
     * 外部调用必须传token
     * @param payId
     * @param param
     * @param type
     * @param price
     * @param notifyUrl
     * @param returnUrl
     * @param sign
     * @return
     */
    public CommonRes createOrder(String payId, String param, Integer type, String price, String notifyUrl, String returnUrl, String sign){
        String username = JWTUtil.getUsername(param);
        String msg = "自定义";
        if (StringUtils.isEmpty(username)) {
            username = default_user;
            msg = "使用";
        }
        log.info("{}【{}】密钥创建订单{}",msg,username,payId);
        String key = getDefaultMd5Key(username);
        String jsSign =  md5(payId+param+type+price+key);
        if (!sign.equals(jsSign)){
            return ResUtil.error("签名校验不通过");
        }

        Double priceD = Double.valueOf(price);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderId = formatter.format(currentTime) + (int)(1000+Math.random()*(9999-1000+1));
        Setting setting = getDefaultSetting(username);//settingDao.getSettingByUserName(username);

        //实际支付价格
        double reallyPrice = priceD;
        int row = 0;
        int payQf = Integer.parseInt(setting.getPayQf());
        while (row == 0){
            try {
                row = tmpPriceDao.checkPrice(username,type+"-"+reallyPrice);
            }catch (Exception e){
                row = 0;
            }
            if (row == 0){
                if (payQf==1){
                    reallyPrice = Arith.add(reallyPrice,0.01);
                }else{
                    reallyPrice = Arith.sub(reallyPrice,0.01);
                }
            }else{
                break;
            }
            if (reallyPrice<=0){
                return ResUtil.error("所有金额均被占用");
            }
        }

        String payUrl = "";
        if (type == 1){
            payUrl = setting.getWxpay();
        }else if (type == 2){
            payUrl = setting.getZfbpay();
        }
        if (payUrl==""){
            return ResUtil.error("请您先进入后台配置程序");
        }

        int isAuto = 1;
        PayQrcode payQrcode = payQrcodeDao.findByPriceAndType(reallyPrice,type);
        if (payQrcode!=null){
            payUrl = payQrcode.getPayUrl();
            isAuto = 0;
        }

        PayOrder tmp = payOrderDao.findByPayId(payId);
        if (tmp!=null){
            tmpPriceDao.delprice(username,type+"-"+reallyPrice);
            return ResUtil.error("商户订单号已存在！");
        }

        PayOrder payOrder = new PayOrder();
        payOrder.setPayId(payId);
        payOrder.setUsername(username);
        payOrder.setOrderId(orderId);
        payOrder.setCreateDate(new Date().getTime());
        payOrder.setPayDate(0);
        payOrder.setCloseDate(0);
        payOrder.setParam(param);
        payOrder.setType(type);
        payOrder.setPrice(priceD);
        payOrder.setReallyPrice(reallyPrice);
        payOrder.setNotifyUrl(notifyUrl);
        payOrder.setReturnUrl(returnUrl);
        payOrder.setState(0);
        payOrder.setIsAuto(isAuto);
        payOrder.setPayUrl(payUrl);

        payOrderDao.save(payOrder);

        String timeOut = setting.getClose();
        CreateOrderRes createOrderRes = new CreateOrderRes(payId,orderId,type,priceD,reallyPrice,payUrl,isAuto,0,Integer.valueOf(timeOut),payOrder.getCreateDate());

        return ResUtil.success(createOrderRes);
    }

    public CommonRes closeOrder(String orderId,String sign){
        log.info("closeOrder...");
        String jssign = "";
        Setting setting = null;
        List<Setting> settingList = settingDao.findAll();
        for (Setting set : settingList) {
            jssign = md5(orderId + set.getMd5key());
            if (jssign.equals(sign)) {
                setting = set;
                break;
            }
        }
        if (StringUtils.isEmpty(sign)){
            return ResUtil.error("签名校验不通过");
        }
        if (Objects.isNull(setting)){
            return ResUtil.error("服务端未配置密钥");
        }
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }
        if (payOrder.getState()==1){
            return ResUtil.error("订单状态不允许关闭");
        }
        tmpPriceDao.delprice(payOrder.getUsername(),payOrder.getType()+"-"+payOrder.getReallyPrice());
        payOrder.setCloseDate(new Date().getTime());
        payOrder.setState(-1);
        payOrderDao.save(payOrder);
        return ResUtil.success();
    }

    public CommonRes appHeart(String t,String sign){
        log.info("appHeart...");
        String jssign = "";
        Setting setting = null;
        List<Setting> settingList = settingDao.findAll();
        for (Setting set : settingList) {
            jssign = md5(t + set.getMd5key());
            if (jssign.equals(sign)) {
                setting = set;
                break;
            }
        }
        if (StringUtils.isEmpty(sign)){
            return ResUtil.error("签名校验错误");
        }
        if (Objects.isNull(setting)){
            return ResUtil.error("服务端未配置密钥");
        }
        long cz = Long.valueOf(t)-new Date().getTime();

        if (cz<0){
            cz = cz*-1;
        }
        if (cz>50*1000){
            return ResUtil.error("客户端时间错误");
        }
        setting.setLastheart(t);
        setting.setJkstate("1");
        settingDao.save(setting);
        return ResUtil.success();
    }

    /**
     * key保存在监控端，服务端不可见，需要通过回调校验sign
     * @param type
     * @param price
     * @param t
     * @param sign
     * @return
     */
    public CommonRes appPush(Integer type,String price,String t,String sign){
        log.info("appPush...");
        List<Setting> settingList = settingDao.findAll();
        long cz = Long.valueOf(t)-new Date().getTime();
        if (cz<0){
            cz = cz*-1;
        }
        if (cz>50*1000){
            return ResUtil.error("客户端时间错误");
        }
        if (StringUtils.isEmpty(sign)) {
            return ResUtil.error("签名校验错误");
        }
        Setting setting = null;
        for (Setting set : settingList) {
            String key = set.getMd5key();
            String jssign = md5(type+""+price+t+key);
            if (jssign.equals(sign)){
                setting = set;
            }
        }
        if (setting == null) {
            return ResUtil.error("签名校验失败");
        }
        String key = setting.getMd5key();
        setting.setLastpay(t);
        settingDao.save(setting);
        PayOrder tmp = payOrderDao.findByPayDate(Long.valueOf(t));
        if (tmp!=null){
            return ResUtil.error("重复推送");
        }

        PayOrder payOrder = payOrderDao.findByReallyPriceAndStateAndType(Double.valueOf(price),0,type);

        if (payOrder==null){

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
            payOrder.setPayUrl("无订单转账");
            payOrderDao.save(payOrder);
            return ResUtil.success();

        }else{
            tmpPriceDao.delprice(payOrder.getUsername(),type+"-"+price);

            payOrder.setState(1);
            payOrder.setPayDate(new Date().getTime());
            payOrder.setCloseDate(new Date().getTime());
            payOrderDao.save(payOrder);

            //执行通知
            String p = "payId="+payOrder.getPayId()+"&orderId="+payOrder.getOrderId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
            sign = md5(payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key);
            p = p+"&sign="+sign;
            String url = payOrder.getNotifyUrl();
            if (url==null || url.equals("")){
                url = setting.getNotifyUrl();
                if (url==null || url.equals("")){
                    payOrderDao.setState(2,payOrder.getId());
                    return ResUtil.error("您还未配置异步通知地址，请现在系统配置中配置");
                }
            }

            String res = HttpRequest.sendGet(url,p);

            if (res!=null && res.equals("success")){
                return ResUtil.success();
            }else {
                //通知失败，设置状态为2
                payOrderDao.setState(2,payOrder.getId());
                return ResUtil.error("通知异步地址失败");
            }
        }
    }

    public CommonRes getOrder(String orderId){
        log.info("getOrder...");
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }
        Setting setting = getDefaultSetting(default_user);
        String timeOut = setting.getClose();
        CreateOrderRes createOrderRes = new CreateOrderRes(
                payOrder.getPayId(),payOrder.getOrderId(),payOrder.getType(),payOrder.getPrice(),payOrder.getReallyPrice()
                ,payOrder.getPayUrl(),payOrder.getIsAuto(),payOrder.getState(),Integer.valueOf(timeOut),payOrder.getCreateDate());
        return ResUtil.success(createOrderRes);
    }

    public CommonRes checkOrder(String orderId){
        log.info("checkOrder...");
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }
        if (payOrder.getState()==0){
            return ResUtil.error("订单未支付");
        }
        if (payOrder.getState()==-1){
            return ResUtil.error("订单已过期");
        }
        String token = payOrder.getParam();
        String username = JWTUtil.getUsername(token);
        if (StringUtils.isEmpty(username)) {
            username = default_user;
        }
        Setting setting = settingDao.getSettingByUserName(username);
        String key = getDefaultMd5Key(payOrder.getUsername());
        //执行通知
        String p = "payId="+payOrder.getPayId()+"&orderId="+payOrder.getOrderId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
        String sign = md5(payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key);
        p = p+"&sign="+sign;
        String url = payOrder.getReturnUrl();
        if (url==null){
            url = setting.getReturnUrl();
        }
        return ResUtil.success(url+"?"+p);
    }

    /**
     * 用户key -> 默认key
     * @param username
     * @return
     */
    public String getDefaultMd5Key(String username) {
        String key = "";
        Setting setting = getDefaultSetting(username);
        if (setting != null) {
            if (StringUtils.isEmpty(setting.getMd5key()) && !setting.getUsername().equals(default_user)) {
                log.debug("【{}】用户未配置密钥，使用【{}】密钥...",username,default_user);
                setting = settingDao.getSettingByUserName(default_user);
            }
            key = setting.getMd5key();
        }
        return key;
    }

    /**
     * 用户配置 -> 默认配置
     * @param username
     * @return
     */
    public Setting getDefaultSetting(String username) {
        if (StringUtils.isEmpty(username)) {
            username = default_user;
        }
        Setting setting = settingDao.getSettingByUserName(username);
        if (setting == null && !username.equals(default_user)) {
            setting = settingDao.getSettingByUserName(default_user);
        }
        return setting;
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
}
