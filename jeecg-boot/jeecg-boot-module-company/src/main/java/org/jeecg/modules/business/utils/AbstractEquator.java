package org.jeecg.modules.business.utils;


import java.util.*;

/**
 * 对比器抽象类
 *
 * @author dadiyang
 * date 2018/11/22
 */
public abstract class AbstractEquator implements Equator {
    private static final List<Class<?>> WRAPPER = Arrays.asList(Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Character.class,
            Boolean.class, String.class);
    private final List<String> includeFields;
    private final List<String> excludeFields;

    public AbstractEquator() {
        includeFields = Collections.emptyList();
        excludeFields = Collections.emptyList();
    }

    /**
     * 指定包含或排除某些字段
     *
     * @param includeFields 包含字段，若为 null 或空集，则不指定
     * @param excludeFields 排除字段，若为 null 或空集，则不指定
     */
    public AbstractEquator(List<String> includeFields, List<String> excludeFields) {
        this.includeFields = includeFields;
        this.excludeFields = excludeFields;
    }

    /**
     * 只要没有不相等的属性，两个对象就全相等
     *
     * @param first  对象1
     * @param second 对象2
     * @return 两个对象是否全相等
     */
    @Override
    public boolean isEquals(Object first, Object second) {
        List<FieldInfo> diff = getDiffFields(first, second);
        return diff == null || diff.isEmpty();
    }

    /**
     * 对比两个对象的指定属性是否相等，默认为两个对象是否 equals
     * <p>
     * 子类可以通过覆盖此方法对某些特殊属性进行比对
     *
     * @param fieldInfo 当前比对属性信息
     * @return 属性是否相等
     */
    protected boolean isFieldEquals(FieldInfo fieldInfo) {
        // 先判断排除，如果需要排除，则无论在不在包含范围，都一律不比对
        if (isExclude(fieldInfo)) {
            return true;
        }
        // 如果有指定需要包含的字段而且当前字段不在需要包含的字段中则不比对
        if (!isInclude(fieldInfo)) {
            return true;
        }
        return nullableEquals(fieldInfo.getFirstVal(), fieldInfo.getSecondVal());
    }

    /**
     * 确定是否需要比较这个字段，子类可以扩展这个方法，自定义判断方式
     */
    protected boolean isInclude(FieldInfo fieldInfo) {
        // 没有指定需要包含的字段，则全部都包含
        if (includeFields == null || includeFields.isEmpty()) {
            return true;
        }
        return includeFields.contains(fieldInfo.getFieldName());
    }

    /**
     * 如果简单数据类型的对象则直接进行比对
     *
     * @param first  对象1
     * @param second 对象2
     * @return 不同的字段信息，相等返回空集，不等则 FieldInfo 的字段名为对象的类型名称
     */
    List<FieldInfo> compareSimpleField(Object first, Object second) {
        boolean eq = Objects.equals(first, second);
        if (eq) {
            return Collections.emptyList();
        } else {
            Object obj = first == null ? second : first;
            Class<?> clazz = obj.getClass();
            // 不等的字段名称使用类的名称
            return Collections.singletonList(new FieldInfo(clazz.getSimpleName(), clazz, first, second));
        }
    }

    /**
     * 确定是否需要需要排除这个字段，子类可以扩展这个方法，自定义判断方式
     */
    protected boolean isExclude(FieldInfo fieldInfo) {
        // 如果有指定需要排除的字段，而且当前字段是需要排除字段，则直接返回 true
        return excludeFields != null && !excludeFields.isEmpty() && excludeFields.contains(fieldInfo.getFieldName());
    }

    /**
     * 判断是否为原始数据类型
     *
     * @param first  对象1
     * @param second 对象2
     * @return 是否为原始数据类型
     */
    boolean isSimpleField(Object first, Object second) {
        Object obj = first == null ? second : first;
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() || WRAPPER.contains(clazz);
    }

    private boolean nullableEquals(Object first, Object second) {
        if (first instanceof Collection
                && second instanceof Collection) {
            // 如果两个都是集合类型，尝试转换为数组再进行深度比较
            return Objects.deepEquals(((Collection) first).toArray(), ((Collection) second).toArray());
        }
        return Objects.deepEquals(first, second);
    }

}
