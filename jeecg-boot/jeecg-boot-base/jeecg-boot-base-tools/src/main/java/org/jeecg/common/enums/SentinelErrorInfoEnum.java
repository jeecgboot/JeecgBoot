package org.jeecg.common.enums;

/**
 * @Description: 异常错误信息定义
 * @author: zyf
 * @date: 2022/4/14 10:05
 */
public enum SentinelErrorInfoEnum {

    /**
     * 流控异常
     */
    FlowException("访问频繁，请稍候再试"),

    /**
     * 热点参数异常
     */
    ParamFlowException("热点参数限流"),

    /**
     * 系统规则限流或降级
     */
    SystemBlockException("系统规则限流或降级"),

    /**
     * 授权规则不通过
     */
    AuthorityException("授权规则不通过"),

    /**
     * 授权规则不通过
     */
    UnknownError("未知异常"),

    /**
     * 服务降级
     */
    DegradeException("服务降级");


    /**
     * 错误信息
     */
    String error;

    /**
     * 错误代码
     */
    Integer code;


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 构造器
     *
     * @param error 错误信息
     * @param code  错误代码
     */
    SentinelErrorInfoEnum(String error, Integer code) {
        this.error = error;
        this.code = code;
    }

    /**
     * 构造器
     *
     * @param error 错误信息
     */
    SentinelErrorInfoEnum(String error) {
        this.error = error;
        this.code = 500;
    }

    /**
     * 根据异常名称匹配
     *
     * @param throwable 异常
     * @return String 错误信息
     */
    public static SentinelErrorInfoEnum getErrorByException(Throwable throwable) {
        if(throwable==null){
            return null;
        }

        String exceptionClass=throwable.getClass().getSimpleName();
        for (SentinelErrorInfoEnum e : SentinelErrorInfoEnum.values()) {
            if (exceptionClass.equals(e.name())) {
                return e;
            }
        }
        return null;
    }
}
