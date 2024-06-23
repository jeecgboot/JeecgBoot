package org.jeecg.config.firewall.SqlInjection;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import java.util.HashSet;
import java.util.Set;

/**
 * 查询的表的信息
 */
@Slf4j
public class SysDictTableWhite {
    //表名
    private String name;
    //表的别名
    private String alias;
    // 字段名集合
    private Set<String> fields;
    // 是否查询所有字段
    private boolean all;

    public SysDictTableWhite() {
        
    }

    public SysDictTableWhite(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.all = false;
        this.fields = new HashSet<>();
    }

    public void addField(String field) {
        this.fields.add(field);
    }

    public String getName() {
        return name;
    }

    public Set<String> getFields() {
        return new HashSet<>(fields);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    /**
     * 判断是否有相同字段
     *
     * @param fieldControlString
     * @return
     */
    public boolean isAllFieldsValid(String fieldControlString) {
        //如果白名单中没有配置字段，则返回false
        String[] controlFields = fieldControlString.split(",");
        if (oConvertUtils.isEmpty(fieldControlString)) {
            return false;
        }

        for (String queryField : fields) {
            if (oConvertUtils.isIn(queryField, controlFields)) {
                log.warn("字典表白名单校验，表【" + name + "】中字段【" + queryField + "】无权限查询");
                return false;
            }
        }

        return true;
    }
    

    @Override
    public String toString() {
        return "QueryTable{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", fields=" + fields +
                ", all=" + all +
                '}';
    }
}