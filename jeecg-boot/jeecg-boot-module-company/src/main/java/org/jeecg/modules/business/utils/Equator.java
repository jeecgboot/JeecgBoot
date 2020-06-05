package org.jeecg.modules.business.utils;

import java.util.List;

/**
 * 对象比对器，用于对比两个对象的所有属性是否完全相等
 *
 * @author dadiyang
 * date 2018/11/22
 */
public interface Equator {
    /**
     * 两个对象是否全相等
     *
     * @param first  对象1
     * @param second 对象2
     * @return 两个对象是否全相等
     */
    boolean isEquals(Object first, Object second);

    /**
     * 获取不相等的属性
     *
     * @param first  对象1
     * @param second 对象2
     * @return 不相等的属性，键为属性名，值为属性类型
     */
    List<FieldInfo> getDiffFields(Object first, Object second);
}
