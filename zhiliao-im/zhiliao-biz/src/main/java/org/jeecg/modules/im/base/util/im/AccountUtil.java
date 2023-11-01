package org.jeecg.modules.im.base.util.im;


import org.jeecg.modules.im.base.constant.ConstantXmpp;

import java.util.Random;

/**
 * 知了号生成
 */
public class AccountUtil {
    //生成系统号
    public static String getSysAccount(){
        Random random = new Random();
        return random.nextInt(ConstantXmpp.sysAccountEnd)+1+"";
    }
    //生成内部号
    public static String getBusinessAccount(){
        Random random = new Random();
        return random.nextInt(ConstantXmpp.businessAccountEnd-ConstantXmpp.businessAccountBegin)+1+"";
    }
    //生成用户号
    public static String getUserAccount(){
        Random random = new Random();
        return random.nextInt(ConstantXmpp.userAccountEnd-ConstantXmpp.userAccountBegin)+1+"";
    }
    //生成群号
    public static String getMucAccount(){
        Random random = new Random();
        return random.nextInt(ConstantXmpp.mucAccountEnd-ConstantXmpp.mucAccountBegin)+1+"";
    }

    public static void main(String[] args) {
        System.out.println(getUserAccount());
    }
}