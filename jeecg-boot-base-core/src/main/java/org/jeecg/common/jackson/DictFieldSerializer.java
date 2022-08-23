package org.jeecg.common.jackson;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.aspect.DictAspect;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @author lw
 * @date 2022/8/23 19:02
 */
@Component
public class DictFieldSerializer extends JacksonAnnotationIntrospector implements Versioned {

    @Lazy
    @Autowired(required = false)
    private DictAspect dictAspect;

    @Override
    public Object findSerializer(Annotated a) {
        Dict dict = _findAnnotation(a, Dict.class);
        if (dict != null) {
            return new DictSerializer(dict);
        }
        return super.findSerializer(a);
    }


    class DictSerializer extends JsonSerializer<Object> {

        private Dict dictAnnotation;

        public DictSerializer(Dict dictAnnotation) {
            this.dictAnnotation = dictAnnotation;
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            super.serializeWithType(value, gen, serializers, typeSer);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String code = dictAnnotation.dicCode();
            String text = dictAnnotation.dicText();
            String table = dictAnnotation.dictTable();

            String dictCode = code;
            if (!StringUtils.isEmpty(table)) {
                dictCode = String.format("%s,%s,%s", table, text, code);
            }

            Map<String, List<String>> dataListMap = new HashMap<>();
            List<String> dataList = new ArrayList<>(Arrays.asList(value.toString().split(",")));
            dataListMap.put(dictCode, dataList);

            Map<String, List<DictModel>> translText = dictAspect.translateAllDict(dataListMap);
            List<DictModel> dictModels = translText.get(dictCode);
            if (CollUtil.isEmpty(dictModels)) {
                gen.writeObject(value);
                return;
            }

            String textValue = dictAspect.translDictText(dictModels, value.toString());
            String fieldName = gen.getOutputContext().getCurrentName();
            gen.writeObject(value);
            gen.writeStringField(fieldName.concat(CommonConstant.DICT_TEXT_SUFFIX), textValue);
        }
    }
}
