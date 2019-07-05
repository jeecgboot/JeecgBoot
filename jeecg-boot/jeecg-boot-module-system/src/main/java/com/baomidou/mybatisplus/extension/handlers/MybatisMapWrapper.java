//package com.baomidou.mybatisplus.extension.handlers;
//
////import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.wrapper.MapWrapper;
//
//import java.util.Map;
//
///**
// * 返回Map结果集，下划线转驼峰(去掉)
// */
//public class MybatisMapWrapper extends MapWrapper {
//
//    public MybatisMapWrapper(MetaObject metaObject, Map<String, Object> map) {
//        super(metaObject, map);
//    }
//
//    @Override
//    public String findProperty(String name, boolean useCamelCaseMapping) {
////        if (useCamelCaseMapping && !StringUtils.isCamel(name)) {
////            return StringUtils.underlineToCamel(name);
////        }
//        return name;
//    }
//}