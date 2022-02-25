package org.jeecg.modules.smartfuel;


import lombok.var;
import org.jeecg.modules.smartfuel.entity.InfluxDBEntity;
import org.jeecg.modules.smartfuel.util.InfluxDBUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestInfluxDB {


    @Autowired
    private InfluxDBUtil influx;


    @Test
    public void SearchBatchMapsTest() {
        List<String> list = new ArrayList<>();
        list.add("BF_IN[0]");
        list.add("BF_IN[33]");
        list.add("BF_IN[35]");
        list.add("BF_IN[36]");
        String startTime = "2022-02-01T13:00:00";
        String endTime = "2022-02-11T13:00:00";
        String every = "10s";//间隔时间
        String number = "1";//数据源编号
        Double min = 3.0;
        Double max = 3.0;
        String intervalSeconds = "10s";//间隔时间
        Integer searchDay = 1;//验推天数  不能为0
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);

        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchBatchMaps  获取历史数据集合-------------------------");
        var res_SearchBatchMaps = influx.SearchBatchMaps(start, end, list, every, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_SearchBatchMaps.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------Search  正常查询-------------------------");
        var res_Search = influx.Search(start, end, list, every, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_Search.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchTotal  计算点数据重置时的值的差-------------------------");
        var res_SearchTotal = influx.SearchTotal(start, end, list, every, number);
        for (Map.Entry<String, Double> entry : res_SearchTotal.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value  is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchDI  通过DI量获取时长-------------------------");
        var res_SearchDI = influx.SearchDI(start, end, list, every, number);
        for (Map.Entry<String, Long> entry : res_SearchDI.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value  is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetIncrease  获取历史数据集合-------------------------");
        var res_GetIncrease = influx.GetIncrease(start, end, list, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_GetIncrease.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------QueryInterval  读取 InfluxDb 线性插补数据-------------------------");
        var res_QueryInterval = influx.QueryInterval(start, end, list, every, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_QueryInterval.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetRangeTagMarginValue  获取时间区间类临界值-------------------------");
        var res_GetRangeTagMarginValue = influx.GetRangeTagMarginValue(start, end, list, every, number);
        for (Map.Entry<String, List<Double>> entry : res_GetRangeTagMarginValue.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchRangeChangeMaps  获取时间区间类临界值-------------------------");
        var res_SearchRangeChangeMaps = influx.SearchRangeChangeMaps(start, end, list, number, every, max, min);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_SearchRangeChangeMaps.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetRangeUpTimeSpan  获取在指定范围数值上的点持续时间戳-------------------------");
        var res_GetRangeUpTimeSpan = influx.GetRangeUpTimeSpan(start, end, list, number, every, min, max);
        for (Map.Entry<String, Long> entry : res_GetRangeUpTimeSpan.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetTimeBeforeValue  获取某个时间点前一个值-------------------------");
        var res_GetTimeBeforeValue = influx.GetTimeBeforeValue(start, list, number, searchDay);
        for (Map.Entry<String, Double> entry : res_GetTimeBeforeValue.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetValuesMax  点一段时间内最大值-------------------------");
        var res_GetValuesMax = influx.GetValuesMax(start, end, list, number);
        for (Map.Entry<String, Double> entry : res_GetValuesMax.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetValuesMin  点一段时间内最小值-------------------------");
        var res_GetValuesMin = influx.GetValuesMin(start, end, list, number);
        for (Map.Entry<String, Double> entry : res_GetValuesMin.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetStatusTagCount  计算DI点从0变化成1次数-------------------------");
        var res_GetStatusTagCount = influx.GetStatusTagCount(start, end, list, number);
        for (Map.Entry<String, Integer> entry : res_GetStatusTagCount.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetStatusTagTimeSpan  计算DI点区间内的时间-------------------------");
        var res_GetStatusTagTimeSpan = influx.GetStatusTagTimeSpan(start, end, list, number);
        for (Map.Entry<String, Long> entry : res_GetStatusTagTimeSpan.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetDayAverage  获取日平均值-------------------------");
        var res_GetDayAverage = influx.GetDayAverage(start, end, list, number);
        for (Map.Entry<String, Double> entry : res_GetDayAverage.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "value size is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');


    }


}
