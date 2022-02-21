package org.jeecg.modules.smartfuel.test.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.var;
import org.jeecg.common.api.vo.Result;

import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.smartfuel.util.InfluxDBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试Influx
 * @Author: jeecg-boot
 * @Date:   2022-02-18
 * @Version: V1.0
 */
@Api(tags="测试Influx")
@RestController
@RequestMapping("/test/influxdbTest")
@Slf4j
public class InfluxDBTestController {
	 @Autowired
	 private InfluxDBUtil influx;

	 /**
	  *读取 InfluxDb 线性插补数据
	  * @param list
	  * @param every
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	@AutoLog(value = "测试-读取 InfluxDb 线性插补数据")
	@ApiOperation(value="测试-读取 InfluxDb 线性插补数据", notes="测试-读取 InfluxDb 线性插补数据")
	@GetMapping(value = "/QueryInterval")
	public Result<?> QueryInterval(@RequestParam List<String> list,String every,String number,String startTime,String endTime) {
		LocalDateTime start = LocalDateTime.parse(startTime);
		LocalDateTime end = LocalDateTime.parse(endTime);
		var res = influx.QueryInterval(start,end,list,every,number);
		return Result.OK(res);
	}

	 /**
	  *获取差值(使用influx自带的increase)
	  * @param list
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 @AutoLog(value = "测试-获取差值(使用influx自带的increase)")
	 @ApiOperation(value="测试-获取差值(使用influx自带的increase)", notes="测试-获取差值(使用influx自带的increase)")
	 @GetMapping(value = "/GetIncrease")
	 public Result<?> GetIncrease(@RequestParam List<String> list,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetIncrease(start,end,list,number);
		 return Result.OK(res);
	 }

	 /**
	  *通过DI量获取时长
	  * @param list
	  * @param every
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 @AutoLog(value = "测试-通过DI量获取时长")
	 @ApiOperation(value="测试-通过DI量获取时长", notes="测试-通过DI量获取时长")
	 @GetMapping(value = "/SearchDI")
	 public Result<?> SearchDI(@RequestParam List<String> list,String every,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.SearchDI(start,end,list,every,number);
		 return Result.OK(res);
	 }

	 /**
	  *计算点数据重置时的值的差
	  * @param list
	  * @param every
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 @AutoLog(value = "测试-计算点数据重置时的值的差")
	 @ApiOperation(value="测试-计算点数据重置时的值的差", notes="测试-计算点数据重置时的值的差")
	 @GetMapping(value = "/SearchTotal")
	 public Result<?> SearchTotal(@RequestParam List<String> list,String every,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.SearchTotal(start,end,list,every,number);
		 return Result.OK(res);
	 }

	 /**
	  *正常查询
	  * @param list
	  * @param every
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 @AutoLog(value = "测试-正常查询")
	 @ApiOperation(value="测试-正常查询", notes="测试-正常查询")
	 @GetMapping(value = "/Search")
	 public Result<?> Search(@RequestParam List<String> list,String every,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.Search(start,end,list,every,number);
		 return Result.OK(res);
	 }

	 /**
	  *获取历史数据集合
	  * @param list
	  * @param every
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 @AutoLog(value = "测试-获取历史数据集合")
	 @ApiOperation(value="测试-获取历史数据集合", notes="测试-获取历史数据集合")
	 @GetMapping(value = "/SearchBatchMaps")
	 public Result<?> SearchBatchMaps(@RequestParam List<String> list,String every,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.SearchBatchMaps(start,end,list,every,number);
		 return Result.OK(res);
	 }

	 /**
	  *获取时间区间类临界值
	  * @param list
	  * @param number
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 @AutoLog(value = "测试-获取时间区间类临界值")
	 @ApiOperation(value="测试-获取时间区间类临界值", notes="测试-获取时间区间类临界值")
	 @GetMapping(value = "/GetRangeTagMarginValue")
	 public Result<?> GetRangeTagMarginValue(@RequestParam List<String> list,String every,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetRangeTagMarginValue(start,end,list,every,number);
		 return Result.OK(res);
	 }

	 /**
	  * 前后 n 秒数据差值范围外集合
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param intervalSeconds 间隔时间
	  * @param number 数据源编号
	  * @param max 最大值
	  * @param min 最小值
	  * @return
	  */
	 @AutoLog(value = "测试-前后 n 秒数据差值范围外集合")
	 @ApiOperation(value="测试-前后 n 秒数据差值范围外集合", notes="测试-前后 n 秒数据差值范围外集合")
	 @GetMapping(value = "/SearchRangeChangeMaps")
	 public Result<?> SearchRangeChangeMaps(@RequestParam List<String> list,String intervalSeconds,String number,String startTime,String endTime,double max,double min) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.SearchRangeChangeMaps(start,end,list,number,intervalSeconds,max,min);
		 return Result.OK(res);
	 }

	 /**
	  * 获取在指定范围数值上的点持续时间戳
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param every 获取间隔
	  * @param number 数据源编号
	  * @param flagMinValue 起始经验值
	  * @param flagMaxValue 结束经验值
	  * @return
	  */
	 @AutoLog(value = "测试-获取在指定范围数值上的点持续时间戳")
	 @ApiOperation(value="测试-获取在指定范围数值上的点持续时间戳", notes="测试-获取在指定范围数值上的点持续时间戳")
	 @GetMapping(value = "/GetRangeUpTimeSpan")
	 public Result<?> GetRangeUpTimeSpan(@RequestParam List<String> list,String every,String number,String startTime,String endTime,double flagMinValue,double flagMaxValue) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetRangeUpTimeSpan(start,end,list,every,number,flagMinValue,flagMaxValue);
		 return Result.OK(res);
	 }

	 /**
	  * 获取某个时间点前一个值
	  * @param startTime 开始时间
	  * @param list 点表
	  * @param number 数据源编号
	  * @param searchDay 验推天数
	  * @return
	  */
	 @AutoLog(value = "测试-获取某个时间点前一个值")
	 @ApiOperation(value="测试-获取某个时间点前一个值", notes="测试-获取某个时间点前一个值")
	 @GetMapping(value = "/GetTimeBeforeValue")
	 public Result<?> GetTimeBeforeValue(@RequestParam List<String> list,String number,String startTime,Integer searchDay) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 var res = influx.GetTimeBeforeValue(start,list,number,searchDay);
		 return Result.OK(res);
	 }

	 /**
	  * 点一段时间内最大值
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param number 数据源编号
	  * @return
	  */
	 @AutoLog(value = "测试-点一段时间内最大值")
	 @ApiOperation(value="测试-点一段时间内最大值", notes="测试-点一段时间内最大值")
	 @GetMapping(value = "/GetValuesMax")
	 public Result<?> GetValuesMax(@RequestParam List<String> list,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetValuesMax(start,end,list,number);
		 return Result.OK(res);
	 }

	 /**
	  * 点一段时间内最小值
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param number 数据源编号
	  * @return
	  */
	 @AutoLog(value = "测试-点一段时间内最小值")
	 @ApiOperation(value="测试-点一段时间内最小值", notes="测试-点一段时间内最小值")
	 @GetMapping(value = "/GetValuesMin")
	 public Result<?> GetValuesMin(@RequestParam List<String> list,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetValuesMin(start,end,list,number);
		 return Result.OK(res);
	 }

	 /**
	  * 计算DI点从0变化成1次数
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param number 数据源编号
	  * @return
	  */
	 @AutoLog(value = "测试-计算DI点从0变化成1次数")
	 @ApiOperation(value="测试-计算DI点从0变化成1次数", notes="测试-计算DI点从0变化成1次数")
	 @GetMapping(value = "/GetStatusTagCount")
	 public Result<?> GetStatusTagCount(@RequestParam List<String> list,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetStatusTagCount(start,end,list,number);
		 return Result.OK(res);
	 }

	 /**
	  * 计算DI点区间内的时间
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param number 数据源编号
	  * @return
	  */
	 @AutoLog(value = "测试-计算DI点区间内的时间")
	 @ApiOperation(value="测试-计算DI点区间内的时间", notes="测试-计算DI点区间内的时间")
	 @GetMapping(value = "/GetStatusTagTimeSpan")
	 public Result<?> GetStatusTagTimeSpan(@RequestParam List<String> list,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetStatusTagTimeSpan(start,end,list,number);
		 return Result.OK(res);
	 }

	 /**
	  * 获取日平均值
	  * @param startTime 开始时间
	  * @param endTime 结束时间
	  * @param list 点表
	  * @param number 数据源编号
	  * @return
	  */
	 @AutoLog(value = "测试-获取日平均值")
	 @ApiOperation(value="测试-获取日平均值", notes="测试-获取日平均值")
	 @GetMapping(value = "/GetDayAverage")
	 public Result<?> GetDayAverage(@RequestParam List<String> list,String number,String startTime,String endTime) {
		 LocalDateTime start = LocalDateTime.parse(startTime);
		 LocalDateTime end = LocalDateTime.parse(endTime);
		 var res = influx.GetDayAverage(start,end,list,number);
		 return Result.OK(res);
	 }


}
