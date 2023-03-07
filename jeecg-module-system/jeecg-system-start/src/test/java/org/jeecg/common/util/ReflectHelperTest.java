package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.security.entity.MyKeyPair;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

@Slf4j
class ReflectHelperTest {

    private final static int LOOP_TIMES = 100000;

    @Test
    void initMethods() {
        long start = System.currentTimeMillis();
        for (int i=0;i<LOOP_TIMES;i++){
            MyKeyPair obj = new MyKeyPair();
            ReflectHelper reflectHelper = new ReflectHelper(obj);
            reflectHelper.initMethods();
        }
        long end = System.currentTimeMillis();
        long cost = end - start;
        // 原来方式
        long start0 = System.currentTimeMillis();
        for (int i=0;i<LOOP_TIMES;i++){
            ReflectHelper helper = new ReflectHelper();
            helper.initMethods2(new MyKeyPair());
        }
        long end0 = System.currentTimeMillis();
        long cost0 = end0 - start0;
        log.warn("[Result] loop {} times for Test. cost time = {}ms , and old type cost time = {}ms", LOOP_TIMES, cost, cost0);
        // Assert
        Assert.assertTrue(cost0 > cost);
        MyKeyPair myKeyPair = new MyKeyPair();
        myKeyPair.setPriKey("KKey");
        ReflectHelper helper1 = new ReflectHelper(myKeyPair);
        ReflectHelper helper2 = new ReflectHelper();
        helper2.initMethods2(myKeyPair);
        String property = "priKey";
        Assert.assertEquals(helper1.getMethodValue(property), helper2.getMethodValue(property));
    }
}