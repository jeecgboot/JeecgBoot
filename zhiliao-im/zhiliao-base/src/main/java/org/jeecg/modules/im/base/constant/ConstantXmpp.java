package org.jeecg.modules.im.base.constant;

/**
 * xmpp常量
 */
public interface ConstantXmpp {
    //系统账号取值范围
    int sysAccountBegin = 1;
    int sysAccountEnd = 999;
    //业务账号取值范围
    int businessAccountBegin = 1000;
    int businessAccountEnd = 9999;
    //用户账号取值范围
    int userAccountBegin = 10000;
    int userAccountEnd = 9999999;
    //群聊账号取值范围
    int mucAccountBegin = 10000000;
    int mucAccountEnd = 999999999;

    //系统账号 有昵称，用于区分不同场景 用于不同场景消息通知
    enum SystemNo{
        system(100,"系统管理员"),
        pay(104,"支付通知"),
        notice(105,"系统通知"),
        collection(106,"收藏夹"),
        ;
        Integer account;
        String name;

        SystemNo(Integer account, String name) {
            this.account = account;
            this.name = name;
        }

        public Integer getAccount() {
            return account;
        }

        public String getName() {
            return name;
        }
    }
}
