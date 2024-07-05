package com.vone.mq.utils;

import com.vone.mq.dao.SettingDao;
import com.vone.mq.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Date;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private SettingDao settingDao;

    @Override
    public void run(ApplicationArguments var1) {
        System.out.println("开始初始化操作...");

        //查询是不是首次启动，如果是就创建基础的设置数据
        int row = (int) settingDao.count();
        if (row==0){
            System.out.println("检测到系统为首次启动，正在进行数据库初始化...");
            Setting setting = new Setting();
            //管理员账号
            setting.setVkey("user");
            setting.setVvalue("admin");
            settingDao.save(setting);

            //管理员密码
            setting.setVkey("pass");
            setting.setVvalue("admin");
            settingDao.save(setting);

            //异步通知地址
            setting.setVkey("notifyUrl");
            setting.setVvalue("");
            settingDao.save(setting);

            //同步通知地址
            setting.setVkey("returnUrl");
            setting.setVvalue("");
            settingDao.save(setting);

            //通讯密钥
            setting.setVkey("key");
            setting.setVvalue(md5(String.valueOf(new Date().getTime())));
            settingDao.save(setting);

            //监控端最后心跳
            setting.setVkey("lastheart");
            setting.setVvalue("0");
            settingDao.save(setting);

            //监控端最后收款
            setting.setVkey("lastpay");
            setting.setVvalue("0");
            settingDao.save(setting);

            //监控端状态
            setting.setVkey("jkstate");
            setting.setVvalue("-1");
            settingDao.save(setting);

            //订单最有效时间
            setting.setVkey("close");
            setting.setVvalue("5");
            settingDao.save(setting);

            //区分方式
            setting.setVkey("payQf");
            setting.setVvalue("1");
            settingDao.save(setting);

            //微信通用收款码
            setting.setVkey("wxpay");
            setting.setVvalue("");
            settingDao.save(setting);
            //支付宝通用收款码
            setting.setVkey("zfbpay");
            setting.setVvalue("");
            settingDao.save(setting);

        }
        System.out.println("系统启动完成！");

    }

    public static String md5(String text) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5DigestAsHex(text.getBytes());
        return encodeStr;
    }
}
