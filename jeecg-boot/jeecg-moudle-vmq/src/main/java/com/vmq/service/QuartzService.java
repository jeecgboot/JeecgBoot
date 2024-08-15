package com.vmq.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.domain.TradeItemResult;
import com.vmq.constant.Constant;
import com.vmq.constant.PayTypeEnum;
import com.vmq.dao.PayOrderDao;
import com.vmq.dao.SettingDao;
import com.vmq.dao.TmpPriceDao;
import com.vmq.entity.VmqSetting;
import com.vmq.utils.AliPayUtil;
import com.vmq.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class QuartzService {

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private PayOrderDao payOrderDao;

    @Autowired
    private TmpPriceDao tmpPriceDao;

    @Autowired
    private WebService webService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 处理过期订单：更新订单状态并删除临时金额
     */
    @Scheduled(fixedRate = 30000)
    public void autoUpdatePayOrderStatus(){
        String method = "autoUpdatePayOrderStatus";
        String temp = redisTemplate.opsForValue().get(method);
        if (StringUtils.isNotBlank(temp)) {
            return;
        }
        String msg = "";
        // 获取超时时间（分钟）
        List<VmqSetting> vmqSettingList = settingDao.findAll();
        for (VmqSetting vmqSetting : vmqSettingList) {
            String username = vmqSetting.getUsername();
            try {
                String timeout = vmqSetting.getClose();
                long timeoutMS = Integer.valueOf(timeout)*Constant.MIN_UNIT;

                String closeTime = String.valueOf(new Date().getTime());
                timeout = String.valueOf(new Date().getTime() - timeoutMS);

                // 更改过期订单关闭时间和状态
                int row = payOrderDao.setTimeout(username,timeout,closeTime);

                long beginTime = Long.valueOf(closeTime) - timeoutMS;

                List<Map<String,Object>> payOrders = payOrderDao.findAllByCloseDate(username,beginTime, Long.valueOf(closeTime));
                for (Map payOrder: payOrders) {
                    tmpPriceDao.deleteByPayId((String) payOrder.get("pay_id"));
                }
                // 补偿机制，删除异常数据
                int delCount = tmpPriceDao.delpriceByUsername(username);
                msg += String.format("[%s]成功清理%d个订单 ", username, row+delCount);
            }catch (Exception e){
                e.printStackTrace();
                log.info("清理订单失败: {}",e.getMessage());
            }
            String lastheart = vmqSetting.getLastheart();
            String state = vmqSetting.getJkstate();
            if ("1".equals(state) && new Date().getTime() - Long.valueOf(lastheart) > 60*1000){
                vmqSetting.setJkstate("0");
                settingDao.save(vmqSetting);
            }
        }
        log.info(msg);
        redisTemplate.opsForValue().set(method, method, Constant.NUMBER_20, TimeUnit.SECONDS);
    }

    /**
     * 查询支付宝账单
     */
    @Scheduled(fixedRate = 5000)
    public void queryAliPayAndPush(){
        String method = "queryAliPayAndPush";
        String temp = redisTemplate.opsForValue().get(method);
        if (StringUtils.isNotBlank(temp)) {
            return;
        }
        // 获取超时时间（分钟）
        List<VmqSetting> vmqSettingList = settingDao.findAll();
        for (VmqSetting vmqSetting : vmqSettingList) {
            String username = vmqSetting.getUsername();
            if (StringUtils.isBlank(vmqSetting.getAppId())
            || StringUtils.isBlank(vmqSetting.getPrivateKey())
            || StringUtils.isBlank(vmqSetting.getAlipayPublicKey())) {
                continue;
            }
            List<TradeItemResult> billSells = AliPayUtil.getAlipayBillSell(Integer.parseInt(vmqSetting.getClose()),vmqSetting);
            if (CollectionUtil.isNotEmpty(billSells)) {
                billSells.stream().forEach(detail -> validAlipayBillSell(username,detail));
            }
        }
        log.info("queryAliPayAndPush success");
        redisTemplate.opsForValue().set(method, method, Constant.NUMBER_8, TimeUnit.SECONDS);
    }

    private void validAlipayBillSell(String username, TradeItemResult detail) {
        int payType = PayTypeEnum.ZFB.getCode();
        String gmtCreate = detail.getGmtCreate();
        String outTradeNo = detail.getMerchantOrderNo();
        String totalAmount = detail.getTotalAmount();
        String gmtPay = detail.getGmtPay();
        String tradeStatus = detail.getTradeStatus();
        long payTime = DateUtil.parseDateTime(gmtPay).getTime();
        long createTime = DateUtil.parseDateTime(gmtCreate).getTime();
        if (Constant.SUCCESS.equals(tradeStatus)) {
            if (System.currentTimeMillis() - payTime > Constant.MIN5) {
                return;
            }
            log.debug("创建时间：{}\t名称：{}\t商户订单号：{}\t买家信息：{}\t订单金额：{}\t退款金额：{}\t交易状态：{}",gmtCreate,detail.getGoodsTitle(),outTradeNo,detail.getOtherAccount(),totalAmount,detail.getRefundAmount(),tradeStatus);
            if (webService.checkRepeatPush(username,payType,totalAmount,Long.valueOf(payTime))) {
                return;
            }
            String result = webService.webPush(username, payType, totalAmount, outTradeNo,createTime,payTime);
            log.info("validAliPayInfo: {}",result);
        }
    }

}
