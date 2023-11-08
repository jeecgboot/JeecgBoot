package org.jeecg.common.util.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgSqlInjectionException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询表/字段 黑名单处理
 * @Author taoYan
 * @Date 2022/3/17 11:21
 **/
@Slf4j
public abstract class AbstractQueryBlackListHandler {

    /**
     * key-表名
     * value-字段名，多个逗号隔开
     * 两种配置方式-- 全部配置成小写
     * ruleMap.put("sys_user", "*")sys_user所有的字段不支持查询
     * ruleMap.put("sys_user", "username,password")sys_user中的username和password不支持查询
     */
    public static Map<String, String> ruleMap = new HashMap<>();

    /**
     * 以下字符不能出现在表名中或是字段名中
     */
    public static final Pattern ILLEGAL_NAME_REG = Pattern.compile("[-]{2,}");

    static {
        ruleMap.put("sys_user", "password,salt");
    }


    /**
     * 根据 sql语句 获取表和字段信息，需要到具体的实现类重写此方法-
     * 不同的场景 处理可能不太一样 需要自定义，但是返回值确定
     * @param sql
     * @return
     */
    protected abstract List<QueryTable> getQueryTableInfo(String sql);


    /**
     * 校验sql语句 成功返回true
     * @param sql
     * @return
     */
    public boolean isPass(String sql) {
        List<QueryTable> list = null;
        //【jeecg-boot/issues/4040】在线报表不支持子查询，解析报错 #4040
        try {
            list = this.getQueryTableInfo(sql.toLowerCase());
        } catch (Exception e) {
            log.warn("校验sql语句，解析报错：{}",e.getMessage());
        }
        
        if(list==null){
            return true;
        }
        log.info("  获取sql信息 ：{} ", list.toString());
        boolean flag = checkTableAndFieldsName(list);
        if(flag == false){
            return false;
        }
        for (QueryTable table : list) {
            String name = table.getName();
            String fieldRule = ruleMap.get(name);
            // 有没有配置这张表
            if (fieldRule != null) {
                if ("*".equals(fieldRule) || table.isAll()) {
                    flag = false;
                    log.warn("sql黑名单校验，表【"+name+"】禁止查询");
                    break;
                } else if (table.existSameField(fieldRule)) {
                    flag = false;
                    break;
                }

            }
        }

        // 返回黑名单校验结果（不合法直接抛出异常）
        if(!flag){
            log.error(this.getError());
            throw new JeecgSqlInjectionException(this.getError());
        }
        return flag;
    }

    /**
     * 校验表名和字段名是否有效，或是是否会带些特殊的字符串进行sql注入
     * issues/4983 SQL Injection in 3.5.1 #4983
     * @return
     */
    private boolean checkTableAndFieldsName(List<QueryTable> list){
        boolean flag = true;
        for(QueryTable queryTable: list){
            String tableName = queryTable.getName();
            if(hasSpecialString(tableName)){
                flag = false;
                log.warn("sql黑名单校验，表名【"+tableName+"】包含特殊字符");
                break;
            }
            Set<String> fields = queryTable.getFields();
            for(String name: fields){
                if(hasSpecialString(name)){
                    flag = false;
                    log.warn("sql黑名单校验，字段名【"+name+"】包含特殊字符");
                    break;
                } 
            }
        }
        return flag;
    }

    /**
     * 是否包含特殊的字符串
     * @param name
     * @return
     */
    private boolean hasSpecialString(String name){
        Matcher m = ILLEGAL_NAME_REG.matcher(name);
        if (m.find()) {
            return true;
        }
        return false;
    }
    

    /**
     * 查询的表的信息
     */
    protected class QueryTable {
        //表名
        private String name;
        //表的别名
        private String alias;
        // 字段名集合
        private Set<String> fields;
        // 是否查询所有字段
        private boolean all;

        public QueryTable() {
        }

        public QueryTable(String name, String alias) {
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
         * @param fieldString
         * @return
         */
        public boolean existSameField(String fieldString) {
            String[] controlFields = fieldString.split(",");
            for (String sqlField : fields) {
                for (String controlField : controlFields) {
                    if (sqlField.equals(controlField)) {
                        // 非常明确的列直接比较
                        log.warn("sql黑名单校验，表【"+name+"】中字段【"+controlField+"】禁止查询");
                        return true;
                    } else {
                        // 使用表达式的列 只能判读字符串包含了
                        String aliasColumn = controlField;
                        if (StringUtils.isNotBlank(alias)) {
                            aliasColumn = alias + "." + controlField;
                        }
                        if (sqlField.indexOf(aliasColumn) != -1) {
                            log.warn("sql黑名单校验，表【"+name+"】中字段【"+controlField+"】禁止查询");
                            return true;
                        }
                    }
                }
            }
            return false;
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

    public String getError(){
        // TODO
        return "系统设置了安全规则，敏感表和敏感字段禁止查询，联系管理员授权!";
    }

}
