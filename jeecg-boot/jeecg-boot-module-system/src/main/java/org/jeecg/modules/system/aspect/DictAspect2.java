package org.jeecg.modules.system.aspect;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.DictField;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.aspect.model.FieldInfo;
import org.jeecg.modules.system.aspect.model.TranslateInfo;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局字典翻译
 *
 * @author zxiaozhou
 * @date 2020-07-22 11:13
 * @since JDK1.8
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DictAspect2 {

    private final ISysDictService dictService;


    /**
     * 反射缓存
     */
    private final static Map<String, TranslateInfo> REFLECT_CACHE = new ConcurrentHashMap<>(128);

    /**
     * 标记是否需要翻译
     */
    private final static Map<String, Boolean> ENABLE_TRANSLATE = new ConcurrentHashMap<>(128);


    /**
     * 切点(使用在方法上添加注解提高性能,避免所有都切入)
     *
     * @author zxiaozhou
     * @date 2020-07-22 11:18
     */
    @Pointcut("@annotation(org.jeecg.common.aspect.annotation.DictMethod)")
    public void invoke() {
    }


    /**
     * 环绕
     *
     * @param pjp ${@link ProceedingJoinPoint}
     * @return Object ${@link Object}
     * @author zxiaozhou
     * @date 2020-07-22 11:18
     */
    @Around("invoke()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("------------DictAspect------------>doAround:{}", "进行字典翻译");
        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        Signature signature = pjp.getSignature();
        long endTime = System.currentTimeMillis();
        log.debug("------------DictAspect-------获取json数据耗时----->doAround:{}", (endTime - startTime) + "ms");
        Object finalResult = this.parseDict(result, signature);
        endTime = System.currentTimeMillis();
        log.debug("------------DictAspect-------字典翻译总耗时----->doAround:{}", (endTime - startTime) + "ms");
        return finalResult;
    }


    /**
     * 翻译字典
     *
     * @param result    ${@link Object}
     * @param signature ${@link Signature}
     * @author zxiaozhou
     * @date 2020-07-22 11:44
     */
    private Object parseDict(Object result, Signature signature) {
        // 缓存key类路径+方法名
        String cacheKey = signature.getDeclaringTypeName() + signature.getName();
        // 如果标记信息为null则去解析反射信息
        if (Objects.isNull(ENABLE_TRANSLATE.get(cacheKey))) {
            long startTime = System.currentTimeMillis();
            log.debug("------------DictAspect------解析字典翻译信息------>parseDict");
            getCache(result, cacheKey);
            long endTime = System.currentTimeMillis();
            log.debug("------------DictAspect-------解析字典翻译信息耗时----->parseDict:{}", (endTime - startTime) + "ms");
        }
        return startTranslation(result, cacheKey);
    }


    /**
     * 获取字典翻译反射信息
     *
     * @param result   ${@link Object} 原始数据
     * @param cacheKey ${@link String} 缓存key
     * @author zxiaozhou
     * @date 2020-07-22 11:48
     */
    private void getCache(Object result, String cacheKey) {
        if (result instanceof Result) {
            Result resultInfo = (Result) result;
            Object data = resultInfo.getResult();
            TranslateInfo translateInfo = new TranslateInfo();
            translateInfo.setType(Constant.TYPE_CLASS);
            if (Objects.isNull(data)) {
                return;
            }
            Object finalOneData = data;
            if (data instanceof List) {
                translateInfo.setType(Constant.TYPE_LIST);
                List list = (List) data;
                if (list.isEmpty()) {
                    return;
                }
                finalOneData = list.get(0);
            } else if (data instanceof Set) {
                Set sets = (Set) data;
                if (sets.isEmpty()) {
                    return;
                }
                translateInfo.setType(Constant.TYPE_SET);
                for (Object obj : sets) {
                    finalOneData = obj;
                    break;
                }
            } else if (data instanceof IPage) {
                translateInfo.setType(Constant.TYPE_PAGE);
                List list = ((IPage) data).getRecords();
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                finalOneData = list.get(0);
            }
            if (Objects.nonNull(finalOneData)) {
                Class<?> aClass = finalOneData.getClass();
                if (Objects.isNull(aClass)) {
                    return;
                }
                translateInfo.setAClass(aClass);
                Field[] declaredFields = aClass.getDeclaredFields();
                List<FieldInfo> fieldInfos = new ArrayList<>();
                for (Field field : declaredFields) {
                    FieldInfo fieldInfo = new FieldInfo();
                    DictField dictField = AnnotationUtils.findAnnotation(field, DictField.class);
                    if (Objects.nonNull(dictField)) {
                        fieldInfo.setDictField(dictField);
                        fieldInfo.setField(field);
                        fieldInfo.setFiledName(field.getName());
                        Class<?> type = field.getType();
                        fieldInfo.setFileTypeClass(type);
                        fieldInfo.setFileType(getBasicType(type.getName()));
                        fieldInfos.add(fieldInfo);
                    }
                }
                if (!fieldInfos.isEmpty()) {
                    translateInfo.setFieldInfos(fieldInfos);
                    REFLECT_CACHE.put(cacheKey, translateInfo);
                    ENABLE_TRANSLATE.put(cacheKey, true);
                } else {
                    ENABLE_TRANSLATE.put(cacheKey, false);
                }
                return;
            }
            return;
        }
        ENABLE_TRANSLATE.put(cacheKey, false);
    }


    /**
     * 开始翻译
     *
     * @param result   ${@link Object} 原始数据
     * @param cacheKey ${@link String} 缓存key
     * @return Object ${@link Object} 翻译后数据
     * @author zxiaozhou
     * @date 2020-07-22 11:49
     */
    private Object startTranslation(Object result, String cacheKey) {
        Boolean translate = ENABLE_TRANSLATE.get(cacheKey);
        TranslateInfo translateInfo = REFLECT_CACHE.get(cacheKey);
        // 开始翻译字典
        if (Objects.nonNull(translate) && translate && Objects.nonNull(translateInfo)) {
            Result resultInfo = (Result) result;
            Object data = resultInfo.getResult();
            if (Objects.nonNull(data)) {
                int type = translateInfo.getType();
                // 单个对象
                if (type == Constant.TYPE_CLASS) {
                    data = setData(data, translateInfo);
                    // list
                } else if (type == Constant.TYPE_LIST) {
                    List<?> objectList = (List<?>) data;
                    if (!objectList.isEmpty()) {
                        List<Object> objectNewList = new ArrayList<>(4);
                        for (Object object : objectList) {
                            Object translateData = setData(object, translateInfo);
                            objectNewList.add(translateData);
                        }
                        if (!objectNewList.isEmpty()) {
                            data = objectNewList;
                        }
                    }
                    // set
                } else if (type == Constant.TYPE_SET) {
                    Set<?> objectSet = (Set<?>) data;
                    if (!objectSet.isEmpty()) {
                        Set<Object> objectNewSet = new HashSet<>(4);
                        for (Object object : objectSet) {
                            Object translateData = setData(object, translateInfo);
                            objectNewSet.add(translateData);
                        }
                        if (!objectNewSet.isEmpty()) {
                            data = objectNewSet;
                        }
                    }
                    // 分页
                } else if (type == Constant.TYPE_PAGE) {
                    Page objectPage = (Page) data;
                    List<?> records = objectPage.getRecords();
                    if (!CollectionUtils.isEmpty(records)) {
                        List recordsNew = new ArrayList(4);
                        for (Object object : records) {
                            Object translateData = setData(object, translateInfo);
                            recordsNew.add(translateData);
                        }
                        if (!recordsNew.isEmpty()) {
                            objectPage.setRecords(recordsNew);
                        }
                    }
                    data = objectPage;
                }
            }
            resultInfo.setResult(data);
            return resultInfo;
        }
        return result;
    }


    /**
     * 数据值处理
     *
     * @param oneData       ${@link Object} 单条具体待翻译数据
     * @param translateInfo ${@link TranslateInfo} 翻译信息
     * @return Object ${@link Object} 翻译后的数据
     * @author zxiaozhou
     * @date 2020-07-22 13:00
     */
    private Object setData(Object oneData, TranslateInfo translateInfo) {
        if (Objects.nonNull(oneData)) {
            List<FieldInfo> fieldInfos = translateInfo.getFieldInfos();
            ObjectMapper mapper = new ObjectMapper();
            String json;
            try {
                // 解决@JsonFormat注解解析不了的问题详见(先调JsonFormat处理数据,否则最后转为json后类型注解丢失导致无法解析)
                json = mapper.writeValueAsString(oneData);
            } catch (JsonProcessingException e) {
                log.debug("------------DictAspect------JSON解析失败------>setData:{}", e.getMessage());
                // 解析异常直接返回原始数据
                return oneData;
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            boolean isTranslate = false;
            for (FieldInfo fieldInfo : fieldInfos) {
                DictField dictField = fieldInfo.getDictField();
                String code = dictField.dicCode();
                String text = dictField.dicText();
                String table = dictField.dictTable();
                String key = fieldInfo.getFiledName();
                Object object = jsonObject.get(key);
                //翻译字典值对应的txt
                if (Objects.nonNull(object)) {
                    String textValue = translateDictValue(code, text, table, key);
                    log.debug(" 字典Val : " + textValue);
                    log.debug(" __翻译字典字段__ " + key + CommonConstant.DICT_TEXT_SUFFIX + "： " + textValue);
                    jsonObject.put(key + CommonConstant.DICT_TEXT_SUFFIX, textValue);
                    isTranslate = true;
                }
            }
            if (isTranslate) {
                oneData = jsonObject;
            }
        }
        return oneData;
    }


    /**
     * 获取基本类型
     *
     * @param fileTypeName ${@link String} 属性类型类
     * @return int ${@link Integer} 基本类型标记
     * @author zxiaozhou
     * @date 2020-07-22 17:09
     */
    public int getBasicType(String fileTypeName) {
        int type;
        switch (fileTypeName) {
            case "int":
            case "com.lang.Integer":
                type = Constant.FILE_TYPE_INT;
                break;
            case "boolean":
            case "com.lang.Boolean":
                type = Constant.FILE_TYPE_BOOLEAN;
                break;
            default:
                type = 9;
        }
        return type;
    }

    /**
     * 翻译字典文本
     *
     * @param code
     * @param text
     * @param table
     * @param key
     * @return
     */
    private String translateDictValue(String code, String text, String table, String key) {
        if (oConvertUtils.isEmpty(key)) {
            return null;
        }
        StringBuffer textValue = new StringBuffer();
        String[] keys = key.split(",");
        for (String k : keys) {
            String tmpValue = null;
            log.debug(" 字典 key : " + k);
            if (k.trim().length() == 0) {
                continue; //跳过循环
            }
            if (!org.springframework.util.StringUtils.isEmpty(table)) {
                log.debug("--DictAspect------dicTable=" + table + " ,dicText= " + text + " ,dicCode=" + code);
                tmpValue = dictService.queryTableDictTextByKey(table, text, code, k.trim());
            } else {
                tmpValue = dictService.queryDictTextByKey(code, k.trim());
            }

            if (tmpValue != null) {
                if (!"".equals(textValue.toString())) {
                    textValue.append(",");
                }
                textValue.append(tmpValue);
            }

        }
        return textValue.toString();
    }
}
