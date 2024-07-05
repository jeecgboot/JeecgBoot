package com.vone.mq.service;

import com.vone.mq.dao.PayOrderDao;
import com.vone.mq.dao.SettingDao;
import com.vone.mq.dao.TmpPriceDao;
import com.vone.mq.entity.PayOrder;
import com.vone.mq.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class QuartzService {
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private TmpPriceDao tmpPriceDao;


    @Scheduled(fixedRate = 30000)
    public void timerToZZP(){

        try {
            System.out.println("开始清理过期订单...");
            String timeout = settingDao.findById("close").get().getVvalue();
            String closeTime = String.valueOf(new Date().getTime());
            timeout = String.valueOf(new Date().getTime() - Integer.valueOf(timeout)*60*1000);

            int row = payOrderDao.setTimeout(timeout,closeTime);

            List<PayOrder> payOrders = payOrderDao.findAllByCloseDate(Long.valueOf(closeTime));
            for (PayOrder payOrder: payOrders) {
                tmpPriceDao.delprice(payOrder.getType()+"-"+payOrder.getReallyPrice());
            }
            System.out.println(row+"成功清理" + row + "个订单");
        }catch (Exception e){
            e.printStackTrace();
        }

        String lastheart = settingDao.findById("lastheart").get().getVvalue();
        String state = settingDao.findById("jkstate").get().getVvalue();
        if (state.equals("1") && new Date().getTime() - Long.valueOf(lastheart) > 60*1000){
            Setting setting = new Setting();
            setting.setVkey("jkstate");
            setting.setVvalue("0");
            settingDao.save(setting);
        }



    }
}
