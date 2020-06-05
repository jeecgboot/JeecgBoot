package org.jeecg.modules.business.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 基于 getter 方法比对两个对象
 * <p>
 * 所有无参的 get 和 is 方法都认为是对象的属性
 *
 * @author dadiyang
 * date 2018/11/22
 */
public class GetterBaseEquator extends AbstractEquator {
    private static final String GET = "get";
    private static final String IS = "is";
    private static final String GET_IS = "get|is";
    private static final String GET_CLASS = "getClass";

    public GetterBaseEquator() {
    }

    /**
     * 指定包含或排除某些字段
     *
     * @param includeFields 包含字段，若为 null 或空集，则不指定
     * @param excludeFields 排除字段，若为 null 或空集，则不指定
     */
    public GetterBaseEquator(List<String> includeFields, List<String> excludeFields) {
        super(includeFields, excludeFields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldInfo> getDiffFields(Object first, Object second) {
        if (first == null && second == null) {
            return Collections.emptyList();
        }
        // 先尝试判断是否为普通数据类型
        if (isSimpleField(first, second)) {
            return compareSimpleField(first, second);
        }
        List<FieldInfo> diffField = new LinkedList<>();
        Object obj = first == null ? second : first;
        Map<String, Method> getters = getAllGetters(obj.getClass());
        for (Map.Entry<String, Method> entry : getters.entrySet()) {
            String fieldName = entry.getKey();
            Method method = entry.getValue();
            try {
                boolean eq;
                Object firstVal = first == null ? null : method.invoke(first);
                Object secondVal = second == null ? null : method.invoke(second);
                FieldInfo fieldInfo = new FieldInfo(fieldName, method.getReturnType(), firstVal, secondVal);
                eq = isFieldEquals(fieldInfo);
                if (!eq) {
                    diffField.add(fieldInfo);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("获取属性进行比对发生异常: " + fieldName, e);
            }
        }
        return diffField;
    }

    /**
     * 获取类中的所有 getter 方法
     *
     * @return key -> fieldName, value -> getter
     */
    private Map<String, Method> getAllGetters(Class<?> clazz) {
        Map<String, Method> getters = new LinkedHashMap<>(8);
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            // getter 方法没有参数
            if (m.getParameterTypes().length > 0) {
                continue;
            }
            if (m.getReturnType() == Boolean.class || m.getReturnType() == boolean.class) {
                // 如果返回值是 boolean 则兼容 isXxx 的写法
                if (m.getName().startsWith(IS)) {
                    String fieldName = uncapitalize(m.getName().substring(2));
                    getters.put(fieldName, m);
                    continue;
                }
            }
            // 以get开头但排除getClass()方法
            if (m.getName().startsWith(GET) && !GET_CLASS.equals(m.getName())) {
                String fieldName = uncapitalize(m.getName().replaceFirst(GET_IS, ""));
                getters.put(fieldName, m);
            }
        }
        return getters;
    }

    /**
     * 来自commons-lang3包的StringUtils
     * <p>
     * 用于使首字母小写
     */
    private String uncapitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toLowerCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            return str;
        }
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }
}
