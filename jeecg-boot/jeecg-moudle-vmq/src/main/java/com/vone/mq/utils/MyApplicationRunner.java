package com.vone.mq.utils;

import com.vone.mq.dao.SettingDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyApplicationRunner implements ApplicationRunner {
    
    @Autowired
    private SettingDao settingDao;

    @Override
    public void run(ApplicationArguments var1) {
        log.info("开始初始化操作...");
    }
}
