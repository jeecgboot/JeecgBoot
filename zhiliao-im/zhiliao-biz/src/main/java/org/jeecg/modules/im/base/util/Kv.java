package org.jeecg.modules.im.base.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Kv (Key Value)
 * <p>
 * Example：
 * Kv para = Kv.by("id", 123);
 * User user = user.findFirst(getSqlPara("find", para));
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class Kv extends HashMap {

    public Kv() {
    }

    public static Kv by(Object key, Object value) {
        return new Kv().set(key, value);
    }

    public static Kv create() {
        return new Kv();
    }

    public Kv set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public Kv setIfNotBlank(Object key, String value) {
        if (StringUtils.isNotBlank(value)) {
            set(key, value);
        }
        return this;
    }

    public Kv setIfNotNull(Object key, Object value) {
        if (value != null) {
            set(key, value);
        }
        return this;
    }

    public Kv set(Map map) {
        super.putAll(map);
        return this;
    }

    public Kv set(Kv kv) {
        super.putAll(kv);
        return this;
    }

    public Kv delete(Object key) {
        super.remove(key);
        return this;
    }

    public <T> T getAs(Object key) {
        return (T) get(key);
    }

    public String getStr(Object key) {
        Object s = get(key);
        return s != null ? s.toString() : null;
    }

    public Integer getInt(Object key) {
        Number n = (Number) get(key);
        return n != null ? n.intValue() : null;
    }

    public Long getLong(Object key) {
        Number n = (Number) get(key);
        return n != null ? n.longValue() : null;
    }

    public Number getNumber(Object key) {
        return (Number) get(key);
    }

    public Boolean getBoolean(Object key) {
        return (Boolean) get(key);
    }

    /**
     * key 存在，并且 value 不为 null
     */
    public boolean notNull(Object key) {
        return get(key) != null;
    }

    /**
     * key 不存在，或者 key 存在但 value 为null
     */
    public boolean isNull(Object key) {
        return get(key) == null;
    }

    /**
     * key 存在，并且 value 为 true，则返回 true
     */
    public boolean isTrue(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value == true));
    }

    /**
     * key 存在，并且 value 为 false，则返回 true
     */
    public boolean isFalse(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value == false));
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    public boolean equals(Object kv) {
        return kv instanceof Kv && super.equals(kv);
    }
}