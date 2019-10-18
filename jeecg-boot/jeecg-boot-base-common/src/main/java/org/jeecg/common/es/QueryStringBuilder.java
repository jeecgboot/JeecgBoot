package org.jeecg.common.es;

/**
 * 用于创建 ElasticSearch 的 queryString
 *
 * @author sunjianlei
 */
public class QueryStringBuilder {

    StringBuilder builder;

    public QueryStringBuilder(String field, String str) {
        builder = new StringBuilder(field).append(":(").append(str);
    }

    public QueryStringBuilder and(String str) {
        builder.append(" AND ").append(str);
        return this;
    }

    public QueryStringBuilder or(String str) {
        builder.append(" OR ").append(str);
        return this;
    }

    public QueryStringBuilder not(String str) {
        builder.append(" NOT ").append(str);
        return this;
    }

    @Override
    public String toString() {
        return builder.append(")").toString();
    }

}
