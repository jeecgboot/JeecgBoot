package com.vmq.service;

import com.vmq.dao.PayOrderDao;
import com.vmq.dao.SettingDao;
import com.vmq.dao.TmpPriceDao;
import com.vmq.entity.VmqSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class QuartzService {
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private TmpPriceDao tmpPriceDao;
    
    @Scheduled(fixedRate = 60000)
    public void timerToZZP(){
        // 获取超时时间（分钟）
        List<VmqSetting> vmqSettingList = settingDao.findAll();
        for (VmqSetting vmqSetting : vmqSettingList) {
            String username = vmqSetting.getUsername();
            try {
                String timeout = vmqSetting.getClose();
                int timeoutMS = Integer.valueOf(timeout)*60*1000;

                String closeTime = String.valueOf(new Date().getTime());
                timeout = String.valueOf(new Date().getTime() - timeoutMS);

                // 更改过期订单关闭时间和状态
                int row = payOrderDao.setTimeout(username,timeout,closeTime);

                long beginTime = Long.valueOf(closeTime) - timeoutMS * 10;

                List<Map<String,Object>> payOrders = payOrderDao.findAllByCloseDate(username,beginTime, Long.valueOf(closeTime));
                for (Map payOrder: payOrders) {
                    tmpPriceDao.delprice(username,payOrder.get("type")+"-"+payOrder.get("really_price"));
                }
                tmpPriceDao.delpriceByUsername(username);
                log.info("成功清理" + row + "个订单");
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
    }
}
