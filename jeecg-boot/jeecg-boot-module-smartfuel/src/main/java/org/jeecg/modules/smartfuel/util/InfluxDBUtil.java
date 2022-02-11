package org.jeecg.modules.smartfuel.util;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class InfluxDBUtil {
    @Autowired
    InfluxDBClient influxDBClient;
    @Value("${spring.influx.url:''}")
    private String url;
    @Value("${spring.influx.token:''}")
    private String token;
    @Value("${spring.influx.org:''}")
    private String org;
    @Value("${spring.influx.bucket:''}")
    private String bucket;
    @Value("${spring.influx.measurement:''}")
    private String measurement;

    /**
     * 查询语法说明
     * 1、bucket 桶
     * 2、range 指定起始时间段
     * range有两个参数start，stop，stop不设置默认为当前。
     * range可以是相对的（使用负持续时间）或绝对（使用时间段）
     * 3、filter 过滤条件查询 _measurement 表  _field 字段
     * 4、yield()函数作为查询结果输出过滤的tables。
     *
     * @return
     */
    public List<FluxRecord> querySingleTag(String tagName) {
        String sql = "from(bucket: \"%s\") |> range(start: -10m)";
        sql += "  |> filter(fn: (r) => r._measurement == \"%s\" and";
        sql += "  r._field == \"%s\")";
        sql += "  |> yield()";
        String flux = String.format(sql, bucket, measurement, tagName);
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        var table = tables.stream().findFirst().orElse(null);
        var res = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords());
        return res.orElse(Collections.emptyList());
    }
}
