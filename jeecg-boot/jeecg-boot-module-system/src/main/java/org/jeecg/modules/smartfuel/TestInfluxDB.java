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
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestInfluxDB {


    @Autowired
    private InfluxDBUtil influx;

    private List<String> list = Arrays.asList("BF_IN[33]", "BF_IN[35]", "BF_IN[36]");
    private String startTime = "2022-02-01T08:00:00";
    private String endTime = "2022-02-05T08:00:00";
    private LocalDateTime start = LocalDateTime.parse(startTime);
    private LocalDateTime end = LocalDateTime.parse(endTime);
    private String every = "5s";//间隔时间
    private String number = "1";//数据源编号
    private Double min = 1.0;
    private Double max = 3.0;
    private Integer searchDay = 28;//验推天数  不能为0

    /**
     * SearchBatchMaps  获取历史数据集合
     * 测试结果：
     * 参数：2022-02-01T08:00:00--2022-02-04T08:00:00 every=10s list={"BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} number=1
     * 结果 BF_IN[36]历史数据集合个数为467
     * BF_IN[35]历史数据集合个数为248
     * BF_IN[33]历史数据集合个数为115
     * 参数：2022-02-01T08:00:00--2022-02-11T08:00:00 every=10s list={"BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} number=1
     * 结果 BF_IN[36]历史数据集合个数为3304
     * BF_IN[35]历史数据集合个数为2623
     * BF_IN[33]历史数据集合个数为302
     * ----------------------
     * 其他参数测试也与数据库查询结果一致，集合中InfluxDBEntity对象属性也一致
     */
    @Test
    public void SearchBatchMapsTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchBatchMaps  获取历史数据集合-------------------------");
        var res_SearchBatchMaps = influx.SearchBatchMaps(start, end, list, every, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_SearchBatchMaps.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "获取历史数据集合 size is " + entry.getValue().size());
            iteratorResult(entry);//遍历打印数据
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * Search  正常查询
     * 测试结果：
     * 参数2022-02-09T08:00:00--2022-02-20T08:00:00  number=1  list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every参数没用到
     * 结果	"result": {
     * "BF_IN[36]": [
     * {"value": 0,"field": "BF_IN[36]","time": "2022-02-09T09:29:19.315Z"},
     * {"value": 0,"field": "BF_IN[36]","time": "2022-02-11T09:30:51.487Z" }
     * ],
     * "BF_IN[35]": [
     * {"value": 6,"field": "BF_IN[35]","time": "2022-02-09T09:29:20.315Z"},
     * {"value": 6,"field": "BF_IN[35]","time": "2022-02-11T09:30:51.487Z"}
     * ],
     * "BF_IN[0]": [
     * {"value": 0,"field": "BF_IN[0]","time": "2022-02-09T08:00:01.645Z" },
     * {"value": 1,"field": "BF_IN[0]","time": "2022-02-11T09:30:50.486Z" }
     * ],
     * "BF_IN[33]": [
     * {"value": 6,"field": "BF_IN[33]","time": "2022-02-09T09:29:28.318Z" },
     * {"value": 1,"field": "BF_IN[33]","time": "2022-02-11T05:46:05.447Z"}
     * ]
     * },
     * 每个点数据，结果只有两条数据
     * 因为InfluxDBUtil中Search（）方法，返回结果只截取list了一头一尾数据
     * 其他参数测试也与数据库查询结果一致
     */
    @Test
    public void SearchTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------Search  正常查询-------------------------");
        var res_Search = influx.Search(start, end, list, every, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_Search.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "正常查询 size is " + entry.getValue().size());
            iteratorResult(entry);//遍历打印数据
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }


    /**
     * SearchTotal  计算点数据重置时的值的差
     * 测试结果：
     * 参数2022-02-01T08:00:00--2022-02-09T08:00:00  number=1  list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every没用到
     * 结果	"result": {
     * "BF_IN[36]": 5763,
     * "BF_IN[35]": 4433,
     * "BF_IN[0]": 149871,
     * "BF_IN[33]": 515
     * },
     * 参数2022-02-09T08:00:00--2022-02-20T08:00:00  number=1  list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every没用到
     * "result": {
     * "BF_IN[36]": 2823,
     * "BF_IN[35]": 2138,
     * "BF_IN[0]": 38676,
     * "BF_IN[33]": 89
     * },
     * 差值是相邻两个值比较相减，然后累加（正数才相加）
     * 其他参数测试也与数据库查询结果一致
     */
    @Test
    public void SearchTotalTest() {
        List<String> list = new ArrayList<>();
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchTotal  计算点数据重置时的值的差-------------------------");
        var res_SearchTotal = influx.SearchTotal(start, end, list, every, number);
        for (Map.Entry<String, Double> entry : res_SearchTotal.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "计算点数据重置时的值的差  is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * SearchDI  通过DI量获取时长
     * 测试结果：
     * 参数 2022-02-01T08:00:00--2022-02-22T08:00:00  number=1  list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every没用到
     * 结果 	"result": {
     * "BF_IN[36]": 427855,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 434626,
     * "BF_IN[33]": 39572
     * },
     * 参数 2022-02-01T08:00:00--2022-02-09T08:00:00  number=1  list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every没用到
     * 结果	"result": {
     * "BF_IN[36]": 348516,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 345636,
     * "BF_IN[33]": 39572
     * },
     * 当value值一直是1，或者从1到0时视为在运行,才累加时长
     * 其他参数测试也与数据库查询结果一致
     */
    @Test
    public void SearchDITest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchDI  通过DI量获取时长-------------------------");
        var res_SearchDI = influx.SearchDI(start, end, list, every, number);
        for (Map.Entry<String, Long> entry : res_SearchDI.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "通过DI量获取时长  is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetIncrease  获取历史数据集合
     * 测试结果：
     * 参数 2022-02-01T08:00:00--2022-02-22T08:00:00  number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * 结果	"result": {
     * "BF_IN[36]": [{"value": 8586,"field": "BF_IN[36]","time": "2022-02-11T09:30:51.487Z" }
     * ],
     * "BF_IN[35]": [{"value": 6576,"field": "BF_IN[35]","time": "2022-02-11T09:30:51.487Z" }
     * ],
     * "BF_IN[0]": [{"value": 188547,"field": "BF_IN[0]","time": "2022-02-11T09:30:50.486Z"}
     * ],
     * "BF_IN[33]": [{"value": 609,"field": "BF_IN[33]","time": "2022-02-11T05:46:05.447Z"}
     * ]
     * },
     * 其他参数测试也与数据`库查询结果一致
     * GetIncrease()方法中sql用了last(),还有increase(),所以只取最后一个作为结果，也就是取最大value
     */
    @Test
    public void GetIncreaseTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetIncrease  获取历史数据集合-------------------------");
        var res_GetIncrease = influx.GetIncrease(start, end, list, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_GetIncrease.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "历史数据集合 size is " + entry.getValue().size());
            iteratorResult(entry);//遍历打印数据
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * QueryInterval  读取 InfluxDb 线性插补数据
     * 测试结果：
     * 参数：2022-02-01T08:00:00--2022-02-02T08:00:00 max=3 min=1 every=10s list={"BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * 结果 BF_IN[36]线性插补数据个数为30
     * BF_IN[35]线性插补数据个数为43
     * BF_IN[33]线性插补数据个数为32
     * 参数 2022-02-01T08:00:00--2022-02-10T08:00:00 max=3 min=1 every=5s list={"BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * 结果BF_IN[36]线性插补数据个数为3247
     * BF_IN[35]线性插补数据个数为2576
     * BF_IN[33]线性插补数据个数为280
     * 其他参数测试也与数据库查询结果一致，InfluxDBEntity对象属性也一致
     */
    @Test
    public void QueryIntervalTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------QueryInterval  读取 InfluxDb 线性插补数据-------------------------");
        var res_QueryInterval = influx.QueryInterval(start, end, list, every, number);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_QueryInterval.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "InfluxDb 线性插补数据 size is " + entry.getValue().size());
            iteratorResult(entry);//遍历打印数据
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetRangeTagMarginValue  获取时间区间类临界值
     * 测试结果：
     * 参数 2022-02-01T08:00:00--2022-02-10T08:00:00  number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every没用到
     * 结果	"result": {
     * "BF_IN[36]": [2,1],
     * "BF_IN[35]": [6,1],
     * "BF_IN[0]": [0,0],
     * "BF_IN[33]": [6,1]
     * },
     * 参数 2022-02-01T08:00:00--2022-03-01T08:00:00  number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"} every没用到
     * 结果 	"result": {
     * "BF_IN[36]": [2,0],
     * "BF_IN[35]": [6,6],
     * "BF_IN[0]": [0,1],
     * "BF_IN[33]": [6,1]
     * },
     * 其他参数测试也与数据库查询结果一致
     */
    @Test
    public void GetRangeTagMarginValueTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetRangeTagMarginValue  获取时间区间类临界值-------------------------");
        var res_GetRangeTagMarginValue = influx.GetRangeTagMarginValue(start, end, list, every, number);
        for (Map.Entry<String, List<Double>> entry : res_GetRangeTagMarginValue.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "获取时间区间类临界值 size is " + entry.getValue().size());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * SearchRangeChangeMaps  前后 n 秒数据差值范围外集合
     * 测试结果：
     * 参数：2022-02-01T08:00:00--2022-02-02T08:00:00 max=3 min=1 every=10s list={"BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * 结果：BF_IN[36]临界值个数大小为32
     * BF_IN[35]临界值个数大小为50
     * BF_IN[33]临界值个数大小为39
     * 其他参数测试也与数据库查询结果一致，InfluxDBEntity对象属性也一致
     */
    @Test
    public void SearchRangeChangeMapsTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------SearchRangeChangeMaps  前后 n 秒数据差值范围外集合-------------------------");
        var res_SearchRangeChangeMaps = influx.SearchRangeChangeMaps(start, end, list, number, every, max, min);
        for (Map.Entry<String, List<InfluxDBEntity>> entry : res_SearchRangeChangeMaps.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "前后 n 秒数据差值范围外集合 size is " + entry.getValue().size());
            iteratorResult(entry);//遍历打印数据
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetRangeUpTimeSpan  获取在指定范围数值上的点持续时间戳
     * 测试结果：
     * 参数 2022-02-01T12:00:00 --2022-02-05T12:00:00 max=3  min=1 number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * 结果"result": {
     * "BF_IN[36]": 865,
     * "BF_IN[35]": 867,
     * "BF_IN[0]": 0,
     * "BF_IN[33]": 846
     * },
     * 参数 2022-02-01T12:00:00 --2022-02-02T12:00:00 max=8  min=3 number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * 结果	"result": {
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 231,
     * "BF_IN[0]": 0,
     * "BF_IN[33]": 22
     * },
     * every获取间隔这个参数好像没有用到
     */
    @Test
    public void GetRangeUpTimeSpanTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetRangeUpTimeSpan  获取在指定范围数值上的点持续时间戳-------------------------");
        var res_GetRangeUpTimeSpan = influx.GetRangeUpTimeSpan(start, end, list, every, number, min, max);
        for (Map.Entry<String, Long> entry : res_GetRangeUpTimeSpan.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "指定范围数值上的点持续时间戳 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetTimeBeforeValue  获取某个时间点前一个值
     * 测试结果：
     * 示例参数：2022-03-01T08:00:00   searchDay=28
     * 数据正确，都是28天内最后一个数据，即2022-02-01T08:00:00--2022-03-01T08:00:00这一时间段最后一个的值
     * 也就是endtime这个时间点前一个值
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 6,
     * "BF_IN[0]": 1,
     * "BF_IN[33]": 1
     * 其他参数测试也与数据库查询结果一致
     */
    @Test
    public void GetTimeBeforeValueTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetTimeBeforeValue  获取某个时间点前一个值-------------------------");
        var res_GetTimeBeforeValue = influx.GetTimeBeforeValue(start, list, number, searchDay);
        for (Map.Entry<String, Double> entry : res_GetTimeBeforeValue.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "获取某个时间点前一个值 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetValuesMax  点一段时间内最大值
     * GetValuesMin  点一段时间内最小值
     * 测试如果：
     * 参数 2022-02-01T08:00:00--2022-03-01T08:00:00 number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * max结果 "result": {
     * "BF_IN[36]": 5,
     * "BF_IN[35]": 6,
     * "BF_IN[0]": 1,
     * "BF_IN[33]": 6
     * },
     * min结果"result": {
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 0,
     * "BF_IN[33]": 0
     * },
     * --------------------------------------------------------------
     * 参数 2021-02-01T08:00:00----2022-03-01T08:00:00 number=1 list={"BF_IN[0]","BF_IN[33]", "BF_IN[35]", "BF_IN[36]"}
     * max结果 	"result": {
     * "BF_IN[36]": 5,
     * "BF_IN[35]": 6,
     * "BF_IN[0]": 1,
     * "BF_IN[33]": 7
     * min结果"result": {
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 0,
     * "BF_IN[33]": 0
     * },
     * },
     * 测试结果与数据库一致,没问题
     */
    @Test
    public void GetValuesMaxAndMinTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetValuesMax  点一段时间内最大值-------------------------");
        var res_GetValuesMax = influx.GetValuesMax(start, end, list, number);
        for (Map.Entry<String, Double> entry : res_GetValuesMax.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "点一段时间内最大值 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetValuesMin  点一段时间内最小值-------------------------");
        var res_GetValuesMin = influx.GetValuesMin(start, end, list, number);
        for (Map.Entry<String, Double> entry : res_GetValuesMin.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "点一段时间内最小值 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetStatusTagCount  计算DI点从0变化成1次数
     * 测试结果：
     * 默认和七天内最后一个数据对比，GetStatusTagCount()调用GetTimeBeforeValue方法，参数searchDay值位7
     * 参数：2022-02-01T12:00:00 --2022-03-01T12:00:00
     * 结果	"result": {
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 185424,
     * "BF_IN[33]": 0
     * },
     * 参数：2022-01-01T12:00:00 --2022-02-01T12:00:00
     * "result": {
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 581410,
     * "BF_IN[33]": 0
     * },
     * 参数：2022-01-01T12:00:00 --2022-03-01T12:00:00
     * "result": {
     * "BF_IN[36]": 0,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 766834,
     * "BF_IN[33]": 0
     * },
     * 测试结果与数据库一致
     */
    @Test
    public void GetStatusTagCountTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetStatusTagCount  计算DI点从0变化成1次数-------------------------");
        var res_GetStatusTagCount = influx.GetStatusTagCount(start, end, list, number);
        for (Map.Entry<String, Integer> entry : res_GetStatusTagCount.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "计算DI点从0变化成1次数 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetStatusTagTimeSpan  计算DI点区间内的时间
     * 测试结果：
     * 参数 2022-02-01T12:00:00 --2022-03-01T12:00:00
     * 结果 "result": {
     * "BF_IN[36]": 427855,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 2846596,
     * "BF_IN[33]": 39572
     * },
     * 参数 2022-02-01T12:00:00 --2022-02-05T12:00:00
     * 结果 	"result": {
     * "BF_IN[36]": 214178,
     * "BF_IN[35]": 0,
     * "BF_IN[0]": 518409,
     * "BF_IN[33]": 5812
     * 测试结果与数据库查询一致
     * },
     */
    @Test
    public void GetStatusTagTimeSpanTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetStatusTagTimeSpan  计算DI点区间内的时间-------------------------");
        var res_GetStatusTagTimeSpan = influx.GetStatusTagTimeSpan(start, end, list, number);
        for (Map.Entry<String, Long> entry : res_GetStatusTagTimeSpan.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "DI点区间内的时间 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * GetDayAverage  获取日平均值
     * 测试结果：
     * 如果起止时间超过一天，比如参数2022.02.01-2022.03.01这短时间
     * 结果得到最后一天endtime的平均值
     * key is BF_IN[36]	日平均值 is 1.0
     * key is BF_IN[35]	日平均值 is 6.0
     * key is BF_IN[0]	日平均值 is 0.5
     * key is BF_IN[33]	日平均值 is 2.24
     */
    @Test
    public void GetDayAverageTest() {
        System.out.println("-----------------------开始测试-------------------------");
        System.out.println("-----------------------GetDayAverage  获取日平均值-------------------------");
        var res_GetDayAverage = influx.GetDayAverage(start, end, list, number);
        for (Map.Entry<String, Double> entry : res_GetDayAverage.entrySet()) {
            System.out.println("key is " + entry.getKey() + '\t' + "日平均值 is " + entry.getValue());
        }
        System.out.println("-----------------------结束测试-------------------------" + '\n');

    }

    /**
     * 遍历Map.Entry<String, List<InfluxDBEntity>>,输出打印InfluxDBEntity详细信息
     */
    public void iteratorResult(Map.Entry<String, List<InfluxDBEntity>> entry) {
        List<InfluxDBEntity> results = new ArrayList<>();
        results = entry.getValue();
        if (!results.isEmpty()) {//如果不为空，就遍历
            Iterator<InfluxDBEntity> itr = results.iterator();// Iterator声明对象，list.iterator返回Iterator对象
            while (itr.hasNext()) {// 判断是否有下一个数据
                InfluxDBEntity influxDBEntity = new InfluxDBEntity();
                influxDBEntity = itr.next();
                System.out.println("[field=" + influxDBEntity.getField() + "--" + "value=" + influxDBEntity.getValue() + "--" + "time=" + influxDBEntity.getTime() + "]");// 遍历输出
            }
        }
    }


}
