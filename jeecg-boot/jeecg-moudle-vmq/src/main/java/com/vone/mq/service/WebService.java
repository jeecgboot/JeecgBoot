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
import com.vone.mq.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebService {
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private TmpPriceDao tmpPriceDao;
    @Autowired
    private PayQrcodeDao payQrcodeDao;

    public CommonRes createOrder(String payId, String param, Integer type, String price, String notifyUrl, String returnUrl, String sign){
        String key = settingDao.findById("key").get().getVvalue();
        String jsSign =  md5(payId+param+type+price+key);
        if (!sign.equals(jsSign)){
            return ResUtil.error("签名校验不通过");
        }

        Double priceD = Double.valueOf(price);


        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String orderId = formatter.format(currentTime) + (int)(1000+Math.random()*(9999-1000+1));

        int payQf = Integer.parseInt(settingDao.findById("payQf").get().getVvalue());
        //实际支付价格
        double reallyPrice = priceD;

        int row = 0;
        while (row == 0){
            try {
                row = tmpPriceDao.checkPrice(type+"-"+reallyPrice);
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
            payUrl = settingDao.findById("wxpay").get().getVvalue();
        }else if (type == 2){
            payUrl = settingDao.findById("zfbpay").get().getVvalue();
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
            tmpPriceDao.deleteById(type+"-"+reallyPrice);
            return ResUtil.error("商户订单号已存在！");
        }

        PayOrder payOrder = new PayOrder();
        payOrder.setPayId(payId);
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



        String timeOut = settingDao.findById("close").get().getVvalue();
        CreateOrderRes createOrderRes = new CreateOrderRes(payId,orderId,type,priceD,reallyPrice,payUrl,isAuto,0,Integer.valueOf(timeOut),payOrder.getCreateDate());

        return ResUtil.success(createOrderRes);
    }
    public CommonRes closeOrder(String orderId,String sign){

        String key = settingDao.findById("key").get().getVvalue();
        String jsSign =  md5(orderId+key);
        if (!sign.equals(jsSign)){
            return ResUtil.error("签名校验不通过");
        }

        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }
        if (payOrder.getState()!=0){
            return ResUtil.error("订单状态不允许关闭");
        }
        tmpPriceDao.delprice(payOrder.getType()+"-"+payOrder.getReallyPrice());
        payOrder.setCloseDate(new Date().getTime());
        payOrder.setState(-1);
        payOrderDao.save(payOrder);
        return ResUtil.success();
    }

    public CommonRes appHeart(String t,String sign){
        String key = settingDao.findById("key").get().getVvalue();
        String jssign = md5(t+key);
        if (!jssign.equals(sign)){
            return ResUtil.error("签名校验错误");
        }
        long cz = Long.valueOf(t)-new Date().getTime();

        if (cz<0){
            cz = cz*-1;
        }
        if (cz>50*1000){
            return ResUtil.error("客户端时间错误");
        }

        Setting setting = new Setting();
        setting.setVkey("lastheart");
        setting.setVvalue(t);
        settingDao.save(setting);

        setting.setVkey("jkstate");
        setting.setVvalue("1");
        settingDao.save(setting);

        return ResUtil.success();
    }

    public CommonRes appPush(Integer type,String price,String t,String sign){
        String key = settingDao.findById("key").get().getVvalue();
        long cz = Long.valueOf(t)-new Date().getTime();

        if (cz<0){
            cz = cz*-1;
        }
        if (cz>50*1000){
            return ResUtil.error("客户端时间错误");
        }
        String jssign = md5(type+""+price+t+key);
        if (!jssign.equals(sign)){
            return ResUtil.error("签名校验错误");
        }

        Setting setting = new Setting();
        setting.setVkey("lastpay");
        setting.setVvalue(t);
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
            tmpPriceDao.delprice(type+"-"+price);

            payOrder.setState(1);
            payOrder.setPayDate(new Date().getTime());
            payOrder.setCloseDate(new Date().getTime());
            payOrderDao.save(payOrder);

            //执行通知
            String p = "payId="+payOrder.getPayId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
            sign = md5(payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key);
            p = p+"&sign="+sign;
            String url = payOrder.getNotifyUrl();
            if (url==null || url.equals("")){
                url = settingDao.findById("notifyUrl").get().getVvalue();
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
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder==null){
            return ResUtil.error("云端订单编号不存在");
        }

        String timeOut = settingDao.findById("close").get().getVvalue();
        CreateOrderRes createOrderRes = new CreateOrderRes(
                payOrder.getPayId(),payOrder.getOrderId(),payOrder.getType(),payOrder.getPrice(),payOrder.getReallyPrice()
                ,payOrder.getPayUrl(),payOrder.getIsAuto(),payOrder.getState(),Integer.valueOf(timeOut),payOrder.getCreateDate());

        return ResUtil.success(createOrderRes);
    }



    public CommonRes checkOrder(String orderId){
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
        String key = settingDao.findById("key").get().getVvalue();
        //执行通知
        String p = "payId="+payOrder.getPayId()+"&param="+payOrder.getParam()+"&type="+payOrder.getType()+"&price="+payOrder.getPrice()+"&reallyPrice="+payOrder.getReallyPrice();
        String sign = md5(payOrder.getPayId()+payOrder.getParam()+payOrder.getType()+payOrder.getPrice()+payOrder.getReallyPrice()+key);
        p = p+"&sign="+sign;
        String url = payOrder.getReturnUrl();
        if (url==null){
            url = settingDao.findById("returnUrl").get().getVvalue();
        }

        return ResUtil.success(url+"?"+p);
    }

    public CommonRes getState(String t,String sign){

        String key = settingDao.findById("key").get().getVvalue();
        String jsSign =  md5(t+key);
        if (!sign.equals(jsSign)){
            return ResUtil.error("签名校验不通过");
        }

        Map<String,String> map = new HashMap<>();
        String state = settingDao.findById("jkstate").get().getVvalue();
        String lastheart = settingDao.findById("lastheart").get().getVvalue();
        String lastpay = settingDao.findById("lastpay").get().getVvalue();
        map.put("state",state);
        map.put("lastheart",lastheart);
        map.put("lastpay",lastpay);

        return ResUtil.success(map);
    }

    public static String md5(String text) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5DigestAsHex(text.getBytes());
        return encodeStr;
    }
}
