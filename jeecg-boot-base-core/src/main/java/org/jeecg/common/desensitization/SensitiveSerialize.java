package org.jeecg.common.desensitization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.desensitization.annotation.Sensitive;
import org.jeecg.common.desensitization.enums.SensitiveEnum;
import org.jeecg.common.desensitization.util.SensitiveInfoUtil;
import org.jeecg.common.util.encryption.AesEncryptUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * @author eightmonth@qq.com
 * @date 2024/6/19 10:43
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveEnum type;

    @Override
    public void serialize(String data, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (type){
            case ENCODE:
                try {
                    jsonGenerator.writeString(AesEncryptUtil.encrypt(data));
                } catch (Exception exception) {
                    log.error("数据加密错误", exception.getMessage());
                    jsonGenerator.writeString(data);
                }
                break;
            case CHINESE_NAME:
                jsonGenerator.writeString(SensitiveInfoUtil.chineseName(data));
                break;
            case ID_CARD:
                jsonGenerator.writeString(SensitiveInfoUtil.idCardNum(data));
                break;
            case FIXED_PHONE:
                jsonGenerator.writeString(SensitiveInfoUtil.fixedPhone(data));
                break;
            case MOBILE_PHONE:
                jsonGenerator.writeString(SensitiveInfoUtil.mobilePhone(data));
                break;
            case ADDRESS:
                jsonGenerator.writeString(SensitiveInfoUtil.address(data, 3));
                break;
            case EMAIL:
                jsonGenerator.writeString(SensitiveInfoUtil.email(data));
                break;
            case BANK_CARD:
                jsonGenerator.writeString(SensitiveInfoUtil.bankCard(data));
                break;
            case CNAPS_CODE:
                jsonGenerator.writeString(SensitiveInfoUtil.cnapsCode(data));
                break;
            default:
                jsonGenerator.writeString(data);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (sensitive != null) {
                    return new SensitiveSerialize(sensitive.type());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
