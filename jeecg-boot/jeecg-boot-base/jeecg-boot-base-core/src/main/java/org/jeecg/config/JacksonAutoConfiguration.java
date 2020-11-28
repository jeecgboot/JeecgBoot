//package org.jeecg.config;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
//import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.TimeZone;
//
//import static org.jeecg.config.JacksonAutoConfiguration.SerializerFeature.*;
//
//
///**
// * @author: zyf
// * @date: 2019/5/20 14:56
// * @description:
// */
//@Slf4j
//@Configuration
//public class JacksonAutoConfiguration {
//
//    public enum SerializerFeature {
//        WriteNullListAsEmpty,
//        WriteNullStringAsEmpty,
//        WriteNullNumberAsZero,
//        WriteNullBooleanAsFalse,
//        WriteNullMapAsEmpty;
//
//        public final int mask;
//
//        SerializerFeature() {
//            mask = (1 << ordinal());
//        }
//    }
//
//    public static class FastJsonSerializerFeatureCompatibleForJackson extends BeanSerializerModifier {
//        final private JsonSerializer<Object> nullBooleanJsonSerializer;
//        final private JsonSerializer<Object> nullNumberJsonSerializer;
//        final private JsonSerializer<Object> nullListJsonSerializer;
//        final private JsonSerializer<Object> nullStringJsonSerializer;
//        final private JsonSerializer<Object> nullMapJsonSerializer;
//
//        FastJsonSerializerFeatureCompatibleForJackson(SerializerFeature... features) {
//            int config = 0;
//            for (SerializerFeature feature : features) {
//                config |= feature.mask;
//            }
//            nullBooleanJsonSerializer = (config & WriteNullBooleanAsFalse.mask) != 0 ? new NullBooleanSerializer() : null;
//            nullNumberJsonSerializer = (config & WriteNullNumberAsZero.mask) != 0 ? new NullNumberSerializer() : null;
//            nullListJsonSerializer = (config & WriteNullListAsEmpty.mask) != 0 ? new NullListJsonSerializer() : null;
//            nullStringJsonSerializer = (config & WriteNullStringAsEmpty.mask) != 0 ? new NullStringSerializer() : null;
//            nullMapJsonSerializer = (config & WriteNullMapAsEmpty.mask) != 0 ? new NullMapSerializer() : null;
//        }
//
//        @Override
//        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
//            for (BeanPropertyWriter writer : beanProperties) {
//                final JavaType javaType = writer.getType();
//                final Class<?> rawClass = javaType.getRawClass();
//                if (javaType.isArrayType() || javaType.isCollectionLikeType()) {
//                    writer.assignNullSerializer(nullListJsonSerializer);
//                } else if (Number.class.isAssignableFrom(rawClass) && (rawClass.getName().startsWith("java.lang") || rawClass.getName().startsWith("java.match"))) {
//                    writer.assignNullSerializer(nullNumberJsonSerializer);
//                } else if (BigDecimal.class.isAssignableFrom(rawClass)) {
//                    writer.assignNullSerializer(nullNumberJsonSerializer);
//                } else if (Boolean.class.equals(rawClass)) {
//                    writer.assignNullSerializer(nullBooleanJsonSerializer);
//                } else if (String.class.equals(rawClass) || Date.class.equals(rawClass)) {
//                    writer.assignNullSerializer(nullStringJsonSerializer);
//                } else if (!Date.class.equals(rawClass)) {
//                    writer.assignNullSerializer(nullMapJsonSerializer);
//                }
//            }
//            return beanProperties;
//        }
//
//        private static class NullListJsonSerializer extends JsonSerializer<Object> {
//            @Override
//            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//                jgen.writeStartArray();
//                jgen.writeEndArray();
//            }
//        }
//
//        private static class NullNumberSerializer extends JsonSerializer<Object> {
//            @Override
//            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//                jgen.writeNumber(0);
//            }
//        }
//
//        private static class NullBooleanSerializer extends JsonSerializer<Object> {
//            @Override
//            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//                jgen.writeBoolean(false);
//            }
//        }
//
//        private static class NullStringSerializer extends JsonSerializer<Object> {
//            @Override
//            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//                jgen.writeString("");
//            }
//        }
//
//        private static class NullMapSerializer extends JsonSerializer<Object> {
//            @Override
//            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
//                jgen.writeStartObject();
//                jgen.writeEndObject();
//            }
//        }
//    }
//
//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        // 排序key
//        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
//
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        //忽略在json字符串中存在，在java类中不存在字段，防止错误。
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
//        /**
//         * 序列换成json时,将所有的long变成string
//         * js中long过长精度丢失
//         */
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        //注册xss解析器
//        //simpleModule.addSerializer(String.class, new XssStringJsonSerializer());
//        //simpleModule.addDeserializer(String.class, new XssStringJsonDeserializer());
//        objectMapper.registerModule(simpleModule);
//        // 兼容fastJson 的一些空值处理
//        SerializerFeature[] features = new SerializerFeature[]{
//                WriteNullListAsEmpty,
//                WriteNullStringAsEmpty,
//                WriteNullNumberAsZero,
//                WriteNullBooleanAsFalse,
//                WriteNullMapAsEmpty
//        };
//        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new FastJsonSerializerFeatureCompatibleForJackson(features)));
//        log.info("ObjectMapper [{}]", objectMapper);
//        return objectMapper;
//    }
//
//}
