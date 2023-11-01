package org.jeecg.modules.im.constant;

/**
 * app端用户角色编码
 */
public enum UserRoleEnum {
    aczl(1,"案场助理"),
    acxs(2,"案场销售"),
    sczy(3,"市场专员"),
    fxry(4,"分销人员");

    int code;
    String title;
    UserRoleEnum(int code,String title){
        this.code = code;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
