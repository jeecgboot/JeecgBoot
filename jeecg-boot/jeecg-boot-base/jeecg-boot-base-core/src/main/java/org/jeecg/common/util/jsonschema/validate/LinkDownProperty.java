package org.jeecg.common.util.jsonschema.validate;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.jsonschema.BaseColumn;
import org.jeecg.common.util.jsonschema.CommonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 级联下拉
 */
public class LinkDownProperty extends CommonProperty {

    /**
     * 配置信息
     */
    String dictTable;

    /**
     * 级联下拉组件 的其他级联列
     */
    List<BaseColumn> otherColumns;

    public String getDictTable(){
        return this.dictTable;
    }

    public void setDictTable(String dictTable){
        this.dictTable = dictTable;
    }

    public List<BaseColumn> getOtherColumns(){
        return this.otherColumns;
    }

    public void setOtherColumns(List<BaseColumn> otherColumns){
        this.otherColumns = otherColumns;
    }

    public LinkDownProperty() {}

    /**
     *  构造器
     */
    public LinkDownProperty(String key,String title,String dictTable) {
        this.type = "string";
        this.view = "link_down";
        this.key = key;
        this.title = title;
        this.dictTable= dictTable;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", getKey());
        JSONObject prop = getCommonJson();
        JSONObject temp = JSONObject.parseObject(this.dictTable);
        prop.put("config", temp);
        prop.put("others", otherColumns);
        map.put("prop", prop);
        return map;
    }
}
