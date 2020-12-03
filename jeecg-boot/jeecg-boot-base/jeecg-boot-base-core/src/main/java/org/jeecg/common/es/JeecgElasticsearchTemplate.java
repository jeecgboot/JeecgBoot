package org.jeecg.common.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 关于 ElasticSearch 的一些方法（创建索引、添加数据、查询等）
 *
 * @author sunjianlei
 */
@Slf4j
@Component
public class JeecgElasticsearchTemplate {
    /** es服务地址 */
    private String baseUrl;
    private final String FORMAT_JSON = "format=json";

    // ElasticSearch 最大可返回条目数
    public static final int ES_MAX_SIZE = 10000;

    public JeecgElasticsearchTemplate(@Value("${jeecg.elasticsearch.cluster-nodes}") String baseUrl, @Value("${jeecg.elasticsearch.check-enabled}") boolean checkEnabled) {
        log.debug("JeecgElasticsearchTemplate BaseURL：" + baseUrl);
        if (StringUtils.isNotEmpty(baseUrl)) {
            this.baseUrl = baseUrl;
            // 验证配置的ES地址是否有效
            if (checkEnabled) {
                try {
                    RestUtil.get(this.getBaseUrl().toString());
                    log.info("ElasticSearch 服务连接成功");
                } catch (Exception e) {
                    log.warn("ElasticSearch 服务连接失败，原因：配置未通过。可能是BaseURL未配置或配置有误，也可能是Elasticsearch服务未启动。接下来将会拒绝执行任何方法！");
                }
            }
        }
    }

    public StringBuilder getBaseUrl(String indexName, String typeName) {
        typeName = typeName.trim().toLowerCase();
        return this.getBaseUrl(indexName).append("/").append(typeName);
    }

    public StringBuilder getBaseUrl(String indexName) {
        indexName = indexName.trim().toLowerCase();
        return this.getBaseUrl().append("/").append(indexName);
    }

    public StringBuilder getBaseUrl() {
        return new StringBuilder("http://").append(this.baseUrl);
    }

    /**
     * cat 查询ElasticSearch系统数据，返回json
     */
    public <T> ResponseEntity<T> _cat(String urlAfter, Class<T> responseType) {
        String url = this.getBaseUrl().append("/_cat").append(urlAfter).append("?").append(FORMAT_JSON).toString();
        return RestUtil.request(url, HttpMethod.GET, null, null, null, responseType);
    }

    /**
     * 查询所有索引
     * <p>
     * 查询地址：GET http://{baseUrl}/_cat/indices
     */
    public JSONArray getIndices() {
        return getIndices(null);
    }


    /**
     * 查询单个索引
     * <p>
     * 查询地址：GET http://{baseUrl}/_cat/indices/{indexName}
     */
    public JSONArray getIndices(String indexName) {
        StringBuilder urlAfter = new StringBuilder("/indices");
        if (!StringUtils.isEmpty(indexName)) {
            urlAfter.append("/").append(indexName.trim().toLowerCase());
        }
        return _cat(urlAfter.toString(), JSONArray.class).getBody();
    }

    /**
     * 索引是否存在
     */
    public boolean indexExists(String indexName) {
        try {
            JSONArray array = getIndices(indexName);
            return array != null;
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            if (HttpStatus.NOT_FOUND == ex.getStatusCode()) {
                return false;
            } else {
                throw ex;
            }
        }
    }

    /**
     * 根据ID获取索引数据，未查询到返回null
     * <p>
     * 查询地址：GET http://{baseUrl}/{indexName}/{typeName}/{dataId}
     *
     * @param indexName 索引名称
     * @param typeName  type，一个任意字符串，用于分类
     * @param dataId    数据id
     * @return
     */
    public JSONObject getDataById(String indexName, String typeName, String dataId) {
        String url = this.getBaseUrl(indexName, typeName).append("/").append(dataId).toString();
        log.info("url:" + url);
        JSONObject result = RestUtil.get(url);
        boolean found = result.getBoolean("found");
        if (found) {
            return result.getJSONObject("_source");
        } else {
            return null;
        }
    }

    /**
     * 创建索引
     * <p>
     * 查询地址：PUT http://{baseUrl}/{indexName}
     */
    public boolean createIndex(String indexName) {
        String url = this.getBaseUrl(indexName).toString();

        /* 返回结果 （仅供参考）
        "createIndex": {
            "shards_acknowledged": true,
            "acknowledged": true,
            "index": "hello_world"
        }
        */
        try {
            return RestUtil.put(url).getBoolean("acknowledged");
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            if (HttpStatus.BAD_REQUEST == ex.getStatusCode()) {
                log.warn("索引创建失败：" + indexName + " 已存在，无需再创建");
            } else {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除索引
     * <p>
     * 查询地址：DELETE http://{baseUrl}/{indexName}
     */
    public boolean removeIndex(String indexName) {
        String url = this.getBaseUrl(indexName).toString();
        try {
            return RestUtil.delete(url).getBoolean("acknowledged");
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            if (HttpStatus.NOT_FOUND == ex.getStatusCode()) {
                log.warn("索引删除失败：" + indexName + " 不存在，无需删除");
            } else {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取索引字段映射（可获取字段类型）
     * <p>
     *
     * @param indexName 索引名称
     * @param typeName  分类名称
     * @return
     */
    public JSONObject getIndexMapping(String indexName, String typeName) {
        String url = this.getBaseUrl(indexName, typeName).append("/_mapping?").append(FORMAT_JSON).toString();
        log.info("getIndexMapping-url:" + url);
        /*
         * 参考返回JSON结构：
         *
         *{
         *    // 索引名称
         *    "[indexName]": {
         *        "mappings": {
         *            // 分类名称
         *            "[typeName]": {
         *                "properties": {
         *                    // 字段名
         *                    "input_number": {
         *                        // 字段类型
         *                        "type": "long"
         *                    },
         *                    "input_string": {
         *                        "type": "text",
         *                        "fields": {
         *                            "keyword": {
         *                                "type": "keyword",
         *                                "ignore_above": 256
         *                            }
         *                        }
         *                    }
         *                 }
         *            }
         *        }
         *    }
         * }
         */
        try {
            return RestUtil.get(url);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String message = e.getMessage();
            if (message != null && message.contains("404 Not Found")) {
                return null;
            }
            throw e;
        }
    }

    /**
     * 获取索引字段映射，返回Java实体类
     *
     * @param indexName
     * @param typeName
     * @return
     */
    public <T> Map<String, T> getIndexMappingFormat(String indexName, String typeName, Class<T> clazz) {
        JSONObject mapping = this.getIndexMapping(indexName, typeName);
        Map<String, T> map = new HashMap<>();
        if (mapping == null) {
            return map;
        }
        // 获取字段属性
        JSONObject properties = mapping.getJSONObject(indexName)
                .getJSONObject("mappings")
                .getJSONObject(typeName)
                .getJSONObject("properties");
        // 封装成 java类型
        for (String key : properties.keySet()) {
            T entity = properties.getJSONObject(key).toJavaObject(clazz);
            map.put(key, entity);
        }
        return map;
    }

    /**
     * 保存数据，详见：saveOrUpdate
     */
    public boolean save(String indexName, String typeName, String dataId, JSONObject data) {
        return this.saveOrUpdate(indexName, typeName, dataId, data);
    }

    /**
     * 更新数据，详见：saveOrUpdate
     */
    public boolean update(String indexName, String typeName, String dataId, JSONObject data) {
        return this.saveOrUpdate(indexName, typeName, dataId, data);
    }

    /**
     * 保存或修改索引数据
     * <p>
     * 查询地址：PUT http://{baseUrl}/{indexName}/{typeName}/{dataId}
     *
     * @param indexName 索引名称
     * @param typeName  type，一个任意字符串，用于分类
     * @param dataId    数据id
     * @param data      要存储的数据
     * @return
     */
    public boolean saveOrUpdate(String indexName, String typeName, String dataId, JSONObject data) {
        String url = this.getBaseUrl(indexName, typeName).append("/").append(dataId).append("?refresh=wait_for").toString();
        /* 返回结果（仅供参考）
       "createIndexA2": {
            "result": "created",
            "_shards": {
                "total": 2,
                "successful": 1,
                "failed": 0
            },
            "_seq_no": 0,
            "_index": "test_index_1",
            "_type": "test_type_1",
            "_id": "a2",
            "_version": 1,
            "_primary_term": 1
        }
         */

        try {
            // 去掉 data 中为空的值
            Set<String> keys = data.keySet();
            List<String> emptyKeys = new ArrayList<>(keys.size());
            for (String key : keys) {
                String value = data.getString(key);
                //1、剔除空值
                if (oConvertUtils.isEmpty(value) || "[]".equals(value)) {
                    emptyKeys.add(key);
                }
                //2、剔除上传控件值(会导致ES同步失败，报异常failed to parse field [ge_pic] of type [text] )
                if (oConvertUtils.isNotEmpty(value) && value.indexOf("[{")!=-1) {
                    emptyKeys.add(key);
                    log.info("-------剔除上传控件字段------------key: "+ key);
                }
            }
            for (String key : emptyKeys) {
                data.remove(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String result = RestUtil.put(url, data).getString("result");
            return "created".equals(result) || "updated".equals(result);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n-- url: " + url + "\n-- data: " + data.toJSONString());
            //TODO 打印接口返回异常json
            return false;
        }
    }

    /**
     * 批量保存数据
     *
     * @param indexName 索引名称
     * @param typeName  type，一个任意字符串，用于分类
     * @param dataList  要存储的数据数组，每行数据必须包含id
     * @return
     */
    public boolean saveBatch(String indexName, String typeName, JSONArray dataList) {
        String url = this.getBaseUrl().append("/_bulk").append("?refresh=wait_for").toString();
        StringBuilder bodySB = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject data = dataList.getJSONObject(i);
            String id = data.getString("id");
            // 该行的操作
            // {"create": {"_id":"${id}", "_index": "${indexName}", "_type": "${typeName}"}}
            JSONObject action = new JSONObject();
            JSONObject actionInfo = new JSONObject();
            actionInfo.put("_id", id);
            actionInfo.put("_index", indexName);
            actionInfo.put("_type", typeName);
            action.put("create", actionInfo);
            bodySB.append(action.toJSONString()).append("\n");
            // 该行的数据
            data.remove("id");
            bodySB.append(data.toJSONString()).append("\n");
        }
        System.out.println("+-+-+-: bodySB.toString(): " + bodySB.toString());
        HttpHeaders headers = RestUtil.getHeaderApplicationJson();
        RestUtil.request(url, HttpMethod.PUT, headers, null, bodySB, JSONObject.class);
        return true;
    }

    /**
     * 删除索引数据
     * <p>
     * 请求地址：DELETE http://{baseUrl}/{indexName}/{typeName}/{dataId}
     */
    public boolean delete(String indexName, String typeName, String dataId) {
        String url = this.getBaseUrl(indexName, typeName).append("/").append(dataId).toString();
        /* 返回结果（仅供参考）
        {
            "_index": "es_demo",
            "_type": "docs",
            "_id": "001",
            "_version": 3,
            "result": "deleted",
            "_shards": {
                "total": 1,
                "successful": 1,
                "failed": 0
            },
            "_seq_no": 28,
            "_primary_term": 18
        }
        */
        try {
            return "deleted".equals(RestUtil.delete(url).getString("result"));
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            if (HttpStatus.NOT_FOUND == ex.getStatusCode()) {
                return false;
            } else {
                throw ex;
            }
        }
    }


    /* = = = 以下关于查询和查询条件的方法 = = =*/

    /**
     * 查询数据
     * <p>
     * 请求地址：POST http://{baseUrl}/{indexName}/{typeName}/_search
     */
    public JSONObject search(String indexName, String typeName, JSONObject queryObject) {
        String url = this.getBaseUrl(indexName, typeName).append("/_search").toString();

        log.info("url:" + url + " ,search: " + queryObject.toJSONString());
        JSONObject res = RestUtil.post(url, queryObject);
        log.info("url:" + url + " ,return res: \n" + res.toJSONString());
        return res;
    }

    /**
     * @param _source （源滤波器）指定返回的字段，传null返回所有字段
     * @param query
     * @param from    从第几条数据开始
     * @param size    返回条目数
     * @return { "query": query }
     */
    public JSONObject buildQuery(List<String> _source, JSONObject query, int from, int size) {
        JSONObject json = new JSONObject();
        if (_source != null) {
            json.put("_source", _source);
        }
        json.put("query", query);
        json.put("from", from);
        json.put("size", size);
        return json;
    }

    /**
     * @return { "bool" : { "must": must, "must_not": mustNot, "should": should } }
     */
    public JSONObject buildBoolQuery(JSONArray must, JSONArray mustNot, JSONArray should) {
        JSONObject bool = new JSONObject();
        if (must != null) {
            bool.put("must", must);
        }
        if (mustNot != null) {
            bool.put("must_not", mustNot);
        }
        if (should != null) {
            bool.put("should", should);
        }
        JSONObject json = new JSONObject();
        json.put("bool", bool);
        return json;
    }

    /**
     * @param field 要查询的字段
     * @param args  查询参数，参考： *哈哈* OR *哒* NOT *呵* OR *啊*
     * @return
     */
    public JSONObject buildQueryString(String field, String... args) {
        if (field == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(field).append(":(");
        if (args != null) {
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
        }
        sb.append(")");
        return this.buildQueryString(sb.toString());
    }

    /**
     * @return { "query_string": { "query": query }  }
     */
    public JSONObject buildQueryString(String query) {
        JSONObject queryString = new JSONObject();
        queryString.put("query", query);
        JSONObject json = new JSONObject();
        json.put("query_string", queryString);
        return json;
    }

    /**
     * @param field      查询字段
     * @param min        最小值
     * @param max        最大值
     * @param containMin 范围内是否包含最小值
     * @param containMax 范围内是否包含最大值
     * @return { "range" : { field : { 『 "gt『e』?containMin" : min 』?min!=null , 『 "lt『e』?containMax" : max 』}} }
     */
    public JSONObject buildRangeQuery(String field, Object min, Object max, boolean containMin, boolean containMax) {
        JSONObject inner = new JSONObject();
        if (min != null) {
            if (containMin) {
                inner.put("gte", min);
            } else {
                inner.put("gt", min);
            }
        }
        if (max != null) {
            if (containMax) {
                inner.put("lte", max);
            } else {
                inner.put("lt", max);
            }
        }
        JSONObject range = new JSONObject();
        range.put(field, inner);
        JSONObject json = new JSONObject();
        json.put("range", range);
        return json;
    }

}

