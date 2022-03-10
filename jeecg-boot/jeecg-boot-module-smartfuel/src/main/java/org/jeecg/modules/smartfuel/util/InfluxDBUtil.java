package org.jeecg.modules.smartfuel.util;

import com.alipay.api.internal.util.StringUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import lombok.var;
import org.jeecg.modules.smartfuel.entity.InfluxDBEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * 转化为influxdb需要的时间和点
     *
     * @param startTime
     * @param endTime
     * @param tagList
     * @return
     */
    private HashMap<String, String> TimeAndStr(LocalDateTime startTime, LocalDateTime endTime, List<String> tagList) {
        var dic = new HashMap<String, String>();
        var startTimeStr = startTime.toInstant(ZoneOffset.of("+8")).toString();
        var endTimeStr = endTime.toInstant(ZoneOffset.of("+8")).toString();
        String str = "";
        for (int i = 0; i < tagList.size(); i++) {
            if (i == 0) {
                str = String.format("r._field == \"%s\"", tagList.get(i));
            } else {
                str = str + String.format(" or r._field == \"%s\"", tagList.get(i));
            }
        }
        dic.put("start", startTimeStr);
        dic.put("end", endTimeStr);
        dic.put("str", str);
        return dic;
    }

    /**
     * 获取历史数据集合
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param every     获取间隔
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, List<InfluxDBEntity>> SearchBatchMaps(LocalDateTime startTime, LocalDateTime endTime,
                                                                 List<String> tagList, String every, String number) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, List<InfluxDBEntity>>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        if (every != "") {
            sql += String.format("|> aggregateWindow(every: %s, fn: mean,createEmpty: false)", every);//是否设置获取间隔
        }
        sql += "|> yield()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var list = new ArrayList<InfluxDBEntity>();
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            for (var x :
                    result) {
                var entity = new InfluxDBEntity();
                entity.setField(x.getField());
                entity.setTime(x.getTime());
                entity.setValue(x.getValue() instanceof Double ? ((double) x.getValue()) : 0);//转化为double
                list.add(entity);
            }
            historyList.put(list.stream().findFirst().get().getField(), list);
        }
        return historyList;
    }

    /**
     * 正常查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param every     获取间隔
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, List<InfluxDBEntity>> Search(LocalDateTime startTime, LocalDateTime endTime,
                                                        List<String> tagList, String every, String number) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var res = new HashMap<String, List<InfluxDBEntity>>();//存放结果
        for (var tag :
                tagList) {
            var point = tags.get(tag);
            if (point != null) {
                List<InfluxDBEntity> list = Stream.of(point.get(0), point.get(point.size() - 1)).collect(Collectors.toList());
                res.put(tag, list);
            }
        }
        return res;
    }

    /**
     * 计算点数据重置时的值的差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param every     获取间隔
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Double> SearchTotal(LocalDateTime startTime, LocalDateTime endTime,
                                               List<String> tagList, String every, String number) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var res = new HashMap<String, Double>();//存放结果
        for (var tag :
                tagList) {
            var point = tags.get(tag);
            if (point != null) {
                double sum = 0;
                for (var i = 0; i < point.size(); i++) {
                    if (i != 0) {
                        var big = point.get(i).getValue();
                        var small = point.get(i - 1).getValue();
                        if (big >= small) {
                            sum += big - small;
                        }
                    }
                }
                res.put(tag, sum);
            }
        }
        return res;
    }

    /**
     * 通过DI量获取时长
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param every     获取间隔
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Long> SearchDI(LocalDateTime startTime, LocalDateTime endTime,
                                          List<String> tagList, String every, String number) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var res = new HashMap<String, Long>();//存放结果
        for (var tag :
                tagList) {
            var point = tags.get(tag);
            if (point != null) {
                long sum = 0;
                for (var i = 0; i < point.size(); i++) {
                    if (i != 0) {
                        var value = point.get(i).getValue();
                        var valuePre = point.get(i - 1).getValue();
                        if ((value == 1.0 && valuePre == 1.0) || (value == 0 && valuePre == 1.0)) {//当值一直是1，或者从1到0时视为在运行
                            var time = point.get(i).getTime();
                            var timePre = point.get(i - 1).getTime();
                            sum += time.getEpochSecond() - timePre.getEpochSecond();
                        }
                    }
                }
                res.put(tag, sum);
            }
        }
        return res;
    }

    /**
     * 获取差值(使用influx自带的increase)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, List<InfluxDBEntity>> GetIncrease(LocalDateTime startTime, LocalDateTime endTime,
                                                             List<String> tagList, String number) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, List<InfluxDBEntity>>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        sql += "|> increase()";
        sql += "|> last()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var list = new ArrayList<InfluxDBEntity>();
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            for (var x :
                    result) {
                var entity = new InfluxDBEntity();
                entity.setField(x.getField());
                entity.setTime(x.getTime());
                entity.setValue(x.getValue() instanceof Double ? ((double) x.getValue()) : 0);//转化为double
                list.add(entity);
            }
            historyList.put(list.stream().findFirst().get().getField(), list);
        }
        return historyList;
    }

    /**
     * 读取 InfluxDb 线性插补数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param every     获取间隔
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, List<InfluxDBEntity>> QueryInterval(LocalDateTime startTime, LocalDateTime endTime,
                                                               List<String> tagList, String every, String number) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, List<InfluxDBEntity>>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        if (every != "") {
            sql += String.format("|> aggregateWindow(every: %s, fn: mean,createEmpty: false)", every);//是否设置获取间隔
        }
        sql += String.format("|> truncateTimeColumn(unit: %s)", every);
        sql += "|> yield()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var list = new ArrayList<InfluxDBEntity>();
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            for (var x :
                    result) {
                var entity = new InfluxDBEntity();
                entity.setField(x.getField());
                entity.setTime(x.getTime());
                entity.setValue(x.getValue() instanceof Double ? ((double) x.getValue()) : 0);//转化为double
                list.add(entity);
            }
            historyList.put(list.stream().findFirst().get().getField(), list);
        }
        return historyList;
    }

    /**
     * 获取时间区间类临界值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param every     获取间隔
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, List<Double>> GetRangeTagMarginValue(LocalDateTime startTime, LocalDateTime endTime,
                                                                List<String> tagList, String every, String number) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var res = new HashMap<String, List<Double>>();//存放结果
        for (var tag :
                tagList) {
            var point = tags.get(tag);
            if (point != null) {
                List<Double> list = Stream.of(point.get(0).getValue(), point.get(point.size() - 1).getValue()).collect(Collectors.toList());
                res.put(tag, list);
            }
        }
        return res;
    }

    /**
     * 前后 n 秒数据差值范围外集合
     *
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @param tagList         点表
     * @param intervalSeconds 间隔时间
     * @param number          数据源编号
     * @param max             最大值
     * @param min             最小值
     * @return
     */
    public HashMap<String, List<InfluxDBEntity>> SearchRangeChangeMaps(LocalDateTime startTime, LocalDateTime endTime,
                                                                       List<String> tagList, String number, String intervalSeconds, Double max, Double min) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, List<InfluxDBEntity>>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        sql += String.format("|> derivative(unit: %s, nonNegative: false)", intervalSeconds);
        sql += String.format("|> filter(fn: (r) => r._value == 0 or r._value > %s or r._value < %s)", max, min);
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        sql += "|> yield()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var list = new ArrayList<InfluxDBEntity>();
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            for (var x :
                    result) {
                var entity = new InfluxDBEntity();
                entity.setField(x.getField());
                entity.setTime(x.getTime());
                entity.setValue(x.getValue() instanceof Double ? ((double) x.getValue()) : 0);//转化为double
                list.add(entity);
            }
            historyList.put(list.stream().findFirst().get().getField(), list);
        }
        return historyList;
    }

    /**
     * 获取在指定范围数值上的点持续时间戳
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param tagList      点表
     * @param every        获取间隔
     * @param number       数据源编号
     * @param flagMinValue 起始经验值
     * @param flagMaxValue 结束经验值
     * @return
     */
    public HashMap<String, Long> GetRangeUpTimeSpan(LocalDateTime startTime, LocalDateTime endTime,
                                                    List<String> tagList, String every, String number, Double flagMinValue, Double flagMaxValue) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var res = new HashMap<String, Long>();//存放结果
        for (var tag :
                tagList) {
            var point = tags.get(tag);
            Long sum = 0L;
            Long lastTime = startTime.toEpochSecond(ZoneOffset.UTC);
            Boolean adhere = false;
            if (point != null) {
                for (var item :
                        point) {
                    Boolean isSection = item.getValue() > flagMinValue && item.getValue() < flagMaxValue;//点的值是否符合范围
                    if (!isSection && adhere) {
                        sum += item.getTime().getEpochSecond() - lastTime;
                    } else if (!adhere && isSection) {
                        lastTime = item.getTime().getEpochSecond();
                    } else if (adhere && isSection) {
                        sum += item.getTime().getEpochSecond() - lastTime;
                        lastTime = item.getTime().getEpochSecond();
                    }
                    adhere = isSection;
                }
                res.put(tag, sum);
            }
        }
        return res;
    }

    /**
     * 获取某个时间点前一个值
     *
     * @param startTime 开始时间
     * @param tagList   点表
     * @param number    数据源编号
     * @param searchDay 验推天数
     * @return
     */
    public HashMap<String, Double> GetTimeBeforeValue(LocalDateTime startTime,
                                                      List<String> tagList, String number, Integer searchDay) {
        var res = TimeAndStr(startTime.minusDays(searchDay), startTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, Double>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        sql += "|> last()";
        sql += "|> yield()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            var sin = result.get(result.size() - 1);
            var value = sin.getValue() instanceof Double ? ((double) sin.getValue()) : 0;
            historyList.put(sin.getField(), value);
        }
        return historyList;
    }

    /**
     * 点一段时间内最大值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Double> GetValuesMax(LocalDateTime startTime, LocalDateTime endTime,
                                                List<String> tagList, String number) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, Double>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        sql += "|> max()";
        sql += "|> yield()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            var sin = result.get(result.size() - 1);
            var value = sin.getValue() instanceof Double ? ((double) sin.getValue()) : 0;
            historyList.put(sin.getField(), value);
        }
        var result = new HashMap<String, Double>();//存放全部的点数据，没查到的用最近的历史数据
        for (String tag :
                tagList) {
            if (historyList.containsKey(tag)) {
                result.put(tag, historyList.get(tag));
            } else {
                var pre = GetTimeBeforeValue(startTime, tagList, "", 3);
                result.put(tag, pre.get(tag));
            }
        }
        return result;
    }

    /**
     * 点一段时间内最小值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Double> GetValuesMin(LocalDateTime startTime, LocalDateTime endTime,
                                                List<String> tagList, String number) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, Double>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        sql += "|> min()";
        sql += "|> yield()";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            var sin = result.get(result.size() - 1);
            var value = sin.getValue() instanceof Double ? ((double) sin.getValue()) : 0;
            historyList.put(sin.getField(), value);
        }
        var result = new HashMap<String, Double>();//存放全部的点数据，没查到的用最近的历史数据
        for (String tag :
                tagList) {
            if (historyList.containsKey(tag)) {
                result.put(tag, historyList.get(tag));
            } else {
                var pre = GetTimeBeforeValue(startTime, tagList, "", 3);
                result.put(tag, pre.get(tag));
            }
        }
        return result;
    }

    /**
     * 计算DI点从0变化成1次数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Integer> GetStatusTagCount(LocalDateTime startTime, LocalDateTime endTime,
                                                      List<String> tagList, String number) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var result = new HashMap<String, Integer>();//存放全部的点数据，没查到的用最近的历史数据
        for (String tag :
                tagList) {
            var count = 0;
            if (!tags.containsKey(tag)) {
                result.put(tag, 0);
            } else {
                var pre = GetTimeBeforeValue(startTime, tagList, "", 7);
                Boolean adhere = !pre.containsKey(tag) ? true : pre.get(tag) == 0;
                for (var item : tags.get(tag)) {
                    var value = item.getValue();
                    if (value == 1 && adhere) count++;
                    adhere = value == 0;
                }
                result.put(tag, count);
            }
        }
        return result;
    }

    /**
     * 计算DI点区间内的时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Long> GetStatusTagTimeSpan(LocalDateTime startTime, LocalDateTime endTime,
                                                      List<String> tagList, String number) {
        var tags = SearchBatchMaps(startTime, endTime, tagList, "", number);
        var result = new HashMap<String, Long>();//存放全部的点数据，没查到的用最近的历史数据
        for (String tag :
                tagList) {
            Long lastTime = startTime.toEpochSecond(ZoneOffset.UTC);
            Long sum = 0L;
            if (!tags.containsKey(tag)) {
                result.put(tag, 0L);
            } else {
                var pre = GetTimeBeforeValue(startTime, tagList, "", 7).get(tag);
                Boolean adhere = pre == 1;
                var lastmode = tags.get(tag).get(tags.size() - 1);
                for (var item : tags.get(tag)) {
                    var value = item.getValue();
                    if (value == 0 && adhere) {
                        sum += item.getTime().getEpochSecond() - lastTime;
                    } else if (!adhere && value == 1) {
                        lastTime = item.getTime().getEpochSecond();
                    } else if (adhere && value == 1) {
                        sum += item.getTime().getEpochSecond() - lastTime;
                        lastTime = item.getTime().getEpochSecond();
                    }
                    adhere = value == 1;
                }
                if (lastmode.getValue() == 1) {
                    sum += endTime.toEpochSecond(ZoneOffset.UTC) - lastmode.getTime().getEpochSecond();
                }
                result.put(tag, sum);
            }
        }
        return result;
    }

    /**
     * 获取日平均值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tagList   点表
     * @param number    数据源编号
     * @return
     */
    public HashMap<String, Double> GetDayAverage(LocalDateTime startTime, LocalDateTime endTime,
                                                 List<String> tagList, String number) {
        var res = TimeAndStr(startTime, endTime, tagList);//获取开始时间、结束时间、点列表
        var historyList = new HashMap<String, List<InfluxDBEntity>>();//存放历史点结果
        String sql = "from(bucket:\"%s\") |> range(start: %s,stop: %s)";
        sql += "|> filter(fn: (r) => r._measurement == \"%s\" )";
        sql += "|> filter(fn: (r) => %s)";
        if (StringUtils.isNumeric(number)) {
            sql += String.format("|> filter(fn: (r) => r.number==\"%s\")", number);
        }
        sql += "|> timedMovingAverage(every:24h ,period: 24h)";
        String flux = String.format(sql, bucket, res.get("start"), res.get("end"), measurement, res.get("str"));
        QueryApi queryApi = influxDBClient.getQueryApi();
        var tables = queryApi.query(flux, org);
        for (var table :
                tables) {
            var list = new ArrayList<InfluxDBEntity>();
            var result = Optional.ofNullable(table).map(fluxTable -> fluxTable.getRecords()).orElse(Collections.emptyList());
            for (var x :
                    result) {
                var entity = new InfluxDBEntity();
                entity.setField(x.getField());
                entity.setTime(x.getTime());
                entity.setValue(x.getValue() instanceof Double ? ((double) x.getValue()) : 0);//转化为double
                list.add(entity);
            }
            historyList.put(list.stream().findFirst().get().getField(), list);
        }
        var result = new HashMap<String, Double>();//存放全部的点数据，没查到的用最近的历史数据
        for (String tag :
                tagList) {
            if (historyList.containsKey(tag)) {
                var entitys = historyList.get(tag);
                result.put(tag, entitys.get(entitys.size() - 1).getValue());
            } else {
                result.put(tag, 0.0);
            }
        }
        return result;
    }
}
