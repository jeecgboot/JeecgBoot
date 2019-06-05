package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.FluentPropertyBeanIntrospector;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

@Slf4j
public class TrimUtil {
    static {
//        Thread.dumpStack();
        PropertyUtils.addBeanIntrospector(new FluentPropertyBeanIntrospector());
    }
    public static void trimObj(Object obj){
//        Thread.dumpStack();
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(obj);
        String name, type;
        for (int i = 0; i < origDescriptors.length; i++) {
            name = origDescriptors[i].getName();
            type = origDescriptors[i].getPropertyType().toString();
            try {
                if (judgedIsUselessField(name)|| !PropertyUtils.isReadable(obj, name)) {
                    continue;
                }
                Object value = PropertyUtils.getSimpleProperty(obj, name);
                if (value != null && value instanceof String) {
                    String val = (value).toString().trim(); //trim 去除空格
//                    log.info(" obj : |{}|,  name : |{}|, val : |{}|", obj.getClass(), name, val);
                    PropertyUtils.setSimpleProperty(obj, name, val);
                }
            } catch (Exception e) {
              log.error(e.getMessage(),e);
            } finally {
            }
        }
    }

    /**
     *
     * @param name
     * @return
     */
    private static boolean judgedIsUselessField(String name) {
        return "class".equals(name) || "ids".equals(name)
                || "page".equals(name) || "rows".equals(name)
                || "sort".equals(name) || "order".equals(name);
    }


}
