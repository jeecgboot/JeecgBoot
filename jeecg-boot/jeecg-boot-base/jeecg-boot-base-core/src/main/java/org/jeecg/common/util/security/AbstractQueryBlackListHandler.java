package org.jeecg.common.util.security;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
        List<QueryTable> list = this.getQueryTableInfo(sql.toLowerCase());
        if(list==null){
            return true;
        }
        log.info("--获取sql信息--", list.toString());
        boolean flag = true;
        for (QueryTable table : list) {
            String name = table.getName();
            String fieldString = ruleMap.get(name);
            // 有没有配置这张表
            if (fieldString != null) {
                if ("*".equals(fieldString) || table.isAll()) {
                    flag = false;
                    log.warn("sql黑名单校验，表【"+name+"】禁止查询");
                    break;
                } else if (table.existSameField(fieldString)) {
                    flag = false;
                    break;
                }

            }
        }
        return flag;
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
            String[] arr = fieldString.split(",");
            for (String exp : fields) {
                for (String config : arr) {
                    if (exp.equals(config)) {
                        // 非常明确的列直接比较
                        log.warn("sql黑名单校验，表【"+name+"】中字段【"+config+"】禁止查询");
                        return true;
                    } else {
                        // 使用表达式的列 只能判读字符串包含了
                        String aliasColumn = config;
                        if (alias != null && alias.length() > 0) {
                            aliasColumn = alias + "." + config;
                        }
                        if (exp.indexOf(aliasColumn) > 0) {
                            log.warn("sql黑名单校验，表【"+name+"】中字段【"+config+"】禁止查询");
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
        return "sql黑名单校验不通过,请联系管理员!";
    }

}
