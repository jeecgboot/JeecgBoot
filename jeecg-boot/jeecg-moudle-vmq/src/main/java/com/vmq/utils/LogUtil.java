package com.vmq.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LogUtil {

    public static String getResquestParam(ProceedingJoinPoint joinPoint) {
        List<String> paramList =  Arrays.stream(joinPoint.getArgs()).map(object -> {
            if (Objects.isNull(object)) {
                return null;
            }
            String paramName = object.getClass().getSimpleName();
            if (isBaseType(object)) {
                PropertyFilter profilter = (obj, name, value) -> {
                    int length = 200;
                    if(value.toString().length()>length){
                        return false;
                    }
                    if(value instanceof MultipartFile){
                        return false;
                    }
                    return true;
                };
                return JSONObject.toJSONString(object, profilter);
            }
            return paramName + ".class";
        }).collect(Collectors.toList());
        return StringUtils.join(paramList);
    }

    private static boolean isBaseType(Object object) {
        if (object instanceof String ||
                object instanceof Integer ||
                object instanceof Byte ||
                object instanceof Long ||
                object instanceof Double ||
                object instanceof Float ||
                object instanceof Character ||
                object instanceof Short ||
                object instanceof Boolean) {
            return true;
        }
        if (Objects.nonNull(object) && object.getClass().getName().startsWith("com.vmq")) {
            return true;
        }
        return false;
    }
}
