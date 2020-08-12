package org.jeecg.modules.system.aspect.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 翻译信息
 *
 * @author zhou
 * @date 2019-11-26 12:37
 * @since JDK1.8
 */
@Data
public class TranslateInfo implements Serializable {
    private static final long serialVersionUID = -5058630416611762799L;
    /**
     * 实体属性信息
     */
    List<FieldInfo> fieldInfos;

    /**
     * 参数类型:1-实体,2-List,3-Set,3-JSONArray,4-PageDto,5-Page
     */
    private int type;

    /**
     * 待翻译数据具体实体类型
     */
    private Class<?> aClass;

}
