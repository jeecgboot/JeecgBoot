//package org.jeecg.common.es;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.jeecg.common.util.RestUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
///**
// * 关于 ElasticSearch 的一些方法（创建索引、添加数据、查询等）
// *
// * @author sunjianlei
// */
//@Slf4j
//@Component
//public class JeecgElasticsearchTemplate {
//
//    /**
//     * 用户配置是否通过，未通过就不走任何方法
//     */
//    private static boolean configIsPassed = true;
//    /**
//     * 是否已检测过配置
//     */
//    private static boolean configIsChecked = false;
//
//    private String baseUrl;
//
//    private final String FORMAT_JSON = "format=json";
//
//    public JeecgElasticsearchTemplate(@Value("${jeecg.elasticsearch.cluster-nodes}") String baseUrl) {
//        log.debug("JeecgElasticsearchTemplate BaseURL：" + baseUrl);
//
//        //  未检测过配置，进行检测操作
//        if (!configIsChecked) {
//            configIsChecked = true;
//            // 为空则代表未配置 baseUrl
//            if (StringUtils.isEmpty(baseUrl)) {
//                configIsPassed = false;
//            } else {
//                this.baseUrl = baseUrl;
//                // 判断配置的地址是否有效
//                try {
//                    RestUtil.get(this.getBaseUrl().toString());
//                } catch (Exception e) {
//                    configIsPassed = false;
//                }
//            }
//            if (configIsPassed) {
//                log.info("ElasticSearch服务连接成功");
//            } else {
//                log.warn("ElasticSearch 服务连接失败，原因：配置未通过。可能是BaseURL未配置或配置有误，也可能是Elasticsearch服务未启动。接下来将会拒绝执行任何方法！");
//            }
//        }
//    }
//
//    /**
//     * 检查配置是否通过，未通过就抛出异常，中断执行
//     */
//    private void checkConfig() {
//        if (!configIsPassed) {
//            throw new RuntimeException("配置未通过，拒绝执行该方法");
//        }
//    }
//
//    public StringBuilder getBaseUrl(String indexName, String typeName) {
//        typeName = typeName.trim().toLowerCase();
//        return this.getBaseUrl(indexName).append("/").append(typeName);
//    }
//
//    public StringBuilder getBaseUrl(String indexName) {
//        indexName = indexName.trim().toLowerCase();
//        return this.getBaseUrl().append("/").append(indexName);
//    }
//
//    public StringBuilder getBaseUrl() {
//        return new StringBuilder("http://").append(this.baseUrl);
//    }
//
//    /**
//     * cat 查询ElasticSearch系统数据，返回json
//     */
//    public <T> ResponseEntity<T> _cat(String urlAfter, Class<T> responseType) {
//        this.checkConfig();
//        String url = this.getBaseUrl().append("/_cat").append(urlAfter).append("?").append(FORMAT_JSON).toString();
//        return RestUtil.request(url, HttpMethod.GET, null, null, null, responseType);
//    }
//
//    /**
//     * 查询所有索引
//     * <p>
//     * 查询地址：GET http://{baseUrl}/_cat/indices
//     */
//    public JSONArray getIndices() {
//        this.checkConfig();
//        return getIndices(null);
//    }
//
//
//    /**
//     * 查询单个索引
//     * <p>
//     * 查询地址：GET http://{baseUrl}/_cat/indices/{indexName}
//     */
//    public JSONArray getIndices(String indexName) {
//        this.checkConfig();
//        StringBuilder urlAfter = new StringBuilder("/indices");
//        if (!StringUtils.isEmpty(indexName)) {
//            urlAfter.append("/").append(indexName.trim().toLowerCase());
//        }
//        return _cat(urlAfter.toString(), JSONArray.class).getBody();
//    }
//
//    /**
//     * 索引是否存在
//     */
//    public boolean indexExists(String indexName) {
//        this.checkConfig();
//        try {
//            JSONArray array = getIndices(indexName);
//            return array != null;
//        } catch (org.springframework.web.client.HttpClientErrorException ex) {
//            if (HttpStatus.NOT_FOUND == ex.getStatusCode()) {
//                return false;
//            } else {
//                throw ex;
//            }
//        }
//    }
//
//    /**
//     * 创建索引
//     * <p>
//     * 查询地址：PUT http://{baseUrl}/{indexName}
//     */
//    public boolean createIndex(String indexName) {
//        this.checkConfig();
//        String url = this.getBaseUrl(indexName).toString();
//
//        /* 返回结果 （仅供参考）
//        "createIndex": {
//            "shards_acknowledged": true,
//            "acknowledged": true,
//            "index": "hello_world"
//        }
//        */
//        try {
//            return RestUtil.put(url).getBoolean("acknowledged");
//        } catch (org.springframework.web.client.HttpClientErrorException ex) {
//            if (HttpStatus.BAD_REQUEST == ex.getStatusCode()) {
//                log.warn("索引创建失败：" + indexName + " 已存在，无需再创建");
//            } else {
//                ex.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 删除索引
//     * <p>
//     * 查询地址：DELETE http://{baseUrl}/{indexName}
//     */
//    public boolean removeIndex(String indexName) {
//        this.checkConfig();
//        String url = this.getBaseUrl(indexName).toString();
//        try {
//            return RestUtil.delete(url).getBoolean("acknowledged");
//        } catch (org.springframework.web.client.HttpClientErrorException ex) {
//            if (HttpStatus.NOT_FOUND == ex.getStatusCode()) {
//                log.warn("索引删除失败：" + indexName + " 不存在，无需删除");
//            } else {
//                ex.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 保存数据，详见：saveOrUpdate
//     */
//    public boolean save(String indexName, String typeName, String dataId, JSONObject data) {
//        this.checkConfig();
//        return this.saveOrUpdate(indexName, typeName, dataId, data);
//    }
//
//    /**
//     * 更新数据，详见：saveOrUpdate
//     */
//    public boolean update(String indexName, String typeName, String dataId, JSONObject data) {
//        this.checkConfig();
//        return this.saveOrUpdate(indexName, typeName, dataId, data);
//    }
//
//    /**
//     * 保存或修改索引数据
//     * <p>
//     * 查询地址：PUT http://{baseUrl}/{indexName}/{typeName}/{dataId}
//     *
//     * @param indexName 索引名称
//     * @param typeName  type，一个任意字符串，用于分类
//     * @param dataId    数据id
//     * @param data      要存储的数据
//     * @return
//     */
//    public boolean saveOrUpdate(String indexName, String typeName, String dataId, JSONObject data) {
//        this.checkConfig();
//        String url = this.getBaseUrl(indexName, typeName).append("/").append(dataId).toString();
//        /* 返回结果（仅供参考）
//       "createIndexA2": {
//            "result": "created",
//            "_shards": {
//                "total": 2,
//                "successful": 1,
//                "failed": 0
//            },
//            "_seq_no": 0,
//            "_index": "test_index_1",
//            "_type": "test_type_1",
//            "_id": "a2",
//            "_version": 1,
//            "_primary_term": 1
//        }
//         */
//
//        try {
//            // 去掉 data 中为空的值
//            Set<String> keys = data.keySet();
//            List<String> emptyKeys = new ArrayList<>(keys.size());
//            for (String key : keys) {
//                String value = data.getString(key);
//                if (StringUtils.isEmpty(value)) {
//                    emptyKeys.add(key);
//                }
//            }
//            for (String key : emptyKeys) {
//                data.remove(key);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String result = RestUtil.put(url, data).getString("result");
//        return "created".equals(result) || "updated".equals(result);
//    }
//
//    /**
//     * 删除索引数据
//     * <p>
//     * 请求地址：DELETE http://{baseUrl}/{indexName}/{typeName}/{dataId}
//     */
//    public boolean delete(String indexName, String typeName, String dataId) {
//        this.checkConfig();
//        String url = this.getBaseUrl(indexName, typeName).append("/").append(dataId).toString();
//        /* 返回结果（仅供参考）
//        {
//            "_index": "es_demo",
//            "_type": "docs",
//            "_id": "001",
//            "_version": 3,
//            "result": "deleted",
//            "_shards": {
//                "total": 1,
//                "successful": 1,
//                "failed": 0
//            },
//            "_seq_no": 28,
//            "_primary_term": 18
//        }
//        */
//        try {
//            return "deleted".equals(RestUtil.delete(url).getString("result"));
//        } catch (org.springframework.web.client.HttpClientErrorException ex) {
//            if (HttpStatus.NOT_FOUND == ex.getStatusCode()) {
//                return false;
//            } else {
//                throw ex;
//            }
//        }
//    }
//
//
//    /* = = = 以下关于查询和查询条件的方法 = = =*/
//
//    /**
//     * 查询数据
//     * <p>
//     * 请求地址：POST http://{baseUrl}/{indexName}/{typeName}/_search
//     */
//    public JSONObject search(String indexName, String typeName, JSONObject queryObject) {
//        this.checkConfig();
//        String url = this.getBaseUrl(indexName, typeName).append("/_search").toString();
//
//        log.info("search: " + queryObject.toJSONString());
//
//        return RestUtil.post(url, queryObject);
//    }
//
//    /**
//     * @return { "query": query }
//     */
//    public JSONObject buildQuery(JSONObject query) {
//        JSONObject json = new JSONObject();
//        json.put("query", query);
//        return json;
//    }
//
//    /**
//     * @return { "bool" : { "must": must, "must_not": mustNot, "should": should } }
//     */
//    public JSONObject buildBoolQuery(JSONArray must, JSONArray mustNot, JSONArray should) {
//        JSONObject bool = new JSONObject();
//        if (must != null) {
//            bool.put("must", must);
//        }
//        if (mustNot != null) {
//            bool.put("must_not", mustNot);
//        }
//        if (should != null) {
//            bool.put("should", should);
//        }
//        JSONObject json = new JSONObject();
//        json.put("bool", bool);
//        return json;
//    }
//
//    /**
//     * @param field 要查询的字段
//     * @param args  查询参数，参考： *哈哈* OR *哒* NOT *呵* OR *啊*
//     * @return
//     */
//    public JSONObject buildQueryString(String field, String... args) {
//        if (field == null) {
//            return null;
//        }
//        StringBuilder sb = new StringBuilder(field).append(":(");
//        if (args != null) {
//            for (String arg : args) {
//                sb.append(arg).append(" ");
//            }
//        }
//        sb.append(")");
//        return this.buildQueryString(sb.toString());
//    }
//
//    /**
//     * @return { "query_string": { "query": query }  }
//     */
//    public JSONObject buildQueryString(String query) {
//        JSONObject queryString = new JSONObject();
//        queryString.put("query", query);
//        JSONObject json = new JSONObject();
//        json.put("query_string", queryString);
//        return json;
//    }
//
//    /**
//     * @param field      查询字段
//     * @param min        最小值
//     * @param max        最大值
//     * @param containMin 范围内是否包含最小值
//     * @param containMax 范围内是否包含最大值
//     * @return { "range" : { field : { 『 "gt『e』?containMin" : min 』?min!=null , 『 "lt『e』?containMax" : max 』}} }
//     */
//    public JSONObject buildRangeQuery(String field, Object min, Object max, boolean containMin, boolean containMax) {
//        JSONObject inner = new JSONObject();
//        if (min != null) {
//            if (containMin) {
//                inner.put("gte", min);
//            } else {
//                inner.put("gt", min);
//            }
//        }
//        if (max != null) {
//            if (containMax) {
//                inner.put("lte", max);
//            } else {
//                inner.put("lt", max);
//            }
//        }
//        JSONObject range = new JSONObject();
//        range.put(field, inner);
//        JSONObject json = new JSONObject();
//        json.put("range", range);
//        return json;
//    }
//
//}
//
