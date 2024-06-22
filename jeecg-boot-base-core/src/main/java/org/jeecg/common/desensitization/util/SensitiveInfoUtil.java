package org.jeecg.common.desensitization.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.desensitization.annotation.SensitiveField;
import org.jeecg.common.desensitization.enums.SensitiveEnum;
import org.jeecg.common.util.encryption.AesEncryptUtil;
import org.jeecg.common.util.oConvertUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

/**
 * 敏感信息处理工具类
 * @author taoYan
 * @date 2022/4/20 18:01
 **/
@Slf4j
public class SensitiveInfoUtil {

    /**
     * 处理嵌套对象
     * @param obj 方法返回值
     * @param entity 实体class
     * @param isEncode 是否加密（true: 加密操作 / false:解密操作）
     * @throws IllegalAccessException
     */
    public static void handleNestedObject(Object obj, Class entity, boolean isEncode) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getType().isPrimitive()){
                continue;
            }
            if(field.getType().equals(entity)){
                // 对象里面是实体
                field.setAccessible(true);
                Object nestedObject = field.get(obj);
                handlerObject(nestedObject, isEncode);
                break;
            }else{
                // 对象里面是List<实体>
                if(field.getGenericType() instanceof ParameterizedType){
                    ParameterizedType pt = (ParameterizedType)field.getGenericType();
                    if(pt.getRawType().equals(List.class)){
                        if(pt.getActualTypeArguments()[0].equals(entity)){
                            field.setAccessible(true);
                            Object nestedObject = field.get(obj);
                            handleList(nestedObject, entity, isEncode);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理Object
     * @param obj 方法返回值
     * @param isEncode 是否加密（true: 加密操作 / false:解密操作）
     * @return
     * @throws IllegalAccessException
     */
    public static Object handlerObject(Object obj, boolean isEncode) throws IllegalAccessException {
        if (oConvertUtils.isEmpty(obj)) {
            return obj;
        }
        long startTime=System.currentTimeMillis();
        log.debug(" obj --> "+ obj.toString());
        
        // 判断是不是一个对象
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean isSensitiveField = field.isAnnotationPresent(SensitiveField.class);
            if(isSensitiveField){
                // 必须有SensitiveField注解 才作处理
                if(field.getType().isAssignableFrom(String.class)){
                    //必须是字符串类型 才作处理
                    field.setAccessible(true);
                    String realValue = (String) field.get(obj);
                    if(realValue==null || "".equals(realValue)){
                        continue;
                    }
                    SensitiveField sf = field.getAnnotation(SensitiveField.class);
                    if(isEncode==true){
                        //加密
                        String value = SensitiveInfoUtil.getEncodeData(realValue,  sf.type());
                        field.set(obj, value);
                    }else{
                        //解密只处理 encode类型的
                        if(sf.type().equals(SensitiveEnum.ENCODE)){
                            String value = SensitiveInfoUtil.getDecodeData(realValue);
                            field.set(obj, value);
                        }
                    }
                }
            }
        }
        //long endTime=System.currentTimeMillis();
        //log.info((isEncode ? "加密操作，" : "解密操作，") + "当前程序耗时：" + (endTime - startTime) + "ms");
        return obj;
    }

    /**
     * 处理 List<实体>
     * @param obj
     * @param entity
     * @param isEncode（true: 加密操作 / false:解密操作）
     */
    public static void handleList(Object obj, Class entity, boolean isEncode){
        List list = (List)obj;
        if(list.size()>0){
            Object first = list.get(0);
            if(first.getClass().equals(entity)){
                for(int i=0; i<list.size(); i++){
                    Object temp = list.get(i);
                    try {
                        handlerObject(temp, isEncode);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 处理数据 获取解密后的数据
     * @param data
     * @return
     */
    public static String getDecodeData(String data){
        String result = null;
        try {
            result = AesEncryptUtil.desEncrypt(data);
        } catch (Exception exception) {
            log.debug("数据解密错误，原数据:"+data);
        }
        //解决debug模式下，加解密失效导致中文被解密变成空的问题
        if(oConvertUtils.isEmpty(result) && oConvertUtils.isNotEmpty(data)){
            result = data;
        }
        return result;
    }

    /**
     * 处理数据 获取加密后的数据 或是格式化后的数据
     * @param data 字符串
     * @param sensitiveEnum 类型
     * @return 处理后的字符串
     */
    public static String getEncodeData(String data, SensitiveEnum sensitiveEnum){
        String result;
        switch (sensitiveEnum){
            case ENCODE:
                try {
                    result = AesEncryptUtil.encrypt(data);
                } catch (Exception exception) {
                    log.error("数据加密错误", exception.getMessage());
                    result = data;
                }
                break;
            case CHINESE_NAME:
                result = chineseName(data);
                break;
            case ID_CARD:
                result = idCardNum(data);
                break;
            case FIXED_PHONE:
                result = fixedPhone(data);
                break;
            case MOBILE_PHONE:
                result = mobilePhone(data);
                break;
            case ADDRESS:
                result = address(data, 3);
                break;
            case EMAIL:
                result = email(data);
                break;
            case BANK_CARD:
                result = bankCard(data);
                break;
            case CNAPS_CODE:
                result = cnapsCode(data);
                break;
            default:
                result = data;
        }
        return result;
    }


    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号
     * @param fullName 全名
     * @return <例子：李**>
     */
    public static String chineseName(String fullName) {
        if (oConvertUtils.isEmpty(fullName)) {
            return "";
        }
        return formatRight(fullName, 1);
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号
     * @param familyName 姓
     * @param firstName 名
     * @return <例子：李**>
     */
    public static String chineseName(String familyName, String firstName) {
        if (oConvertUtils.isEmpty(familyName) || oConvertUtils.isEmpty(firstName)) {
            return "";
        }
        return chineseName(familyName + firstName);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。
     * @param id 身份证号
     * @return <例子：*************5762>
     */
    public static String idCardNum(String id) {
        if (oConvertUtils.isEmpty(id)) {
            return "";
        }
        return formatLeft(id, 4);

    }

    /**
     * [固定电话] 后四位，其他隐藏
     * @param num 固定电话
     * @return <例子：****1234>
     */
    public static String fixedPhone(String num) {
        if (oConvertUtils.isEmpty(num)) {
            return "";
        }
        return formatLeft(num, 4);
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏
     * @param num 手机号码
     * @return <例子:138******1234>
     */
    public static String mobilePhone(String num) {
        if (oConvertUtils.isEmpty(num)) {
            return "";
        }
        int len = num.length();
        if(len<11){
            return num;
        }
        return formatBetween(num, 3, 4);
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护
     * @param address 地址
     * @param sensitiveSize 敏感信息长度
     * @return <例子：北京市海淀区****>
     */
    public static String address(String address, int sensitiveSize) {
        if (oConvertUtils.isEmpty(address)) {
            return "";
        }
        int len = address.length();
        if(len<sensitiveSize){
            return address;
        }
        return formatRight(address, sensitiveSize);
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示
     * @param email 电子邮箱
     * @return <例子:g**@163.com>
     */
    public static String email(String email) {
        if (oConvertUtils.isEmpty(email)) {
            return "";
        }
        int index = email.indexOf("@");
        if (index <= 1){
            return email;
        }
        String begin = email.substring(0, 1);
        String end = email.substring(index);
        String stars = "**";
        return begin + stars + end;
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号
     * @param cardNum 银行卡号
     * @return <例子:6222600**********1234>
     */
    public static String bankCard(String cardNum) {
        if (oConvertUtils.isEmpty(cardNum)) {
            return "";
        }
        return formatBetween(cardNum, 6, 4);
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号
     * @param code 公司开户银行联号
     * @return <例子:12********>
     */
    public static String cnapsCode(String code) {
        if (oConvertUtils.isEmpty(code)) {
            return "";
        }
        return formatRight(code, 2);
    }


    /**
     * 将右边的格式化成*
     * @param str 字符串
     * @param reservedLength 保留长度
     * @return 格式化后的字符串
     */
    public static String formatRight(String str, int reservedLength){
        String name = str.substring(0, reservedLength);
        String stars = String.join("", Collections.nCopies(str.length()-reservedLength, "*"));
        return name + stars;
    }

    /**
     * 将左边的格式化成*
     * @param str 字符串
     * @param reservedLength 保留长度
     * @return 格式化后的字符串
     */
    public static String formatLeft(String str, int reservedLength){
        int len = str.length();
        String show = str.substring(len-reservedLength);
        String stars = String.join("", Collections.nCopies(len-reservedLength, "*"));
        return stars + show;
    }

    /**
     * 将中间的格式化成*
     * @param str 字符串
     * @param beginLen 开始保留长度
     * @param endLen 结尾保留长度
     * @return 格式化后的字符串
     */
    public static String formatBetween(String str, int beginLen, int endLen){
        int len = str.length();
        String begin = str.substring(0, beginLen);
        String end = str.substring(len-endLen);
        String stars = String.join("", Collections.nCopies(len-beginLen-endLen, "*"));
        return begin + stars + end;
    }

}
